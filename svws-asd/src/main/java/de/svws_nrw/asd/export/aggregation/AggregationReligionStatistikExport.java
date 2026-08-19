package de.svws_nrw.asd.export.aggregation;

import static de.svws_nrw.asd.export.aggregation.AggregationStatistikExport.DREI_LEERZEICHEN;
import static de.svws_nrw.asd.export.aggregation.AggregationStatistikExport.EIN_LEERZEICHEN;
import static de.svws_nrw.asd.export.aggregation.AggregationStatistikExport.ZWEI_LEERZEICHEN;
import static de.svws_nrw.asd.export.aggregation.AggregationStatistikExport.istJahrgangErforderlich;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.export.data.ReligionszugehoerigkeitenStatistikExport;
import de.svws_nrw.asd.export.data.StatistikExport;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;

/*
 * AggregationStatistikExport.java
 *
 * Copyright (c) 2026 Projekt SVWS-Server - Schulverwaltungsserver
 *
 * Landesbetrieb Information und Technik Nordrhein-Westfalen (IT.NRW)
 * Alle Rechte vorbehalten.
 *
 * Versionshistorie
 * @version 1.00 - 11.03.2026 - Daniel Knittel (knitt01) - erste Version
 * @version 1.00 - 11.03.2026 - Mahmoud Guedda (guedd01) - erste Version
 */

/**
 * Die Klasse AggregationStatistikExport ist eine Klasse im Paket de.svws_nrw.asd.export.aggregation des Projekts SVWS-Server.
 *
 * @since 2026
 * @version 1.00 - 11.03.2026
 * @author Daniel Knittel (knitt01)
 * @author Mahmoud Guedda (guedd01)
 *
 */
public class AggregationReligionStatistikExport {


	/**
	* Das aktuelle Schuljahr in vierstelliger Form.
	*/
	private final int aktuellesSchuljahr;

	/**
	 * Eine Liste der Fehlermeldungen zu den aufgetretenen Fehlern.
	 */
	private final List<String> fehlermeldungen;

	/**
	 * Zuordnug der Foerderschwerpunkt-IDs der Schule zu den idFoerderschwerpunkt des Katalogs.
	 */
	private final Map<Long, Long> foerderschwerpunktIdMap;

	/**
	 * Zuordnug der Jahrgang-IDs der Schule zu den idJahrgang des Katalogs.
	 */
	private final Map<Long, Long> jahrgangIdMap;

	/**
	 * Zuordnug der Religion-IDs der Schule zu den idReligion des Katalogs.
	 */
	private final Map<Long, Long> religionIdMap;

	/**
	 * Die Schulform der Schule als Enum {@link Schulform}.
	 */
	private final Schulform schulform;

	/**
	 * Die für den Export vorgesehenen Statistikdaten mit den Aggregaten.
	 */
	private final StatistikExport statistikExport;


	/**
	 * Die gesamten Statistikdaten der Schule, welche von einer Schule bei der Erfassung der amtlichen Schulstatistik übertragen werden.
	 */
	private final StatistikGesamt statistikGesamt;


	/**
	 * Konstruktor
	 * @param statistikGesamt
	 * @param statistikExport
	 * @param fehlermeldungen
	 * @param aktuellesSchuljahr
	 * @param religionIdMap
	 * @param foerderschwerpunktIdMap
	 * @param jahrgangIdMap
	 */
	public AggregationReligionStatistikExport(final StatistikGesamt statistikGesamt, final StatistikExport statistikExport,
			final List<String> fehlermeldungen, final Map<Long, Long> jahrgangIdMap, final Map<Long, Long> foerderschwerpunktIdMap,
			final Map<Long, Long> religionIdMap,
			final int aktuellesSchuljahr) {
		this.statistikGesamt = statistikGesamt;
		this.statistikExport = statistikExport;
		this.fehlermeldungen = fehlermeldungen;
		schulform = Schulform.data().getWertByBezeichner(statistikGesamt.schule.schulform);
		this.jahrgangIdMap = jahrgangIdMap;
		this.foerderschwerpunktIdMap = foerderschwerpunktIdMap;
		this.religionIdMap = religionIdMap;
		this.aktuellesSchuljahr = aktuellesSchuljahr;
	}


