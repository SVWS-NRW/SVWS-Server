package de.svws_nrw.core.data.stundenplan;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert Informationen über die Konfigurationen aller Stundenpläne (Default-Werte).
 */
@XmlRootElement
@Schema(description = "Eine Konfiguration, welche für alle Stundenpläne gilt (Default-Werte).")
@TranspilerDTO
public class StundenplanKonfiguration {

	/** Der Default-Wert für den Unterrichtsbeginn. */
	@Schema(description = "Der Default-Wert für den Unterrichtsbeginn.", example = "480 (8:00 Uhr)")
	public int defaultUnterrichtsbeginn = 8 * 60; // 8:00 Uhr

	/** Der Default-Wert für die Dauer einer Unterrichtsstunde. */
	@Schema(description = "Der Default-Wert für die Dauer einer Unterrichtsstunde.", example = "45 (Minuten)")
	public int defaultStundendauer = 45; // 45 Minuten

	/** Der Default-Wert für die Zeit zwischen zwei Unterrichtsstunden die für Raumwechsel gilt. */
	@Schema(description = "Der Default-Wert für die Zeit zwischen zwei Unterrichtsstunden die für Raumwechsel gilt.", example = "5 (Minuten)")
	public int defaultPausenzeitFuerRaumwechsel = 5; // 5 Minuten

	/** Der Default-Wert des Beginns der 1. Vormittagspause. */
	@Schema(description = "Der Default-Wert des Beginns der 1. Vormittagspause.", example = "2 (nach der 2. Stunde)")
	public int defaultVormittagspause1Nach = 2;  // Die 1. Pause ist nach der 2. Stunde

	/** Der Default-Wert der Dauer der 1. Vormittagspause. */
	@Schema(description = "Der Default-Wert der Dauer der 1. Vormittagspause.", example = "25 (Minuten)")
	public int defaultVormittagspause1Dauer = 25; // Die 1. Pause dauert 25 Minuten.

	/** Der Default-Wert des Beginns der 2. Vormittagspause. */
	@Schema(description = "Der Default-Wert des Beginns der 2. Vormittagspause.", example = "4 (nach der 4. Stunde)")
	public int defaultVormittagspause2Nach = 4;  // Die 2. Pause ist nach der 4. Stunde

	/** Der Default-Wert der Dauer der 2. Vormittagspause. */
	@Schema(description = "Der Default-Wert der Dauer der 2. Vormittagspause.", example = "25 (Minuten)")
	public int defaultVormittagspause2Dauer = 25; // Die 2. Pause dauert 25 Minuten.

	/** Der Default-Wert des Beginns der Mittagspause. */
	@Schema(description = "Der Default-Wert des Beginns der Mittagspause.", example = "6 (nach der 6. Stunde)")
	public int defaultMittagspauseNach = 6;  // Die Mittagspause ist nach der 6. Stunde

	/** Der Default-Wert der Dauer der Mittagspause. */
	@Schema(description = "Der Default-Wert der Dauer der Mittagspause.", example = "60 (Minuten)")
	public int defaultMittagspauseDauer = 60; // Die Mittagspause dauert 60 Minuten.

	/**
	 * Leerer Standardkonstruktor.
	 */
	public StundenplanKonfiguration() {
		// leer
	}

}
