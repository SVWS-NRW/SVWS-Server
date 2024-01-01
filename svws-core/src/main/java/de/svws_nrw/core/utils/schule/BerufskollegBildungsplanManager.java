package de.svws_nrw.core.utils.schule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.adt.map.HashMap3D;
import de.svws_nrw.core.data.bk.BKFBFach;
import de.svws_nrw.core.data.bk.BKFachklassenSchluessel;
import de.svws_nrw.core.data.bk.BKBildungsplan;
import de.svws_nrw.core.data.bk.BKBildungsplanKatalog;
import de.svws_nrw.core.data.bk.BKBildungsplanKatalogEintrag;
import de.svws_nrw.core.data.bk.BKLernfeld;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.exceptions.UserNotificationException;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.Map3DUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Zugriff auf die Lehrpläne aus dem Katalog
 * für berufsbildende Schulformen.
 */
public class BerufskollegBildungsplanManager {

	/** der Katalog */
	private final @NotNull BKBildungsplanKatalog _katalog;

	/** Die Version der Daten */
	private final long _version;

	/** Ein Vektor mit allen Katalog-Einträgen */
	private final @NotNull ArrayList<@NotNull BKBildungsplan> _values = new ArrayList<>();

	/** Eine HashMap für den schnellen Zugriff auf ein Fach anhand des Kürzels */
	private final @NotNull HashMap<@NotNull String, @NotNull BKFBFach> _mapFachByKuerzel = new HashMap<>();

	/** Eine HashMap für den Zugriff auf einen Bildungsplan anhand der ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull BKBildungsplan> _mapByID = new HashMap<>();

	/** Eine HashMap für den schnellen Zugriff auf die Lehrpläne anhand des Fachklassen-Schlüssels. */
	private final @NotNull HashMap3D<@NotNull Integer, @NotNull String, @NotNull Long, @NotNull List<@NotNull BKBildungsplan>> _mapBildungsplanByFachklasse = new HashMap3D<>();

	/** Eine HashMap für den schnellen Zugriff auf die Fächer anhand des Fachklassen-Schlüssels. */
	private final @NotNull HashMap3D<@NotNull Integer, @NotNull String, @NotNull String, @NotNull Set<@NotNull BKFBFach>> _mapFachByFachklasse = new HashMap3D<>();


