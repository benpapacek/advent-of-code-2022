object Day16 {

    private val input = FileReader().readFile("input-day16.txt")

    private data class Valve(val id: String, val flowRate: Int) {
        lateinit var neighbours: List<Valve>
        var paths: List<List<Valve>>? = null

        var dist = Int.MAX_VALUE
        var prev: Valve? = null
        override fun toString(): String = "$id ($flowRate)"
    }

    fun part1() {
        val valveParams = input.split("\n").filterNot { it.isBlank() }.map { line ->
            val match = Regex("Valve (.*) has flow rate=(.*); tunnels? leads? to valves? (.*)").matchEntire(line)!!
            match.groupValues
        }
        val valves = valveParams.map { fields -> Valve(id = fields[1], flowRate = fields[2].toInt()) }
        valveParams.forEach { params ->
            val valve = valves.first { it.id == params[1] }
            valve.neighbours = params[3].split(",").map { it.trim() }.map { id -> valves.first { it.id == id } }
        }

        val usefulValves = valves.filter { it.id == "AA" || it.flowRate > 0 }

        usefulValves.forEach { source ->
            source.paths = usefulValves.filter { it != source }.map { target ->
                dijkstra(valves, source, target)
            }
        }

        val ans = solveRecursive(valves, GreedyStrategy())
        println("day 16 part 1: $ans")
    }

    private interface Strategy {
        fun getOptima(candidates: List<List<Valve>>): List<List<Valve>>
    }

    private class ExampleStrategy: Strategy {
        val ids = mutableListOf("DD", "BB", "JJ", "HH", "EE", "CC")
        override fun getOptima(candidates: List<List<Valve>>): List<List<Valve>> {
            return ids.removeFirstOrNull()?.let { k ->
                listOf(candidates.first { it.lastOrNull()?.id == k })
            } ?: emptyList()
        }
    }

    private class BestOfClosestStrategy(val best: Int, val ofClosest: Int): Strategy {
        override fun getOptima(candidates: List<List<Valve>>): List<List<Valve>> {
            val closest = candidates.sortedBy { it.last().paths!!.size }.take(ofClosest)
            return closest.sortedBy { it.last().flowRate }.take(best)
        }
    }

    private class GreedyStrategy: Strategy {
        override fun getOptima(candidates: List<List<Valve>>): List<List<Valve>> {
            return candidates
        }
    }

    private fun solveRecursive(valves: List<Valve>, strategy: Strategy): Int {
        val source = valves.find { it.id == "AA" }!!
        val solutions = mutableSetOf<Int>()
        recurse(solutions, valves, emptyList(), source, strategy, 30, 0)
        return solutions.max()
    }

    private fun recurse(
        solutionSet: MutableSet<Int>,
        valves: List<Valve>,
        visited: List<Valve>,
        source: Valve,
        strategy: Strategy,
        timeRemaining: Int,
        totalPressure: Int
    ) {

        val candidates = source.paths!!.filterNot { visited.contains(it.last()) }
        val optima: List<List<Valve>> = strategy.getOptima(candidates)
        if (optima.isEmpty()) {
            solutionSet.add(totalPressure)
//            println(totalPressure)
        }
        optima.forEach { path ->
            val target = path.last()
            val newTimeRemaining = timeRemaining - path.size
            if (newTimeRemaining > 0) {
//                totalPath.add(target)
                val newTotalPressure = totalPressure + target.flowRate * newTimeRemaining
                val newVisited = visited + listOf(target)
                recurse(solutionSet, valves, newVisited, target, strategy, newTimeRemaining, newTotalPressure)
            } else {
                solutionSet.add(totalPressure)
//                println(totalPressure)
            }
        }
    }

    private fun dijkstra(valves: List<Valve>, source: Valve, target: Valve): List<Valve> {
        val queue = valves.toMutableList()

        queue.forEach { it.dist = Int.MAX_VALUE; it.prev = null }

        source.dist = 0
        while(queue.isNotEmpty()) {
            val u = queue.minBy { it.dist }
            queue.remove(u)
            if (u == target) {
                val path = mutableListOf(u)
                var n = u
                while(n.prev != null) {
                    n = n.prev!!
                    path.add(n)
                }
                return path.toList().reversed()
            }
            u.neighbours.forEach { v ->
                if (queue.contains(v)) {
                    val alt = u.dist + 1
                    if (alt < v.dist) {
                        v.dist = alt
                        v.prev = u
                    }
                }
            }
        }
        throw IllegalStateException("no path found")
    }
}

private val TEST_INPUT = """
    Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
    Valve BB has flow rate=13; tunnels lead to valves CC, AA
    Valve CC has flow rate=2; tunnels lead to valves DD, BB
    Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
    Valve EE has flow rate=3; tunnels lead to valves FF, DD
    Valve FF has flow rate=0; tunnels lead to valves EE, GG
    Valve GG has flow rate=0; tunnels lead to valves FF, HH
    Valve HH has flow rate=22; tunnel leads to valve GG
    Valve II has flow rate=0; tunnels lead to valves AA, JJ
    Valve JJ has flow rate=21; tunnel leads to valve II
""".trimIndent()
