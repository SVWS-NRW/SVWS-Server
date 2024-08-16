package de.svws_nrw.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Das funktionale Interface für ein einfaches DTO-Mapping
 *
 * @param <T> der DB-DTO-Typ
 * @param <R> der Core-DTO-Typ
 * @param <M> der Manager-Typ
 */
@FunctionalInterface
public interface DTOMapper<T, R, M> {

	/**
	 * Applies this function to the given argument.
	 *
	 * @param t the function argument
	 * @return the function result
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	R apply(T t, M m) throws ApiOperationException;


	/**
	 * Führt ein Mapping von den DB-DTOs vom Typ D auf die Core-DTOs vom Typ C auf alle
	 * DB-DTOs der übergebenen Collection durch und gibt die Ergebnisse in einer Liste zurück.
	 *
	 * @param <D>             der DB-DTO-Typ
	 * @param <C>             der Core-DTO-Typ
	 * @param dtoCollection   die Collection der DB-DTOs
	 * @param mapper          der dto-Mapper
	 *
	 * @return die Liste der Core-DTOs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	static <D, C> List<C> mapList(final Collection<D> dtoCollection, final DTOMapper<D, C> mapper) throws ApiOperationException {
		final List<C> daten = new ArrayList<>();
		for (final D dto : dtoCollection)
			daten.add(mapper.apply(dto));
		return daten;
	}

}
