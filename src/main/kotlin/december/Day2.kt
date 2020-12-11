package december

import AbstractDay

class Day2 : AbstractDay() {

    private val input = obtainInput(PassPolicyString::parse)

    // Part 1: 477
    override fun part1(): String = input.count { it.policy1 }.toString()

    // Part 2: 686
    override fun part2(): String = input.count { it.policy2 }.toString()

    private class PassPolicyString(
        private val min: Int,
        private val max: Int,
        private val c: Char,
        private val pass: String
    ) {

        /**
         * Policy 1 checks if a `pass` contains between `min` and `max` amount of `c` (inclusive)
         */
        val policy1 get() = pass.count { ch -> ch == c } in min..max

        /**
         * Policy 2 checks if the character at `pass[min-1]` xor `pass[max-1]` matches `c`
         */
        val policy2 get() = (pass[min - 1] == c) xor (pass[max - 1] == c)

        companion object {

            /**
             * Parses a line/string to a `PassPolicyString`
             *
             * @param s String of format `min-max c: pass`
             * @return A PassPolicyString(`min`, `max`, `c`, `pass`)
             */
            fun parse(s: String): PassPolicyString {
                val line = s.split(" ")
                val range = line[0].split("-")

                return PassPolicyString(range[0].toInt(), range[1].toInt(), line[1][0], line[2].trim())
            }
        }
    }
}
