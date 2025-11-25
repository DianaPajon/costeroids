package com.itrsa.costeroids.logic.engine.state.world

sealed trait WorldObject() {
  def collides (w:WorldObject):Boolean;
  var position:Coordinate;
}

case class Ship(var position:Coordinate, var rotation: Double, playerId:String, var hp:Integer) extends WorldObject(){

  override def collides(w: WorldObject): Boolean =
    math.abs(w.position.x - this.position.x) < 20 &&
    math.abs(w.position.y - this.position.y) < 20;


  enum ShipState():
    case ACTIVE
    case EXPLODING(time:Double)
    case DEAD

  var state:ShipState = ShipState.EXPLODING(0);

  var speed:Coordinate = Coordinate(0,0);

  def explodingTime =
    state match {
      case ShipState.ACTIVE => 0;
      case ShipState.EXPLODING(t) => t;
      case ShipState.DEAD => 0;
    }

}

case class Bullet(var position: Coordinate, var speed:Coordinate, var rotation:Double, var playerId:String) extends WorldObject(){

  override def collides(w: WorldObject): Boolean = w.collides(this);


}
