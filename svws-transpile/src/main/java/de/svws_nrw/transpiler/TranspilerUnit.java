package de.svws_nrw.transpiler;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.UnionType;
import javax.lang.model.type.WildcardType;

import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.InstanceOfTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;

/**
 * This class represents information gathered from a compilation unit
 * of the java compiler.
 */
public final class TranspilerUnit {

	/** the associated transpiler object */
	private final Transpiler transpiler;

	/** the associated compilation unit */
	private final CompilationUnitTree compilationUnit;

	/** the associated class tree */
	private final ClassTree classTree;

	/** the associated class element */
	private final TypeElement classElement;

	/** a set with the full qualified names of all super types of the class of this transpiler unit */
	public final HashSet<String> superTypes = new HashSet<>();

	/** a map with a mapping from the local variable name to a set of scopes where it is used */
	public final HashMap<String, Set<Tree>> allLocalVariables = new HashMap<>();

	/** a map with a mapping from a variable scope to a map with all defined variables by their names */
	public final HashMap<Tree, Map<String, VariableTree>> allLocalVariablesByScope = new HashMap<>();

	/** a map with a mapping from the local method name to a set of {@link ExecutableElement} objects */
	public final HashMap<String, Set<ExecutableElement>> allLocalMethodElements  = new HashMap<>();

	/** a map with a mapping from the local method name to a set of scopes where it is used */
	public final HashMap<String, Set<Tree>> allLocalMethods = new HashMap<>();

	/** a map with a mapping from a method scope to a map with all defined methods by their names */
	public final HashMap<Tree, Map<String, MethodTree>> allLocalMethodsByScope = new HashMap<>();

	/** a map with a mapping from a member select tree to the invoked method */
	public final HashMap<MemberSelectTree, ExecutableElement> allInvokedMethods = new HashMap<>();

	/** a list with all identifiers with the tree path that are visited */
	public final ArrayList<AbstractMap.SimpleEntry<IdentifierTree, TreePath>> allIdentifier = new ArrayList<>();

	/** a map with all identifiers that are not defined locally and must be imported */
	public final HashMap<IdentifierTree, String> allImports = new HashMap<>();

	/** a map with all identifiers in annotations that are not defined locally and must be imported bit are not in the import list of allImports */
	public final HashMap<IdentifierTree, String> allImportsForAnnotations = new HashMap<>();

	/** a map with all annotation identifiers that are not defined locally and must be imported */
	public final HashMap<IdentifierTree, String> allAnnotations = new HashMap<>();

	/** a map that maps the name of an identifier to the name of its package, if the identifier was imported in a super class */
	public final HashMap<String, String> importsSuper = new HashMap<>();

	/** a map that maps a short class name in case of nested classes to its full class name with it enclosing classes */
	public final HashMap<String, String> importsFullClassnames = new HashMap<>();

	/** a map that maps the name of an identifier to the name of its package */
	public final HashMap<String, String> imports = new HashMap<>();

	/** a map that maps the name of an annotation identifier to the name of its package */
	public final HashMap<String, String> annotations = new HashMap<>();

	/** the list of type parameter of this class */
	public final ArrayList<String> typeParameters = new ArrayList<>();

	/** a map with all expressions of this transpiler units in the order of their occurrence. */
	public final ArrayList<ExpressionTree> allExpressions = new ArrayList<>();

	/** a map that maps all expressions of this transpiler unit to its type */
	public final HashMap<ExpressionTree, ExpressionType> allExpressionTypes = new HashMap<>();

	/** a map containing a mapping between a java compiler abstract syntax tree node ant the corresponding tree path object */
	public final HashMap<Tree, TreePath> mapTreePath = new HashMap<>();



	/**
	 * Creates a new transpiler unit associated with the specified compilation unit
	 * and class.
	 *
	 * @param transpiler        a reference to the transpiler
	 * @param compilationUnit   the compilation unit
	 * @param classTree         the class tree
	 * @param classElement      the class element
	 */
	public TranspilerUnit(final Transpiler transpiler, final CompilationUnitTree compilationUnit, final ClassTree classTree, final TypeElement classElement) {
		super();
		this.transpiler = transpiler;
		this.compilationUnit = compilationUnit;
		this.classTree = classTree;
		this.classElement = classElement;
	}


