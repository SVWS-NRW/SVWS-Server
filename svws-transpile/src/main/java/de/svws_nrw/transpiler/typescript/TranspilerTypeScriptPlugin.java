package de.svws_nrw.transpiler.typescript;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BindingPatternTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.CaseLabelTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.CaseTree.CaseKind;
import com.sun.source.tree.CatchTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.ConstantCaseLabelTree;
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.DefaultCaseLabelTree;
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
import com.sun.source.tree.PatternCaseLabelTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.SwitchExpressionTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;

import de.svws_nrw.transpiler.ExpressionArrayType;
import de.svws_nrw.transpiler.ExpressionClassType;
import de.svws_nrw.transpiler.ExpressionPrimitiveType;
import de.svws_nrw.transpiler.ExpressionType;
import de.svws_nrw.transpiler.ExpressionTypeLambda;
import de.svws_nrw.transpiler.ExpressionTypeNone;
import de.svws_nrw.transpiler.ResourceUtils;
import de.svws_nrw.transpiler.Transpiler;
import de.svws_nrw.transpiler.TranspilerException;
import de.svws_nrw.transpiler.TranspilerLanguagePlugin;
import de.svws_nrw.transpiler.TranspilerResource;
import de.svws_nrw.transpiler.TranspilerUnit;

import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.YieldTree;

import jakarta.validation.constraints.NotNull;


/**
 * This is the transpiler language plugin for Type Script.
 */
public final class TranspilerTypeScriptPlugin extends TranspilerLanguagePlugin {

	/** The output directory where all generated files should be placed */
	private final String outputDir;

	/** A prefix for the java packages that will be removed while generating the output */
	private String strIgnoreJavaPackagePrefix = "";

	/** A list of resources containing typescript class as a prepared replacement for java classes */
	private final List<TranspilerResource> tsResources;

	/** A mapping from the class name of an enumeration to the current enumeration ordinal value */
	private final HashMap<String, Integer> enumOrdinals = new HashMap<>();

	/** the indentation state used during transpilation */
	public int indentC = 0;

	/** Ein Set mit allen zusätzlichen TS-Keywords, welche nicht im Java-Code als Bezeichner verwendet werden dürfen. */
	private static final Set<String> tsReservedKeywords = new HashSet<>(Arrays.asList("in", "of", "debugger", "export", "function", "typeOf", "var", "with"));

	private static final String strString = "String";
	private static final String strLong = "Long";
	private static final String strInteger = "Integer";
	private static final String strShort = "Short";
	private static final String strByte = "Byte";
	private static final String strFloat = "Float";
	private static final String strDouble = "Double";
	private static final String strBoolean = "Boolean";
	private static final String strCharacter = "Character";
	private static final String strObject = "Object";
	private static final String strIterator = "Iterator";
	private static final String strMap = "Map";
	private static final String strEntry = "Entry";
	private static final String strSet = "Set";
	private static final String strFunction = "Function";

	private static final String strJavaMapFQ = "java.util.Map";

	private static final String strCompare = "compare";
	private static final String strMIN_VALUE = "MIN_VALUE";
	private static final String strMAX_VALUE = "MAX_VALUE";
	private static final String strSIZE = "SIZE";
	private static final String strBYTES = "BYTES";

	private static final String strSuper = "super";

	private static final String strTsString = "string";
	private static final String strTsNumber = "number";
	private static final String strTsLet = "let";
	private static final String strTsConst = "const";
	private static final String strTsValueOf = ".valueOf()";


	/**
	 * Returns the transpiled comment
	 *
	 * @param comment   the original comment, not yet transpiled
	 *
	 * @return the transpiled comment
	 */
	private String formatComment(final String comment) {
        return (comment == null)
        		? ""
        		: getIndent() + "/**" + System.lineSeparator()
                + Arrays.asList(comment.split("\\r?\\n")).stream().map(s -> (getIndent() + " * " + s.stripTrailing()).stripTrailing()).collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator()
                + getIndent() + " */" + System.lineSeparator();
	}


