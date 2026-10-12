package de.svws_nrw.service.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.core.types.filter.FilterDomain;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;

/**
 * Die Registry hält die konfigurierten Filterkategorien je {@link FilterDomain} sowie die Filterattribute je Kategorie.
 * Sie ist nach dem Aufbau unveränderlich.
 */
final class FilterRegistry {

	private static final FilterRegistry DEFAULT = createDefault();

	private final Map<FilterDomain, List<FilterCategory>> categoriesByDomain;
	private final Map<FilterCategoryKey, List<FilterAttribute>> attributesByCategory; // NOSONAR - wird für die Attribut-Endpunkte noch benötigt

	/**
	 * Erzeugt eine neue Registry und legt unveränderliche Kopien der übergebenen Konfiguration an.
	 *
	 * @param categoriesByDomain Konfiguration für die Filterkategorien
	 * @param attributesByCategory Konfiguration für die Filterattribute
	 *
	 * @throws NullPointerException wenn eine Map, ein Schlüssel oder ein Wert {@code null} ist
	 */
	FilterRegistry(final Map<FilterDomain, ? extends Collection<FilterCategory>> categoriesByDomain,
			final Map<FilterCategoryKey, ? extends Collection<FilterAttribute>> attributesByCategory) {
		Objects.requireNonNull(categoriesByDomain, "Die Filterkategorien je Domäne dürfen nicht null sein.");
		Objects.requireNonNull(attributesByCategory, "Die Filterattribute je Kategorie dürfen nicht null sein.");

		final EnumMap<FilterDomain, List<FilterCategory>> categoriesByDomainCopy = new EnumMap<>(FilterDomain.class);
		final Map<FilterCategoryKey, List<FilterAttribute>> attributesByCategoryCopy = new HashMap<>();

		categoriesByDomain.forEach((domain, categories) -> {
			Objects.requireNonNull(domain, "Die Filterdomäne darf nicht null sein.");
			Objects.requireNonNull(categories, "Die Filterkategorien der Domäne " + domain + " dürfen nicht null sein.");
			categoriesByDomainCopy.put(domain, List.copyOf(categories));
		});

		attributesByCategory.forEach((key, attributes) -> {
			Objects.requireNonNull(key, "Der Schlüssel der Filterkategorie darf nicht null sein.");
			Objects.requireNonNull(attributes, "Die Filterattribute der Kategorie " + key + " dürfen nicht null sein.");
			attributesByCategoryCopy.put(key, List.copyOf(attributes));
		});

		this.categoriesByDomain = Collections.unmodifiableMap(categoriesByDomainCopy);
		this.attributesByCategory = Collections.unmodifiableMap(attributesByCategoryCopy);
	}

	/**
	 * Erzeugt eine FilterRegistry mit der Default Konfiguration für Filterkategorien und Filterattribute.
	 *
	 * @return Default FilterRegistry
	 */
	static FilterRegistry defaults() {
		return DEFAULT;
	}

	/**
	 * Liefert alle Filterkategorien zu einer {@link FilterDomain} zurück.
	 *
	 * @param domain {@link FilterDomain}
	 *
	 * @return Liste mit Filterkategorien
	 */
	List<FilterCategory> getCategoriesByDomain(final FilterDomain domain) {
		if (domain == null) {
			return List.of();
		}

		return categoriesByDomain.getOrDefault(domain, List.of());
	}

	private static FilterRegistry createDefault() {
		final Builder builder = new Builder();

		builder.domain(
				FilterDomain.SCHUELER,
				createCategoryIndividualdaten(builder)
		);

		return builder.build();
	}

	private static FilterCategory createCategoryIndividualdaten(final Builder builder) {
		return builder.category(
				FilterCategoryKeys.SCHUELER_INDIVIDUALDATEN,
				"Individualdaten",
				1,
				new CoreTypeFilterAttribute(
						FilterAttributeKeys.SCHUELER_STATUS,
						"Status",
						"SchuelerStatus",
						FilterCategoryKeys.SCHUELER_INDIVIDUALDATEN,
						AttributeTarget.of(DTOSchueler.class.getName(), "idStatus")
				),
				new CoreTypeFilterAttribute(
						FilterAttributeKeys.SCHUELER_GESCHLECHT,
						"Geschlecht",
						"Geschlecht",
						FilterCategoryKeys.SCHUELER_INDIVIDUALDATEN,
						AttributeTarget.of(DTOSchueler.class.getName(), "Geschlecht")
				),
				new CoreTypeFilterAttribute(
						FilterAttributeKeys.SCHUELER_WOHNORT,
						"Ort",
						"Orte",
						FilterCategoryKeys.SCHUELER_INDIVIDUALDATEN,
						AttributeTarget.of(DTOSchueler.class.getName(), "Ort_ID")
				)
		);
	}

	private static final class Builder {

		private final Map<FilterDomain, List<FilterCategory>> categoriesByDomain = new EnumMap<>(FilterDomain.class);
		private final Map<FilterCategoryKey, List<FilterAttribute>> attributesByCategory = new HashMap<>();

		FilterCategory category(final FilterCategoryKey key, final String name, final int order, final FilterAttribute... attributes) {
			final List<FilterAttributeKey> attributesKeys = new ArrayList<>();

			for (final FilterAttribute attribute : attributes) {
				attributesByCategory.computeIfAbsent(key, ignored -> new ArrayList<>())
						.add(attribute);
				attributesKeys.add(attribute.getKey());
			}

			return new FilterCategory(key, name, order, attributesKeys);
		}

		void domain(final FilterDomain domain, final FilterCategory... categories) {
			categoriesByDomain.computeIfAbsent(domain, ignored -> new ArrayList<>())
					.addAll(List.of(categories));
		}

		FilterRegistry build() {
			return new FilterRegistry(categoriesByDomain, attributesByCategory);
		}
	}
}
