package de.svws_nrw.service.schule;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.utils.schule.SchuljahresabschnittsUtils;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.SchuljahresabschnittMapper;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den Zugriff auf die Schuljahresabschnitte der Schule.
 * <p>
 * Stellt Leseoperationen für Schuljahresabschnitte bereit und legt neue Schuljahresabschnitte an.
 * <p>
 * Schuljahresabschnitte werden immer nur am Anfang oder am Ende der bestehenden Kette angelegt, sodass keine Lücken in der
 * Historie entstehen. Schuljahr, Halbjahresabschnitt und die Verknüpfung mit den Nachbarabschnitten bestimmt der Service selbst.
 * Es werden keine weiteren Daten (z.B. Klassen, Lernabschnitte oder Lehrerabschnittsdaten) für den neuen Schuljahresabschnitt angelegt.
 */
public final class SchuljahresabschnittService {

	private static final String SCHULJAHRESABSCHNITT_NICHT_GEFUNDEN = "Der Schuljahresabschnitt mit der ID %d wurde nicht gefunden.";

	/** Die Anzahl der Halbjahresabschnitte pro Schuljahr. Dieser Wert ist fest vorgegeben und nicht änderbar. */
	static final int HALBJAHRESABSCHNITTE_PRO_SCHULJAHR = 2;

	/** Die maximale Anzahl an Schuljahresabschnitten, die hinter dem aktiven Schuljahresabschnitt angelegt sein dürfen. */
	static final int MAX_SCHULJAHRESABSCHNITTE_NACH_AKTIVEM = 3;

	private static final String KEINE_SCHULJAHRESABSCHNITTE = "Es existiert noch kein Schuljahresabschnitt, vor oder nach dem ein neuer Schuljahresabschnitt angelegt werden könnte.";
	private static final String KEIN_AKTIVER_SCHULJAHRESABSCHNITT = "Für die Schule ist kein aktiver Schuljahresabschnitt gesetzt.";
	private static final String AKTIVER_SCHULJAHRESABSCHNITT_NICHT_GEFUNDEN = "Der aktive Schuljahresabschnitt mit der ID %d wurde nicht gefunden.";
	private static final String UNGUELTIGE_HALBJAHRESABSCHNITTSNUMMER =
			"Der Schuljahresabschnitt mit der ID %d hat die Halbjahresabschnittsnummer %d. Erlaubt sind nur die Nummern 1 bis %d.";
	private static final String SCHULJAHRESABSCHNITT_EXISTIERT_BEREITS = "Der Schuljahresabschnitt für das Schuljahr %d und den Halbjahresabschnitt %d existiert bereits.";
	private static final String ZUKUNFTSGRENZE_ERREICHT =
			"Es dürfen höchstens %d Schuljahresabschnitte nach dem aktiven Schuljahresabschnitt angelegt werden.";

	private static final Comparator<DTOSchuljahresabschnitte> NACH_SCHULJAHR_UND_HALBJAHRESABSCHNITT =
			Comparator.<DTOSchuljahresabschnitte>comparingInt(sja -> sja.Jahr)
					.thenComparingInt(sja -> sja.Abschnitt);

	private final SchuljahresabschnitteRepository repository;
	private final EigeneSchuleRepository eigeneSchuleRepository;
	private final SchuljahresabschnittMapper mapper;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository               das Repository für die Schuljahresabschnitte
	 * @param eigeneSchuleRepository   das Repository für die Daten der eigenen Schule
	 * @param mapper                   der Mapper zur Konvertierung zwischen Datenbank-DTOs und API-Objekten
	 */
	public SchuljahresabschnittService(
			final SchuljahresabschnitteRepository repository,
			final EigeneSchuleRepository eigeneSchuleRepository,
			final SchuljahresabschnittMapper mapper
	) {
		this.repository = repository;
		this.eigeneSchuleRepository = eigeneSchuleRepository;
		this.mapper = mapper;
	}


