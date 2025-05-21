package de.svws_nrw.core.abschluss.bk.d;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient der Fehlerrückmeldung bei einer Belegprüfung im Rahmen der Abiturberechnung.
 * Sie beinhaltet die Informationen eines einzelnen Fehlers.
 */
@XmlRootElement(name = "BKGymBelegpruefungErgebnisFehler")
@Schema(name = "BKGymBelegpruefungErgebnisFehler", description = "gibt die Informationen zu einem Fehler beim Ergebnis der Belegprüfung an.")
@TranspilerDTO
public class BKGymBelegpruefungErgebnisFehler {

	/** Ein eindeutiger Fehlercode für den Fehler */
	@Schema(description = "Ein eindeutiger Fehlercode für den Fehler.", example = "ABI_11")
	public @NotNull String code = "";

	/** Die Art des Belegungsfehlers. */
	@Schema(description = "Die Art des Belegungsfehlers.", example = "BELEGUNG")
	public @NotNull String art = "";

	/** Eine textuelle Beschreibung des Fehlers. */
	@Schema(description = "Eine textuelle Beschreibung des Fehlers.", example = "Religionslehre und Sport dürfen nicht gleichzeitig Abiturfächer sein.")
	public @NotNull String beschreibung = "";


	/**
	 * Erzeugt eine neue Instanz eines Fehlers beim Ergebnis der Belegprüfung.
	 *
	 * @param f           der Typ des Belegungsfehlers
	 */
	public BKGymBelegpruefungErgebnisFehler(final @NotNull BKGymBelegungsfehler f) {
		this.code = f.toString();
		this.art = f.getArt().kuerzel;
		this.beschreibung = f.getText();
	}

	/**
	 * Erzeugt eine neue Instanz eines Fehlers beim Ergebnis der Belegprüfung.
	 * Dieser Konstruktor dient als Default-Konstruktor für die Serialisierung / Deserialisierung
	 * aus JSON-Dateien.
	 */
	@SuppressWarnings("unused")
	private BKGymBelegpruefungErgebnisFehler() {
	}

}
