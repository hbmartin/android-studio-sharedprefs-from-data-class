package me.haroldmartin.kotlindataclasstosharedprefs

internal fun String.keyName(className: String) =
    "${className.camelToSnakeCase()}_${this.camelToSnakeCase()}".toUpperCase()

internal fun List<DefaultableParameter>.toWriters(className: String): String =
    mapNotNull {
        toWriter(className, it.type, it.name)
    }.joinToString(separator = "\n", postfix = "\n")

private fun toWriter(className: String, type: String, name: String): String? {
    if (!isEligibleTypeForSharedPrefs(type)) {
        return null
    }

    return ".put${safeTypeName(type)}(${name.keyName(className)}, ${className.lowerFirst()}.$name)"
}

internal fun List<DefaultableParameter>.toReaders(className: String): String =
    mapNotNull {
        toReader(className, it.type, it.name, it.defaultOrFallback)
    }.joinToString(separator = ",\n", postfix = "\n")

private fun toReader(className: String, type: String, name: String, defaultValue: String): String? {
    if (!isEligibleTypeForSharedPrefs(type)) {
        return null
    }

    val method = "sharedPreferences.get${safeTypeName(type)}"
    val signature = "(${name.keyName(className)}, $defaultValue)${castIfNecessary(type)}"
    return "$name = $method$signature"
}

private val castRequiredForNonNullTypes = setOf("String", "Set<String>", "MutableSet<String>")
private fun castIfNecessary(type: String) =
    if (castRequiredForNonNullTypes.contains(type)) " as $type" else ""

private fun safeTypeName(type: String) = type.replace("?", "")
    .replace("MutableSet<String>", "StringSet")
    .replace("Set<String>", "StringSet")

private val eligibleTypes = setOf(
    "Boolean", "Float", "Int", "Long", "String", "String?",
    "Set<String>", "Set<String>?", "MutableSet<String>", "MutableSet<String>?"
)
fun isEligibleTypeForSharedPrefs(type: String) = eligibleTypes.contains(type)

internal fun String.lowerFirst(): String {
    val c = toCharArray()
    c[0] = Character.toLowerCase(c[0])
    return String(c)
}

private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
private fun String.camelToSnakeCase(): String =
    camelRegex.replace(this) { "_${it.value}" }.toLowerCase()
