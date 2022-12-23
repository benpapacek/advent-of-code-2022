import java.util.*

object Day17 {

    private val input = FileReader().readFile("input-day17.txt")

    private const val WIDTH = 9
    private const val HEIGHT = 4000

    fun part1() {
        var rockCount = 0
        var nextInstructionIndex = 0
        var nextPieceIndex = 0
        val staticGrid = ("|.......|".repeat(HEIGHT - 1) + "---------").toCharArray().toMutableList()
        val dynamicGrid = MutableList(HEIGHT * WIDTH) { '.' }
        addPiece(dynamicGrid, PIECES[nextPieceIndex++], getStartY(staticGrid))

        val instructionQueue = input.trim().toCharArray()

        while(rockCount < 2022) {
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
                rockCount++
                Collections.rotate(dynamicGrid, -WIDTH)
                merge(dynamicGrid, staticGrid)
                val startY = getStartY(staticGrid)
                if (startY < 0) {
                    throw IllegalStateException("Not enough height")
                }
                addPiece(dynamicGrid, PIECES[nextPieceIndex++ % PIECES.size], startY)
            }
        }

//        debug(dynamicGrid, staticGrid)
        val ans = HEIGHT - getHighestOccupiedRow(staticGrid) - 1 // subtract one for floor
        println("day 17 part 1: $ans")
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