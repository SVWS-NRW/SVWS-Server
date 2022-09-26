package de.nrw.schule.svws.transpiler.typescript;

import java.util.Map;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.Tree.Kind;

import de.nrw.schule.svws.transpiler.Transpiler;
import de.nrw.schule.svws.transpiler.TranspilerException;

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
	public ApiClassAnnotations(Transpiler transpiler, ClassTree classTree) {
		this.tag = ApiClassAnnotations.getTag(transpiler, classTree);
		this.path = ApiClassAnnotations.getPath(transpiler, classTree);
		ApiMimeType tmpProduces = ApiClassAnnotations.getMimeType(transpiler, classTree, "jakarta.ws.rs.Produces");
		if (tmpProduces == null) // set JSON as class default mime type for Produces and Consumes, if no mime type is specified
			this.produces = ApiMimeType.APPLICATION_JSON;
		else
			this.produces = tmpProduces;
		ApiMimeType tmpConsumes = ApiClassAnnotations.getMimeType(transpiler, classTree, "jakarta.ws.rs.Consumes");
		if (tmpConsumes == null) // set JSON as class default mime type for Produces and Consumes, if no mime type is specified
			this.consumes = ApiMimeType.APPLICATION_JSON;
		else
			this.consumes = tmpConsumes;
	}

	
	private static String getTag(Transpiler transpiler, ClassTree classTree) {
		AnnotationTree annotationTag = transpiler.getAnnotation("io.swagger.v3.oas.annotations.tags.Tag", classTree);
		if (annotationTag == null)
			throw new TranspilerException("Transpiler Exception: Missing Tag annotation for class " + classTree.getSimpleName().toString() + ".");
		Map<String, ExpressionTree> args = transpiler.getArguments(annotationTag);
		ExpressionTree name = args.get("name");
        if(name == null) {
            throw new TranspilerException("Transpiler Exception: Missing name value for @Tag annotation.");
        }
		if ((name.getKind() == Kind.STRING_LITERAL) && (name instanceof LiteralTree literal) && (literal.getValue() instanceof String tag))
			return tag;
		throw new TranspilerException("Transpiler Exception: Unhandled name argument for Tag annotation.");		
	}
	
	
	private static String getPath(Transpiler transpiler, ClassTree classTree) {
		AnnotationTree annotationPath = transpiler.getAnnotation("jakarta.ws.rs.Path", classTree);
		if (annotationPath == null)
			throw new TranspilerException("Transpiler Exception: Missing Path annotation for class " + classTree.getSimpleName().toString() + ".");
		Map<String, ExpressionTree> args = transpiler.getArguments(annotationPath);
		ExpressionTree value = args.get("value");
        if(value == null) {
            throw new TranspilerException("Transpiler Exception: Missing value value for @Path annotation.");
        }
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof LiteralTree literal) && (literal.getValue() instanceof String path))
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
	private static ApiMimeType getMimeType(Transpiler transpiler, ClassTree classTree, String annotation) {
		AnnotationTree annotationTree = transpiler.getAnnotation(annotation, classTree);
		if (annotationTree == null)
			return null;
		Map<String, ExpressionTree> args = transpiler.getArguments(annotationTree);
		ExpressionTree value = args.get("value");
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof LiteralTree literal) && (literal.getValue() instanceof String str))
			return ApiMimeType.get(str);
		if (value.getKind() == Kind.MEMBER_SELECT)
			return ApiMimeType.get(value.toString());
		throw new TranspilerException("Transpiler Exception: Unhandled value argument for Consumes annotation of kind " + value.getKind() + ".");
	}

}
