package de.svws_nrw.transpiler.typescript;

import java.util.List;

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
		this.decl = decl;
		// Korrigiere ggf. die Information zur NotNull-Annotation, falls der Base-Type des parametrisierten Typs eine NotNull-Annotation hat
		if ((node instanceof ParameterizedTypeTree p) && (p.getType() != null) && (p.getType() instanceof AnnotatedTypeTree att))
			this.notNull = plugin.getTranspiler().hasNotNullAnnotation(att);
		else if (node instanceof AnnotatedTypeTree att)
			this.notNull = plugin.getTranspiler().hasNotNullAnnotation(att);
		else
			this.notNull = notNull;
		this.isVarArg = false;
	}
	
	
	/**
	 * Returns a copy of this type without the flag that indicates that this type is used during declaration
	 *  
	 * @return a copy of this type without the flag that indicates that this type is used during declaration
	 */
	public TypeNode getNoDeclarationType() {
		return  new TypeNode(this.plugin, this.node, false, this.notNull);
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
		return (node instanceof PrimitiveTypeTree);
	}
	
	
	/**
	 * Returns whether this type is a primitive integer type or not
	 * 
	 * @return true if this is a primitive integer type
	 */
	public boolean isPrimitveInteger() {
		if (node instanceof PrimitiveTypeTree ptt) {
			return switch (ptt.getPrimitiveTypeKind()) {
				case BYTE, SHORT, INT, LONG -> true; 
				default -> false;
			};
		}
		return false;
	}
	
	
	/**
	 * Returns whether this type is a primitive floating point number type or not
	 * 
	 * @return true if this is a primitive floating point number type
	 */
	public boolean isPrimitveFloat() {
		if (node instanceof PrimitiveTypeTree ptt) {
			return switch (ptt.getPrimitiveTypeKind()) {
				case FLOAT, DOUBLE -> true; 
				default -> false;
			};
		}
		return false;
	}
	
	
	/**
	 * Returns whether this type is a primitive boolean type or not
	 * 
	 * @return true if this is a primitive boolean type
	 */
	public boolean isPrimitveBoolean() {
		if (node instanceof PrimitiveTypeTree ptt) {
			return switch (ptt.getPrimitiveTypeKind()) {
				case BOOLEAN -> true; 
				default -> false;
			};
		}
		return false;
	}
	
	
	/**
	 * Returns whether this type is a primitive char type or not
	 * 
	 * @return true if this is a primitive char type
	 */
	public boolean isPrimitveChar() {
		if (node instanceof PrimitiveTypeTree ptt) {
			return switch (ptt.getPrimitiveTypeKind()) {
				case CHAR -> true; 
				default -> false;
			};
		}
		return false;
	}
	
	
	/**
	 * Returns whether the type is java.lang.String.
	 * 
	 * @return true if the type is java.lang.String
	 */
	public boolean isString() {
		if (node instanceof IdentifierTree ident) {
			ExpressionType type = plugin.getTranspiler().getExpressionType(ident);
			if (type == null)
				throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + ident.getName().toString());
			return ((type instanceof ExpressionClassType classType) && ("java.lang.String".equals(classType.getFullQualifiedName())));
		} else if (node instanceof AnnotatedTypeTree att) {
			if (att.getUnderlyingType() instanceof ArrayTypeTree)
				return false;
			ExpressionType type = plugin.getTranspiler().getExpressionType(att.getUnderlyingType());
			if (type == null)
				throw new TranspilerException("Transpiler Error: Cannot retrieve the type information from annotated type " + att.toString());
			return ((type instanceof ExpressionClassType classType) && ("java.lang.String".equals(classType.getFullQualifiedName())));
		}
		return false;
	}


	/**
	 * Returns whether the type is java.lang.Boolean.
	 * 
	 * @return true if the type is java.lang.Boolean
	 */
	public boolean isBoolean() {
		if (node instanceof IdentifierTree ident) {
			ExpressionType type = plugin.getTranspiler().getExpressionType(ident);
			if (type == null)
				throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + ident.getName().toString());
			return ((type instanceof ExpressionClassType classType) && ("java.lang.Boolean".equals(classType.getFullQualifiedName())));
		} else if (node instanceof AnnotatedTypeTree att) {
			if (att.getUnderlyingType() instanceof ArrayTypeTree)
				return false;
			ExpressionType type = plugin.getTranspiler().getExpressionType(att.getUnderlyingType());
			if (type == null)
				throw new TranspilerException("Transpiler Error: Cannot retrieve the type information from annotated type " + att.toString());
			return ((type instanceof ExpressionClassType classType) && ("java.lang.Boolean".equals(classType.getFullQualifiedName())));
		}
		return false;
	}


	/**
	 * Returns whether the type is a number class type (e.g. java.lang.Long).
	 * 
	 * @return true if the type is a number class type
	 */
	public boolean isNumberClass() {
		if (node instanceof IdentifierTree ident) {
			ExpressionType type = plugin.getTranspiler().getExpressionType(ident);
			if (type == null)
				throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + ident.getName().toString());
			return ((type instanceof ExpressionClassType classType) && 
					(("java.lang.Number".equals(classType.getFullQualifiedName())) ||
					 ("java.lang.Byte".equals(classType.getFullQualifiedName())) ||
					 ("java.lang.Short".equals(classType.getFullQualifiedName())) ||
					 ("java.lang.Integer".equals(classType.getFullQualifiedName())) ||
					 ("java.lang.Long".equals(classType.getFullQualifiedName())) ||
					 ("java.lang.Float".equals(classType.getFullQualifiedName())) ||
					 ("java.lang.Double".equals(classType.getFullQualifiedName()))));
		} else if (node instanceof AnnotatedTypeTree att) {
			if (att.getUnderlyingType() instanceof ArrayTypeTree)
				return false;
			ExpressionType type = plugin.getTranspiler().getExpressionType(att.getUnderlyingType());
			if (type == null)
				throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the annotated type " + att.toString());
			return ((type instanceof ExpressionClassType classType) && 
					(("java.lang.Number".equals(classType.getFullQualifiedName())) ||
					 ("java.lang.Byte".equals(classType.getFullQualifiedName())) ||
					 ("java.lang.Short".equals(classType.getFullQualifiedName())) ||
					 ("java.lang.Integer".equals(classType.getFullQualifiedName())) ||
					 ("java.lang.Long".equals(classType.getFullQualifiedName())) ||
					 ("java.lang.Float".equals(classType.getFullQualifiedName())) ||
					 ("java.lang.Double".equals(classType.getFullQualifiedName()))));			
		}
		if (node instanceof MemberSelectTree)
			throw new TranspilerException("Transpiler Error: MemberSelectTree nodes not yet supported in Method isNumberClass()");
		return false;
	}
	
	
	
	/**
	 * Returns whether the type is a Java collection type (e.g. java.util.Vector).
	 * 
	 * @return true if the type is a Java collection type
	 */
	public boolean isCollectionType() {
		if (node instanceof IdentifierTree ident) {
			ExpressionType type = plugin.getTranspiler().getExpressionType(ident);
			if (type == null)
				throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + ident.getName().toString());
			return plugin.getTranspiler().checkForSuperclass(type, ExpressionClassType.getExpressionInterfaceType("Collection", "java.util")) >= 0;
		}
		if (node instanceof ParameterizedTypeTree ptt)
			return getParameterizedTypeBaseTypeNode(ptt).isCollectionType(); 
		if (node instanceof MemberSelectTree)
			throw new TranspilerException("Transpiler Error: MemberSelectTree nodes not yet supported in Method isCollectionType()");
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
	public TypeNode getParameterType(int i, boolean decl) {
		if (node instanceof ParameterizedTypeTree ptt) {
			List<? extends Tree> typeArgs = ptt.getTypeArguments();
			if ((typeArgs == null) || (i < 0) || (i >= typeArgs.size()))
				return null;				
			return new TypeNode(plugin, typeArgs.get(i), decl, false);
		}
		return null;
	}
	
	
	/**
	 * Returns whether the type is a Java array type
	 * 
	 * @return true if the type is a Java array type
	 */
	public boolean isArrayType() {
		if (node instanceof IdentifierTree ident) {
			ExpressionType type = plugin.getTranspiler().getExpressionType(ident);
			if (type == null)
				throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + ident.getName().toString());
			return (type instanceof ExpressionArrayType);
		}
		if (node instanceof MemberSelectTree)
			throw new TranspilerException("Transpiler Error: MemberSelectTree nodes not yet supported in Method isArrayType()");
		if ((node instanceof ArrayTypeTree) || ((node instanceof AnnotatedTypeTree att) && (att.getUnderlyingType() instanceof ArrayTypeTree)))
			return true;
		throw new TranspilerException("Transpiler Error: Nodes of kind " + this.node.getKind() + " not yet supported in Method isArrayType()");
	}
	
	
	
	/**
	 * Returns a type node for the array content type
	 * 
	 * @param transpiler   the transpiler used for checking the annotation 
	 * 
	 * @return the type node for the array content type
	 */
	public TypeNode getArrayContentType(Transpiler transpiler) {
		if (node instanceof ArrayTypeTree att) 
			return new TypeNode(plugin, att.getType(), true, isVarArg && this.notNull);
		if ((node instanceof AnnotatedTypeTree ann) && (ann.getUnderlyingType() instanceof ArrayTypeTree att)) {
			boolean notNullAnnotated = transpiler.hasNotNullAnnotation(ann);
			return new TypeNode(plugin, att.getType(), true, (isVarArg && this.notNull) || notNullAnnotated);
		}
		throw new TranspilerException("Transpiler Error: Nodes of kind " + this.node.getKind() + " not yet supported in Method getArrayContentType()");
	}


	
	private static String transpilePrimitiveType(PrimitiveTypeTree node) {
		return switch (node.getPrimitiveTypeKind()) {
			case VOID -> "void";
			case BOOLEAN -> "boolean";
			case BYTE, SHORT, INT, LONG, FLOAT, DOUBLE -> "number";
			case CHAR -> "string";
			default -> throw new TranspilerException("Transpiler Error: " + node.getPrimitiveTypeKind() + " is not supported as a primitive type.");
		};		
	}
	
	
	private String transpileArrayType(ArrayTypeTree node, boolean noTypeArgs, boolean parentNotNull, boolean contentNotNull) {
		TypeNode arrayTypeNode = new TypeNode(plugin, node.getType(), true, contentNotNull);
		return "Array<%s>%s".formatted(arrayTypeNode.transpile(noTypeArgs), (decl && !parentNotNull) ? " | null" : "");
	}

	
	private TypeNode getParameterizedTypeBaseTypeNode(ParameterizedTypeTree node) {
		Tree baseType = node.getType();
		if (baseType instanceof AnnotatedTypeTree att)
			baseType = att.getUnderlyingType();
		return new TypeNode(plugin, baseType, false, false);		
	}
	
	
	private String transpileParameterizedType(ParameterizedTypeTree node, boolean noTypeArgs) {
		TypeNode paramTypeNode = getParameterizedTypeBaseTypeNode(node);
		String typeString = paramTypeNode.transpile(true);
		List<? extends Tree> typeArgs = node.getTypeArguments();
		if (noTypeArgs || (typeArgs == null) || (typeArgs.size() == 0))
			return typeString + ((decl && !notNull) ? " | null" : "");
		typeString += "<";
		boolean first = true;
		for (Tree t : typeArgs) {
			if (!first)
				typeString += ", ";
			first = false;
			TypeNode typeArgNode = new TypeNode(plugin, t, true, false); 
			typeString += typeArgNode.transpile(false);
		}
		typeString += ">" + ((decl && !notNull) ? " | null" : "");
		return typeString;
	}
	

	private String transpileIdentifier(IdentifierTree node) {
		// TODO check it the identifier is valid, ...
		return plugin.convertIdentifier(node) + ((decl && !notNull) ? " | null" : "");		
	}


	private String transpileMemberSelect(MemberSelectTree node) {
		String classname = node.getIdentifier().toString();
		String packagename = node.getExpression().toString();
		ExpressionType type = plugin.getTranspiler().getExpressionType(node.getExpression());
		if (type instanceof ExpressionClassType ect) // for nested classes and interfaces
			return TranspilerTypeScriptPlugin.getImportName(classname, ect.getFullQualifiedName()) + ((decl && !notNull) ? " | null" : "");
		if ("Object".equals(classname) && "java.lang".equals(packagename))
			return "object" + ((decl && !notNull) ? " | null" : "");
		return TranspilerTypeScriptPlugin.getImportName(classname, packagename) + ((decl && !notNull) ? " | null" : "");
	}

	
	private String transpileAnnotatedType(AnnotatedTypeTree node, boolean noTypeArgs, boolean parentNotNull) {
		boolean hasNotNull = plugin.getTranspiler().hasNotNullAnnotation(node);
		TypeNode underlyingTypeNode = new TypeNode(plugin, node.getUnderlyingType(), decl, hasNotNull);
		return underlyingTypeNode.transpileInternal(noTypeArgs, parentNotNull, hasNotNull);
	}

	private String transpileWildcard(WildcardTree node) {
		return switch(node.getKind()) {
			case UNBOUNDED_WILDCARD -> "unknown";
			case SUPER_WILDCARD -> {
				TypeNode boundNode = new TypeNode(plugin, node.getBound(), decl, false);
				// TODO Partial is not really correct - try to find a better solution
				yield "Partial<" + boundNode.transpile(false) + ">";
			}
			case EXTENDS_WILDCARD -> {
				TypeNode boundNode = new TypeNode(plugin, node.getBound(), decl, false);
				yield boundNode.transpile(false);
			}
			default -> throw new TranspilerException("Transpiler Error: " + node.getKind() + " is not supported as a wildcard type."); 
		};		
	}
	
	private String transpileInternal(boolean noTypeArgs, boolean parentNotNull, boolean contentNotNull) {
		if (node == null)
			return "void";
		if (node instanceof PrimitiveTypeTree p)
			return TypeNode.transpilePrimitiveType(p);
		if (node instanceof ArrayTypeTree a)
			return this.transpileArrayType(a, noTypeArgs, parentNotNull, contentNotNull);
		if (node instanceof ParameterizedTypeTree p)
			return this.transpileParameterizedType(p, noTypeArgs);
		if (node instanceof IdentifierTree i)
			return this.transpileIdentifier(i);
		if (node instanceof MemberSelectTree mst)
			return this.transpileMemberSelect(mst);
		if (node instanceof AnnotatedTypeTree att)
			return this.transpileAnnotatedType(att, noTypeArgs, parentNotNull);
		if (node instanceof WildcardTree wt)
			return this.transpileWildcard(wt);
		throw new TranspilerException("Transpiler Error: Type node of kind " + node.getKind() + " not yet supported by the transpiler.");
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
	public String transpile(boolean noTypeArgs) {
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
	public String getInstanceOf(String obj) {
		if (node instanceof IdentifierTree i) {
			ExpressionType type = plugin.getTranspiler().getExpressionType(i);
			if (type == null)
				throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + i.getName().toString());
			if (type instanceof ExpressionTypeVar tvType) {
				if (tvType.getExtendsBound() == null)
					return "(typeof " + obj + " !== \"undefined\")"; 
				type = tvType.getExtendsBound();
			}
			if (type instanceof ExpressionClassType classType) {
				if ("java.lang.String".equals(classType.getFullQualifiedName()))
					return "(typeof " + obj + " === \"string\")";
				if ("java.lang.Byte".equals(classType.getFullQualifiedName()) ||
					"java.lang.Short".equals(classType.getFullQualifiedName()) ||
					"java.lang.Integer".equals(classType.getFullQualifiedName()) ||
					"java.lang.Long".equals(classType.getFullQualifiedName()) ||
					"java.lang.Float".equals(classType.getFullQualifiedName()) ||
					"java.lang.Double".equals(classType.getFullQualifiedName()))
					return "(typeof " + obj + " === \"number\")";
				if ("java.lang.Boolean".equals(classType.getFullQualifiedName()))
					return "(typeof " + obj + " === \"boolean\")";
				// Check whether we check for a functional interface - then we need only an object with the function...
				String functionalInterfaceMethodName = plugin.getTranspiler().getFunctionInterfaceMethodName(classType.getFullQualifiedName()); 
				if (functionalInterfaceMethodName != null)
					return "((typeof " + obj + " !== 'undefined') && (" + obj + " instanceof Object) && (" + obj + " !== null) && ('" + functionalInterfaceMethodName + "' in " + obj + ") && (typeof " + obj + "." + functionalInterfaceMethodName + " === 'function'))";
				// Check for java.lang.Object - in this case we must also check for Numbers
				if ("java.lang.Object".equals(classType.getFullQualifiedName())) {
					return "((" + obj + " instanceof Object) || ((" + obj + " instanceof JavaObject) && (" + obj + ".isTranspiledInstanceOf('" + classType.getFullQualifiedName() + "'))))";
				}
				// Otherwise we can check for a complete Java-object...
				return "((" + obj + " instanceof JavaObject) && (" + obj + ".isTranspiledInstanceOf('" + classType.getFullQualifiedName() + "')))";
			}
			throw new TranspilerException("Transpiler Error: Unhandled type information for the identifier " + i.getName().toString());
		}
		if (node instanceof ParameterizedTypeTree p)
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
	private static String checkPrimitiveType(String identifier, PrimitiveTypeTree node) {		
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
	public String getTypeCheck(String identifier) {
		if (node instanceof PrimitiveTypeTree p)
			return checkPrimitiveType(identifier, p);
		if (node instanceof IdentifierTree i)
			return getInstanceOf(identifier) + (notNull ? "" : (" || (" + identifier + " === null)"));
		if (node instanceof ParameterizedTypeTree p)
			return getParameterizedTypeBaseTypeNode(p).getTypeCheck(identifier);
		if (node instanceof ArrayTypeTree a)
			return "Array.isArray(" + identifier + ")";
		if (node instanceof AnnotatedTypeTree a) {
			boolean hasNotNull = plugin.getTranspiler().hasNotNullAnnotation(a);
			TypeNode underlyingTypeNode = new TypeNode(plugin, a.getUnderlyingType(), decl, hasNotNull);
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
	public String getTypeCast(String identifier) {
		if (node instanceof PrimitiveTypeTree p)
			return identifier + " as " + transpile(false);
		if (node instanceof IdentifierTree i) {
			ExpressionType type = plugin.getTranspiler().getExpressionType(i);
			if (type == null)
				throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + i.getName().toString());
			if (type instanceof ExpressionClassType classType) {
				return switch (classType.getFullQualifiedName()) {
					case "java.lang.String", "java.lang.Boolean", "java.lang.Byte", "java.lang.Short", 
						"java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double" -> identifier;
					case "java.lang.Object" -> "(" + identifier + " instanceof JavaObject) ? cast_" + classType.getFullQualifiedName().replace('.', '_') + "(" + identifier + ") : " + identifier;
					default -> "cast_" + classType.getFullQualifiedName().replace('.', '_') + "(" + identifier + ")"; 
				};
			}
			if (type instanceof ExpressionTypeVar typeVar)
				return identifier + " as unknown as " + type.toString();
			throw new TranspilerException("Transpiler Error: Unhandled type information for the identifier " + i.getName().toString());
		}
		if (node instanceof MemberSelectTree mst) {
			ExpressionType type = plugin.getTranspiler().getExpressionType(mst);
			if (type == null)
				throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the member select " + mst.toString());
			if (type instanceof ExpressionClassType classType) {
				if ("java.lang.String".equals(classType.getFullQualifiedName()))
					return identifier;
				if ("java.lang.Object".equals(classType.getFullQualifiedName())) {
					return "(" + identifier + " instanceof JavaObject) ? cast_" + classType.getFullQualifiedName().replace('.', '_') + "(" + identifier + ") : " + identifier;
				}
				return "cast_" + classType.getFullQualifiedName().replace('.', '_') + "(" + identifier + ")";
			}
			if (type instanceof ExpressionTypeVar typeVar)
				return identifier + " as unknown as " + type.toString();
			throw new TranspilerException("Transpiler Error: Unhandled type information for the member select " + mst.toString());
		}
		if (node instanceof ParameterizedTypeTree p)
			return getParameterizedTypeBaseTypeNode(p).getTypeCast(identifier);
		if (node instanceof ArrayTypeTree a)
			return identifier;
		if (node instanceof AnnotatedTypeTree att) {
			boolean hasNotNull = plugin.getTranspiler().hasNotNullAnnotation(att);
			TypeNode underlyingTypeNode = new TypeNode(plugin, att.getUnderlyingType(), decl, hasNotNull);
			return underlyingTypeNode.getTypeCast(identifier);
		}
		throw new TranspilerException("Transpiler Error: type casting for type nodes of kind " + node.getKind() + " not yet supported by the transpiler.");
	}
	
}
