package de.svws_nrw.module.pdf.html.base;

import org.thymeleaf.context.Context;

/**
 * Abstrakte Klasse als Basisklasse für die Thymeleaf-Daten-Contexts.
 */
public abstract class HtmlContext {

    private Context context;

    /**
     * @return Gibt des Thymeleaf-Daten-Context zurück.
     */
    public Context getContext() {
        return context;
    }

    /**
     * @param context Thymeleaf-Daten-Context mit den Daten.
     */
    public void setContext(final Context context) {
        this.context = context;
    }

}
