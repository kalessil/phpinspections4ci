package com.kalessil.php.lang.parser.parsing.expressions;

import com.kalessil.php.lang.lexer.PhpTokenTypes;
import com.kalessil.php.lang.parser.PhpElementTypes;
import com.kalessil.php.lang.parser.parsing.calls.Variable;
import com.kalessil.php.lang.parser.parsing.expressions.primary.NewExpression;
import com.kalessil.php.lang.parser.util.ListParsingHelper;
import com.kalessil.php.lang.parser.util.ParserPart;
import com.kalessil.php.lang.parser.util.PhpParserErrors;
import com.kalessil.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 15.12.2007
 */
public class AssignmentExpression implements PhpTokenTypes
{

	public static IElementType parse(PhpPsiBuilder builder)
	{
		IElementType result = parseWithoutPriority(builder);
		if(result != PhpElementTypes.EMPTY_INPUT)
		{
			return result;
		}
		PsiBuilder.Marker marker = builder.mark();
		result = TernaryExpression.parse(builder);
		if(result != PhpElementTypes.EMPTY_INPUT && builder.compare(tsASGN_OPS))
		{
			if(!ASSIGNABLE.contains(result))
			{
				builder.error("Expression is not assignable");
			}
			builder.advanceLexer();
			result = parse(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.expected("expression"));
			}
			marker.done(PhpElementTypes.ASSIGNMENT_EXPRESSION);
			return PhpElementTypes.ASSIGNMENT_EXPRESSION;
		}
		else
		{
			marker.drop();
		}
		return result;
	}

	//	kwLIST '(' assignment_list ')' '=' expr
	//	| variable '=' expr
	//	| variable '=' '&' variable
	//	| variable '=' '&' kwNEW class_name_reference ctor_arguments
	//	| variable opPLUS_ASGN expr
	//	| variable opMINUS_ASGN expr
	//	| variable opMUL_ASGN expr
	//	| variable opDIV_ASGN expr
	//	| variable opCONCAT_ASGN expr
	//	| variable opREM_ASGN expr
	//	| variable opBIT_AND_ASGN expr
	//	| variable opBIT_OR_ASGN expr
	//	| variable opBIT_XOR_ASGN expr
	//	| variable opSHIFT_LEFT_ASGN expr
	//	| variable opSHIFT_RIGHT_ASGN expr
	public static IElementType parseWithoutPriority(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		if(builder.compareAndEat(kwLIST))
		{ //kwLIST '(' assignment_list ')' '=' expr
			builder.match(chLPAREN);
			parseAssignmentList(builder);
			builder.match(chRPAREN);
			builder.match(opASGN);
			IElementType result = TernaryExpression.parse(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
			}
			marker.done(PhpElementTypes.MULTIASSIGNMENT_EXPRESSION);
			return PhpElementTypes.MULTIASSIGNMENT_EXPRESSION;
		}
		if(!builder.compare(Variable.START_TOKENS))
		{
			marker.drop();
			return PhpElementTypes.EMPTY_INPUT;
		}
		IElementType result = Variable.parseAssignable(builder);
		if(result == PhpElementTypes.EMPTY_INPUT || !builder.compare(tsASGN_OPS))
		{
			marker.rollbackTo();
			return PhpElementTypes.EMPTY_INPUT;
		}
		marker.done(result);
		marker = marker.precede();
		if(builder.compareAndEat(opASGN))
		{
			if(builder.compare(opBIT_AND))
			{
				builder.advanceLexer();
				if(builder.compare(kwNEW))
				{ //variable '=' '&' kwNEW class_name_reference ctor_arguments
					NewExpression.parse(builder);
					marker.done(PhpElementTypes.ASSIGNMENT_EXPRESSION);
					return PhpElementTypes.ASSIGNMENT_EXPRESSION;
				}
				else
				{ //variable '=' '&' variable
					PsiBuilder.Marker variable = builder.mark();
					IElementType var = Variable.parse(builder);
					if(var == PhpElementTypes.EMPTY_INPUT)
					{
						builder.error(PhpParserErrors.expected("variable"));
						variable.drop();
					}
					else
					{
						variable.done(var);
					}
					marker.done(PhpElementTypes.ASSIGNMENT_EXPRESSION);
					return PhpElementTypes.ASSIGNMENT_EXPRESSION;
				}
			}
			else
			{
				result = TernaryExpression.parse(builder);
				if(result == PhpElementTypes.EMPTY_INPUT)
				{
					builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
				}
				marker.done(PhpElementTypes.ASSIGNMENT_EXPRESSION);
				return PhpElementTypes.ASSIGNMENT_EXPRESSION;
			}
		}
		else
		{
			builder.compareAndEat(tsASGN_OPS);
			result = TernaryExpression.parse(builder);
			if(result == PhpElementTypes.EMPTY_INPUT)
			{
				builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
			}
			marker.done(PhpElementTypes.SELF_ASSIGNMENT_EXPRESSION);
			return PhpElementTypes.SELF_ASSIGNMENT_EXPRESSION;
		}
	}

	//	assignment_list:
	//		assignment_list ',' assignment_list_element
	//		| assignment_list_element
	//	;
	//
	//	assignment_list_element:
	//		variable
	//		| kwLIST '(' assignment_list ')'
	//		| /* empty */
	//	;
	private static void parseAssignmentList(PhpPsiBuilder builder)
	{
		ParserPart assignmentElement = new ParserPart()
		{
			@Override
			public IElementType parse(PhpPsiBuilder builder)
			{
				if(builder.compareAndEat(kwLIST))
				{
					builder.match(chLPAREN);
					parseAssignmentList(builder);
					builder.match(chRPAREN);
					return PhpElementTypes.VARIABLE_REFERENCE;
				}
				IElementType result = Variable.parse(builder);
				if(!ASSIGNABLE.contains(result))
				{
					builder.error("Expression is not assignable");
				}
				return result;
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, assignmentElement.parse(builder), assignmentElement, false);
	}
}
