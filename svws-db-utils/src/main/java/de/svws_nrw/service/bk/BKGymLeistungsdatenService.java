package de.svws_nrw.service.bk;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.asd.data.schueler.Sprachendaten;
import de.svws_nrw.core.data.bk.abi.BKGymLeistungen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;


/**
 * Ein Service für den Zugriff auf die Leistungsdaten im Beruflichen Gymnasium
 */
public final class BKGymLeistungsdatenService {

	private final BKGymLeistungsdatenServiceKontext kontext;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param kontext   der Daten-Kontext für diesen Service
	 */
	public BKGymLeistungsdatenService(final BKGymLeistungsdatenServiceKontext kontext) {
		this.kontext = kontext;
	}


	private BKGymLeistungen toApi(final Long idSchueler) {
		final var daten = new BKGymLeistungen();
		daten.id = idSchueler;
		daten.aktuellesSchuljahr = kontext.getAktuellesSchuljahr();
		daten.aktuellerJahrgang = kontext.getAktuellerJahrgang(idSchueler);
		daten.sprachendaten = new Sprachendaten();
		daten.sprachendaten.schuelerID = idSchueler;
		daten.sprachendaten.belegungen = kontext.getSprachenfolge(idSchueler);
		daten.sprachendaten.pruefungen = kontext.getSprachpruefungen(idSchueler);
		final var halbjahre = kontext.getBewerteteHalbjahre(idSchueler);
		for (int i = 0; (i < halbjahre.length) && (i < daten.bewertetesHalbjahr.length); i++) {
			daten.bewertetesHalbjahr[i] = halbjahre[i];
		}
		daten.faecher.addAll(kontext.getFaecher(idSchueler, daten.sprachendaten));
		return daten;
	}


	/**
	 * Ermittelt die Leistungsdaten für den Schüler mit der angegebenen ID.
	 *
	 * @param schuelerID   die ID des Schülers, für den die Leistungsdaten ermittelt werden
	 *
	 * @return die Leistungsdaten für den Schüler mit der angegebenen ID
	 */
	public BKGymLeistungen get(final long schuelerID) {
		final var list = getList(List.of(schuelerID));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Eintrag mit der ID %d gefunden.".formatted(schuelerID));
		}
		return list.getFirst();
	}


	/**
	 * Ermittelt die Leistungsdaten für die Schüler mit den übergebenen IDs.
	 *
	 * @param idsSchueler   die IDs der Schüler, für die die Leistungsdaten ermittelt werden.
	 *
	 * @return die Liste der Leistungsdaten für die Schüler
	 */
	public List<BKGymLeistungen> getList(final Collection<Long> idsSchueler) {
		kontext.fetchData(idsSchueler);
		return idsSchueler.stream().map(this::toApi).toList();
	}
}
