package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.UvFach;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Fach-Objekt (z. B. Mathematik) für schnelle, dynamische Manipulationen
 * und zur Speicherung der Referenzen.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenFach {

	/** Der Index im internen Array.  */
	final int interneID;

	/** Die externe ID der DB/GUI.  */
	final long uvID;


	/**
	 * Der Konstruktor.
	 *
	 * @param index    Der Index, unter dem dieses Objekt im Array der aufrufenden Klasse gespeichert ist.
	 * @param uvFach   Das {@link UvFach}.
	 */
	public UvAlgorithmusDynDatenFach(final int index, final @NotNull UvFach uvFach) {
		this.interneID = index;
		this.uvID = uvFach.id;
	}


	@Override
	public @NotNull String toString() {
		return "UvAlgorithmusDynDatenFach[interneID=%d, uvID=%d]"
				.formatted(this.interneID, this.uvID);
	}

}
