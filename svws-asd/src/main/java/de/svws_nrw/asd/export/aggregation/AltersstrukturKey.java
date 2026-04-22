package de.svws_nrw.asd.export.aggregation;

import java.util.Objects;


/**
 *Sortier und Vergleichsobjekt für die klassen
 */
public class AltersstrukturKey {
	/**
	 *
	 */
	final String geburtsjahr;
	/**
	 *
	 */
	final String nationalitaet;

	/**
	 * Konstruktor
	 * @param nationalitaet
	 * @param geburtsjahr
	 */
	public AltersstrukturKey(final String nationalitaet, final String geburtsjahr) {
		this.geburtsjahr = geburtsjahr;
		this.nationalitaet = nationalitaet;
	}

	@Override
	public final int hashCode() {
		return Objects.hash(geburtsjahr, nationalitaet);
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AltersstrukturKey other = (AltersstrukturKey) obj;
		return Objects.equals(geburtsjahr, other.geburtsjahr) && Objects.equals(nationalitaet, other.nationalitaet);
	}

	@Override
	public final String toString() {
		return "AltersstrukturKey [geburtsjahr=" + geburtsjahr + ", nationalitaet=" + nationalitaet + "]";
	}

}
