package de.svws_nrw.service.uv.lehrer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.uv.DTOUvLehrerPflichtstundensoll;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.uv.lehrer.UvLehrerPflichtstundensollRepository;
import de.svws_nrw.repo.uv.lehrer.UvLehrerRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service, der das Pflichtstundensoll eines Lehrers aus den Schild-Lehrerabschnittsdaten in
 * die Tabelle der UV-Lehrer-Pflichtstundensoll-Einträge überführt.
 * <p>
 * Aufeinanderfolgende Schuljahresabschnitte mit identischem Pflichtstundensoll werden dabei
 * zu einem gemeinsamen Gültigkeitszeitraum zusammengefasst. Lücken in der Historie führen zu
 * separaten Einträgen.
 */
public final class UvLehrerPflichtstundensollImportService {

	private final UvLehrerPflichtstundensollRepository uvLehrerPflichtstundensollRepository;
	private final UvLehrerRepository uvLehrerRepository;
	private final LehrerPersonalabschnittsdatenRepository lehrerAbschnittsdatenRepository;
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param uvLehrerPflichtstundensollRepository   Repository für die UV-Lehrer-Pflichtstundensoll-Einträge
	 * @param uvLehrerRepository                     Repository für die UV-Lehrer (zur Auflösung der K_Lehrer_ID)
	 * @param lehrerAbschnittsdatenRepository        Repository für die Lehrer-Abschnittsdaten
	 * @param schuljahresabschnitteRepository        Repository für die Schuljahresabschnitte
	 */
	public UvLehrerPflichtstundensollImportService(
			final UvLehrerPflichtstundensollRepository uvLehrerPflichtstundensollRepository,
			final UvLehrerRepository uvLehrerRepository,
			final LehrerPersonalabschnittsdatenRepository lehrerAbschnittsdatenRepository,
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository) {
		this.uvLehrerPflichtstundensollRepository = uvLehrerPflichtstundensollRepository;
		this.uvLehrerRepository = uvLehrerRepository;
		this.lehrerAbschnittsdatenRepository = lehrerAbschnittsdatenRepository;
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
	}


	/**
	 * Importiert das Pflichtstundensoll für die übergebenen UV-Lehrer aus den Schild-Personalabschnittsdaten
	 * und persistiert die zusammengefassten Einträge in der UV-Pflichtstundensoll-Tabelle.
	 * <p>
	 * Hat einer der angegebenen UV-Lehrer bereits Pflichtstundensoll-Einträge (unabhängig vom Gültigkeitszeitraum),
	 * so wird kein Import durchgeführt und stattdessen eine {@link ApiOperationException} geworfen.
	 *
	 * @param uvLehrerIds   die IDs der UV-Lehrer, für die der Import durchgeführt werden soll
	 *
	 * @return die Liste der neu erstellten {@link DTOUvLehrerPflichtstundensoll}-Einträge
	 */
	public List<DTOUvLehrerPflichtstundensoll> importFromPersonalabschnittsdaten(final Collection<Long> uvLehrerIds) {
		if ((uvLehrerIds == null) || uvLehrerIds.isEmpty()) {
			return List.of();
		}

		return transactional(() -> {
			// Stelle sicher, dass keiner der UV-Lehrer bereits Pflichtstundensoll-Einträge besitzt
			final var bestehende = uvLehrerPflichtstundensollRepository.getMapByLehrerIds(uvLehrerIds);
			final List<Long> mitBestehenden = bestehende.entrySet().stream()
					.filter(e -> (e.getValue() != null) && !e.getValue().isEmpty())
					.map(Map.Entry::getKey)
					.sorted()
					.toList();
			if (!mitBestehenden.isEmpty()) {
				throw new ApiOperationException(Status.CONFLICT,
						"Für die UV-Lehrer mit den IDs %s existieren bereits Pflichtstundensoll-Einträge. Der Import wurde abgebrochen."
								.formatted(mitBestehenden));
			}

			// Löse die Schild-Lehrer-IDs (K_Lehrer_ID) zu den übergebenen UV-Lehrer-IDs auf
			final var uvLehrer = uvLehrerRepository.findListByIds(uvLehrerIds);
			if (uvLehrer.size() != uvLehrerIds.stream().distinct().count()) {
				throw new ApiOperationException(Status.NOT_FOUND,
						"Es wurden nicht alle angefragten UV-Lehrer gefunden.");
			}
			final Map<Long, Long> uvLehrerIdZuKLehrerId = new HashMap<>();
			for (final var l : uvLehrer) {
				uvLehrerIdZuKLehrerId.put(l.ID, l.K_Lehrer_ID);
			}
			final List<Long> kLehrerIds = uvLehrerIdZuKLehrerId.values().stream().filter(java.util.Objects::nonNull).distinct().toList();
			if (kLehrerIds.isEmpty()) {
				return List.of();
			}

			// Lade alle Lehrer-Abschnittsdaten und gruppiere sie nach Schild-Lehrer-ID
			final List<DTOLehrerAbschnittsdaten> alleAbschnittsdaten = lehrerAbschnittsdatenRepository.getListByLehrerIds(kLehrerIds);
			if (alleAbschnittsdaten.isEmpty()) {
				return List.of();
			}
			final Map<Long, List<DTOLehrerAbschnittsdaten>> abschnittsdatenJeLehrer =
					alleAbschnittsdaten.stream().collect(Collectors.groupingBy(a -> a.Lehrer_ID));

			// Lade die zugehörigen Schuljahresabschnitte
			final List<Long> sjaIds = alleAbschnittsdaten.stream().map(a -> a.Schuljahresabschnitts_ID).distinct().toList();
			final Map<Long, DTOSchuljahresabschnitte> sjaMap = schuljahresabschnitteRepository.findMapByIds(sjaIds);

			// Erzeuge die zusammengefassten UV-Pflichtstundensoll-Einträge
			long nextId = uvLehrerPflichtstundensollRepository.getNextID();
			final List<DTOUvLehrerPflichtstundensoll> neueDTOs = new ArrayList<>();
			for (final var entry : uvLehrerIdZuKLehrerId.entrySet()) {
				final Long uvLehrerId = entry.getKey();
				final Long kLehrerId = entry.getValue();
				if ((uvLehrerId == null) || (kLehrerId == null)) {
					continue;
				}
				final List<DTOLehrerAbschnittsdaten> abschnittsdaten = abschnittsdatenJeLehrer.get(kLehrerId);
				if ((abschnittsdaten == null) || abschnittsdaten.isEmpty()) {
					continue;
				}
				final List<DTOUvLehrerPflichtstundensoll> lehrerDTOs = fasseAbschnittsdatenZusammen(uvLehrerId, abschnittsdaten, sjaMap, nextId);
				nextId += lehrerDTOs.size();
				neueDTOs.addAll(lehrerDTOs);
			}

			uvLehrerPflichtstundensollRepository.update(neueDTOs);
			uvLehrerPflichtstundensollRepository.flush();
			return neueDTOs;
		});
	}


