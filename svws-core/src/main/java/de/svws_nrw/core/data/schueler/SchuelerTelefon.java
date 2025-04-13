package de.svws_nrw.core.data.schueler;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten einer Telefonart bei einem Schüler.
 */
@XmlRootElement
@Schema(description = "Die Daten der Telefonart eines Schülers.")
@TranspilerDTO
public class SchuelerTelefon {

	/** Die ID des Telefonnummerneintrags. */
	@Schema(description = "Die ID des Telefonnummerneintrags", example = "2", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1L;

	/** Die ID des Schülers zum Telefonnummerneintrag. */
	@Schema(description = "Die ID des Schülers zum Telefonnummerneintrag.", example = "42",  accessMode = Schema.AccessMode.READ_ONLY)
	public long idSchueler = -1L;

	/** Die ID der TelefonArt zum Telefonnummerneintrag. */
	@Schema(description = "Die ID der TelefonArt zum Telefonnummerneintrag.", example = "42")
	public long idTelefonArt = -1L;

	/** Die Telefonnummer für die TelefonArt eines Schülers */
	@Schema(description = "Die Benutzername zum Telefonnummerneintrag eines Schülers.", example = "01234-637631")
	public String telefonnummer;

	/** Die Bemerkung für die TelefonArt eines Schülers */
	@Schema(description = "Die Bemerkung zum Telefonnummerneintrag eines Schülers.", example = "Ist noch aktiv")
	public String bemerkung;

	/** Gibt die Position in der Sortierreihenfolge für die Einträge an. */
	@Schema(description = "Gibt die Position in der Sortierreihenfolge für die Einträge an", example = "1")
	public int sortierung = 1;

	/** Die Sperrung des Telefonnummerneintrags. */
	@Schema(description = "Die Sperrung des Telefonnummerneintrags.", example = "true")
	public boolean istGesperrt;
}
