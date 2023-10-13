package de.svws_nrw.davapi.data.repos.bycategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.adressbuch.AdressbuchEintrag;
import de.svws_nrw.core.data.adressbuch.AdressbuchKontakt;
import de.svws_nrw.core.data.adressbuch.Telefonnummer;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.davapi.data.CollectionRessourceQueryParameters;
import de.svws_nrw.davapi.data.IAdressbuchKontaktRepository;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerErzieherAdresse;
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
 * Suche nach Erziehern aus der Datenbank als {@link AdressbuchEintrag}, welche
 * mit Kategorien ergänzt werden.
 *
 */
public final class ErzieherWithCategoriesRepository implements IAdressbuchKontaktRepository {
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
	 * Konstruktor zum Erstellen des Repositories mit einer Datenbankverbindung
	 *
	 * @param conn                          die Datenbankverbindung
	 * @param kategorienUtil                Instanz der Utility zum Erzeugen von
	 *                                      Adressbuchkategorien
	 * @param aktuellerSchuljahresabschnitt der aktuelle Schuljahresabschnitt für
	 *                                      weitere Queries
	 */
	public ErzieherWithCategoriesRepository(final DBEntityManager conn,
			final DTOSchuljahresabschnitte aktuellerSchuljahresabschnitt, final AdressbuchKategorienUtil kategorienUtil) {
		this.conn = conn;
		this.aktuellerSchuljahresabschnitt = aktuellerSchuljahresabschnitt;
		this.kategorienUtil = kategorienUtil;
	}

	@Override
	public List<AdressbuchEintrag> getKontakteByAdressbuch(final String adressbuchId, final CollectionRessourceQueryParameters params) {
		if (!params.includeRessources
				|| !conn.getUser().pruefeKompetenz(BenutzerKompetenz.ADRESSDATEN_ERZIEHER_ANSEHEN)) {
			return new ArrayList<>();
		}

		final List<DTOSchueler> filteredDtoSchuelers = conn.queryNamed("DTOSchueler.all", DTOSchueler.class).getResultStream()
				.filter(SCHUELER_FILTER).toList();
		final Map<Long, DTOSchueler> schuelerBySchuelerIds = filteredDtoSchuelers.stream()
				.collect(Collectors.toMap(s -> s.ID, s -> s));
		final List<DTOSchuelerErzieherAdresse> dtoSchuelerErzieherAdresseResult = conn.queryNamed(
				"DTOSchuelerErzieherAdresse.schueler_id.multiple",
				filteredDtoSchuelers.stream().map(s -> s.ID).toList(), DTOSchuelerErzieherAdresse.class);
		final Map<Long, DTOSchuelerErzieherAdresse> erzieherBySchuelerID = new HashMap<>();
		for (DTOSchuelerErzieherAdresse schuelerErzAdresse : dtoSchuelerErzieherAdresseResult) {
			// map schuelerErzAdressen zu ihren SchuelerIDs, jeweils nur der Eintrag mit der höchsten
			// Sortierung
			if (erzieherBySchuelerID.containsKey(schuelerErzAdresse.Schueler_ID)) {
				final DTOSchuelerErzieherAdresse dtoSchuelerErzieherAdresse = erzieherBySchuelerID
						.get(schuelerErzAdresse.Schueler_ID);
				if (schuelerErzAdresse.Sortierung < dtoSchuelerErzieherAdresse.Sortierung) {
					schuelerErzAdresse = dtoSchuelerErzieherAdresse;
				}
			}
			erzieherBySchuelerID.put(schuelerErzAdresse.Schueler_ID, schuelerErzAdresse);
		}
		if (params.includeEintragIDs && !params.includeEintragPayload) {
			return erzieherBySchuelerID.values().stream().map(e -> {
				final AdressbuchEintrag a = new AdressbuchEintrag();
				a.id = IAdressbuchKontaktRepository.createErzieherId(e.ID);
				return a;
			}).toList();
		}


		final Set<Long> ortIds = filteredDtoSchuelers.stream().map(s -> s.Ort_ID).collect(Collectors.toSet());
		ortIds.addAll(erzieherBySchuelerID.values().stream().map(e -> e.ErzOrt_ID).collect(Collectors.toSet()));

		final Map<Long, DTOOrt> ortByOrtID = IAdressbuchKontaktRepository.queryOrteByOrtIds(ortIds, conn);
		final Map<Long, List<Telefonnummer>> telefonnummerBySchuelerId = SchuelerWithCategoriesRepository
				.queryTelefonNummernBySchuelerIds(schuelerBySchuelerIds.keySet(), conn);

		final Map<Long, SchuelerStatus> schuelerStatusById = filteredDtoSchuelers.stream()
				.collect(Collectors.toMap(s -> s.ID, s -> s.Status));
		final Map<Long, Set<String>> categoriesBySchuelerId = getCategoriesBySchuelerId(schuelerStatusById);

		return erzieherBySchuelerID.entrySet().stream().map(entry -> {
			final Long schuelerID = entry.getValue().Schueler_ID;
			return mapDTOErzieherAdrToAdressbuchEintrag(entry.getValue(), ortByOrtID.get(entry.getValue().ErzOrt_ID),
					categoriesBySchuelerId.get(schuelerID), schuelerBySchuelerIds.get(schuelerID),
					telefonnummerBySchuelerId.get(schuelerID));
		}).toList();
	}

