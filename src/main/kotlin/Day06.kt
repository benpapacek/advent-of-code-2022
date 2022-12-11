object Day06 {

    private val input = FileReader().readFile("input-day06.txt")

    private fun findMarker(n: Int) = (n..input.length).first { i ->
        input.substring(i - n, i).toSet().size == n
    }

    fun part1() {
        val ans = findMarker(4)
        println("day 06 part 1: $ans")
    }

    fun part2() {
        val ans = findMarker(14)
        println("day 06 part 2: $ans")
    }

}