package de.svws_nrw.core.types.lehrer;

import java.util.HashMap;

import de.svws_nrw.core.data.lehrer.LehrerKatalogLehramtEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Lehrämter von Lehrkräften
 * an der Schule zur Verfügung.
 *
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum LehrerLehramt {

	/** Lehramt 'für die Primarstufe' */
	ID_00(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(82, "00", "für die Primarstufe", null, null)
	}),

	/** Lehramt 'an der Grund- und Hauptschule (Stufenschwerpunkt I)' */
	ID_01(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(83, "01", "an der Grund- und Hauptschule (Stufenschwerpunkt I)", null, null)
	}),

	/** Lehramt 'an der Grund- und Hauptschule (Stufenschwerpunkt II)' */
	ID_02(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(84, "02", "an der Grund- und Hauptschule (Stufenschwerpunkt II)", null, null)
	}),

	/** Lehramt 'an der Volksschule' */
	ID_03(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(85, "03", "an der Volksschule", null, null)
	}),

	/** Lehramt 'für Sonderpädagogik' */
	ID_09(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(86, "09", "für Sonderpädagogik", null, null)
	}),

	/** Lehramt 'an Sonderschulen' */
	ID_10(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(87, "10", "an Sonderschulen", null, null)
	}),

	/** Lehramt 'für Sonderpädagogik und die Primarstufe' */
	ID_11(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(88, "11", "für Sonderpädagogik und die Primarstufe", null, null)
	}),

	/** Lehramt 'für Sonderpädagogik und die Sekundarstufe I' */
	ID_12(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(89, "12", "für Sonderpädagogik und die Sekundarstufe I", null, null)
	}),

	/** Lehramt 'Sonderpädagogik LPO 03' */
	ID_14(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(90, "14", "Sonderpädagogik LPO 03", null, null)
	}),

	/** Lehramt 'Grund-, Haupt- u. Realschule u. entspr. Jahrg.stufen d. Gesamtschule -Schwerp.- Grundschule' */
	ID_15(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(91, "15", "Grund-, Haupt- u. Realschule u. entspr. Jahrg.stufen d. Gesamtschule -Schwerp.- Grundschule", null, null)
	}),

	/** Lehramt 'Grund-, Haupt- u. Realschule u. entspr. Jahrg.stufen d. Gesamtsch. -Schwerp.- H/R/Gesamtsch.' */
	ID_16(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(92, "16", "Grund-, Haupt- u. Realschule u. entspr. Jahrg.stufen d. Gesamtsch. -Schwerp.- H/R/Gesamtsch.", null, null)
	}),

	/** Lehramt 'für die Sekundarstufe I und die Primarstufe' */
	ID_19(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(93, "19", "für die Sekundarstufe I und die Primarstufe", null, null)
	}),

	/** Lehramt 'für die Sekundarstufe I' */
	ID_20(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(94, "20", "für die Sekundarstufe I", null, null)
	}),

	/** Lehramt 'an der Realschule' */
	ID_21(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(95, "21", "an der Realschule", null, null)
	}),

	/** Lehramt 'für die Sekundarstufe II und die Sekundarstufe I' */
	ID_24(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(96, "24", "für die Sekundarstufe II und die Sekundarstufe I", null, null)
	}),

	/** Lehramt 'am Gymnasium' */
	ID_25(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(97, "25", "am Gymnasium", null, null)
	}),

	/** Lehramt 'Gymnasium und Gesamtschule' */
	ID_27(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(98, "27", "Gymnasium und Gesamtschule", null, null)
	}),

	/** Lehramt 'für die Sekundarstufe II (ohne berufliche Fachrichtung)' */
	ID_29(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(99, "29", "für die Sekundarstufe II (ohne berufliche Fachrichtung)", null, null)
	}),

	/** Lehramt 'an berufsbildenden Schulen (alle Lehrer mit 2. Staatsprüfung oder erworbener Anstellungsfähigkeit)' */
	ID_30(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(100, "30", "an berufsbildenden Schulen (alle Lehrer mit 2. Staatsprüfung oder erworbener Anstellungsfähigkeit)", null, null)
	}),

	/** Lehramt 'für die Sekundarstufe II und Sonderpädagogik' */
	ID_31(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(101, "31", "für die Sekundarstufe II und Sonderpädagogik", null, null)
	}),

	/** Lehramt 'für die Sekundarstufe II (mit beruflicher Fachrichtung)' */
	ID_32(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(102, "32", "für die Sekundarstufe II (mit beruflicher Fachrichtung)", null, null)
	}),

	/** Lehramt 'Berufskolleg' */
	ID_35(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(103, "35", "Berufskolleg", null, null)
	}),

	/** Lehramt 'Fachhochschullehrer' */
	ID_40(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(104, "40", "Fachhochschullehrer", null, null)
	}),

	/** Lehramt 'Fachlehrer an Sonderschulen' */
	ID_50(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(105, "50", "Fachlehrer an Sonderschulen", null, null)
	}),

	/** Lehramt 'Religionslehrer/Geistlicher/Katechet' */
	ID_51(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(106, "51", "Religionslehrer/Geistlicher/Katechet", null, null)
	}),

	/** Lehramt 'Fachlehrer mit der Befähigung für die Laufbahn des Werkstattlehrers' */
	ID_52(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(107, "52", "Fachlehrer mit der Befähigung für die Laufbahn des Werkstattlehrers", null, null)
	}),

	/** Lehramt 'Fachlehrer' */
	ID_53(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(108, "53", "Fachlehrer", null, null)
	}),

	/** Lehramt 'Fachlehrer für Kurzschrift und Maschinenschreiben' */
	ID_54(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(109, "54", "Fachlehrer für Kurzschrift und Maschinenschreiben", null, null)
	}),

	/** Lehramt 'Fachlehrer mit der Befähigung für die Laufbahn des technischen Lehrers an beruflichen Schulen' */
	ID_55(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(110, "55", "Fachlehrer mit der Befähigung für die Laufbahn des technischen Lehrers an beruflichen Schulen", null, null)
	}),

	/** Lehramt 'Schulkindergärtnerin' */
	ID_56(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(111, "56", "Schulkindergärtnerin", null, null)
	}),

	/** Lehramt 'ohne sonderpäd. Zusatzausbildung:Sozialarbeiter, Sozialpädagoge, Jugendleiter' */
	ID_57(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(112, "57", "ohne sonderpäd. Zusatzausbildung:Sozialarbeiter, Sozialpädagoge, Jugendleiter", null, null)
	}),

	/** Lehramt 'ohne sonderpäd. Zusatzausbildung:Erzieher, Kindergärtnerin u.a.' */
	ID_58(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(113, "58", "ohne sonderpäd. Zusatzausbildung:Erzieher, Kindergärtnerin u.a.", null, null)
	}),

	/** Lehramt 'ohne sonderpäd. Zusatzausbildung:Sonstige pädagogische Unterrichtshilfe' */
	ID_59(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(114, "59", "ohne sonderpäd. Zusatzausbildung:Sonstige pädagogische Unterrichtshilfe", null, null)
	}),

	/** Lehramt 'mit sonderpäd. Zusatzausbildung:Sozialarbeiter, Sozialpädagoge, Jugendleiter' */
	ID_60(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(115, "60", "mit sonderpäd. Zusatzausbildung:Sozialarbeiter, Sozialpädagoge, Jugendleiter", null, null)
	}),

	/** Lehramt 'mit sonderpäd. Zusatzausbildung:Erzieher, Kindergärtnerin u.a.' */
	ID_61(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(116, "61", "mit sonderpäd. Zusatzausbildung:Erzieher, Kindergärtnerin u.a.", null, null)
	}),

	/** Lehramt 'mit sonderpäd. Zusatzausbildung:Sonstige pädagogische Unterrichtshilfe' */
	ID_62(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(117, "62", "mit sonderpäd. Zusatzausbildung:Sonstige pädagogische Unterrichtshilfe", null, null)
	}),

	/** Lehramt 'Lehrer, der eine Qualifikation erworben hat, die der 1. Staatsprüfung entspricht (z.B. Diplom, sofern nicht Schlüssel 98)' */
	ID_96(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(118, "96", "Lehrer, der eine Qualifikation erworben hat, die der 1. Staatsprüfung entspricht (z.B. Diplom, sofern nicht Schlüssel 98)", null, null)
	}),

	/** Lehramt 'Lehrer, der außerhalb des Geltungsbereichs des Grundgesetzes seine Qualifikation erworben hat' */
	ID_97(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(119, "97", "Lehrer, der außerhalb des Geltungsbereichs des Grundgesetzes seine Qualifikation erworben hat", null, null)
	}),

	/** Lehramt 'Lehramtsanwärter/Studienreferendar' */
	ID_98(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(120, "98", "Lehramtsanwärter/Studienreferendar", null, null)
	}),

	/** Lehramt 'Sonstiger Lehrer (Gymnastik-, Werk-, Hauswirtschaftslehrer, Übungsleiter)' */
	ID_99(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(121, "99", "Sonstiger Lehrer (Gymnastik-, Werk-, Hauswirtschaftslehrer, Übungsleiter)", null, null)
	}),

	/** Lehramt 'Schulverwaltungsassistent' */
	ID_70(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(122, "70", "Schulverwaltungsassistent", null, null)
	}),

	/** Lehramt 'Grundschule' */
	ID_04(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(123, "04", "Grundschule", null, null)
	}),

	/** Lehramt 'Haupt-, Real- und  Gesamtschule' */
	ID_17(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(124, "17", "Haupt-, Real- und  Gesamtschule", null, null)
	}),

	/** Lehramt 'Sonderpädagogische Förderung' */
	ID_08(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(125, "08", "Sonderpädagogische Förderung", null, null)
	}),

	/** Lehramt 'Haupt-, Real-, Sekundar- und Gesamtschule' */
	ID_18(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(126, "18", "Haupt-, Real-, Sekundar- und Gesamtschule", null, null)
	}),

	/** Lehramt 'Berufskolleg mit einer beruflichen Fachrichtung (§ 59 LVO)' */
	ID_49(new LehrerKatalogLehramtEintrag[]{
		new LehrerKatalogLehramtEintrag(127, "49", "Berufskolleg mit einer beruflichen Fachrichtung (§ 59 LVO)", null, null)
	});



	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten des Lehramtes, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull LehrerKatalogLehramtEintrag daten;

	/** Die Historie mit den Einträgen des Lehramtes */
	public final @NotNull LehrerKatalogLehramtEintrag@NotNull[] historie;

	/** Eine Hashmap mit allen Lehrämtern, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, LehrerLehramt> _aemterByID = new HashMap<>();

	/** Eine Hashmap mit allen Lehrämtern, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, LehrerLehramt> _aemterByKuerzel = new HashMap<>();


	/**
	 * Erzeugt ein neues Lehramt in der Aufzählung.
	 *
	 * @param historie   die Historie des Lehramtes, welches ein Array von {@link LehrerKatalogLehramtEintrag} ist
	 */
	LehrerLehramt(final @NotNull LehrerKatalogLehramtEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den IDs der Lehrämter auf die zugehörigen Lehrämter
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den IDs der Lehrämter auf die zugehörigen Lehrämter
	 */
	private static @NotNull HashMap<@NotNull Long, LehrerLehramt> getMapLehraemterByID() {
		if (_aemterByID.size() == 0)
			for (final LehrerLehramt l : LehrerLehramt.values())
				_aemterByID.put(l.daten.id, l);
		return _aemterByID;
	}


	/**
	 * Gibt eine Map von den Kürzeln der Lehrämter auf die zugehörigen Lehrämter
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den Kürzeln der Lehrämter auf die zugehörigen Lehrämter
	 */
	private static @NotNull HashMap<@NotNull String, LehrerLehramt> getMapLehraemterByKuerzel() {
		if (_aemterByKuerzel.size() == 0)
			for (final LehrerLehramt l : LehrerLehramt.values())
				_aemterByKuerzel.put(l.daten.kuerzel, l);
		return _aemterByKuerzel;
	}


	/**
	 * Gibt das Lehramt anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID des Lehramts
	 *
	 * @return das Lehramt oder null, falls die ID ungültig ist
	 */
	public static LehrerLehramt getByID(final long id) {
		return getMapLehraemterByID().get(id);
	}


	/**
	 * Gibt das Lehramt anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Lehramts
	 *
	 * @return das Lehramt oder null, falls das Kürzel ungültig ist
	 */
	public static LehrerLehramt getByKuerzel(final String kuerzel) {
		return getMapLehraemterByKuerzel().get(kuerzel);
	}

}
