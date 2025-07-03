package de.svws_nrw.core.data.bk.abi;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse für die einzelnen Leistungen des Schülers zu einem Fach des beruflichen Gymnasiums zur Verfügung.
 */
@XmlRootElement
@Schema(description = "Informationen zu den Leistungen zu einem Fach von einem Schüler des beruflichen Gymnasiums.")
@TranspilerDTO
public class BKGymLeistungenFach {

	/** Das Fach des beruflichen Gymnasiums, welches dieser Leistung zugeordnet ist. */
	public BKGymFach fach = new BKGymFach();

	/** Die Nummer des Abiturfaches, sofern es sich um ein Abiturfach handelt - ansonsten null */
	public Integer abiturfach;

	/** Gibt an, ob es sich um eine neu einsetzende Fremdsprache handelt oder nicht. */
	public boolean istFSNeu = false; // pruefe durch Lesen aus den Leistungsdaten, nur true false keine gültige Belegung in der Sprachenfolge aus den Jahrgaenge vorher vorhanden ist

	/** Die einzelnen Belegungen dieses Faches */
	public final @NotNull List<BKGymLeistungenFachHalbjahr> belegungen = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public BKGymLeistungenFach() {
		// leer
	}

}
