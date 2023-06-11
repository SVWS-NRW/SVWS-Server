package de.svws_nrw.transpiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;


/**
 * The specialized {@link ExpressionType} if the type is a class type.
 */
public final class ExpressionClassType extends ExpressionType {

	/** the package name of the class */
	private final String packageName;

	/** the simple class name */
	private final String className;


	/** the type arguments if the class type is a parameterized type */
	private final List<ExpressionType> typeArguments = new ArrayList<>();

	/** the type variables if the class is a parameterized type */
	private final List<ExpressionTypeVar> typeVariables = new ArrayList<>();


	/**
	 * Creates a new expression class type instance from the specified {@link TypeElement}
	 *
	 * @param kind          the kind of the class expression type
	 * @param className     the class name
	 * @param packageName   the package name
	 */
	private ExpressionClassType(final Kind kind, final String className, final String packageName) {
		super(kind);
		this.className = className;
		this.packageName = packageName;
	}


	/**
	 * Creates a new expression interface type instance from the specified paramaters.
	 *
	 * @param name          the interface name
	 * @param packageName   the packagename of the interface
	 *
	 * @return the new expression class type instance
	 */
	public static ExpressionClassType getExpressionInterfaceType(final String name, final String packageName) {
		return new ExpressionClassType(Kind.INTERFACE, name, packageName);
	}



	/**
	 * Creates a new expression class type instance for the unknown super class.
	 *
	 * @return the new expression class type instance
	 */
	public static ExpressionClassType getExpressionSuperClassType() {
		return new ExpressionClassType(Kind.CLASS, "super", "");
	}

	/**
	 * Creates a new expression class type instance from the specified {@link TypeMirror}
	 *
	 * @param transpiler   the transpiler used for determining the expression type
	 * @param type   the type mirror
	 *
	 * @return the new expression class type instance
	 *
	 * @throws TranspilerException   an exception if analyzing the type arguments fails
	 */
	public static ExpressionClassType getExpressionClassType(final Transpiler transpiler, final TypeMirror type) throws TranspilerException {
		if (type instanceof final DeclaredType dt) {
			final ExpressionClassType result = new ExpressionClassType(
				dt.getTypeArguments().isEmpty() ? Kind.CLASS : Kind.PARAMETERIZED_TYPE,
				getClassName(dt.toString()),
				getPackageName(dt.toString())
			);
			for (final TypeMirror typeArg : dt.getTypeArguments()) {
				if (typeArg instanceof DeclaredType) {
					result.typeVariables.add(ExpressionTypeVar.getWildcardExpressionTypeVariable());
					result.typeArguments.add(getExpressionClassType(transpiler, typeArg));
					continue;
				}
				if ((typeArg instanceof TypeVariable) || (typeArg instanceof WildcardType)) {
					result.typeVariables.add(ExpressionTypeVar.getExpressionTypeVariable(transpiler, typeArg));
					result.typeArguments.add(ExpressionTypeVar.getExpressionTypeVariable(transpiler, typeArg));
					continue;
				}
				if (typeArg instanceof PrimitiveType)
					throw new TranspilerException("Transpiler Error: Primitive Types cannot be used as type argument");
				if (typeArg instanceof final ArrayType at) {
					int dim = 1;
					TypeMirror elemType = at.getComponentType();
					// iterate to retrieve the element type and the dimension
					while (elemType instanceof final ArrayType ate) {
						elemType = ate.getComponentType();
						dim++;
					}
					if (elemType instanceof DeclaredType) {
						result.typeVariables.add(ExpressionTypeVar.getWildcardExpressionTypeVariable());
						result.typeArguments.add(new ExpressionArrayType(ExpressionType.getExpressionType(transpiler, elemType), dim));
						continue;
					}
					if (elemType instanceof final TypeVariable tv) {
						result.typeVariables.add(ExpressionTypeVar.getWildcardExpressionTypeVariable());
						result.typeVariables.add(ExpressionTypeVar.getExpressionTypeVariable(transpiler, tv));
						continue;
					}
					throw new TranspilerException("Transpiler Error: Array type Argument of kind " + elemType.getKind() + " not supported.");
				}
			}
			return result;
		}
		return new ExpressionClassType(
			Kind.CLASS,
			getClassName(type.toString()),
			getPackageName(type.toString())
		);
	}


