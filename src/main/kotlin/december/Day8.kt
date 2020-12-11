package december

import AbstractDay

class Day8 : AbstractDay() {

    private val input = obtainInput()

    // Part 1: 2025
    override fun part1(): String {
        val itp = Interpreter(input)
        val set = HashSet<Int>()
        while (itp.ip !in set) {
            set.add(itp.ip)
            itp.step()
        }
        return itp.acc.toString()
    }

    // Part 2: 2001
    override fun part2(): String {
        val itp = Interpreter(input)
        for (i in input.indices) {
            if (input[i].split(" ")[0] == "acc") continue
            itp.reset()
            val set = HashSet<Int>()
            while (itp.ip !in set) {
                set.add(itp.ip)
                if (!itp.step(i == itp.ip)) return itp.acc.toString()
            }
        }
        return "Error: Could not fix the input by swapping nop<->jmp!"
    }

    class Interpreter(private val program: List<String>, var acc: Int = 0, var ip: Int = 0) {

        fun reset() {
            acc = 0
            ip = 0
        }

        fun step(debug: Boolean = false): Boolean {
            if (ip >= program.size) return false
            val ins = program[ip].split(" ")
            when {
                ins[0] == "acc" -> {
                    acc += ins[1].toInt()
                    ip++
                }
                ins[0] == "jmp" -> if (debug) ip++ else ip += ins[1].toInt()
                ins[0] == "nop" -> if (debug) ip += ins[1].toInt() else ip++
                else -> throw IllegalArgumentException("${ins[0]} at $ip is not a valid operation!")
            }
            return true
        }
    }
}