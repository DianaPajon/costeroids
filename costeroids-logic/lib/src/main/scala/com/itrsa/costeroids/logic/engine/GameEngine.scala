package com.itrsa.costeroids.logic.engine

import com.itrsa.costeroids.logic.dto.input.{EventDTO, EventType}
import com.itrsa.costeroids.logic.dto.output.{BulletDTO, CoordinateDTO, ShipDTO, StateDTO}
import com.itrsa.costeroids.logic.engine.eventprocessor.EventProcessor
import com.itrsa.costeroids.logic.engine.state.GameState
import com.itrsa.costeroids.logic.engine.state.world.Coordinate
import com.itrsa.costeroids.logic.events.{NewPlayerEvent, PlayerEvent, TickEvent}

import scala.jdk.CollectionConverters.*
import java.util.UUID;

class GameEngine {

  var gameState = new GameState();
  var eventProcessor = new EventProcessor();

  //OneInstruction()
  def processEvents(eventList:java.util.List[EventDTO]) : Unit = {
      gameState = eventList.asScala.foldLeft(gameState)(
        (gs, eventDTO) => eventProcessor.processEvent(eventDTO.getEvent, gs)
      )
  }

  //interrupt->oneTick()
  def tick(elapsedTime: Double) = this.gameState = eventProcessor.processEvent(TickEvent(elapsedTime), this.gameState)


  def poll() : StateDTO = {
    val ships = this.gameState.ships.map((key, ship) => ShipDTO(toDTO(ship.position), ship.rotation, key, ship.hp)).toList.asJava
    val bullets = this.gameState.bullets.map((key, bullet) => BulletDTO(toDTO(bullet.position), bullet.rotation, key, bullet.playerId)).toList.asJava
    StateDTO(ships, bullets, getDeaths, getWinner)
  }
  
  private def getDeaths  : java.util.List[String]  = {
    this.gameState.players.filter((id, state) => {
      state.death
    }).map((id, state) => id).toList.asJava
  }

  private def getWinner : String   = {
    this.gameState.players.filter((id, state) => state.win).map((id, state) => id).headOption.getOrElse("")
  }

  private def toDTO(coordinate: Coordinate) = CoordinateDTO(coordinate.x, 648 - coordinate.y);
}
