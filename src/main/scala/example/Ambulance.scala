package example

import mpmens.Component
import mpmens.traits.map2d.{Position, PositionAware}

case class Ambulance(var position: Position) extends Component with PositionAware
