package org.frawa.elmtest.run

import com.intellij.execution.Location
import com.intellij.execution.PsiLocation
import com.intellij.execution.testframework.sm.FileUrlProvider
import com.intellij.execution.testframework.sm.TestsLocationProviderUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.search.GlobalSearchScope
import org.elm.openapiext.toPsiFile
import org.frawa.elmtest.core.ElmPluginHelper.getPsiElement
import org.frawa.elmtest.core.LabelUtils


object ElmTestLocator : FileUrlProvider() {

    override fun getLocation(protocol: String, path: String, metainfo: String?, project: Project, scope: GlobalSearchScope): List<Location<*>> {
        if (!protocol.startsWith(LabelUtils.ELM_TEST_PROTOCOL)) {
            return super.getLocation(protocol, path, metainfo, project, scope)
        }

        if (protocol.startsWith(LabelUtils.ERROR_PROTOCOL)) {
            val pair = LabelUtils.fromErrorLocationUrlPath(path)
            val filePath = pair.first
            val line = pair.second.first
            val column = pair.second.second
            val systemIndependentPath = FileUtil.toSystemIndependentName(filePath)
            return TestsLocationProviderUtil.findSuitableFilesFor(systemIndependentPath, project)
                    .mapNotNull { getErrorLocation(line, column, project, it) }
        }

        val pair = LabelUtils.fromLocationUrlPath(path)
        val filePath = pair.first
        val labels = pair.second
        val systemIndependentPath = FileUtil.toSystemIndependentName(filePath)
        val isDescribe = LabelUtils.DESCRIBE_PROTOCOL == protocol

        return TestsLocationProviderUtil.findSuitableFilesFor(systemIndependentPath, project)
                .mapNotNull { getLocation(isDescribe, labels, project, it) }
    }

    private fun getLocation(isDescribe: Boolean, labels: String, project: Project, virtualFile: VirtualFile): Location<*>? {
        val psiFile = virtualFile.toPsiFile(project) ?: return null
        val found = getPsiElement(isDescribe, labels, psiFile)
        return PsiLocation.fromPsiElement(project, found)
    }

    private fun getErrorLocation(line: Int, column: Int, project: Project, virtualFile: VirtualFile): Location<*>? {
        val psiFile = virtualFile.toPsiFile(project) ?: return null
        val document = PsiDocumentManager.getInstance(project).getDocument(psiFile)
                ?: return PsiLocation.fromPsiElement(project, psiFile)

        val offset = document.getLineStartOffset(line - 1) + column - 1
        val element = psiFile.findElementAt(offset)
                ?: return PsiLocation.fromPsiElement(project, psiFile)

        return PsiLocation.fromPsiElement(project, element)
    }
}
