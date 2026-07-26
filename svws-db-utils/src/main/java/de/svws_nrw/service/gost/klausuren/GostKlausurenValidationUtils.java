package de.svws_nrw.service.gost.klausuren;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Zentrale Validierungen für die GOSt-Klausurplanung.
 */
public final class GostKlausurenValidationUtils {

	private GostKlausurenValidationUtils() {
		throw new IllegalStateException("Instantiation not allowed.");
	}

	/**
	 * Liefert zu einer Halbjahres-ID das entsprechende GOSt-Halbjahr.
	 *
	 * @param halbjahr die Halbjahres-ID
	 *
	 * @return das GOSt-Halbjahr
	 */
	public static GostHalbjahr checkHalbjahr(final int halbjahr) {
		final GostHalbjahr hj = GostHalbjahr.fromID(halbjahr);
		if (hj == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Kein gültiges GostHalbjahr angegeben: " + halbjahr);
		}
		return hj;
	}

	/**
	 * Überprüft, ob der Wert für ein Quartal gültig ist.
	 *
	 * @param quartal das Quartal
	 *
	 * @return das Quartal
	 */
	public static int checkQuartal(final int quartal) {
		if (quartal < 0) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Quartal ungültig: " + quartal);
		}
		return quartal;
	}

}