	/**
	 * Hilfsmethode für die Suche aller Kategorien der Erzieher
	 *
	 * @param schuelerStatusByID der Status der Schueler gemappt auf ihre Schueler
	 *                           ID
	 *
	 * @return eine Map ,in der jeder Erzieher-Id die entsprechenden Kategorien
	 *         zugeordnet sind
	 */
	private Map<Long, Set<String>> getCategoriesBySchuelerId(final Map<Long, SchuelerStatus> schuelerStatusByID) {
		final List<DTOKlassen> dtoKlassenQueryResult = conn.queryNamed("DTOKlassen.schuljahresabschnitts_id",
				aktuellerSchuljahresabschnitt.ID, DTOKlassen.class);
		final Map<Long, DTOKlassen> klassenById = dtoKlassenQueryResult.stream()
				.collect(Collectors.toMap(s -> s.ID, Function.identity()));
		final List<DTOSchuelerLernabschnittsdaten> dtoSchuelerLernabschnittsdatenQueryResult = conn.queryNamed(
				"DTOSchuelerLernabschnittsdaten.klassen_id.multiple", klassenById.keySet(),
				DTOSchuelerLernabschnittsdaten.class);
		final Map<Long, String> jahrgangKrzByJahrgangId = conn.queryNamed("DTOJahrgang.sichtbar", true, DTOJahrgang.class)
				.stream().collect(Collectors.toMap(j -> j.ID, j -> j.InternKrz));

		final Map<Long, Set<String>> result = new HashMap<>();
		for (final Entry<Long, SchuelerStatus> entry : schuelerStatusByID.entrySet()) {
			if (SchuelerStatus.NEUAUFNAHME == entry.getValue()) {
				final Set<String> listForSchuelerId = result.computeIfAbsent(entry.getKey(), s -> new HashSet<>());
				listForSchuelerId.add(kategorienUtil.formatErzieherNeuaufnahmenAlle());
			}
		}
		for (final DTOSchuelerLernabschnittsdaten dtoSLA : dtoSchuelerLernabschnittsdatenQueryResult) {
			final Set<String> listForSchuelerId = result.computeIfAbsent(dtoSLA.Schueler_ID, s -> new HashSet<>());
			if (klassenById.containsKey(dtoSLA.Klassen_ID)) {
				final DTOKlassen dtoKlassen = klassenById.get(dtoSLA.Klassen_ID);
				if (schuelerStatusByID.get(dtoSLA.Schueler_ID) == SchuelerStatus.NEUAUFNAHME) {
					listForSchuelerId.add(kategorienUtil.formatErzieherNeuaufnahmenKlasse(dtoKlassen.Klasse));
					listForSchuelerId.add(kategorienUtil
							.formatErzieherNeuaufnahmenJahrgang(jahrgangKrzByJahrgangId.get(dtoKlassen.Jahrgang_ID)));
				}

				listForSchuelerId.add(kategorienUtil.formatErzieherKlasse(dtoKlassen.Klasse));
				listForSchuelerId.add(
						kategorienUtil.formatErzieherJahrgang(jahrgangKrzByJahrgangId.get(dtoKlassen.Jahrgang_ID)));
			}
		}
		final List<DTOKurs> dtoKursResult = conn.queryNamed("DTOKurs.schuljahresabschnitts_id",
				aktuellerSchuljahresabschnitt.ID, DTOKurs.class);
		final Map<Long, DTOKurs> kursNameById = dtoKursResult.stream().collect(Collectors.toMap(k -> k.ID, k -> k));
		final List<DTOKursSchueler> dtoKursSchuelerQueryResult = kursNameById.isEmpty() ? new ArrayList<>()
				: conn.queryList("SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID IN ?1 AND e.LernabschnittWechselNr = 0", DTOKursSchueler.class, kursNameById.keySet());
		for (final DTOKursSchueler dtoKursSchueler : dtoKursSchuelerQueryResult) {
			final Set<String> listForSchuelerId = result.computeIfAbsent(dtoKursSchueler.Schueler_ID, s -> new HashSet<>());
			if (kursNameById.containsKey(dtoKursSchueler.Kurs_ID)) {
				final DTOKurs dtoKurs = kursNameById.get(dtoKursSchueler.Kurs_ID);
				listForSchuelerId.add(kategorienUtil.formatErzieherKurs(dtoKurs.KurzBez,
						jahrgangKrzByJahrgangId.get(dtoKurs.Jahrgang_ID)));
			}
		}

		return result;
	}

