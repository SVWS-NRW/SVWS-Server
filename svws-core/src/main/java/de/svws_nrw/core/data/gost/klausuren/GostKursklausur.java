package de.svws_nrw.core.data.gost.klausuren;

import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einer Gost-Kursklausur.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer Gost-Kursklausur")
@TranspilerDTO
public class GostKursklausur {

	/** Die ID der Kursklausur. */
	@Schema(description = "die ID der Kursklausur", example = "815")
	public long id = -1;

	/** Die ID der Klausur-Vorgabe. */
	@Schema(description = "die ID der Klausur-Vorgabe", example = "4711")
	public long idVorgabe = -1;

	/** Das Jahr, in welchem der Jahrgang Abitur machen wird. */
	@Schema(description = "das Jahr, in welchem der Jahrgang Abitur machen wird", example = "2025")
	public int abijahr = -1;

	/** Das Gost-Halbjahr, in dem die Klausurg geschrieben wird. */
	@Schema(description = "das Gost-Halbjahr, in dem die Klausurg geschrieben wird", example = "3")
	public int halbjahr = -1;

	/** Das Quartal, in welchem die Klausur gechrieben wird. */
	@Schema(description = "das Quartal, in welchem die Klausur gechrieben wird", example = "1")
	public int quartal = -1;

	/** Die ID des Faches. */
	@Schema(description = "die ID des Faches", example = "2242")
	public long idFach = -1;

	/** Das Kürzel einer verallgemeinerten Kursart. */
	@Schema(description = "das Kürzel einer verallgemeinerten Kursart", example = "GK")
	public @NotNull String kursart = "";

	/** Die Dauer der Klausur in Minuten. */
	@Schema(description = "die Dauer der Klausur in Minuten", example = "135")
	public int dauer = -1;

	/** Die Auswahlzeit in Minuten, sofern vorhanden. */
	@Schema(description = "die Auswahlzeit in Minuten, sofern vorhanden", example = "30")
	public int auswahlzeit = -1;

	/** Die Information, ob es sich um eine mündliche Prüfung handelt. */
	@Schema(description = "die Information, ob es sich um eine mündliche Prüfung handelt", example = "false")
	public boolean istMdlPruefung = false;

	/** Die Information, ob Audioequipment nötig ist, z.B. für Klasuren mit Hörverstehensanteilen. */
	@Schema(description = "die Information, ob Audioequipment nötig ist, z.B. für Klasuren mit Hörverstehensanteilen", example = "false")
	public boolean istAudioNotwendig = false;

	/** Die Information, ob Videoequipment nötig ist, z.B. für Klasuren mit Videoanalyse. */
	@Schema(description = "die Information, ob Videoequipment nötig ist, z.B. für Klasuren mit Videoanalyse", example = "false")
	public boolean istVideoNotwendig = false;

	/** Die textuelle Bemerkung zur Klausurvorgabe, sofern vorhanden. */
	@Schema(description = "die textuelle Bemerkung zur Klausurvorgabe, sofern vorhanden", example = "Zentrale Vergleichsklausur")
	public String bemerkungVorgabe = null;

	/** Die ID des Klausurkurses. */
	@Schema(description = "die ID des Klausurkurses", example = "9876")
	public long idKurs = -1;

	/** Die Kurzbezeichnung des Klausurkurses. */
	@Schema(description = "die Kurzbezeichnung des Klausurkurses", example = "BI-GK1")
	public String kursKurzbezeichnung = "";

	/** Die Schiene des Kurses. */
	@Schema(description = "die Schienen des Kurses", example = "{5, 4, 1}")
	public @NotNull int[] kursSchiene = {};

	/** Die ID des Kurslehrers. */
	@Schema(description = "die ID des Kurslehrers", example = "175")
	public long idLehrer = -1;

	/** Die ID des Klausurtermins, sofern schon gesetzt. */
	@Schema(description = "die ID des Klausurtermins, sofern schon gesetzt", example = "5555")
	public Long idTermin = null;

	/** Die Startzeit der Klausur, sofern abweichend von Startzeit des gesamten Termins. */
	@Schema(description = "die Startzeit der Klausur, sofern abweichend von Startzeit des gesamten Termins", example = "09:00:00")
	public String startzeit = null;

	/** Die Liste der IDs der zugehörigen Schüler. */
	@Schema(description = "die Liste der IDs der zugehörigen Schüler", example = "[ 5590, 5591, 5592, ... ]")
	public @NotNull List<@NotNull Long> schuelerIds = new ArrayList<>();

}
