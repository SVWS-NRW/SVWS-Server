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
	 * Validiert, ob ein Eingabeparameter unverändert ist.
	 * Note: oldValue set, newValue null -> true
	 *
	 * @param oldValue gespeicherter Wert.
	 * @param newValue neuer Wert.
	 * @return true, wenn der wert unverändert ist, ansonsten false.
	 */
	public static boolean isRelevantUpdate(final String oldValue, final String newValue) {
		return !Strings.CS.equals(oldValue, newValue);
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

	/**
	 * Validiert ein Int auf maximal zulässigen Wert
	 *
	 * @param propertyValue übergebener Wert.
	 * @param maxValue maximale Wert.
	 * @param propertyName name der Feldes.
	 */
	public static void validateMaxInteger(final int propertyValue, final int maxValue, final String propertyName) {
		if (propertyValue > maxValue) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					formatMessage("Fehler beim Konvertieren: Der Zahlwert liegt außerhalb des geforderten Bereichs.", propertyName));
		}

	}

	/**
	 * Validiert einen String gegen required not empty
	 * @param propertyValue übergebener Wert.
	 * @param maxLength maximale Feldlänge.
	 * @param propertyName name der Feldes.
	 */
	public static void validateRequiredNonEmpty(final String propertyValue, final int maxLength, final String propertyName) {
		if (propertyValue == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, formatMessage("Der Wert null ist nicht erlaubt.", propertyName));
		}

		if (propertyValue.isEmpty()) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, formatMessage("Ein leerer String ist hier nicht erlaubt.", propertyName));
		}
		//ggf über exc. mapper..
		if (propertyValue.length() > maxLength) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, formatMessage("Die Länge des Strings ist auf %d Zeichen limitiert."
					.formatted(maxLength), propertyName));
		}
	}

	private static String formatMessage(final String message, final String attrName) {
		if ((attrName == null) || (attrName.isBlank()))
			return message;
		return "Attribut %s: %s".formatted(attrName, message);
	}
}
