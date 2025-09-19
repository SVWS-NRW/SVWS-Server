package de.svws_nrw.davapi.data.carddav;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.data.adressbuch.AdressbuchEintrag;
import de.svws_nrw.core.data.adressbuch.AdressbuchKontakt;
import de.svws_nrw.core.data.adressbuch.Telefonnummer;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerTelefon;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOTelefonArt;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Diese Klasse dient dem Zugriff auf die Datenbank, um ein Schüler-Adressbuch für einen Schuljahresabschnitt zu generieren.
 */
public final class DataCardDavSchueler extends DataManagerCardDav {

	/**
	 * Erzeugt ein neues Schüler-Adressbuch für den übergebenen Schuljahresabschnitt
	 *
	 * @param conn                     die Datenbank-Verbindung
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public DataCardDavSchueler(final DBEntityManager conn, final long idSchuljahresabschnitt) throws ApiOperationException {
		super(conn, idSchuljahresabschnitt);
	}


	/**
	 * Mappt die Informationen zu einem Schüler auf einen Kontakt/Adressbucheintrag.
	 *
	 * @param schueler     die Informationen zum schüler
	 * @param nummern      die Liste der Telefonnummern des Schuelers
	 * @param ort          der Wohnort des Schuelers
	 * @param categories   die Liste an Kategorien, die dem Schueler zugeordnet werden sollen
	 *
	 * @return der Kontakt
	 */
	private AdressbuchEintrag mapContact(final DTOSchueler schueler, final List<Telefonnummer> nummern, final DTOOrt ort, final Set<String> categories) {
		final AdressbuchKontakt k = new AdressbuchKontakt();
		k.id = getKontaktId(schueler.ID);
		k.email = schueler.Email;

		if (nummern != null)
			k.telefonnummern.addAll(nummern);
		addStandardTelefonnummer(schueler.Fax, k.telefonnummern, "cell");
		addStandardTelefonnummer(schueler.Telefon, k.telefonnummern, "voice");

		k.hausnummer = schueler.HausNr;
		k.hausnummerZusatz = schueler.HausNrZusatz;
		k.nachname = schueler.Nachname;
		if (ort != null) {
			k.plz = ort.PLZ;
			k.ort = ort.Bezeichnung;
		}
		k.strassenname = schueler.Strassenname;
		k.vorname = schueler.Vorname;
		k.rolle = "Schüler";
		k.organisation = getSchulname();
		if (categories != null)
			k.kategorien.addAll(categories);
		return k;
	}


	/**
	 * Gibt true zurück, wenn der Schüler durch den Filter durchgelassen werden soll.
	 *
	 * @param schueler   der Schüler
	 *
	 * @return true, wenn der Schüler durch den Filter durchgelassen werden soll, und ansonsten false
	 */
	static boolean filterBySchuelerStatus(final SchuelerListeEintrag schueler) {
		final SchuelerStatus status = SchuelerStatus.data().getWertByID(schueler.status);
		return (status == SchuelerStatus.AKTIV) || (status == SchuelerStatus.EXTERN)
				|| (status == SchuelerStatus.NEUAUFNAHME) || (status == SchuelerStatus.BEURLAUBT);
	}


	@Override
	public String getKontaktId(final long id) {
		return "Schueler_" + schuljahresabschnitt.schuljahr + "_" + schuljahresabschnitt.abschnitt + "_" + id;
	}


	@Override
	public List<AdressbuchEintrag> getKontakte(final String idBook, final boolean withPayload) throws ApiOperationException {
		final List<AdressbuchEintrag> result = new ArrayList<>();
		if (!conn.getUser().pruefeKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN))
			return result;

		// Bestimme zunächst die Schülerliste für den Schuljahresabschnitt und filtere anschließend die relevanten Schüler anhand des Schüler-Status
		final List<SchuelerListeEintrag> listSchueler = DataSchuelerliste.getListeSchueler(conn, schuljahresabschnitt.id, false).stream()
				.filter(s -> filterBySchuelerStatus(s)).toList();

