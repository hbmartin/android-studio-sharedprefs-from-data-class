package me.haroldmartin.kotlindataclasstosharedprefs

import com.intellij.psi.PsiElement

internal fun <T : PsiElement> T.append(element: PsiElement): T {
    addAfter(element, lastChild)
    return this
}

internal fun <T : PsiElement> T.appendAfter(element: PsiElement): T {
    parent.addAfter(element, this)
    return this
}
