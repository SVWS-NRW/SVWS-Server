package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.GostJahrgang;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.gost.GostAbiturjahrUtils;
import de.svws_nrw.core.utils.kataloge.jahrgaenge.JahrgaengeUtils;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenVorgabe;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachkombinationen;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFaecher;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostJahrgang}.
 */
public final class DataGostJahrgangsliste extends DataManager<Integer> {

	/** Der Schuljahresabschnitts, auf den sich die Jahrgangsinformationen bei den Abiturjahrgängen beziehen */
	private final Schuljahresabschnitt schuljahresabschnitt;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostJahrgang}.
	 *
	 * @param conn                     die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes, auf den sich die Jahrgangsinformationen bei den Abiturjahrgängen beziehen
	 *
	 * @throws ApiOperationException   falls die ID des Schuljahresabschnittes ungültig ist
	 */
	public DataGostJahrgangsliste(final DBEntityManager conn, final long idSchuljahresabschnitt) throws ApiOperationException {
		super(conn);
		this.schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt);
		if (this.schuljahresabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die ID %d für den Schuljahresabschnitt ist nicht gültig.");
	}


	/**
	 * Bestimmt die Liste der Abiturjahrgänge
	 *
	 * @param conn        die Datenbankverbindung
	 * @param abschnitt   der Schuljahresabschnitt auf den sich die Anfrage (und die damit die Jahrgänge) bezieht
	 *
	 * @return die Liste der Abiturjahrgänge
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostJahrgang> getGostJahrgangsliste(final DBEntityManager conn, final Schuljahresabschnitt abschnitt) throws ApiOperationException {
		if (!conn.getUser().schuleHatGymOb())
			throw new ApiOperationException(Status.NOT_FOUND, "Die Schule hat eine Schulform ohne gymnasiale Oberstufe.");

		// Bestimme die Jahrgaenge der Schule
		final List<DTOJahrgang> dtosJahrgaenge = conn.queryAll(DTOJahrgang.class);
		if ((dtosJahrgaenge == null) || (dtosJahrgaenge.isEmpty()))
			throw new ApiOperationException(Status.NOT_FOUND, "Es konnten keine Jahrgänge gefunden werden.");

		// Lese alle Abiturjahrgänge aus der Datenbank ein und ergänze diese im Vektor
		final ArrayList<GostJahrgang> daten = new ArrayList<>();
		final List<DTOGostJahrgangsdaten> dtos = conn.queryAll(DTOGostJahrgangsdaten.class);
		if (dtos != null) {
			dtos.sort((a, b) -> Integer.compare(a.Abi_Jahrgang, b.Abi_Jahrgang));
			for (final DTOGostJahrgangsdaten jahrgangsdaten : dtos) {
				if (jahrgangsdaten.Abi_Jahrgang < 0)
					continue;
				final GostJahrgang eintrag = new GostJahrgang();
				eintrag.abiturjahr = jahrgangsdaten.Abi_Jahrgang;
				final int restjahre = jahrgangsdaten.Abi_Jahrgang - abschnitt.schuljahr;
				for (final DTOJahrgang jahrgang : dtosJahrgaenge) {
					final Schulform schulform = conn.getUser().schuleGetSchulform();
					Integer jahrgangRestjahre = JahrgaengeUtils.getRestlicheJahre(schulform, Schulgliederung.data().getWertByKuerzel(jahrgang.GliederungKuerzel),
							jahrgang.ASDJahrgang);
					if ((jahrgangRestjahre != null) && (schulform != Schulform.GY) && JahrgaengeUtils.istSekI(jahrgang.ASDJahrgang))
						jahrgangRestjahre += 3;
					if ((jahrgangRestjahre != null) && (restjahre == jahrgangRestjahre)) {
						eintrag.jahrgang = jahrgang.ASDJahrgang;
						if (JahrgaengeUtils.istGymOb(jahrgang.ASDJahrgang))
							break;
					}
				}
				eintrag.halbjahr = abschnitt.abschnitt;
				eintrag.bezeichnung = "Abi " + eintrag.abiturjahr + ((eintrag.jahrgang == null) ? "" : " (" + eintrag.jahrgang + ")");
				eintrag.istAbgeschlossen = (restjahre < 1);
				// Ergänze die Information, ob bereits eine Blockung persistiert wurde anhand der angelegten Kurse in den entsprechenden Lernabschnitten
				final @NotNull List<Schuljahresabschnitt> abschnitte =
						conn.getUser().schuleGetAbschnitteBySchuljahre(eintrag.abiturjahr - 1, eintrag.abiturjahr - 2, eintrag.abiturjahr - 3);
				for (final Schuljahresabschnitt a : abschnitte) {
					final GostHalbjahr halbjahr = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(eintrag.abiturjahr, a.schuljahr, a.abschnitt);
					eintrag.istBlockungFestgelegt[halbjahr.id] = DBUtilsGost.pruefeHatOberstufenKurseInAbschnitt(conn, halbjahr, a);
					eintrag.existierenNotenInLeistungsdaten[halbjahr.id] = DBUtilsGost.pruefeHatNotenFuerOberstufeInAbschnitt(conn, halbjahr, a);
				}
				daten.add(eintrag);
			}
		}

		// Füge einen Eintrag ein, der für keinen Abiturjahrgang steht - also für
		// allgemeine Informationen zu Abiturjahrgängen
		final GostJahrgang eintrag = new GostJahrgang();
		eintrag.abiturjahr = -1;
		eintrag.jahrgang = null;
		eintrag.bezeichnung = "Allgemeine Vorlagen";
		eintrag.istAbgeschlossen = false;
		daten.add(0, eintrag);
		return daten;
	}

	@Override
	public Response getAll() throws ApiOperationException {
		final List<GostJahrgang> daten = getGostJahrgangsliste(conn, this.schuljahresabschnitt);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() throws ApiOperationException {
		return this.getAll();
	}

	@Override
	public Response get(final Integer id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Integer id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Erstellt einen neuen Abiturjahrgang für die gymnasiale Oberstufe. Dabei
	 * werden die Daten des Jahrgangs mit der übergebenen ID als Grundlage
	 * verwendet.
	 *
	 * @param jahrgang_id die ID des Jahrgangs
	 *
	 * @return die HTTP-Response, im Erfolgsfall mit dem Abiturjahrgang
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response create(final long jahrgang_id) throws ApiOperationException {
		// Prüfe die Schuldaten
		if (!conn.getUser().schuleHatGymOb())
			throw new ApiOperationException(Status.NOT_FOUND, "Die Schule hat eine Schulform ohne gymnasiale Oberstufe.");
		final Schulform schulform = conn.getUser().schuleGetSchulform();

		// Ermittle die Jahrgangsdaten und bestimme das Abiturjahr
		final DTOJahrgang jahrgang = conn.queryByKey(DTOJahrgang.class, jahrgang_id);
		if (jahrgang == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final Integer abiturjahr = GostAbiturjahrUtils.getGostAbiturjahr(schulform, Schulgliederung.data().getWertByKuerzel(jahrgang.GliederungKuerzel),
				schuljahresabschnitt.schuljahr, jahrgang.ASDJahrgang);
		if (abiturjahr == null)
			throw new ApiOperationException(Status.NOT_FOUND);

		// Prüfe, ob der Abiturjahrgang bereits angelegt wurde
		DTOGostJahrgangsdaten jahrgangsdaten = conn.queryByKey(DTOGostJahrgangsdaten.class, abiturjahr);
		if (jahrgangsdaten != null)
			throw new ApiOperationException(Status.CONFLICT);

		// Lade die Vorlage für den neuen Abiturjahrgang
		final DTOGostJahrgangsdaten jahrgangsdatenVorlage = DataGostJahrgangsdaten.getVorlage(conn);

		// Erstelle die Jahrgangsdaten mit Default-Werten, Beratungslehrer sind zunächst nicht zugeordnet
		jahrgangsdaten = new DTOGostJahrgangsdaten(abiturjahr);
		jahrgangsdaten.ZusatzkursGEErstesHalbjahr = jahrgangsdatenVorlage.ZusatzkursGEErstesHalbjahr;
		jahrgangsdaten.ZusatzkursGEVorhanden = jahrgangsdatenVorlage.ZusatzkursGEVorhanden;
		jahrgangsdaten.ZusatzkursSWErstesHalbjahr = jahrgangsdatenVorlage.ZusatzkursSWErstesHalbjahr;
		jahrgangsdaten.ZusatzkursSWVorhanden = jahrgangsdatenVorlage.ZusatzkursSWVorhanden;
		jahrgangsdaten.TextBeratungsbogen = jahrgangsdatenVorlage.TextBeratungsbogen;
		jahrgangsdaten.TextMailversand = jahrgangsdatenVorlage.TextMailversand;
		if (!conn.transactionPersist(jahrgangsdaten))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		conn.transactionFlush();
		// Kopiere die Fächer der Gymnasialen Oberstufe aus der allgemeinen Vorlage
		final List<DTOFach> faecher = conn.queryList(DTOFach.QUERY_BY_ISTOBERSTUFENFACH, DTOFach.class, true);
		if (faecher == null)
			throw new NullPointerException();
		for (final DTOFach fach : faecher) {
			if ((fach.Sichtbar == null) || (!fach.Sichtbar))
				continue;
			final DTOGostJahrgangFaecher gostFach = new DTOGostJahrgangFaecher(abiturjahr, fach.ID, fach.IstMoeglichEF1, fach.IstMoeglichEF2,
					fach.IstMoeglichQ11, fach.IstMoeglichQ12, fach.IstMoeglichQ21, fach.IstMoeglichQ22, fach.IstMoeglichAbiGK, fach.IstMoeglichAbiLK);
			gostFach.WochenstundenQPhase = fach.WochenstundenQualifikationsphase;
			if (!conn.transactionPersist(gostFach))
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren des Faches der gymnasialen Oberstufe");
		}
		conn.transactionFlush();
		// Kopiere die Informationen zu nicht möglichen und geforderten
		// Fachkombinationen aus der Vorlage
		final List<DTOGostJahrgangFachkombinationen> faecherKombis =
				conn.queryList(DTOGostJahrgangFachkombinationen.QUERY_BY_ABI_JAHRGANG, DTOGostJahrgangFachkombinationen.class, -1);
		if (faecherKombis == null)
			throw new NullPointerException();
		if (!faecherKombis.isEmpty()) {
			// Bestimme die ID, für welche der Datensatz eingefügt wird
			long idNMK = conn.transactionGetNextID(DTOGostJahrgangFachkombinationen.class);
			for (final DTOGostJahrgangFachkombinationen kombi : faecherKombis) {
				final DTOGostJahrgangFachkombinationen k = new DTOGostJahrgangFachkombinationen(idNMK++, abiturjahr, kombi.Fach1_ID, kombi.Fach2_ID, kombi.EF1,
						kombi.EF2, kombi.Q11, kombi.Q12, kombi.Q21, kombi.Q22, kombi.Typ, kombi.Hinweistext);
				k.Abi_Jahrgang = abiturjahr;
				k.Kursart1 = kombi.Kursart1;
				k.Kursart2 = kombi.Kursart2;
				if (!conn.transactionPersist(k))
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
			}
		}
		conn.transactionFlush();
		// Kopiere die Vorlage für neue Laufbahnplanungen aus dem Vorlage-Jahrgang
		try {
			DataGostJahrgangLaufbahnplanung.transactionResetJahrgang(conn, jahrgangsdaten);
		} catch (final Exception e) {
			if (e instanceof final ApiOperationException aoe)
				throw aoe;
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		}
		conn.transactionFlush();
		// Bestimme die Fachwahlen aus ggf. schon bestehenden Lernabschnitten
		final Jahrgaenge jg = Jahrgaenge.data().getWertBySchluessel(jahrgang.ASDJahrgang);
		if ((jg == Jahrgaenge.EF) || (jg == Jahrgaenge.Q1) || (jg == Jahrgaenge.Q2)) {
			// Bestimme alle Schüler-IDs des angegebenen Abiturjahrgangs
			final Map<Long, DTOFach> mapFaecher = faecher.stream().collect(Collectors.toMap(f -> f.ID, f -> f));
			final List<DTOSchueler> schueler = DBUtilsGostLaufbahn.getSchuelerOfAbiturjahrgang(conn, abiturjahr);
			if ((schueler != null) && (!schueler.isEmpty())) {
				final List<Long> schuelerIDs = schueler.stream().map(s -> s.ID).toList();
				final Set<Long> schuelerIDsBereitsVorhanden = schuelerIDs.isEmpty() ? Collections.emptySet()
						: conn.queryByKeyList(DTOGostSchueler.class, schuelerIDs).stream().map(s -> s.Schueler_ID).collect(Collectors.toSet());
				final List<Integer> abschnitte = Arrays.asList(1, 2);
				final List<DTOSchuljahresabschnitte> schuljahresabschnitte =
						conn.queryList(DTOSchuljahresabschnitte.QUERY_LIST_BY_ABSCHNITT, DTOSchuljahresabschnitte.class, abschnitte);
				final List<Long> schuljahresabschnittIDs = schuljahresabschnitte.stream().map(a -> a.ID).toList();
				final Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte =
						schuljahresabschnitte.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
				final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryList(
						"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.WechselNr = 0 AND e.ASDJahrgang IN ('EF', 'Q1', 'Q2') AND e.Schuljahresabschnitts_ID IN ?2 AND e.SemesterWertung = true",
						DTOSchuelerLernabschnittsdaten.class, schuelerIDs, schuljahresabschnittIDs);
				final List<Long> lernabschnittIDs = lernabschnitte.stream().map(l -> l.ID).toList();
				final Map<Long, List<DTOSchuelerLernabschnittsdaten>> mapLernabschnitte =
						lernabschnitte.stream().collect(Collectors.groupingBy(l -> l.Schueler_ID));
				final List<DTOSchuelerLeistungsdaten> leistungsdaten = lernabschnittIDs.isEmpty() ? new ArrayList<>()
						: conn.queryList(DTOSchuelerLeistungsdaten.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerLeistungsdaten.class, lernabschnittIDs);
				final Map<Long, List<DTOSchuelerLeistungsdaten>> mapLeistungsdaten =
						leistungsdaten.stream().collect(Collectors.groupingBy(l -> l.Abschnitt_ID));
				for (final long schueler_id : schuelerIDs) {
					// Wenn der Schüler bereits Daten für die Laufbahnplanung hat, so müssen diese auch nicht ergänzt werden
					if (schuelerIDsBereitsVorhanden.contains(schueler_id))
						continue;
					final List<DTOSchuelerLernabschnittsdaten> slas = mapLernabschnitte.get(schueler_id);
					if ((slas == null) || (slas.isEmpty()))
						continue;
					final HashMap<Long, DTOGostSchuelerFachbelegungen> fachbelegungen = new HashMap<>();
					final DTOGostSchuelerFachbelegungen[] abifach = new DTOGostSchuelerFachbelegungen[4];
					final GostHalbjahr[] abifachHalbjahr = new GostHalbjahr[4];
					for (final DTOSchuelerLernabschnittsdaten sla : slas) {
						final List<DTOSchuelerLeistungsdaten> slds = mapLeistungsdaten.get(sla.ID);
						if ((slds == null) || (slds.isEmpty()))
							continue;
						final DTOSchuljahresabschnitte dtoSchuljahresabschnitt = mapSchuljahresabschnitte.get(sla.Schuljahresabschnitts_ID);
						if (dtoSchuljahresabschnitt == null)
							continue;
						final GostHalbjahr halbjahr = GostHalbjahr.fromJahrgangUndHalbjahr(sla.ASDJahrgang, dtoSchuljahresabschnitt.Abschnitt);
						if (halbjahr == null)
							continue;
						for (final DTOSchuelerLeistungsdaten sld : slds) {
							final DTOFach fach = mapFaecher.get(sld.Fach_ID);
							if ((fach == null) || (!fach.IstOberstufenFach))
								continue;
							DTOGostSchuelerFachbelegungen fachbelegung = fachbelegungen.get(fach.ID);
							if (fachbelegung == null) {
								fachbelegung = new DTOGostSchuelerFachbelegungen(schueler_id, fach.ID);
								fachbelegungen.put(fach.ID, fachbelegung);
							}
							// Prüfe, ob das Abiturfach in dem Halbjahr gesetzt wurde. Nehme den jeweils
							// letzten Eintrag in den Halbjahren
							try {
								final int abifachNr = Integer.parseInt(sld.AbiFach);
								if (((abifachNr > 0) && (abifachNr < 5))
										&& ((abifachHalbjahr[abifachNr - 1] == null) || (abifachHalbjahr[abifachNr - 1].id < halbjahr.id))) {
									abifach[abifachNr - 1] = fachbelegung;
									abifachHalbjahr[abifachNr - 1] = halbjahr;
								}
							} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
								// kein gültiges Abbiturfach bei dem Lernabschnitt angegeben
							}
							// Setze Fachwahl für das Halbjahr
							switch (halbjahr) {
								case EF1 -> {
									fachbelegung.EF1_Kursart = funcGetKursart.apply(sld, halbjahr);
									fachbelegung.EF1_Punkte = funcGetNotenpunkte.apply(sld.NotenKrz, schuljahresabschnitt.schuljahr);
								}
								case EF2 -> {
									fachbelegung.EF2_Kursart = funcGetKursart.apply(sld, halbjahr);
									fachbelegung.EF2_Punkte = funcGetNotenpunkte.apply(sld.NotenKrz, schuljahresabschnitt.schuljahr);
								}
								case Q11 -> {
									fachbelegung.Q11_Kursart = funcGetKursart.apply(sld, halbjahr);
									fachbelegung.Q11_Punkte = funcGetNotenpunkte.apply(sld.NotenKrz, schuljahresabschnitt.schuljahr);
								}
								case Q12 -> {
									fachbelegung.Q12_Kursart = funcGetKursart.apply(sld, halbjahr);
									fachbelegung.Q12_Punkte = funcGetNotenpunkte.apply(sld.NotenKrz, schuljahresabschnitt.schuljahr);
								}
								case Q21 -> {
									fachbelegung.Q21_Kursart = funcGetKursart.apply(sld, halbjahr);
									fachbelegung.Q21_Punkte = funcGetNotenpunkte.apply(sld.NotenKrz, schuljahresabschnitt.schuljahr);
								}
								case Q22 -> {
									fachbelegung.Q22_Kursart = funcGetKursart.apply(sld, halbjahr);
									fachbelegung.Q22_Punkte = funcGetNotenpunkte.apply(sld.NotenKrz, schuljahresabschnitt.schuljahr);
								}
							}
						}
					}
					for (int i = 1; i <= 4; i++)
						if (abifach[i - 1] != null)
							abifach[i - 1].AbiturFach = i;
					if (!conn.transactionPersist(new DTOGostSchueler(schueler_id, false)))
						throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
					conn.transactionFlush();
					for (final Map.Entry<Long, DTOGostSchuelerFachbelegungen> entry : fachbelegungen.entrySet()) {
						if (!conn.transactionPersist(entry.getValue()))
							throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
					}
					conn.transactionFlush();
				}
			}
		}

		// Kopiere die Informationen zu Gost-Klausurvorgaben aus der Vorlage
		new DataGostKlausurenVorgabe(conn).copyVorgabenToJahrgang(abiturjahr, null, 0);

		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(abiturjahr).build();
	}

	private final BiFunction<String, Integer, String> funcGetNotenpunkte = (final String notenKuerzel, final Integer schuljahr) -> {
		if (notenKuerzel == null)
			return null;
		final Note note = Note.fromKuerzel(notenKuerzel);
		if (note.istNote(schuljahr))
			return "" + note.daten(schuljahr).notenpunkte;
		return switch (note) {
			case ATTEST, E1_MIT_BESONDEREM_ERFOLG_TEILGENOMMEN, E2_MIT_ERFOLG_TEILGENOMMEN, E3_TEILGENOMMEN -> note.daten(schuljahr).kuerzel;
			default -> null;
		};
	};

	private final BiFunction<DTOSchuelerLeistungsdaten, GostHalbjahr, String> funcGetKursart =
			(final DTOSchuelerLeistungsdaten sld, final GostHalbjahr halbjahr) -> {
				final GostKursart kursart = GostKursart.fromKuerzel(sld.KursartAllg);
				final ZulaessigeKursart zulkursart = ZulaessigeKursart.data().getWertByKuerzel(sld.Kursart);
				if ((kursart == null) || (zulkursart == null))
					return null;
				if (((kursart == GostKursart.LK) || (kursart == GostKursart.GK)) && (Note.data().getWertByKuerzel(sld.NotenKrz) == Note.ATTEST))
					return "AT";
				return switch (kursart) {
					case LK -> "LK";
					case GK -> ((zulkursart == ZulaessigeKursart.GKS)
							|| ((zulkursart == ZulaessigeKursart.AB3) || ((zulkursart == ZulaessigeKursart.AB3) && (halbjahr != GostHalbjahr.Q22)))) ? "S"
									: "M";
					case ZK -> "ZK";
					case PJK -> "M";
					case VTF -> "M";
				};
			};


	/**
	 * Entfernt den angegebenen Abiturjahrgang aus der Datenbank, sofern dieser
	 * nicht bereits persistierte Leistungsdaten hat oder bereits abgeschlossen ist.
	 *
	 * @param abiturjahrgang   der zu entfernende Abiturjahrgang
	 *
	 * @return die HTTP-Response im Erfolgsfall
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response delete(final int abiturjahrgang) throws ApiOperationException {
		// Prüfe, ob der Abiturjahrgang existiert und bereits persistierte Leistungsdaten hat...
		final @NotNull GostJahrgangsdaten jahrgangsdaten = DataGostJahrgangsdaten.getJahrgangsdaten(conn, abiturjahrgang);
		for (final GostHalbjahr hj : GostHalbjahr.values())
			if ((jahrgangsdaten.istBlockungFestgelegt[hj.id]) || (jahrgangsdaten.existierenNotenInLeistungsdaten[hj.id]))
				throw new ApiOperationException(Status.BAD_REQUEST, "Ein Abiturjahrgang mit bereits vorhandenen Leistungsdaten kann nicht entfernt werden.");
		if (jahrgangsdaten.istAbgeschlossen)
			throw new ApiOperationException(Status.BAD_REQUEST, "Ein abgeschlossener Abiturjahrgang kann nicht entfernt werden.");
		// Entferne die Jahrgangsdaten des Abiturjahrgangs aus der Datenbank. Die zugehörigen Fachwahlen, etc. werden dann kaskadierend entfernt.
		final DTOGostJahrgangsdaten dto = conn.queryByKey(DTOGostJahrgangsdaten.class, abiturjahrgang);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		conn.transactionRemove(dto);
		conn.transactionFlush();
		return Response.status(Status.NO_CONTENT).build();
	}

}
