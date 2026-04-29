package de.svws_nrw.mapper;

import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * MapStruct-Hilfsmapper fuer {@link JsonNullable}.
 * Stellt eine Condition fuer "nur mappen wenn present" sowie ein generisches Unwrap bereit.
 * <p>
 * Verwendung in MapStruct über {@code uses = JsonNullableMapper.class}.
 * </p>
 */
@Mapper
public interface JsonNullableMapper {

	/**
	 * MapStruct-Condition: {@code true}, wenn der Wrapper vorhanden ist und einen Wert enthaelt
	 * (inkl. "present, aber null").
	 *
	 * @param input JsonNullable-Wrapper
	 * @return {@code true} wenn {@code input != null && input.isPresent()}, sonst {@code false}
	 */
	@Condition
	default boolean isPresent(final JsonNullable<?> input) {
		return (input != null) && input.isPresent();
	}

	/**
	 * Entpackt den Wert aus {@link JsonNullable}.
	 *
	 * @param input JsonNullable-Wrapper
	 * @param <T>   Zieltyp des entpackten Werts
	 * @return entpackter Wert oder {@code null}, wenn {@code input} nicht present ist
	 */
	default <T> T unwrap(final JsonNullable<T> input) {
		return ((input == null) || !input.isPresent()) ? null : input.get();
	}
}
