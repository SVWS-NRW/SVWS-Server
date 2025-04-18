package de.svws_nrw.core.data.schueler;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt eine Auswahl von Daten eines Schülereintrags aus einer Liste.
 */
@XmlRootElement
@Schema(description = "ein Eintrag eines Schülers in der Schülerliste.")
@TranspilerDTO
public class SchuelerListeEintrag {

	/** Die ID des Schülers. */
	@Schema(description = "die ID des Schülers", example = "4711")
	public long id = -1L;

	/** Der Nachname des Schülers. */
	@Schema(description = "der Nachname des Schülers", example = "Mustermann")
	public @NotNull String nachname = "";

	/** Der Vorname des Schülers. */
	@Schema(description = "der Vorname des Schülers", example = "Max")
	public @NotNull String vorname = "";

	/** Das Geschlecht des Schülers (m, w, d, x - siehe {@link Geschlecht}). */
	@Schema(description = "das Geschlecht des Schülers", example = "4711")
	public @NotNull String geschlecht = "";

	/** Das Geburtsdatum des Schülers. */
	@Schema(description = "das Geburtsdatum", example = "1911-11-11")
	public String geburtsdatum;

	/** Die ID der aktuellen Klasse des Schülers.*/
	@Schema(description = "die ID der aktuellen Klasse des Schülers", example = "47")
	public @NotNull Long idKlasse = -1L;

	/** Die ID des aktuellen Jahrgangs des Schülers.*/
	@Schema(description = "die ID des aktuellen Jahrgangs des Schülers", example = "32")
	public @NotNull Long idJahrgang = -1L;

	/** Der aktuelle Jahrgang des Schülers.*/
	@Schema(description = "der aktuelle Jahrgang des Schülers", example = "09")
	public @NotNull String jahrgang = "";

	/** Die bisherige Anzahl der Jahre in der Schuleingangssphase.*/
	@Schema(description = "die bisherige Anzahl der Jahre in der Schuleingangssphase", example = "2")
	public Integer epJahre = null;

	/** Der Abiturjahrgang, falls es sich um eine Schule mit Gymnasialer Oberstufe handelt. */
	@Schema(description = "der Abiturjahrgang, falls es sich um eine Schule mit Gymnasialer Oberstufe handelt", example = "2030")
	public Integer abiturjahrgang = null;

	/** Das Kürzel der aktuellen Schulgliederung des Schülers */
	@Schema(description = "das Kürzel der aktuellen Schulgliederung des Schülers", example = "GY9")
	public @NotNull String schulgliederung = "";

	/** Die Bezeichnung des Status des Schülers (Aktiv, Extern, etc.).*/
	@Schema(description = "die Bezeichnung des Status des Schülers (Aktiv, Extern, etc.)", example = "2")
	public int status;

	/** Gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht. */
	@Schema(description = "gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht", example = "true")
	public boolean istDuplikat;

	/** Das Schulnummer bei einem externen Schüler oder null, wenn der Schüler kein externer Schüler ist. */
	@Schema(description = "die Schulnummer eines externen Schülers oder null", example = "null")
	public String externeSchulNr;

	/** Die ID des Schuljahresabschnittes des Eintrags. */
	@Schema(description = "die ID des Schuljahresabschnittes des Eintrags", example = "14")
	public @NotNull Long idSchuljahresabschnitt = -1L;

	/** Die ID des Schuljahresabschnittes des Schülers. */
	@Schema(description = "die ID des Schuljahresabschnittes des Schülers", example = "14")
	public @NotNull Long idSchuljahresabschnittSchueler = -1L;

	/** Die Liste der IDs der belegten Kurse im aktuellen Abschnit  */
	@ArraySchema(schema = @Schema(implementation = Long.class))
	public final @NotNull List<Long> kurse = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuelerListeEintrag() {
		// leer
	}

	/**
	 * Vergleicht anhand der ID, ob das akutelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 *
	 * @return true, falls die Objekte indentisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		return (another != null) && (another instanceof SchuelerListeEintrag) && (this.id == ((SchuelerListeEintrag) another).id);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der ID.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

}
