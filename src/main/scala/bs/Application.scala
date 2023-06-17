package bs

object Application {
  def main(args: Array[String]): Unit = {
    // random stuff to begin
    val board1 = new Board()
    val board2 = new Board()
    board1.addShip(2, 'C')
    print(board1)

    //    print("\u001b")   // - supposed to clear console (doesn't work for me)

  }
}
