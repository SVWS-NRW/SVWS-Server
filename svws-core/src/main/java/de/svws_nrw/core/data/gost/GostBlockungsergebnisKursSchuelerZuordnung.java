package de.svws_nrw.core.data.gost;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist die Core-DTO für eine Kurs-Schüler-Zuordnung eines Ergebnisses einer Kursblockung
 */
@XmlRootElement
@Schema(description = "Informationen zu einer Kurs-Schüler-Zuordnung eines Ergebnisses einer Blockung der gymnasialen Oberstufe.")
@TranspilerDTO
public final class GostBlockungsergebnisKursSchuelerZuordnung {

	/** Die ID des Kurses */
	public long idKurs = -1;

	/** Die ID des Schülers */
	public long idSchueler = -1;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostBlockungsergebnisKursSchuelerZuordnung() {
		// leer
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		hashCode = (31 * hashCode) + (int) (idKurs ^ (idKurs >>> 32));
		hashCode = (31 * hashCode) + (int) (idSchueler ^ (idSchueler >>> 32));
		return hashCode;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GostBlockungsergebnisKursSchuelerZuordnung))
			return false;
		final @NotNull GostBlockungsergebnisKursSchuelerZuordnung other = (GostBlockungsergebnisKursSchuelerZuordnung) obj;
		return (idKurs == other.idKurs) && (idSchueler == other.idSchueler);
	}

}
