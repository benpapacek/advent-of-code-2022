object Day10 {

    private val input = FileReader().readFile("input-day10.txt")

    fun part1() {
        val queue = getCycles()
        val ans = listOf(20, 60, 100, 140, 180, 220).sumOf { cycle ->
            signalStrength(queue, cycle)
        }
        println("day 10 part 1: $ans")
    }

    fun part2() {
        val width = 40
        val height = 6
        var spritePos = 1
        val cycles = getCycles()
        println("day 10 part 2:")
        (0 until height).forEach { i ->
            (0 until width).forEach { j ->
                val index = i * width + j
                if (j >= spritePos - 1 && j <= spritePos + 1) {
                    print("#")
                } else {
                    print(" ")
                }
                spritePos += cycles[index]
            }
            println()
        }
    }

    private fun getCycles() = input.split("\n").filterNot { it.isBlank() }.map { command ->
        if (command.startsWith("addx")) {
            val n = command.split(Regex("\\s+"))[1].toInt()
            listOf(0, n)
        } else {
            listOf(0)
        }
    }.flatten()

    private fun signalStrength(instructions: List<Int>, n: Int) = n * (1 + instructions.subList(0, n-1).sum())

}

private val TEST_INPUT = """
    addx 15
    addx -11
    addx 6
    addx -3
    addx 5
    addx -1
    addx -8
    addx 13
    addx 4
    noop
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx -35
    addx 1
    addx 24
    addx -19
    addx 1
    addx 16
    addx -11
    noop
    noop
    addx 21
    addx -15
    noop
    noop
    addx -3
    addx 9
    addx 1
    addx -3
    addx 8
    addx 1
    addx 5
    noop
    noop
    noop
    noop
    noop
    addx -36
    noop
    addx 1
    addx 7
    noop
    noop
    noop
    addx 2
    addx 6
    noop
    noop
    noop
    noop
    noop
    addx 1
    noop
    noop
    addx 7
    addx 1
    noop
    addx -13
    addx 13
    addx 7
    noop
    addx 1
    addx -33
    noop
    noop
    noop
    addx 2
    noop
    noop
    noop
    addx 8
    noop
    addx -1
    addx 2
    addx 1
    noop
    addx 17
    addx -9
    addx 1
    addx 1
    addx -3
    addx 11
    noop
    noop
    addx 1
    noop
    addx 1
    noop
    noop
    addx -13
    addx -19
    addx 1
    addx 3
    addx 26
    addx -30
    addx 12
    addx -1
    addx 3
    addx 1
    noop
    noop
    noop
    addx -9
    addx 18
    addx 1
    addx 2
    noop
    noop
    addx 9
    noop
    noop
    noop
    addx -1
    addx 2
    addx -37
    addx 1
    addx 3
    noop
    addx 15
    addx -21
    addx 22
    addx -6
    addx 1
    noop
    addx 2
    addx 1
    noop
    addx -10
    noop
    noop
    addx 20
    addx 1
    addx 2
    addx 2
    addx -6
    addx -11
    noop
    noop
    noop
""".trimIndent()