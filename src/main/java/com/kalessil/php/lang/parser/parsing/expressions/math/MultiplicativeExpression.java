package com.kalessil.php.lang.parser.parsing.expressions.math;

import com.kalessil.php.lang.lexer.PhpTokenTypes;
import com.kalessil.php.lang.parser.PhpElementTypes;
import com.kalessil.php.lang.parser.parsing.expressions.AssignmentExpression;
import com.kalessil.php.lang.parser.parsing.expressions.logical.LogicalNotExpression;
import com.kalessil.php.lang.parser.util.PhpParserErrors;
import com.kalessil.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author jay
 * @time 16.12.2007 20:50:23
 */
public class MultiplicativeExpression implements PhpTokenTypes
{

	private static TokenSet MULTIPLICATIVE_OPERATORS = TokenSet.create(opDIV, opMUL, opREM);

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = LogicalNotExpression.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT && builder.compareAndEat(MULTIPLICATIVE_OPERATORS))
		{
			result = AssignmentExpression.parseWithoutPriority(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				result = LogicalNotExpression.parse(builder);
			}
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.expected("expression"));
			}
			PsiBuilder.Marker newMarker = marker.precede();
			marker.done(PhpElementTypes.MULTIPLICATIVE_EXPRESSION);
			result = PhpElementTypes.MULTIPLICATIVE_EXPRESSION;
			if(builder.compareAndEat(MULTIPLICATIVE_OPERATORS))
			{
				subParse(builder, newMarker);
			}
			else
			{
				newMarker.drop();
			}
		}
		else
		{
			marker.drop();
		}
		return result;
	}

	private static IElementType subParse(PhpPsiBuilder builder, PsiBuilder.Marker marker)
	{
		IElementType result = AssignmentExpression.parseWithoutPriority(builder);
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			result = LogicalNotExpression.parse(builder);
		}
		if(result == PhpElementTypes.EMPTY_INPUT)
		{
			builder.error(PhpParserErrors.expected("expression"));
		}
		PsiBuilder.Marker newMarker = marker.precede();
		marker.done(PhpElementTypes.MULTIPLICATIVE_EXPRESSION);
		if(builder.compareAndEat(MULTIPLICATIVE_OPERATORS))
		{
			subParse(builder, newMarker);
		}
		else
		{
			newMarker.drop();
		}
		return PhpElementTypes.MULTIPLICATIVE_EXPRESSION;
	}
}
