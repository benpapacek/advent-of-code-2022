object Day03 {

    private val scoreMap = (('a'..'z').joinToString("") + ('A'..'Z').joinToString(""))
        .mapIndexed { index, item -> item to index + 1 }.toMap()

    fun part1() {
        val ans = FileReader().readFile("input-day03.txt")
            .split("\n").filterNot { it.isBlank() }
            .map { it.substring(0, it.length / 2).toSet().intersect(it.substring(it.length / 2, it.length).toSet()) }
            .map {
                assert(it.size == 1)
                scoreMap[it.first()]!!
            }.sumOf { it }

        println("day 03 part 1: $ans")
    }

    fun part2() {
        val ans = FileReader().readFile("input-day03.txt")
            .split("\n").asSequence().filterNot { it.isBlank() }
            .mapIndexed { index, item -> index to item }
            .groupBy { it.first / 3 }
            .map {
                val sets = it.value.map { i -> i.second.toSet() }
                sets[0].intersect(sets[1]).intersect(sets[2])
            }.map {
                assert(it.size == 1)
                scoreMap[it.first()]!!
            }.sumOf { it }

        println("day 03 part 2: $ans")
    }


}