package de.svws_nrw.service.gost.klausurplan;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenVorgaben;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.faecher.FachRepository;
import de.svws_nrw.repo.gost.GostJahrgangsdatenRepository;
import de.svws_nrw.repo.gost.klausurplan.GostKlausurenVorgabeRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf die GOSt-Klausurvorgaben.
 */
public final class GostKlausurenVorgabeService {

	private final GostKlausurenVorgabeRepository repository;
	private final GostJahrgangsdatenRepository jahrgangsdatenRepository;
	private final FachRepository fachRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository das Repository für GOSt-Klausurvorgaben
	 * @param jahrgangsdatenRepository das Repository für GOSt-Jahrgangsdaten
	 * @param fachRepository das Repository für Fächer
	 */
	public GostKlausurenVorgabeService(final GostKlausurenVorgabeRepository repository,
			final GostJahrgangsdatenRepository jahrgangsdatenRepository, final FachRepository fachRepository) {
		this.repository = repository;
		this.jahrgangsdatenRepository = jahrgangsdatenRepository;
		this.fachRepository = fachRepository;
	}

	private static GostKlausurvorgabe toApi(final DTOGostKlausurenVorgaben dto) {
		final var daten = new GostKlausurvorgabe();
		daten.id = dto.ID;
		daten.abiJahrgang = dto.Abi_Jahrgang;
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
			validateJahrgang(createRequest.abiJahrgang);
			validateFach(createRequest.idFach);
			final DTOGostKlausurenVorgaben dto = new DTOGostKlausurenVorgaben(
					repository.getNextID(),
					createRequest.abiJahrgang,
					checkHalbjahr(createRequest.halbjahr),
					checkQuartal(createRequest.quartal),
					createRequest.idFach,
					checkKursart(createRequest.kursart),
					(createRequest.dauer == null) ? 0 : createRequest.dauer,
					(createRequest.auswahlzeit == null) ? 0 : createRequest.auswahlzeit,
					createRequest.istGklMoeglich,
					createRequest.istMdlPruefung,
					createRequest.istAudioNotwendig,
					createRequest.istVideoNotwendig);
			dto.Bemerkungen = convertBemerkung(createRequest.bemerkungVorgabe);
			repository.update(dto);
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
		return transactional(() -> {
			final DTOGostKlausurenVorgaben dto = repository.getById(patch.id);
			applyPatch(dto, patch);
			repository.update(dto);
			repository.flush();
			return get(dto.ID);
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

	private void validateJahrgang(final int abiturjahr) {
		if ((abiturjahr != -1) && jahrgangsdatenRepository.findById(abiturjahr).isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Jahrgang nicht gefunden, ID: " + abiturjahr);
		}
	}

	private void validateFach(final long idFach) {
		if (fachRepository.findById(idFach).isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Fach nicht gefunden, ID: " + idFach);
		}
	}

	/**
	 * Liefert zu einer Halbjahres-ID das entsprechende GOSt-Halbjahr.
	 *
	 * @param halbjahr die Halbjahres-ID
	 *
	 * @return das GOSt-Halbjahr
	 */
	public static GostHalbjahr checkHalbjahr(final int halbjahr) {
		final GostHalbjahr hj = GostHalbjahr.fromID(halbjahr);
		if (hj == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Kein gültiges GostHalbjahr angegeben: " + halbjahr);
		}
		return hj;
	}

	/**
	 * Überprüft, ob der Wert für ein Quartal gültig ist.
	 *
	 * @param quartal das Quartal
	 *
	 * @return das Quartal
	 */
	public static int checkQuartal(final int quartal) {
		if (quartal < 0) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Quartal ungültig: " + quartal);
		}
		return quartal;
	}

	private static GostKursart checkKursart(final String kursart) {
		final GostKursart ka = GostKursart.fromKuerzel(kursart);
		if (ka == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Keine gültige Kursart angegeben: " + kursart);
		}
		return ka;
	}

	private static String convertBemerkung(final String value) {
		return DataGostKlausuren.convertEmptyStringToNull(JSONMapper.convertToString(value, true, true,
				Schema.tab_Gost_Klausuren_Vorgaben.col_Bemerkungen.datenlaenge(), "bemerkungVorgabe"));
	}

}
