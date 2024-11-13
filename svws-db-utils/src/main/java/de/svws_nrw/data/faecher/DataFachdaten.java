package de.svws_nrw.data.faecher;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;



/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das
 * Core-DTO {@link FachDaten}.
 */
public final class DataFachdaten extends DataManagerRevised<Long, DTOFach, FachDaten> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link FachDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataFachdaten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("ID");
		setAttributesRequiredOnCreation("kuerzel", "kuerzelStatistik");
	}

	@Override
	protected void initDTO(final DTOFach dto, final Long newId, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newId;
	}

	@Override
	public FachDaten getById(final Long id) throws ApiOperationException {
		final DTOFach fach = conn.queryByKey(DTOFach.class, id);
		if (fach == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine FachDaten mit der ID %d gefunden".formatted(id));

		return map(fach);
	}

	@Override
	protected FachDaten map(final DTOFach dtoFach) throws ApiOperationException {
		final FachDaten daten = new FachDaten();
		daten.id = dtoFach.ID;
		daten.kuerzel = (dtoFach.Kuerzel == null) ? "" : dtoFach.Kuerzel;
		daten.kuerzelStatistik = dtoFach.StatistikKuerzel;
		daten.bezeichnung = (dtoFach.Bezeichnung == null) ? "" : dtoFach.Bezeichnung;
		daten.sortierung = dtoFach.SortierungAllg;
		daten.istOberstufenFach = dtoFach.IstOberstufenFach;
		daten.istPruefungsordnungsRelevant = dtoFach.IstPruefungsordnungsRelevant;
		daten.istSichtbar = dtoFach.Sichtbar;
		daten.aufgabenfeld = dtoFach.Aufgabenfeld;
		daten.bilingualeSprache = dtoFach.Unterichtssprache;
		daten.istNachpruefungErlaubt = dtoFach.IstNachpruefungErlaubt;
		daten.aufZeugnis = dtoFach.AufZeugnis;
		daten.bezeichnungZeugnis = (dtoFach.BezeichnungZeugnis == null) ? "" : dtoFach.BezeichnungZeugnis;
		daten.bezeichnungUeberweisungszeugnis = (dtoFach.BezeichnungUeberweisungsZeugnis == null) ? "" : dtoFach.BezeichnungUeberweisungsZeugnis;
		daten.maxZeichenInFachbemerkungen = (dtoFach.MaxBemZeichen == null) ? Integer.MAX_VALUE : dtoFach.MaxBemZeichen;
		daten.istSchriftlichZK = dtoFach.IstSchriftlichZK;
		daten.istSchriftlichBA = dtoFach.IstSchriftlichBA;
		daten.holeAusAltenLernabschnitten = dtoFach.AbgeschlFaecherHolen;
		daten.istFHRFach = ((dtoFach.GewichtungFHR != null) && (dtoFach.GewichtungFHR != 0));
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOFach dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "kuerzel" -> dto.Kuerzel = JSONMapper.convertToString(value, false, false, 20, "kuerzel");
			case "kuerzelStatistik" -> {
				final String fachKuerzel = JSONMapper.convertToString(value, false, false, 2, "kuerzelStatistik");
				final Fach fach = Fach.data().getWertBySchluessel(fachKuerzel);
				if (fach == null)
					throw new ApiOperationException(Status.NOT_FOUND, "Ein Fach mit dem Kuerzel %s wurde nicht gefunden".formatted(fachKuerzel));
				dto.StatistikKuerzel = fachKuerzel;
			}
			case "bezeichnung" -> dto.Bezeichnung = JSONMapper.convertToString(value, false, true, 255, "bezeichnung");
			case "istPruefungsordnungsRelevant" -> dto.IstPruefungsordnungsRelevant = JSONMapper.convertToBoolean(
					value, false, "istPruefungsordnungsRelevant");
			case "istOberstufenFach" -> dto.IstOberstufenFach = JSONMapper.convertToBoolean(value, false, "istOberstufenFach");
			case "sortierung" -> dto.SortierungAllg = JSONMapper.convertToIntegerInRange(
					value, false, 0, Integer.MAX_VALUE, "sortierung");
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, "istSichtbar");
			case "aufgabenfeld" -> dto.Aufgabenfeld = JSONMapper.convertToString(value, true, true, 2, "aufgabenfeld");
			case "bilingualeSprache" -> dto.Unterichtssprache = JSONMapper.convertToString(
					value, true, true, 1, "bilingualeSprache");
			case "istNachpruefungErlaubt" -> dto.IstNachpruefungErlaubt = JSONMapper.convertToBoolean(value, false, "istNachpruefungErlaubt");
			case "aufZeugnis" -> dto.AufZeugnis = JSONMapper.convertToBoolean(value, false, "aufZeugnis");
			case "bezeichnungZeugnis" -> dto.BezeichnungZeugnis = JSONMapper.convertToString(
					value, false, true, 255, "bezeichnungZeugnis");
			case "bezeichnungUeberweisungszeugnis" -> dto.BezeichnungUeberweisungsZeugnis = JSONMapper.convertToString(
					value, false, true, 255, "bezeichnungUeberweisungszeugnis");
			case "maxZeichenInFachbemerkungen" -> dto.MaxBemZeichen = JSONMapper.convertToIntegerInRange(
					value, false, 1, null, "maxZeichenInFachbemerkungen");
			case "istSchriftlichZK" -> dto.IstSchriftlichZK = JSONMapper.convertToBoolean(value, false, "istSchriftlichZK");
			case "istSchriftlichBA" -> dto.IstSchriftlichBA = JSONMapper.convertToBoolean(value, false, "istSchriftlichBA");
			case "holeAusAltenLernabschnitten" -> dto.AbgeschlFaecherHolen = JSONMapper.convertToBoolean(
					value, false, "holeAusAltenLernabschnitten");
			case "istFHRFach" -> dto.GewichtungFHR = JSONMapper.convertToBoolean(value, false, "istFHRFach") ? 1 : 0;
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	/**
	 * Mappt eine Liste von {@link DTOFach} Objekten zu {@link FachDaten} nach Id
	 *
	 * @param dtoFaecher   Liste der Fach-DTOs, aus denen die Map erstellt werden soll.
	 *
	 * @return Map der Fachdaten zur Fach-ID.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Map<Long, FachDaten> getMapFachdatenFromDTOFachList(final List<DTOFach> dtoFaecher) throws ApiOperationException {
		if ((dtoFaecher == null) || (dtoFaecher.isEmpty()))
			return Collections.emptyMap();

		final Map<Long, FachDaten> fachdatenMap = new HashMap<>();
		for (final DTOFach dtoFach : dtoFaecher) {
			if (dtoFach != null)
				fachdatenMap.put(dtoFach.ID, map(dtoFach));
		}
		return fachdatenMap;
	}
}
