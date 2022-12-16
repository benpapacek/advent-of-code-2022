object Day11 {

    private val input = FileReader().readFile("input-day11.txt")

    class Monkey(
        val id: Int,
        val items: MutableList<Item>,
        val operation: Operation,
        val modulus: Int,
        val nextIfTrue: Int,
        val nextIfFalse: Int,
        var count: Long = 0
    )

    class Operation(val operator: String, val operand: String)

    class Item(
        var value: Int,
        val moduloMap: MutableMap<Int, Int> = mutableMapOf()
    )

    fun part2() {
        val monkeys = getMonkeyMap()
        (1..10_000).forEach { _ ->
            monkeys.values.sortedBy { it.id }.forEach { monkey ->
                while(monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()

                    monkeys.values.forEach { m ->
                        if (item.moduloMap[m.modulus] == null) {
                            item.moduloMap[m.modulus] = item.value
                        }
                        item.moduloMap[m.modulus] = performOperation(item.moduloMap[m.modulus]!!, monkey.operation) % m.modulus
                    }
                    val nextMonkey = if (item.moduloMap[monkey.modulus] == 0) monkey.nextIfTrue else monkey.nextIfFalse
                    monkeys[nextMonkey]!!.items.add(item)
                    monkey.count++
                }
            }
        }
        val ans = monkeys.values.map { it.count }.sortedDescending().take(2).reduce { a, b ->  a * b }
        println("day 11 part 2: $ans")
    }

    fun part1() {
        val monkeys = getMonkeyMap()

        (1..20).forEach { _ ->
            monkeys.values.sortedBy { it.id }.forEach { monkey ->
//                println("Monkey ${monkey.id}")
                while(monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    item.value = performOperation(item.value, monkey.operation) / 3
                    val nextMonkey = if (item.value % monkey.modulus == 0) monkey.nextIfTrue else monkey.nextIfFalse
                    monkeys[nextMonkey]!!.items.add(item)
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

    private fun getMonkeyMap() = input.split("\n\n").map { monkey ->

        val lines = monkey.split("\n")
        val monkeyId = Regex("Monkey (\\d+):").matchEntire(lines[0])!!.groupValues[1].toInt()
        val startingItems = lines[1].trim().removePrefix("Starting items: ")
            .split(",").map { it.trim().toInt() }.map { Item(it) }.toMutableList()
        val operation = Regex("\\s+Operation: new = old (.*)").matchEntire(lines[2])!!.groupValues[1]
            .split(Regex("\\s+")).map { it.trim() }.let { Operation(it[0], it[1]) }
        val modulus = Regex("\\s+Test: divisible by (\\d+)").matchEntire(lines[3])!!.groupValues[1].toInt()
        val nextIfTrue =
            Regex("\\s+If true: throw to monkey (\\d+)").matchEntire(lines[4])!!.groupValues[1].toInt()
        val nextIfFalse =
            Regex("\\s+If false: throw to monkey (\\d+)").matchEntire(lines[5])!!.groupValues[1].toInt()

        Monkey(
            id = monkeyId,
            items = startingItems,
            operation = operation,
            modulus = modulus,
            nextIfTrue = nextIfTrue,
            nextIfFalse = nextIfFalse
        )
    }.associateBy { it.id }

    private fun performOperation(input: Int, op: Operation): Int {
        return when (op.operator) {
            "+" -> input + (if (op.operand == "old") input else op.operand.toInt())
            "*" -> input * (if (op.operand == "old") input else op.operand.toInt())
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