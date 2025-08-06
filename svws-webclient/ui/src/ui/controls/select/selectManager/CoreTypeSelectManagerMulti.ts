import type { Class } from "../../../../../../core/src/java/lang/Class";
import type { CoreType } from "../../../../../../core/src/asd/types/CoreType";
import type { CoreTypeData } from "../../../../../../core/src/asd/data/CoreTypeData";
import { BaseSelectManagerMulti, type BaseSelectManagerMultiConfig } from "./BaseSelectManagerMulti";
import type { Schulform } from "../../../../../../core/src/asd/types/schule/Schulform";
import { CoreTypeSelectUtils } from "./CoreTypeSelectManagerUtils";
import { CoreTypeDataManager } from "../../../../../../core/src/asd/utils/CoreTypeDataManager";
import type { List } from "../../../../../../core/src/java/util/List";
import { ref } from "vue";

export interface CoreTypeSelectManagerMultiConfig<T extends CoreTypeData, U extends CoreType<T, U>> extends Omit<BaseSelectManagerMultiConfig<T>, 'options'> {
	clazz?: Class<U>;
	schuljahr?: number | null;
	schulformen?: Schulform | Iterable<Schulform> | null;
	selectionDisplayText?: "kuerzel" | "text" | "kuerzelText" | ((option: T) => string);
	optionDisplayText?: "kuerzel" | "text" | "kuerzelText" | ((option: T) => string);
}

export class CoreTypeSelectManagerMulti<T extends CoreTypeData, U extends CoreType<T, U>> extends BaseSelectManagerMulti<T> {

	// Definiert, wie die aktuelle Selektion im Inputfeld dargestellt wird
	protected _selectionDisplayText: "kuerzel" | "text" | "kuerzelText" | (((option: T) => string)) = "kuerzelText";

	// Definiert, wie die Optionen im Dropdown dargestellt werden
	protected _optionDisplayText: "kuerzel" | "text" | "kuerzelText" | (((option: T) => string)) = "kuerzelText";

	// Der CoreTypeDataManager für den Zugriff auf die Daten der CoreTypes
	protected _manager : CoreTypeDataManager<T, U> | null = null;

	// Das Schuljahr, auf deren Basis der Eintrag ermittelt wird
	protected _schuljahr = ref<number | null>(null);

	// Die Schulform, auf dessen Basis der Eintrag ermittelt wird
	protected _schulformen = ref<Schulform | Iterable<Schulform> | null>(null);
	public constructor(config?: CoreTypeSelectManagerMultiConfig<T, U>) {
		if (config === undefined) {
			super(config);
			return;
		}

		let manager: CoreTypeDataManager<T, U> | null = null;
		if (config.clazz !== undefined)
			manager = CoreTypeDataManager.getManager(config.clazz);

		const baseConfig: BaseSelectManagerMultiConfig<T> = {
			options: CoreTypeSelectUtils.getEintraege(manager, config.schuljahr ?? null, config.schulformen ?? null),
			removable: config.removable,
			sort: config.sort,
			filters: config.filters,
			deepSearchAttributes: config.deepSearchAttributes,
		};
		super(baseConfig);
		this.selected = config.selected ?? null;
		this.selectionDisplayText = config.selectionDisplayText ?? "kuerzelText";
		this.optionDisplayText = config.optionDisplayText ?? "kuerzelText";
		this.manager = manager;
		this.schuljahr = config.schuljahr ?? null;
		this.schulformen = config.schulformen ?? null;
		this.updateOptions();
	}

	/**
		 * Setzt alle beliebigen Konfigurationsmerkmale und aktualisiert den Manager intern.
		 *
		 * @param config   Die Config für den SelectManager
		 */
	public setConfig(config: CoreTypeSelectManagerMultiConfig<T, U>) {
		if (config.clazz !== undefined)
			this._manager = CoreTypeDataManager.getManager(config.clazz);
		if (config.schuljahr !== undefined)
			this._schuljahr.value = config.schuljahr;
		if (config.schulformen !== undefined)
			this._schulformen.value = config.schulformen;
		this.updateOptions();
	}

	/**
	 * Getter für den Manager, der die Daten der CoreTypes zur Verfügung stellt.
	 *
	 * @return der Manager
	 */
	public get manager(): CoreTypeDataManager<T, U> | null {
		return this._manager;
	}

	/**
	 * Setter für den Manager, der die Daten der CoreTypes zur Verfügung stellt.
	 *
	 * @param der neue Manager
	 */
	public set manager(value: CoreTypeDataManager<T, U> | null){
		this._manager = value;
		this.updateOptions();
	}


	/**
	 * Getter für das Schuljahr, auf dessen Basis der Eintrag ermittelt wird
	 *
	 * @return das Schuljahr
	 */
	public get schuljahr(): number | null {
		return this._schuljahr.value;
	}

	/**
	 * Setter für das Schuljahr, auf dessen Basis der Eintrag ermittelt wird.
	 *
	 * @param value   das neue Schuljahr
	 */
	public set schuljahr(value: number | null) {
		this._schuljahr.value = value;
		this.updateOptions();
	}

	/**
	 * Getter für die Schulform, auf deren Basis der Eintrag ermittelt wird
	 *
	 * @return das Schulform
	 */
	public get schulformen(): Schulform | Iterable<Schulform> | null {
		return this._schulformen.value;
	}

	/**
	 * Setter für die Schulform, auf deren Basis der Eintrag ermittelt wird
	 *
	 * @param value   die neue Schulform
	 */
	public set schulformen(value: Schulform | Iterable<Schulform> | null) {
		this._schulformen.value = value;
		this.updateOptions();
	}

	/**
	 * Getter für die Anzeige des Optionstextes
	 *
	 * @returns die Definition, wie der Optionstext generiert wird
	 */
	public get optionDisplayText(): "kuerzel" | "text" | "kuerzelText" | ((option: T) => string) {
		return this._optionDisplayText;
	}

	/**
	 * Setter für die Anzeige des Optionstextes
	 *
	 * @param value   der neue Wert für die Generierung
	 */
	public set optionDisplayText(value: "kuerzel" | "text" | "kuerzelText" | ((option: T) => string)) {
		this._optionDisplayText = value;
	}

	/**
	 * Getter für die Anzeige des Selektionstextes
	 *
	 * @returns die Definition, wie der Selektionstext generiert wird
	 */
	public get selectionDisplayText(): "kuerzel" | "text" | "kuerzelText" | ((option: T) => string) {
		return this._selectionDisplayText;
	}

	/**
	 * Setter für die Anzeige des Selektionstextes
	 *
	 * @param value   der neue Wert für die Generierung
	 */
	public set selectionDisplayText(value: "kuerzel" | "text" | "kuerzelText" | ((option: T) => string)) {
		this._selectionDisplayText = value;
	}

	public getSelectionText(option: T | null): string {
		return CoreTypeSelectUtils.getSelectionText(option, this.selectionDisplayText);
	}

	public getOptionText(option: T | null): string {
		return CoreTypeSelectUtils.getOptionText(option, this.optionDisplayText);
	}

	/**
	 * Aktualisiert die Optionen basierend auf dem Manager, dem Schuljahr und den Schulformen
	 */
	private updateOptions() {
		this.options = CoreTypeSelectUtils.getEintraege(this.manager, this.schuljahr, this.schulformen);
	}

}