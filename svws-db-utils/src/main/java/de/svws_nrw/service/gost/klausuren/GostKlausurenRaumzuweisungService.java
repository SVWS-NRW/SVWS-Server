package de.svws_nrw.service.gost.klausuren;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenRaumdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenPatchResponseData;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumRich;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurtermin;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurterminraumstunde;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.core.utils.gost.klausuren.GostKlausurplanManager;
import de.svws_nrw.core.utils.stundenplan.StundenplanListUtils;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenRaumstunden;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermineRaumstunden;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenRaumstundeRepository;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenSchuelerklausurterminraumstundeRepository;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenStundenplanDataRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Service für Raumzuweisungen von Schülerklausurterminen.
 */
public final class GostKlausurenRaumzuweisungService {

	private final GostKlausurenStundenplanDataRepository stundenplanDataRepository;
	private final GostKlausurenRaumstundeRepository raumstundeRepository;
	private final GostKlausurenSchuelerklausurterminraumstundeRepository schuelerklausurterminraumstundeRepository;
	private final GostKlausurenSchuelerklausurterminraumstundeService schuelerklausurterminraumstundeService;
	private final GostKlausurenRaumstundeService raumstundeService;
	private final GostKlausurenTerminService terminService;
	private final GostKlausurenRaumService raumService;
	private final GostKlausurenSchuelerklausurterminService schuelerklausurterminService;
	private final GostKlausurenSchuelerklausurService schuelerklausurService;
	private final GostKlausurenKursklausurService kursklausurService;
	private final GostKlausurenVorgabeService vorgabeService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param stundenplanDataRepository das Repository für externe Stundenplandaten
	 * @param raumstundeRepository das Repository für Klausurraumstunden
	 * @param schuelerklausurterminraumstundeRepository das Repository für Schülerklausurtermin-Raumstunden-Zuordnungen
	 * @param schuelerklausurterminraumstundeService der Service für Schülerklausurtermin-Raumstunden-Zuordnungen
	 * @param raumstundeService der Service für Klausurraumstunden
	 * @param terminService der Service für Klausurtermine
	 * @param raumService der Service für Klausurräume
	 * @param schuelerklausurterminService der Service für Schülerklausurtermine
	 * @param schuelerklausurService der Service für Schülerklausuren
	 * @param kursklausurService der Service für Kursklausuren
	 * @param vorgabeService der Service für Klausurvorgaben
	 */
	public GostKlausurenRaumzuweisungService(final GostKlausurenStundenplanDataRepository stundenplanDataRepository,
			final GostKlausurenRaumstundeRepository raumstundeRepository,
			final GostKlausurenSchuelerklausurterminraumstundeRepository schuelerklausurterminraumstundeRepository,
			final GostKlausurenSchuelerklausurterminraumstundeService schuelerklausurterminraumstundeService,
			final GostKlausurenRaumstundeService raumstundeService,
			final GostKlausurenTerminService terminService,
			final GostKlausurenRaumService raumService,
			final GostKlausurenSchuelerklausurterminService schuelerklausurterminService,
			final GostKlausurenSchuelerklausurService schuelerklausurService,
			final GostKlausurenKursklausurService kursklausurService,
			final GostKlausurenVorgabeService vorgabeService) {
		this.stundenplanDataRepository = stundenplanDataRepository;
		this.raumstundeRepository = raumstundeRepository;
		this.schuelerklausurterminraumstundeRepository = schuelerklausurterminraumstundeRepository;
		this.schuelerklausurterminraumstundeService = schuelerklausurterminraumstundeService;
		this.raumstundeService = raumstundeService;
		this.terminService = terminService;
		this.raumService = raumService;
		this.schuelerklausurterminService = schuelerklausurterminService;
		this.schuelerklausurService = schuelerklausurService;
		this.kursklausurService = kursklausurService;
		this.vorgabeService = vorgabeService;
	}

	/**
	 * Lädt die Raumdaten zu den übergebenen Klausurterminen inklusive Termine am selben Datum.
	 *
	 * @param terminIds die IDs der Klausurtermine
	 *
	 * @return die Raumdaten
	 */
	public GostKlausurenRaumdaten getRaumDataByTerminIds(final List<Long> terminIds) {
		final GostKlausurenRaumdaten result = new GostKlausurenRaumdaten();
		result.idsKlausurtermine = terminService.getListByDatesOfTerminIds(terminIds).stream().map(t -> t.id).toList();
		result.raeume = raumService.getListByTerminIds(result.idsKlausurtermine);
		if (result.raeume.isEmpty()) {
			return result;
		}
		result.raumstunden = raumstundeService.getListByRaumIds(result.raeume.stream().map(r -> r.id).toList());
		result.schuelerklausurterminRaumstunden =
				schuelerklausurterminraumstundeService.getListByRaumstundeIds(result.raumstunden.stream().map(r -> r.id).toList());
		return result;
	}

