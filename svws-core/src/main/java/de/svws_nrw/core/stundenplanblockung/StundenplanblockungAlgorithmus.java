package de.svws_nrw.core.stundenplanblockung;

import de.svws_nrw.core.Service;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManager;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Service wandelt die Eingabedaten {@link GostBlockungsdatenManager}. <br>
 * Die Klasse ist noch Fake. 
 * 
 * @author Benjamin A. Bartsch 
 */
public class StundenplanblockungAlgorithmus
		extends Service<@NotNull StundenplanblockungManager, @NotNull StundenplanblockungManager> {

	@Override
	public @NotNull StundenplanblockungManager handle(final @NotNull StundenplanblockungManager pInput) {
		return pInput;
	}

}
