package de.svws_nrw.data.jahrgaenge;

import de.svws_nrw.asd.data.jahrgang.JahrgaengeKatalogEintrag;
import de.svws_nrw.asd.data.schule.BildungsstufeKatalogEintrag;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.schule.Bildungsstufe;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.data.SimpleOperationResponse;
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
import java.util.Comparator;
import java.util.HashSet;
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
		setAttributesRequiredOnCreation("kuerzel", "bezeichnung", "kuerzelStatistik");
	}

	@Override
	public JahrgangsDaten map(final DTOJahrgang dto) {
		final JahrgangsDaten jahrgangsDaten = new JahrgangsDaten();
		jahrgangsDaten.id = dto.ID;
		jahrgangsDaten.kuerzel = dto.InternKrz;
		jahrgangsDaten.kuerzelStatistik = dto.ASDJahrgang;
		jahrgangsDaten.bezeichnung = Objects.requireNonNullElse(dto.ASDBezeichnung, "");
		jahrgangsDaten.kurzbezeichnung = dto.Kurzbezeichnung;
		jahrgangsDaten.kuerzelSchulgliederung = dto.GliederungKuerzel;
		jahrgangsDaten.idFolgejahrgang = dto.Folgejahrgang_ID;
		jahrgangsDaten.anzahlRestabschnitte = dto.AnzahlRestabschnitte;
		jahrgangsDaten.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		jahrgangsDaten.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		jahrgangsDaten.gueltigVon = dto.GueltigVon;
		jahrgangsDaten.gueltigBis = dto.GueltigBis;
		jahrgangsDaten.idBildungsstufe = mapSekundarstufe(dto.Sekundarstufe);
		return jahrgangsDaten;
	}

	private Long mapSekundarstufe(final String sekundarstufe) {
		final Bildungsstufe wertByKuerzel = Bildungsstufe.data().getWertByKuerzel(sekundarstufe);
		if (wertByKuerzel == null)
			return null;

		final BildungsstufeKatalogEintrag daten = wertByKuerzel.daten(this.conn.getUser().schuleGetSchuljahr());
		if (daten == null)
				return null;

		return daten.id;
	}

	@Override
	protected void initDTO(final DTOJahrgang dtoJahrgang, final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		dtoJahrgang.ID = newID;
	}

	@Override
	public List<JahrgangsDaten> getAll() {
		final List<DTOJahrgang> jahrgaenge = conn.queryAll(DTOJahrgang.class);
		final Set<Long> idsOfReferencedJahrgaenge = getIdsOfReferencedJahrgaenge(jahrgaenge.stream().map(j -> j.ID).collect(Collectors.toSet()));

		return jahrgaenge.stream()
				.map(this::map)
				.map(j -> setReferencedFlag(j, idsOfReferencedJahrgaenge))
				.sorted(Comparator.comparing(j -> j.id))
				.toList();
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
	protected long getLongId(final DTOJahrgang jahrgang) {
		return jahrgang.ID;
	}

	@Override
	protected void mapAttribute(final DTOJahrgang dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "kuerzel" -> updateKuerzel(dto, name, value);
			case "kuerzelStatistik" -> updateKuerzelStatistik(dto, value, name);
			case "bezeichnung" -> updateBezeichnung(dto, name, value);
			case "kurzbezeichnung" -> dto.Kurzbezeichnung =
					JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule_Jahrgaenge.col_Spaltentitel.datenlaenge(), name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToIntegerInRange(value, true, 0, 32001, name);
			case "kuerzelSchulgliederung" -> updateKuerzelSchulgliederung(dto, value, name);
			case "idFolgejahrgang" -> updateIdFolgejahrgang(dto, value, name);
			case "anzahlRestabschnitte" -> dto.AnzahlRestabschnitte = JSONMapper.convertToIntegerInRange(value, true, 0, 41, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, true, name);
			case "gueltigVon" -> dto.GueltigVon = JSONMapper.convertToLong(value, true, name);
			case "gueltigBis" -> dto.GueltigBis = JSONMapper.convertToLong(value, true, name);
			case "idBildungsstufe" -> updateSekundarstufe(dto, value, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void updateSekundarstufe(final DTOJahrgang dto, final Object value, final String name) throws ApiOperationException {
		final Long idBildungsstufe = JSONMapper.convertToLong(value, true, name);
		if (idBildungsstufe == null) {
			dto.Sekundarstufe = null;
			return;
		}
		final BildungsstufeKatalogEintrag eintrag = Bildungsstufe.data().getEintragByID(idBildungsstufe);
		if (eintrag == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Keine Bildungsstufe zur ID %d gefunden.".formatted(idBildungsstufe));

		dto.Sekundarstufe = eintrag.schluessel;
	}

	private static void validateId(final DTOJahrgang dtoJahrgang, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (id != dtoJahrgang.ID)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dtoJahrgang.ID));
	}

	private void updateBezeichnung(final DTOJahrgang dto, final String name, final Object value) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(
				value, false, false, Schema.tab_EigeneSchule_Jahrgaenge.col_ASDBezeichnung.datenlaenge(), name);
		if (Objects.equals(dto.ASDBezeichnung, bezeichnung) || bezeichnung.isBlank())
			return;

		final boolean bezeichnungAlreadyUsed = this.conn.queryAll(DTOJahrgang.class).stream()
				.anyMatch(j -> (j.ID != dto.ID) && bezeichnung.equalsIgnoreCase(j.ASDBezeichnung));
		if (bezeichnungAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));

		dto.ASDBezeichnung = bezeichnung;
	}

	private void updateKuerzel(final DTOJahrgang dto, final String name, final Object value) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(value, false, false, Schema.tab_EigeneSchule_Jahrgaenge.col_InternKrz.datenlaenge(), name);
		if (Objects.equals(dto.InternKrz, kuerzel) || kuerzel.isBlank())
			return;

		final boolean kuerzelAlreadyUsed = this.conn.queryAll(DTOJahrgang.class).stream()
				.anyMatch(j -> (j.ID != dto.ID) && kuerzel.equalsIgnoreCase(j.InternKrz));
		if (kuerzelAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Kürzel %s ist bereits vorhanden.".formatted(kuerzel));

		dto.InternKrz = kuerzel;
	}

	private void updateIdFolgejahrgang(final DTOJahrgang dtoJahrgang, final Object value, final String attrName) throws ApiOperationException {
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

	private void updateKuerzelSchulgliederung(final DTOJahrgang dtoJahrgang, final Object value, final String attrName) throws ApiOperationException {
		final String kuerzelSchuldgliederung = JSONMapper.convertToString(value, true, false, Schema.tab_EigeneSchule_Jahrgaenge.col_SGL.datenlaenge(), attrName);
		if (kuerzelSchuldgliederung == null) {
			dtoJahrgang.GliederungKuerzel = null;
			return;
		}

		final Schulgliederung schulgliederung = Schulgliederung.data().getWertBySchluessel(kuerzelSchuldgliederung);
		if (schulgliederung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Schulgliederung mit dem Schlüssel %s gefunden.".formatted(kuerzelSchuldgliederung));

		if (!schulgliederung.hatSchulform(conn.getUser().schuleGetSchuljahr(), conn.getUser().schuleGetSchulform()))
			throw new ApiOperationException(Status.CONFLICT, "Die Schulgliederung ist für diese Schulform nicht gültig.");

		dtoJahrgang.GliederungKuerzel = kuerzelSchuldgliederung;
	}

	private void updateKuerzelStatistik(final DTOJahrgang dtoJahrgang, final Object value, final String attrName) throws ApiOperationException {
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

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOJahrgang> jahrgaenge, final Map<Long, SimpleOperationResponse> mapResponses) {
		final Set<Long> result = getIdsOfReferencedJahrgaenge(jahrgaenge.stream().map(j -> j.ID).collect(Collectors.toSet()));
		jahrgaenge.stream()
				.filter(j -> result.contains(j.ID))
				.forEach(j -> markResponseAsFailed(mapResponses.get(j.ID), j.ASDBezeichnung));
	}

	private static void markResponseAsFailed(final SimpleOperationResponse response, final String bezeichnung) {
		response.success = false;
		response.log.add("Der Jahrgang mit der Bezeichnung %s ist in der Datenbank referenziert und kann daher nicht gelöscht werden".formatted(bezeichnung));
	}

	private Set<Long> getIdsOfReferencedJahrgaenge(final Set<Long> ids) {
		final String querySchueler = "SELECT DISTINCT a.Entlassjahrgang_ID FROM DTOSchueler a WHERE a.Entlassjahrgang_ID IN :ids";
		final String querySchuelerLernabschnittsdaten = "SELECT DISTINCT b.Jahrgang_ID FROM DTOSchuelerLernabschnittsdaten b WHERE b.Jahrgang_ID IN :ids";
		final String queryKlassen = "SELECT DISTINCT c.Jahrgang_ID FROM DTOKlassen c WHERE c.Jahrgang_ID IN :ids";
		final String queryStundenplanSchienen = "SELECT DISTINCT d.Jahrgang_ID FROM DTOStundenplanSchienen d WHERE d.Jahrgang_ID IN :ids";
		final String queryKurse = "SELECT DISTINCT e.Jahrgang_ID FROM DTOKurs e WHERE e.Jahrgang_ID IN :ids";

		final String query = String.join("\nUNION ALL\n", querySchueler, querySchuelerLernabschnittsdaten, queryKlassen, queryStundenplanSchienen, queryKurse);
		final List<Long> results = conn.query(query, Long.class).setParameter("ids", ids).getResultList();
		return new HashSet<>(results);
	}

	private JahrgangsDaten setReferencedFlag(final JahrgangsDaten jahrgangsdaten, final Set<Long> idsOfReferencedEinwilligungsarten) {
		jahrgangsdaten.referenziertInAnderenTabellen = idsOfReferencedEinwilligungsarten.contains(jahrgangsdaten.id);
		return jahrgangsdaten;
	}

}
