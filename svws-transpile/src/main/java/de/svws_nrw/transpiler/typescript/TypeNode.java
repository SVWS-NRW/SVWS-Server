package de.svws_nrw.transpiler.typescript;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.WildcardTree;

import de.svws_nrw.transpiler.ExpressionArrayType;
import de.svws_nrw.transpiler.ExpressionClassType;
import de.svws_nrw.transpiler.ExpressionType;
import de.svws_nrw.transpiler.ExpressionTypeVar;
import de.svws_nrw.transpiler.Transpiler;
import de.svws_nrw.transpiler.TranspilerException;

/**
 * This class is used to handle java type information and transpiles it.
 */
public class TypeNode {

	/** the {@link TranspilerTypeScriptPlugin} used */
	private final TranspilerTypeScriptPlugin plugin;

	/** the {@link Tree} object of the java compiler */
	private final Tree node;

	/** the {@link TypeMirror} object of the java compiler */
	private final TypeMirror typeMirror;

	/** a map with a mapping from a type variable to its resolved type */
	private final Map<String, TypeMirror> resolved;

	/** specifies whether the type is used during type declaration */
	private final boolean decl;

	/** true if the declaration has a not null annotation */
	private final boolean notNull;

	/** indicates whether this is a type for variable arguments */
	private boolean isVarArg;

	/**
	 * Creates a new variable with transpiled variable information.
	 *
	 * @param plugin    the {@link TranspilerTypeScriptPlugin} used
	 * @param node      the {@link Tree} object of the java compiler
	 * @param decl      specifies whether the type is used during type declaration
	 * @param notNull   true if the declaration has a not null annotation
	 */
	public TypeNode(final TranspilerTypeScriptPlugin plugin, final Tree node, final boolean decl, final boolean notNull) {
		this.plugin = plugin;
		this.node = node;
		this.typeMirror = null;
		this.resolved = new HashMap<>();
		this.decl = decl;
		// Korrigiere ggf. die Information zur NotNull-Annotation, falls der Base-Type des parametrisierten Typs eine NotNull-Annotation hat
		if ((node instanceof final ParameterizedTypeTree p) && (p.getType() != null) && (p.getType() instanceof final AnnotatedTypeTree att))
			this.notNull = plugin.getTranspiler().hasNotNullAnnotation(att);
		else if (node instanceof final AnnotatedTypeTree att)
			this.notNull = plugin.getTranspiler().hasNotNullAnnotation(att);
		else
			this.notNull = notNull;
		this.isVarArg = false;
	}


	/**
	 * Creates a new variable with transpiled variable information.
	 *
	 * @param plugin      the {@link TranspilerTypeScriptPlugin} used
	 * @param typeMirror  the {@link TypeMirror} object of the java compiler
	 * @param decl        specifies whether the type is used during type declaration
	 * @param notNull     true if the declaration has a not null annotation
	 * @param resolved    a map with a mapping from a type variable to its resolved type
	 */
	public TypeNode(final TranspilerTypeScriptPlugin plugin, final TypeMirror typeMirror, final boolean decl, final boolean notNull, final Map<String, TypeMirror> resolved) {
		this.plugin = plugin;
		this.node = null;
		this.typeMirror = typeMirror;
		this.resolved = (resolved == null) ? new HashMap<>() : resolved;
		this.decl = decl;
		this.notNull = notNull;
		this.isVarArg = false;
	}


	/**
	 * Returns a copy of this type without the flag that indicates that this type is used during declaration
	 *
	 * @return a copy of this type without the flag that indicates that this type is used during declaration
	 */
	public TypeNode getNoDeclarationType() {
		return new TypeNode(this.plugin, this.node, false, this.notNull);
	}


	/**
	 * Sets the internal flag that this type is used as a type for variable arguments.
	 */
	public void setIsVarArg() {
		this.isVarArg = true;
	}


	/**
	 * Returns whether this type node has a not null annotation or not.
	 *
	 * @return true if the node has a not null annotation or not.
	 */
	public boolean isNotNull() {
		return notNull;
	}


