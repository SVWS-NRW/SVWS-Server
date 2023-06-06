package de.svws_nrw.transpiler;

import java.util.Objects;

import javax.lang.model.element.TypeElement;

/**
 * The specialized {@link ExpressionType} if the type is a package type.
 */
public final class ExpressionPackageType extends ExpressionType {

	/** the package name */
	private final String name;

	/**
	 * Creates a new expression class type instance from the specified {@link TypeElement}
	 *
	 * @param name   the package name
	 */
	private ExpressionPackageType(final String name) {
		super(Kind.PACKAGE);
		this.name = name;
	}

	/**
	 * Creates a new expression package type instance for the specified package name.
	 *
	 * @param name   the package name
	 *
	 * @return the new expression package type instance
	 */
	public static ExpressionPackageType getExpressionPackageType(final String name) {
		return new ExpressionPackageType(name);
	}


	@Override
	public int isAssignable(final Transpiler transpiler, final ExpressionType other) {
		return -1;
	}

	@Override
	public boolean isPrimitiveOrBoxedPrimitive() {
		return false;
	}


	/**
	 * Returns the package name.
	 *
	 * @return the package name
	 */
	public String getName() {
		return this.name;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(name);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ExpressionPackageType other = (ExpressionPackageType) obj;
		return Objects.equals(name, other.name);
	}

}
