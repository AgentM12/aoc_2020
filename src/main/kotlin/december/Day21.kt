package december

import AbstractDay
import java.util.*

class Day21 : AbstractDay() {

    private val input = obtainInput { line ->
        val (ingredients, allergens, _) = line.split("(", ")")
        val ingredientsSplit = ingredients.trim().split(" ")
        val ingredientsList = ingredientsSplit.map { Ingredient(it.trim()) }
        val allergensSplit = allergens.trim().split(",")
        val allergensList = allergensSplit.map { Allergen(it.replace("contains", "").trim()) }
        ingredientsList to allergensList
    }

    private lateinit var allergensMap: Map<Ingredient, Allergen>

    private fun resolveAllergens(food: List<Pair<MutableList<Ingredient>, MutableList<Allergen>>>): Map<Ingredient, Allergen> {
        val allergens = mutableSetOf<Allergen>()
        food.forEach { (_, a) -> allergens.addAll(a) }
        val allergenQueue = LinkedList(allergens)
        var allergenCandidatesMap = mutableMapOf<Allergen, MutableList<Ingredient>>()
        val allergenMap = mutableMapOf<Ingredient, Allergen>()

        allergens.forEach { allergen ->
            val ingredientListSetContainingAllergen =
                food.fold<Pair<MutableList<Ingredient>, MutableList<Allergen>>, MutableSet<List<Ingredient>>>(
                    mutableSetOf()
                ) { s, (i, a) -> if (allergen in a) s.also { it.add(i) } else s }
            allergenCandidatesMap[allergen] = ingredientListSetContainingAllergen.fold(
                setOf(*ingredientListSetContainingAllergen.first().toTypedArray())
            ) { s, i -> s.intersect(i) }.toMutableList()
        }

        while (allergenQueue.isNotEmpty()) {
            val allergen = allergenQueue.poll()
            if (allergenCandidatesMap[allergen]?.size == 1) {
                val i = allergenCandidatesMap[allergen]!!.first()
                allergenMap[i] = allergen
                allergenCandidatesMap.values.forEach { it.remove(i) }
            } else {
                allergenCandidatesMap = allergenCandidatesMap.filter { it.value.size > 0 }.toMutableMap()
                allergenQueue.offer(allergen)
            }
        }
        return allergenMap
    }

    // Part 1: 2287
    override fun part1(): String {
        allergensMap = resolveAllergens(input.map { (i, a) -> Pair(i.toMutableList(), a.toMutableList()) })
        val ingredients = input.fold(mutableListOf<Ingredient>()) { l, (i, _) ->
            i.forEach { if (it !in allergensMap) l.add(it) }
            l
        }
        return ingredients.size.toString()
    }

    @Suppress("SpellCheckingInspection")
    // Part 2: fntg,gtqfrp,xlvrggj,rlsr,xpbxbv,jtjtrd,fvjkp,zhszc
    override fun part2(): String = allergensMap.toList().sortedBy { (_, v) -> v.name }.joinToString(",") { (i, _) ->
        i.name
    }

    class Allergen(val name: String) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return name == (other as Allergen).name
        }

        override fun hashCode(): Int = name.hashCode()
        override fun toString(): String = name
    }

    class Ingredient(val name: String) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return name == (other as Ingredient).name
        }

        override fun hashCode(): Int = name.hashCode()
        override fun toString(): String = name
    }
}