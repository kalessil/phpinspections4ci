package com.kalessil.php.lang.parser.parsing.classes;

import com.kalessil.php.lang.lexer.PhpTokenTypes;
import com.kalessil.php.lang.parser.PhpElementTypes;
import com.kalessil.php.lang.parser.util.PhpPsiBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 28.10.2007
 */
public class ClassMemberModifiers implements PhpTokenTypes
{

	//	variable_modifiers:
	//		non_empty_member_modifiers
	//		| kwVAR
	//	;
	public static IElementType parseVariableModifiers(PhpPsiBuilder builder)
	{
		if(!builder.compare(tsVARIABLE_MODIFIERS))
		{
			return PhpElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker modifiers = builder.mark();
		if(builder.compareAndEat(kwVAR))
		{
			modifiers.done(PhpElementTypes.MODIFIER_LIST);
			return PhpElementTypes.MODIFIER_LIST;
		}
		parseModifiers(builder);
		modifiers.done(PhpElementTypes.MODIFIER_LIST);
		return PhpElementTypes.MODIFIER_LIST;
	}

	public static void parseModifiers(PhpPsiBuilder builder)
	{
		PsiBuilder.Marker modifiers = builder.mark();
		while(builder.compare(tsMODIFIERS))
		{
			builder.advanceLexer();
		}
		modifiers.done(PhpElementTypes.MODIFIER_LIST);
	}
}
