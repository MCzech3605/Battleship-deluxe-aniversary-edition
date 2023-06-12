package bs

import bs.Ship.maxFieldsNum

import scala.collection.mutable

class Ship {
  val fields: mutable.Set[Field] = collection.mutable.Set()
  var shotsTaken = 0

  def addField(field: Field) = {
    if(size() >= maxFieldsNum) throw new Exception("Ship is already too big")
    else if(size() == 0) fields.add(field)
    else {
      var flag = false
      fields.foreach(f => if(f.isWallNeighbor(field)) flag = true)
      if(flag)
        fields.add(field)
      else
        throw new Exception("Cannot add field " + field + " to the ship - not a neighbor of any of ship's part")
    }
  }

  def size(): Int = {
    fields.size
  }

  def shot(): Boolean = {
    shotsTaken += 1
    shotsTaken == size()
  }
}

object Ship {
  val maxFieldsNum = 4
}
