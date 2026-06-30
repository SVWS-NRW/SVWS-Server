import type { InjectionKey } from "vue";
import { inject } from "vue";
import { DeveloperNotificationException } from "../../../core/src/core/exceptions/DeveloperNotificationException";
import type { GostSchuelerFachwahl } from "../../../core/src/core/data/gost/GostSchuelerFachwahl";
import type { ApiFile } from "../../../core/src/api/BaseApi";
import type { SchuelerListeEintrag } from "../../../core/src/core/data/schueler/SchuelerListeEintrag";
import type { GostJahrgangsdaten } from "../../../core/src/core/data/gost/GostJahrgangsdaten";
import type { GostLaufbahnplanungBeratungsdaten } from "../../../core/src/core/data/gost/GostLaufbahnplanungBeratungsdaten";
import type { GostBelegpruefungErgebnis } from "../../../core/src/core/abschluss/gost/GostBelegpruefungErgebnis";
import type { AbiturdatenManager } from "../../../core/src/core/abschluss/gost/AbiturdatenManager";
import type { LehrerListeEintrag } from "../../../core/src/core/data/lehrer/LehrerListeEintrag";
import type { GostHalbjahr } from "../../../core/src/core/types/gost/GostHalbjahr";
import type { List } from "../../../core/src/java/util/List";
import type { GostBeratungslehrer } from "../../../core/src/core/data/gost/GostBeratungslehrer";

export type GostBelegpruefungsModus = 'ef1' | 'gesamt' | 'auto';

/**
 * Die Schnittstelle für den Zustand der Laufbahnplanung der Gymnasialen Oberstufe
 */
export interface GostLaufbahnplanungState {

	/**
	 * Gibt die Informatione zu dem aktuell ausgewählten Schüler zurück, dessen Laufbahnplanungsdaten
	 * gerade bearbeitet werden.
	 */
	get schueler(): SchuelerListeEintrag;

	/**
	 * Gibt die ID des angemeldeten Lehrers zurück, sofern ein Lehrer angmeldet ist
	 */
	get id(): number | undefined;

	/**
	 * Gibt die Liste der Lehrer zurück.
	 */
	get listeLehrer(): List<LehrerListeEintrag>;

	/**
	 * Gibt eine Map mit den Lehrern zurück.
	 */
	get mapLehrer(): Map<number, LehrerListeEintrag>;

	/**
	 * Gibt die Informationen zum aktuellen Jahrgang der gymnasialen Oberstufe zurück, welchem der
	 * Schüler angehört.
	 */
	get gostJahrgangsdaten(): GostJahrgangsdaten;

	/**
	 * Gibt die Beratungslehrer des Abiturjahrganges als neue Liste zurück.
	 */
	get beratungslehrer(): List<GostBeratungslehrer>;

	/**
	 * Gibt die Beratungsdaten für die Laufbahnplanung zurück.
	 */
	get gostLaufbahnBeratungsdaten(): GostLaufbahnplanungBeratungsdaten;

	/**
	 * Gibt das aktuell berechnete Ergebnis der Belegprüfung zurück.
	 */
	get gostBelegpruefungErgebnis(): GostBelegpruefungErgebnis;

	/**
	 * Gibt den aktuellen Manager für die Abiturdaten zurück.
	 */
	get abiturdatenManager(): AbiturdatenManager;

	/**
	 * Gibt die Art der Belegprüfung zurück, die durchgeführt wird.
	 */
	get gostBelegpruefungsArt(): GostBelegpruefungsModus;

	/**
	 * Setzt die Art der Belegprüfung, die durchgeführt wird.
	 *
	 * @param value   die Art der Belegprüfung
	 */
	setGostBelegpruefungsArt: (value: GostBelegpruefungsModus) => Promise<void>;

	/**
	 * Exportiert die Laufbahnplanung eines Schülers.
	 *
	 * @returns der Export als ApiFile (name und blob)
	 */
	exportLaufbahnplanung: () => Promise<ApiFile>;

	/**
	 * Importiert die Laufbahnplanung von Schülern. Die zu importierenden Daten liegen als FormData
	 * in dem Attribut data vor. Dabei können auch mehrere Dateien angehangen werden.
	 *
	 * @param data   die Laufbahnplanungsdaten der Schüler
	 */
	importLaufbahnplanung: (data: FormData) => Promise<void>;

	/**
	 * Setzt die Fachwahl des Schülers in Bezug auf das angegebene Fach
	 *
	 * @param idFach   die ID des Faches
	 * @param wahl     die zu setzende Fachwahl
	 */
	setWahl: (idFach: number, wahl: GostSchuelerFachwahl) => Promise<void>;

	/**
	 * Führt einen Patch auf den Beratungsdaten aus.
	 *
	 * @param data   die Daten für den Patch
	 */
	patchBeratungsdaten: (data: Partial<GostLaufbahnplanungBeratungsdaten>) => Promise<void>;

	/**
	 * Gibt zurück, ob ein Zwischenspeicher zum Merken der Laufbahnplanung vorliegt oder nicht.
	 */
	get hatZwischenspeicher(): boolean;

	/**
	 * Speichert die aktuelle Laufbahnplanung im Zwischenspeicher
	 */
	saveLaufbahnplanung: () => Promise<void>;

	/**
	 * Stellt die Laufbahnplanung aus dem Zwischenspeicher wieder her und ersetzt damit die
	 * aktuelle Laufbahnplanung.
	 */
	restoreLaufbahnplanung: () => Promise<void>;

	/**
	 * Setzt die Fachwahlen des Schülers zurück.
	 *
	 * @param forceDelete   gibt an, ob die Fachwahlen vollständig gelöscht werden sollen
	 */
	resetFachwahlen: (forceDelete: boolean) => Promise<void>;

	/**
	 * Navigiert in der Anwendung zur Kursplanung für das angegeben Halbjahr der gymnasialen Oberstufe
	 *
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 */
	gotoKursplanung: (halbjahr: GostHalbjahr) => Promise<void>;

	/**
	 * Fügt den Lehrer mit der übegebenen ID als Beratungslehrer zu der Liste der Bratungslehrer des Abiturjahrganges hinzu.
	 * Wird nicht in Schüler-spezifischen Ansichten unterstützt.
	 *
	 * @param id   die ID des neuen Beratungslehrers
	 */
	addBeratungslehrer(id: number): Promise<void>;

	/**
	 * Entfernt die angegebenen Beratungslehrer aus der Liste der Bratungslehrer des Abiturjahrganges.
	 * Wird nicht in Schüler-spezifischen Ansichten unterstützt.
	 *
	 * @param eintraege   die zu entfernenden Einträge zu Beratungslehrern
	 */
	removeBeratungslehrer(eintraege: GostBeratungslehrer[]): Promise<void>;

}


export const GostLaufbahnplanungStateKey: InjectionKey<GostLaufbahnplanungState> = Symbol('GostLaufbahnplanungState');

export function useGostLaufbahnplanungState(): GostLaufbahnplanungState {
	const state = inject(GostLaufbahnplanungStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des GostLaufbahnplanungState über provide in der main.ts eingebunden");
	}
	return state;
}
