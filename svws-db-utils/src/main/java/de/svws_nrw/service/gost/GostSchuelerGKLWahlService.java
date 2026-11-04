package de.svws_nrw.service.gost;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.core.data.gost.GostSchuelerGKLWahl;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenVorgabeRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für die Wahlen zu den gleichwertigen komplexen Leistungsnachweisen der Gymnasialen Oberstufe
 */
public class GostSchuelerGKLWahlService {

	private final BenutzerAllgemeinRepository benutzerRepository;
	private final SchuelerRepository schuelerRepository;
	private final GostSchuelerService gostSchuelerService;
	private final GostKlausurenVorgabeRepository gostKlausurenVorgabeRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param benutzerRepository               das Repository für den Zugriff auf Benutzerdaten und den angemeldeten Benutzer
	 * @param schuelerRepository               das Repository für den Zugriff auf Schülerdaten
	 * @param gostSchuelerService              der Service für den Zugriff auf die Schülerdaten der gymnasialen Oberstufe
	 * @param gostKlausurenVorgabeRepository   das Repository für den Zugriff auf die Klausurvorgaben der gymnasialen Oberstufe
	 */
	public GostSchuelerGKLWahlService(final BenutzerAllgemeinRepository benutzerRepository,
			final SchuelerRepository schuelerRepository,
			final GostSchuelerService gostSchuelerService,
			final GostKlausurenVorgabeRepository gostKlausurenVorgabeRepository) {
		this.benutzerRepository = benutzerRepository;
		this.schuelerRepository = schuelerRepository;
		this.gostSchuelerService = gostSchuelerService;
		this.gostKlausurenVorgabeRepository = gostKlausurenVorgabeRepository;
	}

	private void pruefeGymOb() {
		// Prüfe, ob die aktuelle Schule eine Gymnasiale Oberstufe hat oder nicht
		if (!benutzerRepository.getAktuellerBenutzer().schuleHatGymOb()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Schulform der Schule hat keine gymnasiale Oberstufe.");
		}
	}


	/**
	 * Ermittelt die GKL-Wahlen für die gymnasiale Oberstufe von dem angegebenen Schüler.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return Die GKL-Wahlen des Schülers
	 */
	public GostSchuelerGKLWahl get(final Long idSchueler) {
		return transactional(() -> {
			pruefeGymOb();
			if (schuelerRepository.findById(idSchueler).isEmpty()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
			}
			return gostSchuelerService.getMapGKLWahlen(List.of(idSchueler)).get(idSchueler);
		});
	}


	/**
	 * Setzt die Wahl für die gleichwertigen komplexen Leistungsnachweise für die gymnasiale Oberstufe von einem Schüler.
	 *
	 * @param wahl         die Wahl des Schülers in Bezug auf die GKLs
	 */
	public void put(final GostSchuelerGKLWahl wahl) {
		transactional(() -> {
			pruefeGymOb();

			// Prüfe, ob der Schüler existiert
			schuelerRepository.findById(wahl.idSchueler)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der ID %d gefunden.".formatted(wahl.idSchueler)));

			// Prüfe, ob die Vorgaben zu den gesetzten Klausurvorgaben-IDs existieren
			final Set<Long> idsVorgaben = new HashSet<>();
			if (wahl.idKlausurvorgabeEF_Sprachen != null) {
				idsVorgaben.add(wahl.idKlausurvorgabeEF_Sprachen);
			}
			if (wahl.idKlausurvorgabeEF_GW != null) {
				idsVorgaben.add(wahl.idKlausurvorgabeEF_GW);
			}
			if (wahl.idKlausurvorgabeEF_NW != null) {
				idsVorgaben.add(wahl.idKlausurvorgabeEF_NW);
			}
			if (wahl.idKlausurvorgabeQ_Sprachen != null) {
				idsVorgaben.add(wahl.idKlausurvorgabeQ_Sprachen);
			}
			if (wahl.idKlausurvorgabeQ_GW != null) {
				idsVorgaben.add(wahl.idKlausurvorgabeQ_GW);
			}
			if (wahl.idKlausurvorgabeQ_NW != null) {
				idsVorgaben.add(wahl.idKlausurvorgabeQ_NW);
			}
			if (!idsVorgaben.isEmpty()) {
				final var vorgaben = gostKlausurenVorgabeRepository.findListByIds(idsVorgaben);
				if (vorgaben.size() != idsVorgaben.size()) {
					throw new ApiOperationException(Status.BAD_REQUEST, "Es konnten nicht alle Klausur-Vorgaben zu den übergebenen IDs gefunden werden.");
				}
			}

			// Setze die IDs der Klausurvorgaben für die GKL-Wahlen, bei einer Wiederholung außerhalb der aktuellen Planung für den neuen Jahrgang. Ist der Wechsel
			// bereits vollzogen, so gehen die Planungsdaten zuvor in die Laufbahnplanung über.
			gostSchuelerService.uebernehmeWiederholungen(List.of(wahl.idSchueler));
			gostSchuelerService.setGKLWahl(wahl);
		});
	}

}

