package de.svws_nrw.service.schueler.lernabschnitt;

import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import jakarta.ws.rs.core.Response.Status;


/**
 * Bündelt die Regeln zu den EP-Jahren (Besuchsjahre in der Schuleingangsphase) eines Lernabschnitts. Die EP-Jahre dürfen nur
 * an Schulen mit Primarbereich und nur in den Jahrgängen 1 und 2 geändert werden. In den Jahrgängen 3 und 4 sind sie
 * schreibgeschützt und werden aus der Eingangsphase fortgeschrieben.
 */
public final class SchuelerLernabschnittEpJahre {

	/** Die ASD-Jahrgänge der Schuleingangsphase, in denen die EP-Jahre geändert werden dürfen */
	private static final Set<String> ASD_JAHRGAENGE_EINGANGSPHASE = Set.of("01", "02");

	/** Die ASD-Jahrgänge, in denen die EP-Jahre schreibgeschützt sind und aus der Eingangsphase fortgeschrieben werden */
	private static final Set<String> ASD_JAHRGAENGE_FORTSCHREIBUNG = Set.of("03", "04");

	private final SchuelerLernabschnittRepository repository;

	/**
	 * Erstellt eine neue Instanz für die EP-Jahre-Regeln.
	 *
	 * @param repository   das Repository für die Lernabschnittsdaten
	 */
	public SchuelerLernabschnittEpJahre(final SchuelerLernabschnittRepository repository) {
		this.repository = repository;
	}

	/**
	 * Prüft, ob die EP-Jahre geändert werden dürfen. Das ist nur an Schulen mit Primarbereich und nur in den Jahrgängen 1 und 2
	 * erlaubt. Maßgeblich ist der Jahrgang, den der Lernabschnitt nach dem Request haben wird. Wird ein unveränderter Wert
	 * übermittelt (z. B. weil der Client das gesamte Objekt sendet), so wird dieser nicht als Änderung gewertet.
	 *
	 * @param idEpJahre       die im Request übermittelte ID der EP-Jahre, ggf. {@code null}
	 * @param epJahreBisher   die EP-Jahre vor der Anwendung des Requests, ggf. {@code null}
	 * @param schulform       die Schulform der eigenen Schule
	 * @param asdJahrgang     liefert den ASD-Jahrgang, den der Lernabschnitt nach dem Request haben wird, ggf. {@code null}
	 */
	public void validateAenderbar(final Long idEpJahre, final Integer epJahreBisher, final Schulform schulform,
			final Supplier<String> asdJahrgang) {
		if (Objects.equals(toIdEpJahre(epJahreBisher), idEpJahre)) {
			return;
		}
		if (!schulform.hatPrimarbereich()) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Besuchsjahre in der Schuleingangsphase können nur an Schulen mit Primarbereich geändert werden.");
		}
		if (!isInJahrgaengen(ASD_JAHRGAENGE_EINGANGSPHASE, asdJahrgang.get())) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Besuchsjahre in der Schuleingangsphase können nur in den Jahrgängen 1 und 2 geändert werden.");
		}
	}

	/**
	 * Schreibt die EP-Jahre auf die Lernabschnitte der Jahrgänge 3 und 4 des Schülers fort. Alle diese Lernabschnitte erhalten
	 * das Maximum der EP-Jahre aus den Lernabschnitten der Jahrgänge 1 und 2. Die Fortschreibung erfolgt nur an Schulen mit
	 * Primarbereich und nur, wenn der Request die EP-Jahre oder den Jahrgang ändert, also die Laufbahn betrifft. Liegen in den
	 * Jahrgängen 1 und 2 keine EP-Jahre vor, so bleiben die Lernabschnitte unverändert.
	 *
	 * @param geaendert           der angelegte bzw. geänderte Lernabschnitt
	 * @param schulform           die Schulform der eigenen Schule
	 * @param hasLaufbahnChange   gibt an, ob der Request die EP-Jahre oder den Jahrgang enthält
	 */
	public void propagate(final DTOSchuelerLernabschnittsdaten geaendert, final Schulform schulform, final boolean hasLaufbahnChange) {
		if (!schulform.hatPrimarbereich() || !hasLaufbahnChange) {
			return;
		}
		final var lernabschnitte = loadLernabschnitteMitAktuellemStand(geaendert);
		determineMaxEpJahreEingangsphase(lernabschnitte)
				.ifPresent(maximum -> setEpJahreInFolgeabschnitten(lernabschnitte, maximum));
	}

	private List<DTOSchuelerLernabschnittsdaten> loadLernabschnitteMitAktuellemStand(final DTOSchuelerLernabschnittsdaten geaendert) {
		// Der geänderte Lernabschnitt wird immer mit seinem aktuellen Stand berücksichtigt
		return repository.findListBySchuelerIDs(List.of(geaendert.Schueler_ID)).stream()
				.map(lernabschnitt -> (lernabschnitt.ID == geaendert.ID) ? geaendert : lernabschnitt)
				.toList();
	}

	private static OptionalInt determineMaxEpJahreEingangsphase(final List<DTOSchuelerLernabschnittsdaten> lernabschnitte) {
		return lernabschnitte.stream()
				.filter(lernabschnitt -> isInJahrgaengen(ASD_JAHRGAENGE_EINGANGSPHASE, lernabschnitt.ASDJahrgang))
				.map(lernabschnitt -> lernabschnitt.EPJahre)
				.filter(Objects::nonNull)
				.mapToInt(Integer::intValue)
				.max();
	}

	private static void setEpJahreInFolgeabschnitten(final List<DTOSchuelerLernabschnittsdaten> lernabschnitte, final int epJahre) {
		lernabschnitte.stream()
				.filter(lernabschnitt -> isInJahrgaengen(ASD_JAHRGAENGE_FORTSCHREIBUNG, lernabschnitt.ASDJahrgang))
				.forEach(lernabschnitt -> lernabschnitt.EPJahre = epJahre);
	}

	private static boolean isInJahrgaengen(final Set<String> asdJahrgaenge, final String asdJahrgang) {
		return (asdJahrgang != null) && asdJahrgaenge.contains(asdJahrgang);
	}

	private static Long toIdEpJahre(final Integer epJahre) {
		return (epJahre == null) ? null : Long.valueOf(epJahre);
	}

}
