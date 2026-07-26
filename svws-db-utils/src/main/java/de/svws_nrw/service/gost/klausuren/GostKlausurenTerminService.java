package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurtermin;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenTermine;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenTerminRepository;
import org.apache.commons.lang3.StringUtils;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf GOSt-Klausurtermine.
 */
public final class GostKlausurenTerminService {

	private final GostKlausurenTerminRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository das Repository für GOSt-Klausurtermine
	 */
	public GostKlausurenTerminService(final GostKlausurenTerminRepository repository) {
		this.repository = repository;
	}

	/**
	 * Wandelt ein Datenbank-DTO in ein API-DTO um.
	 *
	 * @param dto das Datenbank-DTO
	 *
	 * @return das API-DTO
	 */
	public static GostKlausurtermin toApi(final DTOGostKlausurenTermine dto) {
		final GostKlausurtermin daten = new GostKlausurtermin();
		daten.id = dto.ID;
		daten.idSchuljahresabschnitt = dto.Schuljahresabschnitt_ID;
		daten.abiturjahrgang = dto.Abi_Jahrgang;
		daten.datum = dto.Datum;
		daten.halbjahr = dto.Halbjahr.id;
		daten.quartal = dto.Quartal;
		daten.startzeit = dto.Startzeit;
		daten.bezeichnung = dto.Bezeichnung;
		daten.bemerkung = dto.Bemerkungen;
		daten.nachschreiberZugelassen = dto.NachschreiberZugelassen;
		daten.istHaupttermin = dto.IstHaupttermin;
		return daten;
	}

	/**
	 * Ermittelt einen Klausurtermin anhand der ID.
	 *
	 * @param id die ID
	 *
	 * @return der Klausurtermin
	 */
	public GostKlausurtermin get(final long id) {
		return toApi(repository.getById(id));
	}

	/**
	 * Ermittelt Klausurtermine zu den angegebenen IDs.
	 *
	 * @param ids die IDs
	 *
	 * @return die Klausurtermine
	 */
	public List<GostKlausurtermin> getListByIds(final Collection<Long> ids) {
		return repository.findListByIds(ids).stream().map(GostKlausurenTerminService::toApi).toList();
	}

	/**
	 * Ermittelt Klausurtermine an denselben Datumswerten wie die übergebenen Termine.
	 *
	 * @param terminIds die IDs der Referenztermine
	 *
	 * @return die Klausurtermine an denselben Datumswerten
	 */
	public List<GostKlausurtermin> getListByDatesOfTerminIds(final Collection<Long> terminIds) {
		final List<DTOGostKlausurenTermine> termine = repository.findListByIds(terminIds);
		return repository.getListByDatum(termine.stream().filter(t -> t.Datum != null).map(t -> t.Datum).toList()).stream()
				.map(GostKlausurenTerminService::toApi).toList();
	}

	List<GostKlausurtermin> getListByDates(final Collection<String> datumswerte) {
		return repository.getListByDatum(datumswerte).stream().map(GostKlausurenTerminService::toApi).toList();
	}

	/**
	 * Ermittelt Klausurtermine eines Abiturjahrgangs und optional zusätzlich übergebene Termin-IDs.
	 *
	 * @param abiturjahr der Abiturjahrgang
	 * @param halbjahr das GOSt-Halbjahr oder ein negativer Wert für alle Halbjahre
	 * @param ganzesSchuljahr true, falls das gesamte Schuljahr geladen werden soll
	 * @param plusTerminIds zusätzlich zu ladende Termin-IDs
	 *
	 * @return die Klausurtermine
	 */
	public List<GostKlausurtermin> getListByAbiturjahrAndHalbjahrIncludingTerminIds(final int abiturjahr, final int halbjahr, final boolean ganzesSchuljahr,
			final Collection<Long> plusTerminIds) {
		final List<DTOGostKlausurenTermine> dtos = (halbjahr < 0)
				? repository.getListByAbiturjahr(abiturjahr)
				: repository.getListByAbiturjahrAndHalbjahre(abiturjahr,
						Arrays.asList(ganzesSchuljahr ? GostHalbjahr.fromIDorException(halbjahr).getSchuljahr()
								: new GostHalbjahr[] { GostHalbjahr.fromIDorException(halbjahr) }));
		final Map<Long, GostKlausurtermin> result = new LinkedHashMap<>();
		dtos.stream().map(GostKlausurenTerminService::toApi).forEach(termin -> result.put(termin.id, termin));
		repository.findListByIds(plusTerminIds).stream().map(GostKlausurenTerminService::toApi).forEach(termin -> result.put(termin.id, termin));
		return new ArrayList<>(result.values());
	}

