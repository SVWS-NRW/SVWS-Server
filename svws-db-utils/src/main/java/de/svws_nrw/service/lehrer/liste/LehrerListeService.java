package de.svws_nrw.service.lehrer.liste;

import java.util.List;
import java.util.Set;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.mapper.lehrer.liste.LehrerListeEintragComparator;
import de.svws_nrw.mapper.lehrer.liste.LehrerListeEintragMapper;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import de.svws_nrw.service.utils.BulkDeleteUtils;

/**
 * Ein Service zur Erstellung der Liste der {@link LehrerListeEintrag}-Objekte für die Lehrer der Schule
 * und zum Löschen mehrerer Lehrer.
 * <p>
 * Die Liste enthält alle Lehrer und ist nach Kürzel, Nachname und Vorname sortiert. Optional wird ein Schuljahresabschnitt
 * angegeben, auf den sich die Information bezieht, ob der Lehrer aktiv ist, und optional wird ergänzt, ob der Lehrer in
 * anderen Tabellen referenziert wird.
 * </p>
 */
public final class LehrerListeService {

	private static final String ENTITY_LABEL = "Lehrer";

	private final LehrerRepository lehrerRepository;
	private final SchuljahresabschnittService schuljahresabschnittService;
	private final LehrerListeEintragMapper mapper;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param lehrerRepository              das Repository für die Lehrer
	 * @param schuljahresabschnittService   der Service für die Schuljahresabschnitte
	 * @param mapper                        der Mapper zur Erstellung der Listeneinträge
	 */
	public LehrerListeService(
			final LehrerRepository lehrerRepository,
			final SchuljahresabschnittService schuljahresabschnittService,
			final LehrerListeEintragMapper mapper) {
		this.lehrerRepository = lehrerRepository;
		this.schuljahresabschnittService = schuljahresabschnittService;
		this.mapper = mapper;
	}

	/**
	 * Erstellt die sortierte Liste aller Lehrer. Ohne Bezug zu einem Schuljahresabschnitt gelten alle Lehrer als aktiv.
	 *
	 * @param mitReferenzInfo   gibt an, ob ergänzt wird, ob der Lehrer in anderen Tabellen referenziert wird. Dies
	 *                          erfordert eine zusätzliche Datenbankabfrage.
	 *
	 * @return die sortierte Liste der Lehrer, leer wenn keine Lehrer vorhanden sind
	 */
	public List<LehrerListeEintrag> getList(final boolean mitReferenzInfo) {
		return createList(lehrerRepository.getAll(), null, mitReferenzInfo);
	}

	/**
	 * Erstellt die sortierte Liste aller Lehrer mit der Information, ob der Lehrer im Schuljahresabschnitt aktiv ist.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts, auf den sich die Information zur Aktivität bezieht
	 * @param mitReferenzInfo          gibt an, ob ergänzt wird, ob der Lehrer in anderen Tabellen referenziert wird. Dies
	 *                                 erfordert eine zusätzliche Datenbankabfrage.
	 *
	 * @return die sortierte Liste der Lehrer, leer wenn keine Lehrer vorhanden sind
	 */
	public List<LehrerListeEintrag> getListBySchuljahresabschnitt(final long idSchuljahresabschnitt, final boolean mitReferenzInfo) {
		final var abschnitt = schuljahresabschnittService.getById(idSchuljahresabschnitt);
		return createList(lehrerRepository.getAll(), abschnitt, mitReferenzInfo);
	}

	/**
	 * Löscht die Lehrer mit den übergebenen IDs. Lehrer, die in anderen Tabellen referenziert werden, und nicht
	 * gefundene IDs werden nicht gelöscht, sondern mit einer Fehlermeldung in der Rückgabe markiert.
	 *
	 * @param idsLehrer   die IDs der zu löschenden Lehrer
	 *
	 * @return die Ergebnisse der Löschvorgänge, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> delete(final List<Long> idsLehrer) {
		return TransactionSupport.transactional(
				() -> BulkDeleteUtils.deleteWithReferenceCheck(
						idsLehrer,
						lehrerRepository,
						l -> l.ID,
						ENTITY_LABEL)
		);
	}

	/**
	 * Erstellt die sortierte Liste der Einträge.
	 *
	 * @param lehrer            die Lehrer
	 * @param abschnitt         der Schuljahresabschnitt für die Aktivität oder null, wenn alle Lehrer als aktiv gelten
	 * @param mitReferenzInfo   gibt an, ob die Referenzierung in anderen Tabellen ergänzt wird
	 *
	 * @return die sortierte Liste der Einträge
	 */
	private List<LehrerListeEintrag> createList(final List<DTOLehrer> lehrer, final Schuljahresabschnitt abschnitt, final boolean mitReferenzInfo) {
		final var idsReferenziert = loadIdsReferenziert(lehrer, mitReferenzInfo);
		return lehrer.stream()
				.map(l -> createEintrag(l, abschnitt, mitReferenzInfo, idsReferenziert))
				.sorted(LehrerListeEintragComparator.INSTANCE)
				.toList();
	}

	/**
	 * Bestimmt die IDs der Lehrer, die in anderen Tabellen referenziert werden, mit einer einzigen Abfrage.
	 *
	 * @param lehrer            die Lehrer
	 * @param mitReferenzInfo   gibt an, ob die Referenzierung benötigt wird
	 *
	 * @return die IDs der referenzierten Lehrer, leer wenn die Information nicht benötigt wird
	 */
	private Set<Long> loadIdsReferenziert(final List<DTOLehrer> lehrer, final boolean mitReferenzInfo) {
		if (!mitReferenzInfo || lehrer.isEmpty()) {
			return Set.of();
		}
		final var idsLehrer = lehrer.stream()
				.map(l -> l.ID)
				.toList();
		return lehrerRepository.getReferencedIds(idsLehrer);
	}

	/**
	 * Erstellt den Eintrag für einen Lehrer.
	 *
	 * @param lehrer            der Lehrer
	 * @param abschnitt         der Schuljahresabschnitt für die Aktivität oder null, wenn der Lehrer als aktiv gilt
	 * @param mitReferenzInfo   gibt an, ob die Referenzierung in anderen Tabellen ergänzt wird
	 * @param idsReferenziert   die IDs der Lehrer, die in anderen Tabellen referenziert werden
	 *
	 * @return der Eintrag
	 */
	private LehrerListeEintrag createEintrag(final DTOLehrer lehrer, final Schuljahresabschnitt abschnitt, final boolean mitReferenzInfo,
			final Set<Long> idsReferenziert) {
		final var eintrag = mapper.toApi(lehrer);
		eintrag.istAktiv = (abschnitt == null) || LehrerAktivPruefung.isAktiv(lehrer, abschnitt);
		if (mitReferenzInfo) {
			eintrag.referenziertInAnderenTabellen = idsReferenziert.contains(lehrer.ID);
		}
		return eintrag;
	}

}
