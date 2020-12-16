package december

import AbstractDay

class Day16 : AbstractDay() {

    private val input = obtainInput()

    private val fields = mutableListOf<Field>()
    private lateinit var myTicket: Ticket
    private val otherTickets = mutableListOf<Ticket>()
    private val validTickets = mutableListOf<Ticket>()

    private fun setup() {
        var state = 0 // 0: Reading ranges, 1: My ticket, 2: Other tickets
        for (line in input) {
            if (line.isEmpty()) {
                state++
                continue
            }
            when (state) {
                0 -> {
                    val s = line.split(":", " or ", "-")
                    val name = s[0].trim()
                    val range1 = s[1].trim().toInt()..s[2].trim().toInt()
                    val range2 = s[3].trim().toInt()..s[4].trim().toInt()
                    fields.add(Field(name, range1, range2))
                }
                1 -> {
                    if (!line.trim().startsWith("your ticket")) {
                        val s = line.split(",")
                        myTicket = Ticket(s.map { it.trim().toInt() })
                    }
                }
                2 -> {
                    if (!line.trim().startsWith("nearby tickets")) {
                        val s = line.split(",")
                        otherTickets.add(Ticket(s.map { it.trim().toInt() }))
                    }
                }
                else -> error("Expected end of input.")
            }
        }
    }

    // Part 1: 25788
    override fun part1(): String {
        setup()
        val faulty = mutableListOf<Int>()
        otherTickets.forEach {
            var good = true
            it.fields.forEach { i ->
                if (!fields.any { f -> i in f.range1 || i in f.range2 }) {
                    faulty.add(i)
                    good = false
                }
            }
            if (good) validTickets.add(it) // For part 2
        }
        return faulty.sum().toString()
    }

    // Part 2: 3902565915559
    override fun part2(): String {
        val fieldNames: MutableList<MutableList<Field>> = mutableListOf()
        for (field in fields) fieldNames.add(fields.toMutableList())

        validTickets.forEach { t ->
            t.fields.indices.forEach { i ->
                val num = t.fields[i]
                val toDelete = mutableListOf<Field>()
                fieldNames[i].forEach { f ->
                    if (num !in f.range1 && num !in f.range2) toDelete.add(f)
                }
                fieldNames[i].removeAll(toDelete)
            }
        }
        val singleNames = Array(fieldNames.size) { "unknown" }
        while (true) {
            val single = fieldNames.find { it.size == 1 } ?: break
            val index = fieldNames.indexOf(single)
            fieldNames.forEach { if (it.size > 1) it.remove(single.first()) }
            singleNames[index] = single.first().name
            single.clear()
        }
        return myTicket.departureProduct(singleNames).toString()
    }


    class Field(val name: String, val range1: IntRange, val range2: IntRange)
    class Ticket(val fields: List<Int>) {

        fun departureProduct(fieldNames: Array<String>): Long {
            var product = 1L
            for (fieldNameID in fieldNames.indices) {
                if (fieldNames[fieldNameID].startsWith("departure")) product *= fields[fieldNameID]
            }
            return product
        }
    }
}