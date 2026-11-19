package de.svws_nrw.data.email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schule.DTOSchuleEmail;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft, dass die SMTP-Konfiguration den Fingerabdruck des Zertifikats nur als gültigen SHA-256-Fingerabdruck und in
 * einheitlicher Form speichert.
 */
@ExtendWith(MockitoExtension.class)
class DataEmailSMTPServerKonfigurationTest {

	private static final String EINHEITLICH = "3A:F2:9C:0E:51:7B:D4:88:26:6F:A1:C3:90:5D:E7:12:4B:B8:0F:6C:D9:73:A5:2E:18:C4:F0:9B:63:7D:E2:45";

	@Mock
	private DBEntityManager conn;

	private DTOSchuleEmail erzeugeDto() {
		final DTOSchuleEmail dto = new DTOSchuleEmail(1, "smtp.example.com", 587, true, false);
		when(conn.querySingle(DTOSchuleEmail.class)).thenReturn(dto);
		return dto;
	}

	private void patch(final String json) throws ApiOperationException {
		new DataEmailSMTPServerKonfiguration(conn).patch(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
	}

	@Test
	@DisplayName("Ein Fingerabdruck in Kleinbuchstaben ohne Trennzeichen wird einheitlich gespeichert und ausgegeben")
	void testFingerabdruckWirdEinheitlichGespeichert() throws ApiOperationException {
		final DTOSchuleEmail dto = erzeugeDto();
		patch("{\"zertifikatFingerabdruck\": \"" + EINHEITLICH.replace(":", "").toLowerCase() + "\"}");
		assertThat(dto.SMTPZertifikatFingerabdruck).isEqualTo(EINHEITLICH);
		assertThat(DataEmailSMTPServerKonfiguration.getOrCreateSMTPServerKonfiguration(conn).zertifikatFingerabdruck).isEqualTo(EINHEITLICH);
	}

	@Test
	@DisplayName("Ein leerer Fingerabdruck entfernt die Freigabe")
	void testLeererFingerabdruckEntferntFreigabe() throws ApiOperationException {
		final DTOSchuleEmail dto = erzeugeDto();
		dto.SMTPZertifikatFingerabdruck = EINHEITLICH;
		patch("{\"zertifikatFingerabdruck\": \"\"}");
		assertThat(dto.SMTPZertifikatFingerabdruck).isNull();
		dto.SMTPZertifikatFingerabdruck = EINHEITLICH;
		patch("{\"zertifikatFingerabdruck\": null}");
		assertThat(dto.SMTPZertifikatFingerabdruck).isNull();
	}

	@Test
	@DisplayName("Eine Angabe, die kein SHA-256-Fingerabdruck ist, wird mit 400 abgelehnt und nicht gespeichert")
	void testUngueltigerFingerabdruckWirdAbgelehnt() {
		final DTOSchuleEmail dto = erzeugeDto();
		assertThatThrownBy(() -> patch("{\"zertifikatFingerabdruck\": \"*\"}"))
				.isInstanceOf(ApiOperationException.class)
				.extracting(e -> ((ApiOperationException) e).getStatus())
				.isEqualTo(Status.BAD_REQUEST);
		assertThat(dto.SMTPZertifikatFingerabdruck).isNull();
	}

	@Test
	@DisplayName("Der Fingerabdruck ändert die Einstellung trustTLSHost nicht, die andere Programme nutzen")
	void testTrustTlsHostBleibtUnveraendert() throws ApiOperationException {
		final DTOSchuleEmail dto = erzeugeDto();
		dto.SMTPTrustTLSHost = "*";
		patch("{\"zertifikatFingerabdruck\": \"" + EINHEITLICH + "\"}");
		assertThat(dto.SMTPTrustTLSHost).isEqualTo("*");
	}

}
