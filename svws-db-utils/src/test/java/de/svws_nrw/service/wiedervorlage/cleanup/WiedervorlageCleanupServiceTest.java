package de.svws_nrw.service.wiedervorlage.cleanup;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import de.svws_nrw.oauth.SchemaService;
import de.svws_nrw.repo.wiedervorlage.WiedervorlageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-Tests für {@link WiedervorlageCleanupService}.
 *
 */
@ExtendWith(MockitoExtension.class)
class WiedervorlageCleanupServiceTest {

	private static final String SCHEMA = "svws_testschema";

	private ConcurrentMap<String, LocalDate> runDateBySchema;

	@Mock
	private WiedervorlageRepository wiedervorlageRepository;

	@Mock
	private SchemaService schemaService;

	private WiedervorlageCleanupService cut;

	@BeforeEach
	void setup() {
		runDateBySchema = new ConcurrentHashMap<>();
		cut = new WiedervorlageCleanupService(runDateBySchema, wiedervorlageRepository, schemaService);
	}


	@Test
	@DisplayName("deleteConditionally löscht und aktualisiert die Throttle, wenn ein Lauf fällig ist")
	void deleteAllExpired_deletesAndUpdatesThrottle_whenRunIsNull() {
		when(schemaService.getActiveSchema()).thenReturn(SCHEMA);

		cut.deleteAllExpired();

		verify(wiedervorlageRepository, times(1)).deleteAbgelaufeneWiedervorlagen(any(LocalDate.class));
		assertNotNull(runDateBySchema.get(SCHEMA));
	}

	@Test
	@DisplayName("deleteConditionally löscht und aktualisiert die Throttle, wenn ein Lauf fällig ist")
	void deleteAllExpired_deletesAndUpdatesThrottle_whenRunShouldRunDailyAndMark() {
		when(schemaService.getActiveSchema()).thenReturn(SCHEMA);
		final LocalDate today = LocalDate.now();
		final LocalDate yesterday = today
				.minusDays(1);
		runDateBySchema.put(SCHEMA, yesterday);

		cut.deleteAllExpired();

		verify(wiedervorlageRepository, times(1)).deleteAbgelaufeneWiedervorlagen(any(LocalDate.class));
		assertEquals(runDateBySchema.get(SCHEMA), today);
	}

	@Test
	@DisplayName("deleteConditionally löscht nichts und aktualisiert die Throttle nicht, wenn kein Lauf fällig ist")
	void deleteAllExpired_doesNothing_whenRunShouldNotRunDailyAndMark() {
		when(schemaService.getActiveSchema()).thenReturn(SCHEMA);
		final LocalDate today = LocalDate.now();
		runDateBySchema.put(SCHEMA, today);

		cut.deleteAllExpired();

		verify(wiedervorlageRepository, never()).deleteAbgelaufeneWiedervorlagen(any(LocalDate.class));
		assertEquals(runDateBySchema.get(SCHEMA), today);
	}
}
