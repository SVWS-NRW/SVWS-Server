package de.svws_nrw.core.utils.uvblockung;

import jakarta.validation.constraints.NotNull;

/**
 * Ein Undo-Objekt, um Zuordnungen im Algorithmus schnell und effizient rückgängig zu machen.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenUndo {

	/** Die Art des Undos. */
	private final int typ;

	/** Undo-Informationen (abhängig vom Typ). */
	private final @NotNull int @NotNull [] data;


	/**
	 * Der Konstruktor.
	 *
	 * @param typ    Die Art des Undos.
	 * @param ref1   Eine Referenz (Nr. 1) auf eine Array-Position.
	 * @param ref2   Eine Referenz (Nr. 2) auf eine Array-Position.
	 */
	public UvAlgorithmusDynDatenUndo(final int typ, final int ref1, final int ref2) {
		this.typ = typ;
		this.data = new int[] { ref1, ref2 };
	}


	/**
	 * Liefert den Typ des Undos.
	 *
	 * @return den Typ des Undos.
	 */
	public int gibTyp() {
		return typ;
	}


	/**
	 * Liefert die Referenzen in Form eines Arrays.
	 *
	 * @return die Referenzen in Form eines Arrays.
	 */
	public @NotNull int @NotNull [] gibData() {
		return data;
	}


}
