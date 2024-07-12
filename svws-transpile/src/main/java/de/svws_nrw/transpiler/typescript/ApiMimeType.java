package de.svws_nrw.transpiler.typescript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewArrayTree;

import de.svws_nrw.transpiler.Transpiler;
import de.svws_nrw.transpiler.TranspilerException;
import jakarta.ws.rs.core.MediaType;

/**
 * Diese Aufzählung beinhaltet alle Mime-Types, die derzeit vom
 * Transpiler für die OpenAPI unterstützt werden.
 */
public enum ApiMimeType {

	/** Plain Text*/
	TEXT_PLAIN("MediaType.TEXT_PLAIN", "text/plain"),

	/** JSON */
	APPLICATION_JSON("MediaType.APPLICATION_JSON", "application/json"),

	/** Multipart Form Data */
	MULTIPART_FORM_DATA("MediaType.MULTIPART_FORM_DATA", "multipart/form-data"),

	/** Binary Data */
	APPLICATION_OCTET_STREAM("MediaType.APPLICATION_OCTET_STREAM", "application/octet-stream"),

	/** SQlite-Database */
	SQLITE("", "application/vnd.sqlite3"),

	/** PDF */
	PDF("", "application/pdf"),

	/** PDF */
	ZIP("", "application/zip");


	private final String mediaType;
	private final String mimetype;

	ApiMimeType(final String mediaType, final String mimetype) {
		this.mediaType = mediaType;
		this.mimetype = mimetype;
	}

	/**
	 * Ermittelt anhand der übergebenen Zeichenkette den Mime-Type
	 * aus dieser Aufzählung. Hierbei kann sowohl der MimeType übergeben
	 * werden oder auch die Konstante der Klasse {@link MediaType}.
	 *
	 * @param str   die Zeichenkette mit dem Mime-Type
	 *
	 * @return das Element dieser Aufzählung
	 */
	public static ApiMimeType get(final String str) {
		for (final ApiMimeType t : ApiMimeType.values()) {
			if (t.mediaType.equals(str) || t.mimetype.equals(str))
				return t;
		}
		throw new TranspilerException("Transpiler Error: Unsupported mime type.");
	}

	@Override
	public String toString() {
		return this.mimetype;
	}



	/**
	 * Bestimmt die Mime-Types, welche durch die angebene Annotation bei einer Klasse spezifiziert wurden.
	 *
	 * @param transpiler   der für die Analyse zu verwendende Transpiler
	 * @param classTree    die Java-API-Klasse
	 * @param annotation   die Annotation
	 *
	 * @return die Mime-Types oder eine leere Liste, falls die Annotation keine Mime-Types spezifiziert
	 */
	public static List<ApiMimeType> fromClassTree(final Transpiler transpiler, final ClassTree classTree, final String annotation) {
		return getListFromAnnotationTree(transpiler.getAnnotation(annotation, classTree));
	}

	/**
	 * Bestimmt die Mime-Types, welche durch die angebene Annotation bei einer Methode spezifiziert wurden.
	 *
	 * @param transpiler   der für die Analyse zu verwendende Transpiler
	 * @param methodTree   die Java-API-Methode
	 * @param annotation   die Annotation
	 *
	 * @return die Mime-Types oder eine leere Liste, falls die Annotation keine Mime-Types spezifiziert
	 */
	public static List<ApiMimeType> fromMethodTree(final Transpiler transpiler, final MethodTree methodTree, final String annotation) {
		return getListFromAnnotationTree(transpiler.getAnnotation(annotation, methodTree));
	}

	private static List<ApiMimeType> getListFromAnnotationTree(final AnnotationTree annotationTree) {
		final List<ApiMimeType> result = new ArrayList<>();
		if (annotationTree == null)
			return result;
		final Map<String, ExpressionTree> args = Transpiler.getArguments(annotationTree);
		return getListFromExpressionTree(args.get("value"));
	}


	private static List<ApiMimeType> getListFromExpressionTree(final ExpressionTree value) {
		final List<ApiMimeType> result = new ArrayList<>();
		return switch (value.getKind()) {
			case NEW_ARRAY -> {
				if (value instanceof final NewArrayTree nat) {
					for (final ExpressionTree et : nat.getInitializers())
						result.addAll(getListFromExpressionTree(et));
					yield result;
				}
				throw new TranspilerException("Transpiler Exception: Unerwarteter interner Transpiler-Fehler.");
			}
			case STRING_LITERAL -> {
				if ((value instanceof final LiteralTree literal) && (literal.getValue() instanceof final String str)) {
					result.add(ApiMimeType.get(str));
					yield result;
				}
				throw new TranspilerException("Transpiler Exception: Unerwarteter interner Transpiler-Fehler.");
			}
			case MEMBER_SELECT -> {
				result.add(ApiMimeType.get(value.toString()));
				yield result;
			}
			default -> throw new TranspilerException("Transpiler Exception: Unhandled value argument for Consumes annotation of kind " + value.getKind() + ".");
		};
	}

}
