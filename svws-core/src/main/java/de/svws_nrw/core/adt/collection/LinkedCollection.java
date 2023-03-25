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
package de.svws_nrw.core.adt.collection;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse LinkedCollection implementiert das Interface {@link Collection}. 
 * Sie stellt eine einfache, unsortierte Collection dar und akzeptiert 
 * keine null-Werte, jedoch Duplikate. Intern ist sie als doppelt verkettete 
 * Liste realisiert.
 * 
 * @author Marina Bachran
 *
 * @param <E>   der Inhaltstyp der LinkedCollection
 */
public class LinkedCollection<@NotNull E> implements Deque<@NotNull E>, Cloneable {

	/** Das erste Element der Collection. */
	LinkedCollectionElement<@NotNull E> _head;
	
	/** Das letze Element der Collection. */
	LinkedCollectionElement<@NotNull E> _tail;
	
	/** Die Anzahl der Elemente in der Collection. */
	private int _size;

	/** Die Anzahl der Modifikationen, die an dieser Datenstruktur vorgenommen wurden */
    int _modCount;


	/**
	 * Erzeugt eine neue LinkedCollection.
	 */
	public LinkedCollection() {
		_head = null;
		_tail = null;
		_size = 0;
		_modCount = 0;
	}
	
	/**
	 * Erzeugt eine neue LinkedCollection als Kopie der übergebenen 
	 * LinkedCollection
	 * 
	 * @param c   die LinkedCollection, die kopiert wird
	 */
	public LinkedCollection(final LinkedCollection<? extends @NotNull E> c) {
		_size = 0;
		_modCount = 0;
		final @NotNull Iterator<? extends @NotNull E> iter = c.iterator();
		while (iter.hasNext())
			this.add(iter.next());
		_modCount = c._modCount;
	}

	@Override
	public int size() {
		return _size;
	}

	@Override
	public boolean isEmpty() {
		return (_head == null) ? true : false;
	}

	@Override
	public boolean contains(final Object obj) {
		if (this.isEmpty())
			return false;
		final @NotNull Iterator<@NotNull E> iter = this.iterator();
		while(iter.hasNext())
			if (iter.next().equals(obj))
				return true;
		return false;
	}

	@Override
	public @NotNull Iterator<@NotNull E> iterator() {
		return new LinkedCollectionIterator<>(this);
	}

