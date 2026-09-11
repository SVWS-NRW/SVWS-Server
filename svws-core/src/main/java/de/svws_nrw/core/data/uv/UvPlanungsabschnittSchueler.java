package de.svws_nrw.core.data.uv;

import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zur Zuordnung eines Schülers zu einem Planungsabschnitt innerhalb der Unterrichtsverteilung.
 */
@XmlRootElement
@Schema(description = "die Informationen zur Zuordnung eines Schülers zu einem Planungsabschnitt innerhalb der Unterrichtsverteilung.")
@TranspilerDTO
public class UvPlanungsabschnittSchueler {

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts", example = "42")
	public long idPlanungsabschnitt = -1;

	/** Die ID des Schülers. */
	@Schema(description = "die ID des Schülers", example = "4711")
	public long idSchueler = -1;

	/** Die ID des Jahrgangs, dem der Schüler zugeordnet ist. */
	@Schema(description = "die ID des Jahrgangs", example = "1")
	public long idJahrgang = -1;

	/** Die ID der Klasse, der der Schüler zugeordnet ist. */
	@Schema(description = "die ID der Klasse", example = "10")
	public Long idKlasse = null;

	/** Der Schülerdatensatz. */
	@Schema(description = "der Nachname", example = "Mustermann")
	public @NotNull Schueler daten = new Schueler();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvPlanungsabschnittSchueler() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvPlanungsabschnittSchueler-Objekts zurück.
	 *
	 * @return die String-Darstellung der Zuordnung
	 */
	@Override
	public String toString() {
		return "UvPlanungsabschnittSchueler{idPlanungsabschnitt=" + idPlanungsabschnitt
				+ ", idSchueler=" + idSchueler
				+ ", idJahrgang=" + idJahrgang
				+ ", idKlasse=" + idKlasse
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
		return (another instanceof final UvPlanungsabschnittSchueler a) && (this.idPlanungsabschnitt == a.idPlanungsabschnitt) && (this.idSchueler == a.idSchueler);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + Long.hashCode(idPlanungsabschnitt);
		result = (prime * result) + Long.hashCode(idSchueler);
		return result;
	}

}
