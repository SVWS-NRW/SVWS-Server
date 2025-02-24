package de.svws_nrw.data.schueler;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerAbgaenge;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerMerkmale;
import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für das
 * Core-DTO {@link SchuelerSchulbesuchsdaten}.
 */
public final class DataSchuelerSchulbesuchsdaten extends DataManagerRevised<Long, DTOSchueler, SchuelerSchulbesuchsdaten> {

	/** Ein Cache für den schnellen Zugriff auf den Katalog der Entlassarten. */
	private final Map<String, DTOEntlassarten> mapEntlassarten;

	/** Ein Cache für den schnellen Zugriff auf den Katalog der Merkmale */
	private final Map<String, DTOMerkmale> merkmale;


	/**
	 * Erstellt einen neuen {@link DataManager} für das Core-DTO {@link SchuelerSchulbesuchsdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerSchulbesuchsdaten(final DBEntityManager conn) {
		super(conn);
		mapEntlassarten = conn.queryAll(DTOEntlassarten.class).stream().collect(Collectors.toMap(e -> e.Bezeichnung, e -> e));
		merkmale = conn.queryAll(DTOMerkmale.class).stream().collect(Collectors.toMap(m -> m.Kurztext, m -> m));
	}


	@Override
	protected long getLongId(final DTOSchueler dto) {
		return dto.ID;
	}

	@Override
	public SchuelerSchulbesuchsdaten getById(final Long idSchueler) throws ApiOperationException {
		if (idSchueler == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den Schüler darf nicht null sein.");
		final DTOSchueler dto = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der Id %d gefunden".formatted(idSchueler));
		return map(dto);
	}

	@Override
	protected SchuelerSchulbesuchsdaten map(final DTOSchueler dtoSchueler) throws ApiOperationException {
		final List<DTOSchuelerMerkmale> schuelerMerkmale = conn.queryList(DTOSchuelerMerkmale.QUERY_BY_SCHUELER_ID, DTOSchuelerMerkmale.class, getLongId(dtoSchueler));
		final List<DTOSchuelerAbgaenge> schuelerAbgaenge = conn.queryList(DTOSchuelerAbgaenge.QUERY_BY_SCHUELER_ID, DTOSchuelerAbgaenge.class, getLongId(dtoSchueler));
		return mapInternal(dtoSchueler, schuelerMerkmale, schuelerAbgaenge);
	}

	private SchuelerSchulbesuchsdaten mapInternal(final DTOSchueler dtoSchueler, final List<DTOSchuelerMerkmale> schuelerMerkmale,
			final List<DTOSchuelerAbgaenge> schuelerAbgaenge) {

		final SchuelerSchulbesuchsdaten daten = new SchuelerSchulbesuchsdaten();
		// Basisdaten
		daten.id = getLongId(dtoSchueler);
		// Informationen zu der Schule, die vor der Aufnahme besucht wurde
		daten.vorigeSchulnummer = dtoSchueler.LSSchulNr;
		daten.vorigeAllgHerkunft = dtoSchueler.LSSchulform;
		daten.vorigeEntlassdatum = dtoSchueler.LSSchulEntlassDatum;
		daten.vorigeEntlassjahrgang = dtoSchueler.LSJahrgang;
		daten.vorigeArtLetzteVersetzung = dtoSchueler.LSVersetzung;
		daten.vorigeBemerkung = dtoSchueler.LSBemerkung;
		final DTOEntlassarten vorigeEntlassgrund = (dtoSchueler.LSEntlassgrund == null) ? null : this.mapEntlassarten.get(dtoSchueler.LSEntlassgrund);
		daten.vorigeEntlassgrundID = (vorigeEntlassgrund == null) ? null : vorigeEntlassgrund.ID;
		daten.vorigeAbschlussartID = dtoSchueler.LSEntlassArt;
		// Informationen zu der Entlassung von der eigenen Schule
		daten.entlassungDatum = dtoSchueler.Entlassdatum;
		daten.entlassungJahrgang = dtoSchueler.Entlassjahrgang;
		final DTOEntlassarten entlassgrund = (dtoSchueler.Entlassgrund == null) ? null : this.mapEntlassarten.get(dtoSchueler.Entlassgrund);
		daten.entlassungGrundID = (entlassgrund == null) ? null : entlassgrund.ID;
		daten.entlassungAbschlussartID = dtoSchueler.Entlassart;
		// Informationen zu der aufnehmenden Schule nach einem Wechsel zu einer anderen Schule
		daten.aufnehmdendSchulnummer = dtoSchueler.SchulwechselNr;
		daten.aufnehmdendWechseldatum = dtoSchueler.Schulwechseldatum;
		daten.aufnehmdendBestaetigt = dtoSchueler.WechselBestaetigt;
		// Informationen zu der besuchten Grundschule
		daten.grundschuleEinschulungsjahr = dtoSchueler.Einschulungsjahr;
		daten.grundschuleEinschulungsartID = dtoSchueler.Einschulungsart_ID;
		daten.grundschuleJahreEingangsphase = dtoSchueler.EPJahre;
		// TODO statkue_schueleruebergangsempfehlung5jg -> daten.grundschuleUebergangsempfehlungID = schueler.Uebergangsempfehlung_JG5;
		// Informationen zu dem Besuch der Sekundarstufe I
		daten.sekIWechsel = dtoSchueler.JahrWechsel_SI;
		daten.sekIErsteSchulform = dtoSchueler.ErsteSchulform_SI;
		daten.sekIIWechsel = dtoSchueler.JahrWechsel_SII;

		// Informationen zu besonderen Merkmalen für die Statistik
		daten.merkmale = DataSchuelerMerkmale.mapMultiple(schuelerMerkmale, merkmale);

		// Informationen zu allen bisher besuchten Schulen
		daten.alleSchulen = DataSchuelerSchulbesuchSchule.mapMultiple(schuelerAbgaenge, mapEntlassarten);

		return daten;
	}


	@Override
	protected void mapAttribute(final DTOSchueler dtoSchueler, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long idPatch = JSONMapper.convertToLong(value, true, "idPatch");
				if (!Objects.equals(idPatch, getLongId(dtoSchueler)))
					throw new ApiOperationException(Status.BAD_REQUEST, "IdPatch %d ist ungleich idSchueler %d.".formatted(idPatch, getLongId(dtoSchueler)));
			}
			// Informationen zu der Schule, die vor der Aufnahme besucht wurde
			case "vorigeSchulnummer" -> dtoSchueler.LSSchulNr =
					JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_LSSchulNr.datenlaenge(), "vorigeSchulnummer");
			case "vorigeAllgHerkunft" -> { /* TODO zur Zeit noch nicht implementiert */ }
			case "vorigeEntlassdatum" -> dtoSchueler.LSSchulEntlassDatum = JSONMapper.convertToString(value, true, true, null, "vorigeEntlassdatum");
			case "vorigeEntlassjahrgang" -> // TODO Katalog ...
					dtoSchueler.LSJahrgang = JSONMapper.convertToString(value, true, true, null, "vorigeEntlassjahrgang");
			case "vorigeArtLetzteVersetzung" -> // TODO Katalog
					dtoSchueler.LSVersetzung = JSONMapper.convertToString(value, true, true, null, "vorigeArtLetzteVersetzung");
			case "vorigeBemerkung" -> dtoSchueler.LSBemerkung =
					JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_LSBemerkung.datenlaenge(), "vorigeBemerkung");
			case "vorigeEntlassgrundID" -> mapEntlassgrundID(value, "vorigeEntlassgrundID", v -> dtoSchueler.LSEntlassgrund = v);
			case "vorigeAbschlussartID" -> // TODO Katalog ...
					dtoSchueler.LSEntlassArt = JSONMapper.convertToString(value, true, true, null, "vorigeAbschlussartID");

			// Informationen zu der Entlassung von der eigenen Schule
			case "entlassungDatum" -> dtoSchueler.Entlassdatum = JSONMapper.convertToString(value, true, true, null, "entlassungDatum");
			case "entlassungJahrgang" -> // TODO Katalog ...
					dtoSchueler.Entlassjahrgang = JSONMapper.convertToString(value, true, true, null, "entlassungJahrgang");
			case "entlassungGrundID" -> mapEntlassgrundID(value, "entlassungGrundID", v -> dtoSchueler.Entlassgrund = v);
			case "entlassungAbschlussartID" -> // TODO Katalog ...
					dtoSchueler.Entlassart = JSONMapper.convertToString(value, true, true, null, "entlassungAbschlussartID");

			// Informationen zu der aufnehmenden Schule nach einem Wechsel zu einer anderen Schule
			case "aufnehmendSchulnummer" -> dtoSchueler.SchulwechselNr =
					JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_SchulwechselNr.datenlaenge(), "aufnehmendSchulnummer");
			case "aufnehmendWechseldatum" -> dtoSchueler.Schulwechseldatum = JSONMapper.convertToString(value, true, true, null, "aufnehmendWechseldatum");
			case "aufnehmendBestaetigt" -> dtoSchueler.WechselBestaetigt = JSONMapper.convertToBoolean(value, true, "aufnehmendBestaetigt");

			// Informationen zu der besuchten Grundschule
			case "grundschuleEinschulungsjahr" -> // TODO Überprüfung des Jahres
					dtoSchueler.Einschulungsjahr = JSONMapper.convertToInteger(value, true, "grundschuleEinschulungsjahr");
			case "grundschuleEinschulungsartID" -> // TODO Katalog ...
					dtoSchueler.Einschulungsart_ID = JSONMapper.convertToLong(value, true, "grundschuleEinschulungsartID");
			case "grundschuleJahreEingangsphase" -> // TODO Auswahl auf 2 und 3 beschränken?
					dtoSchueler.EPJahre = JSONMapper.convertToInteger(value, true, "grundschuleJahreEingangsphase");
			case "grundschuleUebergangsempfehlungID" -> dtoSchueler.Uebergangsempfehlung_JG5 = // TODO Katalog statkue_schueleruebergangsempfehlung5jg
					JSONMapper.convertToString(value, true, false, null, "grundschuleUebergangsempfehlungID");

			// Informationen zu dem Besuch der Sekundarstufe I
			case "sekIWechsel" -> dtoSchueler.JahrWechsel_SI = JSONMapper.convertToInteger(value, true, "sekIWechsel"); // TODO Überprüfung des Jahres
			case "sekIErsteSchulform" -> // TODO Katalog ...
					dtoSchueler.ErsteSchulform_SI = JSONMapper.convertToString(value, true, false, null, "sekIErsteSchulform");
			case "sekIIWechsel" -> dtoSchueler.JahrWechsel_SII = JSONMapper.convertToInteger(value, true, "sekIIWechsel"); // TODO Überprüfung des Jahres

			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}


	private void mapEntlassgrundID(final Object value, final String key, final Consumer<String> setter) throws ApiOperationException {
		final String entlassungGrundID = JSONMapper.convertToString(value, true, true, null, key);
		if (entlassungGrundID == null) {
			setter.accept(null);
			return;
		}
		final DTOEntlassarten dto = this.mapEntlassarten.get(entlassungGrundID);
		if (dto == null)
			throw new ApiOperationException(Status.CONFLICT, "keine Entlassart mit der %s %s gefunden.".formatted(key, entlassungGrundID));

		setter.accept(dto.Bezeichnung);
	}

}
