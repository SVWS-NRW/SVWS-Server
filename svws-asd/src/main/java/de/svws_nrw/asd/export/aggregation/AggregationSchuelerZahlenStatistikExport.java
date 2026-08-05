package de.svws_nrw.asd.export.aggregation;

import java.util.LinkedList;

import org.apache.commons.lang3.ArrayUtils;

import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.export.data.StatistikExport;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;

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
public class AggregationSchuelerZahlenStatistikExport {


	/**
	 * Eine Liste der Fehlermeldungen zu den aufgetretenen Fehlern.
	 */
	private final LinkedList<String> fehlermeldungen;

	/**
	 * Die für den Export vorgesehenen Statistikdaten mit den Aggregaten.
	 */
	private final StatistikExport statistikExport;

	/**
	 * Die gesamten Statistikdaten der Schule, welche von einer Schule bei der Erfassung der amtlichen Schulstatistik übertragen werden.
	 */
	private final StatistikGesamt statistikGesamt;

	/**
	 * Das Schuljahr.
	 */
	private final int aktuellesSchuljahr;

	/**
	 * Die Schulform der Schule als Enum {@link Schulform}.
	 */
	private final Schulform schulform;



	/**
	 * Konstruktor
	 * @param statistikGesamt
	 * @param statistikExport
	 * @param aktuellesSchuljahr
	 * @param fehlermeldungen
	 */
	public AggregationSchuelerZahlenStatistikExport(final StatistikGesamt statistikGesamt, final StatistikExport statistikExport, final int aktuellesSchuljahr,
			final LinkedList<String> fehlermeldungen) {
		this.statistikGesamt = statistikGesamt;
		this.statistikExport = statistikExport;
		this.aktuellesSchuljahr = aktuellesSchuljahr;
		this.fehlermeldungen = fehlermeldungen;
		schulform = Schulform.data().getWertByBezeichner(statistikGesamt.schule.schulform);

	}


	/**
	 * Führt die Aggregation der {@link StatistikGesamt}-Daten der Schule in das {@link StatistikExport}-Datenobjekt aus. <br>
	 * Fehlermeldungen zu gegebenenfalls aufgetretenen Fehlern werden in die Liste {@link #fehlermeldungen} geschrieben.
	 *
	 * @return - Ausführung erfolgreich und ohne schwere Fehler
	 */
	public boolean run() {

		if (statistikGesamt == null) {
			return false;
		}

		// K84
		erstellenSchuelerZahlenStatistikExport();

		return true;
	}




	/**
	 * Erstellen der K84-Daten der Schule
	 */
	private void erstellenSchuelerZahlenStatistikExport() {

		for (final SchuelerStatistikGesamt schueler : statistikGesamt.schueler) {

			final Geschlecht geschlecht = Geschlecht.fromValue(schueler.geschlecht);
			final boolean istWeiblich = geschlecht == Geschlecht.W;

			final String schuelerStatus = SchuelerStatus.data().getNameByIDOrNull(Long.valueOf(schueler.status));

			//Aufsummierung: nur Aktive Schüler
			if (schuelerStatus.equals(SchuelerStatus.AKTIV.name())) {
				aufsummierenSchule(schueler, statistikExport, istWeiblich, geschlecht);
			}

			//Die Beurlaubten werden nur für WB aufsummiert
			if (schuelerStatus.equals(SchuelerStatus.BEURLAUBT.name()) && schulform.istWeiterbildung()) {
				erhoehen(() -> statistikExport.schuelerZahlenStatistikExport.studentenBeurlaubtZusammen++,
						() -> statistikExport.schuelerZahlenStatistikExport.studentenBeurlaubtWeiblich++,
						istWeiblich);
			}


		}

	}

