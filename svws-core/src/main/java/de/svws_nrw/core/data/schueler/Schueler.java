package de.svws_nrw.core.data.schueler;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert ID, Nachname, Vorname und Geschlecht des Schülers mit der angegebenen ID.
 * Außerdem wird der aktuelle Schülerstatus und ggf. der Abschlussjahrgang des Schülers
 * angegeben.
 */
@XmlRootElement
@Schema(description = "Ein Schüler-Eintrag mit der ID, Nachname, Vorname und Geschlecht.")
@TranspilerDTO
public class Schueler {

	/** Die ID des Schülerdatensatzes. */
	@Schema(description = "die ID", example = "4711")
	public long id;

	/** Der Nachname des Schülerdatensatzes. */
	@Schema(description = "der Nachname", example = "Mustermann")
	public @NotNull String nachname = "";

	/** Der Vorname des Schülerdatensatzes. */
	@Schema(description = "der Vorname", example = "Max")
	public @NotNull String vorname = "";

	/** Die ID des Geschlechtes. Gültige Werte sind im Enum-Typ {@link Geschlecht} definiert. */
	@Schema(description = "die ID des Geschlechtes", example = "3")
	public int geschlecht;

	/** Der Status des Schülerdatensatzes. Gültige Werte sind im Enum-Typ {@link SchuelerStatus} definiert. */
	@Schema(description = "die ID des Status des Schülers (Aktiv, Extern, etc.)", example = "8")
	public int status;

	/** Ggf. der Abschlussjahrgang, dem der Schüler aktuell zugeordnet ist */
	@Schema(description = "der Abschlussjahrgang, dem der Schüler aktuell zugeordnet ist", example = "2026")
	public int abschlussjahrgang = -1;

	/** Das Schulnummer bei einem externen Schüler oder null, wenn der Schüler kein externer Schüler ist. */
	@Schema(description = "die Schulnummer eines externen Schülers oder null", example = "null")
	public String externeSchulNr;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Schueler() {
		// leer
	}

}
