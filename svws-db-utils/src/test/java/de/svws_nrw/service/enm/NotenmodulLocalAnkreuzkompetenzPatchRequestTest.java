package de.svws_nrw.service.enm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests für die Validierung von {@link NotenmodulLocalAnkreuzkompetenzPatchRequest}.
 */
@DisplayName("Die Klasse NotenmodulLocalAnkreuzkompetenzPatchRequest")
class NotenmodulLocalAnkreuzkompetenzPatchRequestTest {

	@Test
	@DisplayName("hat keinen Default für die Stufen, damit ein fehlendes Feld nicht still alle Stufen auf false setzt")
	void keinDefaultFuerStufen() {
		assertThat(new NotenmodulLocalAnkreuzkompetenzPatchRequest().stufen).isNull();
	}

	@Test
	@DisplayName("wird bei fehlenden Stufen von der Bean-Validierung mit BAD_REQUEST abgelehnt")
	void fehlendeStufenAbgelehnt() {
		final NotenmodulLocalAnkreuzkompetenzPatchRequest patch = new NotenmodulLocalAnkreuzkompetenzPatchRequest();
		patch.id = 1L;

		assertThatThrownBy(() -> BeanValidator.validate(patch))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

}
