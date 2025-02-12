package de.svws_nrw.data.jahrgaenge;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.asd.data.jahrgang.JahrgaengeKatalogEintrag;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Diese Klasse erweitert den abstrakten {@link de.svws_nrw.data.DataManagerRevised} für den
 * Core-DTO {@link JahrgangsDaten}.
 */
public final class DataJahrgangsdaten extends DataManagerRevised<Long, DTOJahrgang, JahrgangsDaten> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link JahrgangsDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataJahrgangsdaten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("ID");
		setAttributesRequiredOnCreation("kuerzel", "kuerzelStatistik", "bezeichnung");
	}

	@Override
	public JahrgangsDaten map(final DTOJahrgang dtoJahrgang) {
		final JahrgangsDaten jahrgangsDaten = new JahrgangsDaten();
		jahrgangsDaten.id = dtoJahrgang.ID;
		jahrgangsDaten.kuerzel = dtoJahrgang.InternKrz;
		jahrgangsDaten.kuerzelStatistik = dtoJahrgang.ASDJahrgang;
		jahrgangsDaten.bezeichnung = (jahrgangsDaten.bezeichnung == null) ? "" : dtoJahrgang.ASDBezeichnung;
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
		final List<DTOJahrgang> dtoJahrgangs = conn.queryAll(DTOJahrgang.class);
		return dtoJahrgangs.stream().map(this::map).toList();
	}

	@Override
	public JahrgangsDaten getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Keine ID für den Jahrgang übergeben.");
		final DTOJahrgang jahrgang = conn.queryByKey(DTOJahrgang.class, id);
		if (jahrgang == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Jahrgang zur ID " + id + " gefunden.");
		return map(jahrgang);
	}

	@Override
	protected void mapAttribute(final DTOJahrgang dtoJahrgang, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, "id");
				if (id != dtoJahrgang.ID)
					throw new ApiOperationException(Status.BAD_REQUEST, "Id %d der PatchMap ist ungleich der id %d vom Dto".formatted(id, dtoJahrgang.ID));
			}
			case "kuerzel" -> dtoJahrgang.InternKrz = JSONMapper.convertToString(value, false, false, 20, "kuerzel");
			case "kuerzelStatistik" -> {
				final String kuerzelStatistikJahrgang = JSONMapper.convertToString(value, true, false, 2, "kuerzelStatistik");
				final Jahrgaenge jahrgang = (kuerzelStatistikJahrgang == null) ? null : Jahrgaenge.data().getWertBySchluessel(kuerzelStatistikJahrgang);
				if ((jahrgang == null) && (kuerzelStatistikJahrgang != null))
					throw new ApiOperationException(Status.NOT_FOUND, "Kein Jahrgang mit zum Küerzel %s gefunden.".formatted(kuerzelStatistikJahrgang));

				if (jahrgang == null) {
					dtoJahrgang.ASDJahrgang = null;
					dtoJahrgang.ASDBezeichnung = null;
					return;
				}

				final int schuljahr = conn.getUser().schuleGetSchuljahr();
				final Schulform schulform = conn.getUser().schuleGetSchulform();
				dtoJahrgang.ASDJahrgang = jahrgang.daten(schuljahr).kuerzel;
				final JahrgaengeKatalogEintrag jahrgaengeKatalogEintrag = jahrgang.getBySchulform(schuljahr, schulform);
				dtoJahrgang.ASDBezeichnung = (jahrgaengeKatalogEintrag != null) ? jahrgaengeKatalogEintrag.text : null;
			}
			case "bezeichnung" -> dtoJahrgang.ASDBezeichnung = JSONMapper.convertToString(value, true, true, 100, "bezeichnung");
			case "sortierung" -> dtoJahrgang.Sortierung = JSONMapper.convertToInteger(value, true, "sortierung");
			case "kuerzelSchulgliederung" -> {
				final String kuerzelSchuldgliederung = JSONMapper.convertToString(value, true, false, null, "kuerzelSchulgliederung");
				final Schulgliederung schulgliederung = Schulgliederung.data().getWertByKuerzel(kuerzelSchuldgliederung);
				if ((schulgliederung == null) && (kuerzelSchuldgliederung != null))
					throw new ApiOperationException(Status.CONFLICT, "Das Kürzel für die Schulgliederung ist ungültig.");

				if ((schulgliederung == null) || !schulgliederung.hatSchulform(conn.getUser().schuleGetSchuljahr(), conn.getUser().schuleGetSchulform()))
					throw new ApiOperationException(Status.CONFLICT, "Die Schulgliederung ist für die Schulform nicht gültig.");
				dtoJahrgang.GliederungKuerzel = kuerzelSchuldgliederung;
			}
			case "idFolgejahrgang" -> {
				final Long idFolgejahrgang = JSONMapper.convertToLong(value, true, "idFolgejahrgang");
				if (idFolgejahrgang != null) {
					conn.transactionFlush();
					final DTOJahrgang folgeJahrgang = conn.queryByKey(DTOJahrgang.class, idFolgejahrgang);
					if (folgeJahrgang == null)
						throw new ApiOperationException(Status.CONFLICT, "Ein Folgejahrgang mit der ID %d wurde nicht gefunden.".formatted(idFolgejahrgang));
					conn.transactionFlush();
				}
				dtoJahrgang.Folgejahrgang_ID = idFolgejahrgang;
			}
			case "anzahlRestabschnitte" -> dtoJahrgang.AnzahlRestabschnitte = JSONMapper.convertToInteger(value, true, "anzahlRestabschnitte");
			case "istSichtbar" -> dtoJahrgang.Sichtbar = JSONMapper.convertToBoolean(value, true, "istSichtbar");
			case "gueltigVon" -> dtoJahrgang.GueltigVon = JSONMapper.convertToLong(value, true, "gueltigVon");
			case "gueltigBis" -> dtoJahrgang.GueltigBis = JSONMapper.convertToLong(value, true, "gueltigBis");
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
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
			return new HashMap<>();
		final List<Long> idsJahrgaenge = klassen.stream().filter(kl -> (kl.Jahrgang_ID != null)).map(kl -> kl.Jahrgang_ID).toList();
		final Map<Long, DTOJahrgang> mapJahrgaenge = idsJahrgaenge.isEmpty() ? new HashMap<>() : conn.queryByKeyList(DTOJahrgang.class, idsJahrgaenge)
				.stream().collect(Collectors.toMap(j -> j.ID, j -> j));
		final Map<Long, DTOJahrgang> mapJahrgaengeByKlassenId = new HashMap<>();
		for (final DTOKlassen klasse : klassen) {
			if (klasse.Jahrgang_ID == null)
				continue;
			final DTOJahrgang jg = mapJahrgaenge.get(klasse.Jahrgang_ID);
			if (jg != null)
				mapJahrgaengeByKlassenId.put(klasse.ID, jg);
		}
		return mapJahrgaengeByKlassenId;
	}

}
