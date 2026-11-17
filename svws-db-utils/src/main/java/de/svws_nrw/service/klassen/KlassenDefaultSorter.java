package de.svws_nrw.service.klassen;

import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.klassen.KlassenMapper;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import jakarta.ws.rs.core.Response;

/**
 * Setzt die Default-Sortierung der Klassen eines Schuljahresabschnitts anhand der Sortierung der Jahrgänge und der Parallelität.
 */
final class KlassenDefaultSorter {

	private static final String KEINE_JAHRGAENGE = "Es wurden keine Jahrgänge, für das Ermitteln der Default Sortierung, gefunden.";

	private final KlassenRepository klassenRepository;
	private final JahrgangRepository jahrgangRepository;

	/**
	 * Erstellt einen neuen Sorter.
	 *
	 * @param klassenRepository    das Repository für die Klassen
	 * @param jahrgangRepository   das Repository für die Jahrgänge
	 */
	KlassenDefaultSorter(final KlassenRepository klassenRepository, final JahrgangRepository jahrgangRepository) {
		this.klassenRepository = klassenRepository;
		this.jahrgangRepository = jahrgangRepository;
	}

	/**
	 * Setzt für die Klassen des Schuljahresabschnitts die Sortierung als fortlaufende Nummer ab 1 in der Default-Reihenfolge:
	 * nach der Sortierung der Jahrgänge und danach nach der Parallelität. Klassen ohne Jahrgang oder mit einem Jahrgang ohne Sortierung
	 * stehen am Ende. Hat der Schuljahresabschnitt keine Klassen, so wird nichts geändert.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	void apply(final long idSchuljahresabschnitt) {
		final var jahrgaenge = jahrgangRepository.getMap();
		if (jahrgaenge.isEmpty()) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, KEINE_JAHRGAENGE);
		}
		final var klassen = klassenRepository.getListBySchuljahresabschnitt(idSchuljahresabschnitt);
		if (klassen.isEmpty()) {
			return;
		}

		final var sortiert = klassen.stream()
				.sorted((a, b) -> compare(a, b, jahrgaenge))
				.toList();
		IntStream.range(0, sortiert.size())
				.forEach(index -> sortiert.get(index).Sortierung = index + 1);
		klassenRepository.update(sortiert);
	}

	private static int compare(final DTOKlassen a, final DTOKlassen b, final Map<Long, DTOJahrgang> jahrgaenge) {
		final var sortierungA = getSortierungJahrgang(a, jahrgaenge);
		final var sortierungB = getSortierungJahrgang(b, jahrgaenge);
		if ((sortierungA == null) || (sortierungB == null)) {
			return Boolean.compare(sortierungA == null, sortierungB == null);
		}
		if (!sortierungA.equals(sortierungB)) {
			return Integer.compare(sortierungA, sortierungB);
		}
		return compareParallelitaet(getParallelitaet(a), getParallelitaet(b));
	}

	private static Integer getSortierungJahrgang(final DTOKlassen klasse, final Map<Long, DTOJahrgang> jahrgaenge) {
		if (klasse.Jahrgang_ID == null) {
			return null;
		}
		final var jahrgang = jahrgaenge.get(klasse.Jahrgang_ID);
		return (jahrgang == null) ? null : jahrgang.Sortierung;
	}

	private static int compareParallelitaet(final String a, final String b) {
		if (a.length() != b.length()) {
			return Integer.compare(a.length(), b.length());
		}
		return a.compareToIgnoreCase(b);
	}

	private static String getParallelitaet(final DTOKlassen klasse) {
		return Objects.requireNonNullElse(KlassenMapper.INSTANCE.mapParallelitaet(klasse.ASDKlasse), "");
	}

}
