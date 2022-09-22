package de.nrw.schule.svws.davapi.data.repos.bycategory;

import de.nrw.schule.svws.core.data.adressbuch.AdressbuchEintrag;
import de.nrw.schule.svws.core.data.adressbuch.AdressbuchKontakt;
import de.nrw.schule.svws.core.data.adressbuch.Telefonnummer;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.davapi.data.AdressbuchQueryParameters;
import de.nrw.schule.svws.davapi.data.IAdressbuchKontaktRepository;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOOrt;
import de.nrw.schule.svws.db.dto.current.schild.klassen.DTOKlassen;
import de.nrw.schule.svws.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.nrw.schule.svws.db.dto.current.schild.kurse.DTOKurs;
import de.nrw.schule.svws.db.dto.current.schild.lehrer.DTOLehrer;
import de.nrw.schule.svws.db.dto.current.schild.lehrer.DTOLehrerLehramtBefaehigung;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOJahrgang;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Diese Implementierung des {@link IAdressbuchKontaktRepository} dient der
 * Suche nach Lehrern aus der Datenbank als {@link AdressbuchEintrag}, welche
 * mit Kategorien ergänzt werden.
 *
 */
public class LehrerWithCategoriesRepository implements IAdressbuchKontaktRepository {

	/**
	 * die Datenbankverbindung
	 */
	private DBEntityManager conn;
	/**
	 * der aktuelle Schuljahresabschnitt
	 */
	private DTOSchuljahresabschnitte aktuellerSchuljahresabschnitt;
	/**
	 * Utility zum Erzeugen von Adressbuchkategorien
	 */
	private AdressbuchKategorienUtil kategorienUtil;
	/**
	 * Name der eigenen Schule
	 */
	private String schulName;

	/**
	 * Konstruktor zum Erstellen des Repositories mit einer Datenbankverbindung
	 * 
	 * @param conn                          die Datenbankverbindung
	 * @param kategorienUtil                Instanz der Utility zum Erzeugen von
	 *                                      Adressbuchkategorien
	 * @param aktuellerSchuljahresabschnitt der aktuelle Schuljahresabschnitt für
	 *                                      weitere Queries
	 */
	public LehrerWithCategoriesRepository(DBEntityManager conn, DTOSchuljahresabschnitte aktuellerSchuljahresabschnitt,
			AdressbuchKategorienUtil kategorienUtil) {
		this.conn = conn;
		this.aktuellerSchuljahresabschnitt = aktuellerSchuljahresabschnitt;
		this.kategorienUtil = kategorienUtil;
		this.schulName = IAdressbuchKontaktRepository.getSchulname(conn);
	}

	@Override
	public List<AdressbuchEintrag> getKontakteByAdressbuch(String adressbuchId, AdressbuchQueryParameters params) {
		if (!params.includeAdressbuchEintraege
				|| !conn.getUser().pruefeKompetenz(BenutzerKompetenz.LEHRERDATEN_ANSEHEN)) {
			return new ArrayList<>();
		}
		List<DTOLehrer> dtoLehrerResult = conn.queryNamed("DTOLehrer.sichtbar", true, DTOLehrer.class);
		if (params.includeAdressbuchEintragIDsOnly) {
			return dtoLehrerResult.stream().map(e -> {
				AdressbuchEintrag a = new AdressbuchEintrag();
				a.id = IAdressbuchKontaktRepository.createLehrerId(e.ID);
				return a;
			}).toList();
		}
		Map<Long, DTOLehrer> lehrerById = dtoLehrerResult.stream().collect(Collectors.toMap(l -> l.ID, l -> l));
		Map<Long, Set<String>> categoriesbyLehrerId = getCategoriesByLehrerId();
		Map<Long, DTOOrt> orteByOrtIds = IAdressbuchKontaktRepository
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
	private AdressbuchEintrag createKontaktFromlehrer(DTOLehrer l, DTOOrt ort, Set<String> categories) {
		AdressbuchKontakt k = new AdressbuchKontakt();
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
		Map<Long, Set<String>> result = new HashMap<>();
		// Klassenlehrer, Klassenlehrer Jahrgang, Klassenlehrer Klasse

		List<DTOKlassen> dtoKlassenQueryResult = conn.queryNamed("DTOKlassen.schuljahresabschnitts_id",
				aktuellerSchuljahresabschnitt.ID, DTOKlassen.class);
		Map<Long, DTOKlassen> klassenByKlassenId = dtoKlassenQueryResult.stream()
				.collect(Collectors.toMap(k -> k.ID, k -> k));
		List<DTOKlassenLeitung> dtoKlassenLeitungQueryResult = conn.queryNamed("DTOKlassenLeitung.klassen_id.multiple",
				klassenByKlassenId.keySet(), DTOKlassenLeitung.class);
		Map<Long, String> jahrgangKrzByJahrgangId = conn.queryNamed("DTOJahrgang.sichtbar", true, DTOJahrgang.class)
				.stream().collect(Collectors.toMap(j -> j.ID, j -> j.InternKrz));

		for (DTOKlassenLeitung kl : dtoKlassenLeitungQueryResult) {
			Set<String> categories = result.computeIfAbsent(kl.Lehrer_ID, s -> new HashSet<>());
			DTOKlassen dtoKlassen = klassenByKlassenId.get(kl.Klassen_ID);
			String klasse = dtoKlassen.Klasse;
			String jahrgang = jahrgangKrzByJahrgangId.get(dtoKlassen.Jahrgang_ID);
			categories.add(kategorienUtil.formatKlassenlehrer(klasse));
			categories.add(kategorienUtil.formatKlassenlehrerAlle());
			categories.add(kategorienUtil.formatKlassenlehrerJahrgang(jahrgang));
			categories.add(kategorienUtil.formatLehrerJahrgangsteam(jahrgang));
		}

		List<DTOKurs> dtoKursQueryResult = conn.queryNamed("DTOKurs.schuljahresabschnitts_id",
				aktuellerSchuljahresabschnitt.ID, DTOKurs.class);
		for (DTOKurs k : dtoKursQueryResult) {
			Set<String> categories = result.computeIfAbsent(k.Lehrer_ID, s -> new HashSet<>());
			String jahrgang = jahrgangKrzByJahrgangId.get(k.Jahrgang_ID);
			categories.add(kategorienUtil.formatLehrerJahrgangsteam(jahrgang));
		}

		List<DTOLehrerLehramtBefaehigung> dtoLehrerLehramtBefaehigungQueryResult = conn
				.queryNamed("DTOLehrerLehramtBefaehigung.all", DTOLehrerLehramtBefaehigung.class).getResultList();
		for (DTOLehrerLehramtBefaehigung k : dtoLehrerLehramtBefaehigungQueryResult) {
			Set<String> categories = result.computeIfAbsent(k.Lehrer_ID, s -> new HashSet<String>());
			categories.add(kategorienUtil.formatLehrerFachschaft(k.LehrbefKrz));
		}
		return result;
	}
}
