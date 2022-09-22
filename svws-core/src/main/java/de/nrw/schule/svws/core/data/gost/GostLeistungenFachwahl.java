package de.nrw.schule.svws.core.data.gost;

import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse stellt die Core-Types für 
 * die Fachwahlen in den Leistungsdaten für die Abiturberechnung zur Verfügung.
 */
@XmlRootElement
@Schema(description="Informationen zu den Leistungsdaten in einem Fach von einem Schüler der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostLeistungenFachwahl {

	/** Das Fach der Gymnasialen Oberstufe, welches dieser Fachwahl zugeordnet ist. */
	public GostFach fach = new GostFach();
	
	/** Die Nummer des Abiturfaches, sofern es sich um ein Abiturfach handelt - ansonsten null */
	public Integer abiturfach;
	
	/** Gibt an, ob es sich um eine neu einsetzende Fremdsprache handelt oder nicht. */
	public boolean istFSNeu = false; // pruefe durch Lesen aus den Leistungsdaten, nur true false keine gültige Belegung in der Sprachenfolge aus den Jahrgaenge vorher vorhanden ist
	
	/** Die einzelnen Belegungen dieses Faches */
    public final @NotNull Vector<@NotNull GostLeistungenFachbelegung> belegungen = new Vector<>();

}
