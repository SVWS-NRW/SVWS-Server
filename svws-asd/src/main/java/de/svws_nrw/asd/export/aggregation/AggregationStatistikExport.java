package de.svws_nrw.asd.export.aggregation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuleAdressenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.export.data.LehrerAnrechungenStatistikExport;
import de.svws_nrw.asd.export.data.LehrerFachrichtungenStatistikExport;
import de.svws_nrw.asd.export.data.LehrerLehraemterStatistikExport;
import de.svws_nrw.asd.export.data.LehrerLehrbefaehigungenStatistikExport;
import de.svws_nrw.asd.export.data.LehrerMehrleistungenStatistikExport;
import de.svws_nrw.asd.export.data.LehrerMinderleistungenStatistikExport;
import de.svws_nrw.asd.export.data.LehrerStatistikExport;
import de.svws_nrw.asd.export.data.ReligionszugehoerigkeitenStatistikExport;
import de.svws_nrw.asd.export.data.SchuleAdressenStatistikExport;
import de.svws_nrw.asd.export.data.SchuleStatistikExport;
import de.svws_nrw.asd.export.data.StatistikExport;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.types.schule.FormOffenerGanztag;
import de.svws_nrw.asd.types.schule.Reformpaedagogik;
import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.asd.types.schule.Schulform;
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
public class AggregationStatistikExport {

	private final LinkedList<String> fehlermeldungen = new LinkedList<>();

	/**
	 * Die gesamten Statistikdaten der Schule, welche von einer Schule bei der Erfassung der amtlichen Schulstatistik übertragen werden.
	 */
	private StatistikGesamt statistikGesamt;
	/**
	 * Die für den Export vorgesehenen Statistikdaten mit den Aggregaten.
	 */
	private StatistikExport statistikExport;

	/**
	 * Die Schulform der Schule als Enum {@link Schulform}.
	 */
	private final Schulform schulform;

	/**
	 * Ein String mit einem Leerzeichen.
	 */
	private static final String EIN_LEERZEICHEN = " ";

	/**
	 * Ein String mit zwei Leerzeichen.
	 */
	private static final String ZWEI_LEERZEICHEN = "  ";

	/**
	 * Ein String mit drei Leerzeichen.
	 */
	private static final String DREI_LEERZEICHEN = "   ";

	/**
	 * Zuordnug der Jahrgang-IDs der Schule zu den idJahrgang des Katalogs.
	 */
	private final Map<Long, Long> jahrgangIds;

	/**
	 * Zuordnug der Foerderschwerpunkt-IDs der Schule zu den idFoerderschwerpunkt des Katalogs.
	 */
	private final Map<Long, Long> foerderschwerpunktIds;

	/**
	 * Zuordnug der Religion-IDs der Schule zu den idReligion des Katalogs.
	 */
	private final Map<Long, Long> religionIds;

	/**
	 * Zuordnug der Schuljahresabschnitt-IDs der Schule zu dem zugehörigen Schuljahr.
	 */
	private final int aktuellesSchuljahr;


	/**
	 * Konstruktor
	 * @param statistikGesamt
	 */
	public AggregationStatistikExport(final StatistikGesamt statistikGesamt) {
		this.statistikGesamt = statistikGesamt;
		this.schulform = Schulform.data().getWertByBezeichner(statistikGesamt.schule.schulform);
		this.jahrgangIds = statistikGesamt.jahrgaenge.stream().collect(Collectors.toMap(e -> e.id, e -> e.idKatalog));
		this.foerderschwerpunktIds = statistikGesamt.foederschwerpunkte.stream().collect(Collectors.toMap(e -> e.id, e -> e.idKatalog));
		this.religionIds = statistikGesamt.religionen.stream().collect(Collectors.toMap(e -> e.id, e -> e.idKatalog));
		Optional<Schuljahresabschnitt> optional =
				statistikGesamt.schule.abschnitte.stream().filter(e -> e.id == statistikGesamt.schule.idSchuljahresabschnitt).findFirst();
		this.aktuellesSchuljahr = optional.isPresent() ? optional.get().schuljahr : 0;
	}

