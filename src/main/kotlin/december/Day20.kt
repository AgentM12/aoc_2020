package december

import AbstractDay
import java.util.*
import kotlin.math.sqrt

class Day20 : AbstractDay() {

    private val input = obtainInput()

//    private lateinit var tiles: List<Tile>

//    private fun setup() {
//        val mTiles = mutableListOf<Tile>()
//        var state = 0
//        var id = -1
//        var grid = Array(10) { "" }
//        var y = 0
//        for (i in input) {
//            when (state) {
//                0 -> {
//                    id = i.trim().split(" ", ":")[1].toInt()
//                    state = 1
//                }
//                1 -> {
//                    if (i.isBlank()) {
//                        mTiles.add(Tile(id, grid))
//                        grid = Array(10) { "" }
//                        y = 0
//                        state = 0
//                    } else {
//                        grid[y] = i
//                        y++
//                    }
//                }
//            }
//        }
//        tiles = mTiles
//    }

    private val pirated = Day20P(input)

    // Part 1: 104831106565027
    override fun part1(): String {
        return pirated.solvePart1().toString()
//        setup()
//        val availableTiles = LinkedList(tiles)
//        val availableEdges = mutableListOf<Edge>()
//        availableTiles.forEach { tile -> tile.edges.forEach { availableEdges.add(it) } }
//        val edgeConnectQueue = LinkedList<Edge>()
//        availableTiles.poll().edges.forEach {
//            edgeConnectQueue.offer(it)
//            availableEdges.remove(it)
//            it.orientationLockedFlag = 2
//        }
//
//        val unconnectedTileEdges = mutableMapOf<Tile, MutableList<Edge>>()
//
//        while (edgeConnectQueue.isNotEmpty()) {
//            val edge = edgeConnectQueue.poll()
//            val hash = if (edge.orientationLockedFlag != 2) edge.hash else edge.reversedHash
//            var found = false
//            for (e in availableEdges) {
//                if (hash == e.hash && e.orientationLockedFlag != 2) {
//                    val d = edge.direction.diff(e.direction)
//                    e.parent.edges.forEach {
//                        it.direction = it.direction.rotated(d) // rotate
//                        it.orientationLockedFlag = 1
//                    }
//                    availableEdges.remove(e)
//                    availableTiles.remove(e.parent)
//                    e.parent.edges.forEach {
//                        if (it != e) {
//                            edgeConnectQueue.offer(it)
//                            availableEdges.remove(it)
//                        }
//                    }
//                    found = true
//                    break
//                } else if (hash == e.reversedHash && e.orientationLockedFlag != 1) {
//                    val d = edge.direction.diff(e.direction)
//                    e.parent.edges.forEach {
//                        it.direction = it.direction.rotated(d) // rotate
//                        it.orientationLockedFlag = 2
//                    }
//                    e.parent.edges.forEach {
//                        if (it.direction != edge.direction && it.direction != e.direction)
//                            it.direction = it.direction.rotated(180) // flip
//                    }
//                    availableEdges.remove(e)
//                    availableTiles.remove(e.parent)
//                    e.parent.edges.forEach {
//                        if (it != e) {
//                            edgeConnectQueue.offer(it)
//                            availableEdges.remove(it)
//                        }
//                    }
//                    found = true
//                    break
//                }
//            }
//            if (!found) {
//                if (unconnectedTileEdges[edge.parent] != null) unconnectedTileEdges[edge.parent]!!.add(edge)
//                else unconnectedTileEdges[edge.parent] = mutableListOf(edge)
//            }
//        }
//        val cornerPieces = unconnectedTileEdges.filter { it.value.size == 2 }.keys
//        cornerPieces.forEach { println(it.id) }
//        return "-1"
    }

    // Part 2: 2093
    override fun part2(): String = pirated.solvePart2().toString()

//    class Tile(val id: Int, private val grid: Array<String>) {
//        val edges: List<Edge> = init()
//
//        private fun init(): List<Edge> {
//            val mEdges = mutableListOf<Edge>()
//            mEdges.add(Edge(this, grid[0].toInt(2), grid[0].reversed().toInt(2), Dir.NORTH))
//            val ls = grid.fold("") { s, c -> s + c[0] }
//            mEdges.add(Edge(this, ls.toInt(2), ls.reversed().toInt(2), Dir.WEST))
//            mEdges.add(Edge(this, grid[grid.size - 1].toInt(2), grid[grid.size - 1].reversed().toInt(2), Dir.SOUTH))
//            val rs = grid.fold("") { s, c -> s + c[c.length - 1] }
//            mEdges.add(Edge(this, rs.toInt(2), rs.reversed().toInt(2), Dir.EAST))
//            return mEdges
//        }
//
//    }
//
//    enum class Dir(private val deg: Int) {
//        NORTH(0),
//        EAST(90),
//        SOUTH(180),
//        WEST(270);
//
//        fun diff(other: Dir): Int = (this.deg - other.deg - 180) % 360
//
//        fun rotated(d: Int): Dir {
//            return Dir[this.deg + d]
//        }
//
//        companion object {
//            operator fun get(degrees: Int): Dir {
//                val ordinal = ((degrees % 360) + 360) % 360 / 90
//                return values()[ordinal]
//            }
//        }
//
//    }
//
//    class Edge(
//        val parent: Tile,
//        val hash: Int,
//        val reversedHash: Int,
//        var direction: Dir,
//        var orientationLockedFlag: Int = 0
//    )

    // PIRATED AGAIN: https://github.com/tginsberg/advent-2020-kotlin/blob/main/src/main/kotlin/com/ginsberg/advent2020/Day20.kt

    class Day20P(input: List<String>) {