	/**
	 * Stellt die Satzkennung für den Religionssatz aus dem Feldern <br>
	 * {@code religionExport.jahrgang} <br>
	 * {@code religionExport.schulgliederung} <br>
	 * {@code religionExport.bildungsbereich} <br>
	 * {@code religionExport.foerderschwerpunkt} <br>
	 * zusammen.
	 *
	 * @param religionExport
	 * @return Die Satzkennung für  den Religionssatz
	 */
	private static String bauenSatzkennungReligion(final ReligionszugehoerigkeitenStatistikExport religionExport) {
		final StringBuilder satzkennungReli = new StringBuilder();
		satzkennungReli.append(religionExport.jahrgang);
		satzkennungReli.append(religionExport.schulgliederung);
		satzkennungReli.append(religionExport.bildungsbereich);
		satzkennungReli.append(religionExport.foerderschwerpunkt);

		return satzkennungReli.toString();
	}

	/**
	 * Die zu erhöhenden Werte werden als Lambdas rein gereicht und sie können damit über das Interface {@code Runnable} angesprochen werden.
	 * Die Lambdas enthalten das ++, run() führt es aus.
	 *
	 * @param zusammen - Zähler zusammen
	 * @param weiblich - Zähler weiblich
	 * @param istWeiblich
	 */
	private static void erhoehen(final Runnable zusammen, final Runnable weiblich, final boolean istWeiblich) {
		zusammen.run();
		if (istWeiblich) {
			weiblich.run();
		}
	}

	/**
	 * Führt die Aggregation der {@link StatistikGesamt}-Daten der Religion in das {@link StatistikExport}-Datenobjekt aus. <br>
	 * Fehlermeldungen zu gegebenenfalls aufgetretenen Fehlern werden in die Liste {@link #fehlermeldungen} geschrieben.
	 *
	 * @return - Ausführung erfolgreich und ohne schwere Fehler
	 */
	public boolean run() {

		if (statistikGesamt == null) {
			return false;
		}

		// S42
		final Map<String, ReligionszugehoerigkeitenStatistikExport> religionen = new HashMap<>();
		statistikGesamt.schueler.stream().forEach(schueler -> erstellenReligionszugehoerigkeitenStatistikExport(schueler, religionen));
		statistikExport.religionszugehoerigkeitenStatistikExport.addAll(religionen.values());


		return true;
	}



	/**
	 * Hier werden die Schüler-Summen zum Religionsunterricht aufsummiert und in das übergebene
	 * {@link ReligionszugehoerigkeitenStatistikExport}-Objekt geschrieben.
	 *
	 * @param religionExport
	 * @param schueler
	 */
	private void aufsummierenReligionExport(final ReligionszugehoerigkeitenStatistikExport religionExport,
			final SchuelerStatistikGesamt schueler) {

		religionExport.insgesamtZusammen++;

		final boolean istWeiblich = Geschlecht.W == Geschlecht.fromValue(schueler.geschlecht);
		if (istWeiblich) {
			religionExport.insgesamtWeiblich++;
		}

		final Religion religion = resolveReligion(schueler);
		if (religion == null) {
			return;
		}

		switch (religion) {
			case ER -> erhoehenER(religionExport, schueler, istWeiblich);
			case KR -> erhoehenKR(religionExport, schueler, istWeiblich);
			//() -> religionExport.juedischZusammen++ bedeutet: Mach das ++, wenn run() aufgerufen wird
			case HR -> erhoehen(() -> religionExport.juedischZusammen++, () -> religionExport.juedischWeiblich++, istWeiblich);
			case OR, XO -> erhoehen(() -> religionExport.sonstOrthZusammen++, () -> religionExport.sonstOrthWeiblich++, istWeiblich);
			case SO -> erhoehen(() -> religionExport.syrOrthZusammen++, () -> religionExport.syrOrthWeiblich++, istWeiblich);
			case IR -> erhoehen(() -> religionExport.islamischZusammen++, () -> religionExport.islamischWeiblich++, istWeiblich);
			case AR -> erhoehen(() -> religionExport.alevitischZusammen++, () -> religionExport.alevitischWeiblich++, istWeiblich);
			case ME -> erhoehen(() -> religionExport.mennonitenZusammen++, () -> religionExport.mennonitenWeiblich++, istWeiblich);
			case XR -> erhoehen(() -> religionExport.andereZusammen++, () -> religionExport.andereWeiblich++, istWeiblich);
			case OH -> erhoehen(() -> religionExport.ohneZusammen++, () -> religionExport.ohneWeiblich++, istWeiblich);
			//TODO: Aufsummieren 'ohneUnterricht...'
			default -> fehlermeldungen.add("Unbekannte Religion: " + religion);
		}
	}

