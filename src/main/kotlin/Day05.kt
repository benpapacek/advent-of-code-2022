object Day05 {

    private val text = FileReader().readFile("input-day05.txt")

    fun part1() {
        val (startPos, instructions) = text.split("\n\n")
        val queues1 = initQueues(startPos)
        val moves = parseInstructions(instructions)

        moves.forEach { (qty, from, to) ->
            (0 until qty).forEach { _ ->
                queues1[to - 1].add(0, queues1[from - 1].removeFirst())
            }
        }
        val ans = queues1.map { it[0] }.joinToString("")
        println("day 05 part 1: $ans")
    }

    fun part2() {
        val (startPos, instructions) = text.split("\n\n")
        val queues = initQueues(startPos)
        val moves = parseInstructions(instructions)

//        debug("start", queues)

        moves.forEach { (qty, from, to) ->
            val sublist = mutableListOf<Char>()
            (0 until qty).forEach { _ ->
                sublist.add(queues[from - 1].removeFirst())
            }
            queues[to - 1].addAll(0, sublist)
//            debug("move $qty from $from to $to", queues)
        }
        val ans2 = queues.map { it[0] }.joinToString("")

        println("day 05 part 2: $ans2")
    }

    private fun debug(instruction: String, queues: List<List<Char>>) {
        println(instruction)
        queues.forEachIndexed { i, queue ->
            println("${i + 1}: $queue")
        }
        println()
    }

    private fun initQueues(startPos: String): List<MutableList<Char>> {
        val queues = MutableList(9) { mutableListOf<Char>() }
        val columnToIndexMap = (0..8).associateBy { 1 + it * 4 }
        startPos.split("\n").forEach { line ->
            line.forEachIndexed { col, char ->
                if (char in 'A'..'Z') {
                    val queueIndex = columnToIndexMap[col]!!
                    queues[queueIndex].add(char)
                }
            }
        }
        return queues
    }

    private fun parseInstructions(instructions: String): List<List<Int>> {
        return instructions.split("\n").filterNot { it.isBlank() }
            .map { line ->
                Regex("move (\\d+) from (\\d) to (\\d)")
                    .matchEntire(line)!!.groupValues.subList(1, 4).map { it.toInt() }
            }
    }

}