package de.svws_nrw.asd.data.statistik;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die grundlegenden Daten eines Kurses.
 */
@XmlRootElement
@Schema(description = "Die Daten eines Kurses.")
@TranspilerDTO
public class KursStatistikGesamt {

	/** Die ID des Kurses. */
	@Schema(description = "die ID des Kurses", example = "4711")
	public long id;

	/** Das Kürzel des Kurses. */
	@Schema(description = "das Kürzel des Kurses", example = "IF-LK1")
	public @NotNull String kuerzel = "";

	/** Die IDs der Jahrgänge, denen der Kurs zugeordnet ist */
	@Schema(description = "die IDs der Jahrgänge, denen der Kurs zugeordnet ist")
	public @NotNull List<Long> idJahrgaenge = new ArrayList<>();

	/** Die ID des Faches, dem der Kurs zugeordnet ist */
	@Schema(description = "die ID des Faches, dem der Kurs zugeordnet ist", example = "815")
	public long idFach;

	/** Die allgemeine Kursart, welche zur Filterung der speziellen Kursarten verwendet wird. */
	@Schema(description = "die allgemeine Kursart, welche zur Filterung der speziellen Kursarten verwendet wird", example = "GK")
	public @NotNull String kursartAllg = "";

	/** Die Wochenstunden des Kurses. */
	@Schema(description = "die Wochenstunden des Kurses", example = "3")
	public int wochenstunden = -1;

	/** Die ID des Kurslehrers. */
	@Schema(description = "die ID des Kurslehrers", example = "42", nullable = true)
	public Long lehrer;

	/** Die Wochenstunden des Kurslehrers in dem Kurs. */
	@Schema(description = "die Wochenstunden des Kurslehrers in dem Kurs", example = "3")
	public double wochenstundenLehrer = -1;

	/** Die Liste der zusätzlichen Lehrkräfte eines Kurses. */
	@ArraySchema(schema = @Schema(implementation = KursLehrer.class))
	public @NotNull List<KursLehrer> weitereLehrer = new ArrayList<>();

	/** Die Schulnummer des Kurses, falls der Kurs an einer anderen Schule stattfindet (z.B. im Rahmen einer Kooperation). */
	@Schema(description = "die Schulnummer des Kurses, falls der Kurs an einer anderen Schule stattfindet (z.B. im Rahmen einer Kooperation)",
			example = "100001", nullable = true)
	public Integer schulnummer = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KursStatistikGesamt() {
		// leer
	}

}
