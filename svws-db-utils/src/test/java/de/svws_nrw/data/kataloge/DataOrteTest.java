package de.svws_nrw.data.kataloge;

import java.util.Collections;
import java.util.List;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Diese Klasse testet die Klasse {@link DataOrte} */
@DisplayName("Diese Klasse testet die Klasse DataOrte")
@ExtendWith(MockitoExtension.class)
class DataOrteTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataOrte data;

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		final var dto1 = new DTOOrt(1L, "plz1", "bez1");
		final var dto2 = new DTOOrt(2L, "plz2", "bez2");
		when(this.conn.queryAll(DTOOrt.class)).thenReturn(List.of(dto1, dto2));

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(OrtKatalogEintrag.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("plz", "plz1")
								.hasFieldOrPropertyWithValue("ortsname", "bez1"),
						f2 -> assertThat(f2)
								.isInstanceOf(OrtKatalogEintrag.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("plz", "plz2")
								.hasFieldOrPropertyWithValue("ortsname", "bez2")
				);
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllEmpty() {
		when(this.conn.queryAll(DTOOrt.class)).thenReturn(Collections.emptyList());

		verify(this.conn, never()).query(anyString(), eq(Long.class));
		assertThat(this.data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("map | Erfolg")
	void map() {
		final var dto = new DTOOrt(1L, "plz1", "bez1");
		dto.Sichtbar = true;
		dto.Aenderbar = true;
		dto.Kreis = "kreis";
		dto.Land = "land";
		dto.Sortierung = 42;

		assertThat(this.data.map(dto))
				.isInstanceOf(OrtKatalogEintrag.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("plz", dto.PLZ)
				.hasFieldOrPropertyWithValue("ortsname", dto.Bezeichnung)
				.hasFieldOrPropertyWithValue("kreis", dto.Kreis)
				.hasFieldOrPropertyWithValue("kuerzelBundesland", dto.Land)
				.hasFieldOrPropertyWithValue("sortierung", dto.Sortierung)
				.hasFieldOrPropertyWithValue("istSichtbar", dto.Sichtbar)
				.hasFieldOrPropertyWithValue("istAenderbar", dto.Aenderbar);
	}

	@Test
	@DisplayName("map | sichtbar null")
	void mapSichtbarIsNull() {
		final var dto = new DTOOrt(1L, "plz1", "bez1");
		dto.Sichtbar = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(OrtKatalogEintrag.class)
				.hasFieldOrPropertyWithValue("istSichtbar", false);
	}

	@Test
	@DisplayName("map | sortierung null")
	void mapSortierungIsNull() {
		final var dto = new DTOOrt(1L, "plz1", "bez1");
		dto.Sortierung = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(OrtKatalogEintrag.class)
				.hasFieldOrPropertyWithValue("sortierung", 32000);
	}

	@Test
	@DisplayName("map | aenderbar null")
	void mapAenderbarIsNull() {
		final var dto = new DTOOrt(1L, "plz1", "bez1");
		dto.Aenderbar = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(OrtKatalogEintrag.class)
				.hasFieldOrPropertyWithValue("istAenderbar", false);
	}

}
