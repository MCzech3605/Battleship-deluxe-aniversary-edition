package bs.gui

import bs.*
import scalafx.Includes.*
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.geometry.Pos.{Center, TopCenter}
import scalafx.geometry.{HPos, Insets, VPos}
import scalafx.scene.control.Alert.AlertType.Confirmation
import scalafx.scene.control.ButtonBar.ButtonData.CancelClose
import scalafx.scene.control.{Alert, Button, ButtonType, Label}
import scalafx.scene.input.{MouseButton, MouseEvent}
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.{Font, Text}
import scalafx.scene.{Node, Scene}
import scalafx.stage.Stage


class PlayerInterface(private val playerBoard: Board, private val opponentBoard: Board) {
  private var placementPhase: Boolean = true
  private var turnComplete: Boolean = false
  var isVictorious: Boolean = false
  private val primaryStage = Application.stage
  var opponentInterface: Option[PlayerInterface] = None

  val playerGrid = new GridPane:
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

  val playerGridRectangles: Array[Array[Rectangle]] = Array.ofDim[Rectangle](10, 10)

  private val opponentGrid = new GridPane:
    gridLinesVisible = true
    alignment = Center
    onMouseClicked = clickOpponentBoard

  private val shotResultMessage = new Text:
    font = new Font(25)

  private val opponentBox = new VBox:
    spacing = 30
    alignment = TopCenter
    children = Seq(
      new Text {
        text = "Opponent Board"
        font = new Font(30)
      },
      opponentGrid,
      shotResultMessage
    )

  private val opponentGridRectangles: Array[Array[Rectangle]] = Array.ofDim[Rectangle](10, 10)

  private val endTurnButton = new Button:
    text = "End Turn"
    prefHeight = 50
    prefWidth = 100
    margin = Insets(125, 0, 0, 0)
    disable = true
    onAction = endTurn

  private val forfeitButton = new Button:
    text = "Forfeit"
    prefHeight = 30
    prefWidth = 100
    margin = Insets(125, 0, 0, 0)
    onAction = event => {
      val alert = new Alert(Confirmation):
        title = "Forfeit"
        headerText = "Are you sure you want to forfeit the game?"
        contentText = "This will immediately end the game with your opponent's victory"

      val yesButton = new ButtonType("Yes")
      val noButton = new ButtonType("No", CancelClose)
      alert.buttonTypes.setAll(yesButton, noButton)

      if alert.showAndWait().get == yesButton then {
        opponentInterface.get.isVictorious = true
        endTurn(event)
      }
    }

  private val buttonsBox = new VBox:
    spacing = 30
    alignment = Center
    children = Seq(endTurnButton, forfeitButton)

  private val contentsBox = new HBox:
    spacing = 30
//    padding = Insets(100, 0, 100, 0)
    alignment = Center
    children = Seq(playerBox, buttonsBox, opponentBox)

  private val topText = new Text:
    text = "Placement Phase"
    font = new Font(40)

  private val mainBox = new VBox:
    spacing = 30
    padding = Insets(50, 0, 100, 0)
    alignment = TopCenter
    children = Seq(topText, contentsBox)


  private val scene = new Scene(mainBox, 1000, 750)

  drawGridHeaders(playerGrid)
  fillGridCells(playerGrid, playerGridRectangles)
  drawGridHeaders(opponentGrid)
  fillGridCells(opponentGrid, opponentGridRectangles)
  
  def load(): Unit = {
    turnComplete = false
    endTurnButton.disable = true
    shotResultMessage.text = ""
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

  private def endTurn(event: ActionEvent): Unit = {
    if placementPhase then {
      playerBox.children.remove(2)
      placementPhase = false
      topText.text = "Combat Phase"
    }
    if isVictorious || opponentInterface.get.isVictorious then {
      VictoryScreen.load()
    } else {
      PlayerSwitchPanel.load()
    }
  }

  private def selectNode(event: MouseEvent): Option[(Int, Int)] = {
    val selectedNode = event.pickResult.intersectedNode match
      case None => return None
      case Some(node) => node

    val rowIndex = GridPane.getRowIndex(selectedNode)
    val columnIndex = GridPane.getColumnIndex(selectedNode)

    if (columnIndex == null || rowIndex == null) {
      return None
    }

    Some((rowIndex, columnIndex))
  }

  private def clickPlayerBoard(event: MouseEvent): Unit = {
    if !placementPhase then return

    val (rowIndex, columnIndex) = selectNode(event) match
      case None => return
      case Some(result) => result

    if event.button == MouseButton.Primary then placeShip(rowIndex, columnIndex)
    else if event.button == MouseButton.Secondary then removeShip(rowIndex, columnIndex)

    val shipNumBySize = playerBoard.getShipNumBySize
    endTurnButton.disable = shipNumBySize != Board.requiredShipAmountsBySize

    val shipsLeftToPlaceBalance = Board.requiredShipAmountsBySize.map((k, v) => (k, v - shipNumBySize.getOrElse(k, 0)))

    playerShipBalance.text = "Ships of given size left to place:\n" + shipsLeftToPlaceBalance
  }

  private def placeShip(rowIndex: Int, columnIndex: Int): Unit = {
    try {
      playerBoard.addShip(rowIndex - 1, columnIndex - 1)
      playerGridRectangles(rowIndex - 1)(columnIndex - 1).fill = Color(0, 0, 1, 1)
    } catch {
      case e: IllegalArgumentException => println(e)
    }
  }

  private def removeShip(rowIndex: Int, columnIndex: Int): Unit = {
    try {
      playerBoard.removeShip(rowIndex - 1, columnIndex - 1)
      playerGridRectangles(rowIndex - 1)(columnIndex - 1).fill = Color(0, 0, 0, 0)
    } catch {
      case e: IllegalArgumentException => println(e)
    }
  }

  private def clickOpponentBoard(event: MouseEvent): Unit = {
    if placementPhase || turnComplete || isVictorious then return

    val (rowIndex, columnIndex) = selectNode(event) match
      case None => return
      case Some(result) => result

    if event.button != MouseButton.Primary then { return }

    try {
      val shotResult = opponentBoard.shoot(rowIndex - 1, columnIndex - 1)
      if shotResult.hit then {
        opponentGridRectangles(rowIndex - 1)(columnIndex - 1).fill = Color(1, 0, 0, 1)
        opponentInterface.get.playerGridRectangles(rowIndex - 1)(columnIndex - 1).fill = Color(1, 0, 0, 1)
        if opponentInterface.get.didLoseAllShips() then {
          isVictorious = true
          turnComplete = true
          forfeitButton.disable = true
          topText.text = "Victory!"
          endTurnButton.text = "Summary"
        }
      }
      else {
        opponentGridRectangles(rowIndex - 1)(columnIndex - 1).fill = Color(0.7, 0.7, 0.7, 1)
        opponentInterface.get.playerGridRectangles(rowIndex - 1)(columnIndex - 1).fill = Color(0.7, 0.7, 0.7, 1)
        turnComplete = true
      }

      shotResultMessage.text = shotResult.label

    } catch {
      case e: IllegalArgumentException => println(e)
    }
    endTurnButton.disable = !turnComplete
  }

  private def didLoseAllShips(): Boolean = {
    playerBoard.ships.forall(s => s.shotsTaken == s.size)
  }

}
object PlayerInterface {
  private val cellWidth = 35
  private val cellHeight = 35
}
