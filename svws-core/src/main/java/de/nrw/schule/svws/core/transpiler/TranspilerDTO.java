package de.nrw.schule.svws.core.transpiler;

/**
 * This annotation is used to annotate data transfer object classes
 * that will be transpiled (see the SVWS-Transpiler project). 
 * If annotated the Transpiler will generate two methods  
 * 'transpilerFromJSON' and 'transpilerToJSON' for the class.  
 */
public @interface TranspilerDTO {

}
