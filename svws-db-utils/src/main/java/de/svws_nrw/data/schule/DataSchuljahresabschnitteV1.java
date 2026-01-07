package de.svws_nrw.data.schule;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.schuljahresabschnitt.v1.SchuljahresabschnittV1;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse ist der DataManager für die extern bereitgestellten Schuljahresabschnitte.
 */
public class DataSchuljahresabschnitteV1 {

	/** Der DataManager für den Zugriff auf Schuljahresabschnitt Informationen */
	private final DataSchuljahresabschnitte dataSchuljahresabschnitte;

	/**
	 * Comparator zur Sortierung von Schuljahresabschnitten von Neuster absteigend zu Ältester.
	 */
	private static final Comparator<SchuljahresabschnittV1> SORT_BY_SCHULJAHR_AND_ABSCHNITT_DESC = (s1, s2) -> {
		final int schuljahrComparator = Integer.compare(s2.schuljahr, s1.schuljahr);
		if (schuljahrComparator != 0) {
			return schuljahrComparator;
		}
		return Integer.compare(s2.abschnitt, s1.abschnitt);
	};

	/**
	 * Standardkonstruktor
	 *
	 * @param dataSchuljahresabschnitte interner Schuljahresabschnitt-Manager
	 */
	public DataSchuljahresabschnitteV1(final DataSchuljahresabschnitte dataSchuljahresabschnitte) {
		this.dataSchuljahresabschnitte = dataSchuljahresabschnitte;
	}

	/**
	 * Gibt eine Liste der verfügbaren Schuljahresabschnitte als Response zurück
	 *
	 * @return    eine Liste der verfügbaren Schuljahresabschnitte als Response
	 */
	public Response getAllAsResponse() {
		final List<SchuljahresabschnittV1> schuljahresabschnitte = Optional.of(this.dataSchuljahresabschnitte.getAbschnitte())
				.orElse(Collections.emptyList())
				.stream()
				.map(this::map)
				.sorted(SORT_BY_SCHULJAHR_AND_ABSCHNITT_DESC)
				.toList();
		return Response.status(Response.Status.OK)
				.type(MediaType.APPLICATION_JSON)
				.entity(schuljahresabschnitte)
				.build();
	}

	private SchuljahresabschnittV1 map(final Schuljahresabschnitt abschnitt) {
		final SchuljahresabschnittV1 abschnittV1 = new SchuljahresabschnittV1();
		abschnittV1.id = abschnitt.id;
		abschnittV1.schuljahr = abschnitt.schuljahr;
		abschnittV1.abschnitt = abschnitt.abschnitt;
		abschnittV1.idVorigerAbschnitt = abschnitt.idVorigerAbschnitt;
		abschnittV1.idFolgeAbschnitt = abschnitt.idFolgeAbschnitt;
		return abschnittV1;
	}

}
