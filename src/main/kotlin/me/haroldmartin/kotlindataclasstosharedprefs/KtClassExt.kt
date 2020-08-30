package me.haroldmartin.kotlindataclasstosharedprefs

import com.intellij.psi.codeStyle.CodeStyleManager
import org.jetbrains.kotlin.nj2k.postProcessing.type
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.resolve.ImportPath

internal fun KtClass.getParamsAsDefaultableParameterList(): List<DefaultableParameter> {
    return primaryConstructorParameters.map {
        DefaultableParameter(
            name = it.name ?: "",
            type = it.type().toString(),
            default = if (it.hasDefaultValue()) it.defaultValue?.node?.text else null
        )
    }
}

internal fun KtClass.addImports(factory: KtPsiFactory, vararg imports: String) {
    val importList = containingKtFile.importList ?: return
    val importPaths = importList.imports.map { it.importPath.toString() }
    for (importPath in imports) {
        if (!importPaths.contains(importPath)) {
            importList.add(
                factory.createImportDirective(
                    ImportPath.fromString(importPath)
                )
            )
        }
    }
}

internal fun KtClass.formatCode() = CodeStyleManager.getInstance(project).reformat(this)
