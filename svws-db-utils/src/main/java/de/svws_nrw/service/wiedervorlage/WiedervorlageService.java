package de.svws_nrw.service.wiedervorlage;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.WiedervorlageEintrag;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schule.DTOWiedervorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.WiedervorlageMapper;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.benutzer.BenutzergruppeRepository;
import de.svws_nrw.repo.benutzer.BenutzergruppenMitgliedRepository;
import de.svws_nrw.repo.benutzer.ViewBenutzerDetailsRepository;
import de.svws_nrw.repo.erzieher.ErzieherRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.wiedervorlage.WiedervorlageRepository;
import de.svws_nrw.service.wiedervorlage.cleanup.WiedervorlageCleanupService;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Service für die Verwaltung von Wiedervorlage-Einträgen.
 */
public final class WiedervorlageService {
	private static final String WIEDERVORLAGE_NOT_FOUND_MESSAGE = "Es wurde kein Wiedervorlage-Eintrag mit der ID %d gefunden.";
	private static final String PERSON_NOT_FOUND_MESSAGE = "Der angegebene %s ist ungültig oder existiert nicht.";
	private static final String BENUTZERGRUPPE_INVALID_MESSAGE = "Die Benutzergruppe mit der ID %d ist ungültig.";
	private static final String BENUTZERGRUPPE_FORBIDDEN_MESSAGE = "Fehlende Berechtigung für Benutzergruppe mit der ID %d";
	private static final String LOG_MESSAGE_SUCCESS = "Wiedervorlage mit id: %d Bemerkung: %s erfolgreich gelöscht";
	private static final String LOG_MESSAGE_NOT_FOUND = "Wiedervorlage mit id: %d konnte nicht gefunden werden";

	private static final ZoneId ZONE_BERLIN = ZoneId.of("Europe/Berlin");

	private final WiedervorlageCleanupService cleanupService;
	private final WiedervorlageRepository wiedervorlageRepository;
	private final BenutzergruppenMitgliedRepository benutzergruppenMitgliedRepository;
	private final BenutzergruppeRepository benutzergruppeRepository;
	private final BenutzerAllgemeinRepository benutzerAllgemeinRepository;
	private final ViewBenutzerDetailsRepository viewBenutzerDetailsRepository;
	private final LehrerRepository lehrerRepository;
	private final SchuelerRepository schuelerRepository;
	private final ErzieherRepository erzieherRepository;

	private final WiedervorlageMapper wiedervorlageMapper;

	/**
	 * Erstellt einen neuen WiedervorlageService mit allen benötigten Repositories.
	 *
	 * @param wiedervorlageRepository Repository für Wiedervorlage-Entitäten
	 * @param benutzergruppenMitgliedRepository Repository für Benutzergruppen
	 * @param benutzergruppeRepository Repository für Benutzergruppen
	 * @param benutzerAllgemeinRepository Repository für Benutzer Zugriff
	 * @param viewBenutzerDetailsRepository Repository für BenutzerDetails
	 * @param lehrerRepository Repository für Lehrer Zugriff
	 * @param schuelerRepository Repository für Schüler Zugriff
	 * @param erzieherRepository Repository für Erzieher Zugriff
	 * @param wiedervorlageMapper Mapper für Wiedervorlage
	 * @param cleanupService Service zur Löschung veralteter Wiedervorlagen
	 */
	@SuppressWarnings("java:S107")
	public WiedervorlageService(final WiedervorlageRepository wiedervorlageRepository,
			final BenutzergruppenMitgliedRepository benutzergruppenMitgliedRepository,
			final BenutzergruppeRepository benutzergruppeRepository,
			final BenutzerAllgemeinRepository benutzerAllgemeinRepository,
			final ViewBenutzerDetailsRepository viewBenutzerDetailsRepository,
			final LehrerRepository lehrerRepository,
			final SchuelerRepository schuelerRepository,
			final ErzieherRepository erzieherRepository,
			final WiedervorlageMapper wiedervorlageMapper,
			final WiedervorlageCleanupService cleanupService) {
		this.wiedervorlageRepository = wiedervorlageRepository;
		this.benutzergruppenMitgliedRepository = benutzergruppenMitgliedRepository;
		this.benutzergruppeRepository = benutzergruppeRepository;
		this.benutzerAllgemeinRepository = benutzerAllgemeinRepository;
		this.viewBenutzerDetailsRepository = viewBenutzerDetailsRepository;
		this.lehrerRepository = lehrerRepository;
		this.schuelerRepository = schuelerRepository;
		this.erzieherRepository = erzieherRepository;
		this.wiedervorlageMapper = wiedervorlageMapper;
		this.cleanupService = cleanupService;
	}

