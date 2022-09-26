package de.nrw.schule.svws.transpiler.typescript;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.tree.VariableTree;

import de.nrw.schule.svws.transpiler.Transpiler;
import de.nrw.schule.svws.transpiler.TranspilerException;
import de.nrw.schule.svws.transpiler.TranspilerUnit;

/**
 * Diese Klasse repräsentiert den Request-Body einer OpenAPI-Methode.
 */
public class ApiPathParams {

	/** Ein Vektor mit den benötigten Klassen bei den Typescript-Importen */
	public final HashMap<String, String> importsRequired = new HashMap<>();
	
	/** Das Mapping zwischen dem Namen eines Pfad-Parameters auf den TS-Typ des Pfad-Parameters */
	public final Vector<Map.Entry<String, String>> params = new Vector<>();
	
	/**
	 * Erstellt anhand der übergebenen Annotation und mithilfe des übergebenen Transpilers ein
	 * neues {@link ApiPathParams}-Objekt.
	 *   
	 * @param transpiler   der Transpiler
	 * @param method       die API-Methode
	 */
	ApiPathParams(Transpiler transpiler, MethodTree method) {
		// Ermittle die Request-Body Annotation
		List<? extends VariableTree> params = method.getParameters();
		for (VariableTree param : params) {
			AnnotationTree annotation = transpiler.getAnnotation("jakarta.ws.rs.PathParam", param);
			if (annotation != null) {
				String name = determineValue(transpiler, annotation);
				Tree type = param.getType();
				if (type instanceof PrimitiveTypeTree ptt) {
					switch (ptt.getPrimitiveTypeKind()) {
				        case BYTE, SHORT, INT, LONG, FLOAT, DOUBLE -> this.params.add(new AbstractMap.SimpleEntry<>(name, "number"));
				        case BOOLEAN -> this.params.add(new AbstractMap.SimpleEntry<>(name, "boolean"));
				        case CHAR -> this.params.add(new AbstractMap.SimpleEntry<>(name, "string"));
				        default -> throw new TranspilerException("Transpiler Error: Unexpected primitive type kind"); 
					}
				} else {
					String tstype = param.getType().toString();
					if ("String".equals(tstype)) {
						this.params.add(new AbstractMap.SimpleEntry<>(name, "string"));
					} else {
						TranspilerUnit tu = transpiler.getTranspilerUnit(method);
						importsRequired.put(tstype, tu.imports.get(tstype));				
						this.params.add(new AbstractMap.SimpleEntry<>(name, tstype));
					}
				}
			}
		}
	}
	
	private static String determineValue(Transpiler transpiler, AnnotationTree annotation) {
		Map<String, ExpressionTree> args = transpiler.getArguments(annotation);
		ExpressionTree value = args.get("value");
        if(value == null) {
            throw new TranspilerException("Transpiler Exception: Missing value value for @PathParam annotation.");
        }
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof LiteralTree literal) && (literal.getValue() instanceof String str))
			return str;
		throw new TranspilerException("Transpiler Exception: Unhandled value argument for PathParam annotation.");		
	}

}
