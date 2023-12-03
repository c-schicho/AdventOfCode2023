import kotlin.math.max
import kotlin.math.min

class Day03 : Puzzle {

    private fun <E> Iterable<E>.indexesOf(e: E) = mapIndexedNotNull { index, elem -> index.takeIf { elem == e } }

    private fun Iterable<Int>.mapToRanges(): List<IntRange> {
        val intList = this
        return buildList {
            intList.forEach {
                val last = lastOrNull()
                if (last?.endInclusive == it - 1) {
                    set(lastIndex, last.first..it)
                } else {
                    add(it..it)
                }
            }
        }
    }

    private fun Iterable<IntRange>.mapToSearchRanges(maxColIdx: Int): List<IntRange> =
        map { IntRange(max(it.first - 1, 0), min(it.last + 1, maxColIdx)) }

    private fun parseNumber(
        lineIndex: Int,
        searchRange: IntRange,
        engineLines: List<CharArray>,
        digitMask: List<List<Boolean>>,
        parseFirst: Boolean = true
    ): Long {
        val numberRanges = digitMask[lineIndex]
            .indexesOf(true)
            .mapToRanges()

        val numberRange = if (parseFirst) {
            numberRanges
                .first { numberIndexRange -> numberIndexRange.intersect(searchRange).isNotEmpty() }
        } else {
            numberRanges
                .last { numberIndexRange -> numberIndexRange.intersect(searchRange).isNotEmpty() }
        }

        return engineLines[lineIndex]
            .slice(numberRange)
            .joinToString("")
            .toLong()
    }

    private fun List<Boolean>.hasTwoAdjacentNumbers() = this == listOf(true, false, true)

    private fun List<Boolean>.hasAdjacentNumber() = this.contains(true)

    override fun task1(): String {
        val engineLines = readDataFile().readLines().map(String::toCharArray)
        val digitMask = engineLines.map { line -> line.map(Char::isDigit) }
        val symbolMask = engineLines.map { line -> line.map { char -> !char.isDigit() && char != '.' } }
        val maxLineIdx = engineLines.lastIndex
        val maxColIdx = engineLines.first().lastIndex

        var engineSum = 0L

        for (lineIdx in engineLines.indices) {
            val lineAboveIdx = max(lineIdx - 1, 0)
            val lineBelowIdx = min(lineIdx + 1, maxLineIdx)
            val numberIndexRanges = digitMask[lineIdx].indexesOf(true).mapToRanges()
            val symbolSearchRanges = numberIndexRanges.mapToSearchRanges(maxColIdx)

            for ((rangeIndex, symbolSearchRange) in symbolSearchRanges.withIndex()) {
                val hasAdjacentSymbol = (
                    symbolMask[lineAboveIdx].slice(symbolSearchRange) +
                        symbolMask[lineIdx].slice(symbolSearchRange) +
                        symbolMask[lineBelowIdx].slice(symbolSearchRange)
                    ).contains(true)

                if (hasAdjacentSymbol) {
                    val number = engineLines[lineIdx]
                        .slice(numberIndexRanges[rangeIndex])
                        .joinToString("")
                        .toLong()

                    engineSum += number
                }
            }
        }

        return engineSum.toString()
    }

    override fun task2(): String {
        val engineLines = readDataFile().readLines().map(String::toCharArray)
        val digitMask = engineLines.map { line -> line.map(Char::isDigit) }
        val symbolMask = engineLines.map { line -> line.map { char -> char == '*' } }
        val maxLineIdx = engineLines.lastIndex
        val maxColIdx = engineLines.first().lastIndex

        var gearRatioSum = 0L

        for (lineIdx in engineLines.indices) {
            val lineAboveIdx = max(lineIdx - 1, 0)
            val lineBelowIdx = min(lineIdx + 1, maxLineIdx)
            val numberSearchRanges = symbolMask[lineIdx]
                .indexesOf(true)
                .mapToRanges()
                .mapToSearchRanges(maxColIdx)

            for (numberSearchRange in numberSearchRanges) {
                val digitSearchMaskAbove = digitMask[lineAboveIdx].slice(numberSearchRange)
                val digitSearchMaskInLine = digitMask[lineIdx].slice(numberSearchRange)
                val digitSearchMaskBelow = digitMask[lineBelowIdx].slice(numberSearchRange)

                when {
                    digitSearchMaskAbove.hasAdjacentNumber() && digitSearchMaskInLine.hasAdjacentNumber() -> {
                        val numberAbove = parseNumber(lineIdx - 1, numberSearchRange, engineLines, digitMask)
                        val numberInline = parseNumber(lineIdx, numberSearchRange, engineLines, digitMask)
                        gearRatioSum += (numberAbove * numberInline)
                    }

                    digitSearchMaskInLine.hasAdjacentNumber() && digitSearchMaskBelow.hasAdjacentNumber() -> {
                        val numberInline = parseNumber(lineIdx, numberSearchRange, engineLines, digitMask)
                        val numberBelow = parseNumber(lineIdx + 1, numberSearchRange, engineLines, digitMask)
                        gearRatioSum += (numberBelow * numberInline)
                    }

                    digitSearchMaskAbove.hasAdjacentNumber() && digitSearchMaskBelow.hasAdjacentNumber() -> {
                        val numberAbove = parseNumber(lineIdx - 1, numberSearchRange, engineLines, digitMask)
                        val numberBelow = parseNumber(lineIdx + 1, numberSearchRange, engineLines, digitMask)
                        gearRatioSum += (numberAbove * numberBelow)
                    }

                    digitSearchMaskAbove.hasTwoAdjacentNumbers() -> {
                        val firstNumber = parseNumber(lineIdx - 1, numberSearchRange, engineLines, digitMask)
                        val secondNumber = parseNumber(lineIdx - 1, numberSearchRange, engineLines, digitMask, false)
                        gearRatioSum += (firstNumber * secondNumber)
                    }

                    digitSearchMaskInLine.hasTwoAdjacentNumbers() -> {
                        val firstNumber = parseNumber(lineIdx, numberSearchRange, engineLines, digitMask)
                        val secondNumber = parseNumber(lineIdx, numberSearchRange, engineLines, digitMask, false)
                        gearRatioSum += (firstNumber * secondNumber)
                    }

                    digitSearchMaskBelow.hasTwoAdjacentNumbers() -> {
                        val firstNumber = parseNumber(lineIdx + 1, numberSearchRange, engineLines, digitMask)
                        val secondNumber = parseNumber(lineIdx + 1, numberSearchRange, engineLines, digitMask, false)
                        gearRatioSum += (firstNumber * secondNumber)
                    }
                }
            }
        }

        return gearRatioSum.toString()
    }
}

fun main() {
    Day03().printResults()
}
