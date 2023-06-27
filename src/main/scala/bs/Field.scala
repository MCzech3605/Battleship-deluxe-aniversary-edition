package bs

class Field (val r: Int, val c: Char){
  if(c < 'A' || c > 'J') throw new Exception("Column out of range")
  if(r < 1 || r > 10) throw new Exception("Row out of range")
  var wasShot = false
  var ship : Option[Ship] = None


  def isWallNeighbor(other: Field): Boolean = {
    if(this.c == other.c)
      other.r == this.r + 1 || other.r == this.r - 1
    else if(this.c == other.c + 1 || this.c == other.c - 1)
      this.r == other.r
    else
      false
  }

  def isDiagonalNeighbor(other: Field): Boolean = {
    (other.c == this.c + 1 || other.c == this.c - 1) && (this.r == other.r + 1 || this.r == other.r -1)
  }

  def isAnyNeighbor(other: Field): Boolean = {
    isWallNeighbor(other) || isDiagonalNeighbor(other)
  }

  private def canEqual(other: Any) : Boolean = {
    other.isInstanceOf[Field]
  }

  override def equals(other : Any): Boolean ={
    other match {
      case otherField: Field => this.canEqual(other) && this.c == otherField.c && this.r == otherField.r
      case _ => false
    }
  }

  override def hashCode(): Int = {
    var code = 1
    val prime = 31
    code = prime + r
    code = prime*code + c
    code
  }

  override def toString: String = {
    s"$c$r"
  }

  def addShip(ship: Ship): Unit = {
    if(this.ship.isDefined)
      throw new IllegalArgumentException("Field already contains a ship")
    else{
      ship.addField(this)
      this.ship = Some(ship)
    }
  }

  def removeShip(): Unit = {
    if(ship.isEmpty) throw new IllegalArgumentException("Cannot remove a ship, field already unoccupied")
    ship.get.removeField(this)
    this.ship = None
  }

  def shoot(): ShotResult = {
    if(wasShot)
      throw new IllegalArgumentException("Cannot shoot the same field twice")
    wasShot = true
    if(ship.isDefined)
      ship.get.shoot()
    else
      ShotResult.MISS
  }

  def hasShip: Boolean = ship.isDefined
}
