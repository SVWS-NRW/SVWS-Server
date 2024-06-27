package de.svws_nrw.transpiler;

import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.Tree;


/**
 * The specialized {@link ExpressionType} if the type is an array type.
 */
public final class ExpressionArrayType extends ExpressionType implements ArrayTypeTree {

	/** the element type of the array */
	private final ExpressionType type;

	/** the number of dimensions of the array */
	private final long dim;


	/**
	 * Creates a new instance of the array expression type with the specified element
	 * type and the specified number of dimensions. If the element type is an array
	 * expression type, its element type is reused and its number of dimensions is added
	 * to the number of dimensions specified in the constrcutor parameter.
	 *
	 * @param type   the element type
	 * @param dim    the number of dimensions
	 */
	public ExpressionArrayType(final ExpressionType type, final long dim) {
		super(Kind.ARRAY_TYPE);
		if (type instanceof final ExpressionArrayType eat) {
			this.type = eat.type;
			this.dim = eat.dim + dim;
		} else {
			this.type = type;
			this.dim = dim;
		}
	}



	@Override
	public boolean isPrimitiveOrBoxedPrimitive() {
		return false;
	}


	@Override
	public int isAssignable(final Transpiler transpiler, final ExpressionType other) {
		if (other instanceof final ExpressionArrayType otherArray) {
			if (this.dim != otherArray.dim)
				return -1;
			return this.type.isAssignable(transpiler, otherArray.type);
		}
		if (other instanceof ExpressionTypeNull)
			return 1;
		return -1;
	}


	/**
	 * Returns the number of dimensions used in the specified type string (e.g. "int[][]").
	 *
	 * @param typeString   the type string
	 *
	 * @return the number of dimensions
	 */
	public static int getDimension(final String typeString) {
		return (typeString.length() - typeString.replace("[]", "").length()) / 2;
	}


	/**
	 * This method returns the expression type if the array subscript operator [?] is
	 * used on a variable of this expression type. This can either be the element type
	 * or an array expression type if this array is a multidimensional array.
	 * Effectively the number of dimensions is reduced by one.
	 *
	 * @return the expression type after using the array subscript operator
	 */
	public ExpressionType getAccessed() {
		long tmpDim = this.dim;
		tmpDim--;
		if (tmpDim == 0)
			return this.type;
		return new ExpressionArrayType(this.type, tmpDim);
	}


	/**
	 * Returns the number of dimensions of the array type
	 *
	 * @return the number of dimensions
	 */
	public long getDimensions() {
		return dim;
	}


	@Override
	public Tree getType() {
		return type;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(type.toString());
		for (int i = 0; i < dim; i++)
			sb.append("[]");
		return sb.toString();
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = getKind().hashCode();
		result = (prime * result) + (int) (dim ^ (dim >>> 32));
		result = (prime * result) + ((type == null) ? 0 : type.hashCode());
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
		final ExpressionArrayType other = (ExpressionArrayType) obj;
		if (getKind() != other.getKind())
			return false;
		if (dim != other.dim)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
