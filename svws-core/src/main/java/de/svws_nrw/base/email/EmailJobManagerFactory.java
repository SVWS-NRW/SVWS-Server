package de.svws_nrw.base.email;

import java.util.HashMap;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse verwaltet die Instanzen der EmailJobManager und erstellt
 * bei Bedarf neue Job-Manager. Die Factory selber ist ein Singleton.
 */
public final class EmailJobManagerFactory {

	/** Die Instanz der Factory sofern auf diese zugegriffen wurde */
	private static EmailJobManagerFactory instance = null;


	/** Gibt an, ob die Factory aktiv ist und neue {@link EmailJobManager} erstellt werden dürfen. */
	private boolean active = true;

	/** Eine Map für alle Instanzen des Email-Job-Managers. Die einzelnen Job-Manager werden für jedes Schema und jeden Benutzer individuell erzeugt. */
	private final @NotNull HashMap<String, HashMap<Long, EmailJobManager>> mapInstances = new HashMap<>();


	/**
	 * Die Factory ist ein Singleton, somit ist der Konstruktor privat
	 */
	private EmailJobManagerFactory() {
		// nichts zu tun
	}

	/**
	 * Gibt die Instanz der Factory zurück.
	 *
	 * @return die Instanz der Factory
	 */
	public static EmailJobManagerFactory getInstance() {
		if (instance == null)
			instance = new EmailJobManagerFactory();
		return instance;
	}


	/**
	 * Aktiviert die Möglichkeit Email-Job-Manager zu erstellen.
	 * Dieser Zustand ist als Default nach dem Start eingestellt.
	 */
	public synchronized void activate() {
		active = true;
	}


	/**
	 * Deaktiviert die Möglichkeit Email-Job-Manager zu erstellen.
	 * Dies wird z.B. beim Beenden des Programms aufgerufen, um die Erstellung neuer Threads zu verhindern.
	 */
	public synchronized void deactivate() {
		active = false;
	}


	/**
	 * Gibt zurück, ob die Factory aktiv ist oder nicht. Ist sie inaktiv, so können
	 * keine neuen {@link EmailJobManager} erzeugt werden.
	 *
	 * @return true, wenn sie aktiv ist, und ansonsten false
	 */
	public synchronized boolean isActive() {
		return active;
	}


	/**
	 * Liefert die Instanz des Email-Job-Managers für den übergebenen Kontext.
	 *
	 * @param context   der Job-Manager-Kontext mit der Benutzerinformation (Datenbankschemas und Benutzer-ID),
	 *                  der SMTP-Session und weiteren Konfigurationseinstellungen
	 *
	 * @return die Instanz des {@link EmailJobManager}
	 */
	public synchronized @NotNull EmailJobManager getManager(final @NotNull EmailJobManagerContext context) {
		if (context == null)
			throw new IllegalArgumentException("Ohne einen Job-Manager-Context kann kein Manager erzeugt werden.");
		if (!active)
			throw new IllegalArgumentException("Die Klasse EmailJobManager ist nicht mehr aktiv. Es kann daher kein neuer Manager mehr erstellt werden.");
		final HashMap<Long, EmailJobManager> tmp = mapInstances.computeIfAbsent(context.getDBSchema(), k -> new HashMap<>());
		if (tmp == null) // kann nicht eintreten
			throw new IllegalArgumentException("Fehler beim Erstellen einer HashMap in der Methode EmailJobManager.getManager(...)");
		final EmailJobManager jobManager = tmp.computeIfAbsent(context.getUserId(), id -> new EmailJobManager(context));
		if (jobManager == null) // kann nicht eintreten
			throw new IllegalArgumentException("Fehler beim Erstellen eines neuen Managers in der Methode EmailJobManager.getManager(...)");
		return jobManager;
	}


	/**
	 * Liefert die Instanz des Job-Managers für die übergebenen Benutzerinformationen, sofern eine solche Instanz vorhanden ist.
	 *
	 * @param schema    das SVWS-Datenbank-Schema
	 * @param idUser    die Benutzer-ID aus dem SVWS-Datenbank-Schema
	 *
	 * @return die Instanz des {@link EmailJobManager} oder null, falls keine Instanz des Job-Managers für den Benutzer vorhanden ist
	 */
	public synchronized EmailJobManager getManagerByUser(final @NotNull String schema, final long idUser) {
		final HashMap<Long, EmailJobManager> tmp = mapInstances.get(schema);
		if (tmp == null)
			return null;
		return tmp.get(idUser);
	}


	/**
	 * Stoppt den Thread für den Manager, welcher dem übergebenen Datenbank-Schema und der übergebenen
	 * Benutzer-ID zugeordnet ist, und entfernt den Manager aus der Liste der Job-Manager-Instanzen.
	 *
	 * @param schema   der Name des Datenbankschemas
	 * @param idUser   die Benutzer-ID
	 */
	public synchronized void freeManager(final @NotNull String schema, final long idUser) {
		final HashMap<Long, EmailJobManager> tmp = mapInstances.get(schema);
		if (tmp == null)
			return;
		final EmailJobManager jobManager = tmp.get(idUser);
		if (jobManager == null)
			return;
		jobManager.shutdown();
		tmp.remove(idUser);
	}


	/**
	 * Stoppt alle Threads aus allen Job-Manager-Instanzen und entfernt diese auch aus der Liste
	 * der Job-Manager Instanzen.
	 */
	public synchronized void freeAllManager() {
		for (final var tmp : mapInstances.values()) {
			for (final var jobManager : tmp.values())
				jobManager.shutdown();
			tmp.clear();
		}
		mapInstances.clear();
	}

}
