package de.svws_nrw.transpiler.test;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Schnittstelle beschreibt den grundlegenden Aufbau von Core-Type-Daten.
 * Diese müssen eine ID und eine Gültigkeit mit den beiden Schuljahren von und bis
 * haben. Werte von null für die Gültigkeit symbolisieren, dass die Gültigkeit
 * unbegrenzt ist.
 *
 * Anmerkung: Für die Möglichkeit die Klasse zu transpilieren, darf diese nicht als
 *   "abstract" gekennzeichnet sein.
 */
@XmlRootElement
@Schema(description = "ein Eintrag im Katalog eines Core-Types.")
@TranspilerDTO
public class CoreTypeData {

	/** Die ID des Core-Type-Daten-Eintrags */
	@Schema(description = "die ID des Core-Type-Daten-Eintrags", example = "4711")
	public long id = -1;

	/** Gibt an, in welchem Schuljahr die Schulform einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Schulform gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "2025")
	public Integer gueltigBis = null;

}
