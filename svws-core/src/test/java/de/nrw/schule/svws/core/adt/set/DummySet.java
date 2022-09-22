package de.nrw.schule.svws.core.adt.set;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.SortedSet;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert das {@link NavigableSet}-Interface und dient zum Speichern von Integer Werten von 0 bis
 * einem Maximimalwert. Die Zuordnung wird in einem boolean-Array gespeichert.
 *
 * @author Benjamin A. Bartsch
 */
public class DummySet implements NavigableSet<Integer> {

	private final boolean[] _isSet;
	private final @NotNull DummySetSub _sub;

	/**
	 * Erzeugt ein Set mit Hilfe eines Arrays der Größe {@code maxValueExclusive} zum Speichern von Werten von 0 bis
	 * maxValueExclusive-1.
	 * 
	 * @param maxValueExclusive Der größte erlaubte Integer-Wert (exklusiv) im Set. Der kleinste Wert ist 0.
	 */
	public DummySet(int maxValueExclusive) {
		_isSet = new boolean[maxValueExclusive];
		_sub = new DummySetSub(this, new DummySetIntervall(0, true, maxValueExclusive, false), true);
	}

	@Override
	public @NotNull Comparator<? super @NotNull Integer> comparator() {
		return _sub.comparator();
	}

	@Override
	public @NotNull Integer first() { // tested
		return _sub.first();
	}

	@Override
	public @NotNull Integer last() { // tested
		return _sub.last();
	}

	@Override
	public int size() { // tested
		return _sub.size();
	}

	@Override
	public boolean isEmpty() { // tested
		return _sub.isEmpty();
	}

	@Override
	public boolean contains(@NotNull Object o) { // tested
		return _sub.contains(o);
	}

	@Override
	public @NotNull Object @NotNull [] toArray() { // tested
		return _sub.toArray();
	}

	@Override
	public <@NotNull T> @NotNull T @NotNull [] toArray(@NotNull T @NotNull [] a) { // tested
		return _sub.toArray(a);
	}

	@Override
	public boolean add(@NotNull Integer e) { // tested
		return _sub.add(e);
	}

	@Override
	public boolean remove(@NotNull Object o) { // tested
		return _sub.remove(o);
	}

	@Override
	public boolean containsAll(@NotNull Collection<?> c) { // tested
		return _sub.containsAll(c);
	}

	@Override
	public boolean addAll(@NotNull Collection<? extends @NotNull Integer> c) { // tested
		return _sub.addAll(c);
	}

	@Override
	public boolean retainAll(@NotNull Collection<?> c) {
		return _sub.retainAll(c);
	}

	@Override
	public boolean removeAll(@NotNull Collection<?> c) {
		return _sub.removeAll(c);
	}

	@Override
	public void clear() {
		_sub.clear();
	}

	@Override
	public Integer lower(@NotNull Integer e) {
		return _sub.lower(e);
	}

	@Override
	public Integer floor(@NotNull Integer e) {
		return _sub.floor(e);
	}

	@Override
	public Integer ceiling(@NotNull Integer e) {
		return _sub.ceiling(e);
	}

	@Override
	public Integer higher(@NotNull Integer e) {
		return _sub.higher(e);
	}

	@Override
	public Integer pollFirst() {
		return _sub.pollFirst();
	}

	@Override
	public Integer pollLast() {
		return _sub.pollLast();
	}

	@Override
	public @NotNull Iterator<@NotNull Integer> iterator() {
		return _sub.iterator();
	}

	@Override
	public @NotNull NavigableSet<@NotNull Integer> descendingSet() {
		return _sub.descendingSet();
	}

	@Override
	public @NotNull Iterator<@NotNull Integer> descendingIterator() {
		return _sub.descendingIterator();
	}

	@Override
	public @NotNull NavigableSet<@NotNull Integer> subSet(@NotNull Integer fromElement, boolean fromInclusive,
			@NotNull Integer toElement, boolean toInclusive) {
		return _sub.subSet(fromElement, fromInclusive, toElement, toInclusive);
	}

	@Override
	public @NotNull NavigableSet<@NotNull Integer> headSet(@NotNull Integer toElement, boolean inclusive) {
		return _sub.headSet(toElement, inclusive);
	}

