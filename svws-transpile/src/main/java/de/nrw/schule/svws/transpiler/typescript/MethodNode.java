package de.nrw.schule.svws.transpiler.typescript;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;
import java.util.stream.Collectors;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.VariableTree;

import de.nrw.schule.svws.transpiler.TranspilerException;

/**
 * This class is used to store the information of transpiled java methods. This 
 * information can be used to group java methods with the same name. For type script
 * such methods must have a common method body. This can be achieved using optional 
 * parameters and union types for the implementation.   
 */
public class MethodNode {
	
	/** a hashmap with all method nodes created by a constructor call of this class */
	final static HashMap<MethodTree, MethodNode> methodNodes = new HashMap<>(); 
	
	/** the {@link TranspilerTypeScriptPlugin} used */
	private final TranspilerTypeScriptPlugin plugin;
	
	/** the class this method belongs to */
	private final ClassTree _class;
	
	/** the {@link MethodTree} object of the java compiler */
	private final MethodTree method;
	
	/** if true the method belongs to an enumeration */
	private final boolean isEnum;
	
	/** the name of the method */
	private final String name; 

	/** the indent to be used for formatting the method comment */
	private final String indent;
	
	/** the methods comment, transpiled and indented */
	private String comment;
	
	/** specifies whether the method is static or not */
	private boolean isStatic;
	
	/** the methods access modifier */
	private String accessModifier;
	
	/** the list of parameter variables */
	private final ArrayList<VariableNode> parameters = new ArrayList<>();
	
	/** the return type of this method, null for constructors */
	private final TypeNode returnType;

	/** the statement with the super constructor call if it is the first statement in the methode block. */
	private final StatementTree superConstructorCall;
	
	/** the methods body */
	private String body;


	/**
	 * Creates a new method with transpiled method information.
	 * 
	 * @param plugin   the {@link TranspilerTypeScriptPlugin} used
	 * @param clazz    the class this method belongs to
	 * @param method   the {@link MethodTree} object of the java compiler 
	 * @param indent   the indent to be used for formatting the method comment
	 */
	public MethodNode(final TranspilerTypeScriptPlugin plugin, final ClassTree clazz, final MethodTree method, final String indent) {
		if (method == null)
			throw new TranspilerException("Transpiler Error: MethodTree should not be null");
		MethodNode.methodNodes.put(method, this);
		this.plugin = plugin;
		this._class = clazz;
		this.method = method;
		this.isEnum = (this._class.getKind() == Tree.Kind.ENUM);
		String name = this.method.getName().toString();
		this.name = "<init>".equals(name) ? "constructor" : "" + name;
		this.indent = indent;
		this.comment = formatComment(this.plugin.getTranspiler().getComment(method));
		this.accessModifier = this.plugin.getTranspiler().getAccessModifier(method);
		this.isStatic = this.plugin.getTranspiler().hasStaticModifier(method);
		List<? extends VariableTree> parameters = method.getParameters();
		for (int i = 0; i < parameters.size(); i++) {
			VariableNode varNode = new VariableNode(plugin, parameters.get(i));
			this.parameters.add(varNode);
		}
		boolean isNotNull = plugin.getTranspiler().hasNotNullAnnotation(method);
		this.returnType = "constructor".equals(this.name) ? null : new TypeNode(plugin, method.getReturnType(), true, isNotNull);
		plugin.indentC++;
		this.superConstructorCall = getConstructorSuperCall();
		this.body = plugin.convertBlock(method.getBody(), this.superConstructorCall != null);
		plugin.indentC--;		
	}

	
	/**
	 * Returns the return type node of this method
	 * 
	 * @return the return type node or null for constructors
	 */
	public TypeNode getReturnType() {
		return this.returnType;
	}
	

	/**
	 * Returns the name of this method
	 * 
	 * @return the name of this method
	 */
	public String getName() {
		return this.name;
	}


