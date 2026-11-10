package de.svws_nrw.service.gost;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.svws_nrw.core.data.gost.GostZulassungSimulation;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholung;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.gost.GostSchuelerRepository;
import de.svws_nrw.repo.gost.GostSchuelerWiederholungRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den Stand der Simulation der Abiturzulassung der Gymnasialen Oberstufe.
 */
public class GostZulassungSimulationService {

	private final BenutzerAllgemeinRepository benutzerRepository;
	private final SchuelerRepository schuelerRepository;
	private final GostSchuelerRepository gostSchuelerRepository;
	private final GostSchuelerWiederholungRepository gostSchuelerWiederholungRepository;
	private final GostSchuelerService gostSchuelerService;
	private final boolean aktuellePlanung;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param benutzerRepository                   das Repository für den Zugriff auf Benutzerdaten und den angemeldeten Benutzer
	 * @param schuelerRepository                   das Repository für den Zugriff auf Schülerdaten
	 * @param gostSchuelerRepository               das Repository für den Zugriff auf die Schülerdaten der gymnasialen Oberstufe
	 * @param gostSchuelerWiederholungRepository   das Repository für den Zugriff auf die Wiederholungen der gymnasialen Oberstufe
	 * @param gostSchuelerService                  der Service für den Zugriff auf die Schülerdaten der gymnasialen Oberstufe
	 * @param aktuellePlanung                      true, wenn der Service mit den Daten der aktuellen Planung arbeitet
	 */
	public GostZulassungSimulationService(final BenutzerAllgemeinRepository benutzerRepository,
			final SchuelerRepository schuelerRepository,
			final GostSchuelerRepository gostSchuelerRepository,
			final GostSchuelerWiederholungRepository gostSchuelerWiederholungRepository,
			final GostSchuelerService gostSchuelerService,
			final boolean aktuellePlanung) {
		this.benutzerRepository = benutzerRepository;
		this.schuelerRepository = schuelerRepository;
		this.gostSchuelerRepository = gostSchuelerRepository;
		this.gostSchuelerWiederholungRepository = gostSchuelerWiederholungRepository;
		this.gostSchuelerService = gostSchuelerService;
		this.aktuellePlanung = aktuellePlanung;
	}

	private void pruefeGymObUndSchueler(final long idSchueler) {
		if (!benutzerRepository.getAktuellerBenutzer().schuleHatGymOb()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Schulform der Schule hat keine gymnasiale Oberstufe.");
		}
		if (schuelerRepository.findById(idSchueler).isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
		}
	}


	/**
	 * Ermittelt den Stand der Simulation der Abiturzulassung des angegebenen Schülers.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return der Stand der Simulation
	 */
	public GostZulassungSimulation get(final long idSchueler) {
		return transactional(() -> {
			pruefeGymObUndSchueler(idSchueler);
			return getZulassungSimulation(idSchueler);
		});
	}


	/**
	 * Setzt den Stand der Simulation der Abiturzulassung des angegebenen Schülers. Ist der Wechsel einer Wiederholung
	 * bereits vollzogen, so gehen die Planungsdaten zuvor in die Laufbahnplanung über.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param simulation   der neue Stand der Simulation
	 */
	public void put(final long idSchueler, final GostZulassungSimulation simulation) {
		transactional(() -> {
			pruefeGymObUndSchueler(idSchueler);
			gostSchuelerService.uebernehmeWiederholungen(List.of(idSchueler));
			setZulassungSimulation(idSchueler, simulation);
		});
	}


	/**
	 * Gibt den Stand der Simulation der Abiturzulassung des Schülers zurück. Bei einer Wiederholung gilt außerhalb der aktuellen
	 * Planung der Stand der Wiederholung. Ohne Stand und bei einem nicht lesbaren Stand wird ein leerer Stand zurückgegeben.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return der Stand der Simulation
	 */
	private @NotNull GostZulassungSimulation getZulassungSimulation(final long idSchueler) {
		final DTOGostSchuelerWiederholung wiederholung = aktuellePlanung ? null : gostSchuelerWiederholungRepository.findById(idSchueler).orElse(null);
		if (wiederholung != null) {
			return leseZulassungSimulation(wiederholung.ZulassungSimulation);
		}
		final DTOGostSchueler gostSchueler = gostSchuelerRepository.findById(idSchueler).orElse(null);
		return leseZulassungSimulation((gostSchueler == null) ? null : gostSchueler.ZulassungSimulation);
	}


	private static @NotNull GostZulassungSimulation leseZulassungSimulation(final String json) {
		if (json == null) {
			return new GostZulassungSimulation();
		}
		try {
			final GostZulassungSimulation simulation = JSONMapper.mapper.readValue(json, GostZulassungSimulation.class);
			return (simulation == null) ? new GostZulassungSimulation() : simulation;
		} catch (final JsonProcessingException e) {
			// Der Stand besteht nur aus Spieldaten, daher soll ein nicht lesbarer Stand das Öffnen der Simulation nicht verhindern
			return new GostZulassungSimulation();
		}
	}


	/**
	 * Setzt den Stand der Simulation der Abiturzulassung des Schülers. Bei einer Wiederholung gehört er außerhalb der aktuellen
	 * Planung zur Wiederholung.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param simulation   der neue Stand der Simulation
	 */
	private void setZulassungSimulation(final long idSchueler, final @NotNull GostZulassungSimulation simulation) {
		final String json;
		try {
			json = JSONMapper.mapper.writeValueAsString(simulation);
		} catch (final JsonProcessingException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Der Stand der Simulation konnte nicht in JSON umgewandelt werden.");
		}
		final DTOGostSchuelerWiederholung wiederholung = aktuellePlanung ? null : gostSchuelerWiederholungRepository.findById(idSchueler).orElse(null);
		if (wiederholung != null) {
			wiederholung.ZulassungSimulation = json;
			gostSchuelerWiederholungRepository.update(wiederholung);
			return;
		}
		final DTOGostSchueler gostSchueler = gostSchuelerRepository.findById(idSchueler).orElse(new DTOGostSchueler(idSchueler, false));
		gostSchueler.ZulassungSimulation = json;
		gostSchuelerRepository.update(gostSchueler);
	}

}
