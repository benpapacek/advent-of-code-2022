import Day19.Action.*

object Day19 {

    private data class Blueprint(
        val id: Int,
        val oreCostInOre: Int,
        val clayCostInOre: Int,
        val obsidianCostInOre: Int,
        val obsidianCostInClay: Int,
        val geodeCostInOre: Int,
        val geodeCostInObsidian: Int,
    )

    private data class Resources(
        val oreRobots: Int = 0,
        val clayRobots: Int = 0,
        val obsidianRobots: Int = 0,
        val geodeRobots: Int = 0,
        val ore: Int = 0,
        val clay: Int = 0,
        val obsidian: Int = 0,
        val geodes: Int = 0
    )

    private enum class Action {
        ORE_ROBOT, CLAY_ROBOT, OBSIDIAN_ROBOT, GEODE_ROBOT
    }

    private val input = FileReader().readFile("input-day19.txt")

    fun part1() {
        val blueprints = getBlueprints()
        val results = blueprints.map { blueprint ->
            blueprint.id to findOptimumForBlueprint(blueprint, 24)
        }
        val ans = results.sumOf { it.first * it.second }
        println("day 19 part 1: $ans")
    }

    fun part2() {
        val blueprints = getBlueprints()
        val results = (1..2).map { i ->
            blueprints[i].id to findOptimumForBlueprint(blueprints[i], 32)
        }
        println(results)
//        val ans = results.reduce { a, b -> a * b }
//        println("day 19 part 2: $ans")
    }

    private fun getBlueprints(): List<Blueprint> {
        return input.split("\n").filterNot { it.isBlank() }.map { line ->
            val regex = Regex("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.")
            val matches = regex.matchEntire(line.trim())!!
            Blueprint(
                id = matches.groupValues[1].toInt(),
                oreCostInOre = matches.groupValues[2].toInt(),
                clayCostInOre = matches.groupValues[3].toInt(),
                obsidianCostInOre = matches.groupValues[4].toInt(),
                obsidianCostInClay = matches.groupValues[5].toInt(),
                geodeCostInOre = matches.groupValues[6].toInt(),
                geodeCostInObsidian = matches.groupValues[7].toInt(),
            )
        }
    }

    private fun findOptimumForBlueprint(blueprint: Blueprint, time: Int): Int {
//        println("starting blueprint ${blueprint.id}...")
        val solutionSet = mutableSetOf<Int>()
//        val timeTaken = measureNanoTime {
            Action.values().forEach { a ->
                recurse(a, time, blueprint, Resources(oreRobots = 1), solutionSet)
            }
//        }
//        println("found optimum for blueprint ${blueprint.id} (${solutionSet.max()}) in ${timeTaken * 0.000000001}s")
        return solutionSet.max()
    }

    private fun canPerformAction(action: Action, resources: Resources, blueprint: Blueprint) = when(action) {
        ORE_ROBOT -> (resources.ore >= blueprint.oreCostInOre)
        CLAY_ROBOT -> (resources.ore >= blueprint.clayCostInOre)
        OBSIDIAN_ROBOT -> resources.ore >= blueprint.obsidianCostInOre &&
                resources.clay >= blueprint.obsidianCostInClay
        GEODE_ROBOT -> resources.ore >= blueprint.geodeCostInOre &&
                resources.obsidian >= blueprint.geodeCostInObsidian
    }

    private fun recurse(
        action: Action,
        timeRemaining: Int,
        blueprint: Blueprint,
        resources: Resources,
        solutionSet: MutableSet<Int>,
    ) {
        if (timeRemaining <= 0) {
            solutionSet.add(resources.geodes)
            return
        }

        if (canPerformAction(action, resources, blueprint)) {
            val newResources = when (action) {
                ORE_ROBOT -> resources.copy(
                    ore = resources.ore + resources.oreRobots - blueprint.oreCostInOre,
                    clay = resources.clay + resources.clayRobots,
                    obsidian = resources.obsidian + resources.obsidianRobots,
                    geodes = resources.geodes + resources.geodeRobots,
                    oreRobots = resources.oreRobots + 1,
                )

                CLAY_ROBOT -> resources.copy(
                    ore = resources.ore + resources.oreRobots - blueprint.clayCostInOre,
                    clay = resources.clay + resources.clayRobots,
                    obsidian = resources.obsidian + resources.obsidianRobots,
                    geodes = resources.geodes + resources.geodeRobots,
                    clayRobots = resources.clayRobots + 1,
                )

                OBSIDIAN_ROBOT -> resources.copy(
                    ore = resources.ore + resources.oreRobots - blueprint.obsidianCostInOre,
                    clay = resources.clay + resources.clayRobots - blueprint.obsidianCostInClay,
                    obsidian = resources.obsidian + resources.obsidianRobots,
                    geodes = resources.geodes + resources.geodeRobots,
                    obsidianRobots = resources.obsidianRobots + 1,
                )

                GEODE_ROBOT -> resources.copy(
                    ore = resources.ore + resources.oreRobots - blueprint.geodeCostInOre,
                    clay = resources.clay + resources.clayRobots,
                    obsidian = resources.obsidian + resources.obsidianRobots - blueprint.geodeCostInObsidian,
                    geodes = resources.geodes + resources.geodeRobots,
                    geodeRobots = resources.geodeRobots + 1,
                )
            }
            Action.values().forEach { nextAction ->
                recurse(nextAction, timeRemaining - 1, blueprint, newResources, solutionSet)
            }
        } else {
            val newResources = resources.copy(
                ore = resources.ore + resources.oreRobots,
                clay = resources.clay + resources.clayRobots,
                obsidian = resources.obsidian + resources.obsidianRobots,
                geodes = resources.geodes + resources.geodeRobots
            )
            recurse(action, timeRemaining - 1, blueprint, newResources, solutionSet)
        }
    }
}

private val TEST_INPUT = """
Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
""".trimIndent()