	/**
	 * Gibt einen einzelnen Wiedervorlage-Eintrag anhand der ID zurück.
	 *
	 * @param id die ID des Wiedervorlage-Eintrags
	 *
	 * @return der zugehörige {@link WiedervorlageEintrag}
	 */
	public WiedervorlageEintrag get(final long id) {
		final long idBenutzer = benutzerAllgemeinRepository.getAktuellerBenutzerId();
		final DTOWiedervorlage entity = getPersistedEntityByUser(id, idBenutzer);

		return toApi(entity);
	}

	/**
	 * Gibt alle Wiedervorlage-Einträge zurück, auf die der aktuelle Benutzer Zugriff hat.
	 * Abgelaufene Wiedervorlagen die zur automatischen Löschung markiert sind, werden zusätzlich synchron durch diesen Prozess gelöscht.
	 *
	 * @return Liste aller zugänglichen {@link WiedervorlageEintrag}-Objekte
	 */
	public List<WiedervorlageEintrag> getAll() {
		final long idBenutzer = benutzerAllgemeinRepository.getAktuellerBenutzerId();

		cleanupService.deleteAllExpired();

		return wiedervorlageRepository.findAllByBenutzerId(idBenutzer)
				.stream()
				.map(this::toApi)
				.toList();
	}

	private WiedervorlageEintrag toApi(final DTOWiedervorlage entity) {
		final Long idPerson;
		final String namePerson;
		switch (entity.personTyp) {
			case LEHRER -> {
				idPerson = entity.idLehrer;
				namePerson = getNameLehrerById(entity.idLehrer);
			}
			case SCHUELER -> {
				idPerson = entity.idSchueler;
				namePerson = getNameSchuelerById(entity.idSchueler);
			}
			case ERZIEHER -> {
				idPerson = entity.idErzieher;
				namePerson = getNameErzieherById(entity.idErzieher);
			}
			case null -> {
				idPerson = null;
				namePerson = null;
			}
		}

		final String nameBenutzerAngelegt = getNameBenutzerById(entity.idBenutzer);
		final String nameBenutzerErledigt = getNameBenutzerById(entity.idBenutzerErledigt);

		return wiedervorlageMapper.toApi(entity, idPerson, namePerson, nameBenutzerAngelegt, nameBenutzerErledigt);
	}

	/**
	 * Erstellt einen neuen Wiedervorlage-Eintrag.
	 *
	 * @param request das typsichere Create-Request-DTO
	 *
	 * @return der neu erstellte {@link WiedervorlageEintrag}
	 */
	public WiedervorlageEintrag create(final WiedervorlageCreateRequest request) {
		return TransactionSupport.transactional(() -> {
			final long idBenutzer = benutzerAllgemeinRepository.getAktuellerBenutzerId();
			validateBenutzerGruppe(request.idBenutzergruppe, idBenutzer, idBenutzer);

			final DTOWiedervorlage entity = wiedervorlageMapper.createDomain(request, idBenutzer);
			saveThrowing(entity);
			return toApi(entity);
		});
	}

