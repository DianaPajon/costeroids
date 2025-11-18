package com.itrsa.costeroids.logic.events

sealed trait KeyEvent(val playerId:String)


case class ThrustEvent(override val playerId:String) extends KeyEvent(playerId:String);

case class KeyLeftEvent(override val playerId:String) extends KeyEvent(playerId:String);

case class KeyRightEvent(override val playerId:String) extends  KeyEvent(playerId:String);

case class KeyBrakeEvent(override val playerId:String) extends KeyEvent(playerId:String);

case class FireEvent(override val playerId:String) extends KeyEvent(playerId:String);









