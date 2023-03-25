package de.svws_nrw.transpiler.typescript;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.MethodTree;

import de.svws_nrw.transpiler.Transpiler;
import de.svws_nrw.transpiler.TranspilerException;

/**
 * Diese Aufzählung beinhaltet alle HTTP-Methoden, die von diesem Transpiler-Plugin
 * unterstützt werden.  
 */
public enum ApiHttpMethod {

	/** HTTP-GET */
	GET,
	
	/** HTTP-PATCH */
	PATCH,
	
	/** HTTP-POST */
	POST,
	
	/** HTTP-PUT */
	PUT,
	
	/** HTTP-DELETE */
	DELETE;

	/**
	 * Ermittelt anhand der Annotationen der übergebenen Methode die verwendete
	 * HTTP-Methode und gibt das zugehörige Element dieser Aufzählung zurück.
	 * 
	 * @param transpiler   der zu verwendende Transpiler
	 * @param method       die Methode, dessen HTTP-Methode-Typ ermittel wird
	 * 
	 * @return der Typ der HTTP-Methode
	 * 
	 * @throws TranspilerException falls die HTTP-Methode nicht bestimmt werden kann  
	 */
	public static ApiHttpMethod get(Transpiler transpiler, MethodTree method) throws TranspilerException {
		AnnotationTree annotationGET = transpiler.getAnnotation("jakarta.ws.rs.GET", method);
		AnnotationTree annotationPATCH = transpiler.getAnnotation("jakarta.ws.rs.PATCH", method);
		AnnotationTree annotationPOST = transpiler.getAnnotation("jakarta.ws.rs.POST", method);
		AnnotationTree annotationPUT = transpiler.getAnnotation("jakarta.ws.rs.PUT", method);
		AnnotationTree annotationDELETE = transpiler.getAnnotation("jakarta.ws.rs.DELETE", method);
		ApiHttpMethod result = null;
		if (annotationGET != null)
			result = GET;
		if (annotationPATCH != null) {
			if (result != null)
				throw new TranspilerException("Transpiler Error: Multiple HTTP method annotations are not allowed.");
			result = PATCH;
		}
		if (annotationPOST != null) {
			if (result != null)
				throw new TranspilerException("Transpiler Error: Multiple HTTP method annotations are not allowed.");
			result = POST;
		}
		if (annotationPUT != null) {
			if (result != null)
				throw new TranspilerException("Transpiler Error: Multiple HTTP method annotations are not allowed.");
			result = PUT;
		}
		if (annotationDELETE != null) {
			if (result != null)
				throw new TranspilerException("Transpiler Error: Multiple HTTP method annotations are not allowed.");
			result = DELETE;
		}
		if (result == null)
			throw new TranspilerException("Transpiler Error: No supported HTTP method specified.");
		return result;
	}
	
	
	@Override
	public String toString() {
		return this.name();
	}	 

}