	/**
	 * Erhöhen der Werte mit zusätzlicher Auswertung, ob eine Abmeldung vorliegt.
	 *
	 * @param export
	 * @param s
	 * @param istWeiblich
	 */
	private void erhoehenER(final ReligionszugehoerigkeitenStatistikExport export,
			final SchuelerStatistikGesamt s,
			final boolean istWeiblich) {

		erhoehen(() -> export.evZusammen++, () -> export.evWeiblich++, istWeiblich);

		try {
			if (istAbmeldungRelevant(s)) {
				erhoehen(() -> export.abmeldungenEvZusammen++,
						() -> export.abmeldungenEvWeiblich++,
						istWeiblich);
			}
		} catch (final InvalidDateException e) {
			fehlermeldungen.add("Ungültiges Datum: " + e.getMessage());
		}
	}

	/**
	 * Erhöhen der Werte mit zusätzlicher Auswertung, ob eine Abmeldung vorliegt.
	 *
	 * @param export
	 * @param s
	 * @param istWeiblich
	 */
	private void erhoehenKR(final ReligionszugehoerigkeitenStatistikExport export,
			final SchuelerStatistikGesamt s,
			final boolean istWeiblich) {

		erhoehen(() -> export.kathZusammen++, () -> export.kathWeiblich++, istWeiblich);

		try {
			if (istAbmeldungRelevant(s)) {
				erhoehen(() -> export.abmeldungenKathZusammen++,
						() -> export.abmeldungenKathWeiblich++,
						istWeiblich);
			}
		} catch (final InvalidDateException e) {
			fehlermeldungen.add("Ungültiges Datum: " + e.getMessage());
		}
	}

