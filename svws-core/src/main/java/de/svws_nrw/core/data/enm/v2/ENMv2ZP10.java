package de.svws_nrw.core.data.enm.v2;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu den Zentralen
 * Prüfungen der Klasse 10 für das Externe-Noten-Modul ENM.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Struktur von JSON-Daten zu den Zentralen Prüfungen der Klasse 10 für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMv2ZP10 {

	/** Die ID des ZP10-Eintrags */
	@Schema(description = "Die ID des ZP10-Eintrags.", example = "17")
	public long id;

	/** Die ID des Faches der zentralen Prüfungen */
	@Schema(description = "Die ID des Faches der zentralen Prüfungen.", example = "17")
	public long idFach;

	/** Die ID des Fachlehrers */
	@Schema(description = "Die ID des Fachlehrers.", example = "17")
	public long idLehrer;

	/** Das Kürzel der Vornote für dieses Fach */
	@Schema(description = "Das Kürzel der Vornote für dieses Fach.", example = "D")
	public String vornote;

	/** Der Zeitstempel der letzten Änderung an der Vornote */
	@Schema(description = "Der Zeitstempel der letzten Änderung an der Vornote.", example = "2013-11-14 13:12:48.774")
	public String tsVornote;

	/** Das Kürzel der Note, die bei der schriftlichen Prüfung erreicht wurde */
	@Schema(description = "Das Kürzel der Note, die bei der schriftlichen Prüfung erreicht wurde.", example = "3+")
	public String noteSchriftlichePruefung;

	/** Der Zeitstempel der letzten Änderung an der Note der schriftlichen Prüfung */
	@Schema(description = "Der Zeitstempel der letzten Änderung an der Note der schriftlichen Prüfung.", example = "2013-11-14 13:12:48.774")
	public String tsNoteSchriftlichePruefung;

	/** Gibt an, ob eine mündliche Prüfung stattfinden muss */
	@Schema(description = "Gibt an, ob eine mündliche Prüfung stattfinden muss.", example = "true")
	public boolean muendlichePruefung;

	/** Der Zeitstempel der letzten Änderung an der Information, ob eine mündlichen Prüfung stattfinden muss */
	@Schema(description = "Der Zeitstempel der letzten Änderung an der Information, ob eine mündlichen Prüfung stattfinden muss.", example = "2013-11-14 13:12:48.774")
	public String tsMuendlichePruefung;

	/** Gibt an, ob eine freiwillige mündliche Prüfung stattfindet */
	@Schema(description = "Gibt an, ob eine freiwillige mündliche Prüfung stattfindet.", example = "false")
	public boolean muendlichePruefungFreiwillig;

	/** Der Zeitstempel der letzten Änderung an der Information, ob eine mündlichen Prüfung freiwillig stattfinden soll */
	@Schema(description = "Der Zeitstempel der letzten Änderung an der Information, ob eine mündlichen Prüfung freiwillig stattfinden soll.", example = "2013-11-14 13:12:48.774")
	public String tsMuendlichePruefungFreiwillig;

	/** Das Kürzel der Note,die bei der mündlichen Prüfung erreicht wurde, sofern eine stattfindet */
	@Schema(description = "Das Kürzel der Note,die bei der mündlichen Prüfung erreicht wurde, "
			+ "sofern eine stattfindet.", example = "3+")
	public String noteMuendlichePruefung;

	/** Der Zeitstempel der letzten Änderung an der Note der mündlichen Prüfung */
	@Schema(description = "Der Zeitstempel der letzten Änderung an der Note der mündlichen Prüfung.", example = "2013-11-14 13:12:48.774")
	public String tsNoteMuendlichePruefung;

	/** Das Kürzel der Abschlussnote nach der ZP10-Prüfung  */
	@Schema(description = "Das Kürzel der Abschlussnote nach der ZP10-Prüfung.", example = "2")
	public String abschlussnote;

	/** Der Zeitstempel der letzten Änderung an der Abschlussnote */
	@Schema(description = "Der Zeitstempel der letzten Änderung an der Abschlussnote.", example = "2013-11-14 13:12:48.774")
	public String tsAbschlussnote;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public ENMv2ZP10() {
		// leer
	}

}
