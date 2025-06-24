package de.svws_nrw.core.data.lernplattform.v1;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Datenstruktur eines Schülers in Bezug auf den Lernabschnitt, innerhalb des Exports einer Lernplattform.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Datenstruktur eines Schülers in Bezug auf den Lernabschnitt, innerhalb des Exports einer Lernplattform.")
@TranspilerDTO
public class LernplattformV1Schueler {

	/** Die ID des Schülers in der SVWS-DB */
	@Schema(description = "Die ID des Schülers in der SVWS-DB.", example = "12345")
	public long id;

	/** Die ID des aktuellen Jahrgangs, in dem sich der Schüler befindet. */
	@Schema(description = "Die ID des aktuellen Jahrgangs, in dem sich der Schüler befindet.", example = "13")
	public long idJahrgang;

	/** Die ID der aktuellen Klasse, in der sich der Schüler befindet. */
	@Schema(description = "Die ID der aktuellen Klasse, in der sich der Schüler befindet.", example = "42")
	public long idKlasse;

	/** Der Nachname des Schülers (z.B. Mustermann) */
	@Schema(description = "Der Nachname des Schülers", example = "Mustermann")
	public String nachname;

	/** Der Vorname des Schülers (z.B. Max) */
	@Schema(description = "Der Vorname des Schülers", example = "Max")
	public String vorname;

	/** Das Geschlecht des Schülers (m,w,d,x) */
	@Schema(description = "Das Geschlecht des Schülers (m - männlich, w - weiblich, d - divers, x - ohne Angabe im Geburtenregister)", example = "d")
	public String geschlecht;

	/** Logindaten des Schülers bestehend aus Benutzername und Initialpasswort. */
	@Schema(description = "Logindaten des Schülers bestehend aus Benutzername und Initialpasswort.")
	public @NotNull LernplattformV1Login lernplattformlogin = new LernplattformV1Login();

	/** Die IDs der Lerngruppen des Schülers in dem Lernabschnitt. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit IDs der "
			+ "Lerngruppen des Schülers in dem Lernabschnitt"))
	public @NotNull List<Long> idsLerngruppen = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LernplattformV1Schueler() {
		// leer
	}
}
