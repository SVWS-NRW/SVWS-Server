package de.nrw.schule.svws.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import de.nrw.schule.svws.core.types.schule.Schulform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Bezeichnungen zu einer Schulform bei einem Katalog-Eintrag 
 * der Herkunftsarten.
 */
@XmlRootElement
@Schema(description="eine Zuordnung einer Bezeichung zu einer Herkunftsart.")
@TranspilerDTO
public class HerkunftsartKatalogEintragBezeichnung {

	/** Das Kürzel der Schulform */
	@Schema(required = true, description = "das Kürzel der Schulform", example="GY")
	public @NotNull String schulform = "";

	/** Die Kurz-Bezeichnung der Herkunftsart */
	@Schema(required = true, description = "die Kurz-Bezeichnung der Herkunftsart", example="Nichtversetzung")
	public @NotNull String kurzBezeichnung = "";
	
	/** Die Bezeichnung der Herkunftsart */
	@Schema(required = true, description = "die Bezeichnung der Herkunftsart", example="Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)")
	public @NotNull String bezeichnung = "";


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public HerkunftsartKatalogEintragBezeichnung() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 *
	 * @param schulform         die Schulform
	 * @param kurzBezeichnung   die Kurz-Bezeichnung der Herkunftsart
	 * @param bezeichnung       die Bezeichnung der Herkunftsart
	 */
	public HerkunftsartKatalogEintragBezeichnung(@NotNull Schulform schulform, @NotNull String kurzBezeichnung, @NotNull String bezeichnung) {
		this.schulform = schulform.daten.kuerzel;
		this.kurzBezeichnung = kurzBezeichnung;
		this.bezeichnung = bezeichnung;
	}	

}
