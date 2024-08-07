package de.svws_nrw.schulen.v1.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.schulen.v1.data.Schuldatei;
import de.svws_nrw.schulen.v1.data.SchuldateiKataloge;
import de.svws_nrw.schulen.v1.data.SchuldateiKatalogeintrag;
import de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheit;
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
	private final @NotNull Map<String, SchuldateiKatalogManager> _mapKataloge = new HashMap<>();

	/** Der Katalog der Arten von Adressen */
	public final @NotNull SchuldateiKatalogManager katalogAddressarten;

	/** Der Katalog der Arten von Attributen */
	public final @NotNull SchuldateiKatalogManager katalogAttribute;

	/** Der Katalog der Schulbetriebsschlüssel (Betriebsschlüssel)*/
	public final @NotNull SchuldateiKatalogManager katalogSchulbetriebsschluessel;

	/** Der Katalog der Erreichbarkeiten */
	public final @NotNull SchuldateiKatalogManager katalogErreichbarkeiten;

	/** Der Katalog der Gliederung */
	public final @NotNull SchuldateiKatalogManager katalogGliederungen;

	/** Der Katalog der Hauptstandortart (Hauptstandortadresse) */
	public final @NotNull SchuldateiKatalogManager katalogHauptstandort;

	/** Der Katalog der Art von Heimen/Internaten */
	public final @NotNull SchuldateiKatalogManager katalogHeimInternat;

	/** Der Katalog der Kommunikationsgruppen */
	public final @NotNull SchuldateiKatalogManager katalogKommunikationsgruppen;

	/** Der Katalog der Liegenschaften */
	public final @NotNull SchuldateiKatalogManager katalogLiegenschaftsarten;

	/** Der Katalog der Merkmal */
	public final @NotNull SchuldateiKatalogManager katalogMerkmale;

	/** Der Katalog der Eigenschaften von Organisationseinheiten */
	public final @NotNull SchuldateiKatalogManager katalogOergangisationseinheitEigenschaften;

	/** Der Katalog der Arten von Organisationseinheiten*/
	public final @NotNull SchuldateiKatalogManager katalogOrganisationseinheitarten;

	/** Der Katalog der Qualitäten zu den Ortskoordinaten */
	public final @NotNull SchuldateiKatalogManager katalogQualitaetenVerortung;

	/** Der Katalog des Rechtstatus von Schulen */
	public final @NotNull SchuldateiKatalogManager katalogRechtsstatus;

	/** Der Katalog der Schließungsgründe */
	public final @NotNull SchuldateiKatalogManager katalogSchliessungsGruende;

	/** Der Katalog der Schularten */
	public final @NotNull SchuldateiKatalogManager katalogSchularten;

	/** Der Katalog der Schulaufsichten */
	public final @NotNull SchuldateiKatalogManager katalogSchulaufsicht;

	/** Der Katalog der Schulformen */
	public final @NotNull SchuldateiKatalogManager katalogSchulformen;

	/** Der Katalog der Schulträger */
	public final @NotNull SchuldateiKatalogManager katalogArtDerTraegerschaft;

	/** Eine Map mit den Managern für alle Organisationseinheiten, welche den Schulnummern ihrer Organisationseinheiten zugeordnet sind */
	private final @NotNull Map<Integer, SchuldateiOrganisationseinheitManager> _mapOrganisationseinheitManagerBySchulnummer = new HashMap<>();


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
			final SchuldateiKatalogManager katalog =
					_mapKataloge.computeIfAbsent(eintrag.katalog, (final @NotNull String k) -> new SchuldateiKatalogManager(k));
			if (katalog != null)
				katalog.addEintrag(eintrag);
		}
		katalogAddressarten = getKatalogFromMap("ArtDerAdresse");
		katalogAttribute = getKatalogFromMap("Attribut");
		katalogSchulbetriebsschluessel = getKatalogFromMap("Betriebsschluessel");
		katalogErreichbarkeiten = getKatalogFromMap("Erreichbarkeit");
		katalogGliederungen = getKatalogFromMap("Gliederung");
		katalogHauptstandort = getKatalogFromMap("Hauptstandortadresse");
		katalogHeimInternat = getKatalogFromMap("HeimInternat");
		katalogKommunikationsgruppen = getKatalogFromMap("Kommunikationsgruppe");
		katalogLiegenschaftsarten = getKatalogFromMap("LiegenschaftArt");
		katalogMerkmale = getKatalogFromMap("Merkmal");
		katalogOergangisationseinheitEigenschaften = getKatalogFromMap("OE_Eigenschaften");
		katalogOrganisationseinheitarten = getKatalogFromMap("OrganisationseinheitArt");
		katalogQualitaetenVerortung = getKatalogFromMap("QualitaetVerortung");
		katalogRechtsstatus = getKatalogFromMap("Rechtsstatus");
		katalogSchliessungsGruende = getKatalogFromMap("SchliessungGrund");
		katalogSchularten = getKatalogFromMap("Schulart");
		katalogSchulaufsicht = getKatalogFromMap("Schulaufsicht");
		katalogSchulformen = getKatalogFromMap("Schulform");
		katalogArtDerTraegerschaft = getKatalogFromMap("Traeger");

		// Durchwandere die Organisationseinheiten und erzeuge Manager für jede Organisationseinheit
		for (final @NotNull SchuldateiOrganisationseinheit organisationseinheit : schuldatei.organisationseinheit) {
			// Prüfe, ob die Schulnummer schonmal eingelesen wurde. In diesem Fall sind die Daten der Schuldatei inkonsistent
			if (_mapOrganisationseinheitManagerBySchulnummer.containsKey(organisationseinheit.schulnummer))
				throw new IllegalArgumentException("Die Liste mit den Organisationseinheiten enthält mindestens einen doppelten Eintrag (Schulnummer "
						+ organisationseinheit.schulnummer + ")");
			_mapOrganisationseinheitManagerBySchulnummer.put(organisationseinheit.schulnummer,
					new SchuldateiOrganisationseinheitManager(this, organisationseinheit));
		}

		// Durchwandere die Organisationseinheiten und prüfe die Referenzen auf andere Organisationseinheiten
		for (final @NotNull SchuldateiOrganisationseinheitManager manager : this._mapOrganisationseinheitManagerBySchulnummer.values()) {
			manager.validateOeReferenzen();
		}
	}


	/**
	 * Gibt die Liste aller Organisationseinheiten der Schuldatei zurück.
	 *
	 * @return die Liste aller Organisationseinheiten der Schuldatei
	 */
	public @NotNull List<SchuldateiOrganisationseinheit> getList() {
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
