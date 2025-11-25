package de.svws_nrw.data.lernplattformen;

import java.util.List;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.lernplattform.v1.LernplattformV1;
import de.svws_nrw.core.data.schule.Lernplattform;
import de.svws_nrw.data.schule.DataKatalogLernplattformen;
import de.svws_nrw.db.DBEntityManager;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Diese Testklasse testet die Klasse DataLernplattformen")
@ExtendWith(MockitoExtension.class)
final class DataLernplattformenTest {


	@Mock
	private DBEntityManager conn;

	@Mock
	private DataKatalogLernplattformen dataKatalogLernplattformen;

	private DataLernplattformen data;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUpEach() {
		data = new DataLernplattformen(this.conn, -1, this.dataKatalogLernplattformen);
	}

	@Test
	@DisplayName("getAll")
	void getAll() {
		final var lp1 = new Lernplattform();
		lp1.id = 1;
		lp1.bezeichnung = "bezeichnung";
		final var lp2 = new Lernplattform();
		lp2.id = 2;
		lp2.bezeichnung = "bezeichnung2";
		when(this.dataKatalogLernplattformen.getAll()).thenReturn(List.of(lp1, lp2));

		assertThat(this.data.getAllAsResponse())
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(r -> r.getEntity())
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.hasSize(2)
				.satisfiesExactly(
						l1 -> assertThat(l1)
								.isInstanceOf(LernplattformV1.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("bezeichnung", "bezeichnung"),
						l2 -> assertThat(l2)
								.isInstanceOf(LernplattformV1.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("bezeichnung", "bezeichnung2")
				);
	}

	@Test
	@DisplayName("getAll | emptyList")
	void getAllEmptyList() {
		when(this.dataKatalogLernplattformen.getAll()).thenReturn(emptyList());

		assertThat(this.data.getAllAsResponse())
				.extracting(r -> r.getEntity())
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.isEmpty();
	}

	@Test
	@DisplayName("getAll | null")
	void getAllNull() {
		when(this.dataKatalogLernplattformen.getAll()).thenReturn(null);

		assertThat(this.data.getAllAsResponse())
				.extracting(r -> r.getEntity())
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.isEmpty();
	}

}