	/**
	 * Checks whether the identifer is a known identifier with a valid scope.
	 *
	 * @param path   the tree path object of the identifier
	 * @param node   the identifier tree node
	 *
	 * @return true   if identifier is a local identifier
	 */
	public boolean isLocal(final TreePath path, final IdentifierTree node) {
		final String nodeName = node.getName().toString();
		if (getClassName().equals(nodeName) || "this".equals(nodeName))
			return true;
		final Set<Tree> scopesLocalVariables = allLocalVariables.get(nodeName);
		if (scopesLocalVariables != null)
			for (TreePath current = path; current.getParentPath() != null; current = current.getParentPath())
				if (scopesLocalVariables.contains(current.getLeaf()))
					return true;
		final Set<Tree> scopesLocalMethods = allLocalMethods.get(nodeName);
		if (scopesLocalMethods != null)
			for (TreePath current = path; current.getParentPath() != null; current = current.getParentPath())
				if (scopesLocalMethods.contains(current.getLeaf()))
					return true;
		final Set<ExecutableElement> localMethods = allLocalMethodElements.get(nodeName);
		if (localMethods != null)
			return true;
		// Check whether the identifier is part of an AnnotationTree but not the Type (the type is usually imported and not local)
		if (transpiler.isAnnotationArgument(node))
			return true;
		return false;
	}


	/**
	 * Returns the type element of this transpiler unit
	 *
	 * @return the type element
	 */
	TypeElement getElement() {
		return this.classElement;
	}


	/**
	 * Returns the class tree of this transpiler unit
	 *
	 * @return the class tree
	 */
	ClassTree getClassTree() {
		return this.classTree;
	}


	/**
	 * Returns the class name of this transpiler unit
	 *
	 * @return the class name
	 */
	public String getClassName() {
		return classTree.getSimpleName().toString();
	}


	/**
	 * Returns the package name of this transpiler unit
	 *
	 * @return the package name
	 */
	public String getPackageName() {
		return "" + compilationUnit.getPackageName();
	}


	/**
	 * Retrieves the package name of an identifier from the imports of this compilation
	 * unit.
	 *
	 * @param className   the name of the class
	 * @param isImport    decides whether the class should be imported or handled as an annotation class
	 *
	 * @return  the package name of the identifier
	 */
	private String getPackageName(final String className, final boolean isImport) {
		final String name = className.replaceAll("<.*>", "");
		// check whether name is the local class - in this case we need no import, but return the packagename
		if ((name.equals(classTree.getSimpleName().toString())))
			return getPackageName();

		// Check whether an import of that was already found in this transpiler unit
		if (isImport) {
			if (imports.containsKey(name))
				return imports.get(name);
		} else {
			if (annotations.containsKey(name))
				return annotations.get(name);
		}
		// Check for Classes in java.lang and java.io
		switch (name) {
			case "Comparable", "Cloneable", "Override", "System",
				"Object",
				"Boolean", "Byte", "Short", "Integer", "Long", "Float", "Double",
				"Math", "String", "StringBuilder",
				"ArrayIndexOutOfBoundsException",
				"ClassCastException",
				"CloneNotSupportedException",
				"Exception",
				"IllegalAccessError",
				"IllegalArgumentException",
				"IllegalStateException",
				"IndexOutOfBoundsException",
				"NullPointerException",
				"NumberFormatException",
				"RuntimeException",
				"UnsupportedOperationException":
				imports.put(name, "java.lang");
				return "java.lang";
			case "PrintStream":
				imports.put(name, "java.io");
				return "java.io";
		}
		// check the imports of the compilation unit for an import entry
		for (final ImportTree importNode : compilationUnit.getImports()) {
			final MemberSelectTree mst = (MemberSelectTree) importNode.getQualifiedIdentifier();
			final String mstClassName = "" + mst.getIdentifier();
			if ("*".equals(mstClassName))
				throw new TranspilerException("Transpiler Error: Wildcards are not allowed in java imports.");
			if (mstClassName.equals(name)) {
				final String mstPackageName = "" + mst.getExpression();
				if (isImport)
					imports.put(mstClassName, mstPackageName);
				else
					annotations.put(mstClassName, mstPackageName);
				return mstPackageName;
			}
		}

		// check whether the package of this class is the package of the class and a transpiler unit exists...
		if (transpiler.hasTranspilerUnit(getPackageName(), name)) {
			if (isImport)
				imports.put(name, getPackageName());
			else
				annotations.put(name, getPackageName());
			return getPackageName();
		}

		// check whether an import in a super class or implemented interface can be used for determining the package name of the identifier
		if (importsSuper.containsKey(name)) {
			final String packageName = importsSuper.get(name);
			final String fullClassName = importsFullClassnames.get(name);
			imports.put(fullClassName, packageName);
			return packageName;
		}

		throw new TranspilerException("Transpiler Error: Cannot determine package for identifier '" + name + "' in class '" + getClassName() + "'");
	}



