package december

import AbstractDay
import java.util.*


class Day18 : AbstractDay() {

    private val input = obtainInput()

    // Part 1: 3348222486398
    override fun part1(): String = input.sumOf { evaluate(rpn(tokenize(it))) }.toString()

    // Part 2: 43423343619505
    override fun part2(): String = input.sumOf { evaluate(rpn(tokenize(it), true)) }.toString()

    private fun tokenize(line: String): List<Token> {
        val list = mutableListOf<Token>()
        for (c in line) {
            when (c) {
                ' ' -> continue
                '*' -> list.add(Mul())
                '+' -> list.add(Plus())
                '(' -> list.add(LParent())
                ')' -> list.add(RParent())
                else -> list.add(Num((c - '0').toLong()))
            }
        }
        return list
    }

    private fun rpn(list: List<Token>, priorityPrecedence: Boolean = false): List<Token> {
        val stack = LinkedList<Token>()
        val queue = LinkedList<Token>()
        for (tok in list) when (tok) {
            is Num -> queue.offer(tok)
            is Mul, is Plus -> {
                var top = stack.peek()
                while ((top is Plus || top is Mul) && !priorityPrecedence || top is Plus && tok is Mul) {
                    queue.offer(stack.pop())
                    top = stack.peek()
                }
                stack.push(tok)
            }
            is LParent -> stack.push(tok)
            is RParent -> {
                while (stack.peek() !is LParent) queue.offer(stack.pop())
                stack.pop()
            }
        }
        while (stack.isNotEmpty()) queue.offer(stack.pop())
        return queue
    }

    private fun evaluate(list: List<Token>): Long {
        val stack = LinkedList<Long>()
        for (tok in list) {
            when (tok) {
                is Num -> stack.push(tok.v)
                is Mul -> stack.push(stack.pop() * stack.pop())
                is Plus -> stack.push(stack.pop() + stack.pop())
                else -> error("Unexpected value: $tok")
            }
        }
        return stack.pop()
    }

    abstract class Token
    class Num(val v: Long) : Token() {
        override fun toString(): String = v.toString()
    }

    class Plus : Token() {
        override fun toString(): String = "+"
    }

    class Mul : Token() {
        override fun toString(): String = "*"
    }

    class LParent : Token() {
        override fun toString(): String = "("
    }

    class RParent : Token() {
        override fun toString(): String = ")"
    }

}