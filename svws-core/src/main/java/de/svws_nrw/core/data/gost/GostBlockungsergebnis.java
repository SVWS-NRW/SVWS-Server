package de.svws_nrw.core.data.gost;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist die Core-DTO für ein Ergebnis einer Kursblockung
 */
@XmlRootElement
@Schema(description = "Informationen zu dem Ergebnis einer Blockung der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "blockungID", "name", "gostHalbjahr", "istMarkiert", "schienen", "bewertung" })
@TranspilerDTO
public class GostBlockungsergebnis {

	/** Die ID des Zwischenergebnisses der Blockung */
	public long id = -1;

	/** Die ID der Blockung */
	public long blockungID = -1;

	/** Der Name der Blockung */
	public @NotNull String name = "Blockung";

	/** Das Halbjahr, welchem die Kursblockung zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2) */
	public int gostHalbjahr = 0;

	/** Gibt an, ob dieses Ergebnis als aktiv markiert wurde. */
	public boolean istAktiv = false;

	/** Eine Liste der Schienen, welche zugeordnete Kurse beinhalten. */
	public final @NotNull List<GostBlockungsergebnisSchiene> schienen = new ArrayList<>();

	/** Die Informationen zur aktuellen Bewertung des Blockungsergebnisses */
	public @NotNull GostBlockungsergebnisBewertung bewertung = new GostBlockungsergebnisBewertung();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostBlockungsergebnis() {
		// leer
	}

}