	/**
	 * Returns the transpiled comment of this method 
	 * 	
	 * @param comment   the original comment, not yet transpiled
	 */
	private String formatComment(String comment) {
        return (comment == null) 
        		? "" 
        		: indent + "/**" + System.lineSeparator() 
                + Arrays.asList(comment.split("\\r?\\n")).stream().map(s -> indent + " *" +  s.stripTrailing()).collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator()
                + indent + " */" + System.lineSeparator();
	}


	/**
	 * Returns the transpiled and indented java comment
	 * 
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	
	/**
	 * Returns the access modifier of this method
	 * 
	 * @return the access modifier
	 */
	public String getAccessModifier() {
		return accessModifier;
	}


	/**
	 * Sets the access modifier of this method
	 * 
	 * @param accessModifier   the access modifier
	 */
	public void setAccessModifier(String accessModifier) {
		this.accessModifier = accessModifier;
	}
	
	
	/**
	 * Returns whether the mthod is static or not
	 * 
	 * @return true if the method is static and false otherwise
	 */
	public boolean isStatic() {
		return isStatic;
	}
	

	/**
	 * Returns whether this method is a constructor or not.
	 *  
	 * @return true if this method is a constructor and false otherwise
	 */
	public boolean isConstructor() {
		return "constructor".equals(this.name);
	}
	
	
	/**
	 * Returns the first statement in the method block if it is
	 * a super constructor call.
	 *  
	 * @return the first statement in the method block if it is
	 *         a super constructor call and null otherwise 
	 */
	private StatementTree getConstructorSuperCall() {
		if (!isConstructor())
			return null;
		BlockTree block = this.method.getBody();
		if (block == null)
			return null;
		List<? extends StatementTree> statements = block.getStatements();
		if ((statements == null) || (statements.size() < 1))
			return null;
		StatementTree firstStatement = statements.get(0);
		String stmt = firstStatement.toString();
		if ((stmt == null) || ("".equals(stmt)))
			return null;  // this should be unreachable code
		return stmt.startsWith("super") ? firstStatement : null;
	}
	
	
	/**
	 * Returns the method body without braces.
	 * 
	 * @return the method body.
	 */
	public String getBody() {
		return body;
	}
	
	
	/**
	 * Sets the method body
	 * 
	 * @param body   the method body
	 */
	public void setBody(String body) {
		this.body = body;
	}


	/**
	 * Prints the method head using the specified {@link StringBuilder} and the indent
	 * for this method. A body is not printed and the head ends with a semicolon.
	 * 
	 * @param sb   the {@link StringBuilder}
	 */
	public void printHead(StringBuilder sb) {
		// the comment
		sb.append(comment);
		
		// the methods access modifier
		sb.append(indent);
		if (!"".equals(accessModifier)) {
			sb.append(accessModifier);
			sb.append(" ");
		}

		// the static modifier
		if (isStatic)
			sb.append("static ");
		
		// the methods name
		sb.append(name);
		
		// the type parameter list
		sb.append(plugin.convertTypeParameters(method.getTypeParameters(), true));
		
		// the parameter List
		sb.append("(");
		boolean isEnumConstructor = isEnum && isConstructor();
		if (isEnumConstructor) {
			sb.append("name : string, ordinal : number");
			if (parameters.size() > 0)
				sb.append(", ");
		}
		for (int i = 0; i < parameters.size(); i++) {
			sb.append(parameters.get(i).transpile());   
			if (i < parameters.size() - 1)
				sb.append(", ");
		}
		sb.append(")");
		
		// the return type if the method is not a constructor
		if (returnType != null) {
			sb.append(" : ");
			sb.append(returnType.transpile(false));
		}
		sb.append(";");
		sb.append(System.lineSeparator());
	}
	
	
	/**
	 * Returns the number of parameters of this method. 
	 * 
	 * @return the number of parameters of this method.
	 */
	public int getParameterCount() {
		return parameters.size();
	}
	

	/**
	 * Returns the name of the parameter specified by the index i
	 * 
	 * @param i   the index number in the parameter list
	 * 
	 * @return the name of the parameter specified by the index i.
	 */
	public VariableNode getParameter(int i) {
		if (i >= parameters.size())
			return null;
		return parameters.get(i);
	}


