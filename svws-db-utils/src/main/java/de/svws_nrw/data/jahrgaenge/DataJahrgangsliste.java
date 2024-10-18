package de.svws_nrw.data.jahrgaenge;

import java.io.InputStream;
import java.util.List;

import de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link JahrgangsDaten}.
 */
public final class DataJahrgangsliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link JahrgangsDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataJahrgangsliste(final DBEntityManager conn) {
		super(conn);
	}


	private static JahrgangsDaten map(final DBEntityManager conn, final DTOJahrgang j) {
		final JahrgangsDaten eintrag = new JahrgangsDaten();
		eintrag.id = j.ID;
		eintrag.kuerzel = j.InternKrz;
		eintrag.kuerzelStatistik = j.ASDJahrgang;
		eintrag.bezeichnung = j.ASDBezeichnung;
		eintrag.kuerzelSchulgliederung = j.GliederungKuerzel;
		if (eintrag.kuerzelSchulgliederung == null) {
			// TODO Hier muss das Schuljahres-spezifische Auslesen des Default-Wertes noch angepasst werden
			final Schulgliederung sgl = Schulgliederung.getDefault(conn.getUser().schuleGetSchulform());
			final SchulgliederungKatalogEintrag sglke = (sgl == null) ? null : sgl.daten(conn.getUser().schuleGetSchuljahr());
			eintrag.kuerzelSchulgliederung = (sglke == null) ? null : sglke.schluessel;
		}
		eintrag.idFolgejahrgang = j.Folgejahrgang_ID;
		eintrag.anzahlRestabschnitte = j.AnzahlRestabschnitte;
		eintrag.sortierung = j.Sortierung;
		eintrag.istSichtbar = j.Sichtbar;
		eintrag.gueltigVon = j.GueltigVon;
		eintrag.gueltigBis = j.GueltigBis;
		return eintrag;
	}


	/**
	 * Bestimmt eine Liste mit allen Jahrgangsdaten
	 *
	 * @param conn                     die Datenbankverbindung
	 *
	 * @return die Liste mit den Daten der Jahrgänge
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<JahrgangsDaten> getJahrgangsliste(final DBEntityManager conn) throws ApiOperationException {
		final List<DTOJahrgang> jahrgaenge = conn.queryAll(DTOJahrgang.class);
		if (jahrgaenge == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Jahrgänge gefunden");
		return jahrgaenge.stream().map(dto -> map(conn, dto)).sorted((a, b) -> Long.compare(a.sortierung, b.sortierung)).toList();
	}


	@Override
	public Response getAll() throws ApiOperationException {
		final List<JahrgangsDaten> daten = getJahrgangsliste(conn);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() throws ApiOperationException {
		return this.getAll();
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
