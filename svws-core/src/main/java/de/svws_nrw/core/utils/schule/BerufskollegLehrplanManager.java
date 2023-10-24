package de.svws_nrw.core.utils.schule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.map.HashMap3D;
import de.svws_nrw.core.data.bk.BKFBFach;
import de.svws_nrw.core.data.bk.BKFachklassenSchluessel;
import de.svws_nrw.core.data.bk.BKLehrplan;
import de.svws_nrw.core.data.bk.BKLehrplanKatalog;
import de.svws_nrw.core.data.bk.BKLehrplanKatalogEintrag;
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
public class BerufskollegLehrplanManager {

	/** der Katalog */
	private final @NotNull BKLehrplanKatalog _katalog;

	/** Die Version der Daten */
	private final long _version;

	/** Ein Vektor mit allen Katalog-Einträgen */
	private final @NotNull ArrayList<@NotNull BKLehrplan> _values = new ArrayList<>();

	/** Eine HashMap für den schnellen Zugriff auf ein Fach anhand des Kürzels */
	private final @NotNull HashMap<@NotNull String, @NotNull BKFBFach> _mapFachByKuerzel = new HashMap<>();

	/** Eine HashMap für den Zugriff auf einen Lehrplan anhand der ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull BKLehrplan> _mapByID = new HashMap<>();

	/** Eine HashMap für den schnellen Zugriff auf die Lehrpläne anhand des Fachklassen-Schlüssels. */
	private final @NotNull HashMap3D<@NotNull Integer, @NotNull String, @NotNull Long, @NotNull List<@NotNull BKLehrplan>> _mapLehrplanByFachklasse = new HashMap3D<>();

	/** Eine HashMap für den schnellen Zugriff auf die Fächer anhand des Fachklassen-Schlüssels. */
	private final @NotNull HashMap3D<@NotNull Integer, @NotNull String, @NotNull String, @NotNull HashSet<@NotNull BKFBFach>> _mapFachByFachklasse = new HashMap3D<>();