	/**
	 * Add the specified type to the list of imports if it is an imported type
	 *
	 * @param type    the type
	 * @param added   a hash set with all types added so far
	 */
	private void addImport(final TypeMirror type, final HashSet<TypeMirror> added) {
		if (type == null)
			return;
		if (!added.add(type))
			return;
		if (type instanceof final DeclaredType dt) {
			final Element e = dt.asElement();
			if (e instanceof final TypeElement elem)
				addImport(elem);
			for (final TypeMirror t : dt.getTypeArguments())
				addImport(t, added);
		} else if (type instanceof final WildcardType wt) {
			addImport(wt.getExtendsBound(), added);
			addImport(wt.getSuperBound(), added);
		} else if (type instanceof final TypeVariable tv) {
			addImport(tv.getUpperBound(), added);
			addImport(tv.getLowerBound(), added);
		} else if (type instanceof final UnionType ut) {
			for (final TypeMirror t : ut.getAlternatives())
				addImport(t, added);
		} else if (type instanceof final IntersectionType it) {
			for (final TypeMirror t : it.getBounds())
				addImport(t, added);
		}
	}



	/**
	 * Add the specified type element to the list of imports
	 *
	 * @param elem   the type element
	 */
	private void addImport(final TypeElement elem) {
		final String qualifiedName = elem.getQualifiedName().toString();
		int index = qualifiedName.lastIndexOf('.');
		Element enclosing = elem.getEnclosingElement();
		while (enclosing instanceof final TypeElement te) {
			index = te.getQualifiedName().toString().lastIndexOf('.');
			enclosing = enclosing.getEnclosingElement();
		}
		final String packageName = qualifiedName.substring(0, index);
		final String className = qualifiedName.substring(index + 1);
		final String[] classNameParts = className.split("\\.");
		String tmp = null;
		for (int i = classNameParts.length - 1; i >= 0; i--) {
			tmp = (tmp == null) ? classNameParts[i] : classNameParts[i] + "." + tmp;
			final String old = this.importsSuper.put(tmp, packageName);
			this.importsFullClassnames.put(tmp, className);
			if (old != null)  // skip inserting type - it is already known from a previous call - avoid problem due to circular dependencies
				return;
		}
		for (final Element enclosed : elem.getEnclosedElements())
			if (enclosed instanceof final TypeElement enclosedElem)
				addImport(enclosedElem);
	}



	/**
	 * Registers all attributes and methods defined in the specified type element
	 * as local methods / attributes at the specifed transpiler unit.
	 *
	 * @param elem   the type element
	 */
	private void registerAttributeAndMethods(final TypeElement elem) {
		final boolean isUnitElement = (elem == this.getElement());
		for (final Element child : elem.getEnclosedElements()) {
			final TreePath childPath = transpiler.getTreePath(child);
			final HashSet<TypeMirror> importsAdded = new HashSet<>();
			if (child instanceof final VariableElement variable) {
				// add the type and its package to the imports of the super class or implemented interface
				if (variable.asType() instanceof final DeclaredType dt)
					addImport(dt, importsAdded);
				if ((!isUnitElement) && (childPath != null)) {
					// register attribute
					final VariableTree varTree = (VariableTree) childPath.getLeaf();
					final String varName = variable.getSimpleName().toString();
					Set<Tree> scopes = this.allLocalVariables.get(varName);
					if (scopes == null) {
						scopes = new HashSet<>();
						scopes.add(this.classTree);
						this.allLocalVariables.put(varName, scopes);
					} else {
						scopes.add(this.classTree);
					}
					Map<String, VariableTree> vars = this.allLocalVariablesByScope.get(this.classTree);
					if (vars == null) {
						vars = new HashMap<>();
						this.allLocalVariablesByScope.put(this.classTree, vars);
					}
					vars.put(varName, varTree);
				}
			} else if (child instanceof final ExecutableElement method) {
				// add the return and parameter types and their package to the imports of the super class or implemented interface
				if (method.getReturnType() instanceof final DeclaredType dt)
					addImport(dt, importsAdded);
				for (final VariableElement ve : method.getParameters()) {
					if (ve.asType() instanceof final DeclaredType dt)
						addImport(dt, importsAdded);
				}
				final String methodName = method.getSimpleName().toString();
				Set<ExecutableElement> methodElements = this.allLocalMethodElements.get(methodName);
				if (methodElements == null) {
					methodElements = new HashSet<>();
					methodElements.add(method);
					this.allLocalMethodElements.put(methodName, methodElements);
				} else {
					methodElements.add(method);
				}
				if (!isUnitElement) {
					if (childPath != null) {
						// register method
						final MethodTree methodTree = (MethodTree) childPath.getLeaf();
						Set<Tree> scopes = this.allLocalMethods.get(methodName);
						if (scopes == null) {
							scopes = new HashSet<>();
							scopes.add(this.classTree);
							this.allLocalMethods.put(methodName, scopes);
						} else {
							scopes.add(this.classTree);
						}
						Map<String, MethodTree> methods = this.allLocalMethodsByScope.get(this.classTree);
						if (methods == null) {
							methods = new HashMap<>();
							this.allLocalMethodsByScope.put(this.classTree, methods);
						}
						methods.put(methodName, methodTree);
					}
				}
			}
		}
	}