	/**
	 * Führt die Aggregation der {@link StatistikGesamt}-Daten in das {@link StatistikExport}-Datenobjekt aus.
	 *
	 * @return - Ausführung erfolgreich und ohne Fehler
	 */
	public boolean run() {

		if (statistikGesamt == null) {
			return false;
		}

		statistikExport = new StatistikExport();
		// B01 und B02
		erstellenSchuleStatistikExport();
		// S42
		Map<String, ReligionszugehoerigkeitenStatistikExport> religionen = new HashMap<>();
		statistikGesamt.schueler.stream().forEach(schueler -> erstellenReligionszugehoerigkeitenStatistikExport(schueler, religionen));
		statistikExport.religionszugehoerigkeitenStatistikExport.addAll(religionen.values());
		// L61-L68
		statistikGesamt.lehrer.stream().forEach(this::erstellenLehrerStatistikExport);

		// TODO: syso entfernen
		System.out.println(statistikExport.toString());

		return true;
	}

	/**
	 * Hier werden für den Export Lehrämter, Fachrichtungen und Lehrbefähigungen erstellt
	 * @param lehramt
	 * @param lehrerExport
	 */
	private void erstellenLehraemterStatistikExport(final LehrerLehramtEintrag lehramt, final LehrerStatistikExport lehrerExport) {
		LehrerLehraemterStatistikExport lehramtExport = new LehrerLehraemterStatistikExport();
		//lehramtExport.lehramt = lehramt.leh
		lehrerExport.lehraemterStatistikExport.add(lehramtExport);


		lehramt.fachrichtungen.stream().forEach(fachrichtung -> erstellenLehrerFachrichtungenStatistikExport(fachrichtung, lehrerExport));
		lehramt.lehrbefaehigungen.stream().forEach(lehrbefaehigung -> erstellenLehrerLehrbefaehigungenStatistikExport(lehrbefaehigung, lehrerExport));
	}

	private void erstellenLehrerAnrechungenStatistikExport(final LehrerPersonalabschnittsdatenAnrechnungsstunden anrechnung,
			final LehrerStatistikExport lehrerExport) {
		LehrerAnrechungenStatistikExport anrechnungExport = new LehrerAnrechungenStatistikExport();
		//anrechnungExport.grund = anrechnung.idGrund;
		anrechnungExport.anrechungsstunden = anrechnung.anzahl;

		lehrerExport.anrechungenStatistikExport.add(anrechnungExport);
	}

	private void erstellenLehrerFachrichtungenStatistikExport(final LehrerFachrichtungEintrag fachrichtung, final LehrerStatistikExport lehrerExport) {
		LehrerFachrichtungenStatistikExport fachrichtungExport = new LehrerFachrichtungenStatistikExport();
		//fachrichtungExport.fachrichtung = fachrichtung.idFachrichtung;
		//fachrichtungExport.qualifikation = fachrichtung.idAnerkennungsgrund;

		lehrerExport.fachrichtungenStatistikExport.add(fachrichtungExport);

	}

	private void erstellenLehrerLehrbefaehigungenStatistikExport(final LehrerLehrbefaehigungEintrag lehrbefaehigung, final LehrerStatistikExport lehrerExport) {
		LehrerLehrbefaehigungenStatistikExport lehrbefaehigungExport = new LehrerLehrbefaehigungenStatistikExport();
//		lehrbefaehigungExport.lehrbefaehigung = lehrbefaehigung.idLehrbefaehigung;
//		lehrbefaehigungExport.qualifikation = lehrbefaehigung.idAnerkennungsgrund;

		lehrerExport.lehrbefaehigungenStatistikExport.add(lehrbefaehigungExport);
	}

	private void erstellenLehrerMehrleistungenStatistikExport(final LehrerPersonalabschnittsdatenAnrechnungsstunden mehrleistung,
			final LehrerStatistikExport lehrerExport) {
		LehrerMehrleistungenStatistikExport mehrleistungExport = new LehrerMehrleistungenStatistikExport();
		//mehrleistungExport.grund = mehrleistung.idGrund;
		mehrleistungExport.mehrleistungsstunden = mehrleistung.anzahl;

		lehrerExport.mehrleistungenStatistikExport.add(mehrleistungExport);
	}

	private void erstellenLehrerMinderleistungenStatistikExport(final LehrerPersonalabschnittsdatenAnrechnungsstunden minderleistung,
			final LehrerStatistikExport lehrerExport) {
		LehrerMinderleistungenStatistikExport minderleistungExport = new LehrerMinderleistungenStatistikExport();
		//minderleistungExport.grund = minderleistung.idGrund;
		minderleistungExport.minderleistungsstunden = minderleistung.anzahl;

		lehrerExport.minderleistungenStatistikExport.add(minderleistungExport);
	}

