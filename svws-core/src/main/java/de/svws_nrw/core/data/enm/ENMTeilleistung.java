package de.svws_nrw.core.data.enm;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu den Teilleistungsdaten
 * eines Schüler in Bezug auf den Lernabschnitt für das Externe-Noten-Modul ENM.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Struktur von JSON-Daten zu den Teilleistungsdaten eines Schüler in Bezug auf den Lernabschnitt für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMTeilleistung {

	/** Die ID dieser Teilleistung in der SVWS-DB (z.B. 307956) */
	@Schema(description = "Die ID dieser Teilleistung in der SVWS-DB", example = "307956")
	public long id;

	/** Die ID der Teilleistungsart (z.B. 42) */
	@Schema(description = "Die ID der Teilleistungsart", example = "42")
	public long artID;

	/** Der Zeitstempel der letzten Änderung an der Teilleistungsart */
	@Schema(description = "Der Zeitstempel der letzten Änderung an der Teilleistungsart.", example = "2013-11-14 13:12:48.774")
	public String tsArtID;

	/** Das Datum an dem die Teilleistung erbracht bzw. festgelegt wurde. (z.B. "2020-10-10") */
	@Schema(description = "Das Datum an dem die Teilleistung erbracht bzw. festgelegt wurde", example = "2020-10-10")
	public String datum;

	/** Der Zeitstempel der letzten Änderung an dem Datum */
	@Schema(description = "Der Zeitstempel der letzten Änderung an dem Datum.", example = "2013-11-14 13:12:48.774")
	public String tsDatum;

	/** Eine Bemerkung zu der Teilleistung (z.B. "Nachgeschrieben") */
	@Schema(description = "Eine Bemerkung zu der Teilleistung", example = "Nachgeschrieben")
	public String bemerkung;

	/** Der Zeitstempel der letzten Änderung an der Bemerkung */
	@Schema(description = "Der Zeitstempel der letzten Änderung an der Bemerkung.", example = "2013-11-14 13:12:48.774")
	public String tsBemerkung;

	/** Das Noten-Kürzel für die Teilleistung (z.B. "1+") */
	@Schema(description = "Das Noten-Kürzel für die Teilleistung", example = "1+")
	public String note;

	/** Der Zeitstempel der letzten Änderung an der Note */
	@Schema(description = "Der Zeitstempel der letzten Änderung an der Note.", example = "2013-11-14 13:12:48.774")
	public String tsNote;

}
