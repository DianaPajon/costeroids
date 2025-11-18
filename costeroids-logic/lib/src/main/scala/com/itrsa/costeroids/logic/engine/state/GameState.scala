package com.itrsa.costeroids.logic.engine.state

import com.itrsa.costeroids.logic.engine.state.player.PlayerState
import com.itrsa.costeroids.logic.engine.state.world.{Bullet, Coordinate, Ship}

import scala.collection.mutable

class GameState {
  var players = new mutable.HashMap[String, PlayerState]();
  var ships = new mutable.HashMap[String, Ship]();
  var bullets = new mutable.HashMap[String, Bullet]()
  val ratio = 100F;

  def addPlayer(id:String): Unit = {
    if(players.size <= 8) {
      players.addOne(id, PlayerState(id));
      ships.addOne(id, Ship(nextCoordinate, math.Pi - math.Pi / 8 *(players.size-1), id))
    }
  }
  
  

  private def nextCoordinate = {
    val jugadores = players.size -1;
    
    val xCoord = math.sin(math.Pi * 2 / 8 * jugadores.toFloat) ;
    val yCoord = math.cos(math.Pi * 2 / 8 * jugadores.toFloat) ;
    
    Coordinate(1152/2, 648/2).plus(Coordinate(xCoord, yCoord).mult(ratio))
  }
  
}
