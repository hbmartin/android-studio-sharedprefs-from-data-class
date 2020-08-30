package me.haroldmartin.kotlindataclasstosharedprefs

data class DefaultableParameter(
    val name: String,
    val type: String,
    val default: String?
)

val DefaultableParameter.defaultOrFallback: String
    get() = default ?: FALLBACKS.getOrDefault(type, "")

private val FALLBACKS = mapOf(
    "Boolean" to "false",
    "Float" to "0",
    "Int" to "0",
    "Long" to "0",
    "String" to "\"\"",
    "Set<String>" to "setOf<String>()",
    "String?" to "null",
    "Set<String>?" to "null"
)
