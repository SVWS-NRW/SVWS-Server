package de.svws_nrw.service.kurse;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrerPK;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.kurse.KurslehrerMapper;
import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.kurse.KurslehrerRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den Zugriff auf die Zusatzkräfte von Kursen mit dem Core-DTO {@link KursLehrer}. Er liest, legt an,
 * ändert und löscht Zusatzkräfte. Die ID einer Zusatzkraft ist zusammengesetzt aus der ID des Kurses und der ID des Lehrers.
 */
public final class KurslehrerService {

	private static final String KEIN_KURS_GEFUNDEN = "Kein Kurs mit der ID %d gefunden.";
	private static final String KEIN_LEHRER_GEFUNDEN = "Es konnte kein Lehrer mit der ID %d gefunden werden.";
	private static final String KEIN_KURSLEHRER_GEFUNDEN = "Es existiert kein Eintrag für die Kombination aus Kurs-ID %d und Lehrer-ID %d.";
	private static final String KURSLEHRER_EXISTIERT = "Es existiert bereits ein Eintrag für die Kombination aus Kurs-ID %d und Lehrer-ID %d.";
	private static final String IDS_LEHRER_UNGUELTIG = "Die Liste der Lehrer-IDs darf nicht null sein und keine null-Werte enthalten.";
	private static final String KURSLEHRER_NICHT_GELOESCHT = "Der Eintrag für die Kombination aus Kurs-ID %d und Lehrer-ID %d wurde nicht gefunden.";

	private final KurslehrerRepository kurslehrerRepository;
	private final KurseRepository kurseRepository;
	private final LehrerRepository lehrerRepository;
	private final KurslehrerMapper mapper;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param kurslehrerRepository   das Repository für den Zugriff auf die Zusatzkräfte der Kurse
	 * @param kurseRepository        das Repository für den Zugriff auf die Kurse
	 * @param lehrerRepository       das Repository für den Zugriff auf die Lehrer
	 * @param mapper                 der Mapper für die Konvertierung der Zusatzkräfte
	 */
	public KurslehrerService(
			final KurslehrerRepository kurslehrerRepository,
			final KurseRepository kurseRepository,
			final LehrerRepository lehrerRepository,
			final KurslehrerMapper mapper) {
		this.kurslehrerRepository = kurslehrerRepository;
		this.kurseRepository = kurseRepository;
		this.lehrerRepository = lehrerRepository;
		this.mapper = mapper;
	}

	/**
	 * Bestimmt alle Zusatzkräfte eines Kurses.
	 *
	 * @param idKurs   die ID des Kurses
	 *
	 * @return die Zusatzkräfte des Kurses, leer wenn keine vorhanden sind
	 */
	public List<KursLehrer> getListByIdKurs(final long idKurs) {
		return kurslehrerRepository.getListByIdKurs(idKurs).stream()
				.map(mapper::toApi)
				.toList();
	}

	/**
	 * Legt eine Zusatzkraft bei einem Kurs an. Die Operation läuft in einer Transaktion.
	 *
	 * @param idKurs    die ID des Kurses
	 * @param request   die Daten der Zusatzkraft
	 *
	 * @return die angelegte Zusatzkraft
	 */
	public KursLehrer create(final long idKurs, final KurslehrerCreateRequest request) {
		return TransactionSupport.transactional(() -> {
			assertKursExists(idKurs);
			assertLehrerExists(request.idLehrer);
			assertKurslehrerNotExists(idKurs, request.idLehrer);
			final var entity = kurslehrerRepository.create(mapper.toDomain(idKurs, request));
			return mapper.toApi(entity);
		});
	}

	/**
	 * Ändert die im Request gesetzten Attribute einer Zusatzkraft. Die Operation läuft in einer Transaktion.
	 *
	 * @param idKurs     die ID des Kurses
	 * @param idLehrer   die ID des Lehrers
	 * @param request    die zu ändernden Attribute
	 *
	 * @return die geänderte Zusatzkraft
	 */
	public KursLehrer patch(final long idKurs, final long idLehrer, final KurslehrerPatchRequest request) {
		return TransactionSupport.transactional(() -> {
			final var entity = loadKurslehrer(idKurs, idLehrer);
			mapper.patch(request, entity);
			return mapper.toApi(entity);
		});
	}