	/**
	 * Hilfsmethode zum Erstellen eines Adressbucheintrags aus einem
	 * {@link DTOSchuelerErzieherAdresse}
	 *
	 * @param dtoSchuelerErzieherAdresse das {@link DTOSchuelerErzieherAdresse} aus
	 *                                   dem der Kontakt erstellt wird
	 * @param ort                        der Ort des Erziehers
	 * @param categories                 die Kategorien des Erziehers
	 * @param dtoSchueler                der Schueler für den der Erzieher
	 *                                   berechtigt ist
	 * @param telefonnummern             die Liste der Telefonnummern, sofern der
	 *                                   Schueler volljährig ist
	 * @return ein AdressbuchEintrag der diesen Erziehungsberechtigten
	 *         repräsentierts
	 */
	private static AdressbuchEintrag mapDTOErzieherAdrToAdressbuchEintrag(
			final DTOSchuelerErzieherAdresse dtoSchuelerErzieherAdresse, final DTOOrt ort, final Set<String> categories,
			final DTOSchueler dtoSchueler, final List<Telefonnummer> telefonnummern) {
		final AdressbuchKontakt k = new AdressbuchKontakt();

		// TODO erzieher müssen eigentlich mehrere Kontakte sein, so erscheint immer nur
		// der primäre Kontakt
		k.id = IAdressbuchKontaktRepository.createErzieherId(dtoSchuelerErzieherAdresse.ID);
		k.email = dtoSchuelerErzieherAdresse.ErzEmail;

		k.hausnummer = dtoSchuelerErzieherAdresse.ErzHausNr;
		k.hausnummerZusatz = dtoSchuelerErzieherAdresse.ErzHausNrZusatz;
		k.nachname = dtoSchuelerErzieherAdresse.Name1;

		IAdressbuchKontaktRepository.applyOrtToKontakt(k, ort);
		k.kategorien.addAll(categories);
		k.strassenname = dtoSchuelerErzieherAdresse.ErzStrassenname;
		k.vorname = dtoSchuelerErzieherAdresse.Vorname1;
		k.idKind = IAdressbuchKontaktRepository.createSchuelerId(dtoSchuelerErzieherAdresse.Schueler_ID);
		return k;
	}
}
