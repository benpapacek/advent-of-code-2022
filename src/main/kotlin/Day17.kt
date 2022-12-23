import java.util.*

object Day17 {

    private val input = FileReader().readFile("input-day17.txt")

    private const val WIDTH = 9
    private const val HEIGHT = 100

    fun part1() {
        val ans = runSimulation(2022)
        println("day 17 part 1: $ans")
    }

    fun part2() {
        val ans = runSimulation(1000000000000)
        println("day 17 part 2: $ans")
    }

    private fun runSimulation(n: Long): Long {
//        var patternLastRockCount = 0L
//        var patternLastRowCount = 0L
        var rowCount = 0L
        var rockCount = 0L
        var nextInstructionIndex = 0
        var nextPieceIndex = 0
        val staticGrid = ("|.......|".repeat(HEIGHT - 1) + "---------").toCharArray().toMutableList()
        val dynamicGrid = MutableList(HEIGHT * WIDTH) { '.' }
        addPiece(dynamicGrid, PIECES[nextPieceIndex++], getStartY(staticGrid))

        val instructionQueue = input.trim().toCharArray()
        val pattern: Pattern = INPUT_PATTERN

        while (rockCount < n) {
            when (instructionQueue[nextInstructionIndex++ % instructionQueue.size]) {
                '<' -> {
                    Collections.rotate(dynamicGrid, -1)
                    if (!isValidPosition(dynamicGrid, staticGrid)) {
                        Collections.rotate(dynamicGrid, 1)
                    }
                }

                '>' -> {
                    Collections.rotate(dynamicGrid, 1)
                    if (!isValidPosition(dynamicGrid, staticGrid)) {
                        Collections.rotate(dynamicGrid, -1)
                    }
                }

                else -> throw IllegalStateException()
            }

            Collections.rotate(dynamicGrid, WIDTH)
            if (!isValidPosition(dynamicGrid, staticGrid)) {
                if (nextInstructionIndex % instructionQueue.size == 0) {
                    println(rockCount)
                }
                rockCount++
                Collections.rotate(dynamicGrid, -WIDTH)
                merge(dynamicGrid, staticGrid)

                val rowsRemoved = cleanGrid(staticGrid)
                // uncomment to find pattern marker
//                if (rowsRemoved > 0) println("rows removed: $rowsRemoved")
                // uncomment to find pattern rocks / rows
//                if (rowsRemoved == 46) {
//                    val deltaRockCount = rockCount - patternLastRockCount
//                    val deltaRowCount = rowCount - patternLastRowCount
//                    patternLastRowCount = rowCount
//                    patternLastRockCount = rockCount
//                    println("removed $deltaRowCount rows over $deltaRockCount rocks")
//                }

                if (rowsRemoved == pattern.marker) {
                    val skipAhead = (n - rockCount) / pattern.rocks
                    rowCount += skipAhead * pattern.rows
                    rockCount += skipAhead * pattern.rocks
                }
                rowCount += rowsRemoved
//                debug(dynamicGrid, staticGrid)
                val startY = getStartY(staticGrid)
                if (startY < 0) {
                    throw IllegalStateException("Not enough height")
                }
                addPiece(dynamicGrid, PIECES[nextPieceIndex++ % PIECES.size], startY)
            }
        }
        return (rowCount + (HEIGHT - getHighestOccupiedRow(staticGrid) - 1))
    }

    private fun cleanGrid(staticGrid: MutableList<Char>): Int {
        val highestFilled = (1..7).associateWith { i ->
            (0..HEIGHT).first { j -> staticGrid[j * WIDTH + i] != '.' }
        }
        val removableRows = HEIGHT - highestFilled.values.max() - 1

        Collections.rotate(staticGrid, removableRows * WIDTH)
        (0 until removableRows * WIDTH).forEach { i ->
            staticGrid[i] = if (i % WIDTH == 0 || i % WIDTH == 8) '|' else '.'
        }

        return removableRows
    }

    private fun merge(dynamicGrid: MutableList<Char>, staticGrid: MutableList<Char>) {
        dynamicGrid.indices.forEach { i ->
            if (dynamicGrid[i] != '.') {
                staticGrid[i] = '#'
                dynamicGrid[i] = '.'
            }
        }
    }

    private fun getHighestOccupiedRow(staticGrid: MutableList<Char>): Int {
        val first = staticGrid.indexOfFirst { it != '.' && it != '|' }
        return first / WIDTH
    }

    private fun getStartY(staticGrid: MutableList<Char>) = getHighestOccupiedRow(staticGrid) - 4 - 3

    private fun isValidPosition(dynamicGrid: List<Char>, staticGrid: List<Char>) =
        (staticGrid.indices).all { i ->
            dynamicGrid[i] == '.' || staticGrid[i] == '.'
        }

    private fun addPiece(dynamicGrid: MutableList<Char>, piece: CharArray, startY: Int) {
        val startIndex = startY * WIDTH
        (piece.indices).forEach { i ->
            dynamicGrid[startIndex + i] = piece[i]
        }
    }

    private fun debug(dynamicGrid: List<Char>, staticGrid: List<Char>, ) {
        val height = staticGrid.size / WIDTH
        (0 until height).forEach { i ->
            (0 until WIDTH).forEach { j ->
                val index = i * WIDTH + j
                if (dynamicGrid[index] != '.')
                    print(dynamicGrid[index])
                else
                    print(staticGrid[index])
            }
            println()
        }
        println()
    }
}

private val PIECES = arrayOf(
    ("........." + "........." + "........." + "...@@@@..").toCharArray(),
    ("........." + "....@...." + "...@@@..." + "....@....").toCharArray(),
    ("........." + ".....@..." + ".....@..." + "...@@@...").toCharArray(),
    ("...@....." + "...@....." + "...@....." + "...@.....").toCharArray(),
    ("........." + "........." + "...@@...." + "...@@....").toCharArray(),
)
    
private const val TEST_INPUT = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"

private data class Pattern(val marker: Int, val rows: Long, val rocks: Long)

private val TEST_INPUT_PATTERN = Pattern(marker = 27, rows = 53, rocks = 35)
private val INPUT_PATTERN = Pattern(marker = 46, rows = 2647, rocks = 1710)