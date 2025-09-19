package de.svws_nrw.davapi.data.carddav;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung;
import de.svws_nrw.core.data.adressbuch.AdressbuchEintrag;
import de.svws_nrw.core.data.adressbuch.AdressbuchKontakt;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtBefaehigung;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Diese Klasse dient dem Zugriff auf die Datenbank, um ein Lehrer-Adressbuch für einen Schuljahresabschnitt zu generieren.
 */
public final class DataCardDavLehrer extends DataManagerCardDav {

	/**
	 * Erzeugt ein neues Lehrer-Adressbuch für den übergebenen Schuljahresabschnitt
	 *
	 * @param conn                     die Datenbank-Verbindung
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public DataCardDavLehrer(final DBEntityManager conn, final long idSchuljahresabschnitt) throws ApiOperationException {
		super(conn, idSchuljahresabschnitt);
	}


	/**
	 * Mappt die Informationen zu einem Lehrer auf einen Kontakt/Adressbucheintrag.
	 *
	 * @param l          der Lehrer
	 * @param ort        der Wohnort des Lehrers
	 * @param categories die Kategorien, die diesem Lehrer zugeordnet sind
	 *
	 * @return der Kontakt
	 */
	private AdressbuchEintrag mapContact(final DTOLehrer l, final DTOOrt ort, final Set<String> categories) {
		final AdressbuchKontakt k = new AdressbuchKontakt();
		k.id = getKontaktId(l.ID);
		if (l.eMailDienstlich != null)
			k.email = l.eMailDienstlich;
		if (l.telefon != null)
			addStandardTelefonnummer(l.telefon, k.telefonnummern, "voice");
		if (l.telefonMobil != null)
			addStandardTelefonnummer(l.telefonMobil, k.telefonnummern, "cell");
		k.hausnummer = l.HausNr;
		k.hausnummerZusatz = l.HausNrZusatz;
		k.nachname = l.Nachname;
		if (ort != null) {
			k.plz = ort.PLZ;
			k.ort = ort.Bezeichnung;
		}
		if (categories != null)
			k.kategorien.addAll(categories);
		k.strassenname = l.Strassenname;
		k.vorname = l.Vorname;
		k.rolle = "Lehrer";
		k.organisation = getSchulname();
		return k;
	}


	@Override
	public String getKontaktId(final long id) {
		return "Lehrer_" + schuljahresabschnitt.schuljahr + "_" + schuljahresabschnitt.abschnitt + "_" + id;
	}


	@Override
	public List<AdressbuchEintrag> getKontakte(final String idBook, final boolean withPayload) throws ApiOperationException {
		final List<AdressbuchEintrag> result = new ArrayList<>();
		if (!conn.getUser().pruefeKompetenz(BenutzerKompetenz.LEHRERDATEN_ANSEHEN))
			return result;

		// Bestimme alle sichtbaren Lehrer
		final List<DTOLehrer> listLehrer = conn.queryList(DTOLehrer.QUERY_BY_SICHTBAR, DTOLehrer.class, true);
		if (listLehrer.isEmpty())
			return result;

		// Wenn keine Payload erzeugt wird, so können leere Adressbuch-Einträge zurückgegeben werden ...
		if (!withPayload)
			return listLehrer.stream().map(l -> mapEmptyContact(l.ID)).toList();

		// ... ansonsten müssen die entsprechenden Daten zusammengestellt werden.
		final Set<Long> idsOrte = listLehrer.stream().map(l -> l.Ort_ID).collect(Collectors.toSet());
		final Map<Long, DTOOrt> mapOrtID = queryMapOrte(idsOrte);
		final Map<Long, Set<String>> categoriesbyLehrerId = getCategoriesById();

		for (final DTOLehrer l : listLehrer)
			result.add(mapContact(l, mapOrtID.get(l.Ort_ID), categoriesbyLehrerId.get(l.ID)));

		return result;
	}


