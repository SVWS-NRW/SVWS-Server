package de.svws_nrw.transpiler.typescript;

import java.util.List;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

import de.svws_nrw.transpiler.Transpiler;
import de.svws_nrw.transpiler.TranspilerException;


/**
 * Diese Klasse stellt Hilfsmethoden für die Typescript-Umwandlung zur Verfügung
 */
public final class TypeScriptUtils {


	private TypeScriptUtils() {
		// Diese Klasse ist nicht inistantiierbar
	}


	/**
	 * Transpiliert den übergebenen Typ.
	 *
	 * @param type  der zu transpilierende Typ als Type-Mirror
	 *
	 * @return der String mit dem transpilierten Typ
	 */
	public static String transpileTypeParamTypeMirror(final TypeMirror type) {
		final boolean hasNotNull = !Transpiler.hasAllowNullAnnotation(type);
		if (type instanceof final TypeVariable tv) {
			final String result = tv.asElement().getSimpleName().toString();
			return hasNotNull ? result : (result + " | null");
		}
		if (type instanceof final DeclaredType dt) {
			if ("java.util.Map.Entry".equals(dt.asElement().toString())) {
				return "JavaMapEntry<any, any>";
			}
			final StringBuilder sb = new StringBuilder(dt.asElement().getSimpleName().toString());
			final List<? extends TypeMirror> targs = dt.getTypeArguments();
			if (!targs.isEmpty()) {
				sb.append("<");
				boolean first = true;
				for (final TypeMirror ta : targs) {
					if (first)
						first = false;
					else
						sb.append(", ");
					sb.append(transpileTypeParamTypeMirror(ta));
				}
				sb.append(">");
			}
			if (!hasNotNull)
				sb.append(" | null");
			return sb.toString();
		}
		throw new TranspilerException("Transpiler Error: Type Kind " + type.getKind() + " not yet supported");
	}

}
