package de.svws_nrw.service.uv.lehrer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAnrechnungsstunde;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerEntlastungsstunde;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerMehrleistung;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.uv.DTOUvLehrerAnrechnungsstunden;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.lehrer.anrechnung.LehrerAnrechnungRepository;
import de.svws_nrw.repo.lehrer.mehrleistung.LehrerMehrleistungRepository;
import de.svws_nrw.repo.lehrer.minderleistung.LehrerMinderleistungRepository;
import de.svws_nrw.repo.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.uv.lehrer.UvLehrerAnrechnungsstundenRepository;
import de.svws_nrw.repo.uv.lehrer.UvLehrerRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service, der die Anrechnungs-, Mehrleistungs- und Entlastungsstunden eines Lehrers
 * aus den Schild-Lehrerabschnittsdaten in die Tabelle der UV-Lehrer-Anrechnungsstunden überführt.
 * <p>
 * Aufeinanderfolgende Schuljahresabschnitte mit identischer Stundenzahl je Grund werden dabei
 * zu einem gemeinsamen Gültigkeitszeitraum zusammengefasst. Lücken in der Historie führen zu
 * separaten Einträgen.
 */
public final class UvLehrerAnrechnungsstundenImportService {

	private final UvLehrerAnrechnungsstundenRepository uvLehrerAnrechnungsstundenRepository;
	private final UvLehrerRepository uvLehrerRepository;
	private final LehrerPersonalabschnittsdatenRepository lehrerAbschnittsdatenRepository;
	private final LehrerAnrechnungRepository lehrerAnrechnungRepository;
	private final LehrerMehrleistungRepository lehrerMehrleistungRepository;
	private final LehrerMinderleistungRepository lehrerMinderleistungRepository;
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param uvLehrerAnrechnungsstundenRepository   Repository für die UV-Lehrer-Anrechnungsstunden
	 * @param uvLehrerRepository                     Repository für die UV-Lehrer (zur Auflösung der K_Lehrer_ID)
	 * @param lehrerAbschnittsdatenRepository        Repository für die Lehrer-Abschnittsdaten
	 * @param lehrerAnrechnungRepository             Repository für die Schild-Lehrer-Anrechnungsstunden
	 * @param lehrerMehrleistungRepository           Repository für die Schild-Lehrer-Mehrleistungen
	 * @param lehrerMinderleistungRepository         Repository für die Schild-Lehrer-Entlastungsstunden
	 * @param schuljahresabschnitteRepository        Repository für die Schuljahresabschnitte
	 */
	public UvLehrerAnrechnungsstundenImportService(
			final UvLehrerAnrechnungsstundenRepository uvLehrerAnrechnungsstundenRepository,
			final UvLehrerRepository uvLehrerRepository,
			final LehrerPersonalabschnittsdatenRepository lehrerAbschnittsdatenRepository,
			final LehrerAnrechnungRepository lehrerAnrechnungRepository,
			final LehrerMehrleistungRepository lehrerMehrleistungRepository,
			final LehrerMinderleistungRepository lehrerMinderleistungRepository,
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository) {
		this.uvLehrerAnrechnungsstundenRepository = uvLehrerAnrechnungsstundenRepository;
		this.uvLehrerRepository = uvLehrerRepository;
		this.lehrerAbschnittsdatenRepository = lehrerAbschnittsdatenRepository;
		this.lehrerAnrechnungRepository = lehrerAnrechnungRepository;
		this.lehrerMehrleistungRepository = lehrerMehrleistungRepository;
		this.lehrerMinderleistungRepository = lehrerMinderleistungRepository;
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
	}


