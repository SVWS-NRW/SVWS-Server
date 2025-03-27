package de.svws_nrw.asd.data.schule;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.schueler.SchuelerBetriebsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.data.schueler.Sprachendaten;
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
public class SchuleStatistikdatenGesamt {

	/** Die Stammdaten der Schule */
	@Schema(description = "die Stammdaten der Schule")
	public @NotNull SchuleStammdaten stammdaten = new SchuleStammdaten();

	/** Die Stammdaten der Lehrer. */
	@ArraySchema(schema = @Schema(implementation = LehrerStammdaten.class,
			description = "Ein Array mit den Lehrerstammdaten."))
	public @NotNull List<LehrerStammdaten> lehrerStammdaten = new ArrayList<>();

	/** Die Personaldaten der Lehrer. */
	@ArraySchema(schema = @Schema(implementation = LehrerPersonaldaten.class,
			description = "Ein Array mit den Personaldaten der Lehrer."))
	public @NotNull List<LehrerPersonaldaten> lehrerPersonaldaten = new ArrayList<>();

	/** Die allgemeinen Angaben zu dem Lernabschnitt der Schüler. */
	@ArraySchema(schema = @Schema(implementation = SchuelerLernabschnittsdaten.class,
			description = "Ein Array mit den allgemeinen Angaben zu dem Lernabschnitt der Schüler."))
	public @NotNull List<SchuelerLernabschnittsdaten> schuelerLernabschnittsdaten = new ArrayList<>();

	/** Die Schulbesuchsdaten der Schüler. */
	@ArraySchema(schema = @Schema(implementation = SchuelerSchulbesuchsdaten.class,
			description = "Ein Array mit den Schulbesuchsdaten der Schüler."))
	public @NotNull List<SchuelerSchulbesuchsdaten> schuelerSchulbesuchsdaten = new ArrayList<>();

	/** Die Betriebsdaten der Schüler in einem Betrieb. */
	@ArraySchema(schema = @Schema(implementation = SchuelerBetriebsdaten.class,
			description = "Ein Array mit den Betriebsdaten der Schüler in einem Betrieb."))
	public @NotNull List<SchuelerBetriebsdaten> schuelerBetriebsdaten = new ArrayList<>();

	/** Die Informationen zu den Sprachbelegungen und den Sprachprüfungen der Schüler. */
	@ArraySchema(schema = @Schema(implementation = Sprachendaten.class,
			description = "Ein Array mit den Informationen zu den Sprachbelegungen und den Sprachprüfungen der Schüler."))
	public @NotNull List<Sprachendaten> schuelerSprachendaten = new ArrayList<>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuleStatistikdatenGesamt() {
		// leer
	}

}
