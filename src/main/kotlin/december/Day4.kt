package december

import AbstractDay

class Day4 : AbstractDay() {

    private var input = Passport.parsePassports(obtainInput())

    // Part 1: 256
    override fun part1(): String = input.count { it.isValid(false) }.toString()

    // Part 2: 198
    override fun part2(): String = input.count { it.isValid(true) }.toString()

    class Passport(val dict: MutableMap<String, String> = HashMap(12)) {

        /**
         * Each required `key` in `dict` maps to a predicate validating the corresponding `value`.
         */
        private val predicateMap = mapOf(
            "byr" to { s: String -> val i = s.toIntOrNull(); i != null && i in 1920..2002 }, // Birth Year
            "iyr" to { s: String -> val i = s.toIntOrNull(); i != null && i in 2010..2020 }, // Issue Year
            "eyr" to { s: String -> val i = s.toIntOrNull(); i != null && i in (2020..2030) }, // Expiration Year
            "hgt" to { s: String ->
                val i = s.dropLast(2).toIntOrNull()
                val u = s.drop(s.length - 2)
                i != null && (u == "cm" && i in 150..193 || u == "in" && i in 59..76)
            }, // Height
            "hcl" to { s: String -> s matches Regex("^#[0-9a-f]{6}$") }, // Hair Color
            "ecl" to { s: String -> s matches Regex("^(?:amb|blu|brn|gry|grn|hzl|oth)$") }, // Eye Color
            "pid" to { s: String -> s matches Regex("\\d{9}") }  // Passport ID
        )

        /**
         * **Non-strict validation:** Check if the required keys are present in `dict`.
         *
         * **Strict validation:** Additionally to the above, checks if the `values` match the predicate for that `key`.
         */
        fun isValid(strict: Boolean) =
            predicateMap.keys.all { dict[it] != null && (!strict || predicateMap[it]!!(dict[it]!!)) }

        internal companion object {
            /**
             * Parses a raw passport batch file into a list of passports.
             */
            fun parsePassports(strings: List<String>): List<Passport> {
                val passports = ArrayList<Passport>()
                var currentPassport = Passport()
                for (str in strings) {
                    if (str.isBlank()) { // Separate on blank lines. (allows empty passports)
                        passports.add(currentPassport)
                        currentPassport = Passport()
                        continue
                    }
                    val keys = str.split(" ") // Each k:v pair is separated by whitespace
                    keys.forEach {
                        val kv = it.split(":")
                        currentPassport.dict[kv[0]] = kv[1]
                    }
                }
                passports.add(currentPassport)
                return passports
            }
        }
    }


}

