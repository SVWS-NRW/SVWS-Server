package de.svws_nrw.core.data.bk.abi;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse stellt die Core-Types für Eigenschaften einen Oberstufenfaches für die Abiturberechnung
 * zur Verfügung.
 */
@XmlRootElement
@Schema(description = "Informationen zu einem Fach des beruflichen Gymnasiums.")
@TranspilerDTO
public class BKGymFach {

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

	/** Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht */
	public boolean istFremdsprache = false;

	/** Gibt an, ob das Fache eine neu einsetzende Fremdsprache ist. */
	public boolean istFremdSpracheNeuEinsetzend = false;

	/** Gibt im Falle eines bilingualen Sachfaches das einstellige Fremdsprachenkürzel an. */
	public String biliSprache = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public BKGymFach() {
		// leer
	}

}
