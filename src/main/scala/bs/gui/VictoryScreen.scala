package bs.gui

import bs.Application
import scalafx.Includes.*
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.Platform
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.geometry.Pos.{Center, TopCenter}
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.{GridPane, HBox, VBox}
import scalafx.scene.text.{Font, Text}

object VictoryScreen {
  private val primaryStage = Application.stage

  var player1Interface: Option[PlayerInterface] = None
  var player2Interface: Option[PlayerInterface] = None

  private var victoriousPlayer: Option[Int] = None

  private val victoryText = new Text:
    font = Font(50)

  private val player1Box = new VBox:
    spacing = 20
    alignment = TopCenter
    children = new Text:
      text = "Player 1 Board"
      font = new Font(25)

  private val player2Box = new VBox:
    spacing = 20
    alignment = TopCenter
    children = new Text:
      text = "Player 2 Board"
      font = new Font(25)

  private val boardBox = new HBox:
    spacing = 30
    alignment = Center
    children = Seq(player1Box, player2Box)

  private val quitButton = new Button:
    text = "Quit"
    prefHeight = 50
    prefWidth = 100
    alignment = Center
    onAction = event => Platform.exit()

  val mainBox: VBox = new VBox:
    spacing = 45
    alignment = TopCenter
    padding = Insets(50, 0, 0, 0)
    children = Seq(victoryText, boardBox, quitButton)

  val scene: Scene = new Scene(mainBox, 1000, 750)

  def load(): Unit = {
    if player1Interface.get.isVictorious then victoriousPlayer = Some(1)
    else victoriousPlayer = Some(2)
    player1Box.children.add(player1Interface.get.playerGrid)
    player2Box.children.add(player2Interface.get.playerGrid)

    victoryText.text = s"Player ${victoriousPlayer.get} Victory!"

    primaryStage.scene = scene
  }
}
