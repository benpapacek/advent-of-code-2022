object Day01 {

    private val elves = FileReader().readFile("input-day01.txt")
        .split("\n\n").filterNot { it.isBlank() }
        .map { elf ->
            elf.split("\n").filterNot { it.isBlank() }.sumOf { it.toInt() }
        }

    fun part1() {
        val ans = elves.max()
        println("day 01 part 1: $ans")
    }

    fun part2() {
        val ans2 = elves.sortedDescending().take(3).sum()
        println("day 01 part 2: $ans2")
    }

}