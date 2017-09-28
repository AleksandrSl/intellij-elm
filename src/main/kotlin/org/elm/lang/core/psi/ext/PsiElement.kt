/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.elm.lang.core.psi.ext

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil


val PsiElement.ancestors: Sequence<PsiElement> get() = generateSequence(this) { it.parent }


inline fun <reified T : PsiElement> PsiElement.parentOfType(strict: Boolean = true, minStartOffset: Int = -1): T? =
        PsiTreeUtil.getParentOfType(this, T::class.java, strict, minStartOffset)

inline fun <reified T : PsiElement> PsiElement.parentOfType(strict: Boolean = true, stopAt: Class<out PsiElement>): T? =
        PsiTreeUtil.getParentOfType(this, T::class.java, strict, stopAt)

inline fun <reified T : PsiElement> PsiElement.contextOfType(strict: Boolean = true): T? =
        PsiTreeUtil.getContextOfType(this, T::class.java, strict)

inline fun <reified T : PsiElement> PsiElement.childOfType(strict: Boolean = true): T? =
        PsiTreeUtil.findChildOfType(this, T::class.java, strict)

inline fun <reified T : PsiElement> PsiElement.descendantsOfType(): Collection<T> =
        PsiTreeUtil.findChildrenOfType(this, T::class.java)

/**
 * Finds first sibling that is neither comment, nor whitespace before given element.
 */
fun PsiElement?.getPrevNonCommentSibling(): PsiElement? =
        PsiTreeUtil.skipSiblingsBackward(this, PsiWhiteSpace::class.java, PsiComment::class.java)

/**
 * Finds first sibling that is neither comment, nor whitespace after given element.
 */
fun PsiElement?.getNextNonCommentSibling(): PsiElement? =
        PsiTreeUtil.skipSiblingsForward(this, PsiWhiteSpace::class.java, PsiComment::class.java)