	/**
	 * Determines all attributes and methods defined in super classes and interfaces recursively.
	 *
	 * @param elem         the element that is analyzed
	 */
	public void determineInheritedMembers(final TypeElement elem) {
		if ("Object".equals(elem.getSimpleName().toString())
				|| "Constable".equals(elem.getSimpleName().toString())
				|| "Enum".equals(elem.getSimpleName().toString()))
			return;
		// check whether the type element was already handled before - to avoid unnecessary class and avoid problems due to circular dependencies
		superTypes.add(elem.getQualifiedName().toString());
		// add the super class or interface to the list of imports from super classes
		addImport(elem);
		// register the methods
		registerAttributeAndMethods(elem);
		for (final TypeMirror type : elem.getInterfaces()) {
			final Element ifaceElem = transpiler.getTypeUtils().asElement(type);
			if (ifaceElem instanceof final TypeElement te)
				determineInheritedMembers(te);
		}
		final TypeMirror superType = elem.getSuperclass();
		if (superType.getKind() != TypeKind.NONE) {
			final Element superElem = transpiler.getTypeUtils().asElement(superType);
			if (superElem instanceof final TypeElement te)
				determineInheritedMembers(te);
		}
	}


	/**
	 * Searches for the iterable type by recursively traversing the super classes
	 * and interfaces of the specified type element.
	 *
	 * @param elem   the type element
	 *
	 * @return the iterable type
	 */
	private TypeMirror getIterableTypeArgument(final TypeElement elem) {
		if ("java.lang.Iterable".equals(elem.getQualifiedName().toString()))
			return null;
		final Vector<TypeMirror> superTypes = new Vector<>();
		if ((elem.getSuperclass() != null) && (elem.getSuperclass().getKind() != TypeKind.NONE))
			superTypes.add(elem.getSuperclass());
		if (elem.getInterfaces() != null)
			superTypes.addAll(elem.getInterfaces());
		for (final TypeMirror type : superTypes) {
			final Element ifaceElem = transpiler.getTypeUtils().asElement(type);
			if ((ifaceElem instanceof final TypeElement te) && (type instanceof final DeclaredType dt)) {
				final List<? extends TypeParameterElement> typeParams = te.getTypeParameters();
				final List<? extends TypeMirror> typeArgs = dt.getTypeArguments();
				if ((typeParams == null) || (typeArgs == null) || (typeParams.size() != typeArgs.size()))
					continue;
				final TypeMirror result = getIterableTypeArgument(te);
				if ((result == null) && (typeArgs.size() > 0))
					return typeArgs.get(0);
				String name = null;
				if (result instanceof final TypeVariable resultType)
					name = resultType.asElement().getSimpleName().toString();
				else if (result instanceof final DeclaredType resultType)
					name = resultType.asElement().getSimpleName().toString();
				if (name != null) {
					for (int i = 0; i < te.getTypeParameters().size(); i++) {
						final TypeParameterElement typeParam = te.getTypeParameters().get(i);
						if (name.equals(typeParam.getSimpleName().toString()))
							return typeArgs.get(i);
					}
				}
			}
		}
		return null;
	}


	/**
	 * Searches for the iterable type by recursively traversing the super classes
	 * and interfaces of the class of this transpiler unit.
	 *
	 * @return the iterable type
	 */
	public TypeMirror getIterableTypeArgument() {
		if (!superTypes.contains("java.lang.Iterable"))
			return null;
		final TypeMirror result = getIterableTypeArgument(this.classElement);
		return result;
	}


