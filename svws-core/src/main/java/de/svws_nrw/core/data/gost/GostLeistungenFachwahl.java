package de.svws_nrw.core.data.gost;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Diese Klasse stellt die Core-Types für
 * die Fachwahlen in den Leistungsdaten für die Abiturberechnung zur Verfügung.
 */
@XmlRootElement
@Schema(description = "Informationen zu den Leistungsdaten in einem Fach von einem Schüler der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostLeistungenFachwahl {

	/** Das Fach der Gymnasialen Oberstufe, welches dieser Fachwahl zugeordnet ist. */
	public GostFach fach = new GostFach();

	/** Die Nummer des Abiturfaches, sofern es sich um ein Abiturfach handelt - ansonsten null */
	public Integer abiturfach;

	/** Gibt an, ob es sich um eine neu einsetzende Fremdsprache handelt oder nicht. */
	public boolean istFSNeu = false; // pruefe durch Lesen aus den Leistungsdaten, nur true false keine gültige Belegung in der Sprachenfolge aus den Jahrgaenge vorher vorhanden ist

	/** Die einzelnen Belegungen dieses Faches */
	public final @NotNull List<GostLeistungenFachbelegung> belegungen = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostLeistungenFachwahl() {
		// leer
	}

}
