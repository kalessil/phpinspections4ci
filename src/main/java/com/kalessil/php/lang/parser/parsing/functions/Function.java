package com.kalessil.php.lang.parser.parsing.functions;

import com.kalessil.php.lang.lexer.PhpTokenTypes;
import com.kalessil.php.lang.parser.PhpElementTypes;
import com.kalessil.php.lang.parser.parsing.StatementList;
import com.kalessil.php.lang.parser.util.PhpParserErrors;
import com.kalessil.php.lang.parser.util.PhpPsiBuilder;
import com.kalessil.php.lang.psi.PhpStubElements;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 11:44:29
 */
public class Function implements PhpTokenTypes
{

	//	function_declaration_statement:
	//		kwFUNCTION is_reference IDENTIFIER '(' parameter_list ')'
	//			'{' statement_list '}'
	//	;
	public static IElementType parse(PhpPsiBuilder builder)
	{
		if(!builder.compare(kwFUNCTION))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker function = builder.mark();
		builder.advanceLexer();
		IsReference.parse(builder);
		if(!builder.compareAndEat(IDENTIFIER))
		{
			builder.error(PhpParserErrors.expected("function name"));
		}
		ParameterList.parse(builder);
		builder.match(chLBRACE);
		StatementList.parse(builder, chRBRACE);
		builder.match(chRBRACE);
		function.done(PhpStubElements.FUNCTION);
		return PhpStubElements.FUNCTION;
	}
}
