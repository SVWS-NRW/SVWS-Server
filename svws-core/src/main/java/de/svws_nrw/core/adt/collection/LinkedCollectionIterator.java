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

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import jakarta.validation.constraints.NotNull;

/**
 * Iterator für die Klasse LinkedCollection.
 *
 * @author Marina Bachran
 *
 * @param <E>   Der Inhaltstyp der zum Iterator gehörenden LinkedCollection
 */
class LinkedCollectionIterator<@NotNull E> implements Iterator<@NotNull E> {

	/** Die dem Iterator zugehörige Collection */
	private final @NotNull LinkedCollection<@NotNull E> _collection;

	/** Der Zeiger auf das aktuelle Element */
	private LinkedCollectionElement<@NotNull E> _current;

	/** Die Anzahl der Modifikationen, die bei der {@link LinkedCollection} zur Zeit des Erzeugen des Iterators
	 * gemacht wurden. Dieser Wert muss mit dem bei der {@link LinkedCollection} übereinstimmen. Ansonsten
	 * wird eine {@link ConcurrentModificationException} generiert. */
	private final int _expModCount;


	/**
	 * Erzeugt einen neuen LinkedCollectionIterator. Dabei wird die Referenz auf
	 * die {@link LinkedCollection} übergeben.
	 *
	 * @param collection   die zum Iterator zugehörige {@link LinkedCollection}
	 */
	LinkedCollectionIterator(final @NotNull LinkedCollection<@NotNull E> collection) {
		this._collection = collection;
		this._expModCount = collection._modCount;
		this._current = collection._head;
	}

	@Override
	public boolean hasNext() {
		if (_collection._modCount != _expModCount)
			throw new ConcurrentModificationException();
		return (_current != null);
	}

	@Override
	public @NotNull E next() {
		if (_collection._modCount != _expModCount)
			throw new ConcurrentModificationException();
		if (_current == null)
			throw new NoSuchElementException();
		final @NotNull E result = _current.getValue();
		_current = _current.getNext();
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove");
	}

}
