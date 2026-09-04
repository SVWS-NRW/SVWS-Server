import { CoreTypeData } from "@core/asd/data/CoreTypeData";
import { CoreTypeDataNurSchulformen } from "@core/asd/data/CoreTypeDataNurSchulformen";
import { CoreTypeDataNurSchulformenUndSchulgliederungen } from "@core/asd/data/CoreTypeDataNurSchulformenUndSchulgliederungen";
import { Schulform } from "@core/asd/types/schule/Schulform";
import type { CoreTypeDataManager } from "@core/asd/utils/CoreTypeDataManager";
import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import { JavaObject } from "@core/java/lang/JavaObject";
import type { List } from "@core/java/util/List";
import type { BaseSelectManager } from "@ui/ui/controls/select/manager/BaseSelectManager";
import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
import { toRaw } from "vue";

/**
 * Ein Validator, welcher prüft, ob die Selektion eine Selects noch gültig ist.
 */
export class ValidatorSelectOptionsValid<T> extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => T[];

	private readonly manager: BaseSelectManager<T>;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf die aktuelle Selektion
	 *
	 * @param data      die Funktion zum Zugriff auf die selektierten Optionen
	 * @param manager   der SelectManager des Selects
	 */
	constructor(data: () => T[] | null | undefined, manager: BaseSelectManager<T>) {
		super(ValidatorFehlerart.HINWEIS);
		this.data = () => data() ?? [];
		this.manager = manager;
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor die aktuelle Selektion
	 * ermittelt und anschließend prüft, ob diese plausibel ist.
	 *
	 * @returns true, wenn die Selektion in den erlaubten Optionen enthalten ist
	 */
	protected pruefe(): boolean {
		const data = this.data();

		if (data.length === 0) {
			return true;
		}

		let valid = true;
		for (const selection of data) {
			const rawSelection = toRaw(selection);
			if (!this.isInList(rawSelection, this.manager.filteredOptions)) {
				this.generateFehler(rawSelection);
				valid = false;
			}
		}
		return valid;
	}

	/**
	 * Prüft, ob ein Element in einer Liste enthalten ist.
	 * Nutzt den strukturellen Vergleich, damit gleiche Objekte mit unetrschiedlicher Referenz trotzdem als
	 * gleich interpretiert werden.
	 *
	 * @param element   das zu suchende Element
	 * @param list      die Liste, in der gesucht wird
	 *
	 * @returns true, wenn ein strukturell gleiches Element gefunden wurde
	 */
	private isInList(element: T, list: List<T>): boolean {
		for (const option of list) {
			if (JavaObject.equalsTranspiler(toRaw(option), element)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Generiert die Fehlermeldungen, wenn die Selektion nicht plausibel ist
	 *
	 * @param selectionElement   das zu prüfende Element der Selektion
	 */
	private generateFehler(selectionElement: T): void {
		if (selectionElement instanceof CoreTypeData && this.manager instanceof CoreTypeSelectManager) {
			this.generateCoreTypeFehler(selectionElement, this.manager);
		}
		if (this.getFehler().isEmpty()) {
			this.addFehler(0, `Der ausgewählte Wert ${this.getElementText(selectionElement)}ist nicht mehr gültig.`);
		}
	}

	/**
	 * Generiert spezifische Fehlermeldungen bei der Verwendung eines CoreTypeSelectManagers,
	 * indem die Gültigkeit des Schuljahrs und der Schulform geprüft wird.
	 *
	 * @param selectionElement   das zu prüfende Element der Selektion
	 * @param manager            der CoreTypeSelectManager des Selects
	 */
	private generateCoreTypeFehler(selectionElement: T, manager: CoreTypeSelectManager<any, any>): void {
		if (!this.pruefeSchuljahrGueltigkeit(selectionElement, manager)) {
			return;
		}
		if (!this.pruefeSchulformGueltigkeit(selectionElement, manager)) {
			this.addFehler(0,
				`Der ausgewählte Wert ${this.getElementText(selectionElement)}ist für die Schulform ihrer Schule nicht mehr gültig.`);
		}
	}

	/**
	 * Prüft, ob das Schuljahr des selektierten Elements valide ist.
	 *
	 * @param selectionElement   das zu prüfende Element der Selektion
	 * @param manager            der CoreTypeSelectManager des Selects
	 */
	private pruefeSchuljahrGueltigkeit(selectionElement: T, manager: CoreTypeSelectManager<any, any>): boolean {
		const selectionData = selectionElement as CoreTypeData;

		// Fall 1: Schuljahr ist definiert → Bereichsprüfung
		if (manager.schuljahr !== null) {
			return this.pruefeZwischen(selectionElement, manager.schuljahr);
		}

		// Fall 2: Kein Schuljahr → selectionData muss dem aktuellsten Eintrag entsprechen
		const aktuellsterEintrag = this.getAktuellstenEintrag(manager.manager, selectionData);
		if (aktuellsterEintrag === null) {
			// aktuellste Eintrag konnte nicht berechnet werden
			return false;
		}
		if (selectionData !== aktuellsterEintrag) {
			this.addFehler(0,
				`Der ausgewählte Wert ${this.getElementText(selectionElement)}ist nur bis zum Schuljahr ${selectionData.gueltigBis} gültig.`);
			return false;
		}
		return true;
	}


	/**
	 * Prüft, ob das übergebene Schuljahr in dem Bereich liegt, für den das selektierte Element gültig ist.
	 * Wenn gueltigVon oder gueltigBis des selektierten Elements null ist, dann ist diese Grenze in diese Richtung offen.
	 *
	 * @param selectionElement   das selektierte Element
	 * @param schuljahr          das Schuljahr
	 *
	 * @returns true, wenn das Schuljahr in dem gültigen Zeitraum des selektierten Elements liegt
	 */
	private pruefeZwischen(selectionElement: T, schuljahr: number) {
		const selectionData = selectionElement as CoreTypeData;
		if ((selectionData.gueltigBis !== null) && (selectionData.gueltigBis < schuljahr)) {
			this.addFehler(0,
				`Der ausgewählte Wert ${this.getElementText(selectionElement)}ist nur bis zum Schuljahr ${selectionData.gueltigBis} gültig.`);
			return false;
		}
		if ((selectionData.gueltigVon !== null) && (selectionData.gueltigVon > schuljahr)) {
			this.addFehler(0,
				`Der ausgewählte Wert ${this.getElementText(selectionElement)}ist erst ab dem Schuljahr ${selectionData.gueltigVon} gültig.`);
			return false;
		}
		return true;
	}

	/**
	 * Berechnet den aktuellsten Eintrag eines übergebenen Eintrags.
	 *
	 * @param manager
	 * @param selectionData   der selektierte Eintrag
	 *
	 * @returns den aktuellsten Eintrag eines CoreTypes.
	 * 			null, falls kein CoreTypeDataManager definiert ist oder der Wert/Eintrag nicht berechnet werden konnte.
	 */
	private getAktuellstenEintrag(manager: CoreTypeDataManager<any, any> | null, selectionData: CoreTypeData): CoreTypeData | null {
		if (manager === null) {
			return null;
		}
		const wert = manager.getWertByKuerzel(selectionData.kuerzel);
		if ((wert === undefined) || (wert === null)) {
			return null;
		}
		return manager.getHistorieByWert(wert).getLast();
	}


	/**
	 * Prüft, ob die Schulform des selektierten Elements valide ist.
	 *
	 * @param selectionElement   das zu prüfende Element der Selektion
	 * @param manager            der CoreTypeSelectManager des Selects
	 */
	private pruefeSchulformGueltigkeit(selectionElement: T, manager: CoreTypeSelectManager<any, any>): boolean {
		const selectionData = selectionElement as CoreTypeData;
		const managerSchulformen = this.getManagerSchulformen(manager);
		const selectionSchulformen = this.getSelectionSchulformen(selectionData);

		if ((managerSchulformen.length === 0) || (selectionSchulformen.length === 0)) {
			return true;
		}
		return managerSchulformen.some(item => selectionSchulformen.includes(item));
	}

	/**
	 * Ermittelt die gültigen Schulformen des selektierten Elements.
	 *
	 * @param selectionElement   das zu prüfende Element der Selektion
	 *
	 * @returns Ein Array der Schulformen, für die das Element valide ist.
	 */
	private getSelectionSchulformen(selectionElement: CoreTypeData): Schulform[] {
		if (selectionElement instanceof CoreTypeDataNurSchulformen) {
			return [...selectionElement.schulformen]
				.map(schulform => Schulform.data().getWertByBezeichner(schulform));
		}
		if (selectionElement instanceof CoreTypeDataNurSchulformenUndSchulgliederungen) {
			return [...selectionElement.zulaessig]
				.map(z => Schulform.data().getWertByBezeichner(z.schulform));

		}
		return [];
	}

	/**
	 * Ermittelt die gültigen Schulformen des Managers.
	 *
	 * @param manager   der CoreTypeSelectManager des Selects
	 *
	 * @returns Ein Array der Schulformen, für die Optionen gesetzt werden dürfen.
	 */
	private getManagerSchulformen(manager: CoreTypeSelectManager<any, any>): Schulform[] {
		if (manager.schulformen === null) {
			return [];
		}
		if (manager.schulformen instanceof Schulform) {
			return [manager.schulformen];
		}
		return [...manager.schulformen];
	}

	/**
	 * Gibt die Beschreibung des Elements zurück, falls mehr als eins selektiert ist.
	 * Dient zur Aufklärung, welche genaue Selektion bei einem Multi-Select gemeint ist.
	 *
	 * @param selectionElement   das selektierte Element mit dem Fehlertext, der generiert werden soll.
	 *
	 * @returns Den Selektionstext des selektierten Elements, falls mehrere selektiert sind.
	 */
	private getElementText(selectionElement: T) {
		return (this.data().length > 1) ? '"' + this.manager.getSelectionText(selectionElement) + ' "' : '';
	}

}
