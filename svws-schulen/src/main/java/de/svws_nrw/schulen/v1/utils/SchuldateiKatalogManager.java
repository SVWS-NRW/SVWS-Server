package de.svws_nrw.schulen.v1.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.schulen.v1.data.SchuldateiEintrag;
import de.svws_nrw.schulen.v1.data.SchuldateiKatalogeintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient der Handhabung von Katalogen zur Schuldatei.
 */
public class SchuldateiKatalogManager {

	/** Der Name des Katalogs */
	private final @NotNull String _name;

	/** Die Liste aller Katalog-Einträge dieses Katalogs */
	private final @NotNull List<SchuldateiKatalogeintrag> _katalogeintraege = new ArrayList<>();

	/** Eine Map von dem Wert der Katalog-Einträge auf die Liste der Katalog-Einträge mit diesem Wert */
	private final @NotNull Map<String, List<SchuldateiKatalogeintrag>> _mapKatalogeintraegeByWert = new HashMap<>();

	/** Eine Map von dem Schlüssel der Katalog-Einträge auf eine Liste der Katalog-Einträge mit diesem Schlüssel */
	private final @NotNull Map<String, List<SchuldateiKatalogeintrag>> _mapKatalogeintraegeBySchluessel = new HashMap<>();

	/** Cache: Eine Map der Einträge anhand des Schuljahres */
	private final @NotNull Map<Integer, List<SchuldateiKatalogeintrag>> _mapKatalogeintraegeBySchuljahr = new HashMap<>();

	/** Cache: Eine Map nach Schuljahren für Maps von Wert auf einen SchuldateiKatalogeintrag */
	private final @NotNull Map<Integer, Map<String, SchuldateiKatalogeintrag>> _mapKatalogEintraegeBySchuljahrAndWert = new HashMap<>();

	/** Cache: Eine Map nach Schuljahren für Maps von Schlüssel auf eine Liste von SchuldateiKatalogeinträgen */
	private final @NotNull Map<Integer, Map<String, List<SchuldateiKatalogeintrag>>> _mapKatalogEintraegeBySchuljahrAndSchluessel = new HashMap<>();


	/** Der Comparator zur Sortierung der Zeiträume gueltigab - gueltigbis in absteigender Reihenfolge*/
	private static final @NotNull Comparator<SchuldateiKatalogeintrag> _comparatorZeitraumDesc =
			(final @NotNull SchuldateiKatalogeintrag a, final @NotNull SchuldateiKatalogeintrag b) -> {
				if (b.gueltigab.equals(a.gueltigab))
					return SchuldateiUtils.compare(b.gueltigbis, a.gueltigbis);
				return SchuldateiUtils.compare(b.gueltigab, a.gueltigab);
			};


	/**
	 * Erstellt einen neuen Katalog-Manager.
	 *
	 * @param name   der Name des Katalogs
	 */
	public SchuldateiKatalogManager(final @NotNull String name) {
		this._name = name;
	}


	/**
	 * Fügt diesem Katalog einen neuen Eintrag hinzu.
	 * Einträge eines Kataloges werden in der Schuldatei über die Eigenschaft "wert" referenziert.
	 * Es können mehrere Einträge eines Kataloges mit demselben Wert vorkommen, wobei sich der Zeitraum unterscheiden muss.
	 * Die entsprechende Überprüfung führt die Methode validate aus, die auch die Liste der Einträge in _mapEintraegeByWert sortiert.
	 *
	 * @param eintrag   der Eintrag
	 */
	void addEintrag(final @NotNull SchuldateiKatalogeintrag eintrag) {
		// Eintrag ablegen
		_katalogeintraege.add(eintrag);

		// ... in der Map der Einträge anhand des Wertes
		final List<SchuldateiKatalogeintrag> eintraegeByWert =
				_mapKatalogeintraegeByWert.computeIfAbsent(eintrag.wert, (final @NotNull String k) -> new ArrayList<SchuldateiKatalogeintrag>());
		if (eintraegeByWert != null)
			eintraegeByWert.add(eintrag);

		// ... in der Map der Einträge anhand des Schlüssels
		if (!eintrag.schluessel.isBlank()) {
			final List<SchuldateiKatalogeintrag> eintraegeBySchluessel =
					_mapKatalogeintraegeBySchluessel.computeIfAbsent(eintrag.schluessel, (final @NotNull String k) -> new ArrayList<SchuldateiKatalogeintrag>());
			if (eintraegeBySchluessel != null)
				eintraegeBySchluessel.add(eintrag);
		}
	}


