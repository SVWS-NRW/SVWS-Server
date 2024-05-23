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

	/** Der Katalog der Arten von Organisationseinheiten*/
	public final @NotNull SchuldateiKatalogManager katalogOrganisationseinheitarten;

	/** Der Katalog der Erreichbarkeiten */
	public final @NotNull SchuldateiKatalogManager katalogErreichbarkeiten;

	/** Der Katalog der Schulformen */
	public final @NotNull SchuldateiKatalogManager katalogSchulformen;

	/** Der Katalog der Liegenschaften */
	public final @NotNull SchuldateiKatalogManager katalogLiegenschaftsarten;

	/** Der Katalog der Schulträger */
	public final @NotNull SchuldateiKatalogManager katalogTraeger;

	/** Der Katalog der Betriebsschlüssel */
	public final @NotNull SchuldateiKatalogManager katalogBetriebsschluessel;

	/** Der Katalog der Kommunikationsgruppen */
	public final @NotNull SchuldateiKatalogManager katalogKommunikationsgruppen;

	/** Der Katalog der Art von Heimen/Internaten */
	public final @NotNull SchuldateiKatalogManager katalogHeimInternat;

	/** Der Katalog der Schularten */
	public final @NotNull SchuldateiKatalogManager katalogSchularten;

	/** Der Katalog des Rechtstatus von Schulen */
	public final @NotNull SchuldateiKatalogManager katalogRechtsstatus;

	/** Der Katalog der Schließungsgründe */
	public final @NotNull SchuldateiKatalogManager katalogSchliessungsGrund;

	/** Der Katalog der Schulaufsichten */
	public final @NotNull SchuldateiKatalogManager katalogSchulaufsicht;

	/** Der Katalog der Eigenschaften von Organisationseinheiten */
	public final @NotNull SchuldateiKatalogManager katalogOergangisationseinheitEigenschaften;

	/** Der Katalog der Arten von Adressen */
	public final @NotNull SchuldateiKatalogManager katalogAddressarten;

	/** Eine Map mit den Managern für alle Organisationseinheiten, welche den Schulnummern ihrer Organisationseinheiten zugeordnet sind */
	private final @NotNull Map<@NotNull Integer, @NotNull SchuldateiOrganisationseinheitManager> _mapOrganisationseinheitManagerBySchulnummer = new HashMap<>();


	/**
	 * Ermittelt den Manager für den Katalog mit angegebenen Namen aus der übergebenen Map der Katalog-Manager.
	 *
	 * @param nameKatalog   der Name des Katalogs
	 *
	 * @return der Katalog-Manager
	 *
	 * @throws IllegalArgumentException falls kein Katalog mit dem Namen existiert
	 */
	private @NotNull SchuldateiKatalogManager getKatalogFromMap(final @NotNull String nameKatalog) throws IllegalArgumentException {
		final SchuldateiKatalogManager katalog = _mapKataloge.get(nameKatalog);
		if (katalog == null)
			throw new IllegalArgumentException("Die Kataloge enthält keine Einträge für den Katalog mit dem Namen '" + nameKatalog + "'");
		return katalog;
	}


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
			if (katalog != null)
				katalog.addEintrag(eintrag);
		}
		katalogOrganisationseinheitarten = getKatalogFromMap("OrganisationseinheitArt");
		katalogErreichbarkeiten = getKatalogFromMap("Erreichbarkeit");
		katalogSchulformen = getKatalogFromMap("Schulform");
		katalogLiegenschaftsarten = getKatalogFromMap("LiegenschaftArt");
		katalogTraeger = getKatalogFromMap("Traeger");
		katalogBetriebsschluessel = getKatalogFromMap("Betriebsschluessel");
		katalogKommunikationsgruppen = getKatalogFromMap("Kommunikationsgruppe");
		katalogHeimInternat = getKatalogFromMap("HeimInternat");
		katalogSchularten = getKatalogFromMap("Schulart");
		katalogRechtsstatus = getKatalogFromMap("Rechtsstatus");
		katalogSchliessungsGrund = getKatalogFromMap("SchliessungGrund");
		katalogSchulaufsicht = getKatalogFromMap("Schulaufsicht");
		katalogOergangisationseinheitEigenschaften = getKatalogFromMap("OE_Eigenschaften");
		katalogAddressarten = getKatalogFromMap("ArtDerAdresse");

		// Durchwandere die Organisationseinheiten
		for (final @NotNull SchuldateiOrganisationseinheit organisationseinheit : schuldatei.organisationseinheit) {
			// Prüfe, ob die Schulnummer schonmal eingelesen wurde. In diesem Fall sind die Daten der Schuldatei inkonsistent
			if (_mapOrganisationseinheitManagerBySchulnummer.containsKey(organisationseinheit.schulnummer))
				throw new IllegalArgumentException("Die Liste mit den Organisationseinheiten enthält mindestens einen doppelten Eintrag (Schulnummer " + organisationseinheit.schulnummer + ")");
			_mapOrganisationseinheitManagerBySchulnummer.put(organisationseinheit.schulnummer, new SchuldateiOrganisationseinheitManager(this, organisationseinheit));
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
	 * Gibt den Manager für die Organisationseinheit mit der übergebenen Schulnummer zurück.
	 *
	 * @param schulnummer   die Schulnummer
	 *
	 * @return der Manager für die Organisationseinheit
	 */
	public SchuldateiOrganisationseinheitManager getOrganisationsheinheitManager(final @NotNull Integer schulnummer) {
		return this._mapOrganisationseinheitManagerBySchulnummer.get(schulnummer);
	}

}
