package de.svws_nrw.data.bk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.Sprachendaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.data.bk.abi.BKGymFach;
import de.svws_nrw.core.data.bk.abi.BKGymLeistungen;
import de.svws_nrw.core.data.bk.abi.BKGymLeistungenFach;
import de.svws_nrw.core.data.bk.abi.BKGymLeistungenFachHalbjahr;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.bk.BKGymFaecherManager;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import de.svws_nrw.data.schueler.DBUtilsSchueler;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOFachklassen;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;

/**
 * Dies Klassen stellt Hilfmethoden für den Zugriff auf die Leistungsdaten des
 * beruflichen Gymnasiums zur Verfügung.
 */
public final class DataBKGymLeistungen {

	private DataBKGymLeistungen() {
		throw new IllegalStateException("Instantiation of " + DataBKGymLeistungen.class.getName() + " not allowed");
	}


	/**
	 * Bestimmt für den übergegebenen Lernabschnitt eines Schülers das zugehörige Abiturjahr.
	 *
	 * @param schulgliederung   die Schulgliederung des Schülers
	 * @param schuljahr         das aktuelle Schuljahr, in welchem sich der Schüler befindet
	 * @param jahrgang          der Jahrgang des Schülers
	 *
	 * @return das voraussichtliche Jahr des Abiturs
	 */
	public static Integer getBkAbiturjahr(final Schulgliederung schulgliederung, final int schuljahr, final Jahrgaenge jahrgang) {
		if ((schulgliederung == null) || (jahrgang == null))
			return null;
		return switch (jahrgang) {
			case JAHRGANG_01 -> schuljahr + 3;
			case JAHRGANG_02 -> schuljahr + 2;
			case JAHRGANG_03 -> schuljahr + 1;
			case JAHRGANG_04 -> schuljahr;      // Das Jahr nach dem Abitur für den Berufsabschluss
			default -> null;
		};
	}


	/**
	 * Diese Methode erstellt ein {@link BKGymFach}-Objekt mit den Daten aus dem Datenbank-Objekt
	 * von Typ {@link DTOFach}. Dabei werden Informationen aus der Liste der Fächer verwendet.
	 *
	 * @param fach        das Datenbank-Objekt
	 *
	 * @return das {@link BKGymFach}-Objekt
	 */
	public static BKGymFach mapFromDTOFach(final DTOFach fach) {
		final BKGymFach eintrag = new BKGymFach();
		eintrag.id = fach.ID;
		eintrag.kuerzel = fach.StatistikKuerzel;
		eintrag.kuerzelAnzeige = fach.Kuerzel;
		eintrag.bezeichnung = fach.Bezeichnung;
		eintrag.sortierung = fach.SortierungAllg;
		eintrag.istFremdsprache = fach.IstFremdsprache;
		eintrag.istFremdSpracheNeuEinsetzend = fach.IstMoeglichAlsNeueFremdspracheInSekII;
		eintrag.biliSprache = ((fach.Unterrichtssprache != null) && (!"".equals(fach.Unterrichtssprache)) && (!"D".equals(fach.Unterrichtssprache)))
				? fach.Unterrichtssprache.substring(0, 1) : null;
		return eintrag;
	}


