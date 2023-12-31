package bs.gui

import bs.Application
import scalafx.Includes.*
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.{Font, Text}

object PlayerSwitchPanel {
  private val primaryStage = Application.stage

  var player1Interface: Option[PlayerInterface] = None
  var player2Interface: Option[PlayerInterface] = None

  private var currPlayer = 2

  private val startTurnButton = new Button:
    text = "Player 2 - Start Turn"
    font = new Font(15)
    prefHeight = 100
    prefWidth = 200
    alignment = Center
    onAction = loadPlayerInterface


  private val mainBox = new VBox:
    spacing = 30
    padding = Insets(100, 0, 100, 0)
    alignment = Center
    children = startTurnButton

  private val scene = new Scene(mainBox, 1000, 750)

  def load(): Unit = {
    primaryStage.scene = scene
  }

  private def loadPlayerInterface(event: ActionEvent): Unit = {
    if currPlayer == 1 then {
      player1Interface.get.load()
      currPlayer = 2
    }
    else {
      player2Interface.get.load()
      currPlayer = 1
    }
    startTurnButton.text = s"Player $currPlayer - Start Turn"
  }
}
