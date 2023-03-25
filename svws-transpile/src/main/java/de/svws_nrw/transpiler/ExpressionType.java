package de.svws_nrw.transpiler;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.WildcardTree;


/**
 * The base class for all types of expressions that were evaluated by the transpiler.  
 */
public abstract class ExpressionType implements Tree {

	/** the type kind of the evaluated expression */
	private final Kind kind;
	
	/**
	 * Create a new expression type of the specified kind
	 * 
	 * @param kind   the type kind
	 */
	protected ExpressionType(Kind kind) {
		this.kind = kind;
	}
	
	@Override
	public Kind getKind() {
		return kind;
	}

	@Override
	public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
		return null;
	}
	
	
	
	/**
	 * Checks whether a values of the specified type other is assignable to 
	 * a variable of this type or not. If a value of that type is not assignable
	 * a negative value is returned. A zero or positive value is returned if
	 * it is assignable. A zero value indicates a perfect type match and greater 
	 * values indicate a less perfect type match, e.g. unboxing of a primitive
	 * type is required or the a value of a sub class type should be assigned.   
	 * 
	 * @param transpiler   the transpiler object used for utility methods 
	 * @param other        the other type
	 * 
	 * @return a postive value or zero if the type is assignable and a negative value if not. 
	 */
	public abstract int isAssignable(Transpiler transpiler, ExpressionType other);
	
	
	/**
	 * Returns whether the type is a primitive type or a boxed primitive type.
	 * 
	 * @return true if it is a primitive type and false otherwise
	 */
	public abstract boolean isPrimitiveOrBoxedPrimitive();
	
	
	/**
	 * Checks whether this type is the String type.
	 * 
	 * @return true if it a string and false otherwise.
	 */
	public boolean isString() {
		if (this instanceof ExpressionClassType ect)
			return "java.lang.String".equals(ect.getFullQualifiedName());
		return false;
	}
	

	/**
	 * Checks whether this type is a numeric or boxed numeric type. The method
	 * is an override in the subclasses {@link ExpressionPrimitiveType} and
	 * {@link ExpressionClassType}.
	 * 
	 * @return true if it is a numeric type and false otherwise.
	 */
	public boolean isNumberType() {
		return false; 
	}
	
	
	/**
	 * Checks whether this type is an integer type. The method
	 * is an override in the subclasses {@link ExpressionPrimitiveType} and
	 * {@link ExpressionClassType}.
	 * 
	 * @return true if it is an integer type and false otherwise.
	 */
	public boolean isIntegerType() {
		return false; 
	}
	
	
	/**
	 * Determines the expression type for the specified abstract syntax tree (AST) 
	 * node specified by the parameter 'type'.
	 * 
	 * @param transpiler   the transpiler used for determining the expression type 
	 * @param type         the type mirror of the AST node 
	 * 
	 * @return the expression type 
	 * 
	 * @throws TranspilerException   an exception if the expression type cannot be determined 
	 */
	public static ExpressionType getExpressionType(Transpiler transpiler, TypeMirror type) throws TranspilerException {
		if (type instanceof ArrayType at) {
			TypeMirror componentType = at.getComponentType();
			int dim = ExpressionArrayType.getDimension(at.toString());
			while (componentType instanceof ArrayType)
				componentType = ((ArrayType)(componentType)).getComponentType();
			if (componentType.getKind().isPrimitive()) {
				return new ExpressionArrayType(new ExpressionPrimitiveType(componentType.getKind()), dim);
			}
			if (componentType.getKind() == TypeKind.TYPEVAR) {
				return new ExpressionArrayType(ExpressionTypeVar.getExpressionTypeVariable(transpiler, componentType), dim);
			}
			return new ExpressionArrayType(ExpressionClassType.getExpressionClassType(transpiler, componentType), dim);
		}
		if (type.getKind().isPrimitive())
			return new ExpressionPrimitiveType(type.getKind());
		if (type.getKind() == TypeKind.TYPEVAR)
			return ExpressionTypeVar.getExpressionTypeVariable(transpiler, type);
		return ExpressionClassType.getExpressionClassType(transpiler, type);
	}
	
	
	/**
	 * Determines the expression type for the specified abstract syntax tree (AST) 
	 * node specified by the parameter type.
	 * 
	 * @param transpiler   the transpiler used for determining the expression type 
	 * @param type         the AST node 
	 * 
	 * @return the expression type 
	 * 
	 * @throws TranspilerException   an exception if the expression type cannot be determined 
	 */
	public static ExpressionType getExpressionType(Transpiler transpiler, Tree type) throws TranspilerException {
		if (type instanceof ExpressionType et)
			return et;
		if (type instanceof LiteralTree literal) {
			return switch(literal.getKind()) {
				case INT_LITERAL -> new ExpressionPrimitiveType(TypeKind.INT); 
				case LONG_LITERAL -> new ExpressionPrimitiveType(TypeKind.LONG);
				case FLOAT_LITERAL -> new ExpressionPrimitiveType(TypeKind.FLOAT);
				case DOUBLE_LITERAL -> new ExpressionPrimitiveType(TypeKind.DOUBLE);
				case BOOLEAN_LITERAL -> new ExpressionPrimitiveType(TypeKind.BOOLEAN);
				case CHAR_LITERAL -> new ExpressionPrimitiveType(TypeKind.CHAR);
				case STRING_LITERAL -> ExpressionClassType.getExpressionClassType(transpiler, transpiler.getTypeElement("java.lang.String"));
				case NULL_LITERAL -> new ExpressionTypeNull();
				default -> throw new IllegalArgumentException("Transpiler Error: Unexpected literal type kind: " + literal.getKind());
			};			
		}
		if (type instanceof ClassTree ct)
			return ExpressionClassType.getExpressionClassType(transpiler, (TypeElement)transpiler.getElement(ct));
		if (type instanceof MemberSelectTree mst) // occurs when using lambda expressions and contains class type information
			return ExpressionClassType.getExpressionClassType(transpiler, mst);
		if (type instanceof ParameterizedTypeTree parameterizedType)
			return ExpressionClassType.getExpressionClassType(transpiler, parameterizedType);
		if (type instanceof PrimitiveTypeTree primitiveType) {
			TypeKind kind = primitiveType.getPrimitiveTypeKind(); 
			if (ExpressionTypeNone.isNone(kind))
				return new ExpressionTypeNone(kind);
			return new ExpressionPrimitiveType(kind);
		}
		if (type instanceof IdentifierTree identifier) {
			Element e = transpiler.getElement(identifier);
			if (e instanceof TypeElement te) {
				return ExpressionClassType.getExpressionClassType(transpiler, te);
			}
			if (e instanceof TypeParameterElement tpe) {
				return ExpressionTypeVar.getExpressionTypeVariable(transpiler, tpe.asType());
			}
			if (e instanceof VariableElement ve) {
				return ExpressionType.getExpressionType(transpiler, ve.asType());
			}
			throw new TranspilerException("Transpiler Error: Unexpected identifier expression type " + type.toString() + " for element kind " + e.getKind() + ".");
		}
		if (type instanceof ArrayTypeTree arrayType)
			return new ExpressionArrayType(getExpressionType(transpiler, arrayType.getType()), 1);
		if (type instanceof NewArrayTree newArray) {
			Tree baseType = newArray.getType();
			if (baseType != null) {
				while (baseType instanceof ArrayTypeTree at)
					baseType = at.getType();
				long dim = newArray.getDimensions().size();
				if (dim == 0) {
					// TODO analyze newArray.getInitializers() with multiple dimensions
					dim = 1;					
				}
				return new ExpressionArrayType(ExpressionType.getExpressionType(transpiler, baseType), dim);
			}
			List<? extends ExpressionTree> tmp = newArray.getInitializers();
			if (tmp.size() == 0)
				return new ExpressionTypeNone(TypeKind.NONE);
			// TODO improve array initializer analysis - check all list elements and determine a common type
			return new ExpressionArrayType(transpiler.getExpressionType(tmp.get(0)), 1);
		}
		if (type instanceof WildcardTree wt)
			return ExpressionTypeVar.getExpressionTypeVariable(transpiler, wt);
		if (type instanceof AnnotatedTypeTree att)
			return ExpressionType.getExpressionType(transpiler, att.getUnderlyingType());
		if (type instanceof BinaryTree bt) {
			ExpressionType typeLeft = transpiler.getExpressionType(bt.getLeftOperand()); 
			ExpressionType typeRight = transpiler.getExpressionType(bt.getRightOperand()); 
			switch (bt.getKind()) {
				case PLUS: {
					if (typeLeft.isString())
						return typeLeft;
					if (typeRight.isString())
						return typeRight;
					ExpressionPrimitiveType resultType = ExpressionPrimitiveType.getPromotedType(typeLeft, typeRight);
					if (resultType == null)
						throw new TranspilerException("Transpiler Error: Cannot determine the unboxed numeric promotion type");
					return resultType;
				}
				case MINUS, MULTIPLY, DIVIDE, REMAINDER: {
					ExpressionPrimitiveType resultType = ExpressionPrimitiveType.getPromotedType(typeLeft, typeRight);
					if (resultType == null)
						throw new TranspilerException("Transpiler Error: Cannot determine the unboxed numeric promotion type");
					return resultType;
				}
				case LEFT_SHIFT, RIGHT_SHIFT, UNSIGNED_RIGHT_SHIFT:
					if (typeLeft instanceof ExpressionClassType ect)
						return ExpressionPrimitiveType.getUnboxed(ect);
					if (typeLeft instanceof ExpressionPrimitiveType ept)
						return ept;
					throw new TranspilerException("Transpiler Error: Unhandled type for left operand of shift operator");
				case LESS_THAN, GREATER_THAN, LESS_THAN_EQUAL, GREATER_THAN_EQUAL, EQUAL_TO, NOT_EQUAL_TO:
					return ExpressionPrimitiveType.get("boolean");
				case AND, XOR, OR: {
					// check whether both operands are numeric types, then return the binary numeric promotion type 
					if (typeLeft.isNumberType() && typeRight.isNumberType()) {
						ExpressionPrimitiveType resultType = ExpressionPrimitiveType.getPromotedType(typeLeft, typeRight);
						if (resultType == null)
							throw new TranspilerException("Transpiler Error: Cannot determine the unboxed numeric promotion type");
						return resultType;
					}
					return ExpressionPrimitiveType.get("boolean");  
				}
				case CONDITIONAL_AND, CONDITIONAL_OR:
					return ExpressionPrimitiveType.get("boolean");  
				default:
					throw new TranspilerException("Transpiler Error: Unexpected binary operator result type for expression type of kind " + type.getKind() + ".");
			}
		}
		if (type instanceof ExpressionTree et) {
			ExpressionType resultType = transpiler.getExpressionType(et);
			if (resultType != null)
				return resultType;
		}
		throw new TranspilerException("Transpiler Error: Unexpected expression type " + type.toString() + " of kind " + type.getKind() + ".");		
	}

	
	@Override
	public int hashCode() {
		throw new TranspilerException("Transpiler Error: no override for hashCode method in subclass.");
	}

	
	@Override
	public boolean equals(Object obj) {
		throw new TranspilerException("Transpiler Error: no override for equals method in subclass.");
	}

	
	
}
