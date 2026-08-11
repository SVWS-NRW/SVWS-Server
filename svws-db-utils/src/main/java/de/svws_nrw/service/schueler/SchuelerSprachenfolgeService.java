package de.svws_nrw.service.schueler;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;
import de.svws_nrw.repo.schueler.sprachenfolge.SchuelerSprachenfolgeRepository;


/**
 * Ein Service für den Zugriff auf die Sprachenfolge der Schüler
 */
public final class SchuelerSprachenfolgeService {

	/** Das Repository für die Sprachenfolge der Schüler. */
	private final SchuelerSprachenfolgeRepository schuelerSprachenfolgeRepository;


	/**
	 * Erstellt einen neuen Service mit Repository-Anbindung.
	 *
	 * @param schuelerSprachenfolgeRepository   das Repository für die Sprachenfolge
	 */
	public SchuelerSprachenfolgeService(final SchuelerSprachenfolgeRepository schuelerSprachenfolgeRepository) {
		this.schuelerSprachenfolgeRepository = schuelerSprachenfolgeRepository;
	}


	/**
	 * Bestimmt die Sprachbelegungen mehrerer Schüler.
	 *
	 * @param idsSchueler   die ID des Schülers, für welche die Sprachbelegungen bestimmt werden sollen
	 *
	 * @return eine Map mit den Sprachbelegungen der Schüler zugeordnet zu deren IDs
	 */
	public Map<Long, List<Sprachbelegung>> getMapSprachenfolgen(final Collection<Long> idsSchueler) {
		final Map<Long, List<DTOSchuelerSprachenfolge>> map = schuelerSprachenfolgeRepository.getMapBySchuelerIDs(idsSchueler);
		final Map<Long, List<Sprachbelegung>> result = new HashMap<>();
		for (final var entry : map.entrySet()) {
			result.put(entry.getKey(), entry.getValue().stream().map(SchuelerSprachenfolgeService::toApi).toList());
		}
		return result;
	}


	/**
	 * Konvertiert die übergebenen DTO-Objekte in die API-Objekte.
	 *
	 * @param dto   das DTO-Objekt, welches konvertiert werden soll
	 *
	 * @return das API-Objekt, welches aus dem DTO-Objekt konvertiert wurde
	 */
	public static Sprachbelegung toApi(final DTOSchuelerSprachenfolge dto) {
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
