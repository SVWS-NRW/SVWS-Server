package de.svws_nrw.service.gost;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFaecher;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.repo.gost.GostJahrgangFaecherRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den Zugriff auf die Fächerdaten von Abiturjahrgängen
 */
public class GostFaecherService {

	private final BenutzerAllgemeinRepository benutzerRepository;
	private final FachRepository fachRepository;
	private final GostJahrgangFaecherRepository gostJahrgangFaecherRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param benutzerRepository              das Repository für den Zugriff auf Benutzerdaten und den angemeldeten Benutzer
	 * @param fachRepository                  das Repository für den Zugriff auf die Fächer der Schule
	 * @param gostJahrgangFaecherRepository   das Repository für den Zugriff auf die Fächer der Abiturjahrgänge der Gymnasialen Oberstufe
	 */
	public GostFaecherService(final BenutzerAllgemeinRepository benutzerRepository, final FachRepository fachRepository,
			final GostJahrgangFaecherRepository gostJahrgangFaecherRepository) {
		this.benutzerRepository = benutzerRepository;
		this.fachRepository = fachRepository;
		this.gostJahrgangFaecherRepository = gostJahrgangFaecherRepository;
	}


	/**
	 * Ermittelt das Fach der gymnasialen Oberstufe für die angegebenen ID.
	 *
	 * @param id   die ID des Faches
	 *
	 * @return das Fach der Gymnasialen Oberstufe
	 */
	public GostFach get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Response.Status.NOT_FOUND,
					"Es wurde keine Fächerdaten für ein Fach mit der ID %d gefunden.".formatted(id));
		}
		return list.getFirst();
	}


	/**
	 * Ermittelt die Fächer der gymnasialen Oberstufe für die angegebenen IDs.
	 *
	 * @param ids   die IDs der Fächer
	 *
	 * @return die Lister der Fächer der Gymnasialen Oberstufe
	 */
	public List<GostFach> getList(final Collection<Long> ids) {
		return transactional(() -> {
			if (!benutzerRepository.getAktuellerBenutzer().schuleHatGymOb()) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Die Schule hat eine Schulform ohne gymnasiale Oberstufe.");
			}
			final int schuljahr = benutzerRepository.getAktuellerBenutzer().schuleGetSchuljahr();
			final Map<Long, DTOFach> faecher = fachRepository.getMap();
			return faecher.values().stream().filter(fach -> fach.IstOberstufenFach)
					.map(fach -> DBUtilsFaecherGost.mapFromDTOFach(schuljahr, fach, faecher)).filter(Objects::nonNull).toList();
		});
	}


	/**
	 * Erstellt für den angegebenen Abiturjahrgang einen Fächer-Manager
	 *
	 * @param abiJahrgang           der Abiturjahrgang
	 * @param nurWaehlbareFaecher   gibt an, ob in dem Fächer-Manager nur wählbare Fächer aufgenommen werden
	 *
	 * @return der Fächer-Manager
	 */
	public GostFaecherManager getGostFaecherManager(final int abiJahrgang, final boolean nurWaehlbareFaecher) {
		final var map = getMapGostFaecherManager(List.of(abiJahrgang), nurWaehlbareFaecher);
		if (map.isEmpty()) {
			throw new ApiOperationException(Response.Status.NOT_FOUND,
					"Es wurde keine Fächerdaten für den Abiturjahrgang  %d gefunden.".formatted(abiJahrgang));
		}
		return map.values().iterator().next();
	}


	/**
	 * Erstellt für die angegebenen Abiturjahrgänge jeweils einen Fächer-Manager
	 *
	 * @param abiJahrgaenge         die Abiturjahrgänge
	 * @param nurWaehlbareFaecher   gibt an, ob in den Fächer-Managern nur wählbare Fächer aufgenommen werden
	 *
	 * @return eine Map mit den Fächer-Managern zugeordnet zu ihren Abiturjahrgängen
	 */
	public Map<Integer, GostFaecherManager> getMapGostFaecherManager(final Collection<Integer> abiJahrgaenge,
			final boolean nurWaehlbareFaecher) {
		return transactional(() -> {
			// Bestimme erstmal alle Fächer mit den grundlegenden Faten für das Fächermapping
			final Map<Long, DTOFach> faecher = fachRepository.getMap();

			// Bestimme dann für alle Abiturjahrgänge außer dem Vorlagenjahrgang die im Abiturjahrgang definierten Fächer
			final HashMap2D<Integer, Long, DTOGostJahrgangFaecher> mapJahrgangsfaecher = gostJahrgangFaecherRepository.getMap2DByAbiturjahrgangAndFachID(abiJahrgaenge);

			// Erstelle für die einzelnen Abiturjahrgänge die Fächer-Manager
			final Map<Integer, GostFaecherManager> result = new HashMap<>();
			for (final Integer abiJahrgang : abiJahrgaenge) {
				if ((abiJahrgang == null) || (abiJahrgang == -1)) {
					final int schuljahr = benutzerRepository.getAktuellerBenutzer().schuleGetSchuljahr();
					final @NotNull List<GostFach> tmpFaecher = faecher.values().stream().filter(fach -> fach.IstOberstufenFach)
							.map(fach -> DBUtilsFaecherGost.mapFromDTOFach(schuljahr, fach, faecher)).filter(Objects::nonNull).toList();
					result.put(abiJahrgang, new GostFaecherManager(schuljahr, tmpFaecher));
				} else {
					final int schuljahr = abiJahrgang - 1;
					List<GostFach> tmpFaecher = faecher.values().stream().filter(fach -> fach.IstOberstufenFach)
							.map(fach -> DBUtilsFaecherGost.mapFromDTOGostJahrgangFaecher(schuljahr, fach.ID, mapJahrgangsfaecher.getOrNull(abiJahrgang, fach.ID), faecher))
							.filter(Objects::nonNull).toList();
					if (nurWaehlbareFaecher) {
						tmpFaecher = tmpFaecher.stream()
								.filter(f -> (f.istMoeglichEF1 || f.istMoeglichEF2 || f.istMoeglichQ11 || f.istMoeglichQ12 || f.istMoeglichQ21 || f.istMoeglichQ22))
								.toList();
					}
					result.put(abiJahrgang, new GostFaecherManager(schuljahr, tmpFaecher));
				}
			}
			return result;
		});
	}


}
