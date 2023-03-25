package de.svws_nrw.core.data.kursblockung;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten, die beim Schüler-Blockungsalgorithmus einen Kurs
 * einer Fachwahl des Schülers definiert.
 * 
 * @author Benjamin A. Bartsch */
@XmlRootElement(name = "SchuelerblockungInputKurs")
@Schema(name = "SchuelerblockungInputKurs", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten, die beim Schüler-Blockungsalgorithmus einen Kurs einer Fachwahl des Schülers definiert.")
@TranspilerDTO
public class SchuelerblockungInputKurs {

	/** Die ID des Kurses. */
	public long id = -1; // Absichtlich falscher Dummy-Wert, der geändert werden muss.

	/** Die ID des zugeordneten Faches. Beispielsweise gehört der Kurs 'D-LK1' zum Fach 'D'. */
	public long fach = -1; // Absichtlich falscher Dummy-Wert, der geändert werden muss.

	/** Die ID der zugeordneten Kursart. Beispielsweise gehört der Kurs 'D-LK1' zur Kursart 'LK'. */
	public int kursart = -1; // Absichtlich falscher Dummy-Wert, der geändert werden muss.

	/** Falls TRUE, dann darf der Schüler diesen Kurs nicht erhalten. */
	public boolean istGesperrt = false;

	/** Falls TRUE, dann muss der Schüler diesen Kurs erhalten. */
	public boolean istFixiert = false;

	/** Die Anzahl an SuS, die derzeit in diesem Kurs sind, ohne diesen Schüler. */
	public int anzahlSuS = -1; // Absichtlich falscher Dummy-Wert, der geändert werden muss.

	/** Die Schienen, die dieser Kurs belegt. In der Regel steht im Array eine Zahl (Schiene). Die Schienen sind 1-indiziert. */
	public @NotNull int[] schienen = new int[0]; // Falls das Array nicht gefüllt wird, wird es als Fehler interpretiert.

}
