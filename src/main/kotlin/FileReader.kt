class FileReader {
    fun readFile(path: String) = javaClass.getResource(path)!!.readText()
}