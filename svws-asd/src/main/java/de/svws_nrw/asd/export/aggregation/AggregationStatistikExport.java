package de.svws_nrw.asd.export.aggregation;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuleAdressenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.export.data.KlassenAltersstrukturStatistikExport;
import de.svws_nrw.asd.export.data.KlassenNationalitaetenStatistikExport;
import de.svws_nrw.asd.export.data.KlassenStatistikExport;
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

	/**
	 * Ein String mit einem Leerzeichen.
	 */
	public static final String EIN_LEERZEICHEN = " ";

	/**
	 * Ein String mit zwei Leerzeichen.
	 */
	public static final String ZWEI_LEERZEICHEN = "  ";
	/**
	 * Ein String mit drei Leerzeichen.
	 */
	public static final String DREI_LEERZEICHEN = "   ";

	/**
	 * Eine Liste der Fehlermeldungen zu den aufgetretenen Fehlern.
	 */
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
	 * Zuordnung der ID einer Klasse zum zugehörigen {@link KlassenStatistikGesamt}-Objekt.
	 */
	private Map<Long, KlassenStatistikGesamt> klassenStatistikGesamt = new HashMap<>();

	/**
	 * Zuordnung der ID eines Lehrers zum zugehörigen {@link LehrerStatistikGesamt}-Objekt.
	 */
	private Map<Long, LehrerStatistikGesamt> lehrerStatistikGesamt = new HashMap<>();

	/**
	* Das aktuelle Schuljahr in vierstelliger Form.
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
	 * Gibt zurück, ob es sich um eine Schulform handelt, bei der der Förderschwerpunkt zum Erstellen der SatzkennungReligion
	 * erforderlich ist.
	 *
	 *@param schulform - {@link Schulform}
	 * @return true/false
	 */
	public static boolean istFoederschwerpunktErforderlich(final Schulform schulform) {
		return switch (schulform) {
			case BK, SB, S, KS, SR, SG -> true;
			default -> false;
		};
	}

	/**
	 * Gibt zurück, ob es sich um eine Schulform handelt, bei der der Jahrgang zum Erstellen der SatzkennungReligion
	 * erforderlich ist.
	 *
	 *@param schulform - {@link Schulform}
	 * @return true/false
	 */
	public static boolean istJahrgangErforderlich(final Schulform schulform) {
		return switch (schulform) {
			case G, H, V, R, PS, SK, GE, GY -> true;
			default -> false;
		};
	}

	/**
	 * Auffüllen des Klassenkürzel auf sechs Stellen mit Leerzeichen.
	 *
	 * @param klassenkuerzel
	 * @return klassenkuerzel sechsstellig
	 */
	private static String auffuellenKlassenkuerzelStellengerecht(final String klassenkuerzel) {
		String klassenkuerzelFormatiert = klassenkuerzel;

		while (klassenkuerzelFormatiert.toCharArray().length < 6) {
			klassenkuerzelFormatiert = klassenkuerzelFormatiert.concat(EIN_LEERZEICHEN);
		}

		return klassenkuerzelFormatiert;
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
		StringBuilder satzkennungReli = new StringBuilder();
		satzkennungReli.append(religionExport.jahrgang);
		satzkennungReli.append(religionExport.schulgliederung);
		satzkennungReli.append(religionExport.bildungsbereich);
		satzkennungReli.append(religionExport.foerderschwerpunkt);

		return satzkennungReli.toString();
	}

	private static void erstellenKlassenNationalitaetenStatistikExport(final List<SchuelerStatistikGesamt> schuelerStatistikGesamt,
			final KlassenStatistikExport klassenStatistikExport) {
		Map<String, List<SchuelerStatistikGesamt>> map = schuelerStatistikGesamt.stream().collect(Collectors.groupingBy(s -> s.staatsangehoerigkeitID));

		map.entrySet().stream().filter(f -> !f.getKey().equalsIgnoreCase("deu")).forEach(t -> {
			KlassenNationalitaetenStatistikExport klassenNationalitaetenStatistikExport = new KlassenNationalitaetenStatistikExport();
			klassenNationalitaetenStatistikExport.nationalitaet = t.getKey();
			klassenNationalitaetenStatistikExport.insgesamtZusammen = t.getValue().size();
			klassenNationalitaetenStatistikExport.insgesamtWeiblich = (int) t.getValue().stream().filter(f -> Geschlecht.W.id == f.geschlecht).count();
			klassenStatistikExport.klassenNationalitaetenStatistikExport.add(klassenNationalitaetenStatistikExport);
		});

	}

	/**
	 * Hier werden für den Export Lehrämter, Fachrichtungen und Lehrbefähigungen erstellt
	 * @param lehramt
	 * @param lehrerExport
	 */
	private static void erstellenLehraemterStatistikExport(final LehrerLehramtEintrag lehramt, final LehrerStatistikExport lehrerExport) {
		LehrerLehraemterStatistikExport lehramtExport = new LehrerLehraemterStatistikExport();
		lehramtExport.lehramt = LehrerLehramt.data().getSchluesselByIDOrNull(lehramt.idKatalogLehramt);
		lehrerExport.lehraemterStatistikExport.add(lehramtExport);


		lehramt.fachrichtungen.stream().forEach(fachrichtung -> erstellenLehrerFachrichtungenStatistikExport(fachrichtung, lehrerExport));
		lehramt.lehrbefaehigungen.stream().forEach(lehrbefaehigung -> erstellenLehrerLehrbefaehigungenStatistikExport(lehrbefaehigung, lehrerExport));
	}

	private static void erstellenLehrerAnrechungenStatistikExport(final LehrerPersonalabschnittsdatenAnrechnungsstunden anrechnung,
			final LehrerStatistikExport lehrerExport) {
		LehrerAnrechungenStatistikExport anrechnungExport = new LehrerAnrechungenStatistikExport();
		anrechnungExport.grund = LehrerAnrechnungsgrund.data().getSchluesselByIDOrNull(anrechnung.idGrund);
		anrechnungExport.anrechungsstunden = anrechnung.anzahl;

		lehrerExport.anrechungenStatistikExport.add(anrechnungExport);
	}

	private static void erstellenLehrerFachrichtungenStatistikExport(final LehrerFachrichtungEintrag fachrichtung, final LehrerStatistikExport lehrerExport) {
		LehrerFachrichtungenStatistikExport fachrichtungExport = new LehrerFachrichtungenStatistikExport();
		fachrichtungExport.fachrichtung = LehrerFachrichtung.data().getSchluesselByIDOrNull(fachrichtung.idFachrichtung);
		fachrichtungExport.qualifikation = LehrerFachrichtungAnerkennung.data().getSchluesselByIDOrNull(fachrichtung.idAnerkennungsgrund);

		lehrerExport.fachrichtungenStatistikExport.add(fachrichtungExport);

	}

	private static void erstellenLehrerLehrbefaehigungenStatistikExport(final LehrerLehrbefaehigungEintrag lehrbefaehigung,
			final LehrerStatistikExport lehrerExport) {
		LehrerLehrbefaehigungenStatistikExport lehrbefaehigungExport = new LehrerLehrbefaehigungenStatistikExport();
		lehrbefaehigungExport.lehrbefaehigung = LehrerLehrbefaehigung.data().getSchluesselByIDOrNull(lehrbefaehigung.idLehrbefaehigung);
		lehrbefaehigungExport.qualifikation = LehrerLehrbefaehigungAnerkennung.data().getSchluesselByIDOrNull(lehrbefaehigung.idAnerkennungsgrund);

		lehrerExport.lehrbefaehigungenStatistikExport.add(lehrbefaehigungExport);
	}

	private static void erstellenLehrerMehrleistungenStatistikExport(final LehrerPersonalabschnittsdatenAnrechnungsstunden mehrleistung,
			final LehrerStatistikExport lehrerExport) {
		LehrerMehrleistungenStatistikExport mehrleistungExport = new LehrerMehrleistungenStatistikExport();
		mehrleistungExport.grund = LehrerMehrleistungsarten.data().getSchluesselByIDOrNull(mehrleistung.idGrund);
		mehrleistungExport.mehrleistungsstunden = mehrleistung.anzahl;

		lehrerExport.mehrleistungenStatistikExport.add(mehrleistungExport);
	}

	private static void erstellenLehrerMinderleistungenStatistikExport(final LehrerPersonalabschnittsdatenAnrechnungsstunden minderleistung,
			final LehrerStatistikExport lehrerExport) {
		LehrerMinderleistungenStatistikExport minderleistungExport = new LehrerMinderleistungenStatistikExport();
		minderleistungExport.grund = LehrerMinderleistungsarten.data().getSchluesselByIDOrNull(minderleistung.idGrund);
		minderleistungExport.minderleistungsstunden = minderleistung.anzahl;

		lehrerExport.minderleistungenStatistikExport.add(minderleistungExport);
	}

	/**
	 * Gibt das Feld {@link #fehlermeldungen} zurueck.
	 *
	 * @return das Feld {@link #fehlermeldungen}
	 */
	public List<String> getFehlermeldungen() {
		return this.fehlermeldungen;
	}

	/**
	 * Gibt das Feld {@link #statistikExport} zurueck.
	 *
	 * @return das Feld {@link #statistikExport}
	 */
	public StatistikExport getStatistikExport() {
		return this.statistikExport;
	}

	/**
	 * Führt die Aggregation der {@link StatistikGesamt}-Daten in das {@link StatistikExport}-Datenobjekt aus. <br>
	 * Fehlermeldungen zu gegebenenfalls aufgetretenen Fehlern können über {@link #getFehlermeldungen()} hinterher abgerufen werden.
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
		// Klassendaten
		erstellenKlassenStatistikExport();

		return true;
	}


	/**
	 * Hier werden die Schüler-Summen zum Religionsunterricht aufsummiert und in das übergebene
	 * {@link ReligionszugehoerigkeitenStatistikExport}-Objekt geschrieben.
	 *
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
					if ((schueler.religionabmeldung != null)
							&& DateManager.from(schueler.religionabmeldung).compareTo(DateManager.fromValues(aktuellesSchuljahr, 10, 15)) <= 0
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
					if ((schueler.religionabmeldung != null)
							&& DateManager.from(schueler.religionabmeldung).compareTo(DateManager.fromValues(aktuellesSchuljahr, 10, 15)) <= 0
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

	private String bauenBildungsbereich(final TeilKlassenKey klassenKey) {
		String bildungsbereich = "";
		if (Schulform.FW.equals(this.schulform) || Schulform.WF.equals(this.schulform) || Schulform.HI.equals(this.schulform)
				|| Schulform.SG.equals(this.schulform)) {
			if (!klassenKey.foerderschwerp.isBlank()) {
				bildungsbereich = "S";
			} else {
				bildungsbereich = "A";
			}

			if (Schulform.HI.equals(this.schulform)) {
				if ("K02".equals(klassenKey.gliederung)) {
					bildungsbereich = "K";
				}
				if (!"K02".equals(klassenKey.gliederung) && !klassenKey.gliederung.isBlank()) {
					bildungsbereich = "B";
				}
			}
		}

		return bildungsbereich;
	}

	private List<Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>>> bauenTeilklassenSchueler() {
//		statistikGesamt.klassen.stream()
//		.forEach(e -> this.klassenStatistikGesamt
//				.computeIfAbsent(Jahrgaenge.data().getSchluesselByIDOrNull(this.jahrgangIds.get(e.idJahrgang)) + e.parallelitaet,
//						k -> new ArrayList<>())
//				.add(e));
		klassenStatistikGesamt = statistikGesamt.klassen.stream().collect(Collectors.toMap(e -> e.id, e -> e));


		lehrerStatistikGesamt = statistikGesamt.lehrer.stream().collect(Collectors.toMap(e -> e.id, e -> e));

		//Gruppieren
		Map<TeilKlassenKey, List<SchuelerStatistikGesamt>> gruppiert =
				statistikGesamt.schueler.stream()
						// .filter(SchuelerStatistikGesamt::isAktiv) // nur aktive ?
						.collect(Collectors.groupingBy(
								e -> new TeilKlassenKey(e, klassenStatistikGesamt, lehrerStatistikGesamt, statistikGesamt.schule.idSchuljahresabschnitt,
										this.jahrgangIds)));

		//Sortieren
		Comparator<Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>>> comparator =
				Comparator.comparing((Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>> e) -> e.getKey().klassenKuerzel)
						.thenComparing(e -> e.getKey().gliederung)
						.thenComparing(e -> e.getKey().klassenart)
						.thenComparing(e -> e.getKey().orgForm)
						.thenComparing(e -> e.getKey().aktJahrgang)
						.thenComparing(e -> e.getKey().foerderschwerp)
						.thenComparing(e -> e.getKey().schwerstbeh)
						.thenComparing(e -> e.getKey().labk)
						.thenComparing(e -> e.getKey().reformpdg)
						.thenComparing(e -> e.getKey().foerderschwerp2)
						.thenComparing(e -> e.getKey().adressmerkmal);

		List<Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>>> sortiert =
				gruppiert.entrySet().stream()
						.sorted(comparator)
						.toList();


		//Teilklassen erstellen
		Map<String, Long> anzahlProKlasse =
				sortiert.stream()
						.collect(Collectors.groupingBy(
								e -> e.getKey().klassenKuerzel.toUpperCase(),
								Collectors.counting()
						));
		Map<String, Integer> counterProKlasse = new HashMap<>();
		List<Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>>> sortiertTeilklassen = new ArrayList<>();
		for (Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>> entry : sortiert) {

			TeilKlassenKey key = entry.getKey();
			String klasse = key.klassenKuerzel.toUpperCase();

			long anzahl = anzahlProKlasse.getOrDefault(klasse, 0L);

			String teilklasse;

			if (anzahl == 1) {
				// nur eine Klasse → keine Nummer
				teilklasse = auffuellenKlassenkuerzelStellengerecht(klasse);
			} else {
				// mehrere Teilklassen → nummerieren
				int nummer = counterProKlasse.getOrDefault(klasse, 0) + 1;
				counterProKlasse.put(klasse, nummer);

				teilklasse = auffuellenKlassenkuerzelStellengerecht(klasse);
				teilklasse = teilklasse.substring(0, teilklasse.length() - 2) + String.format("%02d", nummer);

			}
			Map.Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>> entryTeilklasse = new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue());
			entryTeilklasse.getKey().klassenKuerzel = teilklasse;
			sortiertTeilklassen.add(entryTeilklasse);
			System.out.println(teilklasse + " -> " + entry.getValue().size());
		}

		System.out.println("sortiert.size(): " + sortiertTeilklassen.size());
		sortiertTeilklassen.stream().forEach(e -> {
			System.out.println(e.getKey());
			//System.out.println(e.getValue());
			System.out.println("-----------------------------------------");
		});

		return sortiertTeilklassen;
	}

	private void erstellenKlassenAltersstrukturStatistikExport(final List<SchuelerStatistikGesamt> schuelerStatistikGesamt,
			final KlassenStatistikExport klassenStatistikExport) {
		Map<AltersstrukturKey, List<SchuelerStatistikGesamt>> map = schuelerStatistikGesamt.stream()
				.collect(Collectors
						.groupingBy(s -> {
							try {
								return new AltersstrukturKey(s.staatsangehoerigkeitID, String.valueOf(DateManager.from(s.geburtsdatum).getJahr()));
							} catch (InvalidDateException e) {
								fehlermeldungen.add("Folgendes Geburtsdatum konnte nicht geparst werden: " + s.geburtsdatum);
								return new AltersstrukturKey(s.staatsangehoerigkeitID, "");
							}
						}));

		map.entrySet().stream().forEach(t -> {
			KlassenAltersstrukturStatistikExport klassenAltersstrukturStatistikExport = new KlassenAltersstrukturStatistikExport();
			klassenAltersstrukturStatistikExport.nationalitaet = t.getKey().nationalitaet;
			if (klassenAltersstrukturStatistikExport.nationalitaet.equalsIgnoreCase("deu")) {
				klassenAltersstrukturStatistikExport.nationalitaet = "";
			}
			klassenAltersstrukturStatistikExport.geburtsjahr = t.getKey().geburtsjahr;
			klassenAltersstrukturStatistikExport.schuelerInsgesamt = t.getValue().size();
			klassenAltersstrukturStatistikExport.schuelerWeiblich = (int) t.getValue().stream().filter(f -> Geschlecht.W.id == f.geschlecht).count();
			klassenStatistikExport.klassenAltersstrukturStatistikExport.add(klassenAltersstrukturStatistikExport);
		});

	}

	private void erstellenKlassenStatistikExport() {

		List<Entry<TeilKlassenKey, List<SchuelerStatistikGesamt>>> teilklassenSchueler = bauenTeilklassenSchueler();


		teilklassenSchueler.forEach(e -> {
			KlassenStatistikExport klassenStatistikExport = new KlassenStatistikExport();
			SchuelerLernabschnittStatistikGesamt lernabschnitt = new SchuelerLernabschnittStatistikGesamt();
			Optional<SchuelerLernabschnittStatistikGesamt> optional =
					e.getValue().getFirst().lernabschnitte.stream().filter(k -> k.idSchuljahresabschnitt == statistikGesamt.schule.idSchuljahresabschnitt)
							.findFirst();

			if (optional.isPresent()) {
				lernabschnitt = optional.get();
			}
			klassenStatistikExport.jahrgang = e.getKey().aktJahrgang;
			if (this.schulform.istAllgemeinbildend()) {
				String parallelitaet = klassenStatistikGesamt.get(lernabschnitt.idKlasse).parallelitaet;
				klassenStatistikExport.bildungsgangkennzeichen =
						parallelitaet == null ? EIN_LEERZEICHEN : parallelitaet.trim();

				klassenStatistikExport.parallelitaet2 = EIN_LEERZEICHEN;

			}
			klassenStatistikExport.jahrgangTeilklasse = e.getKey().aktJahrgang;
			klassenStatistikExport.adresskennzeichen = e.getKey().adressmerkmal;
			klassenStatistikExport.bildungsbereich = bauenBildungsbereich(e.getKey());
			//TODO hier mus noch ein Json-katalog ink. Cortype vorgesehen weredn, erstazweise wird die id verwendet
			klassenStatistikExport.fachklasse = lernabschnitt.idFachklasse == null ? null : String.valueOf(lernabschnitt.idFachklasse);
			klassenStatistikExport.foerderschwerpunkt1 = e.getKey().foerderschwerp;
			klassenStatistikExport.foerderschwerpunkt2 = e.getKey().foerderschwerp2;
			klassenStatistikExport.hatSchwerbehinderungsNachweis = e.getKey().schwerstbeh;
			klassenStatistikExport.jvaKlasse = e.getValue().getFirst().istJvaSchueler ? "1" : "0";
			klassenStatistikExport.klassenart = e.getKey().klassenart;
			klassenStatistikExport.kuerzelKlassenlehrer = e.getKey().labk;
			klassenStatistikExport.organisationsform = e.getKey().orgForm;
			klassenStatistikExport.reformpaedagogik = e.getKey().reformpdg;
			klassenStatistikExport.schuelerAuslaendischWeiblich = (int) e.getValue().stream().filter(w ->
			//TODO staatsangehoerigkeitID auf idStaatsangehoerigkeit umstellen
			Geschlecht.W.id == w.geschlecht && !w.staatsangehoerigkeitID.equalsIgnoreCase("deu")
			).count();
			klassenStatistikExport.schuelerAuslaendischZusammen = (int) e.getValue().stream().filter(w ->
			//TODO staatsangehoerigkeitID auf idStaatsangehoerigkeit umstellen
			!w.staatsangehoerigkeitID.equalsIgnoreCase("deu")
			).count();
			klassenStatistikExport.schuelerInsgesamt = e.getValue().size();
			klassenStatistikExport.schuelerWeiblich = (int) e.getValue().stream().filter(w -> Geschlecht.W.id == w.geschlecht
			).count();
			klassenStatistikExport.schulgliederung = e.getKey().gliederung;
			//TODO muss geklärt werden, ersatzweise wird null verwendet
			klassenStatistikExport.schulinterneBezeichnung = null;
			klassenStatistikExport.teilklasse = e.getKey().klassenKuerzel.substring(4);
			//TODO muss geklärt werden, ersatzweise wird false verwendet
			klassenStatistikExport.verkuerzungHalbjaehrlich = false;
			// **K82 - Herkunft der Schüler**
			// TODO KlassenHerkunftStatistikExport - muss noch vorbereitet werden
			// **K83 - Ausländer**
			erstellenKlassenNationalitaetenStatistikExport(e.getValue(), klassenStatistikExport);
			// **K85 - Ausbildungsort**
			//TODO KlassenAusbildungsortsartStatistikExport - BK-Thema
			// **K87 - Betreuung**
			//TODO KlassenBetreuungStatistikExport - muss noch vorbereitet werden
			// **X94 - Regionale Herkunft der Schüler (Wohnort)
			//TODO KlassenWohnorteStatistikExport - Gemeindeschlüssel muss noch geklärt werden
			// **X95 - Altersstruktur der Schüler**
			erstellenKlassenAltersstrukturStatistikExport(e.getValue(), klassenStatistikExport);


			// **X96 - Regionale Herkunft der Schüler (Ausbildungsort)
			//TODO KlassenAusbildungsorteStatistikExport
			// **X98 - Zuwanderungsgeschichte
			//TODO KlassenAusbildungsorteStatistikExport

			statistikExport.klassenStatistikExport.add(klassenStatistikExport);
		});




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
		lehrerExport.zuErteilenderUnterricht = (lehrerExport.pflichtstundensoll - lehrer.anrechnungen.stream().mapToDouble(e -> e.anzahl).sum()
				+ lehrer.mehrleistung.stream().mapToDouble(e -> e.anzahl).sum() - lehrer.minderleistung.stream().mapToDouble(e -> e.anzahl).sum());
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

		if (jahrgang == null && istJahrgangErforderlich(schulform)) {
			fehlermeldungen.add("Zu folgender idJahrgang konnte kein Jahrgang gefunden werden: " + lernabschnitt.idJahrgang);
			return;
		}
		// Ermitteln Förderschwerpunkt
		String foerderschwerpunkt = Foerderschwerpunkt.data().getSchluesselByIDOrNull(foerderschwerpunktIds.get(lernabschnitt.idFoerderschwerpunkt1));

		if (foerderschwerpunkt == null && istFoederschwerpunktErforderlich(schulform)) {
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

		final String satzkennungReligion = bauenSatzkennungReligion(religionExport);

		if (religionen.containsKey(satzkennungReligion)) {
			religionExport = religionen.get(satzkennungReligion);
		} else {
			religionen.put(satzkennungReligion, religionExport);
		}

		aufsummierenReligionExport(religionExport, schueler);
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
		//TODO: Wo ist der zugehörige Katalog? Einbinden.
		statistikExport.schuleStatistikExport.gebundenerGanztag = statistikGesamt.schule.idGebundenerGanztag; // ?Katalog
		statistikExport.schuleStatistikExport.istOffenerGanztag = statistikGesamt.schule.istOffenerGanztag;
		statistikExport.schuleStatistikExport.formOffenerGanztag =
				FormOffenerGanztag.data().getNameByIDOrNull(statistikGesamt.schule.idFormOffenerGanztag);
		statistikExport.schuleStatistikExport.istJva = statistikGesamt.schule.istJva;
		//TODO: Wo ist der zugehörige Katalog? Einbinden.
//		statistikExport.schuleStatistikExport.bilingualerUnterricht = statistikGesamt.schule.idBilingualerUnterricht; // ?Katalog
		statistikExport.schuleStatistikExport.hatRealschuleHauptschulbildungsgang = statistikGesamt.schule.hatRealschuleHauptschulbildungsgang;
		statistikExport.schuleStatistikExport.hatInternationaleKontakte = statistikGesamt.schule.hatInternationaleKontakte;
		statistikExport.schuleStatistikExport.hatKonfessionelleKooperation = statistikGesamt.schule.hatKonfessionelleKooperation;
		//TODO: 2027 Ausgelaufen-noch erhalten?
		statistikExport.schuleStatistikExport.talentschule = (int) statistikGesamt.schule.idTalentschule; // ?2027 Ausgelaufen-noch erhalten?
		statistikExport.schuleStatistikExport.reformpaedagogik = Reformpaedagogik.data().getNameByIDOrNull(statistikGesamt.schule.idReformpaedagogik);

		statistikGesamt.schule.adressen.stream().forEach(this::erstellenSchuleAdressenStatistikExport);
	}


}
