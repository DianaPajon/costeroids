package com.itrsa.costeroids.logic.engine

import com.itrsa.costeroids.logic.dto.input.KeyDTO
import com.itrsa.costeroids.logic.dto.output.{BulletDTO, CoordinateDTO, ShipDTO, StateDTO}
import com.itrsa.costeroids.logic.engine.eventprocessor.EventProcessor
import com.itrsa.costeroids.logic.engine.state.GameState
import com.itrsa.costeroids.logic.engine.state.world.Coordinate
import com.itrsa.costeroids.logic.events.{NewPlayerEvent, PlayerEvent, TickEvent}

import scala.jdk.CollectionConverters._
import java.util.UUID;

class GameEngine {

  var gameState = new GameState();
  var eventProcessor = new EventProcessor();

  def addPlayer() = {
    val id = UUID.randomUUID().toString
    gameState = eventProcessor.processEvent(NewPlayerEvent(id), gameState)
    id
  }

  //OneInstruction()
  def processEvents(eventList:java.util.List[KeyDTO]) = {
      gameState = eventList.asScala.foldRight(gameState)(
        (keyDto, gs) => eventProcessor.processEvent(PlayerEvent(keyDto.getEvent), gs)
      )
  }

  //interrupt->oneTick()
  def tick(elapsedTime: Double) = this.gameState = eventProcessor.processEvent(TickEvent(elapsedTime), this.gameState)


  def poll() : StateDTO = {
    val ships = this.gameState.ships.map((key, ship) => ShipDTO(toDTO(ship.position), ship.rotation, key, ship.hp)).toList.asJava
    val bullets = this.gameState.bullets.map((key, bullet) => BulletDTO(toDTO(bullet.position), bullet.rotation, key, bullet.playerId)).toList.asJava
    StateDTO(ships, bullets, getDeaths(), getWinner())
  }

  def getDeaths()  : java.util.List[String]  = {
    this.gameState.players.filter((id, state) => {
      state.death
    }).map((id, state) => id).toList.asJava
  }

  def getWinner() : String   = {
    this.gameState.players.filter((id, state) => state.win).map((id, state) => id).headOption.getOrElse("")
  }

  private def toDTO(coordinate: Coordinate) = CoordinateDTO(coordinate.x, 648 - coordinate.y);
}
