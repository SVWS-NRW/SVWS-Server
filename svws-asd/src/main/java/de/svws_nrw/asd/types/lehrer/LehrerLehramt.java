package de.svws_nrw.asd.types.lehrer;


import de.svws_nrw.asd.data.lehrer.LehrerLehramtKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Lehrämter von Lehrkräften
 * an der Schule zur Verfügung.
 */
public enum LehrerLehramt implements @NotNull CoreType<LehrerLehramtKatalogEintrag, LehrerLehramt> {

	/** Lehramt 'für die Primarstufe' */
	ID_00,

	/** Lehramt 'an der Grund- und Hauptschule (Stufenschwerpunkt I)' */
	ID_01,

	/** Lehramt 'an der Grund- und Hauptschule (Stufenschwerpunkt II)' */
	ID_02,

	/** Lehramt 'an der Volksschule' */
	ID_03,

	/** Lehramt 'für Sonderpädagogik' */
	ID_09,

	/** Lehramt 'an Sonderschulen' */
	ID_10,

	/** Lehramt 'für Sonderpädagogik und die Primarstufe' */
	ID_11,

	/** Lehramt 'für Sonderpädagogik und die Sekundarstufe I' */
	ID_12,

	/** Lehramt 'Sonderpädagogik LPO 03' */
	ID_14,

	/** Lehramt 'Grund-, Haupt- u. Realschule u. entspr. Jahrg.stufen d. Gesamtschule -Schwerp.- Grundschule' */
	ID_15,

	/** Lehramt 'Grund-, Haupt- u. Realschule u. entspr. Jahrg.stufen d. Gesamtsch. -Schwerp.- H/R/Gesamtsch.' */
	ID_16,

	/** Lehramt 'für die Sekundarstufe I und die Primarstufe' */
	ID_19,

	/** Lehramt 'für die Sekundarstufe I' */
	ID_20,

	/** Lehramt 'an der Realschule' */
	ID_21,

	/** Lehramt 'für die Sekundarstufe II und die Sekundarstufe I' */
	ID_24,

	/** Lehramt 'am Gymnasium' */
	ID_25,

	/** Lehramt 'Gymnasium und Gesamtschule' */
	ID_27,

	/** Lehramt 'für die Sekundarstufe II (ohne berufliche Fachrichtung)' */
	ID_29,

	/** Lehramt 'an berufsbildenden Schulen (alle Lehrer mit 2. Staatsprüfung oder erworbener Anstellungsfähigkeit)' */
	ID_30,

	/** Lehramt 'für die Sekundarstufe II und Sonderpädagogik' */
	ID_31,

	/** Lehramt 'für die Sekundarstufe II (mit beruflicher Fachrichtung)' */
	ID_32,

	/** Lehramt 'Berufskolleg' */
	ID_35,

	/** Lehramt 'Fachhochschullehrer' */
	ID_40,

	/** Lehramt 'Fachlehrer an Sonderschulen' */
	ID_50,

	/** Lehramt 'Religionslehrer/Geistlicher/Katechet' */
	ID_51,

	/** Lehramt 'Fachlehrer mit der Befähigung für die Laufbahn des Werkstattlehrers' */
	ID_52,

	/** Lehramt 'Fachlehrer' */
	ID_53,

	/** Lehramt 'Fachlehrer für Kurzschrift und Maschinenschreiben' */
	ID_54,

	/** Lehramt 'Fachlehrer mit der Befähigung für die Laufbahn des technischen Lehrers an beruflichen Schulen' */
	ID_55,

	/** Lehramt 'Schulkindergärtnerin' */
	ID_56,

	/** Lehramt 'ohne sonderpäd. Zusatzausbildung:Sozialarbeiter, Sozialpädagoge, Jugendleiter' */
	ID_57,

	/** Lehramt 'ohne sonderpäd. Zusatzausbildung:Erzieher, Kindergärtnerin u.a.' */
	ID_58,

	/** Lehramt 'ohne sonderpäd. Zusatzausbildung:Sonstige pädagogische Unterrichtshilfe' */
	ID_59,

	/** Lehramt 'mit sonderpäd. Zusatzausbildung:Sozialarbeiter, Sozialpädagoge, Jugendleiter' */
	ID_60,

	/** Lehramt 'mit sonderpäd. Zusatzausbildung:Erzieher, Kindergärtnerin u.a.' */
	ID_61,

	/** Lehramt 'mit sonderpäd. Zusatzausbildung:Sonstige pädagogische Unterrichtshilfe' */
	ID_62,

	/** Heilpädagoge/-in */
	ID_63,

	/** Handwerksmeister/-in */
	ID_64,

	/** Alltagshelfer/-in */
	ID_65,

	/** Lehramt 'Lehrer, der eine Qualifikation erworben hat, die der 1. Staatsprüfung entspricht (z.B. Diplom, sofern nicht Schlüssel 98)' */
	ID_96,

	/** Lehramt 'Lehrer, der außerhalb des Geltungsbereichs des Grundgesetzes seine Qualifikation erworben hat' */
	ID_97,

	/** Lehramt 'Lehramtsanwärter/Studienreferendar' */
	ID_98,

	/** Lehramt 'Sonstiger Lehrer (Gymnastik-, Werk-, Hauswirtschaftslehrer, Übungsleiter)' */
	ID_99,

	/** Lehramt 'Schulverwaltungsassistent' */
	ID_70,

	/** Lehramt 'Grundschule' */
	ID_04,

	/** Lehramt 'Haupt-, Real- und  Gesamtschule' */
	ID_17,

	/** Lehramt 'Sonderpädagogische Förderung' */
	ID_08,

	/** Lehramt 'Haupt-, Real-, Sekundar- und Gesamtschule' */
	ID_18,

	/** Lehramt 'Berufskolleg mit einer beruflichen Fachrichtung (§ 59 LVO)' */
	ID_49;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerLehramtKatalogEintrag, LehrerLehramt> manager) {
		CoreTypeDataManager.putManager(LehrerLehramt.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerLehramtKatalogEintrag, LehrerLehramt> data() {
		return CoreTypeDataManager.getManager(LehrerLehramt.class);
	}

}