	/**
	 * Create a Type Script transpiler language plugin
	 *
	 * @param transpiler   the transpiler that uses this plugin
	 * @param outputDir    the output directory where all generated files should be placed
	 */
	public TranspilerTypeScriptPlugin(final Transpiler transpiler, final String outputDir) {
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
	public void setIgnoreJavaPackagePrefix(final String prefix) {
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
	public static void pruefeBezeichner(final String bezeichner) {
		if (tsReservedKeywords.contains(bezeichner))
			throw new TranspilerException("Das Typescript-Schlüsselwort " + bezeichner + " ist als Variablenname nicht zulässig");
	}

	/**
	 * Transpiles the type parameter node.
	 *
	 * @param node         the type parameter node to be transpiled
	 * @param withBounds   specifies whether the bounds of the type parametes should be transpiled or not
	 *
	 * @return the type parameter string
	 */
	public String convertTypeParameter(final TypeParameterTree node, final boolean withBounds) {
		final StringBuilder sb = new StringBuilder(node.getName().toString());
		if (withBounds) {
			final TranspilerUnit unit = transpiler.getTranspilerUnit(node);
			final boolean hasNotNullAnnotation = Transpiler.hasNotNullAnnotation(node.getAnnotations(), unit);
			if ((node.getBounds() != null) && (!node.getBounds().isEmpty())) {
				sb.append(" extends ");
				boolean first = true;
				for (final Tree type : node.getBounds()) {
					if (!first)
						sb.append(" & ");
					first = false;
					boolean boundHasNotNullAnnotation = false;
					if ((type instanceof final AnnotatedTypeTree att))
						boundHasNotNullAnnotation = Transpiler.hasNotNullAnnotation(att.getAnnotations(), unit);
					final TypeNode typeNode = new TypeNode(this, type, false, boundHasNotNullAnnotation);
					sb.append(typeNode.transpile(false));
				}
			}
			sb.append((hasNotNullAnnotation ? "" : " | null"));
		}
		return sb.toString();
	}


	/**
	 * Transpiles the type parameters nodes.
	 *
	 * @param nodes        the type parameter nodes to be transpiled
	 * @param withBounds   specifies whether the bounds of the type parametes should be transpiled or not
	 *
	 * @return the type parameters string
	 */
	public String convertTypeParameters(final List<? extends TypeParameterTree> nodes, final boolean withBounds) {
		if ((nodes == null) || nodes.isEmpty())
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
	public String convertBlockVariable(final VariableTree node) {
		// Prüfe Bezeichner auf Schlüsselwörter
		final boolean isFinal = transpiler.hasFinalModifier(node);
		pruefeBezeichner(node.getName().toString());
		final TypeNode typeNode = new TypeNode(this, node.getType(), true, transpiler.hasNotNullAnnotation(node));
		final ExpressionTree initializer = node.getInitializer();
		if (initializer == null)
			return "%s %s : %s".formatted(isFinal ? strTsConst : strTsLet, node.getName(),  typeNode.transpile(false));
		String strInitializer = convertExpression(initializer);
		final ExpressionType typeInitializer = transpiler.getExpressionType(initializer);
		if ((node.getType().getKind() == Kind.PRIMITIVE_TYPE) && (typeInitializer instanceof ExpressionClassType) && (typeInitializer.isPrimitiveOrBoxedPrimitive()))
			strInitializer += strTsValueOf;
		return "%s %s : %s = %s".formatted(isFinal ? strTsConst : strTsLet, node.getName(),  typeNode.transpile(false), strInitializer);
	}


	/**
	 * Transpiles the for each loop.
	 *
	 * @param node     the enhanced for loop statement
	 *
	 * @return the transpiled for each loop
	 */
	public String convertEnhancedForLoop(final EnhancedForLoopTree node) {
		final boolean isFinal = transpiler.hasFinalModifier(node.getVariable());
		String result = "for (%s %s of %s)".formatted(
			isFinal ? strTsConst : strTsLet,
			node.getVariable().getName(),
			convertExpression(node.getExpression())
		);
		if (node.getStatement() instanceof BlockTree)
			result += " ";
		result += convertStatement(node.getStatement(), false);
		return result;
	}


	/**
	 * Transpiles the for loop.
	 *
	 * @param node     the for loop statement
	 *
	 * @return the transpiled for loop
	 */
	public String convertForLoop(final ForLoopTree node) {
		String result = "for (%s; %s; %s)".formatted(
			"let " + node.getInitializer().stream().map(st -> convertStatement(st, true)).collect(Collectors.joining(", ")).replace("let ", "").replace("const ", ""),
			convertExpression(node.getCondition()),
			node.getUpdate().stream().map(this::convertExpressionStatement).collect(Collectors.joining(", "))
		);
		if (node.getStatement() instanceof BlockTree)
			result += " ";
		result += convertStatement(node.getStatement(), false);
		return result;
	}


	/**
	 * Transpiles the while loop.
	 *
	 * @param node     the while loop statement
	 *
	 * @return the transpiled while loop
	 */
	public String convertWhileLoop(final WhileLoopTree node) {
		String result = "while %s".formatted(convertExpression(node.getCondition()));
		if (node.getStatement() instanceof BlockTree)
			result += " ";
		result += convertStatement(node.getStatement(), false);
		return result;
	}


	/**
	 * Transpiles the do while loop.
	 *
	 * @param node     the do while loop statement
	 *
	 * @return the transpiled do while loop
	 */
	public String convertDoWhileLoop(final DoWhileLoopTree node) {
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
	public String convertIdentifier(final IdentifierTree node) {
		final ExpressionType type = transpiler.getExpressionType(node);
		if (type == null)
			throw new TranspilerException("Transpiler Error: Cannot retrieve the type information for the identifier " + node.getName().toString());
		final String result = switch (node.getName().toString()) {
			case strString -> strTsString;
			case "Enum" -> "JavaEnum";
			case strLong, strInteger, strShort, strByte, strFloat, strDouble -> strTsNumber;
			case strBoolean -> "boolean";
			case strCharacter -> strTsString;
			case strObject -> {
				if (!((type instanceof final ExpressionClassType classType) && (strObject.equals(classType.toString()))))
					yield null;
				if (transpiler.getParent(node) instanceof NewClassTree)
					yield strObject;
				yield "unknown";
			}
			case strIterator ->
				((type instanceof final ExpressionClassType classType) && (strIterator.equals(classType.toString()))) ? getImportName(classType.toString(), classType.getPackageName()) : null;
			case strMap ->
				((type instanceof final ExpressionClassType classType) && (strMap.equals(classType.toString()))) ? getImportName(classType.toString(), classType.getPackageName()) : null;
			case strEntry ->
				((type instanceof final ExpressionClassType classType) && (strEntry.equals(classType.toString())) && (strJavaMapFQ.equals(classType.getPackageName()))) ? getImportName(classType.toString(), classType.getPackageName()) : null;
			case strSet ->
				((type instanceof final ExpressionClassType classType) && (strSet.equals(classType.toString()))) ? getImportName(classType.toString(), classType.getPackageName()) : null;
			case strFunction ->
				((type instanceof final ExpressionClassType classType) && (strFunction.equals(classType.toString()))) ? getImportName(classType.toString(), classType.getPackageName()) : null;
			default -> null;
		};
		if (result != null)
			return result;
		// check whether a String or a Number expression needs a .valueOf call in typescript
		String valueOf = "";
		if ((type instanceof final ExpressionClassType classType) && ((classType.isNumberType()) || (classType.isString()))) {
			final Tree parent = transpiler.getParent(node);
			if ((parent instanceof final BinaryTree bt) && ((bt.getKind() == Kind.MULTIPLY) || (bt.getKind() == Kind.DIVIDE)
			    || (bt.getKind() == Kind.REMAINDER) || (bt.getKind() == Kind.PLUS) || (bt.getKind() == Kind.MINUS)
			    || (bt.getKind() == Kind.LEFT_SHIFT) || (bt.getKind() == Kind.RIGHT_SHIFT) || (bt.getKind() == Kind.UNSIGNED_RIGHT_SHIFT)))
				valueOf = "!";
		}
		// check whether we have a case identifier of a switch statement where we have to add the class/enumeration name
		if ((type instanceof final ExpressionClassType classType) && ((transpiler.getParent(node) instanceof CaseTree) || (transpiler.getParent(node) instanceof ConstantCaseLabelTree))) {
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
	public String convertMemberSelect(final MemberSelectTree node) {
		final String result = switch (node.getExpression().toString()) {
			case strBoolean -> switch (node.getIdentifier().toString()) {
				case "parseBoolean" -> "JavaBoolean.parseBoolean";
				default -> null;
			};
			case strByte -> switch (node.getIdentifier().toString()) {
				case strCompare -> "JavaByte.compare";
				case "parseByte" -> "JavaByte.parseByte";
				case strMIN_VALUE -> "JavaByte.MIN_VALUE";
				case strMAX_VALUE -> "JavaByte.MAX_VALUE";
				case strSIZE -> "JavaByte.SIZE";
				case strBYTES -> "JavaByte.BYTES";
				default -> null;
			};
			case strShort -> switch (node.getIdentifier().toString()) {
				case strCompare -> "JavaShort.compare";
				case "parseShort" -> "JavaShort.parseShort";
				case strMIN_VALUE -> "JavaShort.MIN_VALUE";
				case strMAX_VALUE -> "JavaShort.MAX_VALUE";
				case strSIZE -> "JavaShort.SIZE";
				case strBYTES -> "JavaShort.BYTES";
				default -> null;
			};
			case strInteger -> switch (node.getIdentifier().toString()) {
				case strCompare -> "JavaInteger.compare";
				case "parseInt" -> "JavaInteger.parseInt";
				case strMIN_VALUE -> "JavaInteger.MIN_VALUE";
				case strMAX_VALUE -> "JavaInteger.MAX_VALUE";
				case strSIZE -> "JavaInteger.SIZE";
				case strBYTES -> "JavaInteger.BYTES";
				default -> null;
			};
			case strLong -> switch (node.getIdentifier().toString()) {
				case strCompare -> "JavaLong.compare";
				case "parseLong" -> "JavaLong.parseLong";
				case strMIN_VALUE -> "JavaLong.MIN_VALUE";
				case strMAX_VALUE -> "JavaLong.MAX_VALUE";
				case strSIZE -> "JavaLong.SIZE";
				case strBYTES -> "JavaLong.BYTES";
				default -> null;
			};
			case strFloat -> switch (node.getIdentifier().toString()) {
				case strCompare -> "JavaFloat.compare";
				case "parseFloat" -> "JavaFloat.parseFloat";
				case "NaN" -> "NaN";
				case strMIN_VALUE -> "JavaFloat.MIN_VALUE";
				case strMAX_VALUE -> "JavaFloat.MAX_VALUE";
				case strSIZE -> "JavaFloat.SIZE";
				case strBYTES -> "JavaFloat.BYTES";
				default -> null;
			};
			case strDouble -> switch (node.getIdentifier().toString()) {
				case strCompare -> "JavaDouble.compare";
				case "parseDouble" -> "JavaDouble.parseDouble";
				case "NaN" -> "NaN";
				case strMIN_VALUE -> "JavaDouble.MIN_VALUE";
				case strMAX_VALUE -> "JavaDouble.MAX_VALUE";
				case strSIZE -> "JavaDouble.SIZE";
				case strBYTES -> "JavaDouble.BYTES";
				default -> null;
			};
			default -> null;
		};
		if (result != null)
			return result;
		final String expression = convertExpression(node.getExpression());
		final String identifier = node.getIdentifier().toString();
		return expression + "." + identifier;
	}


	/**
	 * Transpiles the parenthesized node.
	 *
	 * @param node   the parenthesized node
	 *
	 * @return the transpiled parenthesized statement
	 */
	public String convertParenthesized(final ParenthesizedTree node) {
		return "(%s)".formatted(convertExpression(node.getExpression()));
	}


	/**
	 * Transpiles the binary operation.
	 *
	 * @param node   the binary operation node
	 *
	 * @return the transpiled binary operation
	 */
	public String convertBinaryOperator(final BinaryTree node) {
		final String op = switch (node.getKind()) {
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
			final ExpressionType leftType = transpiler.getExpressionType(node.getLeftOperand());
			final ExpressionType rightType = transpiler.getExpressionType(node.getRightOperand());
			if ((leftType instanceof ExpressionClassType) && (rightType instanceof ExpressionClassType)) {
				leftExpression += " as unknown";
				rightExpression += " as unknown";
			}
		}
		if (node.getKind() == Kind.DIVIDE) {
			final ExpressionType leftType = transpiler.getExpressionType(node.getLeftOperand());
			final ExpressionType rightType = transpiler.getExpressionType(node.getRightOperand());
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
	public String convertUnaryOperator(final UnaryTree node) {
		final String expr = convertExpression(node.getExpression());
		return switch (node.getKind()) {
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
	public String convertCompoundAssignment(final CompoundAssignmentTree node) {
		final String variable = convertExpression(node.getVariable());
		final String expression = convertExpression(node.getExpression());
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
	public String convertArrayAccess(final ArrayAccessTree node) {
		final String arrayName = convertExpression(node.getExpression());
		String arrayIndex = convertExpression(node.getIndex());
		final ExpressionType type = transpiler.getExpressionType(node.getIndex());
		if ((type instanceof final ExpressionClassType ect) && (ect.isIntegerType()))
			arrayIndex += strTsValueOf;
		return "%s[%s]".formatted(arrayName, arrayIndex);
	}


	/**
	 * Transpiles the type cast expression.
	 *
	 * @param node   the type cast expression tree node
	 *
	 * @return the transpiled type cast expression
	 */
	public String convertTypeCast(final TypeCastTree node) {
		final TypeNode typeNode = new TypeNode(this, node.getType(), false, false);
		return typeNode.getTypeCast(convertExpression(node.getExpression()));
	}


	/**
	 * Transpiles the instance of expression.
	 *
	 * @param node   the instance of expression tree node
	 *
	 * @return the transpiled instance of expression
	 */
	public String convertInstanceOf(final InstanceOfTree node) {
		// TODO support for PatternTree getPattern();
		final TypeNode typeNode = new TypeNode(this, node.getType(), false, false);
		final String obj = convertExpression(node.getExpression());
		return typeNode.getInstanceOf(obj);
	}


	/**
	 * Transpiles the lambda expression.
	 *
	 * @param node   the lambda expression tree node
	 *
	 * @return the transpiled lambda expression
	 */
	public String convertLambdaExpression(final LambdaExpressionTree node) {
		// determine the the method name to be used for creating the functional interface object in typescript
		final ExpressionType type = transpiler.getExpressionType(node);
		String methodName;
		if (type instanceof final ExpressionTypeLambda etl) {
			methodName = etl.getFunctionalInterfaceMethodName(transpiler);
		} else {
			throw new TranspilerException("TranspilerException: Cannot determine expression type for lambda expression");
		}

		// generate the lambda parameter code
		final StringBuilder sb = new StringBuilder();
		sb.append("{ ").append(methodName).append(" : (");
		boolean first = true;
		for (final VariableTree p : node.getParameters()) {
			if (!first)
				sb.append(", ");
			first = false;
			// TODO Bestimme über den Parent von node, welchen Typ die Variable des Lambdas hat und frage
			// die @NotNull-Annotationen von dort ab. Das ist notwendig, wenn der Typ beim Lambda selbst weggelassen wird.
			// In diesem Fall muss die Variablendeklaration oder die Methodendeklaration, wo der Lambda verwendet wird
			// analysiert werden
			final TypeNode typeNode = new TypeNode(this, p.getType(), true, transpiler.hasNotNullAnnotation(p));
			if (p.toString().contains("..."))
				sb.append("...");
			sb.append(p.getName().toString());
			sb.append(": ").append(typeNode.transpile(false));
		}
		sb.append(")");
		// TODO return type
		// generate the lambda body
		sb.append(" => ");
		switch (node.getBody()) {
			case final StatementTree bodyStatement -> sb.append(convertStatement(bodyStatement, false)).append(" }");
			case final ExpressionTree bodyExpression -> sb.append(convertExpression(bodyExpression)).append(" }");
			default -> throw new TranspilerException("Transpiler Error: Lambda Expression Type of body kind " + node.getBodyKind() + " not yet supported");
		}
		return sb.toString();
	}


	/**
	 * Transpiles the switch expression.
	 *
	 * @param node   the switch expression tree node
	 *
	 * @return the transpiled switch expression
	 */
	public String convertSwitchExpression(final SwitchExpressionTree node) {
		final String tmpVar = "_sevar_" + node.hashCode();
		final String tmpExprVar = "_seexpr_" + node.hashCode();
		final StringBuilder sb = new StringBuilder();
		sb.append("let ").append(tmpVar).append(";").append(System.lineSeparator());
		sb.append(getIndent()).append("const ").append(tmpExprVar).append(" = ").append(convertExpression(node.getExpression())).append(";");
		boolean first = true;
		for (final CaseTree ct : node.getCases()) {
			if (ct.getCaseKind() == CaseKind.STATEMENT)
				throw new TranspilerException("Transpiler Error: Cases of kind STATEMENT are not yet supported in switch expression trees");
			for (final CaseLabelTree clt : ct.getLabels()) {
				switch (clt) {
					case final PatternCaseLabelTree pclt when (pclt.getPattern() instanceof final BindingPatternTree bpt) -> {
						final VariableTree varNode = bpt.getVariable();
						final TypeNode typeNode = new TypeNode(this, varNode.getType(), true, transpiler.hasNotNullAnnotation(varNode));
						if (first) {
							sb.append(System.lineSeparator()).append(getIndent());
							first = false;
						} else {
							sb.append(" else ");
						}
						sb.append("if (").append(tmpExprVar).append(" instanceof ").append(typeNode.transpile(false)).append(") {");
						indentC++;
						sb.append(System.lineSeparator()).append(getIndent());
						sb.append("const ").append(varNode.getName()).append(" = ").append(tmpExprVar).append(" as ").append(typeNode.transpile(false)).append(";");
						final boolean isBlock = ct.getBody() instanceof BlockTree;
						sb.append(System.lineSeparator());
						if (!isBlock)
							sb.append(getIndent()).append(tmpVar).append(" = ");
						switch (ct.getBody()) {
							case final BlockTree bt -> sb.append(convertBlock(bt, false, tmpExprVar));
							case final StatementTree st -> sb.append(convertStatement(st, true));
							case final ExpressionTree et -> sb.append(convertExpression(et));
							default -> throw new TranspilerException("Transpiler Exception: Body Type not yet supported");
						}
						indentC--;
						if (!isBlock)
							sb.append(";").append(System.lineSeparator());
						sb.append(getIndent());
						sb.append("}");
					}
					case final ConstantCaseLabelTree cclt -> throw new TranspilerException("Transpiler Error: Constant Case Label Tree not yet supported");
					case final DefaultCaseLabelTree dclt -> { /* Der Default Case muss immer der letzte in TS sein */ }
					default -> throw new TranspilerException("Transpiler Error: Unkown Case Label Tree of Kind " + clt.getKind());
				}
			}
			for (final CaseLabelTree clt : ct.getLabels()) {
				switch (clt) {
					case final PatternCaseLabelTree pclt -> { /* siehe oben */ }
					case final ConstantCaseLabelTree cclt -> { /* siehe oben */ }
					case final DefaultCaseLabelTree dclt -> {
						if (first) {
							throw new TranspilerException("Transpiler Error: Switch Expression with only a default case is not supported");
						}
						sb.append(" else {");
						indentC++;
						final boolean isBlock = ct.getBody() instanceof BlockTree;
						sb.append(System.lineSeparator());
						if (!isBlock)
							sb.append(getIndent()).append(tmpVar).append(" = ");
						switch (ct.getBody()) {
							case final BlockTree bt -> sb.append(convertBlock(bt, false, tmpExprVar));
							case final StatementTree st -> sb.append(convertStatement(st, true));
							case final ExpressionTree et -> sb.append(convertExpression(et));
							default -> throw new TranspilerException("Transpiler Exception: Body Type not yet supported");
						}
						indentC--;
						if (!isBlock)
							sb.append(";").append(System.lineSeparator());
						sb.append(getIndent());
						sb.append("}");
					}
					default -> throw new TranspilerException("Transpiler Error: Unkown Case Label Tree of Kind " + clt.getKind());
				}
			}
		}
		sb.append(System.lineSeparator()).append(getIndent());
		return sb.toString();
	}


	/**
	 * Transpiles the expression.
	 *
	 * @param node   the expression
	 *
	 * @return the transpiled string if the expression was transpiled successfully and null otherwise
	 */
	public String convertExpression(final ExpressionTree node) {
		return switch (node) {
			case final BinaryTree b -> convertBinaryOperator(b);
			case final AssignmentTree a -> convertAssignment(a);
			case final CompoundAssignmentTree ca -> convertCompoundAssignment(ca);
			case final MethodInvocationTree mi -> convertMethodInvocation(mi);
			case final LiteralTree lit -> convertLiteral(lit);
			case final IdentifierTree ident -> convertIdentifier(ident);
			case final MemberSelectTree ms -> convertMemberSelect(ms);
			case final ParenthesizedTree paren -> convertParenthesized(paren);
			case final NewClassTree nc -> convertNewClass(nc);
			case final NewArrayTree na -> convertNewArray(na);
			case final ConditionalExpressionTree ce -> convertConditionalExpression(ce);
			case final UnaryTree unary -> convertUnaryOperator(unary);
			case final ArrayAccessTree aa -> convertArrayAccess(aa);
			case final TypeCastTree tc -> convertTypeCast(tc);
			case final LambdaExpressionTree le -> convertLambdaExpression(le);
			case final InstanceOfTree io -> convertInstanceOf(io);
			case final SwitchExpressionTree se -> convertSwitchExpression(se);
			default -> throw new TranspilerException("Transpiler Error: The node of kind " + node.getKind() + " is not yet supported for an expression.");
		};
	}



	/**
	 * Transpiles the conditional expression.
	 *
	 * @param node   the conditional expression
	 *
	 * @return the transpiled conditional expression
	 */
	public String convertConditionalExpression(final ConditionalExpressionTree node) {
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
	public static String convertLiteral(final LiteralTree node) {
		return switch (node.getKind()) {
			// TODO use .valueOf() in TS when next relevant parent node ist a method invocation node
			case INT_LITERAL, LONG_LITERAL, FLOAT_LITERAL, DOUBLE_LITERAL, BOOLEAN_LITERAL -> "" + node.getValue();
			case CHAR_LITERAL -> "'" + node.getValue() + "'";
			case STRING_LITERAL -> "\"" + ((String) node.getValue()).replace("\\", "\\\\").replace("\t", "\\t").replace("\b", "\\b")
				        .replace("\n", "\\n").replace("\r", "\\r").replace("\f", "\\f")
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
	public static String getDefaultValueForPrimitiveType(final PrimitiveTypeTree node) {
		return switch (node.getPrimitiveTypeKind()) {
			case BOOLEAN -> "false";
			case BYTE, SHORT, INT, LONG, FLOAT, DOUBLE -> "0";
			case CHAR -> "\"\"";
			default ->
				throw new TranspilerException("Transpiler Error: Default value for primitive type of kind " + node.getPrimitiveTypeKind() + " is not supported.");
		};
	}


	/**
	 * Returns the default value for the specified type node.
	 *
	 * @param node   the type tree node
	 *
	 * @return the default value as String
	 */
	public static String getDefaultValueForType(final Tree node) {
		return switch (node) {
			case final PrimitiveTypeTree primitiveTypeTree -> getDefaultValueForPrimitiveType(primitiveTypeTree);
			case final ArrayTypeTree att -> null;
			case final ParameterizedTypeTree ptt -> null;
			case final IdentifierTree it -> null;
			default -> throw new TranspilerException("Transpiler Error: Type node of kind " + node.getKind() + " not yet supported by the transpiler.");
		};
	}



	/**
	 * Transpiles the new array expression node.
	 *
	 * @param node   the new array expression to be transpiled
	 *
	 * @return the transpiled new array expression
	 */
	public String convertNewArray(final NewArrayTree node) {
		final Tree elementType = node.getType();
		if ((elementType == null) || (node.getDimensions().isEmpty()))
			return node.getInitializers().stream().map(this::convertExpression).collect(Collectors.joining(", ", "[", "]"));
		// initialize an array with null values for java compatibility
		final List<? extends ExpressionTree> dimensions = node.getDimensions();
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
	public String convertAssignment(final AssignmentTree node) {
		final ExpressionTree variable = node.getVariable();
		final ExpressionTree expression = node.getExpression();
		final String strVariable = convertExpression(variable);
		String strExpression = convertExpression(expression);
		final ExpressionType typeVariable = transpiler.getExpressionType(variable);
		final ExpressionType typeExpression = transpiler.getExpressionType(expression);
		if ((typeVariable == null) || (typeExpression == null))
			throw new TranspilerException("Transpiler Exception: Cannot determine expression types for assigment");
		if ((typeVariable instanceof ExpressionPrimitiveType) && (typeExpression instanceof ExpressionClassType) && (typeExpression.isPrimitiveOrBoxedPrimitive()))
			strExpression += strTsValueOf;
		return "%s = %s".formatted(strVariable, strExpression);
	}


	/**
	 * Transpiles the expression statement.
	 *
	 * @param node     the expression statement
	 *
	 * @return the transpiled expression if the statement was transpiled successfully and null otherwise
	 */
	public String convertExpressionStatement(final ExpressionStatementTree node) {
		return convertExpression(node.getExpression());
	}



	/**
	 * Transpiles the throw expression.
	 *
	 * @param node     the expression
	 *
	 * @return the transpiled expression if the statement was transpiled successfully and null otherwise
	 */
	public String convertThrow(final ThrowTree node) {
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
	public String convertStatement(final StatementTree node, final boolean noIndent) {
		if ((node instanceof final BlockTree block) && (!noIndent)) {
			indentC++;
			final String blockConverted = convertBlock(block, false, null);
			indentC--;
			return "{%s%s%s}".formatted(System.lineSeparator(), blockConverted, getIndent());
		}
		indentC++;
		final String tmpIndent = (noIndent ? "" : System.lineSeparator() + getIndent());
		indentC--;
		return switch (node) {
			case final ExpressionStatementTree es -> {
				final String expr = convertExpressionStatement(es);
				if (expr == null)
					yield "";
				yield tmpIndent + expr + ";";
			}
			case final ReturnTree ret -> tmpIndent + convertReturn(ret);
			case final BreakTree brk -> tmpIndent + convertBreak(brk);
			case final ThrowTree tt -> tmpIndent + convertThrow(tt);
			case final ContinueTree cont -> tmpIndent + convertContinue(cont);
			case final VariableTree variable -> tmpIndent + convertBlockVariable(variable);
			case final IfTree ifTree -> {
				indentC++;
				final String ifConverted = convertIf(ifTree);
				indentC--;
				yield tmpIndent + ifConverted;
			}
			case final DoWhileLoopTree dwlTree -> {
				indentC++;
				final String dwlConverted = convertDoWhileLoop(dwlTree);
				indentC--;
				yield tmpIndent + dwlConverted;
			}
			case final WhileLoopTree wlTree -> {
				indentC++;
				final String wlConverted = convertWhileLoop(wlTree);
				indentC--;
				yield tmpIndent + wlConverted;
			}
			case final ForLoopTree flTree -> {
				indentC++;
				final String flConverted = convertForLoop(flTree);
				indentC--;
				yield tmpIndent + flConverted;
			}
			case final EnhancedForLoopTree eflTree -> {
				indentC++;
				final String eflConverted = convertEnhancedForLoop(eflTree);
				indentC--;
				yield tmpIndent + eflConverted;
			}
			case final TryTree ttTree -> {
				indentC++;
				final String ttConverted = convertTry(ttTree);
				indentC--;
				yield tmpIndent + ttConverted;
			}
			case final EmptyStatementTree est -> ";";
			default -> throw new TranspilerException("Transpiler Error: Statement node of kind " + node.getKind() + " not yet supported by the transpiler.");
		};
	}


	/**
	 * Transpiles the if statement.
	 *
	 * @param node     the if statement
	 *
	 * @return the transpiled if statement
	 */
	public String convertIf(final IfTree node) {
		String result = "if ";
		result += convertExpression(node.getCondition());
		if ((node.getThenStatement() instanceof BlockTree))
			result += " ";
		result += convertStatement(node.getThenStatement(), false);
		if (node.getElseStatement() != null) {
			if ((node.getThenStatement() instanceof BlockTree))
				result += " ";
			else
				result += System.lineSeparator() + getIndent();
			result += "else";
			if ((node.getElseStatement() instanceof BlockTree))
				result += " ";
			result += convertStatement(node.getElseStatement(), false);
		}
		return result;
	}


	/**
	 * Transpiles the switch statement.
	 *
	 * @param node     the switch statement
	 * @param noIndent   true if no indentation should be used
	 *
	 * @return the transpiled switch statement
	 */
	public String convertSwitch(final SwitchTree node, final boolean noIndent) {
		final StringBuilder sb = new StringBuilder();
		sb.append("switch ").append(convertExpression(node.getExpression())).append(" {").append(System.lineSeparator());
		for (final CaseTree curCase : node.getCases()) {
			final CaseKind kind = curCase.getCaseKind();
			switch (kind) {
				case RULE -> throw new TranspilerException("Transpiler Error: Case of type " + kind + " currently not supported in switch statements."); // TODO implement
				case STATEMENT -> {
					if (curCase.getExpressions().isEmpty())
						sb.append(getIndent()).append("\tdefault:");
					else
						sb.append(curCase.getExpressions().stream().map(exp -> "case " + convertExpression(exp)).collect(Collectors.joining(":" + System.lineSeparator() + getIndent() + "\t", getIndent() + "\t", ":")));
					indentC++;
					if ((curCase.getStatements().size() == 1) && (curCase.getStatements().get(0) instanceof BlockTree)) {
						sb.append(convertStatement(curCase.getStatements().get(0), false));
						sb.append(System.lineSeparator());
					} else if (!curCase.getStatements().isEmpty()) {
						sb.append(" {");
						sb.append(curCase.getStatements().stream().map(stmt -> convertStatement(stmt, false))
								.collect(Collectors.joining("", "", System.lineSeparator())));
						sb.append(getIndent()).append("}").append(System.lineSeparator());
					} else {
						sb.append(System.lineSeparator());
					}
					indentC--;
				}
				default -> throw new TranspilerException("Transpiler Error: Case of type " + kind + " currently not supported in switch statements.");
			}
		}
		sb.append((noIndent ? "" : getIndent())).append("}");
		return sb.toString();
	}


	/**
	 * Transpiles the return statement.
	 *
	 * @param node     the return statement
	 *
	 * @return the transpiled return statement
	 */
	public String convertReturn(final ReturnTree node) {
		if (node.getExpression() == null)
			return "return;";
		String converted = convertExpression(node.getExpression());
		final ExpressionType type = transpiler.getExpressionType(node.getExpression());
		if ((type instanceof final ExpressionClassType ect) && (ect.isPrimitiveOrBoxedPrimitive())) {
			final Tree parent = transpiler.getMethod(node);
			if (parent instanceof final MethodTree mt) {
				final MethodNode method = MethodNode.methodNodes.get(mt);
				if (method == null)
					throw new TranspilerException("Transpiler Error: Unkown method while handling boxed return type.");
				final TypeNode returnType = method.getReturnType();
				if (returnType == null)
					throw new TranspilerException("Transpiler Error: Method return type expected while handling boxed return type.");
				if (returnType.isPrimitive())
					converted += "!";
			} else if (parent instanceof LambdaExpressionTree) {
				throw new TranspilerException("Transpiler Error: Handling boxed return types for lambda expressions not yet supported");
			}
		}
		if (node.getExpression() instanceof final SwitchExpressionTree set)
			return converted + "return _sevar_" + set.hashCode() + ";";
		return "return %s;".formatted(converted);
	}


	/**
	 * Transpiles the yield statement.
	 *
	 * @param node       the yield statement
	 * @param yieldVar   the temporary variable for the switch-yield-expression
	 *
	 * @return the transpiled yield statement
	 */
	public String convertYield(final YieldTree node, final String yieldVar) {
		final String converted = convertExpression(node.getValue());
		return "%s = %s;".formatted(yieldVar, converted);
	}


	/**
	 * Transpiles the break statement.
	 *
	 * @param node     the break statement
	 *
	 * @return the transpiled break statement
	 */
	public static String convertBreak(final BreakTree node) {
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
	public static String convertContinue(final ContinueTree node) {
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
	public String convertCatch(final CatchTree node) {
		final VariableTree param = node.getParameter();
		if (param == null)
			throw new TranspilerException("Transpiler Error: Catch clause without a parameter variable is not supported.");
		String result = "catch(" + param.getName().toString() + ") {" + System.lineSeparator();
		indentC++;
		result += convertBlock(node.getBlock(), false, null);
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
	public String convertTry(final TryTree node) {
		if ((node.getResources() != null) && (!node.getResources().isEmpty()))
			throw new TranspilerException("Transpiler Error: Try with resources currently not supported.");
		if ((node.getCatches() != null) && (node.getCatches().size() > 1))
			throw new TranspilerException("Transpiler Error: Try with multiple catch clauses currently not supported.");
		String result = "try {" + System.lineSeparator();
		indentC++;
		result += convertBlock(node.getBlock(), false, null);
		indentC--;
		result += getIndent() + "}";
		if (node.getCatches() != null)
			result += node.getCatches().stream().map(this::convertCatch).collect(Collectors.joining(" ", " ", ""));
		final BlockTree finallyBlock = node.getFinallyBlock();
		if (finallyBlock != null) {
			result += " finally {" + System.lineSeparator();
			indentC++;
			result += convertBlock(finallyBlock, false, null);
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
	 * @param yieldVar      the name of the temporary yield variable if yield is used
	 *
	 * @return the transpiled block
	 */
	public String convertBlock(final BlockTree node, final boolean ignoreFirst, final String yieldVar) {
		if (node == null)
			return null;
		final List<? extends StatementTree> childs = node.getStatements();
		final StringBuilder sb = new StringBuilder();
		if (childs.isEmpty()) {
			sb.append(getIndent()).append("// empty block").append(System.lineSeparator());
		} else {
			for (int i = (ignoreFirst ? 1 : 0); i < childs.size(); i++) {
				final StatementTree child = childs.get(i);
				final String strChild = switch (child.getKind()) {
					case VARIABLE -> convertBlockVariable((VariableTree) child) + ";";
					case ENHANCED_FOR_LOOP -> convertEnhancedForLoop((EnhancedForLoopTree) child);
					case FOR_LOOP -> convertForLoop((ForLoopTree) child);
					case WHILE_LOOP -> convertWhileLoop((WhileLoopTree) child);
					case DO_WHILE_LOOP -> convertDoWhileLoop((DoWhileLoopTree) child);
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
					case THROW -> convertThrow((ThrowTree) child);
					case EMPTY_STATEMENT -> "";
					case YIELD -> convertYield((YieldTree) child, yieldVar);
					default -> throw new TranspilerException("Transpiler Error: Child of type " + child.getKind() + " currently not supported in statement blocks.");
				};
				if (strChild != null)
					sb.append(getIndent()).append(strChild).append(System.lineSeparator());
			}
		}
		return sb.toString();
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
	public String convertMethodInvocationParameters(final List<? extends ExpressionTree> expressions, final String enumValueName, final Integer enumOrdinal, final boolean noParentheses) {
		if (expressions == null)
			return "";
		final String enumInject = ((enumValueName != null) && (enumOrdinal != null)) ? "\"" + enumValueName + "\", " + enumOrdinal + ", " : "";
		final StringBuilder sb = new StringBuilder();
		sb.append((noParentheses ? "" : "(")).append(enumInject);
		for (int i = 0; i < expressions.size(); i++) {
			final ExpressionTree expr = expressions.get(i);
			if (i > 0)
				sb.append(", ");
			final ExpressionType type = ExpressionType.getExpressionType(transpiler, expr);

			// check whether we need a spread operator
			if ((i == expressions.size() - 1) && (type instanceof ExpressionArrayType)) {
				// get the parameter and its type
				final TranspilerUnit unit = transpiler.getTranspilerUnit(expr);
				final Tree parent = transpiler.getParent(expr);
				if (parent instanceof final MethodInvocationTree mit) {
					final ExpressionTree exprTree = mit.getMethodSelect();
					if (exprTree instanceof final MemberSelectTree mst) {
						final ExecutableElement ee = unit.allInvokedMethods.get(mst);
						if (ee == null)
							throw new TranspilerException("Transpiler Error: Cannot determine method for method invocation");
						final List<? extends VariableElement> params = ee.getParameters();
						if (expressions.size() != params.size())
							throw new TranspilerException("Transpiler Error: Number of parameters in invoked method is to small");
						if (ee.isVarArgs())
							sb.append("...");
					}
					if (exprTree instanceof final IdentifierTree it) {
						final Set<ExecutableElement> methods = unit.allLocalMethodElements.get(it.toString());
						if (methods == null)
							throw new TranspilerException("Transpiler Error: Cannot determine method for identifier");
						// TODO if methods.size() > 1 check for the method that has the best fitting parameter types
						for (final ExecutableElement method : methods) {
							final List<? extends VariableElement> methodParams = method.getParameters();
							if (methodParams == null)
								continue; // invalid method
							if (expressions.size() != methodParams.size())
								continue; // invalid number of parameters
							if (method.isVarArgs())
								sb.append("...");
						}
					}
				}
			}

			// do the conversion
			sb.append(convertExpression(expr));

			// check parameter expression type and type of the methods parent member select for invoked method and add type conversion for wrapped types
			if ((type instanceof final ExpressionClassType ect) && ect.isNumberType()) {
				// get the parameter and its type
				final TranspilerUnit unit = transpiler.getTranspilerUnit(expr);
				final Tree parent = transpiler.getParent(expr);
				if (parent instanceof final MethodInvocationTree mit) {
					final ExpressionTree exprTree = mit.getMethodSelect();
					if (exprTree instanceof final MemberSelectTree mst) {
						final ExecutableElement ee = unit.allInvokedMethods.get(mst);
						if (ee == null)
							throw new TranspilerException("Transpiler Error: Cannot determine method for method invocation");
						final List<? extends VariableElement> params = ee.getParameters();
						if (i >= params.size())
							throw new TranspilerException("Transpiler Error: Number of parameters in invoked method is to small");
						final VariableElement param = params.get(i);
						// append tsValueOf if the parameter type requires a primitive type instead of the wrapper type
						if (param.asType().getKind().isPrimitive())
							sb.append("!");
					} else if (exprTree instanceof final IdentifierTree it) {
						final Set<ExecutableElement> methods = unit.allLocalMethodElements.get(it.toString());
						if (methods == null)
							throw new TranspilerException("Transpiler Error: Cannot determine method for identifier");
						// TODO if methods.size() > 1 check for the method that has the best fitting parameter types
						for (final ExecutableElement method : methods) {
							final List<? extends VariableElement> methodParams = method.getParameters();
							if (methodParams == null)
								continue; // invalid method
							if (expressions.size() != methodParams.size())
								continue; // invalid number of parameters
							final VariableElement param = methodParams.get(i);
							// append tsValueOf if the parameter type requires a primitive type instead of the wrapper type
							if (param.asType().getKind().isPrimitive())
								sb.append("!");
						}
					}
				}
			}
		}
		sb.append(noParentheses ? "" : ")");
		return sb.toString();
	}


	/**
	 * Transpiles the new class expression.
	 *
	 * @param node   the new class expression
	 *
	 * @return the transpiled new class expression
	 */
	public String convertNewClass(final NewClassTree node) {
		// special handling of String constructor calls
		if (strString.equals(node.getIdentifier().toString())) {
			final List<? extends ExpressionTree> args = node.getArguments();
			if (args.isEmpty())
				return "\"\"";
			if (args.size() == 1) {
				final ExpressionTree expression = args.get(0);
				if ((expression instanceof final LiteralTree li) && (li.getKind() == Tree.Kind.STRING_LITERAL))
					return convertLiteral(li);
				if (expression instanceof final IdentifierTree ident) {
					final VariableTree variable = transpiler.getDeclaration(ident);
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
		final TypeNode typeNode = new TypeNode(this, node.getIdentifier(), false, false);
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
	public String convertMethodInvocation(final MethodInvocationTree node) {
		// print the expression to identify the method
		final StringBuilder sb = new StringBuilder();
		final ExpressionTree methodExpression = node.getMethodSelect();
		if (methodExpression instanceof final IdentifierTree ident) {
			// add super method calls in classes without a specified super class
			if (strSuper.equals(ident.getName().toString())) {
				final ExpressionType et = transpiler.getExpressionType(ident);
				if ((et == null) || (et instanceof ExpressionTypeNone))
					return "super()";
			}
			sb.append(convertIdentifier(ident));
		} else if (methodExpression instanceof final MemberSelectTree ms) {
			final ExpressionType type = transpiler.getExpressionType(ms.getExpression());
			// replace all hashCode invocations
			if ("hashCode".equals(ms.getIdentifier().toString())) {
				final String expression = convertExpression(ms.getExpression());
				if (strSuper.equals(expression))
					return "super.hashCode()";
				return "JavaObject.getTranspilerHashCode(" + expression + ")";
			}
			// replace all equals invocations
			if ("equals".equals(ms.getIdentifier().toString())) {
				final String expression = convertExpression(ms.getExpression());
				if (strSuper.equals(expression))
					return "super.equals(" + convertMethodInvocationParameters(node.getArguments(), null, null, false) + ")";
				return "JavaObject.equalsTranspiler(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, false) + ")";
			}
			// replace Long, Integer, Short, Byte, Float and Double methods...
			if (type.isNumberType()) {
				final String identifier = ms.getIdentifier().toString();
				// replace compare invocations
				if (strCompare.equals(identifier)) {
					final String expression = type.toString();
					return "Java" + expression + "." + identifier + convertMethodInvocationParameters(node.getArguments(), null, null, false);
				}
				//if ("intValue".equals(identifier) && (type instanceof ExpressionClassType ect)) {
				if (("byteValue".equals(identifier) || "shortValue".equals(identifier) || "intValue".equals(identifier) || "longValue".equals(identifier)) && (type instanceof final ExpressionClassType ect)) {
					final String expression = convertExpression(ms.getExpression());
					if ("java.lang.Double".equals(ect.getFullQualifiedName()) || ("java.lang.Float".equals(ect.getFullQualifiedName())))
						return "Math.trunc(" + expression + "!)";
					return expression + "!";
				}
			}
			// replace String methods...
			if (type.isString()) {
				final String expression = convertExpression(ms.getExpression());
				final Set<String> strMethods = Set.of(
					"contains", "indexOf", "compareTo", "compareToIgnoreCase", "equalsIgnoreCase",
					"replaceAll", "replaceFirst", "formatted", "format", "length", "isBlank", "isEmpty"
				);
				if (strMethods.contains(ms.getIdentifier().toString())) {
					transpiler.getTranspilerUnit(node).imports.put("String", "java.lang");
					return switch (ms.getIdentifier().toString()) {
						case "contains" -> "JavaString.contains(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
						case "indexOf" -> "JavaString.indexOf(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
						case "compareTo" -> "JavaString.compareTo(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
						case "compareToIgnoreCase" -> "JavaString.compareToIgnoreCase(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
						case "equalsIgnoreCase" -> "JavaString.equalsIgnoreCase(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
						case "replaceAll" -> "JavaString.replaceAll(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
						case "replaceFirst" -> "JavaString.replaceFirst(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
						case "formatted" -> "JavaString.format(" + expression + ", " + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
						case "format" -> "JavaString.format(" + convertMethodInvocationParameters(node.getArguments(), null, null, true) + ")";
						case "length" -> expression + ".length"; // in typescript it is not a method...
						case "isBlank" -> "JavaString.isBlank(" + expression + ")";
						case "isEmpty" -> "JavaString.isEmpty(" + expression + ")";
						default -> throw new TranspilerException("TranspilerError: Unhandled String method");
					};
				}
			}
			// replace reflective Array commands
			if ((type instanceof final ExpressionClassType classType) && ("java.lang.reflect.Array".equals(classType.getFullQualifiedName()))) {
				if ("newInstance".equals(ms.getIdentifier().toString())) {
					final List<? extends ExpressionTree> params = node.getArguments();
					if ((params == null) || (params.size() < 2))
						throw new TranspilerException("TranspilerError: Invalid number of parameters for Array.newInstance");
					final ExpressionTree param = params.get(1);
					final ExpressionType paramType = transpiler.getExpressionType(param);
					if ((paramType instanceof final ExpressionPrimitiveType ept) && (ept.isNumberType()))
						return "Array(" + this.convertExpression(param) + ").fill(null)";
					if ((paramType instanceof final ExpressionClassType ect) && (ect.isNumberType()))
						return "Array(" + this.convertExpression(param) + ".valueOf()).fill(null)";
					if (paramType instanceof final ExpressionArrayType eat)
						throw new TranspilerException("Transpiler Error: Array.newInstance not yet supported for multidimensional arrays");
					throw new TranspilerException("Transpiler Error: Array.newInstance with unsupported parameter types");
				}
			}
			// replace System.out and System.err commands
			if ((type instanceof final ExpressionClassType classType) && ("java.io.PrintStream".equals(classType.getFullQualifiedName()))) {
				final String expression = convertExpression(ms.getExpression());
				if (expression.equals("System.out")) {
					if ("flush".equals(ms.getIdentifier().toString()))
						return null;
					if ("print".equals(ms.getIdentifier().toString()) || "println".equals(ms.getIdentifier().toString())) {
						if (!node.getArguments().isEmpty())
							return "console.log(JSON.stringify" + convertMethodInvocationParameters(node.getArguments(), null, null, false) + ")";
						return "console.log" + convertMethodInvocationParameters(node.getArguments(), null, null, false);
					}
				} else if (expression.equals("System.err")) {
					if ("flush".equals(ms.getIdentifier().toString()))
						return null;
					if ("print".equals(ms.getIdentifier().toString()) || "println".equals(ms.getIdentifier().toString())) {
						if (!node.getArguments().isEmpty())
							return "console.error(JSON.stringify" + convertMethodInvocationParameters(node.getArguments(), null, null, false) + ")";
						return "console.error" + convertMethodInvocationParameters(node.getArguments(), null, null, false);
					}
				}
			}
			sb.append(convertMemberSelect(ms));
		} else {
			throw new TranspilerException("Transpiler Error: Unhandled method expression type of kind " + methodExpression.getKind() + ".");
		}

		// check whether the result type is String an the parent tree node requires a valueOf() call in TypeScript
		String valueOf = "";
		final ExpressionType type = transpiler.getExpressionType(node);
		if ((type instanceof final ExpressionClassType ect) && ("java.lang.String".equals(ect.getFullQualifiedName()))) {
			final Tree parent = transpiler.getParent(node);
			if ((parent instanceof final BinaryTree bt) && (bt.getKind() == Kind.PLUS))
				valueOf = "!";
		}

		// print arguments for the method call
		sb.append(convertMethodInvocationParameters(node.getArguments(), null, null, false));
		sb.append(valueOf);
		return sb.toString();
	}


	/**
	 * Transpiles the attribute of a class.
	 *
	 * @param node     the attribute to be transpiled
	 * @param enumName   specifies the name of the enum if the attribute belongs to an enum
	 *
	 * @return the transpiled attribute
	 */
	public String convertAttribute(final VariableTree node, final String enumName) {
		// get comment
		final String comment = formatComment(this.getTranspiler().getComment(node));

		// get the typescript attribute initialization code
		String initializer = "";
		boolean forceNotNull = false;
		if (node.getInitializer() != null) {
			initializer = " = ";
			if ((enumName != null) && (node.getInitializer() instanceof final NewClassTree newClassTree) && (newClassTree.getIdentifier()).toString().equals(enumName)) {
				forceNotNull = true; // for attribute generation below...
				// print new operator and class identifier
				final TypeNode typeNode = new TypeNode(this, newClassTree.getIdentifier(), false, false);
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
			if (node.getType() instanceof final PrimitiveTypeTree ptt) {
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
		final String accessModifier = transpiler.getAccessModifier(node);

		// Prüfe ob der Bezeichner in Typescript zulässig ist
		pruefeBezeichner(node.getName().toString());

		// convert to typescrypt code
		final TypeNode typeNode = new TypeNode(this, node.getType(), true, forceNotNull ? true : transpiler.hasNotNullAnnotation(node));
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
	public String getCastFunctionName(final ClassTree node) {
		return "cast_" + this.transpiler.getCompilationUnit(node).getPackageName().toString().replace('.', '_') + "_" + node.getSimpleName().toString();
	}

	/**
	 * Appends a function to cast an object to the class type. The function
	 * is appended after the class definition.
	 *
	 * @param sb       the StringBuilder where the output is written to
	 * @param node     the class to be transpiled
	 */
	public void appendCast(final StringBuilder sb, final ClassTree node) {
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
	public String appendIsTranspiledInstanceOf(final ClassTree node) {
		String result = getIndent() + "isTranspiledInstanceOf(name : string): boolean {" + System.lineSeparator();
		indentC++;
		final TranspilerUnit unit = transpiler.getTranspilerUnit(node);
		String strInstanceOfTypes = unit.superTypes.stream().collect(Collectors.joining("', '", "'", "'"));
		if (node.getKind() == Kind.ENUM)
			strInstanceOfTypes += ", 'java.lang.Enum'";
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
	public String appendTranspilerFromJSON(final ClassTree node) {
		final StringBuilder sb = new StringBuilder();
		sb.append(getIndent() + "public static transpilerFromJSON(json : string): " + node.getSimpleName().toString() + " {" + System.lineSeparator());
		indentC++;
		sb.append(getIndent() + "const obj = JSON.parse(json);" + System.lineSeparator());
		sb.append(getIndent() + "const result = new " + node.getSimpleName().toString() + "();" + System.lineSeparator());
		for (final VariableTree attribute : transpiler.getAttributesWithSuperclassAttributes(node)) {
			final VariableNode variable = new VariableNode(this, attribute);
			if (variable.isStatic()) // ignore static members
				continue;
			final TypeNode type = variable.getTypeNode();
			if (type.isPrimitive() || (type.isNotNull() && (type.isString() || type.isNumberClass() || type.isBoolean()))) {
				sb.append(getIndent() + "if (typeof obj." + attribute.getName() + " === \"undefined\")" + System.lineSeparator());
				sb.append(getIndent() + "\t throw new Error('invalid json format, missing attribute " + attribute.getName() + "');" + System.lineSeparator());
				sb.append(getIndent() + "result." + attribute.getName() + " = obj." + attribute.getName() + ";" + System.lineSeparator());
			} else if ((!type.isNotNull()) && (type.isString() || type.isNumberClass() || type.isBoolean())) {
				String tmpAttribute = "obj." + attribute.getName();
				if (type.isString() || type.isNumberClass() || type.isBoolean())
					tmpAttribute = "" + tmpAttribute + " === null ? null : " + tmpAttribute;
				sb.append(getIndent() + "result." + attribute.getName() + " = typeof obj." + attribute.getName() + " === \"undefined\" ? null : " + tmpAttribute + ";" + System.lineSeparator());
			} else if (type.isCollectionType()) {
				// TODO notNull, Collection initialisiert
				final TypeNode paramType = type.getParameterType(0, false);
				if (paramType == null)
					throw new TranspilerException("Transpiler Error: Cannot determine type parameter for the collection type " + type.transpile(false) + ".");
				sb.append(getIndent() + "if ((obj." + attribute.getName() + " !== undefined) && (obj." + attribute.getName() + " !== null)) {" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "for (const elem of obj." + attribute.getName() + ") {" + System.lineSeparator());
				indentC++;
				if (paramType.isNotNull() && (paramType.isString() || paramType.isNumberClass() || paramType.isBoolean()))
					sb.append(getIndent() + "result." + attribute.getName() + "?.add(elem);" + System.lineSeparator());
				else if (paramType.isString() || paramType.isNumberClass() || paramType.isBoolean())
					sb.append(getIndent() + "result." + attribute.getName() + "?.add(elem === null ? null : elem);" + System.lineSeparator());
				else
					sb.append(getIndent() + "result." + attribute.getName() + "?.add(" + paramType.transpile(true) + ".transpilerFromJSON(JSON.stringify(elem)));" + System.lineSeparator());
				indentC--;
				sb.append(getIndent() + "}" + System.lineSeparator());
				indentC--;
				sb.append(getIndent() + "}" + System.lineSeparator());
			} else if (type.isArrayType()) {
				TypeNode contentType = type.getArrayContentType(transpiler);
				if (contentType == null)
					throw new TranspilerException("Transpiler Error: Cannot determine array content type of " + type.transpile(false) + " for JSON deserialization.");
				contentType = contentType.getNoDeclarationType();
				sb.append(getIndent() + "for (let i = 0; i < obj." + attribute.getName() + ".length; i++) {" + System.lineSeparator());
				indentC++;
				if (contentType.isPrimitive()) {
					sb.append(getIndent() + "result." + attribute.getName() + "[i] = obj." + attribute.getName() + "[i];" + System.lineSeparator());
				} else if (contentType.isNotNull()) {
					final String tmpAttribute = "obj." + attribute.getName() + "[i]";
					if (contentType.isString() || contentType.isNumberClass() || contentType.isBoolean())
						sb.append(getIndent() + "result." + attribute.getName() + "[i] = " + tmpAttribute + ";" + System.lineSeparator());
					else
						sb.append(getIndent() + "result." + attribute.getName() + "[i] = (" + contentType.transpile(true) + ".transpilerFromJSON(JSON.stringify(" + tmpAttribute + ")));" + System.lineSeparator());
				} else {
					final String tmpAttribute = "obj." + attribute.getName() + "[i]";
					if (contentType.isString() || contentType.isNumberClass() || contentType.isBoolean())
						sb.append(getIndent() + "result." + attribute.getName() + "[i] = " + tmpAttribute + " === null ? null : " + tmpAttribute + ";" + System.lineSeparator());
					else
						sb.append(getIndent() + "result." + attribute.getName() + "[i] = " + tmpAttribute + " == null ? null : (" + contentType.transpile(true) + ".transpilerFromJSON(JSON.stringify(" + tmpAttribute + ")));" + System.lineSeparator());
				}
				indentC--;
				sb.append(getIndent() + "}" + System.lineSeparator());
			} else {
				if (type.isNotNull()) {
					sb.append(getIndent() + "if (typeof obj." + attribute.getName() + " === \"undefined\")" + System.lineSeparator());
					sb.append(getIndent() + "\t throw new Error('invalid json format, missing attribute " + attribute.getName() + "');" + System.lineSeparator());
					sb.append(getIndent() + "result." + attribute.getName() + " = " + type.transpile(true) + ".transpilerFromJSON(JSON.stringify(obj." + attribute.getName() + "));" + System.lineSeparator());
				} else {
					sb.append(getIndent() + "result." + attribute.getName() + " = ((typeof obj." + attribute.getName() + " === \"undefined\") || (obj." + attribute.getName() + " === null)) ? null : " + type.getNoDeclarationType().transpile(true) + ".transpilerFromJSON(JSON.stringify(obj." + attribute.getName() + "));" + System.lineSeparator());
				}
			}
		}
		sb.append(getIndent() + "return result;" + System.lineSeparator());
		indentC--;
		sb.append(getIndent() + "}" + System.lineSeparator());
		sb.append(System.lineSeparator());
		return sb.toString();
	}


	/**
	 * Appends a method for creating JSON from the TS object to the
	 * transpiled class
	 *
	 * @param node   the class tree node
	 *
	 * @return the transpilerToJSON method code as a String
	 */
	public String appendTranspilerToJSON(final ClassTree node) {
		final StringBuilder sb = new StringBuilder();
		sb.append(getIndent() + "public static transpilerToJSON(obj : " + node.getSimpleName().toString() + ") : string {" + System.lineSeparator());
		indentC++;
		sb.append(getIndent() + "let result = '{';" + System.lineSeparator());
		final List<VariableTree> attributes = transpiler.getAttributesWithSuperclassAttributes(node);
		for (int i = 0; i < attributes.size(); i++) {
			final VariableTree attribute = attributes.get(i);
			final String endline = " + ',';" + System.lineSeparator();
			final VariableNode variable = new VariableNode(this, attribute);
			if (variable.isStatic()) // ignore static members
				continue;
			final TypeNode type = variable.getTypeNode();
			final String addAttrName = "result += '\"" + attribute.getName() + "\" : '";
			final String objAttr = "obj." + attribute.getName();
			if (type.isPrimitive()) {
				if (type.isPrimitveBoolean()) {
					sb.append(getIndent() + addAttrName + " + " + objAttr + endline);
				} else if (type.isPrimitveChar()) {
					sb.append(getIndent() + addAttrName + " + '\"' + " + objAttr + " + '\"'" + endline);
				} else if (type.isPrimitveInteger()) {
					sb.append(getIndent() + addAttrName + " + " + objAttr + endline);
				} else if (type.isPrimitveFloat()) {
					sb.append(getIndent() + addAttrName + " + " + objAttr + endline);
				} else {
					throw new TranspilerException("Transpiler Error: Unsupported primitive type while generating transpilerToJSON method");
				}
			} else if (type.isString()) {
				if (type.isNotNull()) {
					sb.append(getIndent() + addAttrName + " + JSON.stringify(" + objAttr + "!)" + endline);
				} else {
					sb.append(getIndent() + addAttrName + " + ((!" + objAttr + ") ? 'null' : JSON.stringify(" + objAttr + "))" + endline);
				}
			} else if (type.isNumberClass() || type.isBoolean()) {
				if (type.isNotNull()) {
					sb.append(getIndent() + addAttrName + " + " + objAttr + "!" + endline);
				} else {
					sb.append(getIndent() + addAttrName + " + ((!" + objAttr + ") ? 'null' : " + objAttr + ")" + endline);
				}
			} else if (type.isCollectionType()) {
				// TODO notNull, Collection initialisiert
				final TypeNode paramType = type.getParameterType(0, false);
				if (paramType == null)
					throw new TranspilerException("Transpiler Error: Cannot determine type parameter for the collection type " + type.transpile(false) + ".");
				sb.append(getIndent() + "if (!obj." + attribute.getName() + ") {" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "result += '\"" + attribute.getName() + "\" : []';" + System.lineSeparator());
				indentC--;
				sb.append(getIndent() + "} else {" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "result += '\"" + attribute.getName() + "\" : [ ';" + System.lineSeparator());
				sb.append(getIndent() + "for (let i = 0; i < " + objAttr + ".size(); i++) {" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "const elem = " + objAttr + ".get(i);" + System.lineSeparator());
				if (paramType.isString()) {
					if (paramType.isNotNull())
						sb.append(getIndent() + "result += " + "'\"' + elem + '\"';" + System.lineSeparator());
					else
						sb.append(getIndent() + "result += " + "(elem == null) ? null : '\"' + elem + '\"';" + System.lineSeparator());
				} else if (paramType.isNumberClass() || paramType.isBoolean()) {
					if (paramType.isNotNull())
						sb.append(getIndent() + "result += elem;" + System.lineSeparator());
					else
						sb.append(getIndent() + "result += (elem == null) ? null : elem;" + System.lineSeparator());
				} else
					sb.append(getIndent() + "result += " + paramType.transpile(true) + ".transpilerToJSON(elem);" + System.lineSeparator());
				sb.append(getIndent() + "if (i < " + objAttr + ".size() - 1)" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "result += ',';" + System.lineSeparator());
				indentC--;
				indentC--;
				sb.append(getIndent() + "}" + System.lineSeparator());
				sb.append(getIndent() + "result += ' ]'" + endline);
				indentC--;
				sb.append(getIndent() + "}" + System.lineSeparator());
			} else if (type.isArrayType()) {
				TypeNode contentType = type.getArrayContentType(transpiler);
				if (contentType == null)
					throw new TranspilerException("Transpiler Error: Cannot determine array content type of " + type.transpile(false) + " for JSON deserialization.");
				contentType = contentType.getNoDeclarationType();

				sb.append(getIndent() + "if (!obj." + attribute.getName() + ") {" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "result += '\"" + attribute.getName() + "\" : []';" + System.lineSeparator());
				indentC--;
				sb.append(getIndent() + "} else {" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "result += '\"" + attribute.getName() + "\" : [ ';" + System.lineSeparator());
				sb.append(getIndent() + "for (let i = 0; i < " + objAttr + ".length; i++) {" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "const elem = " + objAttr + "[i];" + System.lineSeparator());
				if (contentType.isString()) {
					if (contentType.isNotNull())
						sb.append(getIndent() + "result += " + "'\"' + elem + '\"';" + System.lineSeparator());
					else
						sb.append(getIndent() + "result += " + "(elem == null) ? null : '\"' + elem + '\"';" + System.lineSeparator());
				} else if (contentType.isNumberClass() || contentType.isBoolean()) {
					if (contentType.isNotNull())
						sb.append(getIndent() + "result += elem;" + System.lineSeparator());
					else
						sb.append(getIndent() + "result += (elem == null) ? null : elem;" + System.lineSeparator());
				} else if (contentType.isPrimitive()) {
					sb.append(getIndent() + "result += JSON.stringify(elem);" + System.lineSeparator());
				} else {
					if (contentType.isNotNull())
						sb.append(getIndent() + "result += " + contentType.transpile(true) + ".transpilerToJSON(elem);" + System.lineSeparator());
					else
						sb.append(getIndent() + "result += (elem == null) ? null : " + contentType.transpile(true) + ".transpilerToJSON(elem);" + System.lineSeparator());
				}
				sb.append(getIndent() + "if (i < " + objAttr + ".length - 1)" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "result += ',';" + System.lineSeparator());
				indentC--;
				indentC--;
				sb.append(getIndent() + "}" + System.lineSeparator());
				sb.append(getIndent() + "result += ' ]'" + endline);
				indentC--;
				sb.append(getIndent() + "}" + System.lineSeparator());
			} else {
				if (type.isNotNull()) {
					sb.append(getIndent() + addAttrName + " + " + type.transpile(true) + ".transpilerToJSON(" + objAttr + ")" + endline);
				} else {
					sb.append(getIndent() + addAttrName + " + ((!" + objAttr + ") ? 'null' : " + type.getNoDeclarationType().transpile(true) + ".transpilerToJSON(" + objAttr + "))" + endline);
				}
			}
		}
		sb.append(getIndent() + "result = result.slice(0, -1);" + System.lineSeparator());
		sb.append(getIndent() + "result += '}';" + System.lineSeparator());
		sb.append(getIndent() + "return result;" + System.lineSeparator());
		indentC--;
		sb.append(getIndent() + "}" + System.lineSeparator());
		sb.append(System.lineSeparator());
		return sb.toString();
	}


	/**
	 * Appends a method for creating a JSON Patch from the TS object to the
	 * transpiled class
	 *
	 * @param node   the class tree node
	 *
	 * @return the transpilerToJSON method code as a String
	 */
	public String appendTranspilerToJSONPatch(final ClassTree node) {
		final StringBuilder sb = new StringBuilder();
		sb.append(getIndent() + "public static transpilerToJSONPatch(obj : Partial<" + node.getSimpleName().toString() + ">) : string {" + System.lineSeparator());
		indentC++;
		sb.append(getIndent() + "let result = '{';" + System.lineSeparator());
		final List<VariableTree> attributes = transpiler.getAttributesWithSuperclassAttributes(node);
		for (int i = 0; i < attributes.size(); i++) {
			final VariableTree attribute = attributes.get(i);
			final String endline = " + ',';" + System.lineSeparator();
			final VariableNode variable = new VariableNode(this, attribute);
			if (variable.isStatic()) // ignore static members
				continue;
			final TypeNode type = variable.getTypeNode();
			final String addAttrName = "result += '\"" + attribute.getName() + "\" : '";
			final String objAttr = "obj." + attribute.getName();
			sb.append(getIndent() + "if (typeof " + objAttr + " !== \"undefined\") {" + System.lineSeparator());
			indentC++;
			if (type.isPrimitive()) {
				if (type.isPrimitveBoolean()) {
					sb.append(getIndent() + addAttrName + " + " + objAttr + endline);
				} else if (type.isPrimitveChar()) {
					sb.append(getIndent() + addAttrName + " + '\"' + " + objAttr + " + '\"'" + endline);
				} else if (type.isPrimitveInteger()) {
					sb.append(getIndent() + addAttrName + " + " + objAttr + endline);
				} else if (type.isPrimitveFloat()) {
					sb.append(getIndent() + addAttrName + " + " + objAttr + endline);
				} else {
					throw new TranspilerException("Transpiler Error: Unsupported primitive type while generating transpilerToJSON method");
				}
			} else if (type.isString()) {
				if (type.isNotNull()) {
					sb.append(getIndent() + addAttrName + " + JSON.stringify(" + objAttr + "!)" + endline);
				} else {
					sb.append(getIndent() + addAttrName + " + ((!" + objAttr + ") ? 'null' : JSON.stringify(" + objAttr + "))" + endline);
				}
			} else if (type.isNumberClass() || type.isBoolean()) {
				if (type.isNotNull()) {
					sb.append(getIndent() + addAttrName + " + " + objAttr + endline);
				} else {
					sb.append(getIndent() + addAttrName + " + ((!" + objAttr + ") ? 'null' : " + objAttr + ")" + endline);
				}
			} else if (type.isCollectionType()) {
				// TODO notNull, Collection initialisiert
				final TypeNode paramType = type.getParameterType(0, false);
				if (paramType == null)
					throw new TranspilerException("Transpiler Error: Cannot determine type parameter for the collection type " + type.transpile(false) + ".");
				sb.append(getIndent() + "if (!obj." + attribute.getName() + ") {" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "result += '\"" + attribute.getName() + "\" : []';" + System.lineSeparator());
				indentC--;
				sb.append(getIndent() + "} else {" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "result += '\"" + attribute.getName() + "\" : [ ';" + System.lineSeparator());
				sb.append(getIndent() + "for (let i = 0; i < " + objAttr + ".size(); i++) {" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "const elem = " + objAttr + ".get(i);" + System.lineSeparator());
				if (paramType.isString()) {
					if (paramType.isNotNull())
						sb.append(getIndent() + "result += " + "'\"' + elem + '\"';" + System.lineSeparator());
					else
						sb.append(getIndent() + "result += " + "(elem == null) ? null : '\"' + elem + '\"';" + System.lineSeparator());
				} else if (paramType.isNumberClass() || paramType.isBoolean()) {
					if (paramType.isNotNull())
						sb.append(getIndent() + "result += elem;" + System.lineSeparator());
					else
						sb.append(getIndent() + "result += (elem == null) ? null : elem;" + System.lineSeparator());
				} else
					sb.append(getIndent() + "result += " + paramType.transpile(true) + ".transpilerToJSON(elem);" + System.lineSeparator());
				sb.append(getIndent() + "if (i < " + objAttr + ".size() - 1)" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "result += ',';" + System.lineSeparator());
				indentC--;
				indentC--;
				sb.append(getIndent() + "}" + System.lineSeparator());
				sb.append(getIndent() + "result += ' ]'" + endline);
				indentC--;
				sb.append(getIndent() + "}" + System.lineSeparator());
			} else if (type.isArrayType()) {
				TypeNode contentType = type.getArrayContentType(transpiler);
				if (contentType == null)
					throw new TranspilerException("Transpiler Error: Cannot determine array content type of " + type.transpile(false) + " for JSON deserialization.");
				contentType = contentType.getNoDeclarationType();
				sb.append(getIndent() + "const a = " + objAttr + ";" + System.lineSeparator());
				sb.append(getIndent() + "if (!a) {" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "result += '\"" + attribute.getName() + "\" : []';" + System.lineSeparator());
				indentC--;
				sb.append(getIndent() + "} else {" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "result += '\"" + attribute.getName() + "\" : [ ';" + System.lineSeparator());
				sb.append(getIndent() + "for (let i = 0; i < a.length; i++) {" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "const elem = a[i];" + System.lineSeparator());
				if (contentType.isString()) {
					if (contentType.isNotNull())
						sb.append(getIndent() + "result += " + "'\"' + elem + '\"';" + System.lineSeparator());
					else
						sb.append(getIndent() + "result += " + "(elem == null) ? null : '\"' + elem + '\"';" + System.lineSeparator());
				} else if (contentType.isNumberClass() || contentType.isBoolean()) {
					if (contentType.isNotNull())
						sb.append(getIndent() + "result += elem;" + System.lineSeparator());
					else
						sb.append(getIndent() + "result += (elem == null) ? null : elem;" + System.lineSeparator());
				} else if (contentType.isPrimitive()) {
					sb.append(getIndent() + "result += JSON.stringify(elem);" + System.lineSeparator());
				} else {
					if (contentType.isNotNull())
						sb.append(getIndent() + "result += " + contentType.transpile(true) + ".transpilerToJSON(elem);" + System.lineSeparator());
					else
						sb.append(getIndent() + "result += (elem == null) ? null : " + contentType.transpile(true) + ".transpilerToJSON(elem);" + System.lineSeparator());
				}
				sb.append(getIndent() + "if (i < a.length - 1)" + System.lineSeparator());
				indentC++;
				sb.append(getIndent() + "result += ',';" + System.lineSeparator());
				indentC--;
				indentC--;
				sb.append(getIndent() + "}" + System.lineSeparator());
				sb.append(getIndent() + "result += ' ]'" + endline);
				indentC--;
				sb.append(getIndent() + "}" + System.lineSeparator());
			} else {
				if (type.isNotNull()) {
					sb.append(getIndent() + addAttrName + " + " + type.transpile(true) + ".transpilerToJSON(" + objAttr + ")" + endline);
				} else {
					sb.append(getIndent() + addAttrName + " + ((!" + objAttr + ") ? 'null' : " + type.getNoDeclarationType().transpile(true) + ".transpilerToJSON(" + objAttr + "))" + endline);
				}
			}
			indentC--;
			sb.append(getIndent() + "}" + System.lineSeparator());
		}
		sb.append(getIndent() + "result = result.slice(0, -1);" + System.lineSeparator());
		sb.append(getIndent() + "result += '}';" + System.lineSeparator());
		sb.append(getIndent() + "return result;" + System.lineSeparator());
		indentC--;
		sb.append(getIndent() + "}" + System.lineSeparator());
		sb.append(System.lineSeparator());
		return sb.toString();
	}


	/**
	 * Transpiles the class.
	 *
	 * @param sb       the StringBuilder where the output is written to
	 * @param node     the class to be transpiled
	 */
	public void transpileClass(final StringBuilder sb, final ClassTree node) {
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
			final TypeNode typeNode = new TypeNode(this, node.getExtendsClause(), false, false);
			sb.append(typeNode.transpile(false));
		}
		final List<? extends Tree> implClause = node.getImplementsClause();
		if (!implClause.isEmpty()) {
			sb.append(" implements ");
			for (int i = 0; i < implClause.size(); i++) {
				final TypeNode typeNode = new TypeNode(this, implClause.get(i), false, false);
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
		for (final VariableTree attribute : transpiler.getAttributes(node)) {
			sb.append(convertAttribute(attribute, null));
			sb.append(System.lineSeparator());
		}
		sb.append(System.lineSeparator());

		// Generate Constructors
		final List<MethodNode> constructors = Transpiler.getConstructors(node).stream()
			.map(method -> new MethodNode(this, node, method, getIndent()))
			.toList();
		if (constructors.size() == 1) {
			constructors.get(0).print(sb, "" + node.getSimpleName());
			sb.append(System.lineSeparator());
		} else {
			MethodNode.setCommonAccessModifier(constructors);
			for (final MethodNode constructor : constructors) {
				constructor.printHead(sb);
				sb.append(System.lineSeparator());
			}
			// TODO handle super constructor calls, especially those with different parameters... is that transpilable ???
			MethodNode.printImplementation(sb, getIndent(), constructors, "" + node.getSimpleName());
			sb.append(System.lineSeparator());
		}

		// Generate Methods
		final List<MethodNode> methods = Transpiler.getMethods(node).stream()
			.map(method -> new MethodNode(this, node, method, getIndent()))
			.toList();
		final Map<String, List<MethodNode>> mapMethods = methods.stream().collect(Collectors.groupingBy(MethodNode::getName));
		for (final MethodNode method : methods) {
			final String methodName = method.getName();
			final List<MethodNode> methodList = mapMethods.get(methodName);
			if (methodList == null)
				continue;
			if (mapMethods.get(methodName).size() == 1) {
				method.print(sb, "" + node.getSimpleName());
				sb.append(System.lineSeparator());
			} else {
				MethodNode.setCommonAccessModifier(methodList);
				mapMethods.remove(methodName);
				for (final MethodNode m : methodList) {
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

		final TranspilerUnit unit = transpiler.getTranspilerUnit(node);
		if (unit.superTypes.contains("java.util.Deque")) {
			sb.append(getIndent()).append("public reversed() : Deque<E> {").append(System.lineSeparator());
			sb.append(getIndent()).append("\tthrow new UnsupportedOperationException(\"Der Transpiler unterstützt diese Methode noch nicht.\");").append(System.lineSeparator());
			sb.append(getIndent()).append("}").append(System.lineSeparator());
			sb.append(System.lineSeparator());
		}
		if (unit.superTypes.contains("java.util.NavigableSet")) {
			String strType = "E";
			if (transpiler.getElement(node) instanceof final TypeElement te) {
				for (final TypeMirror tm: te.getInterfaces()) {
					if ((tm instanceof final DeclaredType dt) && (dt.asElement() instanceof final TypeElement ste)
							&& ("java.util.NavigableSet".equals(ste.getQualifiedName().toString()))
							&& (dt.getTypeArguments().get(0) instanceof final TypeVariable tv)) {
						final String[] parts = tv.toString().split(" ");
						strType = parts[parts.length - 1];
					}
				}
			}
			sb.append(getIndent()).append("public reversed() : NavigableSet<" + strType + "> {").append(System.lineSeparator());
			sb.append(getIndent()).append("\treturn this.descendingSet();").append(System.lineSeparator());
			sb.append(getIndent()).append("}").append(System.lineSeparator());
			sb.append(System.lineSeparator());
		}
		if (unit.superTypes.contains("java.util.NavigableMap")) {
			sb.append(getIndent()).append("public reversed() : NavigableMap<K, V> {").append(System.lineSeparator());
			sb.append(getIndent()).append("\treturn this.descendingMap();").append(System.lineSeparator());
			sb.append(getIndent()).append("}").append(System.lineSeparator());
			sb.append(System.lineSeparator());
		}
		if (unit.superTypes.contains("java.util.Map")) { // TODO check only if the class directly implements Map and not indirectly
			// TODO determine the name of both type parameter and replace K and V in the following code...
			sb.append(getIndent()).append("public computeIfAbsent(key : K, mappingFunction: JavaFunction<K, V> ) : V | null {").append(System.lineSeparator());
			sb.append(getIndent()).append("\tconst v : V | null = this.get(key);").append(System.lineSeparator());
			sb.append(getIndent()).append("\tif (v != null)").append(System.lineSeparator());
			sb.append(getIndent()).append("\t\treturn v;").append(System.lineSeparator());
			sb.append(getIndent()).append("\tconst newValue : V = mappingFunction.apply(key);").append(System.lineSeparator());
			sb.append(getIndent()).append("\tif (newValue == null)").append(System.lineSeparator());
			sb.append(getIndent()).append("\t\treturn null;").append(System.lineSeparator());
			sb.append(getIndent()).append("\tthis.put(key, newValue)").append(System.lineSeparator());
			sb.append(getIndent()).append("\treturn newValue;").append(System.lineSeparator());
			sb.append(getIndent()).append("}").append(System.lineSeparator());
			sb.append(System.lineSeparator());
		}
		if ((unit.superTypes.contains("java.lang.Iterable")) && (!unit.superTypes.contains("java.util.AbstractCollection") && (!unit.superTypes.contains("java.util.AbstractList")))) {
			// TODO Get the type parameter that was passed through inheritance to the Iterable<...> interface
			final TypeMirror type = unit.getIterableTypeArgument();
			if (type == null)
				throw new TranspilerException("Transpiler Error: cannot determine iterable type");
			String typeParam;
			if (type instanceof final TypeVariable tv) {
				typeParam = tv.asElement().getSimpleName() + ((type.getAnnotation(NotNull.class) == null) ? "" : " | null");
			} else if (type instanceof final DeclaredType dt) {
				if ("java.util.Map.Entry".equals(dt.asElement().toString()))
					typeParam = "JavaMapEntry<any, any>";
				else
					throw new TranspilerException("Transpiler Error: Iterable types of Kind " + type.getKind() + " not yet fully supported");
			} else {
				throw new TranspilerException("Transpiler Error: Iterable types of Kind " + type.getKind() + " not yet supported");
			}
			sb.append(getIndent()).append("public [Symbol.iterator](): Iterator<").append(typeParam).append("> {").append(System.lineSeparator());
			sb.append(getIndent()).append("\tconst iter : JavaIterator<").append(typeParam).append("> = this.iterator();").append(System.lineSeparator());
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
	public void transpileEnum(final StringBuilder sb, final ClassTree node) {
		// TODO class comment
		sb.append(getIndent());
		sb.append("export ");
		sb.append("class ");
		sb.append(node.getSimpleName());
		sb.append(" extends JavaEnum<");
		sb.append(node.getSimpleName());
		sb.append("> {");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());

		// Generate Attributes
		indentC++;
		sb.append(getIndent()).append("/** an array containing all values of this enumeration */").append(System.lineSeparator());
		sb.append(getIndent()).append("static readonly all_values_by_ordinal : Array<").append(node.getSimpleName()).append("> = [];").append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append(getIndent()).append("/** an array containing all values of this enumeration indexed by their name*/").append(System.lineSeparator());
		sb.append(getIndent()).append("static readonly all_values_by_name : Map<string, ").append(node.getSimpleName()).append("> = new Map<string, ").append(node.getSimpleName()).append(">();").append(System.lineSeparator());
		sb.append(System.lineSeparator());

		for (final VariableTree attribute : transpiler.getAttributes(node)) {
			sb.append(convertAttribute(attribute, "" + node.getSimpleName()));
			sb.append(System.lineSeparator());
		}

		// Generate Constructors
		final List<MethodNode> constructors = Transpiler.getConstructors(node).stream()
				.map(method -> {
					final MethodNode methodeNode = new MethodNode(this, node, method, getIndent());
					methodeNode.setAccessModifier("private"); // ensure that the constructors are private
					return methodeNode;
				})
				.toList();
		if (constructors.size() == 1) {
			constructors.get(0).print(sb, "" + node.getSimpleName());
			sb.append(System.lineSeparator());
		} else {
			for (final MethodNode constructor : constructors) {
				constructor.printHead(sb);
				sb.append(System.lineSeparator());
			}
			MethodNode.printImplementation(sb, getIndent(), constructors, "" + node.getSimpleName());
			sb.append(System.lineSeparator());
		}

		// Generate Methods
		final List<MethodNode> methods = Transpiler.getMethods(node).stream()
				.map(method -> new MethodNode(this, node, method, getIndent()))
				.toList();
		final Map<String, List<MethodNode>> mapMethods = methods.stream().collect(Collectors.groupingBy(MethodNode::getName));
		for (final MethodNode method : methods) {
			final String methodName = method.getName();
			final List<MethodNode> methodList = mapMethods.get(methodName);
			if (methodList == null)
				continue;
			if (mapMethods.get(methodName).size() == 1) {
				method.print(sb, "" + node.getSimpleName());
				sb.append(System.lineSeparator());
			} else {
				mapMethods.remove(methodName);
				for (final MethodNode m : methodList) {
					m.printHead(sb);
					sb.append(System.lineSeparator());
				}
				MethodNode.printImplementation(sb, getIndent(), methodList, "" + node.getSimpleName());
				sb.append(System.lineSeparator());
			}
		}

		sb.append(
			"""
			%1$s/**
			%1$s * Returns an array with enumeration values.
			%1$s *
			%1$s * @returns the array with enumeration values
			%1$s */
			%1$spublic static values() : Array<%2$s> {
			%1$s\treturn [...this.all_values_by_ordinal];
			%1$s}
			""".formatted(getIndent(), node.getSimpleName())
		).append(System.lineSeparator());

		sb.append(
			"""
			%1$s/**
			%1$s * Returns the enumeration value with the specified name.
			%1$s *
			%1$s * @param name   the name of the enumeration value
			%1$s *
			%1$s * @returns the enumeration values or null
			%1$s */
			%1$spublic static valueOf(name : string) : %2$s | null {
			%1$s\tconst tmp = this.all_values_by_name.get(name);
			%1$s\treturn (!tmp) ? null : tmp;
			%1$s}
			""".formatted(getIndent(), node.getSimpleName())
		).append(System.lineSeparator());

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
	public static String getImportName(final String className, final String packageName) {
		return switch (packageName) {
			case "java.lang" -> switch (className) {
				case strObject -> "JavaObject";
				case strBoolean -> "JavaBoolean";
				case strByte -> "JavaByte";
				case strShort -> "JavaShort";
				case strInteger -> "JavaInteger";
				case strLong -> "JavaLong";
				case strFloat -> "JavaFloat";
				case strDouble -> "JavaDouble";
				case "Enum" -> "JavaEnum";
				default -> className;
			};
			case "java.util" -> switch (className) {
				case strMap -> "JavaMap";
				case "Map.Entry" -> "JavaMapEntry";
				case strSet -> "JavaSet";
				case strIterator -> "JavaIterator";
				default -> className;
			};
			case strJavaMapFQ -> switch (className) {
				case strEntry -> "JavaMapEntry";
				default -> className;
			};
			case "java.util.function" -> switch (className) {
				case strFunction -> "JavaFunction";
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
	public static String getImportPackageName(final String className, final String packageName) {
		return switch (packageName) {
			case "java.lang" -> switch (className) {
				case "Enum" -> "java.lang";
				case strObject -> "java.lang";
				default -> packageName;
			};
			case "java.util" -> switch (className) {
				case strMap -> "java.util";
				case "Map.Entry" -> "java.util";
				case strSet -> "java.util";
				case strIterator -> "java.util";
				default -> packageName;
			};
			case strJavaMapFQ -> switch (className) {
				case strEntry -> "java.util";
				default -> packageName;
			};
			default -> packageName;
		};
	}

	/** Ein Set mit allen im Transpiler umbenannten interfaces */
	public static final Set<String> renamedInterfaces = Set.of("java.lang.JavaIterable", "java.lang.TranspiledObject",
			"java.util.JavaIterator", "java.util.JavaMap", "java.util.JavaSet", "java.util.function.JavaFunction");

	/**
	 * Prints the import information of the transpiler unit
	 *
	 * @param unit   the transpiler unit containing the import information
	 * @param strIgnoreJavaPackagePrefix   the package prefix to be ignored
	 * @param body   the file body of the transpiled class
	 *
	 * @return the imports for the transpiled typescript class
	 */
	public String getImports(final TranspilerUnit unit, final String strIgnoreJavaPackagePrefix, final String body) {
		final String packageName = unit.getPackageName();
		final String shortPackageName = packageName.replace(strIgnoreJavaPackagePrefix + ".", "");
		final String importPathPrefix = "../".repeat((int) (shortPackageName.chars().filter(c -> c == '.').count()) + 1);
		final List<Map.Entry<String, String>> entries = unit.imports.entrySet().stream().collect(Collectors.toList());
		if (!unit.imports.containsKey(strObject))
			entries.add(0, new AbstractMap.SimpleEntry<>(strObject, "java.lang"));
		if ((unit.isEnum()) && (!unit.imports.containsKey("Enum")))
			entries.add(0, new AbstractMap.SimpleEntry<>("Enum", "java.lang"));
		if (unit.superTypes.contains("java.util.Deque"))
			entries.add(0, new AbstractMap.SimpleEntry<>("UnsupportedOperationException", "java.lang"));
		if (unit.superTypes.contains("java.util.Map")) {
			final String pkg = unit.imports.get("Function");
			if ((pkg == null) || (!"java.util.function".equals(pkg)))
				entries.add(0, new AbstractMap.SimpleEntry<>("Function", "java.util.function"));
		}
		final StringBuilder sb = new StringBuilder();
		for (final Map.Entry<String, String> entry : entries) {
			final String key = entry.getKey();
			final String value = entry.getValue();
			final String importCast = "cast_" + value.replace('.', '_') +  "_" + key.replace('.', '_');
			switch (value + "." + key) {
				case "java.lang.String", "java.lang.Long", "java.lang.Integer", "java.lang.Short", "java.lang.Byte", "java.lang.Float", "java.lang.Double", "java.lang.Boolean",
						"java.lang.Enum" -> {
					final String importName = "Java" + key;
					final String importLocation = importPathPrefix + "java/lang/Java" + key;
					final boolean hasClass = body.contains(importName);
					final boolean hasCast = body.contains(importCast);
					if (hasClass && hasCast) {
						sb.append("import { Java%s, %s } from '%s';".formatted(key, importCast, importLocation));
						sb.append(System.lineSeparator());
					} else if (hasClass) {
						sb.append("import { Java%s } from '%s';".formatted(key, importLocation));
						sb.append(System.lineSeparator());
					} else if (hasCast) {
						sb.append("import { %s } from '%s';".formatted(importCast, importLocation));
						sb.append(System.lineSeparator());
					}
				}
				case "java.lang.reflect.Array" -> { /**/ }
				case "java.lang.Character" -> { /**/ }
				case "java.lang.Math" -> { /**/ }
				case "java.io.PrintStream" -> { /**/ }
				default -> {
					final String importName = getImportName(key, value);
					final String importPackage = getImportPackageName(key, value).replace(strIgnoreJavaPackagePrefix + ".", "");
					final String importPath = importPathPrefix + importPackage.replace('.', '/') + "/";
					final boolean hasClass = body.contains(importName);
					final boolean hasCast = body.contains(importCast);

					final TypeElement elem = transpiler.getTypeElement(value + "." + key);
					boolean isImportType = (elem.getKind() == ElementKind.INTERFACE);
					if (renamedInterfaces.contains(importPackage + "." + importName))
						isImportType = true;
					if (hasClass && hasCast && !isImportType) {
						sb.append("import { %s, %s } from '%s';".formatted(importName, importCast, importPath + importName));
						sb.append(System.lineSeparator());
					} else {
						if (hasClass) {
							final String strImport = isImportType ? "import type" : "import";
							sb.append("%s { %s } from '%s';".formatted(strImport, importName, importPath + importName));
							sb.append(System.lineSeparator());
						}
						if (hasCast) {
							sb.append("import { %s } from '%s';".formatted(importCast, importPath + importName));
							sb.append(System.lineSeparator());
						}
					}
				}
			}
		}
		sb.append(System.lineSeparator());
		return sb.toString();
	}


	@Override
	public void transpile() {
		// generate code for all classes
		System.out.println("Running TypeScript-Transpiler...");
		final List<ClassTree> allClasses = transpiler.getAllClasses();
		for (final ClassTree classTree : allClasses) {
			final String fileName = transpiler.getFullClassName(classTree).replace(strIgnoreJavaPackagePrefix + ".", "").replace('.', '/') + ".ts";
			System.out.println("  -> " + fileName);
			final Path path = Paths.get(outputDir + "/" + fileName);
			super.outputFiles.add(fileName);
			super.outputFilesTypeOnly.add(classTree.getKind() == Tree.Kind.INTERFACE);
			try {
				Files.createDirectories(path.getParent());
				final TranspilerUnit transpilerUnit = transpiler.getTranspilerUnit(classTree);
				final StringBuilder sb = new StringBuilder();
				if (classTree.getKind() == Tree.Kind.CLASS) {
					transpileClass(sb, classTree);
				} else if (classTree.getKind() == Tree.Kind.ENUM) {
					transpileEnum(sb, classTree);
				}
				final String data = sb.toString();
				final String imports = getImports(transpilerUnit, strIgnoreJavaPackagePrefix, data);
				Files.writeString(path, imports + data, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
			} catch (@SuppressWarnings("unused") final IOException e) {
				throw new TranspilerException("Transpiler Error: Cannot write output file " + path.toString());
			}
		}

		// write typescript resources
		System.out.println("Writing prepared TypeScript resources...");
		for (final TranspilerResource res : tsResources) {
			// remove package prefix "typescript." from package path and replace all dots by slashes
			final String packageName = res.packageName.substring(11);
			final String fileName = packageName.replace(".", "/") + "/" + res.className + res.extension;
			System.out.println("  -> " + fileName);
			final Path path = Paths.get(outputDir + "/" + fileName);
			super.outputFiles.add(fileName);
			final TypeElement elem = transpiler.getTypeElement(packageName + "." + res.className);
			super.outputFilesTypeOnly.add((elem != null) && (elem.getKind() == ElementKind.INTERFACE));
			try {
				Files.createDirectories(path.getParent());
				Files.writeString(path, res.data, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
			} catch (@SuppressWarnings("unused") final IOException e) {
				throw new TranspilerException("Transpiler Error: Cannot write output file " + path.toString());
			}
		}
	}

	// TODO special handling of String class
	// TODO special handling of System class
	// TODO special handling of Arrays class

}
