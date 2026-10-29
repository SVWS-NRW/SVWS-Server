package de.svws_nrw.mapper.schueler;

import java.util.HashMap;
import java.util.Map;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SchuelerListeEintragMapperTest {

	private static final int SCHULJAHR = 2025;
	private static final long ID_SCHUELER = 4711L;
	private static final long ID_ABSCHNITT_SCHUELER = 14L;
	private static final long ID_ABSCHNITT_LERNABSCHNITT = 15L;
	private static final long ID_KLASSE = 47L;
	private static final long ID_JAHRGANG = 32L;

	private final SchuelerListeEintragMapper mapper = SchuelerListeEintragMapper.INSTANCE;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	private static DTOSchueler createSchueler() {
		final var schueler = new DTOSchueler(ID_SCHUELER, "GU-4711", false);
		schueler.Nachname = "Mustermann";
		schueler.Vorname = "Max";
		schueler.Geschlecht = Geschlecht.W;
		schueler.Geburtsdatum = "2010-05-17";
		schueler.Schuljahresabschnitts_ID = ID_ABSCHNITT_SCHUELER;
		schueler.idStatus = 8;
		schueler.Duplikat = true;
		schueler.ExterneSchulNr = "123456";
		return schueler;
	}

	private static DTOSchuelerLernabschnittsdaten createLernabschnitt() {
		final var abschnitt = new DTOSchuelerLernabschnittsdaten(1L, ID_SCHUELER, ID_ABSCHNITT_LERNABSCHNITT, false, false);
		abschnitt.Klassen_ID = ID_KLASSE;
		abschnitt.Jahrgang_ID = ID_JAHRGANG;
		abschnitt.EPJahre = 2;
		abschnitt.Schulgliederung = null;
		return abschnitt;
	}

	private static Map<Long, DTOJahrgang> createJahrgaenge() {
		final var jahrgang = new DTOJahrgang(ID_JAHRGANG);
		jahrgang.ASDJahrgang = "09";
		final var map = new HashMap<Long, DTOJahrgang>();
		map.put(ID_JAHRGANG, jahrgang);
		return map;
	}

	@Test
	@DisplayName("Ohne Lernabschnitt: Standardfelder werden gemappt und die Abschnittsfelder auf ihre Vorgabewerte gesetzt")
	void ohneLernabschnitt_setztVorgabewerte() {
		final var result = mapper.toSchuelerListeEintrag(createSchueler(), SCHULJAHR, null, null, null);

		assertThat(result.id).isEqualTo(ID_SCHUELER);
		assertThat(result.nachname).isEqualTo("Mustermann");
		assertThat(result.vorname).isEqualTo("Max");
		assertThat(result.geschlecht).isEqualTo(Geschlecht.W.kuerzel);
		assertThat(result.geburtsdatum).isEqualTo("2010-05-17");
		assertThat(result.idSchuljahresabschnittSchueler).isEqualTo(ID_ABSCHNITT_SCHUELER);
		assertThat(result.status).isEqualTo(8);
		assertThat(result.istDuplikat).isTrue();
		assertThat(result.externeSchulNr).isEqualTo("123456");
		assertThat(result.idSchuljahresabschnitt).isEqualTo(ID_ABSCHNITT_SCHUELER);
		assertThat(result.idKlasse).isEqualTo(-1L);
		assertThat(result.idJahrgang).isEqualTo(-1L);
		assertThat(result.jahrgang).isEmpty();
		assertThat(result.epJahre).isNull();
		assertThat(result.idSchulgliederung).isEqualTo(-1L);
		assertThat(result.abiturjahrgang).isNull();
		assertThat(result.kurse).isEmpty();
	}

	@Test
	@DisplayName("Nachname, Vorname und Geschlecht null werden zu leeren Strings")
	void namenUndGeschlechtNull_werdenLeer() {
		final var schueler = createSchueler();
		schueler.Nachname = null;
		schueler.Vorname = null;
		schueler.Geschlecht = null;

		final var result = mapper.toSchuelerListeEintrag(schueler, SCHULJAHR, null, null, null);

		assertThat(result.nachname).isEmpty();
		assertThat(result.vorname).isEmpty();
		assertThat(result.geschlecht).isEmpty();
	}

	@Test
	@DisplayName("Mit Lernabschnitt: Klasse, Jahrgang, EP-Jahre und Schuljahresabschnitt kommen aus dem Lernabschnitt")
	void mitLernabschnitt_mapptAbschnittsfelder() {
		final var result = mapper.toSchuelerListeEintrag(createSchueler(), SCHULJAHR, createLernabschnitt(), createJahrgaenge(), Schulform.GY);

		assertThat(result.idSchuljahresabschnitt).isEqualTo(ID_ABSCHNITT_LERNABSCHNITT);
		assertThat(result.idSchuljahresabschnittSchueler).isEqualTo(ID_ABSCHNITT_SCHUELER);
		assertThat(result.idKlasse).isEqualTo(ID_KLASSE);
		assertThat(result.idJahrgang).isEqualTo(ID_JAHRGANG);
		assertThat(result.jahrgang).isEqualTo("09");
		assertThat(result.epJahre).isEqualTo(2);
	}

	@Test
	@DisplayName("Mit Lernabschnitt ohne Klasse: Die Klassen-ID wird zu -1")
	void ohneKlasse_setztMinusEins() {
		final var abschnitt = createLernabschnitt();
		abschnitt.Klassen_ID = null;

		final var result = mapper.toSchuelerListeEintrag(createSchueler(), SCHULJAHR, abschnitt, createJahrgaenge(), Schulform.GY);

		assertThat(result.idKlasse).isEqualTo(-1L);
	}

	@Test
	@DisplayName("Jahrgang nicht in der Map: Jahrgang-ID wird -1 und Jahrgang leer")
	void jahrgangNichtInMap_setztVorgabewerte() {
		final var result = mapper.toSchuelerListeEintrag(createSchueler(), SCHULJAHR, createLernabschnitt(), new HashMap<>(), Schulform.GY);

		assertThat(result.idJahrgang).isEqualTo(-1L);
		assertThat(result.jahrgang).isEmpty();
	}

	@Test
	@DisplayName("Jahrgang ohne ASD-Jahrgang: Jahrgang ist leer, die ID bleibt gesetzt")
	void jahrgangOhneAsdJahrgang_setztLeerenJahrgang() {
		final var jahrgaenge = createJahrgaenge();
		jahrgaenge.get(ID_JAHRGANG).ASDJahrgang = null;

		final var result = mapper.toSchuelerListeEintrag(createSchueler(), SCHULJAHR, createLernabschnitt(), jahrgaenge, Schulform.GY);

		assertThat(result.idJahrgang).isEqualTo(ID_JAHRGANG);
		assertThat(result.jahrgang).isEmpty();
	}

	@Test
	@DisplayName("Schulgliederung gesetzt: Die ID des Katalogeintrags wird verwendet")
	void schulgliederungGesetzt_verwendetKatalogeintrag() {
		final var katalogEintrag = Schulgliederung.GY9.daten(SCHULJAHR);
		final var abschnitt = createLernabschnitt();
		abschnitt.Schulgliederung = katalogEintrag.schluessel;

		final var result = mapper.toSchuelerListeEintrag(createSchueler(), SCHULJAHR, abschnitt, createJahrgaenge(), Schulform.GY);

		assertThat(result.idSchulgliederung).isEqualTo(katalogEintrag.id);
	}

	@Test
	@DisplayName("Schulgliederung nicht gesetzt: Die Standard-Schulgliederung der Schulform wird verwendet")
	void schulgliederungNichtGesetzt_verwendetDefaultDerSchulform() {
		final var result = mapper.toSchuelerListeEintrag(createSchueler(), SCHULJAHR, createLernabschnitt(), createJahrgaenge(), Schulform.GY);

		assertThat(result.idSchulgliederung).isEqualTo(Schulgliederung.DEFAULT.daten(SCHULJAHR).id);
	}

	@Test
	@DisplayName("Schulgliederung nicht gesetzt und Schulform null: Die ID wird -1")
	void schulgliederungNichtGesetztSchulformNull_setztMinusEins() {
		final var result = mapper.toSchuelerListeEintrag(createSchueler(), SCHULJAHR, createLernabschnitt(), createJahrgaenge(), null);

		assertThat(result.idSchulgliederung).isEqualTo(-1L);
	}

	@Test
	@DisplayName("Schulgliederung nicht auflösbar: Die ID wird -1 (nicht null)")
	void schulgliederungNichtAufloesbar_setztMinusEins() {
		final var abschnitt = createLernabschnitt();
		abschnitt.Schulgliederung = "gibt-es-nicht";

		final var result = mapper.toSchuelerListeEintrag(createSchueler(), SCHULJAHR, abschnitt, createJahrgaenge(), Schulform.GY);

		assertThat(result.idSchulgliederung).isEqualTo(-1L);
	}

	@Test
	@DisplayName("Status null führt zu einer ApiOperationException mit Status 500")
	void statusNull_wirftApiOperationException() {
		final var schueler = createSchueler();
		schueler.idStatus = null;

		assertThatThrownBy(() -> mapper.toSchuelerListeEintrag(schueler, SCHULJAHR, null, null, null))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> {
					assertThat(e.getStatus()).isEqualTo(Status.INTERNAL_SERVER_ERROR);
					assertThat(e.getBody()).isEqualTo("Der Schüler mit der ID 4711 hat keinen Status.");
				});
	}

	@Test
	@DisplayName("Duplikat null führt zu einer ApiOperationException mit Status 500")
	void duplikatNull_wirftApiOperationException() {
		final var schueler = createSchueler();
		schueler.Duplikat = null;

		assertThatThrownBy(() -> mapper.toSchuelerListeEintrag(schueler, SCHULJAHR, null, null, null))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> {
					assertThat(e.getStatus()).isEqualTo(Status.INTERNAL_SERVER_ERROR);
					assertThat(e.getBody()).isEqualTo("Der Schüler mit der ID 4711 hat keine Angabe, ob es sich um ein Duplikat handelt.");
				});
	}

}
