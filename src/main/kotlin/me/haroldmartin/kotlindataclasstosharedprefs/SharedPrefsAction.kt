package me.haroldmartin.kotlindataclasstosharedprefs

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.asJava.elements.KtLightElement
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.idea.internal.Location
import org.jetbrains.kotlin.psi.KtClass

class SharedPrefsAction : AnAction() {

    override fun update(e: AnActionEvent) {
        val ktClass = e.getPsiElement()?.getKtClass()
        e.presentation.isEnabled = ktClass != null && !ktClass.isEnum() && !ktClass.isInterface()
    }

    override fun actionPerformed(e: AnActionEvent) {
        e.getPsiElement()?.let { generateSharedPrefs(it) }
    }

    private fun generateSharedPrefs(element: PsiElement) {
        element.getKtClass()?.let {
            WriteCommandAction.runWriteCommandAction(it.project) {
                CodeGenerator(it).generate()
            }
        }
    }
}

fun AnActionEvent.getPsiElement(): PsiElement? {
    val editor = getData(PlatformDataKeys.EDITOR) ?: return null
    val project = editor.project ?: return null
    val psiFile = getData(LangDataKeys.PSI_FILE)
    if (psiFile == null || psiFile.language !== KotlinLanguage.INSTANCE) return null
    val location = Location.fromEditor(editor, project)
    return psiFile.findElementAt(location.startOffset)
}

fun PsiElement.getKtClass(): KtClass? {
    return if (this is KtLightElement<*, *>) {
        this.kotlinOrigin?.getKtClass()
    } else if (this is KtClass && !this.isEnum() &&
        !this.isInterface() &&
        !this.isAnnotation() &&
        !this.isSealed()
    ) {
        this
    } else {
        this.parent?.getKtClass()
    }
}
