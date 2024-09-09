package de.svws_nrw.data.benutzer;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESException;
import de.svws_nrw.core.data.benutzer.BenutzerAllgemeinCredentials;
import de.svws_nrw.core.data.benutzer.BenutzerDaten;
import de.svws_nrw.core.data.benutzer.BenutzergruppeDaten;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.benutzer.BenutzerTyp;
import de.svws_nrw.asd.types.lehrer.LehrerLeitungsfunktion;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangBeratungslehrer;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzer;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzerAllgemein;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzerKompetenz;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzerMail;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppenKompetenz;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppenMitglied;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerErzieherAdresse;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOSchulleitung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungen;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungsKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentials;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzerDaten}.
 */
public final class DataBenutzerDaten extends DataManager<Long> {

	private static final String strBenutzerMitIDExistiertNicht = "Der Benutzer mit der ID existiert nicht.";

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link BenutzerDaten}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBenutzerDaten(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOViewBenutzerdetails} in einen Core-DTO {@link BenutzerDaten}.
	 */
	private final Function<DTOViewBenutzerdetails, BenutzerDaten> dtoMapper = (final DTOViewBenutzerdetails b) -> {
		final BenutzerDaten daten = new BenutzerDaten();
		daten.id = b.ID;
		daten.typ = b.Typ.id;
		daten.typID = b.TypID;
		daten.anzeigename = b.AnzeigeName;
		daten.name = b.Benutzername;
		daten.istAdmin = (b.IstAdmin != null) && b.IstAdmin;
		daten.idCredentials = b.credentialID;
		return daten;
	};

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOBenutzergruppe}
	 * in einen Core-DTO {@link BenutzergruppeDaten}.
	 */
	private final Function<DTOBenutzergruppe, BenutzergruppeDaten> dtoMapperGruppe = (final DTOBenutzergruppe b) -> {
		final BenutzergruppeDaten daten = new BenutzergruppeDaten();
		daten.id = b.ID;
		daten.bezeichnung = b.Bezeichnung;
		daten.istAdmin = (b.IstAdmin != null) && b.IstAdmin;
		return daten;
	};

	/**
	 * Bestimmt das DTO für den Benutzer aus der Datenbank.
	 *
	 * @param id die ID des Benutzers
	 *
	 * @return das DTO
	 */
	private DTOViewBenutzerdetails getDTO(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die ID des zu änderden Benutzers darf nicht null sein.");
		final DTOViewBenutzerdetails benutzer = conn.queryByKey(DTOViewBenutzerdetails.class, id);
		if (benutzer == null)
			throw new ApiOperationException(Status.NOT_FOUND, strBenutzerMitIDExistiertNicht);
		return benutzer;
	}

	/**
	 * Bestimmt die Anzahl der administrativen Gruppen, welche dem aktuellen
	 * Benutzer administrative Rechte verleihen.
	 *
	 * @return die Anzahl der Gruppen über die der Benutzer administrative
	 *         Berechtigungen hat
	 */
	private int getAnzahlAdminGruppen() {
		// Ermittle die administrativen Gruppen des aktuellen Benutzers
		final List<DTOBenutzergruppe> bgs = conn.queryList(
				"SELECT bg FROM DTOBenutzergruppenMitglied bgm JOIN DTOBenutzergruppe bg "
						+ "ON bgm.Gruppe_ID = bg.ID and bgm.Benutzer_ID= ?1 and bg.IstAdmin= ?2 ",
				DTOBenutzergruppe.class,
				conn.getUser().getId(), true);
		return bgs.size();
	}

	/**
	 * Überprüft für die Schulform die zulässigkeit der Kompetenzen, die einem Objekt hinzugefügt bzw. entzogen werden.
	 *
	 * @param kids die IDs der Kompetenzen
	 *
	 * @return true, wenn alle Kompetenzen zulässig sind, sonst false
	 *
	 */
	private boolean istKompetenzZulaessig(final List<Long> kids) throws ApiOperationException {
		// Überprüfe die Zulässigkeit der Kompetenzen für die Schulform
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Schule angelegt.");
		final Schulform schulform = Schulform.data().getWertBySchluessel(schule.SchulformNr);
		final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine gültiger Schuljahresabschnitt vorhanden.");

		final List<BenutzerKompetenz> bks = new ArrayList<>();
		for (final Long kid : kids) {
			bks.add(BenutzerKompetenz.getByID(kid));
		}

		for (final BenutzerKompetenz bk : bks) {
			if (!bk.hatSchulform(schuljahresabschnitt.Jahr, schulform))
				throw new ApiOperationException(Status.FORBIDDEN, "Die Kompetenz" + bk.daten.bezeichnung + "ist für die Schulform"
						+ schulform.daten(schuljahresabschnitt.Jahr).text + "nicht zulässig");
		}
		return true;
	}


	/**
	 * Setzt für die angegebene Benutzer-ID den Benutzer administrativ.
	 *
	 * @param id die ID des Benutzers
	 *
	 * @return bei Erfolg eine HTTP-Response 200
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response addAdmin(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die ID der zu änderden Benutzer darf nicht null sein.");
		final DTOBenutzer benutzer = conn.queryByKey(DTOBenutzer.class, id);
		if (benutzer == null)
			throw new ApiOperationException(Status.NOT_FOUND, strBenutzerMitIDExistiertNicht);
		if (benutzer.IstAdmin)
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Benutzer hat bereits administrative Rechte.");
		benutzer.IstAdmin = true;
		conn.transactionPersist(benutzer);
		return Response.status(Status.OK).build();
	}

	/**
	 * Setzt für die angegebene Benutzer-ID die Benutzerkompetenzen.
	 *
	 * @param id   die ID des Benutzers
	 * @param kids die IDs der Kompetenzen
	 *
	 * @return bei Erfolg eine HTTP-Response 200
	 *
	 * @throws ApiOperationException   eine Exception mit dem entsprechenden HTTP-Fehlercode im Fehlerfall
	 */
	public Response addKompetenzen(final Long id, final List<Long> kids) throws ApiOperationException {
		// Prüft, die Zulässigkeit der Kompetenzen für die Schulform
		this.istKompetenzZulaessig(kids);
		if ((id == null) || (kids == null))
			throw new ApiOperationException(Status.NOT_FOUND, "Die ID der zu änderden Benutzer bzw IDs der Kompetenzen darf bzw. dürfen nicht null sein.");
		// Prüfe, ob der Benutzer mit der ID existiert.
		getDTO(id);
		// Prüfe, ob die Benutzerkompetenzen mit den Ids existieren.
		for (final Long kid : kids)
			if (BenutzerKompetenz.getByID(kid) == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Die Benutzerkompetenz mit der ID " + kid + " existiert nicht!!");
		// Füge die Kompetenzen hinzu
		for (final Long kid : kids) {
			DTOBenutzerKompetenz bk = conn.queryByKey(DTOBenutzerKompetenz.class, id, kid);
			if (bk == null) {
				bk = new DTOBenutzerKompetenz(id, kid);
				conn.transactionPersist(bk);
			}
		}
		return Response.status(Status.OK).build();
	}

	/**
	 * Erstellt einen neuen Benutzer *
	 *
	 * @param cred       Das JSON-Objekt mit den Daten für Credentials-Obejkt
	 *
	 * @return Eine Response mit dem neuen Benutzer
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response createBenutzerAllgemein(final BenutzerAllgemeinCredentials cred) throws ApiOperationException {
		DTOBenutzerAllgemein benutzer_allg = null;
		DTOBenutzer benutzer = null;
		DTOCredentials credential = null;

		if ((cred.benutzername == null) || (cred.password == null))
			throw new ApiOperationException(Status.BAD_REQUEST, "Benuzername oder Passwort leer!");

		// Bestimme die ID des Benutzers / Credentials / BenutzerAllgemeins
		final long idBenutzerAllgemein = conn.transactionGetNextID(DTOBenutzerAllgemein.class);
		final long idCredential = conn.transactionGetNextID(DTOCredentials.class);
		final long idBenutzer = conn.transactionGetNextID(DTOBenutzer.class);

		// Zuerst die Credentials anlegen ...
		credential = new DTOCredentials(idCredential, cred.benutzername);
		credential.PasswordHash = Benutzer.erstellePasswortHash(cred.password);
		conn.transactionPersist(credential);
		conn.transactionFlush();

		// ... dann die Informationen zum allgemeinen Benutzer mit den Credentials verknüpft ...
		benutzer_allg = new DTOBenutzerAllgemein(idBenutzerAllgemein);
		benutzer_allg.AnzeigeName = cred.anzeigename.isBlank() ? cred.benutzername : cred.anzeigename;
		benutzer_allg.CredentialID = idCredential;
		conn.transactionPersist(benutzer_allg);
		conn.transactionFlush();

		// ... und dann das Benutzer-Objekt anlegen, welches auf den allgemeinen Benutzer verweist
		benutzer = new DTOBenutzer(idBenutzer, BenutzerTyp.ALLGEMEIN, false);
		benutzer.Allgemein_ID = idBenutzerAllgemein;
		conn.transactionPersist(benutzer);

		final BenutzerDaten daten = new BenutzerDaten();
		daten.id = idBenutzer;
		daten.typ = BenutzerTyp.ALLGEMEIN.id;
		daten.typID = benutzer_allg.ID;
		daten.anzeigename = benutzer_allg.AnzeigeName;
		daten.name = credential.Benutzername;
		daten.istAdmin = (benutzer.IstAdmin != null) && benutzer.IstAdmin;
		daten.idCredentials = credential.ID;
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Erstellt einen neuen Benutzer mit dem Benutzertyp Erzieher
	 *
	 * @return Eine Response mit dem neuen Benutzer
	 */
	public Response createBenutzerErzieher() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Erstellt einen neuen Benutzer mit dem Benutzertyp Lehrer
	 *
	 * @return Eine Response mit dem neuen Benutzer
	 */
	public Response createBenutzerLehrer() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Erstellt einen neuen Benutzer mit dem Benutzertyp Lehrer
	 *
	 * @return Eine Response mit dem neuen Benutzer
	 */
	public Response createBenutzerSchueler() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}


	/**
	 * Bestimmt die Menge der Klassen-IDs, welche dem Benutzer als Klassenleitungen zugeordnet sind.
	 * Bei einem Benutzer-Typ der nicht Lehrer ist, wird eine leere Liste zurückgegeben.
	 *
	 * @param conn          die aktuelle Datenbankverbindung
	 * @param typBenutzer   der Typ des Benutzer
	 * @param idBenutzer    die ID des Benutzers in Abhängigkeit vom Typ
	 *
	 * @return die Liste der Klassen-IDs
	 */
	public static List<Long> getKlassenVonKlassenleitungen(final DBEntityManager conn, final int typBenutzer, final long idBenutzer) {
		// Nur Lehrer-Benutzer können funktionsbezogene Kompetenzen haben.
		if (typBenutzer != BenutzerTyp.LEHRER.id)
			return new ArrayList<>();
		// Bestimme die Klassen mit Klassenlehrern oder Abteilungsleiterrechten ...
		final List<DTOKlassenLeitung> klassenleitungen = conn.queryList(DTOKlassenLeitung.QUERY_BY_LEHRER_ID, DTOKlassenLeitung.class, idBenutzer);
		return klassenleitungen.stream().map(kl -> kl.Klassen_ID).distinct().toList();
	}


	/**
	 * Bestimmt die Menge der Klassen-IDs, welche dem Benutzer aufgrund von Abteilungsleitungen zugeordnet sind.
	 * Bei einem Benutzer-Typ der nicht Lehrer ist, wird eine leere Liste zurückgegeben.
	 *
	 * @param conn          die aktuelle Datenbankverbindung
	 * @param typBenutzer   der Typ des Benutzer
	 * @param idBenutzer    die ID des Benutzers in Abhängigkeit vom Typ
	 *
	 * @return die Liste der Klassen-IDs
	 */
	public static List<Long> getKlassenVonAbteilungsleitungen(final DBEntityManager conn, final int typBenutzer, final long idBenutzer) {
		// Nur Lehrer-Benutzer können Leitungsfunktionen haben.
		if (typBenutzer != BenutzerTyp.LEHRER.id)
			return new ArrayList<>();
		// Bestimme die Klassen-IDs aus den Abteilungsinformationen - eine Unterscheidung anhand des Schuljahresabschnittes ist hier nicht nötig
		final List<Long> listAbteilungsIDs = conn.queryList(DTOAbteilungen.QUERY_BY_ABTEILUNGSLEITER_ID, DTOAbteilungen.class, idBenutzer)
				.stream().map(a -> a.ID).toList();
		if (listAbteilungsIDs.isEmpty())
			return new ArrayList<>();
		final List<DTOAbteilungsKlassen> listAbteilungenKlassen =
				conn.queryList(DTOAbteilungsKlassen.QUERY_LIST_BY_ABTEILUNG_ID, DTOAbteilungsKlassen.class, listAbteilungsIDs);
		return listAbteilungenKlassen.stream().map(ak -> ak.Klassen_ID).distinct().toList();
	}


	/**
	 * Bestimmt die Menge der Klassen-IDs, welche dem Benutzer aufgrund von Klassenleitungen und Abteilungsleitungen zugeordnet sind.
	 * Bei einem Benutzer-Typ der nicht Lehrer ist, wird eine leere Liste zurückgegeben.
	 *
	 * @param conn          die aktuelle Datenbankverbindung
	 * @param typBenutzer   der Typ des Benutzer
	 * @param idBenutzer    die ID des Benutzers in Abhängigkeit vom Typ
	 *
	 * @return die Liste der Klassen-IDs
	 */
	public static List<Long> getKlassenFunktionsbezogen(final DBEntityManager conn, final int typBenutzer, final long idBenutzer) {
		return Stream.concat(
				getKlassenVonKlassenleitungen(conn, typBenutzer, idBenutzer).stream(),
				getKlassenVonAbteilungsleitungen(conn, typBenutzer, idBenutzer).stream()
		).distinct().toList();
	}


	/**
	 * Gibt die Liste der aktuellen Leitunsfunktionen des Lehrers mit den angegebenen ID zurück.
	 *
	 * @param conn          die aktuelle Datenbankverbindung
	 * @param typBenutzer   der Typ des Benutzer
	 * @param idBenutzer    die ID des Benutzers in Abhängigkeit vom Typ
	 *
	 * @return die aktuellen Leitunsfunktionen
	 */
	public static List<LehrerLeitungsfunktion> getLeitungsfunktionen(final DBEntityManager conn, final int typBenutzer, final long idBenutzer) {
		// Nur Lehrer-Benutzer können Leitungsfunktionen haben.
		if (typBenutzer != BenutzerTyp.LEHRER.id)
			return new ArrayList<>();
		final List<DTOSchulleitung> schulleitungsfunktionen = conn.queryList(DTOSchulleitung.QUERY_BY_LEHRERID, DTOSchulleitung.class, idBenutzer);
		final List<LehrerLeitungsfunktion> result = new ArrayList<>();
		for (final DTOSchulleitung slf : schulleitungsfunktionen) {
			final LocalDateTime von = ((slf.Von == null) ? LocalDate.of(1900, 1, 1) : LocalDate.parse(slf.Von)).atStartOfDay();
			final LocalDateTime bis = ((slf.Bis == null) ? LocalDate.of(9999, 12, 31) : LocalDate.parse(slf.Bis)).atTime(23, 59, 59);
			final LocalDateTime jetzt = LocalDateTime.now(ZoneId.of("Europe/Berlin"));
			final LehrerLeitungsfunktion funktion = LehrerLeitungsfunktion.data().getWertByID(slf.ID);
			if ((funktion != null) && (von.compareTo(jetzt) <= 0) && (jetzt.compareTo(bis) <= 0))
				result.add(funktion);
		}
		return result;
	}


	/**
	 * Gibt die Liste der IDs der aktuellen Leitungsfunktionen des Lehrers mit den angegebenen ID zurück.
	 *
	 * @param schuljahr     das aktuelle Schuljahr der Schule
	 * @param conn          die aktuelle Datenbankverbindung
	 * @param typBenutzer   der Typ des Benutzer
	 * @param idBenutzer    die ID des Benutzers in Abhängigkeit vom Typ
	 *
	 * @return die IDs der aktuellen Leitunsfunktionen
	 */
	public static List<Long> getLeitungsfunktionenIDs(final int schuljahr, final DBEntityManager conn, final int typBenutzer, final long idBenutzer) {
		return getLeitungsfunktionen(conn, typBenutzer, idBenutzer).stream().map(l -> l.daten(schuljahr).id).toList();
	}


	/**
	 * Gibt die Liste der Abiturjahrgänge des Lehrers mit den angegebenen ID zurück.
	 *
	 * @param conn          die aktuelle Datenbankverbindung
	 * @param typBenutzer   der Typ des Benutzer
	 * @param idBenutzer    die ID des Benutzers in Abhängigkeit vom Typ
	 *
	 * @return die Liste der Abiturjahrgänge
	 */
	public static List<Integer> getBeratungslehrerAbiturjahrgaenge(final DBEntityManager conn, final int typBenutzer, final long idBenutzer) {
		// Nur Lehrer-Benutzer können Beratungslehrer sein.
		if (typBenutzer != BenutzerTyp.LEHRER.id)
			return new ArrayList<>();
		final List<DTOGostJahrgangBeratungslehrer> dtos =
				conn.queryList(DTOGostJahrgangBeratungslehrer.QUERY_BY_LEHRER_ID, DTOGostJahrgangBeratungslehrer.class, idBenutzer);
		final List<Integer> result = new ArrayList<>();
		for (final DTOGostJahrgangBeratungslehrer dto : dtos)
			result.add(dto.Abi_Jahrgang);
		return result;
	}


	@Override
	public Response get(final Long id) throws ApiOperationException {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Schule angelegt.");
		final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine gültiger Schuljahresabschnitt vorhanden.");

		// Lese die Informationen zu den Gruppen ein
		final List<Long> gruppenIDs = conn.queryList(DTOBenutzergruppenMitglied.QUERY_BY_BENUTZER_ID, DTOBenutzergruppenMitglied.class, id).stream()
				.map(g -> g.Gruppe_ID).toList();
		final List<DTOBenutzergruppe> gruppen = (gruppenIDs.isEmpty())
				? new ArrayList<>()
				: conn.queryByKeyList(DTOBenutzergruppe.class, gruppenIDs);
		// Lese die Informationen zu den Kompetenzen ein
		final List<Long> kompetenzIDs = conn.queryList(DTOBenutzerKompetenz.QUERY_BY_BENUTZER_ID, DTOBenutzerKompetenz.class, id)
				.stream().map(b -> b.Kompetenz_ID).sorted().toList();
		final List<DTOBenutzergruppenKompetenz> gruppenKompetenzen = (gruppenIDs.isEmpty())
				? new ArrayList<>()
				: conn.queryList(DTOBenutzergruppenKompetenz.QUERY_LIST_BY_GRUPPE_ID, DTOBenutzergruppenKompetenz.class, gruppenIDs);
		final Map<Long, List<DTOBenutzergruppenKompetenz>> mapGruppenKompetenzen = gruppenKompetenzen.stream()
				.collect(Collectors.groupingBy(k -> k.Gruppe_ID));
		// Erstelle den Core-DTO für die API und gebe diesen zurück
		final BenutzerDaten daten = dtoMapper.apply(getDTO(id));

		for (final Long kompetenzID : kompetenzIDs)
			daten.kompetenzen.add(BenutzerKompetenz.getByID(kompetenzID).daten.id);

		for (final DTOBenutzergruppe gruppe : gruppen) {
			final BenutzergruppeDaten gdaten = dtoMapperGruppe.apply(gruppe);
			final List<DTOBenutzergruppenKompetenz> gruppenkompetenzen = mapGruppenKompetenzen.get(gruppe.ID);
			if (gruppenkompetenzen != null)
				for (final Long kompetenzID : gruppenkompetenzen.stream().map(k -> k.Kompetenz_ID).distinct().sorted()
						.toList())
					gdaten.kompetenzen.add(kompetenzID);
			daten.gruppen.add(gdaten);
		}
		// Füge die Informationen hinzu, zu welchen Klassen funktionsbezogene Kompetenzen vorliegen oder welche Leitungsfunktionen vorliegen
		daten.kompetenzenKlassen.addAll(getKlassenFunktionsbezogen(conn, daten.typ, daten.typID));
		daten.leitungsfunktionen.addAll(getLeitungsfunktionenIDs(schuljahresabschnitt.Jahr, conn, daten.typ, daten.typID));
		daten.kompetenzenAbiturjahrgaenge.addAll(getBeratungslehrerAbiturjahrgaenge(conn, daten.typ, daten.typID));
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Entfernt die Admin-Berechtigung des Benutzers.
	 *
	 * @param id die ID des Benutzers
	 *
	 * @return bei Erfolg eine HTTP-Response 200
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response removeAdmin(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die ID der zu änderden Benutzer darf nicht null sein.");
		final DTOBenutzer benutzer = conn.queryByKey(DTOBenutzer.class, id);
		if (benutzer == null)
			throw new ApiOperationException(Status.NOT_FOUND, strBenutzerMitIDExistiertNicht);
		if (!benutzer.IstAdmin)
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Benutzer mit der ID " + id
					+ " besitzt selbst direkt keine administrative Berechtigung, die entfernt werden könnte.");
		if (id.equals(conn.getUser().getId()) && (getAnzahlAdminGruppen() == 0))
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der aktuelle Benutzer darf seine Admin-Berechtigung nicht entfernen, wenn er diese nicht zusätzlich über administrative Gruppen besitzt.");
		benutzer.IstAdmin = false;
		conn.transactionPersist(benutzer);
		return Response.status(Status.OK).build();
	}


	private void _removeBenutzerAllgemein(final DTOBenutzer benutzer) throws ApiOperationException {
		if (benutzer.Allgemein_ID == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der Benutzer mit der ID %d vom Typ ALLGEMEIN hat keine entsprechende allgemeine ID zugeordnet. Dies ist nicht zulässig."
							.formatted(benutzer.ID));
		final DTOBenutzerAllgemein benutzerAllgemein = conn.queryByKey(DTOBenutzerAllgemein.class, benutzer.Allgemein_ID);
		if (benutzerAllgemein == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der allgemeine Benutzer mit der ID %d ist nicht in der Datenbank vorhanden.".formatted(benutzer.Allgemein_ID));
		if (benutzerAllgemein.CredentialID == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der allgemeine Benutzer mit der ID %d hat keine Credentials zugeordnet. Dies ist nicht zulässig.".formatted(benutzerAllgemein.ID));
		final DTOCredentials credential = conn.queryByKey(DTOCredentials.class, benutzerAllgemein.CredentialID);
		if (credential == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Die Credentials mit der ID %d für den allgemeinen Benutzer mit der ID %d konnten nicht gefunden werden."
							.formatted(benutzerAllgemein.CredentialID, benutzerAllgemein.ID));
		conn.transactionRemove(credential);
		conn.transactionFlush();
		conn.transactionRemove(benutzerAllgemein);
		conn.transactionFlush();
		conn.transactionRemove(benutzer);
		conn.transactionFlush();
	}

	private void _removeBenutzerErzieher(final DTOBenutzer benutzer) throws ApiOperationException {
		if (benutzer.Erzieher_ID == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der Benutzer mit der ID %d vom Typ ERZIEHER hat keine entsprechende Erzieher-ID zugeordnet. Dies ist nicht zulässig."
							.formatted(benutzer.ID));
		final DTOSchuelerErzieherAdresse benutzerErzieher = conn.queryByKey(DTOSchuelerErzieherAdresse.class, benutzer.Erzieher_ID);
		if (benutzerErzieher == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der Erzieher-Benutzer mit der ID %d ist nicht in der Datenbank vorhanden.".formatted(benutzer.Erzieher_ID));
		if (benutzerErzieher.CredentialID == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der Erzieher-Benutzer mit der ID %d hat keine Credentials zugeordnet. Dies ist nicht zulässig.".formatted(benutzerErzieher.ID));
		final DTOCredentials credential = conn.queryByKey(DTOCredentials.class, benutzerErzieher.CredentialID);
		if (credential == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Die Credentials mit der ID %d für den Erzieher-Benutzer mit der ID %d konnten nicht gefunden werden."
							.formatted(benutzerErzieher.CredentialID, benutzerErzieher.ID));
		conn.transactionRemove(credential);
		conn.transactionFlush();
		conn.transactionRemove(benutzer);
		conn.transactionFlush();
	}

	private void _removeBenutzerLehrer(final DTOBenutzer benutzer) throws ApiOperationException {
		if (benutzer.Lehrer_ID == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der Benutzer mit der ID %d vom Typ LEHRER hat keine entsprechende Lehrer-ID zugeordnet. Dies ist nicht zulässig.".formatted(benutzer.ID));
		final DTOLehrer benutzerLehrer = conn.queryByKey(DTOLehrer.class, benutzer.Lehrer_ID);
		if (benutzerLehrer == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der Lehrer-Benutzer mit der ID %d ist nicht in der Datenbank vorhanden.".formatted(benutzer.Lehrer_ID));
		if (benutzerLehrer.CredentialID == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der Lehrer-Benutzer mit der ID %d hat keine Credentials zugeordnet. Dies ist nicht zulässig.".formatted(benutzerLehrer.ID));
		final DTOCredentials credential = conn.queryByKey(DTOCredentials.class, benutzerLehrer.CredentialID);
		if (credential == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Die Credentials mit der ID %d für den Lehrer-Benutzer mit der ID %d konnten nicht gefunden werden.".formatted(benutzerLehrer.CredentialID,
							benutzerLehrer.ID));
		conn.transactionRemove(credential);
		conn.transactionFlush();
		conn.transactionRemove(benutzer);
		conn.transactionFlush();
	}

	private void _removeBenutzerSchueler(final DTOBenutzer benutzer) throws ApiOperationException {
		if (benutzer.Schueler_ID == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der Benutzer mit der ID %d vom Typ SCHUELER hat keine entsprechende Schüler-ID zugeordnet. Dies ist nicht zulässig."
							.formatted(benutzer.ID));
		final DTOSchueler benutzerSchueler = conn.queryByKey(DTOSchueler.class, benutzer.Schueler_ID);
		if (benutzerSchueler == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der Schüler-Benutzer mit der ID %d ist nicht in der Datenbank vorhanden.".formatted(benutzer.Schueler_ID));
		if (benutzerSchueler.CredentialID == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der Schüler-Benutzer mit der ID %d hat keine Credentials zugeordnet. Dies ist nicht zulässig.".formatted(benutzerSchueler.ID));
		final DTOCredentials credential = conn.queryByKey(DTOCredentials.class, benutzerSchueler.CredentialID);
		if (credential == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Die Credentials mit der ID %d für den Schüler-Benutzer mit der ID %d konnten nicht gefunden werden."
							.formatted(benutzerSchueler.CredentialID, benutzerSchueler.ID));
		conn.transactionRemove(credential);
		conn.transactionFlush();
		conn.transactionRemove(benutzer);
		conn.transactionFlush();
	}

	/**
	 * Entfernt die Benutzer mit den übergebenen IDs
	 *
	 * @param benutzerIDs   die IDs der zu entfernenden Benutzer
	 *
	 * @return die HTTP-Response mit dem Status OK (200)
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response removeBenutzerMenge(final List<Long> benutzerIDs) throws ApiOperationException {
		final long idSelf = conn.getUser().getId();
		if (benutzerIDs.contains(idSelf))
			throw new ApiOperationException(Status.CONFLICT, "Der aktuelle Benutzer kann sich nicht selber löschen.");

		for (final Long idBenutzer : benutzerIDs) {
			final DTOBenutzer benutzer = conn.queryByKey(DTOBenutzer.class, idBenutzer);
			if (benutzer == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Ein Benutzer mit der ID %d konnte nicht gefunden werden.".formatted(idBenutzer));
			switch (benutzer.Typ) {
				case ALLGEMEIN -> _removeBenutzerAllgemein(benutzer);
				case ERZIEHER -> _removeBenutzerErzieher(benutzer);
				case LEHRER -> _removeBenutzerLehrer(benutzer);
				case SCHUELER -> _removeBenutzerSchueler(benutzer);
			}
		}
		return Response.status(Status.OK).build();
	}

	/**
	 * Entfernt für die angegebene Benutzer-ID die Benutzerkompetenzen.
	 *
	 * @param id   die ID des Benutzers
	 * @param kids die IDs der Kompetenzen
	 *
	 * @return bei Erfolg eine HTTP-Response 204
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response removeKompetenzen(final Long id, final List<Long> kids) throws ApiOperationException {
		// Prüft, die Zulässigkeit der Kompetenzen für die Schulform
		this.istKompetenzZulaessig(kids);

		if ((id == null) || (kids == null))
			throw new ApiOperationException(Status.NOT_FOUND, "Die ID der zu änderden Benutzer bzw IDs der Kompetenzen darf bzw. dürfen nicht null sein.");
		// Prüfe, ob der Benutzer mit der ID existiert.
		getDTO(id);
		// Prüfe, ob die Benutzerkompetenzen mit den Ids existieren.
		for (final Long kid : kids) {
			if (BenutzerKompetenz.getByID(kid) == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Die Benutzerkompetenz mit der ID " + kid + " existiert nicht!!");
		}
		try {
			for (final Long kid : kids) {
				// Bestimme den Datensatz aus DTOBenutzerKompetenz
				final DTOBenutzerKompetenz bk = conn.queryByKey(DTOBenutzerKompetenz.class, id, kid);
				if (bk == null)
					throw new ApiOperationException(Status.NOT_FOUND,
							"Der zu löschende Datensatz in DTOBenutzerkompetenz mit Benutzer_ID " + id + "und Kompetenz_ID" + kid + " existiert nicht");
				// Entferne die Kompetenz
				conn.transactionRemove(bk);
			}
		} catch (final Exception e) {
			if (e instanceof final ApiOperationException aoe)
				throw aoe;
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		}
		return Response.status(Status.OK).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Setzt für die angegebene Benutzer-ID den Anzeigenamen.
	 *
	 * @param id   die ID der Benutzergruppe
	 * @param name der neue Azeigename
	 *
	 * @return die Response 200 bei Erfolg.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response setAnzeigename(final Long id, final String name) throws ApiOperationException {
		if ((name == null) || "".equals(name))
			throw new ApiOperationException(Status.CONFLICT, "Der Anzeigename muss gültig sein und darf nicht null oder leer sein");
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die ID der zu änderden Benutzer darf nicht null sein.");
		final DTOBenutzerAllgemein benutzerallgemein = conn.queryByKey(DTOBenutzerAllgemein.class, id);
		if (benutzerallgemein == null)
			throw new ApiOperationException(Status.NOT_FOUND, strBenutzerMitIDExistiertNicht);
		// Der Anzeigename wird nur bei den Benutzern mit dem Benutzertyp Allgemein
		// geändert.
		final DTOViewBenutzerdetails benutzerdetails = getDTO(id);
		if (benutzerdetails.Typ != BenutzerTyp.ALLGEMEIN)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Anzeigename kann bei dem Benutzer mit der ID " + id + "aufgrund des Benutzertyps nicht geändert werden");
		// Der alte Anzeigename wurde übergeben.
		if (name.equals(benutzerallgemein.AnzeigeName))
			return Response.status(Status.OK).build();
		// Der Anzeigename wird angepasst...
		benutzerallgemein.AnzeigeName = name;
		conn.transactionPersist(benutzerallgemein);
		return Response.status(Status.OK).build();
	}



	/**
	 * Setzt für die angegebene Benutzer-ID den Name des Benutzers (Anmeldename).
	 *
	 * @param id     die ID des Benutzers
	 * @param name   der neue Benutzername für die Anmeldung
	 *
	 * @return die Response 200 bei Erfolg.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response setBenutzername(final Long id, final String name) throws ApiOperationException {
		if ((name == null) || "".equals(name))
			throw new ApiOperationException(Status.CONFLICT, "Der Name für die Anmeldung muss gültig sein und darf nicht null oder leer sein");
		if (id.equals(conn.getUser().getId()))
			throw new ApiOperationException(Status.CONFLICT, "Der aktuelle Benutzer darf seinen eigenen Benutzernamen nicht ändern.");
		final DTOViewBenutzerdetails benutzer = getDTO(id);
		// der alte Benutzername wurde übergeben...
		if (name.equals(benutzer.Benutzername))
			return Response.status(Status.OK).build();
		final DTOCredentials cred = conn.queryByKey(DTOCredentials.class, benutzer.credentialID);
		if (cred == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Dem Benutzer sind keine gültigen Credentials zugeordnet.");
		// Prüfe vorher, ob der Name nicht bereits verwendet wird -> Conflict
		final List<DTOCredentials> creds = conn.queryAll(DTOCredentials.class);
		for (final DTOCredentials data : creds) {
			if (name.trim().equals(data.Benutzername))
				throw new ApiOperationException(Status.CONFLICT,
						"Ein Benutzer mit dem Namen existiert bereits - die Umbenennung kann nicht durchgeführt werden");
		}
		cred.Benutzername = name;
		conn.transactionPersist(cred);
		return Response.status(Status.OK).build();
	}


	/**
	 * Erstellt für den Benutzer mit der übergebenen ID einen neuen Kennwort-Hash
	 * basierend auf dem übergebenen Kennwort
	 *
	 * @param id       die ID des Benutzers, dessen Kennwort neu gesetzt werden soll
	 * @param password das Kennwort
	 *
	 * @return bei Erfolg eine HTTP-Response 204
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response setPassword(final Long id, final String password) throws ApiOperationException {
		final String hash = Benutzer.erstellePasswortHash(password);
		final DTOViewBenutzerdetails benutzer = conn.queryByKey(DTOViewBenutzerdetails.class, id);
		if ((benutzer == null) || (benutzer.credentialID == null))
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOCredentials credential = conn.queryByKey(DTOCredentials.class, benutzer.credentialID);
		if (credential == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		credential.PasswordHash = hash;
		conn.transactionPersist(credential);
		// Prüfe, ob es sich bei der ID um die ID des angemeldeten Benutzers handelt. Falls ja, dann aktualisiere ggf. auch das SMTP-Email-Kennwort
		if (Objects.equals(id, conn.getUser().getId())) {
			try {
				final DTOBenutzerMail dtoMail = conn.queryByKey(DTOBenutzerMail.class, id);
				if ((dtoMail != null) && (dtoMail.SMTPPassword != null) && (!dtoMail.SMTPPassword.isBlank())) {
					conn.transactionFlush();
					final byte[] smtpPassword = conn.getUser().getAES().decryptBase64(dtoMail.SMTPPassword);
					final AES aesneu = Benutzer.getAESInstance(conn.getUser().getUsername(), password);
					dtoMail.SMTPPassword = aesneu.encryptBase64(smtpPassword);
					conn.transactionPersist(dtoMail);
				}
			} catch (@SuppressWarnings("unused") final AESException e) {
				//
			}
		}
		return Response.status(Status.NO_CONTENT).build();
	}

}
