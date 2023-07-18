package de.svws_nrw.core.data.stundenplan;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle
 * verwendet. Sie liefert die Informationen zu einem Unterricht im Stundenplan
 * eines Lehrers (bestehend auf mehreren Unterrichtsstunden).
 */
@XmlRootElement
@Schema(description = "der Unterricht im Stundenplan eines Lehrers.")
@TranspilerDTO
public class LehrerStundenplanUnterricht {

	/** Die ID der Leistungsdaten, um zusammen gehörige Unterrichtseinheiten zu erkennen. */
	@Schema(description = "die ID der Leistungsdaten, um zusammen gehörige Unterrichtseinheiten zu erkennen", example = "4711")
	public long idLeistungen = -1;

	/** Die ID der Unterrichtseinheit */
	@Schema(description = "die ID der Unterrichtseinheit", example = "815")
	public long idUnterricht = -1;

	/** Die ID im Zeitraster des Stundenplans */
	@Schema(description = "die ID im Zeitraster des Stundenplans", example = "1")
	public long idZeitraster = -1;

	/** Die Kursart der Unterrichtseinheit. */
	@Schema(description = "die Kursart der Unterrichtseinheit", example = "GKS")
	public @NotNull String kursart = "";

	/** Der Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0 */
	@Schema(description = "er Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0 ", example = "0")
	public int wochentyp = -1;

	/** Die Räume in denen der Unterricht stattfindet */
	@Schema(description = "Die Schiene des Unterrichts bei Oberstufenunterrichten")
	public @NotNull List<@NotNull StundenplanRaum> unterrichtsraeume = new ArrayList<>();

	/** Die Schiene des Unterrichts bei Oberstufenunterrichten */
	@Schema(description = "Die Schienen des Unterrichts bei Oberstufenunterrichten", example = "")
	public @NotNull List<@NotNull StundenplanSchiene> schienen = new ArrayList<>();

	/** Die Klassen, welche unterrichtet werden */
	@Schema(description = "Die Klassen, welche unterrichtet werden")
	public @NotNull List<@NotNull StundenplanKlasse> klassen = new ArrayList<>();

	/** Die ID des Faches */
	@Schema(description = "die ID des Faches", example = "89")
	public long idFach = -1;

	/** Das Kürzel des Unterrichtsfaches. */
	@Schema(description = "das Kürzel des Unterrichtsfaches", example = "D")
	public @NotNull String fachKuerzel = "";

	/** Die Bezeichnung des Unterrichtsfaches. */
	@Schema(description = "die Bezeichnung des Unterrichtsfaches", example = "Deutsch")
	public @NotNull String fachBezeichnung = "";

	/** Das Kürzel des Unterrichtsfaches in Bezug auf die amtliche Schulstatistik. */
	@Schema(description = "das Kürzel des Unterrichtsfaches in Bezug auf die amtliche Schulstatistik", example = "D")
	public @NotNull String fachKuerzelStatistik = "";

}
