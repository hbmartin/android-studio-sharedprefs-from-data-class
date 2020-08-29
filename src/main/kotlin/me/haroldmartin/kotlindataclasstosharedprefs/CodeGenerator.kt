package me.haroldmartin.kotlindataclasstosharedprefs

import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtPsiFactory

private const val CLASS_SUFFIX = "SharedPreferencesRepository"

class CodeGenerator(
    private val ktClass: KtClass
) {
    private val params = ktClass.findParams()
    private val factory = KtPsiFactory(ktClass.project)

    fun generate() {
        val newClass = createSharedPrefsClass()

        newClass.addDeclaration(createWriteFunction())
        newClass.addDeclaration(createReadFunction())
        newClass.addDeclaration(createCompanionObject())

        ktClass.appendAfter(factory.createNewLine()).appendAfter(newClass)
        ktClass.addImports(factory, "android.content.SharedPreferences", "javax.inject.Inject")

        newClass.formatCode()
    }

    private fun createCompanionObject() =
        factory
        .createCompanionObject()
        .apply {
            for (param in params) {
                val paramKey = param.keyName(ktClass.name)
                addDeclaration(
                    factory.createProperty(
                        modifiers = "private const",
                        name = paramKey,
                        type = null,
                        isVar = false,
                        initializer = "\"$paramKey\""
                    )
                )
            }
        }

    private fun createWriteFunction() =
        factory
        .createFunction("fun write(${ktClass.name.lowerFirst()}: ${ktClass.name})")
        .append(
            factory.createBlock(
                "sharedPreferences.edit()\n" + params.toWriters(ktClass.name) + ".apply()"
            )
        )

    private fun createReadFunction() =
        factory
        .createFunction("fun read(): ${ktClass.name}")
        .append(
            factory.createBlock(
                "return ${ktClass.name}(\n" + params.toReaders(ktClass.name) + ")"
            )
        )

    private fun createSharedPrefsClass(): KtClass =
        factory.createClass("class ${ktClass.name + CLASS_SUFFIX}")
            .append(
                factory.createPrimaryConstructor(
                    "@Inject constructor(\nprivate val sharedPreferences: SharedPreferences\n)"
                )
            )
            .append(factory.createEmptyClassBody())
}
