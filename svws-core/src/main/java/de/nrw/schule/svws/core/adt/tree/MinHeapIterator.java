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

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import jakarta.validation.constraints.NotNull;

/** 
 * Iterator für die Klasse MinHeap. 
 *  
 * @author Marina Bachran
 *
 * @param <T>   Der Inhaltstyp des zum Iterator gehörenden Minimum Heaps
 */
class MinHeapIterator<@NotNull T> implements Iterator<@NotNull T> {

	/** Referenz auf das Array der Elemente im Minimum Heap. */
	private final @NotNull T[] _elements;
	
	/** Die aktuelle Position beim Durchlaufen des Arrays der den Minimum Heap enthält. */
	private int _current;

	/** Die aktuelle Größe des Minimum Heaps, also die Anzahl der enthaltenen Elemente. */
	private final @NotNull MinHeap<@NotNull T> _heap;
	
	/** Die Anzahl der Modifikationen, die bei dem {@link MinHeap} zur Zeit des Erzeugen des 
	 * Iterators gemacht wurden. Dieser Wert muss mit dem bei der {@link MinHeap} übereinstimmen. 
	 * Ansonsten wird eine {@link ConcurrentModificationException} generiert. */
    private final int _expModCount;


	/**
	 * Erstellt einen neuen Iterator für die Klasse MinHeap
	 * 
	 * @param elem   die Elemente des Minimum Heaps 
	 * @param heap   eine Referenz zum Minimum Heap, um auf parallel erfolgende modifizierende Zugriffe reagieren zu können.
	 */
	MinHeapIterator(final @NotNull T[] elem, final @NotNull MinHeap<@NotNull T> heap) {
		_current = -1;
		_elements = elem;
		_heap = heap;
		_expModCount = heap.getModCount();
	}

	@Override
	public boolean hasNext() {
        if (_heap.getModCount() != _expModCount)
            throw new ConcurrentModificationException();
		return ((_current + 1) < _heap.size());  	
	}

	@Override
	public @NotNull T next() {
		if (!hasNext())
			throw new NoSuchElementException("Keine weiteren Elemente vorhanden. Eine Prüfung mit hasNext() vorab ist empfehlenswert.");
		final T elem = _elements[++_current];
		if (elem == null)
			throw new NoSuchElementException("Interner Fehler in der Datenstruktur.");
		return elem;
	}

	@Override
	public void remove() {
        throw new UnsupportedOperationException("remove");
    }

}
