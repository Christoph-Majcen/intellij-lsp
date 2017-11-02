package org.jetbrains.plugins.scala.lang.psi.stubs.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.{IStubElementType, StubElement}
import com.intellij.util.io.StringRef
import org.jetbrains.plugins.scala.lang.psi.api.base.ScFieldId
import org.jetbrains.plugins.scala.lang.psi.stubs.ScFieldIdStub

/**
  * User: Alexander Podkhalyuzin
  * Date: 19.07.2009
  */
class ScFieldIdStubImpl(parent: StubElement[_ <: PsiElement],
                        elementType: IStubElementType[_ <: StubElement[_ <: PsiElement], _ <: PsiElement],
                        nameRef: StringRef)
  extends ScNamedStubBase[ScFieldId](parent, elementType, nameRef) with ScFieldIdStub