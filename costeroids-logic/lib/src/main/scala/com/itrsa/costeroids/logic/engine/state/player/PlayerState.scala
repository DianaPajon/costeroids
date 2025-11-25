package com.itrsa.costeroids.logic.engine.state.player

class PlayerState(val playerId:String, var hp:Integer) {
  var thrustPresses = 0;
  var brakePresses = 0;
  var leftPresses = 0;
  var rightPresses = 0;
  var fireEvents = 0;
  var win = false;
  var death = false;
}
