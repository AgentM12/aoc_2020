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
        var world = mutableMapOf<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, Cube>>>>()

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
            val unstable = mutableSetOf<Cube>()
            for (cube in cubes) {
                unstable.addAll(cube.neighbors(world, excludeW))
            }
            unstable.addAll(cubes.filter { it.state != CubeState.STABLE })
//            printGrid(unstable.filter { it.state != CubeState.EMPTY }.toSet())
            for (cube in unstable) {
                when (cube.state) {
                    CubeState.NEW -> {
                        cube.state = CubeState.STABLE
                        cubes.add(cube)
                    }
                    CubeState.FADING, CubeState.EMPTY -> {
//                        cube.deregister(world)
                        cubes.remove(cube)
                    }
                    else -> error("Invalid state.")
                }
            }
//            printGrid(cubes)
            world = mutableMapOf()
            cubes.forEach { it.register(world) }
        }
        return cubes.size
    }

//    private fun printGrid(cubes: Set<Cube>) {
//        val maxX = cubes.maxByOrNull { it.x }?.x!!
//        val minX = cubes.minByOrNull { it.x }?.x!!
//        val maxY = cubes.maxByOrNull { it.y }?.y!!
//        val minY = cubes.minByOrNull { it.y }?.y!!
//        val maxZ = cubes.maxByOrNull { it.z }?.z!!
//        val minZ = cubes.minByOrNull { it.z }?.z!!
//        val maxW = cubes.maxByOrNull { it.w }?.w!!
//        val minW = cubes.minByOrNull { it.w }?.w!!
//        println("min($minX, $minY, $minZ, $minW)")
//        for (w in minW..maxW) {
//            for (z in minZ..maxZ) {
//                println("z=$z, w=$w")
//                for (y in minY..maxY) {
//                    for (x in minX..maxX) {
//                        val cube = cubes.find { it.x == x && it.y == y && it.z == z && it.w == w }
//                        print(if (cube == null || cube.state == CubeState.EMPTY) '.' else if (cube.state == CubeState.FADING) 'O' else if (cube.state == CubeState.NEW) 'X' else '#')
//                    }
//                    println()
//                }
//            }
//        }
//    }

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

        fun neighbors(world: Grid4D,  excludeW: Boolean = true, stub: Boolean = false): Set<Cube> {
            val unstable = mutableSetOf<Cube>()
            var count = 0
            for (xx in (x - 1)..(x + 1)) {
                for (yy in (y - 1)..(y + 1)) {
                    for (zz in (z - 1)..(z + 1)) {
                        for (ww in (w - 1)..(w + 1)) {
                            if (excludeW && ww != w) continue
                            if (xx == x && yy == y && zz == z && ww == w) continue
                            val neighbor = world[xx]?.get(yy)?.get(zz)?.get(ww)
                            if (neighbor != null && neighbor.state != CubeState.EMPTY) {
                                when (neighbor.state) {
                                    CubeState.STABLE, CubeState.FADING -> count++
                                    else -> {
                                    }
                                }
                            } else {
                                val cube = neighbor ?: Cube(xx, yy, zz, ww, CubeState.EMPTY).also { it.register(world) }
                                unstable.add(cube)
                                if (!stub) unstable.addAll(cube.neighbors(world, excludeW, true))
                            }
                        }
                    }
                }
            }

            if (state == CubeState.STABLE && count !in 2..3) state = CubeState.FADING
            else if (state == CubeState.EMPTY && count == 3) state = CubeState.NEW
            if (state == CubeState.FADING || state == CubeState.NEW) unstable.add(this)
            return unstable
        }

//        fun deregister(world: Grid4D) {
//            val yMap = world[x]
//            val zMap = world[y]
//            val wMap = world[z]
//            wMap?.remove(w)
//            if (wMap.isNullOrEmpty()) {
//                zMap?.remove(z)
//                if (zMap.isNullOrEmpty()) {
//                    yMap?.remove(y)
//                    if (yMap.isNullOrEmpty()) {
//                        world.remove(x)
//                    }
//                }
//            }
//        }

//        override fun toString(): String {
//            return "Cube($x, $y, $z: $state)"
//        }
    }

}