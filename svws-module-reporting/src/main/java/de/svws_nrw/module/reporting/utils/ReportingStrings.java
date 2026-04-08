package de.svws_nrw.module.reporting.utils;

/** Eine Klasse, die für das Reporting-Modul wiederkehrende Strings bereitstellt. */
public final class ReportingStrings {

	private ReportingStrings() {
		throw new IllegalStateException("Statische Klasse mit Konstanten zur Erzeugung von Strings. Initialisierung nicht möglich.");
	}

	/** Konstante für den HTML-Break-Tag "<br/>". */
	public static final String BR = "<br/>";

	/** Konstante für das Wort Frau */
	public static final String FRAU = "Frau";

	/** Konstante für das Wort Frau gefolgt von einem Leerzeichen */
	public static final String FRAU_SPACE = "Frau ";

	/** Konstante für das Wort Herr */
	public static final String HERR = "Herr";

	/** Konstante für das Wort Herr gefolgt von einem Leerzeichen */
	public static final String HERR_SPACE = "Herr ";

	/** Konstante für das Wort Herrn gefolgt von einem Leerzeichen */
	public static final String HERRN_SPACE = "Herrn ";

	/** Konstante für das Wort Familie */
	public static final String FAMILIE = "Familie";

	/** Konstante für das Wort Familie gefolgt von einem Leerzeichen */
	public static final String FAMILIE_SPACE = "Familie ";
}
