package de.svws_nrw.core.utils.schule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalog;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogDaten;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogEintrag;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogIndex;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Zugriff auf die Fachklassen aus dem Katalog
 * für berufsbildende Schulformen.
 */
public class BerufskollegFachklassenManager {

	/** der Katalog */
	private final @NotNull BerufskollegFachklassenKatalog _katalog;

	/** Die Version der Daten */
	private final long _version;

	/** Ein Vektor mit allen Katalog-Einträgen */
	private final @NotNull List<BerufskollegFachklassenKatalogEintrag> _values = new ArrayList<>();

	/** Eine HashMap für den schnellen Zugriff auf einen Teilkatalog anhand eines Index. */
	private final @NotNull Map<Integer, BerufskollegFachklassenKatalogIndex> _mapByIndex = new HashMap<>();

	/** Eine HashMap für den Zugriff auf den Index anhand eines Eintrags. */
	private final @NotNull Map<BerufskollegFachklassenKatalogEintrag, Integer> _mapIndexByEintrag = new HashMap<>();

	/** Eine HashMap für den schnellen Zugriff auf die Fachklassen anhand des Fachklassen-Schlüssels. */
	private final @NotNull Map<String, BerufskollegFachklassenKatalogEintrag> _mapByKuerzel = new HashMap<>();

	/** Eine HashMap für den schnellen Zugriff auf die Fachklassen anhand der ID. */
	private final @NotNull Map<Long, BerufskollegFachklassenKatalogEintrag> _mapByID = new HashMap<>();

	/** Eine HashMap für den schnellen Zugriff auf die Daten der Fachklasse anhand der ID. */
	private final @NotNull Map<Long, BerufskollegFachklassenKatalogDaten> _mapDatenByID = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager für den Katalog der Fachklassen
	 *
	 * @param katalog   der Katalog der Fachklassen
	 */
	public BerufskollegFachklassenManager(final @NotNull BerufskollegFachklassenKatalog katalog) {
		this._katalog = katalog;
		this._version = katalog.version;
		for (final BerufskollegFachklassenKatalogIndex katIndex : katalog.indizes) {
			_values.addAll(katIndex.fachklassen);
			_mapByIndex.put(katIndex.index, katIndex);
			for (final @NotNull BerufskollegFachklassenKatalogEintrag eintrag : katIndex.fachklassen) {
				this._mapIndexByEintrag.put(eintrag, katIndex.index);
				final String kuerzel = "" + katIndex.index + "-" + eintrag.schluessel + "-" + eintrag.schluessel2;
				this._mapByKuerzel.put(kuerzel, eintrag);
				for (final @NotNull BerufskollegFachklassenKatalogDaten daten : eintrag.historie) {
					final BerufskollegFachklassenKatalogEintrag alt = this._mapByID.put(daten.id, eintrag);
					if (alt != null)
						throw new DeveloperNotificationException("Fehlerhafter Katalog: Doppelte ID '" + daten.id + "' bei der Fachklasse '" + kuerzel + "'");
					this._mapDatenByID.put(daten.id, daten);
				}
			}
		}
	}


	/**
	 * Gibt die Version der Katalog-Daten zurück.
	 *
	 * @return die Version
	 */
	public long getVersion() {
		return this._version;
	}


	/**
	 * Gibt die Version der Daten eines Teilkatalog für einen Index zurück.
	 *
	 * @param index   der Index für die Fachklassen
	 *
	 * @return die Version des Teilkatalogs
	 */
	public long getVersionByIndex(final int index) {
		final BerufskollegFachklassenKatalogIndex katIndex = _mapByIndex.get(index);
		if (katIndex == null)
			throw new IllegalArgumentException("Ungültiger Fachklassen-Index.");
		return katIndex.version;
	}


	/**
	 * Gibt die Version der Daten des Teilkatalog für den Index
	 * der angegebenen Schulgliederung zurück.
	 *
	 * @param schuljahr    das Schuljahr
	 * @param gliederung   die Schulgliederung
	 *
	 * @return die Version des Teilkatalogs
	 */
	public long getVersionBySchuljahrAndGliederung(final int schuljahr, final @NotNull Schulgliederung gliederung) {
		final SchulgliederungKatalogEintrag sglke = gliederung.daten(schuljahr);
		if (sglke == null)
			throw new IllegalArgumentException("Die Schulgliederung %s ist in dem Schuljahr %d nicht gültig.".formatted(gliederung.name(), schuljahr));
		if (sglke.bkIndex == null)
			throw new IllegalArgumentException("Die Schulgliederung " + sglke.kuerzel + " hat keinen Fachklassen-Index.");
		final BerufskollegFachklassenKatalogIndex katIndex = _mapByIndex.get(sglke.bkIndex);
		if (katIndex == null)
			throw new IllegalArgumentException("Keine Fachklassen für den Fachklassen-Index " + sglke.bkIndex + " der Schulgliederung "
					+ sglke.kuerzel + " bekannt.");
		return katIndex.version;
	}


