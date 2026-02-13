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

	/** Der Email-Job-Manager, dessen abgeschlossenen Jobs hier verwaltet werden */
	private final @NotNull EmailJobManager manager;

	/** Die Menge der abgeschlossenen Jobs */
	private final Set<EmailJob> jobsCompleted = new HashSet<>();

	/** Der ThreadBuilder, um Threads zum Aufräumen von abgeschlossenen Threads zur Erzeugen */
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
	 * Fügt einen job als abgeschlossen hinzu und setzt den übergebenen Status und
	 * den aktuellen Zeitstempel bei dem Job
	 *
	 * @param job      der Email-Job
	 * @param status   der Status des Email-Jobs
	 */
	synchronized void add(final @NotNull EmailJob job, final @NotNull EmailJobStatus status) {
		job.setStatus(status);
		this.jobsCompleted.add(job);
		// Starte den Thread zum Aufräumen des Jobs
		threadBuilder.start(() -> waitForRemoval(job));
	}


	/**
	 * Entfernt alle Jobs sofort ohne die vorgegebene Zeit abzuwarten.
	 */
	synchronized void removeAll() {
		this.notifyAll();
	}


	/**
	 * Wird von der add-Methode aufgerufen und wartet die vorgegebene Zeit bevor
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
					return; // "Job" bleibt in der Liste, da er ja nicht fertig wurde.
				}
				zeitRest = zeitEnde - System.currentTimeMillis();
			}

			this.jobsCompleted.remove(job);
			this.manager.removeCompletedJob(job.getId());
		}
	}

}
