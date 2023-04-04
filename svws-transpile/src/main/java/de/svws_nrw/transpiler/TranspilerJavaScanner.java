package de.svws_nrw.transpiler;

import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.AssertTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BindingPatternTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.CatchTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.DoWhileLoopTree;
import com.sun.source.tree.EmptyStatementTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.ExportsTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.InstanceOfTree;
import com.sun.source.tree.IntersectionTypeTree;
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.ModuleTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.OpensTree;
import com.sun.source.tree.PackageTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ProvidesTree;
import com.sun.source.tree.RequiresTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.SwitchExpressionTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.SynchronizedTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.UnionTypeTree;
import com.sun.source.tree.UsesTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.WildcardTree;
import com.sun.source.tree.YieldTree;
import com.sun.source.util.TreePathScanner;

/**
 * This class implements the class {@link TreePathScanner} for scanning the
 * java compilers abstract syntax tree and gather some information to be
 * used in the transpiling process.
 */
public final class TranspilerJavaScanner extends TreePathScanner<Object, Transpiler> {

	@Override
	public Object visitCompilationUnit(final CompilationUnitTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitCompilationUnit(node, transpiler);
	}

	@Override
	public Object visitPackage(final PackageTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitPackage(node, transpiler);
	}

	@Override
	public Object visitImport(final ImportTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitImport(node, transpiler);
	}

	@Override
	public Object visitClass(final ClassTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitClassTree(getCurrentPath(), node);
		return super.visitClass(node, transpiler);
	}

	@Override
	public Object visitMethod(final MethodTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitMethodTree(getCurrentPath(), node);
		return super.visitMethod(node, transpiler);
	}

	@Override
	public Object visitVariable(final VariableTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitVariableTree(getCurrentPath(), node);
		return super.visitVariable(node, transpiler);
	}

	@Override
	public Object visitEmptyStatement(final EmptyStatementTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitEmptyStatement(node, transpiler);
	}

	@Override
	public Object visitBlock(final BlockTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitBlock(node, transpiler);
	}

	@Override
	public Object visitDoWhileLoop(final DoWhileLoopTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitDoWhileLoop(node, transpiler);
	}

	@Override
	public Object visitWhileLoop(final WhileLoopTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitWhileLoop(node, transpiler);
	}

	@Override
	public Object visitForLoop(final ForLoopTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitForLoop(node, transpiler);
	}

	@Override
	public Object visitEnhancedForLoop(final EnhancedForLoopTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitEnhancedForLoop(node, transpiler);
	}

	@Override
	public Object visitLabeledStatement(final LabeledStatementTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitLabeledStatement(node, transpiler);
	}

	@Override
	public Object visitSwitch(final SwitchTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitSwitch(node, transpiler);
	}

	@Override
	public Object visitSwitchExpression(final SwitchExpressionTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitSwitchExpression(node, transpiler);
	}

// Java 17 Preview Features
//	@Override
//	public Object visitDefaultCaseLabel(DefaultCaseLabelTree node, Transpiler transpiler) {
//		transpiler.visitTree(getCurrentPath(), node);
//		return super.visitDefaultCaseLabel(node, transpiler);
//	}
//
//	@Override
//	public Object visitParenthesizedPattern(ParenthesizedPatternTree node, Transpiler transpiler) {
//		transpiler.visitTree(getCurrentPath(), node);
//		return super.visitParenthesizedPattern(node, transpiler);
//	}
//
//	@Override
//	public Object visitGuardedPattern(GuardedPatternTree node, Transpiler transpiler) {
//		transpiler.visitTree(getCurrentPath(), node);
//		return super.visitGuardedPattern(node, transpiler);
//	}

	@Override
	public Object visitCase(final CaseTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitCase(node, transpiler);
	}

	@Override
	public Object visitSynchronized(final SynchronizedTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitSynchronized(node, transpiler);
	}

	@Override
	public Object visitTry(final TryTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitTry(node, transpiler);
	}

	@Override
	public Object visitCatch(final CatchTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitCatch(node, transpiler);
	}

	@Override
	public Object visitConditionalExpression(final ConditionalExpressionTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitConditionalExpression(node, transpiler);
	}

	@Override
	public Object visitIf(final IfTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitIf(node, transpiler);
	}

	@Override
	public Object visitExpressionStatement(final ExpressionStatementTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitExpressionStatement(node, transpiler);
	}

	@Override
	public Object visitBreak(final BreakTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitBreak(node, transpiler);
	}

