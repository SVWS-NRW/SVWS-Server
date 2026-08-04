package de.svws_nrw.service.wiedervorlage.cleanup;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import de.svws_nrw.oauth.SchemaService;
import de.svws_nrw.repo.wiedervorlage.WiedervorlageRepository;


public class WiedervorlageCleanupService {
	private static final long LOESCHUNG_NACH_TAGEN = 14;
	private static final ZoneId ZONE_BERLIN = ZoneId.of("Europe/Berlin");
	private final ConcurrentMap<String, LocalDate> runDateBySchema;

	private final SchemaService schemaService;
	private final WiedervorlageRepository wiedervorlageRepository;

	/**
	 * Initialisiert neuen Cleanup Service
	 * @param runDateBySchema Map die RunDates pro Schema hält (Singleton)
	 * @param wiedervorlageRepository {@link WiedervorlageRepository}
	 * @param schemaService {@link SchemaService}
	 */
	public WiedervorlageCleanupService(final ConcurrentMap<String, LocalDate> runDateBySchema,
			final WiedervorlageRepository wiedervorlageRepository,
			final SchemaService schemaService) {
		this.runDateBySchema = runDateBySchema;
		this.wiedervorlageRepository = wiedervorlageRepository;
		this.schemaService = schemaService;
	}

	/**
	 * Löscht zur Löschung markierte abgelaufene Wiedervorlagen
	 */
	public void deleteAllExpired() {
		final String schema = schemaService.getActiveSchema();
		final LocalDate today = LocalDate.now(ZONE_BERLIN);

		if (shouldRunDailyAndMark(schema, today)) {
			wiedervorlageRepository.deleteAbgelaufeneWiedervorlagen(today.minusDays(LOESCHUNG_NACH_TAGEN));
		}
	}

	private boolean shouldRunDailyAndMark(final String schema, final LocalDate today) {
		final AtomicBoolean shouldRun = new AtomicBoolean(false);

		runDateBySchema.compute(schema, (key, runDate) -> {
			if ((runDate == null) || runDate.isBefore(today)) {
				shouldRun.set(true);
				return today;
			}

			return runDate;
		});

		return shouldRun.get();
	}

}
