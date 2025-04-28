package de.svws_nrw.data.schueler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.types.schule.Verkehrssprache;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerFoto;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSchuelerStammdatenTest {

	@InjectMocks
	private DataSchuelerStammdaten cut;

	@Mock
	private DBEntityManager conn;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	void getById() throws ApiOperationException {
		final DTOSchueler schuelerDto = createDTOSchueler();
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(schuelerDto);

		final DTOSchuelerFoto schuelerFotoDto = new DTOSchuelerFoto(1L);
		schuelerFotoDto.FotoBase64 = "TestBase64Foto";
		when(conn.queryByKey(DTOSchuelerFoto.class, 1L)).thenReturn(schuelerFotoDto);

		final SchuelerStammdaten result = cut.getById(1L);

		assertThat(result).isNotNull()
				.hasFieldOrPropertyWithValue("foto", schuelerFotoDto.FotoBase64)
				.hasFieldOrPropertyWithValue("id", schuelerDto.ID)
				.hasFieldOrPropertyWithValue("vorname", schuelerDto.Vorname)
				.hasFieldOrPropertyWithValue("nachname", schuelerDto.Nachname)
				.hasFieldOrPropertyWithValue("alleVornamen", schuelerDto.AlleVornamen)
				.hasFieldOrPropertyWithValue("geschlecht", schuelerDto.Geschlecht.id)
				.hasFieldOrPropertyWithValue("geburtsdatum", schuelerDto.Geburtsdatum)
				.hasFieldOrPropertyWithValue("geburtsort", schuelerDto.Geburtsort)
				.hasFieldOrPropertyWithValue("geburtsname", schuelerDto.Geburtsname)
				.hasFieldOrPropertyWithValue("strassenname", schuelerDto.Strassenname)
				.hasFieldOrPropertyWithValue("hausnummer", schuelerDto.HausNr)
				.hasFieldOrPropertyWithValue("hausnummerZusatz", schuelerDto.HausNrZusatz)
				.hasFieldOrPropertyWithValue("wohnortID", schuelerDto.Ort_ID)
				.hasFieldOrPropertyWithValue("ortsteilID", schuelerDto.Ortsteil_ID)
				.hasFieldOrPropertyWithValue("telefon", schuelerDto.Telefon)
				.hasFieldOrPropertyWithValue("telefonMobil", schuelerDto.Fax)
				.hasFieldOrPropertyWithValue("emailPrivat", schuelerDto.Email)
				.hasFieldOrPropertyWithValue("emailSchule", schuelerDto.SchulEmail)
				.hasFieldOrPropertyWithValue("staatsangehoerigkeitID", schuelerDto.StaatKrz.historie().getLast().iso3)
				.hasFieldOrPropertyWithValue("staatsangehoerigkeit2ID", schuelerDto.StaatKrz2.historie().getLast().iso3)
				.hasFieldOrPropertyWithValue("religionID", schuelerDto.Religion_ID)
				.hasFieldOrPropertyWithValue("druckeKonfessionAufZeugnisse", schuelerDto.KonfDruck)
				.hasFieldOrPropertyWithValue("religionabmeldung", schuelerDto.Religionsabmeldung)
				.hasFieldOrPropertyWithValue("religionanmeldung", schuelerDto.Religionsanmeldung)
				.hasFieldOrPropertyWithValue("hatMigrationshintergrund", schuelerDto.Migrationshintergrund)
				.hasFieldOrPropertyWithValue("zuzugsjahr", schuelerDto.JahrZuzug)
				.hasFieldOrPropertyWithValue("geburtsland", schuelerDto.GeburtslandSchueler.historie().getLast().iso3)
				.hasFieldOrPropertyWithValue("verkehrspracheFamilie", schuelerDto.VerkehrsspracheFamilie.daten.kuerzel)
				.hasFieldOrPropertyWithValue("geburtslandVater", schuelerDto.GeburtslandVater.historie().getLast().iso3)
				.hasFieldOrPropertyWithValue("geburtslandMutter", schuelerDto.GeburtslandMutter.historie().getLast().iso3)
				.hasFieldOrPropertyWithValue("status", schuelerDto.idStatus)
				.hasFieldOrPropertyWithValue("istDuplikat", schuelerDto.Duplikat)
				.hasFieldOrPropertyWithValue("externeSchulNr", schuelerDto.ExterneSchulNr)
				.hasFieldOrPropertyWithValue("fahrschuelerArtID", schuelerDto.Fahrschueler_ID)
				.hasFieldOrPropertyWithValue("haltestelleID", schuelerDto.Haltestelle_ID)
				.hasFieldOrPropertyWithValue("anmeldedatum", schuelerDto.AnmeldeDatum)
				.hasFieldOrPropertyWithValue("aufnahmedatum", schuelerDto.Aufnahmedatum)
				.hasFieldOrPropertyWithValue("istVolljaehrig", schuelerDto.Volljaehrig)
				.hasFieldOrPropertyWithValue("keineAuskunftAnDritte", schuelerDto.KeineAuskunft)
				.hasFieldOrPropertyWithValue("istSchulpflichtErfuellt", schuelerDto.SchulpflichtErf)
				.hasFieldOrPropertyWithValue("istBerufsschulpflichtErfuellt", schuelerDto.BerufsschulpflErf)
				.hasFieldOrPropertyWithValue("hatMasernimpfnachweis", schuelerDto.MasernImpfnachweis)
				.hasFieldOrPropertyWithValue("erhaeltSchuelerBAFOEG", schuelerDto.Bafoeg)
				.hasFieldOrPropertyWithValue("erhaeltMeisterBAFOEG", schuelerDto.MeisterBafoeg);
	}

	private static Stream<Arguments> mapAttribute() {
		return Stream.of(
				arguments("id", 1L, 1L, null),
				arguments("id", 2L, null, new ApiOperationException(Response.Status.BAD_REQUEST)),
				arguments("foto", "abc", "abc", null),
				arguments("foto", null, "TestBase64Foto", null),
				arguments("nachname", "Musterfrau", "Musterfrau", null),
				arguments("nachname", null, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut nachname: Der Wert null ist nicht erlaubt.")),
				arguments("nachname", "", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut nachname: Ein leerer String ist hier nicht erlaubt.")),
				arguments("nachname", RandomStringUtils.randomAscii(121), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut nachname: Die Länge des Strings ist auf 120 Zeichen limitiert.")),
				arguments("vorname", "Maria", "Maria", null),
				arguments("vorname", null, null, new ApiOperationException(Response.Status.BAD_REQUEST, "Attribut vorname: Der Wert null ist nicht erlaubt.")),
				arguments("vorname", "", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut vorname: Ein leerer String ist hier nicht erlaubt.")),
				arguments("vorname", RandomStringUtils.randomAscii(81), null, new ApiOperationException(Response.Status.BAD_REQUEST, "Attribut vorname: "
						+ "Die Länge des Strings ist auf 80 Zeichen limitiert.")),
				arguments("alleVornamen", "Maria", "Maria", null),
				arguments("alleVornamen", null, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut alleVornamen: Der Wert null ist nicht erlaubt.")),
				arguments("alleVornamen", "", "", null),
				arguments("alleVornamen", RandomStringUtils.randomAscii(256), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut alleVornamen: Die Länge des Strings ist auf 255 Zeichen limitiert.")),
				arguments("geschlecht", Geschlecht.X.id, Geschlecht.X.id, null),
				arguments("geschlecht", 999, null, new ApiOperationException(Response.Status.CONFLICT)),
				arguments("geschlecht", null, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut geschlecht: Der Wert null ist nicht erlaubt")),
				arguments("geschlecht", "", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut geschlecht: Fehler beim Konvertieren zu Integer")),
				arguments("geburtsdatum", "10-10-1990", "10-10-1990", null),
				arguments("geburtsdatum", null, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut geburtsdatum: Der Wert null ist nicht erlaubt.")),
				arguments("geburtsdatum", "", "", new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut geburtsdatum: Ein leerer String ist hier nicht erlaubt.")),
				arguments("geburtsort", "Musterhausen", "Musterhausen", null),
				arguments("geburtsort", null, null, null),
				arguments("geburtsort", "", "", null),
				arguments("geburtsort", RandomStringUtils.randomAscii(101), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut geburtsort: Die Länge des Strings ist auf 100 Zeichen limitiert.")),
				arguments("geburtsname", "Musterfrau", "Musterfrau", null),
				arguments("geburtsname", null, null, null),
				arguments("geburtsname", "", "", null),
				arguments("geburtsname", RandomStringUtils.randomAscii(121), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut geburtsname: Die Länge des Strings ist auf 120 Zeichen limitiert.")),
				arguments("strassenname", "Musterfrau", "Musterfrau", null),
				arguments("strassenname", null, null, null),
				arguments("strassenname", "", "", null),
				arguments("strassenname", RandomStringUtils.randomAscii(56), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut strassenname: Die Länge des Strings ist auf 55 Zeichen limitiert.")),
				arguments("hausnummer", "2", "2", null),
				arguments("hausnummer", null, null, null),
				arguments("hausnummer", "", "", null),
				arguments("hausnummer", RandomStringUtils.randomAscii(11), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut hausnummer: Die Länge des Strings ist auf 10 Zeichen limitiert.")),
				arguments("hausnummerZusatz", "b", "b", null),
				arguments("hausnummerZusatz", null, null, null),
				arguments("hausnummerZusatz", "", "", null),
				arguments("hausnummerZusatz", RandomStringUtils.randomAscii(31), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut hausnummerZusatz: Die Länge des Strings ist auf 30 Zeichen limitiert.")),
				arguments("wohnortID", 312L, 312L, null),
				arguments("wohnortID", null, null, null),
				arguments("wohnortID", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut wohnortID: Der Wert kann nicht in einen Long umgewandelt werden")),
				arguments("ortsteilID", 312L, null, null),
				arguments("ortsteilID", null, null, null),
				arguments("ortsteilID", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut ortsteilID: Der Wert kann nicht in einen Long umgewandelt werden")),
				arguments("telefon", "12345", "12345", null),
				arguments("telefon", null, null, null),
				arguments("telefon", "", "", null),
				arguments("telefon", RandomStringUtils.randomAscii(21), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut telefon: Die Länge des Strings ist auf 20 Zeichen limitiert.")),
				arguments("telefonMobil", "12345", "12345", null),
				arguments("telefonMobil", null, null, null),
				arguments("telefonMobil", "", "", null),
				arguments("telefonMobil", RandomStringUtils.randomAscii(21), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut telefonMobil: Die Länge des Strings ist auf 20 Zeichen limitiert.")),
				arguments("emailPrivat", "abc@abc.de", "abc@abc.de", null),
				arguments("emailPrivat", null, null, null),
				arguments("emailPrivat", "", "", null),
				arguments("emailPrivat", RandomStringUtils.randomAscii(101), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut emailPrivat: Die Länge des Strings ist auf 100 Zeichen limitiert.")),
				arguments("emailSchule", "abc@abc.de", "abc@abc.de", null),
				arguments("emailSchule", null, null, null),
				arguments("emailSchule", "", "", null),
				arguments("emailSchule", RandomStringUtils.randomAscii(101), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut emailSchule: Die Länge des Strings ist auf 100 Zeichen limitiert.")),
				arguments("staatsangehoerigkeitID", "AFG", Nationalitaeten.getByISO3("AFG"), null),
				arguments("staatsangehoerigkeitID", null, null, null),
				arguments("staatsangehoerigkeitID", "", null, null),
				arguments("staatsangehoerigkeitID", "abc", null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("staatsangehoerigkeit2ID", "AFG", Nationalitaeten.getByISO3("AFG"), null),
				arguments("staatsangehoerigkeit2ID", null, null, null),
				arguments("staatsangehoerigkeit2ID", "", null, null),
				arguments("staatsangehoerigkeit2ID", "abc", null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("religionID", 123L, 123L, null),
				arguments("religionID", null, null, null),
				arguments("religionID", -2L, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut religionID: Fehler beim Konvertieren: Der Zahlwert liegt außerhalb des geforderten Bereichs.")),
				arguments("religionID", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut religionID: Fehler beim Konvertieren zu Long: Das Objekt ist keine Zahl.")),
				arguments("religionID", 0L, null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("druckeKonfessionAufZeugnisse", false, false, null),
				arguments("druckeKonfessionAufZeugnisse", null, null,
						new ApiOperationException(Response.Status.BAD_REQUEST, "Attribut druckeKonfessionAufZeugnisse: Der Wert null ist nicht erlaubt")),
				arguments("druckeKonfessionAufZeugnisse", "abc", null,
						new ApiOperationException(Response.Status.BAD_REQUEST, "Attribut druckeKonfessionAufZeugnisse: Fehler beim Konvertieren zu Boolean")),
				arguments("religionabmeldung", "03-03-1993", "03-03-1993", null),
				arguments("religionabmeldung", null, null, null),
				arguments("religionabmeldung", "", "", null),
				arguments("religionanmeldung", "03-03-1993", "03-03-1993", null),
				arguments("religionanmeldung", null, null, null),
				arguments("religionanmeldung", "", "", null),
				arguments("hatMigrationshintergrund", false, false, null),
				arguments("hatMigrationshintergrund", null, null,
						new ApiOperationException(Response.Status.BAD_REQUEST, "Attribut hatMigrationshintergrund: Der Wert null ist nicht erlaubt")),
				arguments("hatMigrationshintergrund", "abc", null,
						new ApiOperationException(Response.Status.BAD_REQUEST, "Attribut hatMigrationshintergrund: Fehler beim Konvertieren zu Boolean")),
				arguments("zuzugsjahr", 2021, 2021, null),
				arguments("zuzugsjahr", 1899, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut zuzugsjahr: Fehler beim Konvertieren: Der Zahlwert liegt außerhalb des geforderten Bereichs.")),
				arguments("zuzugsjahr", 3000, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut zuzugsjahr: Fehler beim Konvertieren: Der Zahlwert liegt außerhalb des geforderten Bereichs.")),
				arguments("zuzugsjahr", null, null, null),
				arguments("zuzugsjahr", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut zuzugsjahr: Fehler beim Konvertieren zu Integer: Das Objekt ist keine Zahl.")),
				arguments("verkehrspracheFamilie", "ace", Verkehrssprache.getByKuerzelAuto("ace"), null),
				arguments("verkehrspracheFamilie", null, null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("verkehrspracheFamilie", "", null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("verkehrspracheFamilie", "abc", null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("geburtsland", "AFG", Nationalitaeten.getByISO3("AFG"), null),
				arguments("geburtsland", null, null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("geburtsland", "", null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("geburtsland", "abc", null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("geburtslandVater", "AFG", Nationalitaeten.getByISO3("AFG"), null),
				arguments("geburtslandVater", null, null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("geburtslandVater", "", null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("geburtslandVater", "abc", null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("geburtslandMutter", "AFG", Nationalitaeten.getByISO3("AFG"), null),
				arguments("geburtslandMutter", null, null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("geburtslandMutter", "", null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("geburtslandMutter", "abc", null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("status", 3, 3, null),
				arguments("status", 999, null, new ApiOperationException(Response.Status.BAD_REQUEST)),
				arguments("status", null, null, new ApiOperationException(Response.Status.BAD_REQUEST, "Attribut status: Der Wert null ist nicht erlaubt")),
				arguments("status", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut status: Fehler beim Konvertieren zu Integer")),
				arguments("externeSchulNr", "123456", "123456", null),
				arguments("externeSchulNr", null, null, null),
				arguments("externeSchulNr", "", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Die Anzahl der Ziffern einer Schulnummer aus NRW muss 6 betragen.")),
				arguments("externeSchulNr", "1234567", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut externeSchulNr: Die Länge des Strings ist auf 6 Zeichen limitiert.")),
				arguments("externeSchulNr", "123", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Die Anzahl der Ziffern einer Schulnummer aus NRW muss 6 betragen.")),
				arguments("fahrschuelerArtID", 22L, 22L, null),
				arguments("fahrschuelerArtID", null, null, null),
				arguments("fahrschuelerArtID", -2L, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut fahrschuelerArtID: Fehler beim Konvertieren: Der Zahlwert liegt außerhalb des geforderten Bereichs.")),
				arguments("fahrschuelerArtID", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut fahrschuelerArtID: Fehler beim Konvertieren zu Long: Das Objekt ist keine Zahl.")),
				arguments("fahrschuelerArtID", 0L, null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("haltestelleID", 33L, 33L, null),
				arguments("haltestelleID", null, null, null),
				arguments("haltestelleID", -2L, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut haltestelleID: Fehler beim Konvertieren: Der Zahlwert liegt außerhalb des geforderten Bereichs.")),
				arguments("haltestelleID", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut haltestelleID: Fehler beim Konvertieren zu Long: Das Objekt ist keine Zahl.")),
				arguments("haltestelleID", 0L, null, new ApiOperationException(Response.Status.NOT_FOUND)),
				arguments("anmeldedatum", "03-03-1993", "03-03-1993", null),
				arguments("anmeldedatum", null, null, null),
				arguments("anmeldedatum", "", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut anmeldedatum: Ein leerer String ist hier nicht erlaubt.")),
				arguments("aufnahmedatum", "03-03-1993", "03-03-1993", null),
				arguments("aufnahmedatum", null, null, null),
				arguments("aufnahmedatum", "", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut aufnahmedatum: Ein leerer String ist hier nicht erlaubt.")),
				arguments("istVolljaehrig", false, false, null),
				arguments("istVolljaehrig", null, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut istVolljaehrig: Der Wert null ist nicht erlaubt")),
				arguments("istVolljaehrig", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut istVolljaehrig: Fehler beim Konvertieren zu Boolean")),
				arguments("istSchulpflichtErfuellt", false, false, null),
				arguments("istSchulpflichtErfuellt", null, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut istSchulpflichtErfuellt: Der Wert null ist nicht erlaubt")),
				arguments("istSchulpflichtErfuellt", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut istSchulpflichtErfuellt: Fehler beim Konvertieren zu Boolean")),
				arguments("istBerufsschulpflichtErfuellt", false, false, null),
				arguments("istBerufsschulpflichtErfuellt", null, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut istBerufsschulpflichtErfuellt: Der Wert null ist nicht erlaubt")),
				arguments("istBerufsschulpflichtErfuellt", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut istBerufsschulpflichtErfuellt: Fehler beim Konvertieren zu Boolean")),
				arguments("hatMasernimpfnachweis", false, false, null),
				arguments("hatMasernimpfnachweis", null, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut hatMasernimpfnachweis: Der Wert null ist nicht erlaubt")),
				arguments("hatMasernimpfnachweis", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut hatMasernimpfnachweis: Fehler beim Konvertieren zu Boolean")),
				arguments("keineAuskunftAnDritte", false, false, null),
				arguments("keineAuskunftAnDritte", null, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut keineAuskunftAnDritte: Der Wert null ist nicht erlaubt")),
				arguments("keineAuskunftAnDritte", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut keineAuskunftAnDritte: Fehler beim Konvertieren zu Boolean")),
				arguments("erhaeltSchuelerBAFOEG", false, false, null),
				arguments("erhaeltSchuelerBAFOEG", null, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut erhaeltSchuelerBAFOEG: Der Wert null ist nicht erlaubt")),
				arguments("erhaeltSchuelerBAFOEG", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut erhaeltSchuelerBAFOEG: Fehler beim Konvertieren zu Boolean")),
				arguments("erhaeltMeisterBAFOEG", false, false, null),
				arguments("erhaeltMeisterBAFOEG", null, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut erhaeltMeisterBAFOEG: Der Wert null ist nicht erlaubt")),
				arguments("erhaeltMeisterBAFOEG", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut erhaeltMeisterBAFOEG: Fehler beim Konvertieren zu Boolean")),
				arguments("istDuplikat", false, false, null),
				arguments("istDuplikat", null, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut istDuplikat: Der Wert null ist nicht erlaubt")),
				arguments("istDuplikat", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut istDuplikat: Fehler beim Konvertieren zu Boolean")),
				arguments("unknown", "oh oh ! das wollen wir auf keinen Fall!", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Das Patchen des Attributes unknown ist nicht implementiert."))
		);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | Erfolg")
	@MethodSource
	void mapAttribute(final String attributeName, final Object givenValue, final Object expectedValue, final ApiOperationException expectedException) {
		final DTOSchueler schuelerDto = createDTOSchueler();

		final DTOSchuelerFoto schuelerFotoDto = new DTOSchuelerFoto(1L);
		schuelerFotoDto.FotoBase64 = "TestBase64Foto";

		mapAttributeStubbings(attributeName, expectedValue, schuelerFotoDto);

		final Throwable throwable = catchThrowable(() -> cut.mapAttribute(schuelerDto, attributeName, givenValue, Collections.emptyMap()));
		if (expectedException != null) {
			assertThat(throwable).isInstanceOf(ApiOperationException.class)
					.hasMessage(expectedException.getMessage())
					.hasFieldOrPropertyWithValue("status", expectedException.getStatus());

		} else {
			switch (attributeName) {
				case "id" -> assertThat(schuelerDto.ID).isEqualTo(expectedValue);
				case "foto" -> assertThat(schuelerFotoDto.FotoBase64).isEqualTo(expectedValue);
				case "nachname" -> assertThat(schuelerDto.Nachname).isEqualTo(expectedValue);
				case "vorname" -> assertThat(schuelerDto.Vorname).isEqualTo(expectedValue);
				case "alleVornamen" -> assertThat(schuelerDto.AlleVornamen).isEqualTo(expectedValue);
				case "geschlecht" -> assertThat(schuelerDto.Geschlecht.id).isEqualTo(expectedValue);
				case "geburtsdatum" -> assertThat(schuelerDto.Geburtsdatum).isEqualTo(expectedValue);
				case "geburtsort" -> assertThat(schuelerDto.Geburtsort).isEqualTo(expectedValue);
				case "geburtsname" -> assertThat(schuelerDto.Geburtsname).isEqualTo(expectedValue);
				// Wohnort und Kontaktdaten
				case "strassenname" -> assertThat(schuelerDto.Strassenname).isEqualTo(expectedValue);
				case "hausnummer" -> assertThat(schuelerDto.HausNr).isEqualTo(expectedValue);
				case "hausnummerZusatz" -> assertThat(schuelerDto.HausNrZusatz).isEqualTo(expectedValue);
				case "wohnortID" -> assertThat(schuelerDto.Ort_ID).isEqualTo(expectedValue);
				case "ortsteilID" -> assertThat(schuelerDto.Ortsteil_ID).isEqualTo(expectedValue);
				case "telefon" -> assertThat(schuelerDto.Telefon).isEqualTo(expectedValue);
				case "telefonMobil" -> assertThat(schuelerDto.Fax).isEqualTo(expectedValue);
				case "emailPrivat" -> assertThat(schuelerDto.Email).isEqualTo(expectedValue);
				case "emailSchule" -> assertThat(schuelerDto.SchulEmail).isEqualTo(expectedValue);
				// Daten zur Staatsangehörigkeit und zur Religion
				case "staatsangehoerigkeitID" -> assertThat(schuelerDto.StaatKrz).isEqualTo(expectedValue);
				case "staatsangehoerigkeit2ID" -> assertThat(schuelerDto.StaatKrz2).isEqualTo(expectedValue);
				case "religionID" -> assertThat(schuelerDto.Religion_ID).isEqualTo(expectedValue);
				case "druckeKonfessionAufZeugnisse" -> assertThat(schuelerDto.KonfDruck).isEqualTo(expectedValue);
				case "religionabmeldung" -> assertThat(schuelerDto.Religionsabmeldung).isEqualTo(expectedValue);
				case "religionanmeldung" -> assertThat(schuelerDto.Religionsanmeldung).isEqualTo(expectedValue);
				// Daten zum Migrationshintergrund
				case "hatMigrationshintergrund" -> assertThat(schuelerDto.Migrationshintergrund).isEqualTo(expectedValue);
				case "zuzugsjahr" -> assertThat(schuelerDto.JahrZuzug).isEqualTo(expectedValue);
				case "verkehrspracheFamilie" -> assertThat(schuelerDto.VerkehrsspracheFamilie).isEqualTo(expectedValue);
				case "geburtsland" -> assertThat(schuelerDto.GeburtslandSchueler).isEqualTo(expectedValue);
				case "geburtslandVater" -> assertThat(schuelerDto.GeburtslandVater).isEqualTo(expectedValue);
				case "geburtslandMutter" -> assertThat(schuelerDto.GeburtslandMutter).isEqualTo(expectedValue);
				// Statusdaten
				case "status" -> assertThat(schuelerDto.idStatus).isEqualTo(expectedValue);
				case "externeSchulNr" -> assertThat(schuelerDto.ExterneSchulNr).isEqualTo(expectedValue);
				case "fahrschuelerArtID" -> assertThat(schuelerDto.Fahrschueler_ID).isEqualTo(expectedValue);
				case "haltestelleID" -> assertThat(schuelerDto.Haltestelle_ID).isEqualTo(expectedValue);
				case "anmeldedatum" -> assertThat(schuelerDto.AnmeldeDatum).isEqualTo(expectedValue);
				case "aufnahmedatum" -> assertThat(schuelerDto.Aufnahmedatum).isEqualTo(expectedValue);
				case "istVolljaehrig" -> assertThat(schuelerDto.Volljaehrig).isEqualTo(expectedValue);
				case "istSchulpflichtErfuellt" -> assertThat(schuelerDto.SchulpflichtErf).isEqualTo(expectedValue);
				case "istBerufsschulpflichtErfuellt" -> assertThat(schuelerDto.BerufsschulpflErf).isEqualTo(expectedValue);
				case "hatMasernimpfnachweis" -> assertThat(schuelerDto.MasernImpfnachweis).isEqualTo(expectedValue);
				case "keineAuskunftAnDritte" -> assertThat(schuelerDto.KeineAuskunft).isEqualTo(expectedValue);
				case "erhaeltSchuelerBAFOEG" -> assertThat(schuelerDto.Bafoeg).isEqualTo(expectedValue);
				case "erhaeltMeisterBAFOEG" -> assertThat(schuelerDto.MeisterBafoeg).isEqualTo(expectedValue);
				case "istDuplikat" -> assertThat(schuelerDto.Duplikat).isEqualTo(expectedValue);
				default -> assertThat(throwable)
						.isInstanceOf(ApiOperationException.class)
						.hasMessageStartingWith("Das Patchen des Attributes %s ist nicht implementiert.".formatted(attributeName))
						.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			}
		}
	}

	void mapAttributeStubbings(final String attributeName, final Object expectedValue, final DTOSchuelerFoto schuelerFotoDto) {
		final DTOKonfession konfessionDto = new DTOKonfession(123L, "TestKonfession");
		final DTOFahrschuelerart fahrschuelerartDto = new DTOFahrschuelerart(22L, "TestFahrschuelerArt");
		final DTOHaltestellen haltestelleDto = new DTOHaltestellen(33L, "TestHaltestelle");

		if (attributeName.equals("foto")) {
			when(conn.queryByKey(DTOSchuelerFoto.class, 1L)).thenReturn(schuelerFotoDto);
		} else if (attributeName.equals("religionID") && Long.valueOf(123L).equals(expectedValue)) {
			when(conn.queryByKey(DTOKonfession.class, 123L)).thenReturn(konfessionDto);
		} else if (attributeName.equals("status") && (expectedValue != null)) {
			final Benutzer benutzer = mock(Benutzer.class);
			final Schuljahresabschnitt schuljahresabschnitt = new Schuljahresabschnitt();
			schuljahresabschnitt.schuljahr = 2000;
			when(benutzer.schuleGetSchuljahresabschnittByIdOrDefault(99L)).thenReturn(schuljahresabschnitt);
			when(conn.getUser()).thenReturn(benutzer);
		} else if (attributeName.equals("fahrschuelerArtID") && Long.valueOf(22L).equals(expectedValue)) {
			when(conn.queryByKey(DTOFahrschuelerart.class, 22L)).thenReturn(fahrschuelerartDto);
		} else if (attributeName.equals("haltestelleID") && Long.valueOf(33L).equals(expectedValue)) {
			when(conn.queryByKey(DTOHaltestellen.class, 33L)).thenReturn(haltestelleDto);
		}
	}

	@Test
	void getListByIdsAsResponse() throws ApiOperationException {
		final List<Long> ids = List.of(1L, 2L);
		final DTOSchueler dtoSchueler1 = createDTOSchuelerWithId(1L);
		final DTOSchueler dtoSchueler2 = createDTOSchuelerWithId(2L);
		when(conn.queryByKeyList(DTOSchueler.class, ids)).thenReturn(List.of(dtoSchueler1, dtoSchueler2));

		final Response result = cut.getListByIdsAsResponse(ids);

		assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(result.getEntity()).asInstanceOf(InstanceOfAssertFactories.LIST)
				.hasSize(2).extracting("id").containsExactly(1L, 2L);

	}

	@Test
	void getDTOList() throws ApiOperationException {
		final List<Long> ids = List.of(1L, 2L);
		final DTOSchueler dtoSchueler1 = createDTOSchuelerSimpleWithId(1L);
		final DTOSchueler dtoSchueler2 = createDTOSchuelerSimpleWithId(2L);
		when(conn.queryByKeyList(DTOSchueler.class, ids)).thenReturn(List.of(dtoSchueler1, dtoSchueler2));

		final List<DTOSchueler> result = cut.getDTOList(ids);

		assertThat(result).hasSize(2).contains(dtoSchueler1, dtoSchueler2);
	}

	@Test
	void getDTOListWithIdsIsNull() {
		final Throwable result = catchThrowable(() -> cut.getDTOList(null));

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Liste der IDs für die Schüler darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	void getDTO() throws ApiOperationException {
		final DTOSchueler dtoSchueler = createDTOSchueler();
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(dtoSchueler);

		final DTOSchueler result = cut.getDTO(1L);

		assertThat(result).isEqualTo(dtoSchueler);
	}

	@Test
	void getDTOWithIdIsNull() {
		final Throwable result = catchThrowable(() -> cut.getDTO(null));

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID für den Schüler darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	void getDTOWithIdNotFound() {
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(null);

		final Throwable result = catchThrowable(() -> cut.getDTO(1L));

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Schüler zur ID 1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	void getLongId() throws ApiOperationException {
		final DTOSchueler schuelerDto = createDTOSchuelerSimple();

		final long result = cut.getLongId(schuelerDto);

		assertThat(result).isEqualTo(1L);
	}

	@Test
	void isOrtsteilGueltigWithOrtsteilIDIsNullAndWohnortIDIsNull() {
		final boolean result = cut.isOrtsteilGueltig(null, null);
		assertThat(result).isFalse();
	}

	@Test
	void isOrtsteilGueltigWithOrtsteilIDIsNull() {
		final boolean result = cut.isOrtsteilGueltig(null, 1L);
		assertThat(result).isFalse();
	}

	@Test
	void isOrtsteilGueltigWithWohnortIDIsNull() {
		final DTOOrtsteil ortsteilDto = new DTOOrtsteil(1L, "TestOrtsteil");
		ortsteilDto.Ort_ID = 2L;

		when(conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(ortsteilDto);

		final boolean result = cut.isOrtsteilGueltig(1L, null);
		assertThat(result).isFalse();
	}

	@Test
	void isOrtsteilGueltigWithWohnortIDIsDifferent() {
		final DTOOrtsteil ortsteilDto = new DTOOrtsteil(1L, "TestOrtsteil");
		ortsteilDto.Ort_ID = 3L;

		when(conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(ortsteilDto);

		final boolean result = cut.isOrtsteilGueltig(1L, 2L);
		assertThat(result).isFalse();
	}

	@Test
	void isOrtsteilGueltigWithOrtsteilIsNull() {
		when(conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(null);

		final boolean result = cut.isOrtsteilGueltig(1L, 2L);
		assertThat(result).isFalse();
	}

	@Test
	void isOrtsteilGueltigWithOrtsteilIsGueltig() {
		final DTOOrtsteil ortsteilDto = new DTOOrtsteil(1L, "TestOrtsteil");
		ortsteilDto.Ort_ID = 2L;

		when(conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(ortsteilDto);

		final boolean result = cut.isOrtsteilGueltig(1L, 2L);
		assertThat(result).isTrue();
	}

	@Test
	void setWohnortWithOrtsteilIsGueltig() throws ApiOperationException {
		cut = spy(cut);
		when(cut.isOrtsteilGueltig(3L, 2L)).thenReturn(true);
		final DTOSchueler schuelerDto = createDTOSchuelerSimple();

		cut.setWohnort(schuelerDto, 2L, 3L);

		assertThat(schuelerDto)
				.hasFieldOrPropertyWithValue("Ort_ID", 2L)
				.hasFieldOrPropertyWithValue("Ortsteil_ID", 3L);
	}

	@Test
	void setWohnortWithOrtsteilIsNotGueltig() throws ApiOperationException {
		cut = spy(cut);
		when(cut.isOrtsteilGueltig(3L, 2L)).thenReturn(false);
		final DTOSchueler schuelerDto = createDTOSchuelerSimple();

		cut.setWohnort(schuelerDto, 2L, 3L);

		assertThat(schuelerDto)
				.hasFieldOrPropertyWithValue("Ort_ID", 2L)
				.hasFieldOrPropertyWithValue("Ortsteil_ID", null);
	}

	@Test
	void setWohnortWithWohnortIDIsLowerThanZero() {
		final DTOSchueler schuelerDto = createDTOSchuelerSimple();

		final Throwable result = catchThrowable(() -> cut.setWohnort(schuelerDto, -2L, 3L));
		assertThat(result)
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}

	@Test
	void setWohnortWithOrtsteilIDIsLowerThanZero() {
		final DTOSchueler schuelerDto = createDTOSchuelerSimple();

		final Throwable result = catchThrowable(() -> cut.setWohnort(schuelerDto, 2L, -3L));
		assertThat(result)
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}


	@Test
	void mapSchuelerFotoWithIdenticalFoto() throws ApiOperationException {
		final DTOSchuelerFoto oldDtoSchuelerFoto = new DTOSchuelerFoto(1L);
		oldDtoSchuelerFoto.FotoBase64 = "Base64TestFoto";
		when(conn.queryByKey(DTOSchuelerFoto.class, 1L)).thenReturn(oldDtoSchuelerFoto);
		final DTOSchueler schuelerDto = createDTOSchuelerSimple();

		cut.mapSchuelerFoto(schuelerDto, "Base64TestFoto");

		verify(conn, times(0)).transactionPersist(any());
	}

	@Test
	void mapSchuelerFotoWithDifferentFoto() throws ApiOperationException {
		final DTOSchuelerFoto oldDtoSchuelerFoto = new DTOSchuelerFoto(1L);
		oldDtoSchuelerFoto.FotoBase64 = "Base64TestFoto";
		when(conn.queryByKey(DTOSchuelerFoto.class, 1L)).thenReturn(oldDtoSchuelerFoto);
		final DTOSchueler schuelerDto = createDTOSchuelerSimple();

		cut.mapSchuelerFoto(schuelerDto, "Base64TestFotoNew");

		verify(conn, times(1)).transactionPersist(
				argThat(o -> o.equals(oldDtoSchuelerFoto)
						&& (o instanceof final DTOSchuelerFoto foto)
						&& foto.FotoBase64.equals("Base64TestFotoNew")
						&& (foto.Schueler_ID == 1L)));
	}

	@Test
	void mapSchuelerFotoWithDifferentFotoAndNoOldFoto() throws ApiOperationException {
		when(conn.queryByKey(DTOSchuelerFoto.class, 1L)).thenReturn(null);
		final DTOSchueler schuelerDto = createDTOSchuelerSimple();

		cut.mapSchuelerFoto(schuelerDto, "Base64TestFotoNew");

		verify(conn, times(1)).transactionPersist(
				argThat(o -> (o instanceof final DTOSchuelerFoto foto)
						&& foto.FotoBase64.equals("Base64TestFotoNew")
						&& (foto.Schueler_ID == 1L)));
	}

	@Test
	void mapSchuelerFotoWithValueIsEmptyString() throws ApiOperationException {
		final DTOSchuelerFoto oldDtoSchuelerFoto = new DTOSchuelerFoto(1L);
		oldDtoSchuelerFoto.FotoBase64 = "Base64TestFoto";
		when(conn.queryByKey(DTOSchuelerFoto.class, 1L)).thenReturn(oldDtoSchuelerFoto);
		final DTOSchueler schuelerDto = createDTOSchuelerSimple();

		cut.mapSchuelerFoto(schuelerDto, "");

		verify(conn, times(1)).transactionPersist(
				argThat(o -> o.equals(oldDtoSchuelerFoto)
						&& (o instanceof final DTOSchuelerFoto foto)
						&& foto.FotoBase64.isEmpty()
						&& (foto.Schueler_ID == 1L)));
	}

	@Test
	void mapSchuelerFotoWithValueIsEmptyStringAndNoOldFoto() throws ApiOperationException {
		when(conn.queryByKey(DTOSchuelerFoto.class, 1L)).thenReturn(null);
		final DTOSchueler schuelerDto = createDTOSchuelerSimple();

		cut.mapSchuelerFoto(schuelerDto, "");

		verify(conn, times(1)).transactionPersist(
				argThat(o -> (o instanceof final DTOSchuelerFoto foto)
						&& foto.FotoBase64.isEmpty()
						&& (foto.Schueler_ID == 1L)));
	}

	@Test
	void mapSchuelerFotoWithValueIsNull() throws ApiOperationException {
		when(conn.queryByKey(DTOSchuelerFoto.class, 1L)).thenReturn(null);
		final DTOSchueler schuelerDto = createDTOSchuelerSimple();

		cut.mapSchuelerFoto(schuelerDto, null);

		verify(conn, times(0)).transactionPersist(any());
	}


	DTOSchueler createDTOSchuelerSimple() {
		return createDTOSchuelerSimpleWithId(1L);
	}

	DTOSchueler createDTOSchuelerSimpleWithId(final Long id) {
		return new DTOSchueler(id, "TestGUID", false);
	}

	DTOSchueler createDTOSchueler() {
		return createDTOSchuelerWithId(1L);
	}

	DTOSchueler createDTOSchuelerWithId(final Long id) {
		final DTOSchueler dto = new DTOSchueler(id, "TestGUID", false);
		dto.Schuljahresabschnitts_ID = 99L;
		dto.Nachname = "Mustermann";
		dto.Vorname = "Max";
		dto.AlleVornamen = "Moritz";
		dto.Geschlecht = Geschlecht.M;
		dto.Geburtsdatum = "12-04-1990";
		dto.Geburtsort = "Musterhausen";
		dto.Geburtsname = "Mustermann";
		// Wohnort und Kontaktdaten
		dto.Strassenname = "Musterstrasse";
		dto.HausNr = "1";
		dto.HausNrZusatz = "a";
		dto.Ort_ID = 33L;
		dto.Ortsteil_ID = 34L;
		dto.Telefon = "0123456789";
		dto.Fax = "0123456789";
		dto.Email = "max.mustermann@muster.com";
		dto.SchulEmail = "max.mustermann@musterschule.com";
		// Daten zur Staatsangehörigkeit und zur Religion
		dto.StaatKrz = Nationalitaeten.getDEU();
		dto.StaatKrz2 = Nationalitaeten.getByISO3("ALB");
		dto.Religion_ID = Religion.KR.daten(2025).id;
		dto.KonfDruck = true;
		dto.Religionsabmeldung = "12-04-1999";
		dto.Religionsanmeldung = "12-04-1995";
		// Daten zum Migrationshintergrund
		dto.Migrationshintergrund = true;
		dto.JahrZuzug = 1991;
		dto.GeburtslandSchueler = Nationalitaeten.getDEU();
		dto.VerkehrsspracheFamilie = Verkehrssprache.DEU;
		dto.GeburtslandVater = Nationalitaeten.getDEU();
		dto.GeburtslandMutter = Nationalitaeten.getByISO3("ALB");
		// Statusdaten
		dto.idStatus = 2;
		dto.Duplikat = true;
		dto.ExterneSchulNr = "1234";
		dto.Fahrschueler_ID = 1337L;
		dto.Haltestelle_ID = 1338L;
		dto.AnmeldeDatum = "01-01-1997";
		dto.Aufnahmedatum = "01-01-1998";
		dto.Volljaehrig = true;
		dto.KeineAuskunft = true;
		dto.SchulpflichtErf = true;
		dto.BerufsschulpflErf = true;
		dto.MasernImpfnachweis = true;
		dto.Bafoeg = true;
		dto.MeisterBafoeg = true;

		return dto;
	}

}
