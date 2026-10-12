package de.svws_nrw.controller.filter;

import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.service.filter.FilterCategoryServiceFactory;
import de.svws_nrw.core.types.filter.FilterDomain;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Factory für die Filter-Controller. Sie prüft die Berechtigungen des Benutzers für die jeweilige {@link FilterDomain}
 * und baut dabei die Datenbankverbindung auf.
 */
public final class FilterControllerFactory {

	/** Die Kompetenzen, die zum Lesen der Filter-Metadaten einer Domain benötigt werden. */
	private static final Map<FilterDomain, Set<BenutzerKompetenz>> READ_KOMPETENZEN_BY_DOMAIN = Map.of(
			FilterDomain.SCHUELER, Set.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)
	);

	private final FilterCategoryServiceFactory filterCategoryServiceFactory;

	private FilterControllerFactory(final FilterCategoryServiceFactory filterCategoryServiceFactory) {
		this.filterCategoryServiceFactory = filterCategoryServiceFactory;
	}

	private static FilterControllerFactory getNewInstance(final HttpServletRequest request, final Set<BenutzerKompetenz> requiredKompetenzen) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, requiredKompetenzen.toArray(new BenutzerKompetenz[0]));
		final var serviceFactory = FilterCategoryServiceFactory.getNewInstance();
		return new FilterControllerFactory(serviceFactory);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Leseberechtigung.
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @param domain die Domain, für die die Filterkategorien gelesen werden sollen
	 *
	 * @return eine {@link FilterControllerFactory}-Instanz mit Leseberechtigung
	 */
	public static FilterControllerFactory withReadAccessByDomain(final HttpServletRequest request, final FilterDomain domain) {
		return getNewInstance(request, READ_KOMPETENZEN_BY_DOMAIN.get(domain));
	}

	/**
	 * Erzeugt einen neuen {@link FilterCategoryController}.
	 *
	 * @return ein neuer {@link FilterCategoryControllerImpl}
	 */
	public FilterCategoryController getFilterCategoryController() {
		return new FilterCategoryControllerImpl(filterCategoryServiceFactory.getService());
	}

}
