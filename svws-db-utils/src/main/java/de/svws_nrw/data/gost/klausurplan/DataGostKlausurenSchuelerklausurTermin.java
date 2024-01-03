package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.function.Function;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenTermine;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurraumstunde}.
 */
public final class DataGostKlausurenSchuelerklausurTermin extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurraumstunde}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenSchuelerklausurTermin(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenRaumstunden} in einen Core-DTO
	 * {@link GostKlausurraumstunde}.
	 */
	public static final Function<DTOGostKlausurenSchuelerklausurenTermine, GostSchuelerklausurTermin> dtoMapper = (final DTOGostKlausurenSchuelerklausurenTermine t) -> {
		final GostSchuelerklausurTermin daten = new GostSchuelerklausurTermin();
		daten.id = t.ID;
		daten.idSchuelerklausur = t.Schuelerklausur_ID;
		daten.idTermin = t.Termin_ID;
		daten.startzeit = t.Startzeit;
		daten.bemerkungSchuelerklausurtermin = t.Bemerkungen;
		return daten;
	};

	@Override
	public Response get(final Long idTermin) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}


}
