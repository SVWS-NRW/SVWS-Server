package de.nrw.schule.svws.transpiler;

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
public class TranspilerJavaScanner extends TreePathScanner<Object, Transpiler> {

	@Override
	public Object visitCompilationUnit(CompilationUnitTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitCompilationUnit(node, transpiler);
	}

	@Override
	public Object visitPackage(PackageTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitPackage(node, transpiler);
	}

	@Override
	public Object visitImport(ImportTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitImport(node, transpiler);
	}

	@Override
	public Object visitClass(ClassTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitClassTree(getCurrentPath(), node);
		return super.visitClass(node, transpiler);
	}

	@Override
	public Object visitMethod(MethodTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitMethodTree(getCurrentPath(), node);
		return super.visitMethod(node, transpiler);
	}

	@Override
	public Object visitVariable(VariableTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitVariableTree(getCurrentPath(), node);
		return super.visitVariable(node, transpiler);
	}

	@Override
	public Object visitEmptyStatement(EmptyStatementTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitEmptyStatement(node, transpiler);
	}

	@Override
	public Object visitBlock(BlockTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitBlock(node, transpiler);
	}

	@Override
	public Object visitDoWhileLoop(DoWhileLoopTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitDoWhileLoop(node, transpiler);
	}

	@Override
	public Object visitWhileLoop(WhileLoopTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitWhileLoop(node, transpiler);
	}

	@Override
	public Object visitForLoop(ForLoopTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitForLoop(node, transpiler);
	}

	@Override
	public Object visitEnhancedForLoop(EnhancedForLoopTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitEnhancedForLoop(node, transpiler);
	}

	@Override
	public Object visitLabeledStatement(LabeledStatementTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitLabeledStatement(node, transpiler);
	}

	@Override
	public Object visitSwitch(SwitchTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitSwitch(node, transpiler);
	}

	@Override
	public Object visitSwitchExpression(SwitchExpressionTree node, Transpiler transpiler) {
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
	public Object visitCase(CaseTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitCase(node, transpiler);
	}

	@Override
	public Object visitSynchronized(SynchronizedTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitSynchronized(node, transpiler);
	}

	@Override
	public Object visitTry(TryTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitTry(node, transpiler);
	}

	@Override
	public Object visitCatch(CatchTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitCatch(node, transpiler);
	}

	@Override
	public Object visitConditionalExpression(ConditionalExpressionTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitConditionalExpression(node, transpiler);
	}

	@Override
	public Object visitIf(IfTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitIf(node, transpiler);
	}

	@Override
	public Object visitExpressionStatement(ExpressionStatementTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitExpressionStatement(node, transpiler);
	}

	@Override
	public Object visitBreak(BreakTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitBreak(node, transpiler);
	}

	@Override
	public Object visitContinue(ContinueTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitContinue(node, transpiler);
	}

	@Override
	public Object visitReturn(ReturnTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitReturn(node, transpiler);
	}

	@Override
	public Object visitThrow(ThrowTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitThrow(node, transpiler);
	}

	@Override
	public Object visitAssert(AssertTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitAssert(node, transpiler);
	}

	@Override
	public Object visitMethodInvocation(MethodInvocationTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitMethodInvocation(node, transpiler);
	}

	@Override
	public Object visitNewClass(NewClassTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitNewClass(node, transpiler);
	}

	@Override
	public Object visitNewArray(NewArrayTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitNewArray(node, transpiler);
	}

	@Override
	public Object visitLambdaExpression(LambdaExpressionTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitLambdaExpression(node, transpiler);
	}

	@Override
	public Object visitParenthesized(ParenthesizedTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitParenthesized(node, transpiler);
	}

	@Override
	public Object visitAssignment(AssignmentTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitAssignment(node, transpiler);
	}

	@Override
	public Object visitCompoundAssignment(CompoundAssignmentTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitCompoundAssignment(node, transpiler);
	}

	@Override
	public Object visitUnary(UnaryTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitUnary(node, transpiler);
	}

	@Override
	public Object visitBinary(BinaryTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitBinary(node, transpiler);
	}

	@Override
	public Object visitTypeCast(TypeCastTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitTypeCast(node, transpiler);
	}

	@Override
	public Object visitInstanceOf(InstanceOfTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitInstanceOf(node, transpiler);
	}

	@Override
	public Object visitBindingPattern(BindingPatternTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitBindingPattern(node, transpiler);
	}

	@Override
	public Object visitArrayAccess(ArrayAccessTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitArrayAccess(node, transpiler);
	}

	@Override
	public Object visitMemberSelect(MemberSelectTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitMemberSelect(node, transpiler);
	}

	@Override
	public Object visitMemberReference(MemberReferenceTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitMemberReference(node, transpiler);
	}

	@Override
	public Object visitIdentifier(IdentifierTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitIdentifierTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitIdentifier(node, transpiler);
	}

	@Override
	public Object visitLiteral(LiteralTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitLiteral(node, transpiler);
	}

	@Override
	public Object visitPrimitiveType(PrimitiveTypeTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitPrimitiveType(node, transpiler);
	}

	@Override
	public Object visitArrayType(ArrayTypeTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitArrayType(node, transpiler);
	}

	@Override
	public Object visitParameterizedType(ParameterizedTypeTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitParameterizedType(node, transpiler);
	}

	@Override
	public Object visitUnionType(UnionTypeTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitUnionType(node, transpiler);
	}

	@Override
	public Object visitIntersectionType(IntersectionTypeTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitIntersectionType(node, transpiler);
	}

	@Override
	public Object visitTypeParameter(TypeParameterTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitTypeParameter(getCurrentPath(), node);
		return super.visitTypeParameter(node, transpiler);
	}

	@Override
	public Object visitWildcard(WildcardTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitWildcard(node, transpiler);
	}

	@Override
	public Object visitModifiers(ModifiersTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitModifierTree(getCurrentPath(), node);
		return super.visitModifiers(node, transpiler);
	}

	@Override
	public Object visitAnnotation(AnnotationTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);
		transpiler.visitAnnotation(getCurrentPath(), node);
		return super.visitAnnotation(node, transpiler);
	}

	@Override
	public Object visitAnnotatedType(AnnotatedTypeTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitAnnotatedType(node, transpiler);
	}

	@Override
	public Object visitModule(ModuleTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitModule(node, transpiler);
	}

	@Override
	public Object visitExports(ExportsTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitExports(node, transpiler);
	}

	@Override
	public Object visitOpens(OpensTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitOpens(node, transpiler);
	}

	@Override
	public Object visitProvides(ProvidesTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitProvides(node, transpiler);
	}

	@Override
	public Object visitRequires(RequiresTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitRequires(node, transpiler);
	}

	@Override
	public Object visitUses(UsesTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitUses(node, transpiler);
	}

	@Override
	public Object visitOther(Tree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitOther(node, transpiler);
	}

	@Override
	public Object visitErroneous(ErroneousTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		transpiler.visitExpressionTree(getCurrentPath(), node);		
		return super.visitErroneous(node, transpiler);
	}

	@Override
	public Object visitYield(YieldTree node, Transpiler transpiler) {
		transpiler.visitTree(getCurrentPath(), node);
		return super.visitYield(node, transpiler);
	}
	
}
