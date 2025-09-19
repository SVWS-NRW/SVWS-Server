package de.svws_nrw.davapi.data.carddav;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.adressbuch.AdressbuchEintrag;
import de.svws_nrw.core.data.adressbuch.AdressbuchKontakt;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerErzieherAdresse;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Diese Klasse dient dem Zugriff auf die Datenbank, um ein Erzieher-Adressbuch für einen Schuljahresabschnitt zu generieren.
 */
public final class DataCardDavErzieher extends DataManagerCardDav {

	/** Data-Manager für Zugriff auf die Daten des zugehörigen Schülers */
	private final DataCardDavSchueler cardDavSchueler;

	/**
	 * Erzeugt ein neues Erzieher-Adressbuch für den übergebenen Schuljahresabschnitt
	 *
	 * @param conn                     die Datenbank-Verbindung
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public DataCardDavErzieher(final DBEntityManager conn, final long idSchuljahresabschnitt) throws ApiOperationException {
		super(conn, idSchuljahresabschnitt);
		cardDavSchueler = new DataCardDavSchueler(conn, idSchuljahresabschnitt);
	}


	/**
	 * Mappt die Informationen zu einem Erzieher auf einen Kontakt/Adressbucheintrag.
	 *
	 * @param e            die Erzieherdaten
	 * @param ort          der Wohnort des Erziehers
	 * @param categories   die Kategorien, die diesem Erzieher zugeordnet sind
	 *
	 * @return der Kontakt
	 */
	private AdressbuchEintrag mapContact(final DTOSchuelerErzieherAdresse e, final DTOOrt ort, final Set<String> categories) {
		// TODO Für die Erziehereinträge sollten beide Einträge verwendet werden. Die ID sollte analog zu APIErzieher erzeugt werden...
		final AdressbuchKontakt k = new AdressbuchKontakt();
		k.id = getKontaktId(e.ID);
		k.email = e.ErzEmail;

		k.hausnummer = e.ErzHausNr;
		k.hausnummerZusatz = e.ErzHausNrZusatz;
		k.nachname = e.Name1;
		if (ort != null) {
			k.ort = ort.Bezeichnung;
			k.plz = ort.PLZ;
		}
		k.kategorien.addAll(categories);
		k.strassenname = e.ErzStrassenname;
		k.vorname = e.Vorname1;
		k.idKind = cardDavSchueler.getKontaktId(e.Schueler_ID);
		return k;
	}


	@Override
	public String getKontaktId(final long id) {
		return "Erzieher_" + schuljahresabschnitt.schuljahr + "_" + schuljahresabschnitt.abschnitt + "_" + id;
	}


	@Override
	public List<AdressbuchEintrag> getKontakte(final String idBook, final boolean withPayload) throws ApiOperationException {
		final List<AdressbuchEintrag> result = new ArrayList<>();

		if (!conn.getUser().pruefeKompetenz(BenutzerKompetenz.CARDDAV_ERZIEHER_ANSEHEN))
			return result;

		// Bestimme alle Erzieher
		// Bestimme zunächst die Schülerliste für den Schuljahresabschnitt und filtere anschließend die relevanten Schüler anhand des Schüler-Status
		final List<SchuelerListeEintrag> listSchueler = DataSchuelerliste.getListeSchueler(conn, schuljahresabschnitt.id, false).stream()
				.filter(DataCardDavSchueler::filterBySchuelerStatus).toList();
		final List<Long> idsSchueler = listSchueler.stream().map(s -> s.id).toList();

		final List<DTOSchuelerErzieherAdresse> listErzieher =
				conn.queryList(DTOSchuelerErzieherAdresse.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerErzieherAdresse.class, idsSchueler);
		final Map<Long, DTOSchuelerErzieherAdresse> erzieherBySchuelerID = new HashMap<>(); // TODO Unterstütze auch mehrere Erzieher pro Schüler
		for (DTOSchuelerErzieherAdresse erzieher : listErzieher) {
			if (erzieherBySchuelerID.containsKey(erzieher.Schueler_ID)) { // TODO nicht nur den Erzieher mit der höchsten Sortierung wählen
				final DTOSchuelerErzieherAdresse vorhandenerErzieher = erzieherBySchuelerID.get(erzieher.Schueler_ID);
				if (erzieher.Sortierung < vorhandenerErzieher.Sortierung)
					erzieher = vorhandenerErzieher;
			}
			erzieherBySchuelerID.put(erzieher.Schueler_ID, erzieher);
		}

		// Wenn keine Payload erzeugt wird, so können leere Adressbuch-Einträge zurückgegeben werden ...
		if (!withPayload)
			return erzieherBySchuelerID.values().stream().map(l -> mapEmptyContact(l.ID)).toList();

		// ... ansonsten müssen die entsprechenden Daten zusammengestellt werden.
		final List<DTOSchueler> listDTOSchueler = new DataSchuelerStammdaten(conn).getDTOList(idsSchueler);
		final Set<Long> idsOrte = listDTOSchueler.stream().map(s -> s.Ort_ID).collect(Collectors.toSet());
		idsOrte.addAll(erzieherBySchuelerID.values().stream().map(e -> e.ErzOrt_ID).collect(Collectors.toSet()));
		final Map<Long, DTOOrt> mapOrtID = queryMapOrte(idsOrte);

		// Leite die Kategorien von den Kategorien der Schüler ab
		final Map<Long, Set<String>> tmpCategoriesBySchuelerID = cardDavSchueler.getCategoriesById(listDTOSchueler);
		final Map<Long, Set<String>> categoriesBySchuelerID = new HashMap<>();
		for (final Map.Entry<Long, Set<String>> e : tmpCategoriesBySchuelerID.entrySet())
			categoriesBySchuelerID.put(e.getKey(), e.getValue().stream().map(s -> "Eltern " + s).collect(Collectors.toSet()));

		// Erzeuge die Kontakte
		for (final DTOSchuelerErzieherAdresse erzieher : erzieherBySchuelerID.values()) { // TODO Unterstütze beide Einträge im DTO
			final AdressbuchEintrag eintrag = mapContact(erzieher, mapOrtID.get(erzieher.ErzOrt_ID), categoriesBySchuelerID.get(erzieher.Schueler_ID));
			result.add(eintrag);
		}
		return result;
	}

}
