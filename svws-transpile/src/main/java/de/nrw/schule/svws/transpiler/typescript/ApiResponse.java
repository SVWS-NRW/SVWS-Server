package de.nrw.schule.svws.transpiler.typescript;

import java.util.Map;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.Tree.Kind;

import de.nrw.schule.svws.transpiler.Transpiler;
import de.nrw.schule.svws.transpiler.TranspilerException;

/**
 * Diese Klasse repräsentiert eine mögliche HTTP-Response für eine 
 * OpenAPI-Methode.
 */
public class ApiResponse {

	/** Der HTTP-Response-Code */
	public final int responseCode;
	
	/** Die Beschreibung der HTTP-Response */
	public final String description;
	
	/** Der Rückgabe-Typ für diese HTTP-Response oder null */
	public final ApiContent content;

	/**
	 * Erstellt anhand der übergebenen Annotation und mithilfe des übergebenen Transpilers ein
	 * neues {@link ApiResponse}-Objekt.
	 *   
	 * @param transpiler   der Transpiler
	 * @param annotation   das {@link AnnotationTree} für die {@link io.swagger.v3.oas.annotations.responses.ApiResponse}-Annotation
	 */
	ApiResponse(Transpiler transpiler, AnnotationTree annotation) {
		Map<String, ExpressionTree> args = transpiler.getArguments(annotation);
		responseCode = determineResponseCode(args);
		description = determineDescription(args);
		AnnotationTree annotationContent = determineContentAnnotation(transpiler, args);
		content = annotationContent == null ? null : new ApiContent(transpiler, annotationContent);
	}
	
	private static int determineResponseCode(Map<String, ExpressionTree> args) {
		ExpressionTree value = args.get("responseCode");
        if(value == null) {
            throw new TranspilerException("Transpiler Exception: Missing responseCode value for @Response annotation.");
        }
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof LiteralTree literal) && (literal.getValue() instanceof String str))
			return Integer.valueOf(str);
		throw new TranspilerException("Transpiler Exception: Unhandled responseCode argument for ApiResponse annotation.");		
	}

	private static String determineDescription(Map<String, ExpressionTree> args) {
		ExpressionTree value = args.get("description");
        if(value == null) {
            throw new TranspilerException("Transpiler Exception: Missing description value for @Response annotation.");
        }
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof LiteralTree literal) && (literal.getValue() instanceof String str))
			return str;
		throw new TranspilerException("Transpiler Exception: Unhandled description argument for ApiResponse annotation.");		
	}

	private static AnnotationTree determineContentAnnotation(Transpiler transpiler, Map<String, ExpressionTree> args) {
		ExpressionTree value = args.get("content");
		if (value == null)
			return null;
		if (value instanceof AnnotationTree att) {
			if (!transpiler.isAnnotationType("io.swagger.v3.oas.annotations.media.Content", att))
				throw new TranspilerException("Transpiler Exception: Unhandled annotation type used in ApiResponse annotation.");
			return att;
		}
		throw new TranspilerException("Transpiler Exception: Unhandled description argument for ApiResponse annotation.");		
	}
	
}
