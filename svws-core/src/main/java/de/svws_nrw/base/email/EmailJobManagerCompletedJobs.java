package de.svws_nrw.base.email;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient dem Job-Manager zur Verwaltung von abgeschlossenen Jobs. Sind in der
 * internen Liste der abgeschlossenen Jobs noch Einträge enthalten, so werden diese
 * nach einer angegebenen Zeit über einen Aufräum-Thread automatisch aus der Liste entfernt.
 * Der Job-Manager wird daraufhin über das Aufräumen informiert.
 */
class EmailJobManagerCompletedJobs {

	/** Der E-Mail-Job-Manager, dessen abgeschlossene Jobs hier verwaltet werden */
	private final @NotNull EmailJobManager manager;

	/** Die Menge der abgeschlossenen Jobs */
	private final Set<EmailJob> jobsCompleted = new HashSet<>();

	/** Der ThreadBuilder, um Threads zum Aufräumen von abgeschlossenen Threads zu Erzeugen */
	private final Thread.Builder threadBuilder;


	/**
	 * Erstellt ein Objekt zur Verwaltung für abgeschlossene Jobs eines Job-Managers
	 *
	 * @param manager   der Job-Manager
	 */
	EmailJobManagerCompletedJobs(final @NotNull EmailJobManager manager) {
		this.manager = manager;
		final @NotNull String threadName = "EmailJobManagerCompletedJobs_" + manager.getContext().getDBSchema() + "_" + manager.getContext().getUserId() + "_";
		this.threadBuilder = Thread.ofVirtual().name(threadName, 1);
	}


	/**
	 * Fügt einen abgeschlossenen Job zur Verwaltung hinzu und startet den zugehörigen Aufräum-Thread.
	 * Der Status des Jobs muss bereits vor dem Aufruf dieser Methode korrekt gesetzt worden sein.
	 *
	 * @param job   der abgeschlossene E-Mail-Job
	 */
	synchronized void add(final @NotNull EmailJob job) {
		this.jobsCompleted.add(job);
		// Starte den Thread zum Aufräumen des Jobs
		threadBuilder.start(() -> waitForRemoval(job));
	}


	/**
	 * Entfernt alle Jobs sofort, ohne die vorgegebene Zeit abzuwarten.
	 */
	synchronized void removeAll() {
		this.jobsCompleted.clear();
		this.notifyAll();
	}


	/**
	 * Wird von der add-Methode aufgerufen und wartet die vorgegebene Zeit, bevor
	 * der abgeschlossene Job endgültig aus dem Manager entfernt wird.
	 *
	 * @param job   der abgeschlossene Job
	 */
	private void waitForRemoval(final @NotNull EmailJob job) {
		synchronized (this) {
			final long zeitEnde = System.currentTimeMillis() + manager.getContext().getTimeToKeepCompletedJobs();

			long zeitRest = zeitEnde - System.currentTimeMillis();
			while ((zeitRest > 0) && (jobsCompleted.contains(job))) {
				try {
					this.wait(zeitRest);
				} catch (@SuppressWarnings("unused") final InterruptedException e) {
					Thread.currentThread().interrupt();
					// Auch bei Unterbrechung den Job aufräumen, damit er nicht dauerhaft in mapJobs verbleibt.
					break;
				}
				zeitRest = zeitEnde - System.currentTimeMillis();
			}

			this.jobsCompleted.remove(job);
			this.manager.removeCompletedJob(job.getId());
		}
	}

}
