package bs

sealed abstract class ShotResult(val hit: Boolean, val label: String)
object ShotResult {
  case object MISS extends ShotResult(false, "Miss!")
  case object HIT extends ShotResult(true, "Hit!")
  case object HIT_AND_SINK extends ShotResult(true, "Hit and Sink!")
}
