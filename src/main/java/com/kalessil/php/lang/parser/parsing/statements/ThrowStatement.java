package com.kalessil.php.lang.parser.parsing.statements;

import com.kalessil.php.lang.lexer.PhpTokenTypes;
import com.kalessil.php.lang.parser.PhpElementTypes;
import com.kalessil.php.lang.parser.parsing.expressions.Expression;
import com.kalessil.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 08.11.2007
 */
public class ThrowStatement implements PhpTokenTypes
{

	//	kwTHROW expr ';'
	public static IElementType parse(PhpPsiBuilder builder)
	{
		if(!builder.compare(kwTHROW))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker statement = builder.mark();
		builder.advanceLexer();
		Expression.parse(builder);
		if(!builder.compare(PHP_CLOSING_TAG))
		{
			builder.match(opSEMICOLON);
		}
		statement.done(PhpElementTypes.THROW);
		return PhpElementTypes.THROW;
	}
}
