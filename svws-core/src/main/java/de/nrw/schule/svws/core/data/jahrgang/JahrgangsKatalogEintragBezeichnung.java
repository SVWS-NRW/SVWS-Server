package de.nrw.schule.svws.core.data.jahrgang;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import de.nrw.schule.svws.core.types.schule.Schulform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Jahrgangsbezeichung zu einer Schulform bei einem Katalog-Eintrag 
 * der zulässigen Jahrgänge.
 */
@XmlRootElement
@Schema(description="eine Zuordnung einer Jahrgangsbezeichung zu einer Schulform.")
@TranspilerDTO
public class JahrgangsKatalogEintragBezeichnung {

	/** Das Kürzel der Schulform */
	@Schema(required = true, description = "das Kürzel der Schulform", example="GY")
	public @NotNull String schulform = "";
	
	/** Die Bezeichnung des Jahrgangs */
	@Schema(required = true, description = "die Bezeichnung des Jahrgangs", example="Einführungsphase")
	public @NotNull String bezeichnung = "";


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public JahrgangsKatalogEintragBezeichnung() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 *
	 * @param schulform     die Schulform
	 * @param bezeichnung   die Bezeichnung des Jahrgangs
	 */
	public JahrgangsKatalogEintragBezeichnung(final @NotNull Schulform schulform, final @NotNull String bezeichnung) {
		this.schulform = schulform.daten.kuerzel;
		this.bezeichnung = bezeichnung;
	}	

}
