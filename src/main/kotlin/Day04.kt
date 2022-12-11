object Day04 {

    private val pairs = FileReader().readFile("input-day04.txt")
        .split("\n").filterNot { it.isBlank() }
        .map {
            it.split(",").map { pair ->
                pair.split("-").let { n -> (n[0].toInt()..n[1].toInt()) }
            }
        }

    fun part1() {
        val ans = pairs.count { (aRange, bRange) ->
            (aRange.contains(bRange.min()) && aRange.contains(bRange.max())) ||
                    (bRange.contains(aRange.min()) && bRange.contains(aRange.max()))
        }
        println("day 04 part 1: $ans")
    }

    fun part2() {
        val ans = pairs.count { (aRange, bRange) ->
            (aRange.contains(bRange.min()) || aRange.contains(bRange.max())) ||
                    (bRange.contains(aRange.min()) || bRange.contains(aRange.max()))
        }
        println("day 04 part 2: $ans")
    }


}