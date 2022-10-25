package de.nrw.schule.svws.core.data.gost;

import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ist die Core-DTO für einen Kurs eines Ergebnisses einer Kursblockung
 */
@XmlRootElement
@Schema(description = "Informationen zu einem Kurs eines Ergebnisses einer Blockung der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "fachID", "kursart", "anzahlSchienen", "schueler", "schienen" })
@TranspilerDTO
public class GostBlockungsergebnisKurs {

	/** Die ID des Kurses */
	public long id = -1;

	/** Die ID des Kurs-Faches */
	public long fachID = -1;

	/** Die Kursart des Kurses */
	public int kursart = -1;

	/** Die Anzahl an Schienen die der Kurs belegen soll. Falls > 1 handelt es sich um einen Multikurs. */
	public int anzahlSchienen = -1;

	/** Eine Liste Schüler-IDs, welche diesem Kurs zugeordnet sind. */
	public final @NotNull Vector<@NotNull Long> schueler = new Vector<>();

	/** Die Schienen-IDs, denen der Kurs zugeordnet ist. */
	public final @NotNull Vector<@NotNull Long> schienen = new Vector<>();
}
