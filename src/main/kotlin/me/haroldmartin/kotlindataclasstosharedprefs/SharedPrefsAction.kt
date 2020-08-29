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
//    private val logger = Logger.getInstance("SharedPrefsAction")

    override fun update(e: AnActionEvent) {
        val ktClass = e.getPsiElement()?.getKtClass()
        e.presentation.isEnabled = ktClass != null && !ktClass.isEnum() && !ktClass.isInterface()
    }

    override fun actionPerformed(e: AnActionEvent) {
//        if(!ktClass.isData()) {
//            Messages.showErrorDialog("ParcelableGenerator only support for data class.", "Sorry");
//
//        } else {
//            GenerateDialog dlg = new GenerateDialog(ktClass);
//            dlg.show();
//            if (dlg.isOK()) {
//                generateParcelable(ktClass, dlg.getSelectedFields());
//            }
//        generateParcelable(ktClass, ktClass.findParams())
        //        }

        e.getPsiElement()?.let { generateSharedPrefs(it) }
    }

    private fun generateSharedPrefs(element: PsiElement) {
        element.getKtClass()?.let {
            object : WriteCommandAction.Simple<Any?>(it.project, it.containingFile) {
                @Throws(Throwable::class)
                override fun run() {
                    CodeGenerator(it).generate()
                }
            }.execute()
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
        this.kotlinOrigin?.let { it.getKtClass() }
    } else if (this is KtClass && !this.isEnum() &&
        !this.isInterface() &&
        !this.isAnnotation() &&
        !this.isSealed()
    ) {
        this
    } else {
        this.parent?.let { it.getKtClass() }
    }
}
