package de.svws_nrw.data.schueler;

import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.types.schueler.Herkunftsarten;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
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
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das
 * Core-DTO {@link SchuelerSchulbesuchsdaten}.
 */
public final class DataSchuelerSchulbesuchsdaten extends DataManagerRevised<Long, DTOSchueler, SchuelerSchulbesuchsdaten> {

	/** Ein Cache für den schnellen Zugriff auf den Katalog der Entlassarten. */
	private final Map<String, DTOEntlassarten> mapEntlassarten;

	/** Ein Cache für den schnellen Zugriff auf den Katalog der Merkmale */
	private final Map<String, DTOMerkmale> merkmale;

	/** Ein Cache für den schnellen Zugriff auf den Katalog der Schulen */
	private final Map<String, DTOSchuleNRW> schulen;


	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link SchuelerSchulbesuchsdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerSchulbesuchsdaten(final DBEntityManager conn) {
		super(conn);
		this.mapEntlassarten = conn.queryAll(DTOEntlassarten.class).stream().collect(Collectors.toMap(e -> e.Bezeichnung, e -> e));
		this.merkmale = conn.queryAll(DTOMerkmale.class).stream().collect(Collectors.toMap(m -> m.Kurztext, m -> m));
		this.schulen = conn.queryAll(DTOSchuleNRW.class).stream().collect(Collectors.toMap(s -> s.SchulNr, s -> s));
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
		final List<DTOSchuelerMerkmale> schuelerMerkmale = conn.queryList(DTOSchuelerMerkmale.QUERY_BY_SCHUELER_ID, DTOSchuelerMerkmale.class, dtoSchueler.ID);
		final List<DTOSchuelerAbgaenge> schuelerAbgaenge = conn.queryList(DTOSchuelerAbgaenge.QUERY_BY_SCHUELER_ID, DTOSchuelerAbgaenge.class, dtoSchueler.ID);
		return mapInternal(dtoSchueler, schuelerMerkmale, schuelerAbgaenge);
	}

	private SchuelerSchulbesuchsdaten mapInternal(final DTOSchueler dtoSchueler, final @NotNull List<DTOSchuelerMerkmale> schuelerMerkmale,
			final @NotNull List<DTOSchuelerAbgaenge> schuelerAbgaenge) {

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
		daten.aufnehmendSchulnummer = dtoSchueler.SchulwechselNr;
		daten.aufnehmendWechseldatum = dtoSchueler.Schulwechseldatum;
		daten.aufnehmendBestaetigt = dtoSchueler.WechselBestaetigt;
		// Informationen zu der besuchten Grundschule
		daten.grundschuleEinschulungsjahr = dtoSchueler.Einschulungsjahr;
		daten.grundschuleEinschulungsartID = dtoSchueler.Einschulungsart_ID;
		daten.grundschuleJahreEingangsphase = dtoSchueler.EPJahre;
		daten.kuerzelGrundschuleUebergangsempfehlung = dtoSchueler.Uebergangsempfehlung_JG5;
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
			case "vorigeSchulnummer" -> mapSchulnummer(value, "vorigeSchulnummer", v -> dtoSchueler.LSSchulNr = v);
			case "vorigeAllgHerkunft" -> { /* TODO zur Zeit noch nicht implementiert */ }
			case "vorigeEntlassdatum" -> dtoSchueler.LSSchulEntlassDatum = JSONMapper.convertToString(value, true, true, null, "vorigeEntlassdatum");
			case "vorigeEntlassjahrgang" -> mapJahrgang(value, "vorigeEntlassjahrgang", v -> dtoSchueler.LSJahrgang = v);
			case "vorigeArtLetzteVersetzung" -> mapVorigeArtLetzteVersetzung(dtoSchueler, value);
			case "vorigeBemerkung" -> dtoSchueler.LSBemerkung =
					JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_LSBemerkung.datenlaenge(), "vorigeBemerkung");
			case "vorigeEntlassgrundID" -> mapEntlassgrundID(value, "vorigeEntlassgrundID", v -> dtoSchueler.LSEntlassgrund = v);
			case "vorigeAbschlussartID" -> // TODO Katalog ...
					dtoSchueler.LSEntlassArt = JSONMapper.convertToString(value, true, true, null, "vorigeAbschlussartID");

			// Informationen zu der Entlassung von der eigenen Schule
			case "entlassungDatum" -> dtoSchueler.Entlassdatum = JSONMapper.convertToString(value, true, true, null, "entlassungDatum");
			case "entlassungJahrgang" -> mapJahrgang(value, "entlassungJahrgang", v -> dtoSchueler.Entlassjahrgang = v);
			case "entlassungGrundID" -> mapEntlassgrundID(value, "entlassungGrundID", v -> dtoSchueler.Entlassgrund = v);
			case "entlassungAbschlussartID" -> // TODO Katalog ...
					dtoSchueler.Entlassart = JSONMapper.convertToString(value, true, true, null, "entlassungAbschlussartID");

			// Informationen zu der aufnehmenden Schule nach einem Wechsel zu einer anderen Schule
			case "aufnehmendSchulnummer" -> mapSchulnummer(value, "aufnehmendSchulnummer", v -> dtoSchueler.SchulwechselNr = v);
			case "aufnehmendWechseldatum" -> dtoSchueler.Schulwechseldatum = JSONMapper.convertToString(value, true, true, null, "aufnehmendWechseldatum");
			case "aufnehmendBestaetigt" -> dtoSchueler.WechselBestaetigt = JSONMapper.convertToBoolean(value, true, "aufnehmendBestaetigt");

