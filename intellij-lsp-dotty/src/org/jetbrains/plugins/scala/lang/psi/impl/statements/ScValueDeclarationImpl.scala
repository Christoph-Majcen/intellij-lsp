package org.jetbrains.plugins.scala.lang.psi.impl.statements

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.plugins.scala.ScalaBundle
import org.jetbrains.plugins.scala.extensions.ifReadAllowed
import org.jetbrains.plugins.scala.lang.parser.ScalaElementTypes._
import org.jetbrains.plugins.scala.lang.psi.ScalaStubBasedElementImpl
import org.jetbrains.plugins.scala.lang.psi.api.ScalaElementVisitor
import org.jetbrains.plugins.scala.lang.psi.api.base._
import org.jetbrains.plugins.scala.lang.psi.api.base.types.ScTypeElement
import org.jetbrains.plugins.scala.lang.psi.api.statements._
import org.jetbrains.plugins.scala.lang.psi.stubs.ScValueStub
import org.jetbrains.plugins.scala.lang.psi.types.result._

/**
 * @author Alexander Podkhalyuzin
 * Date: 22.02.2008
 * Time: 9:55:28
 */

class ScValueDeclarationImpl private (stub: ScValueStub, node: ASTNode)
  extends ScalaStubBasedElementImpl(stub, VALUE_DECLARATION, node) with ScValueDeclaration {

  def this(node: ASTNode) = this(null, node)

  def this(stub: ScValueStub) = this(stub, null)

  override def toString: String = "ScValueDeclaration: " + ifReadAllowed(declaredNames.mkString(", "))("")

  def declaredElements: Seq[ScFieldId] = getIdList.fieldIds

  override def `type`(): TypeResult = typeElement match {
    case Some(te) => te.`type`()
    case None => Failure(ScalaBundle.message("no.type.element.found", getText))
  }

  def typeElement: Option[ScTypeElement] = byPsiOrStub(findChild(classOf[ScTypeElement]))(_.typeElement)

  def getIdList: ScIdList = getStubOrPsiChild(IDENTIFIER_LIST)

  override def accept(visitor: ScalaElementVisitor) {
    visitor.visitValueDeclaration(this)
  }

  override def accept(visitor: PsiElementVisitor) {
    visitor match {
      case s: ScalaElementVisitor => s.visitValueDeclaration(this)
      case _ => super.accept(visitor)
    }
  }
}