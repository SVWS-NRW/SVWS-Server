package de.svws_nrw.core.data.gost.laufbahnplanung.v2;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Datenaustauschformat für die Laufbahnplanung der gymnasialen Oberstufe:
 *
 * - Die Informationen zu einem Fach.
 */
@XmlRootElement
@Schema(description = "Die Fachinformationen zu einem Fach der Gymnasialen Oberstufe.")
@TranspilerDTO
public class GostLaufbahnplanungExportV2Fach {

	/** Die ID des Faches */
	public long id = -1;

	/** Das Statistik-Kürzel des Faches */
	public @NotNull String kuerzel = "";

	/** Das Fach-Kürzel, welches zur Anzeige verwendet wird. */
	public String kuerzelAnzeige = null;

	/** Die Bezeichnung des Faches */
	public String bezeichnung = null;

	/** Die Nummer, welche die Sortierung der Fächer angibt. */
	public int sortierung = 32000;

	/** Gibt an, ob es sich um ein Fach handelt, welches relevant für die Prüfungsordnung ist oder nicht (z.B. bei der Belegprüfung). */
	public boolean istPruefungsordnungsRelevant = true;

	/** Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht */
	public boolean istFremdsprache = false;

	/** Gibt an, ob das Fache eine neu einsetzende Fremdsprache ist. */
	public boolean istFremdSpracheNeuEinsetzend = false;

	/** Gibt im Falle eines bilingualen Sachfaches das einstellige Fremdsprachenkürzel an. */
	public String biliSprache = null;


	/** Gibt an, ob das Fach als Leistungskurs im Abitur gewählt werden kann. */
	public boolean istMoeglichAbiLK = false;

	/** Gibt an, ob das Fach als Grundkurs im Abitur gewählt werden kann. */
	public boolean istMoeglichAbiGK = false;

	/** Gibt an, ob die Belegung dieses Faches in einem Halbjahr möglich ist oder nicht */
	public final @NotNull boolean[] istMoeglich = new boolean[GostHalbjahr.maxHalbjahre];

	/** Die Wochenstundenzahl des Faches in der Qualifikationsphase */
	public int wochenstundenQualifikationsphase = 3;

	/** Die Fach-ID des Referenzfaches eines Projektkurses oder Vertiefungsfaches */
	public Long referenzfach1ID = null;

	/** Die Fach-ID des zweiten Referenzfaches eines Projektkurses */
	public Long referenzfach2ID = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostLaufbahnplanungExportV2Fach() {
		// leer
	}

}
