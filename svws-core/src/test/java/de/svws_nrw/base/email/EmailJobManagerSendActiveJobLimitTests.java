package de.svws_nrw.base.email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;

/**
 * Prüft die Grenze für Jobs, die gleichzeitig warten oder versendet werden. Abgeschlossene und abgebrochene Jobs zählen nicht mit.
 */
class EmailJobManagerSendActiveJobLimitTests extends AbstractEmailJobManagerSendTestBase {

	@Test
	@DisplayName("enqueue mit Grenze: weitere Jobs werden abgelehnt, bis ein aktiver Job abgeschlossen oder abgebrochen ist")
	void testGrenzeAktiverJobs() {
		smtpMockHelper.setDelayMs(300);
		final long id1 = manager.enqueue(createSimpleJob("to_a@example.org"), 2);
		final long id2 = manager.enqueue(createSimpleJob("to_b@example.org"), 2);
		assertThat(id1).isPositive();
		assertThat(id2).isPositive();

		final EmailJob abgelehnt = createSimpleJob("to_c@example.org");
		assertThat(manager.enqueue(abgelehnt, 2)).isEqualTo(-1);
		assertThatThrownBy(abgelehnt::getId).as("Ein abgelehnter Job erhält keine ID").isInstanceOf(DeveloperNotificationException.class);

		manager.cancelJob(id2);
		awaitJobStatus(id2, EmailJobStatus.CANCELED);
		assertThat(manager.enqueue(createSimpleJob("to_d@example.org"), 2)).isPositive();
		assertThat(manager.enqueue(createSimpleJob("to_e@example.org"), 2)).isEqualTo(-1);

		awaitJobStatus(id1, EmailJobStatus.COMPLETED_SUCCESSFULLY);
		assertThat(manager.enqueue(createSimpleJob("to_f@example.org"), 2)).isPositive();
	}

	@Test
	@DisplayName("enqueue mit Grenze: parallele Aufträge überschreiten die Grenze nicht gemeinsam")
	void testGrenzeBeiParallelenAuftraegen() throws Exception {
		smtpMockHelper.setDelayMs(300);
		final int anzahl = 20;
		final CountDownLatch start = new CountDownLatch(1);
		final List<Future<Long>> ergebnisse = new ArrayList<>();
		try (ExecutorService executor = Executors.newFixedThreadPool(anzahl)) {
			for (int i = 0; i < anzahl; i++) {
				final EmailJob job = createSimpleJob("to_" + i + "@example.org");
				ergebnisse.add(executor.submit(() -> {
					start.await();
					return manager.enqueue(job, 3);
				}));
			}
			start.countDown();
			long angenommen = 0;
			for (final Future<Long> ergebnis : ergebnisse) {
				if (ergebnis.get() > 0) {
					angenommen++;
				}
			}
			assertThat(angenommen).isEqualTo(3);
		}
	}

}
