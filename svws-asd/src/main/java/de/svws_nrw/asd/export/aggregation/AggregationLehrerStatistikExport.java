package de.svws_nrw.asd.export.aggregation;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.export.data.LehrerAnrechungenStatistikExport;
import de.svws_nrw.asd.export.data.LehrerFachrichtungenStatistikExport;
import de.svws_nrw.asd.export.data.LehrerLehraemterStatistikExport;
import de.svws_nrw.asd.export.data.LehrerLehrbefaehigungenStatistikExport;
import de.svws_nrw.asd.export.data.LehrerMehrleistungenStatistikExport;
import de.svws_nrw.asd.export.data.LehrerMinderleistungenStatistikExport;
import de.svws_nrw.asd.export.data.LehrerStatistikExport;
import de.svws_nrw.asd.export.data.StatistikExport;
import de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund;
import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.types.lehrer.LehrerFachrichtung;
import de.svws_nrw.asd.types.lehrer.LehrerFachrichtungAnerkennung;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigungAnerkennung;
import de.svws_nrw.asd.types.lehrer.LehrerMehrleistungsarten;
import de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
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
public class AggregationLehrerStatistikExport {


	/**
	 * Eine Liste der Fehlermeldungen zu den aufgetretenen Fehlern.
	 */
	private final List<String> fehlermeldungen;

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
	 * @param fehlermeldungen
	 * @param statistikExport
	 */
	public AggregationLehrerStatistikExport(final StatistikGesamt statistikGesamt, final StatistikExport statistikExport,
			final List<String> fehlermeldungen) {
		this.statistikGesamt = statistikGesamt;
		this.statistikExport = statistikExport;
		this.fehlermeldungen = fehlermeldungen;
	}

	/**
	 * Hier werden für den Export Lehrämter, Fachrichtungen und Lehrbefähigungen erstellt
	 * @param lehramt
	 * @param lehrerExport
	 */
	private static void erstellenLehraemterStatistikExport(final LehrerLehramtEintrag lehramt, final LehrerStatistikExport lehrerExport) {
		final LehrerLehraemterStatistikExport lehramtExport = new LehrerLehraemterStatistikExport();
		lehramtExport.lehramt = LehrerLehramt.data().getSchluesselByIDOrNull(lehramt.idKatalogLehramt);
		lehrerExport.lehraemterStatistikExport.add(lehramtExport);


		lehramt.fachrichtungen.stream().forEach(fachrichtung -> erstellenLehrerFachrichtungenStatistikExport(fachrichtung, lehrerExport));
		lehramt.lehrbefaehigungen.stream().forEach(lehrbefaehigung -> erstellenLehrerLehrbefaehigungenStatistikExport(lehrbefaehigung, lehrerExport));
	}

	private static void erstellenLehrerAnrechungenStatistikExport(final LehrerPersonalabschnittsdatenAnrechnungsstunden anrechnung,
			final LehrerStatistikExport lehrerExport) {
		final LehrerAnrechungenStatistikExport anrechnungExport = new LehrerAnrechungenStatistikExport();
		anrechnungExport.grund = LehrerAnrechnungsgrund.data().getSchluesselByIDOrNull(anrechnung.idGrund);
		anrechnungExport.anrechungsstunden = anrechnung.anzahl;

		lehrerExport.anrechungenStatistikExport.add(anrechnungExport);
	}

	private static void erstellenLehrerFachrichtungenStatistikExport(final LehrerFachrichtungEintrag fachrichtung, final LehrerStatistikExport lehrerExport) {
		final LehrerFachrichtungenStatistikExport fachrichtungExport = new LehrerFachrichtungenStatistikExport();
		fachrichtungExport.fachrichtung = LehrerFachrichtung.data().getSchluesselByIDOrNull(fachrichtung.idFachrichtung);
		fachrichtungExport.qualifikation = LehrerFachrichtungAnerkennung.data().getSchluesselByIDOrNull(fachrichtung.idAnerkennungsgrund);

		lehrerExport.fachrichtungenStatistikExport.add(fachrichtungExport);

	}

	private static void erstellenLehrerLehrbefaehigungenStatistikExport(final LehrerLehrbefaehigungEintrag lehrbefaehigung,
			final LehrerStatistikExport lehrerExport) {
		final LehrerLehrbefaehigungenStatistikExport lehrbefaehigungExport = new LehrerLehrbefaehigungenStatistikExport();
		lehrbefaehigungExport.lehrbefaehigung = LehrerLehrbefaehigung.data().getSchluesselByIDOrNull(lehrbefaehigung.idLehrbefaehigung);
		lehrbefaehigungExport.qualifikation = LehrerLehrbefaehigungAnerkennung.data().getSchluesselByIDOrNull(lehrbefaehigung.idAnerkennungsgrund);

		lehrerExport.lehrbefaehigungenStatistikExport.add(lehrbefaehigungExport);
	}

