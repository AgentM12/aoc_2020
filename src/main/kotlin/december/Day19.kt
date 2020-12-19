package december

import AbstractDay

class Day19 : AbstractDay() {

    private val input = obtainInput()

    private lateinit var rules: List<Rule>
    private lateinit var messages: List<String>

    private fun setup(inputList: List<String>) {
        var state = 0
        val stringRules = mutableListOf<String>()
        val messageList = mutableListOf<String>()
        for (line in inputList) {
            if (line.isEmpty()) {
                state = 1
                continue
            }
            if (state == 0) {
                stringRules.add(line.trim())
            } else {
                messageList.add(line.trim())
            }
        }
        val stringRulesSorted = stringRules.sortedBy { it.split(":")[0].toInt() }
        messages = messageList
        val rp = RuleParser(stringRulesSorted)
        rp.parse()
        rules = rp.ruleSet.values.sortedBy { it.id }

        // PIRATE CODE
        rules1 = stringRules.map { it.split(":").let { (id, rule) -> id.toInt() to rule.trim() } }.toMap()
        rules2 = rules1 + listOf(8 to "42 | 42 8", 11 to "42 31 | 42 11 31").toMap()
    }

    // Part 1: 160
    override fun part1(): String = setup(input).run { messages.count { rules[0].exactMatch(it) }.toString() }

    // Part 2: >212
//    override fun part2(): String =
//        setup(patched(input, rules.size)).run { messages.count { rules[0].exactMatch(it) }.toString() }

//    private fun patched(inputList: List<String>, startIndex: Int): List<String> {
//        var index = startIndex
//        val extra = 10
//        val newList = mutableListOf<String>()
//        var state = 0
//        inputList.forEach {
//            if (it.isEmpty()) state = 1
//            if (state == 0)
//                when {
//                    it.trim().startsWith("8:") -> {
//                        newList.add("8: 42 | 42 $index")
//                        for (i in 1..extra) newList.add("$index: 42 | 42 ${++index}")
//                        newList.add("${index++}: 42")
//                    }
//                    it.trim().startsWith("11:") -> {
//                        newList.add("11: 42 31 | 42 $index 31")
//                        for (i in 1..extra) newList.add("$index: 42 31 | 42 ${++index} 31")
//                        newList.add("${index++}: 42 31")
//                    }
//                    else -> newList.add(it)
//                }
//            else newList.add(it)
//        }
//        return newList
//    }

    abstract class Rule(val id: Int) {
        abstract fun match(s: String): Int
        abstract fun repr(): String
        fun exactMatch(s: String): Boolean = match(s) == s.length
    }

    class End(id: Int, val c: Char) : Rule(id) {
        override fun match(s: String): Int = if (s.isEmpty()) -1 else if (s.first() == c) 1 else 0

        override fun toString(): String = "\"$c\""

        override fun repr(): String = "$id"
    }

    class Choice(id: Int, val list: List<Rule>) : Rule(id) {
        override fun match(s: String): Int = list.find { it.match(s) > 0 }?.match(s) ?: 0

        override fun toString(): String = list.joinToString(" | ")

        override fun repr(): String = list.joinToString(" | ") { if (it.id == -1) it.repr() else "${it.id}" }
    }

    class Seq(id: Int, val list: List<Rule>) : Rule(id) {
        override fun match(s: String): Int = list.fold(0) { acc, rule ->
            val res = rule.match(s.substring(acc))
            if (res < 1) return 0
            acc + res
        }

        override fun toString(): String = list.joinToString(" ")

        override fun repr(): String = list.joinToString(" ") { if (it.id == -1) it.repr() else "${it.id}" }
    }

    class RuleParser(private val stringRulesSorted: List<String>, val ruleSet: MutableMap<Int, Rule> = mutableMapOf()) {
        fun parse() {
            for (s in stringRulesSorted) {
                val (ids, rest) = s.split(":")
                val id = ids.trim().toInt()
                if (id in ruleSet) continue

                val tokens = rest.trim().split(" ")
                ruleSet[id] = parse(id, tokens)
            }
        }

        private fun getById(id: Int): Rule {
            ruleSet[id] = ruleSet.getOrElse(id) { parse(id, stringRulesSorted[id].split(":")[1].trim().split(" ")) }
            return ruleSet[id]!!
        }

        private fun parse(id: Int, tokens: List<String>): Rule {
            if (tokens[0].trim().startsWith("\"")) return End(id, tokens[0].split("\"")[1][0])

            val choice = mutableListOf<Rule>()
            var seq = mutableListOf<Rule>()
            for (tok in tokens) {
                when (tok) {
                    "|" -> {
                        choice.add(Seq(-1, seq))
                        seq = mutableListOf()
                    }
                    else -> {
                        val tid = tok.toInt()
                        seq.add(getById(tid))
                    }
                }
            }
            return when {
                choice.isNotEmpty() -> {
                    choice.add(Seq(-1, seq))
                    choice.map { c -> if (c is Seq && c.list.size == 1) c.list[0] else c }
                    Choice(id, choice)
                }
                else -> Seq(id, seq)
            }
        }
    }

    // PIRATED from: https://github.com/nielsutrecht/adventofcode/blob/master/src/main/kotlin/com/nibado/projects/advent/y2020/Day19.kt
    private lateinit var rules1: Map<Int, String>
    private lateinit var rules2: Map<Int, String>

    // Part 2: 357
    override fun part2() = messages.count { isMatch(rules2, it, listOf(0)) }.toString()

    private fun isMatch(ruleMap: Map<Int, String>, line: CharSequence, rules: List<Int>): Boolean {
        if (line.isEmpty()) {
            return rules.isEmpty()
        } else if (rules.isEmpty()) {
            return false
        }
        return ruleMap.getValue(rules[0]).let { rule ->
            if (rule[1] in 'a'..'z') {
                if (line.startsWith(rule[1])) {
                    isMatch(ruleMap, line.drop(1), rules.drop(1))
                } else false
            } else rule.split(" | ").any {
                isMatch(ruleMap, line, it.split(" ").map(String::toInt) + rules.drop(1))
            }
        }
    }
}