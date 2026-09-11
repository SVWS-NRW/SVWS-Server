package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse beschreibt die Optionen für den Import von Schülern in einen
 * UV-Planungsabschnitt.
 */
@XmlRootElement
@Schema(description = "die Optionen für den Import von Schülern in einen UV-Planungsabschnitt.")
@TranspilerDTO
public class UvSchuelerImportOptions {

	/** Die ID des Schuljahresabschnitts, aus dem importiert werden soll. */
	@Schema(description = "die ID des Schuljahresabschnitts, aus dem importiert werden soll", example = "42")
	@Positive(message = "Die ID des Schuljahresabschnitts darf nicht negativ sein.")
	public long idSchuljahresabschnitt = -1;

	/**
	 * Gibt an, wie mit Jahrgängen umgegangen werden soll.
	 * Gibt an, ob der Folgejahrgang des Quell-Jahrgangs verwendet werden soll.
	 */
	@Schema(description = "gibt an, ob der Folgejahrgang verwendet werden soll", example = "false")
	public boolean folgejahrgang = false;

	/**
	 * Gibt an, wie mit Klassenzuweisungen umgegangen werden soll.
	 * Gibt an, ob Klassenzuweisungen übernommen werden sollen.
	 */
	@Schema(description = "gibt an, ob Klassenzuweisungen übernommen werden sollen", example = "true")
	public boolean klassenzuweisungenUebernehmen = true;

	/**
	 * Legt fest, ob beim Import in den Folgejahrgang die Versetzungsvermerke
	 * und die individuelle Folgeklasse eines Schülers berücksichtigt werden.
	 */
	@Schema(description = "legt fest, ob Versetzungsvermerke berücksichtigt werden", example = "true")
	public boolean versetzungsvermerkeBeruecksichtigen = true;

	/** Gibt an, ob fehlende UV-Klassen im Ziel-Planungsabschnitt automatisch angelegt werden sollen. */
	@Schema(description = "gibt an, ob fehlende UV-Klassen im Ziel-Planungsabschnitt automatisch angelegt werden sollen", example = "false")
	public boolean createMissingKlassen = false;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvSchuelerImportOptions() {
		// leer
	}

}
