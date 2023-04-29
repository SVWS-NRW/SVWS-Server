package de.svws_nrw.transpiler;

import javax.lang.model.type.TypeKind;

import com.sun.source.tree.PrimitiveTypeTree;

/**
 * The specialized {@link ExpressionType} if the type is a no type.
 */
public final class ExpressionTypeNone extends ExpressionType implements PrimitiveTypeTree {

	/** the type king of this expression type (void or none) */
	private final TypeKind typeKind;

	/**
	 * Create the no type instance that can either represent a void or
	 * none {@link TypeKind}.
	 *
	 * @param typeKind   the type kind - must be VOID or NONE
	 *
	 * @throws TranspilerException if the type kind is invalid
	 */
	protected ExpressionTypeNone(final TypeKind typeKind) throws TranspilerException {
		super(Kind.PRIMITIVE_TYPE);
		if (!isNone(typeKind))
			throw new TranspilerException("Transpiler Error: TypeKind " + typeKind + " not valid for the expression type none or void.");
		this.typeKind = typeKind;
	}


	@Override
	public boolean isPrimitiveOrBoxedPrimitive() {
		return false;
	}


	@Override
	public int isAssignable(final Transpiler transpiler, final ExpressionType other) {
		return -1;
	}


	/**
	 * Checks whether the specified {@link TypeKind} is a VOID or NONE
	 * type kind.
	 *
	 * @param typeKind   the type kind
	 *
	 * @return true if the type kind is VOID or NONE
	 */
	public static boolean isNone(final TypeKind typeKind) {
		return ((typeKind == TypeKind.VOID) || (typeKind == TypeKind.NONE));
	}

	@Override
	public TypeKind getPrimitiveTypeKind() {
		return typeKind;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = getKind().hashCode();
		result = prime * result + ((typeKind == null) ? 0 : typeKind.hashCode());
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
		final ExpressionTypeNone other = (ExpressionTypeNone) obj;
		if (getKind() != other.getKind())
			return false;
		return typeKind == other.typeKind;
	}

}