	/**
	 * Creates a new expression class type instance from the specified {@link TypeElement}
	 *
	 * @param transpiler   the transpiler used for determining the expression type
	 * @param elem         the type element
	 *
	 * @return the new expression class type instance
	 */
	public static ExpressionClassType getExpressionClassType(final Transpiler transpiler, final TypeElement elem) {
		if ((transpiler == null) || (elem == null))
			throw new NullPointerException();
		Kind kind = Kind.PARAMETERIZED_TYPE;
		if (elem.getTypeParameters().isEmpty())
			kind = (elem.getKind() == ElementKind.ENUM ? Kind.ENUM : Kind.CLASS);
		final ExpressionClassType result = new ExpressionClassType(kind, elem.getSimpleName().toString(), getPackageName(elem.getQualifiedName().toString()));
		for (final TypeParameterElement tpe : elem.getTypeParameters()) {
			result.typeVariables.add(ExpressionTypeVar.getWildcardExpressionTypeVariable());
			result.typeArguments.add(ExpressionType.getExpressionType(transpiler, tpe.asType()));
		}
		return result;
	}


	/**
	 * Creates a new expression class type instance from the specified {@link ParameterizedTypeTree}
	 *
	 * @param transpiler   the transpiler used for determining the expression type
	 * @param tree         the parameterized type tree node
	 *
	 * @return the new expression class type instance
	 */
	public static ExpressionClassType getExpressionClassType(final Transpiler transpiler, final ParameterizedTypeTree tree) {
		final ExpressionClassType temp = (ExpressionClassType) getExpressionType(transpiler, tree.getType());
		final TypeMirror type = transpiler.getTypeMirror(tree);
		if (type != null)
			return getExpressionClassType(transpiler, type);
		final ExpressionClassType result = new ExpressionClassType(
			Kind.PARAMETERIZED_TYPE,
			temp.toString(),
			temp.getPackageName()
		);
		for (final Tree typeArgument : tree.getTypeArguments()) {
			result.typeVariables.add(ExpressionTypeVar.getWildcardExpressionTypeVariable());
			result.typeArguments.add(getExpressionType(transpiler, typeArgument));
		}
		return result;
	}


	/**
	 * Creates a new expression class type instance from the specified {@link MemberSelectTree}
	 *
	 * @param transpiler   the transpiler used for determining the expression type
	 * @param tree         the member select type tree node
	 *
	 * @return the new expression class type instance
	 */
	public static ExpressionClassType getExpressionClassType(final Transpiler transpiler, final MemberSelectTree tree) {
		// TODO improve type checks
		if (tree.getExpression() instanceof final IdentifierTree ident) {
			final ExpressionType et = ExpressionType.getExpressionType(transpiler, ident);
			if (et instanceof final ExpressionClassType ect) {
				// TODO incorrect class type are generated - check with: System.err.println(ect.getFullQualifiedName() + "   " + tree.getExpression().toString() + "   " + tree.getIdentifier().toString());
				return new ExpressionClassType(
					Kind.PARAMETERIZED_TYPE,
					tree.getIdentifier().toString(),
					ect.getFullQualifiedName()
				);
			}
		}
		return new ExpressionClassType(Kind.PARAMETERIZED_TYPE, tree.getIdentifier().toString(), tree.getExpression().toString());
	}


	/**
	 * Creates a new parameterized expression class type instance for java.lang.Class with
	 * the specified parameter class type.
	 *
	 * @param transpiler    the transpiler used for determining the expression type
	 * @param clParamType   the parameter class type
	 *
	 * @return the new expression class type instance
	 */
	public static ExpressionClassType getExpressionClassType(final Transpiler transpiler, final ExpressionClassType clParamType) {
		final ExpressionClassType result = new ExpressionClassType(Kind.PARAMETERIZED_TYPE, "Class", "java.lang");
		result.typeArguments.add(clParamType);
		return result;
	}