	/**
	 * Schuelerzahlen werden aufsummiert
	 *
	 * @param schueler
	 * @param export
	 * @param istWeiblich
	 * @param geschlecht
	 */
	private void aufsummierenSchule(
			final SchuelerStatistikGesamt schueler, final StatistikExport export, final boolean istWeiblich, final Geschlecht geschlecht) {


		export.schuelerZahlenStatistikExport.insgesamtZusammen++;

		if (istWeiblich) {
			export.schuelerZahlenStatistikExport.insgesamtWeiblich++;
		}

		if (geschlecht == Geschlecht.D) {
			export.schuelerZahlenStatistikExport.schuelerDivers++;
		}

		if (geschlecht == Geschlecht.X) {
			export.schuelerZahlenStatistikExport.schuelerOhneAngabe++;
		}

		if (istAuslaender(schueler)) {
			erhoehen(() -> export.schuelerZahlenStatistikExport.auslaenderZusammen++,
					() -> export.schuelerZahlenStatistikExport.auslaenderWeiblich++,
					istWeiblich);
		}

		if (schueler.lernabschnitte.getLast().hatSchwerbehinderungsNachweis) {
			erhoehen(() -> export.schuelerZahlenStatistikExport.schwerstbehinderteZusammen++,
					() -> export.schuelerZahlenStatistikExport.schwerstbehinderteWeiblich++,
					istWeiblich);
		}

		//Bedingung: nur für WB-Schulform und über bezeichner
		if (istVollbeleger(schueler)) {
			erhoehen(() -> export.schuelerZahlenStatistikExport.vollbelegerZusammen++,
					() -> export.schuelerZahlenStatistikExport.vollbelegerWeiblich++,
					istWeiblich);
		}

		//Bedingung: nur für WB-Schulform und über bezeichner
		if (istTeilbeleger(schueler)) {
			erhoehen(() -> export.schuelerZahlenStatistikExport.teilbelegerZusammen++,
					() -> export.schuelerZahlenStatistikExport.teilbelegerWeiblich++,
					istWeiblich);
		}

		//Bedingung: Ausländer und Schulgliederung A01 bis A04 und A13, A19
		if (istAuslaenderBsTeilzeit(schueler)) {
			erhoehen(() -> export.schuelerZahlenStatistikExport.auslaenderBsTeilzeitZusammen++,
					() -> export.schuelerZahlenStatistikExport.auslaenderBsTeilzeitWeiblich++,
					istWeiblich);
		}



		// über bezeichner schueler.Foerderschwerpunkt != keiner und != null
		if ((Foerderschwerpunkt.data().getNameByIDOrNull(schueler.lernabschnitte.getLast().idFoerderschwerpunkt1) != null)
				&& !Foerderschwerpunkt.data().getNameByIDOrNull(schueler.lernabschnitte.getLast().idFoerderschwerpunkt1).equals("KEINER")) {
			erhoehen(() -> export.schuelerZahlenStatistikExport.foerderschwerpunktZusammen++,
					() -> export.schuelerZahlenStatistikExport.foerderschwerpunktWeiblich++,
					istWeiblich);
		}

		//Bedingung:  Ausländer - Schulgliederung A12, A17, A18
		if (istAuslaenderBsVollzeit(schueler)) {
			erhoehen(() -> export.schuelerZahlenStatistikExport.auslaenderBsVollzeitZusammen++,
					() -> export.schuelerZahlenStatistikExport.auslaenderBsVollzeitWeiblich++,
					istWeiblich);
		}

		// Bedingung: Schulgliederung A12, A17, A18
		if (istZurZeitAngemeldetA12(schueler)) {
			erhoehen(() -> export.schuelerZahlenStatistikExport.zurZeitAngemeldetA12Zusammen++,
					() -> export.schuelerZahlenStatistikExport.zurZeitAngemeldetA12Weiblich++,
					istWeiblich);
		}

		//TODO Zahlen kommen noch über einen noch zu implementiernden Sonderbeleg
//		if (istweitereErwarteteSchuelerA12(schueler)) {
//		erhoehen(() -> export.schuelerZahlenStatistikExport.weitereErwarteteSchuelerA12Zusammen++,
//				() -> export.schuelerZahlenStatistikExport.weitereErwarteteSchuelerA12Weiblich++,
//				istWeiblich);
//	   }


		// Bedingung: Schulgliederung A13 , A19
		if (istZurZeitAngemeldetA13(schueler)) {
			erhoehen(() -> export.schuelerZahlenStatistikExport.zurZeitAngemeldetA13Zusammen++,
					() -> export.schuelerZahlenStatistikExport.zurZeitAngemeldetA13Weiblich++,
					istWeiblich);
		}

		//TODO Zahlen kommen noch über einen noch zu implementiernden Sonderbeleg
//		if (istZuErwartendeSchuelerA13(schueler)) {
//		erhoehen(() -> export.schuelerZahlenStatistikExport.zuErwartendeSchuelerA13Zusammen++,
//				() -> export.schuelerZahlenStatistikExport.zuErwartendeSchuelerA13Weiblich++,
//				istWeiblich);
//	   }

		//TODO Zahlen kommen noch über einen noch zu implementiernden Sonderbeleg
//		if (istWeitereErwarteteSchuelerA13(schueler)) {
//		erhoehen(() -> export.schuelerZahlenStatistikExport.weitereErwarteteSchuelerA13Zusammen++,
//				() -> export.schuelerZahlenStatistikExport.weitereErwarteteSchuelerA13Weiblich++,
//				istWeiblich);
//	   }
	}


