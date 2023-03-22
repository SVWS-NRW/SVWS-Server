package de.nrw.schule.svws.transpiler.typescript;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.CaseTree.CaseKind;
import com.sun.source.tree.CatchTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.DoWhileLoopTree;
import com.sun.source.tree.EmptyStatementTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.InstanceOfTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;

import de.nrw.schule.svws.transpiler.ExpressionArrayType;
import de.nrw.schule.svws.transpiler.ExpressionClassType;
import de.nrw.schule.svws.transpiler.ExpressionPrimitiveType;
import de.nrw.schule.svws.transpiler.ExpressionType;
import de.nrw.schule.svws.transpiler.ExpressionTypeLambda;
import de.nrw.schule.svws.transpiler.ExpressionTypeNone;
import de.nrw.schule.svws.transpiler.ResourceUtils;
import de.nrw.schule.svws.transpiler.Transpiler;
import de.nrw.schule.svws.transpiler.TranspilerException;
import de.nrw.schule.svws.transpiler.TranspilerLanguagePlugin;
import de.nrw.schule.svws.transpiler.TranspilerResource;
import de.nrw.schule.svws.transpiler.TranspilerUnit;
import jakarta.validation.constraints.NotNull;


/**
 * This is the transpiler language plugin for Type Script. 
 */
public class TranspilerTypeScriptPlugin extends TranspilerLanguagePlugin {
	
	/** The output directory where all generated files should be placed */
	private final String outputDir;
	
	/** A prefix for the java packages that will be removed while generating the output */
	private String strIgnoreJavaPackagePrefix = "";
	
	/** A list of resources containing typescript class as a prepared replacement for java classes */
	private final List<TranspilerResource> tsResources;
	
	/** A mapping from the class name of an enumeration to the current enumeration ordinal value */
	private final HashMap<String, Integer> enumOrdinals = new HashMap<>();
	
	/** the indentation state used during transpilation */
	public int indentC = 0;  // TODO find another solution for indent

	/**
	 * Returns the transpiled comment 
	 * 	
	 * @param comment   the original comment, not yet transpiled
	 */
	private String formatComment(String comment) {
        return (comment == null) 
        		? "" 
        		: getIndent() + "/**" + System.lineSeparator() 
                + Arrays.asList(comment.split("\\r?\\n")).stream().map(s -> getIndent() + " * " + s.stripTrailing()).collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator()
                + getIndent() + " */" + System.lineSeparator();
	}
	
	
	/**
	 * Create a Type Script transpiler language plugin
	 * 
	 * @param transpiler   the transpiler that uses this plugin 
	 * @param outputDir    the output directory where all generated files should be placed
	 */
	public TranspilerTypeScriptPlugin(Transpiler transpiler, String outputDir) {
		super(transpiler);
		if (outputDir == null)
			throw new TranspilerException("Transpiler Error: An output directory for the transpiler language plugin is required.");
		this.outputDir = outputDir;
		this.tsResources = ResourceUtils.getFilesInPackage("", ".ts");		
	}

	
	/**
	 * Sets the configuration parameter for the prefix part of the java packages
	 * to be ignored while creating the sub directories for the Type Script output.
	 *   
	 * @param prefix   the prefix part of the java package to be ignored
	 */
	public void setIgnoreJavaPackagePrefix(String prefix) {
		this.strIgnoreJavaPackagePrefix = prefix;
	}

	
	/**
	 * Returns the current indentation string used during transpilation
	 *  
	 * @return the current indentation string 
	 */
	public String getIndent() {
		return "\t".repeat(indentC);
	}

	
	/**
	 * Prüft, ob der übergebene Bezeichner ein Schlüsselwort in Type-Script ist,
	 * welches nicht auch in Java ein Schlüsselwortd er Sprache ist.
	 * 
	 * @param bezeichner   der zu prüfende Bezeichner
	 */
	public void pruefeBezeichner(String bezeichner) {
		switch (bezeichner) {
			case "in", "of", "debugger", "export", "function", "typeOf", "var", "with" 
				-> throw new TranspilerException("Das Typescript-Schlüsselwort " + bezeichner + " ist als Variablenname nicht zulässig");
		}		
	}
	