	/**
	 * Returns whether this type is a primitive type or not
	 *
	 * @return true if this is a primitive type
	 */
	public boolean isPrimitive() {
		return node == null ? typeMirror.getKind().isPrimitive() : (node instanceof PrimitiveTypeTree);
	}


	/**
	 * Returns whether this type is a primitive integer type or not
	 *
	 * @return true if this is a primitive integer type
	 */
	public boolean isPrimitveInteger() {
		TypeKind kind = null;
		if ((node != null) && (node instanceof final PrimitiveTypeTree ptt)) {
			kind = ptt.getPrimitiveTypeKind();
		} else if ((typeMirror != null) && (typeMirror.getKind().isPrimitive())) {
			kind = typeMirror.getKind();
		}
		if (kind == null)
			return false;
		return switch (kind) {
			case BYTE, SHORT, INT, LONG -> true;
			default -> false;
		};
	}


	/**
	 * Returns whether this type is a primitive floating point number type or not
	 *
	 * @return true if this is a primitive floating point number type
	 */
	public boolean isPrimitveFloat() {
		TypeKind kind = null;
		if ((node != null) && (node instanceof final PrimitiveTypeTree ptt)) {
			kind = ptt.getPrimitiveTypeKind();
		} else if ((typeMirror != null) && (typeMirror.getKind().isPrimitive())) {
			kind = typeMirror.getKind();
		}
		if (kind == null)
			return false;
		return switch (kind) {
			case FLOAT, DOUBLE -> true;
			default -> false;
		};
	}


	/**
	 * Returns whether this type is a primitive boolean type or not
	 *
	 * @return true if this is a primitive boolean type
	 */
	public boolean isPrimitveBoolean() {
		TypeKind kind = null;
		if ((node != null) && (node instanceof final PrimitiveTypeTree ptt)) {
			kind = ptt.getPrimitiveTypeKind();
		} else if ((typeMirror != null) && (typeMirror.getKind().isPrimitive())) {
			kind = typeMirror.getKind();
		}
		if (kind == null)
			return false;
		return switch (kind) {
			case BOOLEAN -> true;
			default -> false;
		};
	}


	/**
	 * Returns whether this type is a primitive char type or not
	 *
	 * @return true if this is a primitive char type
	 */
	public boolean isPrimitveChar() {
		TypeKind kind = null;
		if ((node != null) && (node instanceof final PrimitiveTypeTree ptt)) {
			kind = ptt.getPrimitiveTypeKind();
		} else if ((typeMirror != null) && (typeMirror.getKind().isPrimitive())) {
			kind = typeMirror.getKind();
		}
		if (kind == null)
			return false;
		return switch (kind) {
			case CHAR -> true;
			default -> false;
		};
	}


