package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Jahrgangs-Objekt (Stufe) für schnelle, dynamische Manipulationen.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenJahrgang {

	/** Der Index im internen Array.  */
	final int interneID;

	/** Die externe ID der DB/GUI.  */
	final long uvID;


	/**
	 * Der Konstruktor.
	 *
	 * @param index        Der Index, unter dem dieses Objekt im Array der aufrufenden Klasse gespeichert ist.
	 * @param uvJahrgang   Die {@link JahrgangsDaten}.
	 */
	public UvAlgorithmusDynDatenJahrgang(final int index, final @NotNull JahrgangsDaten uvJahrgang) {
		this.interneID = index;
		this.uvID = uvJahrgang.id;
	}


	@Override
	public @NotNull String toString() {
		return "UvAlgorithmusDynDatenJahrgang[interneID=%d, uvID=%d]"
				.formatted(this.interneID, this.uvID);
	}


}
