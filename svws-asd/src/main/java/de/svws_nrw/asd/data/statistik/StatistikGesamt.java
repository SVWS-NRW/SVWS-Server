package de.svws_nrw.asd.data.statistik;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse beschreibt die Struktur der Statistikdaten, welche von einer
 * Schule bei der Erfassung der amtlichen Schulstatistik übertragen werden.
 */
@XmlRootElement
@Schema(description = "Die gesamten Statistikdaten der Schule, welche von einer Schule bei der Erfassung der amtlichen Schulstatistik übertragen werden")
@TranspilerDTO
public class StatistikGesamt {

	/** Die Daten der Schule. */
	@Schema(description = "die Daten der Schule")
	public @NotNull SchuleStatistikGesamt schule = new SchuleStatistikGesamt();

	/** Die Daten der Lehrer. */
	@ArraySchema(schema = @Schema(implementation = LehrerStatistikGesamt.class,
			description = "Ein Array mit den Lehrerdaten."))
	public @NotNull List<LehrerStatistikGesamt> lehrer = new ArrayList<>();

	/** Die Daten der Klassen. */
	@ArraySchema(schema = @Schema(implementation = KlassenStatistikGesamt.class,
			description = "Ein Array mit den Klassen."))
	public @NotNull List<KlassenStatistikGesamt> klassen = new ArrayList<>();

	/** Die Daten der Schüler. */
	@ArraySchema(schema = @Schema(implementation = SchuelerStatistikGesamt.class,
			description = "Ein Array mit den Schülerdaten."))
	public @NotNull List<SchuelerStatistikGesamt> schueler = new ArrayList<>();

	/** Der Katalog der Jahrgänge. */
	@ArraySchema(schema = @Schema(implementation = JahrgaengeStatistikGesamt.class,
			description = "Ein Array mit den Jahrgängen."))
	public @NotNull List<JahrgaengeStatistikGesamt> jahrgaenge = new ArrayList<>();

	/** Der Katalog der Orte. */
	@ArraySchema(schema = @Schema(implementation = OrteStatistikGesamt.class,
			description = "Ein Array mit den Orten."))
	public @NotNull List<OrteStatistikGesamt> orte = new ArrayList<>();

	/** Der Katalog der Förderschwerpunkte. */
	@ArraySchema(schema = @Schema(implementation = FoerderschwerpunktStatistikGesamt.class,
			description = "Ein Array mit den Förderschwerpunkten."))
	public @NotNull List<FoerderschwerpunktStatistikGesamt> foederschwerpunkte = new ArrayList<>();

	/** Der Katalog der Religionen. */
	@ArraySchema(schema = @Schema(implementation = ReligionStatistikGesamt.class,
			description = "Ein Array mit den Religionen."))
	public @NotNull List<ReligionStatistikGesamt> religionen = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public StatistikGesamt() {
		// leer
	}

}
