package de.svws_nrw.asd.data.statistik;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse liefert die allgemeinen Angaben zu den Leistungsdaten eines Schülers zurück.
 */
@XmlRootElement
@Schema(description = "Die allgemeinen Angaben zu den Leistungsdaten eines Schülers.")
@TranspilerDTO
public class SchuelerLeistungsdatenStatistikGesamt {

	/** Die ID der Leistungsdaten in der Datenbank. */
	@Schema(description = "die ID der Leistungsdaten in der Datenbank", example = "126784")
	public long id = -1;

	/** Die ID des Faches, auf welches sich die Leistungsdaten beziehen. */
	@Schema(description = "die ID des Faches, auf welches sich die Leistungsdaten beziehen", example = "46")
	public long fachID = -1;

	/** Die ID des Kurses, auf welches sich die Leistungsdaten beziehen - bei Klassenunterricht NULL. */
	@Schema(description = "die ID des Kurses, auf welches sich die Leistungsdaten beziehen - bei Klassen unterricht NULL", example = "7732")
	public Long kursID = null;

	/** Die spezielle Kursart des Schülers, sofern Kursunterricht vorliegt. */
	@Schema(description = "die spezielle Kursart des Schülers, sofern Kursunterricht vorliegt", example = "LK1")
	public String kursart = null;

	/** Gibt an, ob es sich bei der Fachbelegung um ein Abiturfach des Schülers handelt und wenn ja, um welches (NULL, 1, 2, 3, 4) */
	@Schema(description = "gibt an, ob es sich bei der Fachbelegung um ein Abiturfach des Schülers handelt (NULL, 1, 2, 3, 4)", example = "1")
	public Integer abifach = null;

	/** Die ID des zugehörigen Fach-Lehrers. */
	@Schema(description = "die ID des Lernabschnitts des Schülers, zu dem diese Leistungsdaten gehören", example = "23")
	public Long lehrerID = null;

	/** Die Anzahl der Wochenstunden, welche das Fach unterrichtet wird. */
	@Schema(description = "die Anzahl der Wochenstunden, welche das Fach unterrichtet wird", example = "3")
	public int wochenstunden = 0;

	/** Die ID der Zusatzkraft. */
	@Schema(description = "die ID der Zusatzkraft", example = "23")
	public Long zusatzkraftID = null;

	/** Die Anzahl der Wochenstunden der Zusatzkraft. */
	@Schema(description = "die Anzahl der Wochenstunden der Zusatzkraft", example = "3")
	public int zusatzkraftWochenstunden = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuelerLeistungsdatenStatistikGesamt() {
		// leer
	}

}
