package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Core-DTO für das Pflichtstundensoll eines Lehrers
 * innerhalb der Unterrichtsverteilung.
 */
@XmlRootElement
@Schema(description = "Das Core-DTO für das Pflichtstundensoll eines Lehrers innerhalb der Unterrichtsverteilung.")
@TranspilerDTO
public class UvLehrerPflichtstundensoll {

	/** Die eindeutige ID des Pflichtstundensoll-Eintrags. */
	@Schema(description = "Die eindeutige ID des Pflichtstundensoll-Eintrags.")
	public long id;

	/** Die ID des Lehrers, auf den sich das Pflichtstundensoll bezieht. */
	@Schema(description = "Die ID des Lehrers, auf den sich das Pflichtstundensoll bezieht.")
	public long idLehrer;

	/** Die Anzahl der Pflichtstunden, die der Lehrer in dem Gültigkeitszeitraum zu leisten hat. */
	@Schema(description = "Die Anzahl der Pflichtstunden, die der Lehrer in dem Gültigkeitszeitraum zu leisten hat.")
	public double pflichtstdSoll;

	/** Das Datum, ab dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).")
	public @NotNull String gueltigVon = "";

	/** Das Datum, bis zu dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).")
	public String gueltigBis = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvLehrerPflichtstundensoll() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvLehrerPflichtstundensoll-Objektes zurück.
	 *
	 * @return eine String-Repräsentation
	 */
	@Override
	public String toString() {
		return "UvLehrerPflichtstundensoll{id=" + id
				+ ", idLehrer=" + idLehrer
				+ ", pflichtstdSoll=" + pflichtstdSoll
				+ ", gueltigVon=" + gueltigVon
				+ ", gueltigBis=" + gueltigBis
				+ "}";
	}

	/**
	 * Vergleicht dieses Objekt mit einem anderen.
	 *
	 * @param obj das zu vergleichende Objekt
	 *
	 * @return {@code true}, wenn es sich um dasselbe Objekt oder ein Objekt mit derselben ID handelt,
	 *         sonst {@code false}
	 */
	@Override
	public boolean equals(final Object obj) {
		return (obj instanceof final UvLehrerPflichtstundensoll other) && (this.id == other.id);
	}

	/**
	 * Erzeugt den Hashcode für dieses Objekt.
	 *
	 * @return der Hashcode basierend auf der ID
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

}
