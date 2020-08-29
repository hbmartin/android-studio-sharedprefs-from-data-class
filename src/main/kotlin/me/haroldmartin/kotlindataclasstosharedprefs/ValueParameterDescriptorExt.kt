package me.haroldmartin.kotlindataclasstosharedprefs

import org.jetbrains.kotlin.caches.resolve.KotlinCacheService
import org.jetbrains.kotlin.descriptors.Named
import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.resolve.lazy.ResolveSession

internal fun Named.keyName(className: String?) = "${className}_${name}"

internal fun List<ValueParameterDescriptor>.toWriters(className: String?): String =
    this.mapNotNull { it.toWriter(className) }.joinToString(separator = "\n", postfix = "\n")

private fun ValueParameterDescriptor.toWriter(className: String?): String? {
    if (!isEligibleTypeForSharedPrefs()) { return null }

    return ".put$safeTypeName(${keyName(className)}, ${className.lowerFirst()}.${name})"
}

internal fun List<ValueParameterDescriptor>.toReaders(className: String?): String =
    this.mapNotNull { it.toReader(className) }.joinToString(separator = ",\n", postfix = "\n")

private fun ValueParameterDescriptor.toReader(className: String?): String? {
    if (!isEligibleTypeForSharedPrefs()) { return null }

    return "$name = sharedPreferences.get$safeTypeName(${keyName(className)}, ${getDefaultValue()})${castIfNecessary()}"
}

private fun ValueParameterDescriptor.getDefaultValue(): String {
    // todo find in parameter somehow?

    return when (type.toString()) {
        "Boolean" -> "false"
        "Float", "Int", "Long" -> "0"
        "String" -> "\"\""
        "Set<String>" -> "setOf<String>()"
        "String?", "Set<String>?" -> "null"
        else -> ""
    }
}

private fun ValueParameterDescriptor.castIfNecessary(): String {
    val cast = when (type.toString()) {
        "String" -> "String"
        "Set<String>" -> "Set<String>"
        "Set<String>?" -> "Set<String>?"
        else -> ""
    }
    return if (cast.isEmpty()) cast else " as $cast"
}

val ValueParameterDescriptor.safeTypeName
    get() = this.type.toString()
        .replace("?", "")
        .replace("Set<String>", "StringSet")


private val eligibleTypes = setOf("Boolean", "Float", "Int", "Long", "String", "String?", "Set<String>", "Set<String>?")
fun ValueParameterDescriptor.isEligibleTypeForSharedPrefs() =
    eligibleTypes.contains(type.toString())

fun KtClass.findParams(): List<ValueParameterDescriptor> {
    return KotlinCacheService.getInstance(this.project)
        .getResolutionFacade(listOf(this))
        .getFrontendService(ResolveSession::class.java)
        .getClassDescriptor(this, NoLookupLocation.FROM_IDE)
        .unsubstitutedPrimaryConstructor
        ?.valueParameters
        ?: emptyList()
}

internal fun String?.lowerFirst(default: String = "model"): String {
    if (this == null) return default
    val c = toCharArray();
    c[0] = Character.toLowerCase(c[0]);
    return String(c);
}