	@Override
	public @NotNull NavigableSet<@NotNull Integer> tailSet(@NotNull Integer fromElement, boolean inclusive) {
		return _sub.tailSet(fromElement, inclusive);
	}

	@Override
	public @NotNull SortedSet<@NotNull Integer> subSet(@NotNull Integer fromElement, @NotNull Integer toElement) {
		return _sub.subSet(fromElement, toElement);
	}

	@Override
	public @NotNull SortedSet<@NotNull Integer> headSet(@NotNull Integer toElement) {
		return _sub.headSet(toElement);
	}

	@Override
	public @NotNull SortedSet<@NotNull Integer> tailSet(@NotNull Integer fromElement) {
		return _sub.tailSet(fromElement);
	}

	// ########################################################################
	// ########################### PROTECTED ##################################
	// ########################################################################

	/**
	 * Wird aufgerufen von {@link DummySetSub#first()} bzw. {@link DummySetSub#last()} falls absteigend sortiert.
	 * Liefert den ersten Schlüssel (Key) dieser Datenstruktur.
	 * 
	 * @param iv Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return Liefert den ersten Schlüssel (Key) dieser Datenstruktur, falls vorhanden.
	 * @throws NoSuchElementException falls es kein erstes Element gibt.
	 */
	@NotNull
	Integer bcGetFirstKeyOrException(@NotNull DummySetIntervall iv) {
		for (int i = iv.min(); i <= iv.max(); i++)
			if (_isSet[i])
				return i;
		throw new NoSuchElementException();
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#last()} bzw. {@link DummySetSub#first()} falls absteigend sortiert.
	 * Liefert den letzten Schlüssel (Key) dieser Datenstruktur.
	 * 
	 * @param iv Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return Liefert den letzten Schlüssel (Key) dieser Datenstruktur falls vorhanden.
	 * @throws NoSuchElementException falls es kein letztes Element gibt.
	 */
	@NotNull
	Integer bcGetLastKeyOrException(@NotNull DummySetIntervall iv) {
		for (int i = iv.max(); i >= iv.min(); i--)
			if (_isSet[i])
				return i;
		throw new NoSuchElementException();
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#size()}. Liefert die Anzahl der Elemente innerhalb des übergebenen
	 * Intervalls.
	 * 
	 * @param iv Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return Liefert die Anzahl der Elemente innerhalb des übergebenen Intervalls.
	 */
	int bcGetSize(@NotNull DummySetIntervall iv) {
		int size = 0;
		for (int i = iv.min(); i <= iv.max(); i++)
			if (_isSet[i])
				size++;
		return size;
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#isEmpty()}. Überprüft, ob die Datenstruktur innerhalb des Intervalls leer
	 * ist.
	 * 
	 * @param iv Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return TRUE, falls die Datenstruktur innerhalb des Intervalls leer ist.
	 */
	boolean bcIsEmpty(@NotNull DummySetIntervall iv) {
		for (int i = iv.min(); i <= iv.max(); i++)
			if (_isSet[i])
				return false;
		return true;
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#contains(Object)}. Überprüft, ob ein Schlüssel (Key) in dieser
	 * Datenstruktur existiert.
	 * 
	 * @param objKey Der Schlüssel (Key) nach dem gesucht wird.
	 * @param iv     Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return TRUE, falls der Schlüssel (Key) in dieser Datenstruktur existiert.
	 */
	boolean bcContainsKey(@NotNull DummySetIntervall iv, @NotNull Object objKey) {
		int e = (Integer) objKey;
		return iv.contains(e) && (_isSet[e]);
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#containsAll(Collection)}. Überprüft, ob alle Schlüssel (Keys) der
	 * Collection in dieser Datenstruktur existieren.
	 * 
	 * @param c  Die Collection mit allen Schlüsseln (Keys), welche überprüft werden sollen.
	 * @param iv Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return TRUE, falls alle Schlüssel (Keys) der Collection in dieser Datenstruktur existieren.
	 */
	boolean bcContainsAllKeys(@NotNull DummySetIntervall iv, @NotNull Collection<?> c) {
		for (Object obj : c)
			if (bcContainsKey(iv, obj) == false)
				return false;
		return true;
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#add(Integer)}. Fügt einen Schlüssel (Key) der Datenstruktur hinzu.
	 * 
	 * @param e  Der Schlüssel (Key) der hinzugefügt werden soll.
	 * @param iv Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return TRUE, falls der Schlüssel (Key) noch nicht existierte und somit hinzugefügt wurde.
	 */
	boolean bcAddKey(@NotNull DummySetIntervall iv, @NotNull Integer e) {
		if ((iv.contains(e)) && (_isSet[e] == false)) {
			_isSet[e] = true;
			return true;
		}
		return false;
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#addAll(Collection)}. Fügt alle Schlüssel (Keys) der Collection
	 * hinzuzufügen.
	 * 
	 * @param c  Die Collection mit allen Schlüsseln (Keys) die hinzugefügt werden sollen.
	 * @param iv Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return TRUE, falls mindestens ein Schlüssel (Key) noch nicht existierte und somit hinzugefügt wurde.
	 */
	boolean bcAddAllKeys(@NotNull DummySetIntervall iv, @NotNull Collection<? extends @NotNull Integer> c) {
		boolean changed = false;
		for (Object obj : c)
			changed |= bcAddKey(iv, (Integer) obj);
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#remove(Object)}. Entfernt einen Schlüssel (Key) aus dieser Datenstruktur.
	 * 
	 * @param o  Der Schlüssel (Key) der entfernt werden soll.
	 * @param iv Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return TRUE, falls der Schlüssel existierte und somit entfernt wurde.
	 */
	boolean bcRemoveKeyReturnBool(@NotNull DummySetIntervall iv, @NotNull Object o) {
		int e = (Integer) o;
		if ((iv.contains(e)) && (_isSet[e] == true)) {
			_isSet[e] = false;
			return true;
		}
		return false;
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#retainAll(Collection)}. Entfernt alle Schlüssel (Keys) aus dieser
	 * Datenstruktur innerhalb des übergebenen Intervalls, außer sie sind in der Collection enthalten.
	 *
	 * @param iv Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * @param c  Die Collection deren Schlüssel (Keys) nicht entfernt werden dürfen.
	 * 
	 * @return TRUE, falls mindestens ein Schlüssel (Key) entfernt wurde.
	 */
	boolean bcRetainAllKeys(@NotNull DummySetIntervall iv, @NotNull Collection<?> c) {
		boolean changed = false;
		for (int i = iv.min(); i <= iv.max(); i++)
			if ((_isSet[i]) && (c.contains(i) == false)) {
				_isSet[i] = false;
				changed = true;
			}
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#removeAll(Collection)}. Entfernt alle Schlüssel (Keys) der Collection aus
	 * dieser Datenstruktur.
	 * 
	 * @param c  Die Collection mit allen Schlüsseln (Keys) die entfernt werden sollen.
	 * @param iv Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return TRUE, falls mindestens ein Schlüssel (Key) entfernt wurde.
	 */
	boolean bcRemoveAllKeys(@NotNull DummySetIntervall iv, @NotNull Collection<?> c) {
		boolean changed = false;
		for (Object obj : c)
			changed |= bcRemoveKeyReturnBool(iv, obj);
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#clear()}. Entfernt alle Schlüssel (Keys) innerhalb des übergebenen
	 * Intervalls.
	 * 
	 * @param iv Das {@link DummySetIntervall} des {@link DummySetSub}.
	 */
	void bcClear(@NotNull DummySetIntervall iv) {
		for (int i = iv.min(); i <= iv.max(); i++)
			_isSet[i] = false;
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#lower(Integer)} bzw. {@link DummySetSub#higher(Integer)} falls absteigend
	 * sortiert. Liefert den größten Schlüssel (Key) welcher kleiner ist als der übergebene Schlüssel (Key), somit den
	 * Vorgänger-Schlüssel des Schlüssels (Key).
	 * 
	 * @param key Der Schlüssel (Key) dessen Vorgänger gesucht wird.
	 * @param iv  Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return Den Vorgänger-Schlüssel des übergebenen Schlüssels (Key) falls vorhanden, sonst NULL.
	 */
	Integer bcGetLowerKeyOrNull(@NotNull DummySetIntervall iv, @NotNull Integer key) {
		for (int i = key - 1; i >= iv.min(); i--) // search < key
			if (_isSet[i])
				return i;
		return null; // NULL allowed
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#floor(Integer)} bzw. {@link DummySetSub#ceiling(Integer)} falls absteigend
	 * sortiert. Liefert den größten Schlüssel (Key) welcher kleiner oder gleich dem übergebenen Schlüssel (Key) ist.
	 * Somit der selbe Schlüssel (Key) falls vorhanden, andernfalls den Vorgänger-Schlüssel (Key) falls vorhanden,
	 * andernfalls NULL.
	 * 
	 * @param key Der Schlüssel (Key) der gesucht wird bzw. sein Vorgänger-Schlüssel.
	 * @param iv  Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return Den selben Schlüssel (Key) falls vorhanden, andernfalls sein Vorgänger-Schlüssel falls vorhanden,
	 *         andernfalls NULL.
	 */
	Integer bcGetFloorKeyOrNull(@NotNull DummySetIntervall iv, @NotNull Integer key) {
		for (int i = key; i >= iv.min(); i--) // search <= key
			if (_isSet[i])
				return i;
		return null; // NULL allowed
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#ceiling(Integer)} bzw. {@link DummySetSub#floor(Integer)} falls absteigend
	 * sortiert. Liefert den kleinsten Schlüssel (Key) welcher größer oder gleich dem übergebenen Schlüssel (Key) ist.
	 * Somit der selbe Schlüssel (Key) falls vorhanden, andernfalls sein Nachfolger-Schlüssel (Key) falls vorhanden,
	 * andernfalls NULL.
	 * 
	 * @param key Der Schlüssel (Key) der gesucht wird bzw. sein Nachfolger-Schlüssel.
	 * @param iv  Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return Den selben Schlüssel (Key) falls vorhanden, andernfalls sein Nachfolger-Schlüssel falls vorhanden,
	 *         andernfalls NULL.
	 */
	Integer bcGetCeilingKeyOrNull(@NotNull DummySetIntervall iv, @NotNull Integer key) {
		for (int i = key; i <= iv.max(); i++) // search >= key
			if (_isSet[i])
				return i;
		return null; // NULL allowed
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#higher(Integer)} bzw. {@link DummySetSub#lower(Integer)} falls absteigend
	 * sortiert. Liefert den kleinsten Schlüssel (Key) welcher größer ist als der übergebene Schlüssel (Key), somit den
	 * Nachfolger-Schlüssel des übergebenen Schlüssels (Key).
	 * 
	 * @param key Der Schlüssel (Key) dessen Nachfolger-Schlüssel gesucht wird.
	 * @param iv  Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return Den Nachfolger-Schlüssel des übergebenen Schlüssels (Key) falls vorhanden, sonst NULL.
	 */
	Integer bcGetHigherKeyOrNull(@NotNull DummySetIntervall iv, @NotNull Integer key) {
		for (int i = key + 1; i <= iv.max(); i++) // search > key
			if (_isSet[i])
				return i;
		return null; // NULL allowed
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#pollFirst()} bzw. {@link DummySetSub#pollLast()} falls absteigend
	 * sortiert. Entfernt und liefert den ersten Schlüssel (Key) dieser Datenstruktur.
	 * 
	 * @param iv Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return Entfernt und liefert den ersten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	Integer bcPollFirstKeyOrNull(@NotNull DummySetIntervall iv) {
		for (int i = iv.min(); i <= iv.max(); i++)
			if (_isSet[i]) {
				_isSet[i] = false;
				return i;
			}
		return null;
	}

	/**
	 * Wird aufgerufen von {@link DummySetSub#pollLast()} bzw. {@link DummySetSub#pollFirst()} falls absteigend
	 * sortiert. Entfernt und liefert den letzten Schlüssel (Key) dieser Datenstruktur.
	 * 
	 * @param iv Das {@link DummySetIntervall} des {@link DummySetSub}.
	 * 
	 * @return Entfernt und liefert den letzten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	Integer bcPollLastKeyOrNull(@NotNull DummySetIntervall iv) {
		for (int i = iv.max(); i >= iv.min(); i--)
			if (_isSet[i]) {
				_isSet[i] = false;
				return i;
			}
		return null;
	}

}
