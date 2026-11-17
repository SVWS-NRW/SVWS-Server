package de.svws_nrw.service.statistik;

import de.svws_nrw.asd.data.statistik.ZusatzerhebungenStatistikGesamt;
import de.svws_nrw.asd.types.schule.InternatArt;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schule.DTOStatistikZusatzerhebungen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.statistik.ZusatzerhebungenStatistikMapper;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.schule.ZusatzerhebungenStatistikRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;

/**
 * Der Service für den Zugriff auf die Daten der Zusatzerhebungen zur amtlichen Schulstatistik. Ein Datensatz
 * wird erst angelegt, wenn die Daten für einen Schuljahresabschnitt das erste Mal geändert werden. Bis dahin
 * werden die Standardwerte geliefert.
 */
public final class ZusatzerhebungenStatistikService {

	/** Das Repository für die Daten der Zusatzerhebungen */
	private final ZusatzerhebungenStatistikRepository repository;

	/** Das Repository für die Schuljahresabschnitte */
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;

	/** Der Mapper für die Daten der Zusatzerhebungen */
	private final ZusatzerhebungenStatistikMapper mapper;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository                        das Repository für die Daten der Zusatzerhebungen
	 * @param schuljahresabschnitteRepository   das Repository für die Schuljahresabschnitte
	 * @param mapper                            der Mapper für die Daten der Zusatzerhebungen
	 */
	public ZusatzerhebungenStatistikService(final ZusatzerhebungenStatistikRepository repository,
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository, final ZusatzerhebungenStatistikMapper mapper) {
		this.repository = repository;
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
		this.mapper = mapper;
	}

	/**
	 * Gibt die Daten der Zusatzerhebungen für den angegebenen Schuljahresabschnitt zurück. Existiert noch kein
	 * Datensatz, so werden die Standardwerte zurückgegeben, ohne einen Datensatz anzulegen.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @return die Daten der Zusatzerhebungen
	 */
	public @NotNull ZusatzerhebungenStatistikGesamt get(final long idSchuljahresabschnitt) {
		return repository.findById(idSchuljahresabschnitt)
				.map(mapper::toApi)
				.orElseGet(() -> {
					final var daten = new ZusatzerhebungenStatistikGesamt();
					daten.idSchuljahresabschnitt = idSchuljahresabschnitt;
					return daten;
				});
	}

	/**
	 * Führt einen Patch auf die Daten der Zusatzerhebungen für den angegebenen Schuljahresabschnitt aus. Existiert
	 * noch kein Datensatz, so wird dieser angelegt.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param patch                    der Patch-Request
	 *
	 * @return die Daten der Zusatzerhebungen nach dem Patch
	 *
	 * @throws ApiOperationException mit {@code 404 NOT_FOUND}, wenn der Schuljahresabschnitt nicht existiert, und mit
	 *                               {@code 400 BAD_REQUEST}, wenn die Internatsart nicht im Katalog existiert
	 */
	public @NotNull ZusatzerhebungenStatistikGesamt patch(final long idSchuljahresabschnitt, final @NotNull ZusatzerhebungenStatistikPatchRequest patch) {
		return TransactionSupport.transactional(() -> {
			if (schuljahresabschnitteRepository.findById(idSchuljahresabschnitt).isEmpty()) {
				throw new ApiOperationException(Response.Status.NOT_FOUND,
						"Es existiert kein Schuljahresabschnitt mit der ID %d.".formatted(idSchuljahresabschnitt));
			}
			patch.idInternatArt.ifPresent(this::validateIdInternatArt);
			final var vorhanden = repository.findById(idSchuljahresabschnitt);
			final DTOStatistikZusatzerhebungen entity = vorhanden.orElseGet(() -> new DTOStatistikZusatzerhebungen(idSchuljahresabschnitt, false));
			mapper.patch(patch, entity);
			if (vorhanden.isPresent()) {
				repository.update(entity);
			} else {
				repository.create(entity);
			}
			return mapper.toApi(entity);
		});
	}

	private void validateIdInternatArt(final Long idInternatArt) {
		if ((idInternatArt != null) && (InternatArt.data().getEintragByID(idInternatArt) == null)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Es existiert keine gültige Internatsart mit der ID %d.".formatted(idInternatArt));
		}
	}

}
