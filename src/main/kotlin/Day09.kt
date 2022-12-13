import kotlin.math.abs
import kotlin.math.sign

object Day09 {

    private val input = FileReader().readFile("input-day09.txt")

    private val PART_2_TEST_1 = listOf('R' to 4, 'U' to 4, 'L' to 3, 'D' to 1, 'R' to 4, 'D' to 1, 'L' to 5, 'R' to 2)
    private val PART_2_TEST_2 = listOf('R' to 5, 'U' to 8, 'L' to 8, 'D' to 3, 'R' to 17, 'D' to 10, 'L' to 25, 'U' to 20)

    data class Point(var x: Int, var y: Int)

    fun part1() {
        val head = Point(0, 0)
        val tail = Point(0, 0)
        val tailSet = mutableSetOf<Pair<Int, Int>>()

        val commands = parseInput()
        commands.forEach { command ->
            val (x, steps) = command
            val dir = parseDirection(x)
            (0 until steps).forEach { _ ->
                head.x += dir.x
                head.y += dir.y

                updateNode(tail, head)

                tailSet.add(tail.x to tail.y)
            }
        }
        println("day 09 part 1: ${tailSet.size}")
    }

    fun part2() {
        val rope = (0 .. 9).map { Point(0, 0) }
        val tailSet = mutableSetOf<Pair<Int, Int>>()
        val commands = parseInput()

        commands.forEach { command ->
            val (x, steps) = command
            val dir = parseDirection(x)
            (0 until steps).forEach { _ ->
                rope[0].x += dir.x
                rope[0].y += dir.y
                (1..9).forEach { i ->
                    updateNode(rope[i], rope[i - 1])
                }
                tailSet.add(rope[9].x to rope[9].y)
//                debug(20, rope, tailSet)
            }
        }
        println("day 09 part 2: ${tailSet.size}")
    }

    private fun debug(size: Int, rope: List<Point>, tailSet: Set<Pair<Int, Int>>) {
        val map = rope.mapIndexed { i, point -> point to "$i" }.toMap()
        (-size..size).forEach { i ->
            (-size..size).forEach { j ->
                val k = Point(j, -i)
                if (map.containsKey(k)) {
                    print(map[k])
                } else if (k.x to k.y in tailSet) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
        println()
        println()
    }

    private fun parseInput(): List<Pair<Char, Int>> {
        return input.split("\n").filterNot { it.isBlank() }.map { line ->
            val command = line.split(Regex("\\s+"))
            command[0].trim().toCharArray()[0] to command[1].toInt()
        }
    }

    private fun parseDirection(dir: Char) = when(dir) {
        'L' -> Point(-1, 0)
        'R' -> Point(1, 0)
        'U' -> Point(0, 1)
        'D' -> Point(0, -1)
        else -> throw IllegalStateException()
    }

    private fun updateNode(tail: Point, head: Point) {
        val dx = head.x - tail.x
        val dy = head.y - tail.y
        when {
            abs(dx) > 1 && abs(dy) > 1 -> {
                tail.x += dx.sign
                tail.y += dy.sign
            }
            abs(dx) > 1 -> {
                tail.x += dx.sign
                tail.y = head.y
            }
            abs(dy) > 1 -> {
                tail.y += dy.sign
                tail.x = head.x
            }
        }
    }

}