	/**
	 * Löscht die Zusatzkräfte mit den angegebenen Lehrer-IDs bei einem Kurs. Die Operation läuft in einer Transaktion.
	 *
	 * @param idKurs      die ID des Kurses
	 * @param idsLehrer   die IDs der Lehrer, deren Zusatzkraft-Eintrag gelöscht werden soll
	 *
	 * @return je Lehrer-ID das Ergebnis der Löschoperation, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> delete(final long idKurs, final List<Long> idsLehrer) {
		assertIdsLehrerValid(idsLehrer);
		final var idsLehrerDistinct = idsLehrer.stream()
				.distinct()
				.toList();
		return TransactionSupport.transactional(() -> {
			final var entities = findKurslehrerList(idKurs, idsLehrerDistinct);
			final var idsDeleted = kurslehrerRepository.delete(entities).stream()
					.map(e -> e.Lehrer_ID)
					.collect(Collectors.toSet());
			return buildDeleteResponses(idKurs, idsLehrerDistinct, idsDeleted);
		});
	}

	private Optional<DTOKursLehrer> findKurslehrer(final long idKurs, final long idLehrer) {
		return kurslehrerRepository.findById(new DTOKursLehrerPK(idKurs, idLehrer));
	}

	private DTOKursLehrer loadKurslehrer(final long idKurs, final long idLehrer) {
		return findKurslehrer(idKurs, idLehrer)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, KEIN_KURSLEHRER_GEFUNDEN.formatted(idKurs, idLehrer)));
	}

	private List<DTOKursLehrer> findKurslehrerList(final long idKurs, final List<Long> idsLehrer) {
		final var idsGesucht = Set.copyOf(idsLehrer);
		return kurslehrerRepository.getListByIdKurs(idKurs).stream()
				.filter(e -> idsGesucht.contains(e.Lehrer_ID))
				.toList();
	}

	private static void assertIdsLehrerValid(final List<Long> idsLehrer) {
		if ((idsLehrer == null) || containsNull(idsLehrer)) {
			throw new ApiOperationException(Status.BAD_REQUEST, IDS_LEHRER_UNGUELTIG);
		}
	}

	private static boolean containsNull(final List<Long> ids) {
		return ids.stream()
				.anyMatch(Objects::isNull);
	}

	private static List<SimpleOperationResponse> buildDeleteResponses(final long idKurs, final List<Long> idsLehrer, final Set<Long> idsDeleted) {
		return idsLehrer.stream()
				.map(idLehrer -> idsDeleted.contains(idLehrer)
						? SimpleOperationResponse.ofSuccess(idLehrer)
						: SimpleOperationResponse.ofError(idLehrer, KURSLEHRER_NICHT_GELOESCHT.formatted(idKurs, idLehrer)))
				.sorted(Comparator.comparingLong(r -> r.id))
				.toList();
	}

	private void assertKursExists(final long idKurs) {
		if (kurseRepository.findById(idKurs).isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, KEIN_KURS_GEFUNDEN.formatted(idKurs));
		}
	}

	private void assertLehrerExists(final long idLehrer) {
		if (!lehrerRepository.existsById(idLehrer)) {
			throw new ApiOperationException(Status.NOT_FOUND, KEIN_LEHRER_GEFUNDEN.formatted(idLehrer));
		}
	}

	private void assertKurslehrerNotExists(final long idKurs, final long idLehrer) {
		if (findKurslehrer(idKurs, idLehrer).isPresent()) {
			throw new ApiOperationException(Status.BAD_REQUEST, KURSLEHRER_EXISTIERT.formatted(idKurs, idLehrer));
		}
	}

}
