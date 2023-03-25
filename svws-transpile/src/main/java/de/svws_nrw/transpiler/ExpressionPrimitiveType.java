package de.svws_nrw.transpiler;

import javax.lang.model.type.TypeKind;

import com.sun.source.tree.PrimitiveTypeTree;

/**
 * The specialized {@link ExpressionType} if the type is a primitive type.
 */
public class ExpressionPrimitiveType extends ExpressionType implements PrimitiveTypeTree {

	/** the {@link TypeKind} of this primitive type */
	private final TypeKind typeKind;

	/**
	 * Creates a primitive expression type instance for the specified {@link TypeKind}.
	 *  
	 * @param typeKind   the type kind of the primitive type
	 *   
	 * @throws TranspilerException   an exception if the specified type kind is not a primitive type kind  
	 */
	public ExpressionPrimitiveType(TypeKind typeKind) throws TranspilerException {
		super(Kind.PRIMITIVE_TYPE);
		if (!typeKind.isPrimitive())
			throw new TranspilerException("Transpiler Error: TypeKind " + typeKind + " is not a primitive type kind");
		this.typeKind = typeKind;
	}
	
	/**
	 * Creates a primitive expression type instance for the specified primitive type string.
	 * 
	 * @param type   the primitive type string (e.g. "float")
	 * 
	 * @throws TranspilerException   an exception if the specified type kind is not a primitive type kind  
	 */
	public ExpressionPrimitiveType(String type) {
		super(Kind.PRIMITIVE_TYPE);
		this.typeKind = switch(type) {
			case "boolean" -> TypeKind.BOOLEAN;
	        case "byte" -> TypeKind.BYTE;
	        case "short" -> TypeKind.SHORT;
	        case "int" -> TypeKind.INT;
	        case "long" -> TypeKind.LONG;
	        case "char" -> TypeKind.CHAR;
	        case "float" -> TypeKind.FLOAT;
	        case "double" -> TypeKind.DOUBLE;
			default -> throw new TranspilerException("Transpiler Error: Type " + type + " is not a primitive type kind"); 
		};
	}
	
	
	@Override
	public boolean isPrimitiveOrBoxedPrimitive() {
		return true;
	}
	
	
	@Override
	public boolean isNumberType() {
		return switch(typeKind) {
	        case BYTE, SHORT, INT, LONG, FLOAT, DOUBLE -> true;
			case BOOLEAN, CHAR -> false;
			default -> throw new TranspilerException("Transpiler Error: Unhandled type kind"); 
		};
	}
	
	
	@Override
	public boolean isIntegerType() {
		return switch(typeKind) {
	        case BYTE, SHORT, INT, LONG -> true;
			case FLOAT, DOUBLE, BOOLEAN, CHAR -> false;
			default -> throw new TranspilerException("Transpiler Error: Unhandled type kind"); 
		};
	}
	
	
	/**
	 * Checks whether the specified primitive type other is assignable to this primitive type 
	 * or not.
	 * 
	 * @param other   the other primitive type
	 * 
	 * @return true if it is assignable and false otherwise
	 */
	@Override
	public int isAssignable(Transpiler transpiler, ExpressionType other) {
		if (other instanceof ExpressionClassType o) {
			ExpressionPrimitiveType op = getUnboxed(o);
			if (op == null)
				return -1;
			return 1 + isAssignable(transpiler, op);
		}
		if (other instanceof ExpressionPrimitiveType o) {
			return switch (typeKind) {
				case BOOLEAN -> o.typeKind == TypeKind.BOOLEAN;
		        case CHAR -> o.typeKind == TypeKind.CHAR;
		        case BYTE -> o.typeKind == TypeKind.BYTE;
		        case SHORT -> o.typeKind == TypeKind.BYTE || o.typeKind == TypeKind.SHORT;
		        case INT -> o.typeKind == TypeKind.BYTE || o.typeKind == TypeKind.SHORT || o.typeKind == TypeKind.INT;
		        case LONG -> o.typeKind == TypeKind.BYTE || o.typeKind == TypeKind.SHORT || o.typeKind == TypeKind.INT || o.typeKind == TypeKind.LONG;
		        case FLOAT -> o.typeKind == TypeKind.BYTE || o.typeKind == TypeKind.SHORT || o.typeKind == TypeKind.INT || o.typeKind == TypeKind.LONG || o.typeKind == TypeKind.FLOAT;
		        case DOUBLE -> o.typeKind == TypeKind.BYTE || o.typeKind == TypeKind.SHORT || o.typeKind == TypeKind.INT || o.typeKind == TypeKind.LONG || o.typeKind == TypeKind.FLOAT || o.typeKind == TypeKind.DOUBLE;
		        default -> throw new TranspilerException("Transpiler Error: TypeKind " + typeKind + " is not a primitive type kind");
			} ? 0 : -1;
		}
		return -1;
	}
	
