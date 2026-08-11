package de.svws_nrw.service.statistik;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.data.statistik.AbiturStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLeistungsdatenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.types.schueler.Einschulungsart;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.asd.types.schueler.Versetzungsvermerk;
import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.types.schule.Verkehrssprache;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbitur;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.repo.schueler.SchuelerAbiturFachRepository;
import de.svws_nrw.repo.schueler.SchuelerAbiturRepository;
import de.svws_nrw.repo.schueler.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den Zugriff auf die Schülerdaten für die Statistik
 */
public final class SchuelerStatistikService {

	/** Das Repository für den Zugriff auf die Daten des aktuellen Benutzers */
	private final BenutzerAllgemeinRepository benutzerRepository;

	/** Das Repository für den Zugriff auf die Schülerdaten */
	private final SchuelerRepository schuelerRepository;

	/** Das Repository für den Zugriff auf die Lernabschnitte */
	private final SchuelerLernabschnittRepository schuelerLernabschnittRepository;

	/** Das Repository für den Zugriff auf die Leistungsdaten */
	private final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository;

	/** Das Repository für den Zugriff auf die Abiturdaten */
	private final SchuelerAbiturRepository schuelerAbiturRepository;

	/** Das Repository für den Zugriff auf die Abiturfachdaten */
	private final SchuelerAbiturFachRepository schuelerAbiturFachRepository;

	/** Das Repository für den Zugriff auf allgemeine Fächer-Daten */
	private final FachRepository fachRepository;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param benutzerRepository                das Repository für den Zugriff auf die Daten des aktuellen Benutzers
	 * @param schuelerRepository                das Repository für Schülerdaten
	 * @param schuelerLernabschnittRepository   das Repository für Lernabschnitte der Schüler
	 * @param schuelerLeistungsdatenRepository  das Repository für den Zugriff auf die Leistungsdaten
	 * @param schuelerAbiturRepository          das Repository für Abiturdaten der Schüler
	 * @param schuelerAbiturFachRepository      das Repository für Abiturfachdaten der Schüler
	 * @param fachRepository                    das Repository für Fächer
	 */
	public SchuelerStatistikService(final BenutzerAllgemeinRepository benutzerRepository,
			final SchuelerRepository schuelerRepository,
			final SchuelerLernabschnittRepository schuelerLernabschnittRepository,
			final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository,
			final SchuelerAbiturRepository schuelerAbiturRepository,
			final SchuelerAbiturFachRepository schuelerAbiturFachRepository,
			final FachRepository fachRepository) {
		this.benutzerRepository = benutzerRepository;
		this.schuelerRepository = schuelerRepository;
		this.schuelerLernabschnittRepository = schuelerLernabschnittRepository;
		this.schuelerLeistungsdatenRepository = schuelerLeistungsdatenRepository;
		this.schuelerAbiturRepository = schuelerAbiturRepository;
		this.schuelerAbiturFachRepository = schuelerAbiturFachRepository;
		this.fachRepository = fachRepository;
	}


	private static AbiturStatistikGesamt mapAbiturdaten(final DTOSchuelerAbitur dto, final List<String> faecher) {
		final var daten = new AbiturStatistikGesamt();
		daten.note = dto.AbiturNote;
		daten.istZugelassen = "+".equals(dto.BlockI_HatZulassung);
		daten.hatBestanden = (dto.Pruefung_hatBestanden != null) && dto.Pruefung_hatBestanden;
		daten.istZurueckgetreten = "R".equals(dto.BlockI_HatZulassung);
		daten.abifach = faecher;
		return daten;
	}