	/**
	 * Bestimmt den Schuljahresabschnitt anhand der ID.
	 *
	 * @param id   die ID des Schuljahresabschnittes
	 *
	 * @return der Schuljahresabschnitt
	 *
	 * @throws ApiOperationException   wenn kein Schuljahresabschnitt zur ID gefunden wurde
	 */
	public Schuljahresabschnitt getById(final Long id) {
		return repository.findById(id)
				.map(mapper::toApi)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, SCHULJAHRESABSCHNITT_NICHT_GEFUNDEN.formatted(id)));
	}


	/**
	 * Bestimmt den Schuljahresabschnitt anhand von Schuljahr und Halbjahresabschnitt, sofern dieser existiert.
	 *
	 * @param schuljahr             das Schuljahr
	 * @param halbjahresabschnitt   die Nummer des Halbjahresabschnitts innerhalb des Schuljahres
	 *
	 * @return der Schuljahresabschnitt oder ein leeres Optional, falls er nicht existiert
	 */
	public Optional<Schuljahresabschnitt> findBySchuljahrAndAbschnitt(final int schuljahr, final int halbjahresabschnitt) {
		return repository.findBySchuljahrAndAbschnitt(schuljahr, halbjahresabschnitt).map(mapper::toApi);
	}


	/**
	 * Gibt eine Liste aller Schuljahresabschnitte zurück, aufsteigend sortiert nach Schuljahr und Halbjahresabschnitt.
	 *
	 * @return die sortierte Liste der Schuljahresabschnitte
	 */
	public List<Schuljahresabschnitt> getList() {
		return repository.getAll().stream()
				.map(mapper::toApi)
				.sorted(SchuljahresabschnittsUtils.comparator)
				.toList();
	}


	/**
	 * Gibt eine Liste der Schuljahresabschnitte der angegebenen Schuljahre zurück, aufsteigend sortiert nach Schuljahr und Abschnitt.
	 *
	 * @param schuljahre   die Schuljahre
	 *
	 * @return die sortierte Liste der Schuljahresabschnitte, leer wenn zu den Schuljahren keine Abschnitte existieren
	 */
	public List<Schuljahresabschnitt> getListBySchuljahre(final List<Integer> schuljahre) {
		return repository.findListBySchuljahre(schuljahre).stream()
				.map(mapper::toApi)
				.sorted(SchuljahresabschnittsUtils.comparator)
				.toList();
	}


	/**
	 * Legt den Schuljahresabschnitt direkt vor dem ersten vorhandenen Schuljahresabschnitt an. Die Operation wird in
	 * einer Transaktion ausgeführt:
	 * <ol>
	 *   <li>Der erste vorhandene Schuljahresabschnitt wird bestimmt und seine Halbjahresabschnittsnummer geprüft.</li>
	 *   <li>Der Vorgänger wird berechnet (2025/1 hat den Vorgänger 2024/2) und auf Existenz geprüft.</li>
	 *   <li>Der neue Schuljahresabschnitt wird angelegt und erhält den bisher ersten Schuljahresabschnitt als Folgeabschnitt.</li>
	 *   <li>Beim bisher ersten Schuljahresabschnitt wird der neue Schuljahresabschnitt als Vorgänger eingetragen.</li>
	 * </ol>
	 *
	 * @return der angelegte Schuljahresabschnitt
	 *
	 * @throws ApiOperationException   wenn noch kein Schuljahresabschnitt existiert, der erste Schuljahresabschnitt eine
	 *                                 ungültige Halbjahresabschnittsnummer hat oder der neue Schuljahresabschnitt bereits existiert
	 */
	public Schuljahresabschnitt createVorErstem() {
		return TransactionSupport.transactional(() -> {
			final var erster = getErstenSchuljahresabschnitt();
			validateHalbjahresabschnittsnummer(erster);
			final var neuer = create(getVorgaengerPosition(erster), null, erster.ID);
			erster.VorigerAbschnitt_ID = neuer.ID;
			repository.update(erster);
			return mapper.toApi(neuer);
		});
	}


	/**
	 * Legt den Schuljahresabschnitt direkt nach dem letzten vorhandenen Schuljahresabschnitt an. Die Operation wird in
	 * einer Transaktion ausgeführt:
	 * <ol>
	 *   <li>Der letzte vorhandene und der aktive Schuljahresabschnitt der Schule werden bestimmt und ihre Halbjahresabschnittsnummern geprüft.</li>
	 *   <li>Es wird geprüft, dass höchstens {@value #MAX_SCHULJAHRESABSCHNITTE_NACH_AKTIVEM} Schuljahresabschnitte hinter dem aktiven Schuljahresabschnitt liegen.</li>
	 *   <li>Der Nachfolger wird berechnet (2026/2 hat den Nachfolger 2027/1) und auf Existenz geprüft.</li>
	 *   <li>Der neue Schuljahresabschnitt wird angelegt und erhält den bisher letzten Schuljahresabschnitt als Vorgänger.</li>
	 *   <li>Beim bisher letzten Schuljahresabschnitt wird der neue Schuljahresabschnitt als Folgeabschnitt eingetragen.</li>
	 * </ol>
	 *
	 * @return der angelegte Schuljahresabschnitt
	 *
	 * @throws ApiOperationException   wenn noch kein Schuljahresabschnitt existiert, kein aktiver Schuljahresabschnitt gesetzt ist,
	 *                                 der letzte oder aktive Schuljahresabschnitt eine ungültige
	 *                                 Halbjahresabschnittsnummer hat, die Zukunftsgrenze erreicht ist oder der neue
	 *                                 Schuljahresabschnitt bereits existiert
	 */
	public Schuljahresabschnitt createNachLetztem() {
		return TransactionSupport.transactional(() -> {
			final var letzter = getLetztenSchuljahresabschnitt();
			final var aktiver = getAktivenSchuljahresabschnitt();

			validateHalbjahresabschnittsnummer(letzter);
			validateHalbjahresabschnittsnummer(aktiver);
			validateZukunftsgrenze(letzter, aktiver);

			final var neuer = create(getNachfolgerPosition(letzter), letzter.ID, null);
			letzter.FolgeAbschnitt_ID = neuer.ID;

			repository.update(letzter);
			return mapper.toApi(neuer);
		});
	}


	private DTOSchuljahresabschnitte getErstenSchuljahresabschnitt() {
		return repository.getAll().stream()
				.min(NACH_SCHULJAHR_UND_HALBJAHRESABSCHNITT)
				.orElseThrow(() -> new ApiOperationException(Status.BAD_REQUEST, KEINE_SCHULJAHRESABSCHNITTE));
	}


	private DTOSchuljahresabschnitte getLetztenSchuljahresabschnitt() {
		return repository.getAll().stream()
				.max(NACH_SCHULJAHR_UND_HALBJAHRESABSCHNITT)
				.orElseThrow(() -> new ApiOperationException(Status.BAD_REQUEST, KEINE_SCHULJAHRESABSCHNITTE));
	}


	private DTOSchuljahresabschnitte getAktivenSchuljahresabschnitt() {
		final var idAktiv = eigeneSchuleRepository.findFirst()
				.map(schule -> schule.Schuljahresabschnitts_ID)
				.orElseThrow(() -> new ApiOperationException(Status.BAD_REQUEST, KEIN_AKTIVER_SCHULJAHRESABSCHNITT));

		return repository.findById(idAktiv)
				.orElseThrow(() -> new ApiOperationException(Status.BAD_REQUEST, AKTIVER_SCHULJAHRESABSCHNITT_NICHT_GEFUNDEN.formatted(idAktiv)));
	}


	private static void validateHalbjahresabschnittsnummer(final DTOSchuljahresabschnitte schuljahresabschnitt) {
		if ((schuljahresabschnitt.Abschnitt < 1) || (schuljahresabschnitt.Abschnitt > HALBJAHRESABSCHNITTE_PRO_SCHULJAHR)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					UNGUELTIGE_HALBJAHRESABSCHNITTSNUMMER.formatted(schuljahresabschnitt.ID, schuljahresabschnitt.Abschnitt,
							HALBJAHRESABSCHNITTE_PRO_SCHULJAHR));
		}
	}


	private static void validateZukunftsgrenze(final DTOSchuljahresabschnitte letzter, final DTOSchuljahresabschnitte aktiver) {
		if ((getZeitachsenIndex(letzter) - getZeitachsenIndex(aktiver)) >= MAX_SCHULJAHRESABSCHNITTE_NACH_AKTIVEM) {
			throw new ApiOperationException(Status.BAD_REQUEST, ZUKUNFTSGRENZE_ERREICHT.formatted(MAX_SCHULJAHRESABSCHNITTE_NACH_AKTIVEM));
		}
	}


	// Bestimmt die fortlaufende Nummer eines Schuljahresabschnitts auf der Zeitachse aller Schuljahresabschnitte
	private static int getZeitachsenIndex(final DTOSchuljahresabschnitte schuljahresabschnitt) {
		return (schuljahresabschnitt.Jahr * HALBJAHRESABSCHNITTE_PRO_SCHULJAHR) + (schuljahresabschnitt.Abschnitt - 1);
	}


	private static Position getVorgaengerPosition(final DTOSchuljahresabschnitte schuljahresabschnitt) {
		return (schuljahresabschnitt.Abschnitt > 1)
				? new Position(schuljahresabschnitt.Jahr, schuljahresabschnitt.Abschnitt - 1)
				: new Position(schuljahresabschnitt.Jahr - 1, HALBJAHRESABSCHNITTE_PRO_SCHULJAHR);
	}


	private static Position getNachfolgerPosition(final DTOSchuljahresabschnitte schuljahresabschnitt) {
		return (schuljahresabschnitt.Abschnitt < HALBJAHRESABSCHNITTE_PRO_SCHULJAHR)
				? new Position(schuljahresabschnitt.Jahr, schuljahresabschnitt.Abschnitt + 1)
				: new Position(schuljahresabschnitt.Jahr + 1, 1);
	}


	private DTOSchuljahresabschnitte create(final Position position, final Long idVorigerAbschnitt, final Long idFolgeAbschnitt) {
		final var schuljahresabschnittExistiertBereits = repository.findBySchuljahrAndAbschnitt(position.schuljahr(), position.halbjahresabschnittsnummer())
				.isPresent();
		if (schuljahresabschnittExistiertBereits) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					SCHULJAHRESABSCHNITT_EXISTIERT_BEREITS.formatted(position.schuljahr(), position.halbjahresabschnittsnummer()));
		}
		final var neuer = new DTOSchuljahresabschnitte(0, position.schuljahr(), position.halbjahresabschnittsnummer());
		neuer.VorigerAbschnitt_ID = idVorigerAbschnitt;
		neuer.FolgeAbschnitt_ID = idFolgeAbschnitt;
		return repository.create(neuer);
	}


	// Die Lage eines Schuljahresabschnitts, bestehend aus Schuljahr und Halbjahresabschnittsnummer
	private record Position(int schuljahr, int halbjahresabschnittsnummer) {
	}

}
