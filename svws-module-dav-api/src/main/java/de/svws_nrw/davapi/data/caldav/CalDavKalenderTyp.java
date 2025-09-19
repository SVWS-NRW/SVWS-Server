package de.svws_nrw.davapi.data.caldav;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.kalender.Kalender;
import de.svws_nrw.core.types.dav.DavRessourceCollectionTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Eine Aufzählung der verfügbaren Kalender
 */
public enum CalDavKalenderTyp {

	/** Ein Kalender für den Stundenplan */
	STUNDENPLAN("Stundenplan", "Ein generierter Kalender für den Stundenplan", "Generiert"),

	/** Ein persönlicher Kalender */
	PERSOENLICH("Eigener Kalender", "Ihr persönlicher Kalender", "Persönlich"),

	/** Der öffentliche Kalender der Schule */
	OEFFENTLICH("Öffentlich", "Der öffentliche Kalender der Schule", "Öffentlich");


	/** Die ID des Kalenders */
	public final @NotNull String id;

	/** Der Typ des Kalenders (Persönlich, Generiert oder Öffentlich) */
	public final @NotNull String typ;

	/** Der Anzeigename des Kalenders */
	public final @NotNull String displayname;

	/** Die Beschreibung des Kalenders */
	public final @NotNull String beschreibung;

	/** Eien Map für den schnellen Zugriff auf einen Kalender anhand des Typs */
	private static @NotNull Map<String, CalDavKalenderTyp> mapKalenderByID = new HashMap<>();


	/**
	 * Erstellt einen neuen Eintrag in der Aufzählung der Kalender, welcher den Anzeigename und den Typ beinhaltet.
	 *
	 * @param displayname    der Anzeigename
	 * @param beschreibung   die Beschreibung
	 * @param typ            der Typ des Kalenders (Persönlich, Generiert oder Öffentlich)
	 */
	CalDavKalenderTyp(final @NotNull String displayname, final @NotNull String beschreibung, final @NotNull String typ) {
		this.id = this.toString().toLowerCase();
		this.typ = typ;
		this.displayname = displayname;
		this.beschreibung = beschreibung;
	}


	/**
	 * Gibt die ID für einen beschreibbaren Kalender zurück. Dabei wird die ID der Collection
	 * aus der Datenbank in der String integriert.
	 *
	 * @param id   die ID der Kalender-Collection aus der Datenbank
	 *
	 * @return die ID für den Kalender, z.B. "Persönlich_4711"
	 */
	public String getId(final long id) {
		return this.id + "_" + id;
	}


	/**
	 * Gibt den Kalender-Typ anhand der angegebene Kalender-ID zurück.
	 * Ist die ID ungültig, so wird null zurückgegeben.
	 *
	 * @param idCal   die ID des Kalenders
	 *
	 * @return der Typ des Kalenders oder null
	 */
	public static CalDavKalenderTyp getByID(final @NotNull String idCal) {
		if (mapKalenderByID.isEmpty())
			for (final @NotNull CalDavKalenderTyp cal : CalDavKalenderTyp.values())
				mapKalenderByID.put(cal.id, cal);
		final String[] parts = idCal.split("_", 2);
		if (parts.length == 0)
			return null;
		return mapKalenderByID.get(parts[0]);
	}


	/**
	 * Gibt die ID der Collection in der Datenbank anhand der ID des Kalenders zurück, sofern es sich um einen beschreibbaren
	 * Kalender handeln soll.
	 *
	 * @param idCal   die ID des Kalenders
	 *
	 * @return der Eintrag oder null
	 */
	public Long getDbId(final String idCal) {
		if (getByID(idCal) != this)
			return null;
		final String[] parts = idCal.split("_", 2);
		if (parts.length != 2)
			return null;
		try {
			return Long.parseLong(parts[1]);
		} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
			return null;
		}
	}


	/**
	 * Gibt einen neuen Kalender für die angegebene ID zurück.
	 * Ist die ID ungültig, so wird null zurückgegeben.
	 *
	 * @param id   die ID des Kalenders
	 *
	 * @return der Eintrag oder null
	 */
	public static Kalender getKalenderByID(final @NotNull String id) {
		final CalDavKalenderTyp cal = getByID(id);
		return (cal == null) ? null : cal.toKalender();
	}


	/**
	 * Erstellt einen neuen Kalender basierend auf dem Eintrag der Aufzählung. Dieses kann später durch weitere
	 * Informationen, wie Besitzer, Lese-Schreib-Berechtigungen, dem SyncToken oder den Kalender-Einträgen ergänzt werden.
	 *
	 * @return der neue Kalender
	 */
	public @NotNull Kalender toKalender() {
		final Kalender a = new Kalender();
		a.id = this.id;
		a.kalenderTyp = this.typ;
		a.beschreibung = this.beschreibung;
		a.displayname = this.displayname;
		return a;
	}


	/**
	 * Erstellt eine Liste aller bisher implementierten Kalender.
	 *
	 * @return die Liste der Kalender
	 */
	public static @NotNull List<Kalender> getKalender() {
		final @NotNull List<Kalender> result = new ArrayList<>();
		result.add(CalDavKalenderTyp.PERSOENLICH.toKalender());
		// TODO die weiteren Kalender implementieren (siehe Aufzählung oben)
		return result;
	}


	/**
	 * Gibt den Typ des Kalenders für den übergebenen Collection-Type zurück. Kann kein
	 * passender Typ zugeordnet werden, so wird null zurückgegeben.
	 *
	 * @param typeCollection  der Typ der DAV-Collection
	 *
	 * @return der Kalender-Typ oder null
	 */
	public static CalDavKalenderTyp getByCollectionType(final DavRessourceCollectionTyp typeCollection) {
		return switch (typeCollection) {
			case KALENDER -> CalDavKalenderTyp.OEFFENTLICH;
			case EIGENER_KALENDER -> CalDavKalenderTyp.PERSOENLICH;
			default -> null;
		};
	}

}
