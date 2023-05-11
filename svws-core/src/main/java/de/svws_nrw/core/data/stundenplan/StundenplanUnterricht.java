package de.svws_nrw.core.data.stundenplan;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.kurse.ZulaessigeKursart;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem Unterricht im Stundenplan
 */
@XmlRootElement
@Schema(description = "der Unterricht im Stundenplan.")
@TranspilerDTO
public class StundenplanUnterricht {

	/** Die ID der Unterrichtseinheit */
	@Schema(description = "die ID der Unterrichtseinheit", example = "815")
	public long id = -1;

	/** Die ID im Zeitraster des Stundenplans */
	@Schema(description = "die ID im Zeitraster des Stundenplans", example = "1")
	public long idZeitraster = -1;

	/** Der Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0 */
	@Schema(description = "der Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0 ", example = "0")
	public int wochentyp = -1;

	/** Die ID des Kurses */
	@Schema(description = "die ID des Kurses", example = "89")
	public long idKurs = -1;

	/** Die allgemeine Kursart der Unterrichtseinheit (siehe auch {@link ZulaessigeKursart}). */
	@Schema(description = "die allgemeine Kursart der Unterrichtseinheit", example = "GK")
	public @NotNull String kursart = "";

	/** Die Bezeichnung des Kurses. */
	@Schema(description = "die Bezeichnung des Kurses", example = "M-GK1")
	public @NotNull String kursBezeichnung = "";

	/** Die IDs der Jahrgänge, denen der Kurs zugeordnet ist. */
	@Schema(description = "die IDs der Jahrgänge, denen der Kurs zugeordnet ist")
	public @NotNull List<@NotNull Long> kursJahrgangIDs = new ArrayList<>();

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

	/** Die IDs der Lehrer, die dieser Unterrichtseinheit zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit den IDs der Lehrer, die dieser Unterrichtseinheit zugeordnet sind."))
	public @NotNull List<@NotNull Long> lehrer = new ArrayList<>();

	/** Die IDs der Klassen, die dieser Unterrichtseinheit zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit den IDs der Klassen, die dieser Unterrichtseinheit zugeordnet sind."))
	public @NotNull List<@NotNull Long> klassen = new ArrayList<>();

	/** Die IDs der Räume, die dieser Unterrichtseinheit zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit den IDs der Räume, die dieser Unterrichtseinheit zugeordnet sind."))
	public @NotNull List<@NotNull Long> raeume = new ArrayList<>();

}
