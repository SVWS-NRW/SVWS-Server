package de.svws_nrw.core.data.schueler;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.core.data.gost.GostJahrgang;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten zur Auswahlliste von Schülern.
 */
@XmlRootElement
@Schema(description = "die Daten zur Auswahlliste von Schülern")
@TranspilerDTO
public class SchuelerListe {

	/** Die ID des Schuljahresabschnitts. */
	@Schema(description = "die ID des Schuljahresabschnitts", example = "4711")
	public long idSchuljahresabschnitt = -1L;

	/** Die Listen-Einträge für die Schüler */
	@ArraySchema(schema = @Schema(implementation = SchuelerListeEintrag.class))
	public final @NotNull List<SchuelerListeEintrag> schueler = new ArrayList<>();

	/** Die Klassen-Daten */
	@ArraySchema(schema = @Schema(implementation = KlassenDaten.class))
	public final @NotNull List<KlassenDaten> klassen = new ArrayList<>();

	/** Die Kurs-Daten */
	@ArraySchema(schema = @Schema(implementation = KursDaten.class))
	public final @NotNull List<KursDaten> kurse = new ArrayList<>();

	/** Die Jahrgangs-Daten */
	@ArraySchema(schema = @Schema(implementation = JahrgangsDaten.class))
	public final @NotNull List<JahrgangsDaten> jahrgaenge = new ArrayList<>();

	/** Die Daten zu den Jahrgängen der Gymnasialen Oberstufe */
	@ArraySchema(schema = @Schema(implementation = GostJahrgang.class))
	public final @NotNull List<GostJahrgang> jahrgaengeGost = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuelerListe() {
		// leer
	}

}
