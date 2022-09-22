package de.nrw.schule.svws.core.data.gost;

import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ist die Core-DTO für einen Kurs eines Ergebnisses einer Kursblockung
 */
@XmlRootElement
@Schema(description="Informationen zu einem Kurs eines Ergebnisses einer Blockung der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "schienenID", "name", "anzahlKollisionen", "schueler" })
@TranspilerDTO
public class GostBlockungsergebnisKurs {

	/** Die ID des Kurses */
	public long id = -1;
	
	/** Die ID der Schiene oder null, falls der Kurs keiner Schiene zugeordnet ist */
	public Long schienenID = null;
	
	/** Die ID des Kurs-Faches */
	public long fachID = -1;
	
	/** Die Kursart des Kurses */
	public String kursart = "GK";
	
	/** Der Name des Kurses */
	public @NotNull String name = "D-GK1";

	/** Die Anzahl der Kollisionen in dieser Schiene */
	public int anzahlKollisionen = 0;
	
	/** Eine Liste der Schüler, welche diesem Kurs zugeordnet sind. */
	public final @NotNull Vector<@NotNull GostBlockungsergebnisSchuelerzuordnung> schueler = new Vector<>();
	
}
