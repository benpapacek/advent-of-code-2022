object Day18 {

    private val input = FileReader().readFile("input-day18.txt")

    private data class Point(val x: Int, val y: Int, val z: Int)
    private data class Bounds(
        val xMin: Int, val xMax: Int,
        val yMin: Int, val yMax: Int,
        val zMin: Int, val zMax: Int
    )

    fun part1() {
        val points = getPoints()
        val bounds = getBounds(points)
        val grid = createGrid(points, bounds)

        val ans = countUnboundedSides(grid, bounds)

        println("day 17 part 1: $ans")
    }

    private fun countUnboundedSides(grid: Array<Array<Array<Int>>>, bounds: Bounds): Int {
        val (xMin, xMax, yMin, yMax, zMin, zMax) = bounds
        var count = 0
        (xMin..xMax).forEach { x ->
            (yMin..yMax).forEach { y ->
                (zMin..zMax).forEach { z ->
                    if (grid[x][y][z] == 1) {
                        if (x == 0) count++
                        else if (grid[x - 1][y][z] == 0) count++

                        if (y == 0) count++
                        else if (grid[x][y - 1][z] == 0) count++

                        if (z == 0) count++
                        else if (grid[x][y][z - 1] == 0) count++

                        if (grid[x + 1][y][z] == 0) count++
                        if (grid[x][y + 1][z] == 0) count++
                        if (grid[x][y][z + 1] == 0) count++
                    }
                }
            }
        }
        return count
    }

    private fun getPoints() = input.split("\n").filterNot { it.isBlank() }.map { line ->
        line.trim().split(",").map { it.toInt() }.let { Point(it[0], it[1], it[2]) }
    }

    private fun getBounds(points: List<Point>): Bounds {
        val (xMin, xMax) = points.minOf { it.x } to points.maxOf { it.x }
        val (yMin, yMax) = points.minOf { it.y } to points.maxOf { it.y }
        val (zMin, zMax) = points.minOf { it.z } to points.maxOf { it.z }

        if (xMin < 0 || yMin < 0 || zMin < 0) throw IllegalStateException()

        return Bounds(xMin, xMax, yMin, yMax, zMin, zMax)
    }

    private fun createGrid(points: List<Point>, bounds: Bounds): Array<Array<Array<Int>>> {
        // pad by one either side
        val grid = with (bounds) {
            Array(xMax + 2) { Array(yMax + 2) { Array(zMax + 2) { 0 } } }
        }

        points.forEach { pt ->
            grid[pt.x][pt.y][pt.z] = 1
        }
        return grid
    }



}

private val TEST_INPUT = """
    2,2,2
    1,2,2
    3,2,2
    2,1,2
    2,3,2
    2,2,1
    2,2,3
    2,2,4
    2,2,6
    1,2,5
    3,2,5
    2,1,5
    2,3,5
""".trimIndent()