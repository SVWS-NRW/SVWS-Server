package de.svws_nrw.core.data.enm;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu den Zentralen
 * Prüfungen der Klasse 10 für das Externe-Noten-Modul ENM. 
 */
@XmlRootElement
@Schema(description="Spezifiziert die Struktur von JSON-Daten zu den Zentralen Prüfungen der Klasse 10 für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMZP10 {

	/** Die ID des Faches der zentralen Prüfungen */
	@Schema(description = "Die ID des Faches der zentralen Prüfungen.", example="17")
	public long fachID;

	/** Das Kürzel der Vornote für dieses Fach */
	@Schema(description = "Das Kürzel der Vornote für dieses Fach.", example="D")
	public String vornote;

	/** Das Kürzel der Note,die bei der schriftlichen Prüfung erreicht wurde */
	@Schema(description = "Das Kürzel der Note,die bei der schriftlichen Prüfung erreicht wurde.", example="3+")
	public String noteSchriftlichePruefung;

	/** Gibt an, ob eine mündliche Prüfung stattfinden muss */
	@Schema(description = "Gibt an, ob eine mündliche Prüfung stattfinden muss.", example="true")
	public boolean muendlichePruefung;

	/** Gibt an, ob eine freiwillige mündliche Prüfung stattfindet */
	@Schema(description = "Gibt an, ob eine freiwillige mündliche Prüfung stattfindet.", example="false")
	public boolean muendlichePruefungFreiwillig;

	/** Das Kürzel der Note,die bei der mündlichen Prüfung erreicht wurde, sofern eine stattfindet */
	@Schema(description = "Das Kürzel der Note,die bei der mündlichen Prüfung erreicht wurde, "
			+ "sofern eine stattfindet.", example="3+")
	public String noteMuendlichePruefung;
	
	/** Das Kürzel der Abschlussnote nach der ZP10-Prüfung  */
	@Schema(description = "Das Kürzel der Abschlussnote nach der ZP10-Prüfung.", example="2")
	public String abschlussnote;

}