	/**
	 * Returns whether the type is java.lang.String.
	 *
	 * @return true if the type is java.lang.String
	 */
	public boolean isString() {
		if (node != null) {
			if (node instanceof final IdentifierTree ident) {
				final ExpressionType type = plugin.getTranspiler().getExpressionType(ident);
				if (type == null)
					throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + ident.getName().toString());
				return ((type instanceof final ExpressionClassType classType) && ("java.lang.String".equals(classType.getFullQualifiedName())));
			} else if (node instanceof final AnnotatedTypeTree att) {
				if (att.getUnderlyingType() instanceof ArrayTypeTree)
					return false;
				final ExpressionType type = plugin.getTranspiler().getExpressionType(att.getUnderlyingType());
				if (type == null)
					throw new TranspilerException("Transpiler Error: Cannot retrieve the type information from annotated type " + att.toString());
				return ((type instanceof final ExpressionClassType classType) && ("java.lang.String".equals(classType.getFullQualifiedName())));
			}
		} else if ((typeMirror != null) && (typeMirror instanceof final DeclaredType dt) && (dt.asElement() instanceof final TypeElement te)) {
			return "java.lang.String".equals(te.getQualifiedName().toString());
		}
		return false;
	}


	/**
	 * Returns whether the type is java.lang.Boolean.
	 *
	 * @return true if the type is java.lang.Boolean
	 */
	public boolean isBoolean() {
		if (node != null) {
			if (node instanceof final IdentifierTree ident) {
				final ExpressionType type = plugin.getTranspiler().getExpressionType(ident);
				if (type == null)
					throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + ident.getName().toString());
				return ((type instanceof final ExpressionClassType classType) && ("java.lang.Boolean".equals(classType.getFullQualifiedName())));
			} else if (node instanceof final AnnotatedTypeTree att) {
				if (att.getUnderlyingType() instanceof ArrayTypeTree)
					return false;
				final ExpressionType type = plugin.getTranspiler().getExpressionType(att.getUnderlyingType());
				if (type == null)
					throw new TranspilerException("Transpiler Error: Cannot retrieve the type information from annotated type " + att.toString());
				return ((type instanceof final ExpressionClassType classType) && ("java.lang.Boolean".equals(classType.getFullQualifiedName())));
			}
		} else if ((typeMirror != null) && (typeMirror instanceof final DeclaredType dt) && (dt.asElement() instanceof final TypeElement te)) {
			return "java.lang.Boolean".equals(te.getQualifiedName().toString());
		}
		return false;
	}


	/**
	 * Returns whether the type is a number class type (e.g. java.lang.Long).
	 *
	 * @return true if the type is a number class type
	 */
	public boolean isNumberClass() {
		if (node != null) {
			if (node instanceof final IdentifierTree ident) {
				final ExpressionType type = plugin.getTranspiler().getExpressionType(ident);
				if (type == null)
					throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + ident.getName().toString());
				return ((type instanceof final ExpressionClassType classType)
						&& (("java.lang.Number".equals(classType.getFullQualifiedName()))
							|| ("java.lang.Byte".equals(classType.getFullQualifiedName()))
							|| ("java.lang.Short".equals(classType.getFullQualifiedName()))
							|| ("java.lang.Integer".equals(classType.getFullQualifiedName()))
							|| ("java.lang.Long".equals(classType.getFullQualifiedName()))
							|| ("java.lang.Float".equals(classType.getFullQualifiedName()))
							|| ("java.lang.Double".equals(classType.getFullQualifiedName()))));
			} else if (node instanceof final AnnotatedTypeTree att) {
				if (att.getUnderlyingType() instanceof ArrayTypeTree)
					return false;
				final ExpressionType type = plugin.getTranspiler().getExpressionType(att.getUnderlyingType());
				if (type == null)
					throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the annotated type " + att.toString());
				return ((type instanceof final ExpressionClassType classType)
						&& (("java.lang.Number".equals(classType.getFullQualifiedName()))
							|| ("java.lang.Byte".equals(classType.getFullQualifiedName()))
							|| ("java.lang.Short".equals(classType.getFullQualifiedName()))
							|| ("java.lang.Integer".equals(classType.getFullQualifiedName()))
							|| ("java.lang.Long".equals(classType.getFullQualifiedName()))
							|| ("java.lang.Float".equals(classType.getFullQualifiedName()))
							|| ("java.lang.Double".equals(classType.getFullQualifiedName()))));
			}
			if (node instanceof MemberSelectTree)
				throw new TranspilerException("Transpiler Error: MemberSelectTree nodes not yet supported in Method isNumberClass()");
		} else if ((typeMirror != null) && (typeMirror instanceof final DeclaredType dt) && (dt.asElement() instanceof final TypeElement te)) {
			return "java.lang.Number".equals(te.getQualifiedName().toString())
				|| "java.lang.Byte".equals(te.getQualifiedName().toString())
				|| "java.lang.Short".equals(te.getQualifiedName().toString())
				|| "java.lang.Integer".equals(te.getQualifiedName().toString())
				|| "java.lang.Long".equals(te.getQualifiedName().toString())
				|| "java.lang.Float".equals(te.getQualifiedName().toString())
				|| "java.lang.Double".equals(te.getQualifiedName().toString());
		}
		return false;
	}



	/**
	 * Returns whether the type is a Java collection type (e.g. java.util.ArrayList).
	 *
	 * @return true if the type is a Java collection type
	 */
	public boolean isCollectionType() {
		if (node != null) {
			if (node instanceof final IdentifierTree ident) {
				final ExpressionType type = plugin.getTranspiler().getExpressionType(ident);
				if (type == null)
					throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + ident.getName().toString());
				return plugin.getTranspiler().checkForSuperclass(type, ExpressionClassType.getExpressionInterfaceType("Collection", "java.util")) >= 0;
			}
			if (node instanceof final ParameterizedTypeTree ptt)
				return getParameterizedTypeBaseTypeNode(ptt).isCollectionType();
			if (node instanceof MemberSelectTree)
				throw new TranspilerException("Transpiler Error: MemberSelectTree nodes not yet supported in Method isCollectionType()");
		} else if ((typeMirror != null) && (typeMirror instanceof final DeclaredType dt) && (dt.asElement() instanceof final TypeElement te)) {
			return plugin.getTranspiler().checkForSuperclass(te, "java.util.Collection") >= 0;
		}
		return false;
	}


	/**
	 * Returns the type of the i-th type parameter.
	 *
	 * @param i      the number of the type parameter
	 * @param decl   specifies whether the type is used during type declaration
	 *
	 * @return the type of the i-th type parameter or null if this type no parameterized type or i is invalid
	 */
	public TypeNode getParameterType(final int i, final boolean decl) {
		if (((node != null)) && (node instanceof final ParameterizedTypeTree ptt)) {
			final List<? extends Tree> typeArgs = ptt.getTypeArguments();
			if ((typeArgs == null) || (i < 0) || (i >= typeArgs.size()))
				return null;
			return new TypeNode(plugin, typeArgs.get(i), decl, false);
		}
		if ((typeMirror != null) && (typeMirror instanceof final DeclaredType dt) && (dt.asElement() instanceof final TypeElement te)) {
			final List<? extends TypeParameterElement> typeParams = te.getTypeParameters();
			if ((typeParams == null) || (i < 0) || (i >= typeParams.size()))
				return null;
			return new TypeNode(plugin, typeParams.get(i).asType(), decl, false, resolved);
		}
		return null;
	}


	/**
	 * Returns whether the type is a Java array type
	 *
	 * @return true if the type is a Java array type
	 */
	public boolean isArrayType() {
		if (node != null) {
			if (node instanceof final IdentifierTree ident) {
				final ExpressionType type = plugin.getTranspiler().getExpressionType(ident);
				if (type == null)
					throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + ident.getName().toString());
				return (type instanceof ExpressionArrayType);
			}
			if (node instanceof MemberSelectTree)
				throw new TranspilerException("Transpiler Error: MemberSelectTree nodes not yet supported in Method isArrayType()");
			if ((node instanceof ArrayTypeTree) || ((node instanceof final AnnotatedTypeTree att) && (att.getUnderlyingType() instanceof ArrayTypeTree)))
				return true;
			throw new TranspilerException("Transpiler Error: Nodes of kind " + this.node.getKind() + " not yet supported in Method isArrayType()");
		}
		return (typeMirror instanceof ArrayType);
	}



	/**
	 * Returns a type node for the array content type
	 *
	 * @param transpiler   the transpiler used for checking the annotation
	 *
	 * @return the type node for the array content type
	 */
	public TypeNode getArrayContentType(final Transpiler transpiler) {
		if (node != null) {
			if (node instanceof final ArrayTypeTree att)
				return new TypeNode(plugin, att.getType(), true, isVarArg && this.notNull);
			if ((node instanceof final AnnotatedTypeTree ann) && (ann.getUnderlyingType() instanceof final ArrayTypeTree att)) {
				final boolean notNullAnnotated = transpiler.hasNotNullAnnotation(ann);
				return new TypeNode(plugin, att.getType(), true, (isVarArg && this.notNull) || notNullAnnotated);
			}
			throw new TranspilerException("Transpiler Error: Nodes of kind " + this.node.getKind() + " not yet supported in Method getArrayContentType()");
		}
		if (typeMirror instanceof final ArrayType at)
			return new TypeNode(plugin, at.getComponentType(), true, isVarArg && this.notNull, resolved);
		throw new TranspilerException("Transpiler Error: TypeMirrors of kind " + this.typeMirror.getKind() + " not yet supported in Method getArrayContentType()");
	}



	private static String transpilePrimitiveType(final TypeKind kind) {
		return switch (kind) {
			case VOID -> "void";
			case BOOLEAN -> "boolean";
			case BYTE, SHORT, INT, LONG, FLOAT, DOUBLE -> "number";
			case CHAR -> "string";
			default -> throw new TranspilerException("Transpiler Error: " + kind + " is not supported as a primitive type.");
		};
	}


	private String transpileArrayType(final ArrayTypeTree node, final boolean noTypeArgs, final boolean parentNotNull, final boolean contentNotNull) {
		final TypeNode arrayTypeNode = new TypeNode(plugin, node.getType(), true, contentNotNull);
		return "Array<%s>%s".formatted(arrayTypeNode.transpile(noTypeArgs), (decl && !parentNotNull) ? " | null" : "");
	}


	private TypeNode getParameterizedTypeBaseTypeNode(final ParameterizedTypeTree node) {
		Tree baseType = node.getType();
		if (baseType instanceof final AnnotatedTypeTree att)
			baseType = att.getUnderlyingType();
		return new TypeNode(plugin, baseType, false, false);
	}


	private String transpileParameterizedType(final ParameterizedTypeTree node, final boolean noTypeArgs) {
		final TypeNode paramTypeNode = getParameterizedTypeBaseTypeNode(node);
		String typeString = paramTypeNode.transpile(true);
		final List<? extends Tree> typeArgs = node.getTypeArguments();
		if (noTypeArgs || (typeArgs == null) || (typeArgs.size() == 0))
			return typeString + ((decl && !notNull) ? " | null" : "");
		typeString += "<";
		boolean first = true;
		for (final Tree t : typeArgs) {
			if (!first)
				typeString += ", ";
			first = false;
			final TypeNode typeArgNode = new TypeNode(plugin, t, true, false);
			typeString += typeArgNode.transpile(false);
		}
		typeString += ">" + ((decl && !notNull) ? " | null" : "");
		return typeString;
	}


	private String transpileDeclaredType(final DeclaredType dt, final boolean noTypeArgs) {
		String typeString = "";
		if (dt.asElement() instanceof final TypeElement te)
			typeString = te.getSimpleName().toString();
		else
			throw new TranspilerException("Transpiler Error: Unhandled Declared Type of Kind " + dt.getKind());
		final List<? extends TypeMirror> typeArgs = dt.getTypeArguments();
		if (noTypeArgs || (typeArgs == null) || (typeArgs.isEmpty()))
			return typeString + ((decl && !notNull) ? " | null" : "");
		typeString += "<";
		boolean first = true;
		for (final TypeMirror t : typeArgs) {
			if (!first)
				typeString += ", ";
			first = false;
			final TypeNode typeArgNode = new TypeNode(plugin, t, true, false, resolved);
			typeString += typeArgNode.transpile(false);
		}
		typeString += ">" + ((decl && !notNull) ? " | null" : "");
		return typeString;
	}


	private String transpileTypeVariable(final TypeVariable tv, final boolean withBounds) {
		final String typeVar = tv.asElement().getSimpleName().toString();
		if (resolved.containsKey(typeVar)) {
			final TypeMirror tm = resolved.get(typeVar);
			final TypeNode tn = new TypeNode(plugin, tm, true, Transpiler.hasNotNullAnnotation(tv), resolved);
			return tn.transpile(false);
		}
		if (withBounds) {
			// TODO final TypeMirror upper = tv.getUpperBound();
			// TODO final TypeMirror lower = tv.getLowerBound();
			throw new TranspilerException("Transpiler Exception: Bounds are not yet supported here");
		}
		return typeVar;
	}


	private String transpileIdentifier(final IdentifierTree node) {
		// TODO check it the identifier is valid, ...
		return plugin.convertIdentifier(node) + ((decl && !notNull) ? " | null" : "");
	}


	private String transpileMemberSelect(final MemberSelectTree node) {
		final String classname = node.getIdentifier().toString();
		final String packagename = node.getExpression().toString();
		final ExpressionType type = plugin.getTranspiler().getExpressionType(node.getExpression());
		if (type instanceof final ExpressionClassType ect) // for nested classes and interfaces
			return TranspilerTypeScriptPlugin.getImportName(classname, ect.getFullQualifiedName()) + ((decl && !notNull) ? " | null" : "");
		if ("Object".equals(classname) && "java.lang".equals(packagename))
			return "object" + ((decl && !notNull) ? " | null" : "");
		return TranspilerTypeScriptPlugin.getImportName(classname, packagename) + ((decl && !notNull) ? " | null" : "");
	}


	private String transpileAnnotatedType(final AnnotatedTypeTree node, final boolean noTypeArgs, final boolean parentNotNull) {
		final boolean hasNotNull = plugin.getTranspiler().hasNotNullAnnotation(node);
		final TypeNode underlyingTypeNode = new TypeNode(plugin, node.getUnderlyingType(), decl, hasNotNull);
		return underlyingTypeNode.transpileInternal(noTypeArgs, parentNotNull, hasNotNull);
	}

	private String transpileWildcard(final WildcardTree node) {
		return switch (node.getKind()) {
			case UNBOUNDED_WILDCARD -> "any";
			case SUPER_WILDCARD -> {
				final TypeNode boundNode = new TypeNode(plugin, node.getBound(), decl, false);
				// TODO Partial is not really correct - try to find a better solution
				yield "Partial<" + boundNode.transpile(false) + ">";
			}
			case EXTENDS_WILDCARD -> {
				final TypeNode boundNode = new TypeNode(plugin, node.getBound(), decl, false);
				yield boundNode.transpile(false);
			}
			default -> throw new TranspilerException("Transpiler Error: " + node.getKind() + " is not supported as a wildcard type.");
		};
	}

	private String transpileInternal(final boolean noTypeArgs, final boolean parentNotNull, final boolean contentNotNull) {
		if ((node == null) && (typeMirror == null))
			return "void";
		if (node != null) {
			if (node instanceof final PrimitiveTypeTree p)
				return TypeNode.transpilePrimitiveType(p.getPrimitiveTypeKind());
			if (node instanceof final ArrayTypeTree a)
				return this.transpileArrayType(a, noTypeArgs, parentNotNull, contentNotNull);
			if (node instanceof final ParameterizedTypeTree p)
				return this.transpileParameterizedType(p, noTypeArgs);
			if (node instanceof final IdentifierTree i)
				return this.transpileIdentifier(i);
			if (node instanceof final MemberSelectTree mst)
				return this.transpileMemberSelect(mst);
			if (node instanceof final AnnotatedTypeTree att)
				return this.transpileAnnotatedType(att, noTypeArgs, parentNotNull);
			if (node instanceof final WildcardTree wt)
				return this.transpileWildcard(wt);
			throw new TranspilerException("Transpiler Error: Type node of kind " + node.getKind() + " not yet supported by the transpiler.");
		}
		if (typeMirror.getKind().isPrimitive())
			return TypeNode.transpilePrimitiveType(typeMirror.getKind());
		if (typeMirror instanceof final DeclaredType dt)
			return this.transpileDeclaredType(dt, noTypeArgs);
		if (typeMirror instanceof final TypeVariable tv)
			return this.transpileTypeVariable(tv, false);
		throw new TranspilerException("Transpiler Error: Type mirror of kind " + typeMirror.getKind() + " not yet supported by the transpiler.");
	}


	/**
	 * Returns the transpiled code of this node.
	 *
	 * @param noTypeArgs   if set to true the type arguments of parameterized types are
	 *                     not transpiled. This feature is used e.g. for the instanceof
	 *                     operator.
	 *
	 * @return the transpiled code
	 */
	public String transpile(final boolean noTypeArgs) {
		return transpileInternal(noTypeArgs, this.notNull, isVarArg && this.notNull);
	}


	/**
	 * Returns the transpiled code for checking whether the specified object is an
	 * instance of this type.
	 *
	 * @param obj   the object to be checked
	 *
	 * @return the transpiled instanceof code
	 */
	public String getInstanceOf(final String obj) {
		if (node instanceof final IdentifierTree i) {
			ExpressionType type = plugin.getTranspiler().getExpressionType(i);
			if (type == null)
				throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + i.getName().toString());
			if (type instanceof final ExpressionTypeVar tvType) {
				if (tvType.getExtendsBound() == null)
					return "(typeof " + obj + " !== \"undefined\")";
				type = tvType.getExtendsBound();
			}
			if (type instanceof final ExpressionClassType classType) {
				if ("java.lang.String".equals(classType.getFullQualifiedName()))
					return "(typeof " + obj + " === \"string\")";
				if ("java.lang.Byte".equals(classType.getFullQualifiedName())
						|| "java.lang.Short".equals(classType.getFullQualifiedName())
						|| "java.lang.Integer".equals(classType.getFullQualifiedName())
						|| "java.lang.Long".equals(classType.getFullQualifiedName())
						|| "java.lang.Float".equals(classType.getFullQualifiedName())
						|| "java.lang.Double".equals(classType.getFullQualifiedName()))
					return "(typeof " + obj + " === \"number\")";
				if ("java.lang.Boolean".equals(classType.getFullQualifiedName()))
					return "(typeof " + obj + " === \"boolean\")";
				// Check whether we check for a functional interface - then we need only an object with the function...
				final String functionalInterfaceMethodName = plugin.getTranspiler().getFunctionInterfaceMethodName(classType.getFullQualifiedName());
				if (functionalInterfaceMethodName != null)
					return "((typeof " + obj + " !== 'undefined') && (" + obj + " instanceof Object) && (" + obj + " !== null) && ('" + functionalInterfaceMethodName + "' in " + obj + ") && (typeof " + obj + "." + functionalInterfaceMethodName + " === 'function'))";
				// Check for java.lang.Object - in this case we must also check for Numbers
				if ("java.lang.Object".equals(classType.getFullQualifiedName())) {
					return "((" + obj + " instanceof Object) || ((" + obj + " instanceof JavaObject) && (" + obj + ".isTranspiledInstanceOf('" + classType.getFullQualifiedName() + "'))))";
				}
				// Otherwise we can check for a complete Java-object...
				return "((" + obj + " instanceof JavaObject) && ((" + obj + " as JavaObject).isTranspiledInstanceOf('" + classType.getFullQualifiedName() + "')))";
			}
			throw new TranspilerException("Transpiler Error: Unhandled type information for the identifier " + i.getName().toString());
		}
		if (node instanceof final ParameterizedTypeTree p)
			return getParameterizedTypeBaseTypeNode(p).getInstanceOf(obj);
		throw new TranspilerException("Transpiler Error: instanceof for type nodes of kind " + node.getKind() + " not yet supported by the transpiler.");
	}



	/**
	 * Returns transpiled code to check whether the specified identifier matches
	 * this primitive type.
	 *
	 * @param node         the primitive type node
	 * @param identifier   the identifier to be checked
	 *
	 * @return the type check code
	 */
	private static String checkPrimitiveType(final String identifier, final PrimitiveTypeTree node) {
		return switch (node.getPrimitiveTypeKind()) {
			case BOOLEAN -> "typeof " + identifier + " === \"boolean\"";
			case BYTE, SHORT, INT, LONG, FLOAT, DOUBLE -> "typeof " + identifier + " === \"number\"";
			case CHAR -> "typeof " + identifier + " === \"string\"";
			default -> throw new TranspilerException("Transpiler Error: " + node.getPrimitiveTypeKind() + " is not supported as a primitive type.");
		};
	}


	/**
	 * Returns transpiled code to check whether the specified identifier matches
	 * this type.
	 *
	 * @param identifier   the identifier to be checked
	 *
	 * @return the type check code
	 */
	public String getTypeCheck(final String identifier) {
		if (node instanceof final PrimitiveTypeTree p)
			return checkPrimitiveType(identifier, p);
		if (node instanceof IdentifierTree)
			return getInstanceOf(identifier) + (notNull ? "" : (" || (" + identifier + " === null)"));
		if (node instanceof final ParameterizedTypeTree p)
			return getParameterizedTypeBaseTypeNode(p).getTypeCheck(identifier);
		if (node instanceof ArrayTypeTree)
			return "Array.isArray(" + identifier + ")";
		if (node instanceof final AnnotatedTypeTree a) {
			final boolean hasNotNull = plugin.getTranspiler().hasNotNullAnnotation(a);
			final TypeNode underlyingTypeNode = new TypeNode(plugin, a.getUnderlyingType(), decl, hasNotNull);
			return underlyingTypeNode.getTypeCheck(identifier);
		}
		throw new TranspilerException("Transpiler Error: type check for type nodes of kind " + node.getKind() + " not yet supported by the transpiler.");
	}


	/**
	 * Returns transpiled code for casting the specified identifier
	 * to this type. Casting is only used for Java object types.
	 *
	 * @param identifier   the identifier
	 *
	 * @return the transpiled code
	 */
	public String getTypeCast(final String identifier) {
		if (node instanceof final PrimitiveTypeTree p)
			return identifier + " as " + transpile(false);
		if (node instanceof final IdentifierTree i) {
			final ExpressionType type = plugin.getTranspiler().getExpressionType(i);
			if (type == null)
				throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + i.getName().toString());
			if (type instanceof final ExpressionClassType classType) {
				return switch (classType.getFullQualifiedName()) {
					case "java.lang.String", "java.lang.Boolean", "java.lang.Byte", "java.lang.Short",
						"java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double" -> identifier;
					case "java.lang.Object" -> "(" + identifier + " instanceof JavaObject) ? cast_" + classType.getFullQualifiedName().replace('.', '_') + "(" + identifier + ") : " + identifier;
					case "java.lang.Enum" -> "cast_" + classType.getFullQualifiedName().replace('.', '_') + "(" + identifier + ")";
					default -> "cast_" + classType.getFullQualifiedName().replace('.', '_') + "(" + identifier + ")";
				};
			}
			if (type instanceof ExpressionTypeVar)
				return identifier + " as unknown as " + type.toString();
			throw new TranspilerException("Transpiler Error: Unhandled type information for the identifier " + i.getName().toString());
		}
		if (node instanceof final MemberSelectTree mst) {
			final ExpressionType type = plugin.getTranspiler().getExpressionType(mst);
			if (type == null)
				throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the member select " + mst.toString());
			if (type instanceof final ExpressionClassType classType) {
				if ("java.lang.String".equals(classType.getFullQualifiedName()))
					return identifier;
				if ("java.lang.Enum".equals(classType.getFullQualifiedName())) {
					return "(" + identifier + " instanceof JavaObject) ? cast_" + classType.getFullQualifiedName().replace('.', '_') + "(" + identifier + ") : " + identifier;
				}
				if ("java.lang.Object".equals(classType.getFullQualifiedName())) {
					return "(" + identifier + " instanceof JavaObject) ? cast_" + classType.getFullQualifiedName().replace('.', '_') + "(" + identifier + ") : " + identifier;
				}
				return "cast_" + classType.getFullQualifiedName().replace('.', '_') + "(" + identifier + ")";
			}
			if (type instanceof ExpressionTypeVar)
				return identifier + " as unknown as " + type.toString();
			throw new TranspilerException("Transpiler Error: Unhandled type information for the member select " + mst.toString());
		}
		if (node instanceof final ParameterizedTypeTree p)
			return getParameterizedTypeBaseTypeNode(p).getTypeCast(identifier);
		if (node instanceof ArrayTypeTree)
			return identifier;
		if (node instanceof final AnnotatedTypeTree att) {
			final boolean hasNotNull = plugin.getTranspiler().hasNotNullAnnotation(att);
			final TypeNode underlyingTypeNode = new TypeNode(plugin, att.getUnderlyingType(), decl, hasNotNull);
			return underlyingTypeNode.getTypeCast(identifier);
		}
		throw new TranspilerException("Transpiler Error: type casting for type nodes of kind " + node.getKind() + " not yet supported by the transpiler.");
	}

}
