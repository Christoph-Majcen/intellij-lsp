package org.jetbrains.plugins.scala.lang.psi.api.base.patterns

import org.jetbrains.plugins.scala.lang.psi.api.base.types.ScTypeElement

/**
* @author Alexander Podkhalyuzin
*/

trait ScTypedPattern extends ScBindingPattern  {
  def typePattern: Option[ScTypePattern] = findChild(classOf[ScTypePattern])
}

object ScTypedPattern {
  def unapply(pattern: ScTypedPattern): Option[ScTypeElement] = {
    pattern.typePattern.map(_.typeElement)
  }
}