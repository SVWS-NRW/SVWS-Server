package de.svws_nrw.service.schueler.sprachdaten;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.data.schueler.Sprachendaten;
import de.svws_nrw.asd.data.schueler.Sprachpruefung;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.service.schueler.sprachenfolge.SchuelerSprachenfolgeService;
import de.svws_nrw.service.schueler.sprachpruefung.SchuelerSprachpruefungService;
import jakarta.ws.rs.core.Response;


/**
 * Ein Service für den Zugriff auf die Sprachendaten der Schüler
 */
public final class SchuelerSprachdatenService {

	private final BenutzerAllgemeinRepository benutzerRepository;

	private final SchuelerSprachenfolgeService schuelerSprachenfolgeService;
	private final SchuelerSprachpruefungService schuelerSprachpruefungService;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param benutzerRepository                das Repository für den Zugriff auf Benutzerdaten und den angemeldeten Benutzer
	 * @param schuelerSprachenfolgeService      der Service für die Sprachenfolge
	 * @param schuelerSprachpruefungService   der Service für die Sprachprüfungen
	 */
	public SchuelerSprachdatenService(final BenutzerAllgemeinRepository benutzerRepository,
			final SchuelerSprachenfolgeService schuelerSprachenfolgeService,
			final SchuelerSprachpruefungService schuelerSprachpruefungService) {
		this.benutzerRepository = benutzerRepository;
		this.schuelerSprachenfolgeService = schuelerSprachenfolgeService;
		this.schuelerSprachpruefungService = schuelerSprachpruefungService;
	}


	/**
	 * Ermittelt die Sprachdaten für die übergebene Schüler-ID.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Sprachdaten
	 */
	public Sprachendaten get(final long idSchueler) {
		final var list = getList(List.of(idSchueler));
		if (list.isEmpty()) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde kein Eintrag mit der Schüler-ID %d gefunden.".formatted(idSchueler));
		}
		return list.getFirst();
	}


	/**
	 * Ermittelt eine Liste von Sprachdaten für die übergebene Schüler-IDs.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Liste mit den Sprachdaten der Schüler
	 */
	public List<Sprachendaten> getList(final Collection<Long> idsSchueler) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty())) {
			return Collections.emptyList();
		}

		final Schuljahresabschnitt schuljahresabschnitt = benutzerRepository.getAktuellerBenutzer().schuleGetSchuljahresabschnitt();
		final Map<Long, List<Sprachbelegung>> mapSprachenfolgen = schuelerSprachenfolgeService.getMapSprachenfolgen(idsSchueler);
		final Map<Long, List<Sprachpruefung>> mapSprachenpruefungen = schuelerSprachpruefungService.getMapSprachenfolgen(idsSchueler, schuljahresabschnitt);

		final List<Sprachendaten> result = new ArrayList<>();
		for (final Long idSchueler : idsSchueler) {
			if (idSchueler == null) {
				continue;
			}
			final List<Sprachbelegung> sprachbelegung = mapSprachenfolgen.getOrDefault(idSchueler, new ArrayList<>());
			final List<Sprachpruefung> sprachpruefung = mapSprachenpruefungen.getOrDefault(idSchueler, new ArrayList<>());

			final Sprachendaten sprachendaten = new Sprachendaten();
			sprachendaten.schuelerID = idSchueler;
			sprachendaten.belegungen.addAll(sprachbelegung.stream().filter(b -> b.belegungVonJahrgang != null).toList());
			sprachendaten.pruefungen.addAll(sprachpruefung.stream().filter(
					p -> (p.sprache != null) && (!p.sprache.isBlank() && (p.anspruchsniveauId != null) && (p.istHSUPruefung || p.istFeststellungspruefung)))
					.toList());
			result.add(sprachendaten);
		}
		return result;
	}


	/**
	 * Erstellt eine Map für die Sprachdaten der Schüler mit den übergebenen Schüler-IDs und ordnet diese den IDs zu.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Map mit den Sprachdaten, jeweils zugeordnet zu ihrer Schüler-ID
	 */
	public Map<Long, Sprachendaten> getMap(final Collection<Long> idsSchueler) {
		return this.getList(idsSchueler).stream().collect(Collectors.toMap(s -> s.schuelerID, s -> s));
	}

}
