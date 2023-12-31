package bs

import bs.Ship.maxFieldsNum

import scala.collection.mutable

class Ship {
  val fields: mutable.Set[Field] = collection.mutable.Set()
  var shotsTaken = 0

  def addField(field: Field): Unit = {
    if(fields.size >= maxFieldsNum) throw new IllegalArgumentException("Ship is already of maximum size")
    else if(fields.isEmpty) fields.add(field)
    else {
      var flag = false
      fields.foreach(f => if(f.isWallNeighbor(field)) flag = true)
      if(flag)
        fields.add(field)
      else
        throw new IllegalArgumentException("Cannot add field " + field + " to the ship - not a neighbor of any of ship's parts")
    }
  }

  def removeField(field: Field): Unit = {
    if !fields.contains(field) then throw new IllegalArgumentException("Cannot remove field " + field + " - not a part of the ship")
    val fieldNeighbors: Int = fields.count(f => f.isWallNeighbor(field))
    if fieldNeighbors > 1 then throw new IllegalArgumentException("Cannot remove field " + field + " - would split the ship")
    fields.remove(field)
  }
  
  def size: Int = fields.size

  def shoot(): ShotResult = {
    shotsTaken += 1
    if shotsTaken == size then ShotResult.HIT_AND_SINK
    else ShotResult.HIT
  }
}

object Ship {
  val maxFieldsNum: Int = Board.requiredShipAmountsBySize.keys.max
}
