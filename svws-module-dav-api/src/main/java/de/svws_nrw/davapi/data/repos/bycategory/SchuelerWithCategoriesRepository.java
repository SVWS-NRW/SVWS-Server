package de.svws_nrw.davapi.data.repos.bycategory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.adressbuch.AdressbuchEintrag;
import de.svws_nrw.core.data.adressbuch.Telefonnummer;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.davapi.data.CollectionRessourceQueryParameters;
import de.svws_nrw.davapi.data.IAdressbuchKontaktRepository;
import de.svws_nrw.db.Benutzer;
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
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;

/**
 * Diese Implementierung des {@link IAdressbuchKontaktRepository} dient der
 * Suche nach Schuelern aus der Datenbank als {@link AdressbuchEintrag}, welche
 * mit Kategorien ergänzt werden.
 *
 */
public class SchuelerWithCategoriesRepository implements IAdressbuchKontaktRepository {

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
	/** der Benutzer, dessen Adressbuecher gesucht werden */
	private Benutzer user;

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
	public SchuelerWithCategoriesRepository(DBEntityManager conn, Benutzer user,
			DTOSchuljahresabschnitte aktuellerSchuljahresabschnitt, AdressbuchKategorienUtil kategorienUtil) {
		this.conn = conn;
		this.user = user;
		this.aktuellerSchuljahresabschnitt = aktuellerSchuljahresabschnitt;
		this.kategorienUtil = kategorienUtil;
		this.schulName = IAdressbuchKontaktRepository.getSchulname(conn);
	}

	@Override
	public List<AdressbuchEintrag> getKontakteByAdressbuch(String adressbuchId,
			CollectionRessourceQueryParameters params) {
		if (!params.includeRessources || !user.pruefeKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)) {
			return new Vector<>();
		}
		List<DTOSchueler> filteredDtoSchuelers = conn.queryNamed("DTOSchueler.all", DTOSchueler.class).getResultStream()
				.filter(SCHUELER_FILTER).toList();

		if (params.includeEintragIDs && !params.includeEintragPayload) {
			return filteredDtoSchuelers.stream().map(s -> {
				AdressbuchEintrag a = new AdressbuchEintrag();
				a.id = IAdressbuchKontaktRepository.createSchuelerId(s.ID);
				return a;
			}).toList();
		}

		Set<Long> ortIds = filteredDtoSchuelers.stream().map(s -> s.Ort_ID).collect(Collectors.toSet());
		List<Long> schuelerIds = filteredDtoSchuelers.stream().map(s -> s.ID).toList();
		Map<Long, DTOOrt> ortByOrtID = IAdressbuchKontaktRepository.queryOrteByOrtIds(ortIds, conn);
		Map<Long, List<Telefonnummer>> telefonnummerBySchuelerId = queryTelefonNummernBySchuelerIds(schuelerIds, conn);

