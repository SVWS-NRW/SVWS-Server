package de.svws_nrw.controller.schule.schuljahresabschnitt;

import de.svws_nrw.data.Responses;
import de.svws_nrw.mapper.schule.SchuljahresabschnittV1Mapper;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für den lesenden Zugriff auf Schuljahresabschnitte in der externen API (Version 1).
 */
public final class SchuljahresabschnittV1ControllerImpl implements SchuljahresabschnittV1Controller {

	private final SchuljahresabschnittService service;
	private final SchuljahresabschnittV1Mapper mapper;

	/**
	 * Erstellt einen neuen {@code SchuljahresabschnittV1ControllerImpl} mit dem angegebenen Service und Mapper.
	 *
	 * @param service   der {@link SchuljahresabschnittService}
	 * @param mapper    der {@link SchuljahresabschnittV1Mapper}
	 */
	public SchuljahresabschnittV1ControllerImpl(final SchuljahresabschnittService service, final SchuljahresabschnittV1Mapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@Override
	public Response getList() {
		return Responses.ok(mapper.toApiListNewestFirst(service.getList()));
	}

}