	/**
	 * Gibt den Namen des Katalogs zurück.
	 *
	 * @return der Name des Katalogs
	 */
	public @NotNull String getName() {
		return _name;
	}


	/**
	 * Gibt alle Katalog-Einträge zurück
	 *
	 * @return die Liste mit allen Katalog-Einträgen
	 */
	public @NotNull List<SchuldateiKatalogeintrag> getEintraege() {
		return this._katalogeintraege;
	}


	/**
	 * Gibt die Katalog-Einträge für das angegebene Schuljahr zurück
	 *
	 * @param schuljahr    das Schuljahr, zu dem die Werte geliefert werden
	 *
	 * @return die Liste der Katalog-Einträge, die in dem Schuljahr gültig sind
	 */
	public @NotNull List<SchuldateiKatalogeintrag> getEintraegeBySchuljahr(final int schuljahr) {
		final List<SchuldateiKatalogeintrag> list = this._mapKatalogeintraegeBySchuljahr.get(schuljahr);
		if (list != null)
			return list;
		// Wenn nicht, dann bestimme alle (!) Einträge, welche in das Schuljahr fallen ...
		final @NotNull List<SchuldateiKatalogeintrag> listEintraege = new ArrayList<>();
		for (final @NotNull SchuldateiKatalogeintrag eintrag : this._katalogeintraege)
			if (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))
				listEintraege.add(eintrag);
		this._mapKatalogeintraegeBySchuljahr.put(schuljahr, listEintraege);
		return listEintraege;
	}


	/**
	 * Gibt die Katalog-Einträge zu dem Wert zurück, sofern der Wert gültig ist.
	 * Es werden mehrere Einträge zurückgegeben, wenn für verschiedene Zeiträume entsprechende Einträge im Katalog sind.
	 *
	 * @param wert   der Wert des gesuchten Katalog-Eintrags
	 *
	 * @return die Katalog-Einträge oder null, wenn es keinen für den Wert gibt.
	 */
	public List<SchuldateiKatalogeintrag> getEintraegeByWert(final String wert) {
		return this._mapKatalogeintraegeByWert.get(wert);
	}


	/**
	 * Gibt die Katalog-Einträge zu dem Wert zurück, sofern der Wert gültig ist.
	 *
	 * @param wert   der Wert des gesuchten Katalog-Eintrags
	 *
	 * @return die Katalog-Einträge oder null, wenn es keinen für den Wert gibt.
	 */
	public List<SchuldateiKatalogeintrag> getEintraegeByIntegerWert(final int wert) {
		return this.getEintraegeByWert("" + wert);
	}


	/**
	 * Gibt den Katalog-Eintrag für das Schuljahr und den Wert zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param wert        der Wert
	 *
	 * @return der Katalog-Eintrag für das Schuljahr und den Wert falls er existiert und ansonsten null.
	 */
	public SchuldateiKatalogeintrag getEintragBySchuljahrAndWert(final int schuljahr, final String wert) {
		return getCacheBySchuljahrAndWert(schuljahr).get(wert);
	}


	/**
	 * Gibt den Katalog-Eintrag für das Schuljahr und den numerischen Wert zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param wert        der numerische Wert
	 *
	 * @return der Katalog-Eintrag für das Schuljahr und den numerischen Wert falls er existiert und ansonsten null.
	 */
	public SchuldateiKatalogeintrag getEintragBySchuljahrAndIntegerWert(final int schuljahr, final int wert) {
		return this.getEintragBySchuljahrAndWert(schuljahr, "" + wert);
	}


	/**
	 * Gibt die Katalog-Einträge zu dem Schlüssel zurück, sofern der Schlüssel gültig ist.
	 *
	 * @param schluessel   der Schlüssel der gesuchten Katalog-Einträge
	 *
	 * @return die Liste der Katalog-Einträge für den Schlüssel existiert der Schlüssel nicht, so wird null zurückgegeben
	 */
	public List<SchuldateiKatalogeintrag> getEintraegeBySchluessel(final String schluessel) {
		return this._mapKatalogeintraegeBySchluessel.get(schluessel);
	}


	/**
	 * Gibt die Katalog-Einträge zu dem Schlüssel für ein bestimmtes Schuljahr zurück, sofern der Schlüssel gültig ist.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schluessel   der Schlüssel der gesuchten Katalog-Einträge
	 *
	 * @return die Liste der Katalog-Eintrag für den Schlüssel existiert der Schlüssel nicht, so wird null zurückgegeben
	 */
	public List<SchuldateiKatalogeintrag> getEintraegeBySchuljahrAndSchluessel(final int schuljahr, final String schluessel) {
		if ((schluessel == null) || (!_mapKatalogeintraegeBySchluessel.containsKey(schluessel)))
			return null;
		final Map<String, List<SchuldateiKatalogeintrag>> map = this._mapKatalogEintraegeBySchuljahrAndSchluessel.get(schuljahr);
		if (map != null)
			return map.get(schluessel);
		// Cache wird für ein Schuljahr aufgebaut
		final @NotNull Map<String, List<SchuldateiKatalogeintrag>> neueMap = new HashMap<>();
		for (final @NotNull SchuldateiKatalogeintrag eintrag : this._katalogeintraege)
			if ((!eintrag.schluessel.isBlank()) && (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))) {
				final List<SchuldateiKatalogeintrag> eintraegeBySchluessel =
						neueMap.computeIfAbsent(eintrag.schluessel, (final @NotNull String k) -> new ArrayList<SchuldateiKatalogeintrag>());
				if (eintraegeBySchluessel != null)
					eintraegeBySchluessel.add(eintrag);
			}
		// Map eintragen in den Cache
		this._mapKatalogEintraegeBySchuljahrAndSchluessel.put(schuljahr, neueMap);
		return neueMap.get(schluessel);
	}


	/**
	 * Gibt zurück, ob ein Katalog-Eintrag für den Wert existiert.
	 *
	 * @param wert   der zu prüfende Wert
	 *
	 * @return true, falls ein Katalog-Eintrag existiert und ansonsten false.
	 */
	public boolean hasEintrag(final String wert) {
		if (wert == null)
			return false;
		return this._mapKatalogeintraegeByWert.containsKey(wert);
	}


	/**
	 * Gibt zurück, ob ein Katalog-Eintrag für den Wert existiert.
	 *
	 * @param wert   der zu prüfende Wert
	 *
	 * @return true, falls ein Katalog-Eintrag existiert und ansonsten false.
	 */
	public boolean hasEintrag(final int wert) {
		return hasEintrag("" + wert);
	}


	/**
	 * Gibt zurück, ob ein Katalog-Eintrag für den Wert in einem Schuljahr existiert.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param wert        der zu prüfende Wert
	 *
	 * @return true, falls ein Katalog-Eintrag im angegebenen Schuljahr existiert und ansonsten false.
	 */
	public boolean hasEintragBySchuljahr(final int schuljahr, final @NotNull String wert) {
		return getCacheBySchuljahrAndWert(schuljahr).containsKey(wert);
	}


	/**
	 * Gibt zurück, ob ein Katalog-Eintrag für den numerischen Wert existiert.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param wert        der zu prüfende Wert
	 *
	 * @return true, falls ein Katalog-Eintrag existiert und ansonsten false.
	 */
	public boolean hasEintragBySchuljahr(final int schuljahr, final int wert) {
		return hasEintragBySchuljahr(schuljahr, ("" + wert));
	}


	/**
	 * Gibt zurück, ob ein Katalog-Eintrag für den Wert in einem Zeitraum existiert.
	 *
	 * @param abBis  Der Schuldateieintrag, der den Zeitraum definiert, für den der Eintrag komplett vorliegen muss.
	 * @param wert   der zu prüfende Wert
	 *
	 * @return true, falls ein Katalog-Eintrag existiert und ansonsten false.
	 */
	public boolean hasEintragInZeitraum(final SchuldateiEintrag abBis, final String wert) {
		if (wert == null)
			return false;
		final int ab = abBis.gueltigab == null ? SchuldateiUtils._immerGueltigAb : SchuldateiUtils.schuljahrAusDatum(abBis.gueltigab);
		final int bis = abBis.gueltigbis == null ? SchuldateiUtils._immerGueltigBis : SchuldateiUtils.schuljahrAusDatum(abBis.gueltigbis);
		return hasEintragInZeitraum(ab, bis, wert, false);
	}


	/**
	 * Gibt zurück, ob ein Katalog-Eintrag für den numerischen Wert in einem Zeitraum existiert.
	 *
	 * @param abBis  Der Schuldateieintrag, der den Zeitraum definiert, für den der Eintrag komplett vorliegen muss.
	 * @param wert   der zu prüfende Wert
	 *
	 * @return true, falls ein Katalog-Eintrag existiert und ansonsten false.
	 */
	public boolean hasEintragInZeitraum(final SchuldateiEintrag abBis, final int wert) {
		return hasEintragInZeitraum(abBis, "" + wert);
	}


	/**
	 * Prüft ob ein Katalog-Eintrag für den Wert in einem Zeitraum existiert
	 * Ist der Parameter mitTeilgueltigkeit auf TRUE reicht es wenn der Wert nur teilweise im Zeitraum existiert
	 * Ist er auf FALSE muss der Wert im gesamten Zeitraum definiert sein.
	 *
	 * @param schuljahrAb			das erste Schuljahr
	 * @param schuljahrBis			das letzte Schuljahr
	 * @param wert                  der Wert, auf den geprüft wird
	 * @param mitTeilgueltigkeit	wenn true, reichts es, wenn der Wert nicht im gesamten Zeitraum definiert ist.
	 *
	 * @return boolean, true wenn Eintrag entsprechend vorliegt, sonst false
	 */
	public boolean hasEintragInZeitraum(final int schuljahrAb, final int schuljahrBis, final String wert, final boolean mitTeilgueltigkeit) {
		final List<SchuldateiKatalogeintrag> list = getEintraegeByWert(wert);
		if (list == null)
			return false;
		list.sort(_comparatorZeitraumDesc);
		int ab = schuljahrAb < SchuldateiUtils._immerGueltigAb ? SchuldateiUtils._immerGueltigAb : schuljahrAb;
		int bis = schuljahrBis > SchuldateiUtils._immerGueltigBis ? SchuldateiUtils._immerGueltigBis : schuljahrBis;
		for (SchuldateiKatalogeintrag eintrag : list) {
			if ((eintrag.gueltigbis == null) || SchuldateiUtils.schuljahrAusDatum(eintrag.gueltigbis) < bis)
				return false;
			final int vonSchuljahr = (eintrag.gueltigab == null ? SchuldateiUtils._immerGueltigAb : SchuldateiUtils.schuljahrAusDatum(eintrag.gueltigab));
			if (vonSchuljahr <= bis) {
				if (mitTeilgueltigkeit || (vonSchuljahr <= ab))
					return true;
				bis = vonSchuljahr - 1;
			}
		}
		return false;
	}


	/**
	 * Gibt die Bezeichnung des Katalog-Eintrag zu dem Wert im angegebenen Schuljahr zurück, sofern
	 * der Wert gültig ist.
	 *
	 * @param schuljahr	das Schuljahr
	 * @param wert   	der Wert des Katalog-Eintrags
	 *
	 * @return die Bezeichnung oder null, wenn sie nicht gültig ist
	 */
	public String getBezeichnung(final int schuljahr, final String wert) {
		final SchuldateiKatalogeintrag eintrag = getEintragBySchuljahrAndWert(schuljahr, wert);
		if (eintrag == null)
			return null;
		return eintrag.bezeichnung;
	}


	/**
	 * gibt die Daten aus dem Cache zurück und baut ihn ggfs. auf, wenn er noch nicht existiert
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return Der gefüllte Cache für das übergebene Schuljahr
	 */
	private @NotNull Map<String, SchuldateiKatalogeintrag> getCacheBySchuljahrAndWert(final int schuljahr) {
		final Map<String, SchuldateiKatalogeintrag> map = this._mapKatalogEintraegeBySchuljahrAndWert.get(schuljahr);
		if (map != null)
			return map;
		// Cache wird für ein Schuljahr aufgebaut
		final @NotNull Map<String, SchuldateiKatalogeintrag> neueMap = new HashMap<>();
		for (final @NotNull SchuldateiKatalogeintrag eintrag : this._katalogeintraege)
			if (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))
				neueMap.put(eintrag.wert, eintrag);
		// Map eintragen in den Cache
		this._mapKatalogEintraegeBySchuljahrAndWert.put(schuljahr, neueMap);
		return neueMap;
	}


	/**
	 * sortiert die Listen in _mapKatalogeintraegeByWert und prüft, dass die Werte nicht überlappend sind.
	 * (Die Liste wird absteigend sortiert!)
	 *
	 * @throws IllegalArgumentException   falls eine Überlappung festgestellt wird.
	 */
	public void validate() {
		for (final @NotNull List<SchuldateiKatalogeintrag> list : _mapKatalogeintraegeByWert.values()) {
			//Laufe durch die Liste und prüfe, dass die Zeiträume gültig und disjunkt sind.
			@NotNull SchuldateiKatalogeintrag eintrag;
			if (list.size() > 1) {
				list.sort(_comparatorZeitraumDesc);
				eintrag = list.getFirst();
				int schuljahrBis = eintrag.gueltigbis == null ? SchuldateiUtils._immerGueltigBis : SchuldateiUtils.schuljahrAusDatum(eintrag.gueltigbis);
				int schuljahrAb  = eintrag.gueltigab  == null ? SchuldateiUtils._immerGueltigAb  : SchuldateiUtils.schuljahrAusDatum(eintrag.gueltigab);
				for (int i = 1; i < list.size(); i++) {
					// Wirf Exception, wenn der Zeitraum eines Katalog-Eintrags nicht valide ist
					if (schuljahrBis < schuljahrAb)
						throw new IllegalArgumentException("Dieser Katalogeintrag Katalog='" + eintrag.katalog + "', Wert='" + eintrag.wert	+ "' hat einen ungültigen Gültigkeitszeitraum.");
					// hole nächsten Katalog-Eintrag und bestimme schuljahrBis (Iterationsschritt Teil1)
					eintrag = list.get(i);
					schuljahrBis = eintrag.gueltigbis == null ? SchuldateiUtils._immerGueltigBis : SchuldateiUtils.schuljahrAusDatum(eintrag.gueltigbis);
					// Wirf Exception, wenn der nächste Katalog-Eintrag nicht früher liegt also falls schuljahrBis(n+1) >= schuljahrAb(n)
					if (schuljahrBis >= schuljahrAb)
						throw new IllegalArgumentException("Dieser Katalogeintrag Katalog='" + eintrag.katalog + "', Wert='" + eintrag.wert	+ "' hat überlappende Gültigkeitszeiträume.");
					// Iterationsschritt Teil2
					schuljahrAb = eintrag.gueltigab == null ? SchuldateiUtils._immerGueltigAb : SchuldateiUtils.schuljahrAusDatum(eintrag.gueltigab);
				}
				// Wirf Exception, wenn der Zeitraum eines Katalog-Eintrags nicht valide ist (letzten Eintrag prüfen)
				if (schuljahrBis < schuljahrAb)
					throw new IllegalArgumentException("Dieser Katalogeintrag Katalog='" + eintrag.katalog + "', Wert='" + eintrag.wert	+ "' hat einen ungültigen Gültigkeitszeitraum.");
			} else {
				eintrag = list.getFirst();
				if (SchuldateiUtils.istFrueher(eintrag.gueltigbis, eintrag.gueltigab))
					throw new IllegalArgumentException("Dieser Katalogeintrag Katalog='" + eintrag.katalog + "', Wert='" + eintrag.wert	+ "' hat einen ungültigen Gültigkeitszeitraum.");
 			}
		}
	}
}
