class Day02 : Puzzle {

    val splitLine = { line: String ->
        line
            .split(": ").last()
            .split("; ")
    }

    val parseCube = { cube: String ->
        val splitCube = cube.split(" ")
        splitCube.last() to splitCube.first().toLong()
    }

    val parseSet = { set: String ->
        set
            .split(", ")
            .map { cube -> parseCube(cube) }
    }

    override fun task1(): String {
        val colorsOfInterestToTotalNumber = mapOf("red" to 12L, "green" to 13L, "blue" to 14L)

        return readDataFile()
            .readLines()
            .map { line ->
                splitLine(line)
                    .map { set -> parseSet(set) }
            }
            .withIndex()
            .filter { indexedGame ->
                indexedGame.value
                    .all { game ->
                        game.all { set -> colorsOfInterestToTotalNumber.getValue(set.first) >= set.second }
                    }
            }
            .sumOf { indexedGame -> indexedGame.index + 1L }
            .toString()
    }

    override fun task2(): String {
        return readDataFile()
            .readLines()
            .map { line ->
                splitLine(line)
                    .flatMap { set -> parseSet(set) }
                    .groupBy { it.first }
                    .map { groupedGame -> groupedGame.value.maxOf { it.second } }
            }
            .sumOf { game -> game.reduce(Long::times) }
            .toString()
    }
}

fun main() {
    Day02().printResults()
}
