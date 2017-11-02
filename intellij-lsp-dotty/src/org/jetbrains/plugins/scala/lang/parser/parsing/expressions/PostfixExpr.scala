package org.jetbrains.plugins.scala.lang.parser.parsing.expressions

import org.jetbrains.plugins.scala.lang.lexer.ScalaTokenTypes
import org.jetbrains.plugins.scala.lang.parser.ScalaElementTypes
import org.jetbrains.plugins.scala.lang.parser.parsing.builder.ScalaPsiBuilder

/**
* @author Alexander Podkhalyuzin
* Date: 03.03.2008
*/

/*
 * PostfixExpr ::= InfixExpr [id [nl]]
 */
object PostfixExpr extends PostfixExpr {
  override protected def infixExpr = InfixExpr
}

trait PostfixExpr {
  protected def infixExpr: InfixExpr

  def parse(builder: ScalaPsiBuilder): Boolean = {
    val postfixMarker = builder.mark
    if (!infixExpr.parse(builder)) {
      postfixMarker.drop()
      return false
    }
    builder.getTokenType match {
      case ScalaTokenTypes.tIDENTIFIER if !builder.newlineBeforeCurrentToken =>
        val refMarker = builder.mark
        builder.advanceLexer //Ate id
        refMarker.done(ScalaElementTypes.REFERENCE_EXPRESSION)
        /*builder.getTokenType match {
          case ScalaTokenTypes.tLINE_TERMINATOR => {
            if (LineTerminator(builder.getTokenText)) builder.advanceLexer
          }
          case _ => {}
        }*/
        postfixMarker.done(ScalaElementTypes.POSTFIX_EXPR)
      case _ =>
        postfixMarker.drop
    }
    return true
  }
}