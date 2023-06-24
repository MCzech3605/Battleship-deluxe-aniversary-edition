package bs

import scalafx.Includes.*
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.geometry.Pos.{Center, TopCenter}
import scalafx.geometry.{HPos, Insets, VPos}
import scalafx.scene.control.{Button, Label}
import scalafx.scene.input.{MouseButton, MouseEvent}
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.{Node, Scene}
import scalafx.scene.text.{Font, Text}
import scalafx.stage.Stage


class PlayerInterface(private val playerBoard: Board, private val opponentBoard: Board) {
  private var placementPhase: Boolean = true
  private val primaryStage = Application.stage
  var playerSwitchPanel: Option[PlayerSwitchPanel] = None

  private val playerGrid = new GridPane:
    gridLinesVisible = true
    alignment = Center
    onMouseClicked = clickPlayerBoard

  private val playerShipBalance = new Text:
    text = "Ships of given size left to place:\n" + Board.requiredShipAmountsBySize
    font = new Font(15)

  private val playerBox = new VBox:
    spacing = 30
    alignment = TopCenter
    children = Seq(
      new Text {
        text = "Player Board"
        font = new Font(30)
      },
      playerGrid,
      playerShipBalance
    )

  private val playerGridRectangles: Array[Array[Rectangle]] = Array.ofDim[Rectangle](10, 10)

  private val opponentGrid = new GridPane:
    gridLinesVisible = true
    alignment = Center
    onMouseClicked = clickOpponentBoard

  private val opponentBox = new VBox:
    spacing = 30
    alignment = TopCenter
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
    disable = true
    onAction = nextTurn

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
    padding = Insets(100, 0, 100, 0)
    alignment = Center
    children = Seq(playerBox, buttonsBox, opponentBox)
  private val scene = new Scene(mainBox, 1000, 750)

  drawGridHeaders(playerGrid)
  fillGridCells(playerGrid, playerGridRectangles)
  drawGridHeaders(opponentGrid)
  fillGridCells(opponentGrid, opponentGridRectangles)
  
  def load(): Unit = {
    primaryStage.scene = scene
  }

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

  private def nextTurn(event: ActionEvent): Unit = {
    if placementPhase then {
      playerBox.children.remove(2)
      placementPhase = false
    }
    playerSwitchPanel.get.load()
  }

  private def clickPlayerBoard(event: MouseEvent): Unit = {
    if !placementPhase then return
    val selectedNode: Node = event.pickResult.intersectedNode match
      case None => return
      case Some(node) => node

    val columnIndex = GridPane.getColumnIndex(selectedNode)
    val rowIndex = GridPane.getRowIndex(selectedNode)

    if (columnIndex == null || rowIndex == null) {return}

    if event.button == MouseButton.Primary then placeShip(columnIndex, rowIndex)
    else if event.button == MouseButton.Secondary then removeShip(columnIndex, rowIndex)

    val shipNumBySize = playerBoard.getShipNumBySize
    nextTurnButton.disable = shipNumBySize != Board.requiredShipAmountsBySize

    val shipsLeftToPlaceBalance = Board.requiredShipAmountsBySize.map((k, v) => (k, v - shipNumBySize.getOrElse(k, 0)))

    playerShipBalance.text = "Ships of given size left to place:\n" + shipsLeftToPlaceBalance
  }

  private def placeShip(columnIndex: Int, rowIndex: Int): Unit = {
    try {
      playerBoard.addShip(rowIndex - 1, columnIndex - 1)
      playerGridRectangles(rowIndex - 1)(columnIndex - 1).fill = Color(0, 0, 1, 1)
    } catch {
      case e: IllegalArgumentException => println(e)
    }
  }

  private def removeShip(columnIndex: Int, rowIndex: Int): Unit = {
    try {
      playerBoard.removeShip(rowIndex - 1, columnIndex - 1)
      playerGridRectangles(rowIndex - 1)(columnIndex - 1).fill = Color(0, 0, 0, 0)
    } catch {
      case e: IllegalArgumentException => println(e)
    }
  }

  private def clickOpponentBoard(event: MouseEvent): Unit = {
    if placementPhase then return

  }

}
object PlayerInterface {
  private val cellWidth = 35
  private val cellHeight = 35
}
