package de.svws_nrw.transpiler;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

import com.sun.source.tree.Tree;
import com.sun.source.tree.WildcardTree;

/**
 * The specialized {@link ExpressionType} if the type is a type
 * variable, e.g. the type variable "T" used in the type "Comparable T".
 */
public final class ExpressionTypeVar extends ExpressionType {

	/** the name of the type variable or null for the wildcard ? */
	private final String name;

    /** the extends bound of the type variable if it is a wildcard type (? extends T) */
    private ExpressionType extendsBound;

    /** the super bound of the type variable if it is a wildcard type (? super T) */
    private ExpressionType superBound;



	@Override
	public boolean isPrimitiveOrBoxedPrimitive() {
		return false;
	}


	@Override
	public int isAssignable(final Transpiler transpiler, final ExpressionType other) {
		if (other instanceof final ExpressionTypeVar otherETV) {
			if ("?".equals(otherETV.name) && (otherETV.extendsBound != null))
				return isAssignable(transpiler, otherETV.extendsBound);
			if (this.name.equals("?"))
				return 1;
			// TODO check bounds
			if (this.name.equals(otherETV.name))
				return 1;
		}
		if (other instanceof final ExpressionTypeNull otherNull) {
			// TODO check not null annotations
			return 2;
		}
		return -1;
	}


	/**
	 * Creates a new type variable
	 *
	 * @param tv   the type variable
	 */
	private ExpressionTypeVar(final TypeVariable tv) {
		super(Kind.TYPE_PARAMETER);
		this.name = (tv == null) ? "?" : tv.asElement().getSimpleName().toString();
	}


	/**
	 * Creates a new type variable expression type instance from the specified {@link Tree}
	 *
	 * @param transpiler   the transpiler used for determining the expression type
	 * @param type         the type tree node
	 *
	 * @return the new type variable expression type instance
	 *
	 * @throws TranspilerException   an exception if analyzing the type arguments fails
	 */
	public static ExpressionTypeVar getExpressionTypeVariable(final Transpiler transpiler, final Tree type) throws TranspilerException {
		if (type instanceof final WildcardTree wt) {
			final ExpressionTypeVar result = new ExpressionTypeVar(null);
			switch (wt.getKind()) {
				case UNBOUNDED_WILDCARD -> { /**/ }
				case EXTENDS_WILDCARD -> result.extendsBound = ExpressionType.getExpressionType(transpiler, wt.getBound());
				case SUPER_WILDCARD -> result.superBound = ExpressionType.getExpressionType(transpiler, wt.getBound());
				default ->
					throw new TranspilerException("Transpiler Error: Unexpected expression type " + type.toString() + " of kind " + type.getKind() + ".");
			}
			return result;
		}
		throw new TranspilerException("Transpiler Error: Unexpected expression type " + type.toString() + " of kind " + type.getKind() + ".");
	}


	/**
	 * Creates a new type variable expression type instance from the specified {@link TypeMirror}
	 *
	 * @param transpiler   the transpiler used for determining the expression type
	 * @param type         the type mirror
	 *
	 * @return the new type variable expression type instance
	 *
	 * @throws TranspilerException   an exception if analyzing the type arguments fails
	 */
	public static ExpressionTypeVar getExpressionTypeVariable(final Transpiler transpiler, final TypeMirror type) throws TranspilerException {
		if (type instanceof final TypeVariable tv)
			return new ExpressionTypeVar(tv);
		if (type instanceof final WildcardType wt) {
			final ExpressionTypeVar result = new ExpressionTypeVar(null);
			if (wt.getSuperBound() != null)
				result.superBound = ExpressionType.getExpressionType(transpiler, wt.getSuperBound());
			if (wt.getExtendsBound() != null)
				result.extendsBound = ExpressionType.getExpressionType(transpiler, wt.getExtendsBound());
			return result;
		}
		throw new TranspilerException("Transpiler Error: Type Variable of kind " + type.getKind() + " not supported.");
	}


	/**
	 * Returns true if this type variable is a wildcard type with or without
	 * bounds (extends or super).
	 *
	 * @return true if this type varirable ist a wildcard type variable and false otherwise
	 */
	public boolean isWildcard() {
		return "?".equals(name);
	}


	/**
	 * Returns the name of the type variable
	 *
	 * @return the name of the type variable
	 */
	public String getName() {
		return name;
	}


	/**
	 * Returns the extends bound expression type if this type variable is
	 * a wildcard type of kind '? extends T'.
	 *
	 * @return the extends bound
	 */
	public ExpressionType getExtendsBound() {
		return extendsBound;
	}


	/**
	 * Returns the super bound expression type if this type variable is a
	 * wildcard type of kind '? super T'.
	 *
	 * @return the super bound expression type
	 */
	public ExpressionType getSuperBound() {
		return superBound;
	}


	@Override
	public String toString() {
		return this.getName();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = getKind().hashCode();
		result = prime * result + ((extendsBound == null) ? 0 : extendsBound.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((superBound == null) ? 0 : superBound.hashCode());
		return result;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		final ExpressionTypeVar other = (ExpressionTypeVar) obj;
		if (getKind() != other.getKind())
			return false;
		if (extendsBound == null) {
			if (other.extendsBound != null)
				return false;
		} else if (!extendsBound.equals(other.extendsBound))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (superBound == null) {
			if (other.superBound != null)
				return false;
		} else if (!superBound.equals(other.superBound))
			return false;
		return true;
	}


}
