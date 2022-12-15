object Day11 {

    private val input = FileReader().readFile("input-day11.txt")

    class Monkey(
        val id: Int,
        val items: MutableList<Int>,
        val operation: (Int) -> Int,
        val test: (Int) -> Int,
        var count: Int = 0
    )

    fun part1() {
        val monkeys = input.split("\n\n").map { monkey ->

            val lines = monkey.split("\n")
            val monkeyId = Regex("Monkey (\\d+):").matchEntire(lines[0])!!.groupValues[1].toInt()
            val startingItems =
                lines[1].trim().removePrefix("Starting items: ").split(",").map { it.trim().toInt() }.toMutableList()
            val operation =
                parseOperation(Regex("\\s+Operation: new = old (.*)").matchEntire(lines[2])!!.groupValues[1])
            val testCondition = Regex("\\s+Test: divisible by (\\d+)").matchEntire(lines[3])!!.groupValues[1].toInt()
            val resultIfTrue =
                Regex("\\s+If true: throw to monkey (\\d+)").matchEntire(lines[4])!!.groupValues[1].toInt()
            val resultIfFalse =
                Regex("\\s+If false: throw to monkey (\\d+)").matchEntire(lines[5])!!.groupValues[1].toInt()
            val test: (Int) -> Int = { n -> if (n % testCondition == 0) resultIfTrue else resultIfFalse }

            Monkey(
                id = monkeyId,
                items = startingItems,
                operation = operation,
                test = test
            )
        }.associateBy { it.id }

        (1..20).forEach { _ ->
            monkeys.values.sortedBy { it.id }.forEach { monkey ->
//                println("Monkey ${monkey.id}")
                while(monkey.items.isNotEmpty()) {
                    val nextItem = monkey.items.removeFirst()
                    val result = monkey.operation.invoke(nextItem) / 3
                    val nextMonkey = monkey.test(result)
                    monkeys[nextMonkey]!!.items.add(result)
                    monkey.count++
//                    println("\tMonkey inspects an item with a worry level of $nextItem.")
//                    println("\t\tWorry level is now $result.")
//                    println("\t\tItem with worry level $result is thrown to monkey $nextMonkey")
                }
            }
        }
        val ans = monkeys.values.map { it.count }.sortedDescending().take(2).reduce { a, b ->  a * b }
        println("day 11 part 1: $ans")
    }

    private fun parseOperation(operation: String): (Int) -> Int {
        val (operator, operand) = operation.split(Regex("\\s+")).map { it.trim() }
        return when (operator) {
            "+" -> { n -> n + if (operand == "old") n else operand.toInt() }
            "*" -> { n -> n * if (operand == "old") n else operand.toInt() }
            else -> throw IllegalStateException()
        }
    }

}


private val TEST_INPUT = """
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
""".trimIndent()