	private void erstellenReligionszugehoerigkeitenStatistikExport(final SchuelerStatistikGesamt schueler,
			final Map<String, ReligionszugehoerigkeitenStatistikExport> religionen) {
		ReligionszugehoerigkeitenStatistikExport religionExport = new ReligionszugehoerigkeitenStatistikExport();
		final SchuelerLernabschnittStatistikGesamt lernabschnitt = schueler.lernabschnitte.getFirst();
		// Ermitteln Jahrgang
		String jahrgang = Jahrgaenge.data().getSchluesselByIDOrNull(jahrgangIdMap.get(lernabschnitt.idJahrgang));

		if ((jahrgang == null) && istJahrgangErforderlich(schulform)) {
			fehlermeldungen
					.add("Zu folgender idJahrgang konnte kein Jahrgang gefunden werden: " + lernabschnitt.idJahrgang + " bei Schüler mit ID: " + schueler.id);
			return;
		}

		// Jahrgänge "01" und "02" müssen in bestimmten Fällen in die Bezeichnung für die Schuleingangsphase umgesetzt werden
		if (Set.of("01", "02").contains(jahrgang) && !(Schulform.BK.equals(schulform) || Schulform.SB.equals(schulform) || Schulform.WB.equals(schulform))) {
			jahrgang = PrimarstufeSchuleingangsphaseBesuchsjahre.data()
					.getSchluesselByIDOrNull(schueler.lernabschnitte.getFirst().idEpJahre);
		}



		// Ermitteln Förderschwerpunkt
		String foerderschwerpunkt = Foerderschwerpunkt.data().getSchluesselByIDOrNull(foerderschwerpunktIdMap.get(lernabschnitt.idFoerderschwerpunkt1));

		if (foerderschwerpunkt == null) {
			// Die IDs können Null-Werte enthalten, wenn der Schüler keinen Förderschwerpunkt hat.
			foerderschwerpunkt = "";
		}

		final Schulgliederung schulgliederung = Schulgliederung.data().getWertByIDOrNull(lernabschnitt.idSchulgliederung);
		final String gliederungStr;

		if (schulgliederung != null) {
			gliederungStr = schulgliederung.name();
		} else {
			gliederungStr = "";
			fehlermeldungen.add("Beim Schüler mit der ID: " + schueler.id + " ist die Schulgliederung NULL.");
		}

		switch (schulform) {
			case G -> {
				religionExport.jahrgang = jahrgang;
				religionExport.schulgliederung = gliederungStr;
				religionExport.bildungsbereich = EIN_LEERZEICHEN;
				religionExport.foerderschwerpunkt = ZWEI_LEERZEICHEN;
			}
			case H, V, R, PS, SK, GE, GY -> {
				religionExport.jahrgang = jahrgang;
				religionExport.schulgliederung = DREI_LEERZEICHEN;
				religionExport.bildungsbereich = EIN_LEERZEICHEN;
				religionExport.foerderschwerpunkt = ZWEI_LEERZEICHEN;
			}
			case S, KS, SR, SG -> {
				religionExport.jahrgang = ZWEI_LEERZEICHEN;
				religionExport.schulgliederung = DREI_LEERZEICHEN;
				religionExport.bildungsbereich = EIN_LEERZEICHEN;
				religionExport.foerderschwerpunkt = foerderschwerpunkt;
			}
			case FW, HI, WF -> {
				religionExport.jahrgang = ZWEI_LEERZEICHEN;
				religionExport.schulgliederung = DREI_LEERZEICHEN;
				religionExport.bildungsbereich =
						AggregationStatistikExport.bauenBildungsbereich(lernabschnitt.idSchulgliederung, lernabschnitt.idFoerderschwerpunkt1, fehlermeldungen);
				religionExport.foerderschwerpunkt = ZWEI_LEERZEICHEN;
			}
			case BK, SB -> {
				religionExport.jahrgang = ZWEI_LEERZEICHEN;
				religionExport.schulgliederung = gliederungStr;
				religionExport.bildungsbereich = EIN_LEERZEICHEN;
				religionExport.foerderschwerpunkt = foerderschwerpunkt;
			}
			default ->
				throw new IllegalArgumentException("Unbekannte Schulform: " + statistikGesamt.schule.schulform);
		}

		final String satzkennungReligion = bauenSatzkennungReligion(religionExport);

		if (religionen.containsKey(satzkennungReligion)) {
			religionExport = religionen.get(satzkennungReligion);
		} else {
			religionen.put(satzkennungReligion, religionExport);
		}

		aufsummierenReligionExport(religionExport, schueler);
	}

	private boolean istAbmeldungRelevant(final SchuelerStatistikGesamt s) throws InvalidDateException {
		if (s.religionabmeldung == null) {
			return false;
		}

		final var stichtag = DateManager.fromValues(aktuellesSchuljahr, 10, 15);

		return (DateManager.from(s.religionabmeldung).compareTo(stichtag) <= 0)
				&& ((s.religionanmeldung == null) || (DateManager.from(s.religionanmeldung).compareTo(stichtag) > 0));


	}

	private Religion resolveReligion(final SchuelerStatistikGesamt schueler) {
		final Long mappedId = religionIdMap.get(schueler.religionID);

		if (mappedId == null) {
			fehlermeldungen.add("Keine Mapping-ID für religionID: " + schueler.religionID);
			return null;
		}

		final Religion religion = Religion.data().getWertByIDOrNull(mappedId);

		if (religion == null) {
			fehlermeldungen.add("Keine Religion gefunden für ID: " + schueler.religionID);
		}

		return religion;
	}


}
