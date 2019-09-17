package org.elm.ide.commenter

import com.intellij.codeInsight.generation.CommenterDataHolder
import com.intellij.codeInsight.generation.SelfManagingCommenter
import com.intellij.lang.Commenter
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.util.text.CharArrayUtil

class ElmCommenter : SelfManagingCommenter<CommenterDataHolder>, Commenter {
    override fun getLineCommentPrefix() =
        "--"

    override fun getBlockCommentPrefix() =
        "{-"

    override fun getBlockCommentSuffix() =
        "-}"

    override fun getCommentedBlockCommentPrefix() =
        "{-"

    override fun getCommentedBlockCommentSuffix() =
        "-}"

    override fun getBlockCommentPrefix(selectionStart: Int, document: Document, data: CommenterDataHolder): String? =
        blockCommentPrefix

    override fun getBlockCommentSuffix(selectionEnd: Int, document: Document, data: CommenterDataHolder): String? =
        blockCommentSuffix

    override fun uncommentBlockComment(
        startOffset: Int,
        endOffset: Int,
        document: Document?,
        data: CommenterDataHolder?
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertBlockComment(
        startOffset: Int,
        endOffset: Int,
        document: Document?,
        data: CommenterDataHolder?
    ): TextRange {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun uncommentLine(line: Int, offset: Int, document: Document, data: CommenterDataHolder) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlockCommentRange(
        selectionStart: Int,
        selectionEnd: Int,
        document: Document,
        data: CommenterDataHolder
    ): TextRange? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isLineCommented(line: Int, offset: Int, document: Document, data: CommenterDataHolder): Boolean {
        print("WTF")
        return CharArrayUtil.regionMatches(document.charsSequence, offset, lineCommentPrefix)
    }

    override fun commentLine(line: Int, offset: Int, document: Document, data: CommenterDataHolder) {
        val logger = logger<ElmCommenter>()
        logger.info(document.charsSequence.subSequence(offset, document.getLineEndOffset(line)).toString())
    }

    override fun getCommentPrefix(line: Int, document: Document, data: CommenterDataHolder) = "--"

    override fun createBlockCommentingState(
        selectionStart: Int,
        selectionEnd: Int,
        document: Document,
        file: PsiFile
    ): CommenterDataHolder? = null

    override fun createLineCommentingState(
        startLine: Int,
        endLine: Int,
        document: Document,
        file: PsiFile
    ): CommenterDataHolder? = null
}