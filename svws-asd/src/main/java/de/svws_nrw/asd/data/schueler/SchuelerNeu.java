package de.svws_nrw.asd.data.schueler;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieses Core-DTO beinhaltet die Information zum Anlegen eines Schuelers.
 */
@XmlRootElement
@Schema(description = "Dieses Core-DTO beinhaltet die Information zum Anlegen eines Schuelers.")
@TranspilerDTO
public class SchuelerNeu {

	// --- Stammdaten ---

	/** Der Nachname des Schülers. */
	@Schema(description = "Der Nachname des Schülers.", example = "Mustermann")
	public String nachname;

	/** Der Vorname des Schülers. */
	@Schema(description = "Der Vorname des Schülers.", example = "Max")
	public String vorname;

	/** Alle Vornamen des Schülers. */
	@Schema(description = "Alle Vornamen des Schülers.", example = "Max Moritz")
	public String alleVornamen;

	/** Das Geschlecht des Schülers. */
	@Schema(description = "Das Geschlecht des Schülers", example = "3")
	public int geschlecht;

	/** Das Geburtsdatum des Schülers. */
	@Schema(description = "Das Geburtsdatum des Schülers.", example = "2005-10-09")
	public String geburtsdatum;

	/** Der Status des Schülers.  */
	@Schema(description = "Der Status des Schülers.", example = "2")
	public int status;

	/** Das Anmeldedatum des Schülers. */
	@Schema(description = "Das Anmeldedatum des Schülers.", example = "2005-10-09")
	public String anmeldedatum;

	/** Das Aufnahmedatum des Schülers. */
	@Schema(description = "Das Aufnahmedatum des Schülers.", example = "2005-10-09")
	public String aufnahmedatum;

	/** Der Beginn des Bildungsgangs des Schülers. */
	@Schema(description = "Der Beginn des Bildungsgangs des Schülers.", example = "2005-10-09")
	public String beginnBildungsgang;

	/** Dauer des Bildungsgangs des Schülers. (nur bei BK/SB) */
	@Schema(description = "Dauer des Bildungsgangs des Schülers. (nur bei BK/SB)", example = "0")
	public Integer dauerBildungsgang;

	/** Die ID der Religion des Schülers. */
	@Schema(description = "Die ID der Religion des Schülers.", example = "11")
	public Long idReligion;

	// --- Lernabschnittsdaten ---

	/** Die ID des Schuljahresabschnitts, zu dem die Lernabschnittdaten angelegt werden. */
	@Schema(description = "Die ID des Schuljahresabschnitts, zu dem die Lernabschnittdaten angelegt werden.", example = "1")
	public long idSchuljahresabschnitt;

	/** Die ID des Jahrgangs des Schülers. */
	@Schema(description = "Die ID des Jahrgangs des Schülers.", example = "4")
	public Long idJjahrgang;

	/** Die ID der Klasse des Schülers. */
	@Schema(description = "Die ID der Klasse des Schülers.", example = "10")
	public Long idKlasse;

	// --- Schulbesuchsdaten ---

	/** Die ID der Einschulungsart (nur bei Grundschule). */
	@Schema(description = "Die ID der Einschulungsart (nur bei Grundschule).", example = "51")
	public Long idGrundschuleEinschulungsart;

}
