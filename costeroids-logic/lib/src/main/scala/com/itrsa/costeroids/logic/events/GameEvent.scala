package com.itrsa.costeroids.logic.events

import com.itrsa.costeroids.logic.events.KeyEvent

sealed trait GameEvent()

case class NewPlayerEvent(id:String) extends GameEvent;

case class PlayerEvent(playerEvent: KeyEvent) extends GameEvent;

case class PlayerDeathEvent(playerId:String) extends GameEvent;

case class TickEvent(elapsedTime: Double) extends GameEvent;

case class WinEvent(playerId:String) extends GameEvent;


case class HitEvent(playerId:String, bulletId:String) extends GameEvent;