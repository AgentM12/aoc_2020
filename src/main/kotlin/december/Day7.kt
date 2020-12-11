package december

import AbstractDay
import december.Day7.Bag.Companion.parse
import sun.misc.Queue

class Day7 : AbstractDay() {

    private var input = obtainInput { parse(it) }

    // Part 1: 179
    override fun part1(): String = findAllParents(Bag("shiny gold")).toString()

    private fun findAllParents(bag: Bag): Int {
        val traversed: MutableSet<Bag> = HashSet()
        val includedBags: MutableSet<Bag> = HashSet()
        val queue: Queue<Bag> = Queue()
        queue.enqueue(bag)
        while (!queue.isEmpty) {
            val cur = queue.dequeue()
            if (cur !in traversed) {
                traversed.add(cur)
                val hasChild = input.filter { it.children.any { c -> c.color == cur.color } }
                for (parent in hasChild) {
                    if (parent !in traversed) {
                        includedBags.add(parent)
                        queue.enqueue(parent)
                    }
                }
            }
        }
        return includedBags.size
    }

    // Part 2: 18925
    override fun part2(): String = countBags(Bag("shiny gold")).toString()

    private fun countBags(bagKey: Bag): Int {
        val bagRef =
            input.find { it.color == bagKey.color } ?: throw NullPointerException("${bagKey.color} bag was not found!")
        return bagRef.children.fold(0, { acc, bag -> acc + bag.amount + bag.amount * countBags(bag) })
    }

    class Bag(var color: String, var amount: Int = 1, val children: MutableSet<Bag> = HashSet()) {

        override fun toString(): String =
            "($color -> ${children.fold("", { s, b -> s + "${b.amount}x ${b.color}, " }).dropLast(2)})"

        companion object {
            fun parse(s: String): Bag {
                val splitString = s.split(" ")
                val color = splitString[0] + " " + splitString[1]
                val more = splitString[4]
                val bag = Bag(color)
                if (more != "no") {
                    var cColor = ""
                    var cCount = 0
                    for (i in 4 until splitString.size) {
                        val c = splitString[i]
                        when {
                            i % 4 == 0 -> cCount = c.toInt()
                            c.startsWith("bag") -> {
                                val child = Bag(cColor, cCount)
                                bag.children.add(child)
                                cColor = ""
                            }
                            else -> cColor += if (cColor.isBlank()) c else " $c"
                        }
                    }
                }
                return bag
            }
        }
    }
}