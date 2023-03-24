package de.nrw.schule.svws.core.abschluss.gost;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient der Fehlerrückmeldung bei einer Belegprüfung im Rahmen der Abiturberechnung.
 * Sie beinhaltet die Informationen eines einzelnen Fehlers.   
 */
@XmlRootElement(name = "GostBelegpruefungErgebnisFehler")
@Schema(name="GostBelegpruefungErgebnisFehler", description="gibt die Informationen zu einem Fehler beim Ergebnis der Belegprüfung an.")
@TranspilerDTO
public class GostBelegpruefungErgebnisFehler {
	
	/** Ein eindeutiger Fehlercode für den Fehler */
	@Schema(required = true, description = "Ein eindeutiger Fehlercode für den Fehler.", example="ABI_11")
	public @NotNull String code = "";
	
	/** Die Art des Belegungsfehlers (siehe {@link GostBelegungsfehlerArt}). */
	@Schema(required = true, description = "Die Art des Belegungsfehlers.", example="BELEGUNG")
	public @NotNull String art = "";
	
	/** Eine textuelle Beschreibung des Fehlers. */
	@Schema(required = true, description = "Eine textuelle Beschreibung des Fehlers.", example="Religionslehre und Sport dürfen nicht gleichzeitig Abiturfächer sein.")
	public @NotNull String beschreibung = "";
	
	
	/**
	 * Erzeugt eine neue Instanz eines Fehlers beim Ergebnis der Belegprüfung.
	 * 
	 * @param f           der Typ des Belegungsfehlers (siehe {@link GostBelegungsfehler})
	 * @param pruef_art   die Art der durchgeführten Belegungsprüfung (siehe {@link GostBelegpruefungsArt}), um 
	 *                    die konkrete Ausprägung des Textinformationen bestimmen zu können.
	 */
	public GostBelegpruefungErgebnisFehler(final @NotNull GostBelegungsfehler f, final @NotNull GostBelegpruefungsArt pruef_art) {
		this.code = f.toString();
		this.art = f.getArt().kuerzel;
		this.beschreibung = f.getText(pruef_art);
	}

	/**
	 * Erzeugt eine neue Instanz eines Fehlers beim Ergebnis der Belegprüfung.
	 * Dieser Konstruktor dient als Default-Konstruktor für die Serialisierung / Deserialisierung
	 * aus JSON-Dateien.
	 */
	@SuppressWarnings("unused")
	private GostBelegpruefungErgebnisFehler() {
	}
	
}