	@Override
	public Object visitContinue(final ContinueTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitContinue(node, transpiler);
	}

	@Override
	public Object visitReturn(final ReturnTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitReturn(node, transpiler);
	}

	@Override
	public Object visitThrow(final ThrowTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitThrow(node, transpiler);
	}

	@Override
	public Object visitAssert(final AssertTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitAssert(node, transpiler);
	}

	@Override
	public Object visitMethodInvocation(final MethodInvocationTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitMethodInvocation(node, transpiler);
	}

	@Override
	public Object visitNewClass(final NewClassTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitNewClass(node, transpiler);
	}

	@Override
	public Object visitNewArray(final NewArrayTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitNewArray(node, transpiler);
	}

	@Override
	public Object visitLambdaExpression(final LambdaExpressionTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitLambdaExpression(node, transpiler);
	}

	@Override
	public Object visitParenthesized(final ParenthesizedTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitParenthesized(node, transpiler);
	}

	@Override
	public Object visitAssignment(final AssignmentTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitAssignment(node, transpiler);
	}

	@Override
	public Object visitCompoundAssignment(final CompoundAssignmentTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitCompoundAssignment(node, transpiler);
	}

	@Override
	public Object visitUnary(final UnaryTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitUnary(node, transpiler);
	}

	@Override
	public Object visitBinary(final BinaryTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitBinary(node, transpiler);
	}

	@Override
	public Object visitTypeCast(final TypeCastTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitTypeCast(node, transpiler);
	}

	@Override
	public Object visitInstanceOf(final InstanceOfTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitInstanceOf(node, transpiler);
	}

	@Override
	public Object visitBindingPattern(final BindingPatternTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitBindingPattern(node, transpiler);
	}

	@Override
	public Object visitArrayAccess(final ArrayAccessTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitArrayAccess(node, transpiler);
	}

	@Override
	public Object visitMemberSelect(final MemberSelectTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitMemberSelect(node, transpiler);
	}

	@Override
	public Object visitMemberReference(final MemberReferenceTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitMemberReference(node, transpiler);
	}

	@Override
	public Object visitIdentifier(final IdentifierTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitIdentifierTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitIdentifier(node, transpiler);
	}

	@Override
	public Object visitLiteral(final LiteralTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitLiteral(node, transpiler);
	}

	@Override
	public Object visitPrimitiveType(final PrimitiveTypeTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitPrimitiveType(node, transpiler);
	}

	@Override
	public Object visitArrayType(final ArrayTypeTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitArrayType(node, transpiler);
	}

	@Override
	public Object visitParameterizedType(final ParameterizedTypeTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitParameterizedType(node, transpiler);
	}

	@Override
	public Object visitUnionType(final UnionTypeTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitUnionType(node, transpiler);
	}

	@Override
	public Object visitIntersectionType(final IntersectionTypeTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitIntersectionType(node, transpiler);
	}

	@Override
	public Object visitTypeParameter(final TypeParameterTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitTypeParameter(getCurrentPath(), node);
		return super.visitTypeParameter(node, transpiler);
	}

	@Override
	public Object visitWildcard(final WildcardTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitWildcard(node, transpiler);
	}

	@Override
	public Object visitModifiers(final ModifiersTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitModifierTree(getCurrentPath(), node);
		return super.visitModifiers(node, transpiler);
	}

	@Override
	public Object visitAnnotation(final AnnotationTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		transpiler.visitAnnotation(getCurrentPath(), node);
		return super.visitAnnotation(node, transpiler);
	}

	@Override
	public Object visitAnnotatedType(final AnnotatedTypeTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitAnnotatedType(node, transpiler);
	}

	@Override
	public Object visitModule(final ModuleTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitModule(node, transpiler);
	}

	@Override
	public Object visitExports(final ExportsTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitExports(node, transpiler);
	}

	@Override
	public Object visitOpens(final OpensTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitOpens(node, transpiler);
	}

	@Override
	public Object visitProvides(final ProvidesTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitProvides(node, transpiler);
	}

	@Override
	public Object visitRequires(final RequiresTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitRequires(node, transpiler);
	}

	@Override
	public Object visitUses(final UsesTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitUses(node, transpiler);
	}

	@Override
	public Object visitOther(final Tree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitOther(node, transpiler);
	}

	@Override
	public Object visitErroneous(final ErroneousTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		return super.visitErroneous(node, transpiler);
	}

	@Override
	public Object visitYield(final YieldTree node, final Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitYield(node, transpiler);
	}

}
