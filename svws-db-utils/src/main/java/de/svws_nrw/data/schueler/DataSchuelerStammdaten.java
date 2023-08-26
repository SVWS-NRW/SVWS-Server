package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Function;

import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.core.types.schule.Verkehrssprache;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerFoto;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerStammdaten}.
 */
public final class DataSchuelerStammdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerStammdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerStammdaten(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchueler} in einen Core-DTO {@link SchuelerStammdaten}.
	 */
	private final Function<DTOSchueler, SchuelerStammdaten> dtoMapper = (final DTOSchueler schueler) -> {
		final SchuelerStammdaten daten = new SchuelerStammdaten();
		// Basisdaten
		daten.id = schueler.ID;
		daten.foto = "";
		daten.nachname = schueler.Nachname == null ? "" : schueler.Nachname;
		daten.vorname = schueler.Vorname == null ? "" : schueler.Vorname;
		daten.alleVornamen = schueler.AlleVornamen == null ? "" : schueler.AlleVornamen;
		daten.geschlecht = schueler.Geschlecht.id;
		daten.geburtsdatum = schueler.Geburtsdatum;
		daten.geburtsort = schueler.Geburtsort;
		daten.geburtsname = schueler.Geburtsname;
		// Wohnort und Kontaktdaten
		daten.strassenname = schueler.Strassenname;
		daten.hausnummer = schueler.HausNr;
		daten.hausnummerZusatz = schueler.HausNrZusatz;
		daten.wohnortID = schueler.Ort_ID;
		daten.ortsteilID = schueler.Ortsteil_ID;
		daten.telefon = schueler.Telefon;
		daten.telefonMobil = schueler.Fax;
		daten.emailPrivat = schueler.Email;
		daten.emailSchule = schueler.SchulEmail;
		// Daten zur Staatsangehörigkeit und zur Religion
		daten.staatsangehoerigkeitID = schueler.StaatKrz == null ? null : schueler.StaatKrz.daten.iso3;
		daten.staatsangehoerigkeit2ID = schueler.StaatKrz2 == null ? null : schueler.StaatKrz2.daten.iso3;
		daten.religionID = schueler.Religion_ID;
		daten.druckeKonfessionAufZeugnisse = schueler.KonfDruck;
		daten.religionabmeldung = schueler.Religionsabmeldung;
		daten.religionanmeldung = schueler.Religionsanmeldung;
		// Daten zum Migrationshintergrund
		// TODO DB-Converter für boolean statt Boolean beim Migrationshintergrund
		daten.hatMigrationshintergrund = schueler.Migrationshintergrund != null && schueler.Migrationshintergrund;
		daten.zuzugsjahr = schueler.JahrZuzug == null ? null : schueler.JahrZuzug.toString();
		daten.geburtsland = schueler.GeburtslandSchueler == null ? null : schueler.GeburtslandSchueler.daten.iso3;
		daten.verkehrspracheFamilie = schueler.VerkehrsspracheFamilie == null ? null : schueler.VerkehrsspracheFamilie.daten.kuerzel;
		daten.geburtslandVater = schueler.GeburtslandVater == null ? null : schueler.GeburtslandVater.daten.iso3;
		daten.geburtslandMutter = schueler.GeburtslandMutter == null ? null : schueler.GeburtslandMutter.daten.iso3;
		// Statusdaten
		daten.status = schueler.Status.id;
		daten.istDuplikat = schueler.Duplikat;
		daten.fahrschuelerArtID = schueler.Fahrschueler_ID;
		daten.haltestelleID = schueler.Haltestelle_ID;
		daten.anmeldedatum = schueler.AnmeldeDatum;
		daten.aufnahmedatum = schueler.Aufnahmedatum;
		daten.istVolljaehrig = schueler.Volljaehrig; // TODO ermittle die Information aus den anderen Schülerdaten
		daten.keineAuskunftAnDritte = schueler.KeineAuskunft;
		daten.istSchulpflichtErfuellt = schueler.SchulpflichtErf; // TODO ermittle die Information aus den anderen Schülerdaten
		daten.istBerufsschulpflichtErfuellt = schueler.BerufsschulpflErf; // TODO ermittle die Information aus den anderen Schülerdaten
		daten.hatMasernimpfnachweis = schueler.MasernImpfnachweis;
		daten.erhaeltSchuelerBAFOEG = schueler.Bafoeg;
		daten.erhaeltMeisterBAFOEG = schueler.MeisterBafoeg;
		// Bemerkungen
		daten.bemerkungen = schueler.Bemerkungen;
		return daten;
	};

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
		if (schueler == null)
			return OperationError.NOT_FOUND.getResponse();
		final SchuelerStammdaten daten = dtoMapper.apply(schueler);
		final DTOSchuelerFoto schuelerFoto = conn.queryByKey(DTOSchuelerFoto.class, id);
		if (schuelerFoto != null)
			daten.foto = schuelerFoto.FotoBase64;
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private final Map<String, DataBasicMapper<DTOSchueler>> patchMappings = Map.ofEntries(
			Map.entry("id", (schueler, value, map) -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != schueler.ID))
					throw OperationError.BAD_REQUEST.exception();
			}),
			Map.entry("foto", (schueler, value, map) -> {
				final String strData = JSONMapper.convertToString(value, true, true, null);
				DTOSchuelerFoto schuelerFoto = conn.queryByKey(DTOSchuelerFoto.class, schueler.ID);
				if (schuelerFoto == null)
					schuelerFoto = new DTOSchuelerFoto(schueler.ID);
				final String oldFoto = schuelerFoto.FotoBase64;
				if (((strData == null) && (oldFoto == null)) || ((strData != null) && (strData.equals(oldFoto))))
					return;
				schuelerFoto.FotoBase64 = strData;
				conn.transactionPersist(schuelerFoto);
			}),
			Map.entry("nachname", (schueler, value, map) -> schueler.Nachname = JSONMapper.convertToString(value, false, false, Schema.tab_Schueler.col_Name.datenlaenge())),
			Map.entry("vorname", (schueler, value, map) -> schueler.Vorname = JSONMapper.convertToString(value, false, false, Schema.tab_Schueler.col_Vorname.datenlaenge())),
			Map.entry("alleVornamen", (schueler, value, map) -> schueler.AlleVornamen = JSONMapper.convertToString(value, false, true, Schema.tab_Schueler.col_Zusatz.datenlaenge())),
			Map.entry("geschlecht", (schueler, value, map) -> {
				final Geschlecht geschlecht = Geschlecht.fromValue(JSONMapper.convertToInteger(value, false));
				if (geschlecht == null)
					throw OperationError.CONFLICT.exception();
				schueler.Geschlecht = geschlecht;
			}),
			Map.entry("geburtsdatum", (schueler, value, map) -> schueler.Geburtsdatum = JSONMapper.convertToString(value, false, false, null)),
			Map.entry("geburtsort", (schueler, value, map) -> schueler.Geburtsort = JSONMapper.convertToString(value, false, true, Schema.tab_Schueler.col_Geburtsort.datenlaenge())),
			Map.entry("geburtsname", (schueler, value, map) -> schueler.Geburtsname = JSONMapper.convertToString(value, false, true, Schema.tab_Schueler.col_Geburtsname.datenlaenge())),

			// Wohnort und Kontaktdaten
			Map.entry("strassenname", (schueler, value, map) -> schueler.Strassenname = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Strassenname.datenlaenge())),
			Map.entry("hausnummer", (schueler, value, map) -> schueler.HausNr = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_HausNr.datenlaenge())),
			Map.entry("hausnummerZusatz", (schueler, value, map) -> schueler.HausNrZusatz = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_HausNrZusatz.datenlaenge())),
			Map.entry("wohnortID", (schueler, value, map) -> setWohnort(schueler, JSONMapper.convertToLong(value, true), map.get("ortsteilID") == null ? schueler.Ortsteil_ID : ((Long) map.get("ortsteilID")))),
			Map.entry("ortsteilID", (schueler, value, map) -> setWohnort(schueler, map.get("wohnortID") == null ? schueler.Ort_ID : ((Long) map.get("wohnortID")), JSONMapper.convertToLong(value, true))),
			Map.entry("telefon", (schueler, value, map) -> schueler.Telefon = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Telefon.datenlaenge())),
			Map.entry("telefonMobil", (schueler, value, map) -> schueler.Fax = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Fax.datenlaenge())),
			Map.entry("emailPrivat", (schueler, value, map) -> schueler.Email = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Email.datenlaenge())),
			Map.entry("emailSchule", (schueler, value, map) -> schueler.SchulEmail = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_SchulEmail.datenlaenge())),

			// Daten zur Staatsangehörigkeit und zur Religion
			Map.entry("staatsangehoerigkeitID", (schueler, value, map) -> {
				final String staatsangehoerigkeitID = JSONMapper.convertToString(value, true, true, null);
				if ((staatsangehoerigkeitID == null) || ("".equals(staatsangehoerigkeitID))) {
					schueler.StaatKrz = null;
				} else {
					final Nationalitaeten nat = Nationalitaeten.getByISO3(staatsangehoerigkeitID);
					if (nat == null)
						throw OperationError.NOT_FOUND.exception();
					schueler.StaatKrz = nat;
				}
			}),
			Map.entry("staatsangehoerigkeit2ID", (schueler, value, map) -> {
				final String staatsangehoerigkeit2ID = JSONMapper.convertToString(value, true, true, null);
				if ((staatsangehoerigkeit2ID == null) || ("".equals(staatsangehoerigkeit2ID))) {
					schueler.StaatKrz2 = null;
				} else {
					final Nationalitaeten nat = Nationalitaeten.getByISO3(staatsangehoerigkeit2ID);
					if (nat == null)
						throw OperationError.NOT_FOUND.exception();
					schueler.StaatKrz2 = nat;
				}
			}),
			Map.entry("religionID", (schueler, value, map) -> {
				final Long religionID = JSONMapper.convertToLong(value, false);
				if ((religionID != null) && (religionID < 0))
					throw OperationError.CONFLICT.exception();
				final DTOKonfession rel = conn.queryByKey(DTOKonfession.class, religionID);
				if (rel == null)
					throw OperationError.NOT_FOUND.exception();
				schueler.Religion_ID = religionID;
			}),
			Map.entry("druckeKonfessionAufZeugnisse", (schueler, value, map) -> schueler.KonfDruck = JSONMapper.convertToBoolean(value, false)),
			Map.entry("religionabmeldung", (schueler, value, map) -> schueler.Religionsabmeldung = JSONMapper.convertToString(value, true, true, null)),
			Map.entry("religionanmeldung", (schueler, value, map) -> schueler.Religionsanmeldung = JSONMapper.convertToString(value, true, true, null)),

			// Daten zum Migrationshintergrund
			Map.entry("hatMigrationshintergrund", (schueler, value, map) -> schueler.Migrationshintergrund = JSONMapper.convertToBoolean(value, false)),
			Map.entry("zuzugsjahr", (schueler, value, map) -> {
				final String text = JSONMapper.convertToString(value, true, true, null);
				Integer jahr = null;
				if ((text != null) && (!"".equals(text))) {
					try {
						jahr = Integer.parseUnsignedInt(text);
						if ((jahr <= 1900) || (jahr > 3000))   // TODO Bestimme das aktuelle Jahr für die obere Grenze des Bereichs
							throw OperationError.BAD_REQUEST.exception();
					} catch (final NumberFormatException e) {
						throw OperationError.BAD_REQUEST.exception(e);
					}
				}
				schueler.JahrZuzug = jahr;
			}),
			Map.entry("geburtsland", (schueler, value, map) -> {
				final String strData = JSONMapper.convertToString(value, true, true, null);
				final Nationalitaeten nat = Nationalitaeten.getByISO3(strData);
				if (nat == null)
					throw OperationError.NOT_FOUND.exception();
				schueler.GeburtslandSchueler = nat;
			}),
			Map.entry("verkehrspracheFamilie", (schueler, value, map) -> {
				final String strData = JSONMapper.convertToString(value, true, true, null);
				final Verkehrssprache vs = Verkehrssprache.getByKuerzelAuto(strData);
				if (vs == null)
					throw OperationError.NOT_FOUND.exception();
				schueler.VerkehrsspracheFamilie = vs;
			}),
			Map.entry("geburtslandVater", (schueler, value, map) -> {
				final String strData = JSONMapper.convertToString(value, true, true, null);
				final Nationalitaeten nat = Nationalitaeten.getByISO3(strData);
				if (nat == null)
					throw OperationError.NOT_FOUND.exception();
				schueler.GeburtslandVater = nat;
			}),
			Map.entry("geburtslandMutter", (schueler, value, map) -> {
				final String strData = JSONMapper.convertToString(value, true, true, null);
				final Nationalitaeten nat = Nationalitaeten.getByISO3(strData);
				if (nat == null)
					throw OperationError.NOT_FOUND.exception();
				schueler.GeburtslandMutter = nat;
			}),

			// Statusdaten
			Map.entry("status", (schueler, value, map) -> {
				final SchuelerStatus s = SchuelerStatus.fromID(JSONMapper.convertToInteger(value, false));
				if (s == null)
					throw OperationError.BAD_REQUEST.exception();
				schueler.Status = s;
			}),
			Map.entry("fahrschuelerArtID", (schueler, value, map) -> {
				final Long fid = JSONMapper.convertToLong(value, true);
				if ((fid != null) && (fid < 0))
					throw OperationError.CONFLICT.exception();
				if (fid == null) {
					schueler.Fahrschueler_ID = null;
				} else {
					final DTOFahrschuelerart f = conn.queryByKey(DTOFahrschuelerart.class, fid);
					if (f == null)
						throw OperationError.NOT_FOUND.exception();
					schueler.Fahrschueler_ID = fid;
				}
			}),
			Map.entry("haltestelleID", (schueler, value, map) -> {
				final Long hid = JSONMapper.convertToLong(value, true);
				if ((hid != null) && (hid < 0))
					throw OperationError.CONFLICT.exception();
				if (hid == null) {
					schueler.Haltestelle_ID = null;
				} else {
					final DTOHaltestellen h = conn.queryByKey(DTOHaltestellen.class, hid);
					if (h == null)
						throw OperationError.NOT_FOUND.exception();
					schueler.Haltestelle_ID = hid;
				}
			}),
			Map.entry("anmeldedatum", (schueler, value, map) -> schueler.AnmeldeDatum = JSONMapper.convertToString(value, false, false, null)),
			Map.entry("aufnahmedatum", (schueler, value, map) -> {
				final String aufnahmedatum = JSONMapper.convertToString(value, true, true, null);
				schueler.Aufnahmedatum = "".equals(aufnahmedatum) ? null : aufnahmedatum;
			}),
			Map.entry("istVolljaehrig", (schueler, value, map) -> schueler.Volljaehrig = JSONMapper.convertToBoolean(value, false)),
			Map.entry("istSchulpflichtErfuellt", (schueler, value, map) -> schueler.SchulpflichtErf = JSONMapper.convertToBoolean(value, false)),
			Map.entry("istBerufsschulpflichtErfuellt", (schueler, value, map) -> schueler.BerufsschulpflErf = JSONMapper.convertToBoolean(value, false)),
			Map.entry("hatMasernimpfnachweis", (schueler, value, map) -> schueler.MasernImpfnachweis = JSONMapper.convertToBoolean(value, false)),
			Map.entry("keineAuskunftAnDritte", (schueler, value, map) -> schueler.KeineAuskunft = JSONMapper.convertToBoolean(value, false)),
			Map.entry("erhaeltSchuelerBAFOEG", (schueler, value, map) -> schueler.Bafoeg = JSONMapper.convertToBoolean(value, false)),
			Map.entry("erhaeltMeisterBAFOEG", (schueler, value, map) -> schueler.MeisterBafoeg = JSONMapper.convertToBoolean(value, false)),
			Map.entry("istDuplikat", (schueler, value, map) -> schueler.Duplikat = JSONMapper.convertToBoolean(value, false)),

		// Bemerkungen
		Map.entry("bemerkungen", (schueler, value, map) -> schueler.Bemerkungen = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Bemerkungen.datenlaenge()))
	);

	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOSchueler.class, patchMappings);
	}


	/**
	 * Setzt den Wohnort bei den Schülerdaten und prüft dabei die Angabe des Ortsteils auf Korrektheit in Bezug auf die Ortsteile
	 * in der Datenbank. Ggf. wird der Ortsteil auf null gesetzt.
	 *
	 * @param schueler     das Schüler-DTO der Datenbank
	 * @param wohnortID    die zu setzende Wohnort-ID
	 * @param ortsteilID   die zu setzende O	eil-ID
	 *
	 * @throws WebApplicationException   eine Exception mit dem HTTP-Fehlercode 409, falls die ID negative und damit ungültig ist
	 */
	private void setWohnort(final DTOSchueler schueler, final Long wohnortID, final Long ortsteilID) throws WebApplicationException {
		if ((wohnortID != null) && (wohnortID < 0))
			throw OperationError.CONFLICT.exception();
		if ((ortsteilID != null) && (ortsteilID < 0))
			throw OperationError.CONFLICT.exception();
		schueler.Ort_ID = wohnortID;
		// Prüfe, ob die Ortsteil ID in Bezug auf die WohnortID gültig ist, wähle hierbei null-Verweise auf die K_Ort-Tabelle als überall gültig
		Long ortsteilIDNeu = ortsteilID;
		if (ortsteilIDNeu != null) {
			final DTOOrtsteil ortsteil = conn.queryByKey(DTOOrtsteil.class, ortsteilIDNeu);
			if ((ortsteil == null) || ((ortsteil.Ort_ID != null) && (!ortsteil.Ort_ID.equals(wohnortID)))) {
				ortsteilIDNeu = null;
			}
		}
		schueler.Ortsteil_ID = ortsteilIDNeu;
	}


}
