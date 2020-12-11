typealias CharGrid = Array<CharArray>

abstract class AbstractDay {

    private val thisClass = this::class.java
    private val day = thisClass.simpleName.substring(3).toInt()

    abstract fun part1(): String
    abstract fun part2(): String

    fun run() {
        println("\n=== Day $day ===")
        println("Part 1: " + part1())
        println("Part 2: " + part2())
    }

    protected fun obtainInput() = thisClass.getResourceAsStream("/day$day.txt").bufferedReader().readLines()

    protected fun <T> obtainInput(f: (String) -> T): List<T> = obtainInput().map(f)

    protected fun obtainInputAsGrid(): CharGrid {
        val input = obtainInput()
        return Array(input.size) { input[it].toCharArray() }
    }

    protected fun CharGrid.copy() = Array(size) { get(it).clone() }
}