	/**
	 * Transpiles the type parameter node.
	 * 
	 * @param node         the type parameter node to be transpiled
	 * @param withBounds   specifies whether the bounds of the type parametes should be transpiled or not
	 * 
	 * @return the type parameter string
	 */
	public String convertTypeParameter(TypeParameterTree node, boolean withBounds) {
		String result = node.getName().toString();
		if (withBounds) {
			TranspilerUnit unit = transpiler.getTranspilerUnit(node);
			boolean hasNotNullAnnotation = Transpiler.hasNotNullAnnotation(node.getAnnotations(), unit);
			if ((node.getBounds() != null) && (node.getBounds().size() > 0)) {
				result += " extends ";
				boolean first = true;
				for (Tree type : node.getBounds()) {
					if (!first)
						result += " & ";
					first = false;
					boolean boundHasNotNullAnnotation = false;
					if ((type instanceof AnnotatedTypeTree att))
						boundHasNotNullAnnotation = Transpiler.hasNotNullAnnotation(att.getAnnotations(), unit);
					TypeNode typeNode = new TypeNode(this, type, false, boundHasNotNullAnnotation);
					result += typeNode.transpile(false);				
				}
			}
			result += (hasNotNullAnnotation ? "" : " | null");
		}
		return result;
	}

	
	/**
	 * Transpiles the type parameters nodes.
	 * 
	 * @param nodes        the type parameter nodes to be transpiled
	 * @param withBounds   specifies whether the bounds of the type parametes should be transpiled or not 
	 * 
	 * @return the type parameters string
	 */
	public String convertTypeParameters(List<? extends TypeParameterTree> nodes, boolean withBounds) {
		if ((nodes == null) || nodes.size() <= 0)
			return "";
		return nodes.stream().map(tp -> convertTypeParameter(tp, withBounds)).collect(Collectors.joining(", ", "<", ">"));
	}
	
	
	/**
	 * Transpiles the variable declared in a statement block.
	 * 
	 * @param node     the variable declaration
	 * 
	 * @return the transpiled statement block
	 */
	public String convertBlockVariable(VariableTree node) {
		// TODO ??? getNameExpression for node.getName()
		// Prüfe Bezeichner auf Schlüsselwörter
		pruefeBezeichner(node.getName().toString());
		TypeNode typeNode = new TypeNode(this, node.getType(), true, transpiler.hasNotNullAnnotation(node));
		ExpressionTree initializer = node.getInitializer();
		if (initializer == null)
			return "let %s : %s".formatted(node.getName(),  typeNode.transpile(false));
		String strInitializer = convertExpression(initializer);
		ExpressionType typeInitializer = transpiler.getExpressionType(initializer);
		if ((node.getType().getKind() == Kind.PRIMITIVE_TYPE) && (typeInitializer instanceof ExpressionClassType) && (typeInitializer.isPrimitiveOrBoxedPrimitive()))
			strInitializer += ".valueOf()";
		return "let %s : %s = %s".formatted(node.getName(),  typeNode.transpile(false), strInitializer);
	}
	
	
	/**
	 * Transpiles the for each loop.
	 * 
	 * @param node     the enhanced for loop statement
	 * 
	 * @return the transpiled for each loop
	 */
	public String convertEnhancedForLoop(EnhancedForLoopTree node) {
		return "for (let %s of %s) %s".formatted(
			node.getVariable().getName(),
			convertExpression(node.getExpression()),
			convertStatement(node.getStatement(), false)
		);
	}

	
	/**
	 * Transpiles the for loop.
	 * 
	 * @param node     the for loop statement
	 * 
	 * @return the transpiled for loop
	 */
	public String convertForLoop(ForLoopTree node) {
		return "for (%s; %s; %s)%s".formatted(
			"let " + node.getInitializer().stream().map(st -> convertStatement(st, true)).collect(Collectors.joining(", ")).replace("let ", ""),
			convertExpression(node.getCondition()),
			node.getUpdate().stream().map(es -> convertExpressionStatement(es)).collect(Collectors.joining(", ")),
			convertStatement(node.getStatement(), false)			
		);
	}
	
	
	/**
	 * Transpiles the while loop.
	 * 
	 * @param node     the while loop statement
	 * 
	 * @return the transpiled while loop
	 */
	public String convertWhileLoop(WhileLoopTree node) {
		return "while %s %s".formatted(
			convertExpression(node.getCondition()),
			convertStatement(node.getStatement(), false)			
		);		
	}
	
	
	/**
	 * Transpiles the do while loop.
	 * 
	 * @param node     the do while loop statement
	 * 
	 * @return the transpiled do while loop
	 */
	public String convertDoWhileLoop(DoWhileLoopTree node) {
		return "do %s while %s;".formatted(
			convertStatement(node.getStatement(), false),			
			convertExpression(node.getCondition())
		);		
	}
	
	
	/**
	 * Transpiles the identifier.
	 * 
	 * @param node   the identifier
	 * 
	 * @return the transpiled identifier
	 */
	public String convertIdentifier(IdentifierTree node) {
		ExpressionType type = transpiler.getExpressionType(node);
		if (type == null)
			throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + node.getName().toString());
		String result = switch (node.getName().toString()) {
			case "String" -> "string";
			case "Long", "Integer", "Short", "Byte", "Float", "Double" -> "number";
			case "Boolean" -> "boolean";
			case "Character" -> "string";
			case "Object" -> {
				if (!((type instanceof ExpressionClassType classType) && ("Object".equals(classType.toString()))))
					yield null;
				if (transpiler.getParent(node) instanceof NewClassTree nct)
					yield "Object";
				yield "unknown";
			}
			case "Iterator" ->
				((type instanceof ExpressionClassType classType) && ("Iterator".equals(classType.toString()))) ? getImportName(classType.toString(), classType.getPackageName()) : null;
			case "Map" ->
				((type instanceof ExpressionClassType classType) && ("Map".equals(classType.toString()))) ? getImportName(classType.toString(), classType.getPackageName()) : null;
			case "Entry" ->
				((type instanceof ExpressionClassType classType) && ("Entry".equals(classType.toString())) && ("java.util.Map".equals(classType.getPackageName()))) ? getImportName(classType.toString(), classType.getPackageName()) : null;
			case "Set" ->
				((type instanceof ExpressionClassType classType) && ("Set".equals(classType.toString()))) ? getImportName(classType.toString(), classType.getPackageName()) : null;
			default -> null;
		};
		if (result != null)
			return result;
		// check whether a String or a Number expression needs a .valueOf call in typescript
		String valueOf = "";
		if ((type instanceof ExpressionClassType classType) && ((classType.isNumberType()) || (classType.isString()))) {
			Tree parent = transpiler.getParent(node);
			if ((parent instanceof BinaryTree bt) && ((bt.getKind() == Kind.MULTIPLY) || (bt.getKind() == Kind.DIVIDE)
			    || (bt.getKind() == Kind.REMAINDER) || (bt.getKind() == Kind.PLUS) || (bt.getKind() == Kind.MINUS)
			    || (bt.getKind() == Kind.LEFT_SHIFT) || (bt.getKind() == Kind.RIGHT_SHIFT) || (bt.getKind() == Kind.UNSIGNED_RIGHT_SHIFT)))
				valueOf = "!";
		}
		// check whether we have a case identifier of a switch statement where we have to add the class/enumeration name
		if ((type instanceof ExpressionClassType classType) && (transpiler.getParent(node) instanceof CaseTree)) {
			return classType.toString() + "." + node.getName().toString() + valueOf;
		}
		if (!transpiler.isClassMember(node))
			return node.getName().toString() + valueOf;
		if (transpiler.isStaticClassMember(node))
			return transpiler.getClass(node).getSimpleName().toString() + "." + node.getName().toString() + valueOf;
		return "this." + node.getName().toString() + valueOf;
	}


	/**
	 * Transpiles the member select statement.
	 * 
	 * @param node   the member select statement
	 * 
	 * @return the transpiled member select statement
	 */
	public String convertMemberSelect(MemberSelectTree node) {
		String result = switch (node.getExpression().toString()) {
			case "Boolean" -> {
				yield switch (node.getIdentifier().toString()) {
					case "parseBoolean" -> "JavaBoolean.parseBoolean";
					default -> null;
				};
			}
			case "Byte" -> {
				yield switch (node.getIdentifier().toString()) {
					case "compare" -> "JavaByte.compare";
					case "parseByte" -> "JavaByte.parseByte";
					case "MIN_VALUE" -> "JavaByte.MIN_VALUE";
					case "MAX_VALUE" -> "JavaByte.MAX_VALUE";
					case "SIZE" -> "JavaByte.SIZE";
					case "BYTES" -> "JavaByte.BYTES";
					default -> null;
				};
			}
			case "Short" -> {
				yield switch (node.getIdentifier().toString()) {
					case "compare" -> "JavaShort.compare";
					case "parseShort" -> "JavaShort.parseShort";
					case "MIN_VALUE" -> "JavaShort.MIN_VALUE";
					case "MAX_VALUE" -> "JavaShort.MAX_VALUE";
					case "SIZE" -> "JavaShort.SIZE";
					case "BYTES" -> "JavaShort.BYTES";
					default -> null;
				};
			}
			case "Integer" -> {
				yield switch (node.getIdentifier().toString()) {
					case "compare" -> "JavaInteger.compare";
					case "parseInt" -> "JavaInteger.parseInt";
					case "MIN_VALUE" -> "JavaInteger.MIN_VALUE";
					case "MAX_VALUE" -> "JavaInteger.MAX_VALUE";
					case "SIZE" -> "JavaInteger.SIZE";
					case "BYTES" -> "JavaInteger.BYTES";
					default -> null;
				};
			}
			case "Long" -> {
				yield switch (node.getIdentifier().toString()) {
					case "compare" -> "JavaLong.compare";
					case "parseLong" -> "JavaLong.parseLong";
					case "MIN_VALUE" -> "JavaLong.MIN_VALUE";
					case "MAX_VALUE" -> "JavaLong.MAX_VALUE";
					case "SIZE" -> "JavaLong.SIZE";
					case "BYTES" -> "JavaLong.BYTES";
					default -> null;
				};
			}
			case "Float" -> {
				yield switch (node.getIdentifier().toString()) {
					case "compare" -> "JavaFloat.compare";
					case "parseFloat" -> "JavaFloat.parseFloat";
					case "NaN" -> "NaN";
					case "MIN_VALUE" -> "JavaFloat.MIN_VALUE";
					case "MAX_VALUE" -> "JavaFloat.MAX_VALUE";
					case "SIZE" -> "JavaFloat.SIZE";
					case "BYTES" -> "JavaFloat.BYTES";
					default -> null;
				};
			}
			case "Double" -> {
				yield switch (node.getIdentifier().toString()) {
					case "compare" -> "JavaDouble.compare";
					case "parseDouble" -> "JavaDouble.parseDouble";
					case "NaN" -> "NaN";
					case "MIN_VALUE" -> "JavaDouble.MIN_VALUE";
					case "MAX_VALUE" -> "JavaDouble.MAX_VALUE";
					case "SIZE" -> "JavaDouble.SIZE";
					case "BYTES" -> "JavaDouble.BYTES";
					default -> null;
				};
			}
			default -> null;
		};
		if (result != null)
			return result;
		String expression = convertExpression(node.getExpression());
		String identifier = node.getIdentifier().toString();
		return expression + "." + identifier;
	}


	/**
	 * Transpiles the parenthesized node.
	 * 
	 * @param node   the parenthesized node
	 * 
	 * @return the transpiled parenthesized statement
	 */
	public String convertParenthesized(ParenthesizedTree node) {
		return "(%s)".formatted(convertExpression(node.getExpression()));
	}
	
	
	/**
	 * Transpiles the binary operation.
	 * 
	 * @param node   the binary operation node
	 * 
	 * @return the transpiled binary operation
	 */
	public String convertBinaryOperator(BinaryTree node) {
		String op = switch (node.getKind()) {
			case MULTIPLY -> "*"; 
			case DIVIDE -> "/";
			case REMAINDER -> "%";
			case PLUS -> "+";
			case MINUS -> "-";
			case LEFT_SHIFT -> "<<";
			case RIGHT_SHIFT -> ">>";
			case UNSIGNED_RIGHT_SHIFT -> ">>>";
			case LESS_THAN -> "<";
			case GREATER_THAN -> ">";
			case LESS_THAN_EQUAL -> "<=";
			case GREATER_THAN_EQUAL -> ">=";
			case INSTANCE_OF -> "instanceof";
			case EQUAL_TO -> "===";
			case NOT_EQUAL_TO -> "!==";
			case AND -> "&";
			case OR -> "|";
			case XOR -> "^";
			case CONDITIONAL_AND -> "&&";
			case CONDITIONAL_OR -> "||";
			default -> throw new TranspilerException("Transpiler Error: Binary operator of kind " + node.getKind() + " not supported by the transpiler.");
		};
		String leftExpression = convertExpression(node.getLeftOperand());
		String rightExpression = convertExpression(node.getRightOperand());
		if ((node.getKind() == Kind.EQUAL_TO) || (node.getKind() == Kind.NOT_EQUAL_TO)) {
			ExpressionType leftType = transpiler.getExpressionType(node.getLeftOperand());
			ExpressionType rightType = transpiler.getExpressionType(node.getRightOperand());
			if ((leftType instanceof ExpressionClassType) && (rightType instanceof ExpressionClassType)) {
				leftExpression += " as unknown";
				rightExpression += " as unknown";
			}
		}
		if (node.getKind() == Kind.DIVIDE) {
			ExpressionType leftType = transpiler.getExpressionType(node.getLeftOperand());
			ExpressionType rightType = transpiler.getExpressionType(node.getRightOperand());
			if (leftType.isIntegerType() && rightType.isIntegerType())
				return "Math.trunc(%s %s %s)".formatted(leftExpression, op, rightExpression);
		}
// TODO Einbinden, nachdem ExpressionType um eine Unterscheidung zwischen string und String in Typescript erweitert wurde
//		if (node.getKind() == Kind.PLUS) {
//            // TODO Überprüfung, ob es sich beim Typ um string oder String handelt hier nicht endgültig erkennbar. 
//		      // TODO ExpressionType muss zur Unterscheidung erweitert werden, um eine tiefe Analyse zuzulassen
//            ExpressionType leftType = transpiler.getExpressionType(node.getLeftOperand());
//            ExpressionType rightType = transpiler.getExpressionType(node.getRightOperand());
//            if (leftType.isString() && (node.getLeftOperand().getKind() != Kind.STRING_LITERAL))
//                leftExpression = "(" + leftExpression + ").valueOf()";
//            if (rightType.isString() && (node.getRightOperand().getKind() != Kind.STRING_LITERAL))
//                rightExpression = "(" + rightExpression + ").valueOf()";
//		}
		return "%s %s %s".formatted(leftExpression, op, rightExpression);
	}

	
	/**
	 * Transpiles the unary operation.
	 * 
	 * @param node   the unary operation node
	 * 
	 * @return the transpiled unary operation
	 */
	public String convertUnaryOperator(UnaryTree node) {
		String expr = convertExpression(node.getExpression());
		return switch(node.getKind()) {
			case POSTFIX_INCREMENT -> expr + "++"; 
			case POSTFIX_DECREMENT -> expr + "--"; 
			case PREFIX_INCREMENT -> "++" + expr; 
			case PREFIX_DECREMENT -> "--" + expr;
			case UNARY_PLUS -> "+" + expr; 
			case UNARY_MINUS -> "-" + expr;
			case BITWISE_COMPLEMENT -> "~" + expr;
			case LOGICAL_COMPLEMENT -> "!" + expr;
			default -> throw new TranspilerException("Transpiler Error: Unary operator of kind " + node.getKind() + " not supported by the transpiler.");
		};
	}
	
	
	/**
	 * Transpiles the compound assignment.
	 * 
	 * @param node   the compound assignment
	 * 
	 * @return the transpiled compound assignment
	 */
	public String convertCompoundAssignment(CompoundAssignmentTree node) {
		String variable = convertExpression(node.getVariable());
		String expression = convertExpression(node.getExpression());
		return switch (node.getKind()) {
			case MULTIPLY_ASSIGNMENT -> "%s *= %s".formatted(variable, expression); 
			case DIVIDE_ASSIGNMENT -> "%s /= %s".formatted(variable, expression);
			case REMAINDER_ASSIGNMENT -> "%s %= %s".formatted(variable, expression);
			case PLUS_ASSIGNMENT -> "%s += %s".formatted(variable, expression);
			case MINUS_ASSIGNMENT -> "%s -= %s".formatted(variable, expression);
			case LEFT_SHIFT_ASSIGNMENT -> "%s <<= %s".formatted(variable, expression);
			case RIGHT_SHIFT_ASSIGNMENT -> "%s >>= %s".formatted(variable, expression);
			case UNSIGNED_RIGHT_SHIFT_ASSIGNMENT -> "%s >>>= %s".formatted(variable, expression);
			case AND_ASSIGNMENT -> "%s = %s && %s".formatted(variable, variable, expression);
			case XOR_ASSIGNMENT -> "%s = %s ^ %s".formatted(variable, variable, expression);
			case OR_ASSIGNMENT -> "%s = %s || %s".formatted(variable, variable, expression);
			default -> throw new TranspilerException("Transpiler error: The kind " + node.getKind() + " for the compound assignment is not supported by the transpiler");
		};
	}
	
	
	/**
	 * Transpiles the array access expression.
	 * 
	 * @param node   the array access expression
	 * 
	 * @return the transpiled array access expression 
	 */
	public String convertArrayAccess(ArrayAccessTree node) {
		String arrayName = convertExpression(node.getExpression());
		String arrayIndex = convertExpression(node.getIndex());
		ExpressionType type = transpiler.getExpressionType(node.getIndex());
		if ((type instanceof ExpressionClassType ect) && (ect.isIntegerType()))
			arrayIndex += ".valueOf()";
		return "%s[%s]".formatted(arrayName, arrayIndex);
	}
	
	
	/**
	 * Transpiles the type cast expression.
	 * 
	 * @param node   the type cast expression tree node
	 * 
	 * @return the transpiled type cast expression 
	 */
	public String convertTypeCast(TypeCastTree node) {
		TypeNode typeNode = new TypeNode(this, node.getType(), false, false);
		return typeNode.getTypeCast(convertExpression(node.getExpression()));
	}
	
	
	/**
	 * Transpiles the instance of expression.
	 * 
	 * @param node   the instance of expression tree node
	 * 
	 * @return the transpiled instance of expression 
	 */
	public String convertInstanceOf(InstanceOfTree node) {
		// TODO support for PatternTree getPattern();
		TypeNode typeNode = new TypeNode(this, node.getType(), false, false);
		String obj = convertExpression(node.getExpression());
		return typeNode.getInstanceOf(obj);
	}
	
	
	/**
	 * Transpiles the lambda expression.
	 * 
	 * @param node   the lambda expression tree node
	 * 
	 * @return the transpiled lambda expression 
	 */
	public String convertLambdaExpression(LambdaExpressionTree node) {
		// determine the the method name to be used for creating the functional interface object in typescript
		ExpressionType type = transpiler.getExpressionType(node);
		String methodName = "accept";
		if (type instanceof ExpressionTypeLambda etl) {
			methodName = etl.getFunctionalInterfaceMethodName(transpiler);
		} else {
			throw new TranspilerException("TranspilerException: Cannot determine expression type for lambda expression");
		}

		// generate the lambda parameter code
		String result = "{ " + methodName + " : (";
		boolean first = true;
		for (VariableTree p : node.getParameters()) {
			if (!first)
				result += ", ";
			first = false;
			TypeNode typeNode = new TypeNode(this, p.getType(), true, transpiler.hasNotNullAnnotation(p));
			if (p.toString().contains("..."))
				result += "...";
			result += p.getName().toString();
			result += ": " + typeNode.transpile(false);
		}
		result += ")";
		// TODO return type
		// generate the lambda body
		result += " => ";
		Tree body = node.getBody();
		if (body instanceof StatementTree bodyStatement)
			return result + convertStatement(bodyStatement, false) + " }";
		if (body instanceof ExpressionTree bodyExpression)
			return result + convertExpression(bodyExpression) + " }";
		throw new TranspilerException("Transpiler Error: Lambda Expression Type of body kind " + node.getBodyKind() + " not yet supported");
	}
	

	/**
	 * Transpiles the expression.
	 * 
	 * @param node   the expression
	 * 
	 * @return the transpiled string if the expression was transpiled successfully and null otherwise
	 */
	public String convertExpression(ExpressionTree node) {
		if (node instanceof BinaryTree b)
			return convertBinaryOperator(b);
		if (node instanceof AssignmentTree a)
			return convertAssignment(a);
		if (node instanceof CompoundAssignmentTree ca)
			return convertCompoundAssignment(ca);
		if (node instanceof MethodInvocationTree mi)
			return convertMethodInvocation(mi);
		if (node instanceof LiteralTree lit)
			return convertLiteral(lit);
		if (node instanceof IdentifierTree ident)
			return convertIdentifier(ident);
		if (node instanceof MemberSelectTree ms)
			return convertMemberSelect(ms);
		if (node instanceof ParenthesizedTree paren)
			return convertParenthesized(paren);
		if (node instanceof MethodInvocationTree mi)
			return convertMethodInvocation(mi);
		if (node instanceof NewClassTree nc)
			return convertNewClass(nc);
		if (node instanceof NewArrayTree na)
			return convertNewArray(na);
		if (node instanceof ConditionalExpressionTree ce)
			return convertConditionalExpression(ce);
		if (node instanceof UnaryTree unary)
			return convertUnaryOperator(unary);
		if (node instanceof ArrayAccessTree aa)
			return convertArrayAccess(aa);
		if (node instanceof TypeCastTree tc)
			return convertTypeCast(tc);
		if (node instanceof LambdaExpressionTree le)
			return convertLambdaExpression(le);
		if (node instanceof InstanceOfTree io)
			return convertInstanceOf(io);
		throw new TranspilerException("Transpiler Error: The node of kind " + node.getKind() + " is not yet supported for an expression.");
	}
	
	
	
	/**
	 * Transpiles the conditional expression.
	 * 
	 * @param node   the conditional expression
	 * 
	 * @return the transpiled conditional expression
	 */
	public String convertConditionalExpression(ConditionalExpressionTree node) {
		return "%s ? %s : %s".formatted(
			convertExpression(node.getCondition()),
			convertExpression(node.getTrueExpression()),
			convertExpression(node.getFalseExpression())
		);
	}
	
	
	/**
	 * Transpiles the literal.
	 * 
	 * @param node   the literal
	 * 
	 * @return the transpiled literal
	 */
	public String convertLiteral(LiteralTree node) {
		return switch (node.getKind()) {
			// TODO use .valueOf() in TS when next relevant parent node ist a method invocation node 
			case INT_LITERAL, LONG_LITERAL, FLOAT_LITERAL, DOUBLE_LITERAL, BOOLEAN_LITERAL -> "" + node.getValue();
			case CHAR_LITERAL -> "'" + node.getValue() + "'";
			case STRING_LITERAL -> "\"" + ((String)node.getValue()).replace("\\", "\\\\").replace("\t", "\\t").replace("\b", "\\b")
				        .replace("\n", "\\n").replace("\r", "\\r").replace("\f", "\\f").replace("\'", "\\'")
				        .replace("\"", "\\\"") + "\"";
			case NULL_LITERAL -> "null";
			default -> throw new TranspilerException("Transpiler Exception: Unknown literal type " + node.getKind() + ".");
		};
	}
	

	/**
	 * Returns the default value for primitive types.
	 * 
	 * @param node   the primitive type tree node
	 * 
	 * @return the default value as String
	 */
	public String getDefaultValueForPrimitiveType(PrimitiveTypeTree node) {
		switch (node.getPrimitiveTypeKind()) {
			case BOOLEAN:
				return("false");
			case BYTE:
			case SHORT:
			case INT:
			case LONG:
			case FLOAT:
			case DOUBLE:
				return "0";
			case CHAR:
				return "\"\"";
			default:
				throw new TranspilerException("Transpiler Error: Default value for primitive type of kind " + node.getPrimitiveTypeKind() + " is not supported.");
		}		
	}
	
	
	/**
	 * Returns the default value for the specified type node.
	 * 
	 * @param node   the type tree node
	 * 
	 * @return the default value as String
	 */
	public String getDefaultValueForType(Tree node) {
		if (node instanceof PrimitiveTypeTree) {
			return getDefaultValueForPrimitiveType((PrimitiveTypeTree) node);
		} else if (node instanceof ArrayTypeTree) {
			return null;
		} else if (node instanceof ParameterizedTypeTree) {
			return null;
		} else if (node instanceof IdentifierTree) {
			return null;
		} else {
			throw new TranspilerException("Transpiler Error: Type node of kind " + node.getKind() + " not yet supported by the transpiler.");
		}
	}
	
	
	
	/**
	 * Transpiles the new array expression node.
	 * 
	 * @param node   the new array expression to be transpiled
	 * 
	 * @return the transpiled new array expression
	 */
	public String convertNewArray(NewArrayTree node) {
		Tree elementType = node.getType();
		if ((elementType == null) || (node.getDimensions().size() <= 0))
			return node.getInitializers().stream().map(i -> convertExpression(i)).collect(Collectors.joining(", ", "[", "]"));
		// initialize an array with null values for java compatibility
		List<? extends ExpressionTree> dimensions = node.getDimensions();
		String initializer = "Array(%s).fill(%s)".formatted(
			convertExpression(dimensions.get(dimensions.size() - 1)),
			getDefaultValueForType(elementType)
		);
		for (int i = dimensions.size() - 2; i >= 0; i--)
			initializer = "[...Array(%s)].map(e => %s)".formatted(convertExpression(dimensions.get(i)), initializer);
		return initializer;
	}
	
	
	/**
	 * Transpiles the simple assignment.
	 * 
	 * @param node   the assignment
	 * 
	 * @return the transpiled simple assignment 
	 */
	public String convertAssignment(AssignmentTree node) {
		ExpressionTree variable = node.getVariable();
		ExpressionTree expression = node.getExpression();
		String strVariable = convertExpression(variable);
		String strExpression = convertExpression(expression);
		ExpressionType typeVariable = transpiler.getExpressionType(variable);
		ExpressionType typeExpression = transpiler.getExpressionType(expression);
		if ((typeVariable == null) || (typeExpression == null))
			throw new TranspilerException("Transpiler Exception: Cannot determine expression types for assigment");
		if ((typeVariable instanceof ExpressionPrimitiveType) && (typeExpression instanceof ExpressionClassType) && (typeExpression.isPrimitiveOrBoxedPrimitive()))
			strExpression += ".valueOf()";
		return "%s = %s".formatted(strVariable, strExpression);
	}
	
	
	/**
	 * Transpiles the expression statement.
	 * 
	 * @param node     the expression statement
	 * 
	 * @return the transpiled expression if the statement was transpiled successfully and null otherwise
	 */
	public String convertExpressionStatement(ExpressionStatementTree node) {
		return convertExpression(node.getExpression()); 
	}
	
	
	
	/**
	 * Transpiles the throw expression.
	 * 
	 * @param node     the expression
	 * 
	 * @return the transpiled expression if the statement was transpiled successfully and null otherwise
	 */
	public String convertThrow(ThrowTree node) {
		return "throw %s".formatted(convertExpression(node.getExpression()));
	}
	
	
	/**
	 * Transpiles the statement.
	 * 
	 * @param node     the statement
	 * @param noIndent   true if no indentation should be used
	 * 
	 * @return the transpiled statement
	 */
	public String convertStatement(StatementTree node, boolean noIndent) {
		if ((node instanceof BlockTree block) && (!noIndent)) {
			indentC++;
			String blockConverted = convertBlock(block, false);
			indentC--;
			return "{%s%s%s}".formatted(System.lineSeparator(), blockConverted, getIndent());
		}
		indentC++;
		String tmpIndent = (noIndent ? "" : System.lineSeparator() + getIndent());
		indentC--;
		if (node instanceof ExpressionStatementTree es) {
			String expr = convertExpressionStatement(es);
			if (expr == null)
				return "";
			return tmpIndent + expr + ";";
		} 
		if (node instanceof ReturnTree ret)
			return tmpIndent + convertReturn(ret);
		if (node instanceof BreakTree brk)
			return tmpIndent + convertBreak(brk);
		if (node instanceof ThrowTree tt)
			return tmpIndent + convertThrow(tt);
		if (node instanceof ContinueTree cont)
			return tmpIndent + convertContinue(cont);
		if (node instanceof VariableTree variable)
			return tmpIndent + convertBlockVariable(variable);
		if (node instanceof IfTree ifTree) {
			indentC++;			
			String ifConverted = convertIf(ifTree);
			indentC--;			
			return tmpIndent + ifConverted;
		}
		if (node instanceof DoWhileLoopTree dwlTree) {
			indentC++;			
			String dwlConverted = convertDoWhileLoop(dwlTree);
			indentC--;			
			return tmpIndent + dwlConverted;
		}
		if (node instanceof WhileLoopTree wlTree) {
			indentC++;			
			String wlConverted = convertWhileLoop(wlTree);
			indentC--;			
			return tmpIndent + wlConverted;
		}
		if (node instanceof ForLoopTree flTree) {
			indentC++;			
			String flConverted = convertForLoop(flTree);
			indentC--;			
			return tmpIndent + flConverted;
		}
		if (node instanceof EnhancedForLoopTree eflTree) {
			indentC++;			
			String eflConverted = convertEnhancedForLoop(eflTree);
			indentC--;			
			return tmpIndent + eflConverted;
		}
		if (node instanceof TryTree ttTree) {
			indentC++;			
			String ttConverted = convertTry(ttTree);
			indentC--;			
			return tmpIndent + ttConverted;
		}
		if (node instanceof EmptyStatementTree est) 
			return ";";
		throw new TranspilerException("Transpiler Error: Statement node of kind " + node.getKind() + " not yet supported by the transpiler.");
	}
	
	
	/**
	 * Transpiles the if statement.
	 * 
	 * @param node     the if statement
	 * 
	 * @return the transpiled if statement 
	 */
	public String convertIf(IfTree node) {
		return "if %s %s%s".formatted(
			convertExpression(node.getCondition()),
			convertStatement(node.getThenStatement(), false),
			node.getElseStatement() == null ? "" : " else " + convertStatement(node.getElseStatement(), false)
		);
	}
	

	/**
	 * Transpiles the switch statement.
	 * 
	 * @param node     the switch statement
	 * @param noIndent   true if no indentation should be used
	 * 
	 * @return the transpiled switch statement 
	 */
	public String convertSwitch(SwitchTree node, boolean noIndent) {
		String result = "switch " + convertExpression(node.getExpression()) + " {" + System.lineSeparator();
		for (CaseTree curCase : node.getCases()) {
			CaseKind kind = curCase.getCaseKind();
			switch (kind) {
				case RULE -> throw new TranspilerException("Transpiler Error: Case of type " + kind + " currently not supported in switch statements."); // TODO implement					
				case STATEMENT -> {
					if (curCase.getExpressions().size() == 0)
						result += getIndent() + "\tdefault: ";
					else
						result += curCase.getExpressions().stream().map(exp -> "case " + convertExpression(exp)).collect(Collectors.joining(":" + System.lineSeparator() + getIndent() + "\t", getIndent() + "\t", ": "));
					indentC++;
					result += curCase.getStatements().stream().map(stmt -> {
						String tmp = convertStatement(stmt, false);
						return tmp;
					}).collect(Collectors.joining("", "", System.lineSeparator())); 
					indentC--;
				}
				default -> throw new TranspilerException("Transpiler Error: Case of type " + kind + " currently not supported in switch statements.");				
			}
		}
		result += (noIndent ? "" : getIndent()) + "}";
		return result;
	}


	/**
	 * Transpiles the return statement.
	 * 
	 * @param node     the return statement
	 *  
	 * @return the transpiled return statement 
	 */
	public String convertReturn(ReturnTree node) {
		if (node.getExpression() == null)
			return "return;";
		String converted = convertExpression(node.getExpression());
		ExpressionType type = transpiler.getExpressionType(node.getExpression());
		if ((type instanceof ExpressionClassType ect) && (ect.isPrimitiveOrBoxedPrimitive())) {
			Tree parent = transpiler.getMethod(node);
			if (parent instanceof MethodTree mt) {
				MethodNode method = MethodNode.methodNodes.get(mt);
				if (method == null)
					throw new TranspilerException("Transpiler Error: Unkown method while handling boxed return type.");
				TypeNode returnType = method.getReturnType();
				if (returnType == null)
					throw new TranspilerException("Transpiler Error: Method return type expected while handling boxed return type.");
				if (returnType.isPrimitive())
					converted += "!";
			} else if (parent instanceof LambdaExpressionTree let) {
				throw new TranspilerException("Transpiler Error: Handling boxed return types for lambda expressions not yet supported");
			}
		}
		return "return %s;".formatted(converted);
	}

	
	/**
	 * Transpiles the break statement.
	 * 
	 * @param node     the break statement
	 *  
	 * @return the transpiled break statement 
	 */
	public String convertBreak(BreakTree node) {
		return "break%s;".formatted(
			node.getLabel() == null ? "" : " " + node.getLabel()  
		);
	}
	
	
	/**
	 * Transpiles the continue statement.
	 * 
	 * @param node     the continue statement
	 * 
	 * @return the transpiled continue statement
	 */
	public String convertContinue(ContinueTree node) {
		return "continue%s;".formatted(
			node.getLabel() == null ? "" : " " + node.getLabel()  
		);
	}
	
	
	/**
	 * Transpiles the catch clause.
	 * 
	 * @param node     the catch clause
	 * 
	 * @return the transpiled catch clause
	 */
	public String convertCatch(CatchTree node) {
		VariableTree param = node.getParameter();
		if (param == null)
			throw new TranspilerException("Transpiler Error: Catch clause without a parameter variable is not supported.");
		String result = "catch(" + param.getName().toString() + ") {" + System.lineSeparator();
		indentC++;
		result += convertBlock(node.getBlock(), false);
		indentC--;
		result += getIndent() + "}";
		return result;
	}
	
	
	/**
	 * Transpiles the try statement.
	 * 
	 * @param node     the try statement
	 * 
	 * @return the transpiled try statement
	 */
	public String convertTry(TryTree node) {
		if ((node.getResources() != null) && (node.getResources().size() > 0))
			throw new TranspilerException("Transpiler Error: Try with resources currently not supported.");
		if ((node.getCatches() != null) && (node.getCatches().size() > 1))
			throw new TranspilerException("Transpiler Error: Try with multiple catch clauses currently not supported.");
		String result = "try {" + System.lineSeparator();
		indentC++;
		result += convertBlock(node.getBlock(), false);
		indentC--;
		result += getIndent() + "}";
		if (node.getCatches() != null)
			result += node.getCatches().stream().map(c -> convertCatch(c)).collect(Collectors.joining(" ", " ", ""));
		BlockTree finallyBlock = node.getFinallyBlock();
		if (finallyBlock != null) {
			result += " finally {" + System.lineSeparator();
			indentC++;
			result += convertBlock(finallyBlock, false);
			indentC--;
			result += getIndent() + "}";
		}
		return result;
	}
	
	
	/**
	 * Transpiles the statement block.
	 * 
	 * @param node          the statement block
	 * @param ignoreFirst   if set, the first statement is ignored
	 *                      (sometimes super constructor calls must be ignored)
	 * 
	 * @return the transpiled block
	 */
	public String convertBlock(BlockTree node, boolean ignoreFirst) {
		if (node == null)
			return null;
		List<? extends StatementTree> childs = node.getStatements();
		String result = "";
		for (int i = (ignoreFirst ? 1 : 0); i < childs.size(); i++) {
			StatementTree child = childs.get(i);
			String strChild = switch (child.getKind()) {
				case VARIABLE -> convertBlockVariable((VariableTree) child) + ";";
				case ENHANCED_FOR_LOOP -> convertEnhancedForLoop((EnhancedForLoopTree) child);
				case FOR_LOOP -> convertForLoop((ForLoopTree) child);
				case WHILE_LOOP -> convertWhileLoop((WhileLoopTree) child);
				case DO_WHILE_LOOP -> convertDoWhileLoop((DoWhileLoopTree)child);
				case EXPRESSION_STATEMENT -> {
					String converted = convertExpressionStatement((ExpressionStatementTree) child);
					if (converted != null)
						converted += ";";
					yield converted;
				}
				case IF -> convertIf((IfTree) child);
				case SWITCH -> convertSwitch((SwitchTree) child, false);
				case RETURN -> convertReturn((ReturnTree) child);
				case BREAK -> convertBreak((BreakTree) child);
				case CONTINUE -> convertContinue((ContinueTree) child);
				case TRY -> convertTry((TryTree) child);
				case THROW -> convertThrow((ThrowTree)child);
				case EMPTY_STATEMENT -> "";
				default -> throw new TranspilerException("Transpiler Error: Child of type " + child.getKind() + " currently not supported in statement blocks.");
			};
			if (strChild != null)
				result += getIndent() + strChild + System.lineSeparator();
		}
		return result;
	}
	

	/**
	 * Transpiles the list of method invocation parameters.
	 * 
	 * @param expressions   the list of method invocation parameters
	 * @param enumValueName   the name of the enum value as first method invocation parameter, if the method is the constructor of an enum value
	 * @param enumOrdinal     the ordinal as second method invocation parameter, if the method is the constructor of an enum value
	 * @param noParentheses   defines whether the parameters should be surrounded by parentheses
	 * 
	 * @return the transpiled method invocation parameters 
	 */
	public String convertMethodInvocationParameters(List<? extends ExpressionTree> expressions, String enumValueName, Integer enumOrdinal, boolean noParentheses) {
		if (expressions == null)
			return "";
		String enumInject = ((enumValueName != null) && (enumOrdinal != null)) ? "\"" + enumValueName + "\", " + enumOrdinal + ", " : "";
		String result = (noParentheses ? "" : "(") + enumInject;
		for (int i = 0; i < expressions.size(); i++) {
			ExpressionTree expr = expressions.get(i);
			if (i > 0)
				result += ", ";
			ExpressionType type = ExpressionType.getExpressionType(transpiler, expr);

			// check whether we need a spread operator 
			if ((i == expressions.size() - 1) && (type instanceof ExpressionArrayType eat)) {
				// get the parameter and its type 
				TranspilerUnit unit = transpiler.getTranspilerUnit(expr);
				Tree parent = transpiler.getParent(expr);
				if (parent instanceof MethodInvocationTree mit) {
					ExpressionTree exprTree = mit.getMethodSelect();
					if (exprTree instanceof MemberSelectTree mst) {
						ExecutableElement ee = unit.allInvokedMethods.get(mst);
						if (ee == null)
							throw new TranspilerException("Transpiler Error: Cannot determine method for method invocation");
						List<? extends VariableElement> params = ee.getParameters();
						if (expressions.size() != params.size())
							throw new TranspilerException("Transpiler Error: Number of parameters in invoked method is to small");
						if (ee.isVarArgs())
							result += "...";
					}
					if (exprTree instanceof IdentifierTree it) {
						Set<ExecutableElement> methods = unit.allLocalMethodElements.get(it.toString());
						if (methods == null)
							throw new TranspilerException("Transpiler Error: Cannot determine method for identifier");
						// TODO if methods.size() > 1 check for the method that has the best fitting parameter types
						for (ExecutableElement method : methods) {
							List<? extends VariableElement> methodParams = method.getParameters();
							if (methodParams == null)
								continue; // invalid method
							if (expressions.size() != methodParams.size())
								continue; // invalid number of parameters
							if (method.isVarArgs())
								result += "...";
						}
					}
				}
			}
			
			// do the conversion
			result += convertExpression(expr);
			
			// check parameter expression type and type of the methods parent member select for invoked method and add type conversion for wrapped types 
			if (type instanceof ExpressionClassType ect) {
				if (ect.isNumberType()) {
					// get the parameter and its type 
					TranspilerUnit unit = transpiler.getTranspilerUnit(expr);
					Tree parent = transpiler.getParent(expr);
					if (parent instanceof MethodInvocationTree mit) {
						ExpressionTree exprTree = mit.getMethodSelect();
						if (exprTree instanceof MemberSelectTree mst) {
							ExecutableElement ee = unit.allInvokedMethods.get(mst);
							if (ee == null)
								throw new TranspilerException("Transpiler Error: Cannot determine method for method invocation");
							List<? extends VariableElement> params = ee.getParameters();
							if (i >= params.size())
								throw new TranspilerException("Transpiler Error: Number of parameters in invoked method is to small");
							VariableElement param = params.get(i);
							// append ".valueOf()" if the parameter type requires a primitive type instead of the wrapper type 
							if (param.asType().getKind().isPrimitive())
								result += "!";
						} else if (exprTree instanceof IdentifierTree it) {
							Set<ExecutableElement> methods = unit.allLocalMethodElements.get(it.toString());
							if (methods == null)
								throw new TranspilerException("Transpiler Error: Cannot determine method for identifier");
							// TODO if methods.size() > 1 check for the method that has the best fitting parameter types
							for (ExecutableElement method : methods) {
								List<? extends VariableElement> methodParams = method.getParameters();
								if (methodParams == null)
									continue; // invalid method
								if (expressions.size() != methodParams.size())
									continue; // invalid number of parameters
								VariableElement param = methodParams.get(i);
								// append ".valueOf()" if the parameter type requires a primitive type instead of the wrapper type 
								if (param.asType().getKind().isPrimitive())
									result += "!";
							}
						}
					}
				}
			}
		}
		result += noParentheses ? "" : ")";
		return result;
	}


	/**
	 * Transpiles the new class expression.
	 * 
	 * @param node   the new class expression
	 * 
	 * @return the transpiled new class expression
	 */
	public String convertNewClass(NewClassTree node) {
		// special handling of String constructor calls
		if ("String".equals(node.getIdentifier().toString())) {
			List<? extends ExpressionTree> args = node.getArguments();
			if (args.size() == 0)
				return "\"\"";
			if (args.size() == 1) {
				ExpressionTree expression = args.get(0);
				if ((expression instanceof LiteralTree li) && (li.getKind() == Tree.Kind.STRING_LITERAL))
					return convertLiteral(li);
				if (expression instanceof IdentifierTree ident) {
					VariableTree variable = transpiler.getDeclaration(ident);
					if ((variable != null) && ("char[]".equals(variable.getType().toString())))
						return "" + ident.getName() + ".join(\"\")";
					// TODO handle all possible String constructors
				}
				// TODO handle all possible String constructors
			}
			// TODO handle all possible String constructors
			throw new TranspilerException("Unhandled String constructor.");
		}
		
		// print arguments for the constructor call
		TypeNode typeNode = new TypeNode(this, node.getIdentifier(), false, false);
		return "new " + typeNode.transpile(false) + convertMethodInvocationParameters(node.getArguments(), null, null, false);
	}
	
	
	/**
	 * Transpiles the method invocation expression.
	 * 
	 * @param node   the method invocation expression
	 * 
	 * @return the transpiled method invocation expression and null if the method invocation was 
	 *         skipped to prevent typescript errors
	 */
	public String convertMethodInvocation(MethodInvocationTree node) {
		// print the expression to identify the method
		String result = "";
		ExpressionTree methodExpression = node.getMethodSelect();
		if (methodExpression instanceof IdentifierTree ident) {
			// add super method calls in classes without a specified super class 
			if ("super".equals(ident.getName().toString())) {
				ExpressionType et = transpiler.getExpressionType(ident);
				if ((et == null) || (et instanceof ExpressionTypeNone))
					return "super()";
			}
			result = convertIdentifier(ident);
		} else if (methodExpression instanceof MemberSelectTree ms) {
			ExpressionType type = transpiler.getExpressionType(ms.getExpression());
			// replace all hashCode invocations
			if ("hashCode".equals(ms.getIdentifier().toString())) {
				String expression = convertExpression(ms.getExpression());
				return "JavaObject.getTranspilerHashCode(" + expression + ")";
			}
			// replace all equals invocations
			if ("equals".equals(ms.getIdentifier().toString())) {
				String expression = convertExpression(ms.getExpression());
				return "JavaObject.equalsTranspiler(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, false) + ")";
			}
			// TODO replace Long, Integer, Short, Byte, Float and Double methods...
			if (type.isNumberType()) {
				String identifier = ms.getIdentifier().toString();
				// replace compare invocations
				if ("compare".equals(identifier)) {
					String expression = type.toString();
					return "Java" + expression + "." + identifier + convertMethodInvocationParameters(node.getArguments(), null, null, false);
				}
				//if ("intValue".equals(identifier) && (type instanceof ExpressionClassType ect)) {
				if (("byteValue".equals(identifier) || "shortValue".equals(identifier) || "intValue".equals(identifier) || "longValue".equals(identifier)) && (type instanceof ExpressionClassType ect)) {
					String expression = convertExpression(ms.getExpression());
					if ("java.lang.Double".equals(ect.getFullQualifiedName()) || ("java.lang.Float".equals(ect.getFullQualifiedName())))
						return "Math.trunc(" + expression + "!)";
					return expression + "!";
				}
			}
			// TODO replace String methods...
			if (type.isString()) {
				String expression = convertExpression(ms.getExpression());
				if ("contains".equals(ms.getIdentifier().toString()))
					return "JavaString.contains(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
				if ("indexOf".equals(ms.getIdentifier().toString()))
					return "JavaString.indexOf(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
				if ("compareTo".equals(ms.getIdentifier().toString()))
					return "JavaString.compareTo(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
				if ("compareToIgnoreCase".equals(ms.getIdentifier().toString()))
					return "JavaString.compareToIgnoreCase(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
				if ("equalsIgnoreCase".equals(ms.getIdentifier().toString()))
					return "JavaString.equalsIgnoreCase(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
				if ("replaceAll".equals(ms.getIdentifier().toString()))
					return "JavaString.replaceAll(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
				if ("replaceFirst".equals(ms.getIdentifier().toString()))
					return "JavaString.replaceFirst(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
				if ("length".equals(ms.getIdentifier().toString()))
					return expression + ".length"; // in typescript it is not a method...
			}
			// TODO replace reflective Array commands
			if ((type instanceof ExpressionClassType classType) && ("java.lang.reflect.Array".equals(classType.getFullQualifiedName()))) {
				if ("newInstance".equals(ms.getIdentifier().toString())) {
					List<? extends ExpressionTree> params = node.getArguments();
					if ((params == null) || (params.size() < 2))
						throw new TranspilerException("TranspilerError: Invalid number of parameters for Array.newInstance");
					ExpressionTree param = params.get(1);
					ExpressionType paramType = transpiler.getExpressionType(param);
					if ((paramType instanceof ExpressionPrimitiveType ept) && (ept.isNumberType()))
						return "Array(" + this.convertExpression(param) + ").fill(null)";
					if ((paramType instanceof ExpressionClassType ect) && (ect.isNumberType()))
						return "Array(" + this.convertExpression(param) + ".valueOf()).fill(null)";
					if (paramType instanceof ExpressionArrayType eat)
						throw new TranspilerException("Transpiler Error: Array.newInstance not yet supported for multidimensional arrays");
					throw new TranspilerException("Transpiler Error: Array.newInstance with unsupported parameter types");
				}
			}
			// TODO replace System.out and System.err commands
			if ((type instanceof ExpressionClassType classType) && ("java.io.PrintStream".equals(classType.getFullQualifiedName()))) {
				String expression = convertExpression(ms.getExpression());
				if (expression.equals("System.out")) {
					if ("flush".equals(ms.getIdentifier().toString()))
						return null;
					if ("print".equals(ms.getIdentifier().toString()) || "println".equals(ms.getIdentifier().toString())) {
						if (node.getArguments().size() > 0)
							return "console.log(JSON.stringify" + convertMethodInvocationParameters(node.getArguments(), null, null, false) + ")";
						return "console.log" + convertMethodInvocationParameters(node.getArguments(), null, null, false);
					}
				} else if (expression.equals("System.err")) {
					if ("flush".equals(ms.getIdentifier().toString()))
						return null;
					if ("print".equals(ms.getIdentifier().toString()) || "println".equals(ms.getIdentifier().toString())) {
						if (node.getArguments().size() > 0)
							return "console.error(JSON.stringify" + convertMethodInvocationParameters(node.getArguments(), null, null, false) + ")";
						return "console.error" + convertMethodInvocationParameters(node.getArguments(), null, null, false);
					}
				}
			}
			result = convertMemberSelect(ms);
		} else {
			throw new TranspilerException("Transpiler Error: Unhandled method expression type of kind " + methodExpression.getKind() + ".");
		}

		// check whether the result type is String an the parent tree node requires a valueOf() call in TypeScript 
		String valueOf = "";
		ExpressionType type = transpiler.getExpressionType(node);
		if ((type instanceof ExpressionClassType ect) && ("java.lang.String".equals(ect.getFullQualifiedName()))) {
			Tree parent = transpiler.getParent(node);
			if ((parent instanceof BinaryTree bt) && (bt.getKind() == Kind.PLUS))
				valueOf = "!";
		}
		
		// print arguments for the method call
		return result + convertMethodInvocationParameters(node.getArguments(), null, null, false) + valueOf;
	}
	
	
	/**
	 * Transpiles the attribute of a class.
	 * 
	 * @param node     the attribute to be transpiled
	 * @param enumName   specifies the name of the enum if the attribute belongs to an enum
	 * 
	 * @return the transpiled attribute 
	 */
	public String convertAttribute(VariableTree node, String enumName) {
		// get comment
		String comment = formatComment(this.getTranspiler().getComment(node));
		
		// get the typescript attribute initialization code
		String initializer = "";
		boolean forceNotNull = false; 
		if (node.getInitializer() != null) {
			initializer = " = ";
			if ((enumName != null) && (node.getInitializer() instanceof NewClassTree) && (((NewClassTree)node.getInitializer()).getIdentifier()).toString().equals(enumName)) {
				forceNotNull = true; // for attribute generation below...
				NewClassTree newClassTree = ((NewClassTree)node.getInitializer());
				// print new operator and class identifier
				TypeNode typeNode = new TypeNode(this, newClassTree.getIdentifier(), false, false);
				initializer += "new " + typeNode.transpile(false);
				// print arguments for the constructor call
				Integer enumOrdinal = enumOrdinals.get(enumName);
				if (enumOrdinal == null)
					enumOrdinal = 0;
				else
					enumOrdinal++;
				enumOrdinals.put(enumName, enumOrdinal);
				initializer += convertMethodInvocationParameters(newClassTree.getArguments(), "" + node.getName(), enumOrdinal, false);
			} else {
				initializer += convertExpression(node.getInitializer());
			}
		} else if (!transpiler.hasFinalModifier(node)) {
			// fill in default values to avoid typescript warnings
			if (node.getType() instanceof PrimitiveTypeTree ptt) {
				initializer = " = " + switch (ptt.getPrimitiveTypeKind()) {
					case BOOLEAN -> "false";
					case BYTE, SHORT, INT, LONG, FLOAT, DOUBLE -> "0";
					case CHAR -> "''";
					default -> throw new TranspilerException("Transpiler Error: " + ptt.getPrimitiveTypeKind() + " is not supported as a primitive type for attributes.");
				};
			} else {
				if (!transpiler.hasNotNullAnnotation(node))
					initializer = " = null";
			}
		}
		
		// get the accessmodifier  
		String accessModifier = transpiler.getAccessModifier(node);
		
		// Prüfe ob der Bezeichner in Typescript zulässig ist
		pruefeBezeichner(node.getName().toString());
		
		// convert to typescrypt code
		TypeNode typeNode = new TypeNode(this, node.getType(), true, forceNotNull ? true : transpiler.hasNotNullAnnotation(node));
		return comment + getIndent() + "%s%s%s%s : %s%s;%s".formatted(
			"".equals(accessModifier) ? "" : accessModifier + " ",
			transpiler.hasStaticModifier(node) ? "static " : "",
			transpiler.hasFinalModifier(node) ? "readonly " : "",
			node.getName(),  // the attribute name
			// the attribute type, for enums it is generated with a not null annonation
			typeNode.transpile(false),
			initializer,
			System.lineSeparator()
		);
	}

	
	/**
	 * Determines the cast function name for the specified class tree node
	 * 	
	 * @param node   the class tree node
	 * 
	 * @return the name of the cast function fpr the specified class tree object
	 */
	public String getCastFunctionName(ClassTree node) {
		return "cast_" + this.transpiler.getCompilationUnit(node).getPackageName().toString().replace('.', '_') + "_" + node.getSimpleName().toString();
	}
	
	/**
	 * Appends a function to cast an object to the class type. The function
	 * is appended after the class definition. 
	 *  
	 * @param sb       the StringBuilder where the output is written to
	 * @param node     the class to be transpiled
	 */
	public void appendCast(StringBuilder sb, ClassTree node) {
		sb.append(System.lineSeparator());	
		sb.append("%s%s%s%s%s%s%s%s".formatted(
			getIndent(),
			"export function ",
			getCastFunctionName(node),
			convertTypeParameters(node.getTypeParameters(), true),
			"(obj : unknown) : ",
			node.getSimpleName(),
			convertTypeParameters(node.getTypeParameters(), false),
			" {"
		));
		sb.append(System.lineSeparator());
		indentC++;
		sb.append("%s%s%s%s%s".formatted(
				getIndent(),
				"return obj as ",
				node.getSimpleName(),
				convertTypeParameters(node.getTypeParameters(), true),
				";"
			));
		sb.append(System.lineSeparator());	
		indentC--;
		sb.append(getIndent());
		sb.append("}");
		sb.append(System.lineSeparator());
	}
	

	/**
	 * Appends a method to the transpiled class code for simulating the Java instanceof
	 * operator. This is required to support instanceof checks on interfaces that
	 * are not available in typescript since the underlying javascript does not
	 * support interfaces.
	 *  
	 * @param node   the class tree node
	 * 
	 * @return the isTranspiledInstanceOf method code as a String
	 */
	public String appendIsTranspiledInstanceOf(ClassTree node) {
		String result = getIndent() + "isTranspiledInstanceOf(name : string): boolean {" + System.lineSeparator();
		indentC++;
		TranspilerUnit unit = transpiler.getTranspilerUnit(node);
		String strInstanceOfTypes = unit.superTypes.stream().collect(Collectors.joining("', '", "'", "'"));
		result += getIndent() + "return [" + strInstanceOfTypes + "].includes(name);" + System.lineSeparator();		
		indentC--;
		result += getIndent() + "}" + System.lineSeparator();
		return result; 
	}
	
	
	/**
	 * Appends a method for creating a TS object from JSON to the transpiled class 
	 * code. 
	 * 
	 * @param node   the class tree node
	 * 
	 * @return the transpilerFromJSON method code as a String
	 */
	public String appendTranspilerFromJSON(ClassTree node) {
		String result = getIndent() + "public static transpilerFromJSON(json : string): " + node.getSimpleName().toString() + " {" + System.lineSeparator();
		indentC++;
		result += getIndent() + "const obj = JSON.parse(json);" + System.lineSeparator();
		result += getIndent() + "const result = new " + node.getSimpleName().toString() + "();" + System.lineSeparator();
		for (VariableTree attribute : Transpiler.getAttributes(node)) {
			VariableNode variable = new VariableNode(this, attribute);
			if (variable.isStatic()) // ignore static members
				continue;
			TypeNode type = variable.getTypeNode();
			if (type.isPrimitive() || (type.isNotNull() && (type.isString() || type.isNumberClass() || type.isBoolean()))) {
				result += getIndent() + "if (typeof obj." + attribute.getName() + " === \"undefined\")" + System.lineSeparator();
				result += getIndent() + "\t throw new Error('invalid json format, missing attribute " + attribute.getName() + "');" + System.lineSeparator();
				result += getIndent() + "result." + attribute.getName() + " = obj." + attribute.getName() + ";" + System.lineSeparator();
			} else if ((!type.isNotNull()) && (type.isString() || type.isNumberClass() || type.isBoolean())) {
				String tmpAttribute = "obj." + attribute.getName();
				if (type.isString() || type.isNumberClass() || type.isBoolean())
					tmpAttribute = "" + tmpAttribute + " === null ? null : " + tmpAttribute;
				result += getIndent() + "result." + attribute.getName() + " = typeof obj." + attribute.getName() + " === \"undefined\" ? null : " + tmpAttribute + ";" + System.lineSeparator();
			} else if (type.isCollectionType()) {
				// TODO notNull, Collection initialisiert
				TypeNode paramType = type.getParameterType(0, false);
				if (paramType == null)
					throw new TranspilerException("Transpiler Error: Cannot determine type parameter for the collection type " + type.transpile(false) + ".");
				result += getIndent() + "if (!((obj." + attribute.getName() + " === undefined) || (obj." + attribute.getName() + " === null))) {" + System.lineSeparator();
				indentC++;
				result += getIndent() + "for (const elem of obj." + attribute.getName() + ") {" + System.lineSeparator();
				indentC++;
				if (paramType.isNotNull() && (paramType.isString() || paramType.isNumberClass() || paramType.isBoolean()))
					result += getIndent() + "result." + attribute.getName() + "?.add(elem);" + System.lineSeparator();
				else if (paramType.isString() || paramType.isNumberClass() || paramType.isBoolean())
					result += getIndent() + "result." + attribute.getName() + "?.add(elem === null ? null : elem);" + System.lineSeparator();
				else 
					result += getIndent() + "result." + attribute.getName() + "?.add(" + paramType.transpile(true) + ".transpilerFromJSON(JSON.stringify(elem)));" + System.lineSeparator();
				indentC--;
				result += getIndent() + "}" + System.lineSeparator();
				indentC--;
				result += getIndent() + "}" + System.lineSeparator();
			} else if (type.isArrayType()) {
				TypeNode contentType = type.getArrayContentType(transpiler);
				if (contentType == null)
					throw new TranspilerException("Transpiler Error: Cannot determine array content type of " + type.transpile(false) + " for JSON deserialization.");
				contentType = contentType.getNoDeclarationType();
				result += getIndent() + "for (let i = 0; i < obj." + attribute.getName() + ".length; i++) {" + System.lineSeparator();
				indentC++;
				if (contentType.isPrimitive()) {
					result += getIndent() + "result." + attribute.getName() + "[i] = obj." + attribute.getName() + "[i];" + System.lineSeparator();
				} else if (contentType.isNotNull()) {
					String tmpAttribute = "obj." + attribute.getName() + "[i]";
					if (contentType.isString() || contentType.isNumberClass() || contentType.isBoolean())
						result += getIndent() + "result." + attribute.getName() + "[i] = " + tmpAttribute + ";" + System.lineSeparator();
					else 
						result += getIndent() + "result." + attribute.getName() + "[i] = (" + contentType.transpile(true) + ".transpilerFromJSON(JSON.stringify(" + tmpAttribute + ")));" + System.lineSeparator();
				} else {
					String tmpAttribute = "obj." + attribute.getName() + "[i]";
					if (contentType.isString() || contentType.isNumberClass() || contentType.isBoolean())
						result += getIndent() + "result." + attribute.getName() + "[i] = " + tmpAttribute + " === null ? null : " + tmpAttribute + ";" + System.lineSeparator();
					else 
						result += getIndent() + "result." + attribute.getName() + "[i] = " + tmpAttribute + " == null ? null : (" + contentType.transpile(true) + ".transpilerFromJSON(JSON.stringify(" + tmpAttribute + ")));" + System.lineSeparator();
				}
				indentC--;
				result += getIndent() + "}" + System.lineSeparator();
			} else {
				if (type.isNotNull()) {
					result += getIndent() + "if (typeof obj." + attribute.getName() + " === \"undefined\")" + System.lineSeparator();
					result += getIndent() + "\t throw new Error('invalid json format, missing attribute " + attribute.getName() + "');" + System.lineSeparator();
					result += getIndent() + "result." + attribute.getName() + " = " + type.transpile(true) + ".transpilerFromJSON(JSON.stringify(obj." + attribute.getName() + "));" + System.lineSeparator();
				} else {
					result += getIndent() + "result." + attribute.getName() + " = ((typeof obj." + attribute.getName() + " === \"undefined\") || (obj." + attribute.getName() + " === null)) ? null : " + type.getNoDeclarationType().transpile(true) + ".transpilerFromJSON(JSON.stringify(obj." + attribute.getName() + "));" + System.lineSeparator();
				}
			}
		}
		result += getIndent() + "return result;" + System.lineSeparator();
		indentC--;
		result += getIndent() + "}" + System.lineSeparator();
		return result + System.lineSeparator();		
	}


	/**
	 * Appends a method for creating JSON from the TS object to the 
	 * transpiled class 
	 * 
	 * @param node   the class tree node
	 * 
	 * @return the transpilerToJSON method code as a String
	 */
	public String appendTranspilerToJSON(ClassTree node) {
		String result = getIndent() + "public static transpilerToJSON(obj : " + node.getSimpleName().toString() + ") : string {" + System.lineSeparator();
		indentC++;
		result += getIndent() + "let result = '{';" + System.lineSeparator();
		List<VariableTree> attributes = Transpiler.getAttributes(node);
		for (int i = 0; i < attributes.size(); i++) {
			VariableTree attribute = attributes.get(i);
			String endline = " + ',';" + System.lineSeparator(); 
			VariableNode variable = new VariableNode(this, attribute);
			if (variable.isStatic()) // ignore static members
				continue;
			TypeNode type = variable.getTypeNode();
			String addAttrName = "result += '\"" + attribute.getName() + "\" : '";
			String objAttr = "obj." + attribute.getName(); 
			if (type.isPrimitive()) {
				if (type.isPrimitveBoolean()) {
					result += getIndent() + addAttrName + " + " + objAttr + endline;
				} else if (type.isPrimitveChar()) {
					result += getIndent() + addAttrName + " + '\"' + " + objAttr + " + '\"'" + endline;
				} else if (type.isPrimitveInteger()) {
					result += getIndent() + addAttrName + " + " + objAttr + endline;
				} else if (type.isPrimitveFloat()) {
					result += getIndent() + addAttrName + " + " + objAttr + endline;
				} else {
					throw new TranspilerException("Transpiler Error: Unsupported primitive type while generating transpilerToJSON method");
				}
			} else if (type.isString()) {
				if (type.isNotNull()) {
					result += getIndent() + addAttrName + " + '\"' + " + objAttr + "! + '\"'" + endline;
				} else { 
					result += getIndent() + addAttrName + " + ((!" + objAttr + ") ? 'null' : '\"' + " + objAttr + " + '\"')" + endline;
				}
			} else if (type.isNumberClass() || type.isBoolean()) {
				if (type.isNotNull()) {
					result += getIndent() + addAttrName + " + " + objAttr + "!" + endline;
				} else { 
					result += getIndent() + addAttrName + " + ((!" + objAttr + ") ? 'null' : " + objAttr + ")" + endline;
				}
			} else if (type.isCollectionType()) {
				// TODO notNull, Collection initialisiert
				TypeNode paramType = type.getParameterType(0, false);
				if (paramType == null)
					throw new TranspilerException("Transpiler Error: Cannot determine type parameter for the collection type " + type.transpile(false) + ".");
				result += getIndent() + "if (!obj." + attribute.getName() + ") {" + System.lineSeparator();
				indentC++;
				result += getIndent() + "result += '\"" + attribute.getName() + "\" : []';" + System.lineSeparator();
				indentC--;
				result += getIndent() + "} else {" + System.lineSeparator();
				indentC++;
				result += getIndent() + "result += '\"" + attribute.getName() + "\" : [ ';" + System.lineSeparator();
				result += getIndent() + "for (let i = 0; i < " + objAttr + ".size(); i++) {" + System.lineSeparator();
				indentC++;
				result += getIndent() + "const elem = " + objAttr + ".get(i);" + System.lineSeparator();
				if (paramType.isString()) {
					if (paramType.isNotNull())
						result += getIndent() + "result += " + "'\"' + elem + '\"';" + System.lineSeparator();
					else
						result += getIndent() + "result += " + "(elem == null) ? null : '\"' + elem + '\"';" + System.lineSeparator();
				} else if (paramType.isNumberClass() || paramType.isBoolean()) {
					if (paramType.isNotNull())
						result += getIndent() + "result += elem;" + System.lineSeparator();
					else
						result += getIndent() + "result += (elem == null) ? null : elem;" + System.lineSeparator();
				} else
					result += getIndent() + "result += " + paramType.transpile(true) + ".transpilerToJSON(elem);" + System.lineSeparator();
				result += getIndent() + "if (i < " + objAttr + ".size() - 1)" + System.lineSeparator();
				indentC++;
				result += getIndent() + "result += ',';" + System.lineSeparator();
				indentC--;
				indentC--;
				result += getIndent() + "}" + System.lineSeparator();
				result += getIndent() + "result += ' ]'" + endline;
				indentC--;
				result += getIndent() + "}" + System.lineSeparator();
			} else if (type.isArrayType()) {
				TypeNode contentType = type.getArrayContentType(transpiler);
				if (contentType == null)
					throw new TranspilerException("Transpiler Error: Cannot determine array content type of " + type.transpile(false) + " for JSON deserialization.");
				contentType = contentType.getNoDeclarationType();

				result += getIndent() + "if (!obj." + attribute.getName() + ") {" + System.lineSeparator();
				indentC++;
				result += getIndent() + "result += '\"" + attribute.getName() + "\" : []';" + System.lineSeparator();
				indentC--;
				result += getIndent() + "} else {" + System.lineSeparator();
				indentC++;
				result += getIndent() + "result += '\"" + attribute.getName() + "\" : [ ';" + System.lineSeparator();
				result += getIndent() + "for (let i = 0; i < " + objAttr + ".length; i++) {" + System.lineSeparator();
				indentC++;
				result += getIndent() + "const elem = " + objAttr + "[i];" + System.lineSeparator();
				if (contentType.isString()) {
					if (contentType.isNotNull())
						result += getIndent() + "result += " + "'\"' + elem + '\"';" + System.lineSeparator();
					else
						result += getIndent() + "result += " + "(elem == null) ? null : '\"' + elem + '\"';" + System.lineSeparator();
				} else if (contentType.isNumberClass() || contentType.isBoolean()) {
					if (contentType.isNotNull())
						result += getIndent() + "result += elem;" + System.lineSeparator();
					else
						result += getIndent() + "result += (elem == null) ? null : elem;" + System.lineSeparator();
				} else if (contentType.isPrimitive()) {
					result += getIndent() + "result += JSON.stringify(elem);" + System.lineSeparator();
				} else {
					if (contentType.isNotNull())
						result += getIndent() + "result += " + contentType.transpile(true) + ".transpilerToJSON(elem);" + System.lineSeparator();
					else
						result += getIndent() + "result += (elem == null) ? null : " + contentType.transpile(true) + ".transpilerToJSON(elem);" + System.lineSeparator();
				}
				result += getIndent() + "if (i < " + objAttr + ".length - 1)" + System.lineSeparator();
				indentC++;
				result += getIndent() + "result += ',';" + System.lineSeparator();
				indentC--;
				indentC--;
				result += getIndent() + "}" + System.lineSeparator();
				result += getIndent() + "result += ' ]'" + endline;
				indentC--;
				result += getIndent() + "}" + System.lineSeparator();
			} else {
				if (type.isNotNull()) {
					result += getIndent() + addAttrName + " + " + type.transpile(true) + ".transpilerToJSON(" + objAttr + ")" + endline;
				} else { 
					result += getIndent() + addAttrName + " + ((!" + objAttr + ") ? 'null' : " + type.getNoDeclarationType().transpile(true) + ".transpilerToJSON(" + objAttr + "))" + endline;
				}
			}
		}
		result += getIndent() + "result = result.slice(0, -1);" + System.lineSeparator();
		result += getIndent() + "result += '}';" + System.lineSeparator();
		result += getIndent() + "return result;" + System.lineSeparator();
		indentC--;
		result += getIndent() + "}" + System.lineSeparator();
		return result + System.lineSeparator();		
	}


	/**
	 * Appends a method for creating a JSON Patch from the TS object to the 
	 * transpiled class 
	 * 
	 * @param node   the class tree node
	 * 
	 * @return the transpilerToJSON method code as a String
	 */
	public String appendTranspilerToJSONPatch(ClassTree node) {
		String result = getIndent() + "public static transpilerToJSONPatch(obj : Partial<" + node.getSimpleName().toString() + ">) : string {" + System.lineSeparator();
		indentC++;
		result += getIndent() + "let result = '{';" + System.lineSeparator();
		List<VariableTree> attributes = Transpiler.getAttributes(node);
		for (int i = 0; i < attributes.size(); i++) {
			VariableTree attribute = attributes.get(i);
			String endline = " + ',';" + System.lineSeparator(); 
			VariableNode variable = new VariableNode(this, attribute);
			if (variable.isStatic()) // ignore static members
				continue;
			TypeNode type = variable.getTypeNode();
			String addAttrName = "result += '\"" + attribute.getName() + "\" : '";
			String objAttr = "obj." + attribute.getName();
			result += getIndent() + "if (typeof " + objAttr + " !== \"undefined\") {" + System.lineSeparator();
			indentC++;
			if (type.isPrimitive()) {
				if (type.isPrimitveBoolean()) {
					result += getIndent() + addAttrName + " + " + objAttr + endline;
				} else if (type.isPrimitveChar()) {
					result += getIndent() + addAttrName + " + '\"' + " + objAttr + " + '\"'" + endline;
				} else if (type.isPrimitveInteger()) {
					result += getIndent() + addAttrName + " + " + objAttr + endline;
				} else if (type.isPrimitveFloat()) {
					result += getIndent() + addAttrName + " + " + objAttr + endline;
				} else {
					throw new TranspilerException("Transpiler Error: Unsupported primitive type while generating transpilerToJSON method");
				}
			} else if (type.isString()) {
				if (type.isNotNull()) {
					result += getIndent() + addAttrName + " + '\"' + " + objAttr + " + '\"'" + endline;
				} else { 
					result += getIndent() + addAttrName + " + ((!" + objAttr + ") ? 'null' : '\"' + " + objAttr + " + '\"')" + endline;
				}
			} else if (type.isNumberClass() || type.isBoolean()) {
				if (type.isNotNull()) {
					result += getIndent() + addAttrName + " + " + objAttr + endline;
				} else { 
					result += getIndent() + addAttrName + " + ((!" + objAttr + ") ? 'null' : " + objAttr + ")" + endline;
				}
			} else if (type.isCollectionType()) {
				// TODO notNull, Collection initialisiert
				TypeNode paramType = type.getParameterType(0, false);
				if (paramType == null)
					throw new TranspilerException("Transpiler Error: Cannot determine type parameter for the collection type " + type.transpile(false) + ".");
				result += getIndent() + "if (!obj." + attribute.getName() + ") {" + System.lineSeparator();
				indentC++;
				result += getIndent() + "result += '\"" + attribute.getName() + "\" : []';" + System.lineSeparator();
				indentC--;
				result += getIndent() + "} else {" + System.lineSeparator();
				indentC++;
				result += getIndent() + "result += '\"" + attribute.getName() + "\" : [ ';" + System.lineSeparator();
				result += getIndent() + "for (let i = 0; i < " + objAttr + ".size(); i++) {" + System.lineSeparator();
				indentC++;
				result += getIndent() + "const elem = " + objAttr + ".get(i);" + System.lineSeparator();
				if (paramType.isString()) {
					if (paramType.isNotNull())
						result += getIndent() + "result += " + "'\"' + elem + '\"';" + System.lineSeparator();
					else
						result += getIndent() + "result += " + "(elem == null) ? null : '\"' + elem + '\"';" + System.lineSeparator();
				} else if (paramType.isNumberClass() || paramType.isBoolean()) {
					if (paramType.isNotNull())
						result += getIndent() + "result += elem;" + System.lineSeparator();
					else
						result += getIndent() + "result += (elem == null) ? null : elem;" + System.lineSeparator();
				} else
					result += getIndent() + "result += " + paramType.transpile(true) + ".transpilerToJSON(elem);" + System.lineSeparator();
				result += getIndent() + "if (i < " + objAttr + ".size() - 1)" + System.lineSeparator();
				indentC++;
				result += getIndent() + "result += ',';" + System.lineSeparator();
				indentC--;
				indentC--;
				result += getIndent() + "}" + System.lineSeparator();
				result += getIndent() + "result += ' ]'" + endline;
				indentC--;
				result += getIndent() + "}" + System.lineSeparator();
			} else if (type.isArrayType()) {
				TypeNode contentType = type.getArrayContentType(transpiler);
				if (contentType == null)
					throw new TranspilerException("Transpiler Error: Cannot determine array content type of " + type.transpile(false) + " for JSON deserialization.");
				contentType = contentType.getNoDeclarationType();
				result += getIndent() + "const a = " + objAttr + ";" + System.lineSeparator();
				result += getIndent() + "if (!a) {" + System.lineSeparator();
				indentC++;
				result += getIndent() + "result += '\"" + attribute.getName() + "\" : []';" + System.lineSeparator();
				indentC--;
				result += getIndent() + "} else {" + System.lineSeparator();
				indentC++;
				result += getIndent() + "result += '\"" + attribute.getName() + "\" : [ ';" + System.lineSeparator();
				result += getIndent() + "for (let i = 0; i < a.length; i++) {" + System.lineSeparator();
				indentC++;
				result += getIndent() + "const elem = a[i];" + System.lineSeparator();
				if (contentType.isString()) {
					if (contentType.isNotNull())
						result += getIndent() + "result += " + "'\"' + elem + '\"';" + System.lineSeparator();
					else
						result += getIndent() + "result += " + "(elem == null) ? null : '\"' + elem + '\"';" + System.lineSeparator();
				} else if (contentType.isNumberClass() || contentType.isBoolean()) {
					if (contentType.isNotNull())
						result += getIndent() + "result += elem;" + System.lineSeparator();
					else
						result += getIndent() + "result += (elem == null) ? null : elem;" + System.lineSeparator();
				} else if (contentType.isPrimitive()) {
					result += getIndent() + "result += JSON.stringify(elem);" + System.lineSeparator();
				} else {
					if (contentType.isNotNull())
						result += getIndent() + "result += " + contentType.transpile(true) + ".transpilerToJSON(elem);" + System.lineSeparator();
					else
						result += getIndent() + "result += (elem == null) ? null : " + contentType.transpile(true) + ".transpilerToJSON(elem);" + System.lineSeparator();
				}
				result += getIndent() + "if (i < a.length - 1)" + System.lineSeparator();
				indentC++;
				result += getIndent() + "result += ',';" + System.lineSeparator();
				indentC--;
				indentC--;
				result += getIndent() + "}" + System.lineSeparator();
				result += getIndent() + "result += ' ]'" + endline;
				indentC--;
				result += getIndent() + "}" + System.lineSeparator();
			} else {
				if (type.isNotNull()) {
					result += getIndent() + addAttrName + " + " + type.transpile(true) + ".transpilerToJSON(" + objAttr + ")" + endline;
				} else { 
					result += getIndent() + addAttrName + " + ((!" + objAttr + ") ? 'null' : " + type.getNoDeclarationType().transpile(true) + ".transpilerToJSON(" + objAttr + "))" + endline;
				}
			}
			indentC--;
			result += getIndent() + "}" + System.lineSeparator();
		}
		result += getIndent() + "result = result.slice(0, -1);" + System.lineSeparator();
		result += getIndent() + "result += '}';" + System.lineSeparator();
		result += getIndent() + "return result;" + System.lineSeparator();
		indentC--;
		result += getIndent() + "}" + System.lineSeparator();
		return result + System.lineSeparator();		
	}


	/**
	 * Transpiles the class.
	 * 
	 * @param sb       the StringBuilder where the output is written to
	 * @param node     the class to be transpiled
	 */
	public void transpileClass(StringBuilder sb, ClassTree node) {
		// TODO class comment
		sb.append(getIndent());
		sb.append("export ");
		if (transpiler.hasAbstractModifier(node))
			sb.append("abstract ");
		sb.append("class ");
		sb.append(node.getSimpleName());
		sb.append(convertTypeParameters(node.getTypeParameters(), true));
		sb.append(" extends ");
		if (node.getExtendsClause() == null)
			sb.append("JavaObject");
		else {
			TypeNode typeNode = new TypeNode(this, node.getExtendsClause(), false, false);
			sb.append(typeNode.transpile(false));
		}
		List<? extends Tree> implClause = node.getImplementsClause();
		if (implClause.size() > 0) {
			sb.append(" implements ");
			for (int i = 0; i < implClause.size(); i++) {
				TypeNode typeNode = new TypeNode(this, implClause.get(i), false, false);
				sb.append(typeNode.transpile(false));
				if (i < implClause.size() - 1)
					sb.append(", ");
			}
		}
		sb.append(" {");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		indentC++;

		// Generate Attributes
		for (VariableTree attribute : Transpiler.getAttributes(node)) {
			sb.append(convertAttribute(attribute, null));
			sb.append(System.lineSeparator());
		}
		sb.append(System.lineSeparator());
		
		// Generate Constructors
		List<MethodNode> constructors = Transpiler.getConstructors(node).stream()
			.map(method -> new MethodNode(this, node, method, getIndent()))
			.collect(Collectors.toList());
		if (constructors.size() == 1) {
			constructors.get(0).print(sb, "" + node.getSimpleName());
			sb.append(System.lineSeparator());
		} else {
			MethodNode.setCommonAccessModifier(constructors);
			for (MethodNode constructor : constructors) {
				constructor.printHead(sb);
				sb.append(System.lineSeparator());
			}
			// TODO handle super constructor calls, especially those with different parameters... is that transpilable ???
			MethodNode.printImplementation(sb, getIndent(), constructors, "" + node.getSimpleName());
			sb.append(System.lineSeparator());
		}
		
		// Generate Methods
		List<MethodNode> methods = Transpiler.getMethods(node).stream()
			.map(method -> new MethodNode(this, node, method, getIndent()))
			.collect(Collectors.toList());
		Map<String, List<MethodNode>> mapMethods = methods.stream().collect(Collectors.groupingBy(method -> method.getName()));
		for (MethodNode method : methods) {
			String methodName = method.getName();
			List<MethodNode> methodList = mapMethods.get(methodName);
			if (methodList == null)
				continue;
			if (mapMethods.get(methodName).size() == 1) {
				method.print(sb, "" + node.getSimpleName());
				sb.append(System.lineSeparator());
			} else {
				MethodNode.setCommonAccessModifier(methodList);
				mapMethods.remove(methodName);
				for (MethodNode m : methodList) {
					m.printHead(sb);
					sb.append(System.lineSeparator());
				}
				MethodNode.printImplementation(sb, getIndent(), methodList, "" + node.getSimpleName());
				sb.append(System.lineSeparator());
			}
		}

		sb.append(appendIsTranspiledInstanceOf(node));
		sb.append(System.lineSeparator());
		
		if (transpiler.hasTranspilerDTOAnnotation(node)) {
			sb.append(appendTranspilerFromJSON(node));
			sb.append(appendTranspilerToJSON(node));
			sb.append(appendTranspilerToJSONPatch(node));
		}
		
		TranspilerUnit unit = transpiler.getTranspilerUnit(node);
		if ((unit.superTypes.contains("java.lang.Iterable")) && (!unit.superTypes.contains("java.util.AbstractCollection") && (!unit.superTypes.contains("java.util.AbstractList")))) {
			// TODO Get the type parameter that was passed through inheritance to the Iterable<...> interface
			TypeMirror type = unit.getIterableTypeArgument();
			if (type == null)
				throw new TranspilerException("Transpiler Error: cannot determine iterable type");
			String typeParam = "T";
			if (type instanceof TypeVariable tv) {
				typeParam = tv.asElement().getSimpleName() + ((type.getAnnotation(NotNull.class) == null) ? "" : " | null");
			} else if (type instanceof DeclaredType dt) {
				if ("java.util.Map.Entry".equals(dt.asElement().toString()))
					typeParam = "JavaMapEntry<any, any>";
				else
					throw new TranspilerException("Transpiler Error: Iterable types of Kind " + type.getKind() + " not yet fully supported");
			} else {
				throw new TranspilerException("Transpiler Error: Iterable types of Kind " + type.getKind() + " not yet supported");
			}
			sb.append(getIndent()).append("public [Symbol.iterator](): Iterator<").append(typeParam).append("> {").append(System.lineSeparator());
			sb.append(getIndent()).append("\tlet iter : JavaIterator<").append(typeParam).append("> = this.iterator();").append(System.lineSeparator());
			sb.append(getIndent()).append("\tconst result : Iterator<").append(typeParam).append("> = {").append(System.lineSeparator());
			sb.append(getIndent()).append("\t\tnext() : IteratorResult<").append(typeParam).append("> {").append(System.lineSeparator());
			sb.append(getIndent()).append("\t\t\tif (iter.hasNext())").append(System.lineSeparator());
			sb.append(getIndent()).append("\t\t\t\treturn { value : iter.next(), done : false };").append(System.lineSeparator());
			sb.append(getIndent()).append("\t\t\treturn { value : null, done : true };").append(System.lineSeparator());
			sb.append(getIndent()).append("\t\t}").append(System.lineSeparator());
			sb.append(getIndent()).append("\t};").append(System.lineSeparator());
			sb.append(getIndent()).append("\treturn result;").append(System.lineSeparator());
			sb.append(getIndent()).append("}").append(System.lineSeparator());
			sb.append(System.lineSeparator());
		}
		
		indentC--;
		sb.append(getIndent());
		sb.append("}");
		sb.append(System.lineSeparator());
		appendCast(sb, node);
	}
	

	/**
	 * Transpiles the enumeration.
	 * 
	 * @param sb       the StringBuilder where the output is written to
	 * @param node     the enumeration class to be transpiled
	 */
	public void transpileEnum(StringBuilder sb, ClassTree node) {
		// TODO class comment
		sb.append(getIndent());
		sb.append("export ");
		sb.append("class ");
		sb.append(node.getSimpleName());
		sb.append(" extends JavaObject {");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());

		// Generate Attributes
		indentC++;
		sb.append(getIndent()).append("/** the name of the enumeration value */").append(System.lineSeparator());
		sb.append(getIndent()).append("private readonly __name : string;").append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append(getIndent()).append("/** the ordinal value for the enumeration value */").append(System.lineSeparator());
		sb.append(getIndent()).append("private readonly __ordinal : number;").append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append(getIndent()).append("/** an array containing all values of this enumeration */").append(System.lineSeparator());
		sb.append(getIndent()).append("private static readonly all_values_by_ordinal : Array<").append(node.getSimpleName()).append("> = [];").append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append(getIndent()).append("/** an array containing all values of this enumeration indexed by their name*/").append(System.lineSeparator());
		sb.append(getIndent()).append("private static readonly all_values_by_name : Map<string, ").append(node.getSimpleName()).append("> = new Map<string, ").append(node.getSimpleName()).append(">();").append(System.lineSeparator());
		sb.append(System.lineSeparator());
		
		for (VariableTree attribute : Transpiler.getAttributes(node)) {
			sb.append(convertAttribute(attribute, "" + node.getSimpleName()));
			sb.append(System.lineSeparator());
		}
		
		// Generate Constructors
		List<MethodNode> constructors = Transpiler.getConstructors(node).stream()
				.map(method -> new MethodNode(this, node, method, getIndent()))
				.peek(method -> method.setAccessModifier("private"))   // ensure that the constructors are private 
				.collect(Collectors.toList());
		if (constructors.size() == 1) {
			constructors.get(0).print(sb, "" + node.getSimpleName());
			sb.append(System.lineSeparator());
		} else {
			for (MethodNode constructor : constructors) {
				constructor.printHead(sb);
				sb.append(System.lineSeparator());
			}
			MethodNode.printImplementation(sb, getIndent(), constructors, "" + node.getSimpleName());
			sb.append(System.lineSeparator());
		}
		
		// Generate Methods
		List<MethodNode> methods = Transpiler.getMethods(node).stream()
				.map(method -> new MethodNode(this, node, method, getIndent()))
				.collect(Collectors.toList());
		Map<String, List<MethodNode>> mapMethods = methods.stream().collect(Collectors.groupingBy(method -> method.getName()));
		boolean hasToString = false;
		for (MethodNode method : methods) {
			String methodName = method.getName();
			if ("toString".equals(methodName))
				hasToString = true;
			List<MethodNode> methodList = mapMethods.get(methodName);
			if (methodList == null)
				continue;
			if (mapMethods.get(methodName).size() == 1) {
				method.print(sb, "" + node.getSimpleName());
				sb.append(System.lineSeparator());
			} else {
				mapMethods.remove(methodName);
				for (MethodNode m : methodList) {
					m.printHead(sb);
					sb.append(System.lineSeparator());
				}
				MethodNode.printImplementation(sb, getIndent(), methodList, "" + node.getSimpleName());
				sb.append(System.lineSeparator());
			}
		}
		
		sb.append(getIndent()).append("/**").append(System.lineSeparator());
		sb.append(getIndent()).append(" * Returns the name of this enumeration value.").append(System.lineSeparator());
		sb.append(getIndent()).append(" *").append(System.lineSeparator());
		sb.append(getIndent()).append(" * @returns the name").append(System.lineSeparator());
		sb.append(getIndent()).append(" */").append(System.lineSeparator());
		sb.append(getIndent()).append("private name() : string {").append(System.lineSeparator());
		sb.append(getIndent()).append("\treturn this.__name;").append(System.lineSeparator());
		sb.append(getIndent()).append("}").append(System.lineSeparator());
		sb.append(System.lineSeparator());

		sb.append(getIndent()).append("/**").append(System.lineSeparator());
		sb.append(getIndent()).append(" * Returns the ordinal value of this enumeration value.").append(System.lineSeparator());
		sb.append(getIndent()).append(" *").append(System.lineSeparator());
		sb.append(getIndent()).append(" * @returns the ordinal value").append(System.lineSeparator());
		sb.append(getIndent()).append(" */").append(System.lineSeparator());
		sb.append(getIndent()).append("private ordinal() : number {").append(System.lineSeparator());
		sb.append(getIndent()).append("\treturn this.__ordinal;").append(System.lineSeparator());
		sb.append(getIndent()).append("}").append(System.lineSeparator());
		sb.append(System.lineSeparator());

		if (!hasToString) {
			sb.append(getIndent()).append("/**").append(System.lineSeparator());
			sb.append(getIndent()).append(" * Returns the name of this enumeration value.").append(System.lineSeparator());
			sb.append(getIndent()).append(" *").append(System.lineSeparator());
			sb.append(getIndent()).append(" * @returns the name").append(System.lineSeparator());
			sb.append(getIndent()).append(" */").append(System.lineSeparator());
			sb.append(getIndent()).append("public toString() : string {").append(System.lineSeparator());
			sb.append(getIndent()).append("\treturn this.__name;").append(System.lineSeparator());
			sb.append(getIndent()).append("}").append(System.lineSeparator());
			sb.append(System.lineSeparator());			
		}

		sb.append(getIndent()).append("/**").append(System.lineSeparator());
		sb.append(getIndent()).append(" * Returns true if this and the other enumeration values are equal.").append(System.lineSeparator());
		sb.append(getIndent()).append(" *").append(System.lineSeparator());
		sb.append(getIndent()).append(" * @param other   the other enumeration value").append(System.lineSeparator());
		sb.append(getIndent()).append(" *").append(System.lineSeparator());
		sb.append(getIndent()).append(" * @returns true if they are equal and false otherwise").append(System.lineSeparator());
		sb.append(getIndent()).append(" */").append(System.lineSeparator());
		sb.append(getIndent()).append("public equals(other : JavaObject) : boolean {").append(System.lineSeparator());
		sb.append(getIndent()).append("\tif (!(other instanceof ").append(node.getSimpleName()).append("))").append(System.lineSeparator());
		sb.append(getIndent()).append("\t\treturn false;").append(System.lineSeparator());
		sb.append(getIndent()).append("\treturn this === other;").append(System.lineSeparator());
		sb.append(getIndent()).append("}").append(System.lineSeparator());
		sb.append(System.lineSeparator());

		sb.append(getIndent()).append("/**").append(System.lineSeparator());
		sb.append(getIndent()).append(" * Returns the ordinal value as hashcode, since the ordinal value is unique.").append(System.lineSeparator());
		sb.append(getIndent()).append(" *").append(System.lineSeparator());
		sb.append(getIndent()).append(" * @returns the ordinal value as hashcode").append(System.lineSeparator());
		sb.append(getIndent()).append(" */").append(System.lineSeparator());
		sb.append(getIndent()).append("public hashCode() : number {").append(System.lineSeparator());
		sb.append(getIndent()).append("\treturn this.__ordinal;").append(System.lineSeparator());
		sb.append(getIndent()).append("}").append(System.lineSeparator());
		sb.append(System.lineSeparator());
		
		sb.append(getIndent()).append("/**").append(System.lineSeparator());
		sb.append(getIndent()).append(" * Compares this enumeration value with the other enumeration value by their ordinal value.").append(System.lineSeparator());
		sb.append(getIndent()).append(" *").append(System.lineSeparator());
		sb.append(getIndent()).append(" * @param other   the other enumeration value").append(System.lineSeparator());
		sb.append(getIndent()).append(" *").append(System.lineSeparator());
		sb.append(getIndent()).append(" * @returns a negative, zero or postive value as this enumeration value is less than, equal to").append(System.lineSeparator());
		sb.append(getIndent()).append(" *          or greater than the other enumeration value").append(System.lineSeparator());
		sb.append(getIndent()).append(" */").append(System.lineSeparator());
		sb.append(getIndent()).append("public compareTo(other : ").append(node.getSimpleName()).append(") : number {").append(System.lineSeparator());
		sb.append(getIndent()).append("\treturn this.__ordinal - other.__ordinal;").append(System.lineSeparator());
		sb.append(getIndent()).append("}").append(System.lineSeparator());
		sb.append(System.lineSeparator());

		sb.append(getIndent()).append("/**").append(System.lineSeparator());
		sb.append(getIndent()).append(" * Returns an array with enumeration values.").append(System.lineSeparator());
		sb.append(getIndent()).append(" *").append(System.lineSeparator());
		sb.append(getIndent()).append(" * @returns the array with enumeration values").append(System.lineSeparator());
		sb.append(getIndent()).append(" */").append(System.lineSeparator());
		sb.append(getIndent()).append("public static values() : Array<").append(node.getSimpleName()).append("> {").append(System.lineSeparator());
		sb.append(getIndent()).append("\treturn [...this.all_values_by_ordinal];").append(System.lineSeparator());
		sb.append(getIndent()).append("}").append(System.lineSeparator());
		sb.append(System.lineSeparator());

		sb.append(getIndent()).append("/**").append(System.lineSeparator());
		sb.append(getIndent()).append(" * Returns the enumeration value with the specified name.").append(System.lineSeparator());
		sb.append(getIndent()).append(" *").append(System.lineSeparator());
		sb.append(getIndent()).append(" * @param name   the name of the enumeration value").append(System.lineSeparator());
		sb.append(getIndent()).append(" *").append(System.lineSeparator());
		sb.append(getIndent()).append(" * @returns the enumeration values or null").append(System.lineSeparator());
		sb.append(getIndent()).append(" */").append(System.lineSeparator());
		sb.append(getIndent()).append("public static valueOf(name : string) : ").append(node.getSimpleName()).append(" | null {").append(System.lineSeparator());
		sb.append(getIndent()).append("\tlet tmp : ").append(node.getSimpleName()).append(" | undefined = this.all_values_by_name.get(name);").append(System.lineSeparator());
		sb.append(getIndent()).append("\treturn (!tmp) ? null : tmp;").append(System.lineSeparator());
		sb.append(getIndent()).append("}").append(System.lineSeparator());
		sb.append(System.lineSeparator());
		
