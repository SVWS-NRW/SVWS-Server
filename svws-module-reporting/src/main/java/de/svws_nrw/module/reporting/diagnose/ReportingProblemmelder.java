package de.svws_nrw.module.reporting.diagnose;

/**
 * Der schmale Zugang zur Meldefassade für Stellen, die den Reporting-Context nicht kennen sollen - insbesondere die Thymeleaf-Dialekte.
 * Der Renderer legt eine Methodenreferenz auf die Fassade als Context-Variable ab; eine Vorlage, die diese Variable erreicht, kann darüber
 * ausschließlich melden. Läge stattdessen der ganze Reporting-Context im Thymeleaf-Context, könnte jede Vorlage per OGNL seine Repositories
 * aufrufen und damit die Filterung und die Schichtentrennung umgehen.
 */
@FunctionalInterface
public interface ReportingProblemmelder {

	/**
	 * Meldet ein Ausgabeproblem an die Meldefassade des laufenden Reports. Die Parameter entsprechen denen der Fassade.
	 *
	 * @param ursache      Woran es liegt.
	 * @param auswirkung   Was daraus in der Ausgabe folgt.
	 * @param schluessel   Welches Objekt betroffen ist.
	 * @param beschreibung Der Sachverhalt für das Log.
	 * @param fehler       Der auslösende Fehler oder {@code null}.
	 */
	void melde(ReportingProblemursache ursache, ReportingProblemauswirkung auswirkung, ReportingProblemSchluessel schluessel,
			String beschreibung, Exception fehler);

}
