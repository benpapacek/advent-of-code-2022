object Day14 {

    private val input = FileReader().readFile("input-day14.txt")

    fun part1() {
        val paths = input.split("\n").filterNot { it.isBlank() }.map { path ->
            path.split("->").map { point ->
                point.split(",").map { it.trim().toInt() }
            }
        }
        val (xMin, xMax) = paths.flatten().map{ it[0] }.let {
            it.min() to it.max()
        }
        val yMin = 0
        val yMax = paths.flatten().map{ it[1] }.max()

        val grid = (yMin..yMax).map {
            (xMin..xMax).map {
                '.'
            }.toMutableList()
        }.toMutableList()

        paths.forEach { path ->
            val pointPairs = (0 until path.size - 1).map { i -> path[i] to path[i + 1] }
            pointPairs.forEach { (a, b) ->
                if (a[0] == b[0]) {
                    val x = a[0]
                    val (y0, y1) = listOf(a[1], b[1]).sorted()
                    (y0..y1).forEach { y -> grid[y][x - xMin] = '#' }
                } else if (a[1] == b[1]) {
                    val y = a[1]
                    val (x0, x1) = listOf(a[0], b[0]).sorted()
                    (x0..x1).forEach { x ->
                        grid[y][x - xMin] = '#'
                    }
                } else {
                    throw IllegalStateException()
                }
            }
        }
        grid[0][500 - xMin] = '+'

        var count = 0
        var isComplete = false
        while(!isComplete) {
            var falling = true
            var x = 500
            var y = 0
            while(falling) {
                if(y < grid.size - 1 && grid[y + 1][x - xMin] == '.') {
                    y++
                } else if (x > xMin && grid[y + 1][x - xMin - 1] == '.') {
                    x--; y++
                } else if (x < xMax && grid[y + 1][x - xMin + 1] == '.') {
                    x++; y++
                } else {
                    falling = false
                }
            }
            if (x <= xMin || x >= xMax || y >= yMax) {
                isComplete = true
            } else {
                count++
                grid[y][x - xMin] = 'o'
            }
        }
//        debug(grid)
        println("day 14 part 1: $count")
    }

    private fun debug(grid: List<List<Char>>) {
        grid.forEach { row ->
            row.forEach { c ->
                print(c)
            }
            println()
        }
    }
}

private val TEST_INPUT = """
    498,4 -> 498,6 -> 496,6
    503,4 -> 502,4 -> 502,9 -> 494,9
""".trimIndent()