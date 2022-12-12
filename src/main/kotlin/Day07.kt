object Day07 {

    private val input = FileReader().readFile("input-day07.txt")

    private data class File(var name: String, var size: Int?, var parent: File?, var children: List<File>?) {
        val isDirectory get() = size == null
        fun totalSize(): Int = size ?: children!!.sumOf { it.totalSize() }
    }

    fun part1() {
        val root = createFileTree()
        val list = findFiles(root) { f ->
            f.isDirectory && f.totalSize() <= 100_000
        }
        val ans = list.sumOf { it.totalSize() }
        println("day 07 part 1: $ans")
    }

    fun part2() {
        val root = createFileTree()

        val totalDiskSpace = 70_000_000
        val totalSpaceRequired = 30_000_000
        val spaceUsed = root.totalSize()
        val spaceAvailable = totalDiskSpace - spaceUsed
        val additionalSpaceRequired = totalSpaceRequired - spaceAvailable

        val list = findFiles(root) { f ->
            f.totalSize() >= additionalSpaceRequired
        }

        val optimalCandidate = list.sortedBy { it.totalSize() }
            .first { it.totalSize() >= additionalSpaceRequired }

        val ans = optimalCandidate.totalSize()
        println("day 07 part 2: $ans")
    }

    private fun findFiles(root: File, include: (File) -> Boolean): List<File> {
        val list = mutableListOf<File>()
        recurseFileTree(root, 0, list, include)
        return list
    }

    private fun recurseFileTree(f: File,
                                level: Int,
                                candidates: MutableList<File>,
                                include: (File) -> Boolean) {
        if (include(f)) {
            candidates.add(f)
        }
        f.children?.forEach {
            recurseFileTree(it, level + 1, candidates, include)
        }
    }

    private fun createFileTree(): File {
        val root = File("/", null, null, null)
        root.parent = root
        var currentDir: File = root
        input.split("\n\$ ").filterNot { it.isBlank() }
            .forEach { block ->
                if (block.startsWith("cd")) {
                    currentDir = when(val target = block.removePrefix("cd").trim()) {
                        "/" -> root
                        ".." -> currentDir.parent ?: root
                        else -> currentDir.children!!.first { f -> f.name == target }
                    }
                } else if (block.startsWith("ls")) {
                    val files = block.split("\n").filterNot { it.isBlank() }.drop(1).map { line ->
                        if (line.startsWith("dir")) {
                            File(line.removePrefix("dir").trim(), null, currentDir, null)
                        } else {
                            val f = line.split(Regex("\\s+"))
                            File(f[1], f[0].toInt(), currentDir, null)
                        }
                    }
                    currentDir.children = files
                }
            }
        return root
    }

}