	/**
	 * Aktualisiert einen bestehenden Wiedervorlage-Eintrag partiell.
	 *
	 * @param request das typsichere Patch-Request-DTO
	 * @param id      die ID des zu aktualisierenden Eintrags
	 *
	 * @return der aktualisierte {@link WiedervorlageEintrag}
	 */
	public WiedervorlageEintrag patch(final WiedervorlagePatchRequest request, final long id) {
		return TransactionSupport.transactional(() -> {
			final long idBenutzer = benutzerAllgemeinRepository.getAktuellerBenutzerId();
			final DTOWiedervorlage persisted = getPersistedEntityByUser(id, idBenutzer);

			wiedervorlageMapper.patch(request, persisted);

			validateBenutzerGruppe(persisted.idBenutzergruppe, persisted.idBenutzer, idBenutzer);

			return toApi(persisted);
		});
	}

	/**
	 * Löscht einen Wiedervorlage-Eintrag anhand der ID.
	 *
	 * @param id die ID des zu löschenden Eintrags
	 *
	 * @return {@link SimpleOperationResponse} Status Informationen
	 */
	public SimpleOperationResponse delete(final long id) {
		return delete(Set.of(id)).stream()
				.findFirst()
				.orElse(null);
	}

	/**
	 * Löscht einen Wiedervorlage-Eintrag anhand der ID.
	 *
	 * @param ids die IDs des zu löschenden Eintrags
	 *
	 * @return Liste von {@link SimpleOperationResponse} Status Informationen
	 */
	public List<SimpleOperationResponse> delete(final Set<Long> ids) {
		if (ids.isEmpty()) {
			return Collections.emptyList();
		}
		final long idBenutzer = benutzerAllgemeinRepository.getAktuellerBenutzerId();

		final List<DTOWiedervorlage> assignedWiedervorlagen = wiedervorlageRepository.findAllByIdsAndBenutzerId(ids, idBenutzer);
		final List<SimpleOperationResponse> responses = buildLogsForWiedervorlagen(assignedWiedervorlagen, ids);

		wiedervorlageRepository.deleteByIds(ids);

		return responses;

	}

	private List<SimpleOperationResponse> buildLogsForWiedervorlagen(final List<DTOWiedervorlage> assignedWiedervorlagen, final Set<Long> ids) {
		final List<Long> assignedIds = assignedWiedervorlagen.stream()
				.map(w -> w.id)
				.toList();

		final List<SimpleOperationResponse> validOperationResponses = assignedWiedervorlagen
				.stream()
				.map(w -> createOperationResponse(w.id, true, String.format(LOG_MESSAGE_SUCCESS, w.id, w.bemerkung)))
				.toList();
		final List<SimpleOperationResponse> notFoundOperationResponses = ids.stream()
				.filter(id -> !assignedIds.contains(id))
				.map(id -> createOperationResponse(id, false, String.format(LOG_MESSAGE_NOT_FOUND, id)))
				.toList();

		final List<SimpleOperationResponse> responses = new ArrayList<>(validOperationResponses);
		responses.addAll(notFoundOperationResponses);

		return responses;
	}

	private static SimpleOperationResponse createOperationResponse(final long id, final boolean success, final String log) {
		final var operationResponse = new SimpleOperationResponse();
		operationResponse.id = id;
		operationResponse.success = success;
		operationResponse.log.add(log);

		return operationResponse;
	}

	/**
	 * Markiert einen Wiedervorlage-Eintrag als erledigt.
	 *
	 * @param id die ID des als erledigt zu markierenden Eintrags
	 *
	 * @return der aktualisierte {@link WiedervorlageEintrag}
	 */
	public WiedervorlageEintrag markiereAlsErledigt(final long id) {
		return TransactionSupport.transactional(() -> {
			final long idUser = benutzerAllgemeinRepository.getAktuellerBenutzerId();
			final DTOWiedervorlage wiedervorlage = getPersistedEntityByUser(id, idUser);

			wiedervorlage.idBenutzerErledigt = idUser;
			wiedervorlage.tsErledigt = JSONMapper.tsFormatter.format(ZonedDateTime.now(ZONE_BERLIN));

			return toApi(wiedervorlage);
		});
	}

