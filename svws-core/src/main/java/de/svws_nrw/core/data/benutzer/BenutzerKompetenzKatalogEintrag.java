package de.svws_nrw.core.data.benutzer;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenzGruppe;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt den Katalog der Benutzerkompetenzen.  
 */
@XmlRootElement
@Schema(description="ein Eintrag im Katalog der Benutzerkompetenzen.")
@TranspilerDTO
public class BenutzerKompetenzKatalogEintrag {

	/** Die ID der Benutzerkompetenz. */
	@Schema(description = "die ID der Benutzerkompetenz", example="11")
	public long id = -1;
	
	/** Die ID der zugehörigen Benutzerkompetenzgruppe. */
	@Schema(description = "die ID der zugehörigen Benutzerkompetenzgruppe", example="100")
	public @NotNull long gruppe_id = -1;
	
	/** Die Bezeichnung der Benutzerkompetenz. */
	@Schema(description = "die Bezeichnung der Benutzerkompetenz", example="Ansehen")
	public @NotNull String bezeichnung = "";
	

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
	 */
	public BenutzerKompetenzKatalogEintrag(final long id, final @NotNull BenutzerKompetenzGruppe gruppe, final @NotNull String bezeichnung) {
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.gruppe_id = gruppe.daten.id; 
	}
	
}