	/**
 	 * Erstellt einen neuen Manager für den Katalog der berufsbezogenen Fächer im BK
	 *
	 * @param katalog   der Katalog der berufsbezogenen Fächer
	 */
	public BerufskollegBildungsplanManager(final @NotNull BKBildungsplanKatalog katalog) {
		this._katalog = katalog;
		this._version = katalog.version;
		for (final @NotNull BKBildungsplanKatalogEintrag eintrag : katalog.lehrplaene) {
			this._values.addAll(eintrag.historie);
			for (final @NotNull BKBildungsplan bildungsplan : eintrag.historie) {
				if (!bildungsplan.fachklasse.index.equals(eintrag.index) || !bildungsplan.fachklasse.schluessel.equals(eintrag.schluessel))
					throw new DeveloperNotificationException("Fehlerhafter Katalog: Fachklasse in Historie mit ID '" + bildungsplan.id + "' ungleich Fachklasse des Bildungsplans");
				final BKBildungsplan alt = this._mapByID.put(bildungsplan.id, bildungsplan);
				if (alt != null)
					throw new DeveloperNotificationException("Fehlerhafter Katalog: Doppelte ID '" + bildungsplan.id);
				Map3DUtils.getOrCreateArrayList(_mapBildungsplanByFachklasse, eintrag.index, eintrag.schluessel, bildungsplan.id).add(bildungsplan);
				for (final @NotNull BKFBFach fach : bildungsplan.fbFaecher) {
					this._mapFachByKuerzel.put(fach.kuerzel, fach);
					Map3DUtils.getOrCreateSet(_mapFachByFachklasse, eintrag.index, eintrag.schluessel, fach.kuerzel).add(fach);
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
	 * Gibt alle Katalog-Einträge zurück.
	 *
	 * @return ein Array mit allen Katalog-Einträgen
	 */
	public BKBildungsplan[] values() {
		return this._values.toArray(new BKBildungsplan[0]);
	}


	/**
	 * Gibt die Lehrpläne für ein Schuljahr zurück
	 *
	 * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return Die Lehrpläne für das angegebene Schuljahr
	 */
	public BKBildungsplan[] getLehrplaeneBySchuljahr(final int schuljahr) {
		final @NotNull ArrayList<@NotNull BKBildungsplan> lehrplaene = new ArrayList<>();
		for (final @NotNull BKBildungsplan bildungsplan : this._values) {
			if (((bildungsplan.gueltigVon == null) || (bildungsplan.gueltigVon <= schuljahr))
			 && ((bildungsplan.gueltigBis == null) || (bildungsplan.gueltigBis >= schuljahr)))
				lehrplaene.add(bildungsplan);
		}
		return lehrplaene.toArray(new BKBildungsplan[0]);
	}

	/**
	 * Gibt die Lehrpläne eines Index für ein Schuljahr zurück
	 *
	 * @param index  	  der Schulgliederungs-Index des Teilkatalogs
     * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return Die Lehrpläne eines Index für das angegebene Schuljahr
	 */
	public BKBildungsplan[] getLehrplaeneByIndexSchuljahr(final @NotNull Integer index, final int schuljahr) {
		final @NotNull ArrayList<@NotNull BKBildungsplan> lehrplaene = new ArrayList<>();
		final @NotNull List<@NotNull List<@NotNull BKBildungsplan>> lehrplaeneOfIndex = _mapBildungsplanByFachklasse.getNonNullValuesOfMap2AsList(index);
		for (final @NotNull List<@NotNull BKBildungsplan> list : lehrplaeneOfIndex) {
			for (final @NotNull BKBildungsplan bildungsplan : list) {
				if (((bildungsplan.gueltigVon == null) || (bildungsplan.gueltigVon <= schuljahr))
				 && ((bildungsplan.gueltigBis == null) || (bildungsplan.gueltigBis >= schuljahr)))
					lehrplaene.add(bildungsplan);
			}
		}
		return lehrplaene.toArray(new BKBildungsplan[0]);
	}

	/**
	 * Gibt die Lehrpläne eines Index der angegebenen Schulgliederung für ein Schuljahr zurück
	 *
 	 * @param gliederung   die Schulgliederung
     * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return Die Lehrpläne eines Gliederungsindex für das angegebene Schuljahr
	 */
	public BKBildungsplan[] getLehrplaeneBySchulgliederungSchuljahr(final @NotNull Schulgliederung gliederung, final int schuljahr) {
		if (gliederung.daten.bkIndex == null)
			throw new IllegalArgumentException("Die Schulgliederung " + gliederung.daten.kuerzel + " hat keinen Schulgliederungs-Index.");
		return getLehrplaeneByIndexSchuljahr(gliederung.daten.bkIndex, schuljahr);
	}

	/**
	 * Gibt die Lehrpläne eines Index für ein Schuljahr zurück, wobei auch die auslaufenden
	 * Lehrpläne mit ausgegeben werden.
	 *
	 * @param index  	  der Schulgliederungs-Index des Teilkatalogs
     * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return Die Lehrpläne eines Gliederungsindex für das angegebene Schuljahr
	 */
	public BKBildungsplan[] getLehrplaeneByIndexSchuljahrAll(final @NotNull Integer index, final int schuljahr) {
		final @NotNull ArrayList<@NotNull BKBildungsplan> lehrplaene = new ArrayList<>();
		final @NotNull List<@NotNull List<@NotNull BKBildungsplan>> lehrplaeneOfIndex = _mapBildungsplanByFachklasse.getNonNullValuesOfMap2AsList(index);

		for (final @NotNull List<@NotNull BKBildungsplan> list : lehrplaeneOfIndex) {
			for (final @NotNull BKBildungsplan bildungsplan : list) {
				if (((bildungsplan.gueltigVon == null) || (bildungsplan.gueltigVon <= schuljahr))
				 && ((bildungsplan.gueltigBis == null) || (bildungsplan.gueltigBis + bildungsplan.dauer / 2 + 1 >= schuljahr)))
					lehrplaene.add(bildungsplan);
			}
		}
		return lehrplaene.toArray(new BKBildungsplan[0]);
	}

	/**
	 * Gibt die Lehrpläne eines Index der angegebenen Schulgliederung für ein Schuljahr zurück
	 *
 	 * @param gliederung  die Schulgliederung
     * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return Die Lehrpläne eines Gliederungsindex für das angegebene Schuljahr
	 */
	public BKBildungsplan[] getLehrplaeneBySchulgliederungSchuljahrAll(final @NotNull Schulgliederung gliederung, final int schuljahr) {
		if (gliederung.daten.bkIndex == null)
			throw new IllegalArgumentException("Die Schulgliederung " + gliederung.daten.kuerzel + " hat keinen Schulgliederungs-Index.");
		return getLehrplaeneByIndexSchuljahrAll(gliederung.daten.bkIndex, schuljahr);
	}


	/**
	 * Gibt den Bildungsplan mit der gegebenen ID zurück.
	 *
	 * @param id   die ID des Katalog-Eintrags
	 *
	 * @return den Bildungsplan für die ID oder null bei einer fehlerhaften ID
	 */
	public BKBildungsplan getBildungsplanById(final long id) {
		return this._mapByID.get(id);
	}

	/**
	 * Gibt den Bildungsplan zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird davon ausgegangen, dass der Bildungsgang im 1. Jahrgang aufgenommen wird.
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Bildungsplan bestimmt werden sollen
	 *
	 * @return den Bildungsplan für die ID oder null bei einer fehlerhaften ID
	 */
	public BKBildungsplan getBildungsplanByFachklassenschluesselSchuljahr(final @NotNull BKFachklassenSchluessel schluessel, final int schuljahr) {
		return this.getBildungsplanByIndexFachklasseSchuljahr(schluessel.index, schluessel.schluessel, schuljahr);
	}

	/**
	 * Gibt den Bildungsplan zum Tupel(Schulgliederungsindex,Fachklasse) in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird davon ausgegangen, dass der Bildungsgang im 1. Jahrgang aufgenommen wird.
	 *
	 * @param index  	   der Schulgliederungs-Index des Teilkatalogs
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Bildungsplan bestimmt werden soll
	 *
	 * @return den Bildungsplan für eine Fachklasse im angegebenen Schuljahr
	 */
	public BKBildungsplan getBildungsplanByIndexFachklasseSchuljahr(final @NotNull Integer index, final @NotNull String schluessel, final int schuljahr) {
		final Map<@NotNull Long, @NotNull List<@NotNull BKBildungsplan>> mapById = this._mapBildungsplanByFachklasse.getMap3OrNull(index, schluessel);

		if (mapById == null)
			return null;

		for (@NotNull final List<@NotNull BKBildungsplan> lehrplaene : mapById.values())
			for (@NotNull final BKBildungsplan bildungsplan : lehrplaene)
				if (((bildungsplan.gueltigVon == null) || (bildungsplan.gueltigVon <= schuljahr))
				 && ((bildungsplan.gueltigBis == null) || (bildungsplan.gueltigBis >= schuljahr)))
					return bildungsplan;

		return null;
	}

	/**
	 * Gibt den Bildungsplan zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Bildungsplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return den Bildungsplan für eine Fachklasse im angegebenen Schuljahr und Jahrgang
	 */
	public BKBildungsplan getBildungsplanByFachklassenschluesselSchuljahrJahrgang(final @NotNull BKFachklassenSchluessel schluessel, final int schuljahr, final int jahrgang) {
		return this.getBildungsplanByIndexFachklasseSchuljahrJahrgang(schluessel.index, schluessel.schluessel, schuljahr, jahrgang);
	}

	/**
	 * Gibt den Bildungsplan zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param index  	   der Schulgliederungs-Index des Teilkatalogs
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Bildungsplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return den Bildungsplan für eine Fachklasse im angegebenen Schuljahr und Jahrgang
	 */
	public BKBildungsplan getBildungsplanByIndexFachklasseSchuljahrJahrgang(final @NotNull Integer index, final @NotNull String schluessel, final int schuljahr, final int jahrgang) {
		final BKBildungsplan bildungsplan = getBildungsplanByIndexFachklasseSchuljahr(index, schluessel, schuljahr - jahrgang);
		if (bildungsplan == null)
			return null;

		if ((bildungsplan.dauer + 1) / 2 < jahrgang)
			throw new UserNotificationException("Fehlerhafter Jahrgang: Der Jahrgang " + jahrgang + " ist zu groß für den Bildungsgang mit einer Dauer von " + bildungsplan.dauer + " Monaten!");

		return bildungsplan;
	}


	/**
	 * Gibt alle bekannten Bündelfächer zurück.
	 *
	 * @return die Liste aller bekannten Bündelfächer
	 */
	public BKFBFach[] getFaecherByFachklassenschuesselSchuljahr() {
		return this._mapFachByKuerzel.values().toArray(new BKFBFach[0]);
	}

	/**
	 * Gibt ein Bündelfach zu einem Kürzel zurück
	 *
	 * @param kuerzel  	   das Kürzel des Fachs
	 *
	 * @return das Fach zu einem Kürzel,
	 */
	public BKFBFach getFachByKuerzel(final @NotNull String kuerzel) {
		return this._mapFachByKuerzel.get(kuerzel);
	}

	/**
	 * Gibt die Bündelfächer zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird davon ausgegangen, dass der Bildungsgang im 1. Jahrgang aufgenommen wird.
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Bildungsplan bestimmt werden sollen
	 *
	 * @return die Liste der Bündelfächer für die Fachklasse und Schuljahr oder null falls keine Bildungsplan für Fachklasse vorhanden ist.
	 */
	public BKFBFach[] getFaecherByFachklassenschuesselSchuljahr(final @NotNull BKFachklassenSchluessel schluessel, final int schuljahr) {
		return this.getFaecherByIndexFachklasseSchuljahr(schluessel.index, schluessel.schluessel, schuljahr);
	}

	/**
	 * Gibt die Bündelfächer zu dem Tupel(Schulgliederungsindex,Fachklasse) in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird davon ausgegangen, dass der Bildungsgang im 1. Jahrgang aufgenommen wird.
	 *
	 * @param index  	   der Schulgliederungs-Index des Teilkatalogs
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Bildungsplan bestimmt werden soll
	 *
	 * @return die Liste der Bündelfächer für die Fachklasse und Schuljahr oder null falls keine Leh
	 */
	public BKFBFach[] getFaecherByIndexFachklasseSchuljahr(final @NotNull Integer index, final @NotNull String schluessel, final int schuljahr) {
		final Map<@NotNull Long, @NotNull List<@NotNull BKBildungsplan>> mapById = this._mapBildungsplanByFachklasse.getMap3OrNull(index, schluessel);

		if (mapById == null)
			return null;

		for (@NotNull final List<@NotNull BKBildungsplan> lehrplaene : mapById.values())
			for (@NotNull final BKBildungsplan bildungsplan : lehrplaene)
				if (((bildungsplan.gueltigVon == null) || (bildungsplan.gueltigVon <= schuljahr))
				 && ((bildungsplan.gueltigBis == null) || (bildungsplan.gueltigBis >= schuljahr)))
					return bildungsplan.fbFaecher.toArray(new BKFBFach[0]);

		return null;
	}

	/**
	 * Gibt die Bündelfächer zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Bildungsplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return die Liste der Bündelfächer für Fachklasse, Schuljahr und Jahrgang oder null bei einer fehlerhaften ID
	 */
	public BKFBFach[] getFaecherByFachklassenschluesselSchuljahrJahrgang(final @NotNull BKFachklassenSchluessel schluessel, final int schuljahr, final int jahrgang) {
		return this.getFaecherByIndexFachklasseSchuljahrJahrgang(schluessel.index, schluessel.schluessel, schuljahr, jahrgang);
	}

	/**
	 * Gibt die Bündelfächer zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param index  	   der Schulgliederungs-Index des Teilkatalogs
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Bildungsplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return die Liste der Bündelfächer für Fachklasse, Schuljahr und Jahrgang oder null bei einer fehlerhaften ID
	 */
	public BKFBFach[] getFaecherByIndexFachklasseSchuljahrJahrgang(final @NotNull Integer index, final @NotNull String schluessel, final int schuljahr, final int jahrgang) {
		final BKBildungsplan bildungsplan = getBildungsplanByIndexFachklasseSchuljahrJahrgang(index, schluessel, schuljahr, jahrgang);

		if (bildungsplan == null)
			return null;

		return bildungsplan.fbFaecher.toArray(new BKFBFach[0]);
	}


	/**
	 * Gibt die Lernfelder zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird davon ausgegangen, dass der Bildungsgang im 1. Jahrgang aufgenommen wird.
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Bildungsplan bestimmt werden sollen
	 *
	 * @return die Liste der Lernfelder für eine Fachklasse und Schuljahr oder null, wenn nicht vorhanden
	 */
	public BKLernfeld[] getLernfelderByFachklassenschluesselSchuljahr(final @NotNull BKFachklassenSchluessel schluessel, final int schuljahr) {
		return this.getLernfelderByIndexFachklasseSchuljahr(schluessel.index, schluessel.schluessel, schuljahr);
	}

	/**
	 * Gibt die Lernfelder zu dem Tupel(Schulgliederungsindex,Fachklasse) in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird davon ausgegangen, dass der Bildungsgang im 1. Jahrgang aufgenommen wird.
	 *
	 * @param index  	   der Schulgliederungs-Index des Teilkatalogs
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Bildungsplan bestimmt werden soll
	 *
	 * @return die Liste der Lernfelder für eine Fachklasse und Schuljahr oder null, wenn nicht vorhanden
	 */
	public BKLernfeld[] getLernfelderByIndexFachklasseSchuljahr(final @NotNull Integer index, final @NotNull String schluessel, final int schuljahr) {
		final Map<@NotNull Long, @NotNull List<@NotNull BKBildungsplan>> mapById = this._mapBildungsplanByFachklasse.getMap3OrNull(index, schluessel);

		if (mapById == null)
			return null;

		for (@NotNull final List<@NotNull BKBildungsplan> lehrplaene : mapById.values())
			for (@NotNull final BKBildungsplan bildungsplan : lehrplaene)
				if (((bildungsplan.gueltigVon == null) || (bildungsplan.gueltigVon <= schuljahr))
				 && ((bildungsplan.gueltigBis == null) || (bildungsplan.gueltigBis >= schuljahr)))
					return bildungsplan.lernfelder.toArray(new BKLernfeld[0]);

		return null;
	}

	/**
	 * Gibt die Lernfelder zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Bildungsplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return die Liste der Lernfelder für eine Fachklasse und Schuljahr im angegebenen Jahrgang oder null, wenn nicht vorhanden
	 */
	public BKLernfeld[] getLernfelderByFachklassenschluesselSchuljahrJahrgang(final @NotNull BKFachklassenSchluessel schluessel, final int schuljahr, final int jahrgang) {
		return this.getLernfelderByIndexFachklasseSchuljahrJahrgang(schluessel.index, schluessel.schluessel, schuljahr, jahrgang);
	}

	/**
	 * Gibt die Lernfelder zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param index  	   der Schulgliederungs-Index des Teilkatalogs
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Bildungsplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return die Liste der Lernfelder für eine Fachklasse und Schuljahr im angegebenen Jahrgang oder null, wenn nicht vorhanden
	 */
	public BKLernfeld[] getLernfelderByIndexFachklasseSchuljahrJahrgang(final @NotNull Integer index, final @NotNull String schluessel, final int schuljahr, final int jahrgang) {
		final BKBildungsplan bildungsplan = getBildungsplanByIndexFachklasseSchuljahrJahrgang(index, schluessel, schuljahr, jahrgang);

		if (bildungsplan == null)
			return null;

		return bildungsplan.lernfelder.toArray(new BKLernfeld[0]);
	}


	/**
	 * Gibt den Katalog zurück.
	 *
	 * @return der Katalog
	 */
	public @NotNull BKBildungsplanKatalog getKatalog() {
		return this._katalog;
	}

}
