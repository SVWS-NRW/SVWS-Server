package de.nrw.schule.svws.core.data.gost;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ist die Core-DTO für einen Schülerzuordnung zu einem Kurs in einem Ergebnis einer Kursblockung
 */
@XmlRootElement
@Schema(description="Informationen zu einer Schülerzuordnung zu einem Kurs in einem Ergebnis einer Blockung der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "name", "hatKollisionen" })
@TranspilerDTO
public class GostBlockungsergebnisSchuelerzuordnung {

	/** Die ID des Schülers */
	public long id = -1;
	
	/** Der Name des Schülers bestehend aus Nachname, Vorname */
	public @NotNull String name = "Mustermann, Max";

	/** Gibt an, ob bei dieser Zuordnung eine Kollision vorliegt oder nicht */
	public boolean hatKollisionen = false;

}
