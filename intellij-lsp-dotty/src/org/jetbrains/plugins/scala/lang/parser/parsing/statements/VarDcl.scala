package org.jetbrains.plugins.scala.lang.parser.parsing.statements

import org.jetbrains.plugins.scala.ScalaBundle
import org.jetbrains.plugins.scala.lang.lexer.ScalaTokenTypes
import org.jetbrains.plugins.scala.lang.parser.parsing.base.Ids
import org.jetbrains.plugins.scala.lang.parser.parsing.builder.ScalaPsiBuilder
import org.jetbrains.plugins.scala.lang.parser.parsing.types.Type

/**
* @author Alexander Podkhalyuzin
* Date: 11.02.2008
*/

/*
 * VarDcl ::= ids ':' Type
 */
object VarDcl extends VarDcl {
  override protected def `type` = Type
}

trait VarDcl {
  protected def `type`: Type

  def parse(builder: ScalaPsiBuilder): Boolean = {
    val returnMarker = builder.mark
    //Look for val
    builder.getTokenType match {
      case ScalaTokenTypes.kVAR => builder.advanceLexer() //Ate var
      case _ =>
        returnMarker.rollbackTo
        return false
    }
    //Look for identifier
    builder.getTokenType match {
      case ScalaTokenTypes.tIDENTIFIER =>
        Ids parse builder
        //Look for :
        builder.getTokenType match {
          case ScalaTokenTypes.tCOLON => {
            builder.advanceLexer //Ate :
            if (`type`.parse(builder)) {
              returnMarker.drop
            }
            else {
              builder error ScalaBundle.message("wrong.type")
              returnMarker.drop
            }
          }
          case _ => {
            builder error ScalaBundle.message("wrong.var.declaration")
            returnMarker.drop
          }
        }

        builder.getTokenType match {
          case ScalaTokenTypes.tASSIGN => {
            builder.advanceLexer
            builder.error("Expected expression")
            return true
          }
          case _ => {
            return true
          }
        }
      case _ =>
        builder error ScalaBundle.message("identifier.expected")
        returnMarker.drop
        return false
    }
  }
}