	/**
	 * Aufsumierung der Schüler mit Organisationsform 'Teilbeleger' (nur WB-Schulform)
	 *
	 * @param schueler SchuelerStatistikGesamt
	 * @return true, wenn schulform=wb und Organisationsform = Teilzeit
	 */
	private boolean istTeilbeleger(final SchuelerStatistikGesamt schueler) {

		final Long id = schueler.lernabschnitte.getLast().idOrganisationsform;
		return schulform.istWeiterbildung() && WeiterbildungskollegOrganisationsformen.data()
				.getNameByIDOrNull(id).equals(WeiterbildungskollegOrganisationsformen.TEILZEIT.name());
	}


	/**
	 * Aufsumierung der Schüler mit Organisationsform 'Vollbeleger'  (nur WB-Schulform)
	 *
	 * @param schueler SchuelerStatistikGesamt
	 * @return true, wenn schulform=wb und Organisationsform = Vollzeit
	 */
	private boolean istVollbeleger(final SchuelerStatistikGesamt schueler) {

		final Long id = schueler.lernabschnitte.getLast().idOrganisationsform;
		return schulform.istWeiterbildung() && WeiterbildungskollegOrganisationsformen.data()
				.getNameByIDOrNull(id).equals(WeiterbildungskollegOrganisationsformen.VOLLZEIT.name());
	}




	/**
	 * Aufsumierung der Schüler, wenn Schulgliederung A13, A19
	 *
	 * @param schueler SchuelerStatistikGesamt
	 * @return true, wenn sgl gültig ist
	 */
	private boolean istZurZeitAngemeldetA13(final SchuelerStatistikGesamt schueler) {
		final String[] sgl = { "A13", "A19" };
		final String schuelerSgl = Schulgliederung.data().getSchluesselByID(schueler.lernabschnitte.getLast().idSchulgliederung);

		return ArrayUtils.contains(sgl, schuelerSgl);
	}


	/**
	 * Aufsumierung der Schüler, wenn Schulgliederung A12, A17, A18
	 *
	 * @param schueler SchuelerStatistikGesamt
	 * @return true, wenn sgl gültig ist
	 */
	private boolean istZurZeitAngemeldetA12(final SchuelerStatistikGesamt schueler) {

		final String[] sgl = { "A12", "A17", "A18" };
		final String schuelerSgl = Schulgliederung.data().getSchluesselByID(schueler.lernabschnitte.getLast().idSchulgliederung);

		return ArrayUtils.contains(sgl, schuelerSgl);

	}


	/**
	 * Aufsumierung der ausländischen Schüler, wenn Schulgliederung A12, A17, A18
	 *
	 * @param schueler SchuelerStatistikGesamt
	 * @return true, wenn sgl gültig ist
	 */
	private boolean istAuslaenderBsVollzeit(final SchuelerStatistikGesamt schueler) {
		if (istAuslaender(schueler)) {

			final String[] sgl = { "A12", "A17", "A18" };
			final String schuelerSgl = Schulgliederung.data().getSchluesselByID(schueler.lernabschnitte.getLast().idSchulgliederung);

			return ArrayUtils.contains(sgl, schuelerSgl);

		}
		return false;
	}


	/**
	 * Aufsumierung der ausländischen Schüler, wenn Schulgliederung A01 bis A04 und A13, A19
	 *
	 * @param schueler SchuelerStatistikGesamt
	 * @return true, wenn sgl gültig ist
	 */
	private boolean istAuslaenderBsTeilzeit(final SchuelerStatistikGesamt schueler) {

		if (istAuslaender(schueler)) {

			final String[] sgl = { "A01", "A02", "A03", "A04", "A13", "A19" };
			final String schuelerSgl = Schulgliederung.data().getSchluesselByID(schueler.lernabschnitte.getLast().idSchulgliederung);

			return ArrayUtils.contains(sgl, schuelerSgl);

		}
		return false;
	}


	/**
	 * Erhöht den Gesamtzähler und – falls weiblich – auch den weiblich-zähler.
	 *
	 * @param zusammen Zähler für insgesamt
	 * @param weiblich Zähler für weiblich
	 * @param istWeiblich true, wenn es sich um eine Schülerin handelt
	 */
	private static void erhoehen(
			final Runnable zusammen,
			final Runnable weiblich,
			final boolean istWeiblich) {

		zusammen.run();

		if (istWeiblich) {
			weiblich.run();
		}
	}

	/**
	 * Prüft, ob der Schüler ausländer ist.
	 *
	 * @param schueler SchuelerStatistikGesamt
	 * @return true, schueler ausländer ist
	 */
	private boolean istAuslaender(final SchuelerStatistikGesamt schueler) {
		return !schueler.idStaatsangehoerigkeit.equals(Nationalitaeten.getDEU().id(aktuellesSchuljahr));
	}


}
