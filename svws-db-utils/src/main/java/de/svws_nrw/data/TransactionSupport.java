package de.svws_nrw.data;

import java.util.function.Supplier;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.repo.DbConnectionProvider;

/**
 * Eine Hilfsklasse für das Ausführen von Transaktionen
 */
public final class TransactionSupport {

	private TransactionSupport() {
		// Dies ist eine Utility-Klasse ohne eigene Instanzen
	}

	/**
	 * Führt den angegebenen Task in einer Transaktion auf der für den Request genutzten Connection aus.
	 *
	 * @param <T>    der Typ des Rückgabe-Wertes
	 * @param task   der auszuführende Task
	 *
	 * @return der Rückgabe-Wert
	 */
	public static <T> T transactional(final Supplier<T> task) {
		final DBEntityManager conn = DbConnectionProvider.getConnection();

		// Prüfe, ob eine Transaktion schon offen ist, dann wird der Task direkt aufgerufen
		if (conn.hasActiveTransaction())
			return task.get();

		// Starte eine Transaktion und führe den Task in dieser Transaktion aus
		Throwable taskException = null; // Speichert eine Exception zwischen, um im finally entscheiden zu können, ob noch auf eine aktive Transaktion geprüft wird
		try {
			conn.transactionBegin();
			final T response = task.get();
			conn.transactionCommitOrThrow();
			return response;
		} catch (final Exception t) {
			taskException = t;
			try {
				// Führe einen Rollback aus
				conn.transactionRollbackOrThrow();
			} catch (final Exception rollbackException) {
				// Hänge die Exeption vom Rollback an die Exception vom Task an, um Shadowing zu vermeiden
				t.addSuppressed(rollbackException);
			}
			throw t;
		} finally {
			// Prüfe, für den Fall, dass keine Exception im try-Block aufgetreten ist...
			if (taskException == null) {
				// ... ob in einem Ausnahmefall das Commit eine aktive Transaktion übergelassen hat und ein Fall für ein Rollback vorliegt
				conn.transactionRollbackOrThrow();
			}
		}
	}


	/**
	 * Führt den angegebenen Task in einer Transaktion auf der für den Request genutzten Connection aus.
	 *
	 * @param task   der auszuführende Task
	 */
	public static void transactional(final Runnable task) {
		// Verwende die obige Supplier-Variante mit einem Object-Rückgabe-Wert null...
		transactional(() -> {
			task.run();
			return null;
		});
	}

}
