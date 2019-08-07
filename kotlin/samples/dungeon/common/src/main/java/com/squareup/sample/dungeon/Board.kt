package com.squareup.sample.dungeon

import kotlin.streams.toList

private const val WALL = "🌳"
private const val SPACE = "\u00A0"

data class Board(
  val width: Int,
  val height: Int,
  val cells: List<BoardCell>
) {
  data class Location(
    val x: Int,
    val y: Int
  ) {
    fun up(): Location = copy(y = y - 1)
    fun down(): Location = copy(y = y + 1)
    fun left(): Location = copy(x = x - 1)
    fun right(): Location = copy(x = x + 1)

    override fun toString(): String = "($x, $y)"
  }

  init {
    require(cells.size == width * height) {
      "Cells must be $width×$height=${width * height}, but was ${cells.size}"
    }
  }

  fun cellIndexOf(
    x: Int,
    y: Int
  ): Int = (y * width) + x

  fun withOverlay(players: Map<Location, BoardCell>): Board {
    val playersByIndex = players.mapKeys { (location, _) -> cellIndexOf(location.x, location.y) }
    return copy(cells = cells.mapIndexed { index, cell ->
      playersByIndex[index] ?: cell
    })
  }

  override fun toString(): String {
    return cells.asSequence()
        .chunked(width)
        .joinToString(separator = "\n") {
          it.joinToString(separator = "")
        }
  }

  companion object {
    val EMPTY = ((WALL.repeat(16)) +
        (WALL + SPACE.repeat(14) + WALL).repeat(14) +
        (WALL.repeat(16)))
        .codePoints()
        .toList()
        .map(::BoardCell)
  }
}
