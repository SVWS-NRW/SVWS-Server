package de.svws_nrw.data.lehrer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.lehrer.LehrerBeschaeftigungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerEinsatzstatusKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerRechtsverhaeltnisKatalogEintrag;
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
import de.svws_nrw.service.lehrer.LehrerAnrechnungsstundenService;
import de.svws_nrw.service.lehrer.LehrerMehrleistungService;
import de.svws_nrw.service.lehrer.LehrerMinderleistungService;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das
 * Core-DTO {@link LehrerPersonalabschnittsdaten}.
 */
public final class DataLehrerPersonalabschnittsdaten extends DataManagerRevised<Long, DTOLehrerAbschnittsdaten, LehrerPersonalabschnittsdaten> {

	/** Nutzung neuer Services in einer alten Data-Klasse bis zur Umstellung dieser Klasse auf Services */
	private final LehrerAnrechnungsstundenService anrechnungsService;
	private final LehrerMehrleistungService mehrleistungService;
	private final LehrerMinderleistungService minderleistungService;

	/**
	 * Erstellt einen neuen {@link DataManager} für das Core-DTO {@link LehrerPersonalabschnittsdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerPersonalabschnittsdaten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id", "idLehrer", "idSchuljahresabschnitt");
		setAttributesRequiredOnCreation("idLehrer", "idSchuljahresabschnitt");

		// Nutzung neuer Services in einer alten Data-Klasse bis zur Umstellung dieser Klasse auf Services
		final LehrerServiceFactory factory = LehrerServiceFactory.getNewInstance();
		this.anrechnungsService = factory.getLehrerAnrechnungsstundenService();
		this.mehrleistungService = factory.getLehrerMehrleistungService();
		this.minderleistungService = factory.getLehrerMinderleistungService();
	}

	@Override
	protected void initDTO(final DTOLehrerAbschnittsdaten dto, final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newID;
	}


	@Override
	public LehrerPersonalabschnittsdaten getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Die ID für die Personalabschnittsdaten der Lehrkraft darf nicht null sein.");
		}
		final DTOLehrerAbschnittsdaten dto = conn.queryByKey(DTOLehrerAbschnittsdaten.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Personalabschnittsdaten einer Lehrkraft mit der ID %d gefunden.".formatted(id));
		}
		final LehrerPersonalabschnittsdaten daten = map(dto);
		daten.anrechnungen.addAll(anrechnungsService.getListByLehrerabschnittsdatenId(id));
		daten.mehrleistung.addAll(mehrleistungService.getListByLehrerabschnittsdatenId(id));
		daten.minderleistung.addAll(minderleistungService.getListByLehrerabschnittsdatenId(id));
		daten.funktionen.addAll(DataLehrerPersonalabschnittsdatenLehrerfunktionen.getByLehrerabschnittsdatenId(conn, id));
		return daten;
	}


	protected static LehrerPersonalabschnittsdaten mapInternal(final DTOLehrerAbschnittsdaten dto, final int schuljahr) {
		final LehrerPersonalabschnittsdaten daten = new LehrerPersonalabschnittsdaten();
		daten.id = dto.ID;
		daten.idLehrer = dto.Lehrer_ID;
		daten.idSchuljahresabschnitt = dto.Schuljahresabschnitts_ID;
		daten.pflichtstundensoll = dto.PflichtstdSoll;
		final var rechtsverhaeltnis = LehrerRechtsverhaeltnis.data().getWertBySchluessel(dto.Rechtsverhaeltnis);
		final var rechtsverhaeltnisEintrag = (rechtsverhaeltnis == null) ? null : rechtsverhaeltnis.daten(schuljahr);
		daten.idRechtsverhaeltnis = (rechtsverhaeltnisEintrag == null) ? null : rechtsverhaeltnisEintrag.id;
		final var beschaeftigungsart = LehrerBeschaeftigungsart.data().getWertBySchluessel(dto.Beschaeftigungsart);
		final var beschaeftigungsartEintrag = (beschaeftigungsart == null) ? null : beschaeftigungsart.daten(schuljahr);
		daten.idBeschaeftigungsart = (beschaeftigungsartEintrag == null) ? null : beschaeftigungsartEintrag.id;
		final var einsatzstatus = LehrerEinsatzstatus.data().getWertBySchluessel(dto.Einsatzstatus);
		final var einsatzstatusEintrag = (einsatzstatus == null) ? null : einsatzstatus.daten(schuljahr);
		daten.idEinsatzstatus = (einsatzstatusEintrag == null) ? null : einsatzstatusEintrag.id;
		daten.stammschulnummer = dto.StammschulNr;
		return daten;
	}


	@Override
	protected LehrerPersonalabschnittsdaten map(final DTOLehrerAbschnittsdaten dto) throws ApiOperationException {
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID);
		return mapInternal(dto, abschnitt.schuljahr);
	}


	@Override
	protected void mapAttribute(final DTOLehrerAbschnittsdaten dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "pflichtstundensoll" -> updatePflichtstundensoll(dto, value, name);
			case "idRechtsverhaeltnis" -> dto.Rechtsverhaeltnis = validateRechtverhaeltnis(value, dto);
			case "idBeschaeftigungsart" -> dto.Beschaeftigungsart = validateBeschaeftigunsart(value, dto);
			case "idEinsatzstatus" -> dto.Einsatzstatus = validateEinsatzstatus(value, dto);
			case "stammschulnummer" -> dto.StammschulNr =
					JSONMapper.convertToString(value, true, false, Schema.tab_LehrerAbschnittsdaten.col_StammschulNr.datenlaenge(), "stammschulnummer");
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void updatePflichtstundensoll(final DTOLehrerAbschnittsdaten dto, final Object value, final String name) throws ApiOperationException {
		final Double pflichtstundensoll = JSONMapper.convertToDouble(value, true, name);
		if (pflichtstundensoll == null) {
			dto.PflichtstdSoll = null;
			return;
		}

		final BigDecimal bd = BigDecimal.valueOf(pflichtstundensoll);
		if (bd.scale() > 2) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Wert Pflichtstundensoll darf höchstens zwei Nachkommastellen haben.");
		}

		dto.PflichtstdSoll = pflichtstundensoll;
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
		if (abschnittsdaten == null) {
			return result;
		}

		final LehrerServiceFactory factory = LehrerServiceFactory.getNewInstance();
		final LehrerMehrleistungService mehrleistungService = factory.getLehrerMehrleistungService();
		final LehrerMinderleistungService minderleistungService = factory.getLehrerMinderleistungService();
		final LehrerAnrechnungsstundenService anrechnungsstundenService = factory.getLehrerAnrechnungsstundenService();
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerAbschnittsdaten l : abschnittsdaten) {
			final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(l.Schuljahresabschnitts_ID);
			final LehrerPersonalabschnittsdaten daten = mapInternal(l, abschnitt.schuljahr);
			daten.anrechnungen.addAll(anrechnungsstundenService.getListByLehrerabschnittsdatenId(l.ID));
			daten.mehrleistung.addAll(mehrleistungService.getListByLehrerabschnittsdatenId(l.ID));
			daten.minderleistung.addAll(minderleistungService.getListByLehrerabschnittsdatenId(l.ID));
			daten.funktionen.addAll(DataLehrerPersonalabschnittsdatenLehrerfunktionen.getByLehrerabschnittsdatenId(conn, l.ID));
			result.add(daten);
		}
		return result;
	}


	private String validateRechtverhaeltnis(final Object value, final DTOLehrerAbschnittsdaten dto) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, true, "idRechtsverhaeltnis");
		if (id == null) {
			return null;
		}
		final LehrerRechtsverhaeltnis rv = LehrerRechtsverhaeltnis.data().getWertByIDOrNull(id);
		if (rv == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Das Rechtsverhältnis mit der ID %d ist nicht vorhanden".formatted(id));
		}
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID);
		final LehrerRechtsverhaeltnisKatalogEintrag eintrag = rv.daten(abschnitt.schuljahr);
		if (eintrag == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Rechtsverhältnis mit dem Kürzel %s ist im Schuljahr %d nicht gültig."
					.formatted(id, abschnitt.schuljahr));
		}
		return eintrag.kuerzel;
	}


	private String validateBeschaeftigunsart(final Object value, final DTOLehrerAbschnittsdaten dto) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, true, "idBeschaeftigungsart");
		if (id == null) {
			return null;
		}
		final LehrerBeschaeftigungsart ba = LehrerBeschaeftigungsart.data().getWertByIDOrNull(id);
		if (ba == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Die Beschäftigungsart mit dem Kürzel %s ist nicht vorhanden".formatted(id));
		}
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID);
		final LehrerBeschaeftigungsartKatalogEintrag eintrag = ba.daten(abschnitt.schuljahr);
		if (eintrag == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Beschäftigungsart mit dem Kürzel %s ist im Schuljahr %d nicht gültig."
					.formatted(id, abschnitt.schuljahr));
		}
		return eintrag.kuerzel;
	}


	private String validateEinsatzstatus(final Object value, final DTOLehrerAbschnittsdaten dto) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, true, "idEinsatzstatus");
		if (id == null) {
			return null;
		}
		final LehrerEinsatzstatus es = LehrerEinsatzstatus.data().getWertByIDOrNull(id);
		if (es == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Der Einsatzstatus mit dem Kürzel %s ist nicht vorhanden".formatted(id));
		}
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID);
		final LehrerEinsatzstatusKatalogEintrag eintrag = es.daten(abschnitt.schuljahr);
		if (eintrag == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Einsatzstatus mit dem Kürzel %s ist im Schuljahr %d nicht gültig."
					.formatted(id, abschnitt.schuljahr));
		}
		return eintrag.kuerzel;
	}

}
