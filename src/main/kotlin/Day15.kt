import kotlin.math.abs

object Day15 {

    private val input = FileReader().readFile("input-day15.txt")

    private data class Point(val x: Int, val y: Int)

    private class Sensor(val loc: Point, val beaconLoc: Point) {
        private val radius: Int = abs(loc.x - beaconLoc.x) + abs(loc.y - beaconLoc.y)
        val xMin = loc.x - radius
        val xMax = loc.x + radius

        fun covers(point: Point): Boolean {
            val distanceToPoint = abs(loc.x - point.x) + abs(loc.y - point.y)
            return distanceToPoint <= radius
        }
    }

    fun part1() {
        val sensors = input.split("\n").filterNot { it.isBlank() }.map { line ->
            val match = Regex("Sensor at x=(.*), y=(.*): closest beacon is at x=(.*), y=(.*)").matchEntire(line)!!
            val sensorX = match.groupValues[1].toInt()
            val sensorY = match.groupValues[2].toInt()
            val beaconX = match.groupValues[3].toInt()
            val beaconY = match.groupValues[4].toInt()
            Sensor(
                loc = Point(sensorX, sensorY),
                beaconLoc = Point(beaconX, beaconY)
            )
        }

        val xMin = sensors.map { it.xMin }.min()
        val xMax = sensors.map { it.xMax }.max()

        val y = 2_000_000
        val ans = (xMin..xMax).count { x ->
            val pt = Point(x, y)
            sensors.any { it.covers(pt) } && sensors.none { it.beaconLoc == pt }
        }
        println("day 15 part 1: $ans")
    }
}

private val TEST_INPUT = """
    Sensor at x=2, y=18: closest beacon is at x=-2, y=15
    Sensor at x=9, y=16: closest beacon is at x=10, y=16
    Sensor at x=13, y=2: closest beacon is at x=15, y=3
    Sensor at x=12, y=14: closest beacon is at x=10, y=16
    Sensor at x=10, y=20: closest beacon is at x=10, y=16
    Sensor at x=14, y=17: closest beacon is at x=10, y=16
    Sensor at x=8, y=7: closest beacon is at x=2, y=10
    Sensor at x=2, y=0: closest beacon is at x=2, y=10
    Sensor at x=0, y=11: closest beacon is at x=2, y=10
    Sensor at x=20, y=14: closest beacon is at x=25, y=17
    Sensor at x=17, y=20: closest beacon is at x=21, y=22
    Sensor at x=16, y=7: closest beacon is at x=15, y=3
    Sensor at x=14, y=3: closest beacon is at x=15, y=3
    Sensor at x=20, y=1: closest beacon is at x=15, y=3
""".trimIndent()