	/**
	 * Gibt die Anzahl offener Wiedervorlagen des heutigen Datums zurück
	 *
	 * @return Anzahl offener Wiedervorlagen als Int
	 */
	public long getAnzahlOffeneWiedervorlagen() {
		final var idBenutzer = benutzerAllgemeinRepository.getAktuellerBenutzerId();

		return wiedervorlageRepository.getAnzahlOffeneWiedervorlagen(idBenutzer);
	}

	/**
	 * Führt eine Datenbankoperation aus und wandelt eine {@link PersistenceException}
	 * (z.B. FK-Constraint-Violation) in eine sprechende {@link ApiOperationException} um.
	 *
	 * @param dto die auszuführende Operation
	 *
	 * @throws ApiOperationException falls eine FK-Constraint-Violation auftritt
	 */
	private void saveThrowing(final DTOWiedervorlage dto) throws ApiOperationException {
		try {
			wiedervorlageRepository.create(dto);
			wiedervorlageRepository.flush();
		} catch (final PersistenceException e) {
			final String bezeichnung = (dto.personTyp != null) ? dto.personTyp.bezeichnung : "Eintrag";
			throw new ApiOperationException(Status.CONFLICT,
					String.format(PERSON_NOT_FOUND_MESSAGE, bezeichnung));
		}
	}

	private void validateBenutzerGruppe(final Long idWiedervorlageBenutzerGruppe, final long idWiedervorlageBenutzer, final long idLoggedInBenutzer) {
		if (idWiedervorlageBenutzerGruppe == null) {
			return;
		}

		benutzergruppeRepository.findById(idWiedervorlageBenutzerGruppe)
				.orElseThrow(() -> new ApiOperationException(Status.BAD_REQUEST,
						BENUTZERGRUPPE_INVALID_MESSAGE.formatted(idWiedervorlageBenutzerGruppe)));
		//falls der eingeloggte nutzer die vorlage erstellt hat → zuordnung zu jeder existenten gruppe valide
		if (idWiedervorlageBenutzer == idLoggedInBenutzer) {
			return;
		}

		if (!benutzergruppenMitgliedRepository.hasGroupRights(idLoggedInBenutzer, idWiedervorlageBenutzerGruppe)) {
			throw new ApiOperationException(Status.FORBIDDEN, BENUTZERGRUPPE_FORBIDDEN_MESSAGE.formatted(idWiedervorlageBenutzerGruppe));
		}
	}

	private DTOWiedervorlage getPersistedEntityByUser(final long idWiedervorlage, final long idUser) {
		return wiedervorlageRepository.findByIdAndBenutzerId(idWiedervorlage, idUser)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND,
						WIEDERVORLAGE_NOT_FOUND_MESSAGE.formatted(idWiedervorlage)));
	}

	private String getNameErzieherById(final Long idErzieher) {
		final var erzieher = erzieherRepository.findById(idErzieher)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, String.format("Es wurde kein Erzieher zur ID %d gefunden.", idErzieher)));
		return getFullName(erzieher.Vorname1, erzieher.Name1);
	}

	private String getNameSchuelerById(final Long idSchueler) {
		final var schueler = schuelerRepository.findById(idSchueler)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, String.format("Es wurde kein Schueler zur ID %d gefunden.", idSchueler)));
		return getFullName(schueler.Vorname, schueler.Nachname);
	}

	private String getNameLehrerById(final Long idLehrer) {
		final var lehrer = lehrerRepository.findById(idLehrer)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, String.format("Es wurde keine Lehrkraft zur ID %d gefunden.", idLehrer)));
		return getFullName(lehrer.Vorname, lehrer.Nachname);
	}

	private String getNameBenutzerById(final Long idBenutzer) {
		if (idBenutzer == null) {
			return null;
		}

		return viewBenutzerDetailsRepository.findById(idBenutzer)
				.map(b -> b.AnzeigeName)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, String.format("Es wurde kein Benutzer zur ID %d gefunden.", idBenutzer)));
	}

	private static String getFullName(final String firstName, final String lastName) {
		return String.format("%s %s", firstName, lastName);
	}
}
