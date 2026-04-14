package de.svws_nrw.module.reporting.html.contexts;

import java.util.List;

/**
 * Interface für HTML-Kontexte, die aufgeteilt werden können (z. B. für jeden Schüler oder jede Klasse).
 *
 * @param <C> Der Typ des Einzel-Kontexts, der durch diese Schnittstelle zurückgegeben wird.
 */
public interface HtmlContextAufteilbar<C extends HtmlContext<?>> {
	/**
	 * Zerlegt diesen Kontext in eine Liste von Einzel-Kontexten (z. B. für jeden Schüler oder jede Klasse).
	 *
	 * @return Liste der Einzel-Contexts.
	 */
	List<C> getEinzelContexts();
}
