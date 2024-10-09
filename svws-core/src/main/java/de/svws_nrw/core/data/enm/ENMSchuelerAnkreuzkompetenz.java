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

	/** Die zugewiesene Stufe (1-5) bei der Ankreuzkompetenz oder null, falls noch keine Stufe zugewiesen wurde. */
	@Schema(description = "die zugewiesene Stufe (1-5) bei der Ankreuzkompetenz oder null, falls noch keine Stufe zugewiesen wurde.", example = "2")
	public Integer stufe = null;

	/** Der Zeitstempel der letzten Änderung an der zugewiesenen Stufe */
	@Schema(description = "Der Zeitstempel der letzten Änderung an der zugewiesenen Stufe.", example = "2013-11-14 13:12:48.774")
	public String tsStufe;

}
