package de.svws_nrw.core.data.gost;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.gost.GostKursart;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse repräsentiert einen Kurs in einer Blockung der gymnasialen Oberstufe.
 */
@XmlRootElement
@Schema(description = "Informationen zu einem Kurs der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "bezeichnung", "wochenstunden" }) // TODO Soll hier "suffix" stehen statt "bezeichnung" ? 
@TranspilerDTO
public class GostBlockungKurs {
	
	/** Die ID des Kurses */
	public long id = -1;

	/** Die ID des Faches */
	public long fach_id = -1;

	/** Die Kursart siehe auch ID von {@link GostKursart} */
	public int kursart = 0;

	/** Die Nummer des Kurses (gezählt ab 1) */
	public int nummer = 0;
	
	/** Gibt an, ob es sich um einen Kooperationskurs an einer anderen Schule handelt */
	public boolean istKoopKurs = false;
	
    /** Ein Suffix, welches einer Standard-Kursbezeichnung angehangen wird - z.B. um spezielle Kurse zu markieren. */
	public @NotNull String suffix = "";

	/** Die Anzahl der Wochenstunden, welche dem Kurs zugeordnet */
	public int wochenstunden = 3;

	/** Die Anzahl an Schienen, die der Kurs belegt, meistens =1. Falls > 1 ist es ein 'Multikurs'.*/
	public int anzahlSchienen = 1;

	/** Die Lehrer, die diesem Kurs bereits fest zugeordnet sind. */
	public @NotNull Vector<@NotNull GostBlockungKursLehrer> lehrer = new Vector<>();

}
