package de.svws_nrw.module.reporting.sortierung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ComparatorBuilder {
	private ComparatorBuilder() {
	}

	/**
	 * Erstellt einen Comparator für Objekte des Typs T basierend auf den angegebenen Attributen und der Registrierung
	 * von Attributen im übergebenen {@link SortierungRegistry}. Nicht erkannte Attribute werden protokolliert, falls
	 * die Liste der Validierungsfehler übergeben wurde.
	 *
	 * @param <T> Der Typ der Objekte, die mit dem Comparator verglichen werden sollen.
	 * @param registry Eine {@link SortierungRegistry}, die Comparatoren für bestimmte Attribute bereitstellt.
	 * @param attribute Eine Liste von Strings, die die Sortierattribute und optional Sortierrichtungen spezifizieren.
	 *                  Sortierrichtungen können entweder durch ein vorangestelltes "-" für absteigend oder durch Strings wie ":asc" oder ":desc" spezifiziert werden.
	 * @param validierungsfehler Eine Liste, in der nicht erkannte Attribute als Fehler protokolliert werden.
	 *                         Kann null sein, falls keine Validierungsfehler protokolliert werden sollen.
	 *
	 * @return Ein Comparator, der basierend auf den angegebenen Attributen sortiert. Falls keine gültigen Attribute
	 *         vorhanden sind, wird ein Comparator zurückgegeben, der keine spezielle Sortierung anwendet.
	 */
	public static <T> Comparator<T> build(final SortierungRegistry<T> registry, final List<String> attribute, final List<String> validierungsfehler) {
		final List<Comparator<T>> comparators = new ArrayList<>();
		final List<SortierungAttribut> sortierungAttribute = ComparatorBuilder.sortierungAttribute(attribute);

		for (final SortierungAttribut attribut : sortierungAttribute) {
			registry.attributComparator(attribut.attribut(), attribut.sortiereAufsteigend)
					.ifPresentOrElse(comparators::add, () -> {
						if (validierungsfehler != null)
							validierungsfehler.add("Unbekanntes Sortierattribut: " + attribut.attribut());
					});
		}
		return Comparators.verketten(comparators);
	}

	/**
	 * Diese Klasse repräsentiert ein Attribut, das zur Sortierung verwendet wird.
	 * Sie enthält das zu sortierende Attribut sowie die Information über die Sortierrichtung,
	 * ob diese aufsteigend oder absteigend erfolgen soll.
	 * Eine Instanz dieser Klasse wird typischerweise bei der Verarbeitung von Sortierkriterien
	 * verwendet, um sowohl das Attribut selbst als auch die gewünschte Sortierreihenfolge
	 * sicher und eindeutig zu speichern.
	 * @param attribut Die Bezeichnung des Attributs, das zur Sortierung verwendet werden soll.
	 * @param sortiereAufsteigend Der Wert gibt die Sortierreihenfolge an und ist true, wenn aufsteigend sortiert werden soll, false, wenn absteigend sortiert werden soll.
	 */
	private record SortierungAttribut(String attribut, boolean sortiereAufsteigend) {
	}

	/**
	 * Analysiert eine Liste von Strings mit den Attributen als Sortierkriterium und wandelt diese in eine Liste von SortierungAttribut-Objekten um,
	 * welche die Sortierattribute und Sortierrichtungen repräsentieren.
	 * Die Interpretation der String-Einträge in der übergebenen erfolgt nach folgenden Regeln:
	 * Ein minus-Zeichen ("-") am Anfang des String-Eintrags kennzeichnet ein absteigendes Sortierkriterium.
	 * Eine optionale Endung ":asc" oder ":desc" legt die Sortierrichtung explizit fest.
	 * Alle anderen Eingaben werden als Sortierattribut ohne Richtung (Standard aufsteigend) interpretiert.
	 *
	 * @param attribute Eine Liste von Strings, die Attribute als Sortierkriterien spezifizieren (unter Umständen mit Sortierrichtung). Null- oder leere
	 *               Einträge innerhalb der Liste werden ignoriert.
	 * @return Eine Liste von SortierungAttribut-Objekten, die die gültigen Sortierdefinitionen enthalten.
	 *         Gibt eine leere Liste zurück, wenn die Eingabe null ist oder keinen gültigen String-Eintrag enthält.
	 */
	private static List<SortierungAttribut> sortierungAttribute(final List<String> attribute) {
		if (attribute == null)
			return List.of();

		final List<SortierungAttribut> result = new ArrayList<>();

		for (final String listeneintrag : attribute) {
			if (listeneintrag == null)
				continue;

			String trimmedListeneintrag = listeneintrag.trim();
			if (trimmedListeneintrag.isEmpty())
				continue;

			boolean sortiereAufsteigend = true;

			// Prüfe, ob ein Minus vorangestellt wurde, um eine absteigende Sortierung zu erzwingen.
			if (trimmedListeneintrag.startsWith("-")) {
				sortiereAufsteigend = false;
				trimmedListeneintrag = trimmedListeneintrag.substring(1).trim();
			}

			String attribut = trimmedListeneintrag;

			// Prüfe, ob eine Sortierrichtung per Doppelpunkt angegeben wurde.
			final int indexDoppelpunkt = trimmedListeneintrag.lastIndexOf(':');
			if (indexDoppelpunkt > 0) {
				attribut = trimmedListeneintrag.substring(0, indexDoppelpunkt).trim();
				final String sortierrichtung = trimmedListeneintrag.substring(indexDoppelpunkt + 1).trim().toLowerCase();
				// Der Standardwert ist aufsteigend, auch für unbekannte Sortierrichtungen.
				sortiereAufsteigend = !"desc".equals(sortierrichtung);
			}

			// Erzeuge ein neues SortierAttribut und setze es in die Liste.
			if (!attribut.isBlank())
				result.add(new SortierungAttribut(attribut, sortiereAufsteigend));
		}
		return result;
	}

}
