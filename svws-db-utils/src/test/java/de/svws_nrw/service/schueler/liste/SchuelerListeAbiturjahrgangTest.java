package de.svws_nrw.service.schueler.liste;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;

/**
 * Tests für die Klasse {@link SchuelerListeAbiturjahrgang}.
 */
class SchuelerListeAbiturjahrgangTest {

	private static final int SCHULJAHR = 2025;
	private static final long ID_ABSCHNITT = 20L;

	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	private static SchuelerListeEintrag eintrag(final long idAbschnitt, final String jahrgang) {
		final var eintrag = new SchuelerListeEintrag();
		eintrag.idSchuljahresabschnitt = idAbschnitt;
		eintrag.jahrgang = jahrgang;
		eintrag.idSchulgliederung = Schulgliederung.DEFAULT.daten(SCHULJAHR).id;
		return eintrag;
	}

	private static Map<Long, Schuljahresabschnitt> abschnitte() {
		final var abschnitt = new Schuljahresabschnitt();
		abschnitt.id = ID_ABSCHNITT;
		abschnitt.schuljahr = SCHULJAHR;
		abschnitt.abschnitt = 1;
		return Map.of(ID_ABSCHNITT, abschnitt);
	}

	@Test
	@DisplayName("addAbiturjahrgang | Schulform mit gymnasialer Oberstufe: Der Abiturjahrgang wird bestimmt")
	void addAbiturjahrgang_mitGymnasialerOberstufe() {
		final var eintrag = eintrag(ID_ABSCHNITT, "EF");

		SchuelerListeAbiturjahrgang.addAbiturjahrgang(List.of(eintrag), Schulform.GY, SCHULJAHR, abschnitte());

		assertThat(eintrag.abiturjahrgang).isEqualTo(2028);
	}

	@Test
	@DisplayName("addAbiturjahrgang | Schulform ohne gymnasiale Oberstufe: Der Abiturjahrgang bleibt unverändert")
	void addAbiturjahrgang_ohneGymnasialeOberstufe() {
		final var eintrag = eintrag(ID_ABSCHNITT, "EF");

		SchuelerListeAbiturjahrgang.addAbiturjahrgang(List.of(eintrag), Schulform.G, SCHULJAHR, abschnitte());

		assertThat(eintrag.abiturjahrgang).isNull();
	}

	@Test
	@DisplayName("addAbiturjahrgang | Nicht auflösbarer Schuljahresabschnitt: Der Eintrag bleibt unverändert")
	void addAbiturjahrgang_unbekannterAbschnitt() {
		final var eintrag = eintrag(999L, "EF");

		SchuelerListeAbiturjahrgang.addAbiturjahrgang(List.of(eintrag), Schulform.GY, SCHULJAHR, abschnitte());

		assertThat(eintrag.abiturjahrgang).isNull();
	}

	@Test
	@DisplayName("addAbiturjahrgang | Jahrgang ohne Oberstufenbezug in der Sekundarstufe I: Der Abiturjahrgang wird aus den Restjahren bestimmt")
	void addAbiturjahrgang_jahrgangSekundarstufeI() {
		final var eintrag = eintrag(ID_ABSCHNITT, "09");

		SchuelerListeAbiturjahrgang.addAbiturjahrgang(List.of(eintrag), Schulform.GY, SCHULJAHR, abschnitte());

		// Gymnasium, Gliederung DEFAULT wird als G8 interpretiert: 4 Restjahre
		assertThat(eintrag.abiturjahrgang).isEqualTo(SCHULJAHR + 4);
	}

}
