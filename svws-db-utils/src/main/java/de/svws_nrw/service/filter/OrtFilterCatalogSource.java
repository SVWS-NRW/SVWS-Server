package de.svws_nrw.service.filter;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.svws_nrw.core.data.filter.FilterCatalogValue;
import de.svws_nrw.mapper.schule.katalog.ort.OrtMapper;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;

/**
 * Die Quelle für die auswählbaren Werte des Katalogs der Orte.
 */
public final class OrtFilterCatalogSource implements FilterCatalogSource {

	private final OrtRepository repository;

	private final OrtMapper mapper;

	/**
	 * Erzeugt eine neue Quelle.
	 *
	 * @param repository das Repository für den Zugriff auf die Orte
	 * @param mapper der Mapper für die Abbildung der Orte auf die Response-DTOs
	 *
	 * @throws NullPointerException wenn einer der Parameter {@code null} ist
	 */
	public OrtFilterCatalogSource(final OrtRepository repository, final OrtMapper mapper) {
		this.repository = Objects.requireNonNull(repository, "Das OrtRepository darf nicht null sein.");
		this.mapper = Objects.requireNonNull(mapper, "Der OrtMapper darf nicht null sein.");
	}

	@Override
	public FilterCatalog getCatalog() {
		return FilterCatalog.ORTE;
	}

	@Override
	public List<FilterCatalogValue> loadValues() {
		return repository.getAll().stream()
				.map(mapper::toFilterCatalogValue)
				.sorted(Comparator.comparing((final FilterCatalogValue value) -> value.bezeichnung, Collator.getInstance(Locale.GERMAN)))
				.toList();
	}

}
