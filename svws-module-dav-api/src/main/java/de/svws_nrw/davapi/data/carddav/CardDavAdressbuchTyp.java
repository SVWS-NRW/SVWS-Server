package de.svws_nrw.davapi.data.carddav;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.adressbuch.Adressbuch;
import jakarta.validation.constraints.NotNull;

/**
 * Eine Aufzählung der verfügbaren Adressbuecher
 */
public enum CardDavAdressbuchTyp {

	/** Ein Adressbuch für Schüler-Kontaktdaten */
	SCHUELER("Schüler", "Ein generiertes Adressbuch mit allen Schülern", "Generiert"),

	/** Ein Adressbuch für Lehrer-Kontaktdaten */
	LEHRER("Lehrer", "Ein generiertes Adressbuch mit allen Lehrern", "Generiert"),

	/** Ein Adressbuch für Erzieher-Kontaktdaten */
	ERZIEHER("Erzieher", "Ein generiertes Adressbuch mit allen Erziehungsberechtigten", "Generiert"),

	/** Ein persönliches Adressbuch */
	PERSOENLICH("Persönlich", "Ihr persönliches Adressbuch", "Persönlich"),

	/** Ein Adressbuch mit Daten von Personengruppen */
	PERSONENGRUPPEN("Personengruppen", "Ein generiertes Adressbuch mit Personengruppen", "Generiert"),

	/** Das öffentliche Adressbuch der Schule */
	OEFFENTLICH("Öffentlich", "Das öffentliche Adressbuch der Schule", "Öffentlich");


	/** Die ID des Adressbuches */
	public final @NotNull String id;

	/** Der Typ des Adressuches (Persönlich, Generiert oder Öffentlich) */
	public final @NotNull String typ;

	/** Der Anzeigename des Adressbuches */
	public final @NotNull String displayname;

	/** Die Beschreibung des Adressbuches */
	public final @NotNull String beschreibung;

	/** Eien Map für den schnellen Zugriff auf ein Adressbuch anhand des Typs */
	private static @NotNull Map<String, CardDavAdressbuchTyp> mapAdressbuecherByID = new HashMap<>();


	/**
	 * Erstellt einen neuen Eintrag in der Aufzählung der Adressbücher, welcher den Anzeigename und den Typ beinhaltet.
	 *
	 * @param displayname    der Anzeigename
	 * @param beschreibung   die Beschreibung
	 * @param typ            der Typ des Adressuches (Persönlich, Generiert oder Öffentlich)
	 */
	CardDavAdressbuchTyp(final @NotNull String displayname, final @NotNull String beschreibung, final @NotNull String typ) {
		this.id = this.toString().toLowerCase();
		this.typ = typ;
		this.displayname = displayname;
		this.beschreibung = beschreibung;
	}


	/**
	 * Gibt den Adressbuch-Eintrag für die angegebene ID zurück.
	 * Ist die ID ungültig, so wird null zurückgegeben.
	 *
	 * @param id   die ID des Adressbuches
	 *
	 * @return der Eintrag oder null
	 */
	public static CardDavAdressbuchTyp getByID(final @NotNull String id) {
		if (mapAdressbuecherByID.isEmpty())
			for (final @NotNull CardDavAdressbuchTyp book : CardDavAdressbuchTyp.values())
				mapAdressbuecherByID.put(book.id, book);
		return mapAdressbuecherByID.get(id);
	}


	/**
	 * Gibt ein neues Adressbuch für die angegebene ID zurück.
	 * Ist die ID ungültig, so wird null zurückgegeben.
	 *
	 * @param id   die ID des Adressbuches
	 *
	 * @return der Eintrag oder null
	 */
	public static Adressbuch getAdressbuchByID(final @NotNull String id) {
		if (mapAdressbuecherByID.isEmpty())
			for (final @NotNull CardDavAdressbuchTyp book : CardDavAdressbuchTyp.values())
				mapAdressbuecherByID.put(book.id, book);
		final CardDavAdressbuchTyp book = mapAdressbuecherByID.get(id);
		return (book == null) ? null : book.toAdressbuch();
	}


	/**
	 * Erstellt ein neues Adressbuch basierend auf dem Eintrag der Aufzählung. Dieses kann später durch weitere
	 * Informationen, wie dem SyncToken oder den Kontakten ergänzt werden.
	 *
	 * @return das neue Adressbuch
	 */
	public @NotNull Adressbuch toAdressbuch() {
		final Adressbuch a = new Adressbuch();
		a.id = this.id;
		a.adressbuchTyp = this.typ;
		a.beschreibung = this.beschreibung;
		a.displayname = this.displayname;
		return a;
	}


	/**
	 * Erstellt eine Liste aller bisher implementierten Adressbücher.
	 *
	 * @return die Liste der Adressbücher
	 */
	public static @NotNull List<Adressbuch> getAdressbuecher() {
		final @NotNull List<Adressbuch> result = new ArrayList<>();
		result.add(CardDavAdressbuchTyp.SCHUELER.toAdressbuch());
		result.add(CardDavAdressbuchTyp.LEHRER.toAdressbuch());
		result.add(CardDavAdressbuchTyp.ERZIEHER.toAdressbuch());
		// TODO die weiteren Adressbücher implementieren (siehe Aufzählung oben)
		return result;
	}

}
