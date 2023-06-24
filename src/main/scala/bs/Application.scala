package bs

object Application {
  def main(args: Array[String]): Unit = {
    // random stuff to begin
    val board1 = new Board()
    val board2 = new Board()
//    board1.addShip(2, 'C')
//    board1.addShip(2, 'D')
//    board1.addShip(3, 'D')
    board1.shot(2, 'B')
    board1.shot(2, 'D')
    println(Board.legend)
    print(board1)

    //    print("\u001b")   // - supposed to clear console (doesn't work for me)

  }
}
