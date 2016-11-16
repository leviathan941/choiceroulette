package choiceroulette.gui

import scalafx.scene.Group
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color
import scalafx.scene.shape._
import scalafx.scene.text.Text
import scalafx.scene.transform.Rotate

/**
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ChoiceArc(radius: Double,
                angleStart: Double,
                angleLength: Double) extends StackPane {

  private val mArc = new Arc() {
    `type` = ArcType.Round
    radiusX = radius
    radiusY = radius
    startAngle = angleStart
    length = angleLength
    centerX = radius
    centerY = radius

    strokeLineCap = StrokeLineCap.Butt
    stroke = Color.Red
    strokeType = StrokeType.Inside
    fill = Color.Aqua

  }

  private val mBackRectangle = Rectangle(2 * radius, 2 * radius, Color.Transparent)

  private val mText = new Text("Choice") {
    stroke = Color.White
  }

  private def rotateText(text: Text): Unit = {
    text.transforms.add(new Rotate(-(angleStart + angleLength / 2), radius, radius))
  }

  rotateText(mText)

  children = List(new Group(mBackRectangle, mArc), mText)
  minHeight = 0
  minWidth = 0
  maxHeight = 2 * radius
  maxWidth = 2 * radius
}
