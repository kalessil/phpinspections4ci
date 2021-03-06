package com.kalessil.php.lang.psi.impl;

import com.kalessil.php.lang.psi.PhpWhileStatement;
import com.kalessil.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;

/**
 * @author jay
 * @date May 18, 2008 9:29:28 PM
 */
public class PhpWhileStatementImpl extends PhpElementImpl implements PhpWhileStatement
{
	public PhpWhileStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitWhileStatement(this);
	}
}
