package de.svws_nrw.mapper.schule;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.schuljahresabschnitt.v1.SchuljahresabschnittV1;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct-Mapper für die externe API (Version 1): mappt {@link Schuljahresabschnitt} in {@link SchuljahresabschnittV1}.
 */
@Mapper
public interface SchuljahresabschnittV1Mapper {

	/** Instanz des Mappers */
	SchuljahresabschnittV1Mapper INSTANCE = Mappers.getMapper(SchuljahresabschnittV1Mapper.class);

	/** Sortiert nach Schuljahr und Abschnitt absteigend, der neueste Abschnitt steht also zuerst */
	Comparator<SchuljahresabschnittV1> NEWEST_FIRST = Comparator.comparingInt((final SchuljahresabschnittV1 abschnitt) -> abschnitt.schuljahr)
			.thenComparingInt(abschnitt -> abschnitt.abschnitt)
			.reversed();

	/**
	 * Mappt einen {@link Schuljahresabschnitt} in einen {@link SchuljahresabschnittV1}.
	 *
	 * @param abschnitt   der {@link Schuljahresabschnitt}
	 *
	 * @return der {@link SchuljahresabschnittV1}
	 */
	SchuljahresabschnittV1 toApi(Schuljahresabschnitt abschnitt);

	/**
	 * Mappt die {@link Schuljahresabschnitt}e in {@link SchuljahresabschnittV1} und sortiert sie nach Schuljahr und
	 * Abschnitt absteigend, der neueste Abschnitt steht also zuerst.
	 *
	 * @param abschnitte   die {@link Schuljahresabschnitt}e
	 *
	 * @return die sortierte Liste der {@link SchuljahresabschnittV1}
	 */
	default List<SchuljahresabschnittV1> toApiListNewestFirst(final Collection<Schuljahresabschnitt> abschnitte) {
		return abschnitte.stream()
				.map(this::toApi)
				.sorted(NEWEST_FIRST)
				.toList();
	}

}
