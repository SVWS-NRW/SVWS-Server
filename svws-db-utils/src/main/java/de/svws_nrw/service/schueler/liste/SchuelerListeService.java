package de.svws_nrw.service.schueler.liste;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.schueler.SchuelerListe;
import de.svws_nrw.service.gost.GostJahrgangslisteService;
import de.svws_nrw.service.klassen.KlassenService;
import de.svws_nrw.service.kurse.KursService;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangService;

/**
 * Ein Service zur Erstellung der Daten für die Schüler-Auswahlliste ({@link SchuelerListe}). Die Daten werden aus
 * den Services für Schüler, Klassen, Kurse, Jahrgänge und die gymnasiale Oberstufe zusammengesetzt.
 * <p>
 * Klassen, die von Schülern referenziert werden, aber nicht zum gewählten Schuljahresabschnitt gehören
 * (z. B. weil der Schüler dort keinen Lernabschnitt hat), werden nachgeladen. Dabei werden sie in Bezug auf ihren
 * eigenen Schuljahresabschnitt aufbereitet.
 * </p>
 */
public final class SchuelerListeService {

	private static final long MIN_ID_KLASSE = 0L;

	private final SchuelerListeEintragService schuelerListeEintragService;
	private final KlassenService klassenService;
	private final KursService kursService;
	private final JahrgangService jahrgangService;
	private final GostJahrgangslisteService gostJahrgangslisteService;
	private final SchuljahresabschnittService schuljahresabschnittService;
	private final EigeneSchuleService eigeneSchuleService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuelerListeEintragService   der Service für die Einträge der Schülerliste
	 * @param klassenService                der Service für die Klassen
	 * @param kursService                   der Service für die Kurse
	 * @param jahrgangService               der Service für die Jahrgänge
	 * @param gostJahrgangslisteService     der Service für die Jahrgänge der gymnasialen Oberstufe
	 * @param schuljahresabschnittService   der Service für die Schuljahresabschnitte
	 * @param eigeneSchuleService           der Service für die Daten der eigenen Schule
	 */
	public SchuelerListeService(
			final SchuelerListeEintragService schuelerListeEintragService,
			final KlassenService klassenService,
			final KursService kursService,
			final JahrgangService jahrgangService,
			final GostJahrgangslisteService gostJahrgangslisteService,
			final SchuljahresabschnittService schuljahresabschnittService,
			final EigeneSchuleService eigeneSchuleService) {
		this.schuelerListeEintragService = schuelerListeEintragService;
		this.klassenService = klassenService;
		this.kursService = kursService;
		this.jahrgangService = jahrgangService;
		this.gostJahrgangslisteService = gostJahrgangslisteService;
		this.schuljahresabschnittService = schuljahresabschnittService;
		this.eigeneSchuleService = eigeneSchuleService;
	}

	/**
	 * Aggregiert alle Daten, welche für eine Auswahlliste mit dem SchuelerListeManager benötigt werden.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts, für welchen die Daten aggregiert werden
	 *
	 * @return die Daten für die Schüler-Auswahlliste
	 */
	public SchuelerListe getBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		final var abschnitt = schuljahresabschnittService.getById(idSchuljahresabschnitt);

		final var result = new SchuelerListe();
		result.idSchuljahresabschnitt = idSchuljahresabschnitt;
		addSchueler(result);
		addKlassen(result);
		addKurse(result);
		addJahrgaenge(result);
		addGostJahrgaenge(result, abschnitt);
		addMissingKlassen(result);
		return result;
	}

	private void addSchueler(final SchuelerListe liste) {
		liste.schueler.addAll(schuelerListeEintragService.getListe(liste.idSchuljahresabschnitt, false));
	}

	private void addKlassen(final SchuelerListe liste) {
		liste.klassen.addAll(klassenService.getListBySchuljahresabschnitt(liste.idSchuljahresabschnitt, false));
	}

	private void addKurse(final SchuelerListe liste) {
		liste.kurse.addAll(kursService.getListBySchuljahresabschnitt(liste.idSchuljahresabschnitt, true));
	}

	private void addJahrgaenge(final SchuelerListe liste) {
		liste.jahrgaenge.addAll(jahrgangService.getAll());
	}

	private void addGostJahrgaenge(final SchuelerListe liste, final Schuljahresabschnitt abschnitt) {
		if (eigeneSchuleService.hatGymnasialeOberstufe()) {
			liste.jahrgaengeGost.addAll(gostJahrgangslisteService.getList(abschnitt));
		}
	}

	/**
	 * Lädt die Klassen nach, welche von Schülern referenziert werden, aber noch nicht in der Liste enthalten sind.
	 * Die Klassen enthalten keine Schüler.
	 *
	 * @param liste   die Liste, welche ergänzt wird
	 */
	private void addMissingKlassen(final SchuelerListe liste) {
		final var idsMissingKlassen = determineIdsMissingKlassen(liste);
		if (!idsMissingKlassen.isEmpty()) {
			liste.klassen.addAll(klassenService.getListByIds(idsMissingKlassen, false));
		}
	}

	private List<Long> determineIdsMissingKlassen(final SchuelerListe liste) {
		final Set<Long> idsKlassen = liste.klassen.stream()
				.map(k -> k.id)
				.collect(Collectors.toSet());
		return liste.schueler.stream()
				.map(s -> s.idKlasse)
				.filter(id -> hasKlasse(id) && !idsKlassen.contains(id))
				.distinct()
				.toList();
	}

	private static boolean hasKlasse(final Long idKlasse) {
		return (idKlasse != null) && (idKlasse >= MIN_ID_KLASSE);
	}

}
