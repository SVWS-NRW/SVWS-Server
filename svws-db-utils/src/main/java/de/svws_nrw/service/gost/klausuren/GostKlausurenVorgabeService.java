package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenVorgaben;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenVorgabeRepository;
import jakarta.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die GOSt-Klausurvorgaben.
 */
public final class GostKlausurenVorgabeService {

	private final GostKlausurenVorgabeRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository das Repository für GOSt-Klausurvorgaben
	 */
	public GostKlausurenVorgabeService(final GostKlausurenVorgabeRepository repository) {
		this.repository = repository;
	}

	static GostKlausurvorgabe toApi(final DTOGostKlausurenVorgaben dto) {
		final var daten = new GostKlausurvorgabe();
		daten.id = dto.ID;
		daten.abiturjahrgang = dto.Abi_Jahrgang;
		daten.idFach = dto.Fach_ID;
		daten.kursart = dto.Kursart.kuerzel;
		daten.halbjahr = dto.Halbjahr.id;
		daten.quartal = dto.Quartal;
		daten.bemerkungVorgabe = dto.Bemerkungen;
		daten.auswahlzeit = dto.Auswahlzeit;
		daten.dauer = dto.Dauer;
		daten.istAudioNotwendig = dto.IstAudioNotwendig;
		daten.istVideoNotwendig = dto.IstVideoNotwendig;
		daten.istMdlPruefung = dto.IstMdlPruefung;
		daten.istGklMoeglich = dto.IstGklMoeglich;
		return daten;
	}

	static List<GostKlausurvorgabe> toApiList(final Collection<DTOGostKlausurenVorgaben> dtos) {
		return dtos.stream().map(GostKlausurenVorgabeService::toApi).toList();
	}

	/**
	 * Ermittelt die Klausurvorgabe anhand der ID.
	 *
	 * @param id die ID
	 *
	 * @return die Klausurvorgabe
	 */
	public GostKlausurvorgabe get(final long id) {
		return toApi(repository.getById(id));
	}

	/**
	 * Ermittelt die Klausurvorgaben eines Abiturjahrgangs und optional eines GOSt-Halbjahres.
	 *
	 * @param abiturjahr das Abiturjahr
	 * @param halbjahr das GOSt-Halbjahr oder ein negativer Wert für alle Halbjahre
	 * @param ganzesSchuljahr true, falls das gesamte Schuljahr geladen werden soll
	 *
	 * @return die Klausurvorgaben
	 */
	public List<GostKlausurvorgabe> getListByAbiturjahr(final int abiturjahr, final int halbjahr, final boolean ganzesSchuljahr) {
		final List<DTOGostKlausurenVorgaben> vorgaben = (halbjahr < 0)
				? repository.getListByAbiturjahr(abiturjahr)
				: repository.getListByAbiturjahrAndHalbjahre(abiturjahr,
						Arrays.asList(ganzesSchuljahr ? GostHalbjahr.fromIDorException(halbjahr).getSchuljahr()
								: new GostHalbjahr[] { GostHalbjahr.fromIDorException(halbjahr) }));
		return vorgaben.stream().map(GostKlausurenVorgabeService::toApi).toList();
	}

	/**
	 * Ermittelt die Klausurvorgaben zu den angegebenen IDs.
	 *
	 * @param ids die IDs der Klausurvorgaben
	 *
	 * @return die Klausurvorgaben
	 */
	public List<GostKlausurvorgabe> getListByIds(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für die Suche nach Klausurvorgaben müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		final List<DTOGostKlausurenVorgaben> vorgaben = repository.findListByIds(ids);
		if (vorgaben.isEmpty() && !ids.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Klausurvorgaben zu angegebenen IDs nicht gefunden.");
		}
		return vorgaben.stream().map(GostKlausurenVorgabeService::toApi).toList();
	}

