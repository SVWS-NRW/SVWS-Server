package de.svws_nrw.data.lehrer;

import java.time.LocalDate;
import java.util.Map;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.types.lehrer.LehrerAbgangsgrund;
import de.svws_nrw.asd.types.lehrer.LehrerZugangsgrund;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link LehrerPersonaldaten}.
 */
public final class DataLehrerPersonaldaten extends DataManagerRevised<Long, DTOLehrer, LehrerPersonaldaten> {

	/**
	 * Erstellt einen {@link DataManagerRevised} für das Core-DTO {@link LehrerPersonaldaten}.
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerPersonaldaten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
	}

	@Override
	protected void initDTO(final DTOLehrer dto, final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newID;
	}

	@Override
	public LehrerPersonaldaten getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Lehrers darf nicht null sein.");
		}
		final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, id);
		if (lehrer == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Lehrkraft mit der ID %d gefunden.".formatted(id));
		}
		return map(lehrer);
	}

	@Override
	protected LehrerPersonaldaten map(final DTOLehrer dto) throws ApiOperationException {
		final LehrerPersonaldaten daten = new LehrerPersonaldaten();
		daten.id = dto.ID;
		daten.identNrTeil1 = dto.identNrTeil1;
		daten.identNrTeil2SerNr = dto.identNrTeil2SerNr;
		daten.personalaktennummer = dto.PANr;
		daten.lbvPersonalnummer = dto.personalNrLBV;
		daten.lbvVerguetungsschluessel = dto.verguetungsSchluessel;
		daten.zugangsdatum = dto.DatumZugang;
		daten.zugangsgrund = dto.GrundZugang;
		daten.abgangsdatum = dto.DatumAbgang;
		daten.abgangsgrund = dto.GrundAbgang;
		daten.abschnittsdaten.addAll(DataLehrerPersonalabschnittsdaten.getByLehrerId(conn, dto.ID));
		daten.lehraemter.addAll(DataLehrerLehramt.getListByLehrerId(conn, dto.ID));
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOLehrer dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "identNrTeil1" -> dto.identNrTeil1 =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_IdentNr1.datenlaenge(), name);
			case "identNrTeil2SerNr" -> dto.identNrTeil2SerNr =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_SerNr.datenlaenge(), name);
			case "personalaktennummer" -> dto.PANr =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_PANr.datenlaenge(), name);
			case "lbvPersonalnummer" -> dto.personalNrLBV =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_LBVNr.datenlaenge(), name);
			case "lbvVerguetungsschluessel" -> dto.verguetungsSchluessel =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_VSchluessel.datenlaenge(), name);
			case "zugangsdatum" -> updateZugangsdatum(dto, value);
			case "abgangsdatum" -> updateAbgangsdatum(dto, value);
			case "zugangsgrund" -> updateZugangsGrund(dto, value);
			case "abgangsgrund" -> updateAbgangsGrund(dto, value);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void updateAbgangsdatum(final DTOLehrer dto, final Object value) throws ApiOperationException {
		final String date = JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_DatumAbgang.datenlaenge(), "Abgangsdatum");
		validateDate(date);
		dto.DatumAbgang = date;
	}

	private static void updateZugangsdatum(final DTOLehrer dto, final Object value) throws ApiOperationException {
		final String date = JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_DatumZugang.datenlaenge(), "Zugangsdatum");
		validateDate(date);
		dto.DatumZugang = date;
	}

	private static void validateDate(final String date) throws ApiOperationException {
		if (StringUtils.isBlank(date)) {
			return;
		}
		try {
			LocalDate.parse(date);
		} catch (final Exception ignored) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Datumsformat für %s ist ungültig".formatted(date));
		}
	}

	private void updateZugangsGrund(final DTOLehrer dto, final Object value) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_GrundZugang.datenlaenge(), "Zugangsgrund");
		if (StringUtils.isNotBlank(kuerzel)) {
			final LehrerZugangsgrund zugangsgrund = LehrerZugangsgrund.data().getWertByKuerzel(kuerzel);
			validateCoreType(zugangsgrund, kuerzel, "Zugangsgrund");
		}
		dto.GrundZugang = kuerzel;
	}

	private void updateAbgangsGrund(final DTOLehrer dto, final Object value) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_GrundAbgang.datenlaenge(), "Abgangsgrund");
		if (StringUtils.isNotBlank(kuerzel)) {
			final LehrerAbgangsgrund abgangsgrund = LehrerAbgangsgrund.data().getWertByKuerzel(kuerzel);
			validateCoreType(abgangsgrund, kuerzel, "Abgangsgrund");
		}
		dto.GrundAbgang = kuerzel;
	}

	private <T extends CoreTypeData, U extends CoreType<T, U>> void validateCoreType(final CoreType<T, U> coreType, final String kuerzel,
			final String attributeName)
			throws ApiOperationException {
		if (coreType == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Kein %s mit dem Kürzel %s gefunden.".formatted(attributeName, kuerzel));
		}
		final int schuljahr = conn.getUser().schuleGetSchuljahr();
		final CoreTypeData daten = coreType.daten(schuljahr);
		if (daten == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der %s mit dem Kürzel %s ist im Schuljahr %d nicht gültig.".formatted(attributeName, kuerzel, schuljahr));
		}
	}

}
