package de.svws_nrw.davapi.data.repos.bycategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.adressbuch.AdressbuchEintrag;
import de.svws_nrw.core.data.adressbuch.AdressbuchKontakt;
import de.svws_nrw.core.data.adressbuch.Telefonnummer;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.davapi.data.CollectionRessourceQueryParameters;
import de.svws_nrw.davapi.data.IAdressbuchKontaktRepository;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLehramtBefaehigung;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;

/**
 * Diese Implementierung des {@link IAdressbuchKontaktRepository} dient der
 * Suche nach Lehrern aus der Datenbank als {@link AdressbuchEintrag}, welche
 * mit Kategorien ergänzt werden.
 *
 */
public final class LehrerWithCategoriesRepository implements IAdressbuchKontaktRepository {

	/**
	 * die Datenbankverbindung
	 */
	private final DBEntityManager conn;
	/**
	 * der aktuelle Schuljahresabschnitt
	 */
	private final DTOSchuljahresabschnitte aktuellerSchuljahresabschnitt;
	/**
	 * Utility zum Erzeugen von Adressbuchkategorien
	 */
	private final AdressbuchKategorienUtil kategorienUtil;
	/**
	 * Name der eigenen Schule
	 */
	private final String schulName;
	/** der Benutzer, dessen Adressbuecher gesucht werden */
	private final Benutzer user;

	/**
	 * Konstruktor zum Erstellen des Repositories mit einer Datenbankverbindung
	 *
	 * @param conn                          die Datenbankverbindung
	 * @param user                          der Benutzer, dessen Adressbuecher
	 *                                      gesucht werden
	 * @param kategorienUtil                Instanz der Utility zum Erzeugen von
	 *                                      Adressbuchkategorien
	 * @param aktuellerSchuljahresabschnitt der aktuelle Schuljahresabschnitt für
	 *                                      weitere Queries
	 */
	public LehrerWithCategoriesRepository(final DBEntityManager conn, final Benutzer user,
			final DTOSchuljahresabschnitte aktuellerSchuljahresabschnitt, final AdressbuchKategorienUtil kategorienUtil) {
		this.user = user;
		this.conn = conn;
		this.aktuellerSchuljahresabschnitt = aktuellerSchuljahresabschnitt;
		this.kategorienUtil = kategorienUtil;
		this.schulName = IAdressbuchKontaktRepository.getSchulname(conn);
	}

	@Override
	public List<AdressbuchEintrag> getKontakteByAdressbuch(final String adressbuchId,
			final CollectionRessourceQueryParameters params) {
		if (!params.includeRessources || !user.pruefeKompetenz(BenutzerKompetenz.LEHRERDATEN_ANSEHEN)) {
			return new ArrayList<>();
		}
		final List<DTOLehrer> dtoLehrerResult = conn.queryNamed("DTOLehrer.sichtbar", true, DTOLehrer.class);
		if (params.includeEintragIDs && !params.includeEintragPayload) {
			return dtoLehrerResult.stream().map(e -> {
				final AdressbuchEintrag a = new AdressbuchEintrag();
				a.id = IAdressbuchKontaktRepository.createLehrerId(e.ID);
				return a;
			}).toList();
		}
		final Map<Long, DTOLehrer> lehrerById = dtoLehrerResult.stream().collect(Collectors.toMap(l -> l.ID, l -> l));
		final Map<Long, Set<String>> categoriesbyLehrerId = getCategoriesByLehrerId();
		final Map<Long, DTOOrt> orteByOrtIds = IAdressbuchKontaktRepository
				.queryOrteByOrtIds(dtoLehrerResult.stream().map(l -> l.Ort_ID).collect(Collectors.toSet()), conn);
		return lehrerById.values().stream()
				.map(l -> createKontaktFromlehrer(l, orteByOrtIds.get(l.Ort_ID), categoriesbyLehrerId.get(l.ID)))
				.toList();
	}