	/**
	 * Fasst die Lehrer-Abschnittsdaten eines UV-Lehrers zu möglichst wenigen UV-Pflichtstundensoll-Einträgen
	 * zusammen. Aufeinanderfolgende Schuljahresabschnitte mit identischem Pflichtstundensoll werden zu einem
	 * gemeinsamen Gültigkeitszeitraum kombiniert. Lücken in der Historie führen zu separaten Einträgen.
	 * Der zuletzt erzeugte Eintrag erhält ein offenes Gültigkeitsende ({@code GueltigBis = null}).
	 *
	 * @param uvLehrerId        die ID des UV-Lehrers
	 * @param abschnittsdaten   die Lehrer-Abschnittsdaten des Lehrers
	 * @param sjaMap            Zuordnung von Schuljahresabschnitts-ID zum DTO
	 * @param startId           die erste zu vergebende ID; weitere Einträge erhalten fortlaufend folgende IDs
	 *
	 * @return die zusammengefassten {@link DTOUvLehrerPflichtstundensoll}-Einträge
	 */
	private static List<DTOUvLehrerPflichtstundensoll> fasseAbschnittsdatenZusammen(final long uvLehrerId,
			final List<DTOLehrerAbschnittsdaten> abschnittsdaten, final Map<Long, DTOSchuljahresabschnitte> sjaMap, final long startId) {
		final List<DTOUvLehrerPflichtstundensoll> result = new ArrayList<>();
		long nextId = startId;
		final List<DTOLehrerAbschnittsdaten> sortiert = abschnittsdaten.stream()
				.filter(a -> sjaMap.containsKey(a.Schuljahresabschnitts_ID))
				.sorted(Comparator.comparing((DTOLehrerAbschnittsdaten a) -> sjaMap.get(a.Schuljahresabschnitts_ID).Jahr)
						.thenComparing(a -> sjaMap.get(a.Schuljahresabschnitts_ID).Abschnitt))
				.toList();

		DTOUvLehrerPflichtstundensoll aktuell = null;
		Long letzteSjaId = null;
		for (final DTOLehrerAbschnittsdaten ad : sortiert) {
			final DTOSchuljahresabschnitte sja = sjaMap.get(ad.Schuljahresabschnitts_ID);
			final String von = (sja.Abschnitt == 1) ? "%d-08-01".formatted(sja.Jahr) : "%d-02-01".formatted(sja.Jahr + 1);
			final String bis = (sja.Abschnitt == 1) ? "%d-01-31".formatted(sja.Jahr + 1) : "%d-07-31".formatted(sja.Jahr + 1);
			final double pflichtstdSoll = (ad.PflichtstdSoll == null) ? 0.0 : ad.PflichtstdSoll;

			final boolean istFolgeabschnitt = (letzteSjaId != null) && letzteSjaId.equals(sja.VorigerAbschnitt_ID);
			if ((aktuell != null) && istFolgeabschnitt && (Double.compare(aktuell.PflichtstdSoll, pflichtstdSoll) == 0)) {
				aktuell.GueltigBis = bis;
			} else {
				aktuell = new DTOUvLehrerPflichtstundensoll(nextId++, uvLehrerId, pflichtstdSoll, von);
				aktuell.GueltigBis = bis;
				result.add(aktuell);
			}
			letzteSjaId = sja.ID;
		}
		// Der zuletzt erzeugte Eintrag erhält ein offenes Gültigkeitsende.
		if (aktuell != null) {
			aktuell.GueltigBis = null;
		}
		return result;
	}

}
