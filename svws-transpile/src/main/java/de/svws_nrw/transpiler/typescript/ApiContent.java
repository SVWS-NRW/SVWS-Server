package de.svws_nrw.transpiler.typescript;

import java.util.HashMap;
import java.util.Map;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.Tree.Kind;

import de.svws_nrw.transpiler.Transpiler;
import de.svws_nrw.transpiler.TranspilerException;
import de.svws_nrw.transpiler.TranspilerUnit;

/**
 * Diese Klasse repräsentiert einen möglichen Content für einen HTTP-Request oder
 * eine HTTP-Response bei einer OpenAPI-Methode.
 */
public class ApiContent {

	/** Der Mime-Type */
	public final ApiMimeType mimetype;

	/** Der Typescript-Datentyp */
	public final String datatype;

	/** Gibt an, ob der Inhalt ein JSON-Array-basierter Datentyp ist */
	public final boolean isArrayType;

	/** Der Typescript-Datentyp */
	public final String arrayElementType;

	/** a non final temporary attribute used for the determining the final value */
	private String tmpArrayElementType = null;

	/** Ein Vektor mit den benötigten Klassen bei den Typescript-Importen */
	public final HashMap<String, String> importsRequired = new HashMap<>();


	/**
	 * Erstellt anhand der übergebenen Annotation und mithilfe des übergebenen Transpilers ein
	 * neues {@link ApiContent}-Objekt.
	 *
	 * @param transpiler   der Transpiler
	 * @param annotation   das {@link AnnotationTree} für die {@link io.swagger.v3.oas.annotations.media.Content}-Annotation
	 */
	ApiContent(final Transpiler transpiler, final AnnotationTree annotation) {
		final Map<String, ExpressionTree> args = transpiler.getArguments(annotation);
		mimetype = determineMimetype(args);
		if (mimetype == ApiMimeType.MULTIPART_FORM_DATA) {
			datatype = "FormData";
			isArrayType = false;
			arrayElementType = null;
		} else {
			datatype = determineImplementationType(transpiler, args);
			isArrayType = tmpArrayElementType != null;
			arrayElementType = tmpArrayElementType;
		}
	}

	private static ApiMimeType determineMimetype(final Map<String, ExpressionTree> args) {
		final ExpressionTree value = args.get("mediaType");
        if (value == null) {
            throw new TranspilerException("Transpiler Exception: Missing mediaType value for @Content annotation.");
        }
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof final LiteralTree literal) && (literal.getValue() instanceof final String str))
			return ApiMimeType.get(str);
		if (value.getKind() == Kind.MEMBER_SELECT)
			return ApiMimeType.get(value.toString());
		throw new TranspilerException("Transpiler Exception: Unhandled mediaType argument for Content annotation.");
	}

	private String determineImplementationType(final Transpiler transpiler, final Map<String, ExpressionTree> args) {
		ExpressionTree value = args.get("array");
		if (value != null) {
			if (value instanceof final AnnotationTree att) {
				if (!transpiler.isAnnotationType("io.swagger.v3.oas.annotations.media.ArraySchema", att))
					throw new TranspilerException("Transpiler Exception: Unhandled annotation type used in Content annotation.");
				final Map<String, ExpressionTree> arrayArgs = transpiler.getArguments(att);
				importsRequired.put("List", "java.util");
				importsRequired.put("ArrayList", "java.util");
				tmpArrayElementType = determineImplementationType(transpiler, arrayArgs);
				return "List<" + tmpArrayElementType + ">";
			}
			throw new TranspilerException("Transpiler Exception: Unhandled array argument for Content annotation.");
		}

		value = args.get("schema");
		if (value != null) {
			if (value instanceof final AnnotationTree att) {
				if (!transpiler.isAnnotationType("io.swagger.v3.oas.annotations.media.Schema", att))
					throw new TranspilerException("Transpiler Exception: Unhandled annotation type used in Content annotation.");
				final Map<String, ExpressionTree> schemaArgs = transpiler.getArguments(att);
				ExpressionTree expr = schemaArgs.get("implementation");
				if (expr instanceof final MemberSelectTree mst) {
					if (!"class".equals(mst.getIdentifier().toString()))
						throw new TranspilerException("Transpiler Exception: Unhandled member select for implementation argument of Schema annotation.");
					final String implType = mst.getExpression().toString();
					final TranspilerUnit tu = transpiler.getTranspilerUnit(mst);
					importsRequired.put(implType, tu.imports.get(implType));
					return implType;
				}
				expr = schemaArgs.get("format");
				if (expr instanceof final LiteralTree lt) {
					if ((lt.getKind() == Kind.STRING_LITERAL) && "binary".equals(lt.getValue()))
						return "ApiFile";
				}
				throw new TranspilerException("Transpiler Exception: Unhandled implementation argument for Schema annotation.");
			}
			throw new TranspilerException("Transpiler Exception: Unhandled schema argument for Content annotation.");
		}
		throw new TranspilerException("Transpiler Exception: Unhandled implementation argument for Content annotation.");
	}

}