	/**
	 * Erstellt aus einem {@link DTOLehrer} einen {@link AdressbuchEintrag}
	 *
	 * @param l          der Lehrer
	 * @param ort        der Wohnort des Lehrers
	 * @param categories die Kategorien, die diesem Lehrer zugeordnet sind
	 * @return den Adressbucheintrag, der den Lehrer repräsentiert
	 */
	private AdressbuchEintrag createKontaktFromlehrer(final DTOLehrer l, final DTOOrt ort, final Set<String> categories) {
		final AdressbuchKontakt k = new AdressbuchKontakt();
		k.id = IAdressbuchKontaktRepository.createLehrerId(l.ID);
		if (l.eMailDienstlich != null) {
			k.email = l.eMailDienstlich;
		}
		Telefonnummer t = new Telefonnummer();
		if (l.telefon != null) {
			t.number = l.telefon;
			t.type = "voice";
			k.telefonnummern.add(t);
		}
		if (l.telefonMobil != null) {
			t = new Telefonnummer();
			t.number = l.telefonMobil;
			t.type = "cell";
			k.telefonnummern.add(t);
		}

		k.hausnummer = l.HausNr;
		k.hausnummerZusatz = l.HausNrZusatz;
		k.nachname = l.Nachname;

		IAdressbuchKontaktRepository.applyOrtToKontakt(k, ort);
		if (categories != null)
			k.kategorien.addAll(categories);
		k.strassenname = l.Strassenname;
		k.vorname = l.Vorname;
		k.rolle = "Lehrer";
		k.organisation = schulName;
		return k;
	}

	/**
	 * Erstellt eine Map der Kategorien nach LehrerId
	 *
	 * @return eine Map, in der alle Kategorien eines Lehrers der Lehrer ID
	 *         zugeordnet sind
	 */
	private Map<Long, Set<String>> getCategoriesByLehrerId() {
		final Map<Long, Set<String>> result = new HashMap<>();
		// Klassenlehrer, Klassenlehrer Jahrgang, Klassenlehrer Klasse

		final List<DTOKlassen> dtoKlassenQueryResult = conn.queryNamed("DTOKlassen.schuljahresabschnitts_id",
				aktuellerSchuljahresabschnitt.ID, DTOKlassen.class);
		final Map<Long, DTOKlassen> klassenByKlassenId = dtoKlassenQueryResult.stream()
				.collect(Collectors.toMap(k -> k.ID, k -> k));
		final List<DTOKlassenLeitung> dtoKlassenLeitungQueryResult = conn.queryNamed("DTOKlassenLeitung.klassen_id.multiple",
				klassenByKlassenId.keySet(), DTOKlassenLeitung.class);
		final Map<Long, String> jahrgangKrzByJahrgangId = conn.queryNamed("DTOJahrgang.sichtbar", true, DTOJahrgang.class)
				.stream().collect(Collectors.toMap(j -> j.ID, j -> j.InternKrz));

		for (final DTOKlassenLeitung kl : dtoKlassenLeitungQueryResult) {
			final Set<String> categories = result.computeIfAbsent(kl.Lehrer_ID, s -> new HashSet<>());
			final DTOKlassen dtoKlassen = klassenByKlassenId.get(kl.Klassen_ID);
			final String klasse = dtoKlassen.Klasse;
			final String jahrgang = jahrgangKrzByJahrgangId.get(dtoKlassen.Jahrgang_ID);
			categories.add(kategorienUtil.formatKlassenlehrer(klasse));
			categories.add(kategorienUtil.formatKlassenlehrerAlle());
			categories.add(kategorienUtil.formatKlassenlehrerJahrgang(jahrgang));
			categories.add(kategorienUtil.formatLehrerJahrgangsteam(jahrgang));
		}

		final List<DTOKurs> dtoKursQueryResult = conn.queryNamed("DTOKurs.schuljahresabschnitts_id",
				aktuellerSchuljahresabschnitt.ID, DTOKurs.class);
		for (final DTOKurs k : dtoKursQueryResult) {
			final Set<String> categories = result.computeIfAbsent(k.Lehrer_ID, s -> new HashSet<>());
			final String jahrgang = jahrgangKrzByJahrgangId.get(k.Jahrgang_ID);
			categories.add(kategorienUtil.formatLehrerJahrgangsteam(jahrgang));
		}

		final List<DTOLehrerLehramtBefaehigung> dtoLehrerLehramtBefaehigungQueryResult = conn
				.queryNamed("DTOLehrerLehramtBefaehigung.all", DTOLehrerLehramtBefaehigung.class).getResultList();
		for (final DTOLehrerLehramtBefaehigung k : dtoLehrerLehramtBefaehigungQueryResult) {
			final Set<String> categories = result.computeIfAbsent(k.Lehrer_ID, s -> new HashSet<>());
			categories.add(kategorienUtil.formatLehrerFachschaft(k.LehrbefKrz));
		}
		return result;
	}
}
