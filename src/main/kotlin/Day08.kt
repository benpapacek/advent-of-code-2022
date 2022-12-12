object Day08 {

    private val input = FileReader().readFile("input-day08.txt")

    private data class Tree(val i: Int, val j: Int, val height: Int,
                    var isVisible: Boolean = false, var scenicScore: ScenicScore? = null)
    private data class ScenicScore(val north: Int, val east: Int, val south: Int, val west: Int, val score: Int)

    private fun createGrid() = input.split("\n").filterNot { it.isBlank() }.mapIndexed { i, line ->
        line.toCharArray().mapIndexed { j, c -> Tree(i, j, c.toString().toInt()) }
    }

    private fun assignScenicScore(grid: List<List<Tree>>, i: Int, j: Int) {
        val tree = grid[i][j]
        var (north, south, east, west) = IntArray(4) { 1 }

        if (j == grid[i].indices.last) east = 0
        else while(j + east < grid[i].indices.last && grid[i][j + east].height < tree.height) east++

        if (j == 0) west = 0
        else while(j - west > 0 && grid[i][j - west].height < tree.height) west++

        if (i == 0) north = 0
        else while(i - north > 0 && grid[i - north][j].height < tree.height) north++

        if (j == grid.indices.last) south = 0
        else while(i + south < grid.indices.last && grid[i + south][j].height < tree.height) south++

        tree.scenicScore = ScenicScore(north, east, south, west, north * east * south * west)
    }

    fun part2() {
        val grid = createGrid()
        (grid.indices).forEach { i ->
            (grid[i].indices).forEach { j ->
                assignScenicScore(grid, i, j)
            }
        }
        val optimalTree = grid.flatten().maxBy { it.scenicScore!!.score }
        println("day 08 part 2: ${optimalTree.scenicScore!!.score}")
    }

    fun part1() {
        val grid = createGrid()

        (grid.indices).forEach { i ->
            var (northMax, southMax, eastMax, westMax) = IntArray(4) { -1 }
            (grid[i].indices).forEach { j ->
                val westTree = grid[i][j]
                if (westTree.height > westMax) {
                    westMax = westTree.height
                    westTree.isVisible = true
                }
                val northTree = grid[j][i]
                if (northTree.height > northMax) {
                    northMax = northTree.height
                    northTree.isVisible = true
                }
                val eastTree = grid[i][grid[i].size - 1 - j]
                if (eastTree.height > eastMax) {
                    eastMax = eastTree.height
                    eastTree.isVisible = true
                }
                val southTree = grid[grid[i].size - 1 - j][i]
                if (southTree.height > southMax) {
                    southMax = southTree.height
                    southTree.isVisible = true
                }
            }
        }

        val ans = grid.sumOf { line -> line.count { it.isVisible } }
        println("day 08 part 1: $ans")
    }

}