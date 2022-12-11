object Day02 {

    private fun solve(scoreMap: Map<String, Int>) = FileReader().readFile("input-day02.txt")
        .split("\n").filterNot { it.isBlank() }
        .map { round ->
            val key = round.replace(" ", "").trim()
            scoreMap[key]!!
        }.sumOf { it }

    fun part1() {
        // A = rock, B = paper, C = scissors
        // X = rock, Y = paper, Z = scissors
        val scoreMap1 = mapOf(
            "AX" to 3 + 1, "AY" to 6 + 2, "AZ" to 0 + 3,
            "BX" to 0 + 1, "BY" to 3 + 2, "BZ" to 6 + 3,
            "CX" to 6 + 1, "CY" to 0 + 2, "CZ" to 3 + 3
        )
        val ans1 = solve(scoreMap1)
        println("day 02 part 1: $ans1")
    }

    fun part2() {
        // A = rock, B = paper, C = scissors
        // X = lose, Y = draw, Z = win
        val scoreMap2 = mapOf(
            "AX" to 0 + 3, "AY" to 3 + 1, "AZ" to 6 + 2,
            "BX" to 0 + 1, "BY" to 3 + 2, "BZ" to 6 + 3,
            "CX" to 0 + 2, "CY" to 3 + 3, "CZ" to 6 + 1
        )
        val ans2 = solve(scoreMap2)
        println("day 02 part 2: $ans2")
    }


}