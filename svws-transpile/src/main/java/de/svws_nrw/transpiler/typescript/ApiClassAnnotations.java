package de.svws_nrw.transpiler.typescript;

import java.util.Map;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.Tree.Kind;

import de.svws_nrw.transpiler.Transpiler;
import de.svws_nrw.transpiler.TranspilerException;

/**
 * Diese Klasse analysiert den Java-Code einer OpenAPI-Klasse und stellt die Informationen
 * für den Transpiler zur Verfügung.
 */
public class ApiClassAnnotations {

	/** Der Name der API */
	public final String tag;

	/** Der Pfad, der als Basis-Pfad für alle Methoden dieser API-Klasse dient. */
	public final String path;

	/** Der Mime-Type, welcher für alle Ergebnisse der API-Methoden als Standard gilt, sofern dort kein anderer Wert gesetzt wird.*/
	public final ApiMimeType produces;

	/** Der Mime-Type, welcher für den Input der API-Methoden als Standard gilt, sofern dort kein anderer Wert gesetzt wird.*/
	public final ApiMimeType consumes;


	/**
	 * Erstellt eine neue Übersicht über die Klassen-Annoationen für das Erstellen der
	 * TypeScript-Client-API
	 *
	 * @param transpiler   der für die Analyse zu verwendende Transpiler
	 * @param classTree    die zu analysierende Java-API-Klasse
	 */
	public ApiClassAnnotations(final Transpiler transpiler, final ClassTree classTree) {
		this.tag = ApiClassAnnotations.getTag(transpiler, classTree);
		this.path = ApiClassAnnotations.getPath(transpiler, classTree);
		final ApiMimeType tmpProduces = ApiClassAnnotations.getMimeType(transpiler, classTree, "jakarta.ws.rs.Produces");
		if (tmpProduces == null) // set JSON as class default mime type for Produces and Consumes, if no mime type is specified
			this.produces = ApiMimeType.APPLICATION_JSON;
		else
			this.produces = tmpProduces;
		final ApiMimeType tmpConsumes = ApiClassAnnotations.getMimeType(transpiler, classTree, "jakarta.ws.rs.Consumes");
		if (tmpConsumes == null) // set JSON as class default mime type for Produces and Consumes, if no mime type is specified
			this.consumes = ApiMimeType.APPLICATION_JSON;
		else
			this.consumes = tmpConsumes;
	}


	private static String getTag(final Transpiler transpiler, final ClassTree classTree) {
		final AnnotationTree annotationTag = transpiler.getAnnotation("io.swagger.v3.oas.annotations.tags.Tag", classTree);
		if (annotationTag == null)
			throw new TranspilerException("Transpiler Exception: Missing Tag annotation for class " + classTree.getSimpleName().toString() + ".");
		final Map<String, ExpressionTree> args = transpiler.getArguments(annotationTag);
		final ExpressionTree name = args.get("name");
        if (name == null) {
            throw new TranspilerException("Transpiler Exception: Missing name value for @Tag annotation.");
        }
		if ((name.getKind() == Kind.STRING_LITERAL) && (name instanceof final LiteralTree literal) && (literal.getValue() instanceof final String tag))
			return tag;
		throw new TranspilerException("Transpiler Exception: Unhandled name argument for Tag annotation.");
	}


	private static String getPath(final Transpiler transpiler, final ClassTree classTree) {
		final AnnotationTree annotationPath = transpiler.getAnnotation("jakarta.ws.rs.Path", classTree);
		if (annotationPath == null)
			throw new TranspilerException("Transpiler Exception: Missing Path annotation for class " + classTree.getSimpleName().toString() + ".");
		final Map<String, ExpressionTree> args = transpiler.getArguments(annotationPath);
		final ExpressionTree value = args.get("value");
        if (value == null) {
            throw new TranspilerException("Transpiler Exception: Missing value value for @Path annotation.");
        }
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof final LiteralTree literal) && (literal.getValue() instanceof final String path))
			return path;
		throw new TranspilerException("Transpiler Exception: Unhandled value argument for Path annotation.");
	}


	/**
	 * Bestimmt den Mime-Type, welche durch die angebenen Annotation spezifiziert wurde.
	 *
	 * @param transpiler   der für die Analyse zu verwendende Transpiler
	 * @param classTree    die Java-API-Klasse
	 * @param annotation   die Annotation
	 *
	 * @return der Mime-Type oder null, falls ie Annotation keinen Mime-Type spezifiziert
	 */
	private static ApiMimeType getMimeType(final Transpiler transpiler, final ClassTree classTree, final String annotation) {
		final AnnotationTree annotationTree = transpiler.getAnnotation(annotation, classTree);
		if (annotationTree == null)
			return null;
		final Map<String, ExpressionTree> args = transpiler.getArguments(annotationTree);
		final ExpressionTree value = args.get("value");
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof final LiteralTree literal) && (literal.getValue() instanceof final String str))
			return ApiMimeType.get(str);
		if (value.getKind() == Kind.MEMBER_SELECT)
			return ApiMimeType.get(value.toString());
		throw new TranspilerException("Transpiler Exception: Unhandled value argument for Consumes annotation of kind " + value.getKind() + ".");
	}

}
