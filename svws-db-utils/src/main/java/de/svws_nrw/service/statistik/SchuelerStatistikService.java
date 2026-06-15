package de.svws_nrw.service.statistik;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.statistik.AbiturStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLeistungsdatenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.types.schueler.Einschulungsart;
import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbitur;
import de.svws_nrw.repo.faecher.FachRepository;
import de.svws_nrw.repo.schueler.SchuelerAbiturFachRepository;
import de.svws_nrw.repo.schueler.SchuelerAbiturRepository;
import de.svws_nrw.repo.schueler.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schule.SchuleRepository;

/**
 * Ein Service für den Zugriff auf die Schülerdaten für die Statistik
 */
public final class SchuelerStatistikService {

	/** Das Repository für den Zugriff auf die Daten der Schule */
	private final SchuleRepository schuleRepository;

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
	 * @param schuleRepository                  das Repository für Schuldaten
	 * @param schuelerRepository                das Repository für Schülerdaten
	 * @param schuelerLernabschnittRepository   das Repository für Lernabschnitte der Schüler
	 * @param schuelerLeistungsdatenRepository  das Repository für den Zugriff auf die Leistungsdaten
	 * @param schuelerAbiturRepository          das Repository für Abiturdaten der Schüler
	 * @param schuelerAbiturFachRepository      das Repository für Abiturfachdaten der Schüler
	 * @param fachRepository                    das Repository für Fächer
	 */
	public SchuelerStatistikService(final SchuleRepository schuleRepository,
			final SchuelerRepository schuelerRepository,
			final SchuelerLernabschnittRepository schuelerLernabschnittRepository,
			final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository,
			final SchuelerAbiturRepository schuelerAbiturRepository,
			final SchuelerAbiturFachRepository schuelerAbiturFachRepository,
			final FachRepository fachRepository) {
		this.schuleRepository = schuleRepository;
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

	private static SchuelerLernabschnittStatistikGesamt mapLernabschnittsdaten(final DTOSchuelerLernabschnittsdaten dto,
			final List<DTOSchuelerLeistungsdaten> dtosLeistungen) {
		final var daten = new SchuelerLernabschnittStatistikGesamt();
		daten.id = dto.ID;
		daten.idSchuljahresabschnitt = dto.Schuljahresabschnitts_ID;
		daten.idKlasse = dto.Klassen_ID;
		daten.schulgliederung = dto.Schulgliederung;
		daten.idJahrgang = dto.Jahrgang_ID;
		daten.epJahre = dto.EPJahre;
		daten.idFachklasse = dto.Fachklasse_ID;
		daten.organisationsform = dto.OrgFormKrz;
		daten.Klassenart = dto.Klassenart;
		daten.hatSchwerbehinderungsNachweis = (dto.Schwerbehinderung != null) && dto.Schwerbehinderung;
		daten.idFoerderschwerpunkt1 = dto.Foerderschwerpunkt_ID;
		daten.idFoerderschwerpunkt2 = dto.Foerderschwerpunkt2_ID;
		daten.versetzungsvermerk = dto.VersetzungKrz;
		daten.leistungsdaten.addAll(dtosLeistungen.stream().map(SchuelerStatistikService::mapLeistungsdaten).toList());
		return daten;
	}

	private static SchuelerStatistikGesamt map(final DTOSchueler dtoSchueler, final DTOSchuelerLernabschnittsdaten dtoLernabschnitt,
			final List<DTOSchuelerLeistungsdaten> dtosLeistungen, final DTOSchuelerAbitur dtoAbitur, final List<String> abiturfaecher) {
		final var daten = new SchuelerStatistikGesamt();
		daten.id = dtoSchueler.ID;
		daten.geschlecht = dtoSchueler.Geschlecht.id;
		daten.geburtsdatum = dtoSchueler.Geburtsdatum;
		daten.wohnortID = dtoSchueler.Ort_ID;
		daten.idStaatsangehoerigkeit = (dtoSchueler.StaatKrz == null) ? null : dtoSchueler.StaatKrz.historie().getLast().id;
		daten.idStaatsangehoerigkeit2 = (dtoSchueler.StaatKrz2 == null) ? null : dtoSchueler.StaatKrz2.historie().getLast().id;
		daten.religionID = dtoSchueler.Religion_ID;
		daten.status = dtoSchueler.idStatus;
		daten.religionabmeldung = dtoSchueler.Religionsabmeldung;
		daten.religionanmeldung = dtoSchueler.Religionsanmeldung;
		daten.bkAzvo = dtoSchueler.BKAZVO;

		daten.hatMigrationshintergrund = Boolean.TRUE.equals(dtoSchueler.Migrationshintergrund);
		daten.zuzugsjahr = dtoSchueler.JahrZuzug;
		daten.idGeburtsland = (dtoSchueler.GeburtslandSchueler == null) ? null : dtoSchueler.GeburtslandSchueler.historie().getLast().id;
		daten.idVerkehrspracheFamilie = (dtoSchueler.VerkehrsspracheFamilie == null) ? null : dtoSchueler.VerkehrsspracheFamilie.historie().getLast().id;
		daten.idGeburtslandVater = (dtoSchueler.GeburtslandVater == null) ? null : dtoSchueler.GeburtslandVater.historie().getLast().id;
		daten.idGeburtslandMutter = (dtoSchueler.GeburtslandMutter == null) ? null : dtoSchueler.GeburtslandMutter.historie().getLast().id;

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
		daten.idGrundschuleEinschulungsart = (einschulungsart == null) ? null : einschulungsart.getLetzterEintrag().id;
		final Uebergangsempfehlung uebergangsempfehlung = Uebergangsempfehlung.data().getWertBySchluessel(dtoSchueler.Uebergangsempfehlung_JG5);
		daten.idKuerzelGrundschuleUebergangsempfehlung = (uebergangsempfehlung == null) ? null : uebergangsempfehlung.historie().getLast().id;
		// Füge die Daten zum Lernabschnitt hinzu
		daten.lernabschnitte.add(mapLernabschnittsdaten(dtoLernabschnitt, dtosLeistungen));

		// Ergänze die Daten aus der Abiturtabelle, sofern für den Schüler welche vorliegen
		if ((dtoAbitur == null) || (abiturfaecher == null) || (abiturfaecher.isEmpty())) {
			daten.abitur = null;
		} else {
			daten.abitur = mapAbiturdaten(dtoAbitur, abiturfaecher);
		}

		return daten;
	}


	/**
	 * Bestimme für die amtliche Schulstatistik die Liste mit den Schülerdaten und gebe diese als Liste
	 * von SchuelerStatistikGesamt-Objekten zurück.
	 *
	 * @return die Liste mit den Schülerdaten für die amtliche Schulstatistik
	 */
	public List<SchuelerStatistikGesamt> getList() {
		// Bestimme zunächst den aktuellen Schuljahresabschnitt der Schule
		final long idSchuljahresabschnitt = this.schuleRepository.getSchuljahresabschnitt();

		// Bestimme dann die aktiven Schüler aus diesem Schuljahresabschnitt
		final var mapSchueler = this.schuelerRepository.getMapAktiveBySchuljahresabschnitt(idSchuljahresabschnitt);

		// Bestimme dann die aktuellen Lernabschnitte zu diesen Schülern
		final var mapLernabschnitte =
				this.schuelerLernabschnittRepository.getMapBySchuelerIDsAndSchuljahreabschnitt(mapSchueler.keySet(), idSchuljahresabschnitt);
		final var idsAbschnitte = mapLernabschnitte.values().stream().map(a -> a.ID).toList();

		// Bestimme die Leistungsdaten zu den Lernabschnitten
		final var mapLeistungsdaten = this.schuelerLeistungsdatenRepository.getMapListByLernabschnittsIds(idsAbschnitte);

		// Bestimme dann die Prüfungsfächer im Abiturbereich zu den übergebenen Schülern
		final var dtosSchuelerAbitur = this.schuelerAbiturRepository.getListBySchuelerIds(mapSchueler.keySet());
		final var mapSchuelerAbitur = dtosSchuelerAbitur.stream().collect(Collectors.toMap(sa -> sa.Schueler_ID, sa -> sa));
		final var dtosSchuelerAbiturFach = this.schuelerAbiturFachRepository.getListBySchuelerIdsNurPruefungsfaecher(mapSchueler.keySet());
		final var idsFaecher = dtosSchuelerAbiturFach.stream().map(f -> f.Fach_ID).distinct().toList();
		final var mapFaecher = this.fachRepository.findListByIds(idsFaecher).stream().collect(Collectors.toMap(f -> f.ID, f -> f.StatistikKuerzel));
		final Map<Long, List<String>> mapSchuelerAbiturFach =
				dtosSchuelerAbiturFach.stream().collect(Collectors.groupingBy(f -> f.Schueler_ID, Collectors.collectingAndThen(
						Collectors.toList(), list -> {
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
						}
				)));

		// Gebe alle Schülerinformationen zurück, wo ein Lernabschnitt im aktuellen Schuljahresabschnitt vorliegt
		return mapLernabschnitte.keySet().stream().map(
				id -> map(mapSchueler.get(id), mapLernabschnitte.get(id), mapLeistungsdaten.get(mapLernabschnitte.get(id).ID), mapSchuelerAbitur.get(id),
						mapSchuelerAbiturFach.get(id)))
				.toList();
	}

}