	/**
	 * Determines all imports in this transpiler unit.
	 */
	public void determineImports() {
		for (final AbstractMap.SimpleEntry<IdentifierTree, TreePath> entry : allIdentifier) {
			final IdentifierTree node = entry.getKey();
			final TreePath path = entry.getValue();
			final Tree parent = path.getParentPath().getLeaf();
			// skip annotations and add all non local identifiers to the import map, also skip case trees
			if (!((parent instanceof CaseTree) || (parent instanceof AnnotationTree) || (isLocal(path, node)) || ("super".equals(node.getName().toString())) || typeParameters.contains(node.getName().toString()))) {
				allImports.put(node, getPackageName(node.getName().toString(), true));
				// TODO handle static imports...
			} else if (parent instanceof AnnotationTree) {
				allAnnotations.put(node, getPackageName(node.getName().toString(), false));
			} else if (transpiler.isAnnotationArgument(node)) {
				switch (parent.getKind()) {
					case ASSIGNMENT:
						break;
					default:
						allImportsForAnnotations.put(node, getPackageName(node.getName().toString(), true));
						break;
				}
			}
		}
	}


	/**
	 * Returns the type tree node that is associated with the specified identifier
	 *
	 * @param node   the identifier
	 *
	 * @return the type tree node.
	 */
	private ExpressionType getIdentifierType(final IdentifierTree node) {
		final TreePath path = mapTreePath.get(node);
		final String nodeName = node.getName().toString();
		if (path == null)
			throw new TranspilerException("Transpiler Error: Cannot retrieve tree path object for the specified identifier.");
		if (("this".equals(nodeName)) || ("" + classTree.getSimpleName()).equals(nodeName))
			return ExpressionClassType.getExpressionClassType(transpiler, classElement);
		if (("super".equals(nodeName))) {
			final Tree superType = classTree.getExtendsClause();
			if (superType == null)
				return new ExpressionTypeNone(TypeKind.NONE);
			return ExpressionClassType.getExpressionClassType(transpiler, (TypeElement) transpiler.getElement(superType));
		}
		final Set<Tree> scopesLocalMethods = allLocalMethods.get(nodeName);
		if (scopesLocalMethods != null) {
			for (TreePath current = path; current.getParentPath() != null; current = current.getParentPath()) {
				final Tree scope = current.getLeaf();
				if (scopesLocalMethods.contains(scope)) {
					final MethodTree method = allLocalMethodsByScope.get(scope).get(nodeName);
					return ExpressionType.getExpressionType(transpiler, method.getReturnType());
				}
			}
		}
		final Set<Tree> scopesLocalVariables = allLocalVariables.get(nodeName);
		if (scopesLocalVariables != null) {
			for (TreePath current = path; current.getParentPath() != null; current = current.getParentPath()) {
				final Tree scope = current.getLeaf();
				if (scopesLocalVariables.contains(scope)) {
					final VariableTree variable = allLocalVariablesByScope.get(scope).get(nodeName);
					return ExpressionType.getExpressionType(transpiler, variable.getType());
				}
			}
		}
		// check type parameters
		if (typeParameters.contains(nodeName)) {
			final Element elem = transpiler.getElement(node);
			if ((elem instanceof final TypeParameterElement tpe) && (tpe.asType().getKind() == TypeKind.TYPEVAR))
				return ExpressionTypeVar.getExpressionTypeVariable(transpiler, tpe.asType());
			throw new TranspilerException("Transpiler Error: Cannot determine type of type parameter identifier '" + nodeName + "' in transpiler unit " + classTree.getSimpleName());
		}

		// get identifier of local package members if a transpiler unit exists
		if (transpiler.hasTranspilerUnit(getPackageName(), nodeName))
			return ExpressionClassType.getExpressionClassType(transpiler, transpiler.getTypeElement(getPackageName() + "." + nodeName));

		// check imports
		final String importPackageName = imports.get(nodeName);
		if (importPackageName != null)
			return ExpressionClassType.getExpressionClassType(transpiler, transpiler.getTypeElement(importPackageName + "." + nodeName));

		// check in imports for nested classes
		final Entry<String, String> importsEntry = importsSuper.entrySet().stream()
			.filter(e -> e.getKey().endsWith(nodeName))
			.max((a, b) -> Integer.compare(a.getKey().length(), b.getKey().length()))
			.orElse(null);
		if (importsEntry != null)
			return ExpressionClassType.getExpressionClassType(transpiler, transpiler.getTypeElement(importsEntry.getValue() + "." + importsEntry.getKey()));

		// check annotations
		final String annotationPackageName = annotations.get(nodeName);
		if (annotationPackageName != null)
			return ExpressionClassType.getExpressionClassType(transpiler, transpiler.getTypeElement(annotationPackageName + "." + nodeName));

		// return class type for specific known classes
		final String canonicalNodeName = switch (nodeName) {
			case "Comparable", "String", "StringBuilder", "System", "Math",
			     "Boolean", "Byte", "Short", "Integer", "Long", "Float", "Double",
			     "NumberFormatException",
			     "NullPointerException", "UnsupportedOperationException",
			     "Exception",
			     "CloneNotSupportedException", "IndexOutOfBoundsException"
			     -> "java.lang." + nodeName;
			default -> null;
		};
		if (canonicalNodeName != null)
			return ExpressionClassType.getExpressionClassType(transpiler, transpiler.getTypeElement(canonicalNodeName));

		// check whether its a case tree in a switch expression
		final TreePath parent = path.getParentPath();
		if (parent.getLeaf() instanceof CaseTree) {
			if (parent.getParentPath().getLeaf() instanceof final SwitchTree st) {
				if (st.getExpression() instanceof final ParenthesizedTree pt) {
					if (pt.getExpression() instanceof final IdentifierTree it)
						return getIdentifierType(it);
				}
			}
		}

		// TODO check for annotation identifier types
		final Element element = transpiler.getElement(node);
		if ((element != null) && (element instanceof final ExecutableElement ee))
			return ExpressionClassType.getExpressionClassType(transpiler, ee.getReturnType());
		throw new TranspilerException("Transpiler Error: Cannot determine type of identifier '" + nodeName + "' in transpiler unit " + classTree.getSimpleName());
	}


