package de.svws_nrw.core.data.schueler;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert ID, Nachname, Vorname, Einwilligungsart und die Anzahl der Einwilligungen des Schülers mit der angegebenen Einwilligungsart ID.
 */
@XmlRootElement
@Schema(description = "Ein Schüler-Eintrag mit der ID, Nachname, Vorname und Geschlecht.")
@TranspilerDTO
public class SchuelerEinwilligungsartenZusammenfassung {

	/** Die ID des Schülerdatensatzes. */
	@Schema(description = "die ID", example = "4711")
	public long id;

	/** Der Nachname des Schülerdatensatzes. */
	@Schema(description = "der Nachname", example = "Mustermann")
	public @NotNull String nachname = "";

	/** Der Vorname des Schülerdatensatzes. */
	@Schema(description = "der Vorname", example = "Max")
	public @NotNull String vorname = "";

	/** Die ID der entsprechenden Einwilligungsart */
	@Schema(description = "Die ID der entsprechenden Einwilligungsart", example = "8")
	public long idEinwilligungsart;

	/** Die Anzahl der Einwilligungen der entsprechenden Einwilligungsart  */
	@Schema(description = "Die Anzahl der Einwilligungen der entsprechenden Einwilligungsart", example = "8")
	public long anzahlEinwilligungen;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuelerEinwilligungsartenZusammenfassung() {
		// leer
	}

}
