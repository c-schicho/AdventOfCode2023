import java.io.File

interface Puzzle {

    val day: Int
        get() = this.javaClass.name.takeLast(2).toInt()

    fun task1(): String

    fun task2(): String

    fun readDataFile(readTestFile: Boolean = false): File = File(Utils.getDataPath(day = day, testFile = readTestFile))

    fun printResults() {
        println("### DAY $day / TASK 1 ###")
        println(task1())

        println("### DAY $day / TASK 2 ###")
        println(task2())
    }
}
