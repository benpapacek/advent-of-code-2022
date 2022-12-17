object Day12 {

    private val input = FileReader().readFile("input-day12.txt")

    data class Node(
        val row: Int, val col: Int, val c: Char, val value: Char, var neighbours: List<Node>,
        var dist: Int = Integer.MAX_VALUE, var prev: Node? = null
    ) {
        override fun toString() = "$c($row,$col)"
        override fun equals(other: Any?) = (other as? Node)?.row == this.row && (other as? Node)?.col == this.col
        override fun hashCode() = 31 * row + col
    }

    fun part1() {
        val nodes = getNodes()
        val source = nodes.first { it.c == 'S' }
        val target = nodes.first { it.c == 'E' }

        val path = dijkstra(nodes, source, target)
//        println(path.reversed())

        // subtract one to account for the start node
        val ans = path.size - 1
        println("day 12 part 1: $ans")
    }

    fun part2() {
        val originalNodes = getNodes()

        // add a dummy node for the source
        val source = Node(-1, -1, 'S', 'A', originalNodes.filter { it.value == 'a' })
        val target = originalNodes.first { it.c == 'E' }
        val nodes = originalNodes + listOf(source)

        val path = dijkstra(nodes, source, target)
//        println(path.reversed())

        // now subtract two to account for the dummy 'start' node and the start node itself
        val ans = path.size - 2
        println("day 12 part 2: $ans")
    }

    private fun getNodes(): List<Node> {
        val nodeGrid = input.split("\n").filterNot { it.isBlank() }.mapIndexed { i, line ->
            line.toCharArray().mapIndexed { j, c ->
                val elevation = when (c) {
                    'S' -> 'a'
                    'E' -> 'z'
                    else -> c
                }
                Node(i, j, c, elevation, listOf())
            }
        }
        (0 .. nodeGrid.indices.last).forEach { i ->
            (0 .. nodeGrid[i].indices.last).forEach { j ->
                val node = nodeGrid[i][j]
                val north = if (i > 0 && nodeGrid[i - 1][j].value <= node.value + 1) nodeGrid[i - 1][j] else null
                val south = if (i < nodeGrid.size - 1 && nodeGrid[i + 1][j].value <= node.value + 1) nodeGrid[i + 1][j] else null
                val east = if (j < nodeGrid[i].size - 1 && nodeGrid[i][j + 1].value <= node.value + 1) nodeGrid[i][j + 1] else null
                val west = if (j > 0 && nodeGrid[i][j - 1].value <= node.value + 1) nodeGrid[i][j - 1] else null

                node.neighbours = listOfNotNull(north, south, east, west)
            }
        }
        return nodeGrid.flatten()
    }

    // Dijkstra's algorithm https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
    private fun dijkstra(nodes: List<Node>, source: Node, target: Node): List<Node> {
        val queue = nodes.toMutableList()

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
                return path.toList()
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
    Sabqponm
    abcryxxl
    accszExk
    acctuvwj
    abdefghi
""".trimIndent()