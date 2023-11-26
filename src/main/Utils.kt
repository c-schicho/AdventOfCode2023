object Utils {

    fun getDataPath(day: Int, testFile: Boolean): String {
        val dayString = day.toString().padStart(2, '0')
        val fileName = "${if (testFile) "$dayString-test" else dayString}.txt"
        return "data/$fileName"
    }
}
