package de.svws_nrw.transpiler;

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
	public int isAssignable(final Transpiler transpiler, final ExpressionType other) {
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
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ExpressionTypeNull other = (ExpressionTypeNull) obj;
		return getKind() == other.getKind();
	}

}