	/**
	 * Ermittelt die Liste aller Fächer der gymnasialen Oberstufe, je nach Parameter alle oder nur die in mindestens einem Halbjahr anwählbaren Fächer.
	 *
	 * @param schuljahr         das Schuljahr, auf welches sich die Anfrage bezieht
	 * @param conn              die Datenbank-Verbindung
	 * @param schulgliederung   die Schulgliederung
	 * @param fks               der fünfstellige Fachklassen-Schlüssen (mit laufender Nummer in den letzen beiden Stellen)
	 *
	 * @return die Liste aller Fächer der gymnasialen Oberstufe
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static @NotNull BKGymFaecherManager getFaecherManager(final int schuljahr, final DBEntityManager conn,
			final @NotNull Schulgliederung schulgliederung, final @NotNull String fks) throws ApiOperationException {
		final Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		if (faecher == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final @NotNull List<BKGymFach> tmpFaecher = faecher.values().stream()
				.map(fach -> mapFromDTOFach(fach)).filter(Objects::nonNull).toList();
		return new BKGymFaecherManager(schuljahr, tmpFaecher, schulgliederung, fks);
	}


	private static void getLeistung(final BKGymLeistungen daten, final DTOSchuelerLernabschnittsdaten lernabschnitt,
			final DTOSchuelerLeistungsdaten leistung, final Schuljahresabschnitt abschnittLeistungsdaten,
			final Jahrgaenge jahrgang, final GostHalbjahr halbjahr, final Sprachendaten sprachendaten,
			final BKGymFaecherManager fachManager, final Map<String, BKGymLeistungenFach> faecher) {
		// Prüfe, ob die Kursart eine Kursart der Oberstufe ist.
		final GostKursart kursart = GostKursart.fromKuerzel(leistung.KursartAllg);
		if (kursart == null)
			return;
		// Prüfe, ob das Fach ein Fach des beruflichen Gymnasiums ist
		final BKGymFach bkGymFach = fachManager.get(leistung.Fach_ID);
		if (bkGymFach == null)
			return;
		// Füge die Fächer aus den Leistungsdaten zunächst in die HashMap ein...
		BKGymLeistungenFach fach = faecher.get(bkGymFach.kuerzelAnzeige);
		if (fach == null) {
			fach = new BKGymLeistungenFach();
			fach.fach = bkGymFach;
			faecher.put(bkGymFach.kuerzelAnzeige, fach);
		}
		// Prüfe ggf., ob eine Sprache fortgeführt wurde oder nicht
		final String fremdsprache = BKGymFaecherManager.getFremdsprache(bkGymFach);
		if (fremdsprache != null)
			fach.istFSNeu = (SprachendatenUtils.istNeueinsetzbareSpracheInGOSt(sprachendaten, fremdsprache)); // TODO Prüfung auch zu BK kompatibel ?

		final GostAbiturFach tmpAbiFach = GostAbiturFach.fromIDString(leistung.AbiFach);
		fach.abiturfach = (tmpAbiFach == null) ? null : tmpAbiFach.id;

		// Füge eine Belegung der Kurse für die einzelnen Fächer in dem Halbjahr ein
		final BKGymLeistungenFachHalbjahr belegung = new BKGymLeistungenFachHalbjahr();
		belegung.id = leistung.ID;
		belegung.schuljahr = abschnittLeistungsdaten.schuljahr;
		belegung.halbjahrKuerzel = halbjahr.kuerzel;
		belegung.abschnittGewertet = lernabschnitt.SemesterWertung;
		belegung.jahrgang = jahrgang.daten(belegung.schuljahr).kuerzel;
		belegung.idKurs = leistung.Kurs_ID;
		belegung.idFachlehrer = leistung.Fachlehrer_ID;
		belegung.notenKuerzel = leistung.NotenKrz;
		belegung.kursartKuerzel = kursart.kuerzel;
		belegung.istSchriftlich = (kursart == GostKursart.LK)
				|| ((kursart == GostKursart.GK) && (("GKS".equals(leistung.Kursart))
						|| ("AB3".equals(leistung.Kursart))
						|| ("AB4".equals(leistung.Kursart) && (halbjahr != GostHalbjahr.Q22))));
		belegung.bilingualeSprache = bkGymFach.biliSprache;
		belegung.wochenstunden = (leistung.Wochenstunden == null)
				? kursart.getWochenstunden(fach.istFSNeu)          // TODO Setzen des Wertes in Abhängigkeit der Fächertabelle aus der APO-BK -> Funktion in Fächer-Manager
				: leistung.Wochenstunden;
		belegung.fehlstundenGesamt = (leistung.FehlStd == null) ? 0 : leistung.FehlStd;
		belegung.fehlstundenUnentschuldigt = (leistung.uFehlStd == null) ? 0 : leistung.uFehlStd;
		fach.belegungen.add(belegung);
	}


	/**
	 * Ermittelt die Leistungsdaten des beruflichen Gymnasiums für den Schüler mit der angegebenen ID aus der Datenbank.
	 *
	 * @param conn   die Datenbank-Verbindung
	 * @param id     die ID des Schülers
	 *
	 * @return die Leistungsdaten des beruflichen Gymnasiums für den Schüler mit der angegebenen ID
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static BKGymLeistungen getLeistungsdaten(final DBEntityManager conn, final long id) throws ApiOperationException {
		// Prüfe die Schulform
		final Schulform schulform = conn.getUser().schuleGetSchulform();
		if ((schulform != Schulform.BK) && (schulform != Schulform.SB))
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Schulform der Schule erlaubt kein berufliches Gymnasium.");

		// Bestimme den Schüler und seinen aktuellen Schuljahresabschnitt
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(schueler.Schuljahresabschnitts_ID);

		// Ermittle die Jahrgänge
		final Map<Long, DTOJahrgang> mapJahrgaenge = conn.queryAll(DTOJahrgang.class).stream().collect(Collectors.toMap(j -> j.ID, j -> j));

		// Ermittle die Informationen zu den Sprachendaten und zu Sprachenprüfungen
		final Sprachendaten sprachendaten = DBUtilsSchueler.getSchuelerSprachendaten(conn, id);

		// Bestimme alle Lernabschnitte der Oberstufe des Schülers, sortiert nach dem Schuljahr und dem Abschnitt und anschließend den letzten Lernabschnitt
		final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHUELER_ID,
				DTOSchuelerLernabschnittsdaten.class, id).stream()
				.sorted((l1, l2) -> {
					final Schuljahresabschnitt a1 = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(l1.Schuljahresabschnitts_ID);
					final Schuljahresabschnitt a2 = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(l2.Schuljahresabschnitts_ID);
					return (a1.schuljahr != a2.schuljahr) ? Integer.compare(a1.schuljahr, a2.schuljahr) : Integer.compare(a1.abschnitt, a2.abschnitt);
				})
				.toList();
		final DTOSchuelerLernabschnittsdaten aktLernabschnitt = lernabschnitte.get(lernabschnitte.size() - 1);

		// Bestimme den neuesten Lernabschnitt des Schülers. Aus diesem kann die Fachklasse und das voraussichtliche Abiturjahr ermittelt werden.
		final Schulgliederung schulgliederung = (aktLernabschnitt.Schulgliederung == null)
				? Schulgliederung.getDefault(schulform)
				: Schulgliederung.data().getWertByKuerzel(aktLernabschnitt.Schulgliederung);
		final DTOJahrgang dtoAktJahrgang = mapJahrgaenge.get(aktLernabschnitt.Jahrgang_ID);
		final Jahrgaenge aktJahrgang =
				((dtoAktJahrgang == null) || (dtoAktJahrgang.ASDJahrgang == null)) ? null : Jahrgaenge.data().getWertBySchluessel(dtoAktJahrgang.ASDJahrgang);
		final Integer abiturjahr = getBkAbiturjahr(schulgliederung, schuljahresabschnitt.schuljahr, aktJahrgang); // TODO Spezialfall JAHRGANG_04 mit Wiederholung ggf. berücksichtigen
		if (abiturjahr == null)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Für den Schüler mit der ID %d konnte das Abiturjahr nicht ermittelt werden.".formatted(schueler.ID));
		if (aktLernabschnitt.Fachklasse_ID == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Konnte die Fachklasse des aktuellen Lernabschnittes des Schülers mit der ID %d nicht bestimmen.".formatted(schueler.ID));
		final DTOFachklassen fachklasse = conn.queryByKey(DTOFachklassen.class, aktLernabschnitt.Fachklasse_ID);
		if ((fachklasse == null) || (fachklasse.FKS_AP_SIM == null))
			throw new ApiOperationException(Status.NOT_FOUND,
					"Konnte die Fachklasse mit der ID %d des aktuellen Lernabschnittes des Schülers mit der ID %d nicht bestimmen."
							.formatted(aktLernabschnitt.Fachklasse_ID, schueler.ID));
		final BKGymFaecherManager fachManager = getFaecherManager(abiturjahr - 1, conn, schulgliederung, fachklasse.FKS_AP_SIM);

		// Prüfe, ob die Schulgliederung den Abschluss Abitur erlaubt, um festzustellen, ob der Schüler in einem Bildungsgang des beruflichen Gymnasiums ist
		// TODO Prüfe, ob die Schulgliedungen bei den allgemeinbildenden Abschlüssen ABITUR in dem abiturjahr des Schülers erlaubt


		// Ermittle nun die Leistungsdaten aus den Lernabschnitten
		final BKGymLeistungen daten = new BKGymLeistungen();
		daten.id = schueler.ID;
		daten.aktuellesSchuljahr = schuljahresabschnitt.schuljahr;
		daten.aktuellerJahrgang = (aktJahrgang == null) ? null : aktJahrgang.daten(schuljahresabschnitt.schuljahr).kuerzel;
		daten.sprachendaten = sprachendaten;
		final String biliZweig = aktLernabschnitt.BilingualerZweig;
		if ((biliZweig != null) && (!"".equals(biliZweig)))
			daten.bilingualeSprache = biliZweig.toUpperCase().substring(0, 1);
		// eine Map zur temporären Speicherung der Fächer -> muss später noch sortiert werden
		final Map<String, BKGymLeistungenFach> faecher = new HashMap<>();
		for (final DTOSchuelerLernabschnittsdaten lernabschnitt : lernabschnitte) {
			final Schuljahresabschnitt abschnittLeistungsdaten = conn.getUser().schuleGetAbschnittById(lernabschnitt.Schuljahresabschnitts_ID);
			if (abschnittLeistungsdaten == null)
				continue;

			final DTOJahrgang dtoJahrgang = mapJahrgaenge.get(lernabschnitt.Jahrgang_ID);
			final Jahrgaenge jahrgang =
					((dtoJahrgang == null) || (dtoJahrgang.ASDJahrgang == null)) ? null : Jahrgaenge.data().getWertBySchluessel(dtoJahrgang.ASDJahrgang);
			if (jahrgang == null)
				continue;
			final GostHalbjahr halbjahr =
					GostHalbjahr.fromBkJahrgangUndHalbjahr(jahrgang.daten(schuljahresabschnitt.schuljahr).kuerzel, abschnittLeistungsdaten.abschnitt);
			if (halbjahr == null)
				continue;
			if (Boolean.TRUE.equals(lernabschnitt.SemesterWertung))
				daten.bewertetesHalbjahr[halbjahr.id] = true;
			final List<DTOSchuelerLeistungsdaten> leistungen = conn.queryList(DTOSchuelerLeistungsdaten.QUERY_BY_ABSCHNITT_ID,
					DTOSchuelerLeistungsdaten.class, lernabschnitt.ID);
			if (leistungen.isEmpty())
				daten.bewertetesHalbjahr[halbjahr.id] = false;
			for (final DTOSchuelerLeistungsdaten leistung : leistungen)
				getLeistung(daten, lernabschnitt, leistung, abschnittLeistungsdaten, jahrgang, halbjahr, sprachendaten, fachManager, faecher);
		}
		// Sortiere Fächer anhand der SII-Sortierung der Fächer
		faecher.values().stream()
				.sorted((a, b) -> Integer.compare(a.fach.sortierung, b.fach.sortierung))
				.forEach(daten.faecher::add);
		return daten;
	}


	/**
	 * Ermittelt die für die Abiturberechnung des beruflichen Gymnasiums relevanten Daten für den Schüler mit der angegebenen ID
	 * aus den in der Datenbank gespeicherten Leistungsdaten.
	 *
	 * @param conn   die Datenbank-Verbindung
	 * @param id     die ID des Schülers
	 *
	 * @return die für das Abitur relevanten Daten für den Schüler mit der angegebenen ID
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Abiturdaten getAbiturdatenFromLeistungsdaten(final DBEntityManager conn, final long id) throws ApiOperationException {
		// Prüfe die Schulform
		final Schulform schulform = conn.getUser().schuleGetSchulform();
		if ((schulform != Schulform.BK) && (schulform != Schulform.SB))
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Schulform der Schule erlaubt kein berufliches Gymnasium.");

		// Ermittle den Schüler
		final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, id);
		if (dtoSchueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Schüler mit der ID %d gefunden.".formatted(id));
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(dtoSchueler.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Fehlerhafter Schuljahresabschnitt bei dem Schüler mit der ID %d.".formatted(id));

		// Bestimme den aktuellen Lernabschnitt des Schülers
		final TypedQuery<DTOSchuelerLernabschnittsdaten> queryAktAbschnitt = conn.query(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = :schueler_id AND e.Schuljahresabschnitts_ID = :abschnitt_id AND e.WechselNr = 0",
				DTOSchuelerLernabschnittsdaten.class);
		final DTOSchuelerLernabschnittsdaten aktLernabschnitt = queryAktAbschnitt
				.setParameter("schueler_id", id)
				.setParameter("abschnitt_id", dtoSchueler.Schuljahresabschnitts_ID)
				.getResultList().stream().findFirst().orElse(null);
		if (aktLernabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Konnte keinen aktuellen Lernabschnitt für den Schüler mit der ID %d ermitteln.".formatted(id));

		// Bestimme die Jahrgänge der Schule
		final Map<Long, DTOJahrgang> mapJahrgaenge = conn.queryAll(DTOJahrgang.class).stream().collect(Collectors.toMap(j -> j.ID, j -> j));
		// Bestimme das Abiturjahr
		final Schulgliederung schulgliederung = (aktLernabschnitt.Schulgliederung == null)
				? Schulgliederung.getDefault(schulform)
				: Schulgliederung.data().getWertByKuerzel(aktLernabschnitt.Schulgliederung);
		final DTOJahrgang dtoJahrgang = mapJahrgaenge.get(aktLernabschnitt.Jahrgang_ID);
		final Jahrgaenge aktJahrgang =
				((dtoJahrgang == null) || (dtoJahrgang.ASDJahrgang == null)) ? null : Jahrgaenge.data().getWertBySchluessel(dtoJahrgang.ASDJahrgang);
		final Integer abiturjahr = getBkAbiturjahr(schulgliederung, schuljahresabschnitt.schuljahr, aktJahrgang); // TODO Spezialfall JAHRGANG_04 mit Wiederholung ggf. berücksichtigen
		if (abiturjahr == null)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Für den Schüler mit der ID %d konnte das Abiturjahr nicht ermittelt werden.".formatted(id));
		if (aktLernabschnitt.Fachklasse_ID == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Konnte die Fachklasse des aktuellen Lernabschnittes des Schülers mit der ID %d nicht bestimmen.".formatted(id));
		final DTOFachklassen fachklasse = conn.queryByKey(DTOFachklassen.class, aktLernabschnitt.Fachklasse_ID);
		if ((fachklasse == null) || (fachklasse.FKS_AP_SIM == null))
			throw new ApiOperationException(Status.NOT_FOUND,
					"Konnte die Fachklasse mit der ID %d des aktuellen Lernabschnittes des Schülers mit der ID %d nicht bestimmen."
							.formatted(aktLernabschnitt.Fachklasse_ID, id));
		final BKGymFaecherManager fachManager = getFaecherManager(abiturjahr - 1, conn, schulgliederung, fachklasse.FKS_AP_SIM);

		// Bestimme die bereits vorhandenen Leistungsdaten für die weitere Laufbahnplanung
		final BKGymLeistungen leistungen = getLeistungsdaten(conn, id);
		if (leistungen == null)
			throw new ApiOperationException(Status.NOT_FOUND);

		final Abiturdaten abidaten = new Abiturdaten();
		abidaten.schuelerID = id;
		abidaten.abiturjahr = abiturjahr;
		abidaten.schuljahrAbitur = abidaten.abiturjahr - 1;
		abidaten.sprachendaten = leistungen.sprachendaten;
		abidaten.bilingualeSprache = leistungen.bilingualeSprache;

		for (final GostHalbjahr hj : GostHalbjahr.values())
			abidaten.bewertetesHalbjahr[hj.id] = leistungen.bewertetesHalbjahr[hj.id];

		for (final BKGymLeistungenFach leistungenFach : leistungen.faecher) {
			GostHalbjahr letzteBelegungHalbjahr = null;   // das Halbjahr der letzten Belegung
			final AbiturFachbelegung fach = new AbiturFachbelegung();
			fach.fachID = leistungenFach.fach.id;
			fach.istFSNeu = leistungenFach.istFSNeu;
			fach.abiturFach = (GostAbiturFach.fromID(leistungenFach.abiturfach) == null) ? null : leistungenFach.abiturfach;
			for (final BKGymLeistungenFachHalbjahr leistungenBelegung : leistungenFach.belegungen) {
				if (!leistungenBelegung.abschnittGewertet)
					continue;
				// Nehme jeweils die Kursart, welche beim letzten gewerteten Abschnitt eingetragen ist
				if (((letzteBelegungHalbjahr == null) || (GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel).compareTo(letzteBelegungHalbjahr) > 0))
						&& (GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel) != null)) {
					letzteBelegungHalbjahr = GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel);
					fach.letzteKursart = (GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel) == null) ? null
							: GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel).kuerzel;
				}

				// Erzeuge die zugehörige Belegung
				final AbiturFachbelegungHalbjahr belegung = new AbiturFachbelegungHalbjahr();
				belegung.halbjahrKuerzel = (GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel) == null) ? null
						: GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel).kuerzel;
				belegung.kursartKuerzel = (GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel) == null) ? null
						: GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel).kuerzel;
				if ("AT".equals(leistungenBelegung.notenKuerzel)) {
					final BKGymFach bkGymFach = fachManager.get(fach.fachID);
					if (Fach.SP == Fach.getBySchluesselOrDefault(bkGymFach.kuerzel))
						belegung.kursartKuerzel = "AT";
				}
				belegung.schriftlich = leistungenBelegung.istSchriftlich;
				belegung.biliSprache = leistungenBelegung.bilingualeSprache;
				belegung.idKurs = leistungenBelegung.idKurs;
				belegung.lehrer = leistungenBelegung.idFachlehrer;
				belegung.wochenstunden = leistungenBelegung.wochenstunden;
				belegung.fehlstundenGesamt = leistungenBelegung.fehlstundenGesamt;
				belegung.fehlstundenUnentschuldigt = leistungenBelegung.fehlstundenUnentschuldigt;
				belegung.notenkuerzel = (leistungenBelegung.notenKuerzel == null) ? "" : leistungenBelegung.notenKuerzel;
				fach.belegungen[GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel).id] = belegung;
			}
			// Prüfe, ob das Fach in einem gewerteten Abschnitt belegt wurde. Wenn ja, dann füge es zu es den Fachbelegungen hinzu
			if (letzteBelegungHalbjahr != null)
				abidaten.fachbelegungen.add(fach);
		}

		// Bestimmt die Fehlstunden-Summe für den Block I (Qualifikationsphase) anhand der Fehlstunden bei den einzelnen Kurs-Belegungen.
		int block1FehlstundenGesamt = 0;
		int block1FehlstundenUnentschuldigt = 0;
		for (final AbiturFachbelegung fach : abidaten.fachbelegungen) {
			for (final AbiturFachbelegungHalbjahr belegung : fach.belegungen) {
				if ((belegung == null) || !GostHalbjahr.fromKuerzel(belegung.halbjahrKuerzel).istQualifikationsphase())
					continue;
				block1FehlstundenGesamt += belegung.fehlstundenGesamt;
				block1FehlstundenUnentschuldigt += belegung.fehlstundenUnentschuldigt;
			}
		}
		abidaten.block1FehlstundenGesamt = block1FehlstundenGesamt;
		abidaten.block1FehlstundenUnentschuldigt = block1FehlstundenUnentschuldigt;

		// und gib die Abiturdaten zurück...
		return abidaten;
	}

}
