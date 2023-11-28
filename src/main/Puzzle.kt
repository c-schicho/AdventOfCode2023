import java.io.File

interface Puzzle {

    val day: String
        get() = this.javaClass.name.takeLast(2)

    fun task1(): String

    fun task2(): String

    private fun getInputFilename(readTestFile: Boolean): String = if(readTestFile) "$day-test.txt" else "$day.txt"

    private fun getInputFilePath(readTestFile: Boolean): String = "data/${getInputFilename(readTestFile)}"

    fun readDataFile(readTestFile: Boolean = false): File = File(getInputFilePath(readTestFile))

    fun printResultTask1() {
        println("### DAY $day / TASK 1 ###")
        println(task1())
    }

    fun printResultTask2() {
        println("### DAY $day / TASK 2 ###")
        println(task2())
    }

    fun printResults() {
        printResultTask1()
        printResultTask2()
    }
}
