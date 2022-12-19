import kotlin.math.abs

object Day15 {

    private val input = FileReader().readFile("input-day15.txt")

    private data class Point(val x: Int, val y: Int)

    private data class Square(val x0: Int, val y0: Int, val x1: Int, val y1: Int)

    private class Sensor(val loc: Point, val beaconLoc: Point) {
        private val radius: Int = abs(loc.x - beaconLoc.x) + abs(loc.y - beaconLoc.y)
        val xMin = loc.x - radius
        val xMax = loc.x + radius

        fun covers(x: Int, y: Int): Boolean {
            val distanceToPoint = abs(loc.x - x) + abs(loc.y - y)
            return distanceToPoint <= radius
        }

        fun covers(s: Square): Boolean {
            return covers(s.x0, s.y0) && covers(s.x0, s.y1) &&
                    covers(s.x1, s.y0) && covers(s.x1, s.y1)
        }
    }

    fun part1() {
        val sensors = getSensors()

        val xMin = sensors.map { it.xMin }.min()
        val xMax = sensors.map { it.xMax }.max()

        val y = 2_000_000
        val ans = (xMin..xMax).count { x ->
            sensors.any { it.covers(x, y) } && sensors.none { it.beaconLoc.x == x && it.beaconLoc.y == y }
        }
        println("day 15 part 1: $ans")
    }

    fun part2() {
        val sensors = getSensors()
        var searchSquares = listOf(Square(0, 0, 4_000_000, 4_000_000))
        (1..6).forEach { i ->
            searchSquares = searchSquares.filter { square ->
                sensors.none { it.covers(square) }
            }
            searchSquares = searchSquares.map { subdivide(it) }.flatten()
        }
        var targetPoint: Point? = null
        var i = 0
        while (i < searchSquares.size && targetPoint == null) {
            val s = searchSquares[i++]
            (s.x0..s.x1).forEach { x ->
                (s.y0..s.y1).forEach { y ->
                    if (sensors.none { it.covers(x, y) }) {
                        targetPoint = Point(x, y)
                    }
                }
            }
        }

        val ans = with(targetPoint!!) { x * 4_000_000L + y }
        println("day 15 part 2: $ans")
    }

    private fun subdivide(square: Square): List<Square> {
        val divisor = 10
        val width = abs(square.x1 - square.x0)
        val subWidth = width / divisor
        return (0 until divisor).map { x ->
            (0 until divisor).map { y ->
                Square(square.x0 + (x * subWidth), square.y0 + (y * subWidth),
                    square.x0 + (x+1) * subWidth, square.y0 + (y+1) * subWidth)
            }
        }.flatten()
    }

    private fun getSensors(): List<Sensor> {
        return input.split("\n").filterNot { it.isBlank() }.map { line ->
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