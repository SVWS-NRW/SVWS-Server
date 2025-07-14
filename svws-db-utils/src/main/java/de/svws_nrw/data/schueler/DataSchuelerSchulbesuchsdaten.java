package de.svws_nrw.data.schueler;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag;
import de.svws_nrw.asd.data.schule.KindergartenbesuchKatalogEintrag;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.schueler.Einschulungsart;
import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.asd.types.schule.Kindergartenbesuch;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.types.schueler.Herkunftsarten;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOKindergarten;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.EinschulungsartKatalogEintrag;
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
	private final Map<String, DTOEntlassarten> entlassartenByBezeichnung;

	/** Ein Cache für den schnellen Zugriff auf den Katalog der Merkmale */
	private final Map<String, DTOMerkmale> merkmaleByKurztext;

	/** Ein Cache für den schnellen Zugriff auf den Katalog der Schulen */
	private final Map<String, DTOSchuleNRW> schulenBySchulnummer;


	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link SchuelerSchulbesuchsdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerSchulbesuchsdaten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		this.entlassartenByBezeichnung = conn.queryAll(DTOEntlassarten.class).stream().collect(Collectors.toMap(e -> e.Bezeichnung, e -> e));
		this.merkmaleByKurztext = conn.queryAll(DTOMerkmale.class).stream().collect(Collectors.toMap(m -> m.Kurztext, m -> m));
		this.schulenBySchulnummer = conn.queryAll(DTOSchuleNRW.class).stream().collect(Collectors.toMap(s -> s.SchulNr, s -> s));
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

	/**
	 * Ruft eine Liste von SchuelerSchulbesuchsdaten ab, basierend auf den angegebenen Schüler-IDs.
	 *
	 * @param idsSchueler die Liste der IDs der Schüler, für die die Schulbesuchsdaten abgerufen werden sollen.
	 *                    Die Liste darf nicht null oder leer sein und darf keine null-Werte enthalten.
	 * @return eine Liste von SchuelerSchulbesuchsdaten, die den angegebenen IDs entsprechen.
	 * @throws ApiOperationException wenn die Eingabe ungültig ist oder keine Daten für die angegebenen IDs gefunden werden konnten.
	 */
	public List<SchuelerSchulbesuchsdaten> getListByIds(final List<Long> idsSchueler) throws ApiOperationException {
		if (idsSchueler == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Liste der IDs der Schüler darf nicht null sein.");
		final List<Long> ids = idsSchueler.stream().distinct().filter(Objects::nonNull).toList();
		if (ids.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Liste der IDs der Schüler darf nicht leer sein.");
		final List<DTOSchueler> dtos = conn.queryByKeyList(DTOSchueler.class, idsSchueler);
		if ((dtos == null) || dtos.isEmpty() || (dtos.size() != ids.size()))
			throw new ApiOperationException(Status.NOT_FOUND, "Es konnten nicht zu allen übergebenen IDs Schüler ermittelt werden.");
		final List<SchuelerSchulbesuchsdaten> schulbesuchsdaten = new ArrayList<>(dtos.size());
		for (final DTOSchueler dto : dtos) {
			schulbesuchsdaten.add(map(dto));
		}
		return schulbesuchsdaten;
	}

	@Override
	protected SchuelerSchulbesuchsdaten map(final DTOSchueler dtoSchueler) throws ApiOperationException {
		final List<DTOSchuelerMerkmale> schuelerMerkmale = conn.queryList(DTOSchuelerMerkmale.QUERY_BY_SCHUELER_ID, DTOSchuelerMerkmale.class, dtoSchueler.ID);
		final List<DTOSchuelerAbgaenge> schuelerAbgaenge = conn.queryList(DTOSchuelerAbgaenge.QUERY_BY_SCHUELER_ID, DTOSchuelerAbgaenge.class, dtoSchueler.ID);
		return mapInternal(dtoSchueler, schuelerMerkmale, schuelerAbgaenge);
	}

	private SchuelerSchulbesuchsdaten mapInternal(final DTOSchueler dtoSchueler, final @NotNull List<DTOSchuelerMerkmale> schuelerMerkmale,
			final @NotNull List<DTOSchuelerAbgaenge> schuelerAbgaenge) throws ApiOperationException {

		final SchuelerSchulbesuchsdaten daten = new SchuelerSchulbesuchsdaten();
		// Basisdaten
		daten.id = getLongId(dtoSchueler);
		// Informationen zu der Schule, die vor der Aufnahme besucht wurde
		final DTOSchuleNRW vorherigeSchule = this.schulenBySchulnummer.get(dtoSchueler.LSSchulNr);
		daten.idVorherigeSchule = (vorherigeSchule != null) ? vorherigeSchule.ID : null;
		daten.vorigeAllgHerkunft = dtoSchueler.LSSchulform;
		daten.vorigeEntlassdatum = dtoSchueler.LSSchulEntlassDatum;
		daten.vorigeEntlassjahrgang = dtoSchueler.LSJahrgang;
		daten.vorigeArtLetzteVersetzung = dtoSchueler.LSVersetzung;
		daten.vorigeBemerkung = dtoSchueler.LSBemerkung;
		final DTOEntlassarten vorigeEntlassgrund = (dtoSchueler.LSEntlassgrund == null) ? null : this.entlassartenByBezeichnung.get(dtoSchueler.LSEntlassgrund);
		daten.vorigeEntlassgrundID = (vorigeEntlassgrund == null) ? null : vorigeEntlassgrund.ID;
		daten.vorigeAbschlussartID = dtoSchueler.LSEntlassArt;
		// Informationen zu der Entlassung von der eigenen Schule
		daten.entlassungDatum = dtoSchueler.Entlassdatum;
		daten.entlassungJahrgang = dtoSchueler.Entlassjahrgang;
		final DTOEntlassarten entlassgrund = (dtoSchueler.Entlassgrund == null) ? null : this.entlassartenByBezeichnung.get(dtoSchueler.Entlassgrund);
		daten.entlassungGrundID = (entlassgrund == null) ? null : entlassgrund.ID;
		daten.entlassungAbschlussartID = dtoSchueler.Entlassart;
		// Informationen zu der aufnehmenden Schule nach einem Wechsel zu einer anderen Schule
		final DTOSchuleNRW aufnehmendeSchule = this.schulenBySchulnummer.get(dtoSchueler.SchulwechselNr);
		daten.idAufnehmendeSchule = (aufnehmendeSchule != null) ? aufnehmendeSchule.ID : null;
		daten.aufnehmendWechseldatum = dtoSchueler.Schulwechseldatum;
		daten.aufnehmendBestaetigt = dtoSchueler.WechselBestaetigt;
		// Informationen zu der besuchten Grundschule
		daten.grundschuleEinschulungsjahr = dtoSchueler.Einschulungsjahr;
		final Einschulungsart einschulungsart = Einschulungsart.data().getWertBySchluessel(dtoSchueler.EinschulungsartASD);
		daten.grundschuleEinschulungsartID = (einschulungsart == null) ? null : einschulungsart.getLetzterEintrag().id;
		daten.idGrundschuleJahreEingangsphase = mapGrundschuleJahreEingangsphase(dtoSchueler);
		daten.kuerzelGrundschuleUebergangsempfehlung = dtoSchueler.Uebergangsempfehlung_JG5;
		// Informationen zu dem Besuch der Sekundarstufe I
		daten.sekIWechsel = dtoSchueler.JahrWechsel_SI;
		daten.sekIErsteSchulform = dtoSchueler.ErsteSchulform_SI;
		daten.sekIIWechsel = dtoSchueler.JahrWechsel_SII;
		daten.idDauerKindergartenbesuch = mapSchluesselDauerKindergartenbesuch(dtoSchueler);
		daten.idKindergarten = dtoSchueler.Kindergarten_ID;
		daten.verpflichtungSprachfoerderkurs = (dtoSchueler.VerpflichtungSprachfoerderkurs != null) && dtoSchueler.VerpflichtungSprachfoerderkurs;
		daten.teilnahmeSprachfoerderkurs = (dtoSchueler.TeilnahmeSprachfoerderkurs != null) && dtoSchueler.TeilnahmeSprachfoerderkurs;

		// Informationen zu besonderen Merkmalen für die Statistik
		daten.merkmale = DataSchuelerMerkmale.mapMultiple(schuelerMerkmale, merkmaleByKurztext);

		// Informationen zu allen bisher besuchten Schulen
		daten.alleSchulen = DataSchuelerSchulbesuchSchule.mapMultiple(schuelerAbgaenge, entlassartenByBezeichnung, schulenBySchulnummer);

		return daten;
	}

	private static Long mapSchluesselDauerKindergartenbesuch(final DTOSchueler dtoSchueler) throws ApiOperationException {
		if (dtoSchueler.DauerKindergartenbesuch == null)
			return null;

		final Kindergartenbesuch kindergartenbesuch = Kindergartenbesuch.data().getWertBySchluessel(dtoSchueler.DauerKindergartenbesuch);
		if (kindergartenbesuch == null)
			throw new ApiOperationException(
					Status.NOT_FOUND, "Kein Kindergartenbesuch mit dem Schlüssel %s gefunden.".formatted(dtoSchueler.DauerKindergartenbesuch));

		try {
			return kindergartenbesuch.historie().getLast().id;
		} catch (final CoreTypeException | NoSuchElementException e) {
			throw new ApiOperationException(
					Status.NOT_FOUND, "Kein Historien-Eintrag für den Bezeichner %s gefunden".formatted(kindergartenbesuch.name()));
		}
	}

	private static Long mapGrundschuleJahreEingangsphase(final DTOSchueler dtoSchueler) {
		if ((dtoSchueler.EPJahre == null) || (dtoSchueler.Einschulungsjahr == null))
			return null;
		final PrimarstufeSchuleingangsphaseBesuchsjahre wert =
				PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWertBySchluessel(String.valueOf(dtoSchueler.EPJahre));
		return wert.daten(dtoSchueler.Einschulungsjahr).id;
	}


	@Override
	protected void mapAttribute(final DTOSchueler dtoSchueler, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long idPatch = JSONMapper.convertToLong(value, true, "idPatch");
				if (!Objects.equals(idPatch, getLongId(dtoSchueler)))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(idPatch, getLongId(dtoSchueler)));
			}
			// Informationen zu der Schule, die vor der Aufnahme besucht wurde
			case "idVorherigeSchule" -> mapSchulnummer(value, "idVorherigeSchule", v -> dtoSchueler.LSSchulNr = v);
			case "vorigeAllgHerkunft" -> { /* Feld ist historisch überflüssig */ }
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
			case "idAufnehmendeSchule" -> mapSchulnummer(value, "idAufnehmendeSchule", v -> dtoSchueler.SchulwechselNr = v);
			case "aufnehmendWechseldatum" -> dtoSchueler.Schulwechseldatum = JSONMapper.convertToString(value, true, true, null, "aufnehmendWechseldatum");
			case "aufnehmendBestaetigt" -> dtoSchueler.WechselBestaetigt = JSONMapper.convertToBoolean(value, true, "aufnehmendBestaetigt");

			// Informationen zu der besuchten Grundschule
			case "grundschuleEinschulungsjahr" -> mapJahr(value, "grundschuleEinschulungsjahr", v -> dtoSchueler.Einschulungsjahr = v);
			case "grundschuleEinschulungsartID" -> mapEinschulungsart(dtoSchueler, value);
			case "idGrundschuleJahreEingangsphase" -> mapEingangsphase(dtoSchueler, value);
			case "kuerzelGrundschuleUebergangsempfehlung" -> mapUebergangsempfehlung(dtoSchueler, value);

			// Informationen zu dem Besuch der Sekundarstufe I
			case "sekIWechsel" -> mapJahr(value, "sekIWechsel", v -> dtoSchueler.JahrWechsel_SI = v);
			case "sekIErsteSchulform" -> mapSchulform(dtoSchueler, value);
			case "sekIIWechsel" -> mapJahr(value, "sekIIWechsel", v -> dtoSchueler.JahrWechsel_SII = v);

			// Informationen zu dem Besuch des Kindergartens
			case "idDauerKindergartenbesuch" -> mapIdDauerKindergartenbesuch(dtoSchueler, value);
			case "idKindergarten" -> mapKindergarten(dtoSchueler, value);
			case "verpflichtungSprachfoerderkurs" -> dtoSchueler.VerpflichtungSprachfoerderkurs = JSONMapper.convertToBoolean(
					value, false, "verpflichtungSprachfoerderkurs");
			case "teilnahmeSprachfoerderkurs" -> dtoSchueler.TeilnahmeSprachfoerderkurs = JSONMapper.convertToBoolean(
					value, false, "teilnahmeSprachfoerderkurs");
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void mapIdDauerKindergartenbesuch(final DTOSchueler dtoSchueler, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, true, "idDauerKindergartenbesuch");
		if (id == null) {
			dtoSchueler.DauerKindergartenbesuch = null;
			return;
		}
		final KindergartenbesuchKatalogEintrag eintrag = Kindergartenbesuch.data().getEintragByID(id);
		if (eintrag == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Kindergartenbesuch mit der ID %d gefunden.".formatted(id));

		dtoSchueler.DauerKindergartenbesuch = eintrag.schluessel;
	}

	private void mapKindergarten(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, true, "idKindergarten");
		if (id == null) {
			dto.Kindergarten_ID = null;
			return;
		}
		final DTOKindergarten kindergartenDTO = conn.queryByKey(DTOKindergarten.class, id);
		if (kindergartenDTO == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Kindergarten mit der ID %d gefunden.".formatted(id));

		dto.Kindergarten_ID = id;
	}

	private static void mapEinschulungsart(final DTOSchueler dtoSchueler, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, true, "grundschuleEinschulungsartID");
		if (id == null) {
			dtoSchueler.EinschulungsartASD = null;
			return;
		}
		final EinschulungsartKatalogEintrag eintrag = Einschulungsart.data().getEintragByID(id);
		if (eintrag == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Einschulungsart mit der ID %d gefunden.".formatted(id));

		dtoSchueler.EinschulungsartASD = eintrag.schluessel;
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
		final Long id = JSONMapper.convertToLong(value, true, "idGrundschuleJahreEingangsphase");
		if (id == null) {
			dtoSchueler.EPJahre = null;
			return;
		}
		final PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag eintrag = PrimarstufeSchuleingangsphaseBesuchsjahre.data().getEintragByID(id);
		if (eintrag == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Eingangsphase mit der ID %d vorhanden.".formatted(id));

		dtoSchueler.EPJahre = Integer.valueOf(eintrag.schluessel);
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
		final Long idSchule = JSONMapper.convertToLong(value, true, key);
		if (idSchule == null) {
			setter.accept(null);
			return;
		}
		final String schulnummer = this.schulenBySchulnummer.values().stream().filter(s -> Objects.equals(s.ID, idSchule)).findFirst()
				.map(s -> s.SchulNr)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Keine Schule mit der ID %d gefunden.".formatted(idSchule)));

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
		final String bezeichnung = this.entlassartenByBezeichnung.values().stream().filter(e -> Objects.equals(e.ID, entlassungGrundID)).findFirst()
				.map(e -> e.Bezeichnung)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Keine Entlassart mit der ID %s gefunden.".formatted(entlassungGrundID)));

		setter.accept(bezeichnung);
	}
}
