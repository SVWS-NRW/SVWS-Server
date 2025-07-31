package de.svws_nrw.data.jahrgaenge;

import de.svws_nrw.asd.data.jahrgang.JahrgaengeKatalogEintrag;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link JahrgangsDaten}.
 */
public final class DataJahrgangsdaten extends DataManagerRevised<Long, DTOJahrgang, JahrgangsDaten> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link JahrgangsDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataJahrgangsdaten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("kuerzel", "kuerzelStatistik");
	}

	@Override
	public JahrgangsDaten map(final DTOJahrgang dtoJahrgang) {
		final JahrgangsDaten jahrgangsDaten = new JahrgangsDaten();
		jahrgangsDaten.id = dtoJahrgang.ID;
		jahrgangsDaten.kuerzel = dtoJahrgang.InternKrz;
		jahrgangsDaten.kuerzelStatistik = dtoJahrgang.ASDJahrgang;
		jahrgangsDaten.bezeichnung = (dtoJahrgang.ASDBezeichnung == null) ? "" : dtoJahrgang.ASDBezeichnung;
		jahrgangsDaten.kurzbezeichnung = dtoJahrgang.Kurzbezeichnung;
		jahrgangsDaten.kuerzelSchulgliederung = dtoJahrgang.GliederungKuerzel;
		jahrgangsDaten.idFolgejahrgang = dtoJahrgang.Folgejahrgang_ID;
		jahrgangsDaten.anzahlRestabschnitte = dtoJahrgang.AnzahlRestabschnitte;
		jahrgangsDaten.sortierung = (dtoJahrgang.Sortierung == null) ? 32000 : dtoJahrgang.Sortierung;
		jahrgangsDaten.istSichtbar = (dtoJahrgang.Sichtbar == null) || (dtoJahrgang.Sichtbar);
		jahrgangsDaten.gueltigVon = dtoJahrgang.GueltigVon;
		jahrgangsDaten.gueltigBis = dtoJahrgang.GueltigBis;
		return jahrgangsDaten;
	}

	@Override
	protected void initDTO(final DTOJahrgang dtoJahrgang, final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		dtoJahrgang.ID = newID;
	}

	@Override
	public List<JahrgangsDaten> getAll() {
		final List<DTOJahrgang> jahrgaenge = conn.queryAll(DTOJahrgang.class);
		return jahrgaenge.stream().map(this::map).toList();
	}

	@Override
	public JahrgangsDaten getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Keine ID für den Jahrgang übergeben.");

		final DTOJahrgang jahrgang = conn.queryByKey(DTOJahrgang.class, id);
		if (jahrgang == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Jahrgang zur ID %d gefunden.".formatted(id));

		return map(jahrgang);
	}

	@Override
	protected void mapAttribute(final DTOJahrgang dtoJahrgang, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, "id");
				if (id != dtoJahrgang.ID)
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dtoJahrgang.ID));
			}
			case "kuerzel" -> mapKuerzel(dtoJahrgang, name, value);
			case "kuerzelStatistik" -> mapKuerzelStatistik(dtoJahrgang, value, name);
			case "bezeichnung" -> mapBezeichnung(dtoJahrgang, name, value);
			case "kurzbezeichnung" -> dtoJahrgang.Kurzbezeichnung =
					JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule_Jahrgaenge.col_Spaltentitel.datenlaenge(), name);
			case "sortierung" -> dtoJahrgang.Sortierung = JSONMapper.convertToInteger(value, true, name);
			case "kuerzelSchulgliederung" -> mapKuerzelSchulgliederung(dtoJahrgang, value, name);
			case "idFolgejahrgang" -> mapIdFolgejahrgang(dtoJahrgang, value, name);
			case "anzahlRestabschnitte" -> dtoJahrgang.AnzahlRestabschnitte = JSONMapper.convertToInteger(value, true, name);
			case "istSichtbar" -> dtoJahrgang.Sichtbar = JSONMapper.convertToBoolean(value, true, name);
			case "gueltigVon" -> dtoJahrgang.GueltigVon = JSONMapper.convertToLong(value, true, name);
			case "gueltigBis" -> dtoJahrgang.GueltigBis = JSONMapper.convertToLong(value, true, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void mapBezeichnung(final DTOJahrgang dto, final String name, final Object value) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, true, true,
				Schema.tab_EigeneSchule_Jahrgaenge.col_ASDBezeichnung.datenlaenge(), name);
		// Bezeichnung unverändert
		if ((dto.ASDBezeichnung != null) && dto.ASDBezeichnung.equals(bezeichnung))
			return;

		//Bezeichnung null
		if (bezeichnung == null) {
			dto.ASDBezeichnung = null;
			return;
		}

		//theoretischer Fall, der nicht eintreffen sollte
		final List<DTOJahrgang> jahrgaenge = this.conn.queryList(DTOJahrgang.QUERY_BY_ASDBEZEICHNUNG, DTOJahrgang.class, bezeichnung);
		if (jahrgaenge.size() > 1)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Mehr als ein Jahrgang mit der gleichen Bezeichnung vorhanden.");

		//Jahrgang mit dieser Bezeichnung bereits vorhanden
		if (jahrgaenge.size() == 1) {
			final DTOJahrgang jahrgang = jahrgaenge.getFirst();
			if ((jahrgang != null) && (jahrgang.ID != dto.ID))
				throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));
		}

		dto.ASDBezeichnung = bezeichnung;
	}

	private void mapKuerzel(final DTOJahrgang dto, final String name, final Object value) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(value, false, false, Schema.tab_EigeneSchule_Jahrgaenge.col_InternKrz.datenlaenge(), name);
		// Kürzel unverändert
		if ((dto.InternKrz != null) && dto.InternKrz.equals(kuerzel))
			return;

		//theoretischer Fall, der nicht eintreffen sollte
		final List<DTOJahrgang> jahrgaenge = this.conn.queryList(DTOJahrgang.QUERY_BY_INTERNKRZ, DTOJahrgang.class, kuerzel);
		if (jahrgaenge.size() > 1)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Mehr als ein Jahrgang mit dem gleichen Kürzel vorhanden.");

		//Jahrgang mit dieser Bezeichnung bereits vorhanden
		if (jahrgaenge.size() == 1) {
			final DTOJahrgang jahrgang = jahrgaenge.getFirst();
			if ((jahrgang != null) && (jahrgang.ID != dto.ID))
				throw new ApiOperationException(Status.BAD_REQUEST, "Das Kürzel %s ist bereits vorhanden.".formatted(kuerzel));
		}

		dto.InternKrz = kuerzel;
	}

	private void mapIdFolgejahrgang(final DTOJahrgang dtoJahrgang, final Object value, final String attrName) throws ApiOperationException {
		final Long idFolgejahrgang = JSONMapper.convertToLong(value, true, attrName);
		if (idFolgejahrgang == null) {
			dtoJahrgang.Folgejahrgang_ID = null;
			return;
		}
		final DTOJahrgang folgeJahrgang = conn.queryByKey(DTOJahrgang.class, idFolgejahrgang);
		if (folgeJahrgang == null)
			throw new ApiOperationException(Status.CONFLICT, "Ein Folgejahrgang mit der ID %d wurde nicht gefunden.".formatted(idFolgejahrgang));
		dtoJahrgang.Folgejahrgang_ID = idFolgejahrgang;
	}

	private void mapKuerzelSchulgliederung(final DTOJahrgang dtoJahrgang, final Object value, final String attrName) throws ApiOperationException {
		final String kuerzelSchuldgliederung = JSONMapper.convertToString(value, true, false, null, attrName);
		final Schulgliederung schulgliederung = Schulgliederung.data().getWertBySchluessel(kuerzelSchuldgliederung);
		if (schulgliederung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Schulgliederung mit dem Schlüssel %s gefunden.".formatted(kuerzelSchuldgliederung));

		if (!schulgliederung.hatSchulform(conn.getUser().schuleGetSchuljahr(), conn.getUser().schuleGetSchulform()))
			throw new ApiOperationException(Status.CONFLICT, "Die Schulgliederung ist für diese Schulform nicht gültig.");

		dtoJahrgang.GliederungKuerzel = kuerzelSchuldgliederung;
	}

	private void mapKuerzelStatistik(final DTOJahrgang dtoJahrgang, final Object value, final String attrName) throws ApiOperationException {
		final String kuerzelASDJahrgang = JSONMapper.convertToString(
				value, true, false, Schema.tab_EigeneSchule_Jahrgaenge.col_ASDJahrgang.datenlaenge(), attrName);
		if (kuerzelASDJahrgang == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Kein ASD-Jahrgang ausgewählt.");

		final Jahrgaenge jahrgang = Jahrgaenge.data().getWertBySchluessel(kuerzelASDJahrgang);
		if (jahrgang == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Jahrgang mit dem Schlüssel %s gefunden.".formatted(kuerzelASDJahrgang));

		final int schuljahr = conn.getUser().schuleGetSchuljahr();
		final JahrgaengeKatalogEintrag eintrag = jahrgang.daten(schuljahr);
		dtoJahrgang.ASDJahrgang = eintrag.kuerzel;
	}

	/**
	 * Bestimmt zu den übergebenen Klassen die jeweils zugehörigen Jahrgänge aus der Datenbank und gib eine
	 * Map mit der Zuordnung zurück.
	 *
	 * @param conn      die aktuelle Datenbank-Verbindung
	 * @param klassen   die Klassen
	 *
	 * @return die Zuordnung der Jahrgänge zu den Klassen-IDs
	 */
	public static Map<Long, DTOJahrgang> getDTOMapByKlassen(final @NotNull DBEntityManager conn, final @NotNull List<DTOKlassen> klassen) {
		if (klassen.isEmpty())
			return Collections.emptyMap();

		final Set<Long> idsJahrgaenge = klassen.stream().map(kl -> kl.Jahrgang_ID).filter(Objects::nonNull).collect(Collectors.toSet());
		if (idsJahrgaenge.isEmpty())
			return Collections.emptyMap();

		final Map<Long, DTOJahrgang> jahrgaengeById = conn.queryByKeyList(DTOJahrgang.class, idsJahrgaenge).stream()
				.collect(Collectors.toMap(j -> j.ID, j -> j));
		return klassen.stream().filter(kl -> (kl.Jahrgang_ID != null)).filter(kl -> jahrgaengeById.containsKey(kl.Jahrgang_ID))
				.collect(Collectors.toMap(kl -> kl.ID, kl -> jahrgaengeById.get(kl.Jahrgang_ID)));
	}

}