	private static void erstellenLehrerMehrleistungenStatistikExport(final LehrerPersonalabschnittsdatenAnrechnungsstunden mehrleistung,
			final LehrerStatistikExport lehrerExport) {
		final LehrerMehrleistungenStatistikExport mehrleistungExport = new LehrerMehrleistungenStatistikExport();
		mehrleistungExport.grund = LehrerMehrleistungsarten.data().getSchluesselByIDOrNull(mehrleistung.idGrund);
		mehrleistungExport.mehrleistungsstunden = mehrleistung.anzahl;

		lehrerExport.mehrleistungenStatistikExport.add(mehrleistungExport);
	}

	private static void erstellenLehrerMinderleistungenStatistikExport(final LehrerPersonalabschnittsdatenAnrechnungsstunden minderleistung,
			final LehrerStatistikExport lehrerExport) {
		final LehrerMinderleistungenStatistikExport minderleistungExport = new LehrerMinderleistungenStatistikExport();
		minderleistungExport.grund = LehrerMinderleistungsarten.data().getSchluesselByIDOrNull(minderleistung.idGrund);
		minderleistungExport.minderleistungsstunden = minderleistung.anzahl;

		lehrerExport.minderleistungenStatistikExport.add(minderleistungExport);
	}

	/**
	 * Führt die Aggregation der {@link StatistikGesamt}-Daten der Lehrer in das {@link StatistikExport}-Datenobjekt aus. <br>
	 * Fehlermeldungen zu gegebenenfalls aufgetretenen Fehlern werden in die Liste {@link #fehlermeldungen} geschrieben.
	 *
	 * @return - Ausführung erfolgreich und ohne schwere Fehler
	 */
	public boolean run() {

		if (statistikGesamt == null) {
			return false;
		}

		// L61-L68
		statistikGesamt.lehrer.stream().forEach(this::erstellenLehrerStatistikExport);

		return true;
	}


	private void erstellenLehrerStatistikExport(final LehrerStatistikGesamt lehrer) {
		final LehrerStatistikExport lehrerExport = new LehrerStatistikExport();
		lehrerExport.kuerzel = lehrer.kuerzel;
		lehrerExport.nachname = lehrer.nachname;
		lehrerExport.vorname = lehrer.vorname;
		try {
			lehrerExport.geburtsdatumTag = String.valueOf(DateManager.from(lehrer.geburtsdatum).getTag());
			lehrerExport.geburtsdatumMonat = String.valueOf(DateManager.from(lehrer.geburtsdatum).getMonat());
			lehrerExport.geburtsdatumJahr = String.valueOf(DateManager.from(lehrer.geburtsdatum).getJahr());
		} catch (final InvalidDateException e) {
			lehrerExport.geburtsdatumTag = null;
			lehrerExport.geburtsdatumMonat = null;
			lehrerExport.geburtsdatumJahr = null;
			fehlermeldungen.add(e.getLocalizedMessage() + " Das Geburtsdatum des Lehrers mit folgender ID konnte nicht geparst werden " + lehrer.id);
		}
		lehrerExport.geschlecht = lehrer.geschlecht;
		lehrerExport.staatsangehoerigkeit = AggregationUtils.ermittleStaatsangehoerigkeitSchluessel(lehrer.idStaatsangehoerigkeit);
		lehrerExport.rechtsverhaeltnis = LehrerRechtsverhaeltnis.data().getNameByIDOrNull(lehrer.idRechtsverhaeltnis);
		lehrerExport.beschaeftigungsart = LehrerBeschaeftigungsart.data().getNameByIDOrNull(lehrer.idBeschaeftigungsart);
		lehrerExport.einsatzstatus = LehrerEinsatzstatus.data().getSchluesselByIDOrNull(lehrer.idEinsatzstatus);
		if ("*".equals(lehrerExport.einsatzstatus)) {
			lehrerExport.einsatzstatus = "";
		}
		lehrerExport.pflichtstundensoll = lehrer.pflichtstundensoll == null ? 0.0 : lehrer.pflichtstundensoll;
		lehrerExport.zuErteilenderUnterricht = ((lehrerExport.pflichtstundensoll - lehrer.anrechnungen.stream().mapToDouble(e -> e.anzahl).sum())
				+ lehrer.mehrleistung.stream().mapToDouble(e -> e.anzahl).sum()) - lehrer.minderleistung.stream().mapToDouble(e -> e.anzahl).sum();

		lehrer.lehraemter.stream().forEach(lehramt -> erstellenLehraemterStatistikExport(lehramt, lehrerExport));
		lehrer.anrechnungen.stream().forEach(anrechnung -> erstellenLehrerAnrechungenStatistikExport(anrechnung, lehrerExport));
		lehrer.mehrleistung.stream().forEach(mehrleistung -> erstellenLehrerMehrleistungenStatistikExport(mehrleistung, lehrerExport));
		lehrer.minderleistung.stream().forEach(minderleistung -> erstellenLehrerMinderleistungenStatistikExport(minderleistung, lehrerExport));
		//TODO LehrerErteilteStundenStatistikExport

		statistikExport.lehrerStatistikExport.add(lehrerExport);

	}


}
