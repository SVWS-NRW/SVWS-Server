package de.svws_nrw.schuldatei.v1.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.schuldatei.v1.data.Schuldatei;
import de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheit;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse dient dem Verwalten der Daten zu den Organisationseinheiten
 * aus der Schuldatei.
 */
public final class SchuldateiManager {

	/** Die Daten der Schuldatei */
	private final @NotNull Schuldatei schuldatei;

	/** Eine Map mit allen Organisationseinheiten, welche ihrer Schulnummer zugeordnet sind */
	private final @NotNull Map<@NotNull Integer, @NotNull SchuldateiOrganisationseinheit> mapOrganisationseinheitenBySchulnummer = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen
	 * Organisationseinheiten der Schuldatei.
	 *
	 * @param daten   die Liste mit den Organisationseinheiten der Schuldatei
	 */
	public SchuldateiManager(final @NotNull Schuldatei daten) {
		this.schuldatei = daten;
		// Durchwandere die Organisationseinheiten
		for (final @NotNull SchuldateiOrganisationseinheit organisationseinheit : schuldatei.organisationseinheit) {
			// Prüfe, ob die Schulnummer schonmal eingelesen wurde. In diesem Fall sind die Daten der Schuldatei inkonsistent
			if (mapOrganisationseinheitenBySchulnummer.containsKey(organisationseinheit.schulnummer))
				throw new IllegalArgumentException("Die Liste mit den Organisationseinheiten enthält mindestens einen doppelten Eintrag (Schulnummer " + organisationseinheit.schulnummer + ")");
			mapOrganisationseinheitenBySchulnummer.put(organisationseinheit.schulnummer, organisationseinheit);
		}
	}


	/**
	 * Gibt die Liste aller Organisationseinheiten der Schuldatei zurück.
	 *
	 * @return die Liste aller Organisationseinheiten der Schuldatei
	 */
	public @NotNull List<@NotNull SchuldateiOrganisationseinheit> getList() {
		return this.schuldatei.organisationseinheit;
	}


	/**
	 * Gibt die Organisationseinheit für die übergebene Schulnummer zurück.
	 *
	 * @param schulnummer   die Schulnummer
	 *
	 * @return die Organisationseinheit
	 */
	public SchuldateiOrganisationseinheit getOrganisationsheinheitBySchulnummer(final @NotNull Integer schulnummer) {
		return this.mapOrganisationseinheitenBySchulnummer.get(schulnummer);
	}

}
