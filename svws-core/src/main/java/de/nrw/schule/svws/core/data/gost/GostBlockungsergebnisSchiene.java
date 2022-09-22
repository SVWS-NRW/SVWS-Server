package de.nrw.schule.svws.core.data.gost;

import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ist die Core-DTO für eine Schiene eines Ergebnisses einer Kursblockung
 */
@XmlRootElement
@Schema(description="Informationen zu einer Schiene eines Ergebnisses einer Blockung der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "name", "anzahlKollisionen", "kurse" })
@TranspilerDTO
public class GostBlockungsergebnisSchiene {

	/** Die ID der Schiene */
	public long id = -1;
	
	/** Der Name der Schiene */
	public @NotNull String name = "Schiene -1";

	/** Die Anzahl der Kollisionen in dieser Schiene */
	public int anzahlKollisionen = 0;
	
	/** Eine Liste der Kurse, welche dieser Schiene zugeordnet sind. */
	public final @NotNull Vector<@NotNull GostBlockungsergebnisKurs> kurse = new Vector<>();
	
}
