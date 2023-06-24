package bs

import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.geometry.Pos.Center
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.*
import scalafx.scene.paint.Color.*
import scalafx.scene.text.{Text, Font}
import scalafx.scene.text.TextIncludes.jfxFont2sfxFont

import scala.language.implicitConversions

object MainMenu extends JFXApp3 {
  override def start(): Unit = {

    stage = new JFXApp3.PrimaryStage()

    val title = new Text:
      text = "Battleship"
      font = Font(50)

    val startButton = new Button:
      text = "Start"
      prefHeight = 50
      prefWidth = 100
      onAction = event => new PlayerInterface(new Board(), new Board(), stage)

    val mainBox: VBox = new VBox:
      spacing = 30
      alignment = Center
      children = Seq(title, startButton)

    val myScene = new Scene(1000, 750):
      root = mainBox

    stage.scene = myScene
  }
}
