package de.svws_nrw.core.data.uv;

import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem UV-Kurs.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einem UV-Kurs.")
@TranspilerDTO
public class UvKurs {

	/**
	 * Die ID des Kurses.
	 */
	@Schema(description = "die ID des Kurses", example = "501")
	public long id;

	/**
	 * Die ID des Planungsabschnitts.
	 */
	@Schema(description = "die ID des Planungsabschnitts", example = "815")
	public long idPlanungsabschnitt;

	/**
	 * Die ID des Schuljahresabschnitts.
	 */
	@Schema(description = "die ID des Schuljahresabschnitts", example = "42")
	public long idSchuljahresabschnitt;

	/**
	 * Die ID des Faches.
	 */
	@Schema(description = "die ID des Faches", example = "12")
	public long idFach;

	/**
	 * Die Kursart (siehe {@link GostKursart}).
	 */
	@Schema(description = "die Kursart", example = "GK")
	public @NotNull String kursart = "";

	/**
	 * Die Kursnummer.
	 */
	@Schema(description = "die Kursnummer", example = "1")
	public int kursnummer;

	/**
	 * Die ID der zugehörigen Schülergruppe.
	 */
	@Schema(description = "die ID der Schülergruppe", example = "815")
	public long idSchuelergruppe;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvKurs() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvKurs-Objekts zurück.
	 *
	 * @return eine lesbare Beschreibung des Objekts
	 */
	@Override
	public String toString() {
		return "UvKurs{id=" + id
				+ ", idPlanungsabschnitt=" + idPlanungsabschnitt
				+ ", idSchuljahresabschnitt=" + idSchuljahresabschnitt
				+ ", idFach=" + idFach
				+ ", kursart=" + kursart
				+ ", kursnummer=" + kursnummer
				+ ", idSchuelergruppe=" + idSchuelergruppe
				+ "}";
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		return (another instanceof final UvKurs ano) && (this.id == ano.id);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt auf Basis der ID.
	 *
	 * @return den Hashcode
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

}
