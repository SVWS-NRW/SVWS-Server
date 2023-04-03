package de.svws_nrw.core.data.erzieher;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Erzieherart eines Erziehers.  
 */
@XmlRootElement
@Schema(description = "Die Erzieherart eines Erziehers.")
@TranspilerDTO
public class Erzieherart {
    
	/** ID der Erzieherart */
	@Schema(description = "die ID der Erzieherart, welchem der Erzieher zugeordnet ist", example = "1")
	public long id;

	/** Bezeichnung der Erzieherart */
	@Schema(description = "die Bezeichnung der Erzieherart, welchem der Erzieher zugeordnet ist", example = "Mutter")
	public String bezeichnung;

	//TODO zu entscheiden, ob folgende Attribute benötigt werden?
   /** Sortierung der Erzieherart   
	public Integer Sortierung;
   	
    /** Sichbarkeit der Erzieherart */
	//@Schema(description = "die Sichtbarkeit der Erzieherart, welchem der Erzieher zugeordnet ist", example = "true")
    //public Boolean Sichtbar;

	/** Änderbarkeit der Erzieherart */
    //@Schema(description = "die Änderbarbeit der Erzieherart, welchem der Erzieher zugeordnet ist", example = "flase")
	//public Boolean Aenderbar;


    
}
