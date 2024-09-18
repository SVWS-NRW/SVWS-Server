package de.svws_nrw.asd.data.schule;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.CoreTypeDataNurSchulformen;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gültigen Statistikwerte für den Katalog der Schulformen.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Schulformen.")
@TranspilerDTO
public class SchulgliederungKatalogEintrag extends CoreTypeDataNurSchulformen {

	/** Gibt an, ob es sich um einen Bildungsgang am Berufskolleg handelt. */
	@Schema(description = "gibt an, ob es sich um einen Bildungsgang am Berufskolleg handelt", example = "true")
	public boolean istBK = false;

	/** Gibt an, ob es sich um eine auslaufende Schulgliederung oder einen auslaufenden Bildungsgang handelt. */
	@Schema(description = "gibt an, ob es sich um eine auslaufende Schulgliederung oder einen auslaufenden Bildungsgang handelt", example = "false")
	public boolean istAuslaufend = false;

	/** Gibt an, ob es sich um eine ausgelaufene Schulgliederung oder einen ausgelaufenen Bildungsgang handelt. */
	@Schema(description = "gibt an, ob es sich um eine ausgelaufene Schulgliederung oder einen ausgelaufenen Bildungsgang handelt", example = "false")
	public boolean istAusgelaufen = false;

	/** Die Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt. */
	@Schema(description = "die Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt", example = "A")
	public String bkAnlage = null;

	/** Der Typ der Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt. */
	@Schema(description = "der Typ der Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt", example = "01")
	public String bkTyp = null;

	/** Der Index für den Zugriff auf die Fachklassen am Berufskolleg. Dieser kann bei unterschiedlichen Gliederungen identisch sein. */
	@Schema(description = "der Index für den Zugriff auf die Fachklassen am Berufskolleg. Dieser kann bei unterschiedlichen Gliederungen identisch sein.", example = "10")
	public Integer bkIndex = null;

	/** Gibt an, ob es sich um einen Bildungsgang in Vollzeit handelt oder nicht */
	@Schema(description = "gibt an, ob es sich um einen Bildungsgang in Vollzeit handelt oder nicht.", example = "false")
	public boolean istVZ = false;

	/** Gibt eine Liste von Abschlusskombinationen aus beruflichen und allgemeinbildenden Abschluss an, mit Angabe der zulässigen Jahrgänge */
	@Schema(description = "gibt die möglichen Abschlusskombinationen (berufsbildend und allgemeinbildend) unter Angabe der zulässigen Jahrgänge", example = "BS")
	public @NotNull List<SchulgliederungGueltigerAbschluss> abschluesse = new ArrayList<>();

}
