package de.svws_nrw.schuldatei.v1.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.schuldatei.v1.data.Schuldatei;
import de.svws_nrw.schuldatei.v1.data.SchuldateiKataloge;
import de.svws_nrw.schuldatei.v1.data.SchuldateiKatalogeintrag;
import de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheit;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse dient dem Verwalten der Daten zu den Organisationseinheiten
 * aus der Schuldatei.
 */
public final class SchuldateiManager {

	/** Die Daten der Schuldatei */
	private final @NotNull Schuldatei _schuldatei;

	/** Die Kataloge zu der Schuldatei */
	private final @NotNull SchuldateiKataloge _kataloge;

	/** Die Kataloge zu der Schuldatei anhand ihrer Namen */
	private final @NotNull Map<@NotNull String, @NotNull SchuldateiKatalogManager> _mapKataloge = new HashMap<>();

	/** Eine Map mit allen Organisationseinheiten, welche ihrer Schulnummer zugeordnet sind */
	private final @NotNull Map<@NotNull Integer, @NotNull SchuldateiOrganisationseinheit> _mapOrganisationseinheitenBySchulnummer = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen
	 * Organisationseinheiten der Schuldatei.
	 *
	 * @param schuldatei   die Schuldatei
	 * @param kataloge     die Kataloge zu der Schuldatei
	 */
	public SchuldateiManager(final @NotNull Schuldatei schuldatei, final @NotNull SchuldateiKataloge kataloge) {
		this._schuldatei = schuldatei;
		this._kataloge = kataloge;
		// Durchwandere die Katalog-Einträge und erzeuge die zugehörigen Katalog-Manager
		for (final @NotNull SchuldateiKatalogeintrag eintrag : kataloge.katalog) {
			final SchuldateiKatalogManager katalog = _mapKataloge.computeIfAbsent(eintrag.katalog, (final @NotNull String k) -> new SchuldateiKatalogManager(k));
			if (katalog != null) {
				katalog.addEintrag(eintrag);
			}
		}
		// Durchwandere die Organisationseinheiten
		for (final @NotNull SchuldateiOrganisationseinheit organisationseinheit : schuldatei.organisationseinheit) {
			// Prüfe, ob die Schulnummer schonmal eingelesen wurde. In diesem Fall sind die Daten der Schuldatei inkonsistent
			if (_mapOrganisationseinheitenBySchulnummer.containsKey(organisationseinheit.schulnummer))
				throw new IllegalArgumentException("Die Liste mit den Organisationseinheiten enthält mindestens einen doppelten Eintrag (Schulnummer " + organisationseinheit.schulnummer + ")");
			_mapOrganisationseinheitenBySchulnummer.put(organisationseinheit.schulnummer, organisationseinheit);
		}
	}


	/**
	 * Gibt die Liste aller Organisationseinheiten der Schuldatei zurück.
	 *
	 * @return die Liste aller Organisationseinheiten der Schuldatei
	 */
	public @NotNull List<@NotNull SchuldateiOrganisationseinheit> getList() {
		return this._schuldatei.organisationseinheit;
	}


	/**
	 * Gibt die Organisationseinheit für die übergebene Schulnummer zurück.
	 *
	 * @param schulnummer   die Schulnummer
	 *
	 * @return die Organisationseinheit
	 */
	public SchuldateiOrganisationseinheit getOrganisationsheinheitBySchulnummer(final @NotNull Integer schulnummer) {
		return this._mapOrganisationseinheitenBySchulnummer.get(schulnummer);
	}

}
