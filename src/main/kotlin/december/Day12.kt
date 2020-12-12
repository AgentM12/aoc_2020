package december

import AbstractDay
import kotlin.math.abs

class Day12 : AbstractDay() {

    enum class Dir(val degrees: Int, val dx: Int, val dy: Int, val c: Char) {
        NORTH(0, 0, -1, 'N'),
        EAST(90, 1, 0, 'E'),
        SOUTH(180, 0, 1, 'S'),
        WEST(270, -1, 0, 'W');

        companion object {
            operator fun get(degrees: Int): Dir {
                val ordinal = ((degrees % 360) + 360) % 360 / 90
                @Suppress("RemoveRedundantQualifierName") // Wrong IntelliJ warning
                return Dir.values()[ordinal]
            }

            fun fromChar(c: Char): Dir? = values().find { it.c == c }
        }
    }

    private val input = obtainInput()

    // Part 1: 1645
    override fun part1(): String = input.fold(Ship(), { s, ins -> s.move(ins) }).manhattanDistance().toString()

    // Part 2: 35292
    override fun part2(): String = input.fold(Ship(), { s, ins -> s.moveWaypoint(ins) }).manhattanDistance().toString()

    class Ship(
        private var facing: Dir = Dir.EAST,
        private var x: Int = 0,
        private var y: Int = 0,
        private val waypoint: Waypoint = Waypoint()
    ) {

        fun manhattanDistance() = abs(this.x) + abs(this.y)

        fun move(ins: String): Ship {
            val op = ins[0]
            val v = ins.substring(1).toInt()
            when (op) {
                'R', 'L' -> facing = rotate(if (op == 'R') v else -v)
                'F' -> {
                    x += facing.dx * v
                    y += facing.dy * v
                }
                else -> {
                    val d = Dir.fromChar(op)!!
                    x += d.dx * v
                    y += d.dy * v
                }
            }
            return this
        }

        fun moveWaypoint(ins: String): Ship {
            val op = ins[0]
            val v = ins.substring(1).toInt()
            when (op) {
                'R', 'L' -> waypoint.rotate(if (op == 'R') v else -v)
                'F' -> {
                    x += waypoint.dx * v
                    y += waypoint.dy * v
                }
                else -> {
                    val d = Dir.fromChar(op)!!
                    waypoint.dx += d.dx * v
                    waypoint.dy += d.dy * v
                }
            }
            return this
        }

        private fun rotate(degrees: Int) = Dir[facing.degrees + degrees]

        class Waypoint {
            var dx = 10
            var dy = -1

            fun rotate(degrees: Int) {
                when (((degrees % 360) + 360) % 360) {
                    90 -> {
                        val t = dy
                        dy = dx
                        dx = -t
                    }
                    180 -> {
                        dx = -dx
                        dy = -dy
                    }
                    270 -> {
                        val t = dx
                        dx = dy
                        dy = -t
                    }
                }
            }
        }
    }
}
