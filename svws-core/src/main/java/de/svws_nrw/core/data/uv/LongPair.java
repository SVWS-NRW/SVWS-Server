package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert ein Paar aus zwei long-Werten.
 */
@XmlRootElement
@Schema(description = "ein Paar aus zwei long-Werten.")
@TranspilerDTO
public class LongPair {

	/**
	 * Der erste long-Wert.
	 */
	@Schema(description = "der erste long-Wert", example = "4711")
	public long a;

	/**
	 * Der zweite long-Wert.
	 */
	@Schema(description = "der zweite long-Wert", example = "815")
	public long b;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LongPair() {
		// leer
	}

	/**
	 * Erzeugt ein neues LongPair-Objekt mit den übergebenen Werten.
	 *
	 * @param a der erste long-Wert
	 * @param b der zweite long-Wert
	 */
	public LongPair(final long a, final long b) {
		this.a = a;
		this.b = b;
	}

	/**
	 * Gibt eine String-Repräsentation des Objekts zurück.
	 *
	 * @return eine lesbare Beschreibung des Objekts
	 */
	@Override
	public String toString() {
		return a + "-" + b;
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		return (another instanceof final LongPair ano) && (this.a == ano.a) && (this.b == ano.b);
	}

	/**
	 * Erzeugt den Hashcode des Objekts basierend auf den beiden Werten.
	 *
	 * @return den Hashcode
	 */
	@Override
	public int hashCode() {
		return (31 * Long.hashCode(a)) + Long.hashCode(b);
	}

}
