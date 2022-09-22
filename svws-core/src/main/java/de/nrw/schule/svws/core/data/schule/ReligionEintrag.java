package de.nrw.schule.svws.core.data.schule;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten für den schulspezifischen Katalog der Religionen 
 * übergeben werden.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der schulspezifischen Religionen.")
@TranspilerDTO
public class ReligionEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id;

	/** Die Bezeichnung bzw. der Name der Religion. */
	@Schema(required = true, description = "die Bezeichnung bzw. der Name der Religion", example="röm.-kath.")
	public String text;
	
	/** Die Bezeichnung bzw. der Name der Religion, wie sie auf einem Zeugnis erscheint. */
	@Schema(required = true, description = "die Bezeichnung bzw. der Name der Religion, wie sie auf einem Zeugnis erscheint", example="röm.-kath.")
	public String textZeugnis;

	/** Das Kürzel des Eintrages für die Statistik. */
	@Schema(required = true, description = "das Kürzel des Eintrages für die Statistik", example="KR")
	public String kuerzel;

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(required = true, description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example="1")
	public int sortierung;
	
	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(required = true, description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example="true")
	public boolean istSichtbar;
	
	/** Gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht. */
	@Schema(required = true, description = "gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht", example="true")
	public boolean istAenderbar;

}
