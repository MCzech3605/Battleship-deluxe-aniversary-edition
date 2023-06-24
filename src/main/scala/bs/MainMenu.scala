package bs

import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.geometry.Pos.Center
import scalafx.scene.layout.VBox
import scalafx.scene.text.{Font, Text}

object MainMenu {
  private val primaryStage = Application.stage
  var player1Interface: Option[PlayerInterface] = None

  private val title = new Text:
    text = "Battleship"
    font = Font(50)

  private val startButton = new Button:
    text = "Start"
    prefHeight = 50
    prefWidth = 100
    onAction = event => player1Interface.get.load()

  val mainBox: VBox = new VBox:
    spacing = 30
    alignment = Center
    children = Seq(title, startButton)

  val scene: Scene = new Scene(1000, 750):
    root = mainBox
}
