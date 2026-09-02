package de.svws_nrw.service.schueler.schulbesuch;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.schueler.Einschulungsart;
import de.svws_nrw.asd.types.schueler.Hochschulabschluss;
import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.asd.types.schule.Fachklasse;
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
import org.apache.commons.lang3.StringUtils;

public final class SchuelerSchulbesuchService {

	private final SchuelerRepository repository;
	private final SchuelerMerkmalService schuelerMerkmalService;
	private final SchuelerBisherigeSchuleService schuelerBisherigeSchuleService;

	private final DataKatalogEntlassgruende dataEntlassgruende;
	private final DataSchulen dataSchulen;

	private final SchulbesuchMapper mapper;

	/**
	 * Constructor
	 *
	 * @param schuelerRepository schuelerRepository
	 * @param schuelerMerkmalService schuelerMerkmalService
	 * @param schuelerBisherigeSchuleService bisherigeSchuleService
	 * @param dataEntlassgruende dataEntlassgruende
	 * @param dataSchulen dataSchulen
	 * @param schulbesuchMapper schulbesuchMapper
	 */
	public SchuelerSchulbesuchService(
			final SchuelerRepository schuelerRepository,
			final SchuelerMerkmalService schuelerMerkmalService,
			final SchuelerBisherigeSchuleService schuelerBisherigeSchuleService,
			final DataKatalogEntlassgruende dataEntlassgruende,
			final DataSchulen dataSchulen,
			final SchulbesuchMapper schulbesuchMapper) {
		this.repository = schuelerRepository;
		this.schuelerMerkmalService = schuelerMerkmalService;
		this.schuelerBisherigeSchuleService = schuelerBisherigeSchuleService;
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
	public SchuelerSchulbesuchsdaten patch(final long idSchueler, final SchuelerSchulbesuchPatchRequest patchRequest) {
		return TransactionSupport.transactional(() -> {
			final var schueler = getSchueler(idSchueler);
			validateAndResolvePatch(schueler, patchRequest);
			mapper.patch(patchRequest, schueler);
			return map(schueler, getSchulenBySchulnummer(), getEntlassartenByBezeichnung());
		});
	}

	private void validateAndResolvePatch(final DTOSchueler entity, final SchuelerSchulbesuchPatchRequest patchRequest) {
		if (patchRequest.idVorherigeSchule.isPresent() && patchRequest.idHerkunftSonstigeVorherigeSchule.isPresent()) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"idVorherigeSchule und idHerkunftSonstigeVorherigeSchule dürfen nicht gleichzeitig gesetzt sein");
		}
		patchRequest.idVorherigeSchule.ifPresent(id -> SchuelerSchulbesuchResolver.patchVorherigeSchuleAndSchulform(entity, id, dataSchulen));
		patchRequest.idEntlassgrundVorherigeSchule.ifPresent(id -> patchLSEntlassgrund(entity, id));
		patchRequest.idEntlassgrundDieseSchule.ifPresent(id -> patchEntlassgrund(entity, id));
		patchRequest.idAufnehmendeSchule.ifPresent(id -> patchSchulwechselNr(entity, id));
		patchRequest.idEinschulungsartGrundschule.ifPresent(id -> patchEinschulungsart(entity, id));
		patchRequest.idEingangsphaseGrundschule.ifPresent(id -> patchEPJahre(entity, id));
		patchRequest.idUebergangsempfehlungGrundschule.ifPresent(id -> patchUebergangsempfehlung(entity, id));
		patchRequest.idDauerKindergartenbesuch.ifPresent(id -> patchKindergartenbesuch(entity, id));
		patchRequest.schluesselHoechsterSchulabschluss.ifPresent(schluessel -> patchHoechsterSchulabschluss(entity, schluessel));
		patchRequest.idSchulgliederungVorherigeSchule.ifPresent(id -> SchuelerSchulbesuchResolver.patchHerkunftbildungsgang(entity, id));
		patchRequest.schluesselCoreTypeFachklasseVorherigeSchule.ifPresent(schluessel -> patchFachklasse(entity, schluessel));
		patchRequest.idHerkunftSonstigeVorherigeSchule.ifPresent(id -> SchuelerSchulbesuchResolver.patchHerkunftSonstigeVorherigeSchule(entity, id));
		patchRequest.idHochschulabschluss.ifPresent(this::validateIdHochschulabschluss);
		patchAbschlussartVorherigeSchule(entity, patchRequest);
	}

	private void patchFachklasse(final DTOSchueler entity, final String schluessel) {
		if (schluessel == null) {
			entity.LSFachklKennung = null;
			entity.LSFachklSIM = null;
			return;
		}
		if (!schluessel.matches("\\d{2,3}-\\d{5}")) {
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Der Schlüssel '%s' entspricht nicht dem erwarteten Format (z.B. 'XX-XXXXX' oder 'XXX-XXXXX')."
							.formatted(schluessel));
		}
		if (Fachklasse.data().getWertBySchluessel(schluessel) == null) {
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Keine Fachklasse mit dem Schlüssel %s gefunden.".formatted(schluessel));
		}
		final var sb = new StringBuilder(schluessel);
		entity.LSFachklKennung = sb.insert(sb.length() - 2, "-").toString();
		entity.LSFachklSIM = schluessel.substring(schluessel.indexOf("-") + 1);
	}

	private void patchAbschlussartVorherigeSchule(final DTOSchueler entity, final SchuelerSchulbesuchPatchRequest patchRequest) {
		final boolean allgemeinbildendPresent = patchRequest.schluesselAbschlussartAllgemeinbildendVorherigeSchule
				.isPresent();
		final boolean berufsbildendPresent = patchRequest.schluesselAbschlussartBerufsbildendVorherigeSchule.isPresent();

		if (!allgemeinbildendPresent && !berufsbildendPresent) {
			return;
		}

		final String schluesselAllgemeinbildend = patchRequest.schluesselAbschlussartAllgemeinbildendVorherigeSchule
				.orElseGet(() -> extractAllgemeinbildend(entity.LSEntlassArt));

		if (schluesselAllgemeinbildend == null) {
			// kein berufsbildend ohne allgemeinbildend möglich
			entity.LSEntlassArt = null;
			return;
		}

		validateSchulabschlussAllgemeinbildend(schluesselAllgemeinbildend);

		final String schluesselBerufsbildend = patchRequest.schluesselAbschlussartBerufsbildendVorherigeSchule
				.orElseGet(() -> extractBerufsbildend(entity.LSEntlassArt));

		if (schluesselBerufsbildend == null) {
			entity.LSEntlassArt = schluesselAllgemeinbildend;
			return;
		}

		validateSchulabschlussBerufsbildend(schluesselBerufsbildend);
		entity.LSEntlassArt = schluesselBerufsbildend + schluesselAllgemeinbildend;
	}

	private static void validateSchulabschlussBerufsbildend(final String schluesselBerufsbildend) {
		if (SchulabschlussBerufsbildend.data().getWertBySchluessel(schluesselBerufsbildend) == null) {
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Keine berufsbildender Schulabschluss mit dem Schlüssel %s gefunden.".formatted(schluesselBerufsbildend));
		}
	}

	private static void validateSchulabschlussAllgemeinbildend(final String schluesselAllgemeinbildend) {
		if (SchulabschlussAllgemeinbildend.data().getWertBySchluessel(schluesselAllgemeinbildend) == null) {
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Keine allgemeiner Schulabschluss mit dem Schlüssel %s gefunden.".formatted(schluesselAllgemeinbildend));
		}
	}

	private String extractAllgemeinbildend(final String lsEntlassArt) {
		if ((lsEntlassArt == null) || lsEntlassArt.isBlank()) {
			return null;
		}
		return lsEntlassArt.substring(lsEntlassArt.length() - 1);
	}

	private String extractBerufsbildend(final String lsEntlassArt) {
		if ((lsEntlassArt == null) || lsEntlassArt.isBlank() || (lsEntlassArt.length() < 2)) {
			return null;
		}
		return lsEntlassArt.substring(0, 1);
	}

	private void patchHoechsterSchulabschluss(final DTOSchueler entity, final String schluessel) {
		if (schluessel == null) {
			entity.Entlassart = null;
			return;
		}
		validateSchulabschlussAllgemeinbildend(schluessel);
		entity.Entlassart = schluessel;
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

	private void validateIdHochschulabschluss(final Long idHochschulabschluss) {
		if (idHochschulabschluss == null) {
			return;
		}
		if (Hochschulabschluss.data().getEintragByID(idHochschulabschluss) == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Kein Hochschulabschluss mit der ID %d gefunden.".formatted(idHochschulabschluss));
		}
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
		final var bisherigeSchulen = this.schuelerBisherigeSchuleService.getAllByIdSchueler(schueler.ID);
		final var schuljahr = extractYearOrNull(schueler.LSSchulEntlassDatum);
		final var ctx = new SchulbesuchMappingContext(
				entlassartenByBezeichnung,
				schulenBySchulnummer,
				merkmale,
				bisherigeSchulen,
				schuljahr
		);
		return mapper.toApi(schueler, ctx);
	}

	private static Integer extractYearOrNull(final String isoDate) {
		if (StringUtils.isBlank(isoDate)) {
			return null;
		}
		try {
			return LocalDate.parse(isoDate).getYear();
		} catch (final DateTimeParseException ex) {
			return null;
		}
	}

}
