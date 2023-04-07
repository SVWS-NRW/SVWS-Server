package de.svws_nrw.data.enm;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.enm.ENMDaten;
import de.svws_nrw.core.data.enm.ENMFach;
import de.svws_nrw.core.data.enm.ENMFloskel;
import de.svws_nrw.core.data.enm.ENMFloskelgruppe;
import de.svws_nrw.core.data.enm.ENMJahrgang;
import de.svws_nrw.core.data.enm.ENMKlasse;
import de.svws_nrw.core.data.enm.ENMLerngruppe;
import de.svws_nrw.core.data.enm.ENMSchueler;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.kurse.ZulaessigeKursart;
import de.svws_nrw.core.utils.enm.ENMDatenManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFloskelgruppen;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFloskeln;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.db.utils.dto.enm.DTOENMLehrerSchuelerAbschnittsdaten;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link ENMDaten}.
 */
public final class DataENMDaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link ENMDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataENMDaten(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		final ENMDaten daten = getDaten(null);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Gibt die ENM-Daten als mit GZIP komprimiertes JSON-Objekt zurück.
	 *
	 * @return das GZIP-komprimierte JSON-Objekt.
	 */
	public Response getAllGZip() {
		final ENMDaten daten = getDaten(null);
		return JSONMapper.gzipFileResponseFromObject(daten, "enm.json.gz");
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
		final ENMDaten daten = getDaten(id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Ermittelt die ENM-Daten zu dem Lehrer mit der angegebenen ID.
	 * Ist die ID null so werden die ENM-Daten für alle Lehrer des aktuellen
	 * Schuljahresabschnitts generiert.
	 *
	 * @param id   die ID des Lehrers oder null
	 *
	 * @return die ENMDaten
	 */
	private ENMDaten getDaten(final Long id) {
		// Lese die Daten aus der Datenbank ein
		final DTOEigeneSchule schule = getSchule();
    	final DTOSchuljahresabschnitte abschnitt = getSchuljahresabschnitt(schule);
    	final Map<Long, DTOLehrer> mapLehrer = getLehrerListe();
		final DTOLehrer dtoLehrer = (id == null) ? null : mapLehrer.get(id);   // Ermittle den Lehrer nur, falls ENM-Daten für einen speziellen Lehrer bestimt werden.
    	if ((id != null) && (dtoLehrer == null))
    		throw OperationError.NOT_FOUND.exception();
    	final Map<Long, DTOSchueler> mapSchueler = getSchuelerListe(schule);
    	final Map<Long, DTOFach> mapFaecher = getFaecherListe();
    	final Map<Long, DTOJahrgang> mapJahrgaenge = getJahrgangsListe();
    	final Map<String, DTOKlassen> mapKlassen = getKlassenListe(schule);
    	final Map<Long, List<DTOKlassenLeitung>> mapKlassenLeitung = getKlassenleitungen(mapKlassen);
    	final Map<Long, DTOKurs> mapKurse = getKurse(schule);

    	// Erstelle einen ENM-Daten-Manager und füge ggf. den Lehrer hinzu für welchen die ENM-Daten erzeugt werden
    	final ENMDatenManager manager = new ENMDatenManager(id);
    	if (dtoLehrer != null)
    		manager.addLehrer(manager.daten.lehrerID, dtoLehrer.Kuerzel, dtoLehrer.Nachname, dtoLehrer.Vorname, dtoLehrer.Geschlecht, dtoLehrer.eMailDienstlich);
		initManager(manager, schule, abschnitt);

   	    // Aggregiert aus den Schülerabschnittsdaten und -leistungsdaten die einzelnen Informationen für das ENM.
    	// Dabei wird unter anderem auch ermittelt, welche Kataloginformationen (Klassen, Lehrer, Jahrgänge)
   	    // bei den Daten für das ENM einzutragen sind.
    	final List<DTOENMLehrerSchuelerAbschnittsdaten> schuelerabschnitte = dtoLehrer == null
    			? DTOENMLehrerSchuelerAbschnittsdaten.queryAll(conn, schule.Schuljahresabschnitts_ID)
    			: DTOENMLehrerSchuelerAbschnittsdaten.query(conn, schule.Schuljahresabschnitts_ID, dtoLehrer.Kuerzel);
    	for (final DTOENMLehrerSchuelerAbschnittsdaten schuelerabschnitt : schuelerabschnitte) {
    		final DTOKlassen dtoKlasse = mapKlassen.get(schuelerabschnitt.klasse);
			if (dtoKlasse == null) {
				// TODO DB-Error -> should not happen but must be handled
				throw new NullPointerException();
			}
    		ENMKlasse enmKlasse = manager.getKlasse(dtoKlasse.ID);
			if (enmKlasse == null) {
				manager.addKlasse(dtoKlasse.ID, dtoKlasse.ASDKlasse, dtoKlasse.Klasse, dtoKlasse.Jahrgang_ID, dtoKlasse.Sortierung);
				enmKlasse = manager.getKlasse(dtoKlasse.ID);
				final List<DTOKlassenLeitung> klassenleitungen = mapKlassenLeitung.get(dtoKlasse.ID);
				if (klassenleitungen != null) {
					for (final DTOKlassenLeitung kl : klassenleitungen) {
						if (manager.getLehrer(kl.Lehrer_ID) == null) {
							final DTOLehrer dtoKlassenlehrer = mapLehrer.get(kl.Lehrer_ID);
							if (dtoKlassenlehrer != null) {
								manager.addLehrer(dtoKlassenlehrer.ID, dtoKlassenlehrer.Kuerzel,
										          dtoKlassenlehrer.Nachname, dtoKlassenlehrer.Vorname, dtoKlassenlehrer.Geschlecht, dtoKlassenlehrer.eMailDienstlich);
							}
						}
						enmKlasse.klassenlehrer.add(kl.Lehrer_ID);
					}
				}
			}

    		final ZulaessigeKursart kursart = (schuelerabschnitt.kursID == null) ? null : ZulaessigeKursart.getByASDKursart(schuelerabschnitt.kursart);

    		ENMSchueler enmSchueler = manager.getSchueler(schuelerabschnitt.schuelerID);
    		if (enmSchueler == null) {
    			final var dtoSchueler = mapSchueler.get(schuelerabschnitt.schuelerID);
    			ENMJahrgang enmJahrgang = manager.getJahrgang(schuelerabschnitt.jahrgangID);
    			if (enmJahrgang == null) {
    				final DTOJahrgang dtoJahrgang = mapJahrgaenge.get(schuelerabschnitt.jahrgangID);
    				if (dtoJahrgang == null) {
    					// TODO DB-Error -> should not happen but must be handled
    					throw new NullPointerException();
    				}
    				manager.addJahrgang(dtoJahrgang.ID, dtoJahrgang.ASDJahrgang, dtoJahrgang.InternKrz,
    						dtoJahrgang.ASDBezeichnung, dtoJahrgang.Sekundarstufe, dtoJahrgang.Sortierung);
    				enmJahrgang = manager.getJahrgang(schuelerabschnitt.jahrgangID);
    			}

    			manager.addSchueler(dtoSchueler.ID, schuelerabschnitt.jahrgangID, dtoKlasse.ID, dtoSchueler.Nachname, dtoSchueler.Vorname,
    					            dtoSchueler.Geschlecht, schuelerabschnitt.BilingualerZweig, schuelerabschnitt.ZieldifferentesLernen,
    								dtoSchueler.Lernstandsbericht);  // Deutsch als Fremdsprache liegt vor, wenn für den Schüler Lernstandsberichte geschrieben werden...
    			enmSchueler = manager.getSchueler(schuelerabschnitt.schuelerID);
    			enmSchueler.lernabschnitt.id = schuelerabschnitt.abschnittID;
    			enmSchueler.lernabschnitt.fehlstundenGesamt = schuelerabschnitt.fehlstundenSummeGesamt;
    			enmSchueler.lernabschnitt.tsFehlstundenGesamt = schuelerabschnitt.tsFehlstundenSummeGesamt;
    			enmSchueler.lernabschnitt.fehlstundenGesamtUnentschuldigt = schuelerabschnitt.fehlstundenSummeUnentschuldigt;
    			enmSchueler.lernabschnitt.tsFehlstundenGesamtUnentschuldigt = schuelerabschnitt.tsFehlstundenSummeUnentschuldigt;
    			enmSchueler.lernabschnitt.pruefungsordnung = schuelerabschnitt.pruefungsordnung;
    			enmSchueler.lernabschnitt.lernbereich1note = schuelerabschnitt.lernbereich1note == null ? null : schuelerabschnitt.lernbereich1note.kuerzel;
    			enmSchueler.lernabschnitt.lernbereich2note = schuelerabschnitt.lernbereich2note == null ? null : schuelerabschnitt.lernbereich2note.kuerzel;
    			enmSchueler.lernabschnitt.foerderschwerpunkt1 = schuelerabschnitt.foerderschwerpunkt1Kuerzel;
    			enmSchueler.lernabschnitt.foerderschwerpunkt2 = schuelerabschnitt.foerderschwerpunkt2Kuerzel;
    			enmSchueler.bemerkungen.ASV = schuelerabschnitt.ASV;
    			enmSchueler.bemerkungen.tsASV = schuelerabschnitt.tsASV;
    			enmSchueler.bemerkungen.AUE = schuelerabschnitt.AUE;
    			enmSchueler.bemerkungen.tsAUE = schuelerabschnitt.tsAUE;
    			enmSchueler.bemerkungen.ZB = schuelerabschnitt.zeugnisBemerkungen;
    			enmSchueler.bemerkungen.tsZB = schuelerabschnitt.tsZeugnisBemerkungen;
    			enmSchueler.bemerkungen.LELS = schuelerabschnitt.LELS;
    			// TODO Schulform-Empfehlung enmSchueler.bemerkungen.schulformEmpf = ...
    			enmSchueler.bemerkungen.individuelleVersetzungsbemerkungen = schuelerabschnitt.bemerkungVersetzung;
    			enmSchueler.bemerkungen.tsIndividuelleVersetzungsbemerkungen = schuelerabschnitt.tsBemerkungVersetzung;
    			enmSchueler.bemerkungen.foerderbemerkungen = schuelerabschnitt.bemerkungFSP;
    		}

    		// Hier wird die temporäre LerngruppenID erstellt, mit der in der Klasse ENMDaten gearbeitet wird.
    		// Es wird eine eindeutige ID benötigt, die Kurs- und Klassenübergreifend diese Lerngruppe identifiziert.
    		final String strLerngruppenID = (schuelerabschnitt.kursID == null)
    			? "Klasse:" + schuelerabschnitt.klasse + "/" + schuelerabschnitt.fachID
    			: "Kurs:" + schuelerabschnitt.kursID;
    		ENMLerngruppe lerngruppe = manager.getLerngruppe(strLerngruppenID);
    		if (lerngruppe == null) {
				final DTOFach fach = mapFaecher.get(schuelerabschnitt.fachID);
				final String kursartAllg = (kursart == null) ? null : (kursart.daten.kuerzelAllg == null) || "".equals(kursart.daten.kuerzelAllg)
						? kursart.daten.kuerzel : kursart.daten.kuerzelAllg;
				// Fach zu ENMDaten hinzufügen ?
				if (manager.getFach(fach.ID) == null)
					manager.addFach(fach.ID, fach.StatistikFach.daten.kuerzelASD, fach.Kuerzel, fach.SortierungAllg, fach.IstFremdsprache);
				// Unterscheidung zwischen den beiden Lerngruppen-Typen...
    			if (schuelerabschnitt.kursID == null) {  // es ist eine Klasse
    				manager.addLerngruppe(strLerngruppenID, dtoKlasse.ID, schuelerabschnitt.fachID, null,
        					            fach.Kuerzel, kursartAllg, fach.Unterichtssprache,
        					            schuelerabschnitt.wochenstunden == null ? 0 : schuelerabschnitt.wochenstunden);
    			} else {  // es ist ein Kurs
    				final DTOKurs kurs = mapKurse.get(schuelerabschnitt.kursID);
    				manager.addLerngruppe(strLerngruppenID, schuelerabschnitt.kursID, schuelerabschnitt.fachID,
        					kursart == null ? -1 : Integer.parseInt(kursart.daten.nummer), kurs.KurzBez, kursartAllg,
        					fach.Unterichtssprache, kurs.WochenStd);
    			}
    			lerngruppe = manager.getLerngruppe(strLerngruppenID);
    			lerngruppe.lehrerID.add(schuelerabschnitt.lehrerID);
				if (manager.getLehrer(schuelerabschnitt.lehrerID) == null) {
					final DTOLehrer dtoFachlehrer = mapLehrer.get(schuelerabschnitt.lehrerID);
					if (dtoFachlehrer != null) {
						manager.addLehrer(dtoFachlehrer.ID, dtoFachlehrer.Kuerzel,
								dtoFachlehrer.Nachname, dtoFachlehrer.Vorname, dtoFachlehrer.Geschlecht, dtoFachlehrer.eMailDienstlich);
					}
				}
				// TODO ggf. im Team-Teaching unterrichtende Lehrer hinzufügen (Zusatzkraft in Leistungsdaten bzw. weitere Lehrkraft bei Kursen)
    		}

    		final int halbjahr = (schule.AnzahlAbschnitte == 4) ? abschnitt.Abschnitt / 2 : abschnitt.Abschnitt;
    		final boolean istSchriftlich = (schuelerabschnitt.kursID != null)
    			&& ((kursart == ZulaessigeKursart.LK1) || (kursart == ZulaessigeKursart.LK2)
    				|| (kursart == ZulaessigeKursart.GKS) || (kursart == ZulaessigeKursart.AB3)
    				|| ((kursart == ZulaessigeKursart.AB4) && (!("Q2".equals(dtoKlasse.ASDKlasse) && (halbjahr == 2)))));

    		final String tmpAbiFach = schuelerabschnitt.AbiturFach;
    		final Integer abiFach = (tmpAbiFach == null) ? null : switch (tmpAbiFach) {
    			case "1" -> 1;
    			case "2" -> 2;
    			case "3" -> 3;
    			case "4" -> 4;
    			default -> null;
    		};
    		final boolean istGemahnt = (schuelerabschnitt.istGemahnt != null) && schuelerabschnitt.istGemahnt;
    		final String mahndatum = schuelerabschnitt.mahndatum;
    		manager.addSchuelerLeistungsdaten(enmSchueler, schuelerabschnitt.leistungID, lerngruppe.id,
    				schuelerabschnitt.note.kuerzel, schuelerabschnitt.tsNote, istSchriftlich, abiFach,
    				schuelerabschnitt.fehlstundenGesamt, schuelerabschnitt.tsFehlstundenGesamt,
    				schuelerabschnitt.fehlstundenUnentschuldigt, schuelerabschnitt.tsFehlstundenUnentschuldigt,
    				schuelerabschnitt.fachbezogeneBemerkungen, schuelerabschnitt.tsFachbezogeneBemerkungen, null,
    				istGemahnt, schuelerabschnitt.tsIstGemahnt, mahndatum);
    		// TODO get and add Teilleistungen
    		// TODO check and add ZP10 - Data
    		// TODO check and add BKAbschluss - Data
    	}

    	// Kopiere den Floskel-Katalog in die ENM-Daten
    	getFloskeln(manager, mapJahrgaenge);

    	return manager.daten;
	}


	private static void initManager(final ENMDatenManager manager, final DTOEigeneSchule schule, final DTOSchuljahresabschnitte abschnitt) {
		// Setze die grundlegenden Schuldaten
    	manager.setSchuldaten(schule.SchulNr, abschnitt.Jahr, schule.AnzahlAbschnitte, abschnitt.Abschnitt,
    			/* TODO */ null, /* TODO */ true, /* TODO */ false, /* TODO */ true,
    			schule.Schulform.daten.kuerzel, /* TODO */ null);
    	// Kopiere den Noten-Katalog aus dem Core-type in die ENM-Daten
		manager.addNoten();
    	// Kopiere den Förderschwerpunkt-Katalog aus dem Core-type in die ENM-Daten
		manager.addFoerderschwerpunkte(schule.Schulform);
	}

	private DTOEigeneSchule getSchule() {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw OperationError.NOT_FOUND.exception();
		return schule;
	}

	private DTOSchuljahresabschnitte getSchuljahresabschnitt(final DTOEigeneSchule schule) {
		final DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (abschnitt == null)
			throw OperationError.NOT_FOUND.exception();
		return abschnitt;
	}

	// TODO Optimierung: Lese nur die aktiven Lehrer aus der Datenbank aus.
	private Map<Long, DTOLehrer> getLehrerListe() {
		final List<DTOLehrer> lehrer = conn.queryAll(DTOLehrer.class);
		if (lehrer.size() == 0)
			throw OperationError.NOT_FOUND.exception();
		return lehrer.stream().collect(Collectors.toMap(e -> e.ID, e -> e));
	}

	private Map<Long, DTOSchueler> getSchuelerListe(final DTOEigeneSchule schule) {
		final List<DTOSchueler> schueler = conn.queryNamed("DTOSchueler.schuljahresabschnitts_id", schule.Schuljahresabschnitts_ID, DTOSchueler.class);
		if (schueler.size() == 0)
			throw OperationError.NOT_FOUND.exception();
		return schueler.stream()
				.filter(s -> s.Status == SchuelerStatus.AKTIV || s.Status == SchuelerStatus.EXTERN)
				.collect(Collectors.toMap(s -> s.ID, s -> s));
	}

	private Map<Long, DTOFach> getFaecherListe() {
		final List<DTOFach> faecher = conn.queryAll(DTOFach.class);
		if (faecher.size() == 0)
			throw OperationError.NOT_FOUND.exception();
		return faecher.stream().collect(Collectors.toMap(f -> f.ID, f -> f));
	}

	private Map<Long, DTOJahrgang> getJahrgangsListe() {
		final List<DTOJahrgang> jahrgaenge = conn.queryAll(DTOJahrgang.class);
		if (jahrgaenge.size() == 0)
			throw OperationError.NOT_FOUND.exception();
		return jahrgaenge.stream().collect(Collectors.toMap(j -> j.ID, j -> j));
	}

	private Map<String, DTOKlassen> getKlassenListe(final DTOEigeneSchule schule) {
		final List<DTOKlassen> klassen = conn.queryNamed("DTOKlassen.schuljahresabschnitts_id", schule.Schuljahresabschnitts_ID, DTOKlassen.class);
		if (klassen.size() == 0)
			throw OperationError.NOT_FOUND.exception();
		return klassen.stream().collect(Collectors.toMap(e -> e.Klasse, e -> e));
	}

	private Map<Long, List<DTOKlassenLeitung>> getKlassenleitungen(final Map<String, DTOKlassen> mapKlassen) {
		final List<Long> klassenIDs = mapKlassen.values().stream().map(kl -> kl.ID).collect(Collectors.toList());
		return conn.queryNamed("DTOKlassenLeitung.klassen_id.multiple", klassenIDs, DTOKlassenLeitung.class).stream()
				.collect(Collectors.groupingBy(kl -> kl.Klassen_ID));
	}

	private Map<Long, DTOKurs> getKurse(final DTOEigeneSchule schule) {
		final List<DTOKurs> kurse = conn.queryNamed("DTOKurs.schuljahresabschnitts_id", schule.Schuljahresabschnitts_ID, DTOKurs.class);
		if (kurse == null)
			throw OperationError.NOT_FOUND.exception();
		return conn.queryAll(DTOKurs.class).stream().collect(Collectors.toMap(k -> k.ID, k -> k));
	}


	/**
	 * Füllt die Datenstruktur für die Floskelgruppen des ENM mit den in der SVWS-DB hinterlegten
	 * Floskelgruppen und den zugehörigen Floskeln.
	 *
	 * @param manager         der ENM-Daten-Manager
	 * @param mapJahrgaenge   eine Map mit den jeweiligen Jahrgängen
	 */
	private void getFloskeln(final ENMDatenManager manager, final Map<Long, DTOJahrgang> mapJahrgaenge) {
		final Map<String, DTOJahrgang> mapJG = mapJahrgaenge.values().stream().collect(Collectors.toMap(j -> j.ASDJahrgang, j -> j));
		final List<DTOFloskelgruppen> dtoFloskelgruppen = conn.queryAll(DTOFloskelgruppen.class);
		final HashMap<String, ENMFloskelgruppe> map = new HashMap<>();
		for (final DTOFloskelgruppen dto : dtoFloskelgruppen) {
			final ENMFloskelgruppe enmFG = new ENMFloskelgruppe();
			enmFG.kuerzel = dto.Kuerzel;
			enmFG.bezeichnung = dto.Bezeichnung;
			enmFG.hauptgruppe = dto.Hauptgruppe;
			map.put(enmFG.kuerzel, enmFG);
			manager.daten.floskelgruppen.add(enmFG);
		}
		final List<DTOFloskeln> dtoFloskeln = conn.queryAll(DTOFloskeln.class);
		for (final DTOFloskeln dto : dtoFloskeln) {
		    final ENMFach fach = manager.getFachByKuerzel(dto.FloskelFach);
			final ENMFloskel enmFl = new ENMFloskel();
			enmFl.kuerzel = dto.Kuerzel;
			enmFl.text = dto.FloskelText;
			enmFl.fachID = (fach == null) ? null : fach.id;
			try {
				enmFl.niveau = Long.parseLong(dto.FloskelNiveau);
			} catch (@SuppressWarnings("unused") final NumberFormatException e) {
				enmFl.niveau = null;
			}
			final DTOJahrgang jg = mapJG.get(dto.FloskelJahrgang);
			enmFl.jahrgangID = (jg == null) ? null : jg.ID;
			final ENMFloskelgruppe enmFG = map.get(dto.FloskelGruppe);
			if (enmFG != null) {
				enmFG.floskeln.add(enmFl);
			}
			// TODO else ... Fehlerbehandlung: Wie ordnet man die Floskel zu? -> allgemein
		}
	}

}
