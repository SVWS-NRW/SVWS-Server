package de.svws_nrw.data.schueler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.schueler.SchuelerListe;
import de.svws_nrw.data.gost.DataGostJahrgangsliste;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schueler.SchuelerServiceFactory;
import de.svws_nrw.service.schueler.liste.SchuelerListeEintragService;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.katalog.KatalogServiceFactory;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangService;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse stellt statische Hilfsmethoden für die Schülerliste bereit. Sie bleibt vorerst für das
 * Aggregat der Auswahlliste ({@link #getSchuelerListe(DBEntityManager, long)}) bestehen. Die Einträge der
 * Schülerliste werden vom {@link SchuelerListeEintragService} erstellt.
 */
public final class DataSchuelerliste {

	/**
	 * Privater Konstruktor, da die Klasse nur statische Methoden bereitstellt.
	 */
	private DataSchuelerliste() {
		// leer
	}


	/**
	 * Aggregiert alle Daten, welche für eine Auswahlliste mit dem SchuelerListeManager
	 * benötigt werden.
	 *
	 * @param conn                      die Datenbankverbindung
	 * @param idSchuljahresabschnitt    die ID des Schuljahresabschnitt für welchen die Daten aggregiert werden sollen
	 *
	 * @return die Daten für die Schüler-Auswahlliste
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static SchuelerListe getSchuelerListe(final DBEntityManager conn, final long idSchuljahresabschnitt) throws ApiOperationException {
		// Bestimme zunächst alle Schuljahresabschnitte und prüfe, ob die übergeben ID gültig ist
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt);
		if (schuljahresabschnitt == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es konnte kein Schuljahresabschnitt mit der ID %d gefunden werden"
					.formatted(idSchuljahresabschnitt));
		}

		final DataKlassendaten dataKlassendaten = new DataKlassendaten(conn);

		// Erstelle das Ergebnis-DTO
		final SchuelerListe result = new SchuelerListe();
		result.idSchuljahresabschnitt = idSchuljahresabschnitt;
		result.schueler.addAll(getSchuelerListeEintragService().getListe(idSchuljahresabschnitt, false));
		result.klassen.addAll(dataKlassendaten.getListBySchuljahresabschnittID(idSchuljahresabschnitt, false));
		result.kurse.addAll(DataKurse.getKursListenFuerAbschnitt(conn, idSchuljahresabschnitt, true));
		result.jahrgaenge.addAll(getJahrgangService().getAll());

		if (conn.getUser().schuleHatGymOb()) {
			result.jahrgaengeGost.addAll(DataGostJahrgangsliste.getGostJahrgangsliste(schuljahresabschnitt));
		}

		// ermittle ggf. weitere Klassen
		final Set<Long> idsKlassen = result.klassen.stream().map(k -> k.id).collect(Collectors.toSet());
		final List<Long> idsFehlendeKlassen = result.schueler.stream().map(s -> s.idKlasse)
				.filter(id -> (id != null) && (id >= 0) && (!idsKlassen.contains(id)))
				.distinct().toList();

		if (!idsFehlendeKlassen.isEmpty()) {
			result.klassen.addAll(dataKlassendaten.getListByIdsOhneSchueler(idsFehlendeKlassen, idSchuljahresabschnitt));
		}

		return result;
	}

	private static @NotNull SchuelerListeEintragService getSchuelerListeEintragService() {
		return SchuelerServiceFactory.getNewInstance().getSchuelerListeEintragService();
	}

	private static @NotNull JahrgangService getJahrgangService() {
		final var katalogRepositoryFactory = new KatalogRepositoryFactory();
		final var eigeneSchuleRepositoryFactory = new EigeneSchuleRepositoryFactory();
		final var eigeneSchuleServiceFactory = EigeneSchuleServiceFactory.getNewInstance(eigeneSchuleRepositoryFactory);
		final var katalogServiceFactory = KatalogServiceFactory.getNewInstance(katalogRepositoryFactory, eigeneSchuleServiceFactory);
		return katalogServiceFactory.getJahrgangService();
	}

}
