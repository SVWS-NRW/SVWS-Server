package de.svws_nrw.base.email;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailJobManagerFactoryTests {

	private MailSmtpSession smtp;

	@BeforeEach
	void setup() {
		smtp = mock(MailSmtpSession.class);
		EmailJobManagerFactory.getInstance().activate();
	}

	@AfterEach
	void reset() {
		EmailJobManagerFactory.getInstance().freeAllManager();
		EmailJobManagerFactory.getInstance().activate();
	}

	@Test
	@DisplayName("getManager mit null-Context wirft Exception")
	void testGetManagerWithNullContext() {
		final EmailJobManagerFactory f = EmailJobManagerFactory.getInstance();
		assertThrows(IllegalArgumentException.class, () -> f.getManager(null));
	}

	@Test
	@DisplayName("Mittels isActive/activate/deactivate wird der Factory Aktivierungsstatus richtig gesetzt und zurückgegeben.")
	void testActivationFlags() {
		final EmailJobManagerFactory f = EmailJobManagerFactory.getInstance();
		assertTrue(f.isActive());
		f.deactivate();
		assertFalse(f.isActive());
		f.activate();
		assertTrue(f.isActive());
	}

	@Test
	@DisplayName("Factory deactivate verhindert Erstellung neuer Manager")
	void testFactoryDeactivate() {
		final EmailJobManagerFactory f = EmailJobManagerFactory.getInstance();
		f.deactivate();

		try {
			final EmailJobManagerContext ctx = new EmailJobManagerContext("schemaForUser1", 1L, smtp);
			assertThrows(IllegalArgumentException.class, () -> f.getManager(ctx));
		} finally {
			f.activate();
		}
	}

	@Test
	@DisplayName("Job-Manager in Factory ermitteln und Job-Manager im Betrieb schließen (getManagerByUser/freeManager)")
	void testGetByUserAndFree() {
		final EmailJobManagerFactory f = EmailJobManagerFactory.getInstance();
		final EmailJobManagerContext ctx = new EmailJobManagerContext("schemaForUser1", 1L, smtp);
		final EmailJobManager m = f.getManager(ctx);
		assertNotNull(m);

		final EmailJobManager byUser = f.getManagerByUser("schemaForUser1", 1L);
		assertSame(m, byUser);

		// Enqueue einen Job, damit der Manager genutzt wird.
		final EmailJob job = new EmailJob("from@example.org");
		job.addRecipient(new EmailJobRecipient("to@example.org"));
		final long id = m.enqueue(job);
		assertTrue(id > 0);

		// Entferne den Job-Manager, auch wenn er genutzt wird
		f.freeManager("schemaForUser1", 1L);
		assertNull(f.getManagerByUser("schemaForUser1", 1L));
	}

	@Test
	@DisplayName("Schließen aller Job-Manager der Factory (freeAllManager)")
	void testFreeAll() {
		final EmailJobManagerFactory f = EmailJobManagerFactory.getInstance();
		final EmailJobManager m1 = f.getManager(new EmailJobManagerContext("schemaForUser1", 1L, smtp));
		final EmailJobManager m2 = f.getManager(new EmailJobManagerContext("schemaForUser2", 2L, smtp));
		assertNotNull(m1);
		assertNotNull(m2);
		f.freeAllManager();
		assertNull(f.getManagerByUser("schemaForUser1", 1L));
		assertNull(f.getManagerByUser("schemaForUser2", 2L));
	}

	@Test
	@DisplayName("Factory liefert gleiche Instanz pro (Schema,Nutzer)")
	void testFactorySameInstance() {
		final EmailJobManagerFactory f = EmailJobManagerFactory.getInstance();
		final EmailJobManagerContext ctx1 = new EmailJobManagerContext("schemaForUser1", 1L, smtp);
		final EmailJobManagerContext ctx2 = new EmailJobManagerContext("schemaForUser1", 1L, smtp);

		assertSame(f.getManager(ctx1), f.getManager(ctx2));
	}

	@Test
	@DisplayName("Factory liefert unterschiedliche Instanzen für verschiedene Kontexte")
	void testFactoryDifferentInstances() {
		final EmailJobManagerFactory f = EmailJobManagerFactory.getInstance();

		final EmailJobManager m1 = f.getManager(new EmailJobManagerContext("schema1", 1L, smtp));
		final EmailJobManager m2 = f.getManager(new EmailJobManagerContext("schema1", 2L, smtp));
		final EmailJobManager m3 = f.getManager(new EmailJobManagerContext("schema2", 1L, smtp));

		assertNotSame(m1, m2);
		assertNotSame(m1, m3);
	}

	@Test
	@DisplayName("Gleichzeitiger Zugriff auf Factory von 4 Threads für gleiches Schema/User muss dieselbe Manager-Instanz liefern")
	void testFactoryConcurrentAccess() throws Exception {
		final EmailJobManagerFactory f = EmailJobManagerFactory.getInstance();

		final CountDownLatch latch = new CountDownLatch(4);
		final List<Future<EmailJobManager>> futures = new ArrayList<>();

		try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
			for (int i = 0; i < 4; i++) {
				futures.add(executor.submit(() -> {
					latch.countDown();
					latch.await();
					return f.getManager(new EmailJobManagerContext("schema", 1L, smtp));
				}));
			}
		}

		// Alle Threads sollten dieselbe Manager-Instanz erhalten
		final EmailJobManager first = futures.getFirst().get();
		for (final Future<EmailJobManager> future : futures) {
			assertSame(first, future.get());
		}
	}

}
