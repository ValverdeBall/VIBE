package com.valverdeball.vibe

class TextBuffer(initialText: String = "") {

    private val lines: MutableList<StringBuilder> = initialText
    .split("\n")
    .map { StringBuilder(it) }
    .toMutableList()

    val lineCount: Int
    get() = lines.size

    fun getLine(index: Int): String {
        return lines[index].toString()
    }

    fun getAllLines(): List<String> {
        return lines.map { it.toString() }
    }

    fun fullText(): String {
        return lines.joinToString("\n")
    }

    /**
     * inserts a single character at (line, col)
     * return the new cursor position ar Pair(line, col)
     */
    fun insertChar(line: Int, col: Int, char:Char): Pair<Int, Int> {
        lines[line].insert(col, char)
        return Pair(line, col + 1)
    }

    /**
     * splits the line at (line, col) into two lines, i.e enter,
     * returns the new position
     */
    fun insertNewLine(line: Int, col: Int): Pair<Int, Int> {
        val current = lines[line]
        val before = current.substring(0, col)
        val after = current.substring(col)

        lines[line] = StringBuilder(before)
        lines.add(line + 1, StringBuilder(after))

        return Pair(line + 1, 0)
    }

    /**
     * deletes the character immediately before (line, col), i.e backspace,
     * handles merging with the previous line if col is 0
     * and returns the new cursor position
     */
    fun deleteBefore(line: Int, col: Int): Pair<Int, Int> {
        if (col > 0) {
            lines[line].deleteCharAt(col - 1)
            return Pair(line, col - 1)
        }

        if (line > 0) {
            val previousLength = lines[line - 1].length
            lines[line - 1].append(lines[line])
            lines.removeAt(line)
            return Pair(line - 1, previousLength)
        }

        return Pair(line, col) // nothin to delete, start of file
    }

    fun lineLength(line: Int): Int {
        return lines[line].length
    }
}
