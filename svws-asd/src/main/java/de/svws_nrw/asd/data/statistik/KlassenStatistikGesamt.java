package de.svws_nrw.asd.data.statistik;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die grundlegenden Daten einer Klasse.
 */
@XmlRootElement
@Schema(description = "Die Daten einer Klasse.")
@TranspilerDTO
public class KlassenStatistikGesamt {

	/** Die ID der Klasse. */
	@Schema(description = "die ID der Klasse", example = "4709")
	public long id;

	/** Die ID des Schuljahresabschnittes des Kurses. */
	@Schema(description = "die ID des Schuljahresabschnittes des Kurses", example = "14")
	public long idSchuljahresabschnitt;

	/** Das Kürzel der Klasse. */
	@Schema(description = "das Kürzel der Klasse", example = "06b", nullable = true)
	public String kuerzel;

	/** Die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist, null falls es eine Jahrgangsübergreifende Klasse ist */
	@Schema(description = "die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist, null falls es eine Jahrgangsübergreifende Klasse ist",
			example = "815", nullable = true)
	public Long idJahrgang;

	/** Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z). */
	@Schema(description = "das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z)", example = "B", nullable = true)
	public String parallelitaet;

	/** Die Sortierreihenfolge des Klassenlisten-Eintrags. */
	@Schema(description = "die Sortierreihenfolge des Klassenlisten-Eintrags", example = "1")
	public int sortierung;

	/** Die Liste der IDs der Klassenleitungen der Klasse. */
	@ArraySchema(schema = @Schema(implementation = Long.class))
	public @NotNull List<Long> klassenLeitungen = new ArrayList<>();

	/** Die Schüler der Klasse. */
	@ArraySchema(schema = @Schema(implementation = Long.class))
	public @NotNull List<Long> idsSchueler = new ArrayList<>();

	/** Adressmerkmal des Teilstandorts für die Klasse */
	@Schema(description = "Adressmerkmal des Teilstandorts für die Klasse", example = "A")
	public @NotNull String teilstandort = "";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KlassenStatistikGesamt() {
		// leer
	}

}
