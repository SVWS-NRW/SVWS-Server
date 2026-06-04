package de.svws_nrw.service.schueler;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;
import de.svws_nrw.repo.schueler.SchuelerSprachenfolgeRepository;


/**
 * Ein Service für den Zugriff auf die Sprachenfolge der Schüler
 */
public final class SchuelerSprachenfolgeService {

	/** Das Repository für die Sprachenfolge der Schüler. */
	private final SchuelerSprachenfolgeRepository schuelerSprachenfolgeRepository;

	/** Cache für die Sprachenfolge-Daten, gruppiert nach Schüler-ID. */
	private Map<Long, List<DTOSchuelerSprachenfolge>> mapSprachenfolge = new HashMap<>();


	/**
	 * Erstellt einen neuen Service.
	 */
	public SchuelerSprachenfolgeService() {
		this.schuelerSprachenfolgeRepository = null;
	}


	/**
	 * Erstellt einen neuen Service mit Repository-Anbindung.
	 *
	 * @param schuelerSprachenfolgeRepository   das Repository für die Sprachenfolge
	 */
	public SchuelerSprachenfolgeService(final SchuelerSprachenfolgeRepository schuelerSprachenfolgeRepository) {
		this.schuelerSprachenfolgeRepository = schuelerSprachenfolgeRepository;
	}


	/**
	 * Lädt die Sprachenfolge-Daten für die angegebenen Schüler in den Service-Cache.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 */
	public void fetchData(final Collection<Long> idsSchueler) {
		if (schuelerSprachenfolgeRepository == null) {
			throw new IllegalStateException("Der SchuelerSprachenfolgeService wurde ohne Repository initialisiert.");
		}
		mapSprachenfolge = schuelerSprachenfolgeRepository.getMapBySchuelerIDs(idsSchueler);
	}


	/**
	 * Gibt die Sprachbelegungen eines Schülers aus dem Service-Cache zurück.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Sprachbelegungen des Schülers
	 */
	public List<Sprachbelegung> getSprachenfolge(final Long idSchueler) {
		final var list = mapSprachenfolge.get(idSchueler);
		if (list == null) {
			return List.of();
		}
		return list.stream().map(this::toApi).toList();
	}


	/**
	 * Konvertiert die übergebenen DTO-Objekte in die API-Objekte.
	 *
	 * @param dto   das DTO-Objekt, welches konvertiert werden soll
	 *
	 * @return das API-Objekt, welches aus dem DTO-Objekt konvertiert wurde
	 */
	public Sprachbelegung toApi(final DTOSchuelerSprachenfolge dto) {
		final var daten = new Sprachbelegung();
		daten.id = dto.ID;
		daten.sprache = dto.Sprache;
		daten.istNachweis = Boolean.TRUE.equals(dto.IstNachweis);
		daten.reihenfolge = dto.ReihenfolgeNr;
		daten.belegungVonJahrgang = dto.ASDJahrgangVon;
		daten.belegungBisJahrgang = dto.ASDJahrgangBis;
		daten.belegungVonAbschnitt = dto.AbschnittVon;
		daten.belegungBisAbschnitt = dto.AbschnittBis;
		daten.referenzniveau = dto.Referenzniveau;
		daten.hatKleinesLatinum = Boolean.TRUE.equals(dto.KleinesLatinumErreicht);
		daten.hatLatinum = Boolean.TRUE.equals(dto.LatinumErreicht);
		daten.hatGraecum = Boolean.TRUE.equals(dto.GraecumErreicht);
		daten.hatHebraicum = Boolean.TRUE.equals(dto.HebraicumErreicht);
		return daten;
	}

}
