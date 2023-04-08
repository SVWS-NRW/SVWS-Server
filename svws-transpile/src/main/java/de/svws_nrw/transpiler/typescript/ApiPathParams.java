package de.svws_nrw.transpiler.typescript;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;

import de.svws_nrw.transpiler.Transpiler;
import de.svws_nrw.transpiler.TranspilerException;
import de.svws_nrw.transpiler.TranspilerUnit;

import com.sun.source.tree.VariableTree;

/**
 * Diese Klasse repräsentiert den Request-Body einer OpenAPI-Methode.
 */
public class ApiPathParams {

	/** Ein Vektor mit den benötigten Klassen bei den Typescript-Importen */
	public final HashMap<String, String> importsRequired = new HashMap<>();

	/** Das Mapping zwischen dem Namen eines Pfad-Parameters auf den TS-Typ des Pfad-Parameters */
	public final ArrayList<Map.Entry<String, String>> params = new ArrayList<>();

	/**
	 * Erstellt anhand der übergebenen Annotation und mithilfe des übergebenen Transpilers ein
	 * neues {@link ApiPathParams}-Objekt.
	 *
	 * @param transpiler   der Transpiler
	 * @param method       die API-Methode
	 */
	ApiPathParams(final Transpiler transpiler, final MethodTree method) {
		// Ermittle die Request-Body Annotation
		final List<? extends VariableTree> params = method.getParameters();
		for (final VariableTree param : params) {
			final AnnotationTree annotation = transpiler.getAnnotation("jakarta.ws.rs.PathParam", param);
			if (annotation != null) {
				final String name = determineValue(transpiler, annotation);
				final Tree type = param.getType();
				if (type instanceof final PrimitiveTypeTree ptt) {
					switch (ptt.getPrimitiveTypeKind()) {
				        case BYTE, SHORT, INT, LONG, FLOAT, DOUBLE -> this.params.add(new AbstractMap.SimpleEntry<>(name, "number"));
				        case BOOLEAN -> this.params.add(new AbstractMap.SimpleEntry<>(name, "boolean"));
				        case CHAR -> this.params.add(new AbstractMap.SimpleEntry<>(name, "string"));
				        default -> throw new TranspilerException("Transpiler Error: Unexpected primitive type kind");
					}
				} else {
					final String tstype = param.getType().toString();
					if ("String".equals(tstype)) {
						this.params.add(new AbstractMap.SimpleEntry<>(name, "string"));
					} else {
						final TranspilerUnit tu = transpiler.getTranspilerUnit(method);
						importsRequired.put(tstype, tu.imports.get(tstype));
						this.params.add(new AbstractMap.SimpleEntry<>(name, tstype));
					}
				}
			}
		}
	}

	private static String determineValue(final Transpiler transpiler, final AnnotationTree annotation) {
		final Map<String, ExpressionTree> args = transpiler.getArguments(annotation);
		final ExpressionTree value = args.get("value");
        if (value == null) {
            throw new TranspilerException("Transpiler Exception: Missing value value for @PathParam annotation.");
        }
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof final LiteralTree literal) && (literal.getValue() instanceof final String str))
			return str;
		throw new TranspilerException("Transpiler Exception: Unhandled value argument for PathParam annotation.");
	}

}
