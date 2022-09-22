package de.nrw.schule.svws.transpiler.typescript;

import com.sun.source.tree.VariableTree;

/**
 * This class is used to store the information of transpiled java method parameters.
 */
public class VariableNode {

	/** the {@link VariableTree} object of the java compiler */
	private final VariableTree variable;
	
	/** the {@link TypeNode} object for this variable */
	private final TypeNode typeNode;
	
	/** indicates whether this variable declaration is variable argument definition*/
	private final boolean isVarArg;
	
	/** indicates whether this variable declaration is a static class attribute */
	private final boolean isStatic;
	
	
	/**
	 * Creates a new variable with transpiled variable information.
	 * 
	 * @param plugin     the {@link TranspilerTypeScriptPlugin} used
	 * @param variable   the {@link VariableTree} object of the java compiler 
	 */
	public VariableNode(final TranspilerTypeScriptPlugin plugin, final VariableTree variable) {
		this.variable = variable;
		this.isVarArg = variable.toString().contains("...");
		this.isStatic = plugin.getTranspiler().hasStaticModifier(variable);
		boolean isNotNull = isVarArg || plugin.getTranspiler().hasNotNullAnnotation(variable);
		this.typeNode = new TypeNode(plugin, variable.getType(), true, isNotNull);
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
	public String getTypeCheck(String identifier) {
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
	public String getTypeCast(String identifier) {
		return typeNode.getTypeCast(identifier);
	}
	

	/**
	 * Returns the transpiled code of this node.
	 * 
	 * @return the transpiled code
	 */
	public String transpile() {
		return "%s%s : %s".formatted(this.isVarArg ? "..." : "", "" + variable.getName(), this.transpileType());
	}
	
		
}
