package de.nrw.schule.svws.core.data.kursblockung;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten, die beim Schüler-Blockungsalgorithmus eine
 * Fachwahl des Schülers definiert. */
@XmlRootElement(name = "SchuelerblockungInputFachwahl")
@Schema(name = "SchuelerblockungInputFachwahl", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten, die beim Schüler-Blockungsalgorithmus eine Fachwahl des Schülers definiert.")
@TranspilerDTO
public class SchuelerblockungInputFachwahl {

	/** Die ID der Fachwahl. */
	public long id;

	/** Die ID des Faches. Beispielsweise des Faches 'D'. */
	public long fach;

	/** Die ID der Kursart. Beispielsweise der Kursart 'LK'. */
	public long kursart;

	/** Eine String-Darstellung der Fachwahl, damit bei Warnungen oder Fehlern dem Benutzer diese angezeigt werden kann,
	 * beispielsweise 'Mareike Musterfrau hat D;LK'. */
	public @NotNull String representation = "";

}
