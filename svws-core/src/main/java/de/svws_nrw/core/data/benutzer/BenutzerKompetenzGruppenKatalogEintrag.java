package de.svws_nrw.core.data.benutzer;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt den Katalog der Benutzerkompetenz-Gruppen.  
 */
@XmlRootElement
@Schema(description="ein Eintrag im Katalog der Benutzerkompetenz-Gruppen.")
@TranspilerDTO
public class BenutzerKompetenzGruppenKatalogEintrag {

	/** Die ID der Kompetenzgruppe. */
	@Schema(required = true, description = "die ID der Kompetenzgruppe", example="100")
	public long id = -1;
	
	/** Die Bezeichnung der Kompetenzgruppe. */
	@Schema(required = true, description = "die Bezeichnung der Kompetenzgruppe", example="Schüler-Individualdaten")
	public @NotNull String bezeichnung = "";
	
	/** Die Spalte bei der Darstellung der Benutzerverwaltung in Schild. */
	@Schema(required = true, description = "die Spalte bei der Darstellung der Benutzerverwaltung in Schild", example="1")
	public int spalte = -1;
	
	/** Die Zeile bei der Darstellung der Benutzerverwaltung in Schild. */
	@Schema(required = true, description = "die Zeile bei der Darstellung der Benutzerverwaltung in Schild", example="1")
	public int zeile = -1;

	
	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public BenutzerKompetenzGruppenKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id             die ID der Benutzerkompetenz-Gruppe
	 * @param bezeichnung    die Bezeichnung der Benutzerkompetenz-Gruppe
	 * @param spalte         die Spalte bei der Darstellung der Benutzerverwaltung in Schild
	 * @param zeile          die Zeile bei der Darstellung der Benutzerverwaltung in Schild
	 *  
	 */
	public BenutzerKompetenzGruppenKatalogEintrag(final long id, final @NotNull String bezeichnung, final int spalte, final int zeile) {
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.spalte = spalte;
		this.zeile = zeile;
	}
	
}
