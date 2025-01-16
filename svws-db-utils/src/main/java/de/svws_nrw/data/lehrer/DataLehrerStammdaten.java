package de.svws_nrw.data.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schule.DataSchulleitung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFoto;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das
 * Core-DTO {@link LehrerStammdaten}.
 */
public final class DataLehrerStammdaten extends DataManagerRevised<Long, DTOLehrer, LehrerStammdaten> {

	/**
	 * Erstellt einen neuen {@link DataManager} für das Core-DTO {@link LehrerStammdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerStammdaten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("kuerzel", "nachname", "geschlecht", "personalTyp");
	}

	@Override
	protected void initDTO(final DTOLehrer dto, final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newID;
	}

	@Override
	public LehrerStammdaten getById(final Long id) throws ApiOperationException {
		final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, id);
		if (lehrer == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Lehrkraft mit der ID %d gefunden.".formatted(id));

		return map(lehrer);
	}

	/**
	 * Gibt die Liste der Stammdaten aller Lehrer zurück, die in der angegebenen Liste enthalten sind.
	 *
	 * @param idsLehrer die Liste der IDs der gewünschten Lehrer
	 *
	 * @return Liste der Stammdaten der Lehrer zu den IDs, bei einer leeren ID-Liste werden alle Lehrer zurückgegeben.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<LehrerStammdaten> getListByIDs(final List<Long> idsLehrer) throws ApiOperationException {
		if ((idsLehrer == null) || idsLehrer.isEmpty())
			return new ArrayList<>();

		final List<DTOLehrer> lehrer = conn.queryByKeyList(DTOLehrer.class, idsLehrer);
		return mapList(lehrer);
	}

	/**
	 * Gibt die Liste der Stammdaten der Lehrer zurück, die sichtbar sind.
	 *
	 * @return Liste der Stammdaten der sichtbaren Lehrer
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<LehrerStammdaten> getSichtbareLehrerStammdaten() throws ApiOperationException {
		final List<DTOLehrer> lehrer = conn.queryList(DTOLehrer.QUERY_BY_SICHTBAR, DTOLehrer.class, true);
		return mapList(lehrer);
	}

	@Override
	public List<LehrerStammdaten> getAll() throws ApiOperationException {
		final List<DTOLehrer> lehrer = conn.queryAll(DTOLehrer.class);
		return mapList(lehrer);
	}

	@Override
	protected LehrerStammdaten map(final DTOLehrer dtoLehrer) throws ApiOperationException {
		final LehrerStammdaten daten = mapWithoutFoto(dtoLehrer);
		final DTOLehrerFoto lehrerFoto = conn.queryByKey(DTOLehrerFoto.class, dtoLehrer.ID);
		daten.foto = (lehrerFoto == null) ? null : lehrerFoto.FotoBase64;
		return daten;
	}

	private LehrerStammdaten mapWithoutFoto(final DTOLehrer dtoLehrer) throws ApiOperationException {
		final LehrerStammdaten daten = new LehrerStammdaten();
		daten.id = dtoLehrer.ID;
		daten.kuerzel = dtoLehrer.Kuerzel;
		daten.personalTyp = (dtoLehrer.PersonTyp == null) ? "" : dtoLehrer.PersonTyp.kuerzel;
		daten.anrede = StringUtils.defaultString(dtoLehrer.Anrede);
		daten.titel = StringUtils.defaultString(dtoLehrer.Titel);
		daten.amtsbezeichnung = StringUtils.defaultString(dtoLehrer.Amtsbezeichnung);
		daten.nachname = StringUtils.defaultString(dtoLehrer.Nachname);
		daten.vorname = StringUtils.defaultString(dtoLehrer.Vorname);
		daten.geschlecht = (dtoLehrer.Geschlecht == null) ? -1 : dtoLehrer.Geschlecht.id;
		daten.geburtsdatum = dtoLehrer.Geburtsdatum;
		daten.staatsangehoerigkeitID = (dtoLehrer.staatsangehoerigkeit == null) ? null : dtoLehrer.staatsangehoerigkeit.daten.iso3;
		daten.strassenname = dtoLehrer.Strassenname;
		daten.hausnummer = dtoLehrer.HausNr;
		daten.hausnummerZusatz = dtoLehrer.HausNrZusatz;
		daten.wohnortID = dtoLehrer.Ort_ID;
		daten.ortsteilID = dtoLehrer.Ortsteil_ID;
		daten.telefon = dtoLehrer.telefon;
		daten.telefonMobil = dtoLehrer.telefonMobil;
		daten.emailDienstlich = dtoLehrer.eMailDienstlich;
		daten.emailPrivat = dtoLehrer.eMailPrivat;
		daten.istSichtbar = (dtoLehrer.Sichtbar == null) || dtoLehrer.Sichtbar;
		daten.istRelevantFuerStatistik = (dtoLehrer.statistikRelevant == null) || dtoLehrer.statistikRelevant;
		daten.leitungsfunktionen.addAll(DataSchulleitung.getSchulleitungsfunktionen(conn, dtoLehrer.ID));
		return daten;
	}

	@Override
	public List<LehrerStammdaten> mapList(final Collection<DTOLehrer> lehrer) throws ApiOperationException {
		final var result = new ArrayList<LehrerStammdaten>();
		if ((lehrer == null) || lehrer.isEmpty())
			return result;
		final Map<Long, DTOLehrerFoto> mapFotos = conn.queryByKeyList(DTOLehrerFoto.class, lehrer.stream().map(l -> l.ID).toList())
				.stream().collect(Collectors.toMap(lf -> lf.Lehrer_ID, lf -> lf));
		for (final DTOLehrer l : lehrer) {
			final LehrerStammdaten daten = mapWithoutFoto(l);
			final var tmpFoto = mapFotos.get(daten.id);
			daten.foto = (tmpFoto == null) ? null : tmpFoto.FotoBase64;
			result.add(daten);
		}
		return result;
	}

	@Override
	protected void mapAttribute(final DTOLehrer dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "kuerzel" -> updateKuerzel(dto, value);
			case "personalTyp" -> dto.PersonTyp =
					Optional.ofNullable(PersonalTyp.fromKuerzel(JSONMapper.convertToString(value, false, false, null, "personalTyp")))
							.orElseThrow(() -> new ApiOperationException(Status.CONFLICT, "Ein PersonalTyp mit dem Kuerzel %s wurde nicht gefunden.".formatted(value)));
			case "anrede" -> dto.Anrede =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Anrede.datenlaenge(), "anrede");
			case "titel" -> dto.Titel =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Titel.datenlaenge(), "titel");
			case "amtsbezeichnung" -> dto.Amtsbezeichnung =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Amtsbezeichnung.datenlaenge(), "amtsbezeichnung");
			case "nachname" -> dto.Nachname =
					JSONMapper.convertToString(value, false, false, Schema.tab_K_Lehrer.col_Nachname.datenlaenge(), "nachname");
			case "vorname" -> dto.Vorname =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Vorname.datenlaenge(), "vorname");
			case "geschlecht" -> dto.Geschlecht =
					Optional.ofNullable(Geschlecht.fromValue(JSONMapper.convertToInteger(value, false, "geschlecht")))
							.orElseThrow(() -> new ApiOperationException(Status.CONFLICT, "Kein Geschlecht mit dem dem Wert %s vorhanden.".formatted(value)));
			case "geburtsdatum" -> dto.Geburtsdatum =
					JSONMapper.convertToString(value, false, false, null, "geburtsdatum"); // TODO convertToDate im JSONMapper
			case "staatsangehoerigkeitID" -> updateStaatsangehoerigkeitID(dto, value);
			case "strassenname" -> dto.Strassenname =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Strassenname.datenlaenge(), "strassenname");
			case "hausnummer" -> dto.HausNr =
						 JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_HausNr.datenlaenge(), "hausnummer");
			case "hausnummerZusatz" -> dto.HausNrZusatz =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_HausNrZusatz.datenlaenge(), "hausnummerZusatz");
			case "wohnortID" -> setWohnort(dto, JSONMapper.convertToLong(value, true, "wohnortID"),
					Optional.ofNullable(map.get("ortsteilID")).map(v -> Long.parseLong(v.toString())).orElse(dto.Ortsteil_ID));
			case "ortsteilID" -> setWohnort(dto, (map.get("wohnortID") == null) ? dto.Ort_ID : (Long.valueOf((Integer) map.get("wohnortID"))),
					JSONMapper.convertToLong(value, true, "ortsteilID"));
			case "telefon" -> dto.telefon =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Tel.datenlaenge(), "telefon");
			case "telefonMobil" -> dto.telefonMobil =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Handy.datenlaenge(), "telefonMobil");
			case "emailDienstlich" -> dto.eMailDienstlich =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_EmailDienstlich.datenlaenge(), "emailDienstlich");
			case "emailPrivat" -> dto.eMailPrivat =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Email.datenlaenge(), "emailPrivat");
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, "istSichtbar");
			case "istRelevantFuerStatistik" -> dto.statistikRelevant = JSONMapper.convertToBoolean(value, false, "istRelevantFuerStatistik");
			case "foto" -> updateFoto(dto, value);
			case "leitungsfunktionen" -> {
				/* nicht notwendig */
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void updateKuerzel(final DTOLehrer dto, final Object value) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(value, false, false, Schema.tab_K_Lehrer.col_Kuerzel.datenlaenge(), "kuerzel");
		// Kuerzel ist unveraendert
		if ((dto.Kuerzel != null) && dto.Kuerzel.equals(kuerzel))
			return;

