package de.svws_nrw.transpiler.test;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert einen Historieneintrag eines Faches aus dem Katalog der berufsbezogenen Fächer
 */
@XmlRootElement
@Schema(description = "eine Historiendatum eines Eintrags in dem Katalog der berufsbezogenen Fächer.")
@TranspilerDTO
public class BKFBFach {

	/** Die Fachklassen, in denen das Fach im Lehrplan steht */
	@Schema(description = "Die Fachklassen, in denen das Fach im Lehrplan steht")
	public @NotNull List<@NotNull BKFachklassenSchluessel> fachklassen = new ArrayList<>();

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id = -1;

	/** Das im Alltag verwendete Kuerzel des Fachs. */
	@Schema(description = "das im Alltag verwendete Kuerzel des Fachs", example = "FP")
	public @NotNull String  kuerzel = "";

	/** Die Zeugnisbezeichnung des Fachs */
	@Schema(description = "die Zeugnisbezeichnung des Fachs", example = "Fertigungsprozesse")
	public @NotNull String bezeichnung = "";

	/** Gibt an, in welchem Schuljahr der Historien-Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr der Historien-Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Historien-Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr der Historien-Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigBis = null;

}
