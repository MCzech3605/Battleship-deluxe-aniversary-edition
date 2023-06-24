package bs

import scalafx.Includes.*
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.geometry.Pos.Center
import scalafx.geometry.{HPos, VPos}
import scalafx.scene.control.{Button, Label}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.{Node, Scene}
import scalafx.scene.text.{Text, Font}
import scalafx.stage.Stage


class PlayerInterface(private val playerBoard: Board, private val opponentBoard: Board, private val primaryStage: PrimaryStage) {
  private var placementPhase: Boolean = true

  private val playerGrid = new GridPane:
    gridLinesVisible = true
    alignment = Center
    onMouseClicked = clickPlayerBoard

  private val playerBox = new VBox:
    spacing = 30
    alignment = Center
    children = Seq(
      new Text {
        text = "Player Board"
        font = new Font(30)
      },
      playerGrid
    )

  private val playerGridRectangles: Array[Array[Rectangle]] = Array.ofDim[Rectangle](10, 10)

  private val opponentGrid = new GridPane:
    gridLinesVisible = true
    alignment = Center
    onMouseClicked = clickOpponentBoard

  private val opponentBox = new VBox:
    spacing = 30
    alignment = Center
    children = Seq(
      new Text {
        text = "Opponent Board"
        font = new Font(30)
      },
      opponentGrid
    )

  private val opponentGridRectangles: Array[Array[Rectangle]] = Array.ofDim[Rectangle](10, 10)

  private val nextTurnButton = new Button:
    text = "Next Turn"
    prefHeight = 50
    prefWidth = 100
    onAction = event => println(event)

  private val forfeitButton = new Button:
    text = "Forfeit"
    prefHeight = 50
    prefWidth = 100
    onAction = event => println(event)

  private val buttonsBox = new VBox:
    spacing = 30
    alignment = Center
    children = Seq(nextTurnButton, forfeitButton)

  private val mainBox = new HBox:
    spacing = 30
    alignment = Center
    children = Seq(playerBox, buttonsBox, opponentBox)
  private val scene = new Scene(mainBox, 1000, 750)
  drawGridHeaders(playerGrid)
  fillGridCells(playerGrid, playerGridRectangles)
  drawGridHeaders(opponentGrid)
  fillGridCells(opponentGrid, opponentGridRectangles)
  primaryStage.scene = scene

  private def drawGridHeaders(grid: GridPane): Unit = {
    grid.columnConstraints.add(ColumnConstraints(PlayerInterface.cellWidth))
    grid.rowConstraints.add(RowConstraints(PlayerInterface.cellHeight))

    for (i <- 1 to 10) {
      val xLabel = Label(i.toString)
      val yLabel = Label(Board.itoc(i-1).toString)
      grid.add(xLabel, i, 0)
      grid.add(yLabel, 0, i)
      GridPane.setHalignment(xLabel, HPos.Center)
      GridPane.setHalignment(yLabel, HPos.Center)
      grid.columnConstraints.add(ColumnConstraints(PlayerInterface.cellWidth))
      grid.rowConstraints.add(RowConstraints(PlayerInterface.cellHeight))

    }
  }

  private def fillGridCells(grid: GridPane, gridRectangles: Array[Array[Rectangle]]): Unit = {
    for (x <- 1 to 10; y <- 1 to 10) {
      val rect = Rectangle(PlayerInterface.cellWidth - 2, PlayerInterface.cellHeight - 2, Color(0, 0, 0, 0))
      grid.add(rect, x, y)
      gridRectangles(y - 1)(x - 1) = rect
      GridPane.setHalignment(rect, HPos.Center)
      GridPane.setValignment(rect, VPos.Center)
    }
  }

  private def clickPlayerBoard(event: MouseEvent): Unit = {
    if (!placementPhase) {return}
    println("Placing")
    val selectedNode: Node = event.pickResult.intersectedNode match
      case None => return
      case Some(node) => node

//    println(selectedNode.getClass)
//    if (!selectedNode.isInstanceOf[Rectangle]) {return}

    val columnIndex = GridPane.getColumnIndex(selectedNode)
    val rowIndex = GridPane.getRowIndex(selectedNode)

    if (columnIndex == null || rowIndex == null) {return}

    if (columnIndex == -1) {return }

    try {
      playerBoard.addShip(rowIndex - 1, columnIndex - 1)
      playerGridRectangles(rowIndex - 1)(columnIndex - 1).fill = Color(0, 0, 1, 1)
    } catch {
      case e: IllegalArgumentException => println(e)
    }

    println(columnIndex)
    println(rowIndex)
  }

  private def clickOpponentBoard(event: MouseEvent): Unit = {
    if placementPhase then return

  }

}
object PlayerInterface {
  private val cellWidth = 35
  private val cellHeight = 35
}