		// theoretischer Fall, der nicht eintreten sollte
		final List<DTOLehrer> lehrer = conn.queryList(DTOLehrer.QUERY_BY_KUERZEL, DTOLehrer.class, kuerzel);

		if (lehrer.size() > 1)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Mehr als ein Lehrer mit dem gleichen Kuerzel vorhanden");

		// kuerzel bereits vorhanden
		if (!lehrer.isEmpty()) {
			final DTOLehrer dtoLehrer = lehrer.getFirst();
			if ((dtoLehrer != null) && (dtoLehrer.ID != dto.ID))
				throw new ApiOperationException(Status.BAD_REQUEST, "Das Kuerzel %s ist bereits vorhanden.".formatted(value));
		}

		// kuerzel wird gepatched
		dto.Kuerzel = JSONMapper.convertToString(kuerzel, false, false, Schema.tab_K_Lehrer.col_Kuerzel.datenlaenge(), "kuerzel");
	}

	private static void updateStaatsangehoerigkeitID(final DTOLehrer dto, final Object value) throws ApiOperationException {
		final String id = JSONMapper.convertToString(value, true, true, null, "staatsangehoerigkeitID");
		if ((id == null) || (id.isBlank()))
			dto.staatsangehoerigkeit = null;
		else
			dto.staatsangehoerigkeit = Optional.ofNullable(Nationalitaeten.getByISO3(id))
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "keine Nationalität zur ID %s vorhanden.".formatted(id)));
	}

	/**
	 * Setzt den Wohnort bei den Lehrerdaten und prüft dabei die Angabe des Ortsteils auf Korrektheit in Bezug auf die Ortsteile
	 * in der Datenbank. Ggf. wird der Ortsteil auf null gesetzt.
	 *
	 * @param lehrer       das Lehrer-DTO der Datenbank
	 * @param wohnortID    die zu setzende Wohnort-ID
	 * @param ortsteilID   die zu setzende Ortsteil-ID
	 *
	 * @throws ApiOperationException   eine Exception mit dem HTTP-Fehlercode 409, falls die ID negative und damit ungültig ist
	 */
	private void setWohnort(final DTOLehrer lehrer, final Long wohnortID, final Long ortsteilID)
			throws ApiOperationException {
		if ((wohnortID != null) && ((wohnortID < 0) || (conn.queryByKey(DTOOrt.class, wohnortID) == null)))
			throw new ApiOperationException(Status.CONFLICT, "WohnortID %d ungültig.".formatted(wohnortID));

		if ((ortsteilID != null) && (ortsteilID < 0))
			throw new ApiOperationException(Status.CONFLICT, "OrtsteilID %d ungültig.".formatted(ortsteilID));

		lehrer.Ort_ID = wohnortID;
		// Prüfe, ob die Ortsteil ID in Bezug auf die WohnortID gültig ist, wähle hierbei null-Verweise auf die K_Ort-Tabelle als überall gültig
		lehrer.Ortsteil_ID = Optional.ofNullable(ortsteilID)
				.map(id -> {
					final DTOOrtsteil ortsteil = conn.queryByKey(DTOOrtsteil.class, id);
					if ((ortsteil == null) || (ortsteil.Ort_ID == null) || (!ortsteil.Ort_ID.equals(wohnortID)))
						return null;
					return id;
				})
				.orElse(null);
	}

	private void updateFoto(final DTOLehrer dto, final Object value) throws ApiOperationException {
		final String strData = JSONMapper.convertToString(value, true, true, null, "foto: strgData");
		DTOLehrerFoto lehrerFoto = conn.queryByKey(DTOLehrerFoto.class, dto.ID);
		if (lehrerFoto == null)
			lehrerFoto = new DTOLehrerFoto(dto.ID);
		final String oldFoto = lehrerFoto.FotoBase64;
		if (((strData == null) && (oldFoto == null)) || ((strData != null) && (strData.equals(oldFoto))))
			return;
		lehrerFoto.FotoBase64 = strData;
		conn.transactionPersist(lehrerFoto);
	}
}
