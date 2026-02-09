package de.svws_nrw.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jboss.resteasy.core.ResteasyContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class TransactionSupportTest {

	@Mock
	private DBEntityManager mockedConn;

	@Mock
	private HttpServletRequest mockedRequest;

	@BeforeEach
	void setup() {
		ResteasyContext.pushContext(HttpServletRequest.class, mockedRequest);
		when(mockedRequest.getAttribute("connection")).thenReturn(mockedConn);
	}

	@AfterEach
	void tearDown() {
		ResteasyContext.clearContextData();
	}

	@Test
	@DisplayName("Supplier Task: Führe den Task mit der Transaktion und mit einem gültigem Rückgabewert aus")
	void testTransactionalSupplierErfolgreich() {
		when(mockedConn.hasActiveTransaction()).thenReturn(false);

		final String result = TransactionSupport.transactional(() -> "Erfolg");

		assertEquals("Erfolg", result);
		verify(mockedConn, times(1)).transactionBegin();
		verify(mockedConn, times(1)).transactionCommitOrThrow();
		verify(mockedConn, times(1)).transactionRollbackOrThrow();
	}

	@Test
	@DisplayName("Supplier Task: Im Falle einer Exception im Task muss der Rollback ausgeführt werden, prüfe auch auf Suppressed Exception")
	void testTransactionalSupplierTaskUndRollbackFehler() {
		when(mockedConn.hasActiveTransaction()).thenReturn(false);

		final RuntimeException taskException = new RuntimeException("Task fehlgeschlagen");
		final RuntimeException rollbackException = new RuntimeException("Rollback fehlgeschlagen");
		doThrow(rollbackException).when(mockedConn).transactionRollbackOrThrow();

		final RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			TransactionSupport.transactional(() -> {
				throw taskException;
			});
		});

		assertEquals(taskException, thrown);
		// Prüfe auf angehängte Suppressed Exception
		assertEquals(1, thrown.getSuppressed().length);
		assertEquals(rollbackException, thrown.getSuppressed()[0]);
	}

	@Test
	@DisplayName("Supplier Task: Existiert bereits eine aktive Transaktion, so wird keine neue Transaktion gestartet, der Task aber dennoch ausgeführt.")
	void testTransactionalSupplierExistingTransaction() {
		when(mockedConn.hasActiveTransaction()).thenReturn(true);

		final String result = TransactionSupport.transactional(() -> "Irgendeine Rückgabe");
		assertEquals("Irgendeine Rückgabe", result);

		// Es wird keine Methode zur Steuerung der Transaktion aufgerufen.
		verify(mockedConn, never()).transactionBegin();
		verify(mockedConn, never()).transactionCommitOrThrow();
		verify(mockedConn, never()).transactionRollbackOrThrow();
	}


	@Test
	@DisplayName("Supplier Task: Edge-Case - Der Aufruf der Rollback-Methode im finally-Block wirft eine Exception, obwohl im Task keine Auftritt")
	void testTransactionalSupplierNurRollbackFehler() {
		when(mockedConn.hasActiveTransaction()).thenReturn(false);

		// Edge-Case: Der Aufruf der Rollback-Methode im finally-Block wirft eine Exception, obwohl im Task keine Auftritt -> wird ignoriert
		final RuntimeException rollbackException = new RuntimeException("Exception beim Rollback im finally-Block");
		doThrow(rollbackException).when(mockedConn).transactionRollbackOrThrow();

		// Der Task wird erfolgreich ausgeführt
		final String expectedResult = "Task erfolgreich";

		final RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			TransactionSupport.transactional(() -> expectedResult);
		});

		assertEquals(rollbackException, thrown);
		verify(mockedConn, times(1)).transactionRollbackOrThrow();
	}

	@Test
	@DisplayName("Supplier Task: Im Task tritt ein Fehler auf, was zu einem erfolgreichen Rollback führt.")
	void testTransactionalSupplierNurTaskFehler() {
		when(mockedConn.hasActiveTransaction()).thenReturn(false);

		final RuntimeException taskException = new RuntimeException("Einfacher Task Fehler");
		final RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			TransactionSupport.transactional(() -> {
				throw taskException;
			});
		});

		assertEquals(taskException, thrown);
		assertEquals(0, thrown.getSuppressed().length, "Es sollte keine Suppressed Exception geben, wenn der Rollback erfolgreich war.");
		verify(mockedConn).transactionRollbackOrThrow();
	}

	@Test
	@DisplayName("Runnable Task: Führe den Task mit der Transaktion aus")
	void testTransactionalRunnableErfolgreich() {
		when(mockedConn.hasActiveTransaction()).thenReturn(false);

		TransactionSupport.transactional(() -> {
			/* mache irgendetwas */ });

		verify(mockedConn, times(1)).transactionBegin();
		verify(mockedConn, times(1)).transactionCommitOrThrow();
		verify(mockedConn, times(1)).transactionRollbackOrThrow();
	}

	@Test
	@DisplayName("Runnable Task: Im Falle einer Exception im Task muss der Rollback ausgeführt werden, prüfe auch auf Suppressed Exception")
	void testTransactionalRunnableTaskUndRollbackFehler() {
		when(mockedConn.hasActiveTransaction()).thenReturn(false);

		final RuntimeException taskException = new RuntimeException("Task fehlgeschlagen");
		final RuntimeException rollbackException = new RuntimeException("Rollback fehlgeschlagen");
		doThrow(rollbackException).when(mockedConn).transactionRollbackOrThrow();

		final RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			TransactionSupport.transactional((Runnable) () -> {
				throw taskException;
			});
		});

		assertEquals(taskException, thrown);
		// Prüfe auf angehängte Suppressed Exception
		assertEquals(1, thrown.getSuppressed().length);
		assertEquals(rollbackException, thrown.getSuppressed()[0]);
	}

	@Test
	@DisplayName("Runnable Task: Existiert bereits eine aktive Transaktion, so wird keine neue Transaktion gestartet, der Task aber dennoch ausgeführt.")
	void testTransactionalRunnableExistingTransaction() {
		when(mockedConn.hasActiveTransaction()).thenReturn(true);

		TransactionSupport.transactional(() -> {
			/* mache irgendetwas */ });

		// Es wird keine Methode zur Steuerung der Transaktion aufgerufen.
		verify(mockedConn, never()).transactionBegin();
		verify(mockedConn, never()).transactionCommitOrThrow();
		verify(mockedConn, never()).transactionRollbackOrThrow();
	}


	@Test
	@DisplayName("Runnable Task: Edge-Case - Der Aufruf der Rollback-Methode im finally-Block wirft eine Exception, obwohl im Task keine Auftritt")
	void testTransactionalRunnableNurRollbackFehler() {
		when(mockedConn.hasActiveTransaction()).thenReturn(false);

		// Edge-Case: Der Aufruf der Rollback-Methode im finally-Block wirft eine Exception, obwohl im Task keine Auftritt -> wird ignoriert
		final RuntimeException rollbackException = new RuntimeException("Exception beim Rollback im finally-Block");
		doThrow(rollbackException).when(mockedConn).transactionRollbackOrThrow();

		// Der Task wird erfolgreich ausgeführt
		final RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			TransactionSupport.transactional(() -> {
				/* mache irgendetwas */ });
		});

		assertEquals(rollbackException, thrown);

		// Die Rollback-Methode wurde dabei auch aufgerufen
		verify(mockedConn, times(1)).transactionRollbackOrThrow();
	}

	@Test
	@DisplayName("Runnable Task: Im Task tritt ein Fehler auf, was zu einem erfolgreichen Rollback führt.")
	void testTransactionalRunnableNurTaskFehler() {
		when(mockedConn.hasActiveTransaction()).thenReturn(false);

		final RuntimeException taskException = new RuntimeException("Einfacher Task Fehler");
		final RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			TransactionSupport.transactional((Runnable) () -> {
				throw taskException;
			});
		});

		assertEquals(taskException, thrown);
		assertEquals(0, thrown.getSuppressed().length, "Es sollte keine Suppressed Exception geben, wenn der Rollback erfolgreich war.");
		verify(mockedConn).transactionRollbackOrThrow();
	}

}
