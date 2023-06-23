package bs

import scalafx.application.JFXApp3.PrimaryStage
import scalafx.geometry.HPos.Center
import scalafx.scene.Scene
import scalafx.scene.layout.{ColumnConstraints, GridPane, RowConstraints}
import scalafx.stage.Stage
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox


class BoardVisual(private val board: Board, private val primaryStage: PrimaryStage) {
  private val grid = new GridPane()
  private val mainBox = new VBox(grid)
  private val scene = new Scene(mainBox, 1000, 750)
  private val stage = new Stage()
  drawHeaders()
  grid.gridLinesVisible = true

  primaryStage.scene = scene
//  stage.show()



  private def drawHeaders(): Unit = {
    grid.columnConstraints.add(ColumnConstraints(50))
    grid.rowConstraints.add(RowConstraints(50))

    for (i <- 1 to 10) {
      val xLabel = Label(i.toString)
      val yLabel = Label(Board.itoc(i-1).toString)
      grid.add(xLabel, i, 0)
      grid.add(yLabel, 0, i)
      GridPane.setHalignment(xLabel, Center)
      GridPane.setHalignment(yLabel, Center)
      grid.rowConstraints.add(RowConstraints(50))
      grid.columnConstraints.add(ColumnConstraints(50))

    }
  }
}
