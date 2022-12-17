import com.google.gson.*

object Day13 {

    private val input = FileReader().readFile("input-day13.txt")

    fun part1() {

        val pairs = input.split("\n\n").filterNot { it.isBlank() }.map { block ->
            block.split("\n").filterNot { it.isBlank() }.map {
                JsonParser.parseString(it).asJsonArray }.let { it[0] to it[1] }
        }
        val results = pairs.mapIndexed { index, pair ->
//            println("Pair ${index + 1}\n${pair.first}\n${pair.second}")

            val result = compare(pair.first, pair.second)
            if (result == 0)
                throw IllegalStateException()
//            println(if (result == -1) "right order\n" else "wrong order\n")

            if (result == -1) index + 1 else 0
        }

        val ans = results.sum()

        println("day 13 part 1: $ans")
    }

    private fun compare(a: JsonElement, b: JsonElement): Int {
        if (a.isJsonPrimitive && b.isJsonPrimitive) {
            return a.asInt.compareTo(b.asInt)
        } else if (a.isJsonArray && b.isJsonArray) {
            if (a.asJsonArray.size() == 0 && b.asJsonArray.size() == 0) {
                return 0
            }
            (0 until a.asJsonArray.size()).forEach { i ->
                if (i >= b.asJsonArray.size())
                    return 1
                val result = compare(a.asJsonArray.get(i), b.asJsonArray.get(i))
                if (result != 0)
                    return result
            }
            return -1
        } else if (a.isJsonPrimitive && b.isJsonArray) {
            return compare(JsonArray(1).apply { add(a) }, b)
        } else if (a.isJsonArray && b.isJsonPrimitive) {
            return compare(a, JsonArray(1).apply { add(b) })
        }
        return 0
    }


}

private val TEST_INPUT = """
    [1,1,3,1,1]
    [1,1,5,1,1]

    [[1],[2,3,4]]
    [[1],4]

    [9]
    [[8,7,6]]

    [[4,4],4,4]
    [[4,4],4,4,4]

    [7,7,7,7]
    [7,7,7]

    []
    [3]

    [[[]]]
    [[]]

    [1,[2,[3,[4,[5,6,7]]]],8,9]
    [1,[2,[3,[4,[5,6,0]]]],8,9]
""".trimIndent()
