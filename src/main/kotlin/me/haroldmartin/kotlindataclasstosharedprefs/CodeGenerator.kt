package me.haroldmartin.kotlindataclasstosharedprefs

import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtPsiFactory

private const val CLASS_SUFFIX = "SharedPreferencesRepository"

class CodeGenerator(
    private val ktClass: KtClass
) {
    private val params = ktClass.getParamsAsDefaultableParameterList()
    private val factory = KtPsiFactory(ktClass.project)
    private val ktClassName = ktClass.name ?: "SP"

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
                    val paramKey = param.name.keyName(ktClassName)
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
            .createFunction("fun write(${ktClassName.lowerFirst()}: $ktClassName)")
            .append(
                factory.createBlock(
                    "sharedPreferences.edit()\n" + params.toWriters(ktClassName) + ".apply()"
                )
            )

    private fun createReadFunction() =
        factory
            .createFunction("fun read(): $ktClassName")
            .append(
                factory.createBlock(
                    "return $ktClassName(\n" + params.toReaders(ktClassName) + ")"
                )
            )

    private fun createSharedPrefsClass(): KtClass =
        factory.createClass("class ${ktClassName + CLASS_SUFFIX}")
            .append(
                factory.createPrimaryConstructor(
                    "@Inject constructor(\nprivate val sharedPreferences: SharedPreferences\n)"
                )
            )
            .append(factory.createEmptyClassBody())
}
