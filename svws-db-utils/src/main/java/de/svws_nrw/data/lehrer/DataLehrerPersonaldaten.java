package de.svws_nrw.data.lehrer;

import java.util.Map;

import de.svws_nrw.asd.data.lehrer.LehrerAbgangsgrundKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.data.lehrer.LehrerZugangsgrundKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerAbgangsgrund;
import de.svws_nrw.asd.types.lehrer.LehrerZugangsgrund;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das
 * Core-DTO {@link LehrerPersonaldaten}.
 */
public final class DataLehrerPersonaldaten extends DataManagerRevised<Long, DTOLehrer, LehrerPersonaldaten> {

	/**
	 * Erstellt einen {@link DataManager} für das Core-DTO {@link LehrerPersonaldaten}.
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerPersonaldaten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		/* setAttributesRequiredOnCreation muss nicht gesetzt werden, da die Lehrerpersonaldaten aktuell zu DTOLehrer gehören und diese automatisch beim
		 * Anlegen der Stammdaten mit erzeugt werden. */
	}

	@Override
	protected void initDTO(final DTOLehrer dto, final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newID;
	}

	@Override
	public LehrerPersonaldaten getById(final Long id) throws ApiOperationException {
		final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, id);
		if (lehrer == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Lehrkraft mit der ID %d gefunden.".formatted(id));
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
		daten.fachrichtungen.addAll(DataLehrerFachrichtungen.getListByLehrerId(conn, dto.ID));
		daten.lehrbefaehigungen.addAll(DataLehrerLehrbefaehigung.getListByLehrerId(conn, dto.ID));
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOLehrer dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "identNrTeil1" -> dto.identNrTeil1 =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_IdentNr1.datenlaenge(), "identNrTeil1");
			case "identNrTeil2SerNr" -> dto.identNrTeil2SerNr =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_SerNr.datenlaenge(), "identNrTeil2SerNr");
			case "personalaktennummer" -> dto.PANr =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_PANr.datenlaenge(), "personalaktennummer");
			case "lbvPersonalnummer" -> dto.personalNrLBV =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_LBVNr.datenlaenge(), "lbvPersonalnummer");
			case "lbvVerguetungsschluessel" -> dto.verguetungsSchluessel =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_VSchluessel.datenlaenge(), "lbvVerguetungsschluessel");
			case "zugangsdatum" -> dto.DatumZugang =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Titel.datenlaenge(), "zugangsdatum");
			case "zugangsgrund" -> dto.GrundZugang = validateZugangsGrund(value);
			case "abgangsdatum" -> dto.DatumAbgang =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_GrundAbgang.datenlaenge(), "abgangsdatum");
			case "abgangsgrund" -> dto.GrundZugang = validateAbgangsGrund(value);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	protected String validateZugangsGrund(final Object value) throws ApiOperationException {
		final String strData = JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_GrundZugang.datenlaenge(), "zugangsgrund");
		if (strData == null)
			return null;
		final int schuljahr = conn.getUser().schuleGetSchuljahr();
		final LehrerZugangsgrund zg = LehrerZugangsgrund.data().getWertByKuerzel(strData);
		if (zg == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final LehrerZugangsgrundKatalogEintrag eintrag = zg.daten(schuljahr);
		if (eintrag == null)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Zugangsgrund mit dem Kürzel %s ist im Schuljahr %d nicht gültig.".formatted(strData, schuljahr));
		return eintrag.kuerzel;
	}

	protected String validateAbgangsGrund(final Object value) throws ApiOperationException {
		final String strData = JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_GrundAbgang.datenlaenge(), "abgangsgrund");
		if (strData == null)
			return null;
		final int schuljahr = conn.getUser().schuleGetSchuljahr();
		final LehrerAbgangsgrund ag = LehrerAbgangsgrund.data().getWertByKuerzel(strData);
		if (ag == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final LehrerAbgangsgrundKatalogEintrag eintrag = ag.daten(schuljahr);
		if (eintrag == null)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Abgangsgrund mit dem Kürzel %s ist im Schuljahr %d nicht gültig.".formatted(strData, schuljahr));
		return eintrag.kuerzel;
	}

}