	/**
	 * Determines the method invocation tree, if the specified member select selects a method.
	 * If not null is returned.
	 *
	 * @param node    the member select tree node
	 *
	 * @return the method invocation tree node or null
	 */
	private MethodInvocationTree getMethodInvocationTree(final MemberSelectTree node) {
		final TreePath path = mapTreePath.get(node);
		if (path == null)
			throw new TranspilerException("Transpiler Error: Cannot retrieve tree path object for the specified identifier.");
		final Tree parent = path.getParentPath().getLeaf();
		final MethodInvocationTree miTree = (parent instanceof MethodInvocationTree) ? (MethodInvocationTree) parent : null;
		if (miTree == null)
			return null;
		final String memberName = node.getIdentifier().toString();
		if (!(miTree.getMethodSelect() instanceof MemberSelectTree))
			return null;
		final String parentChildMemberName = ((MemberSelectTree) miTree.getMethodSelect()).getIdentifier().toString();
		return (memberName.equals(parentChildMemberName)) ? miTree : null;
	}


	/**
	 * Returns the type tree node that is associated with the member select node
	 *
	 * @param node   the member select node
	 *
	 * @return the type tree node.
	 */
	private ExpressionType getMemberSelectType(final MemberSelectTree node) {
		final MethodInvocationTree miTree = getMethodInvocationTree(node);
		final String name = node.getExpression().toString();
		ExpressionType type = "this".equals(name) ? ExpressionClassType.getExpressionClassType(transpiler, classElement) : allExpressionTypes.get(node.getExpression());
		if (type == null)
			throw new TranspilerException("Transpiler Error: Cannot get the expression type for the member select expression");
		if (type instanceof ExpressionArrayType) {
			if ("length".equals(node.getIdentifier().toString()))
				return new ExpressionPrimitiveType(TypeKind.INT);
			return type;
		}
		// handle ExpressionTypeVar - if unbound or superbounded check Object members, if it has an extend bound check the bounding Type and all super classes and interfaces
		if (type instanceof final ExpressionTypeVar etv) {
			if (etv.getExtendsBound() != null) {
				type = etv.getExtendsBound();
			} else {
				type = ExpressionClassType.getExpressionClassType(transpiler, transpiler.getTypeElement("java.lang.Object"));
			}
		}
		if (!(type instanceof ExpressionClassType))
			throw new TranspilerException("Transpiler Error: Expression Type " + type.getKind() + " not supported for the member select expression");
		final ExpressionClassType typeOfClass = (ExpressionClassType) type;
		final String packageName = typeOfClass.getPackageName();
		final String typeName = typeOfClass.toString();
		final String member = node.getIdentifier().toString();
		if (miTree == null) {
			// check Attribute
			ExpressionType result = transpiler.getAttributeType(packageName + "." + typeName, member);
			if (result != null)
				return result;
			if ("class".equals(member))
				return ExpressionClassType.getExpressionClassType(transpiler, typeOfClass);
			// check for nested classes or interfaces
			result = transpiler.getNestedType(packageName + "." + typeName, member);
			if (result != null)
				return result;
			throw new TranspilerException("Transpiler Error: Could not retrieve parameter type for " + name + "." + member);
		}

		// check method invocation
		final ArrayList<ExpressionType> paramTypes = new ArrayList<>();
		for (final ExpressionTree param : miTree.getArguments()) {
			final ExpressionType paramType = allExpressionTypes.get(param);
			if (paramType == null)
				throw new TranspilerException("Transpiler Error: Could not retrieve parameter type for " + param + " of method " + name + "." + member);
			paramTypes.add(paramType);
		}
		final ExpressionType result = transpiler.getMethodReturnType(this, node, typeOfClass, member, paramTypes);
		if (result != null)
			return result;
		if ("this".equals(name)) {
		    // TODO Prüfe zusätzlich, ob es sich um einen Enum-Type handelt. Ansonsten könnte compareTo nicht definiert sein
		    if ("compareTo".equals(member))
		        return new ExpressionPrimitiveType(TypeKind.INT);
		}
		if (typeOfClass.getKind() == Kind.ENUM) {
		    if ("compareTo".equals(member))
		        return new ExpressionPrimitiveType(TypeKind.INT);
		}
		throw new TranspilerException("Transpiler Error: Cannot determine method return type for " + name + "." + member + " : " + packageName + "." + typeName);
	}


