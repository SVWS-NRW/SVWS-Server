package de.svws_nrw.data.benutzer;

import java.util.List;

import de.svws_nrw.core.data.benutzer.BenutzerLehrerCredentials;
import de.svws_nrw.core.data.benutzer.LehrerCredentialsOhneBenutzerEintrag;
import de.svws_nrw.core.types.benutzer.BenutzerTyp;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentials;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.ext.jbcrypt.BCrypt;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Diese Testklasse testet die Lehrer-Benutzer betreffenden Methoden der Klasse DataBenutzerDaten")
@ExtendWith(MockitoExtension.class)
class DataBenutzerDatenTest {

	private static final long ID_LEHRER = 5L;
	private static final long ID_BENUTZER = 42L;
	private static final long ID_CREDENTIALS_NEU = 77L;
	private static final long ID_CREDENTIALS_ALT = 13L;

	@Mock
	private DBEntityManager conn;

	private DataBenutzerDaten dataBenutzerDaten;

	@BeforeEach
	void setUp() {
		dataBenutzerDaten = new DataBenutzerDaten(conn);
		lenient().when(conn.transactionGetNextID(DTOBenutzer.class)).thenReturn(ID_BENUTZER);
		lenient().when(conn.transactionGetNextID(DTOCredentials.class)).thenReturn(ID_CREDENTIALS_NEU);
		lenient().when(conn.queryByKey(DTOViewBenutzerdetails.class, ID_BENUTZER)).thenReturn(viewBenutzerdetails());
	}

	@Test
	@DisplayName("createBenutzerLehrer | Lehrer ohne Credentials: Benutzer und Credentials werden angelegt und mit dem Lehrer verknüpft")
	void createBenutzerLehrerWithoutCredentialsTest() throws ApiOperationException {
		final DTOLehrer lehrer = lehrer(null);
		when(conn.queryByKey(DTOLehrer.class, ID_LEHRER)).thenReturn(lehrer);

		final Response response = dataBenutzerDaten.createBenutzerLehrer(credentials("MUE", "geheim"));

		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		final DTOBenutzer benutzer = persisted(DTOBenutzer.class);
		assertThat(benutzer.ID).isEqualTo(ID_BENUTZER);
		assertThat(benutzer.Typ).isEqualTo(BenutzerTyp.LEHRER);
		assertThat(benutzer.Lehrer_ID).isEqualTo(ID_LEHRER);
		final DTOCredentials credential = persisted(DTOCredentials.class);
		assertThat(credential.ID).isEqualTo(ID_CREDENTIALS_NEU);
		assertThat(credential.Benutzername).isEqualTo("MUE");
		assertThat(BCrypt.checkpw("geheim", credential.PasswordHash)).isTrue();
		assertThat(lehrer.CredentialID).isEqualTo(ID_CREDENTIALS_NEU);
	}

	@Test
	@DisplayName("createBenutzerLehrer | Für den Lehrer existiert bereits ein Benutzer: 409")
	void createBenutzerLehrerBenutzerExistsTest() {
		when(conn.queryByKey(DTOLehrer.class, ID_LEHRER)).thenReturn(lehrer(null));
		when(conn.queryList(DTOBenutzer.QUERY_BY_LEHRER_ID, DTOBenutzer.class, ID_LEHRER))
				.thenReturn(List.of(new DTOBenutzer(1L, BenutzerTyp.LEHRER, false)));

		final ApiOperationException e = catchThrowableOfType(ApiOperationException.class,
				() -> dataBenutzerDaten.createBenutzerLehrer(credentials("MUE", "geheim")));

		assertThat(e.getStatus()).isEqualTo(Status.CONFLICT);
		verify(conn, never()).transactionPersist(any());
	}

