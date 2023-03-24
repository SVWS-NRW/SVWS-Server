/*
 * Copyright 2022 Marina Bachran
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its 
 *    contributors may be used to endorse or promote products derived from 
 *    this software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED 
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.nrw.schule.svws.core.adt.tree;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

import jakarta.validation.constraints.NotNull;

/**
 * Eine Implementierung eines Minimum-Heaps. Die Wurzel eines Teilbaumes enthält immer das kleinste Element des Teilbaums. 
 * Duplikate sind zugelassen.
 * 
 * Einschränkung: Darf nicht mehr als 2.147.483.647 (entspricht Integer.MAX_VALUE) Elemente enthalten.
 * 
 * @author Marina Bachran
 *
 * @param <T> der Inhaltstyp des Minimum-Heaps
 */
public class MinHeap<@NotNull T> implements Queue<@NotNull T>, Cloneable {

	/** Die Anzahl der Elemente in diesem Heap. */
	private int _size = 0;

	/** Dieses Array enthält die Elemente des MinHeap. */
	@SuppressWarnings("unchecked")
	private @NotNull T[] _nodes = (T[])new Object[0];

	/** Ein Objekt zum Vergleichen von Werten. */
	private final @NotNull Comparator<@NotNull T> _comparator;

	/** Die initiale Kapazität des Baums */
	private final int _initialCapacity;

	/** Die Anzahl der Modifikationen, die an dieser Datenstruktur vorgenommen wurden */
    protected int _modCount;


	/**
	 * Erzeugt einen neuen Minimum-Heap mit dem übergebenen {@link Comparator} und
	 * der übergebenen initialen Kapazität.
	 * 
	 * @param comparator      das Objekt zum Vergleich von zwei Objekten des Typ T
	 * @param initialCapacity die initiale Kapazität des Baums
	 */
	public MinHeap(final @NotNull Comparator<@NotNull T> comparator, final int initialCapacity) {
		if (initialCapacity <= 0)
			throw new IllegalArgumentException("Die initiale Kapazität muss größer als 0 sein.");
		this._comparator = comparator;
		this._initialCapacity = initialCapacity;
		this._modCount = 0;
	}

	/**
	 * Erzeugt einen neuen Minimum-Heap mit dem übergebenen {@link Comparator} und
	 * einer initialen Kapazität von 63.
	 * 
	 * @param comparator das Objekt zum Vergleich von zwei Objekten des Typ T
	 */
	public MinHeap(final @NotNull Comparator<@NotNull T> comparator) {
		this._comparator = comparator;
		this._initialCapacity = 63;
		this._modCount = 0;
	}
	
	/**
	 * Erstellt eine Kopie des als Parameter übergebenen Heaps. 
	 * 
	 * @param original    Das zu kopierende Original
	 */
	public MinHeap(final @NotNull MinHeap<@NotNull T> original) {
		this._comparator = original._comparator;
		this._initialCapacity = original._initialCapacity;
		this._nodes = Arrays.copyOf(original._nodes, original._nodes.length);
		this._size = original._size;
		this._modCount = original._modCount; 
	}

	@Override
	public boolean add(final @NotNull T e) throws IllegalStateException {
		if (e == null)
			return false;
		if (_nodes.length == 0)
			this._nodes = newArray(e, _initialCapacity);
		if (_size == _nodes.length)
			grow();
		_nodes[_size] = e;
		heapifyUp(_size++);
		this._modCount++;
		return true;
	}

	@Override
	public @NotNull T element() {
		if ((_size == 0) || (_nodes[0] == null))
			throw new NoSuchElementException();
		return _nodes[0];
	}

	@Override
	public boolean offer(final @NotNull T e) {
		return this.add(e);
	}

	@Override
	public T peek() {
		return _nodes.length == 0 ? null : _nodes[0];
	}

	@Override
	public T poll() {
		if (_size == 0)
			return null;
		final T elem = _nodes[0];
		_nodes[0] = _nodes[--_size];
		_nodes[_size] = null;
		heapifyDown(0);
		this._modCount++;
		return elem;
	}

