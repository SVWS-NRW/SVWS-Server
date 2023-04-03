package de.svws_nrw.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für einen Eintrag im Katalog der Fachklassen beim Berufskolleg.
 */
@XmlRootElement
@Schema(description = "die Daten für einen Eintrag im Katalog der Fachklassen beim Berufskolleg.")
@TranspilerDTO
public class BerufskollegFachklassenKatalogDaten {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id;

	/** Gibt an, ob die Fachklassen ausgelaufen ist oder nicht */
	@Schema(description = "gibt an, ob die Fachklassen ausgelaufen ist oder nicht", example = "false")
	public boolean istAusgelaufen = false;

	/** Die Gruppe des Berufsfeldes. */
	@Schema(description = "die Gruppe des Berufsfeldes", example = "T")
	public String berufsfeldGruppe;

	/** Das Berufsfeld. */
	@Schema(description = "das Berufsfeld", example = "MT")
	public String berufsfeld;

	/** Ebene 1 des Berufsfeldes */
	@Schema(description = "Ebene 1 des Berufsfeldes", example = "TE")
	public String ebene1;

	/** Ebene 2 des Berufsfeldes */
	@Schema(description = "Ebene 2 des Berufsfeldes", example = "ME")
	public String ebene2;

	/** Ebene 3 des Berufsfeldes */
	@Schema(description = "Ebene 3 des Berufsfeldes", example = "")
	public String ebene3;

	/** Die Bezeichnung der Fachklasse */
	@Schema(description = "die Bezeichnung der Fachklasse", example = "Metalltechnik")
	public @NotNull String bezeichnung = "";

	/** Die Bezeichnung der Fachklasse (männlich) */
	@Schema(description = "die Bezeichnung der Fachklasse (männlich)", example = "Metalltechnik")
	public @NotNull String bezeichnungM = "";

	/** Die Bezeichnung der Fachklasse (weiblich) */
	@Schema(description = "die Bezeichnung der Fachklasse (weiblich)", example = "Metalltechnik")
	public @NotNull String bezeichnungW = "";

	/** Gibt an, in welchem Schuljahr die Fachklasse einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr die Anlage einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Fachklasse gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr die Anlage gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "2025")
	public Integer gueltigBis = null;

}
