package de.svws_nrw.data.lehrer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.lehrer.LehrerBeschaeftigungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerEinsatzstatusKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerRechtsverhaeltnisKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das
 * Core-DTO {@link LehrerStammdaten}.
 */
public final class DataLehrerPersonalabschnittsdaten extends DataManagerRevised<Long, DTOLehrerAbschnittsdaten, LehrerPersonalabschnittsdaten> {

	/**
	 * Erstellt einen neuen {@link DataManager} für das Core-DTO {@link LehrerStammdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerPersonalabschnittsdaten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id", "idLehrer", "idSchuljahresabschnitt");
		setAttributesRequiredOnCreation("idLehrer", "idSchuljahresabschnitt");
	}


	@Override
	protected void initDTO(final DTOLehrerAbschnittsdaten dto, final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newID;
	}


	@Override
	public LehrerPersonalabschnittsdaten getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die ID für die Personalabschnittsdaten der Lehrkraft darf nicht null sein.");
		final DTOLehrerAbschnittsdaten dto = conn.queryByKey(DTOLehrerAbschnittsdaten.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Personalabschnittsdaten einer Lehrkraft mit der ID %d gefunden.".formatted(id));
		final LehrerPersonalabschnittsdaten daten = map(dto);
		daten.anrechnungen.addAll(DataLehrerPersonalabschnittsdatenAnrechungen.getByLehrerabschnittsdatenId(conn, dto.Schuljahresabschnitts_ID, id));
		daten.mehrleistung.addAll(DataLehrerPersonalabschnittsdatenMehrleistungen.getByLehrerabschnittsdatenId(conn, dto.Schuljahresabschnitts_ID, id));
		daten.minderleistung.addAll(DataLehrerPersonalabschnittsdatenMinderleistungen.getByLehrerabschnittsdatenId(conn, id));
		daten.funktionen.addAll(DataLehrerPersonalabschnittsdatenLehrerfunktionen.getByLehrerabschnittsdatenId(conn, id));
		return daten;
	}


	protected static LehrerPersonalabschnittsdaten mapInternal(final DTOLehrerAbschnittsdaten dto) {
		final LehrerPersonalabschnittsdaten daten = new LehrerPersonalabschnittsdaten();
		daten.id = dto.ID;
		daten.idLehrer = dto.Lehrer_ID;
		daten.idSchuljahresabschnitt = dto.Schuljahresabschnitts_ID;
		daten.pflichtstundensoll = dto.PflichtstdSoll;
		daten.rechtsverhaeltnis = dto.Rechtsverhaeltnis;
		daten.beschaeftigungsart = dto.Beschaeftigungsart;
		daten.einsatzstatus = dto.Einsatzstatus;
		daten.stammschulnummer = dto.StammschulNr;
		return daten;
	}


	@Override
	protected LehrerPersonalabschnittsdaten map(final DTOLehrerAbschnittsdaten dto) throws ApiOperationException {
		return mapInternal(dto);
	}


	@Override
	protected void mapAttribute(final DTOLehrerAbschnittsdaten dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "pflichtstundensoll" -> dto.PflichtstdSoll = JSONMapper.convertToDouble(value, true);
			case "rechtsverhaeltnis" -> dto.Rechtsverhaeltnis = validateRechtverhaeltnis(value, dto);
			case "beschaeftigungsart" -> dto.Beschaeftigungsart = validateBeschaeftigunsart(value, dto);
			case "einsatzstatus" -> dto.Einsatzstatus = validateEinsatzstatus(value, dto);
			case "stammschulnummer" -> dto.StammschulNr =
					JSONMapper.convertToString(value, true, false, Schema.tab_LehrerAbschnittsdaten.col_StammschulNr.datenlaenge(), "stammschulnummer");
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}


	/**
	 * Ermittelt die Abschnittsdaten für den Lehrer mit der angegebenen ID und und gibt diese zurück.
	 *
	 * @param conn       die Datenbankverbindung zur Abfrage der Abschnittsdaten
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Liste mit den Abschnittsdaten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<LehrerPersonalabschnittsdaten> getByLehrerId(final DBEntityManager conn, final Long idLehrer) throws ApiOperationException {
		final List<LehrerPersonalabschnittsdaten> result = new ArrayList<>();
		// Bestimme die Abschnittsdaten des Lehrers
		final List<DTOLehrerAbschnittsdaten> abschnittsdaten =
				conn.queryList(DTOLehrerAbschnittsdaten.QUERY_BY_LEHRER_ID, DTOLehrerAbschnittsdaten.class, idLehrer);
		if (abschnittsdaten == null)
			return result;
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerAbschnittsdaten l : abschnittsdaten) {
			LehrerPersonalabschnittsdaten daten;
			daten = mapInternal(l);
			daten.anrechnungen.addAll(DataLehrerPersonalabschnittsdatenAnrechungen.getByLehrerabschnittsdatenId(conn, l.Schuljahresabschnitts_ID, l.ID));
			daten.mehrleistung.addAll(DataLehrerPersonalabschnittsdatenMehrleistungen.getByLehrerabschnittsdatenId(conn, l.Schuljahresabschnitts_ID, l.ID));
			daten.minderleistung.addAll(DataLehrerPersonalabschnittsdatenMinderleistungen.getByLehrerabschnittsdatenId(conn, l.ID));
			daten.funktionen.addAll(DataLehrerPersonalabschnittsdatenLehrerfunktionen.getByLehrerabschnittsdatenId(conn, l.ID));
			result.add(daten);
		}
		return result;
	}


	private String validateRechtverhaeltnis(final Object value, final DTOLehrerAbschnittsdaten dto) throws ApiOperationException {
		final String strData =
				JSONMapper.convertToString(value, true, false, Schema.tab_LehrerAbschnittsdaten.col_Rechtsverhaeltnis.datenlaenge(), "rechtsverhaeltnis");
		if (strData == null)
			return null;
		final LehrerRechtsverhaeltnis rv = LehrerRechtsverhaeltnis.data().getWertBySchluessel(strData);
		if (rv == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Das Rechtsverhältnis mit dem Kürzel %s ist nicht vorhanden".formatted(strData));
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID);
		final LehrerRechtsverhaeltnisKatalogEintrag eintrag = rv.daten(abschnitt.schuljahr);
		if (eintrag == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Rechtsverhältnis mit dem Kürzel %s ist im Schuljahr %d nicht gültig."
					.formatted(strData, abschnitt.schuljahr));
		return eintrag.kuerzel;
	}


	private String validateBeschaeftigunsart(final Object value, final DTOLehrerAbschnittsdaten dto) throws ApiOperationException {
		final String strData =
				JSONMapper.convertToString(value, true, false, Schema.tab_LehrerAbschnittsdaten.col_Beschaeftigungsart.datenlaenge(), "beschaeftigungsart");
		if (strData == null)
			return null;
		final LehrerBeschaeftigungsart ba = LehrerBeschaeftigungsart.data().getWertBySchluessel(strData);
		if (ba == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Beschäftigungsart mit dem Kürzel %s ist nicht vorhanden".formatted(strData));
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID);
		final LehrerBeschaeftigungsartKatalogEintrag eintrag = ba.daten(abschnitt.schuljahr);
		if (eintrag == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Beschäftigungsart mit dem Kürzel %s ist im Schuljahr %d nicht gültig."
					.formatted(strData, abschnitt.schuljahr));
		return eintrag.kuerzel;
	}


	private String validateEinsatzstatus(final Object value, final DTOLehrerAbschnittsdaten dto) throws ApiOperationException {
		final String strData =
				JSONMapper.convertToString(value, true, false, Schema.tab_LehrerAbschnittsdaten.col_Einsatzstatus.datenlaenge(), "einsatzstatus");
		if (strData == null)
			return null;
		final LehrerEinsatzstatus es = LehrerEinsatzstatus.data().getWertBySchluessel(strData);
		if (es == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Der Einsatzstatus mit dem Kürzel %s ist nicht vorhanden".formatted(strData));
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID);
		final LehrerEinsatzstatusKatalogEintrag eintrag = es.daten(abschnitt.schuljahr);
		if (eintrag == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Einsatzstatus mit dem Kürzel %s ist im Schuljahr %d nicht gültig."
					.formatted(strData, abschnitt.schuljahr));
		return eintrag.kuerzel;
	}

}