	@Override
	public TypeKind getPrimitiveTypeKind() {
		return this.typeKind;
	}

	@Override
	public String toString() {
		return switch (typeKind) {
			case BOOLEAN -> "boolean";
	        case BYTE -> "byte";
	        case SHORT -> "short";
	        case INT -> "int";
	        case LONG -> "long";
	        case CHAR -> "char";
	        case FLOAT -> "float";
	        case DOUBLE -> "double";
	        default -> throw new TranspilerException("Transpiler Error: TypeKind " + typeKind + " is not a primitive type kind");
		};
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = getKind().hashCode();
		result = prime * result + ((typeKind == null) ? 0 : typeKind.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		ExpressionPrimitiveType other = (ExpressionPrimitiveType) obj;
		if (getKind() != other.getKind())
			return false;
		if (typeKind != other.typeKind)
			return false;
		return true;
	}


	/**
	 * Determines the primitive type for the specified boxed class type. If the 
	 * classType is invalid null is returned.
	 *  
	 * @param classType   the class name of the boxed type
	 * 
	 * @return the unboxed primitive type or null on error
	 */
	public static ExpressionPrimitiveType getUnboxed(ExpressionClassType classType) {
		return switch(classType.getFullQualifiedName()) {
		    case "java.lang.Boolean" -> new ExpressionPrimitiveType(TypeKind.BOOLEAN);
	        case "java.lang.Byte" -> new ExpressionPrimitiveType(TypeKind.BYTE);
	        case "java.lang.Short" -> new ExpressionPrimitiveType(TypeKind.SHORT);
	        case "java.lang.Integer" -> new ExpressionPrimitiveType(TypeKind.INT);
	        case "java.lang.Long" -> new ExpressionPrimitiveType(TypeKind.LONG);
	        case "java.lang.Character" -> new ExpressionPrimitiveType(TypeKind.CHAR);
	        case "java.lang.Float" -> new ExpressionPrimitiveType(TypeKind.FLOAT);
	        case "java.lang.Double" -> new ExpressionPrimitiveType(TypeKind.DOUBLE);
			default -> null;
		};
	}
	
	
	/**
	 * Returns the primitive type of specified type if it is a primitive or boxed primitive
	 * type.
	 *  
	 * @param type   the specified type
	 * 
	 * @return the primitive type or null
	 */
	public static ExpressionPrimitiveType getPrimitiveType(ExpressionType type) {
		if (type instanceof ExpressionPrimitiveType)
			return new ExpressionPrimitiveType(type.toString());
		if (type instanceof ExpressionClassType ect)
			return ExpressionPrimitiveType.getUnboxed(ect);
		return null;
	}
	
	
	/**
	 * Returns the unboxed binary numeric promotion type of the two specified type.
	 * 
	 * @param type1   the first type
	 * @param type2   teh second type
	 * 
	 * @return the unboxed binary numeric promotion type
	 */
	public static ExpressionPrimitiveType getPromotedType(ExpressionType type1, ExpressionType type2) {
		ExpressionPrimitiveType et1 = ExpressionPrimitiveType.getPrimitiveType(type1);
		ExpressionPrimitiveType et2 = ExpressionPrimitiveType.getPrimitiveType(type2);
		if ((et1 == null) || (et2 == null))
			return null;
		if ((et1.typeKind == TypeKind.DOUBLE) || (et2.typeKind == TypeKind.DOUBLE))
			return new ExpressionPrimitiveType(TypeKind.DOUBLE);
		if ((et1.typeKind == TypeKind.FLOAT) || (et2.typeKind == TypeKind.FLOAT))
			return new ExpressionPrimitiveType(TypeKind.FLOAT);
		if ((et1.typeKind == TypeKind.LONG) || (et2.typeKind == TypeKind.LONG))
			return new ExpressionPrimitiveType(TypeKind.FLOAT);
		if ((et1.typeKind == TypeKind.INT) || (et2.typeKind == TypeKind.INT))
			return new ExpressionPrimitiveType(TypeKind.FLOAT);
		if ((et1.typeKind == TypeKind.SHORT) || (et2.typeKind == TypeKind.SHORT))
			return new ExpressionPrimitiveType(TypeKind.FLOAT);
		if ((et1.typeKind == TypeKind.BYTE) || (et2.typeKind == TypeKind.BYTE))
			return new ExpressionPrimitiveType(TypeKind.FLOAT);
		throw new TranspilerException("Transpiler Error: invalid primitive type kind");
	}


	/**
	 * Determines the primitive type for the primitive type string.
	 * If the string does not represent a valid primitive type
	 * null is returned. 
	 *  
	 * @param typeName   the primitive type string
	 * 
	 * @return the primitive type or null on error
	 */
	public static ExpressionPrimitiveType get(String typeName) {
		try {
			return new ExpressionPrimitiveType(typeName);
		} catch (@SuppressWarnings("unused") TranspilerException te) {
			return null;
		}
	}
	
}