	/**
	 * Determines the result type of the specified binary operation
	 *
	 * @param binary   the binary operation
	 *
	 * @return the binary operation result type
	 */
	private ExpressionType getBinaryOperationResultTyp(final BinaryTree binary) {
		final ExpressionType leftType = allExpressionTypes.get(binary.getLeftOperand());
		final ExpressionType rightType = allExpressionTypes.get(binary.getRightOperand());
		// TODO Unbox primitive numeric types...
		return switch (binary.getKind()) {
			case LESS_THAN, GREATER_THAN, LESS_THAN_EQUAL, GREATER_THAN_EQUAL, EQUAL_TO,
			     NOT_EQUAL_TO, AND, XOR, OR, CONDITIONAL_AND, CONDITIONAL_OR
			     -> new ExpressionPrimitiveType(TypeKind.BOOLEAN);
			case LEFT_SHIFT, RIGHT_SHIFT, UNSIGNED_RIGHT_SHIFT -> leftType;
			case MULTIPLY, DIVIDE, REMAINDER, MINUS, PLUS -> {
				if (leftType.toString().equals("String") || rightType.toString().equals("String"))
					yield ExpressionClassType.getExpressionClassType(transpiler, transpiler.getTypeElement("java.lang.String"));
				if (!leftType.isPrimitiveOrBoxedPrimitive() || !rightType.isPrimitiveOrBoxedPrimitive()) {
					if (binary.getKind() == Tree.Kind.PLUS)
						throw new TranspilerException("Transpiler Error: Operands of binary operation of kind " + binary.getKind() + " must be numeric or string types");
					throw new TranspilerException("Transpiler Error: Operands of binary operation of kind " + binary.getKind() + " must be numeric types");
				}
				final ExpressionPrimitiveType left = (leftType instanceof final ExpressionClassType lct) ? ExpressionPrimitiveType.getUnboxed(lct) : (ExpressionPrimitiveType) leftType;
				final ExpressionPrimitiveType right = (rightType instanceof final ExpressionClassType rct) ? ExpressionPrimitiveType.getUnboxed(rct) : (ExpressionPrimitiveType) rightType;
				// TODO check invalid primitive kinds
				if ((left.getPrimitiveTypeKind() == TypeKind.DOUBLE) || (right.getPrimitiveTypeKind() == TypeKind.DOUBLE))
					yield new ExpressionPrimitiveType(TypeKind.DOUBLE);
				if ((left.getPrimitiveTypeKind() == TypeKind.FLOAT) || (right.getPrimitiveTypeKind() == TypeKind.FLOAT))
					yield new ExpressionPrimitiveType(TypeKind.FLOAT);
				if ((left.getPrimitiveTypeKind() == TypeKind.LONG) || (right.getPrimitiveTypeKind() == TypeKind.LONG))
					yield new ExpressionPrimitiveType(TypeKind.LONG);
				yield new ExpressionPrimitiveType(TypeKind.INT);
			}
			default -> throw new TranspilerException("Transpiler Error: Unhandled binary operation of kind " + binary.getKind());
		};
	}


