package de.svws_nrw.mapper.schueler;

import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SchuelerMapperTest {

	private final SchuelerMapper mapper = SchuelerMapper.INSTANCE;

	private static DTOSchueler createEntity() {
		final var entity = new DTOSchueler(4711L, "GU-4711", false);
		entity.Nachname = "Mustermann";
		entity.Vorname = "Max";
		entity.Geschlecht = Geschlecht.W;
		entity.idStatus = 8;
		entity.ExterneSchulNr = "123456";
		return entity;
	}

	@Test
	@DisplayName("toSchueler mit Abschlussjahrgang: Alle Felder werden gemappt")
	void toSchuelerMitAbschlussjahrgang_mapptAlleFelder() {
		final Schueler result = mapper.toSchueler(createEntity(), 2026);

		assertThat(result.id).isEqualTo(4711L);
		assertThat(result.nachname).isEqualTo("Mustermann");
		assertThat(result.vorname).isEqualTo("Max");
		assertThat(result.geschlecht).isEqualTo(Geschlecht.W.id);
		assertThat(result.abschlussjahrgang).isEqualTo(2026);
		assertThat(result.status).isEqualTo(8);
		assertThat(result.externeSchulNr).isEqualTo("123456");
	}

	@Test
	@DisplayName("toSchueler mit Abschlussjahrgang null: Abschlussjahrgang wird zu -1")
	void toSchuelerMitAbschlussjahrgangNull_setztMinusEins() {
		final Schueler result = mapper.toSchueler(createEntity(), null);

		assertThat(result.abschlussjahrgang).isEqualTo(-1);
	}

	@Test
	@DisplayName("toSchueler ohne Abschlussjahrgang: Abschlussjahrgang wird zu -1, übrige Felder werden gemappt")
	void toSchuelerOhneAbschlussjahrgang_setztMinusEins() {
		final Schueler result = mapper.toSchueler(createEntity());

		assertThat(result.id).isEqualTo(4711L);
		assertThat(result.nachname).isEqualTo("Mustermann");
		assertThat(result.vorname).isEqualTo("Max");
		assertThat(result.geschlecht).isEqualTo(Geschlecht.W.id);
		assertThat(result.abschlussjahrgang).isEqualTo(-1);
		assertThat(result.status).isEqualTo(8);
		assertThat(result.externeSchulNr).isEqualTo("123456");
	}

	@Test
	@DisplayName("toSchueler: Vorname null wird zu leerem String")
	void toSchuelerVornameNull_setztLeerenString() {
		final var entity = createEntity();
		entity.Vorname = null;

		final Schueler result = mapper.toSchueler(entity);

		assertThat(result.vorname).isEmpty();
	}

	@Test
	@DisplayName("toSchueler: Externe Schulnummer null bleibt null")
	void toSchuelerExterneSchulNrNull_bleibtNull() {
		final var entity = createEntity();
		entity.ExterneSchulNr = null;

		final Schueler result = mapper.toSchueler(entity);

		assertThat(result.externeSchulNr).isNull();
	}

	@Test
	@DisplayName("toSchueler: Status null führt zu einer ApiOperationException mit Status 500")
	void toSchuelerStatusNull_wirftApiOperationException() {
		final var entity = createEntity();
		entity.idStatus = null;

		assertThatThrownBy(() -> mapper.toSchueler(entity))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> {
					assertThat(e.getStatus()).isEqualTo(Status.INTERNAL_SERVER_ERROR);
					assertThat(e.getBody()).isEqualTo("Der Schüler mit der ID 4711 hat keinen Status.");
				});
	}

	@Test
	@DisplayName("toSchueler: Geschlecht null führt zu einer ApiOperationException mit Status 500")
	void toSchuelerGeschlechtNull_wirftApiOperationException() {
		final var entity = createEntity();
		entity.Geschlecht = null;

		assertThatThrownBy(() -> mapper.toSchueler(entity, 2026))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> {
					assertThat(e.getStatus()).isEqualTo(Status.INTERNAL_SERVER_ERROR);
					assertThat(e.getBody()).isEqualTo("Der Schüler mit der ID 4711 hat kein gültiges Geschlecht.");
				});
	}

}
