package bs

import bs.Board.*

import scala.collection.mutable

class Board {
  val fields: Array[Array[Field]] = Array.ofDim[Field](height, width)
  for(i <- 0 until height; j <- 0 until width) fields(i)(j) = new Field(i+1, itoc(j))
  val ships: mutable.Set[Ship] = mutable.Set()

  def addShip(r: Int, c: Int): Unit = {
    var cornerShips = mutable.Set[Ship]()
    for(nbhInd <- getCornerNbhIndexes(r, c)){
      if(fields(nbhInd.head)(nbhInd(1)).hasShip)
        cornerShips.add(fields(nbhInd.head)(nbhInd(1)).ship.get)
    }
    if(cornerShips.size > 1)
     throw new IllegalArgumentException("Cannot place ship in the corner of the other ship")
    var field: Option[Field] = None
    for(nbhInd <- getWallNbhIndexes(r, c)){
      if(fields(nbhInd.head)(nbhInd(1)).hasShip){
        if(field.isDefined) throw new IllegalArgumentException("Cannot merge 2 ships")
        else field = Some(fields(nbhInd.head)(nbhInd(1)))
      }
    }
    if(field.isDefined) {
      if(cornerShips.nonEmpty && field.get.ship.get != cornerShips.head)
        throw new IllegalArgumentException("Cannot place ship in the corner of the other ship")
      fields(r)(c).addShip(field.get.ship.get)
    } else {
      if(cornerShips.nonEmpty)
        throw new IllegalArgumentException("Cannot place ship in the corner of the other ship")
      fields(r)(c).addShip(new Ship())
      ships.add(fields(r)(c).ship.get)
    }
  }

  def removeShip(r: Int, c: Char): Unit = fields(r-1)(ctoi(c)).removeShip()

  def getShipNumBySize: mutable.HashMap[Int, Int] = {
    val sizes: mutable.HashMap[Int, Int] = mutable.HashMap()
    for(i <- 1 to 4){
      sizes.addOne((i, 0))
    }
    for(ship <- ships){
      sizes(ship.size()) += 1
    }
    sizes
  }

  def shot(r: Int, c: Char): Unit = fields(r-1)(ctoi(c)).shot()

  override def toString: String = {
    var str: String = "   "
    var colName = 'A'
    var rowInd = 1
    for(_ <- 1 to width){
      str += colName
      str += ' '
      colName = (colName.toInt + 1).toChar
    }
    str += '\n'
    for(row <- fields){
      str += rowInd
      if(rowInd < 10) str += ' '
      rowInd += 1
      str += '|'
      for(field <- row){
        (field.hasShip, field.wasShot) match {
          case (true, true) => str += 'X'
          case (true, false) => str += '#'
          case (false, true) => str += '*'
          case (false, false) => str += ' '
        }
        str += '|'
      }
      str += '\n'
    }
    str
  }

  def toStringHidden: String = {
    var str: String = ""
    for (row <- fields) {
      str += '|'
      for (field <- row) {
        (field.hasShip, field.wasShot) match {
          case (true, true) => str += 'X'
          case (false, true) => str += '*'
          case _ => str += ' '
        }
        str += '|'
      }
      str += '\n'
    }
    str
  }
}

object Board {
  val height = 10
  val width = 10
  val legend = "| | - empty field\n|#| - ship\n|*| - shot empty field\n|X| - shot ship"
  def ctoi(a: Char): Int = a.toInt - 65
  def itoc(i: Int): Char = (i+65).toChar
  def getWallNbhIndexes(rInd: Int, cInd: Int): mutable.Set[Seq[Int]] = {
    val nbh = mutable.Set[Seq[Int]]()
    for(i <- Seq(-1, 1)){
      if(rInd + i < height && rInd + i >= 0) nbh.add(Seq(rInd+i, cInd))
      if(cInd + i < width && cInd + i >= 0) nbh.add(Seq(rInd, cInd+i))
    }
//    println(rInd)
//    println(cInd)
//    println(nbh)
    nbh
  }
  def getCornerNbhIndexes(rInd: Int, cInd: Int): mutable.Set[Seq[Int]] = {
    val nbh = mutable.Set[Seq[Int]]()
    for (i <- Seq(-1, 1)) {
      if (rInd + i < height && rInd + i >= 0 && cInd + i < width && cInd + i >= 0) nbh.add(Seq(rInd + i, cInd + i))
      if (rInd + i < height && rInd + i >= 0 && cInd - i < width && cInd - i >= 0) nbh.add(Seq(rInd + i, cInd - i))
    }
    nbh
  }


}