	/**
	 * Create a new class type instance for the specified identifier node object.
	 *
	 * @param transpiler    the transpiler used for determining the expression type
	 * @param node          the identifier node instance
	 *
	 * @return the new expression class type instance or null on error
	 */
	public static ExpressionClassType getExpressionClassType(final Transpiler transpiler, final IdentifierTree node) {
		final TranspilerUnit transpilerUnit = transpiler.getTranspilerUnit(node);
		TreePath curPath = transpilerUnit.mapTreePath.get(node).getParentPath();
		while (curPath.getLeaf() instanceof MemberSelectTree)
			curPath = curPath.getParentPath();
		final Tree curNode = curPath.getLeaf();
		if (curNode instanceof final VariableTree vt) {
			final String strType = vt.getType().toString();
			final int pos = strType.lastIndexOf(".");
			final String packageName = strType.substring(0, pos);
			final String className = strType.substring(pos + 1);
			return new ExpressionClassType(Kind.CLASS, className, packageName);
		}
		return null;
	}


	/**
	 * Returns the class name part of the specified class type string
	 *
	 * @param classType   the class type string
	 *
	 * @return the simple class name
	 */
	private static String getClassName(final String classType) {
		// remove type parameters
		final String className = classType.replaceAll("<.*>", "");
		// determine the last dot...
		final int lastDot = className.lastIndexOf('.');
		// ...and remove all before the simple class name, which is at the end
		return (lastDot < 0) ? className : className.substring(lastDot + 1);
	}


	/**
	 * Returns the package part of the specified class type string
	 *
	 * @param classType   the class type string
	 *
	 * @return the package name extracted from the specified class type string, if the class
	 * type string was a qualified class type string and null otherwise
	 */
	private static String getPackageName(final String classType) {
		// remove annotations
		final String classTypeWithoutAnnotations = classType.replaceAll("@\\S+\\s", "");
		// remove type parameters
		final String className = classTypeWithoutAnnotations.replaceAll("<.*>", "");
		// determine the last dot...
		final int lastDot = className.lastIndexOf('.');
		// ...and remove the simple class name at the end
		return (lastDot < 0) ? null : className.substring(0, lastDot);
	}


	/**
	 * Returns the package name.
	 *
	 * @return the package name
	 */
	public String getPackageName() {
		return this.packageName;
	}


	/**
	 * Returns the full qualified name of the class, i.e. without the list
	 * of type parameters if type parameters exist
	 *
	 * @return the full qualified name of the class
	 */
	public String getFullQualifiedName() {
		return this.packageName + "." + this.className;
	}


	/**
	 * Returns the number of type arguments/variables of this class
	 * type.
	 *
	 * @return the number of type arguments/variables
	 */
	public int getTypeArgumentCount() {
		return Math.max(this.typeArguments.size(), this.typeVariables.size());
	}


	/**
	 * Resolves the type variables using the specified map and writes the
	 * expression type into the typeArguments list.
	 *
	 * @param knownTypeVars   the mapping of type variables to the type arguments
	 *
	 * @return true on success and false if not all type variables could be resolved
	 */
	public boolean resolveTypeVariables(final HashMap<String, ExpressionType> knownTypeVars) {
		// TODO improvement: allow mixed generic parameters with type variables and fixed types
		// TODO improvement: replace type variables recursively - see comment in Transpiler
		if (typeVariables.isEmpty())
			return true;
		for (int i = 0; i < typeVariables.size(); i++) {
			if (typeVariables.get(i) != typeArguments.get(i))
				continue;
			final String tvName = typeVariables.get(i).getName();
			final ExpressionType t = knownTypeVars.get(tvName);
			if ((t == null) && (!isWildcardType(tvName))) {
				typeArguments.clear();
				return false;
			}
			typeArguments.add(t);
		}
		return true;
	}


	private static boolean isWildcardType(final String typename) {
		return typename.contains("?");
	}


	/**
	 * Returns the list of type arguments of this class. If no
	 * type arguments are available an empty list is returned.
	 *
	 * @return the list of type arguments
	 */
	public List<ExpressionType> getTypeArguments() {
		return this.typeArguments;
	}


