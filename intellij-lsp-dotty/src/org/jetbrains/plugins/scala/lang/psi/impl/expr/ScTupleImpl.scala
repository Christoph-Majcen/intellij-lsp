package org.jetbrains.plugins.scala.lang.psi.impl.expr

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.plugins.scala.lang.psi.ScalaPsiElementImpl
import org.jetbrains.plugins.scala.lang.psi.api.ScalaElementVisitor
import org.jetbrains.plugins.scala.lang.psi.api.expr._
import org.jetbrains.plugins.scala.lang.psi.types.api.{TupleType, Unit}
import org.jetbrains.plugins.scala.lang.psi.types.result._

/**
 * @author ilyas, Alexander Podkhalyuzin
 */
class ScTupleImpl(node: ASTNode) extends ScalaPsiElementImpl(node) with ScTuple {
  override def toString: String = "Tuple"

  protected override def innerType: TypeResult = {
    val result = exprs.map(_.`type`().getOrAny) match {
      case Seq() => Unit
      case components => TupleType(components)
    }
    Right(result)
  }

  override def accept(visitor: ScalaElementVisitor) {
    visitor.visitTupleExpr(this)
  }

  override def accept(visitor: PsiElementVisitor) {
    visitor match {
      case visitor: ScalaElementVisitor => visitor.visitTupleExpr(this)
      case _ => super.accept(visitor)
    }
  }
}