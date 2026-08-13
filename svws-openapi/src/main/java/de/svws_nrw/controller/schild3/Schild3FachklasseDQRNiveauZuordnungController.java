package de.svws_nrw.controller.schild3;

import de.svws_nrw.core.data.schild3.Schild3FachklasseDQRNiveauZuordnung;
import jakarta.ws.rs.core.Response;

public interface Schild3FachklasseDQRNiveauZuordnungController {

	/**
	 * Liefert Liste mit allen {@link Schild3FachklasseDQRNiveauZuordnung} Objekten.
	 *
	 * @return Liste von {@link Schild3FachklasseDQRNiveauZuordnung} als Response
	 */
	Response getAll();
}
