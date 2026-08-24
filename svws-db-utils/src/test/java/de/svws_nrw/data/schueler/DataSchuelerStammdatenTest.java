package de.svws_nrw.data.schueler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.Year;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import de.svws_nrw.db.dto.current.schild.katalog.DTOReligion;
import de.svws_nrw.service.schueler.foto.SchuelerFoto;
import de.svws_nrw.service.schueler.foto.SchuelerFotoService;
import de.svws_nrw.service.schueler.foto.SchuelerFotoServiceFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.asd.types.schule.Verkehrssprache;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.data.util.TestUtils;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

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
		when(this.conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(schuelerDto);

		final var schuelerFotoService = mock(SchuelerFotoService.class);
		final var schuelerFotoServiceFactory = mock(SchuelerFotoServiceFactory.class);
		when(schuelerFotoServiceFactory.getSchuelerFotoService()).thenReturn(schuelerFotoService);
		when(schuelerFotoService.findByIdSchueler(1L)).thenReturn(Optional.of(new SchuelerFoto(1L, "TestBase64Foto")));

		try (MockedStatic<SchuelerFotoServiceFactory> factoryMock = mockStatic(SchuelerFotoServiceFactory.class)) {
			factoryMock.when(SchuelerFotoServiceFactory::getNewInstance).thenReturn(schuelerFotoServiceFactory);

			final SchuelerStammdaten result = this.cut.getById(1L);

			assertThat(result).isNotNull()
					.hasFieldOrPropertyWithValue("foto", "TestBase64Foto")
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
					.hasFieldOrPropertyWithValue("idStaatsangehoerigkeit", schuelerDto.StaatKrz.historie().getLast().id)
					.hasFieldOrPropertyWithValue("idStaatsangehoerigkeit2", schuelerDto.StaatKrz2.historie().getLast().id)
					.hasFieldOrPropertyWithValue("religionID", schuelerDto.Religion_ID)
					.hasFieldOrPropertyWithValue("druckeKonfessionAufZeugnisse", schuelerDto.KonfDruck)
					.hasFieldOrPropertyWithValue("religionabmeldung", schuelerDto.Religionsabmeldung)
					.hasFieldOrPropertyWithValue("religionanmeldung", schuelerDto.Religionsanmeldung)
					.hasFieldOrPropertyWithValue("hatMigrationshintergrund", schuelerDto.Migrationshintergrund)
					.hasFieldOrPropertyWithValue("zuzugsjahr", schuelerDto.JahrZuzug)
					.hasFieldOrPropertyWithValue("idGeburtsland", schuelerDto.GeburtslandSchueler.historie().getLast().id)
					.hasFieldOrPropertyWithValue("idVerkehrspracheFamilie", schuelerDto.VerkehrsspracheFamilie.historie().getLast().id)
					.hasFieldOrPropertyWithValue("idGeburtslandVater", schuelerDto.GeburtslandVater.historie().getLast().id)
					.hasFieldOrPropertyWithValue("idGeburtslandMutter", schuelerDto.GeburtslandMutter.historie().getLast().id)
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
					.hasFieldOrPropertyWithValue("erhaeltMeisterBAFOEG", schuelerDto.MeisterBafoeg)
					.hasFieldOrPropertyWithValue("beruf", schuelerDto.Beruf);
		}
	}

	private static Stream<Arguments> mapAttribute() {
		return Stream.of(
				arguments("id", 1L, 1L, null),
				arguments("id", 2L, null, new ApiOperationException(Response.Status.BAD_REQUEST, "Die ID darf nicht verändert werden.")),
				arguments("nachname", "Musterfrau", "Musterfrau", null),
				arguments("nachname", null, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut nachname: Der Wert null ist nicht erlaubt.")),
				arguments("nachname", "", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut nachname: Ein leerer String ist hier nicht erlaubt.")),
				arguments("nachname", RandomStringUtils.insecure().nextAscii(121), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut nachname: Die Länge des Strings ist auf 120 Zeichen limitiert.")),
				arguments("vorname", "Maria", "Maria", null),
				arguments("vorname", null, null, new ApiOperationException(Response.Status.BAD_REQUEST, "Attribut vorname: Der Wert null ist nicht erlaubt.")),
				arguments("vorname", "", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut vorname: Ein leerer String ist hier nicht erlaubt.")),
				arguments("vorname", RandomStringUtils.insecure().nextAscii(81), null,
						new ApiOperationException(Response.Status.BAD_REQUEST, "Attribut vorname: "
								+ "Die Länge des Strings ist auf 80 Zeichen limitiert.")),
				arguments("alleVornamen", "Maria", "Maria", null),
				arguments("alleVornamen", null, null, null),
				arguments("alleVornamen", "", "", null),
				arguments("alleVornamen", RandomStringUtils.insecure().nextAscii(256), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut alleVornamen: Die Länge des Strings ist auf 255 Zeichen limitiert.")),
				arguments("geschlecht", Geschlecht.X.id, Geschlecht.X.id, null),
				arguments("geschlecht", 999, null, new ApiOperationException(Response.Status.BAD_REQUEST, "Geschlecht darf nicht null sein.")),
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
				arguments("geburtsort", RandomStringUtils.insecure().nextAscii(101), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut geburtsort: Die Länge des Strings ist auf 100 Zeichen limitiert.")),
				arguments("geburtsname", "Musterfrau", "Musterfrau", null),
				arguments("geburtsname", null, null, null),
				arguments("geburtsname", "", "", null),
				arguments("geburtsname", RandomStringUtils.insecure().nextAscii(121), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut geburtsname: Die Länge des Strings ist auf 120 Zeichen limitiert.")),
				arguments("strassenname", "Musterfrau", "Musterfrau", null),
				arguments("strassenname", null, null, null),
				arguments("strassenname", "", "", null),
				arguments("strassenname", RandomStringUtils.insecure().nextAscii(56), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut strassenname: Die Länge des Strings ist auf 55 Zeichen limitiert.")),
				arguments("hausnummer", "2", "2", null),
				arguments("hausnummer", null, null, null),
				arguments("hausnummer", "", "", null),
				arguments("hausnummer", RandomStringUtils.insecure().nextAscii(11), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut hausnummer: Die Länge des Strings ist auf 10 Zeichen limitiert.")),
				arguments("hausnummerZusatz", "b", "b", null),
				arguments("hausnummerZusatz", null, null, null),
				arguments("hausnummerZusatz", "", "", null),
				arguments("hausnummerZusatz", RandomStringUtils.insecure().nextAscii(31), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut hausnummerZusatz: Die Länge des Strings ist auf 30 Zeichen limitiert.")),
				arguments("telefon", "12345", "12345", null),
				arguments("telefon", null, null, null),
				arguments("telefon", "", "", null),
				arguments("telefon", RandomStringUtils.insecure().nextAscii(21), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut telefon: Die Länge des Strings ist auf 20 Zeichen limitiert.")),
				arguments("telefonMobil", "12345", "12345", null),
				arguments("telefonMobil", null, null, null),
				arguments("telefonMobil", "", "", null),
				arguments("telefonMobil", RandomStringUtils.insecure().nextAscii(21), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut telefonMobil: Die Länge des Strings ist auf 20 Zeichen limitiert.")),
				arguments("emailPrivat", "abc@abc.de", "abc@abc.de", null),
				arguments("emailPrivat", null, null, null),
				arguments("emailPrivat", "", "", null),
				arguments("emailPrivat", RandomStringUtils.insecure().nextAscii(101), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut emailPrivat: Die Länge des Strings ist auf 100 Zeichen limitiert.")),
				arguments("emailSchule", "abc@abc.de", "abc@abc.de", null),
				arguments("emailSchule", null, null, null),
				arguments("emailSchule", "", "", null),
				arguments("emailSchule", RandomStringUtils.insecure().nextAscii(101), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut emailSchule: Die Länge des Strings ist auf 100 Zeichen limitiert.")),
				arguments("idStaatsangehoerigkeit", 65070071L, Nationalitaeten.data().getWertByBezeichner("AFG"), null),
				arguments("idStaatsangehoerigkeit", null, null, null),
				arguments("idStaatsangehoerigkeit", 999999L, null,
						new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde keine Nationalität zum Wert 999999 gefunden.")),
				arguments("idStaatsangehoerigkeit2", 65070071L, Nationalitaeten.data().getWertByBezeichner("AFG"), null),
				arguments("idStaatsangehoerigkeit2", null, null, null),
				arguments("idStaatsangehoerigkeit2", 999999L, null,
						new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde keine Nationalität zum Wert 999999 gefunden.")),
				arguments("religionID", 123L, 123L, null),
				arguments("religionID", null, null, null),
				arguments("religionID", -2L, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut religionID: Fehler beim Konvertieren: Der Zahlwert liegt außerhalb des geforderten Bereichs.")),
				arguments("religionID", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut religionID: Fehler beim Konvertieren zu Long: Das Objekt ist keine Zahl.")),
				arguments("religionID", 0L, null, new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde keine Konfession zur ID 0 gefunden.")),
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
				arguments("zuzugsjahr", Year.now().getValue(), Year.now().getValue(), null),
				arguments("zuzugsjahr", 1899, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut zuzugsjahr: Fehler beim Konvertieren: Der Zahlwert liegt außerhalb des geforderten Bereichs.")),
				arguments("zuzugsjahr", Year.now().plusYears(1).getValue(), null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut zuzugsjahr: Fehler beim Konvertieren: Der Zahlwert liegt außerhalb des geforderten Bereichs.")),
				arguments("zuzugsjahr", null, null, null),
				arguments("zuzugsjahr", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut zuzugsjahr: Fehler beim Konvertieren zu Integer: Das Objekt ist keine Zahl.")),
				arguments("idVerkehrspracheFamilie", 3000, Verkehrssprache.getByIsoKuerzel("abk"), null),
				arguments("idVerkehrspracheFamilie", null, null, null),
				arguments("idVerkehrspracheFamilie", 999999L, null,
						new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde keine Verkehrssprache zum Wert 999999 gefunden.")),
				arguments("idGeburtsland", 65070071L, Nationalitaeten.data().getWertByBezeichner("AFG"), null),
				arguments("idGeburtsland", null, null, null),
				arguments("idGeburtsland", 999999L, null,
						new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde keine Nationalität zum Wert 999999 gefunden.")),
				arguments("idGeburtslandVater", 65070071L, Nationalitaeten.data().getWertByBezeichner("AFG"), null),
				arguments("idGeburtslandVater", null, null, null),
				arguments("idGeburtslandVater", 999999L, null,
						new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde keine Nationalität zum Wert 999999 gefunden.")),
				arguments("idGeburtslandMutter", 65070071L, Nationalitaeten.data().getWertByBezeichner("AFG"), null),
				arguments("idGeburtslandMutter", null, null, null),
				arguments("idGeburtslandMutter", 999999L, null,
						new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde keine Nationalität zum Wert 999999 gefunden.")),
				arguments("status", 3, 3, null),
				arguments("status", 999, null, new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde kein SchuelerStatus zum Wert 999 gefunden.")),
				arguments("status", null, null, new ApiOperationException(Response.Status.BAD_REQUEST, "Attribut status: Der Wert null ist nicht erlaubt")),
				arguments("status", "abc", null,
						new ApiOperationException(Response.Status.BAD_REQUEST, "Attribut status: Fehler beim Konvertieren zu Integer")),
				arguments("externeSchulNr", "123456", "123456", null),
				arguments("externeSchulNr", null, null, null),
				arguments("externeSchulNr", "", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Die Anzahl der Ziffern einer Schulnummer aus NRW muss 6 betragen.")),
				arguments("externeSchulNr", "1234567", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut externeSchulNr: Die Länge des Strings ist auf 6 Zeichen limitiert.")),
				arguments("externeSchulNr", "123", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Die Anzahl der Ziffern einer Schulnummer aus NRW muss 6 betragen.")),
				arguments("idSchuelerausweis", null, null, null),
				arguments("idSchuelerausweis", "", "", null),
				arguments("idSchuelerausweis", "1234abc", "1234abc", null),
				arguments("idSchuelerausweis", "12345abcde12345abcde12345abcde1", null, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Die Länge des Strings ist auf 30 Zeichen limitiert.")),
				arguments("fahrschuelerArtID", 22L, 22L, null),
				arguments("fahrschuelerArtID", null, null, null),
				arguments("fahrschuelerArtID", -2L, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut fahrschuelerArtID: Fehler beim Konvertieren: Der Zahlwert liegt außerhalb des geforderten Bereichs.")),
				arguments("fahrschuelerArtID", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut fahrschuelerArtID: Fehler beim Konvertieren zu Long: Das Objekt ist keine Zahl.")),
				arguments("fahrschuelerArtID", 0L, null,
						new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde keine Fahrschülerart zur ID 0 gefunden.")),
				arguments("haltestelleID", 33L, 33L, null),
				arguments("haltestelleID", null, null, null),
				arguments("haltestelleID", -2L, null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut haltestelleID: Fehler beim Konvertieren: Der Zahlwert liegt außerhalb des geforderten Bereichs.")),
				arguments("haltestelleID", "abc", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Attribut haltestelleID: Fehler beim Konvertieren zu Long: Das Objekt ist keine Zahl.")),
				arguments("haltestelleID", 0L, null, new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde keine Haltestelle zur ID 0 gefunden.")),
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
				arguments("beruf", "Tischler", "Tischler", null, null),
				arguments("beruf", null, null, null, null),
				arguments("beruf", RandomStringUtils.insecure().nextAscii(101), null,
						new ApiOperationException(Response.Status.BAD_REQUEST, "Attribut beruf: Die Länge des Strings ist auf 100 Zeichen limitiert.")),
				arguments("unknown", "oh oh ! das wollen wir auf keinen Fall!", null, new ApiOperationException(Response.Status.BAD_REQUEST,
						"Das Patchen des Attributes unknown ist nicht implementiert."))
		);
	}

	@ParameterizedTest
	@MethodSource
	void mapAttribute(final String attributeName, final Object givenValue, final Object expectedValue, final ApiOperationException expectedException) {
		final DTOSchueler schuelerDto = createDTOSchueler();

		mapAttributeStubbings(attributeName, expectedValue);

		final Throwable throwable = catchThrowable(() -> this.cut.mapAttribute(schuelerDto, attributeName, givenValue, Collections.emptyMap()));
		if (expectedException != null) {
			assertThat(throwable).isInstanceOf(ApiOperationException.class)
					.hasMessage(expectedException.getMessage())
					.hasFieldOrPropertyWithValue("status", expectedException.getStatus());

		} else {
			switch (attributeName) {
				case "id" -> assertThat(schuelerDto.ID).isEqualTo(expectedValue);
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
				case "telefon" -> assertThat(schuelerDto.Telefon).isEqualTo(expectedValue);
				case "telefonMobil" -> assertThat(schuelerDto.Fax).isEqualTo(expectedValue);
				case "emailPrivat" -> assertThat(schuelerDto.Email).isEqualTo(expectedValue);
				case "emailSchule" -> assertThat(schuelerDto.SchulEmail).isEqualTo(expectedValue);
				// Daten zur Staatsangehörigkeit und zur Religion
				case "idStaatsangehoerigkeit" -> assertThat(schuelerDto.StaatKrz).isEqualTo(expectedValue);
				case "idStaatsangehoerigkeit2" -> assertThat(schuelerDto.StaatKrz2).isEqualTo(expectedValue);
				case "religionID" -> assertThat(schuelerDto.Religion_ID).isEqualTo(expectedValue);
				case "druckeKonfessionAufZeugnisse" -> assertThat(schuelerDto.KonfDruck).isEqualTo(expectedValue);
				case "religionabmeldung" -> assertThat(schuelerDto.Religionsabmeldung).isEqualTo(expectedValue);
				case "religionanmeldung" -> assertThat(schuelerDto.Religionsanmeldung).isEqualTo(expectedValue);
				// Daten zum Migrationshintergrund
				case "hatMigrationshintergrund" -> assertThat(schuelerDto.Migrationshintergrund).isEqualTo(expectedValue);
				case "zuzugsjahr" -> assertThat(schuelerDto.JahrZuzug).isEqualTo(expectedValue);
				case "idVerkehrspracheFamilie" -> assertThat(schuelerDto.VerkehrsspracheFamilie).isEqualTo(expectedValue);
				case "idGeburtsland" -> assertThat(schuelerDto.GeburtslandSchueler).isEqualTo(expectedValue);
				case "idGeburtslandVater" -> assertThat(schuelerDto.GeburtslandVater).isEqualTo(expectedValue);
				case "idGeburtslandMutter" -> assertThat(schuelerDto.GeburtslandMutter).isEqualTo(expectedValue);
				// Statusdaten
				case "status" -> assertThat(schuelerDto.idStatus).isEqualTo(expectedValue);
				case "externeSchulNr" -> assertThat(schuelerDto.ExterneSchulNr).isEqualTo(expectedValue);
				case "idSchuelerausweis" -> assertThat(schuelerDto.Ausweisnummer).isEqualTo(expectedValue);
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
				case "beruf" -> assertThat(schuelerDto.Beruf).isEqualTo(expectedValue);
				default -> assertThat(throwable)
						.isInstanceOf(ApiOperationException.class)
						.hasMessageStartingWith("Das Patchen des Attributes %s ist nicht implementiert.".formatted(attributeName))
						.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			}
		}
	}

	void mapAttributeStubbings(final String attributeName, final Object expectedValue) {
		final DTOReligion konfessionDto = new DTOReligion(123L, "TestKonfession");
		final DTOFahrschuelerart fahrschuelerartDto = new DTOFahrschuelerart(22L, "TestFahrschuelerArt");
		final DTOHaltestellen haltestelleDto = new DTOHaltestellen(33L, "TestHaltestelle");

		if (attributeName.equals("religionID") && Long.valueOf(123L).equals(expectedValue)) {
			when(this.conn.queryByKey(DTOReligion.class, 123L)).thenReturn(konfessionDto);
		} else if (attributeName.equals("status") && (expectedValue != null)) {
			final Benutzer benutzer = mock(Benutzer.class);
			final Schuljahresabschnitt schuljahresabschnitt = new Schuljahresabschnitt();
			schuljahresabschnitt.schuljahr = 2000;
			when(benutzer.schuleGetSchuljahresabschnittByIdOrDefault(99L)).thenReturn(schuljahresabschnitt);
			when(this.conn.getUser()).thenReturn(benutzer);
		} else if (attributeName.equals("fahrschuelerArtID") && Long.valueOf(22L).equals(expectedValue)) {
			when(this.conn.queryByKey(DTOFahrschuelerart.class, 22L)).thenReturn(fahrschuelerartDto);
		} else if (attributeName.equals("haltestelleID") && Long.valueOf(33L).equals(expectedValue)) {
			when(this.conn.queryByKey(DTOHaltestellen.class, 33L)).thenReturn(haltestelleDto);
		}
	}

	@Test
	void getListByIdsAsResponse() throws ApiOperationException {
		final List<Long> ids = List.of(1L, 2L);
		final DTOSchueler dtoSchueler1 = createDTOSchuelerWithId(1L);
		final DTOSchueler dtoSchueler2 = createDTOSchuelerWithId(2L);
		when(this.conn.queryByKeyList(DTOSchueler.class, ids)).thenReturn(List.of(dtoSchueler1, dtoSchueler2));

		final var schuelerFotoService = mock(SchuelerFotoService.class);
		final var schuelerFotoServiceFactory = mock(SchuelerFotoServiceFactory.class);
		when(schuelerFotoServiceFactory.getSchuelerFotoService()).thenReturn(schuelerFotoService);
		when(schuelerFotoService.getBySchuelerIds(ids)).thenReturn(List.of());

		try (MockedStatic<SchuelerFotoServiceFactory> factoryMock = mockStatic(SchuelerFotoServiceFactory.class)) {
			factoryMock.when(SchuelerFotoServiceFactory::getNewInstance).thenReturn(schuelerFotoServiceFactory);

			final Response result = this.cut.getListByIdsAsResponse(ids);

			assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
			assertThat(result.getEntity()).asInstanceOf(InstanceOfAssertFactories.LIST)
					.hasSize(2).extracting("id").containsExactly(1L, 2L);
		}
	}

	@Test
	void getDTOList() throws ApiOperationException {
		final List<Long> ids = List.of(1L, 2L);
		final DTOSchueler dtoSchueler1 = createDTOSchuelerSimpleWithId(1L);
		final DTOSchueler dtoSchueler2 = createDTOSchuelerSimpleWithId(2L);
		when(this.conn.queryByKeyList(DTOSchueler.class, ids)).thenReturn(List.of(dtoSchueler1, dtoSchueler2));

		final List<DTOSchueler> result = this.cut.getDTOList(ids);

		assertThat(result).hasSize(2).contains(dtoSchueler1, dtoSchueler2);
	}

	@Test
	void getDTOListWithIdsIsNull() {
		assertThatThrownBy(() -> this.cut.getDTOList(null))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Liste der IDs für die Schüler darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	void getDTO() throws ApiOperationException {
		final DTOSchueler dtoSchueler = createDTOSchueler();
		when(this.conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(dtoSchueler);

		final DTOSchueler result = this.cut.getDTO(1L);

		assertThat(result).isEqualTo(dtoSchueler);
	}

	@Test
	void getDTOWithIdIsNull() {
		assertThatThrownBy(() -> this.cut.getDTO(null))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID für den Schüler darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	void getDTOWithIdNotFound() {
		when(this.conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(null);

		assertThatThrownBy(() -> this.cut.getDTO(1L))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Schüler zur ID 1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	void getLongId() throws ApiOperationException {
		final DTOSchueler schuelerDto = createDTOSchuelerSimple();

		final long result = this.cut.getLongId(schuelerDto);

		assertThat(result).isEqualTo(1L);
	}

	private static Stream<Arguments> patchWohnortAndOrtsteilArguments() {
		return Stream.of(
				Arguments.of(Map.of("wohnortID", 10L, "ortsteilID", 11L), 10L, 11L, null),
				Arguments.of(Map.of("wohnortID", 10L), null, null,
						new ApiOperationException(Response.Status.BAD_REQUEST,
								"Der Patch enthält keine ortsteilID. Ort und Ortsteil können nur zusammen geändert werden.")),
				Arguments.of(Map.of("ortsteilID", 11L), null, null,
						new ApiOperationException(Response.Status.BAD_REQUEST,
								"Der Patch enthält keine wohnortID. Ort und Ortsteil können nur zusammen geändert werden.")),
				Arguments.of(Map.of("wohnortID", 10L, "ortsteilID", 21L), null, null,
						new ApiOperationException(Response.Status.BAD_REQUEST,
								"Die Kombination von Ort und Ortsteil ist nicht zulässig. Der Ortsteil ist dem Ort nicht zugeordnet.")),
				Arguments.of(Map.of("wohnortID", 20L, "ortsteilID", 11L), null, null,
						new ApiOperationException(Response.Status.BAD_REQUEST,
								"Die Kombination von Ort und Ortsteil ist nicht zulässig. Der Ortsteil ist dem Ort nicht zugeordnet.")),
				Arguments.of(
						new HashMap<>() {
							{
								put("wohnortID", null);
								put("ortsteilID", 11L);
							}
						},
						null, null,
						new ApiOperationException(Response.Status.BAD_REQUEST,
								"Die Kombination von Ort und Ortsteil ist nicht zulässig. Der Ortsteil ist dem Ort nicht zugeordnet.")),
				Arguments.of(
						new HashMap<>() {
							{
								put("wohnortID", 10L);
								put("ortsteilID", null);
							}
						},
						10L, null, null),
				Arguments.of(
						new HashMap<>() {
							{
								put("wohnortID", null);
								put("ortsteilID", null);
							}
						},
						null, null, null)
		);
	}

	@ParameterizedTest
	@MethodSource("patchWohnortAndOrtsteilArguments")
	void patchWohnortAndOrtsteil(final Map<String, Object> givenPatchMap, final Long expectedOrtID, final Long expectedOrtsteilID,
			final ApiOperationException expectedException) {
		final var schuelerDto = createDTOSchuelerWithId(1L);
		final var ortsteilDto1 = new DTOOrtsteil(11L, "Ortsteil1");
		ortsteilDto1.idOrt = 10L;
		final var ortsteilDto2 = new DTOOrtsteil(21L, "Ortsteil2");
		ortsteilDto2.idOrt = 20L;


		lenient().when(this.conn.queryByKey(DTOSchueler.class, schuelerDto.ID)).thenReturn(schuelerDto);
		lenient().when(this.conn.transactionPersist(any())).thenReturn(true);
		lenient().doNothing().when(this.conn).transactionFlush();
		lenient().when(this.conn.existsBy(DTOOrt.QUERY_BY_ID, DTOOrt.class, 10L)).thenReturn(true);
		lenient().when(this.conn.existsBy(DTOOrt.QUERY_BY_ID, DTOOrt.class, 20L)).thenReturn(true);
		lenient().when(this.conn.queryByKey(DTOOrtsteil.class, 11L)).thenReturn(ortsteilDto1);
		lenient().when(this.conn.queryByKey(DTOOrtsteil.class, 21L)).thenReturn(ortsteilDto2);

		final Throwable throwable = catchThrowable(() -> this.cut.patchAsResponse(schuelerDto.ID, TestUtils.fromObject(givenPatchMap)));
		if ((expectedException != null) || (throwable != null)) {
			Assertions.assertNotNull(expectedException, "Es wurde eine Exception geworfen, obwohl keine Exception erwartet wurde.");
			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage(expectedException.getMessage())
					.hasFieldOrPropertyWithValue("status", expectedException.getStatus());
		} else {
			assertThat(schuelerDto)
					.extracting("Ort_ID", "Ortsteil_ID")
					.containsExactly(expectedOrtID, expectedOrtsteilID);
		}
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
		dto.VerkehrsspracheFamilie = Verkehrssprache.getByIsoKuerzel("deu");
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
		dto.Beruf = "Tischler";

		return dto;
	}

}
