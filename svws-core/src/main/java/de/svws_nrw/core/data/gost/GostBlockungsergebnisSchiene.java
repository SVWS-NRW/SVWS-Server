package de.svws_nrw.core.data.gost;

import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ist die Core-DTO für eine Schiene eines Ergebnisses einer Kursblockung
 */
@XmlRootElement
@Schema(description="Informationen zu einer Schiene eines Ergebnisses einer Blockung der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "kurse" })
@TranspilerDTO
public class GostBlockungsergebnisSchiene {

	/** Die ID der Schiene */
	public long id = -1;
	
	/** Eine Liste der Kurse, welche dieser Schiene zugeordnet sind. */
	public final @NotNull Vector<@NotNull GostBlockungsergebnisKurs> kurse = new Vector<>();
	
}
