package de.svws_nrw.data.lehrer;

import java.util.HashMap;
import java.util.Map;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.lehrer.LehrerAnrechnungsstundenService;
import de.svws_nrw.service.lehrer.LehrerMehrleistungService;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Diese Testklasse testet die Klasse DataLehrerPersonalabschnittsdaten")
@ExtendWith(MockitoExtension.class)
class DataLehrerPersonalabschnittsdatenTest {

	@Mock
	private DBEntityManager conn;

	@Mock
	private Benutzer user;

	@Mock
	private LehrerAnrechnungsstundenService anrechnungsService;

	@Mock
	private LehrerMehrleistungService mehrleistungService;

	@Mock
	private LehrerServiceFactory serviceFactory;

	private DataLehrerPersonalabschnittsdaten data;

	private MockedStatic<LehrerServiceFactory> mockedFactory;

	@BeforeEach
	void setUp() {
		ASDCoreTypeUtils.initAll();
		mockedFactory = Mockito.mockStatic(LehrerServiceFactory.class);
		mockedFactory.when(LehrerServiceFactory::getNewInstance).thenReturn(serviceFactory);
		when(serviceFactory.getLehrerAnrechnungsstundenService()).thenReturn(anrechnungsService);
		when(serviceFactory.getLehrerMehrleistungService()).thenReturn(mehrleistungService);
		data = new DataLehrerPersonalabschnittsdaten(conn);
	}

	@AfterEach
	void tearDown() {
		mockedFactory.close();
	}


	private void mockSchuljahr(final int schuljahr) {
		when(conn.getUser()).thenReturn(user);
		final var abschnitt = new Schuljahresabschnitt();
		abschnitt.schuljahr = schuljahr;
		when(user.schuleGetSchuljahresabschnittByIdOrDefault(3L)).thenReturn(abschnitt);
	}


	@Test
	@DisplayName("patch | pflichtstundensoll | null")
	void patch_pflichtstundensollIsNull() throws ApiOperationException {
		mockSchuljahr(2023);
		final var dto = new DTOLehrerAbschnittsdaten(1L, 2L, 3L);
		dto.PflichtstdSoll = 42D;
		when(this.conn.queryByKey(DTOLehrerAbschnittsdaten.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		final var map = new HashMap<String, Object>();
		map.put("pflichtstundensoll", null);

		this.data.patch(1L, map);

		assertThat(dto.PflichtstdSoll).isNull();
	}

	@Test
	@DisplayName("patch | pflichtstundensoll |3 decimal places")
	void patch_pflichtstundensoll_3decimalPlaces() {
		when(this.conn.queryByKey(DTOLehrerAbschnittsdaten.class, 1L)).thenReturn(mock(DTOLehrerAbschnittsdaten.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("pflichtstundensoll", 33.333D)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Der Wert Pflichtstundensoll darf höchstens zwei Nachkommastellen haben.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | pflichtstundensoll")
	void patch_pflichtstundensoll() throws ApiOperationException {
		mockSchuljahr(2023);
		final var dto = new DTOLehrerAbschnittsdaten(1L, 2L, 3L);
		dto.PflichtstdSoll = 42D;
		when(this.conn.queryByKey(DTOLehrerAbschnittsdaten.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("pflichtstundensoll", 33.33D));

		assertThat(dto.PflichtstdSoll).isEqualTo(33.33D);
	}

}