	/**
	 * Importiert die Anrechnungsstunden für die übergebenen UV-Lehrer aus den Schild-Personalabschnittsdaten
	 * (Anrechnungen, Mehrleistungen und Entlastungen) und persistiert die zusammengefassten Einträge in der
	 * UV-Anrechnungsstunden-Tabelle.
	 * <p>
	 * Hat einer der angegebenen UV-Lehrer bereits Anrechnungsstunden-Einträge (unabhängig vom Gültigkeitszeitraum),
	 * so wird kein Import durchgeführt und stattdessen eine {@link ApiOperationException} geworfen.
	 *
	 * @param uvLehrerIds   die IDs der UV-Lehrer, für die der Import durchgeführt werden soll
	 *
	 * @return die Liste der neu erstellten {@link DTOUvLehrerAnrechnungsstunden}-Einträge
	 */
	public List<DTOUvLehrerAnrechnungsstunden> importFromPersonalabschnittsdaten(final Collection<Long> uvLehrerIds) {
		if ((uvLehrerIds == null) || uvLehrerIds.isEmpty()) {
			return List.of();
		}

		return transactional(() -> {
			// Stelle sicher, dass keiner der UV-Lehrer bereits Anrechnungsstunden besitzt
			final var bestehende = uvLehrerAnrechnungsstundenRepository.getMapByLehrerIds(uvLehrerIds);
			final List<Long> mitBestehenden = bestehende.entrySet().stream()
					.filter(e -> (e.getValue() != null) && !e.getValue().isEmpty())
					.map(Map.Entry::getKey)
					.sorted()
					.toList();
			if (!mitBestehenden.isEmpty()) {
				throw new ApiOperationException(Status.CONFLICT,
						"Für die UV-Lehrer mit den IDs %s existieren bereits Anrechnungsstunden-Einträge. Der Import wurde abgebrochen."
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

			// Lade Anrechnungen, Mehrleistungen und Entlastungen je Lehrer-Abschnitt
			final List<Long> abschnittIds = alleAbschnittsdaten.stream().map(a -> a.ID).toList();
			final Map<Long, List<DTOLehrerAnrechnungsstunde>> anrechnungenJeAbschnitt = lehrerAnrechnungRepository.getMapByAbschnitt(abschnittIds);
			final Map<Long, List<DTOLehrerMehrleistung>> mehrleistungenJeAbschnitt =
					lehrerMehrleistungRepository.getMapByIdsLehrerAbschnittsdaten(abschnittIds);
			final Map<Long, List<DTOLehrerEntlastungsstunde>> entlastungenJeAbschnitt = lehrerMinderleistungRepository.getMapByAbschnittIds(abschnittIds);

			// Erzeuge die zusammengefassten UV-Anrechnungsstunden-Einträge
			long nextId = uvLehrerAnrechnungsstundenRepository.getNextID();
			final List<DTOUvLehrerAnrechnungsstunden> neueDTOs = new ArrayList<>();
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
				final Map<String, List<Eintrag>> gruppen = gruppiereEintraegeNachGrund(
						abschnittsdaten, anrechnungenJeAbschnitt, mehrleistungenJeAbschnitt, entlastungenJeAbschnitt);
				for (final var gruppe : gruppen.entrySet()) {
					final List<DTOUvLehrerAnrechnungsstunden> gruppeDTOs =
							fasseGruppeZusammen(uvLehrerId, gruppe.getKey(), gruppe.getValue(), sjaMap, nextId);
					nextId += gruppeDTOs.size();
					neueDTOs.addAll(gruppeDTOs);
				}
			}

			uvLehrerAnrechnungsstundenRepository.update(neueDTOs);
			uvLehrerAnrechnungsstundenRepository.flush();
			return neueDTOs;
		});
	}


	/**
	 * Gruppiert die Anrechnungen, Mehrleistungen und Entlastungsstunden eines Lehrers nach ihrem Grund.
	 *
	 * @param abschnittsdaten           die Abschnittsdaten des Lehrers
	 * @param anrechnungenJeAbschnitt   alle Anrechnungen je Abschnitts-ID
	 * @param mehrleistungenJeAbschnitt alle Mehrleistungen je Abschnitts-ID
	 * @param entlastungenJeAbschnitt   alle Entlastungsstunden je Abschnitts-ID
	 *
	 * @return eine Map vom Anrechnungsgrund-Kürzel auf die Liste der zugehörigen Einträge
	 */
	private static Map<String, List<Eintrag>> gruppiereEintraegeNachGrund(
			final List<DTOLehrerAbschnittsdaten> abschnittsdaten,
			final Map<Long, List<DTOLehrerAnrechnungsstunde>> anrechnungenJeAbschnitt,
			final Map<Long, List<DTOLehrerMehrleistung>> mehrleistungenJeAbschnitt,
			final Map<Long, List<DTOLehrerEntlastungsstunde>> entlastungenJeAbschnitt) {
		final Map<String, List<Eintrag>> gruppen = new HashMap<>();
		for (final DTOLehrerAbschnittsdaten ad : abschnittsdaten) {
			for (final DTOLehrerAnrechnungsstunde a : anrechnungenJeAbschnitt.getOrDefault(ad.ID, List.of())) {
				gruppen.computeIfAbsent(a.AnrechnungsgrundKrz, k -> new ArrayList<>())
						.add(new Eintrag(ad.Schuljahresabschnitts_ID, (a.AnrechnungStd == null) ? 0.0 : a.AnrechnungStd));
			}
			for (final DTOLehrerMehrleistung m : mehrleistungenJeAbschnitt.getOrDefault(ad.ID, List.of())) {
				gruppen.computeIfAbsent(m.idGrund, k -> new ArrayList<>())
						.add(new Eintrag(ad.Schuljahresabschnitts_ID, (m.anzahl == null) ? 0.0 : m.anzahl));
			}
			for (final DTOLehrerEntlastungsstunde e : entlastungenJeAbschnitt.getOrDefault(ad.ID, List.of())) {
				gruppen.computeIfAbsent(e.entlastungsgrundKrz, k -> new ArrayList<>())
						.add(new Eintrag(ad.Schuljahresabschnitts_ID, (e.anzahl == null) ? 0.0 : -e.anzahl));
			}
		}
		return gruppen;
	}


	/**
	 * Fasst die Einträge einer Gruppe zu möglichst wenigen UV-Anrechnungsstunden-Einträgen zusammen.
	 * Aufeinanderfolgende Schuljahresabschnitte mit identischer Stundenzahl werden zu einem gemeinsamen
	 * Gültigkeitszeitraum kombiniert. Lücken in der Historie führen zu separaten Einträgen.
	 *
	 * @param uvLehrerId   die ID des UV-Lehrers
	 * @param grund        das Kürzel des Anrechnungsgrunds
	 * @param eintraege    die Einträge der Gruppe
	 * @param sjaMap       Zuordnung von Schuljahresabschnitts-ID zum DTO
	 * @param startId      die erste zu vergebende ID; weitere Einträge erhalten fortlaufend folgende IDs
	 *
	 * @return die zusammengefassten {@link DTOUvLehrerAnrechnungsstunden}-Einträge
	 */
	private static List<DTOUvLehrerAnrechnungsstunden> fasseGruppeZusammen(final long uvLehrerId, final String grund,
			final List<Eintrag> eintraege, final Map<Long, DTOSchuljahresabschnitte> sjaMap, final long startId) {
		final List<DTOUvLehrerAnrechnungsstunden> result = new ArrayList<>();
		long nextId = startId;
		final List<Eintrag> sortiert = eintraege.stream()
				.filter(e -> sjaMap.containsKey(e.sjaId))
				.sorted(Comparator.comparing((Eintrag e) -> sjaMap.get(e.sjaId).Jahr)
						.thenComparing(e -> sjaMap.get(e.sjaId).Abschnitt))
				.toList();

		DTOUvLehrerAnrechnungsstunden aktuell = null;
		Long letzteSjaId = null;
		for (final Eintrag eintrag : sortiert) {
			final DTOSchuljahresabschnitte sja = sjaMap.get(eintrag.sjaId);
			final String von = (sja.Abschnitt == 1) ? "%d-08-01".formatted(sja.Jahr) : "%d-02-01".formatted(sja.Jahr + 1);
			final String bis = (sja.Abschnitt == 1) ? "%d-01-31".formatted(sja.Jahr + 1) : "%d-07-31".formatted(sja.Jahr + 1);

			final boolean istFolgeabschnitt = (letzteSjaId != null) && letzteSjaId.equals(sja.VorigerAbschnitt_ID);
			if ((aktuell != null) && istFolgeabschnitt && (Double.compare(aktuell.AnzahlStunden, eintrag.stunden) == 0)) {
				aktuell.GueltigBis = bis;
			} else {
				aktuell = new DTOUvLehrerAnrechnungsstunden(nextId++, uvLehrerId, grund, eintrag.stunden, von);
				aktuell.GueltigBis = bis;
				result.add(aktuell);
			}
			letzteSjaId = sja.ID;
		}
		return result;
	}


	/**
	 * Hilfsklasse für einen einzelnen Eintrag während der Gruppierung der Anrechnungs-, Mehrleistungs-
	 * und Entlastungsstunden eines Lehrers.
	 *
	 * @param sjaId     die ID des Schuljahresabschnitts, zu dem der Eintrag gehört
	 * @param stunden   die Anzahl der Stunden (negativ bei Entlastungsstunden)
	 */
	private record Eintrag(long sjaId, double stunden) {
	}


}
