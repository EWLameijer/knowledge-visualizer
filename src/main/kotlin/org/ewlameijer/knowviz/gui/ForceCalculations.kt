package org.ewlameijer.knowviz.gui

import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

fun Iterable<RepellingForce>.sum(): RepellingForce =
    this.reduce { acc, f -> RepellingForce(acc.dx + f.dx, acc.dy + f.dy) }

data class Coordinate(val x: Int, val y: Int) {
    fun directionTo(other: Coordinate) = CoordinateDifference(other.x - x, other.y - y)

    operator fun plus(other: CoordinateDifference) = Coordinate(x + other.dx, y + other.dy)
}

data class CoordinateDifference(val dx: Int, val dy: Int) {
    companion object {
        fun from(v: Vector) = CoordinateDifference(v.dx.roundToInt(), v.dy.roundToInt())
    }

    fun toVector() = Vector(dx.toDouble(), dy.toDouble())
}

// vector has direction and size
open class Vector(dx: Double, dy: Double) {
    val size = sqrt(dx * dx + dy * dy)

    val direction =
        if (size == 0.0) Direction.none
        else Direction(dx / size, dy / size)

    operator fun component1() = size

    operator fun component2() = direction

    val dx = size * direction.dx

    val dy = size * direction.dy
}

data class Direction(val dx: Double, val dy: Double) {
    companion object {
        private val rand = Random.Default

        val none = Direction(0.0, 0.0)

        fun random(): Direction {
            val degree = rand.nextDouble(2 * Math.PI)
            return Direction(cos(degree), sin(degree))
        }
    }

    operator fun times(factor: Double) = Vector(dx * factor, dy * factor)
}

fun Direction.toVector() = Vector(dx, dy)

class RepellingForce(dx: Double, dy: Double) : Vector(dx, dy) {
    companion object {
        fun from(repellerPosition: Coordinate, repelledPosition: Coordinate, strength: Double): RepellingForce {
            val vectorToOther = repellerPosition.directionTo(repelledPosition).toVector()
            val (distance, direction) = safeVectorForForceCalculation(vectorToOther)
            val forceSize = strength / (distance * distance)
            val totalForce = direction * forceSize
            return RepellingForce(totalForce.dx, totalForce.dy)
        }

        // ensure that if points overlap, their repulsion is not infinite, and productive (in a concrete direction)
        private fun safeVectorForForceCalculation(v: Vector): Vector =
            if (v.direction == Direction.none) Direction.random().toVector()
            else v
    }
}