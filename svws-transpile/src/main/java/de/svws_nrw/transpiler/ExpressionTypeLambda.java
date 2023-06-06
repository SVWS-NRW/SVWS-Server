package de.svws_nrw.transpiler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;

import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;


/**
 * The specialized {@link ExpressionType} if the type is a class type.
 */
public final class ExpressionTypeLambda extends ExpressionType {

	/** the lambda expression used to generate this type information */
	LambdaExpressionTree tree = null;

	/** the result type of the lambda expression */
	private ExpressionType resultType = null;

	/** a list of the lambda expression parameters type. */
	private final List<ExpressionType> paramTypes = new ArrayList<>();

	/**
	 * Creates a new expression class type instance from the specified {@link TypeElement}
	 */
	private ExpressionTypeLambda() {
		super(Kind.LAMBDA_EXPRESSION);
	}


	/**
	 * Creates a new lambda expression type instance from the specified {@link LambdaExpressionTree}
	 *
	 * @param transpiler   the transpiler used for determining the expression type
	 * @param tree         the lambda expression tree node
	 *
	 * @return the new lambda expression type instance
	 */
	public static ExpressionTypeLambda getExpressionTypeLambda(final Transpiler transpiler, final LambdaExpressionTree tree) {
		final ExpressionTypeLambda result = new ExpressionTypeLambda();
		result.tree = tree;
		for (final VariableTree varTree : tree.getParameters())
			result.paramTypes.add(ExpressionType.getExpressionType(transpiler, varTree.getType()));
		if (tree.getBody() instanceof final ExpressionTree body)
			result.resultType = transpiler.getExpressionType(body);
		return result;
	}


	/**
	 * Returns the resulting type of this lambda expression type
	 *
	 * @return the resulting type of this lambda expression type
	 */
	public ExpressionType getResultType() {
		return resultType;
	}


	/**
	 * Returns the parameter types of this lambda expression type
	 *
	 * @return the parameter types
	 */
	public List<? extends ExpressionType> getParamTypes() {
		return paramTypes;
	}


	/**
	 * Returns the name of the functional interface that is implemented with this
	 * lambda expression.
	 *
	 * @param transpiler   the transpiler used for getting further information about the functional interface
	 *
	 * @return the name of the functional interface
	 */
	public String getFunctionalInterfaceName(final Transpiler transpiler) {
		final Tree parent = transpiler.getParent(tree);
		if (parent instanceof final VariableTree vt) {
			final Tree varType = vt.getType();
			if (varType instanceof final ParameterizedTypeTree ptt) {
				Tree baseType = ptt.getType();
				if (baseType instanceof final AnnotatedTypeTree att)
					baseType = att.getUnderlyingType();
				if (baseType instanceof final IdentifierTree ident) {
					final ExpressionType type = transpiler.getExpressionType(ident);
					if (type == null)
						throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + ident.getName().toString());
					if (type instanceof final ExpressionClassType classType)
						return classType.getFullQualifiedName();
				}
			}
			throw new TranspilerException("Transpiler Error: Unhandled type for functional interfaces");
		}
		if (parent instanceof final MethodInvocationTree mit) {
			// determine the index in the parameter list where the lambda is used as parameter
			final int index = mit.getArguments().indexOf(tree);
			if (index < 0)
				throw new TranspilerException("Transpiler Error: Lambda Expression is expected to be in the method invocation argument list.");
			final ExecutableElement ee = transpiler.findExecutableElement(mit);
			if (index >= ee.getParameters().size())
				throw new TranspilerException("Transpiler Error: Unexpected internal error.");
			final VariableElement ve = ee.getParameters().get(index);
			final Element type = ((DeclaredType) ve.asType()).asElement();
			return type.toString();
		}
		if (parent instanceof final AssignmentTree at) {
			final ExpressionType type = transpiler.getExpressionType(at.getVariable());
			if (type == null)
				throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + at.getVariable().toString());
			if (type instanceof final ExpressionClassType classType)
				return classType.getFullQualifiedName();
		}
		// TODO improve type analyses to determine the name if lambdas are used in other situations
		return "java.util.function.Consumer";
	}

	/**
	 * Returns the name of the abstract method of the functional interface that
	 * is implemented with this lambda expression.
	 *
	 * @param transpiler   the transpiler used for getting further information about the functional interface
	 *
	 * @return the name of the abstract method of the functional interface that
	 * is implemented with this lambda expression
	 */
	public String getFunctionalInterfaceMethodName(final Transpiler transpiler) {
		return transpiler.getFunctionInterfaceMethodName(this.getFunctionalInterfaceName(transpiler));
	}


	@Override
	public int isAssignable(final Transpiler transpiler, final ExpressionType other) {
		throw new TranspilerException("Transpiler Error: Assignments with lambda expressions currently not supported.");
	}


	@Override
	public boolean isPrimitiveOrBoxedPrimitive() {
		return false;
	}


	@Override
	public String toString() {
		return paramTypes.stream().map(Object::toString).collect(Collectors.joining(", ", "(", ")")) + " -> " + (resultType == null ? "void" : resultType.toString());
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = getKind().hashCode();
		result = prime * result + paramTypes.hashCode();
		result = prime * result + ((resultType == null) ? 0 : resultType.hashCode());
		return result;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ExpressionTypeLambda other = (ExpressionTypeLambda) obj;
		if (getKind() != other.getKind())
			return false;
		if (!paramTypes.equals(other.paramTypes))
			return false;
		if (resultType == null) {
			if (other.resultType != null)
				return false;
		} else if (!resultType.equals(other.resultType))
			return false;
		return true;
	}


}