	@Override
	public @NotNull Object@NotNull[] toArray() {
		// keine Elemente vorhanden. Gebe ein leeres Array zurück.
		if (_size == 0)
			return new Object[0];
		@SuppressWarnings("unchecked")
		final @NotNull E@NotNull[] array = (@NotNull E@NotNull[]) Array.newInstance(_head.getValue().getClass(), _size);
		final @NotNull Iterator<? extends @NotNull E> iter = this.iterator();
		for (int i = 0; i < _size; i++ ) {
			// Die Werte der LinkedCollection werden zurückgegeben. Nicht die Elemente mit der Zeigerstruktur und den Werten
			array[i] = iter.next();  
		}		
		return array;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <@NotNull T> @NotNull T@NotNull[] toArray(final @NotNull T@NotNull[] a) {
		if (a.length < _size)
			return (@NotNull T@NotNull[]) this.toArray();
		final @NotNull Iterator<? extends @NotNull E> iter = this.iterator();
		for (int i = 0; i < _size; i++ ) {
			final @NotNull E e = iter.next();
			a[i] = (@NotNull T) e;
		}
		Arrays.fill(a, _size, a.length, null);
		return a;
	}

	@Override
	public boolean add(final E e) {
		if (e == null)
			return false;
		final @NotNull LinkedCollectionElement<@NotNull E> newElem = new LinkedCollectionElement<>(e, null, null);
		if ((_head == null) || (_tail == null)) {
			_head = newElem;
			_tail = newElem;
			_size++;
			_modCount++;
			return true;
		}
		newElem.setPrev(_tail);
		_tail.setNext(newElem);
		_tail = newElem;
		_size++;
		_modCount++;
		return true;
	}


	/**
	 * Entfernt das übergebene Element.
	 * 
	 * @param elem   das zu entfernende Element
	 *  
	 * @return true, falls das Element erfolgreich entfernt wurde, und false, falls null übergeben wurde. 
	 */
	private boolean removeElement(final LinkedCollectionElement<@NotNull E> elem) {
		if (elem == null)
			return false;
		final LinkedCollectionElement<@NotNull E> prev = elem.getPrev();
		final LinkedCollectionElement<@NotNull E> next = elem.getNext();
		if (this._size == 1) {
			_head = null;
			_tail = null;
		} else if (elem.equals(_head)) {
			if (next == null)
				throw new NullPointerException();
			_head = next;
			next.setPrev(null);
		} else if (elem.equals(_tail)){
			if (prev == null)
				throw new NullPointerException();
			_tail = prev;
			prev.setNext(null);
		} else {
			if ((next == null) || (prev == null))
				throw new NullPointerException();
			next.setPrev(prev);
			prev.setNext(next);
		}
		_size--;
		_modCount++;
		return true;		
	}
	
	
	@Override
	public boolean remove(final Object obj) {
		if (this.isEmpty())
			return false;
		return this.removeElement(findFirst(obj));
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		if ((c == null) || (this == c))
			return true;
		for (final Object o : c)
			if (!this.contains(o))
				return false;
		return true;
	}

	@Override
	public boolean addAll(final Collection<? extends @NotNull E> c) {
		if ((c == null) || (c.size() == 0))
			return false;
		if (c instanceof LinkedCollection) {  // handle the special case where c is this collection (and also other cases)
			final @NotNull LinkedCollection<? extends @NotNull E> coll = (LinkedCollection<? extends @NotNull E>)c;
			// _tail and _head are never null since coll is not empty
			if ((coll._tail == null) || (coll._head == null))
				throw new NullPointerException();
			final @NotNull LinkedCollectionElement<? extends @NotNull E> last = coll._tail;
			LinkedCollectionElement<? extends @NotNull E> current = coll._head;
			this.add(current.getValue());
			while (current != last) {
				current = current.getNext();
				if (current == null)
					throw new NullPointerException();
				this.add(current.getValue());
			}
			return true;
		}
		boolean result = false;
		for (final E elem : c) {
			if (add(elem))
				result = true;
		}
		return result;
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
		if ((this == c) || (_head == null))
			return false;
		if (c == null) {
			this.clear();
			return true;
		}
		final @NotNull Iterator<@NotNull E> iter = this.iterator();
		final @NotNull LinkedCollection<@NotNull E> tmp = new LinkedCollection<>();
		while (iter.hasNext()) {
			final @NotNull E elem = iter.next();
			if (!c.contains(elem))
				tmp.add(elem);				
		}
		if (tmp.isEmpty()) 
			return false;
		return this.removeAll(tmp);
	}

	@Override
	public void clear() {
		_head = null;
		_tail = null;
		_size = 0;
		_modCount++;
	}
	
	@Override
	public int hashCode() {
		int hashCode = 1;
        for (final @NotNull E e : this)
            hashCode = 31*hashCode + (e==null ? 0 : e.hashCode());
        return hashCode;
        
        /*
        if (_head == null)
            return 0;
        int result = 1;
        @NotNull Iterator<@NotNull E> iter = this.iterator();
        while (iter.hasNext()) {
        	@NotNull E elem = iter.next();
        	result = 31 * result + (elem == null ? 0 : elem.hashCode());
        }
        return result;		
        */
	}

	@Override
	public boolean equals(final Object obj) {
		if ((obj == null) || (!(obj instanceof Collection)))
			return false;
		final @NotNull Collection<?> other = (@NotNull Collection<?>) obj;
		if (this._size != other.size())
			return false;
		final @NotNull Iterator<@NotNull E> iter = this.iterator();
		final @NotNull Iterator<?> otherIter = other.iterator();
        while (iter.hasNext()) {
        	if (!iter.next().equals(otherIter.next()))
        			return false;
        }
        return true;
	}

	@Override
	public @NotNull Object clone() throws CloneNotSupportedException {
		return new LinkedCollection<>(this);
	}

	@Override
	public @NotNull String toString() {
		final @NotNull StringBuilder sb = new StringBuilder();
		sb.append("[");
		final @NotNull Iterator<@NotNull E> iter = this.iterator();
		while (iter.hasNext()) {
			sb.append(iter.next());
			if (iter.hasNext() == true)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();	
	}


	/**
	 * Diese Methode ist eine Hilfsmethode für die Methode sort(). Sie mischt die beiden über die prev-Zeiger
	 * verketteten Listen left und right zu einer kombinierten, über die prev-Zeiger verketteten Liste. 
	 * 
	 * @param comparator   ein {@link Comparator} zum Vergleichen zweier Elemente
	 * @param left         die erste sortierte Liste
	 * @param right        die zweite sortierte Liste
	 * 
	 * @return die kombinierte sortierte Liste 
	 */
	private @NotNull LinkedCollectionElement<@NotNull E> merge(final @NotNull Comparator<@NotNull E> comparator, final @NotNull LinkedCollectionElement<@NotNull E> left, final @NotNull LinkedCollectionElement<@NotNull E> right) {
		// Bestimme, was die Quell- und was die Zielliste ist. Die Zielliste enthält danach das kleinste Element 
		LinkedCollectionElement<@NotNull E> headTo;
		LinkedCollectionElement<@NotNull E> headFrom;
		if (comparator.compare(left.getValue(), right.getValue()) > 0) {
			headFrom = left;
			headTo = right;
		} else {
			headFrom = right;
			headTo = left;			
		}
		// Iteriere durch die Zielliste (target-Zeiger)
		@NotNull LinkedCollectionElement<@NotNull E> target = headTo;
		while (headFrom != null) {
			// Entferne das erste Element aus der Quellliste
			final @NotNull LinkedCollectionElement<@NotNull E> current = headFrom;
			headFrom = headFrom.getPrev();
			// Finde das Element in der Zielliste, welches kleiner ist als das aktuelle Element
			LinkedCollectionElement<@NotNull E> targetPrev = target.getPrev(); 
			while ((targetPrev != null) && (comparator.compare(targetPrev.getValue(), current.getValue()) < 0)) {
				target = targetPrev;
				targetPrev = target.getPrev();
			}
			// Füge das Element in die Zielliste ein
			if (targetPrev == null) {
				target.setPrev(current);
				break;
			}
			current.setPrev(targetPrev);
			target.setPrev(current);
		}
		// Gebe die Zielliste zurück
		return headTo;
	}
		
	/**
	 * Sortiert den Inhalte dieser Liste mithilfe des übergebenen {@link Comparator}-Objekts. 
	 * 
	 * @param comparator   ein {@link Comparator} zum Vergleichen zweier Elemente
	 * 
	 * @return true, falls eine Sortierung erfolgreich war 
	 */
	public boolean sort(final Comparator<@NotNull E> comparator) {
		if (comparator == null)
			return false;
		// Spezialfall bei einem oder keinem Element -> die Liste ist automatisch sortiert.
		if ((this._size <= 1) || (this._head == null) || (this._tail == null))
			return true;
		_modCount++;
		// Alle prev-Zeiger auf null setzen (über diese wird die Sortierung aufgebaut, 
		// während die next-Zeiger, die noch zu kombinierenden Teil-Listen miteinander verketten.
		for (LinkedCollectionElement<@NotNull E> current = this._head; current != null; current = current.getNext())
			current.setPrev(null);
		// Entferne aus der Liste der noch zu kombinierenden Teil-Listen die ersten beiden Element, führe eine Merge aus und hänge diese hinten an
		while (this._head != null) {
			// Entferne die ersten beiden Elemente (zwei sortierte Teil-Listen, die zu kombinieren sind)
			final @NotNull LinkedCollectionElement<@NotNull E> left = this._head;
			final LinkedCollectionElement<@NotNull E> right = left.getNext();
			if (right == null)
				throw new NullPointerException();
			this._head = right.getNext();
			left.setNext(null);
			right.setNext(null);
			// Führe einen Merge der beiden Teillisten aus
			final @NotNull LinkedCollectionElement<@NotNull E> sorted = this.merge(comparator, left, right);
			// Hänge die kombinierte Liste hinten an.
			this._tail.setNext(sorted);
			this._tail = sorted;
		}
		this._head = this._tail;
		// Setzte alle next-Pointer auf die prev-Pointer (Wiederherstellen der next-Zeigerverkettung)
		this._tail.setNext(this._tail.getPrev());
		while (this._tail.getPrev() != null) {
			this._tail = this._tail.getPrev();
			if (this._tail == null)
				throw new NullPointerException();
			this._tail.setNext(this._tail.getPrev());
		}
		// Setze die prev-Zeiger für die umgekehrte Verkettung.
		this._head.setPrev(null);
		@NotNull LinkedCollectionElement<@NotNull E> current = this._head;
		LinkedCollectionElement<@NotNull E> next = current.getNext();
		while (next != null) {
			next.setPrev(current);
			current = next;
			next = current.getNext();
		}
		_modCount++;
		return true;
	}

	
	/**
	 * Sucht das Element an der Stelle Index.
	 *   
	 * @param index   die Stelle des gesuchten Elements
	 * 
	 * @return das Element an der gesuchten Stelle 
	 * 
	 * @throws IndexOutOfBoundsException   wenn der Index nicht im gültigen Bereich liegt (index >= 0) && (index < size()))  
	 */
	private @NotNull LinkedCollectionElement<@NotNull E> find(final int index) throws IndexOutOfBoundsException {
		if ((index < 0) || (index >= this._size))
			throw new IndexOutOfBoundsException();
		LinkedCollectionElement<@NotNull E> current = this._head;
		for (int i = 0; (current != null); i++, current = current.getNext())
			if (i == index)
				return current;
		throw new IndexOutOfBoundsException();
	}


	/**
	 * Sucht ein LinkedCollectionElement in der Collection mit dem Wert obj
	 * und gibt es zurück
	 * 
	 * @param obj   der Wert der in der LinkedCollection gesucht werden soll
	 * 
	 * @return  ein LinkedCollectionElement<E> falls der Wert in der Collection 
	 * 			enthalten ist und das Element dessen , ansonsten null
	 */
	private LinkedCollectionElement<@NotNull E> findFirst(final Object obj) {
		if (obj == null)
			return null;
		LinkedCollectionElement<@NotNull E> current = _head;
		while (current != null) {
			if (current.getValue().equals(obj))
				return current;
			current = current.getNext();
		}
		return null;
	}

	/**
	 * Sucht ein LinkedCollectionElement in der Collection mit dem Wert obj
	 * und gibt es zurück
	 * 
	 * @param obj   der Wert der in der LinkedCollection gesucht werden soll
	 * 
	 * @return  ein LinkedCollectionElement<E> falls der Wert in der Collection 
	 * 			enthalten ist und das Element dessen, ansonsten null
	 */
	private LinkedCollectionElement<@NotNull E> findLast(final Object obj) {
		if (obj == null)
			return null;
		LinkedCollectionElement<@NotNull E> current = _tail;
		while (current != null) {
			if (current.getValue().equals(obj))
				return current;
			current = current.getPrev();
		}
		return null;
	}

	@Override
	public boolean offer(final E e) {
		return this.add(e);
	}

	@Override
	public @NotNull E remove() {
		final E value = this.poll();
		if (value == null)
			throw new NoSuchElementException();
		return value;
	}

	@Override
	public E poll() {
		if (this._head == null)
			return null;
		final @NotNull E value = this._head.getValue();
		this._head = this._head.getNext();
		if (this._head == null)
			this._tail = null;
		else
			this._head.setPrev(null);		
		_size--;
		_modCount++;
		return value;
	}

	@Override
	public @NotNull E element() {
		if (this._head == null)
			throw new NoSuchElementException();
		return this._head.getValue();
	}

	@Override
	public E peek() {
		return (this._head == null) ? null : this._head.getValue();
	}

	@Override
	public void addFirst(final E e) {
		if (e == null)
			throw new NullPointerException();
		final @NotNull LinkedCollectionElement<@NotNull E> newElem = new LinkedCollectionElement<>(e, null, null);
		if ((_head == null) || (_tail == null)) {
			_head = newElem;
			_tail = newElem;
		} else {
			newElem.setNext(_head);
			_head.setPrev(newElem);
			_head = newElem;
		}
		_size++;
		_modCount++;
	}

	@Override
	public void addLast(final E e) {
		if (e == null)
			throw new NullPointerException();
		this.add(e);
	}

	@Override
	public boolean offerFirst(final E e) {
		this.addFirst(e);
		return true;
	}

	@Override
	public boolean offerLast(final E e) {
		this.addLast(e);
		return true;
	}

	@Override
	public @NotNull E removeFirst() {
		final E value = this.poll();
		if (value == null)
			throw new NoSuchElementException();
		return value;
	}

	@Override
	public @NotNull E removeLast() {
		final E value = this.pollLast();
		if (value == null)
			throw new NoSuchElementException();
		return value;
	}

	@Override
	public E pollFirst() {
		return this.poll();
	}

	@Override
	public E pollLast() {
		if (this._tail == null)
			return null;
		final @NotNull E value = this._tail.getValue();
		this._tail = this._tail.getPrev();
		if (this._tail == null)
			this._head = null;
		else
			this._tail.setNext(null);		
		_size--;
		_modCount++;
		return value;
	}

	@Override
	public @NotNull E getFirst() {
		if (this._head == null)
			throw new NoSuchElementException();
		return this._head.getValue();
	}

	@Override
	public @NotNull E getLast() {
		if (this._tail == null)
			throw new NoSuchElementException();
		return this._tail.getValue();
	}

	@Override
	public E peekFirst() {
		return (this._head == null) ? null : this._head.getValue();
	}

	@Override
	public E peekLast() {
		return (this._tail == null) ? null : this._tail.getValue();
	}

	@Override
	public boolean removeFirstOccurrence(final Object obj) {
		if (this.isEmpty())
			return false;
		return this.removeElement(findFirst(obj));
	}

	@Override
	public boolean removeLastOccurrence(final Object obj) {
		if (this.isEmpty())
			return false;
		return this.removeElement(findLast(obj));
	}

	@Override
	public void push(final @NotNull E e) {
		this.addFirst(e);
	}

	@Override
	public @NotNull E pop() {
		final E value = this.poll();
		if (value == null)
			throw new NoSuchElementException();
		return value;
	}

	@Override
	public @NotNull Iterator<@NotNull E> descendingIterator() {
		return new LinkedCollectionDescendingIterator<>(this);
	}


	/**
	 * Gibt den Wert an der Stelle index zurück.
	 * 
	 * @param index   der Index
	 * 
	 * @return der Wert
	 * 
	 * @throws IndexOutOfBoundsException   wenn der Index nicht im gültigen Bereich liegt {@code (index >= 0) && (index < size()))}  
	 */
	public @NotNull E get(final int index) throws IndexOutOfBoundsException {
		return this.find(index).getValue();
	}

	/**
	 * Ersetzt den Wert an der Stelle index mit dem neuen übergebenen Wert.
	 *  
	 * @param index     die Stelle, wo der Wert ersetzt werden soll
	 * @param element   der neue Wert für die Stelle 
	 * 
	 * @return der alte Wert an der Stelle
	 * 
	 * @throws IndexOutOfBoundsException   wenn der Index nicht im gültigen Bereich liegt {@code (index >= 0) && (index < size()))}  
	 */
	public @NotNull E set(final int index, final @NotNull E element) throws IndexOutOfBoundsException {
		return this.find(index).setValue(element);
	}
	
}