	/**
	 * Sets the less restrivtice common access modifer at the specified
	 * methods.
	 * 
	 * @param methods   the methods
	 */
	public static void setCommonAccessModifier(List<MethodNode> methods) {
		String commonAccessModifier = getCommonAccessModifier(methods);
		for (MethodNode method : methods)
			method.accessModifier = commonAccessModifier;
	}
	
	/**
	 * Determines the access modifier of all specified methods that is
	 * least restrictive.
	 * 
	 *  @param methods   the methods
	 */
	private static String getCommonAccessModifier(List<MethodNode> methods) {
		String result = methods.get(0).getAccessModifier();
		for (int i = 1; i < methods.size(); i++) {
			String current = methods.get(i).getAccessModifier();
			if ("public".equals(current) || "".equals(current))
				result = "public";
			else if ("protected".equals(current) && "private".equals(result))
				result = "protected";
		}
		return result;
	}
	
	
	/**
	 * Determines whether all or none of the methods have the static modifier.
	 * If only some have the static modifier an exception ist thrown.
	 *  
	 * @param methods   the methods
	 * 
	 * @return true if all are static and false if none is static
	 */
	private static boolean areStatic(List<MethodNode> methods) {
		boolean result = methods.get(0).isStatic;
		for (int i = 1; i < methods.size(); i++) {
			if (methods.get(0).isStatic != result)
				throw new RuntimeException("Methods with the same name must either all be static or none of them.");
		}
		return result;
	}
	
	
	/**
	 * Returns whether the parameter at index i must be optional for a common 
	 * method implementation of all methods in the specified list.
	 * 
	 * @param methods   the list of methods
	 * @param i         the index of the parameter
	 * 
	 * @return true if the parameter must be optional and false otherwise
	 */
	private static boolean isOptionalParameter(List<MethodNode> methods, int i) {
		for (MethodNode m : methods) {
			if (i >= m.getParameterCount())
				return true;
		}
		return false;
	}

	
	/**
	 * Returns the union type for the parameter at index i for the common method 
	 * implementation of all methods in the specified list. 
	 * 
	 * @param methods   the list of methods
	 * @param i         the index of the parameter
	 * 
	 * @return the union type
	 */
	private static String getUnionParamType(List<MethodNode> methods, int i) {
		TreeSet<String> types = new TreeSet<>();
		for (MethodNode m : methods) {
			String type = m.getParameter(i) == null ? null : m.getParameter(i).transpileType();
			if (type != null) {
				String[] tmpTypesSplit = type.split("\\|");
				int brackets = 0;
				String tmpTypeResult = "";
				for (String tmpType : tmpTypesSplit) {
					brackets += tmpType.replace(">", "").length() - tmpType.replace("<", "").length(); // determine the number of open < brackets
					if (brackets > 0) {
						tmpTypeResult += tmpType + "|";
					} else {
						types.add((tmpTypeResult + tmpType).trim());
						tmpTypeResult = "";
					}
				}
			}
		}
		return types.stream().collect(Collectors.joining(" | "));
	}

	
	/**
	 * Returns the union return type for the common method implementation of all 
	 * methods in the specified list. 
	 * 
	 * @param methods   the list of methods
	 * 
	 * @return the union return type
	 */
	private static String getUnionReturnType(List<MethodNode> methods) {
		TreeSet<String> types = new TreeSet<>();
		for (MethodNode m : methods) {
			if (m.returnType != null) {
				String type = m.returnType.transpile(false);
				String[] tmpTypesSplit = type.split("\\|");
				int brackets = 0;
				String tmpTypeResult = "";
				for (String tmpType : tmpTypesSplit) {
					brackets += tmpType.replace(">", "").length() - tmpType.replace("<", "").length(); // determine the number of open < brackets
					if (brackets > 0) {
						tmpTypeResult += tmpType + "|";
					} else {
						types.add((tmpTypeResult + tmpType).trim());
						tmpTypeResult = "";
					}
				}
			}
		}
		return types.stream().collect(Collectors.joining(" | "));
	}

	
	/**
	 * Returns a type check for the parameter with the index i of this method.
	 * 
	 * @param i      the index of the parameter
	 * 
	 * @return the type script code to check the type of a methods parameter at index i 
	 */
	private String getTypeCheck(int i) {
		if (i >= parameters.size())
			return "(typeof __param" + i + " === \"undefined\")";
		return "((typeof __param" + i + " !== \"undefined\") && " + getParameter(i).getTypeCheck("__param" + i) + ")";
	}