	/**
	 * Gibt den Katalog-Eintrag für das übergebene Kürzel zurück. Das Kürzel setzt
	 * sich zusammen aus dem Index und den beiden Teilschlüsseln der Fachklasse:
	 * "Index-Schlüssel1-Schlüssel2".
	 *
	 * @param kuerzel   das Kürzel des Katalog-Eintrags
	 *
	 * @return der Katalog-Eintrag oder null, falls das Kürzel ungültig ist.
	 */
	public BerufskollegFachklassenKatalogEintrag getByKuerzel(final @NotNull String kuerzel) {
		return this._mapByKuerzel.get(kuerzel);
	}


	/**
	 * Gibt alle Katalog-Einträge zurück.
	 *
	 * @return ein Array mit allen Katalog-Einträgen
	 */
	public BerufskollegFachklassenKatalogEintrag[] values() {
		return this._values.toArray(new BerufskollegFachklassenKatalogEintrag[0]);
	}


	/**
	 * Gibt die Katalog-Daten für das übergebene Kürzel und das angegebene Schuljahr zurück.
	 * Das Kürzel setzt sich zusammen aus dem Index und den beiden Teilschlüsseln der Fachklasse:
	 * "Index-Schlüssel1-Schlüssel2".
	 *
	 * @param kuerzel     das Kürzel des Katalog-Eintrags
	 * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return der Katalog-Eintrag oder null, falls das Kürzel ungültig ist oder der Katalog-Eintrag
	 *         keine Daten für das übergebene Schuljahr hat
	 */
	public BerufskollegFachklassenKatalogDaten getDatenByKuerzelAndSchuljahr(final @NotNull String kuerzel, final int schuljahr) {
		final BerufskollegFachklassenKatalogEintrag eintrag = this._mapByKuerzel.get(kuerzel);
		if (eintrag == null)
			return null;
		for (final @NotNull BerufskollegFachklassenKatalogDaten daten : eintrag.historie)
			if (((daten.gueltigVon == null) || (daten.gueltigVon <= schuljahr))
					&& ((daten.gueltigBis == null) || (daten.gueltigBis >= schuljahr)))
				return daten;
		return null;
	}


	/**
	 * Gibt die Katalog-Daten für die Fachklasse zurück.
	 *
	 * @param id   die die des Katalog-Eintrags
	 *
	 * @return die Daten für die ID oder null bei einer fehlerhaften ID
	 */
	public BerufskollegFachklassenKatalogDaten getDatenByID(final long id) {
		return this._mapDatenByID.get(id);
	}


	/**
	 * Gibt das Kürzel für die Fachklasse mit der angebenen ID zurück. Das Kürzel setzt
	 * sich zusammen aus dem Index und den beiden Teilschlüsseln der Fachklasse:
	 * "Index-Schlüssel1-Schlüssel2".
	 *
	 * @param id   die ID der Fachklasse
	 *
	 * @return das Kürzel der Fachklasse oder null, falls die ID ungültig ist
	 */
	public String getKuerzel(final long id) {
		final BerufskollegFachklassenKatalogEintrag eintrag = this._mapByID.get(id);
		final Integer index = this._mapIndexByEintrag.get(eintrag);
		return ((eintrag == null) || (index == null)) ? null : ("" + index + "-" + eintrag.schluessel + "-" + eintrag.schluessel2);
	}


	/**
	 * Gibt den Teilkatalog für den angegebenen Fachklassen-Index zurück.
	 *
	 * @param index   der Fachklassen-Index des Teilkatalogs
	 *
	 * @return der Teilkatalog
	 */
	public @NotNull BerufskollegFachklassenKatalogIndex getTeilKatalog(final int index) {
		final BerufskollegFachklassenKatalogIndex katIndex = _mapByIndex.get(index);
		if (katIndex == null)
			throw new IllegalArgumentException("Ungültiger Fachklassen-Index.");
		return katIndex;
	}


	/**
	 * Gibt den Teilkatalog des Fachklassen-Index
	 * für die angegebene Schulgliederung zurück.
	 *
	 * @param schuljahr    das Schuljahr, für welches der Teilkatalog bestimmt werden soll
	 * @param gliederung   die Schulgliederung
	 *
	 * @return der Teilkatalog
	 */
	public @NotNull BerufskollegFachklassenKatalogIndex getTeilKatalogBySchuljahrAndGliederung(final int schuljahr, final @NotNull Schulgliederung gliederung) {
		final SchulgliederungKatalogEintrag sglke = gliederung.daten(schuljahr);
		if (sglke == null)
			throw new IllegalArgumentException("Die Schulgliederung %s ist in dem Schuljahr %d nicht gültig.".formatted(gliederung.name(), schuljahr));
		if (sglke.bkIndex == null)
			throw new IllegalArgumentException("Die Schulgliederung " + sglke.kuerzel + " hat keinen Fachklassen-Index.");
		final BerufskollegFachklassenKatalogIndex katIndex = _mapByIndex.get(sglke.bkIndex);
		if (katIndex == null)
			throw new IllegalArgumentException("Keine Fachklassen für den Fachklassen-Index " + sglke.bkIndex + " der Schulgliederung "
					+ sglke.kuerzel + " bekannt.");
		return katIndex;
	}


	/**
	 * Gibt den Katalog zurück.
	 *
	 * @return der Katalog
	 */
	public @NotNull BerufskollegFachklassenKatalog getKatalog() {
		return this._katalog;
	}

}
