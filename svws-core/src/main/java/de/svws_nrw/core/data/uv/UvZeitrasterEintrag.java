package de.svws_nrw.core.data.uv;

import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem Eintrag innerhalb eines Zeitrasters.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einem Eintrag innerhalb eines Zeitrasters.")
@TranspilerDTO
public class UvZeitrasterEintrag {

	/** Die ID des Zeitraster-Eintrags. */
	@Schema(description = "die ID des Zeitraster-Eintrags", example = "4711")
	public long id = -1;

	/** Die ID des übergeordneten Zeitrasters. */
	@Schema(description = "die ID des übergeordneten Zeitrasters", example = "1001")
	public long idZeitraster = -1;

	/** Der {@link Wochentag} an dem der Unterricht stattfindet (1=Montag, 2=Dienstag, ..., 7=Sonntag) */
	@Schema(description = "der Wochentag an dem der Unterricht stattfindet (1=Montag, 2=Dienstag, ..., 7=Sonntag)", example = "1")
	public int wochentag = -1;

	/** Die Stunde (z. B. 1 = erste Stunde). */
	@Schema(description = "die Stunde (z. B. 1 = erste Stunde)", example = "1")
	public int stunde = -1;

	/** Beginn der Stunde (als Minuten seit Mitternacht). */
	@Schema(description = "Beginn der Stunde (als Minuten seit Mitternacht)", example = "480")
	public int beginn = -1;

	/** Ende der Stunde (als Minuten seit Mitternacht). */
	@Schema(description = "Ende der Stunde (als Minuten seit Mitternacht)", example = "525")
	public int ende = -1;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvZeitrasterEintrag() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvZeitrasterEintrag-Objekts zurück.
	 *
	 * @return die String-Darstellung
	 */
	@Override
	public String toString() {
		return "UvZeitrasterEintrag{id=" + id
				+ ", idZeitraster=" + idZeitraster
				+ ", tag=" + wochentag
				+ ", stunde=" + stunde
				+ ", beginn=" + beginn
				+ ", ende=" + ende
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
		return (another instanceof final UvZeitrasterEintrag a)
				&& (this.id == a.id);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt auf Basis der ID.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

}