	private void erstellenLehrerStatistikExport(final LehrerStatistikGesamt lehrer) {
		LehrerStatistikExport lehrerExport = new LehrerStatistikExport();
		lehrerExport.kuerzel = lehrer.kuerzel;
		lehrerExport.nachname = lehrer.nachname;
		lehrerExport.vorname = lehrer.vorname;
		try {
			lehrerExport.geburtsdatumTag = String.valueOf(DateManager.from(lehrer.geburtsdatum).getTag());
			lehrerExport.geburtsdatumMonat = String.valueOf(DateManager.from(lehrer.geburtsdatum).getMonat());
			lehrerExport.geburtsdatumJahr = String.valueOf(DateManager.from(lehrer.geburtsdatum).getJahr());
		} catch (InvalidDateException e) {
			lehrerExport.geburtsdatumTag = null;
			lehrerExport.geburtsdatumMonat = null;
			lehrerExport.geburtsdatumJahr = null;
			fehlermeldungen.add(e.getLocalizedMessage() + " Das Geburtsdatum des Lehrers konnte nicht geparst werden " + "[" + this.getClass() + "]");
		}
		lehrerExport.geschlecht = lehrer.geschlecht;
		lehrerExport.staatsangehoerigkeit = lehrer.staatsangehoerigkeitID;
		lehrerExport.rechtsverhaeltnis = LehrerRechtsverhaeltnis.data().getNameByIDOrNull(lehrer.idRechtsverhaeltnis);
		lehrerExport.beschaeftigungsart = LehrerBeschaeftigungsart.data().getNameByIDOrNull(lehrer.idBeschaeftigungsart);
		lehrerExport.einsatzstatus = LehrerEinsatzstatus.data().getNameByIDOrNull(lehrer.idEinsatzstatus);
		lehrerExport.pflichtstundensoll = lehrer.pflichtstundensoll == null ? 0.0 : lehrer.pflichtstundensoll;
		//	lehrerExport.zuErteilenderUnterricht = lehrer.zuErteilenderUnterricht;
		//	lehrerExport.erteilerUnterricht = lehrer.erteilerUnterricht;


		lehrer.lehraemter.stream().forEach(lehramt -> erstellenLehraemterStatistikExport(lehramt, lehrerExport));
		lehrer.anrechnungen.stream().forEach(anrechnung -> erstellenLehrerAnrechungenStatistikExport(anrechnung, lehrerExport));
		lehrer.mehrleistung.stream().forEach(mehrleistung -> erstellenLehrerMehrleistungenStatistikExport(mehrleistung, lehrerExport));
		lehrer.minderleistung.stream().forEach(minderleistung -> erstellenLehrerMinderleistungenStatistikExport(minderleistung, lehrerExport));
		//TODO LehrerErteilteStundenStatistikExport

		statistikExport.lehrerStatistikExport.add(lehrerExport);

	}

	private void erstellenReligionszugehoerigkeitenStatistikExport(final SchuelerStatistikGesamt schueler,
			final Map<String, ReligionszugehoerigkeitenStatistikExport> religionen) {
		ReligionszugehoerigkeitenStatistikExport religionExport = new ReligionszugehoerigkeitenStatistikExport();
		SchuelerLernabschnittStatistikGesamt lernabschnitt = schueler.lernabschnitte.getFirst();
		// Ermitteln Jahrgang
		String jahrgang = Jahrgaenge.data().getSchluesselByIDOrNull(jahrgangIds.get(lernabschnitt.idJahrgang));

		if (jahrgang == null) {
			fehlermeldungen.add("Zu folgender idJahrgang konnte kein Jahrgang gefunden werden: " + lernabschnitt.idJahrgang);
			return;
		}
		// Ermitteln Förderschwerpunkt
		String foerderschwerpunkt = Foerderschwerpunkt.data().getSchluesselByIDOrNull(foerderschwerpunktIds.get(lernabschnitt.idFoerderschwerpunkt1));

		if (foerderschwerpunkt == null) {
			fehlermeldungen.add("Zu folgender idFoerderschwerpunkt1 konnte kein Förderschwerpunkt gefunden werden: " + lernabschnitt.idFoerderschwerpunkt1);
			return;
		}


		// Für die Schulform 25 (WB, Weiterbildungskolleg) wird diese Satzart nicht erhoben.
		switch (this.schulform) {
			case G -> {
				religionExport.jahrgang = jahrgang;
				religionExport.schulgliederung = lernabschnitt.schulgliederung;
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
				religionExport.bildungsbereich = bauenBildungsbereich(lernabschnitt.schulgliederung, lernabschnitt.idFoerderschwerpunkt1);
				religionExport.foerderschwerpunkt = ZWEI_LEERZEICHEN;
			}

			case BK, SB -> {
				religionExport.jahrgang = ZWEI_LEERZEICHEN;
				religionExport.schulgliederung = lernabschnitt.schulgliederung;
				religionExport.bildungsbereich = EIN_LEERZEICHEN;
				religionExport.foerderschwerpunkt = foerderschwerpunkt;
			}

			default ->
				throw new IllegalArgumentException("Unbekannte Schulform: " + statistikGesamt.schule.schulform);
		}

		final String satzkennungReli = bauenSatzkennungReli(religionExport);

		if (religionen.containsKey(satzkennungReli)) {
			religionExport = religionen.get(satzkennungReli);
		} else {
			religionen.put(satzkennungReli, religionExport);
		}

		aufsummierenReligionExport(religionExport, schueler);
	}


