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
package de.nrw.schule.svws.core.adt.collection;

import jakarta.validation.constraints.NotNull;

/** 
 * Ein Element, das in eine Collection vom Typ LinkedCollection eingefügt werden kann.
 * Es besteht aus einem Wert, der nicht Null sein darf und zwei Zeigern, einer auf das 
 * vorige und einer auf das nachfolgende Element. 
 * Diese Klasse ist package private und außerhalb des packages deshalb nicht sichtbar.
 *  
 * @author Marina Bachran
 *
 * @param <E>   Der Inhaltstyp der LinkedCollection
 */
class LinkedCollectionElement<@NotNull E> {
	
	/** Der Wert, der in diesem Element gespeichert wird. */
	private @NotNull E _value;
	
	/** Referenz auf das vorige Element. */
	private LinkedCollectionElement<@NotNull E> _prev = null;

	/** Referenzt auf das nachfolgende Element. */
	private LinkedCollectionElement<@NotNull E> _next = null;

	/**
	 * Erstellt eine neues LinkedCollectionElement mit den Wert _value und den 
	 * übergebenen Vorgänger bzw. Nachfolger
	 * 
	 * @param value   der Wert des SimpleCollectionElements
	 * @param prev    der Vorgänger
	 * @param next    der Nachfolger
	 */
	LinkedCollectionElement(@NotNull E value, LinkedCollectionElement<@NotNull E> prev, LinkedCollectionElement<@NotNull E> next) {
		this._value = value;
		this._prev = prev;
		this._next = next;
	}
	
	/**
	 * Gibt den Wert des Elements zurück.
	 * 
	 * @return der Wert des Elements
	 */
	@NotNull E getValue() {
		return _value;
	}
	
	/**
	 * Ersetzt den Wert des Elements.
	 * 
	 * @param value   der neue Wert des Elements
	 * 
	 * @return der alte Wert des Elements
	 */
	@NotNull E setValue(@NotNull E value) {
		@NotNull E oldValue = this._value;
		this._value = value;
		return oldValue;
	}
	
	/**
	 * Gibt den Vorgänger des Elementes zurück.
	 * 
	 * @return   das LinkedCollectionElement das der Vorgänger des Elementes ist
	 */
	LinkedCollectionElement<@NotNull E> getPrev() {
		return _prev;
	}
	
	/**
	 * Setzt den Vorgänger des Elementes auf _prev
	 * 
	 * @param prev   der Vorgänger des Elements
	 */
	void setPrev(LinkedCollectionElement<@NotNull E> prev) {
		this._prev = prev;
	}
	
	/**
	 * Gibt den Nachfolger des Elementes zurück.
	 * 
	 * @return   das LinkedCollectionElement das der Nachfolger des Elementes ist
	 */
	LinkedCollectionElement<@NotNull E> getNext() {
		return _next;
	}
	
	/**
	 * Setzt den Nachfolger des Elementes auf _next
	 * 
	 * @param next   der Nachfolger des Elements
	 */
	void setNext(LinkedCollectionElement<@NotNull E> next) {
		this._next = next;
	}

}
