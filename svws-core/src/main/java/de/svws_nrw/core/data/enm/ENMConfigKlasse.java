package de.svws_nrw.core.data.enm;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zur Konfiguration
 * für eine Klasse bei der Zusammenstellung der im Client anzuzeigenden Daten.
 */
@XmlRootElement
@Schema(description = "Die Konfiguration für eine Klasse bei der Zusammenstellung der im Client anzuzeigenden Daten.")
@TranspilerDTO
public class ENMConfigKlasse {

	/** Die ID der Klasse aus der SVWS-DB (z.B. 16) */
	@Schema(description = "Die ID der Klasse aus der SVWS-DB.", example = "12")
	public long id;

	/** Der Zeitstempel, ab wann die Noteneingabe erlaubt ist, sofern eine Einschränkung vorliegt, sonst null. */
	@Schema(description = "Der Zeitstempel, ab wann die Noteneingabe erlaubt ist, sofern eine Einschränkung vorliegt, sonst null.",
		example = "2013-11-14 13:12:48.774")
	public String tsEingabeAb = null;

	/** Der Zeitstempel, bis wann die Noteneingabe erlaubt ist, sofern eine Einschränkung vorliegt, sonst null. */
	@Schema(description = "Der Zeitstempel, bis wann die Noteneingabe erlaubt ist, sofern eine Einschränkung vorliegt, sonst null.",
		example = "2013-11-14 13:12:48.774")
	public String tsEingabeBis = null;

	/** Gibt an, ob die Fehlstunden klassen- oder kursweise eingegeben werden. */
	@Schema(description = "Gibt an, ob die Fehlstunden klassen- oder kursweise eingegeben werden.", example = "true")
	public boolean istFehlstundenEingabeKlassenweise = false;

	/** die globale Konfiguration für die einzelnen Spalten für diese Klasse. */
	@Schema(description = "die globale Konfiguration für die einzelnen Spalten für diese Klasse.")
	public @NotNull List<ENMConfigKlasseSpalte> spalten = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ENMConfigKlasse() {
		// leer
	}

}