	/**
	 * Hilfsmethode für die Suche aller Kategorien (für Gruppen im Adressbuch) zu den Lehrer-IDs.
	 *
	 * @return eine Map, welche den Lehrer-IDs die Liste der zugehörigen Kategorien zugeordnet
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private Map<Long, Set<String>> getCategoriesById() {
		final Map<Long, Set<String>> result = new HashMap<>();

		// TODO Kategorie für Klassenteams (alle dort unterrichtenden Lehrer)

		// Kategorien zu Klassenlehrern und Jahrgangsteams anhand der Klassen
		final Map<Long, String> mapJahrgangById = conn.queryAll(DTOJahrgang.class).stream().collect(Collectors.toMap(j -> j.ID, j -> j.InternKrz));
		final Map<Long, DTOKlassen> mapKlasseById = conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, schuljahresabschnitt.id)
				.stream().collect(Collectors.toMap(k -> k.ID, k -> k));
		final List<DTOKlassenLeitung> listKlassenleitungen =
				conn.queryList(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID, DTOKlassenLeitung.class, mapKlasseById.keySet());
		for (final DTOKlassenLeitung kl : listKlassenleitungen) {
			final Set<String> categories = result.computeIfAbsent(kl.Lehrer_ID, s -> new HashSet<>());
			final DTOKlassen klasse = mapKlasseById.get(kl.Klassen_ID);
			if (klasse == null)
				continue;
			final String jahrgang = mapJahrgangById.get(klasse.Jahrgang_ID);
			if (klasse.Klasse != null)
				categories.add("Klassenlehrer %s %s".formatted(klasse.Klasse, strSchuljahresabschnitt));
			categories.add("Klassenlehrer %s".formatted(strSchuljahresabschnitt));
			if (jahrgang != null)
				categories.add("Klassenlehrer JG %s %s".formatted(jahrgang, strSchuljahresabschnitt));
			if (jahrgang != null)
				categories.add("Jahrgangsteam %s %s".formatted(jahrgang, strSchuljahresabschnitt));
		}

		// Kategorie der Jahrgangsteams anhand der Kurse (ergänzend zu den Klassenlehrern)
		final List<DTOKurs> dtoKursQueryResult = conn.queryList(DTOKurs.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKurs.class, schuljahresabschnitt.id);
		for (final DTOKurs k : dtoKursQueryResult) {
			final Set<String> categories = result.computeIfAbsent(k.Lehrer_ID, s -> new HashSet<>());
			final String jahrgang = mapJahrgangById.get(k.Jahrgang_ID);
			if (jahrgang != null)
				categories.add("Jahrgangsteam %s %s".formatted(jahrgang, strSchuljahresabschnitt));
		}

		// Kategorie der Fachschaften
		final List<DTOLehrerPersonaldatenLehramt> dtoLehraemter = conn.queryAll(DTOLehrerPersonaldatenLehramt.class);
		final Map<Long, List<DTOLehrerPersonaldatenLehramtBefaehigung>> mapLehraemterLehrbefaehigungen =
				conn.queryAll(DTOLehrerPersonaldatenLehramtBefaehigung.class).stream().collect(Collectors.groupingBy(b -> b.Lehreramt_ID));
		for (final DTOLehrerPersonaldatenLehramt lehramt : dtoLehraemter) {
			final List<DTOLehrerPersonaldatenLehramtBefaehigung> befaehigungen = mapLehraemterLehrbefaehigungen.get(lehramt.ID);
			if (befaehigungen == null)
				continue;
			for (final DTOLehrerPersonaldatenLehramtBefaehigung befaehigung : befaehigungen) {
				final LehrerLehrbefaehigungKatalogEintrag bef = LehrerLehrbefaehigung.data().getEintragByID(befaehigung.Lehrbefaehigung_Katalog_ID);
				if (bef == null)
					continue;
				final Set<String> categories = result.computeIfAbsent(lehramt.Lehrer_ID, s -> new HashSet<>());
				categories.add("Fachschaft %s %s".formatted(bef.kuerzel, strSchuljahresabschnitt));
			}
		}
		return result;
	}

}
