import type { ApiFile } from "@core/api/BaseApi";
import type { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import type { GostBelegpruefungErgebnis } from "@core/core/abschluss/gost/GostBelegpruefungErgebnis";
import type { GostBeratungslehrer } from "@core/core/data/gost/GostBeratungslehrer";
import type { GostFach } from "@core/core/data/gost/GostFach";
import type { GostJahrgangsdaten } from "@core/core/data/gost/GostJahrgangsdaten";
import type { GostLaufbahnplanungBeratungsdaten } from "@core/core/data/gost/GostLaufbahnplanungBeratungsdaten";
import type { GostSchuelerFachwahl } from "@core/core/data/gost/GostSchuelerFachwahl";
import type { GostSchuelerGKLWahl } from "@core/core/data/gost/GostSchuelerGKLWahl";
import type { GostKlausurvorgabe } from "@core/core/data/gost/klausuren/GostKlausurvorgabe";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import type { List } from "@core/java/util/List";
import { AppContext } from "@ui/AppContext";
import type { InjectionKey } from "vue";

export type GostBelegpruefungsModus = 'ef1' | 'gesamt' | 'auto';

export type GostKlausurvorgabeEintrag = { fach: GostFach, halbjahr: GostHalbjahr, vorgabe: GostKlausurvorgabe };


/**
 * Die Schnittstelle für den Zustand der Laufbahnplanung der Gymnasialen Oberstufe
 */
export interface GostLaufbahnplanungState {

	/**
	 * Gibt zurück, ob der State für die Laufbahnplanung aktuell gültig ist oder nicht.
	 */
	get valid(): boolean;

	/**
	 * Gibt die Informatione zu dem aktuell ausgewählten Schüler zurück, dessen Laufbahnplanungsdaten
	 * gerade bearbeitet werden.
	 */
	get schueler(): SchuelerListeEintrag;

	/**
	 * Gibt die Informatione zu dem aktuell ausgewählten Schüler zurück, dessen Laufbahnplanungsdaten
	 * gerade bearbeitet werden. Ist keiner ausgewählt, so wird null zurückgegeben.
	 */
	get schuelerOrNull(): SchuelerListeEintrag | null;

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
	setGostBelegpruefungsArt(value: GostBelegpruefungsModus): Promise<void>;

	/**
	 * Exportiert die Laufbahnplanung eines Schülers.
	 *
	 * @returns der Export als ApiFile (name und blob)
	 */
	exportLaufbahnplanung(): Promise<ApiFile>;

	/**
	 * Importiert die Laufbahnplanung von Schülern. Die zu importierenden Daten liegen als FormData
	 * in dem Attribut data vor. Dabei können auch mehrere Dateien angehangen werden.
	 *
	 * @param data   die Laufbahnplanungsdaten der Schüler
	 */
	importLaufbahnplanung(data: FormData): Promise<void>;

	/**
	 * Setzt die Fachwahl des Schülers in Bezug auf das angegebene Fach
	 *
	 * @param idFach   die ID des Faches
	 * @param wahl     die zu setzende Fachwahl
	 */
	setWahl(idFach: number, wahl: GostSchuelerFachwahl): Promise<void>;


	/**
	 * Gibt die Klausurvorgabe für die übergebene ID zurück.
	 *
	 * @param id   die ID der Klausurvorgabe
	 */
	getKlausurvorgabe(id: number | null): GostKlausurvorgabeEintrag | null;

	/**
	 * Gibt zurück, ob ein Gleichwertig Komplexer Leistungsnachweis in dem übergebenen Fach in dem übergebenen Halbjahr
	 * möglich ist oder nicht.
	 *
	 * @param idFach     die ID des Faches
	 * @param halbjahr   das Halbjahr der Gymnasialen Oberstufe
	 *
	 * @returns false, falls kein GKL möglich ist, 0 wenn ein GKL in beiden Quartalen möglich ist und ansonsten das Quartal 1 oder 2
	 */
	istGKLMoeglich(idFach: number, halbjahr: GostHalbjahr): List<GostKlausurvorgabeEintrag>;

	/**
	 * Gibt zurück, ob bei dem angegebenen Fach in dem angegebenen Halbjahr eine Gleichwertig Komplexe Lernleistung (GKL)
	 * gewählt wurde oder nicht.
	 *
	 * @param idFach     die ID des Faches
	 * @param halbjahr   das Halbjahr der Gymnasialen Oberstufe
	 *
	 * return true, falls hier eine GKL gewählt wurde, und ansonsten false
	 */
	istGKLGewaehlt(idFach: number, halbjahr: GostHalbjahr): boolean;

	/**
	 * Gibt die Wahlen zu den Gleichwertig Komplexen Lernleistungen (GKL) zurück.
	 */
	get gklWahlen(): GostSchuelerGKLWahl;

	/**
	 * Führt einen Patch auf die Wahlen zu den Gleichwertig Komplexen Lernleistungen (GKL) aus.
	 *
	 * @param patch   der Patch
	 */
	patchGKLWahlen(patch: Partial<GostSchuelerGKLWahl>): Promise<void>;

	/**
	 * Führt einen Patch auf den Beratungsdaten aus.
	 *
	 * @param data   die Daten für den Patch
	 */
	patchBeratungsdaten(data: Partial<GostLaufbahnplanungBeratungsdaten>): Promise<void>;

	/**
	 * Gibt zurück, ob ein Zwischenspeicher zum Merken der Laufbahnplanung vorliegt oder nicht.
	 */
	get hatZwischenspeicher(): boolean;

	/**
	 * Speichert die aktuelle Laufbahnplanung im Zwischenspeicher
	 */
	saveLaufbahnplanung(): Promise<void>;

	/**
	 * Stellt die Laufbahnplanung aus dem Zwischenspeicher wieder her und ersetzt damit die
	 * aktuelle Laufbahnplanung.
	 */
	restoreLaufbahnplanung(): Promise<void>;

	/**
	 * Setzt die Fachwahlen des Schülers zurück.
	 *
	 * @param forceDelete   gibt an, ob die Fachwahlen vollständig gelöscht werden sollen
	 */
	resetFachwahlen(forceDelete: boolean): Promise<void>;

	/**
	 * Navigiert in der Anwendung zur Kursplanung für das angegeben Halbjahr der gymnasialen Oberstufe
	 *
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 */
	gotoKursplanung(halbjahr: GostHalbjahr): Promise<void>;

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
	const state = AppContext.instance.inject(GostLaufbahnplanungStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des GostLaufbahnplanungState über provide in der main.ts eingebunden");
	}
	return state;
}
