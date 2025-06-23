package de.svws_nrw.core.data.lernplattform.v1;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Datenstruktur eines Lehrers, innerhalb des Exports einer Lernplattform.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Datenstruktur eines Lehrers, innerhalb des Exports einer Lernplattform.")
@TranspilerDTO
public class LernplattformV1Lehrer {
	/** Die ID des Lehrers aus der SVWS-DB (z.B. 42) */
	@Schema(description = "Die ID des Lehrers aus der SVWS-DB", example = "42")
	public long id;

	/** Das Kürzel des Lehrers (z.B. Mus) */
	@Schema(description = "Das Kürzel des Lehrers", example = "MUS")
	public String kuerzel;

	/** Der Nachname des Lehrers (z.B. Mustermann) */
	@Schema(description = "Der Nachname des Lehrers.", example = "Mustermann")
	public String nachname;

	/** Der Vorname des Lehrers (z.B. Max) */
	@Schema(description = "Der Vorname des Lehrers.", example = "Max")
	public String vorname;

	/** Das Geschlecht des Lehrers (m,w,d,x) */
	@Schema(description = "Das Geschlecht des Lehrers (m - männlich, w - weiblich, d - divers, x - ohne Angabe im Geburtenregister)", example = "d")
	public String geschlecht;

	/** Die Dienst-EMail-Adresse des Lehrers */
	@Schema(description = "Die dienstliche EMail-Adresse des Lehrers.", example = "max.musterman@irgendeine.schule.nrw")
	public String emailDienstlich;

	/** Die Login-Daten des Lehrers bestehend aus EMail und Initialpasswort für die Lernplattform. */
	@Schema(description = "Logindaten des Lehrers bestehend aus Benutzername und Initialpasswort für eine Lernplattform.")
	public @NotNull LernplattformV1Login lernplattformlogin = new LernplattformV1Login();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LernplattformV1Lehrer() {
		// leer
	}
}
