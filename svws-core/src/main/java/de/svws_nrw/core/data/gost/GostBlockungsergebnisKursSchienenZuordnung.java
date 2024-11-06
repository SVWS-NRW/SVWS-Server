package de.svws_nrw.core.data.gost;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist die Core-DTO für eine Kurs-Schienen-Zuordnung eines Ergebnisses einer Kursblockung
 */
@XmlRootElement
@Schema(description = "Informationen zu einer Kurs-Schienen-Zuordnung eines Ergebnisses einer Blockung der gymnasialen Oberstufe.")
@TranspilerDTO
public final class GostBlockungsergebnisKursSchienenZuordnung {

	/** Die ID des Kurses */
	public long idKurs = -1;

	/** Die ID der Schiene */
	public long idSchiene = -1;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostBlockungsergebnisKursSchienenZuordnung() {
		// leer
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		hashCode = (31 * hashCode) + (int) (idKurs ^ (idKurs >>> 32));
		hashCode = (31 * hashCode) + (int) (idSchiene ^ (idSchiene >>> 32));
		return hashCode;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GostBlockungsergebnisKursSchienenZuordnung))
			return false;
		final @NotNull GostBlockungsergebnisKursSchienenZuordnung other = (GostBlockungsergebnisKursSchienenZuordnung) obj;
		return (idKurs == other.idKurs) && (idSchiene == other.idSchiene);
	}

}
