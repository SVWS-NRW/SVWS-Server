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

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.data.adressbuch.AdressbuchEintrag;
import de.svws_nrw.core.data.adressbuch.AdressbuchKontakt;
import de.svws_nrw.core.data.adressbuch.Telefonnummer;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerTelefon;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOTelefonArt;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schueler.SchuelerServiceFactory;

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
	 * @param schueler     die Informationen zum Schüler
	 * @param nummern      die Liste der Telefonnummern des Schülers
	 * @param ort          der Wohnort des Schülers
	 * @param categories   die Liste an Kategorien, die dem Schüler zugeordnet werden sollen
	 *
	 * @return der Kontakt
	 */
	private AdressbuchEintrag mapContact(final SchuelerStammdaten schueler, final List<Telefonnummer> nummern, final DTOOrt ort, final Set<String> categories) {
		final AdressbuchKontakt k = new AdressbuchKontakt();
		k.id = getKontaktId(schueler.id);
		k.email = schueler.emailPrivat;

		if (nummern != null) {
			k.telefonnummern.addAll(nummern);
		}
		addStandardTelefonnummer(schueler.telefonMobil, k.telefonnummern, "cell");
		addStandardTelefonnummer(schueler.telefon, k.telefonnummern, "voice");

		k.hausnummer = schueler.hausnummer;
		k.hausnummerZusatz = schueler.hausnummerZusatz;
		k.nachname = schueler.nachname;
		if (ort != null) {
			k.plz = ort.plz;
			k.ort = ort.ortsname;
		}
		k.strassenname = schueler.strassenname;
		k.vorname = schueler.vorname;
		k.rolle = "Schüler";
		k.organisation = getSchulname();
		if (categories != null) {
			k.kategorien.addAll(categories);
		}
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
		final SchuelerStatus status = SchuelerStatus.data().getWertByID((long) schueler.status);
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
		if (!conn.getUser().pruefeKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)) {
			return result;
		}

		// Bestimme zunächst die Schülerliste für den Schuljahresabschnitt und filtere anschließend die relevanten Schüler anhand des Schüler-Status
		final List<SchuelerListeEintrag> listSchueler = DataSchuelerliste.getListeSchueler(conn, schuljahresabschnitt.id, false).stream()
				.filter(DataCardDavSchueler::filterBySchuelerStatus).toList();

		// Wenn keine Payload erzeugt wird, so können leere Adressbuch-Einträge zurückgegeben werden ...
		if (!withPayload) {
			return listSchueler.stream().map(s -> mapEmptyContact(s.id)).toList();
		}

		// ... ansonsten müssen die entsprechenden Daten zusammengestellt werden.

		// Bestimme nun die vollständigen Schüler-DTOs
		final List<Long> idsSchueler = listSchueler.stream().map(s -> s.id).toList();
		if (idsSchueler.isEmpty()) {
			return result;
		}
		final var schueler = SchuelerServiceFactory.getNewInstance().getSchuelerStammdatenService().getList(idsSchueler);

		final Set<Long> idsOrte = schueler.stream().map(s -> s.wohnortID).collect(Collectors.toSet());

		final Map<Long, DTOOrt> mapOrtID = queryMapOrte(idsOrte);
		final Map<Long, List<Telefonnummer>> mapTelefonnummernBySchuelerId = getMapTelefonnummernBySchuelerId(idsSchueler);
		final Map<Long, Set<String>> mapCategoriesBySchuelerId = getCategoriesById(schueler);
		for (final var s : schueler) {
			final List<Telefonnummer> telefonnummern = mapTelefonnummernBySchuelerId.get(s.id);
			final AdressbuchEintrag eintrag = mapContact(s, telefonnummern, mapOrtID.get(s.wohnortID), mapCategoriesBySchuelerId.get(s.id));
			result.add(eintrag);
		}
		return result;
	}


	/**
	 * Erzeugt eine Map mit einer Liste aller Telefonnummern eines Schüler zugeordnet zu dessen ID.
	 * Die Daten werden über {@link DTOSchuelerTelefon} aus der Datenbank eingelesen.
	 *
	 * @param idsSchueler   die IDs der Schüler, für welche die Telefonnummern eingelesen werden sollen
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
			if (Boolean.TRUE.equals(dto.Gesperrt)
					|| (dto.Telefonnummer == null)
					|| (art == null)
					|| !Boolean.TRUE.equals(art.Sichtbar)) {
				continue;
			}

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
	 * @param schueler   die Liste der Schüler
	 *
	 * @return eine Map, welche den Schüler-IDs die Liste der zugehörigen Kategorien zugeordnet
	 */
	Map<Long, Set<String>> getCategoriesById(final List<SchuelerStammdaten> schueler) {
		final Map<Long, Set<String>> result = new HashMap<>();
		final List<Long> idsSchueler = schueler.stream()
				.map(s -> s.id)
				.toList();

		final Set<Long> setNeuaufnahmen = collectNeuaufnahmen(schueler);
		setNeuaufnahmen.forEach(idSchueler -> result.computeIfAbsent(idSchueler, s -> new HashSet<>()).add("Neuaufnahmen"));

		final Map<Long, String> jahrgangById = conn.queryAll(DTOJahrgang.class).stream()
				.collect(Collectors.toMap(j -> j.ID, j -> j.InternKrz));
		final Map<Long, DTOKlassen> klassenById = new DataKlassendaten(conn)
				.getDTOsBySchuljahresabschnittId(schuljahresabschnitt.id)
				.stream().collect(Collectors.toMap(s -> s.ID, Function.identity()));

		addLernabschnittKategorien(result, idsSchueler, jahrgangById, klassenById, setNeuaufnahmen);
		addKursKategorien(result, jahrgangById);

		return result;
	}

	private Set<Long> collectNeuaufnahmen(final List<SchuelerStammdaten> schueler) {
		return schueler.stream()
				.filter(s -> SchuelerStatus.data().getWertByIDOrNull((long) s.status) == SchuelerStatus.NEUAUFNAHME)
				.map(s -> s.id)
				.collect(Collectors.toSet());
	}

	private void addLernabschnittKategorien(
			final Map<Long, Set<String>> result,
			final List<Long> idsSchueler,
			final Map<Long, String> mapJahrgangById,
			final Map<Long, DTOKlassen> mapKlassenById,
			final Set<Long> setNeuaufnahmen
	) {
		final List<DTOSchuelerLernabschnittsdaten> listLernabschnitte =
				conn.queryList("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.Schuljahresabschnitts_ID = ?2",
						DTOSchuelerLernabschnittsdaten.class, idsSchueler, schuljahresabschnitt.id);
		for (final DTOSchuelerLernabschnittsdaten lernabschnitt : listLernabschnitte) {
			final DTOKlassen klasse = mapKlassenById.get(lernabschnitt.Klassen_ID);
			if (klasse == null) {
				continue;
			}

			final Set<String> categories = result.computeIfAbsent(lernabschnitt.Schueler_ID, s -> new HashSet<>());
			final String krzJahrgang = mapJahrgangById.get(klasse.Jahrgang_ID);
			if (setNeuaufnahmen.contains(lernabschnitt.Schueler_ID)) {
				if (klasse.Klasse != null) {
					categories.add("Neuaufnahmen %s %s".formatted(klasse.Klasse, strSchuljahresabschnitt));
				}
				if (krzJahrgang != null) {
					categories.add("Neuaufnahmen Jahrgang %s %s".formatted(klasse.Jahrgang_ID, strSchuljahresabschnitt));
				}
			}

			if (klasse.Klasse != null) {
				categories.add("Klasse %s %s".formatted(klasse.Klasse, strSchuljahresabschnitt));
			}
			if (krzJahrgang != null) {
				categories.add("Jahrgang %s %s".formatted(klasse.Jahrgang_ID, strSchuljahresabschnitt));
			}
		}
	}

	private void addKursKategorien(final Map<Long, Set<String>> result, final Map<Long, String> mapJahrgangById) {
		final Map<Long, DTOKurs> mapKursById = conn.queryList(DTOKurs.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKurs.class, schuljahresabschnitt.id)
				.stream().collect(Collectors.toMap(k -> k.ID, k -> k));
		final List<DTOKursSchueler> dtoKursSchuelerQueryResult =
				conn.queryList("SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID IN ?1 AND e.LernabschnittWechselNr = 0", DTOKursSchueler.class,
						mapKursById.keySet());
		for (final DTOKursSchueler dtoKursSchueler : dtoKursSchuelerQueryResult) {
			final DTOKurs dtoKurs = mapKursById.get(dtoKursSchueler.Kurs_ID);
			if (dtoKurs == null) {
				continue;
			}

			final Set<String> categories = result.computeIfAbsent(dtoKursSchueler.Schueler_ID, s -> new HashSet<>());
			final String krzJahrgang = mapJahrgangById.get(dtoKurs.Jahrgang_ID);
			if (krzJahrgang != null) {
				categories.add("Kurs %s %s %s".formatted(dtoKurs.KurzBez, krzJahrgang, strSchuljahresabschnitt));
			} else {
				// TODO Jahrgangsübergreifende Kurse: "Kurs %s %s %s", Beispiel "Kurs AG-Netzwerk (05,06,07) 2024/25.2"
			}
		}
	}

}
