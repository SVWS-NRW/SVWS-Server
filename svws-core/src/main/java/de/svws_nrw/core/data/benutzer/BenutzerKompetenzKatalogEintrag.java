package de.svws_nrw.core.data.benutzer;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenzGruppe;
import de.svws_nrw.core.types.schule.Schulform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt den Katalog der Benutzerkompetenzen.
 */
@XmlRootElement
@Schema(description = "ein Eintrag im Katalog der Benutzerkompetenzen.")
@TranspilerDTO
public class BenutzerKompetenzKatalogEintrag {

	/** Die ID der Benutzerkompetenz. */
	@Schema(description = "die ID der Benutzerkompetenz", example = "11")
	public long id = -1;

	/** Die ID der zugehörigen Benutzerkompetenzgruppe. */
	@Schema(description = "die ID der zugehörigen Benutzerkompetenzgruppe", example = "100")
	public @NotNull long gruppe_id = -1;

	/** Die Bezeichnung der Benutzerkompetenz. */
	@Schema(description = "die Bezeichnung der Benutzerkompetenz", example = "Ansehen")
	public @NotNull String bezeichnung = "";
	
	/** Die Schulformen. */
    @Schema(required = true, description = "die Schulformen, bei denen die Kompetenz zulässig ist", example="Liste")
	public @NotNull Schulform@NotNull[] nurSchulformen = null;
	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public BenutzerKompetenzKatalogEintrag() {
	}


	
	
	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 *
	 * @param id             die ID
	 * @param gruppe         die Gruppe, welcher die Benutzerkompetenz zugeordnet ist
	 * @param bezeichnung    die Bezeichnung der Benutzerkompetenz
	 * @param schulFormen    die Schulformen, bei denen die Kompetenz zulässig ist.
	 */
	public BenutzerKompetenzKatalogEintrag(final long id, final @NotNull BenutzerKompetenzGruppe gruppe, final @NotNull String bezeichnung, @NotNull Schulform@NotNull[] schulFormen ) {
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.gruppe_id = gruppe.daten.id; 
		this.nurSchulformen = schulFormen;
	}
	public BenutzerKompetenzKatalogEintrag(final long id, final @NotNull BenutzerKompetenzGruppe gruppe, final @NotNull String bezeichnung) {
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.gruppe_id = gruppe.daten.id; 
	}
	
	
	
	/**
	 * überprüft, ob für die übergebene Schulform die Kompetenz zulässig ist.
	 * 
	 * @param pSchulform             dieSchulform
	 * 
	 * @return  true, wenn die Kompetenz für die Schulform zulässig ist.
	 */
	public boolean hatSchulform(final @NotNull Schulform pSchulform) {
	    
		if(this.nurSchulformen == null)
			return true;
		for(final @NotNull Schulform sf : this.nurSchulformen) {
	        if(sf.daten.id == pSchulform.daten.id)
	            return true;
	    }
	    return false;
	 }
	
	
	
	
	
	
}