	/**
 	 * Erstellt einen neuen Manager für den Katalog der berufsbezogenen Fächer im BK
	 *
	 * @param katalog   der Katalog der berufsbezogenen Fächer
	 */
	public BerufskollegLehrplanManager(final @NotNull BKLehrplanKatalog katalog) {
		this._katalog = katalog;
		this._version = katalog.version;
		for (final @NotNull BKLehrplanKatalogEintrag eintrag : katalog.lehrplaene) {
			this._values.addAll(eintrag.historie);
			for (final @NotNull BKLehrplan lehrplan : eintrag.historie) {
				if (lehrplan.fachklasse.index != eintrag.index || lehrplan.fachklasse.schluessel != eintrag.schluessel)
					throw new DeveloperNotificationException("Fehlerhafter Katalog: Fachklasse in Historie mit ID '" + lehrplan.id + "' ungleich Fachklasse des Lehrplans");
				final BKLehrplan alt = this._mapByID.put(lehrplan.id, lehrplan);
				if (alt != null)
					throw new DeveloperNotificationException("Fehlerhafter Katalog: Doppelte ID '" + lehrplan.id);
				Map3DUtils.getOrCreateArrayList(_mapLehrplanByFachklasse, eintrag.index, eintrag.schluessel, lehrplan.id).add(lehrplan);
				for (final @NotNull BKFBFach fach : lehrplan.fbFaecher) {
					this._mapFachByKuerzel.put(fach.kuerzel, fach);
					Map3DUtils.getOrCreateHashSet(_mapFachByFachklasse, eintrag.index, eintrag.schluessel, fach.kuerzel).add(fach);
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
	public BKLehrplan[] values() {
		return this._values.toArray(new BKLehrplan[0]);
	}


	/**
	 * Gibt die Lehrpläne für ein Schuljahr zurück
	 *
	 * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return Die Lehrpläne für das angegebene Schuljahr
	 */
	public BKLehrplan[] getLehrplaeneBySchuljahr(final int schuljahr) {
		final @NotNull ArrayList<@NotNull BKLehrplan> lehrplaene = new ArrayList<>();
		for (final @NotNull BKLehrplan lehrplan : this._values) {
			if (((lehrplan.gueltigVon == null) || (lehrplan.gueltigVon <= schuljahr))
			 && ((lehrplan.gueltigBis == null) || (lehrplan.gueltigBis >= schuljahr)))
				lehrplaene.add(lehrplan);
		}
		return lehrplaene.toArray(new BKLehrplan[0]);
	}

	/**
	 * Gibt die Lehrpläne eines Index für ein Schuljahr zurück
	 *
	 * @param index  	  der Schulgliederungs-Index des Teilkatalogs
     * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return Die Lehrpläne eines Index für das angegebene Schuljahr
	 */
	public BKLehrplan[] getLehrplaeneByIndexSchuljahr(final @NotNull Integer index, final int schuljahr) {
		final @NotNull ArrayList<@NotNull BKLehrplan> lehrplaene = new ArrayList<>();
		final @NotNull List<@NotNull List<@NotNull BKLehrplan>> lehrplaeneOfIndex = _mapLehrplanByFachklasse.getNonNullValuesOfMap2AsList(index);
		for (final @NotNull List<@NotNull BKLehrplan> list : lehrplaeneOfIndex) {
			for (final @NotNull BKLehrplan lehrplan : list) {
				if (((lehrplan.gueltigVon == null) || (lehrplan.gueltigVon <= schuljahr))
				 && ((lehrplan.gueltigBis == null) || (lehrplan.gueltigBis >= schuljahr)))
					lehrplaene.add(lehrplan);
			}
		}
		return lehrplaene.toArray(new BKLehrplan[0]);
	}

	/**
	 * Gibt die Lehrpläne eines Index der angegebenen Schulgliederung für ein Schuljahr zurück
	 *
 	 * @param gliederung   die Schulgliederung
     * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return Die Lehrpläne eines Gliederungsindex für das angegebene Schuljahr
	 */
	public BKLehrplan[] getLehrplaeneBySchulgliederungSchuljahr(final @NotNull Schulgliederung gliederung, final int schuljahr) {
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
	public BKLehrplan[] getLehrplaeneByIndexSchuljahrAll(final @NotNull Integer index, final int schuljahr) {
		final @NotNull ArrayList<@NotNull BKLehrplan> lehrplaene = new ArrayList<>();
		final @NotNull List<@NotNull List<@NotNull BKLehrplan>> lehrplaeneOfIndex = _mapLehrplanByFachklasse.getNonNullValuesOfMap2AsList(index);

		for (final @NotNull List<@NotNull BKLehrplan> list : lehrplaeneOfIndex) {
			for (final @NotNull BKLehrplan lehrplan : list) {
				if (((lehrplan.gueltigVon == null) || (lehrplan.gueltigVon <= schuljahr))
				 && ((lehrplan.gueltigBis == null) || (lehrplan.gueltigBis + lehrplan.dauer / 2 + 1 >= schuljahr)))
					lehrplaene.add(lehrplan);
			}
		}
		return lehrplaene.toArray(new BKLehrplan[0]);
	}

	/**
	 * Gibt die Lehrpläne eines Index der angegebenen Schulgliederung für ein Schuljahr zurück
	 *
 	 * @param gliederung  die Schulgliederung
     * @param schuljahr   das Schuljahr für welches die Katalog-Daten bestimmt werden sollen
	 *
	 * @return Die Lehrpläne eines Gliederungsindex für das angegebene Schuljahr
	 */
	public BKLehrplan[] getLehrplaeneBySchulgliederungSchuljahrAll(final @NotNull Schulgliederung gliederung, final int schuljahr) {
		if (gliederung.daten.bkIndex == null)
			throw new IllegalArgumentException("Die Schulgliederung " + gliederung.daten.kuerzel + " hat keinen Schulgliederungs-Index.");
		return getLehrplaeneByIndexSchuljahrAll(gliederung.daten.bkIndex, schuljahr);
	}


	/**
	 * Gibt den Lehrplan mit der gegebenen ID zurück.
	 *
	 * @param id   die ID des Katalog-Eintrags
	 *
	 * @return den Lehrplan für die ID oder null bei einer fehlerhaften ID
	 */
	public BKLehrplan getLehrplanById(final long id) {
		return this._mapByID.get(id);
	}

	/**
	 * Gibt den Lehrplan zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird davon ausgegangen, dass der Bildungsgang im 1. Jahrgang aufgenommen wird.
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
	 *
	 * @return den Lehrplan für die ID oder null bei einer fehlerhaften ID
	 */
	public BKLehrplan getLehrplanByFachklassenschluesselSchuljahr(final @NotNull BKFachklassenSchluessel schluessel, final int schuljahr) {
		return this.getLehrplanByIndexFachklasseSchuljahr(schluessel.index, schluessel.schluessel, schuljahr);
	}

	/**
	 * Gibt den Lehrplan zum Tupel(Schulgliederungsindex,Fachklasse) in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird davon ausgegangen, dass der Bildungsgang im 1. Jahrgang aufgenommen wird.
	 *
	 * @param index  	   der Schulgliederungs-Index des Teilkatalogs
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden soll
	 *
	 * @return den Lehrplan für eine Fachklasse im angegebenen Schuljahr
	 */
	public BKLehrplan getLehrplanByIndexFachklasseSchuljahr(final @NotNull Integer index, final @NotNull String schluessel, final int schuljahr) {
		final Map<@NotNull Long, @NotNull List<@NotNull BKLehrplan>> mapById = this._mapLehrplanByFachklasse.getMap3OrNull(index, schluessel);

		if (mapById == null)
			return null;

		for (@NotNull List<@NotNull BKLehrplan> lehrplaene : mapById.values())
			for (@NotNull BKLehrplan lehrplan : lehrplaene)
				if (((lehrplan.gueltigVon == null) || (lehrplan.gueltigVon <= schuljahr))
				 && ((lehrplan.gueltigBis == null) || (lehrplan.gueltigBis >= schuljahr)))
					return lehrplan;

		return null;
	}

	/**
	 * Gibt den Lehrplan zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return den Lehrplan für eine Fachklasse im angegebenen Schuljahr und Jahrgang
	 */
	public BKLehrplan getLehrplanByFachklassenschluesselSchuljahrJahrgang(final @NotNull BKFachklassenSchluessel schluessel, final int schuljahr, final int jahrgang) {
		return this.getLehrplanByIndexFachklasseSchuljahrJahrgang(schluessel.index, schluessel.schluessel, schuljahr, jahrgang);
	}

	/**
	 * Gibt den Lehrplan zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param index  	   der Schulgliederungs-Index des Teilkatalogs
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return den Lehrplan für eine Fachklasse im angegebenen Schuljahr und Jahrgang
	 */
	public BKLehrplan getLehrplanByIndexFachklasseSchuljahrJahrgang(final @NotNull Integer index, final @NotNull String schluessel, final int schuljahr, final int jahrgang) {
		final BKLehrplan lehrplan = getLehrplanByIndexFachklasseSchuljahr(index, schluessel, schuljahr - jahrgang);
		if (lehrplan == null)
			return null;

		if ((lehrplan.dauer + 1) / 2 < jahrgang)
			throw new UserNotificationException("Fehlerhafter Jahrgang: Der Jahrgang " + jahrgang + " ist zu groß für den Bildungsgang mit einer Dauer von " + lehrplan.dauer + " Monaten!");

		return lehrplan;
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
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
	 *
	 * @return die Liste der Bündelfächer für die Fachklasse und Schuljahr oder null falls keine Lehrplan für Fachklasse vorhanden ist.
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
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden soll
	 *
	 * @return die Liste der Bündelfächer für die Fachklasse und Schuljahr oder null falls keine Leh
	 */
	public BKFBFach[] getFaecherByIndexFachklasseSchuljahr(final @NotNull Integer index, final @NotNull String schluessel, final int schuljahr) {
		final Map<@NotNull Long, @NotNull List<@NotNull BKLehrplan>> mapById = this._mapLehrplanByFachklasse.getMap3OrNull(index, schluessel);

		if (mapById == null)
			return null;

		for (@NotNull List<@NotNull BKLehrplan> lehrplaene : mapById.values())
			for (@NotNull BKLehrplan lehrplan : lehrplaene)
				if (((lehrplan.gueltigVon == null) || (lehrplan.gueltigVon <= schuljahr))
				 && ((lehrplan.gueltigBis == null) || (lehrplan.gueltigBis >= schuljahr)))
					return lehrplan.fbFaecher.toArray(new BKFBFach[0]);

		return null;
	}

	/**
	 * Gibt die Bündelfächer zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
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
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return die Liste der Bündelfächer für Fachklasse, Schuljahr und Jahrgang oder null bei einer fehlerhaften ID
	 */
	public BKFBFach[] getFaecherByIndexFachklasseSchuljahrJahrgang(final @NotNull Integer index, final @NotNull String schluessel, final int schuljahr, final int jahrgang) {
		final BKLehrplan lehrplan = getLehrplanByIndexFachklasseSchuljahrJahrgang(index, schluessel, schuljahr, jahrgang);

		if (lehrplan == null)
			return null;

		return lehrplan.fbFaecher.toArray(new BKFBFach[0]);
	}


	/**
	 * Gibt die Lernfelder zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird davon ausgegangen, dass der Bildungsgang im 1. Jahrgang aufgenommen wird.
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
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
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden soll
	 *
	 * @return die Liste der Lernfelder für eine Fachklasse und Schuljahr oder null, wenn nicht vorhanden
	 */
	public BKLernfeld[] getLernfelderByIndexFachklasseSchuljahr(final @NotNull Integer index, final @NotNull String schluessel, final int schuljahr) {
		final Map<@NotNull Long, @NotNull List<@NotNull BKLehrplan>> mapById = this._mapLehrplanByFachklasse.getMap3OrNull(index, schluessel);

		if (mapById == null)
			return null;

		for (@NotNull List<@NotNull BKLehrplan> lehrplaene : mapById.values())
			for (@NotNull BKLehrplan lehrplan : lehrplaene)
				if (((lehrplan.gueltigVon == null) || (lehrplan.gueltigVon <= schuljahr))
				 && ((lehrplan.gueltigBis == null) || (lehrplan.gueltigBis >= schuljahr)))
					return lehrplan.lernfelder.toArray(new BKLernfeld[0]);

		return null;
	}

	/**
	 * Gibt die Lernfelder zu einem Fachklassenschlüssel in einem (Einschulungs-)Schuljahr zurück.
	 * Es wird der Jahrgang angegeben, in dem der Bildungsgang aufgenommen wurde (Anrechnung).
	 *
	 * @param schluessel   der Fachklassenschlüssel
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
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
	 * @param schuljahr    das Schuljahr, für welches der Lehrplan bestimmt werden sollen
	 * @param jahrgang     der Jahrgang, der beim Einstieg in den Bildungsgang belegt wurde
	 *
	 * @return die Liste der Lernfelder für eine Fachklasse und Schuljahr im angegebenen Jahrgang oder null, wenn nicht vorhanden
	 */
	public BKLernfeld[] getLernfelderByIndexFachklasseSchuljahrJahrgang(final @NotNull Integer index, final @NotNull String schluessel, final int schuljahr, final int jahrgang) {
		final BKLehrplan lehrplan = getLehrplanByIndexFachklasseSchuljahrJahrgang(index, schluessel, schuljahr, jahrgang);

		if (lehrplan == null)
			return null;

		return lehrplan.lernfelder.toArray(new BKLernfeld[0]);
	}


	/**
	 * Gibt den Katalog zurück.
	 *
	 * @return der Katalog
	 */
	public @NotNull BKLehrplanKatalog getKatalog() {
		return this._katalog;
	}

}
