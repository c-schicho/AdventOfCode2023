class Day01 : Puzzle {
    override fun task1(): String {
        return readDataFile()
            .readLines()
            .map { line -> line.toCharArray() }
            .sumOf { line -> "${line.first { it.isDigit() }}${line.last { it.isDigit() }}".toLong() }
            .toString()
    }

    override fun task2(): String {
        return readDataFile()
            .readLines()
            .sumOf { line ->
                val transformWordToValue: (String, Int) -> Char = { searchLine: String, idx: Int ->
                    val linePartWithWord = searchLine.substring(idx)

                    wordValueMap.keys
                        .firstNotNullOf { key -> if (linePartWithWord.startsWith(key)) wordValueMap[key] else null }
                }

                val indexFirstNumber = line.indexOfFirst { it.isDigit() }
                val indexFirstNumberWord = line.indexOfAny(wordValueMap.keys)
                val firstDigit = if (
                    indexFirstNumberWord < 0 || (indexFirstNumber in 0..<indexFirstNumberWord)
                ) {
                    line[indexFirstNumber]
                } else {
                    transformWordToValue(line, indexFirstNumberWord)
                }

                val indexLastNumber = line.indexOfLast { it.isDigit() }
                val indexLastNumberWord = line.lastIndexOfAny(wordValueMap.keys)
                val lastDigit = if (
                    indexLastNumberWord < 0 || (indexLastNumber >= 0 && indexLastNumber > indexLastNumberWord)
                ) {
                    line[indexLastNumber]
                } else {
                    transformWordToValue(line, indexLastNumberWord)
                }

                "$firstDigit$lastDigit".toLong()
            }.toString()
    }

    companion object {
        val wordValueMap = mapOf(
            "one" to '1',
            "two" to '2',
            "three" to '3',
            "four" to '4',
            "five" to '5',
            "six" to '6',
            "seven" to '7',
            "eight" to '8',
            "nine" to '9'
        )
    }
}

fun main() {
    Day01().printResults()
}
