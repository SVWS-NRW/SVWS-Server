package de.svws_nrw.asd.data.fach;

import de.svws_nrw.asd.data.CoreTypeDataNurSchulformenUndSchulgliederungen;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert den Katalog der Fächer und die Information für welche Schulformen
 * diese zulässig sind.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Fächer.")
@TranspilerDTO
public class FachKatalogEintrag extends CoreTypeDataNurSchulformenUndSchulgliederungen {

	/** Das Aufgabenfeld, welchem das Fach ggf. zugeordnet ist (1, 2 oder 3) */
	@Schema(description = "das Aufgabenfeld, welchem das Fach ggf. zugeordnet ist (1, 2 oder 3)", example = "1")
	public Integer aufgabenfeld = -1;

	/** Das Kürzel der zugeordneten Fachgruppe */
	@Schema(description = "das Kürzel der zugeordneten Fachgruppe", example = "FS")
	public String fachgruppe = "";

	/** Der ASD-Jahrgang, ab dem das Fach zulässig ist (z.B. bei Fremdsprachen) */
	@Schema(description = "der ASD-Jahrgang, ab dem das Fach zulässig ist (z.B. bei Fremdsprachen) - Teil des Kürzels für die amtliche Schulstatistik", example = "EF")
	public String abJahrgang = "";

	/** Gibt an, ob es sich um eine Fremdsprache handelt */
	@Schema(description = "gibt an, ob es sich um eine Fremdsprache handelt", example = "true")
	public boolean istFremdsprache = false;

	/** Gibt an, ob es sich um ein Fach der Herkuntftsprache handelt (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer Pflichtfremdsprache) */
	@Schema(description = "gibt an, ob es sich um ein Fach der Herkuntftsprache handelt (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer Pflichtfremdsprache)", example = "false")
	public boolean istHKFS = false;

	/** Gibt an, ob das Fach außerhalb des regulären Fachunterichts unterrichtet wird. */
	@Schema(description = "gibt an, ob das Fach außerhalb des regulären Fachunterichts unterrichtet wird", example = "false")
	public boolean istAusRegUFach = false;

	/** Gibt an, ob es sich bei dem Fach um einen Ersatz für eine Pflichtfremdsprache handelt (siehe auch istHKFS) */
	@Schema(description = "gibt an, ob es sich bei dem Fach um einen Ersatz für eine Pflichtfremdsprache handelt (siehe auch istHKFS)", example = "false")
	public boolean istErsatzPflichtFS = false;

	/** Gibt an, ob das Religionsfach konfessionell kooperativ unterrichtet wird oder nicht - Teil des Kürzels für die amtliche Schulstatistik */
	@Schema(description = "gibt an, ob das Religionsfach konfessionell kooperativ unterrichtet wird oder nicht - Teil des Kürzels für die amtliche Schulstatistik", example = "false")
	public boolean istKonfKoop = false;

	/** Gibt an, ob das Fach nur in der Sekundarstufe II unterrichtet wird. */
	@Schema(description = "gibt an, ob das Fach nur in der Sekundarstufe II unterrichtet wird", example = "true")
	public boolean nurSII = false;

	/** Gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden soll oder nicht. */
	@Schema(description = "gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden soll oder nicht", example = "true")
	public boolean exportASD = false;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public FachKatalogEintrag() {
		// leer
	}

}
