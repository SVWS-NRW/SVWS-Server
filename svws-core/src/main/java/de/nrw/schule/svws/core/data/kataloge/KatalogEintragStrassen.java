package de.nrw.schule.svws.core.data.kataloge;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse ist eine DTO-Klasse für den Strassen-Katalog von IT.NRW. Der
 * Katalog wird in den Java-Resourcen unter "daten/csv/statkue/Strassen.csv"
 * erwartet.
 */
@JsonPropertyOrder({"Ort","RegSchl","Strasse","Stand"})
@XmlRootElement
@Schema(description="ein Eintrag in einem standardisierten Katalog.")
@TranspilerDTO
public class KatalogEintragStrassen {

	/** Katalog von ITNRW Straßen: zugehörige Ort */
	@Schema(required = true, description = "der zugehörige Ort des Katalog-Eintrags", example="Düsseldorf")
	public String Ort;

	/** Katalog von ITNRW Straßen: zugehöriger Regionalschlüssel */
	@Schema(required = true, description = "der zugehörige Regionalschlüssel des Katalog-Eintrags", example="05111000")
	public String RegSchl;

	/** Katalog von ITNRW Straßen: Straßenname */
	@Schema(required = true, description = "der Straßenname des Katalog-Eintrags", example="Aachener Platz")
	public String Strasse;

	/** Katalog von ITNRW Straßen: aktueller Stand */
	@Schema(required = true, description = "der Stand des Katalog-Eintrags", example="09.04.2020")
	public String Stand;

}
