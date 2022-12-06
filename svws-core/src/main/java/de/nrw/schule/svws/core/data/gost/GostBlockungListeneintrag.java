package de.nrw.schule.svws.core.data.gost;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ist die Core-DTO für eine Kursblockung
 */
@XmlRootElement
@Schema(description="Informationen zu einer Blockung der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "name", "gostHalbjahr" })
@TranspilerDTO
public class GostBlockungListeneintrag {
	
	/** Die ID der Blockung */
	public long id = -1;
	
	/** Der Name der Blockung */
	public @NotNull String name = "Neue Blockung";

	/** Das Halbjahr, welchem die Kursblockung zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2) */
	public int gostHalbjahr = 0;
	
	/** Gibt an, ob diese Blockung aktiviert wurde, d.h. bereits in die Leistungsdaten übertragen wurde. */
	public boolean istAktiv = false;
	
}