	/**
	 * Setzt Raumzuweisungen für Schülerklausurtermine.
	 *
	 * @param raumSchuelerZuteilung die Raumzuweisungen
	 *
	 * @return die geänderten Raumdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenPatchResponseData setzeRaumzuweisungenFuerSchuelerklausurtermine(final List<GostKlausurraumRich> raumSchuelerZuteilung)
			throws ApiOperationException {
		return transactional(() -> setzeRaumzuweisungenFuerSchuelerklausurtermineInTransaction(raumSchuelerZuteilung));
	}

	private GostKlausurenPatchResponseData setzeRaumzuweisungenFuerSchuelerklausurtermineInTransaction(
			final List<GostKlausurraumRich> raumSchuelerZuteilung) {
		if (raumSchuelerZuteilung.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND);
		}

		final GostKlausurenPatchResponseData result = new GostKlausurenPatchResponseData();
		for (final GostKlausurraumRich raum : raumSchuelerZuteilung) {
			if ((raum.idsSchuelerklausurtermine == null) || raum.idsSchuelerklausurtermine.isEmpty()) {
				continue;
			}

			final List<GostSchuelerklausurtermin> neueSchuelerklausurtermine = schuelerklausurterminService.getListByIds(raum.idsSchuelerklausurtermine);
			final List<GostSchuelerklausurterminraumstunde> bestehendeRaumzuordnungen =
					getSchuelerklausurterminraumstundenZuRaumid(raum.klausurraum.id);
			final List<GostSchuelerklausurtermin> alleSchuelerklausurtermineImRaum = new ArrayList<>(
					schuelerklausurterminService.getListByIds(bestehendeRaumzuordnungen.stream()
							.map(zuordnung -> zuordnung.idSchuelerklausurtermin).toList()));
			alleSchuelerklausurtermineImRaum.addAll(neueSchuelerklausurtermine);
			final GostKlausurplanManager manager =
					createKlausurplanManagerMitStundenplan(null, alleSchuelerklausurtermineImRaum, List.of(raum.klausurraum));
			result.addAll(recreateRaumstundenZuRaum(raum, manager));
		}
		return result;
	}

	/**
	 * Aktualisiert Raumstunden und Zuordnungen für die Räume der angegebenen Schülerklausurtermine.
	 *
	 * @param schuelerklausurtermine die Schülerklausurtermine
	 *
	 * @return die geänderten Raumdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenPatchResponseData updateRaeumeZuSchuelerklausurterminen(final List<GostSchuelerklausurtermin> schuelerklausurtermine)
			throws ApiOperationException {
		return transactional(() -> updateRaeumeZuSchuelerklausurterminenInTransaction(schuelerklausurtermine));
	}

	private GostKlausurenPatchResponseData updateRaeumeZuSchuelerklausurterminenInTransaction(final List<GostSchuelerklausurtermin> schuelerklausurtermine) {
		if (schuelerklausurtermine.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND);
		}
		final GostKlausurenPatchResponseData result = new GostKlausurenPatchResponseData();
		final GostKlausurplanManager manager = createKlausurplanManagerMitStundenplan(null, schuelerklausurtermine, null);
		final Set<GostKlausurraum> raeume = schuelerklausurtermine.stream()
				.map(manager::raumGetBySchuelerklausurtermin)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		for (final GostKlausurraum raum : raeume) {
			result.addAll(recreateRaumstundenZuRaum(new GostKlausurraumRich(raum, null), manager));
		}
		return result;
	}

	/**
	 * Aktualisiert Raumdaten eines Klausurtermins.
	 *
	 * @param termin der Klausurtermin
	 *
	 * @return die geänderten Raumdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenPatchResponseData updateRaeumeZuKlausurtermin(final GostKlausurtermin termin) throws ApiOperationException {
		return transactional(() -> updateRaeumeZuKlausurterminInTransaction(termin));
	}

	private GostKlausurenPatchResponseData updateRaeumeZuKlausurterminInTransaction(final GostKlausurtermin termin) {
		if ((termin.datum == null) || (termin.startzeit == null)) {
			final List<GostSchuelerklausurtermin> schuelerklausurtermine = schuelerklausurterminService.getListByTerminIds(List.of(termin.id));
			return loescheRaumzuweisungenFuerSchuelerklausurtermine(schuelerklausurtermine.stream().map(skt -> skt.id).toList());
		}

		final List<GostKlausurraum> raeume = raumService.getListByTerminIds(List.of(termin.id));
		final GostKlausurplanManager manager = createKlausurplanManagerMitStundenplan(List.of(termin), null, null);
		final GostKlausurenPatchResponseData result = new GostKlausurenPatchResponseData();

		for (final GostKlausurraum raum : raeume) {
			if (manager.schuelerklausurterminGetMengeByRaum(raum).isEmpty()) {
				continue;
			}
			result.addAll(recreateRaumstundenZuRaum(new GostKlausurraumRich(raum, null), manager));
		}
		return result;
	}

	/**
	 * Behandelt die Raumzuweisungen vor einer Terminverschiebung.
	 *
	 * @param referenzTermin der Termin vor der Datumsänderung
	 *
	 * @return die geänderten Raumdaten oder null, falls keine Raumdaten betroffen sind
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenPatchResponseData handleRaumzuweisungenBeiTerminverschiebung(final GostKlausurtermin referenzTermin)
			throws ApiOperationException {
		return transactional(() -> handleRaumzuweisungenBeiTerminverschiebungInTransaction(referenzTermin));
	}

	private GostKlausurenPatchResponseData handleRaumzuweisungenBeiTerminverschiebungInTransaction(final GostKlausurtermin referenzTermin) {
		return new TerminverschiebungRun(referenzTermin).execute();
	}

	private final class TerminverschiebungRun {

		private final GostKlausurtermin referenzTermin;
		private final GostKlausurenPatchResponseData geaenderteRaumdaten = new GostKlausurenPatchResponseData();
		private final List<GostKlausurraumRich> neueRaumzuweisungen = new ArrayList<>();
		private final Set<GostKlausurraum> geaenderteRaeume = new HashSet<>();
		private GostKlausurplanManager manager;

		private TerminverschiebungRun(final GostKlausurtermin referenzTermin) {
			this.referenzTermin = referenzTermin;
		}

		private GostKlausurenPatchResponseData execute() {
			final List<GostKlausurtermin> termineAmGleichenDatum = getTermineAmGleichenDatum();
			final List<Long> terminIds = termineAmGleichenDatum.stream().map(t -> t.id).toList();
			final GostKlausurenRaumdaten originalRaumdaten = getRaumDataByTerminIds(terminIds);
			manager = createKlausurManager(termineAmGleichenDatum, terminIds, originalRaumdaten);
			for (final GostKlausurraum raum : originalRaumdaten.raeume) {
				processRaum(raum);
			}
			addNeueRaumzuweisungenToResponse();
			geaenderteRaumdaten.raumdaten.raeume.addAll(geaenderteRaeume);
			return geaenderteRaumdaten;
		}

		private List<GostKlausurtermin> getTermineAmGleichenDatum() {
			if (referenzTermin.datum != null) {
				return terminService.getListByDates(List.of(referenzTermin.datum));
			}
			return List.of(referenzTermin);
		}

		private GostKlausurplanManager createKlausurManager(final List<GostKlausurtermin> termineAmGleichenDatum, final List<Long> terminIds,
				final GostKlausurenRaumdaten originalRaumdaten) {
			final List<GostSchuelerklausurtermin> schuelerklausurtermine = schuelerklausurterminService.getListByTerminIds(terminIds);
			final List<GostSchuelerklausur> schuelerklausuren = getSchuelerklausurenZuSchuelerklausurterminen(schuelerklausurtermine);
			final List<GostKursklausur> kursklausuren = getKursklausurenZuSchuelerklausuren(schuelerklausuren);
			final GostKlausurplanManager result = new GostKlausurplanManager(
					vorgabeService.getListByIds(kursklausuren.stream().map(k -> k.idVorgabe).toList()),
					kursklausuren,
					termineAmGleichenDatum,
					schuelerklausuren,
					schuelerklausurtermine);
			result.addRaumData(originalRaumdaten);
			return result;
		}

		private void processRaum(final GostKlausurraum raum) {
			entferneStundenplanraumWennReferenzterminRaum(raum);
			if (istTerminraumMitTerminfremdenKlausuren(raum)) {
				splitTerminfremdeKlausurenAusRaum(raum);
			} else if (istTerminfremderRaumMitTerminklausuren(raum)) {
				verschiebeTerminklausurenInNeuenRaum(raum);
			}
		}

		private void entferneStundenplanraumWennReferenzterminRaum(final GostKlausurraum raum) {
			if ((raum.idTermin != referenzTermin.id) || (raum.idStundenplanRaum == null)) {
				return;
			}
			final GostKlausurenRaumPatchRequest patchRequest = new GostKlausurenRaumPatchRequest();
			patchRequest.id = raum.id;
			patchRequest.idStundenplanRaum = org.openapitools.jackson.nullable.JsonNullable.of(null);
			patchRaum(patchRequest);
			raum.idStundenplanRaum = null;
			geaenderteRaeume.add(raum);
		}

		private boolean istTerminraumMitTerminfremdenKlausuren(final GostKlausurraum raum) {
			return (raum.idTermin == referenzTermin.id) && manager.raumEnthaeltTerminfremdeKlausuren(raum);
		}

		private boolean istTerminfremderRaumMitTerminklausuren(final GostKlausurraum raum) {
			return (raum.idTermin != referenzTermin.id) && !manager.schuelerklausurterminGetMengeByRaumAndTermin(raum, referenzTermin).isEmpty();
		}

		private void splitTerminfremdeKlausurenAusRaum(final GostKlausurraum raum) {
			final List<GostSchuelerklausurtermin> fremdterminSchuelerklausurtermine =
					manager.schuelerklausurterminFremdterminGetMengeByRaum(raum);
			final GostKlausurtermin fremdTermin = manager.terminOrExceptionBySchuelerklausurtermin(fremdterminSchuelerklausurtermine.get(0));
			final GostKlausurenRaumCreateRequest createRequest = new GostKlausurenRaumCreateRequest();
			createRequest.idTermin = fremdTermin.id;
			createRequest.bemerkung = org.openapitools.jackson.nullable.JsonNullable.of(raum.bemerkung);
			final GostKlausurraum neuerRaum = createRaum(createRequest);
			geaenderteRaeume.add(neuerRaum);
			geaenderteRaeume.add(raum);
			final GostKlausurraumRich neuerRaumRich = new GostKlausurraumRich(neuerRaum, null);
			neuerRaumRich.idsSchuelerklausurtermine = fremdterminSchuelerklausurtermine.stream().map(skt -> skt.id).toList();
			neueRaumzuweisungen.add(neuerRaumRich);
		}

		private void verschiebeTerminklausurenInNeuenRaum(final GostKlausurraum raum) {
			final GostKlausurenRaumCreateRequest createRequest = new GostKlausurenRaumCreateRequest();
			createRequest.idTermin = referenzTermin.id;
			createRequest.bemerkung = org.openapitools.jackson.nullable.JsonNullable.of(raum.bemerkung);
			final GostKlausurraum neuerRaum = createRaum(createRequest);
			geaenderteRaeume.add(neuerRaum);
			final GostKlausurraumRich neuerRaumRich = new GostKlausurraumRich(neuerRaum, null);
			neuerRaumRich.idsSchuelerklausurtermine =
					manager.schuelerklausurterminGetMengeByRaumAndTermin(raum, referenzTermin).stream().map(skt -> skt.id).toList();
			neueRaumzuweisungen.add(neuerRaumRich);
		}

		private void addNeueRaumzuweisungenToResponse() {
			if (!neueRaumzuweisungen.isEmpty()) {
				geaenderteRaumdaten.addAll(setzeRaumzuweisungenFuerSchuelerklausurtermine(neueRaumzuweisungen));
			}
		}

		private GostKlausurraum createRaum(final GostKlausurenRaumCreateRequest createRequest) throws ApiOperationException {
			terminService.get(createRequest.idTermin);
			validateStundenplanRaum(createRequest.idStundenplanRaum);
			return raumService.create(createRequest);
		}

		private GostKlausurraum patchRaum(final GostKlausurenRaumPatchRequest patchRequest) throws ApiOperationException {
			validateStundenplanRaum(patchRequest.idStundenplanRaum);
			return raumService.patch(patchRequest);
		}

		private void validateStundenplanRaum(final org.openapitools.jackson.nullable.JsonNullable<Long> idStundenplanRaumPatch) {
			if (!idStundenplanRaumPatch.isPresent()) {
				return;
			}
			final Long idStundenplanRaum = idStundenplanRaumPatch.get();
			if ((idStundenplanRaum != null) && !stundenplanDataRepository.existsStundenplanRaum(idStundenplanRaum)) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Stundenplanraum nicht gefunden, ID: " + idStundenplanRaum);
			}
		}

	}

	/**
	 * Löscht Raumzuweisungen für Schülerklausurtermine.
	 *
	 * @param schuelerklausurterminIds die IDs der Schülerklausurtermine
	 *
	 * @return die geänderten Raumdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenPatchResponseData loescheRaumzuweisungenFuerSchuelerklausurtermine(final List<Long> schuelerklausurterminIds)
			throws ApiOperationException {
		return transactional(() -> loescheRaumzuweisungenFuerSchuelerklausurtermineInTransaction(schuelerklausurterminIds));
	}

	private GostKlausurenPatchResponseData loescheRaumzuweisungenFuerSchuelerklausurtermineInTransaction(final List<Long> schuelerklausurterminIds) {
		final GostKlausurenPatchResponseData result = new GostKlausurenPatchResponseData();
		if (schuelerklausurterminIds.isEmpty()) {
			return result;
		}
		result.schuelerklausurterminraumstundenGeloescht =
				schuelerklausurterminraumstundeService.deleteBySchuelerklausurterminIds(schuelerklausurterminIds);
		result.raumstundenGeloescht = raumstundeService.deleteUnreferenced();
		return result;
	}

	private GostKlausurplanManager createKlausurplanManagerMitStundenplan(final List<GostKlausurtermin> termine,
			final List<GostSchuelerklausurtermin> schuelerklausurtermine, final Collection<GostKlausurraum> raeume) throws ApiOperationException {
		return new KlausurManagerContextBuilder(termine, schuelerklausurtermine, raeume).create();
	}

	private final class KlausurManagerContextBuilder {

		private final List<GostKlausurtermin> initialTermine;
		private final List<GostSchuelerklausurtermin> initialSchuelerklausurtermine;
		private final Collection<GostKlausurraum> initialRaeume;
		private final Set<GostKlausurtermin> managerTermine = new HashSet<>();
		private final Set<GostKlausurraum> managerRaeume = new HashSet<>();
		private final Set<GostSchuelerklausurtermin> managerSchuelerklausurtermine = new HashSet<>();
		private final Set<GostKlausurraumstunde> managerRaumstunden = new HashSet<>();
		private final Set<GostSchuelerklausurterminraumstunde> managerSchuelerklausurterminraumstunden = new HashSet<>();

		private KlausurManagerContextBuilder(final List<GostKlausurtermin> initialTermine,
				final List<GostSchuelerklausurtermin> initialSchuelerklausurtermine, final Collection<GostKlausurraum> initialRaeume) {
			this.initialTermine = initialTermine;
			this.initialSchuelerklausurtermine = initialSchuelerklausurtermine;
			this.initialRaeume = initialRaeume;
		}

		private GostKlausurplanManager create() {
			addInitialTermine();
			addInitialSchuelerklausurtermine();
			addInitialRaeume();
			loadRaumdataDependencies();
			final GostKlausurplanManager manager = createManager();
			manager.addRaumData(createRaumData());
			addStundenplanManager(manager);
			return manager;
		}

		private void addInitialTermine() {
			if (initialTermine == null) {
				return;
			}
			managerTermine.addAll(initialTermine);
			managerRaeume.addAll(raumService.getListByTerminIds(initialTermine.stream().map(t -> t.id).toList()));
			managerSchuelerklausurtermine.addAll(schuelerklausurterminService.getListByTerminIds(initialTermine.stream().map(t -> t.id).toList()));
		}

		private void addInitialSchuelerklausurtermine() {
			if (initialSchuelerklausurtermine == null) {
				return;
			}
			managerSchuelerklausurtermine.addAll(initialSchuelerklausurtermine);
			managerRaeume.addAll(raumService.getListByTerminIds(getTerminIdsZuSchuelerklausurterminen(initialSchuelerklausurtermine)));
			managerSchuelerklausurterminraumstunden.addAll(schuelerklausurterminraumstundeService
					.getListBySchuelerklausurterminIds(initialSchuelerklausurtermine.stream().map(skt -> skt.id).toList()));
			managerRaumstunden.addAll(raumstundeService.getListByIds(managerSchuelerklausurterminraumstunden.stream()
					.map(zuordnung -> zuordnung.idRaumstunde).toList()));
		}

		private void addInitialRaeume() {
			if (initialRaeume != null) {
				managerRaeume.addAll(initialRaeume);
			}
		}

		private void loadRaumdataDependencies() {
			managerRaeume.addAll(raumService.getListByIds(managerRaumstunden.stream().map(krs -> krs.idRaum).toList()));
			managerRaumstunden.addAll(raumstundeService.getListByRaumIds(managerRaeume.stream().map(raum -> raum.id).toList()));
			managerSchuelerklausurterminraumstunden.addAll(schuelerklausurterminraumstundeService
					.getListByRaumstundeIds(managerRaumstunden.stream().map(krs -> krs.id).toList()));
			managerSchuelerklausurtermine.addAll(schuelerklausurterminService.getListByIds(managerSchuelerklausurterminraumstunden.stream()
					.map(zuordnung -> zuordnung.idSchuelerklausurtermin).toList()));
			managerTermine.addAll(terminService.getListByIds(getTerminIdsZuSchuelerklausurterminen(managerSchuelerklausurtermine)));
		}

		private GostKlausurenRaumdaten createRaumData() {
			final GostKlausurenRaumdaten raumData = new GostKlausurenRaumdaten();
			raumData.raeume.addAll(managerRaeume);
			raumData.schuelerklausurterminRaumstunden.addAll(managerSchuelerklausurterminraumstunden);
			raumData.raumstunden.addAll(managerRaumstunden);
			return raumData;
		}

		private GostKlausurplanManager createManager() {
			final List<GostSchuelerklausur> schuelerklausuren =
					getSchuelerklausurenZuSchuelerklausurterminen(new ArrayList<>(managerSchuelerklausurtermine));
			final List<GostKursklausur> kursklausuren = getKursklausurenZuSchuelerklausuren(schuelerklausuren);
			final List<GostKlausurvorgabe> vorgaben = vorgabeService.getListByIds(kursklausuren.stream().map(k -> k.idVorgabe).toList());
			return new GostKlausurplanManager(vorgaben, kursklausuren, managerTermine, schuelerklausuren, managerSchuelerklausurtermine);
		}

		private List<Long> getTerminIdsZuSchuelerklausurterminen(final Collection<GostSchuelerklausurtermin> schuelerklausurtermine) {
			if (schuelerklausurtermine.isEmpty()) {
				return new ArrayList<>();
			}
			final Set<Long> result = new HashSet<>();
			final List<GostSchuelerklausurtermin> haupttermine = schuelerklausurtermine.stream().filter(skt -> skt.folgeNr == 0).toList();
			if (!haupttermine.isEmpty()) {
				final List<GostSchuelerklausur> schuelerklausuren = getSchuelerklausurenZuSchuelerklausurterminen(haupttermine);
				final List<GostKursklausur> kursklausuren = getKursklausurenZuSchuelerklausuren(schuelerklausuren);
				result.addAll(kursklausuren.stream().map(k -> k.idTermin).filter(Objects::nonNull).toList());
			}
			result.addAll(schuelerklausurtermine.stream().map(skt -> skt.idTermin).filter(Objects::nonNull).toList());
			return new ArrayList<>(result);
		}

		private void addStundenplanManager(final GostKlausurplanManager manager) {
			long idSchuljahresabschnitt = -1;
			List<StundenplanListeEintrag> aktiveStundenplaene = new ArrayList<>();
			for (final GostKlausurtermin termin : manager.terminMitDatumGetMenge()) {
				if (manager.stundenplanManagerGetByTerminOrNull(termin) != null) {
					continue;
				}
				if (idSchuljahresabschnitt != termin.idSchuljahresabschnitt) {
					idSchuljahresabschnitt = termin.idSchuljahresabschnitt;
					aktiveStundenplaene = stundenplanDataRepository.getStundenplaeneAktiv(idSchuljahresabschnitt);
				}
				final StundenplanListeEintrag stundenplan = StundenplanListUtils.get(aktiveStundenplaene, termin.datum);
				manager.stundenplanManagerAdd(stundenplanDataRepository.getStundenplanManager(stundenplan.id));
			}
		}

	}

	private GostKlausurenPatchResponseData recreateRaumstundenZuRaum(final GostKlausurraumRich raum, final GostKlausurplanManager manager)
			throws ApiOperationException {
		return new RaumstundenRebuildRun(raum, manager).execute();
	}

	private final class RaumstundenRebuildRun {

		private final GostKlausurraumRich raum;
		private final GostKlausurplanManager manager;
		private final GostKlausurenPatchResponseData result = new GostKlausurenPatchResponseData();
		private final List<GostSchuelerklausurtermin> schuelerklausurtermine;
		private final GostKlausurtermin termin;

		private RaumstundenRebuildRun(final GostKlausurraumRich raum, final GostKlausurplanManager manager) {
			this.raum = raum;
			this.manager = manager;
			this.schuelerklausurtermine = getSchuelerklausurtermineZuRaum();
			this.termin = manager.terminGetByIdOrException(raum.klausurraum.idTermin);
		}

		private GostKlausurenPatchResponseData execute() {
			result.raumdaten.raumstunden.addAll(persistiereFehlendeRaumstunden(getZeitrasterZuRaum()));
			result.addAll(persistiereSchuelerklausurterminRaumstunden());
			result.raumstundenGeloescht.addAll(raumstundeService.deleteUnreferenced());
			return result;
		}

		private List<GostSchuelerklausurtermin> getSchuelerklausurtermineZuRaum() {
			final List<GostSchuelerklausurtermin> schuelerklausurtermineZuRaum =
					manager.schuelerklausurterminGetMengeByRaumid(raum.klausurraum.id);
			if (!raum.idsSchuelerklausurtermine.isEmpty()) {
				schuelerklausurtermineZuRaum.addAll(schuelerklausurterminService.getListByIds(raum.idsSchuelerklausurtermine));
			}
			return schuelerklausurtermineZuRaum;
		}

		private List<StundenplanZeitraster> getZeitrasterZuRaum() {
			final int minStart = manager.minKlausurstartzeitByKlausurraumAndSchuelerklausurterminMenge(raum.klausurraum, schuelerklausurtermine, true);
			final int maxEnd = manager.maxKlausurendzeitByKlausurraumAndSchuelerklausurterminMenge(raum.klausurraum, schuelerklausurtermine, true);
			final LocalDate klausurdatum = LocalDate.parse(termin.datum);
			final StundenplanManager stundenplanManager = manager.stundenplanManagerGetByTerminOrException(termin);
			final List<StundenplanZeitraster> zeitrasterRaum =
					stundenplanManager.getZeitrasterByWochentagStartVerstrichen(
							Wochentag.fromIDorException(klausurdatum.getDayOfWeek().getValue()), minStart, maxEnd - minStart);
			if (zeitrasterRaum.isEmpty()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Zeitraster konnte nicht ermittelt werden");
			}
			return zeitrasterRaum;
		}

		private List<GostKlausurraumstunde> persistiereFehlendeRaumstunden(final List<StundenplanZeitraster> zeitrasterRaum) {
			final List<DTOGostKlausurenRaumstunden> dtos = new ArrayList<>();
			for (final StundenplanZeitraster stunde : zeitrasterRaum) {
				if (manager.raumstundeGetByRaumAndZeitrasterOrNull(raum.klausurraum, stunde) == null) {
					final DTOGostKlausurenRaumstunden dto = new DTOGostKlausurenRaumstunden(-1L, raum.klausurraum.id);
					dto.Zeitraster_ID = stunde.id;
					dtos.add(dto);
				}
			}
			raumstundeRepository.create(dtos);
			raumstundeRepository.flush();
			final List<GostKlausurraumstunde> raumstunden = dtos.stream().map(GostKlausurenRaumstundeService::toApi).toList();
			raumstunden.forEach(manager::raumstundeAdd);
			return raumstunden;
		}

		private GostKlausurraumstunde persistiereRaumstunde(final StundenplanZeitraster zeitraster) {
			if (manager.raumstundeGetByRaumAndZeitrasterOrNull(raum.klausurraum, zeitraster) != null) {
				throw new DeveloperNotificationException("Raumstunde für Raum %d und Zeitraster %d existiert bereits."
						.formatted(raum.klausurraum.id, zeitraster.id));
			}
			final DTOGostKlausurenRaumstunden dto = new DTOGostKlausurenRaumstunden(-1L, raum.klausurraum.id);
			dto.Zeitraster_ID = zeitraster.id;
			raumstundeRepository.create(dto);
			raumstundeRepository.flush();
			final GostKlausurraumstunde raumstunde = GostKlausurenRaumstundeService.toApi(dto);
			manager.raumstundeAdd(raumstunde);
			return raumstunde;
		}

		private GostKlausurenPatchResponseData persistiereSchuelerklausurterminRaumstunden() {
			final GostKlausurenPatchResponseData response = new GostKlausurenPatchResponseData();
			final LocalDate klausurdatum = LocalDate.parse(termin.datum);
			for (final GostSchuelerklausurtermin schuelerklausurtermin : schuelerklausurtermine) {
				final List<StundenplanZeitraster> zeitrasterSk = getZeitrasterZuSchuelerklausurtermin(schuelerklausurtermin, klausurdatum);
				schuelerklausurterminraumstundeService.deleteBySchuelerklausurterminIds(List.of(schuelerklausurtermin.id));
				final List<GostSchuelerklausurterminraumstunde> bestehendeZuordnungen =
						manager.schuelerklausurraumstundeGetMengeByIdSchuelerklausurtermin(schuelerklausurtermin.id);
				final Set<GostSchuelerklausurterminraumstunde> geloeschteZuordnungen = new HashSet<>(bestehendeZuordnungen);
				for (final StundenplanZeitraster stunde : zeitrasterSk) {
					GostKlausurraumstunde raumstundeVorhanden = manager.raumstundeGetByRaumAndZeitrasterOrNull(raum.klausurraum, stunde);
					if (raumstundeVorhanden == null) {
						raumstundeVorhanden = persistiereRaumstunde(stunde);
						response.raumdaten.raumstunden.add(raumstundeVorhanden);
					}
					final DTOGostKlausurenSchuelerklausurenTermineRaumstunden dto =
							new DTOGostKlausurenSchuelerklausurenTermineRaumstunden(schuelerklausurtermin.id, raumstundeVorhanden.id);
					schuelerklausurterminraumstundeRepository.create(dto);
					schuelerklausurterminraumstundeRepository.flush();
					final GostSchuelerklausurterminraumstunde aktuell =
							GostKlausurenSchuelerklausurterminraumstundeService.toApi(dto);
					if (geloeschteZuordnungen.remove(aktuell)) {
						continue;
					}
					response.raumdaten.schuelerklausurterminRaumstunden.add(aktuell);
				}
				response.schuelerklausurterminraumstundenGeloescht.addAll(
						bestehendeZuordnungen.stream().filter(geloeschteZuordnungen::contains).toList());
			}
			return response;
		}

		private List<StundenplanZeitraster> getZeitrasterZuSchuelerklausurtermin(final GostSchuelerklausurtermin schuelerklausurtermin,
				final LocalDate klausurdatum) {
			final int startzeit = manager.startzeitByKlausurraumAndSchuelerklausurterminOrException(raum.klausurraum, schuelerklausurtermin);
			final List<StundenplanZeitraster> zeitrasterSk =
					manager.stundenplanManagerGetByTerminOrException(termin).getZeitrasterByWochentagStartVerstrichen(
							Wochentag.fromIDorException(klausurdatum.getDayOfWeek().getValue()),
							startzeit, manager.vorgabeBySchuelerklausurtermin(schuelerklausurtermin).dauer);
			if (zeitrasterSk.isEmpty()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Zeitraster konnte nicht ermittelt werden");
			}
			return zeitrasterSk;
		}

	}

	private List<GostSchuelerklausurterminraumstunde> getSchuelerklausurterminraumstundenZuRaumid(final long idRaum) {
		final List<GostKlausurraumstunde> raumstunden = raumstundeService.getListByRaumIds(List.of(idRaum));
		if (raumstunden.isEmpty()) {
			return new ArrayList<>();
		}
		return schuelerklausurterminraumstundeService.getListByRaumstundeIds(raumstunden.stream().map(krs -> krs.id).toList());
	}

	private List<GostSchuelerklausur> getSchuelerklausurenZuSchuelerklausurterminen(final List<GostSchuelerklausurtermin> schuelerklausurtermine) {
		if (schuelerklausurtermine.isEmpty()) {
			return new ArrayList<>();
		}
		return schuelerklausurService.getListByIds(schuelerklausurtermine.stream().map(sk -> sk.idSchuelerklausur).toList());
	}

	private List<GostKursklausur> getKursklausurenZuSchuelerklausuren(final List<GostSchuelerklausur> schuelerklausuren) {
		if (schuelerklausuren.isEmpty()) {
			return new ArrayList<>();
		}
		return kursklausurService.getListByIds(schuelerklausuren.stream().map(sk -> sk.idKursklausur).toList());
	}

}
