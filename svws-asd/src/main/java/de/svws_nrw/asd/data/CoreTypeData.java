package de.svws_nrw.asd.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Schnittstelle beschreibt den grundlegenden Aufbau von Core-Type-Daten.
 * Diese müssen eine ID und eine Gültigkeit mit den beiden Schuljahren von und bis
 * haben. Werte von null für die Gültigkeit symbolisieren, dass die Gültigkeit
 * unbegrenzt ist.
 *
 * Anmerkung: Für die Möglichkeit die Klasse zu transpilieren, darf diese nicht als
 *   "abstract" gekennzeichnet sein.
 */
@Schema(description = "ein Eintrag im Katalog eines Core-Types.")
@TranspilerDTO
public class CoreTypeData {

	/** Die ID des Core-Type-Daten-Eintrags und der neue Schlüssel für die ASD */
	@Schema(description = "die ID des Core-Type-Daten-Eintrags und der neue Schlüssel für die ASD", example = "4711")
	public long id = -1;

	/**
	 * Ein Schlüssel als Zeichenkette, welcher sich auf den Schlüssel eines externen Katalogs bezieht. Als fremder Katalog können
	 * hier auch Schlüsselwerte aus der früheren ASD-Statistik angegeben. Diese müssen für ein Jahr der Historie über den
	 * Katalog eindeutig sein, jedoch nicht im Verlauf der Jahre. In der Historie eines Bezeichners können diese allerdings abweichen.
	 */
	@Schema(description = "ein Schlüssel als Zeichenkette, welcher ggf. auf externe Katalog verweist und für ein Jahr der Historie des Katalogs eindeutig sein muss.", example = "17")
	public @NotNull String schluessel = "";

	/** Das Kürzel, welches als Kurztext zu Visualisierung verwendet wird. Sollte nicht als identifizierendes Merkmal verwendet werden. */
	@Schema(description = "das Kürzel, welches als Kurztext zu Visualisierung verwendet wird. Sollte nicht als identifizierendes Merkmal verwendet werden", example = "GE")
	public @NotNull String kuerzel = "";

	/** Die Bezeichnung, welche als Langtext zu Visualisierung verwendet wird. Sollte nicht als identifizierendes Merkmal verwendet werden. */
	@Schema(description = "die Bezeichnung, welche als Langtext zu Visualisierung verwendet wird. Sollte nicht als identifizierendes Merkmal verwendet werden", example = "Gesamtschule")
	public @NotNull String text = "";

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "2025")
	public Integer gueltigBis = null;

}
