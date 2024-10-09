package de.svws_nrw.core.data.enm;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten zu
 * einem Eintrag bei einem Schüler zu einer Ankreuzkompetenz für das Externe-Noten-Modul ENM.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die grundlegende Struktur von JSON-Daten einem Eintrag bei einem Schüler zu einer Ankreuzkompetenz für das"
		+ " Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMSchuelerAnkreuzkompetenz {

	/** Die ID des Eintrages aus der SVWS-DB */
	@Schema(description = "die ID des Eintrages aus der SVWS-DB", example = "12345")
	public long id = -1;

	/** Die ID der der Ankreuzkompetenz, auf welches sich der Eintrag bezieht */
	@Schema(description = "die ID der der Ankreuzkompetenz, auf welches sich der Eintrag bezieht", example = "42")
	public Long fachID = null;

	/** Gibt an, ob Stufe 1 bei der Ankreuzkompetenz zugewiesen ist oder nicht. */
	@Schema(description = "gibt an, ob Stufe 1 bei der Ankreuzkompetenz zugewiesen ist oder nicht.", example = "false")
	public boolean stufe1 = false;

	/** Gibt an, ob Stufe 2 bei der Ankreuzkompetenz zugewiesen ist oder nicht. */
	@Schema(description = "gibt an, ob Stufe 2 bei der Ankreuzkompetenz zugewiesen ist oder nicht.", example = "true")
	public boolean stufe2 = false;

	/** Gibt an, ob Stufe 3 bei der Ankreuzkompetenz zugewiesen ist oder nicht. */
	@Schema(description = "gibt an, ob Stufe 3 bei der Ankreuzkompetenz zugewiesen ist oder nicht.", example = "true")
	public boolean stufe3 = false;

	/** Gibt an, ob Stufe 4 bei der Ankreuzkompetenz zugewiesen ist oder nicht. */
	@Schema(description = "gibt an, ob Stufe 4 bei der Ankreuzkompetenz zugewiesen ist oder nicht.", example = "false")
	public boolean stufe4 = false;

	/** Gibt an, ob Stufe 5 bei der Ankreuzkompetenz zugewiesen ist oder nicht. */
	@Schema(description = "gibt an, ob Stufe 5 bei der Ankreuzkompetenz zugewiesen ist oder nicht.", example = "false")
	public boolean stufe5 = false;

	/** Der Zeitstempel der letzten Änderung an den zugewiesenen Stufen */
	@Schema(description = "Der Zeitstempel der letzten Änderung an den zugewiesenen Stufen.", example = "2013-11-14 13:12:48.774")
	public String tsStufe;

}