		Map<Long, SchuelerStatus> schuelerStatusById = filteredDtoSchuelers.stream()
				.collect(Collectors.toMap(s -> s.ID, s -> s.Status));
		Map<Long, Set<String>> categoriesBySchuelerId = getCategoriesBySchuelerId(schuelerStatusById);
		return filteredDtoSchuelers.stream()
				.map(s -> IAdressbuchKontaktRepository.mapDTOSchuelerToKontakt(s, telefonnummerBySchuelerId.get(s.ID),
						ortByOrtID.get(s.Ort_ID), categoriesBySchuelerId.get(s.ID), schulName))
				.toList();
	}

	/**
	 * Sucht und erzeugt eine Map, in der jeder gegebenen {@link DTOSchueler#ID}
	 * über {@link DTOSchuelerTelefon} eine Liste von Telefonnummern zugeordnet ist.
	 * Schueler, denen über {@link DTOSchuelerTelefon} keine Telefonnummern
	 * zugeordnet sind, tauchen in der Map nicht auf.
	 *
	 * @param schuelerIds ids der Schueler, für die nach Telefonnummern gesucht
	 *                    werden soll
	 * @param conn        die Datenbankverbindung
	 * @return eine Map, in der jeder Schueler ID eine Liste von Telefonnummern
	 *         zugeordnet ist
	 */
	static Map<Long, List<Telefonnummer>> queryTelefonNummernBySchuelerIds(Collection<Long> schuelerIds,
			DBEntityManager conn) {
		List<DTOSchuelerTelefon> dtoSchuelerTelefonQueryResult = conn
				.queryNamed("DTOSchuelerTelefon.schueler_id.multiple", schuelerIds, DTOSchuelerTelefon.class);
		List<DTOTelefonArt> dtoTelefonArtQueryResults = conn.queryNamed("DTOTelefonArt.all", DTOTelefonArt.class)
				.getResultList();
		Map<Long, DTOTelefonArt> telefonArtById = dtoTelefonArtQueryResults.stream()
				.collect(Collectors.toMap(ta -> ta.ID, Function.identity()));
		Map<Long, List<Telefonnummer>> telefonnummerBySchuelerId = new HashMap<>();

		for (DTOSchuelerTelefon dto : dtoSchuelerTelefonQueryResult) {
			DTOTelefonArt dtoTelefonArt = telefonArtById.get(dto.TelefonArt_ID);
			if (dto.Gesperrt.booleanValue() || dto.Telefonnummer == null || !dtoTelefonArt.Sichtbar.booleanValue()) {
				continue;
			}
			List<Telefonnummer> telefonnummern = telefonnummerBySchuelerId.get(dto.Schueler_ID);

			if (telefonnummern == null) {
				telefonnummern = new Vector<>();
				telefonnummerBySchuelerId.put(dto.Schueler_ID, telefonnummern);
			}
			Telefonnummer tel = new Telefonnummer();
			tel.number = dto.Telefonnummer;
			tel.type = dtoTelefonArt.Bezeichnung;
			telefonnummern.add(tel);
		}
		return telefonnummerBySchuelerId;
	}

	/**
	 * Hilfsmethode für die Suche aller Kategorien einer SchülerId
	 *
	 * @param collect
	 * @param filteredDtoSchuelers
	 *
	 * @return eine Map ,in der jeder SchülerId die entsprechenden Kategorien
	 *         zugeordnet sind
	 */
	private Map<Long, Set<String>> getCategoriesBySchuelerId(Map<Long, SchuelerStatus> schuelerStatusByID) {
		List<DTOKlassen> dtoKlassenQueryResult = conn.queryNamed("DTOKlassen.schuljahresabschnitts_id",
				aktuellerSchuljahresabschnitt.ID, DTOKlassen.class);
		Map<Long, DTOKlassen> klassenById = dtoKlassenQueryResult.stream()
				.collect(Collectors.toMap(s -> s.ID, Function.identity()));
		List<DTOSchuelerLernabschnittsdaten> dtoSchuelerLernabschnittsdatenQueryResult = conn.queryNamed(
				"DTOSchuelerLernabschnittsdaten.klassen_id.multiple", klassenById.keySet(),
				DTOSchuelerLernabschnittsdaten.class);
		Map<Long, String> jahrgangKrzByJahrgangId = conn.queryNamed("DTOJahrgang.sichtbar", true, DTOJahrgang.class)
				.stream().collect(Collectors.toMap(j -> j.ID, j -> j.InternKrz));

		Map<Long, Set<String>> result = new HashMap<>();
		for (Entry<Long, SchuelerStatus> entry : schuelerStatusByID.entrySet()) {
			if (entry.getValue() == SchuelerStatus.NEUAUFNAHME) {
				Set<String> listForSchuelerId = result.computeIfAbsent(entry.getKey(), s -> new HashSet<>());
				listForSchuelerId.add(kategorienUtil.formatSchuelerNeuaufnahmenAlle());
			}
		}
		for (DTOSchuelerLernabschnittsdaten dtoSLA : dtoSchuelerLernabschnittsdatenQueryResult) {
			Set<String> listForSchuelerId = result.computeIfAbsent(dtoSLA.Schueler_ID, s -> new HashSet<>());
			if (klassenById.containsKey(dtoSLA.Klassen_ID)) {
				DTOKlassen dtoKlassen = klassenById.get(dtoSLA.Klassen_ID);
				if (schuelerStatusByID.get(dtoSLA.Schueler_ID) == SchuelerStatus.NEUAUFNAHME) {
					listForSchuelerId.add(kategorienUtil.formatSchuelerNeuaufnahmeKlasse(dtoKlassen.Klasse));
					listForSchuelerId.add(kategorienUtil
							.formatSchuelerNeuaufnahmeJahrgang(jahrgangKrzByJahrgangId.get(dtoKlassen.Jahrgang_ID)));
				}
				listForSchuelerId.add(kategorienUtil.formatSchuelerKlasse(dtoKlassen.Klasse));
				listForSchuelerId.add(
						kategorienUtil.formatSchuelerJahrgang(jahrgangKrzByJahrgangId.get(dtoKlassen.Jahrgang_ID)));
			}
		}
		List<DTOKurs> dtoKursResult = conn.queryNamed("DTOKurs.schuljahresabschnitts_id",
				aktuellerSchuljahresabschnitt.ID, DTOKurs.class);
		Map<Long, DTOKurs> kursNameById = dtoKursResult.stream().collect(Collectors.toMap(k -> k.ID, k -> k));
		// TODO kurse sind nur in einem Jahrgang eindeutig, Kursbezeichnung um
		// Jahrgangsbezeichnung anreichnern
		// 'Kurs' + [Jahrgang bei eindeutigkeit] + 'Kursbezeichnung' + [(Jahrgänge bei
		// nicht-Eindeutigkeit)]
		List<DTOKursSchueler> dtoKursSchuelerQueryResult = conn.queryNamed("DTOKursSchueler.kurs_id.multiple",
				kursNameById.keySet(), DTOKursSchueler.class);
		for (DTOKursSchueler dtoKursSchueler : dtoKursSchuelerQueryResult) {
			Set<String> listForSchuelerId = result.computeIfAbsent(dtoKursSchueler.Schueler_ID, s -> new HashSet<>());
			if (kursNameById.containsKey(dtoKursSchueler.Kurs_ID)) {
				DTOKurs dtoKurs = kursNameById.get(dtoKursSchueler.Kurs_ID);
				listForSchuelerId.add(kategorienUtil.formatSchuelerKurs(dtoKurs.KurzBez,
						jahrgangKrzByJahrgangId.get(dtoKurs.Jahrgang_ID)));
			}
		}
		return result;
	}
}