	private static SchuelerLeistungsdatenStatistikGesamt mapLeistungsdaten(final DTOSchuelerLeistungsdaten dto) {
		final var daten = new SchuelerLeistungsdatenStatistikGesamt();
		daten.id = dto.ID;
		daten.fachID = dto.Fach_ID;
		daten.kursID = dto.Kurs_ID;
		daten.kursart = (dto.Kursart == null) ? ZulaessigeKursart.PUK.historie().getLast().kuerzel : dto.Kursart;
		try {
			daten.abifach = (dto.AbiFach == null) ? null : Integer.parseInt(dto.AbiFach);
		} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
			daten.abifach = null;
		}
		daten.lehrerID = dto.Fachlehrer_ID;
		daten.wochenstunden = (dto.Wochenstunden == null) ? 0 : dto.Wochenstunden;
		daten.zusatzkraftID = dto.Zusatzkraft_ID;
		daten.zusatzkraftWochenstunden = (dto.WochenstdZusatzkraft == null) ? 0 : dto.WochenstdZusatzkraft;
		return daten;
	}

	private SchuelerLernabschnittStatistikGesamt mapLernabschnittsdaten(final DTOSchuelerLernabschnittsdaten dto,
			final List<DTOSchuelerLeistungsdaten> dtosLeistungen, final int schuljahr) {
		final var daten = new SchuelerLernabschnittStatistikGesamt();
		daten.id = dto.ID;
		daten.idSchuljahresabschnitt = dto.Schuljahresabschnitts_ID;
		daten.idKlasse = dto.Klassen_ID;
		daten.idSchulgliederung = Schulgliederung.data().getIDByWertAndSchuljahr(Schulgliederung.data().getWertBySchluessel(dto.Schulgliederung),
				schuljahr);
		daten.idJahrgang = dto.Jahrgang_ID;
		daten.epJahre = dto.EPJahre;
		daten.idFachklasse = dto.Fachklasse_ID;
		final Schulform schulform = benutzerRepository.getAktuellerBenutzer().schuleGetSchulform();

		if (Schulform.WB == schulform) {
			daten.idOrganisationsform = WeiterbildungskollegOrganisationsformen.data().getIDByWertAndSchuljahr(
					WeiterbildungskollegOrganisationsformen.data().getWertBySchluessel(dto.OrgFormKrz), schuljahr);
		} else if ((Schulform.BK == schulform) || (Schulform.SB == schulform)) {
			daten.idOrganisationsform = BerufskollegOrganisationsformen.data().getIDByWertAndSchuljahr(
					BerufskollegOrganisationsformen.data().getWertBySchluessel(dto.OrgFormKrz), schuljahr);
		} else {
			daten.idOrganisationsform = AllgemeinbildendOrganisationsformen.data()
					.getIDByWertAndSchuljahr(AllgemeinbildendOrganisationsformen.data().getWertBySchluessel(dto.OrgFormKrz), schuljahr);
		}
		daten.idKlassenart = Klassenart.data().getIDByWertAndSchuljahr(Klassenart.data().getWertBySchluessel(dto.Klassenart), schuljahr);
		daten.hatSchwerbehinderungsNachweis = (dto.Schwerbehinderung != null) && dto.Schwerbehinderung;
		daten.idFoerderschwerpunkt1 = dto.Foerderschwerpunkt_ID;
		daten.idFoerderschwerpunkt2 = dto.Foerderschwerpunkt2_ID;
		daten.idVersetzungsvermerk = (Versetzungsvermerk.data().getWertByKuerzel(dto.VersetzungKrz) == null) ? -1L : Versetzungsvermerk.data().getWertByKuerzel(dto.VersetzungKrz).id(schuljahr);
		daten.leistungsdaten.addAll(dtosLeistungen.stream().map(SchuelerStatistikService::mapLeistungsdaten).toList());
		return daten;
	}

	private SchuelerStatistikGesamt map(final DTOSchueler dtoSchueler, final DTOSchuelerLernabschnittsdaten dtoLernabschnitt,
			final DTOSchuelerLernabschnittsdaten lernabschnittLetzteVersetzung, final List<DTOSchuelerLeistungsdaten> dtosLeistungen,
			final DTOSchuelerAbitur dtoAbitur, final List<String> abiturfaecher) {
		final Schuljahresabschnitt schuljahresabschnitt =
				benutzerRepository.getAktuellerBenutzer().schuleGetSchuljahresabschnittByIdOrDefault(dtoSchueler.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Konnte keinen Schuljahresabschnitt für den Schüler bestimmen.");
		}
		final int schuljahr = schuljahresabschnitt.schuljahr;
		final var daten = new SchuelerStatistikGesamt();
		daten.id = dtoSchueler.ID;
		daten.geschlecht = dtoSchueler.Geschlecht.id;
		daten.geburtsdatum = dtoSchueler.Geburtsdatum;
		daten.wohnortID = dtoSchueler.Ort_ID;
		daten.idStaatsangehoerigkeit = Nationalitaeten.data().getIDByWertAndSchuljahr(dtoSchueler.StaatKrz, schuljahr);
		daten.idStaatsangehoerigkeit2 = Nationalitaeten.data().getIDByWertAndSchuljahr(dtoSchueler.StaatKrz2, schuljahr);
		daten.religionID = dtoSchueler.Religion_ID;
		daten.status = dtoSchueler.idStatus;
		daten.religionabmeldung = dtoSchueler.Religionsabmeldung;
		daten.religionanmeldung = dtoSchueler.Religionsanmeldung;
		daten.bkAzvo = dtoSchueler.BKAZVO;

		daten.hatMigrationshintergrund = Boolean.TRUE.equals(dtoSchueler.Migrationshintergrund);
		daten.zuzugsjahr = dtoSchueler.JahrZuzug;
		daten.idGeburtsland = Nationalitaeten.data().getIDByWertAndSchuljahr(dtoSchueler.GeburtslandSchueler, schuljahr);
		daten.idVerkehrspracheFamilie = Verkehrssprache.data().getIDByWertAndSchuljahr(dtoSchueler.VerkehrsspracheFamilie, schuljahr);
		daten.idGeburtslandVater = Nationalitaeten.data().getIDByWertAndSchuljahr(dtoSchueler.GeburtslandVater, schuljahr);
		daten.idGeburtslandMutter = Nationalitaeten.data().getIDByWertAndSchuljahr(dtoSchueler.GeburtslandMutter, schuljahr);

		daten.vorherigeSchuleNr = dtoSchueler.LSSchulNr;
		daten.vorigeAllgHerkunft = dtoSchueler.LSSchulform;
		daten.vorigeArtLetzteVersetzung = dtoSchueler.LSVersetzung;
		daten.idVorigeAbschlussart = dtoSchueler.LSEntlassArt;
		daten.vorigeEntlassdatum = dtoSchueler.LSSchulEntlassDatum;
		daten.vorigeEntlassjahrgang = dtoSchueler.LSJahrgang;

		daten.entlassungDatum = dtoSchueler.Entlassdatum;
		daten.idEntlassungAbschlussart = dtoSchueler.Entlassart;
		daten.beginnBildungsgang = dtoSchueler.BeginnBildungsgang;
		daten.istJvaSchueler = (dtoSchueler.JVA != null) && dtoSchueler.JVA;

		final Einschulungsart einschulungsart = Einschulungsart.data().getWertBySchluessel(dtoSchueler.EinschulungsartASD);
		daten.idGrundschuleEinschulungsart = Einschulungsart.data().getIDByWertAndSchuljahr(einschulungsart, schuljahr);
		final Uebergangsempfehlung uebergangsempfehlung = Uebergangsempfehlung.data().getWertBySchluessel(dtoSchueler.Uebergangsempfehlung_JG5);
		daten.idKuerzelGrundschuleUebergangsempfehlung =
				Uebergangsempfehlung.data().getIDByWertAndSchuljahr(uebergangsempfehlung, schuljahr);

		// Füge die Daten zum Lernabschnitt hinzu
		if (lernabschnittLetzteVersetzung != null) {
			final int schuljahrVorher = benutzerRepository.getAktuellerBenutzer()
					.schuleGetSchuljahresabschnittByIdOrDefault(lernabschnittLetzteVersetzung.Schuljahresabschnitts_ID).schuljahr;
			daten.lernabschnitte.add(mapLernabschnittsdaten(lernabschnittLetzteVersetzung, new ArrayList<>(), schuljahrVorher));
		}
		daten.lernabschnitte.add(mapLernabschnittsdaten(dtoLernabschnitt, dtosLeistungen, schuljahr));


		// Ergänze die Daten aus der Abiturtabelle, sofern für den Schüler welche vorliegen
		if ((dtoAbitur == null) || (abiturfaecher == null) || (abiturfaecher.isEmpty())) {
			daten.abitur = null;
		} else {
			daten.abitur = mapAbiturdaten(dtoAbitur, abiturfaecher);
		}

		return daten;
	}

	private List<SchuelerStatistikGesamt> getAktiveSchueler(final Schuljahresabschnitt schuljahresabschnitt,
			final Schuljahresabschnitt schuljahresabschnittLetzteVersetzung) {
		// Bestimme dann die aktiven Schüler aus diesem Schuljahresabschnitt
		final var listStatus = List.of(SchuelerStatus.AKTIV.historie().getLast().id, SchuelerStatus.EXTERN.historie().getLast().id);
		final var mapSchueler = schuelerRepository.getMapByStatusAndSchuljahresabschnitt(schuljahresabschnitt.id, listStatus);

		// Bestimme dann die aktuellen Lernabschnitte zu diesen Schülern
		final var mapLernabschnitte =
				schuelerLernabschnittRepository.getMapBySchuelerIDsAndSchuljahreabschnitt(mapSchueler.keySet(), schuljahresabschnitt.id);
		final var idsAbschnitte = new ArrayList<>(mapLernabschnitte.values().stream().map(a -> a.ID).toList());

		// Bestimme dann die vorherigen Lernabschnitte zu diesen Schuelern
		final var mapLernabschnitteVorher = (schuljahresabschnittLetzteVersetzung == null) ? new HashMap<Long, DTOSchuelerLernabschnittsdaten>()
				: schuelerLernabschnittRepository.getMapBySchuelerIDsAndSchuljahreabschnitt(mapSchueler.keySet(), schuljahresabschnittLetzteVersetzung.id);
		idsAbschnitte.addAll(mapLernabschnitteVorher.values().stream().map(a -> a.ID).toList());

		// Bestimme die Leistungsdaten zu den Lernabschnitten
		final var mapLeistungsdaten = schuelerLeistungsdatenRepository.getMapListByLernabschnittsIds(idsAbschnitte);

		// Bestimme dann die Prüfungsfächer im Abiturbereich zu den übergebenen Schülern
		final var abiturDaten = getSchuelerAbiturDaten(
				mapSchueler.keySet(),
				schuljahresabschnittLetzteVersetzung);

		final var mapSchuelerAbitur = abiturDaten.mapSchuelerAbitur();
		final var mapSchuelerAbiturFach = abiturDaten.mapSchuelerAbiturFach();

		final List<SchuelerStatistikGesamt> result = new ArrayList<>();
		for (final long id : mapLernabschnitte.keySet()) {
			final var schueler = mapSchueler.get(id);
			final var lernabschnitt = mapLernabschnitte.get(id);
			final var leistungsdaten = mapLeistungsdaten.get(lernabschnitt.ID);
			final var lernabschnittLetzteVersetzung = mapLernabschnitteVorher.get(id);
			final var schuelerAbitur = mapSchuelerAbitur.get(id);
			final var schuelerAbiturFaecher = mapSchuelerAbiturFach.get(id);
			result.add(map(schueler, lernabschnitt, lernabschnittLetzteVersetzung, leistungsdaten, schuelerAbitur, schuelerAbiturFaecher));
		}
		return result;
	}

	private List<SchuelerStatistikGesamt> getAndereSchueler(final Schuljahresabschnitt schuljahresabschnitt,
			final Schuljahresabschnitt schuljahresabschnittLetzteVersetzung) {
		if (schuljahresabschnitt.idVorigerAbschnitt == null) {
			return Collections.emptyList();
		}

		// Bestimme dann die beurlaubten und abgegangenen Schüler aus den Schuljahresabschnitten von diesem und dem vorherigen Schuljahr
		final var listSchuljahresabschnitte = new ArrayList<Long>();
		Schuljahresabschnitt current = schuljahresabschnitt;

		while ((current != null) && (current.schuljahr >= (schuljahresabschnitt.schuljahr - 1))) {
			listSchuljahresabschnitte.add(current.id);
			current = benutzerRepository.getAktuellerBenutzer().schuleGetAbschnittById(current.idVorigerAbschnitt);
		}
		final var listStatus = List.of(SchuelerStatus.ABGANG.historie().getLast().id, SchuelerStatus.ABSCHLUSS.historie().getLast().id,
				SchuelerStatus.BEURLAUBT.historie().getLast().id);
		final var mapSchueler = schuelerRepository.getMapByStatusAndSchuljahresabschnitte(listSchuljahresabschnitte, listStatus);
		// Bestimme dann die aktuellen Lernabschnitte zu diesen Schülern
		final var mapLernabschnitte = schuelerLernabschnittRepository.getMapAktuelleBySchuelerIDs(mapSchueler.keySet());

		// Bestimme dann die Prüfungsfächer im Abiturbereich zu den übergebenen Schülern
		final var abiturDaten = getSchuelerAbiturDaten(
				mapSchueler.keySet(),
				schuljahresabschnittLetzteVersetzung);

		final var mapSchuelerAbitur = abiturDaten.mapSchuelerAbitur();
		final var mapSchuelerAbiturFach = abiturDaten.mapSchuelerAbiturFach();

		final List<SchuelerStatistikGesamt> result = new ArrayList<>();
		for (final long id : mapLernabschnitte.keySet()) {
			final var schueler = mapSchueler.get(id);
			final var lernabschnitt = mapLernabschnitte.get(id);
			final var leistungsdaten = new ArrayList<DTOSchuelerLeistungsdaten>();
			final var schuelerAbitur = mapSchuelerAbitur.get(id);
			final var schuelerAbiturFaecher = mapSchuelerAbiturFach.get(id);

			result.add(map(schueler, lernabschnitt, null, leistungsdaten, schuelerAbitur, schuelerAbiturFaecher));
		}
		return result;
	}

	/**
	 * Bestimme für die amtliche Schulstatistik die Liste mit den Schülerdaten und gebe diese als Liste
	 * von SchuelerStatistikGesamt-Objekten zurück.
	 *
	 * @return die Liste mit den Schülerdaten für die amtliche Schulstatistik
	 */
	public List<SchuelerStatistikGesamt> getList() {
		// Bestimme zunächst den aktuellen und den davor liegenden Schuljahresabschnitt der Schule
		final Schuljahresabschnitt schuljahresabschnitt = benutzerRepository.getAktuellerBenutzer().schuleGetSchuljahresabschnitt();
		final Schuljahresabschnitt schuljahresabschnittVorher = (schuljahresabschnitt.idVorigerAbschnitt == null) ? null
				: benutzerRepository.getAktuellerBenutzer().schuleGetAbschnittById(schuljahresabschnitt.idVorigerAbschnitt);
		final Schuljahresabschnitt schuljahresabschnittLetztesSchuljahr =
				benutzerRepository.getAktuellerBenutzer().schuleGetAbschnittBySchuljahrUndHalbjahr(schuljahresabschnitt.schuljahr - 1, 2);
		final Schuljahresabschnitt schuljahresabschnittLetzteVersetzung = (Schulform.WB == benutzerRepository.getAktuellerBenutzer().schuleGetSchulform())
				? schuljahresabschnittVorher : schuljahresabschnittLetztesSchuljahr;

		final List<SchuelerStatistikGesamt> result = getAktiveSchueler(schuljahresabschnitt, schuljahresabschnittLetzteVersetzung);
		result.addAll(getAndereSchueler(schuljahresabschnitt, schuljahresabschnittLetzteVersetzung));

		return result;
	}

	private record SchuelerAbiturDaten(
			Map<Long, DTOSchuelerAbitur> mapSchuelerAbitur,
			Map<Long, List<String>> mapSchuelerAbiturFach) {
	}


	private SchuelerAbiturDaten getSchuelerAbiturDaten(
			final Set<Long> schuelerIds,
			final Schuljahresabschnitt schuljahresabschnittLetzteVersetzung) {

		// Bestimme dann die Prüfungsfächer im Abiturbereich zu den übergebenen Schülern
		final List<DTOSchuelerAbitur> dtosSchuelerAbitur =
				(schuljahresabschnittLetzteVersetzung == null)
						? Collections.emptyList()
						: schuelerAbiturRepository.getListBySchuelerIds(schuelerIds).stream()
								.filter(s -> (s.Schuljahresabschnitts_ID == null)
										|| (s.Schuljahresabschnitts_ID == schuljahresabschnittLetzteVersetzung.id))
								.toList();

		final var mapSchuelerAbitur = dtosSchuelerAbitur.stream()
				.collect(Collectors.toMap(sa -> sa.Schueler_ID, sa -> sa));

		final var dtosSchuelerAbiturFach =
				schuelerAbiturFachRepository.getListBySchuelerIdsNurPruefungsfaecher(schuelerIds);

		final var idsFaecher = dtosSchuelerAbiturFach.stream()
				.map(f -> f.Fach_ID)
				.distinct()
				.toList();

		final var mapFaecher = fachRepository.findListByIds(idsFaecher).stream()
				.collect(Collectors.toMap(f -> f.ID, f -> f.StatistikKuerzel));

		final Map<Long, List<String>> mapSchuelerAbiturFach =
				dtosSchuelerAbiturFach.stream().collect(Collectors.groupingBy(
						f -> f.Schueler_ID,
						Collectors.collectingAndThen(Collectors.toList(), list -> {
							if (list.size() < 4) {
								return Collections.emptyList();
							}
							list.sort(Comparator.comparing(dto -> dto.AbiturFach));

							final List<String> kuerzel = new ArrayList<>();
							for (int i = 0; i < list.size(); i++) {
								final var abiFach = list.get(i);
								if ((i + 1) != abiFach.AbiturFach.id) {
									return Collections.emptyList();
								}
								final var fachkuerzel = mapFaecher.get(abiFach.Fach_ID);
								if (fachkuerzel == null) {
									return Collections.emptyList();
								}
								kuerzel.add(fachkuerzel);
							}
							return kuerzel;
						})));

		return new SchuelerAbiturDaten(mapSchuelerAbitur, mapSchuelerAbiturFach);
	}

}
