package com.itrsa.costeroids.logic.engine.state.world

class Coordinate(val x:Double, val y:Double) {
  def mult(n:Double) = Coordinate(this.x*n, this.y*n);
  def plus(other:Coordinate) = Coordinate(other.x + this.x , other.y + this.y);
}

