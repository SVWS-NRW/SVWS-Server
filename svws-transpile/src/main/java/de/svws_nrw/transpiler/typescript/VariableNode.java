package de.svws_nrw.transpiler.typescript;

import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import com.sun.source.tree.VariableTree;

import de.svws_nrw.transpiler.Transpiler;

/**
 * This class is used to store the information of transpiled java method parameters.
 */
public class VariableNode {

	/** the {@link VariableElement} object of the java compiler */
	private final VariableElement varElem;

	/** the {@link VariableTree} object of the java compiler */
	private final VariableTree variable;

	/** the {@link TypeNode} object for this variable */
	private final TypeNode typeNode;

	/** indicates whether this variable declaration is variable argument definition*/
	private final boolean isVarArg;

	/** indicates whether this variable declaration is a static class attribute */
	private final boolean isStatic;

	/** indicates whether this variable declaration is marked final */
	private final boolean isFinal;


	/**
	 * Creates a new variable with transpiled variable information.
	 *
	 * @param plugin     the {@link TranspilerTypeScriptPlugin} used
	 * @param variable   the {@link VariableTree} object of the java compiler
	 */
	public VariableNode(final TranspilerTypeScriptPlugin plugin, final VariableTree variable) {
		this.variable = variable;
		this.varElem = null;
		this.isVarArg = variable.toString().contains("...");
		this.isStatic = plugin.getTranspiler().hasStaticModifier(variable);
		this.isFinal = plugin.getTranspiler().hasFinalModifier(variable);
		final boolean isNotNull = isVarArg || plugin.getTranspiler().hasNotNullAnnotation(variable);
		this.typeNode = new TypeNode(plugin, variable.getType(), true, isNotNull);
		if (this.isVarArg)
			this.typeNode.setIsVarArg();
	}


	/**
	 * Creates a new variable with transpiled variable information.
	 *
	 * @param plugin      the {@link TranspilerTypeScriptPlugin} used
	 * @param varElem     the {@link VariableElement} object of the java compiler
	 * @param isVarArg    specifies whether this variable node is a methods var arg parameter
	 * @param resolved    a map with a mapping from a type variable to its resolved type
	 */
	public VariableNode(final TranspilerTypeScriptPlugin plugin, final VariableElement varElem, final boolean isVarArg,
			final Map<String, TypeMirror> resolved) {
		this.variable = null;
		this.varElem = varElem;
		this.isVarArg = isVarArg;
		this.isStatic = varElem.getModifiers().contains(Modifier.STATIC);
		this.isFinal = varElem.getModifiers().contains(Modifier.FINAL);
		final boolean isNotNull = isVarArg || Transpiler.hasNotNullAnnotation(varElem);
		this.typeNode = new TypeNode(plugin, varElem.asType(), true, isNotNull, resolved);
		if (this.isVarArg)
			this.typeNode.setIsVarArg();
	}


	/**
	 * Returns whether this variable declaration is a static class attribute
	 *
	 * @return true if this variable declaration is a static class attribute and false otherwise
	 */
	public boolean isStatic() {
		return this.isStatic;
	}


	/**
	 * Returns whether this variable declaration is marked final
	 *
	 * @return true if this variable declaration is marked final and false otherwise
	 */
	public boolean isFinal() {
		return this.isFinal;
	}


	/**
	 * Returns the transpiled code for the type of this node.
	 *
	 * @return the transpiled code for the type of this node
	 */
	public String transpileType() {
		return typeNode.transpile(false);
	}


	/**
	 * Returns the type node of this variable node.
	 *
	 * @return the type node
	 */
	public TypeNode getTypeNode() {
		return this.typeNode;
	}


	/**
	 * Returns transpiled code to check whether the specified identifier matches
	 * the type of this variable.
	 *
	 * @param identifier   the identifier to be checked
	 *
	 * @return the type check code
	 */
	public String getTypeCheck(final String identifier) {
		return typeNode.getTypeCheck(identifier);
	}


	/**
	 * Returns transpiled code for casting the specified identifier
	 * to the type of this variable. Casting is only used for Java
	 * object types.
	 *
	 * @param identifier   the identifier
	 *
	 * @return the transpiled code
	 */
	public String getTypeCast(final String identifier) {
		return typeNode.getTypeCast(identifier);
	}


	/**
	 * Gibt den Namen der Variable zurück.
	 *
	 * @return der Name der Variable
	 */
	public String getName() {
		return (variable == null) ? varElem.getSimpleName().toString() : variable.getName().toString();
	}


	/**
	 * Returns the transpiled code of this node.
	 *
	 * @return the transpiled code
	 */
	public String transpile() {
		if ((variable != null) && (Transpiler.isDeclaredUsingVar(variable)))
			return "" + this.getName();
		return "%s%s : %s".formatted(this.isVarArg ? "..." : "", "" + this.getName(), this.transpileType());
	}

}
