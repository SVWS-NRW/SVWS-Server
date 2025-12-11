package de.svws_nrw.data.kataloge;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link OrtKatalogEintrag}.
 */
public final class DataOrte extends DataManagerRevised<Long, DTOOrt, OrtKatalogEintrag> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link OrtKatalogEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataOrte(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	protected OrtKatalogEintrag map(final DTOOrt dto) {
		final OrtKatalogEintrag daten = new OrtKatalogEintrag();
		daten.id = dto.ID;
		daten.plz = dto.PLZ;
		daten.ortsname = dto.Bezeichnung;
		daten.kreis = dto.Kreis;
		daten.kuerzelBundesland = dto.Land;
		daten.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		daten.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		daten.istAenderbar = Boolean.TRUE.equals(dto.Aenderbar);
		return daten;
	}

	@Override
	public List<OrtKatalogEintrag> getAll() {
		final List<DTOOrt> orte = conn.queryAll(DTOOrt.class);

		return orte
				.stream()
				.map(this::map)
				.sorted(Comparator.comparing(v -> v.id))
				.toList();
	}

}
