package com.itrsa.costeroids.logic.events

import com.itrsa.costeroids.logic.events.KeyEvent

sealed trait GameEvent

case class NewPlayerEvent(id:String) extends GameEvent;

case class PlayerEvent(playerEvent: KeyEvent) extends GameEvent;

case class PlayerDeathEvent(playerId:String) extends GameEvent;

case class TickEvent(elapsedTime: Double) extends GameEvent;

case class WinEvent(playerId:String) extends GameEvent;

case class HitEvent(playerId:String, bulletId:String) extends GameEvent;

trait KeyEvent(val playerId:String) extends GameEvent;

case class ThrustEvent(override val playerId:String) extends KeyEvent(playerId);

case class KeyLeftEvent(override val playerId:String) extends KeyEvent(playerId);

case class KeyRightEvent(override val playerId:String) extends  KeyEvent(playerId);

case class KeyBrakeEvent(override val playerId:String) extends KeyEvent(playerId);

case class FireEvent(override val playerId:String) extends KeyEvent(playerId);


