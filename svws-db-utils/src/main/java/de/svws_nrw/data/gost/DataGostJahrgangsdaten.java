package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.jahrgang.JahrgangsUtils;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangBeratungslehrer;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostJahrgangsdaten}.
 */
public final class DataGostJahrgangsdaten extends DataManager<Integer> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostJahrgangsdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostJahrgangsdaten(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Liest die Vorlage-Daten für neue Abiturjahrgänge aus der Datenbank.
	 * Ist keine Vorlage vorhanden, so wird ein Eintrag in der Datenbank generiert.
	 *
	 * @param conn   die zu nutzende Datenbank-Verbindung
	 *
	 * @return die Vorlage-Daten
	 */
	public static DTOGostJahrgangsdaten getVorlage(final DBEntityManager conn) {
		DTOGostJahrgangsdaten jahrgangsdaten = conn.queryByKey(DTOGostJahrgangsdaten.class, -1);
		if (jahrgangsdaten == null) {
			jahrgangsdaten = new DTOGostJahrgangsdaten(-1);
			jahrgangsdaten.TextBeratungsbogen = "";
			jahrgangsdaten.TextMailversand = "";
			jahrgangsdaten.ZusatzkursGEErstesHalbjahr = "Q2.1";
			jahrgangsdaten.ZusatzkursGEVorhanden = true;
			jahrgangsdaten.ZusatzkursSWErstesHalbjahr = "Q2.1";
			jahrgangsdaten.ZusatzkursSWVorhanden = true;
			conn.persist(jahrgangsdaten);
		}
		return jahrgangsdaten;
	}

	@Override
	public Response get(final Integer abi_jahrgang) {
		final DTOEigeneSchule schule = GostUtils.pruefeSchuleMitGOSt(conn);

    	// Bestimme den aktuellen Schuljahresabschnitt der Schule
		final DTOSchuljahresabschnitte aktuellerAbschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (aktuellerAbschnitt == null)
    		return OperationError.NOT_FOUND.getResponse();

		// Bestimme die Jahrgaenge der Schule
		final List<DTOJahrgang> dtosJahrgaenge = conn.queryAll(DTOJahrgang.class);
		if ((dtosJahrgaenge == null) || (dtosJahrgaenge.size() <= 0))
    		return OperationError.NOT_FOUND.getResponse();

    	// Lese alle Abiturjahrgänge aus der Datenbank ein und ergänze diese im Vektor
		final DTOGostJahrgangsdaten jahrgangsdaten = (abi_jahrgang == -1)
				? getVorlage(conn)
				: conn.queryByKey(DTOGostJahrgangsdaten.class, abi_jahrgang);
		if (jahrgangsdaten == null)
    		return OperationError.NOT_FOUND.getResponse();

		final GostJahrgangsdaten daten = new GostJahrgangsdaten();
		daten.abiturjahr = jahrgangsdaten.Abi_Jahrgang;
		if (daten.abiturjahr >= 0) {
			final int restjahre = jahrgangsdaten.Abi_Jahrgang - aktuellerAbschnitt.Jahr;
			for (final DTOJahrgang jahrgang : dtosJahrgaenge) {
				final Integer jahrgangRestjahre = JahrgangsUtils.getRestlicheJahre(schule.Schulform, jahrgang.Gliederung, jahrgang.ASDJahrgang);
				if (jahrgangRestjahre != null && restjahre == jahrgangRestjahre) {
					daten.jahrgang = jahrgang.ASDJahrgang;
					break;
				}
			}
			daten.bezeichnung = "Abi " + daten.abiturjahr + ((daten.jahrgang == null) ? "" : " (" + daten.jahrgang + ")");
			daten.istAbgeschlossen = (restjahre < 1);
		} else {
			daten.jahrgang = null;
			daten.bezeichnung = "Allgemein / Vorlage";
			daten.istAbgeschlossen = false;
		}
    	daten.textBeratungsbogen = jahrgangsdaten.TextBeratungsbogen;
    	daten.textMailversand = jahrgangsdaten.TextMailversand;
    	daten.hatZusatzkursGE = jahrgangsdaten.ZusatzkursGEVorhanden;
    	daten.beginnZusatzkursGE = jahrgangsdaten.ZusatzkursGEErstesHalbjahr;
    	daten.hatZusatzkursSW = jahrgangsdaten.ZusatzkursSWVorhanden;
    	daten.beginnZusatzkursSW = jahrgangsdaten.ZusatzkursSWErstesHalbjahr;
    	// Ergänze die Information, ob bereits eine Blockung persistiert wurde anhand der angelegten Kurse in den entsprechenden Lernabschnitten
    	if (abi_jahrgang >= 0) {
    		final int anzahlAbschnitte = DataSchuleStammdaten.getAnzahlAbschnitte(conn);
	    	final List<Integer> jahre = Arrays.asList(abi_jahrgang - 1, abi_jahrgang - 2, abi_jahrgang - 3);
	    	final List<DTOSchuljahresabschnitte> alleAbschnitte = conn.queryNamed("DTOSchuljahresabschnitte.jahr.multiple", jahre, DTOSchuljahresabschnitte.class);
	    	for (final DTOSchuljahresabschnitte abschnitt : alleAbschnitte) {
	    		final GostHalbjahr halbjahr = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(abi_jahrgang, abschnitt.Jahr,
	    				(anzahlAbschnitte == 4) ? (abschnitt.Abschnitt + 1) / 2 : abschnitt.Abschnitt);
	    		daten.istBlockungFestgelegt[halbjahr.id] = GostUtils.pruefeHatOberstufenKurseInAbschnitt(conn, halbjahr, abschnitt);
	    	}
    	}
    	// Ergänze die Beratungslehrer
    	final List<DTOGostJahrgangBeratungslehrer> dtosBeratungslehrer = conn.queryNamed("DTOGostJahrgangBeratungslehrer.abi_jahrgang", daten.abiturjahr, DTOGostJahrgangBeratungslehrer.class);
    	daten.beratungslehrer.addAll(DataGostBeratungslehrer.getBeratungslehrer(conn, dtosBeratungslehrer));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Integer abiturjahr, final InputStream is) {
    	final Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() <= 0)
	    	return Response.status(Status.OK).build();
		try {
			conn.transactionBegin();
			GostUtils.pruefeSchuleMitGOSt(conn);
			final DTOGostJahrgangsdaten jahrgangsdaten = conn.queryByKey(DTOGostJahrgangsdaten.class, abiturjahr);
	    	if (jahrgangsdaten == null)
	    		throw OperationError.NOT_FOUND.exception();
	    	for (final Entry<String, Object> entry : map.entrySet()) {
	    		final String key = entry.getKey();
	    		final Object value = entry.getValue();
	    		switch (key) {
					case "abiturjahr" -> {
						final Integer patch_abiturjahr = JSONMapper.convertToInteger(value, true);
						if ((patch_abiturjahr == null) || (patch_abiturjahr.intValue() != abiturjahr.intValue()))
							throw OperationError.BAD_REQUEST.exception();
					}
    				case "jahrgang" -> throw OperationError.BAD_REQUEST.exception();
    				case "bezeichnung" -> throw OperationError.BAD_REQUEST.exception();
    				case "istAbgeschlossen" -> throw OperationError.BAD_REQUEST.exception();
    				case "textBeratungsbogen" -> jahrgangsdaten.TextBeratungsbogen = JSONMapper.convertToString(value, true, true);
    				case "textMailversand" -> jahrgangsdaten.TextMailversand = JSONMapper.convertToString(value, true, true);
    				case "hatZusatzkursGE" -> jahrgangsdaten.ZusatzkursGEVorhanden = JSONMapper.convertToBoolean(value, false);
    				case "beginnZusatzkursGE" -> {
    					final String tmp = JSONMapper.convertToString(value, false, false);
    					final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(tmp);
    					if ((halbjahr == null) || (halbjahr.istEinfuehrungsphase()))
    						throw OperationError.BAD_REQUEST.exception();
    					jahrgangsdaten.ZusatzkursGEErstesHalbjahr = halbjahr.kuerzel;
    				}
    				case "hatZusatzkursSW" -> jahrgangsdaten.ZusatzkursSWVorhanden = JSONMapper.convertToBoolean(value, false);
    				case "beginnZusatzkursSW" -> {
    					final String tmp = JSONMapper.convertToString(value, false, false);
    					final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(tmp);
    					if ((halbjahr == null) || (halbjahr.istEinfuehrungsphase()))
    						throw OperationError.BAD_REQUEST.exception();
    					jahrgangsdaten.ZusatzkursSWErstesHalbjahr = halbjahr.kuerzel;
    				}
    				// TODO case "beratungslehrer" -> TODO set Beratungslehrer - zusätzliche API
	    			default -> throw OperationError.BAD_REQUEST.exception();
	    		}
	    	}
	    	conn.transactionPersist(jahrgangsdaten);
	    	conn.transactionCommit();
	    	return Response.status(Status.OK).build();
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webAppException)
				return webAppException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			// Perform a rollback if necessary
			conn.transactionRollback();
		}
	}

}