			// Informationen zu der besuchten Grundschule
			case "grundschuleEinschulungsjahr" -> mapJahr(value, "grundschuleEinschulungsjahr", v -> dtoSchueler.Einschulungsjahr = v);
			case "grundschuleEinschulungsartID" -> // TODO Katalog ...
					dtoSchueler.Einschulungsart_ID = JSONMapper.convertToLong(value, true, "grundschuleEinschulungsartID");
			case "grundschuleJahreEingangsphase" -> mapEingangsphase(dtoSchueler, value);
			case "kuerzelGrundschuleUebergangsempfehlung" -> mapUebergangsempfehlung(dtoSchueler, value);

			// Informationen zu dem Besuch der Sekundarstufe I
			case "sekIWechsel" -> mapJahr(value, "sekIWechsel", v -> dtoSchueler.JahrWechsel_SI = v);
			case "sekIErsteSchulform" -> mapSchulform(dtoSchueler, value);
			case "sekIIWechsel" -> mapJahr(value, "sekIIWechsel", v -> dtoSchueler.JahrWechsel_SII = v);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void mapSchulform(final DTOSchueler dtoSchueler, final Object value) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(value, true, true, null, "sekIErsteSchulform");
		if (kuerzel == null) {
			dtoSchueler.ErsteSchulform_SI = null;
			return;
		}
		if (Schulform.data().getWertByKuerzel(kuerzel) == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Für das Kürzel %s wurde keine Schulform gefunden".formatted(kuerzel));

		dtoSchueler.ErsteSchulform_SI = kuerzel;
	}

	private static void mapUebergangsempfehlung(final DTOSchueler dtoSchueler, final Object value) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(
				value, true, true, null, "kuerzelGrundschuleUebergangsempfehlung");
		if (kuerzel == null) {
			dtoSchueler.Uebergangsempfehlung_JG5 = null;
			return;
		}

		if (Uebergangsempfehlung.data().getWertByKuerzel(kuerzel) == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Übergangsempfehlung für das Kürzel %s gefunden.".formatted(kuerzel));

		dtoSchueler.Uebergangsempfehlung_JG5 = kuerzel;
	}

	private static void mapEingangsphase(final DTOSchueler dtoSchueler, final Object value) throws ApiOperationException {
		final Integer ep = JSONMapper.convertToInteger(value, true, "grundschuleJahreEingangsphase");
		if (ep == null) {
			dtoSchueler.EPJahre = null;
			return;
		}
		if (PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWertBySchluessel(String.valueOf(ep)) == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Eingangsphase mit dem Schlüssel %d vorhanden.".formatted(ep));

		dtoSchueler.EPJahre = ep;
	}

	private static void mapJahr(final Object value, final String key, final Consumer<Integer> setter) throws ApiOperationException {
		final Integer jahr = JSONMapper.convertToInteger(value, true, key);
		if (jahr == null) {
			setter.accept(null);
			return;
		}
		if ((jahr < 1900) || (jahr > 2100))
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Jahr %d ist nicht gültig. Es muss zwischen 1900 und 2100 liegen.".formatted(jahr));

		setter.accept(jahr);
	}

	private void mapSchulnummer(final Object value, final String key, final Consumer<String> setter) throws ApiOperationException {
		final String schulnummer = JSONMapper.convertToString(value, true, true, null, key);
		if (schulnummer == null) {
			setter.accept(null);
			return;
		}
		final boolean exists = this.schulen.values().stream().anyMatch(s -> Objects.equals(s.SchulNr, schulnummer));
		if (!exists)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Schule mit Schulnummer %s gefunden.".formatted(schulnummer));

		setter.accept(schulnummer);
	}

	private static void mapJahrgang(final Object value, final String key, final Consumer<String> setter) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(
				value, true, true, null, key);
		if (kuerzel == null) {
			setter.accept(null);
			return;
		}
		if (Jahrgaenge.data().getWertByKuerzel(kuerzel) == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Jahrgang für das Kürzel %s gefunden.".formatted(kuerzel));

		setter.accept(kuerzel);
	}

	private static void mapVorigeArtLetzteVersetzung(final DTOSchueler dtoSchueler, final Object value) throws ApiOperationException {
		final String id = JSONMapper.convertToString(
				value, true, true, null, "vorigeArtLetzteVersetzung");
		if (id == null) {
			dtoSchueler.LSVersetzung = null;
			return;
		}

		final Long idAsLong = JSONMapper.convertToLong(id, true, "id");
		if (Herkunftsarten.getByID(idAsLong) == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Herkunftsart für die ID: %s gefunden.".formatted(id));

		dtoSchueler.LSVersetzung = id;
	}

	private void mapEntlassgrundID(final Object value, final String key, final Consumer<String> setter) throws ApiOperationException {
		final Long entlassungGrundID = JSONMapper.convertToLong(value, true, key);
		if (entlassungGrundID == null) {
			setter.accept(null);
			return;
		}
		final String bezeichnung = this.mapEntlassarten.values().stream().filter(e -> Objects.equals(e.ID, entlassungGrundID)).findFirst()
				.map(e -> e.Bezeichnung)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Keine Entlassart mit der ID %s gefunden.".formatted(entlassungGrundID)));

		setter.accept(bezeichnung);
	}
}