// TODO Typescript code for Iterable (see transpileClass - public [Symbol.iterator](): Iterator ...)
		
		sb.append(appendIsTranspiledInstanceOf(node));
		sb.append(System.lineSeparator());
		
		indentC--;
		sb.append(getIndent());
		sb.append("}");
		sb.append(System.lineSeparator());
		appendCast(sb, node);
	}
	
	
	/**
	 * Bestimmt anhand der Namen der Klasse und des Packages den 
	 * Klassennamen für das Schreiben des transpilierten Codes.
	 * Hierbei finden Umbenennungen statt, falls in Typescript
	 * identische Bezeichner vorkommen.
	 *  
	 * @param className     der Name der Java-Klasse
	 * @param packageName   der Name des Java-Packages
	 * 
	 * @return der Typescript-Klassenname 
	 */
	public static String getImportName(String className, String packageName) {
		return switch (packageName) {
			case "java.lang" -> switch (className) {
				case "Object" -> "JavaObject";
				case "Boolean" -> "JavaBoolean";
				case "Byte" -> "JavaByte";
				case "Short" -> "JavaShort";
				case "Integer" -> "JavaInteger";
				case "Long" -> "JavaLong";
				case "Float" -> "JavaFloat";
				case "Double" -> "JavaDouble";
				default -> className;
			};
			case "java.util" -> switch (className) {
				case "Map" -> "JavaMap"; 
				case "Map.Entry" -> "JavaMapEntry"; 
				case "Set" -> "JavaSet";
				case "Iterator" -> "JavaIterator";
				default -> className;
			};
			case "java.util.Map" -> switch (className) {
				case "Entry" -> "JavaMapEntry"; 
				default -> className;
			};
			default -> className;
		};
	}
	
	/**
	 * Bestimmt anhand der Namen der Klasse und des Packages den 
	 * Packagenamen für das Schreiben des transpilierten Codes.
	 * Hierbei finden ggf. Umbenennungen statt.
	 *  
	 * @param className     der Name der Java-Klasse
	 * @param packageName   der Name des Java-Packages
	 * 
	 * @return der Typescript-Packagename 
	 */
	public static String getImportPackageName(String className, String packageName) {
		return switch (packageName) {
			case "java.lang" -> switch (className) {
				case "Object" -> "java.lang"; 
				default -> packageName;
			};
			case "java.util" -> switch (className) {
				case "Map" -> "java.util"; 
				case "Map.Entry" -> "java.util"; 
				case "Set" -> "java.util"; 
				case "Iterator" -> "java.util"; 
				default -> packageName;
			};
			case "java.util.Map" -> switch (className) {
				case "Entry" -> "java.util"; 
				default -> packageName;
			};
			default -> packageName;
		};
	}
	
	/**
	 * Prints the import information of the transpiler unit
	 * 
	 * @param unit   the transpiler unit containing the import information
	 * @param strIgnoreJavaPackagePrefix   the package prefix to be ignored
	 * 
	 * @return the imports for the transpiled typescript class
	 */
	public static String getImports(TranspilerUnit unit, String strIgnoreJavaPackagePrefix) {
		String packageName = unit.getPackageName();
		String shortPackageName = packageName.replace(strIgnoreJavaPackagePrefix + ".", "");
		String importPathPrefix = "../".repeat((int)(shortPackageName.chars().filter(c -> c == '.').count()) + 1);
		List<Map.Entry<String, String>> entries = unit.imports.entrySet().stream().collect(Collectors.toList());
		if (!unit.imports.containsKey("Object"))
			entries.add(0, new AbstractMap.SimpleEntry<>("Object", "java.lang"));
		String result = "";
		for (Map.Entry<String, String> entry : entries) {
			String key = entry.getKey();
			String value = entry.getValue();
			String importCast = "cast_" + value.replace('.', '_') +  "_" + key.replace('.', '_');
			switch (value + "." + key) {
				case "java.lang.String", "java.lang.Long", "java.lang.Integer", "java.lang.Short", "java.lang.Byte", "java.lang.Float", "java.lang.Double", "java.lang.Boolean" -> {
					result += "import { Java%s, %s } from '%s';".formatted(key, importCast, importPathPrefix + "java/lang/Java" + key);
					result += System.lineSeparator();
				}
				case "java.lang.reflect.Array" -> { /**/ }
				case "java.lang.Character" -> { /**/ }
				case "java.lang.Math" -> { /**/ }
				case "java.io.PrintStream" -> { /**/ }
				default -> {
					String importName = getImportName(key, value);
					String importPackage = getImportPackageName(key, value).replace(strIgnoreJavaPackagePrefix + ".", "");
					String importPath = importPathPrefix + importPackage.replace('.', '/') + "/";
					result += "import { %s, %s } from '%s';".formatted(importName, importCast, importPath + importName);
					result += System.lineSeparator();
				}
			}
		}
		result += System.lineSeparator();
		return result;
	}
	

	@Override
	public void transpile() {
		// generate code for all classes
		System.out.println("Running TypeScript-Transpiler...");
		List<ClassTree> allClasses = transpiler.getAllClasses();
		for (ClassTree classTree : allClasses) {
			String fileName = transpiler.getFullClassName(classTree).replace(strIgnoreJavaPackagePrefix + ".", "").replace('.', '/') + ".ts";
			System.out.println("  -> " + fileName);
			Path path = Paths.get(outputDir + "/" + fileName);
			super.outputFiles.add(fileName);
			try {
				Files.createDirectories(path.getParent());
				StringBuilder sb = new StringBuilder();
				sb.append(getImports(transpiler.getTranspilerUnit(classTree), strIgnoreJavaPackagePrefix));
				if (classTree.getKind() == Tree.Kind.CLASS) {
					transpileClass(sb, classTree);					
				} else if (classTree.getKind() == Tree.Kind.ENUM) {
					transpileEnum(sb, classTree);
				}
				Files.writeString(path, sb.toString(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
			} catch (@SuppressWarnings("unused") IOException e) {
				throw new TranspilerException("Transpiler Error: Cannot write output file " + path.toString());
			}
		}
		
		// write typescript resources
		System.out.println("Writing prepared TypeScript resources...");
		for (TranspilerResource res : tsResources) {
			// remove package prefix "typescript." from package path and replace all dots by slashes
			String fileName = res.packageName.substring(11).replace(".", "/") + "/" + res.className + res.extension;
			System.out.println("  -> " + fileName);
			Path path = Paths.get(outputDir + "/" + fileName);
			super.outputFiles.add(fileName);
			try {
				Files.createDirectories(path.getParent());
				Files.writeString(path, res.data, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
			} catch (@SuppressWarnings("unused") IOException e) {
				throw new TranspilerException("Transpiler Error: Cannot write output file " + path.toString());
			}
		}
	}

	// TODO special handling of String class
	// TODO special handling of System class
	// TODO special handling of Arrays class

}
