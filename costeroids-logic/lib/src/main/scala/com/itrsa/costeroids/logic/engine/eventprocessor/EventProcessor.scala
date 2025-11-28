package com.itrsa.costeroids.logic.engine.eventprocessor

import com.itrsa.costeroids.logic.engine.state.GameState
import com.itrsa.costeroids.logic.engine.state.player.PlayerState
import com.itrsa.costeroids.logic.engine.state.world.{Bullet, Coordinate}
import com.itrsa.costeroids.logic.events.{FireEvent, GameEvent, HitEvent, KeyBrakeEvent, KeyEvent, KeyLeftEvent, KeyRightEvent, NewPlayerEvent, PlayerDeathEvent, PlayerEvent, ThrustEvent, TickEvent, WinEvent}

import java.util.UUID
import scala.collection.mutable.{ArrayBuffer, ListBuffer};

class EventProcessor {

  def processEvent(e: GameEvent, s:GameState):GameState =
    e match {
      case NewPlayerEvent (id:String) => addPlayerEvent(id, s);
      case PlayerEvent(ke:KeyEvent) => playerEvent(ke, s);
      case PlayerDeathEvent(id:String) => deathEvent(id, s);
      case TickEvent(elapsedTime:Double) => tickEvent(s, elapsedTime);
      case WinEvent(id:String) => winEvent(id, s);
      case HitEvent(id:String, bulletId:String) => hitEvent(id, bulletId,  s);
      case _ => s
    }

  def addPlayerEvent(id:String, s:GameState) = {
    s.addPlayer(id);
    s
  }

  def deathEvent(id: String, s:GameState) = {
    s.ships = s.ships.filter((shipid, ship) => id != shipid )
    s.players(id).death = true
    s
  }
  def tickEvent(gameState : GameState, elapsedTime:Double) = {
    val playerIds = gameState.players.filter((id, player) => !player.death).keySet;
    //run actions and clear frame state
    playerIds.foreach(
      (id:String) => {
        val player = gameState.players(id)
        if(player.fireEvents > 0){
          gameState.bullets.put(UUID.randomUUID().toString, getBullet(player, gameState))
        }
        var ship = gameState.ships(id)
        ship.speed = ship.speed.plus(getSpeed(player, ship.rotation))
        ship.rotation = getRotation(player, gameState.ships(id).rotation)
        gameState.ships.put(id, ship)
        gameState.players.put(id, new PlayerState(id, 10))
      }
    )



    //move everything
    gameState.ships = gameState.ships.map(
      (id, ship) => {
        ship.position = ship.position.plus(ship.speed.mult(elapsedTime*10000))
        if(ship.position.x < 0)
          ship.position = Coordinate(ship.position.x + 1152, ship.position.y)
        if (ship.position.x > 1152)
          ship.position = Coordinate(ship.position.x - 1152, ship.position.y)
        if (ship.position.y < 0)
          ship.position = Coordinate(ship.position.x, ship.position.y + 648)
        if (ship.position.y > 648)
          ship.position = Coordinate(ship.position.x, ship.position.y - 648)
        (id, ship)
      }
    )
    val toRemove = gameState.ships.filter((id, ship) =>
      ship.position.x > 1152 || ship.position.y > 648 || ship.position.x < 0 || ship.position.y < 0
    ).keySet
    gameState.ships = gameState.ships.filter((key, ship) => !toRemove.contains(key))

    gameState.bullets = gameState.bullets.map(
      (id, bullet) => {
        bullet.position = bullet.position.plus(bullet.speed.mult(elapsedTime))
        (id, bullet)
      }
    )
    val bulletsToRemove = gameState.bullets.filter((id, bullet) =>
      bullet.position.x > 1152 || bullet.position.y > 648 || bullet.position.x < 0 || bullet.position.y < 0
    ).keySet
    gameState.bullets = gameState.bullets.filter((key,bulet) => !bulletsToRemove.contains(key))




    //check for deaths
    val deathEvents = gameState.bullets.map(
      (id, bullet) => {
        val hit = gameState.ships.filter((id, ship) =>
        {
          bullet.playerId != ship.playerId && ship.collides(bullet) && ship.hp <= 1
        }).map((k,v) => k).headOption
        hit.map(present => PlayerDeathEvent(present))
      }
    ).filter(
      option => {
        option.isDefined
      }
    )

    //process deaths
    var finalState = deathEvents.foldLeft(gameState)(
      (state, event) => {
       this.processEvent(event.get, state);
      }
    )


    //check for collisions
    val hitEvents = gameState.bullets.map(
      (id, bullet) => {
        val hit = gameState.ships.filter((id, ship) =>
        {
          bullet.playerId != ship.playerId && ship.collides(bullet) && ship.hp > 1
        }).map((k,v) => k).headOption
        hit.map(present => HitEvent(present, id))
      }
    ).filter(
      option => {
        option.isDefined
      }
    )
    finalState = hitEvents.foldLeft(gameState)(
      (state, event) => {
        this.processEvent(event.get, state);
      }
    )


    finalState
  }


  def playerEvent(ke:KeyEvent, s:GameState) = {
    val  player = s.players(ke.playerId)
    ke match {
      case ThrustEvent(id:String) => {
        player.thrustPresses=player.thrustPresses+1
      } ;
      case KeyBrakeEvent(id:String) => player.brakePresses=player.brakePresses+1;
      case KeyLeftEvent(id:String) => player.leftPresses= player.leftPresses+1;
      case KeyRightEvent(id:String) => player.rightPresses=player.rightPresses+1;
      case FireEvent(id:String) => player.fireEvents=player.fireEvents+1;
    }
    s.players.put(ke.playerId, player)
    s
  };

  def winEvent(id: String, state: GameState): GameState = {
    state.players.map(
      (k, v) => if (k == id) {
        v.win = true; v
      } else {
        v.win = false; v.death = true; v
      }
    )
    state
  }


  def hitEvent(id: String, bulletId: String, state: GameState): GameState = {
    state.players.get(id).head.hp = state.players.get(id).head.hp - 1
    state.ships.get(id).head.hp = state.ships.get(id).head.hp - 1
    state.bullets.remove(bulletId)
    state
  }


  private def getSpeed(player: PlayerState, rotation:Double) =
    Coordinate(math.cos(math.Pi/2-rotation), math.sin(math.Pi/2-rotation)).mult(player.thrustPresses - player.brakePresses).mult(0.0001)

  private def getRotation(player: PlayerState, rotation:Double) :Double = {
    var suma = rotation +  (2 * math.Pi / 350) * player.rightPresses - (2 * math.Pi / 350) * player.leftPresses;
    while(suma > 2*math.Pi) suma = suma-2*math.Pi
    while(suma < 0) suma = suma+2*math.Pi
    suma
  }

  private def getBullet(player: PlayerState, gameState: GameState): Bullet = {
    val rotation = gameState.ships(player.playerId).rotation;
    val xCoord = math.sin(rotation);
    val yCoord = math.cos(rotation);
    val speed = Coordinate(xCoord, yCoord).mult(BULLET_SPEED)
    val position = gameState.ships(player.playerId).position;
    return Bullet(position, speed, math.Pi/2-rotation, player.playerId);
  }


  val BULLET_SPEED = 300;

}
