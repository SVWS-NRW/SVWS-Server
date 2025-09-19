package de.svws_nrw.data.schueler;

import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link FoerderschwerpunktEintrag}.
 */
public final class DataKatalogSchuelerFoerderschwerpunkte extends DataManagerRevised<Long, DTOFoerderschwerpunkt, FoerderschwerpunktEintrag> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link FoerderschwerpunktEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogSchuelerFoerderschwerpunkte(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("kuerzelStatistik");
		setAttributesNotPatchable("id", "text", "kuerzelStatistik");
	}

	@Override
	protected void initDTO(final DTOFoerderschwerpunkt dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
		dto.Sichtbar = true;
		dto.Sortierung = 32000;
	}

	@Override
	protected long getLongId(final DTOFoerderschwerpunkt dto) {
		return dto.ID;
	}

	@Override
	public FoerderschwerpunktEintrag getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Förderschwerpunktes darf nicht null sein.");

		final DTOFoerderschwerpunkt dto = this.conn.queryByKey(DTOFoerderschwerpunkt.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Förderschwerpunkt mit der ID %d gefunden.".formatted(id));

		return map(dto);
	}

	@Override
	public List<FoerderschwerpunktEintrag> getAll() {
		final List<DTOFoerderschwerpunkt> foerderschwerpunkte = this.conn.queryAll(DTOFoerderschwerpunkt.class);
		return foerderschwerpunkte.stream().map(this::map).toList();
	}

	@Override
	protected FoerderschwerpunktEintrag map(final DTOFoerderschwerpunkt dto) {
		final FoerderschwerpunktEintrag foerderschwerpunkt = new FoerderschwerpunktEintrag();
		foerderschwerpunkt.id = dto.ID;
		foerderschwerpunkt.kuerzel = (dto.Bezeichnung == null) ? "" : dto.Bezeichnung;
		foerderschwerpunkt.kuerzelStatistik = dto.StatistikKrz;
		foerderschwerpunkt.istSichtbar = (dto.Sichtbar == null) || dto.Sichtbar;
		foerderschwerpunkt.sortierung = (dto.Sortierung != null) ? dto.Sortierung : 32000;
		final Foerderschwerpunkt fs = Foerderschwerpunkt.data().getWertBySchluessel(dto.StatistikKrz);
		final int schuljahr = conn.getUser().schuleGetSchuljahr();
		foerderschwerpunkt.text = ((fs == null) || (fs.daten(schuljahr) == null)) ? "" : fs.daten(schuljahr).text;
		return foerderschwerpunkt;

	}

	@Override
	protected void mapAttribute(final DTOFoerderschwerpunkt dto, final String name, final Object value, final Map<String, Object> attributes)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
			}
			case "kuerzel" -> mapBezeichnung(dto, value, name);
			case "kuerzelStatistik" -> mapKuerzelStatistik(dto, value);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			case "text" -> {
				// kein mapping
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void mapBezeichnung(final DTOFoerderschwerpunkt dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, 50, name);
		if (Objects.equals(dto.Bezeichnung, bezeichnung) || bezeichnung.isBlank())
			return;

		final boolean bezeichnungAlreadyUsed = this.conn.queryAll(DTOFoerderschwerpunkt.class).stream()
				.anyMatch(f -> (f.ID != dto.ID) && f.Bezeichnung.equalsIgnoreCase(bezeichnung));
		if (bezeichnungAlreadyUsed)
				throw new ApiOperationException(Status.BAD_REQUEST, "Das Kürzel %s darf nicht doppelt vergeben werden".formatted(bezeichnung));

		dto.Bezeichnung = bezeichnung;
	}

	private static void mapKuerzelStatistik(final DTOFoerderschwerpunkt dto, final Object value) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(value, false, false, 2, "kuerzelStatistik");
		if (Objects.equals(dto.StatistikKrz, kuerzel) || kuerzel.isBlank())
			return;

		if (Foerderschwerpunkt.data().getWertBySchluessel(kuerzel) == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Zum angegebenen Kürzel %s wurde kein passender Förderschwerpunkt gefunden.".formatted(kuerzel));

		dto.StatistikKrz = kuerzel;
	}

}