	@Override
	public @NotNull T remove() {
		final T result = poll();
		if (result == null)
			throw new NoSuchElementException();
		return result;
	}

	@Override
	public int size() {
		return _size;
	}

	@Override
	public boolean isEmpty() {
		return this._size == 0;
	}

	@Override
	public boolean contains(final Object o) {
		if (o == null)
			return false;
		for (int i = 0; i < _size; i++) {
			if (_nodes[i].equals(o))
				return true;
		}
		return false;
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		if (c == null)
			return true;
		if (this == c)
			return true;
		for (final Object o : c)
			if (!this.contains(o))
				return false;
		return true;
	}

	@Override
	public boolean addAll(final Collection<? extends @NotNull T> c) throws IllegalStateException {
		if (c == null)
			return false;
		if (this == c) {
			if (_size == 0)
				return false;
			final @NotNull T[] tmp = Arrays.copyOf(_nodes, _size);
			for (final @NotNull T t : tmp)
				if (t != null)
					this.add(t);
			return true;
		}
		boolean result = false;
		for (final T t : c) {
			if (this.add(t))
				result = true;
		}
		return result;
	}

	@Override
	public boolean remove(final Object o) {
		if (o == null)
			return false;
		final int index = findIndex(o);
		if (index == -1)
			return false;
		_size--;
		this._modCount++;
		if (index == _size)
			return true;
		_nodes[index] = _nodes[_size];
		_nodes[_size] = null;
		heapifyUp(index);
		heapifyDown(index);
		return true;
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		if (c == null)
			return false;
		if (this == c) {
			if (this.size() == 0)
				return false;
			this.clear();
			return true;
		}
		boolean result = false;
		for (final Object o : c) {
			if (this.remove(o)) {
				result = true;
				while (this.remove(o)) {
					// Entferne ggf. noch weitere vorkommen...
				}
			}
		}
		return result;
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		if (_size == 0)
			return false;
		if (c == null) {
			this.clear();
			return true;
		}
		if (this == c)
			return false;
		final @NotNull T[] tmp = newArray(_nodes[0], _nodes.length);
		if (tmp == null)
			return false;
		int i = 0;
		T elem;
		boolean changed = false;
		while ((elem = this.poll()) != null) {
			if (c.contains(elem)) 
				tmp[i++] = elem;
			else
				changed = true;
		}
		this._nodes = tmp;
		this._size = i;
		this._modCount++;
		return changed;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		_nodes = (T[])new Object[0];
		_size = 0;
		this._modCount++;
	}