	@Test
	@DisplayName("createBenutzerLehrer | Unbekannte Lehrer-ID: 404")
	void createBenutzerLehrerLehrerNotFoundTest() {
		when(conn.queryByKey(DTOLehrer.class, ID_LEHRER)).thenReturn(null);

		final ApiOperationException e = catchThrowableOfType(ApiOperationException.class,
				() -> dataBenutzerDaten.createBenutzerLehrer(credentials("MUE", "geheim")));

		assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND);
		verify(conn, never()).transactionPersist(any());
	}

	@ParameterizedTest(name = "Benutzername \"{0}\"")
	@ValueSource(strings = { "", " ", " MUE", "MUE ", "M UE", "M\tUE" })
	@DisplayName("createBenutzerLehrer | Ungültiger Benutzername: 400")
	void createBenutzerLehrerInvalidBenutzernameTest(final String benutzername) {
		final ApiOperationException e = catchThrowableOfType(ApiOperationException.class,
				() -> dataBenutzerDaten.createBenutzerLehrer(credentials(benutzername, "geheim")));

		assertThat(e.getStatus()).isEqualTo(Status.BAD_REQUEST);
		verify(conn, never()).transactionPersist(any());
	}

	@ParameterizedTest(name = "Kennwort \"{0}\"")
	@ValueSource(strings = { "", " " })
	@DisplayName("createBenutzerLehrer | Leeres Kennwort: 400")
	void createBenutzerLehrerEmptyPasswordTest(final String password) {
		final ApiOperationException e = catchThrowableOfType(ApiOperationException.class,
				() -> dataBenutzerDaten.createBenutzerLehrer(credentials("MUE", password)));

		assertThat(e.getStatus()).isEqualTo(Status.BAD_REQUEST);
		verify(conn, never()).transactionPersist(any());
	}

	@ParameterizedTest(name = "Benutzername \"{0}\"")
	@ValueSource(strings = { "MUE", "mue", "Mue" })
	@DisplayName("createBenutzerLehrer | Benutzername bereits anderweitig vergeben (Groß-/Kleinschreibung egal): 409")
	void createBenutzerLehrerBenutzernameTakenTest(final String benutzername) {
		when(conn.queryByKey(DTOLehrer.class, ID_LEHRER)).thenReturn(lehrer(null));
		when(conn.queryAll(DTOCredentials.class)).thenReturn(List.of(new DTOCredentials(99L, "MUE")));

		final ApiOperationException e = catchThrowableOfType(ApiOperationException.class,
				() -> dataBenutzerDaten.createBenutzerLehrer(credentials(benutzername, "geheim")));

		assertThat(e.getStatus()).isEqualTo(Status.CONFLICT);
		verify(conn, never()).transactionPersist(any());
	}

	@Test
	@DisplayName("createBenutzerLehrer | Vorhandene Credentials, bisheriger Benutzername gewählt: werden wiederverwendet und das Kennwort neu gesetzt")
	void createBenutzerLehrerExistingCredentialsKeepNameTest() throws ApiOperationException {
		final DTOLehrer lehrer = lehrer(ID_CREDENTIALS_ALT);
		final DTOCredentials alt = credentialsAlt("MUE");
		when(conn.queryByKey(DTOLehrer.class, ID_LEHRER)).thenReturn(lehrer);
		when(conn.queryByKey(DTOCredentials.class, ID_CREDENTIALS_ALT)).thenReturn(alt);
		when(conn.queryAll(DTOCredentials.class)).thenReturn(List.of(alt));

		final Response response = dataBenutzerDaten.createBenutzerLehrer(credentials("MUE", "geheim"));

		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		final DTOCredentials credential = persisted(DTOCredentials.class);
		assertThat(credential).isSameAs(alt);
		assertThat(credential.Benutzername).isEqualTo("MUE");
		assertThat(BCrypt.checkpw("geheim", credential.PasswordHash)).isTrue();
		assertThat(lehrer.CredentialID).isEqualTo(ID_CREDENTIALS_ALT);
		assertThat(persisted(DTOBenutzer.class).Lehrer_ID).isEqualTo(ID_LEHRER);
	}

	@Test
	@DisplayName("createBenutzerLehrer | Vorhandene Credentials, neuer freier Benutzername gewählt: Credentials werden wiederverwendet und umbenannt")
	void createBenutzerLehrerExistingCredentialsNewNameTest() throws ApiOperationException {
		final DTOLehrer lehrer = lehrer(ID_CREDENTIALS_ALT);
		final DTOCredentials alt = credentialsAlt("ALT");
		when(conn.queryByKey(DTOLehrer.class, ID_LEHRER)).thenReturn(lehrer);
		when(conn.queryByKey(DTOCredentials.class, ID_CREDENTIALS_ALT)).thenReturn(alt);
		when(conn.queryAll(DTOCredentials.class)).thenReturn(List.of(alt, new DTOCredentials(99L, "ANDERER")));

		dataBenutzerDaten.createBenutzerLehrer(credentials("NEU", "geheim"));

		final DTOCredentials credential = persisted(DTOCredentials.class);
		assertThat(credential).isSameAs(alt);
		assertThat(credential.ID).isEqualTo(ID_CREDENTIALS_ALT);
		assertThat(credential.Benutzername).isEqualTo("NEU");
		assertThat(BCrypt.checkpw("geheim", credential.PasswordHash)).isTrue();
		assertThat(lehrer.CredentialID).isEqualTo(ID_CREDENTIALS_ALT);
	}

	@Test
	@DisplayName("createBenutzerLehrer | Vorhandene Credentials, neuer Benutzername bereits anderweitig vergeben: 409")
	void createBenutzerLehrerExistingCredentialsNameTakenTest() {
		final DTOCredentials alt = credentialsAlt("ALT");
		when(conn.queryByKey(DTOLehrer.class, ID_LEHRER)).thenReturn(lehrer(ID_CREDENTIALS_ALT));
		when(conn.queryAll(DTOCredentials.class)).thenReturn(List.of(alt, new DTOCredentials(99L, "ANDERER")));

		final ApiOperationException e = catchThrowableOfType(ApiOperationException.class,
				() -> dataBenutzerDaten.createBenutzerLehrer(credentials("anderer", "geheim")));

		assertThat(e.getStatus()).isEqualTo(Status.CONFLICT);
		assertThat(alt.Benutzername).isEqualTo("ALT");
		verify(conn, never()).transactionPersist(any());
	}


	@Test
	@DisplayName("getLehrerCredentialsOhneBenutzer | liefert nur Lehrer ohne Lehrer-Benutzer, aber mit vorhandenen Credentials")
	void getLehrerCredentialsOhneBenutzerTest() {
		final DTOLehrer ohneBenutzer = new DTOLehrer(1L, "A", "Anders");
		ohneBenutzer.CredentialID = 11L;
		final DTOLehrer mitBenutzer = new DTOLehrer(2L, "B", "Berger");
		mitBenutzer.CredentialID = 12L;
		final DTOLehrer ohneCredentials = new DTOLehrer(3L, "C", "Conrad");
		final DTOBenutzer benutzer = new DTOBenutzer(20L, BenutzerTyp.LEHRER, false);
		benutzer.Lehrer_ID = 2L;
		when(conn.queryAll(DTOLehrer.class)).thenReturn(List.of(ohneBenutzer, mitBenutzer, ohneCredentials));
		when(conn.queryList(DTOBenutzer.QUERY_LIST_BY_LEHRER_ID, DTOBenutzer.class, List.of(1L, 2L))).thenReturn(List.of(benutzer));
		when(conn.queryByKeyList(DTOCredentials.class, List.of(11L))).thenReturn(List.of(new DTOCredentials(11L, "ALT")));

		final List<LehrerCredentialsOhneBenutzerEintrag> result = dataBenutzerDaten.getLehrerCredentialsOhneBenutzer();

		assertThat(result).singleElement()
				.hasFieldOrPropertyWithValue("idLehrer", 1L)
				.hasFieldOrPropertyWithValue("idCredentials", 11L)
				.hasFieldOrPropertyWithValue("benutzername", "ALT");
	}

	@Test
	@DisplayName("getLehrerCredentialsOhneBenutzer | ohne Lehrer mit Credentials wird eine leere Liste geliefert")
	void getLehrerCredentialsOhneBenutzerEmptyTest() {
		when(conn.queryAll(DTOLehrer.class)).thenReturn(List.of(new DTOLehrer(3L, "C", "Conrad")));

		assertThat(dataBenutzerDaten.getLehrerCredentialsOhneBenutzer()).isEmpty();
	}


	private static DTOCredentials credentialsAlt(final String benutzername) {
		final DTOCredentials alt = new DTOCredentials(ID_CREDENTIALS_ALT, benutzername);
		alt.PasswordHash = BCrypt.hashpw("altesKennwort", BCrypt.gensalt());
		return alt;
	}

	private static BenutzerLehrerCredentials credentials(final String benutzername, final String password) {
		final BenutzerLehrerCredentials cred = new BenutzerLehrerCredentials();
		cred.idLehrer = ID_LEHRER;
		cred.benutzername = benutzername;
		cred.password = password;
		return cred;
	}

	private static DTOLehrer lehrer(final Long idCredentials) {
		final DTOLehrer lehrer = new DTOLehrer(ID_LEHRER, "MUE", "Müller");
		lehrer.Vorname = "Anna";
		lehrer.CredentialID = idCredentials;
		return lehrer;
	}

	private static DTOViewBenutzerdetails viewBenutzerdetails() {
		final DTOViewBenutzerdetails view = mock(DTOViewBenutzerdetails.class);
		view.ID = ID_BENUTZER;
		view.credentialID = ID_CREDENTIALS_NEU;
		view.Typ = BenutzerTyp.LEHRER;
		view.TypID = ID_LEHRER;
		view.AnzeigeName = "Anna Müller";
		view.Benutzername = "MUE";
		view.IstAdmin = false;
		return view;
	}

	private <T> T persisted(final Class<T> cl) {
		final ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
		verify(conn, atLeastOnce()).transactionPersist(captor.capture());
		return captor.getAllValues().stream().filter(cl::isInstance).map(cl::cast).reduce((a, b) -> b).orElseThrow();
	}

}
