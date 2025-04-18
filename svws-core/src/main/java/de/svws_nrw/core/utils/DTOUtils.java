package de.svws_nrw.core.utils;

import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnung;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnung;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet hilfreiche statische Methoden für DTO. Primär um die fehlenden Konstruktoren zu ersetzen.
 *
 * @author Benjamin A. Bartsch
 */
public final class DTOUtils {

	private DTOUtils() {
	}

	/**
	 * Liefert ein neues {@link GostBlockungsergebnisKursSchuelerZuordnung}-Objekt.
	 *
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return ein neues {@link GostBlockungsergebnisKursSchuelerZuordnung}-Objekt.
	 *
	 */
	public static @NotNull GostBlockungsergebnisKursSchuelerZuordnung newGostBlockungsergebnisKursSchuelerZuordnung(final long idKurs, final long idSchueler) {
		final @NotNull GostBlockungsergebnisKursSchuelerZuordnung z = new GostBlockungsergebnisKursSchuelerZuordnung();
		z.idKurs = idKurs;
		z.idSchueler = idSchueler;
		return z;
	}

	/**
	 * Liefert ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt.
	 *
	 */
	public static @NotNull GostBlockungsergebnisKursSchienenZuordnung newGostBlockungsergebnisKursSchienenZuordnung(final long idKurs, final long idSchiene) {
		final @NotNull GostBlockungsergebnisKursSchienenZuordnung z = new GostBlockungsergebnisKursSchienenZuordnung();
		z.idKurs = idKurs;
		z.idSchiene = idSchiene;
		return z;
	}

	/**
	 * Liefert ein neues {@link GostBlockungsergebnisSchiene}-Objekt.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return ein neues {@link GostBlockungsergebnisSchiene}-Objekt.
	 */
	public static @NotNull GostBlockungsergebnisSchiene newGostBlockungsergebnisSchiene(final long idSchiene) {
		final @NotNull GostBlockungsergebnisSchiene eSchiene = new GostBlockungsergebnisSchiene();
		eSchiene.id = idSchiene;
		return eSchiene;
	}

	/**
	 * Liefert ein neues {@link GostBlockungsergebnisKurs}-Objekt.
	 * @param idKurs          Die Datenbank-ID des Kurses.
	 * @param idFach          Die Datenbank-ID des Faches.
	 * @param idKursart       Die ID der Kursart.
	 * @param anzahlSchienen  Die Anzahl an Schienen, die der Kurs belegt.
	 *
	 * @return ein neues {@link GostBlockungsergebnisKurs}-Objekt.
	 */
	public static @NotNull GostBlockungsergebnisKurs newGostBlockungsergebnisKurs(final long idKurs, final long idFach, final int idKursart,
			final int anzahlSchienen) {
		final @NotNull GostBlockungsergebnisKurs eKurs = new GostBlockungsergebnisKurs();
		eKurs.id = idKurs;
		eKurs.fachID = idFach;
		eKurs.kursart = idKursart;
		eKurs.anzahlSchienen = anzahlSchienen;
		return eKurs;
	}

	/**
	 * Liefert ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit keinem Parameter.
	 *
	 * @param typ     Der {@link GostKursblockungRegelTyp}.
	 *
	 * @return ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit keinem Parameter.
	 */
	public static @NotNull GostBlockungRegel newGostBlockungRegel0(final int typ) {
		final @NotNull GostBlockungRegel r = new GostBlockungRegel();
		r.id = -1;
		r.typ = typ;
		return r;
	}

	/**
	 * Liefert ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit genau einem Parameter.
	 *
	 * @param typ     Der {@link GostKursblockungRegelTyp}.
	 * @param param1  Der 1. Parameter.
	 *
	 * @return ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit genau einem Parameter.
	 */
	public static @NotNull GostBlockungRegel newGostBlockungRegel1(final int typ, final long param1) {
		final @NotNull GostBlockungRegel r = new GostBlockungRegel();
		r.id = -1;
		r.typ = typ;
		r.parameter.add(param1);
		return r;
	}

	/**
	 * Liefert ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit genau zwei Parametern.
	 *
	 * @param typ     Der {@link GostKursblockungRegelTyp}.
	 * @param param1  Der 1. Parameter.
	 * @param param2  Der 2. Parameter.
	 *
	 * @return ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit genau zwei Parametern.
	 */
	public static @NotNull GostBlockungRegel newGostBlockungRegel2(final int typ, final long param1, final long param2) {
		final @NotNull GostBlockungRegel r = new GostBlockungRegel();
		r.id = -1;
		r.typ = typ;
		r.parameter.add(param1);
		r.parameter.add(param2);
		return r;
	}

	/**
	 * Liefert ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit genau drei Parametern.
	 *
	 * @param typ     Der {@link GostKursblockungRegelTyp}.
	 * @param param1  Der 1. Parameter.
	 * @param param2  Der 2. Parameter.
	 * @param param3  Der 3. Parameter.
	 *
	 * @return ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit genau drei Parametern.
	 */
	public static @NotNull GostBlockungRegel newGostBlockungRegel3(final int typ, final long param1, final long param2, final long param3) {
		final @NotNull GostBlockungRegel r = new GostBlockungRegel();
		r.id = -1;
		r.typ = typ;
		r.parameter.add(param1);
		r.parameter.add(param2);
		r.parameter.add(param3);
		return r;
	}

	/**
	 * Liefert TRUE, falls die Parameter der Regeln sich vom übergebenen Array unterscheiden.
	 *
	 * @param r   Die zu prüfende Regel.
	 * @param a   Die zu prüfenden neuen Parameter.
	 *
	 * @return TRUE, falls die Parameter der Regeln sich vom übergebenen Array unterscheiden.
	 */
	public static boolean testRegelParameterChanged(final @NotNull GostBlockungRegel r, final @NotNull long[] a) {
		for (int i = 0; i < a.length; i++)
			if (a[i] != r.parameter.get(i))
				return true;
		return false;
	}

}
