package de.svws_nrw.core.data.gost;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist die Core-DTO für einen Kurs eines Ergebnisses einer Kursblockung
 */
@XmlRootElement
@Schema(description = "Informationen zu einem Kurs eines Ergebnisses einer Blockung der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "fachID", "kursart", "anzahlSchienen", "schueler", "schienen" })
@TranspilerDTO
public final class GostBlockungsergebnisKurs {

	/** Die ID des Kurses */
	public long id = -1;

	/** Die ID des Kurs-Faches */
	public long fachID = -1;

	/** Die Kursart des Kurses */
	public int kursart = -1;

	/** Die Anzahl an Schienen die der Kurs belegen soll. Falls > 1 handelt es sich um einen Multikurs. */
	public int anzahlSchienen = -1;

	/** Eine Liste Schüler-IDs, welche diesem Kurs zugeordnet sind. */
	public final @NotNull List<Long> schueler = new ArrayList<>();

	/** Die Schienen-IDs, denen der Kurs zugeordnet ist. */
	public final @NotNull List<Long> schienen = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostBlockungsergebnisKurs() {
		// leer
	}

	@Override
	public int hashCode() {
		return 31 + (int) (id ^ (id >>> 32));
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GostBlockungsergebnisKurs))
			return false;
		final @NotNull GostBlockungsergebnisKurs other = (GostBlockungsergebnisKurs) obj;
		return (id == other.id);
	}

}