	/**
	 * Prints the implementation of a method with multiple signatures 
	 * 
	 * @param sb          the StringBuilder
	 * @param indent      the indent used to print the implementation
	 * @param methods     the list of methods
	 * @param className   the name of the class to be used in an enum constructor
	 */
	public static void printImplementation(StringBuilder sb, String indent, List<MethodNode> methods, String className) {
		// a comment for the method implementation
		// TODO also describe parameters...
		String comment = indent + "/**" + System.lineSeparator()
		               + indent + " * Implementation for method overloads of '" + methods.get(0).getName() + "'" + System.lineSeparator()
		               + indent + " */" + System.lineSeparator();
		sb.append(comment);
		
		// the methods access modifier
		sb.append(indent);
		String am = getCommonAccessModifier(methods);
		if (!"".equals(am)) {
			sb.append(am);
			sb.append(" ");
		}
		
		if (areStatic(methods))
			sb.append("static ");
		
		// the methods name
		sb.append(methods.get(0).getName());
		
		// the type parameter list
		Vector<TypeParameterTree> typeParams = new Vector<>();
		for (MethodNode current : methods)
			typeParams.addAll(current.method.getTypeParameters());
		sb.append(methods.get(0).plugin.convertTypeParameters(typeParams, true));
				
		// the parameter List
		sb.append("(");
		boolean isEnumConstructor = methods.get(0).isEnum && methods.get(0).isConstructor();
		if (isEnumConstructor)
			sb.append("name : string, ordinal : number, ");
		int maxParams = methods.stream().mapToInt(m -> m.getParameterCount()).max().orElse(0);
		for (int i = 0; i < maxParams; i++) {
			sb.append("__param" + i);
			if (isOptionalParameter(methods, i))
				sb.append("?");
			sb.append(" : ");
			
			sb.append(getUnionParamType(methods, i));
			
			if (i < maxParams - 1)
				sb.append(", ");
		}
		sb.append(")");
		
		// the return type if the method is not a constructor
		String returnType = getUnionReturnType(methods);
		if (!"".equals(returnType)) {
			sb.append(" : ");
			sb.append(returnType);
		}
		
		// implementation block begin
		sb.append(" {");
		sb.append(System.lineSeparator());
		
		// print the implementation for all methods
		String blockIndent = indent + "\t";
		if (methods.get(0).isConstructor()) {
			if (methods.get(0)._class.getExtendsClause() == null) {
				sb.append(blockIndent);
				sb.append("super();");
				sb.append(System.lineSeparator());
			} else {
				sb.append(blockIndent);
				sb.append(methods.get(0).plugin.convertStatement(methods.get(0).superConstructorCall, true));
				// TODO combine statement of all constructors if possible...
				sb.append(System.lineSeparator());
			}
		}
		
		if (isEnumConstructor) {
			sb.append(blockIndent);
			sb.append("this.__name = name;");
			sb.append(System.lineSeparator());
			sb.append(blockIndent);
			sb.append("this.__ordinal = ordinal;");
			sb.append(System.lineSeparator());
			sb.append(blockIndent);
			sb.append(className).append(".all_values_by_ordinal.push(this);");
			sb.append(System.lineSeparator());
			sb.append(blockIndent);
			sb.append(className).append(".all_values_by_name.set(name, this);");
			sb.append(System.lineSeparator());
		}
		for (int m = 0; m < methods.size(); m++) {
			MethodNode method = methods.get(m);
			// choose implementation by types
			if (m == 0) {
				sb.append(blockIndent);
				sb.append("if (");
			} else {
				sb.append(" else if (");
			}
			for (int i = 0; i < maxParams; i++) {
				VariableNode param = method.getParameter(i);
				if (param == null)
					sb.append("(typeof __param" + i + " === \"undefined\")");
				else
					sb.append(method.getTypeCheck(i));
				if (i < maxParams - 1)
					sb.append(" && ");
			}
			sb.append(") {");
			sb.append(System.lineSeparator());
			
			// define local variable for original parameter name
			for (int i = 0; i < method.getParameterCount(); i++) {
				sb.append(blockIndent);
				sb.append("\t");
				VariableNode param = method.getParameter(i);
				sb.append(param.isFinal() ? "const " : "let ");
				sb.append(param.transpile() + " = " + param.getTypeCast("__param" + i) + ";");
				sb.append(System.lineSeparator());
			}
			
			// print the method block
			if ("".equals(method.getBody().trim())) {
				sb.append(blockIndent);
				sb.append("\t// empty method body\n");
			} else {
				sb.append("\t" + method.getBody().replace("\n\t", "\n\t\t"));
			}
			sb.append(blockIndent);
			sb.append("}");
		}
		sb.append(" else throw new Error('invalid method overload');");
		sb.append(System.lineSeparator());
		
		// implementation block end
		sb.append(indent);
		sb.append("}");
		sb.append(System.lineSeparator());
	}