		// Wenn keine Payload erzeugt wird, so können leere Adressbuch-Einträge zurückgegeben werden ...
		if (!withPayload)
			return listSchueler.stream().map(s -> mapEmptyContact(s.id)).toList();

		// ... ansonsten müssen die entsprechenden Daten zusammengestellt werden.

		// Bestimme nun die vollständigen Schüler-DTOs
		final List<Long> idsSchueler = listSchueler.stream().map(s -> s.id).toList();
		if (idsSchueler.isEmpty())
			return result;
		final List<DTOSchueler> listDTOSchueler = new DataSchuelerStammdaten(conn).getDTOList(idsSchueler);

		final Set<Long> idsOrte = listDTOSchueler.stream().map(s -> s.Ort_ID).collect(Collectors.toSet());

		final Map<Long, DTOOrt> mapOrtID = queryMapOrte(idsOrte);
		final Map<Long, List<Telefonnummer>> mapTelefonnummernBySchuelerId = getMapTelefonnummernBySchuelerId(idsSchueler);
		final Map<Long, Set<String>> mapCategoriesBySchuelerId = getCategoriesById(listDTOSchueler);
		for (final DTOSchueler s : listDTOSchueler) {
			final List<Telefonnummer> telefonnummern = mapTelefonnummernBySchuelerId.get(s.ID);
			final AdressbuchEintrag eintrag = mapContact(s, telefonnummern, mapOrtID.get(s.Ort_ID), mapCategoriesBySchuelerId.get(s.ID));
			result.add(eintrag);
		}
		return result;
	}


	/**
	 * Erzeugt eine Map mit einer Liste aller Telefonnummern eines Schüler zugeordnet zu dessen ID.
	 * Die Daten werden über {@link DTOSchuelerTelefon} aus der Datenbank eingelesen.
	 *
	 * @param idsSchueler   die IDs der Schueler, für welche die Telefonnummern eingelesen werden sollen
	 *
	 * @return die Map
	 */
	private Map<Long, List<Telefonnummer>> getMapTelefonnummernBySchuelerId(final Collection<Long> idsSchueler) {
		final Map<Long, List<Telefonnummer>> result = new HashMap<>();

		// Erstelle eine Map für die Telefonarten
		final Map<Long, DTOTelefonArt> mapTelefonartById =
				conn.queryAll(DTOTelefonArt.class).stream().collect(Collectors.toMap(ta -> ta.ID, Function.identity()));

		// Lese die Datensätze aus der Datenbank und erzeuge die Map-Einträge
		final List<DTOSchuelerTelefon> dtoSchuelerTelefonQueryResult =
				conn.queryList(DTOSchuelerTelefon.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerTelefon.class, idsSchueler);
		for (final DTOSchuelerTelefon dto : dtoSchuelerTelefonQueryResult) {
			final DTOTelefonArt art = mapTelefonartById.get(dto.TelefonArt_ID);
			if (dto.Gesperrt.booleanValue() || (dto.Telefonnummer == null) || (art == null) || !art.Sichtbar.booleanValue())
				continue;

			final List<Telefonnummer> nummern = result.computeIfAbsent(dto.Schueler_ID, s -> new ArrayList<>());
			final Telefonnummer tel = new Telefonnummer();
			tel.number = dto.Telefonnummer;
			tel.type = art.Bezeichnung;
			nummern.add(tel);
		}
		return result;
	}


	/**
	 * Hilfsmethode für die Suche aller Kategorien (für Gruppen im Adressbuch) zu den Schüler-IDs.
	 *
	 * @param listSchueler   die Liste der Schüler
	 *
	 * @return eine Map, welche den Schüler-IDs die Liste der zugehörigen Kategorien zugeordnet
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	Map<Long, Set<String>> getCategoriesById(final List<DTOSchueler> listSchueler) throws ApiOperationException {
		// Die Map mit der Zuordnung der einzelnen Kategorien der Schüler zu deren Schüler-IDs
		final Map<Long, Set<String>> result = new HashMap<>();

		// Die Liste der Schüler-IDs für spätere Datenbankzugriffe
		final List<Long> idsSchueler = listSchueler.stream().map(s -> s.ID).toList();

		// Kategorie "Neuaufnahmen" setzen
		final Set<Long> setNeuaufnahmen =
				listSchueler.stream().filter(schueler -> SchuelerStatus.data().getWertByIDOrNull(schueler.idStatus) == SchuelerStatus.NEUAUFNAHME)
						.map(s -> s.ID).collect(Collectors.toSet());
		setNeuaufnahmen.stream().forEach(idSchueler -> result.computeIfAbsent(idSchueler, s -> new HashSet<>()).add("Neuaufnahmen"));

		// Kategorie zur Jahrgangs- und Klassenzugehörigkeit anhand des Lernabschnittes
		final Map<Long, String> mapJahrgangById = conn.queryAll(DTOJahrgang.class).stream().collect(Collectors.toMap(j -> j.ID, j -> j.InternKrz));
		final Map<Long, DTOKlassen> mapKlassenById = new DataKlassendaten(conn).getDTOsBySchuljahresabschnittId(schuljahresabschnitt.id)
				.stream().collect(Collectors.toMap(s -> s.ID, Function.identity()));
		final List<DTOSchuelerLernabschnittsdaten> listLernabschnitte =
				conn.queryList("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.Schuljahresabschnitts_ID = ?2",
						DTOSchuelerLernabschnittsdaten.class, idsSchueler, schuljahresabschnitt.id);
		for (final DTOSchuelerLernabschnittsdaten lernabschnitt : listLernabschnitte) {
			final DTOKlassen klasse = mapKlassenById.get(lernabschnitt.Klassen_ID);
			if (klasse == null)
				continue;

			final Set<String> categories = result.computeIfAbsent(lernabschnitt.Schueler_ID, s -> new HashSet<>());
			final String krzJahrgang = mapJahrgangById.get(klasse.Jahrgang_ID);
			if (setNeuaufnahmen.contains(lernabschnitt.Schueler_ID)) {
				if (klasse.Klasse != null)
					categories.add("Neuaufnahmen %s %s".formatted(klasse.Klasse, strSchuljahresabschnitt));
				if (krzJahrgang != null)
					categories.add("Neuaufnahmen Jahrgang %s %s".formatted(klasse.Jahrgang_ID, strSchuljahresabschnitt));
			}

			if (klasse.Klasse != null)
				categories.add("Klasse %s %s".formatted(klasse.Klasse, strSchuljahresabschnitt));
			if (krzJahrgang != null)
				categories.add("Jahrgang %s %s".formatted(klasse.Jahrgang_ID, strSchuljahresabschnitt));
		}

		// Kategorie Kurs
		final Map<Long, DTOKurs> mapKursById = conn.queryList(DTOKurs.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKurs.class, schuljahresabschnitt.id)
				.stream().collect(Collectors.toMap(k -> k.ID, k -> k));
		final List<DTOKursSchueler> dtoKursSchuelerQueryResult =
				conn.queryList("SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID IN ?1 AND e.LernabschnittWechselNr = 0", DTOKursSchueler.class,
						mapKursById.keySet());
		for (final DTOKursSchueler dtoKursSchueler : dtoKursSchuelerQueryResult) {
			final DTOKurs dtoKurs = mapKursById.get(dtoKursSchueler.Kurs_ID);
			if (dtoKurs == null)
				continue;

			final Set<String> categories = result.computeIfAbsent(dtoKursSchueler.Schueler_ID, s -> new HashSet<>());
			final String krzJahrgang = mapJahrgangById.get(dtoKurs.Jahrgang_ID);
			if (krzJahrgang != null)
				categories.add("Kurs %s %s %s".formatted(dtoKurs.KurzBez, krzJahrgang, strSchuljahresabschnitt));
			else {
				// TODO Jahrgangsübergreifende Kurse: "Kurs %s %s %s", Beispiel "Kurs AG-Netzwerk (05,06,07) 2024/25.2"
			}
		}
		return result;
	}

}
