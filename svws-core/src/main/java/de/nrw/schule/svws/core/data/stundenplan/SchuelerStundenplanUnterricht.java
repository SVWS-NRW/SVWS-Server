package de.nrw.schule.svws.core.data.stundenplan;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem Unterricht im Stundenplan eines Schülers
 * (bestehend auf mehreren Unterrichtsstunden).  
 */
@XmlRootElement
@Schema(description="der Unterricht im Stundenplan eines Schülers.")
@TranspilerDTO
public class SchuelerStundenplanUnterricht {

	/** Die ID der Leistungsdaten, um zusammen gehörige Unterrichtseinheiten zu erkennen. */
	@Schema(required = true, description = "die ID der Leistungsdaten, um zusammen gehörige Unterrichtseinheiten zu erkennen", example="4711")
	public long idLeistungen = -1;

	/** Die ID der Unterrichtseinheit */
	@Schema(required = true, description = "die ID der Unterrichtseinheit", example="815")
	public long idUnterricht = -1;
	
	/** Die ID im Zeitraster des Stundenplans */
	@Schema(required = true, description = "die ID im Zeitraster des Stundenplans", example="1")
	public long idZeitraster = -1;
	
	/** Die Kursart der Unterrichtseinheit. */
	@Schema(required = false, description = "die Kursart der Unterrichtseinheit", example="GKS")
	public @NotNull String kursart = "";

	/** Der Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0 */
	@Schema(required = true, description = "er Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0 ", example="0")
	public int wochentyp = -1;
	
	/** Die ID des Faches */
	@Schema(required = true, description = "die ID des Faches", example="89")
	public long idFach = -1;

	/** Das Kürzel des Unterrichtsfaches. */
	@Schema(required = false, description = "das Kürzel des Unterrichtsfaches", example="D")
	public @NotNull String fachKuerzel = "";

	/** Die Bezeichnung des Unterrichtsfaches. */
	@Schema(required = false, description = "die Bezeichnung des Unterrichtsfaches", example="Deutsch")
	public @NotNull String fachBezeichnung = "";

	/** Das Kürzel des Unterrichtsfaches in Bezug auf die amtliche Schulstatistik. */
	@Schema(required = false, description = "das Kürzel des Unterrichtsfaches in Bezug auf die amtliche Schulstatistik", example="D")
	public @NotNull String fachKuerzelStatistik = "";

	/** Die ID des Lehrers. */
	@Schema(required = true, description = "die ID des Lehrers", example="7")
	public long idLehrer = -1;

	/** Der Nachname des Schülers. */
	@Schema(required = false, description = "das Kürzel des Lehrers", example="007")
	public @NotNull String lehrerKuerzel = "";
	
	/** Der Nachname des Schülers. */
	@Schema(required = false, description = "der Nachname des Lehrers", example="Mustermann")
	public @NotNull String lehrerNachname = "";
	
	/** Der Vorname des Schülers. */
	@Schema(required = false, description = "der Vorname des Lehrers", example="Max")
	public @NotNull String lehrerVorname = "";

}
