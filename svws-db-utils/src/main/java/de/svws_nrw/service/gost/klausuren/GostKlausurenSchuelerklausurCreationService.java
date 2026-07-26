package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdaten;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenSchuelerklausurRepository;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenSchuelerklausurterminRepository;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Service für das Erzeugen mehrerer Schülerklausuren inklusive Termine.
 */
public final class GostKlausurenSchuelerklausurCreationService {

	private final GostKlausurenSchuelerklausurRepository schuelerklausurRepository;
	private final GostKlausurenSchuelerklausurterminRepository schuelerklausurterminRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuelerklausurRepository das Repository für Schülerklausuren
	 * @param schuelerklausurterminRepository das Repository für Schülerklausurtermine
	 */
	public GostKlausurenSchuelerklausurCreationService(final GostKlausurenSchuelerklausurRepository schuelerklausurRepository,
			final GostKlausurenSchuelerklausurterminRepository schuelerklausurterminRepository) {
		this.schuelerklausurRepository = schuelerklausurRepository;
		this.schuelerklausurterminRepository = schuelerklausurterminRepository;
	}

	/**
	 * Erzeugt mehrere Schülerklausuren.
	 *
	 * @param createRequests die Erstell-Daten
	 *
	 * @return die erzeugten Daten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenKlausurdaten addMultiple(final Collection<GostKlausurenSchuelerklausurCreateRequest> createRequests) throws ApiOperationException {
		return transactional(() -> addMultipleInTransaction(createRequests));
	}

	private GostKlausurenKlausurdaten addMultipleInTransaction(final Collection<GostKlausurenSchuelerklausurCreateRequest> createRequests) {
		final GostKlausurenKlausurdaten ergebnis = new GostKlausurenKlausurdaten();
		final List<DTOGostKlausurenSchuelerklausuren> schuelerklausuren = createSchuelerklausuren(createRequests);
		schuelerklausurRepository.create(schuelerklausuren);
		final List<DTOGostKlausurenSchuelerklausurenTermine> termine = createHaupttermineZuSchuelerklausuren(schuelerklausuren);
		schuelerklausurterminRepository.create(termine);
		schuelerklausurRepository.flush();
		schuelerklausurterminRepository.flush();
		ergebnis.schuelerklausuren.addAll(schuelerklausuren.stream().map(GostKlausurenSchuelerklausurService::toApi).toList());
		ergebnis.schuelerklausurtermine.addAll(termine.stream().map(GostKlausurenSchuelerklausurterminService::toApi).toList());
		return ergebnis;
	}

	private List<DTOGostKlausurenSchuelerklausuren> createSchuelerklausuren(final Collection<GostKlausurenSchuelerklausurCreateRequest> createRequests) {
		final List<DTOGostKlausurenSchuelerklausuren> result = new ArrayList<>();
		for (final GostKlausurenSchuelerklausurCreateRequest createRequest : createRequests) {
			result.add(createSchuelerklausur(createRequest));
		}
		return result;
	}

	private static DTOGostKlausurenSchuelerklausuren createSchuelerklausur(final GostKlausurenSchuelerklausurCreateRequest createRequest) {
		final DTOGostKlausurenSchuelerklausuren dto = new DTOGostKlausurenSchuelerklausuren(
				-1L,
				createRequest.idKursklausur,
				createRequest.idSchueler,
				(createRequest.aktiv == null) ? true : createRequest.aktiv);
		if (createRequest.bemerkung.isPresent()) {
			dto.Bemerkungen = JSONMapper.convertToString(createRequest.bemerkung.get(), true, true,
					Schema.tab_Gost_Klausuren_Schuelerklausuren.col_Bemerkungen.datenlaenge(), "bemerkung");
		}
		return dto;
	}

	private static List<DTOGostKlausurenSchuelerklausurenTermine> createHaupttermineZuSchuelerklausuren(
			final List<DTOGostKlausurenSchuelerklausuren> schuelerklausuren) {
		final List<DTOGostKlausurenSchuelerklausurenTermine> result = new ArrayList<>();
		for (final DTOGostKlausurenSchuelerklausuren sk : schuelerklausuren) {
			result.add(new DTOGostKlausurenSchuelerklausurenTermine(-1L, sk.ID, 0));
		}
		return result;
	}

}