	/**
	 * Erstellt eine neue Klausurvorgabe.
	 *
	 * @param createRequest der Create-Request
	 *
	 * @return die neue Klausurvorgabe
	 */
	public GostKlausurvorgabe create(final GostKlausurenVorgabeCreateRequest createRequest) {
		return transactional(() -> {
			final DTOGostKlausurenVorgaben dto = new DTOGostKlausurenVorgaben(
					-1L,
					createRequest.abiturjahrgang,
					GostKlausurenValidationUtils.checkHalbjahr(createRequest.halbjahr),
					GostKlausurenValidationUtils.checkQuartal(createRequest.quartal),
					createRequest.idFach,
					checkKursart(createRequest.kursart),
					(createRequest.dauer == null) ? 0 : createRequest.dauer,
					(createRequest.auswahlzeit == null) ? 0 : createRequest.auswahlzeit,
					createRequest.istGklMoeglich,
					createRequest.istMdlPruefung,
					createRequest.istAudioNotwendig,
					createRequest.istVideoNotwendig);
			dto.Bemerkungen = convertBemerkung(createRequest.bemerkungVorgabe);
			repository.create(dto);
			repository.flush();
			return get(dto.ID);
		});
	}

	/**
	 * Führt einen Patch auf einer Klausurvorgabe aus.
	 *
	 * @param patch der Patch
	 *
	 * @return die gepatchte Klausurvorgabe
	 */
	public GostKlausurvorgabe patch(final GostKlausurenVorgabePatchRequest patch) {
		return patchMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Führt mehrere Patches auf Klausurvorgaben aus.
	 *
	 * @param patches die Patches
	 *
	 * @return die gepatchten Klausurvorgaben
	 */
	public List<GostKlausurvorgabe> patchMultiple(final Collection<GostKlausurenVorgabePatchRequest> patches) {
		if (patches == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Patchen müssen Daten angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final List<GostKlausurvorgabe> result = new ArrayList<>();
			for (final GostKlausurenVorgabePatchRequest patch : patches) {
				if ((patch == null) || (patch.id == null)) {
					throw new ApiOperationException(Status.BAD_REQUEST, "Jeder Patch muss eine ID der Klausurvorgabe enthalten.");
				}
				final DTOGostKlausurenVorgaben dto = repository.getById(patch.id);
				applyPatch(dto, patch);
				repository.update(dto);
				result.add(toApi(dto));
			}
			repository.flush();
			return result;
		});
	}

	private static void applyPatch(final DTOGostKlausurenVorgaben dto, final GostKlausurenVorgabePatchRequest patch) {
		patch.dauer.ifPresent(val -> dto.Dauer = val);
		patch.auswahlzeit.ifPresent(val -> dto.Auswahlzeit = val);
		patch.istGklMoeglich.ifPresent(val -> dto.IstGklMoeglich = val);
		patch.istMdlPruefung.ifPresent(val -> dto.IstMdlPruefung = val);
		patch.istAudioNotwendig.ifPresent(val -> dto.IstAudioNotwendig = val);
		patch.istVideoNotwendig.ifPresent(val -> dto.IstVideoNotwendig = val);
		if (patch.bemerkungVorgabe.isPresent()) {
			dto.Bemerkungen = convertBemerkung(patch.bemerkungVorgabe.orElse(null));
		}
	}

	/**
	 * Löscht eine Klausurvorgabe.
	 *
	 * @param id die ID
	 *
	 * @return die gelöschte Klausurvorgabe
	 */
	public GostKlausurvorgabe delete(final long id) {
		return deleteMultiple(List.of(id)).getFirst();
	}

	/**
	 * Löscht mehrere Klausurvorgaben.
	 *
	 * @param ids die IDs
	 *
	 * @return die gelöschten Klausurvorgaben
	 */
	public List<GostKlausurvorgabe> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final List<DTOGostKlausurenVorgaben> entities = repository.findListByIds(ids);
			if (entities.size() != ids.size()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Es wurden nicht alle Klausurvorgaben zu den IDs gefunden (%d von %d)".formatted(entities.size(), ids.size()));
			}
			final List<GostKlausurvorgabe> result = entities.stream().map(GostKlausurenVorgabeService::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

	private static GostKursart checkKursart(final String kursart) {
		final GostKursart ka = GostKursart.fromKuerzel(kursart);
		if (ka == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Keine gültige Kursart angegeben: " + kursart);
		}
		return ka;
	}

	private static String convertBemerkung(final String value) {
		return StringUtils.trimToNull(JSONMapper.convertToString(value, true, true,
				Schema.tab_Gost_Klausuren_Vorgaben.col_Bemerkungen.datenlaenge(), "bemerkungVorgabe"));
	}

}
