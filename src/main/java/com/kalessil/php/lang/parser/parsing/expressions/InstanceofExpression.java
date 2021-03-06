package com.kalessil.php.lang.parser.parsing.expressions;

import com.kalessil.php.lang.lexer.PhpTokenTypes;
import com.kalessil.php.lang.parser.PhpElementTypes;
import com.kalessil.php.lang.parser.parsing.classes.ClassReference;
import com.kalessil.php.lang.parser.util.PhpParserErrors;
import com.kalessil.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @time 16.12.2007 21:22:48
 */
public class InstanceofExpression implements PhpTokenTypes
{

	public static IElementType parse(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = UnaryExpression.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT && builder.compareAndEat(kwINSTANCEOF))
		{
			if(ClassReference.parseClassNameReference(builder, null, false, false, false) == null)
			{
				builder.error(PhpParserErrors.expected("class reference"));
			}
			marker.done(PhpElementTypes.INSTANCEOF_EXPRESSION);
			result = PhpElementTypes.INSTANCEOF_EXPRESSION;
		}
		else
		{
			marker.drop();
		}
		return result;
	}
}
