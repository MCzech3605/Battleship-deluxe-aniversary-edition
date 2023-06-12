package bs

class Field (val c: Char, val r: Int){
  if(c < 'A' || c > 'J') throw new Exception("Column out of range")
  if(r < 1 || r > 10) throw new Exception("Row out of range")
  var wasShot = false
  var ship : Option[Ship] = None

  def isWallNeighbor(other: Field): Boolean = {
    other.c match {
      case this.c => other.r == this.r + 1 || other.r == this.r - 1
      case this.c - 1 | this.c + 1 => other.r == this.r
      case _ => false
    }
  }

  def isDiagonalNeighbor(other: Field): Boolean = {
    other.c match {
      case this.c - 1 | this.c + 1 => other.r == this.r + 1 || other.r == this.r -1
      case _ => false
    }
  }

  def isAnyNeighbor(other: Field): Boolean = {
    isWallNeighbor(other) || isDiagonalNeighbor(other)
  }

  private def canEqual(other: Any) : Boolean = {
    other.isInstanceOf[Field]
  }

  override def equals(other : Any): Boolean ={
    other match {
      case otherField: Field => (this.canEqual(other) && this.c == otherField.c && this.r == otherField.r)
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
    c + r.toString
  }

  def addShip(ship: Ship): Unit = {
    if(this.ship.isDefined)
      throw new Exception("Field already contains a ship")
    else this.ship = Some(ship)
  }

  def removeShip(): Ship = {
    if(ship.isEmpty) throw new Exception("Cannot remove a ship, field already unoccupied")
    var tmp = this.ship.get
    this.ship = None
    tmp
  }

  def shot(): Boolean = {
    if(wasShot)
      throw new Exception("Cannot shoot same field twice")
    wasShot = true
    if(ship.isDefined)
      ship.get.shot()
    else
      false
  }

  def hasShip(): Boolean = ship.isDefined
}
