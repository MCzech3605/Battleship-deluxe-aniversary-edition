package bs

import scalafx.Includes.*
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.Text

class PlayerSwitchPanel(private val playerInterfaces: (PlayerInterface, PlayerInterface)) {
  private val primaryStage = Application.stage

  private var currPlayer = 2

  private val startTurnButton = new Button:
    text = "Player 2 - Start Turn"
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
      playerInterfaces._1.load()
      currPlayer = 2
    }
    else {
      playerInterfaces._2.load()
      currPlayer = 1
    }
    startTurnButton.text = s"Player $currPlayer - Start Turn"
  }
}