	@Override
	public @NotNull Object@NotNull[] toArray() {
		return copyNodes();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <@NotNull U> @NotNull U@NotNull[] toArray(final @NotNull U@NotNull[] a) {
		if (a.length < _size)
			return (@NotNull U[]) copyNodes();
		System.arraycopy(_nodes, 0, a, 0, _size);
		Arrays.fill(a, _size, a.length, null);
		return a;
	}

	@Override
	public @NotNull Iterator<@NotNull T> iterator() {
		return new MinHeapIterator<>(_nodes, this);
	}
	
	@Override
	public @NotNull Object clone() throws CloneNotSupportedException {
		return new MinHeap<>(this);
	}
	
	/**
	 * Gibt den {@link Comparator} des Minimum Heaps zurück.
	 * 
	 * @return der Comparator
	 */
	public @NotNull Comparator<@NotNull T> comparator() {
		return this._comparator;
	}
	
	/**
	 * Gibt die aktuelle Kapazität des Arrays zurück.
	 * 
	 * @return die aktuelle Kapazität des Arrays zurück
	 */
	public int capacity() {
		return (this._nodes.length == 0) ? this._initialCapacity : this._nodes.length;
	}
	
	/**
	 * Gibt den Inhalt des Minimum Heaps in einem sortierten Array zurück.
	 * 
	 * @return ein sortiertes Array mit den Elementen des Minimum Heaps.
	 */
	@SuppressWarnings("unchecked")
	public @NotNull T@NotNull[] toSortedArray() {
		if (_size == 0)
			return (@NotNull T@NotNull[])new Object[0];
		final @NotNull MinHeap<@NotNull T> copy = new MinHeap<>(this);
		final @NotNull T@NotNull[] tmp = newArray(_nodes[0], _size);
		T current;
		int i = 0;
		while ((current = copy.poll()) != null)
			tmp[i++] = current;
		return tmp;
	}

	/**
	 * Gibt den Inhalt des Heaps als Array-Repräsentation aus.
	 * 
	 * @return der Inhalt des Heaps
	 */
	@Override
	public @NotNull String toString() {
		final @NotNull StringBuilder sb = new StringBuilder();
		for (int i = 0; i < _size; i++) {
			sb.append(_nodes[i]);
			if (i != _size-1)
				sb.append(", ");
		}
		return sb.toString();
	}

	/**
	 * Ermittelt eine Hash-Code für dieses Objekt basierend auf den gespeicherten 
	 * Daten im Heap (die konkrete Ordnung des Baumes wird nicht unterschieden).
	 * 
	 * @return der Hashcode des Minimum Heaps
	 */
	@Override
	public int hashCode() {
		return Arrays.deepHashCode(this.toSortedArray());
	}

	/**
	 * Prüft, ob das übergebene Objekt ein Minimum-Heap ist, der
	 * die gleichen Elemente mit der gleichen Ordnung beinhaltet.
	 * 
	 * @param obj   das zu vergleichende Objekt
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof MinHeap) {
			final MinHeap<?> other = (MinHeap<?>)obj;
			return Arrays.deepEquals(this.toSortedArray(), other.toSortedArray());
		}
		return false;
	}
	
	/**
	 * Liefert zum Index i den Index des Elternteils zurück.
	 * 
	 * @param i
	 * 
	 * @return den Index des Elternteils
	 */
	private static int getParentIndex(final int i) {
		return (i <= 0) ? -1 : (i - 1) / 2;
	}

	/**
	 * Liefert zum Index i den Index des linken Kindes zurück.
	 * 
	 * @param i
	 * 
	 * @return den Index des linken Kindes
	 */
	private static int getLeftChildIndex(final int i) {
		return 2 * i + 1;
	}

	/**
	 * Liefert zum Index i den Index des rechten Kindes zurück.
	 * 
	 * @param i
	 * 
	 * @return den Index des rechten Kindes
	 */
	private static int getRightChildIndex(final int i) {
		return 2 * i + 2;
	}

	/**
	 * Tauscht die Elemente an den Stellen i und j im Array
	 * 
	 * @param i
	 * @param j
	 */
	private void swap(final int i, final int j) {
		final T elem = _nodes[i];
		_nodes[i] = _nodes[j];
		_nodes[j] = elem;
	}

	/**
	 * Stellt die Minimum Heap Eigenschaft vom Index i aus im Baum abwärts her.
	 * 
	 * @param i   ab diesem Index wird im Baum abwärts geprüft.
	 */
	private void heapifyDown(final int i) {
		final int left = getLeftChildIndex(i);
		final int right = getRightChildIndex(i);
		// Prüfe, ob i ein Blatt ist, wenn ja: Abbruch der Rekursion
		if (left >= _size)
			return;
		// gehe davon aus, dass das rechte das 'kleinere' ist und wähle dieses zunächst 
		int child = right;
		if (right == _size) { // prüfe nun ob kein rechtes Kind existiert, dann wähle das linke Kind  
			child = left;
		} else { // prüfe nun, ob das linke Kind nicht doch das 'kleinere' ist
			final T nodeLeft = _nodes[left];
			final T nodeRight = _nodes[right];
			if ((nodeLeft == null) || (nodeRight == null))  // tritt nicht auf 
				return;
			if (_comparator.compare(nodeLeft, nodeRight) < 0)
				child = left;
		}
		// vergleiche und tausche, falls nötig
		final T nodeCurrent = _nodes[i];
		final T nodeChild = _nodes[child];
		if ((nodeCurrent == null) || (nodeChild == null))  // tritt nicht auf
			throw new NullPointerException();
		if (_comparator.compare(nodeCurrent, nodeChild) <= 0)
			return;
		swap(i, child);
		heapifyDown(child);
	}

	/**
	 * Stellt die Minimum-Heap-Eigenschaft des Arrays ab Position i aufwärts wieder her.
	 * 
	 * @param i   ab diesem Index wird überprüft
	 */
	private void heapifyUp(final int i) {
		final int parentIndex = getParentIndex(i);
		if (parentIndex < 0)
			return;
		final T nodeCurrent = _nodes[i];
		final T nodeParent = _nodes[parentIndex];
		if ((nodeCurrent == null) || (nodeParent == null) || (_comparator.compare(nodeCurrent, nodeParent) >= 0))
			return;
		swap(i, parentIndex);
		heapifyUp(parentIndex);
	}

	/**
	 * Erstellt ein neues Array vom Typ T mit der angegebenen Länge.
	 *   
	 * @param elem     Ein Element vom Typ T, welches als Vorlage für die Elemente des Arrays dient 
	 * @param length   die Länge des neuen Arrays
	 * 
	 * @return das neue Array
	 */
	@SuppressWarnings("unchecked")
	private @NotNull T@NotNull[] newArray(final T elem, final int length) {
		if (elem == null)
			return (@NotNull T@NotNull[]) Array.newInstance(Object.class, length);
		return (@NotNull T@NotNull[]) Array.newInstance(elem.getClass(), length);
	}
	
	
	/**
	 * Erzeugt eine Kopie des internen Arrays _nodes.
	 * 
	 * @return die Kopie des _nodes-Array.
	 */
	private @NotNull T@NotNull[] copyNodes() {
		final @NotNull T@NotNull[] result = newArray(_size <= 0 ? null : _nodes[0], _size);
		System.arraycopy(_nodes, 0, result, 0, _size);
		return result;
	}
	
	/**
	 * Lässt den dem Baum zu Grunde liegenden Baum wachsen. Verdoppelt die Menge der Elemente, die im Heap 
	 * gespeichert werden können. 
	 * 
	 * Falls der Heap durch das Wachsen auf mehr als {@link Integer.MAX_VALUE} Elemente ansteigen würde, 
	 * wird eine IllegalStateException geworfen.
	 * 
	 * @throws IllegalStateException
	 */
	private void grow() throws IllegalStateException  {
		if (_nodes.length == Integer.MAX_VALUE)
			throw new IllegalStateException("Der Minimum-Heap kann nicht mehr als " + Integer.MAX_VALUE + " Elemente beinhalten.");
		int newLength = _nodes.length * 2 + 1;
		if (newLength < 0)
			newLength = Integer.MAX_VALUE;
		final @NotNull T@NotNull[] tmp = newArray(_nodes[0], newLength);
		System.arraycopy(_nodes, 0, tmp, 0, _size);
		this._nodes = tmp;
	}

	/**
	 * Findet den Index an dem das Element t im dem dem Heap zu Grunde liegendem Array gespeichert ist. 
	 * Gibt -1 zurück, falls das Element nicht vorhanden ist.
	 * 
	 * @param t   zu diesem Element soll der Index gefunden werden
	 * 
	 * @return  der Index, falls das Element enthalten ist, ansonsten -1
	 */
	private int findIndex(final Object obj) {
		if (obj == null)
			return -1;
		for (int i = 0; i < _size; i++) {
			if (_nodes[i].equals(obj))
				return i;
		}
		return -1;
	}

	/**
	 * Gibt die Anzahl der Operationen zurück, die diese Datenstruktur
	 * verändert haben.
	 * 
	 * @return die Anzahl der Operationen
	 */
	int getModCount() {
		return this._modCount;
	}

}
