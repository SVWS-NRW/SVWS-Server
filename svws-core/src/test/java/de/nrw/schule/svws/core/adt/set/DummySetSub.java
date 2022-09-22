package de.nrw.schule.svws.core.adt.set;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert mit Hilfe von {@link DummySetIntervall} eine Sub-Sicht auf das {@link DummySet}. Der Parameter
 * {@link DummySetSub#_asc} entscheidet darüber, ob die Sub-Sicht die Elemente auf- oder absteigend interpretiert.
 *
 * @author Benjamin A. Bartsch
 */
public class DummySetSub implements NavigableSet<Integer> {

	/**
	 * Das Set auf das sich diese Sub-Sicht bezieht.
	 */
	private final @NotNull DummySet _par;

	/**
	 * Definiert das Intervall der Sub-Sicht.
	 */
	private final @NotNull DummySetIntervall _iv;

	/**
	 * Falls FALSE werden alle Elemente absteigend interpretiert.
	 */
	private boolean _asc;

	/**
	 * Definiert mit Hilfe von {@link DummySetIntervall} eine Sicht auf das {@link DummySet}.
	 * 
	 * @param par Das Set auf das sich diese Sub-Sicht bezieht.
	 * @param iv  Definiert das Intervall der Sub-Sicht.
	 * @param asc Falls FALSE werden alle Elemente absteigend interpretiert.
	 */
	public DummySetSub(@NotNull DummySet par, @NotNull DummySetIntervall iv, boolean asc) {
		_par = par;
		_iv = iv;
		_asc = asc;
	}

	@Override
	public @NotNull Comparator<? super @NotNull Integer> comparator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public @NotNull Integer first() {
		return _asc ? _par.bcGetFirstKeyOrException(_iv) : _par.bcGetLastKeyOrException(_iv);
	}

	@Override
	public @NotNull Integer last() {
		return _asc ? _par.bcGetLastKeyOrException(_iv) : _par.bcGetFirstKeyOrException(_iv);
	}

	@Override
	public int size() {
		return _par.bcGetSize(_iv);
	}

	@Override
	public boolean isEmpty() {
		return _par.bcIsEmpty(_iv);
	}

	@Override
	public boolean contains(@NotNull Object o) {
		return _par.bcContainsKey(_iv, o);
	}

	@Override
	public boolean add(@NotNull Integer e) {
		return _par.bcAddKey(_iv, e);
	}

	@Override
	public boolean remove(@NotNull Object o) {
		return _par.bcRemoveKeyReturnBool(_iv, o);
	}

	@Override
	public boolean containsAll(@NotNull Collection<?> c) {
		return _par.bcContainsAllKeys(_iv, c);
	}

	@Override
	public boolean addAll(@NotNull Collection<? extends @NotNull Integer> c) {
		return _par.bcAddAllKeys(_iv, c);
	}

	@Override
	public boolean retainAll(@NotNull Collection<?> c) {
		return _par.bcRetainAllKeys(_iv, c);
	}

	@Override
	public boolean removeAll(@NotNull Collection<?> c) {
		return _par.bcRemoveAllKeys(_iv, c);
	}

	@Override
	public void clear() {
		_par.bcClear(_iv);
	}

	@Override
	public Integer lower(@NotNull Integer e) {
		return _asc ? _par.bcGetLowerKeyOrNull(_iv, e) : _par.bcGetHigherKeyOrNull(_iv, e);
	}

	@Override
	public Integer floor(@NotNull Integer e) {
		return _asc ? _par.bcGetFloorKeyOrNull(_iv, e) : _par.bcGetCeilingKeyOrNull(_iv, e);
	}

	@Override
	public Integer ceiling(@NotNull Integer e) {
		return _asc ? _par.bcGetCeilingKeyOrNull(_iv, e) : _par.bcGetFloorKeyOrNull(_iv, e);
	}

	@Override
	public Integer higher(@NotNull Integer e) {
		return _asc ? _par.bcGetHigherKeyOrNull(_iv, e) : _par.bcGetLowerKeyOrNull(_iv, e);
	}

	@Override
	public Integer pollFirst() {
		return _asc ? _par.bcPollFirstKeyOrNull(_iv) : _par.bcPollLastKeyOrNull(_iv);
	}

	@Override
	public Integer pollLast() {
		return _asc ? _par.bcPollLastKeyOrNull(_iv) : _par.bcPollFirstKeyOrNull(_iv);
	}

	@Override
	public @NotNull Iterator<@NotNull Integer> iterator() {
		return new DummySetIterator(this);
	}

	@Override
	public @NotNull NavigableSet<@NotNull Integer> descendingSet() {
		return new DummySetSub(_par, _iv, !_asc);
	}

	@Override
	public @NotNull Iterator<@NotNull Integer> descendingIterator() {
		return new DummySetSub(_par, _iv, !_asc).iterator();
	}

	@Override
	public NavigableSet<Integer> subSet(Integer fromElement, boolean fromInclusive, Integer toElement,
			boolean toInclusive) {
		return createSet(fromElement, fromInclusive, toElement, toInclusive);
	}

	@Override
	public @NotNull NavigableSet<@NotNull Integer> headSet(@NotNull Integer toElement, boolean inclusive) {
		return createSet(_iv.from, _iv.fromInc, toElement, inclusive);
	}

	@Override
	public @NotNull NavigableSet<@NotNull Integer> tailSet(@NotNull Integer fromElement, boolean inclusive) {
		return createSet(fromElement, inclusive, _iv.to, _iv.toInc);
	}

	@Override
	public @NotNull SortedSet<@NotNull Integer> subSet(@NotNull Integer fromElement, @NotNull Integer toElement) {
		return createSet(fromElement, true, toElement, false);
	}

	@Override
	public @NotNull SortedSet<@NotNull Integer> headSet(@NotNull Integer toElement) {
		return createSet(_iv.from, _iv.fromInc, toElement, false);
	}

	@Override
	public @NotNull SortedSet<@NotNull Integer> tailSet(@NotNull Integer fromElement) {
		return createSet(fromElement, true, _iv.to, _iv.toInc);
	}

	private @NotNull NavigableSet<@NotNull Integer> createSet(Integer from, boolean fromInc, @NotNull Integer to,
			boolean toInc) {
		if (_iv.contains(from, fromInc))
			throw new IllegalArgumentException();
		if (_iv.contains(to, toInc))
			throw new IllegalArgumentException();
		DummySetIntervall info = new DummySetIntervall(from, fromInc, to, toInc);
		return new DummySetSub(_par, info, _asc);
	}

	@Override
	public Object[] toArray() {
		return getArrayListOfKeys().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return getArrayListOfKeys().toArray(a);
	}

	private ArrayList<Integer> getArrayListOfKeys() {
		ArrayList<Integer> list = new ArrayList<>();
		for (Integer key : this)
			list.add(key);
		return list;
	}

}
