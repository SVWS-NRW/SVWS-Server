package de.nrw.schule.svws.transpiler;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.lang.model.element.TypeElement;

import com.sun.source.tree.AnnotatedTypeTree;
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
public class ExpressionTypeLambda extends ExpressionType {

	/** the lambda expression used to generate this type information */
	LambdaExpressionTree tree = null;
	
	/** the result type of the lambda expression */
	private ExpressionType resultType = null;
	
	/** a list of the lambda expression parameters type. */
	private final List<ExpressionType> paramTypes = new Vector<>();
		
	/**
	 * Creates a new expression class type instance from the specified {@link TypeElement}
	 * 
	 * @param elem   the type element
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
	public static ExpressionTypeLambda getExpressionTypeLambda(Transpiler transpiler, LambdaExpressionTree tree) {
		ExpressionTypeLambda result = new ExpressionTypeLambda();
		result.tree = tree;
		for (VariableTree varTree : tree.getParameters())
			result.paramTypes.add(ExpressionType.getExpressionType(transpiler, varTree.getType()));
		if (tree.getBody() instanceof ExpressionTree body)
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
	public String getFunctionalInterfaceName(Transpiler transpiler) {
		Tree parent = transpiler.getParent(tree);
		if (parent instanceof VariableTree vt) {
			Tree varType = vt.getType();
			if (varType instanceof ParameterizedTypeTree ptt) {
				Tree baseType = ptt.getType();
				if (baseType instanceof AnnotatedTypeTree att)
					baseType = att.getUnderlyingType();
				if (baseType instanceof IdentifierTree ident) {
					ExpressionType type = transpiler.getExpressionType(ident);
					if (type == null)
						throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + ident.getName().toString());
					if (type instanceof ExpressionClassType classType)
						// TODO improve type analyses to determine the name if lambdas are used in method invocation parameters
						return classType.getFullQualifiedName();
				}
			} 
			throw new TranspilerException("Transpiler Error: Unhandled type for functional interfaces");				
		} 
		if (parent instanceof MethodInvocationTree mit) {
			// TODO improve type analyses to determine the name if lambdas are used in method invocation parameters
		}
		// TODO improve type analyses to determine the name if lambdas are used in method invocation parameters
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
	public String getFunctionalInterfaceMethodName(Transpiler transpiler) {
		return transpiler.getFunctionInterfaceMethodName(this.getFunctionalInterfaceName(transpiler));
	}


	@Override
	public int isAssignable(Transpiler transpiler, ExpressionType other) {
		throw new TranspilerException("Transpiler Error: Assignments with lambda expressions currently not supported.");
	}


	@Override
	public boolean isPrimitiveOrBoxedPrimitive() {
		return false;
	}
	

	@Override
	public String toString() {
		return paramTypes.stream().map(pt -> pt.toString()).collect(Collectors.joining(", ", "(", ")")) + " -> " + (resultType == null ? "void" : resultType.toString());
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = getKind().hashCode();
		result = prime * result + ((paramTypes == null) ? 0 : paramTypes.hashCode());
		result = prime * result + ((resultType == null) ? 0 : resultType.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		ExpressionTypeLambda other = (ExpressionTypeLambda) obj;
		if (getKind() != other.getKind())
			return false;
		if (paramTypes == null) {
			if (other.paramTypes != null)
				return false;
		} else if (!paramTypes.equals(other.paramTypes))
			return false;
		if (resultType == null) {
			if (other.resultType != null)
				return false;
		} else if (!resultType.equals(other.resultType))
			return false;
		return true;
	}


}