	/**
	 * Returns the list of type variables of this class. If no
	 * type variables are available an empty list is returned.
	 *
	 * @return the list of type variables
	 */
	public List<ExpressionTypeVar> getTypeVariables() {
		return this.typeVariables;
	}


	@Override
	public boolean isPrimitiveOrBoxedPrimitive() {
		return ExpressionPrimitiveType.getUnboxed(this) != null;
	}


	@Override
	public boolean isNumberType() {
		if (!"java.lang".equals(packageName))
			return false;
		return switch (className) {
			case "Byte", "Short", "Integer", "Long" -> true;
			case "Float", "Double" -> true;
			default -> false;
		};
	}


	@Override
	public boolean isIntegerType() {
		if (!"java.lang".equals(packageName))
			return false;
		return switch (className) {
			case "Byte", "Short", "Integer", "Long" -> true;
			default -> false;
		};
	}


	@Override
	public int isAssignable(final Transpiler transpiler, final ExpressionType other) {
		if (other instanceof final ExpressionPrimitiveType otherPrimitive) {
			if ("Object".equals(this.className) && "java.lang".equals(this.packageName))
				return 2;  // unboxed and assigned to a super type
			final ExpressionPrimitiveType thisPrimitive = ExpressionPrimitiveType.getUnboxed(this);
			if (thisPrimitive == null)
				return -1;
			return thisPrimitive.isAssignable(transpiler, otherPrimitive) == -1 ? -1 : 1; // if it is assignable increase by one
		}
		if (other instanceof final ExpressionClassType otherClass) {
			return transpiler.checkForSuperclass(otherClass, this);
		}
		if ((other instanceof ExpressionArrayType) && ("Object".equals(this.className)) && ("java.lang".equals(this.packageName)))
			return 1;
		if (other instanceof final ExpressionTypeLambda otherLambda) {
			final List<? extends ExpressionType> paramTypes = otherLambda.getParamTypes();
			final ExpressionType resultType = otherLambda.getResultType();
			return switch (getFullQualifiedName()) {
				case "java.util.function.Consumer" -> ((resultType == null) && (paramTypes.size() == 1)) ? 1 : -1;
				case "java.util.Comparator" -> {
					if (!"int".equals(resultType.toString()))
						yield -1;
					if (paramTypes.size() != 2)
						yield -1;
					yield paramTypes.get(0).equals(paramTypes.get(1)) ? 1 : -1;
				}
				case "java.util.function.Function" -> {
					if (resultType == null)
						yield -1;
					if (paramTypes.size() != 1)
						yield -1;
					if (this.typeArguments.size() != 2)
						yield -1;
					if (this.typeArguments.get(1).isAssignable(transpiler, resultType) < 0)
						yield -1;
					if (this.typeArguments.get(0).isAssignable(transpiler, paramTypes.get(0)) < 0)
						yield -1;
					yield 1;
				}
				case "java.util.function.Predicate" -> {
					if (!"boolean".equals(resultType.toString()))
						yield -1;
					if (paramTypes.size() != 1)
						yield -1;
					yield 1;
				}
				default -> throw new TranspilerException("Transpiler Error: Lambda expression type checking for type " + getFullQualifiedName() + " not yet supported.");
			};
		}
		if (other instanceof ExpressionTypeNull)
			return 1;
		if (other instanceof ExpressionTypeVar) {
			if ("Object".equals(this.className) && "java.lang".equals(this.packageName))
				return 1;
			// TODO
		}
		return -1;
	}


	@Override
	public String toString() {
		return this.className;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = getKind().hashCode();
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((packageName == null) ? 0 : packageName.hashCode());
		result = prime * result + typeArguments.hashCode();
		result = prime * result + typeVariables.hashCode();
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
		final ExpressionClassType other = (ExpressionClassType) obj;
		if (getKind() != other.getKind())
			return false;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		if (!typeArguments.equals(other.typeArguments))
			return false;
		return (typeVariables.equals(other.typeVariables));
	}

}
