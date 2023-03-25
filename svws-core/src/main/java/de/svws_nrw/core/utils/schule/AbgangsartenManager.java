package de.svws_nrw.core.utils.schule;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.data.schule.AbgangsartKatalog;
import de.svws_nrw.core.data.schule.AbgangsartKatalogDaten;
import de.svws_nrw.core.data.schule.AbgangsartKatalogEintrag;
import de.svws_nrw.core.types.schule.SchulabschlussAllgemeinbildend;
import de.svws_nrw.core.types.schule.SchulabschlussBerufsbildend;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Zugriff auf die Abgangsarten aus den Katalogen
 * für allgemeinbildende und berufsbildende Schulformen.   
 */
public class AbgangsartenManager {

	/** der Katalog für die allgemeinbildenden Schulformen */
	private final @NotNull AbgangsartKatalog _katalogAllgemein;

	/** der Katalog für die berufsbildenden Schulformen */
	private final @NotNull AbgangsartKatalog _katalogBeruf;

	/** Die Version der Daten für die Kombination der beiden Kataloge */
	private final long _version;

	/** Die kombinierten Daten der beiden Kataloge */
	private final @NotNull Vector<@NotNull AbgangsartKatalogEintrag> _alle = new Vector<>();

	/** Eine HashMap für den schnellen Zugriff auf die Abgangsarten anhand des Kürzels. */
	private final @NotNull HashMap<@NotNull String, @NotNull AbgangsartKatalogEintrag> _mapByKuerzel = new HashMap<>();

	/** Eine HashMap für den schnellen Zugriff auf die Abgangsarten anhand der ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull AbgangsartKatalogEintrag> _mapByID = new HashMap<>();

	/** Eine HashMap für den schnellen Zugriff auf die Daten der Abgangsarten anhand der ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull AbgangsartKatalogDaten> _mapDatenByID = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager für die möglichen Abgangsarten
	 * 
	 * @param katalogAllgemein   der Katalog für die allgemeinbildenden Schulformen
	 * @param katalogBeruf       der Katalog für die berufsbildenden Schulformen
	 */
	public AbgangsartenManager(final @NotNull AbgangsartKatalog katalogAllgemein, final @NotNull AbgangsartKatalog katalogBeruf) {
		this._katalogAllgemein = katalogAllgemein;
		this._katalogBeruf = katalogBeruf;
		this._version = katalogAllgemein.version + katalogBeruf.version;
		_alle.addAll(katalogAllgemein.eintraege);
		_alle.addAll(katalogBeruf.eintraege);
		for (final @NotNull AbgangsartKatalogEintrag eintrag : _alle) {
			this._mapByKuerzel.put(eintrag.kuerzel, eintrag);
			for (final @NotNull AbgangsartKatalogDaten daten : eintrag.historie) {
				final AbgangsartKatalogEintrag alt = this._mapByID.put(daten.id, eintrag);
				if (alt != null)
					throw new RuntimeException("Fehlerhafter Katalog: Doppelte ID '" + daten.id + "' bei den Abgangsarten '" + eintrag.kuerzel + "' und '" + alt.kuerzel + "'");
				this._mapDatenByID.put(daten.id, daten);
			}
		}
	}


	/**
	 * Gibt die Version der Daten im kombinierten Katalog für die allgemeinbildenden
	 * und dir berufsbildenden Schule zurück.
	 * 
	 * @return die Version
	 */
	public long getVersion() {
		return this._version;
	}


	/**
	 * Gibt den Katalog-Eintrag für das übergebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel des Katalog-Eintrags
	 * 
	 * @return der Katalog-Eintrag oder null, falls das Kürzel ungültig ist. 
	 */
	public AbgangsartKatalogEintrag get(final @NotNull String kuerzel) {
		return this._mapByKuerzel.get(kuerzel);
	}


	/**
	 * Gibt alle Katalog-Einträge zurück.
	 * 
	 * @return eine Liste mit allen Katalog-Einträgen
	 */
	public List<AbgangsartKatalogEintrag> getAll() {
		return this._alle;
	}


