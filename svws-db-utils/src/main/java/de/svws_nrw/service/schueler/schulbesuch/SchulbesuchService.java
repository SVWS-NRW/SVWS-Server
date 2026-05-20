package de.svws_nrw.service.schueler.schulbesuch;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.schueler.Einschulungsart;
import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.asd.types.schule.Kindergartenbesuch;
import de.svws_nrw.asd.types.schule.SchulabschlussAllgemeinbildend;
import de.svws_nrw.asd.types.schule.SchulabschlussBerufsbildend;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.data.kataloge.DataKatalogEntlassgruende;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.schulbesuch.SchulbesuchMapper;
import de.svws_nrw.mapper.schueler.schulbesuch.SchulbesuchMappingContext;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import jakarta.ws.rs.core.Response.Status;

public final class SchulbesuchService {

	private final SchuelerRepository repository;
	private final SchuelerMerkmalService schuelerMerkmalService;
	private final BisherigeSchuleService bisherigeSchuleService;

	private final DataKatalogEntlassgruende dataEntlassgruende;
	private final DataSchulen dataSchulen;

	private final SchulbesuchMapper mapper;

	/**
	 * Constructor
	 *
	 * @param schuelerRepository schuelerRepository
	 * @param schuelerMerkmalService schuelerMerkmalService
	 * @param bisherigeSchuleService bisherigeSchuleService
	 * @param dataEntlassgruende dataEntlassgruende
	 * @param dataSchulen dataSchulen
	 * @param schulbesuchMapper schulbesuchMapper
	 */
	public SchulbesuchService(
			final SchuelerRepository schuelerRepository,
			final SchuelerMerkmalService schuelerMerkmalService,
			final BisherigeSchuleService bisherigeSchuleService,
			final DataKatalogEntlassgruende dataEntlassgruende,
			final DataSchulen dataSchulen,
			final SchulbesuchMapper schulbesuchMapper) {
		this.repository = schuelerRepository;
		this.schuelerMerkmalService = schuelerMerkmalService;
		this.bisherigeSchuleService = bisherigeSchuleService;
		this.dataEntlassgruende = dataEntlassgruende;
		this.dataSchulen = dataSchulen;
		this.mapper = schulbesuchMapper;
	}

	/**
	 * Ruft alle Schulbesuch-Entitäten für den Schüler mit der gegebenen ID ab.
	 *
	 * @param id id
	 * @return alle Schulbesuch-Entitäten für den Schüler mit der gegebenen ID
	 */
	public SchuelerSchulbesuchsdaten getById(final long id) {
		final var schueler = getSchueler(id);
		return this.map(schueler, getSchulenBySchulnummer(), getEntlassartenByBezeichnung());
	}



