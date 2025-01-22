package de.svws_nrw.data.lehrer;

import java.util.Collections;
import java.util.List;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Diese Klasse testet die Klasse DataLehrerListe")
@ExtendWith(MockitoExtension.class)
class DataLehrerListeTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataLehrerliste dataLehrerliste;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("map | erfolgreiches mapping | check Basic Attributes")
	void mapTest() {
		final var dtoLehrer = getDtoLehrer();

		assertThat(this.dataLehrerliste.map(dtoLehrer))
				.isInstanceOf(LehrerListeEintrag.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("kuerzel", "abc")
				.hasFieldOrPropertyWithValue("titel", "abc")
				.hasFieldOrPropertyWithValue("nachname", "abc")
				.hasFieldOrPropertyWithValue("vorname", "abc")
				.hasFieldOrPropertyWithValue("personTyp", PersonalTyp.LEHRKRAFT.kuerzel)
				.hasFieldOrPropertyWithValue("sortierung", 100)
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true);
	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | some Values null")
	void mapTest_someValuesNull() {
		final var dtoLehrer = getDtoLehrer();
		dtoLehrer.Titel = null;
		dtoLehrer.Nachname = null;
		dtoLehrer.Vorname = null;
		dtoLehrer.PersonTyp = null;
		dtoLehrer.Sortierung = null;
		dtoLehrer.Sichtbar = null;
		dtoLehrer.statistikRelevant = null;

		assertThat(this.dataLehrerliste.map(dtoLehrer))
				.isInstanceOf(LehrerListeEintrag.class)
				.hasFieldOrPropertyWithValue("titel", "")
				.hasFieldOrPropertyWithValue("nachname", "")
				.hasFieldOrPropertyWithValue("vorname", "")
				.hasFieldOrPropertyWithValue("personTyp", "")
				.hasFieldOrPropertyWithValue("sortierung", 32000)
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true);
	}

	@Test
	@DisplayName("getLehrerListe | Erfolg")
	void getLehrerListeTest() {
		when(this.conn.queryAll(DTOLehrer.class)).thenReturn(List.of(getDtoLehrer()));

		assertThat(this.dataLehrerliste.getLehrerListe())
				.isInstanceOf(List.class)
				.isNotNull()
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(LehrerListeEintrag.class)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("kuerzel", "abc")
						.hasFieldOrPropertyWithValue("titel", "abc")
						.hasFieldOrPropertyWithValue("nachname", "abc")
						.hasFieldOrPropertyWithValue("vorname", "abc")
						.hasFieldOrPropertyWithValue("personTyp", PersonalTyp.LEHRKRAFT.kuerzel)
						.hasFieldOrPropertyWithValue("sortierung", 100)
						.hasFieldOrPropertyWithValue("istSichtbar", true)
						.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true));
	}

	@Test
	@DisplayName("getLehrerListe | emptyList")
	void getLehrerListeTest_emptyList() {
		when(this.conn.queryAll(DTOLehrer.class)).thenReturn(Collections.emptyList());

		assertThat(this.dataLehrerliste.getLehrerListe())
				.isInstanceOf(List.class)
				.isEmpty();
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() {
		when(this.conn.queryAll(DTOLehrer.class)).thenReturn(List.of(getDtoLehrer()));

		assertThat(this.dataLehrerliste.getAll())
				.isInstanceOf(List.class)
				.isNotNull()
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(LehrerListeEintrag.class)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("kuerzel", "abc")
						.hasFieldOrPropertyWithValue("titel", "abc")
						.hasFieldOrPropertyWithValue("nachname", "abc")
						.hasFieldOrPropertyWithValue("vorname", "abc")
						.hasFieldOrPropertyWithValue("personTyp", PersonalTyp.LEHRKRAFT.kuerzel)
						.hasFieldOrPropertyWithValue("sortierung", 100)
						.hasFieldOrPropertyWithValue("istSichtbar", true)
						.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true));
	}

	@Test
	@DisplayName("getAll | emptyList")
	void getAllTest_emptyList() {
		when(this.conn.queryAll(DTOLehrer.class)).thenReturn(Collections.emptyList());

		assertThat(this.dataLehrerliste.getAll())
				.isInstanceOf(List.class)
				.isEmpty();
	}

	@Test
	@DisplayName("getList | Erfolg")
	void getListTest() {
		when(this.conn.queryList(DTOLehrer.QUERY_BY_SICHTBAR, DTOLehrer.class, true)).thenReturn(List.of(getDtoLehrer()));

		assertThat(this.dataLehrerliste.getList())
				.isInstanceOf(List.class)
				.isNotNull()
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(LehrerListeEintrag.class)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("kuerzel", "abc")
						.hasFieldOrPropertyWithValue("titel", "abc")
						.hasFieldOrPropertyWithValue("nachname", "abc")
						.hasFieldOrPropertyWithValue("vorname", "abc")
						.hasFieldOrPropertyWithValue("personTyp", PersonalTyp.LEHRKRAFT.kuerzel)
						.hasFieldOrPropertyWithValue("sortierung", 100)
						.hasFieldOrPropertyWithValue("istSichtbar", true)
						.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true));
	}

	@Test
	@DisplayName("getList | emptyList")
	void getListTest_emptyList() {
		when(this.conn.queryList(DTOLehrer.QUERY_BY_SICHTBAR, DTOLehrer.class, true)).thenReturn(Collections.emptyList());

		assertThat(this.dataLehrerliste.getList())
				.isInstanceOf(List.class)
				.isEmpty();
	}


	private DTOLehrer getDtoLehrer() {
		final var dtoLehrer = new DTOLehrer(1L, "abc", "abc");
		dtoLehrer.Titel = "abc";
		dtoLehrer.Vorname = "abc";
		dtoLehrer.PersonTyp = PersonalTyp.LEHRKRAFT;
		dtoLehrer.Sortierung = 100;
		dtoLehrer.Sichtbar = true;
		dtoLehrer.statistikRelevant = true;
		return dtoLehrer;
	}
}
