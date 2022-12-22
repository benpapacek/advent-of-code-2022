import kotlin.system.measureNanoTime

object Day16 {

    private val input = FileReader().readFile("input-day16.txt")

    private data class Valve(val id: String, val flowRate: Int) {
        lateinit var neighbours: List<Valve>
        var paths: List<List<Valve>>? = null

        var isOpen: Boolean = false

        var dist = Int.MAX_VALUE
        var prev: Valve? = null
        override fun toString(): String = "$id ($flowRate)"
    }

    fun part1() {
        val valves = getValves()
        val start = valves.find { it.id == "AA" }!!
        val solutions = mutableSetOf<Int>()

        recursePart1(solutions, valves, listOf(), start, 30, 0)

        val ans = solutions.max()
        println("day 16 part 1: $ans")
    }

    // TODO This is still not very efficient (takes over 5min to complete)
    fun part2() {
        val valves = getValves()
        val start = valves.find { it.id == "AA" }!!
        val solutions = mutableSetOf<Int>()

        val agent1 = Agent(0, listOf(), listOf(start), 0)
        val agent2 = Agent(0, listOf(), listOf(start), 0)

        recursePart2(solutions, valves, agent1, agent2, 26)

        val ans = solutions.max()
        println("day 16 part 2: $ans")
    }

    private fun getValves(): List<Valve> {
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
        return valves
    }

    private fun recursePart1(
        solutionSet: MutableSet<Int>,
        valves: List<Valve>,
        visited: List<Valve>,
        source: Valve,
        timeRemaining: Int,
        totalPressure: Int
    ) {
        val candidates = source.paths!!.filterNot { visited.contains(it.last()) }
        if (candidates.isEmpty()) {
            solutionSet.add(totalPressure)
        }
        candidates.forEach { path ->
            val target = path.last()
            val newTimeRemaining = timeRemaining - path.size
            if (newTimeRemaining > 0) {
                val newTotalPressure = totalPressure + target.flowRate * newTimeRemaining
                val newVisited = visited + listOf(target)
                recursePart1(solutionSet, valves, newVisited, target, newTimeRemaining, newTotalPressure)
            } else {
                solutionSet.add(totalPressure)
            }
        }
    }

    private data class Agent(
        val totalPressure: Int, val visited: List<Valve>, val path: List<Valve>, val step: Int, val done: Boolean = false
    )

    private fun recursePart2(
        solutionSet: MutableSet<Int>,
        valves: List<Valve>,
        agent1: Agent,
        agent2: Agent,
        timeRemaining: Int,
    ) {
        if ((agent1.done && agent2.done) || timeRemaining <= 0) {
            solutionSet.add(agent1.totalPressure + agent2.totalPressure)
            return
        }
        val agents = listOf(agent1, agent2)

        val newTimeRemaining = timeRemaining - 1
        val agent1Options = getAgentOptions(agent1, timeRemaining) { path ->
            agents.none { a -> a.path.last() == path.last() || a.visited.contains(path.last()) }
        }
        agent1Options.forEach { newAgent1 ->
            val agent2Options = getAgentOptions(agent2, timeRemaining) { path ->
                agents.none { a -> a.path.last() == path.last() || a.visited.contains(path.last()) }
                        && newAgent1.path.last() != path.last()
            }
            agent2Options.forEach { newAgent2 ->
                recursePart2(solutionSet, valves, newAgent1, newAgent2, newTimeRemaining)
            }
        }
    }

    private fun getAgentOptions(agent: Agent, timeRemaining: Int, pathFilter: (List<Valve>) -> Boolean) =
        if (agent.done) {
            listOf(agent)
        } else if (agent.step + 1 < agent.path.size) {
            listOf(agent.copy(step = agent.step + 1))
        } else {
            val source = agent.path.last()
            val newTotalPressure = agent.totalPressure + source.flowRate * timeRemaining
            val newVisited = agent.visited + listOf(source)
            val candidates = source.paths!!.filter(pathFilter)
            if (candidates.isEmpty()) {
                listOf(agent.copy(done = true, totalPressure = newTotalPressure, visited = newVisited))
            } else {
                candidates.map { newPath ->
                    agent.copy(path = newPath, step = 0, totalPressure = newTotalPressure, visited = newVisited)
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
