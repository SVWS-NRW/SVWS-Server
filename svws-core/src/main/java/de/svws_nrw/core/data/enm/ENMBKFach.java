package de.svws_nrw.core.data.enm;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu den Fächern
 * eines Schülers für das Externe-Noten-Modul ENM in Bezug auf Abschlüsse
 * am Berufskolleg. 
 */
@XmlRootElement
@Schema(description="Spezifiziert die Struktur von JSON-Daten zu den Fächern eines Schülers für das Externe-Noten-Modul ENM in Bezug auf Abschlüsse am Berufskolleg.")
@TranspilerDTO
public class ENMBKFach {

	/** Die ID des Faches der zentralen Prüfungen. */
	@Schema(required = true, description = "Die ID des Faches der zentralen Prüfungen.", example="42")
	public long fachID;

	/** Die ID des Lehrers, der das Prüfungsfach unterrichtet. */
	@Schema(required = true, description = "Die ID des Lehrers, der das Prüfungsfach unterrichtet.", example="42")
	public long lehrerID;

	/** Gibt an, ob das Fach schriftlich ist. */
	@Schema(required = true, description = "Gibt an, ob das Fach schriftlich ist. ", example="false")
	public boolean istSchriftlich;

	/** Das Notenkürzel der Vornote für dieses Fach. */
	@Schema(required = true, description = "Das Notenkürzel der Vornote für dieses Fach.", example="4+")
	public String vornote;

	/** Das Notenkürzel der Note, die bei der schriftlichen Prüfung erreicht wurde. */
	@Schema(required = true, description = "Das Notenkürzel der Note, die bei der schriftlichen Prüfung erreicht wurde.", example="3-")
	public String noteSchriftlichePruefung;

	/** Gibt an, ob eine mündliche Prüfung stattfinden muss. */
	@Schema(required = true, description = "Gibt an, ob eine mündliche Prüfung stattfinden muss.", example="false")
	public boolean muendlichePruefung;

	/** Gibt an, ob eine freiwillige mündliche Prüfung stattfindet. */
	@Schema(required = true, description = "Gibt an, ob eine freiwillige mündliche Prüfung stattfindet.", example="true")
	public boolean muendlichePruefungFreiwillig;

	/** Das Notenkürzel der Note, die bei der mündlichen Prüfung erreicht wurde, sofern eine stattfindet. */
	@Schema(required = true, description = "Das Notenkürzel der Note, die bei der mündlichen Prüfung erreicht wurde, sofern eine stattfindet.", example="3+")
	public String noteMuendlichePruefung;

	/** Gibt an, ob das Fach in Bezug auf den Berufsabschluss schriftlich ist. */
	@Schema(required = true, description = "Gibt an, ob das Fach in Bezug auf den Berufsabschluss schriftlich ist.", example="true")
	public boolean istSchriftlichBerufsabschluss;

	/** Die Note in Bezug auf den Berufsabschluss. */
	@Schema(required = true, description = "Die Note in Bezug auf den Berufsabschluss.", example="3+")
	public String noteBerufsabschluss;

	/** Das Notenkürzel der Abschlussnote nach der Prüfung. */
	@Schema(required = true, description = "Das Notenkürzel der Abschlussnote nach der Prüfung.", example="2-")
	public String abschlussnote;

}
