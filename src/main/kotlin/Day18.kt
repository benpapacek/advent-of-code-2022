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

        val ans = countUnboundedSides(grid, bounds, 0)

        println("day 17 part 1: $ans")
    }

    fun part2() {
        val points = getPoints()
        val bounds = getBounds(points)
        val grid = createGrid(points, bounds)

        val tag = 2
        tagExternal(grid, tag, 0, 0, 0)
        val ans = countUnboundedSides(grid, bounds, tag)

        println("day 17 part 2: $ans")
    }

    private fun tagExternal(grid: Array<Array<Array<Int>>>, tag: Int, x: Int, y: Int, z: Int) {
        if (grid[x][y][z] != 0) return
        if (grid[x][y][z] == 0) {
            grid[x][y][z] = 2
            if (x > 0) tagExternal(grid, tag, x - 1, y, z)
            if (y > 0) tagExternal(grid, tag, x, y - 1, z)
            if (z > 0) tagExternal(grid, tag, x, y, z - 1)
            if (x < grid.size - 1) tagExternal(grid, tag, x + 1, y, z)
            if (y < grid[x].size - 1) tagExternal(grid, tag, x, y + 1, z)
            if (z < grid[x][y].size - 1) tagExternal(grid, tag, x, y, z + 1)
        }
    }

    private fun countUnboundedSides(grid: Array<Array<Array<Int>>>,
                                    bounds: Bounds,
                                    target: Int): Int {
        val (xMin, xMax, yMin, yMax, zMin, zMax) = bounds
        var count = 0
        (xMin..xMax).forEach { x ->
            (yMin..yMax).forEach { y ->
                (zMin..zMax).forEach { z ->
                    if (grid[x][y][z] == 1) {
                        if (x == 0) count++
                        else if (grid[x - 1][y][z] == target) count++

                        if (y == 0) count++
                        else if (grid[x][y - 1][z] == target) count++

                        if (z == 0) count++
                        else if (grid[x][y][z - 1] == target) count++

                        if (grid[x + 1][y][z] == target) count++
                        if (grid[x][y + 1][z] == target) count++
                        if (grid[x][y][z + 1] == target) count++
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