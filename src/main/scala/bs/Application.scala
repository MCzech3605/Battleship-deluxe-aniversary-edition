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

object Application extends JFXApp3 {
  
  
  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage()
    val player1Board = new Board()
    val player2Board = new Board()
    val player1Interface = new PlayerInterface(player1Board, player2Board)
    val player2Interface = new PlayerInterface(player2Board, player1Board)
    
    val playerSwitchPanel = new PlayerSwitchPanel((player1Interface, player2Interface))
    
    MainMenu.player1Interface = Some(player1Interface)
    player1Interface.playerSwitchPanel = Some(playerSwitchPanel)
    player2Interface.playerSwitchPanel = Some(playerSwitchPanel)
    
    stage.scene = MainMenu.scene
  }
}