        data class Point2D(val x: Int, val y: Int) {
            operator fun plus(other: Point2D): Point2D = Point2D(x + other.x, y + other.y)
            operator fun times(by: Int): Point2D = Point2D(x * by, y * by)
        }

        private val tiles: List<Tile> = parseInput(input)
        private val image: List<List<Tile>> = createImage()

        fun solvePart1(): Long =
            image.first().first().id * image.first().last().id * image.last().first().id * image.last().last().id

        fun solvePart2(): Int {
            val seaMonsterOffsets = listOf(
                Point2D(0, 18), Point2D(1, 0), Point2D(1, 5), Point2D(1, 6), Point2D(1, 11), Point2D(1, 12),
                Point2D(1, 17), Point2D(1, 18), Point2D(1, 19), Point2D(2, 1), Point2D(2, 4), Point2D(2, 7),
                Point2D(2, 10), Point2D(2, 13), Point2D(2, 16)
            )

            return imageToSingleTile().orientations().first { it.maskIfFound(seaMonsterOffsets) }.body.sumBy { row ->
                row.count { char -> char == '#' }
            }
        }

        private fun imageToSingleTile(): Tile {
            val rowsPerTile = tiles.first().body.size
            val body = image.flatMap { row ->
                (1 until rowsPerTile - 1).map { y ->
                    row.joinToString("") { it.insetRow(y) }.toCharArray()
                }
            }.toTypedArray()
            return Tile(0, body)
        }

        private fun createImage(): List<List<Tile>> {
            val width = sqrt(tiles.count().toFloat()).toInt()
            var mostRecentTile: Tile = findTopCorner()
            var mostRecentRowHeader: Tile = mostRecentTile
            return (0 until width).map { row ->
                (0 until width).map { col ->
                    when {
                        row == 0 && col == 0 ->
                            mostRecentTile
                        col == 0 -> {
                            mostRecentRowHeader =
                                mostRecentRowHeader.findAndOrientNeighbor(Orientation.South, Orientation.North, tiles)
                            mostRecentTile = mostRecentRowHeader
                            mostRecentRowHeader
                        }
                        else -> {
                            mostRecentTile =
                                mostRecentTile.findAndOrientNeighbor(Orientation.East, Orientation.West, tiles)
                            mostRecentTile
                        }
                    }
                }
            }
        }

        private fun findTopCorner(): Tile =
            tiles
                .first { tile -> tile.sharedSideCount(tiles) == 2 }
                .orientations()
                .first {
                    it.isSideShared(Orientation.South, tiles) && it.isSideShared(Orientation.East, tiles)
                }

        private enum class Orientation {
            North, East, South, West
        }

        private class Tile(val id: Long, var body: Array<CharArray>) {

            private val sides: Set<String> = Orientation.values().map { sideFacing(it) }.toSet()
            private val sidesReversed = sides.map { it.reversed() }.toSet()

            fun sharedSideCount(tiles: List<Tile>): Int =
                sides.sumOf { side ->
                    tiles
                        .filterNot { it.id == id }
                        .count { tile -> tile.hasSide(side) }
                }

            fun isSideShared(dir: Orientation, tiles: List<Tile>): Boolean =
                tiles
                    .filterNot { it.id == id }
                    .any { tile -> tile.hasSide(sideFacing(dir)) }

            fun findAndOrientNeighbor(mySide: Orientation, theirSide: Orientation, tiles: List<Tile>): Tile {
                val mySideValue = sideFacing(mySide)
                return tiles
                    .filterNot { it.id == id }
                    .first { it.hasSide(mySideValue) }
                    .also { it.orientToSide(mySideValue, theirSide) }
            }

            fun insetRow(row: Int): String =
                body[row].drop(1).dropLast(1).joinToString("")

            fun maskIfFound(mask: List<Point2D>): Boolean {
                var found = false
                val maxWidth = mask.maxByOrNull { it.y }!!.y
                val maxHeight = mask.maxByOrNull { it.x }!!.x
                (0..(body.size - maxHeight)).forEach { x ->
                    (0..(body.size - maxWidth)).forEach { y ->
                        val lookingAt = Point2D(x, y)
                        val actualSpots = mask.map { it + lookingAt }
                        if (actualSpots.all { body[it.x][it.y] == '#' }) {
                            found = true
                            actualSpots.forEach { body[it.x][it.y] = '0' }
                        }
                    }
                }
                return found
            }

            fun orientations(): Sequence<Tile> = sequence {
                repeat(2) {
                    repeat(4) {
                        yield(this@Tile.rotateClockwise())
                    }
                    this@Tile.flip()
                }
            }

            private fun hasSide(side: String): Boolean =
                side in sides || side in sidesReversed

            private fun flip(): Tile {
                body = body.map { it.reversed().toCharArray() }.toTypedArray()
                return this
            }

            private fun rotateClockwise(): Tile {
                body = body.mapIndexed { x, row ->
                    row.mapIndexed { y, _ ->
                        body[y][x]
                    }.reversed().toCharArray()
                }.toTypedArray()
                return this
            }

            private fun sideFacing(dir: Orientation): String =
                when (dir) {
                    Orientation.North -> body.first().joinToString("")
                    Orientation.South -> body.last().joinToString("")
                    Orientation.West -> body.map { row -> row.first() }.joinToString("")
                    Orientation.East -> body.map { row -> row.last() }.joinToString("")
                }

            private fun orientToSide(side: String, direction: Orientation) =
                orientations().first { it.sideFacing(direction) == side }

        }

        private fun parseInput(input: List<String>): List<Tile> =
            input.joinToString("\n").split("\n\n").map { it.lines() }.map { tileText ->
                val id = tileText.first().substringAfter(" ").substringBefore(":").toLong()
                val body = tileText.drop(1).map { it.toCharArray() }.toTypedArray()
                Tile(id, body)
            }
    }
}