	/**
	 * Gibt die Katalog-Daten für das übergebene Kürzel 
	 * und das angegebene Schuljahr zurück.
	 * 
	 * @param kuerzel     das Kürzel des Katalog-Eintrags
	 * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 * 
	 * @return der Katalog-Eintrag oder null, falls das Kürzel ungültig ist oder der Katalog-Eintrag 
	 *         keine Daten für das übergebene Schuljahr hat 
	 */
	public AbgangsartKatalogDaten getDaten(final @NotNull String kuerzel, final int schuljahr) {
		final AbgangsartKatalogEintrag eintrag = this._mapByKuerzel.get(kuerzel);
		if (eintrag == null)
			return null;
		for (final @NotNull AbgangsartKatalogDaten daten : eintrag.historie)
			if (((daten.gueltigVon == null) || (daten.gueltigVon <= schuljahr)) &&
			    ((daten.gueltigBis == null) || (daten.gueltigBis >= schuljahr)))
				return daten;
		return null;
	}


	/**
	 * Gibt die Katalog-Daten für die Abgangsart zurück. 
	 * 
	 * @param id   die die des Katalog-Eintrags
	 * 
	 * @return die Daten für die ID oder null bei einer fehlerhaften ID
	 */
	public AbgangsartKatalogDaten getDaten(final long id) {
		return this._mapDatenByID.get(id);
	}


	/**
	 * Gibt das Kürzel für die Abgangsart mit der angebenen ID zurück.
	 * 
	 * @param id   die ID der Abgangsart
	 * 
	 * @return das Kürzel der Abgangsart oder null, falls die ID ungültig ist 
	 */
	public String getKuerzel(final long id) {
		final AbgangsartKatalogEintrag eintrag = this._mapByID.get(id);
		return eintrag == null ? null : eintrag.kuerzel;
	}


	/**
	 * Gibt den Katalog für allgemeinbildende Schulformen zurück.
	 * 
	 * @return der Katalog für allgemeinbildende Schulformen
	 */
	public @NotNull AbgangsartKatalog getKatalogAllgemeinbildend() {
		return this._katalogAllgemein;
	}
	
	/**
	 * Gibt den Katalog für berufsbildende Schulformen zurück.
	 * 
	 * @return der Katalog für berufsbildende Schulformen
	 */
	public @NotNull AbgangsartKatalog getKatalogBerufsbildend() {
		return this._katalogBeruf;
	}

	/**
	 * Bestimmt den Allgemeinbildenden Abschluss der Abschlussart.
	 * 
	 * @param abschlussart   die Abschlussart
	 * 
	 * @return der allgemeinbildende Abschluss oder null in einem unerwarteten Fehlerfall
	 */
	public static SchulabschlussAllgemeinbildend getAbschlussAllgemeinbildend(final @NotNull AbgangsartKatalogEintrag abschlussart) {
		if ((abschlussart.kuerzel.length() < 0) || (abschlussart.kuerzel.length() > 2))
			throw new RuntimeException("Fehlerhafter Katalog-Eintrag: Das Kürzel einer Abgangsart muss entweder ein- oder zweistelig sein.");
		final @NotNull String kuerzelAbschluss = abschlussart.kuerzel.length() == 1 ? abschlussart.kuerzel : abschlussart.kuerzel.substring(1, 2);
		return SchulabschlussAllgemeinbildend.getByKuerzelStatistik(kuerzelAbschluss);
	}

	/**
	 * Bestimmt den Berufsbildenden Abschluss der Abschlussart.
	 * 
	 * @param abschlussart   die Abschlussart
	 * 
	 * @return der berufsbildende Abschluss oder null, wenn nur ein allgemeinbildender Abschluss vorliegt.
	 */
	public static SchulabschlussBerufsbildend getAbschlussBerufsbildend(final @NotNull AbgangsartKatalogEintrag abschlussart) {
		if ((abschlussart.kuerzel.length() < 0) || (abschlussart.kuerzel.length() > 2))
			throw new RuntimeException("Fehlerhafter Katalog-Eintrag: Das Kürzel einer Abgangsart muss entweder ein- oder zweistelig sein.");
		if (abschlussart.kuerzel.length() == 1)
			return null;
		return SchulabschlussBerufsbildend.getByKuerzelStatistik(abschlussart.kuerzel.substring(0, 1));
	}

}
