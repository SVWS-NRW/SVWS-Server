package de.svws_nrw.core.data.gost;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist die Core-DTO für eine Schiene eines Ergebnisses einer Kursblockung
 */
@XmlRootElement
@Schema(description = "Informationen zu einer Schiene eines Ergebnisses einer Blockung der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "kurse" })
@TranspilerDTO
public final class GostBlockungsergebnisSchiene {

	/** Die ID der Schiene */
	public long id = -1;

	/** Eine Liste der Kurse, welche dieser Schiene zugeordnet sind. */
	public final @NotNull List<GostBlockungsergebnisKurs> kurse = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostBlockungsergebnisSchiene() {
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
		if (!(obj instanceof GostBlockungsergebnisSchiene))
			return false;
		final @NotNull GostBlockungsergebnisSchiene other = (GostBlockungsergebnisSchiene) obj;
		return (id == other.id);
	}

}