	/**
	 * Ruft alle Schulbesuch-Entitäten für die Schüler mit den gegebenen IDs ab.
	 *
	 * @param ids ids
	 * @return alle Schulbesuch-Entitäten für die Schüler mit der gegebenen IDs
	 */
	public List<SchuelerSchulbesuchsdaten> getByIds(final List<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptyList();
		}
		final var schueler = this.repository.findListByIds(ids);
		if (schueler.isEmpty()) {
			return Collections.emptyList();
		}
		final var schulenBySchulnummer = getSchulenBySchulnummer();
		final var entlassartenByBezeichnung = getEntlassartenByBezeichnung();
		return schueler.stream()
				.map(s -> this.map(s, schulenBySchulnummer, entlassartenByBezeichnung))
				.sorted(Comparator.comparing(s -> s.id))
				.toList();
	}

	/**
	 * Aktualisiert einen bestehenden Eintrag eines Schülers teilweise (PATCH).
	 * Nur die im Request angegebenen Felder werden aktualisiert.
	 * Die Operation wird in einer Transaktion ausgeführt.
	 *
	 * @param idSchueler  die ID des zu aktualisierenden Eintrags
	 * @param patchRequest die zu aktualisierenden Felder
	 * @return der aktualisierte Eintrag als API-Modell
	 */
	public SchuelerSchulbesuchsdaten patch(final long idSchueler, final SchulbesuchPatchRequest patchRequest) {
		return TransactionSupport.transactional(() -> {
			final var schueler = getSchueler(idSchueler);
			validateAndResolvePatch(schueler, patchRequest);
			mapper.patch(patchRequest, schueler);
			return map(schueler, getSchulenBySchulnummer(), getEntlassartenByBezeichnung());
		});
	}

	private void validateAndResolvePatch(final DTOSchueler entity, final SchulbesuchPatchRequest patchRequest) {
		patchRequest.idVorherigeSchule.ifPresent(id -> patchLSSchulNr(entity, id));
		patchRequest.idEntlassgrundVorherigeSchule.ifPresent(id -> patchLSEntlassgrund(entity, id));
		patchRequest.idEntlassgrundDieseSchule.ifPresent(id -> patchEntlassgrund(entity, id));
		patchRequest.idAufnehmendeSchule.ifPresent(id -> patchSchulwechselNr(entity, id));
		patchRequest.idEinschulungsartGrundschule.ifPresent(id -> patchEinschulungsart(entity, id));
		patchRequest.idEingangsphaseGrundschule.ifPresent(id -> patchEPJahre(entity, id));
		patchRequest.idUebergangsempfehlungGrundschule.ifPresent(id -> patchUebergangsempfehlung(entity, id));
		patchRequest.idDauerKindergartenbesuch.ifPresent(id -> patchKindergartenbesuch(entity, id));
		patchRequest.schluesselHoechsterSchulabschluss.ifPresent(schluessel -> patchHoechsterSchulabschluss(entity, schluessel));
	}

	private void patchHoechsterSchulabschluss(final DTOSchueler entity, final String schluessel) {
		if (schluessel == null) {
			entity.Entlassart = null;
			return;
		}
		final var abschlussAllgemeinbildend = SchulabschlussAllgemeinbildend.data().getWertBySchluessel(schluessel);
		final var abschlussBerufsbildend = SchulabschlussBerufsbildend.data().getWertBySchluessel(schluessel);
		if ((abschlussAllgemeinbildend == null) && (abschlussBerufsbildend == null)) {
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Keine Schulabschluss mit dem Schlüssel %s gefunden.".formatted(schluessel));
		}
		entity.Entlassart = schluessel;
	}

	private void patchLSSchulNr(final DTOSchueler entity, final Long id) {
		if (id == null) {
			entity.LSSchulNr = null;
		} else {
			entity.LSSchulNr = this.dataSchulen.getEntityById(id).SchulNr;
		}
	}

	private void patchLSEntlassgrund(final DTOSchueler entity, final Long id) {
		if (id == null) {
			entity.LSEntlassgrund = null;
		} else {
			entity.LSEntlassgrund = this.dataEntlassgruende.getEntityById(id).Bezeichnung;
		}
	}

	private void patchEntlassgrund(final DTOSchueler entity, final Long id) {
		if (id == null) {
			entity.Entlassgrund = null;
		} else {
			entity.Entlassgrund = this.dataEntlassgruende.getEntityById(id).Bezeichnung;
		}
	}

	private void patchSchulwechselNr(final DTOSchueler entity, final Long id) {
		if (id == null) {
			entity.SchulwechselNr = null;
		} else {
			entity.SchulwechselNr = this.dataSchulen.getEntityById(id).SchulNr;
		}
	}

	private void patchEinschulungsart(final DTOSchueler entity, final Long idEinschulungsart) {
		if (idEinschulungsart == null) {
			entity.EinschulungsartASD = null;
			return;
		}
		final var eintrag = Einschulungsart.data().getEintragByID(idEinschulungsart);
		if (eintrag == null) {
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Keine Einschulungsart mit der ID %d gefunden.".formatted(idEinschulungsart));
		}
		entity.EinschulungsartASD = eintrag.schluessel;
	}

	private void patchEPJahre(final DTOSchueler entity, final Long idGrundschuleJahreEingangsphase) {
		if (idGrundschuleJahreEingangsphase == null) {
			entity.EPJahre = null;
			return;
		}
		final var eintrag = PrimarstufeSchuleingangsphaseBesuchsjahre.data().getEintragByID(idGrundschuleJahreEingangsphase);
		if (eintrag == null) {
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Keine Eingangsphase mit der ID %d vorhanden.".formatted(idGrundschuleJahreEingangsphase));
		}
		entity.EPJahre = Math.toIntExact(eintrag.id);
	}

	private void patchUebergangsempfehlung(final DTOSchueler entity, final Long id) {
		if (id == null) {
			entity.Uebergangsempfehlung_JG5 = null;
			return;
		}
		final var eintrag = Uebergangsempfehlung.data().getEintragByID(id);
		if (eintrag == null) {
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Keine Übergangsempfehlung für die ID %d gefunden.".formatted(id));
		}
		entity.Uebergangsempfehlung_JG5 = eintrag.schluessel;
	}

	private void patchKindergartenbesuch(final DTOSchueler entity, final Long id) {
		if (id == null) {
			entity.DauerKindergartenbesuch = null;
			return;
		}
		final var eintrag = Kindergartenbesuch.data().getEintragByID(id);
		if (eintrag == null) {
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Kein Kindergartenbesuch mit der ID %d gefunden.".formatted(id));
		}
		entity.DauerKindergartenbesuch = eintrag.schluessel;
	}

	private DTOSchueler getSchueler(final long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Kein Schüler zur ID %d gefunden.".formatted(id)));
	}

	private Map<String, DTOSchuleNRW> getSchulenBySchulnummer() {
		return this.dataSchulen.getAllEntities().stream()
				.collect(Collectors.toMap(s -> s.SchulNr, s -> s));
	}

	private Map<String, DTOEntlassarten> getEntlassartenByBezeichnung() {
		return this.dataEntlassgruende.getAllEntities().stream()
				.collect(Collectors.toMap(e -> e.Bezeichnung, e -> e));
	}

	private SchuelerSchulbesuchsdaten map(final DTOSchueler schueler, final Map<String, DTOSchuleNRW> schulenBySchulnummer,
			final Map<String, DTOEntlassarten> entlassartenByBezeichnung) {
		final var merkmale = this.schuelerMerkmalService.getAllByIdSchueler(schueler.ID);
		final var bisherigeSchulen = this.bisherigeSchuleService.getAllByIdSchueler(schueler.ID);

		final var ctx = new SchulbesuchMappingContext(
				entlassartenByBezeichnung,
				schulenBySchulnummer,
				merkmale,
				bisherigeSchulen
		);
		return mapper.toApi(schueler, ctx);
	}


}