	/**
	 * Prints the method using the specified {@link StringBuilder} and the indent
	 * for this method.
	 * 
	 * @param sb                  the {@link StringBuilder}
	 * @param className           the name of the class to be used in an enum constructor
	 */
	public void print(StringBuilder sb, String className) {
		// the comment
		sb.append(comment);
		
		sb.append(indent);

		// the methods access modifier
		if (!"".equals(accessModifier)) {
			sb.append(accessModifier);
			sb.append(" ");
		}
		
		// the abstract modifier
		if (body == null)
			sb.append("abstract ");
		
		// the static modifier 
		if (isStatic)
			sb.append("static ");
		
		// the methods name
		sb.append(name);
		
		// the type parameter list
		sb.append(plugin.convertTypeParameters(method.getTypeParameters(), true));
		
		// the parameter List
		sb.append("(");
		boolean isEnumConstructor = isEnum && isConstructor();
		if (isEnumConstructor) {
			sb.append("name : string, ordinal : number");
			if (parameters.size() > 0)
				sb.append(", ");
		}
		for (int i = 0; i < parameters.size(); i++) {
			sb.append(parameters.get(i).transpile());   
			if (i < parameters.size() - 1)
				sb.append(", ");
		}
		sb.append(")");
		
		// the return type if the method is not a constructor
		if (returnType != null) {
			sb.append(" : ");
			sb.append(returnType.transpile(false));
		}

		// method block;
		if (body == null) {
			sb.append(";");
		} else {
			sb.append(" {");
			sb.append(System.lineSeparator());
			if (isConstructor()) {
				if (_class.getExtendsClause() == null) {
					sb.append(indent + "\t");
					sb.append("super();");
					sb.append(System.lineSeparator());
				} else {
					sb.append(indent + "\t");
					sb.append(plugin.convertStatement(superConstructorCall, true));
					sb.append(System.lineSeparator());
				}
			}
			if (isEnumConstructor) {
				sb.append(indent + "\t");
				sb.append("this.__name = name;");
				sb.append(System.lineSeparator());
				sb.append(indent + "\t");
				sb.append("this.__ordinal = ordinal;");
				sb.append(System.lineSeparator());
				sb.append(indent + "\t");
				sb.append(className).append(".all_values_by_ordinal.push(this);");
				sb.append(System.lineSeparator());
				sb.append(indent + "\t");
				sb.append(className).append(".all_values_by_name.set(name, this);");
				sb.append(System.lineSeparator());
			}
			sb.append(body);
			sb.append(indent);
			sb.append("}");
		}
		sb.append(System.lineSeparator());
	}



}