	/**
	 * @param idFoerderschwerpunkt1
	 * @param schulgliederung
	 * @return der Bildungsbereich
	 */
	private String bauenBildungsbereich(final String schulgliederung, final Long idFoerderschwerpunkt1) {

		if (DREI_LEERZEICHEN.equals(schulgliederung) && idFoerderschwerpunkt1 == null) { // If unnötig! Kann weg?!
			return "A";
		}

		if (idFoerderschwerpunkt1 != null) {
			return "S";
		}

		if ("K02".equals(schulgliederung)) {
			return "K";
		}

		if ("H01".equals(schulgliederung) || "H02".equals(schulgliederung)) {
			return "B";
		}

		fehlermeldungen.add(
				"Bildungsbereich konnnte nicht ermittelt werden - Schulgliederung: " + schulgliederung + " idFörderschwerpunkt1: " + idFoerderschwerpunkt1);
		return null;
	}

	/**
	 * @param religionExport
	 * @param schueler
	 */
	private void aufsummierenReligionExport(final ReligionszugehoerigkeitenStatistikExport religionExport, final SchuelerStatistikGesamt schueler) {
		religionExport.insgesamtZusammen++;
		Geschlecht geschlecht = Geschlecht.fromValue(schueler.geschlecht);

		if (Geschlecht.W == geschlecht) {
			religionExport.insgesamtWeiblich++;
		}

		Religion religion = null;

		if (religionIds.get(schueler.religionID) != null) {
			religion = Religion.data().getWertByIDOrNull(religionIds.get(schueler.religionID));
		}

		if (religion == null) {
			fehlermeldungen.add("Zu folgender religionID konnte keine Religion gefunden werden: " + schueler.religionID);
			return;
		}

		switch (religion) {
			case ER -> {
				religionExport.evZusammen++;

				if (Geschlecht.W == geschlecht) {
					religionExport.evWeiblich++;
				}
				// Abmeldungen
				try {
					if (DateManager.from(schueler.religionabmeldung).compareTo(DateManager.fromValues(aktuellesSchuljahr, 10, 15)) <= 0
							&& ((schueler.religionanmeldung == null)
									|| DateManager.from(schueler.religionanmeldung).compareTo(DateManager.fromValues(aktuellesSchuljahr, 10, 15)) > 0)) {
						religionExport.abmeldungenEvZusammen++;

						if (Geschlecht.W == geschlecht) {
							religionExport.abmeldungenEvWeiblich++;
						}
					}
				} catch (InvalidDateException e) {
					fehlermeldungen.add("Ungültiges Datumsformat bei religionabmeldung oder religionanmeldung: " + e.getLocalizedMessage());
				}
			}

			case KR -> {
				religionExport.kathZusammen++;

				if (Geschlecht.W == geschlecht) {
					religionExport.kathWeiblich++;
				}
				// Abmeldungen
				try {
					if (DateManager.from(schueler.religionabmeldung).compareTo(DateManager.fromValues(aktuellesSchuljahr, 10, 15)) <= 0
							&& ((schueler.religionanmeldung == null)
									|| DateManager.from(schueler.religionanmeldung).compareTo(DateManager.fromValues(aktuellesSchuljahr, 10, 15)) > 0)) {
						religionExport.abmeldungenKathZusammen++;

						if (Geschlecht.W == geschlecht) {
							religionExport.abmeldungenKathWeiblich++;
						}
					}
				} catch (InvalidDateException e) {
					fehlermeldungen.add("Ungültiges Datumsformat bei religionabmeldung oder religionanmeldung: " + e.getLocalizedMessage());
				}

			}

			case HR -> {
				religionExport.juedischZusammen++;

				if (Geschlecht.W == geschlecht) {
					religionExport.juedischWeiblich++;
				}
			}

			case OR, XO -> {
				religionExport.sonstOrthZusammen++;

				if (Geschlecht.W == geschlecht) {
					religionExport.sonstOrthWeiblich++;
				}
			}

			case SO -> {
				religionExport.syrOrthZusammen++;

				if (Geschlecht.W == geschlecht) {
					religionExport.syrOrthWeiblich++;
				}
			}

			case IR -> {
				religionExport.islamischZusammen++;

				if (Geschlecht.W == geschlecht) {
					religionExport.islamischWeiblich++;
				}
			}

			case AR -> {
				religionExport.alevitischZusammen++;

				if (Geschlecht.W == geschlecht) {
					religionExport.alevitischWeiblich++;
				}
			}

			case ME -> {
				religionExport.mennonitenZusammen++;

				if (Geschlecht.W == geschlecht) {
					religionExport.mennonitenWeiblich++;
				}
			}

			case XR -> {
				religionExport.andereZusammen++;

				if (Geschlecht.W == geschlecht) {
					religionExport.andereWeiblich++;
				}
			}

			case OH -> {
				religionExport.ohneZusammen++;

				if (Geschlecht.W == geschlecht) {
					religionExport.ohneWeiblich++;
				}
			}

			default ->
				getFehlermeldungen().add("Zu folgender Religion konnte kein Wert ermittelt werden: " + religion);
		}

		//TODO: Aufsummieren 'ohneUnterricht...'
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
	private static String bauenSatzkennungReli(final ReligionszugehoerigkeitenStatistikExport religionExport) {
		StringBuilder satzkennungReli = new StringBuilder();
		satzkennungReli.append(religionExport.jahrgang);
		satzkennungReli.append(religionExport.schulgliederung);
		satzkennungReli.append(religionExport.bildungsbereich);
		satzkennungReli.append(religionExport.foerderschwerpunkt);

		return satzkennungReli.toString();
	}

	/**
	 * Erstellen der 'Adressen einer Schule (B02)'. <br>
	 * Die Adresse wird der Liste {@link SchuleStatistikExport#adressenStatistikExport} im Objekt {@link #statistikExport} hinzugefügt.
	 *
	 * @param adresse - Eine Adresse der Schule
	 */
	private void erstellenSchuleAdressenStatistikExport(final SchuleAdressenStatistikGesamt adresse) {
		SchuleAdressenStatistikExport adresseExport = new SchuleAdressenStatistikExport();
		adresseExport.id = adresse.id;
		adresseExport.adresskennzeichen = adresse.adresskennzeichen;

		// TODO: Soll noch von SVWS-Team aus Schuldateidaten gefüllt werden
		//	adresseExport.regionalschluesselLaenderkuerzel = adresse.regionalschluesselLaenderkuerzel;
		//	adresseExport.regionalschluesselGemeindekennzahl = adresse.regionalschluesselGemeindekennzahl;

		adresseExport.strassenname = adresse.strassenname;
		adresseExport.hausnummer = adresse.hausnummer;
		adresseExport.plz = adresse.plz;
		adresseExport.ort = adresse.ort;
		adresseExport.istHauptsitz = adresse.istHauptsitz;
		adresseExport.standortkennzeichen = adresse.standortkennzeichen;
		adresseExport.istAktiv = adresse.istAktiv;
		adresseExport.idArt = adresse.idArt;

		// TODO: Soll noch von SVWS-Team aus Schuldateidaten gefüllt werden
		//	adresseExport.verortungQualitaet = adresse.verortungQualitaet;
		//  adresseExport.koordinateRechtswert = adresse.koordinateRechtswert;
		//  adresseExport.koordinateHochwert = adresse.koordinateHochwert;

		//  adresseExport.istAdressvorgabedatensatz = adresse.istAdressvorgabedatensatz; // Kann evtl. entfallen
		//  adresseExport.datumStempelVorgabedaten = adresse.datumStempelVorgabedaten; // Kann evtl. entfallen

		// TODO: Summe bilden oder Kontrollzahl übernehmen?
		//  adresseExport.schuleAdressenSchuelerStatistikExport.insgesamtZusammen = adresse.;

		statistikExport.schuleStatistikExport.adressenStatistikExport.add(adresseExport);
	}

	/**
	 * Erstellen der 'Daten der Schule (B01)' und der 'Adressen einer Schule (B02)'.
	 */
	private void erstellenSchuleStatistikExport() {
		statistikExport.schuleStatistikExport.schulNr = statistikGesamt.schule.schulNr;
		statistikExport.schuleStatistikExport.schulform = statistikGesamt.schule.schulform;
		statistikExport.schuleStatistikExport.bezeichnung1 = statistikGesamt.schule.bezeichnung1;
		statistikExport.schuleStatistikExport.bezeichnung2 = statistikGesamt.schule.bezeichnung2;
		statistikExport.schuleStatistikExport.bezeichnung3 = statistikGesamt.schule.bezeichnung3;
		StringBuilder strassenname = new StringBuilder();
		strassenname.append(statistikGesamt.schule.strassenname.trim());
		strassenname.append(" ");
		strassenname.append(statistikGesamt.schule.hausnummer.trim());
		strassenname.append(" ");
		strassenname.append(statistikGesamt.schule.hausnummerZusatz.trim());
		statistikExport.schuleStatistikExport.strassenname = strassenname.toString();
		statistikExport.schuleStatistikExport.plz = statistikGesamt.schule.plz;
		statistikExport.schuleStatistikExport.ort = statistikGesamt.schule.ort;
		statistikExport.schuleStatistikExport.telefon = statistikGesamt.schule.telefon;
		statistikExport.schuleStatistikExport.fax = statistikGesamt.schule.fax;
		statistikExport.schuleStatistikExport.email = statistikGesamt.schule.email;
		statistikExport.schuleStatistikExport.webAdresse = statistikGesamt.schule.webAdresse;
		statistikExport.schuleStatistikExport.zeitmodel = statistikGesamt.schule.dauerUnterrichtseinheit != 45 ? 1 : 45;
		//TODO:  Ab hier prüfen umformung notwendig!
		statistikExport.schuleStatistikExport.gebundenerGanztag = statistikGesamt.schule.idGebundenerGanztag; // ?Katalog
		statistikExport.schuleStatistikExport.istOffenerGanztag = statistikGesamt.schule.istOffenerGanztag;
		statistikExport.schuleStatistikExport.formOffenerGanztag =
				FormOffenerGanztag.data().getNameByIDOrNull(statistikGesamt.schule.idFormOffenerGanztag);
		statistikExport.schuleStatistikExport.istJva = statistikGesamt.schule.istJva;
		//statistikExport.schuleStatistikExport.bilingualerUnterricht = statistikGesamt.schule.idBilingualerUnterricht; // ?Katalog
		statistikExport.schuleStatistikExport.hatRealschuleHauptschulbildungsgang = statistikGesamt.schule.hatRealschuleHauptschulbildungsgang;
		statistikExport.schuleStatistikExport.hatInternationaleKontakte = statistikGesamt.schule.hatInternationaleKontakte;
		statistikExport.schuleStatistikExport.hatKonfessionelleKooperation = statistikGesamt.schule.hatKonfessionelleKooperation;
		statistikExport.schuleStatistikExport.talentschule = (int) statistikGesamt.schule.idTalentschule; // ?2027 Ausgelaufen-noch erhalten?
		statistikExport.schuleStatistikExport.reformpaedagogik = Reformpaedagogik.data().getNameByIDOrNull(statistikGesamt.schule.idReformpaedagogik);

		statistikGesamt.schule.adressen.stream().forEach(this::erstellenSchuleAdressenStatistikExport);
	}

	/**
	 * Gibt das Feld {@link #fehlermeldungen} zurueck.
	 *
	 * @return das Feld {@link #fehlermeldungen}
	 */
	public LinkedList<String> getFehlermeldungen() {
		return this.fehlermeldungen;
	}

}
