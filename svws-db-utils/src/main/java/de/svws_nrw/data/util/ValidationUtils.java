package de.svws_nrw.data.util;

import java.util.Objects;

import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

/**
 * Utilities für Validationen.
 */
public final class ValidationUtils {

	private ValidationUtils() {
		//static only
	}

	/**
	 * Validiert, ob ein Eingabeparameter leer oder unverändert ist.
	 * Note: oldValue set, newValue null -> true
	 *
	 * @param oldValue gespeicherter Wert.
	 * @param newValue neuer Wert.
	 * @return true, wenn der wert blank ist oder unverändert ist, ansonsten false.
	 */
	public static boolean isBlankOrUnchanged(final String oldValue, final String newValue) {
		return StringUtils.isBlank(newValue) || Strings.CS.equals(oldValue, newValue);
	}

	/**
	 * Validiert, ob die übergebene ID konform zur Sequenz des Entities ist
	 *
	 * @param sequenceID  ID der Datenbank Sequenz.
	 * @param name Name des Attributs.
	 * @param providedId ID des Requests.
	 */
	public static void validateId(final Long sequenceID, final String name, final Object providedId) {
		final Long id = JSONMapper.convertToLong(providedId, false, name);
		if (!Objects.equals(sequenceID, id)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, sequenceID));
		}
	}
}
