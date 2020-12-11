package december

import AbstractDay
import CharGrid
import kotlin.math.max
import kotlin.math.min

typealias YXPair = Pair<Int, Int>
typealias NGrid = Array<Array<MutableList<YXPair>>> // Each Grid Cell has a list of neighbor coordinates (y, x)

class Day11 : AbstractDay() {

    private val input = obtainInputAsGrid()

    // Part 1: 2346
    override fun part1(): String = simulate(input, getNeighbors(input), 4).toString()

    // Part 2: 2111
    override fun part2(): String = simulate(input, getNeighborsAdvanced(input), 5).toString()

    private fun getNeighbors(grid: CharGrid): NGrid {
        val neighborMem = Array(grid.size) { Array(grid[it].size) { mutableListOf<YXPair>() } }
        grid.indices.forEach { y ->
            grid[y].indices.forEach { x ->
                for (i in max(y - 1, 0)..min(y + 1, grid.size - 1)) {
                    for (j in max(x - 1, 0)..min(x + 1, grid[y].size - 1)) {
                        if (i == y && j == x) continue
                        neighborMem[y][x].add(Pair(i, j))
                    }
                }
            }
        }
        return neighborMem
    }

    private fun getNeighborsAdvanced(grid: CharGrid): NGrid {
        val neighborMem = Array(grid.size) { Array(grid[it].size) { mutableListOf<YXPair>() } }
        grid.indices.forEach { y ->
            grid[y].indices.forEach { x ->
                for (i in -1..1) {
                    for (j in -1..1) {
                        if (i == 0 && j == 0) continue
                        var yy = y + i
                        var xx = x + j
                        loop@ while (xx >= 0 && yy >= 0 && yy < grid.size && xx < grid[yy].size) {
                            when {
                                grid[yy][xx] == '.' -> {
                                    yy += i
                                    xx += j
                                }
                                else -> {
                                    neighborMem[y][x].add(Pair(yy, xx))
                                    break@loop
                                }
                            }
                        }
                    }
                }
            }
        }
        return neighborMem
    }

    private fun countNeighbors(from: CharGrid, to: CharGrid, x: Int, y: Int, neighborMem: NGrid, limit: Int): Boolean {
        val seat = from[y][x]
        if (seat == '.') return false
        val n = neighborMem[y][x].count { from[it.first][it.second] == '#' }
        if (n == 0 && seat == 'L') {
            to[y][x] = '#'
            return true
        } else if (n >= limit && seat == '#') {
            to[y][x] = 'L'
            return true
        }
        to[y][x] = from[y][x]
        return false
    }

    private fun simulate(grid: CharGrid, neighborMem: NGrid, limit: Int): Int {
        var buffer = grid.copy()
        var swap = grid.copy()
        do {
            var updated = false
            buffer.indices.forEach { y ->
                buffer[y].indices.forEach { x ->
                    updated = countNeighbors(buffer, swap, x, y, neighborMem, limit) || updated
                }
            }
            val temp = buffer
            buffer = swap
            swap = temp
        } while (updated)
        return countSeats(buffer)
    }

    private fun countSeats(grid: CharGrid): Int = grid.fold(0, { a, v -> a + v.count { it == '#' } })
}