	/**
	 * Erstellt einen Klausurtermin.
	 *
	 * @param createRequest die Erstell-Daten
	 *
	 * @return der neue Klausurtermin
	 */
	public GostKlausurtermin create(final GostKlausurenTerminCreateRequest createRequest) {
		return transactional(() -> {
			final DTOGostKlausurenTermine dto = new DTOGostKlausurenTermine(
					-1L,
					createRequest.idSchuljahresabschnitt,
					createRequest.abiturjahrgang,
					GostKlausurenValidationUtils.checkHalbjahr(createRequest.halbjahr),
					GostKlausurenValidationUtils.checkQuartal(createRequest.quartal),
					(createRequest.istHaupttermin == null) ? true : createRequest.istHaupttermin,
					(createRequest.nachschreiberZugelassen == null) ? false : createRequest.nachschreiberZugelassen);
			applyCreateAttributes(dto, createRequest);
			repository.create(dto);
			repository.flush();
			return toApi(dto);
		});
	}

	/**
	 * Patcht ausschließlich den Klausurtermin selbst ohne fachliche Querprüfungen und ohne Raumdaten-Seiteneffekte.
	 *
	 * Package-private, damit REST-Workflows den höherwertigen {@link GostKlausurenTerminPatchService} verwenden und diese Prüfungen und Seiteneffekte
	 * nicht umgehen.
	 *
	 * @param patchRequest die Patch-Daten
	 *
	 * @return der gepatchte Klausurtermin
	 */
	GostKlausurtermin patch(final GostKlausurenTerminPatchRequest patchRequest) {
		final DTOGostKlausurenTermine dto = repository.getById(patchRequest.id);
		applyPatch(dto, patchRequest);
		repository.update(dto);
		repository.flush();
		return toApi(dto);
	}

	/**
	 * Löscht mehrere Klausurtermine.
	 *
	 * @param ids die IDs
	 *
	 * @return die gelöschten Klausurtermine
	 */
	public List<GostKlausurtermin> deleteMultiple(final Collection<Long> ids) {
		return transactional(() -> {
			final List<GostKlausurtermin> result = new ArrayList<>();
			for (final Long id : ids) {
				final DTOGostKlausurenTermine dto = repository.getById(id);
				result.add(toApi(dto));
				repository.delete(dto);
			}
			return result;
		});
	}

	/**
	 * Löscht einen Klausurtermin.
	 *
	 * @param id die ID
	 *
	 * @return der gelöschte Klausurtermin
	 */
	public GostKlausurtermin delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	private static void applyCreateAttributes(final DTOGostKlausurenTermine dto, final GostKlausurenTerminCreateRequest createRequest) {
		if (createRequest.datum.isPresent()) {
			dto.Datum = JSONMapper.convertToString(createRequest.datum.get(), true, false, null, "datum");
			if (dto.Datum == null) {
				dto.Startzeit = null;
			}
		}
		if (createRequest.startzeit.isPresent()) {
			dto.Startzeit = JSONMapper.convertToIntegerInRange(createRequest.startzeit.get(), true, 0, 1440, "startzeit");
		}
		if (createRequest.bezeichnung.isPresent()) {
			dto.Bezeichnung = StringUtils.trimToNull(JSONMapper.convertToString(createRequest.bezeichnung.get(), true, true,
					Schema.tab_Gost_Klausuren_Termine.col_Bezeichnung.datenlaenge(), "bezeichnung"));
		}
		if (createRequest.bemerkung.isPresent()) {
			dto.Bemerkungen = StringUtils.trimToNull(JSONMapper.convertToString(createRequest.bemerkung.get(), true, true,
					Schema.tab_Gost_Klausuren_Termine.col_Bemerkungen.datenlaenge(), "bemerkung"));
		}
	}

	private static void applyPatch(final DTOGostKlausurenTermine dto, final GostKlausurenTerminPatchRequest patchRequest) {
		if (patchRequest.quartal.isPresent()) {
			dto.Quartal = GostKlausurenValidationUtils.checkQuartal(patchRequest.quartal.get());
		}
		if (patchRequest.datum.isPresent()) {
			dto.Datum = JSONMapper.convertToString(patchRequest.datum.get(), true, false, null, "datum");
			if (dto.Datum == null) {
				dto.Startzeit = null;
			}
		}
		if (patchRequest.startzeit.isPresent()) {
			dto.Startzeit = JSONMapper.convertToIntegerInRange(patchRequest.startzeit.get(), true, 0, 1440, "startzeit");
		}
		if (patchRequest.bezeichnung.isPresent()) {
			dto.Bezeichnung = StringUtils.trimToNull(JSONMapper.convertToString(patchRequest.bezeichnung.get(), true, true,
					Schema.tab_Gost_Klausuren_Termine.col_Bezeichnung.datenlaenge(), "bezeichnung"));
		}
		if (patchRequest.bemerkung.isPresent()) {
			dto.Bemerkungen = StringUtils.trimToNull(JSONMapper.convertToString(patchRequest.bemerkung.get(), true, true,
					Schema.tab_Gost_Klausuren_Termine.col_Bemerkungen.datenlaenge(), "bemerkung"));
		}
		if (patchRequest.nachschreiberZugelassen.isPresent()) {
			dto.NachschreiberZugelassen = JSONMapper.convertToBoolean(patchRequest.nachschreiberZugelassen.get(), false, "nachschreiberZugelassen");
		}
	}

}
