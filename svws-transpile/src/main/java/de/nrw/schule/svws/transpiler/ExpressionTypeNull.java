package de.nrw.schule.svws.transpiler;

/**
 * The specialized {@link ExpressionType} if the type is a no type.
 */
public final class ExpressionTypeNull extends ExpressionType {

	/**
	 * Create the no type instance that represents an unspecfied 
	 * type for null literals.
	 */
	protected ExpressionTypeNull() {
		super(Kind.NULL_LITERAL);
	}


	@Override
	public boolean isPrimitiveOrBoxedPrimitive() {
		return false;
	}


	@Override
	public int isAssignable(Transpiler transpiler, ExpressionType other) {
		if (other instanceof ExpressionClassType)
			return 1;
		if (other instanceof ExpressionArrayType)
			return 1;
		if (other instanceof ExpressionTypeVar)
			return 2;
		return -1;
	}


	@Override
	public int hashCode() {
		return getKind().hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		ExpressionTypeNull other = (ExpressionTypeNull) obj;
		if (getKind() != other.getKind())
			return false;
		return true;
	}	
	
}
