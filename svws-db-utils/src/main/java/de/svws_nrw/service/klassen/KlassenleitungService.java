package de.svws_nrw.service.klassen;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.RepositoryException;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.klassen.KlassenleitungenRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import jakarta.ws.rs.core.Response;

/**
 * Ein Service für die Klassenleitungen einer Klasse. Er ersetzt die Klassenleitungen einer Klasse durch eine neue Liste von Lehrkräften.
 * Der Service öffnet keine eigene Transaktion, diese liegt beim Aufrufer.
 */
public final class KlassenleitungService {

	private static final String KLASSE_NICHT_GEFUNDEN = "Keine Klasse zur ID %d gefunden.";
	private static final String LEHRER_IDS_UNGUELTIG = "Die Liste der Lehrer-IDs der Klassenleitungen darf nicht null sein und keine null-Einträge enthalten.";
	private static final String KLASSENLEITUNG_DOPPELT = "Es dürfen keine Lehrer-IDs doppelt in der Klassenleitungs-Liste vorhanden sein";
	private static final String KLASSENLEITUNG_LEHRER_UNBEKANNT = "Mindestens zu einer der angegebenen Lehrer-IDs ist keine Lehrkraft bekannt";
	private static final String KLASSENLEITUNG_ENTFERNEN_FEHLER = "Die Klassenleitung mit der ID %d konnte bei der Klasse mit der ID %d nicht entfernt werden.";
	private static final String KLASSENLEITUNG_AKTUALISIEREN_FEHLER = "Klassenleitung konnte nicht aktualisiert werden";
	private static final String KLASSENLEITUNG_ANLEGEN_FEHLER = "Klassenleitung konnte nicht persistiert werden";

	/** Die Reihenfolge der ersten Klassenleitung, die Reihenfolge ist 1-indiziert. */
	private static final int REIHENFOLGE_BASIS_INDEX = 1;

	private final KlassenRepository klassenRepository;
	private final KlassenleitungenRepository klassenleitungenRepository;
	private final LehrerRepository lehrerRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param klassenRepository            das Repository für die Klassen
	 * @param klassenleitungenRepository   das Repository für die Klassenleitungen
	 * @param lehrerRepository             das Repository für die Lehrkräfte
	 */
	public KlassenleitungService(final KlassenRepository klassenRepository, final KlassenleitungenRepository klassenleitungenRepository,
			final LehrerRepository lehrerRepository) {
		this.klassenRepository = klassenRepository;
		this.klassenleitungenRepository = klassenleitungenRepository;
		this.lehrerRepository = lehrerRepository;
	}

	/**
	 * Ersetzt die Klassenleitungen der Klasse durch die übergebene Liste. Die Reihenfolge ergibt sich aus der Position in der Liste
	 * (beginnend bei 1). Nicht mehr enthaltene Klassenleitungen werden gelöscht, neue angelegt und bestehende nur bei geänderter
	 * Reihenfolge aktualisiert. Eine leere Liste entfernt alle Klassenleitungen der Klasse.
	 *
	 * @param idKlasse    die ID der Klasse
	 * @param idsLehrer   die IDs der Lehrkräfte in der Reihenfolge der Klassenleitungen
	 */
	public void replace(final long idKlasse, final List<Long> idsLehrer) {
		validateReplace(idKlasse, idsLehrer);
		final var bisherige = loadBisherige(idKlasse);
		removeNichtMehrEnthaltene(idKlasse, bisherige, idsLehrer);
		saveInReihenfolge(idKlasse, bisherige, idsLehrer);
	}

	private void validateReplace(final long idKlasse, final List<Long> idsLehrer) {
		validateIdsLehrer(idsLehrer);
		validateKlasseExistiert(idKlasse);
		validateKeineDoppelten(idsLehrer);
		validateLehrerExistieren(idsLehrer);
	}

	private static void validateIdsLehrer(final List<Long> idsLehrer) {
		if ((idsLehrer == null) || idsLehrer.stream().anyMatch(Objects::isNull)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, LEHRER_IDS_UNGUELTIG);
		}
	}

	private void validateKlasseExistiert(final long idKlasse) {
		if (!klassenRepository.existsById(idKlasse)) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, KLASSE_NICHT_GEFUNDEN.formatted(idKlasse));
		}
	}

	private static void validateKeineDoppelten(final List<Long> idsLehrer) {
		if (hasDoppelteIds(idsLehrer)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KLASSENLEITUNG_DOPPELT);
		}
	}

	private static boolean hasDoppelteIds(final List<Long> ids) {
		return ids.stream().distinct().count() != ids.size();
	}

	private void validateLehrerExistieren(final List<Long> idsLehrer) {
		if (lehrerRepository.existsByIds(idsLehrer).size() != idsLehrer.size()) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KLASSENLEITUNG_LEHRER_UNBEKANNT);
		}
	}

	private Map<Long, DTOKlassenLeitung> loadBisherige(final long idKlasse) {
		return klassenleitungenRepository.findListByIdKlasse(idKlasse).stream()
				.collect(Collectors.toMap(kl -> kl.Lehrer_ID, Function.identity()));
	}

	private void removeNichtMehrEnthaltene(final long idKlasse, final Map<Long, DTOKlassenLeitung> bisherige, final List<Long> idsLehrer) {
		final var zuEntfernen = bisherige.values().stream()
				.filter(kl -> !idsLehrer.contains(kl.Lehrer_ID))
				.toList();
		zuEntfernen.forEach(kl -> removeKlassenleitung(idKlasse, kl));
		if (!zuEntfernen.isEmpty()) {
			klassenleitungenRepository.flush();
		}
	}

	private void removeKlassenleitung(final long idKlasse, final DTOKlassenLeitung klassenleitung) {
		runOrException(() -> klassenleitungenRepository.delete(klassenleitung), KLASSENLEITUNG_ENTFERNEN_FEHLER.formatted(klassenleitung.Lehrer_ID, idKlasse));
	}

	private void saveInReihenfolge(final long idKlasse, final Map<Long, DTOKlassenLeitung> bisherige, final List<Long> idsLehrer) {
		IntStream.range(0, idsLehrer.size())
				.forEach(index -> saveKlassenleitung(idKlasse, bisherige.get(idsLehrer.get(index)), idsLehrer.get(index), index + REIHENFOLGE_BASIS_INDEX));
		klassenleitungenRepository.flush();
	}

	private void saveKlassenleitung(final long idKlasse, final DTOKlassenLeitung bisherige, final long idLehrer, final int reihenfolge) {
		if (bisherige == null) {
			runOrException(() -> klassenleitungenRepository.create(new DTOKlassenLeitung(idKlasse, idLehrer, reihenfolge)), KLASSENLEITUNG_ANLEGEN_FEHLER);
		} else if (bisherige.Reihenfolge != reihenfolge) {
			bisherige.Reihenfolge = reihenfolge;
			runOrException(() -> klassenleitungenRepository.update(bisherige), KLASSENLEITUNG_AKTUALISIEREN_FEHLER);
		}
	}

	private static void runOrException(final Runnable aktion, final String meldung) {
		try {
			aktion.run();
		} catch (final RepositoryException e) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, e, meldung);
		}
	}

}
