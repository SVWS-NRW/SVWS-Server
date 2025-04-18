package de.svws_nrw.core.data.kataloge;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten für den schul-spezifischen reduzierten Katalog der Schulen
 * übergeben werden.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem schul-spezifischen reduzierten Katalog der Schulen.")
@TranspilerDTO
public class SchulEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Ein Kürzel, welches der Schule zugeordnet ist. */
	@Schema(description = "ein Kürzel, welches der Schule zugeordnet ist", example = "EKS")
	public String kuerzel;

	/** Eine Kurzbezeichnung für die Schule. */
	@Schema(description = "eine Kurzbezeichnung für die Schule", example = "Erich-Kästner-Schule")
	public String kurzbezeichnung;

	/** Die Statistik-Schulnummer der Schule */
	@Schema(description = "Die Statistik-Schulnummer der Schule", example = "989123")
	public String schulnummerStatistik;

	/** Der Name des Schule. */
	@Schema(description = "der Name des Schule", example = "Erich-Kästner-Realschule der Stadt Düsseldorf - Sekundarstufe I -")
	public @NotNull String name = "";

	/** Die ID der Schulform. */
	@Schema(description = "die ID der Schulform", example = "1")
	public Long idSchulform = null;

	/** Der Straßenname der Straße in der die Schule liegt. */
	@Schema(description = "der Straßenname der Straße in der die Schule liegt.", example = "Musterweg")
	public String strassenname;

	/** Die Hausnummer zur Straße in der die Schule liegt. */
	@Schema(description = "Ggf. die Hausnummer zur Straße in der die Schule liegt.", example = "4711")
	public String hausnummer;

	/** Ggf. der Hausnummerzusatz zur Straße in der die Schule liegt. */
	@Schema(description = "Ggf. der Hausnummerzusatz zur Straße in der die Schule liegt.", example = "a-d")
	public String zusatzHausnummer;

	/** Die Postleitzahl des Gebietes in dem die Schule liegt. */
	@Schema(description = "die Postleitzahl der Schule", example = "42287")
	public String plz;

	/** Der Ort in dem die Schule liegt. */
	@Schema(description = "der Ort der Schule", example = "Düsseldorf")
	public String ort;

	/** Die Telefonnummer der Schule. */
	@Schema(description = "die Telefonnummer der Schule", example = "0211-58670")
	public String telefon;

	/** Die Faxnummer der Schule. */
	@Schema(description = "die Faxnummer der Schule", example = "0211-58671")
	public String fax;

	/** Die Mailadresse der Schule. */
	@Schema(description = "die Mailadresse der Schule", example = "info@schule.de")
	public String email;

	/** Der Name des/der Schuleiters/Schulleiterin. */
	@Schema(description = "der Name des/der Schuleiters/Schulleiterin", example = "")
	public String schulleiter;

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public int sortierung = 32000;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchulEintrag() {
		// leer
	}

}
