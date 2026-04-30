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
import de.svws_nrw.repo.benutzer.BenutzerRepository;
import de.svws_nrw.repo.benutzer.BenutzergruppeRepository;
import de.svws_nrw.repo.benutzer.BenutzergruppenMitgliedRepository;
import de.svws_nrw.repo.wiedervorlage.WiedervorlageRepository;
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

	private final WiedervorlageRepository wiedervorlageRepository;
	private final BenutzergruppenMitgliedRepository benutzergruppenMitgliedRepository;
	private final BenutzergruppeRepository benutzergruppeRepository;
	private final BenutzerRepository benutzerRepository;

	private final WiedervorlageMapper wiedervorlageMapper;

	/**
	 * Erstellt einen neuen WiedervorlageService mit allen benötigten Repositories.
	 *
	 * @param wiedervorlageRepository   Repository für Wiedervorlage-Entitäten
	 * @param benutzergruppenMitgliedRepository Repository für Benutzergruppen
	 * @param benutzergruppeRepository  Repository für Benutzergruppen
	 * @param benutzerRepository        "Repo" für User Zugriff
	 * @param wiedervorlageMapper       Mapper Wiedervorlage
	 */
	public WiedervorlageService(
			final WiedervorlageRepository wiedervorlageRepository,
			final BenutzergruppenMitgliedRepository benutzergruppenMitgliedRepository,
			final BenutzergruppeRepository benutzergruppeRepository,
			final BenutzerRepository benutzerRepository,
			final WiedervorlageMapper wiedervorlageMapper) {
		this.wiedervorlageRepository = wiedervorlageRepository;
		this.benutzergruppenMitgliedRepository = benutzergruppenMitgliedRepository;
		this.benutzergruppeRepository = benutzergruppeRepository;
		this.benutzerRepository = benutzerRepository;
		this.wiedervorlageMapper = wiedervorlageMapper;
	}

	/**
	 * Gibt einen einzelnen Wiedervorlage-Eintrag anhand der ID zurück.
	 *
	 * @param id die ID des Wiedervorlage-Eintrags
	 *
	 * @return der zugehörige {@link WiedervorlageEintrag}
	 */
	public WiedervorlageEintrag get(final long id) {
		final long idBenutzer = benutzerRepository.getAktuellerBenutzerId();
		final DTOWiedervorlage entity = getPersistedByUser(id, idBenutzer);

		return wiedervorlageMapper.toApi(entity);
	}

	/**
	 * Gibt alle Wiedervorlage-Einträge zurück, auf die der aktuelle Benutzer Zugriff hat.
	 *
	 * @return Liste aller zugänglichen {@link WiedervorlageEintrag}-Objekte
	 */
	public List<WiedervorlageEintrag> getAll() {
		final long idBenutzer = benutzerRepository.getAktuellerBenutzerId();

		return wiedervorlageRepository.findAllByBenutzerId(idBenutzer)
				.stream()
				.map(wiedervorlageMapper::toApi)
				.toList();
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
			final long idBenutzer = benutzerRepository.getAktuellerBenutzerId();
			validateBenutzerGruppe(request.idBenutzergruppe, idBenutzer, idBenutzer);

			final DTOWiedervorlage entity = wiedervorlageMapper.createDomain(request, idBenutzer);
			saveThrowing(entity);
			return wiedervorlageMapper.toApi(entity);
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
			final long idBenutzer = benutzerRepository.getAktuellerBenutzerId();
			final DTOWiedervorlage persisted = getPersistedByUser(id, idBenutzer);

			wiedervorlageMapper.patch(request, persisted);

			validateBenutzerGruppe(persisted.idBenutzergruppe, persisted.idBenutzer, idBenutzer);

			return wiedervorlageMapper.toApi(persisted);
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
		final long idBenutzer = benutzerRepository.getAktuellerBenutzerId();

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
			final long idUser = benutzerRepository.getAktuellerBenutzerId();
			final DTOWiedervorlage wiedervorlage = getPersistedByUser(id, idUser);

			wiedervorlage.idBenutzerErledigt = idUser;
			wiedervorlage.tsErledigt = JSONMapper.tsFormatter.format(ZonedDateTime.now(ZoneId.of("Europe/Berlin")));

			return wiedervorlageMapper.toApi(wiedervorlage);
		});
	}

	/**
	 * Gibt die Anzahl offener Wiedervorlagen des heutigen Datums zurück
	 *
	 * @return Anzahl offener Wiedervorlagen als Int
	 */
	public long getAnzahlOffeneWiedervorlagen() {
		final var idBenutzer = benutzerRepository.getAktuellerBenutzerId();

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

	private DTOWiedervorlage getPersistedByUser(final long idWiedervorlage, final long idUser) {
		return wiedervorlageRepository.findByIdAndBenutzerId(idWiedervorlage, idUser)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND,
						WIEDERVORLAGE_NOT_FOUND_MESSAGE.formatted(idWiedervorlage)));
	}
}
