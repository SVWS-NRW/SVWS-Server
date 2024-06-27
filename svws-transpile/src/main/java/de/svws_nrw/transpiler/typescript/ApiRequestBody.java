package de.svws_nrw.transpiler.typescript;

import java.util.List;
import java.util.Map;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree.Kind;

import de.svws_nrw.transpiler.Transpiler;
import de.svws_nrw.transpiler.TranspilerException;

import com.sun.source.tree.VariableTree;

/**
 * Diese Klasse repräsentiert den Request-Body einer OpenAPI-Methode.
 */
public class ApiRequestBody {

	/** Gibt an, ob ein Request-Body bei der zugehörigen API-Methode existiert oder nicht*/
	public final boolean exists;

	/** Die Beschreibung der HTTP-Response */
	public final String description;

	/** Gibt an, ob der RequestBody benötigt wird oder null sein kann */
	public final boolean required;

	/** Der Typ für den Request-Body oder null */
	public final ApiContent content;

	/**
	 * Erstellt anhand der übergebenen Annotation und mithilfe des übergebenen Transpilers ein
	 * neues {@link ApiRequestBody}-Objekt.
	 *
	 * @param transpiler   der Transpiler
	 * @param method       die API-Methode
	 */
	ApiRequestBody(final Transpiler transpiler, final MethodTree method) {
		// Ermittle die Request-Body Annotation
		final List<? extends VariableTree> params = method.getParameters();
		AnnotationTree annotation = null;
		for (final VariableTree param : params) {
			final AnnotationTree tmp = transpiler.getAnnotation("io.swagger.v3.oas.annotations.parameters.RequestBody", param);
			if (tmp != null) {
				annotation = tmp;
				break;
			}
		}

		// Existiert ein Request-Body? Wenn nicht, dann belege die Attribute mit Default-Werten
		this.exists = (annotation != null);
		if (!exists) {
			this.description = "";
			this.required = false;
			this.content = null;
			return;
		}

		// Interpretiere die Parameter der Annotation
		final Map<String, ExpressionTree> args = transpiler.getArguments(annotation);
		description = determineDescription(args);
		required = determineRequired(args);
		final AnnotationTree annotationContent = determineContentAnnotation(transpiler, args);
		if (annotationContent == null) {
			final String className = transpiler.getClass(method).getSimpleName().toString();
			final String methodName = method.getName().toString();
			throw new TranspilerException("Transpiler Exception: Transpiler requires content annotation for ApiResponse annotation (method " + methodName
					+ " in class " + className + ").");
		}
		content = new ApiContent(transpiler, annotationContent);
	}

	private static String determineDescription(final Map<String, ExpressionTree> args) {
		final ExpressionTree value = args.get("description");
		if (value == null)
			throw new TranspilerException("Transpiler Exception: Missing description value for @RequestBody annotation.");
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof final LiteralTree literal) && (literal.getValue() instanceof final String str))
			return str;
		throw new TranspilerException("Transpiler Exception: Unhandled description argument for ApiResponse annotation.");
	}

	private static boolean determineRequired(final Map<String, ExpressionTree> args) {
		final ExpressionTree value = args.get("required");
		if ((value.getKind() == Kind.BOOLEAN_LITERAL) && (value instanceof final LiteralTree literal) && (literal.getValue() instanceof final Boolean bool))
			return bool;
		return false;
	}

	private static AnnotationTree determineContentAnnotation(final Transpiler transpiler, final Map<String, ExpressionTree> args) {
		final ExpressionTree value = args.get("content");
		if (value == null)
			return null;
		if (value instanceof final AnnotationTree att) {
			if (!transpiler.isAnnotationType("io.swagger.v3.oas.annotations.media.Content", att))
				throw new TranspilerException("Transpiler Exception: Unhandled annotation type used in ApiResponse annotation.");
			return att;
		}
		throw new TranspilerException("Transpiler Exception: Unhandled description argument for ApiResponse annotation.");
	}

}
