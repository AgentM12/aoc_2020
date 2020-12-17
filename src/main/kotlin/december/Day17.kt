package december

import AbstractDay

typealias Grid4D = MutableMap<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, Day17.Cube>>>>

class Day17 : AbstractDay() {

    private val input = obtainInput()

    // Part 1: 315
    override fun part1(): String = simulate(6).toString()

    // Part 2: 1520
    override fun part2(): String = simulate(6, false).toString()

    private fun simulate(cycles: Int, excludeW: Boolean = true): Int {

        val cubes = mutableSetOf<Cube>()
        val unstable = mutableSetOf<Cube>()
        val world = mutableMapOf<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, Cube>>>>()

        input.indices.forEach { y ->
            input[y].indices.forEach { x ->
                if (input[y][x] == '#') {
                    val cube = Cube(x, y, 0, 0, CubeState.STABLE)
                    cubes.add(cube)
                    cube.register(world)
                }
            }
        }

        for (c in 1..cycles) {
            cubes.forEach { it.neighbors(world, excludeW, unstable) }
            for (cube in unstable) {
                when (cube.state) {
                    CubeState.NEW -> cubes.add(cube.also { it.state = CubeState.STABLE })
                    CubeState.FADING -> cubes.remove(cube)
                    else -> error("Invalid state.")
                }
            }
            world.clear()
            unstable.clear()
            cubes.forEach { it.register(world) }
        }
        return cubes.size
    }

    enum class CubeState {
        NEW, // Created in this cycle
        STABLE, // Unchanged
        FADING, // Deleted in this cycle
        EMPTY, // Checked already to remain empty
    }

    class Cube(val x: Int, val y: Int, val z: Int, val w: Int, var state: CubeState) {

        fun register(world: Grid4D) {
            val yMap = world.getOrPut(x) { mutableMapOf() }
            val zMap = yMap.getOrPut(y) { mutableMapOf() }
            val wMap = zMap.getOrPut(z) { mutableMapOf() }
            wMap[w] = this
        }

        fun neighbors(world: Grid4D, excludeW: Boolean = true, unstable: MutableSet<Cube>, stub: Boolean = false) {
            var count = 0
            for (xx in (x - 1)..(x + 1)) {
                for (yy in (y - 1)..(y + 1)) {
                    for (zz in (z - 1)..(z + 1)) {
                        for (ww in (w - 1)..(w + 1)) {
                            if (excludeW && ww != w) continue
                            if (xx == x && yy == y && zz == z && ww == w) continue
                            val neighbor = world[xx]?.get(yy)?.get(zz)?.get(ww)
                            if (neighbor != null && neighbor.state != CubeState.EMPTY) {
                                if (neighbor.state == CubeState.STABLE || neighbor.state == CubeState.FADING) count++
                            } else {
                                val cube = neighbor ?: Cube(xx, yy, zz, ww, CubeState.EMPTY).also { it.register(world) }
                                if (!stub) cube.neighbors(world, excludeW, unstable, true)
                            }
                        }
                    }
                }
            }

            if (state == CubeState.STABLE && count !in 2..3) state = CubeState.FADING
            else if (state == CubeState.EMPTY && count == 3) state = CubeState.NEW
            if (state == CubeState.FADING || state == CubeState.NEW) unstable.add(this)
        }
    }
}