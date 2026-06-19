package de.svws_nrw.service.gost;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.gost.GostBeratungslehrer;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangBeratungslehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.gost.GostJahrgangBeratungslehrerRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import jakarta.ws.rs.core.Response;

/**
 * Ein Service für den Zugriff auf die Abiturdaten von Schülern basierend auf den
 * Leistungsdaten der Schüler - nicht aus den Abiturtabellen
 */
public class GostBeratungslehrerService {

	private final LehrerRepository lehrerRepository;
	private final GostJahrgangBeratungslehrerRepository gostJahrgangBeratungslehrerRepository;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param lehrerRepository                        das Repository für den Zugriff auf die Lehrer
	 * @param gostJahrgangBeratungslehrerRepository   das Repository für den Zugriff auf die Beratungslehrer eines Abiturjahrgangs der gymnasialen Oberstufe
	 */
	public GostBeratungslehrerService(final LehrerRepository lehrerRepository, final GostJahrgangBeratungslehrerRepository gostJahrgangBeratungslehrerRepository) {
		this.lehrerRepository = lehrerRepository;
		this.gostJahrgangBeratungslehrerRepository = gostJahrgangBeratungslehrerRepository;
	}


	/**
	 * Ermittelt die Beratungslehrer für den übergebenen Abiturjahrgang der gymnasialen Oberstufe.
	 *
	 * @param abiturjahrgang   der Abiturjahrgang
	 *
	 * @return die Liste der Beratungslehrer
	 */
	public List<GostBeratungslehrer> get(final int abiturjahrgang) {
		final var map = getMap(List.of(abiturjahrgang));
		if (map.isEmpty()) {
			throw new ApiOperationException(Response.Status.NOT_FOUND,
					"Es wurde keine Abiturdaten für den Abiturjahrgang %d gefunden.".formatted(abiturjahrgang));
		}
		return map.values().iterator().next();
	}


	private static GostBeratungslehrer toApi(final DTOGostJahrgangBeratungslehrer dto, final DTOLehrer lehrer) {
		final GostBeratungslehrer result = new GostBeratungslehrer();
		result.id = dto.Lehrer_ID;
		if (lehrer == null) {
			return result;
		}
		result.kuerzel = lehrer.Kuerzel;
		result.nachname = lehrer.Nachname;
		result.vorname = lehrer.Vorname;
		return result;
	}


	/**
	 * Ermittelt die Beratungslehrer für die übergebenen Abiturjahrgänge der gymnasialen Oberstufe und gibt
	 * eine Map mit Listen zurück, welche jeweils ihrem Abiturjahrgang zugeordnet sind.
	 *
	 * @param abiturjahrgaenge   die Abiturjahrgänge
	 *
	 * @return eine Map mit den zugeordneten Listen der Beratungslehrer
	 */
	public Map<Integer, List<GostBeratungslehrer>> getMap(final Collection<Integer> abiturjahrgaenge) {
		return transactional(() -> {
			// Initialisiere dir Rückgabe-Map
			final Map<Integer, List<GostBeratungslehrer>> result = new HashMap<>();
			for (final Integer abiturjahrgang : abiturjahrgaenge) {
				result.put(abiturjahrgang, new ArrayList<>());
			}

			// Bestimme zunächst die Beratungslehrer-Einträge zu den Abiturjahrgängen aus der Datenbank
			final List<DTOGostJahrgangBeratungslehrer> listBeratungslehrer = gostJahrgangBeratungslehrerRepository.getListByAbiturjahrgaenge(abiturjahrgaenge);
			if (listBeratungslehrer.isEmpty()) {
				return result;
			}

			// Bestimme die Lehrer-Daten zu den Beratungslehrern
			final List<Long> idsLehrer = listBeratungslehrer.stream().map(b -> b.Lehrer_ID).filter(l -> l != null).distinct().toList();
			if (idsLehrer.isEmpty()) {
				return result;
			}
			final Map<Long, DTOLehrer> mapLehrer = lehrerRepository.findMapByIds(idsLehrer);

			// Füge die Beratungslehrer hinzu
			for (final DTOGostJahrgangBeratungslehrer beratungslehrer : listBeratungslehrer) {
				result.getOrDefault(beratungslehrer.Abi_Jahrgang, new ArrayList<>())
						.add(toApi(beratungslehrer, mapLehrer.get(beratungslehrer.Lehrer_ID)));
			}
			return result;
		});
	}

}