	/**
	 * Determines all the result types for all expression of this transpiler unit
	 */
	public void determineExpressionTypes() {
		// cycle through all expressions in reverse order, i.e. from the tree leaves to the tree root
		for (int i = allExpressions.size() - 1; i >= 0; i--) {
			final ExpressionTree expr = allExpressions.get(i);
			if (expr instanceof final IdentifierTree ident) {
				allExpressionTypes.put(ident, getIdentifierType(ident));
			} else if (expr instanceof final AssignmentTree assignment) {
				allExpressionTypes.put(assignment, allExpressionTypes.get(assignment.getExpression()));
			} else if (expr instanceof final AnnotatedTypeTree annotatedType) {
				if (annotatedType.getUnderlyingType() instanceof final ArrayTypeTree att)
					allExpressionTypes.put(annotatedType, ExpressionType.getExpressionType(transpiler, att));
				else
					allExpressionTypes.put(annotatedType, allExpressionTypes.get(annotatedType.getUnderlyingType()));
			} else if (expr instanceof final AnnotationTree annotation) {
				allExpressionTypes.put(annotation, allExpressionTypes.get(annotation.getAnnotationType()));
			} else if (expr instanceof final ArrayAccessTree arrayAccess) {
				final ExpressionType type = allExpressionTypes.get(arrayAccess.getExpression());
				if (type instanceof final ExpressionArrayType exprArrayType) {
					allExpressionTypes.put(arrayAccess, exprArrayType.getAccessed());
				} else {
					throw new TranspilerException("Transpiler Error: Unexpected Expression Type.");
				}
			} else if (expr instanceof final UnaryTree unary) {
				allExpressionTypes.put(unary, allExpressionTypes.get(unary.getExpression()));
			} else if (expr instanceof final BinaryTree binary) {
				allExpressionTypes.put(binary, getBinaryOperationResultTyp(binary));
			} else if (expr instanceof final ConditionalExpressionTree conditionalExpr) {
				allExpressionTypes.put(conditionalExpr, allExpressionTypes.get(conditionalExpr.getTrueExpression()));
			} else if (expr instanceof final InstanceOfTree exprInstanceOf) {
				allExpressionTypes.put(exprInstanceOf, new ExpressionPrimitiveType(TypeKind.BOOLEAN));
			} else if (expr instanceof final CompoundAssignmentTree compoundAssignment) {
				allExpressionTypes.put(compoundAssignment, allExpressionTypes.get(compoundAssignment.getVariable()));
			} else if (expr instanceof final LiteralTree literal) {
				allExpressionTypes.put(literal, ExpressionType.getExpressionType(transpiler, literal));
			} else if (expr instanceof final MemberSelectTree memberSelect) {
				allExpressionTypes.put(memberSelect, getMemberSelectType(memberSelect));
			} else if (expr instanceof final MethodInvocationTree methodInvocation) {
				allExpressionTypes.put(methodInvocation, allExpressionTypes.get(methodInvocation.getMethodSelect()));
			} else if (expr instanceof final NewArrayTree newArray) {
				allExpressionTypes.put(newArray, ExpressionType.getExpressionType(transpiler, newArray));
			} else if (expr instanceof final NewClassTree newClass) {
				allExpressionTypes.put(newClass, ExpressionType.getExpressionType(transpiler, newClass.getIdentifier()));
			} else if (expr instanceof final ParenthesizedTree parenthesizedTree) {
				allExpressionTypes.put(parenthesizedTree, allExpressionTypes.get(parenthesizedTree.getExpression()));
			} else if (expr instanceof final TypeCastTree typeCast) {
				allExpressionTypes.put(typeCast, ExpressionType.getExpressionType(transpiler, typeCast.getType()));
			} else if (expr instanceof final LambdaExpressionTree lambdaExpression) {
				allExpressionTypes.put(lambdaExpression, ExpressionTypeLambda.getExpressionTypeLambda(transpiler, lambdaExpression));
			}

			// TODO MemberReferenceTree
		}
	}


}


