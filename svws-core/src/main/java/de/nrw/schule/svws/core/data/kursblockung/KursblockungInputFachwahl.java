package de.nrw.schule.svws.core.data.kursblockung;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für die Fachwahl (z.B. 'D;LK') eines Schüler
 * (z.B. 'Mareike Musterfrau') beim Blockungsalgorithmus. Normalerweise hat ein Schüler eine konkrete Fachwahl (z.B.
 * KR-GK) nicht doppelt. Da eine Fachwahl eine eindeutige {@code id} hat, ist der Sonderfall einer mehrfachen Fachwahl
 * dennoch möglich.
 * 
 * @author Benjamin A. Bartsch */
@XmlRootElement(name = "KursblockungInputFachwahl")
@Schema(name = "KursblockungInputFachwahl", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für eine Fachwahl beim Kursblockungsalgorithmus.")
@TranspilerDTO
public class KursblockungInputFachwahl {

	/** Die Revision der Kursblockungs-Eingabedaten, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1
	 * für Entwickler-Revisionen und ansonsten aufsteigend ab 1. */
	@Schema(required = true, description = "Die Revision der Kursblockungs-Eingabedaten, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1.", example = "1")
	public int enmRevision = -1;

	/** Die ID der Fachwahl. */
	public long id;

	/** Die ID des Schülers. Beispielsweise der Schülerin 'Mareike Musterfrau'. */
	public long schueler;

	/** Die ID des Faches. Beispielsweise des Faches 'D'. */
	public long fach;

	/** Die ID der Kursart. Beispielsweise der Kursart 'LK'. */
	public long kursart;

	/** Eine String-Darstellung der Fachwahl, damit bei Warnungen oder Fehlern dem Benutzer diese angezeigt werden kann,
	 * beispielsweise 'Mareike Musterfrau hat D;LK'. */
	public @NotNull String representation = "";

}
