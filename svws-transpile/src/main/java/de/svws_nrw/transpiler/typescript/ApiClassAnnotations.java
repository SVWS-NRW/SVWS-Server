package de.svws_nrw.transpiler.typescript;

import java.util.List;
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
	public final List<ApiMimeType> produces;

	/** Der Mime-Type, welcher für den Input der API-Methoden als Standard gilt, sofern dort kein anderer Wert gesetzt wird.*/
	public final List<ApiMimeType> consumes;


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
		final List<ApiMimeType> tmpProduces = ApiMimeType.fromClassTree(transpiler, classTree, "jakarta.ws.rs.Produces");
		if (tmpProduces.isEmpty()) // set JSON as class default mime type for Produces and Consumes, if no mime type is specified
			this.produces = List.of(ApiMimeType.APPLICATION_JSON);
		else
			this.produces = tmpProduces;
		final List<ApiMimeType> tmpConsumes = ApiMimeType.fromClassTree(transpiler, classTree, "jakarta.ws.rs.Consumes");
		if (tmpConsumes.isEmpty()) // set JSON as class default mime type for Produces and Consumes, if no mime type is specified
			this.consumes = List.of(ApiMimeType.APPLICATION_JSON);
		else
			this.consumes = tmpConsumes;
	}


	private static String getTag(final Transpiler transpiler, final ClassTree classTree) {
		final AnnotationTree annotationTag = transpiler.getAnnotation("io.swagger.v3.oas.annotations.tags.Tag", classTree);
		if (annotationTag == null)
			throw new TranspilerException("Transpiler Exception: Missing Tag annotation for class " + classTree.getSimpleName().toString() + ".");
		final Map<String, ExpressionTree> args = Transpiler.getArguments(annotationTag);
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
		final Map<String, ExpressionTree> args = Transpiler.getArguments(annotationPath);
		final ExpressionTree value = args.get("value");
		if (value == null) {
			throw new TranspilerException("Transpiler Exception: Missing value value for @Path annotation.");
		}
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof final LiteralTree literal) && (literal.getValue() instanceof final String path))
			return path;
		throw new TranspilerException("Transpiler Exception: Unhandled value argument for Path annotation.");
	}

}
