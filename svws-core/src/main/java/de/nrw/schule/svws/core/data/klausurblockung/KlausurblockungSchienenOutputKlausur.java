package de.nrw.schule.svws.core.data.klausurblockung;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/** Spezifiziert die grundlegende Struktur von JSON-Daten für eine Klausur und in welcher Schiene sie ist. */
@XmlRootElement(name = "KlausurblockungSchienenOutputKlausur")
@Schema(name = "KlausurblockungSchienenOutputKlausur", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für eine Klausur und in welcher Schiene sie ist.")
@TranspilerDTO
public class KlausurblockungSchienenOutputKlausur {

	/** Die Datenbank-ID der Klausur. */
	@Schema(required = true, description = "Die Datenbank-ID der Klausur.", example = "42")
	public long id = -1;

	/** Die zugeordnete Schiene dieser Klausur. Hinweis: Die Schiene ist 0-indiziert! */
	@Schema(required = true, description = "Die zugeordnete Schiene dieser Klausur. Hinweis: Die Schiene ist 0-indiziert!", example = "0")
	public int schiene = -1;

}
