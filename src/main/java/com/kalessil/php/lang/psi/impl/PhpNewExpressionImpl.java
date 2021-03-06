package com.kalessil.php.lang.psi.impl;

import com.kalessil.php.lang.psi.PhpNewExpression;
import com.kalessil.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;

/**
 * @author jay
 * @date Jun 18, 2008 1:34:34 AM
 */
public class PhpNewExpressionImpl extends PhpTypeOwnerImpl implements PhpNewExpression
{
	public PhpNewExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitNewExpression(this);
	}
}
