package ru.netology.nmedia

import org.junit.Assert.*
import org.junit.Test

class DiffMethodsTest {
    @Test
    fun shouldGetCurrentDateFormatted() {
        val result = DiffMethods.getCurrentDateFormatted()
        val pattern = """\d{1,2} [A-Za-z]+, \d{2}:\d{2}""".toRegex()
        assertTrue(
            "Expected format like '7 February, 11:30', but got: $result",
            pattern.matches(result)
        )
    }
}