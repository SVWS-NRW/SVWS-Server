package de.svws_nrw.asd.data.kurse;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.asd.data.schule.SchulformSchulgliederung;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert den Katalog der Kursarten und die Information für welche Schulformen
 * diese zulässig sind.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Kursarten.")
@TranspilerDTO
public class ZulaessigeKursartKatalogEintrag extends CoreTypeData {

	/** Die Nummer der Kursart entsprechend der Vorgaben der amtlichen Schulstatistik */
	@Schema(description = "die Nummer der Kursart entsprechend der Vorgaben der amtlichen Schulstatistik", example = "71")
	public @NotNull String nummer = "";

	/** Ergänzende Bemerkungen zu der Kursart */
	@Schema(description = "ergänzende Bemerkungen zu der Kursart", example = "gemäß § 9 Abs. 2, 3  SchulG")
	public String bemerkungen = null;

	/** Das Kürzel einer verallgemeinerten Kursart, sofern diese angegeben ist */
	@Schema(description = "das Kürzel einer verallgemeinerten Kursart, sofern diese angegeben ist", example = "GK")
	public String kuerzelAllg = null;

	/** Die Bezeichnung der verallgemeinerter Kursart, sofern diese angegeben ist */
	@Schema(description = "die Bezeichnung der verallgemeinerter Kursart, sofern diese angegeben ist", example = "Grundkurs")
	public String bezeichnungAllg = null;

	/** Gibt an, ob die Kursart in der Gymnasialen Oberstufe zulässig ist */
	@Schema(description = "gibt an, ob die Kursart in der Gymnasialen Oberstufe zulässig ist", example = "true")
	public boolean erlaubtGOSt = false;

	/** Die Informationen zu Schulformen und -gliederungen, wo die Kursart zulässig ist. */
	@Schema(description = "die Informationen zu Schulformen und -gliederungen, wo die Kursart zulässig ist.")
	public @NotNull List<SchulformSchulgliederung> zulaessig = new ArrayList<>();

}
