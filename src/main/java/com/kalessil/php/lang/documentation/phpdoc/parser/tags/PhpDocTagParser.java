package com.kalessil.php.lang.documentation.phpdoc.parser.tags;

import com.kalessil.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.kalessil.php.lang.parser.util.PhpPsiBuilder;

/**
 * @author jay
 * @date Jun 28, 2008 6:02:39 PM
 */
abstract public class PhpDocTagParser implements PhpDocElementTypes
{

	protected PhpDocTagParser()
	{
	}

	public static void register()
	{
	}

	abstract public String getName();

	abstract public void parse(PhpPsiBuilder builder);

}
