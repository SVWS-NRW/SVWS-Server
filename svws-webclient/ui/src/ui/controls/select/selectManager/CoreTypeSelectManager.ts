import { watch, toValue, triggerRef, shallowRef, type MaybeRef } from "vue";
import type { Class } from "../../../../../../core/src/java/lang/Class";
import type { CoreType } from "../../../../../../core/src/asd/types/CoreType";
import type { CoreTypeData } from "../../../../../../core/src/asd/data/CoreTypeData";
import { CoreTypeDataManager } from "../../../../../../core/src/asd/utils/CoreTypeDataManager";
import { Schulform } from "../../../../../../core/src/asd/types/schule/Schulform";
import { BaseSelectManager, type BaseSelectManagerConfig } from "./BaseSelectManager";
import { ArrayList } from "../../../../../../core/src/java/util/ArrayList";
import type { List } from "../../../../../../core/src/java/util/List";


/**
 * Config des Managers, die die BaseSelectManagerConfig<T> ohne `options` erweitert.
 * Alle Werte können als Ref übergeben werden, wenn der Manager reaktiv auf Änderungen reagieren soll.
 */
export interface CoreTypeSelectManagerConfig<T extends CoreTypeData, U extends CoreType<T, U>> extends Omit<BaseSelectManagerConfig<T>, 'options'> {
	clazz?: MaybeRef<Class<U> | null>;
	schuljahr?: MaybeRef<number | null>;
	schulformen?: MaybeRef<Schulform | Iterable<Schulform> | null>;
	selectionDisplayText?: MaybeRef<"kuerzel" | "text" | "kuerzelText" | ((option: T) => string)>;
	optionDisplayText?: MaybeRef<"kuerzel" | "text" | "kuerzelText" | ((option: T) => string)>;
}

/**
 * Eine konkrete SelectManager-Klasse für CoreTypes zur Verwendung in einer Select-Komponente (UiSelect.vue oder UiSelectMulti.vue). SelectManager übernehmen
 * die Logik der Select-Komponente. T bezeichnet dabei den Datentyp der auswählbaren Optionen.
 * Dieser Manager übernimmt die Berechnung der Optionen des Selects selbst.
 */
export class CoreTypeSelectManager<T extends CoreTypeData, U extends CoreType<T, U>> extends BaseSelectManager<T> {

	protected _selectionDisplayText = shallowRef<"kuerzel" | "text" | "kuerzelText" | (((option: T) => string))>("kuerzelText");

	protected _optionDisplayText = shallowRef<"kuerzel" | "text" | "kuerzelText" | (((option: T) => string))>("kuerzelText");

	/**
	 * Der CoreTypeDataManager für den Zugriff auf die Daten der CoreTypes.
	 * Bei null werden keine Optionen im Select angezeigt.
	 */
	protected _clazz= shallowRef<Class<U> | null>(null);

	/**
	 * Der CoreTypeDataManager für den Zugriff auf die Daten der CoreTypes.
	 * Bei null werden keine Optionen im Select angezeigt.
	 */
	protected _manager: CoreTypeDataManager<T, U> | null = null;

	/**
	 * Das Schuljahr, auf deren Basis der Eintrag ermittelt wird
	 * Bei null werden keine Optionen im Select angezeigt.
	 */
	protected _schuljahr = shallowRef<number | null>(null);

	/**
	 * Die Schulform, auf dessen Basis der Eintrag ermittelt wird.
	 * Bei null werden alle Optionen für alle Schulformen angezeigt.
	 */
	protected _schulformen = shallowRef<Schulform | Iterable<Schulform> | null>(null);


	public constructor(config?: CoreTypeSelectManagerConfig<T, U>) {
		const baseConfig: BaseSelectManagerConfig<T> = {
			sort: config?.sort,
			filters: config?.filters,
		};

		super(baseConfig);
		this.initCoreTypeSelectManager(config);
	}

	public setConfig(config?: CoreTypeSelectManagerConfig<T, U>, setDefaults?: boolean) {
		if (config === undefined)
			super.setConfig(config, setDefaults);

		const baseConfig: BaseSelectManagerConfig<T> = {
			sort: config?.sort,
			filters: config?.filters,
		};

		super.setConfig(baseConfig, setDefaults);
		this.initCoreTypeSelectManager(config, setDefaults);
	}

	/**
	 * Initialisierung bzw. Neukonfiguration des Selects.
	 *
	 * @param config   die (neue) Konfiguration des Selects
	 * @param setDefaults   wenn true werden die Defaultwerte für alle Konfigurationen gesetzt, die nicht übergeben wurden. Andernfalls werden die alten
	 * 					    beibehalten. Default ist false
	 */
	protected initCoreTypeSelectManager(config?: CoreTypeSelectManagerConfig<T, U>, setDefaults: boolean = false) {
		const clazzValue = toValue(config?.clazz);
		this._manager = ((clazzValue !== undefined) && (clazzValue !== null)) ? CoreTypeDataManager.getManager(clazzValue) : null;
		const newSelectionDisplayText = config?.selectionDisplayText ?? (setDefaults ? "kuerzelText" : this.selectionDisplayText);
		this._selectionDisplayText = this.initShallowRef(newSelectionDisplayText, v => v);
		const newOptionDisplayText = config?.optionDisplayText ?? (setDefaults ? "kuerzelText" : this.optionDisplayText);
		this._optionDisplayText = this.initShallowRef(newOptionDisplayText, v => v);
		const newClazz = config?.clazz ?? (setDefaults ? null : this.clazz);
		this._clazz = this.initShallowRef(newClazz, v => v);
		const newSchuljahr = config?.schuljahr ?? (setDefaults ? null : this.schuljahr);
		this._schuljahr = this.initShallowRef(newSchuljahr, v => v);
		const newSchulformen = config?.schulformen ?? (setDefaults ? null : this.schulformen);
		this._schulformen = this.initShallowRef(newSchulformen, v => v);

		this.updateOptions();

		/*
		 * Die Watcher beobachten Änderungen der reaktiven Werte der Konfiguration, um diese intern weiterzuleiten.
		 */

		this._watcher.push(
			watch(() => config?.clazz, newClazz => {
				this.clazz = toValue(newClazz ?? null);
			}, { deep: true }
			)
		);

		this._watcher.push(
			watch(() => config?.schuljahr, newSchuljahr => {
				this.schuljahr = toValue(newSchuljahr ?? null);
			}, { deep: true }
			)
		);

		this._watcher.push(
			watch(() => config?.schulformen, newSchulformen => {
				this.schulformen = toValue(newSchulformen ?? null);
			}, { deep: true }
			)
		);

		this._watcher.push(
			watch(() => config?.selectionDisplayText, newDisplay => {
				this.selectionDisplayText = toValue(newDisplay ?? "kuerzelText");
			}, { deep: true }
			)
		);

		this._watcher.push(
			watch(() => config?.optionDisplayText, newDisplay => {
				this.optionDisplayText = toValue(newDisplay ?? "kuerzelText");
			}, { deep: true }
			)
		);
	}

	public get selectionDisplayText(): "kuerzel" | "text" | "kuerzelText" | (((option: T) => string)) {
		return this._selectionDisplayText.value;
	}

	public set selectionDisplayText(value: "kuerzel" | "text" | "kuerzelText" | (((option: T) => string))) {
		this._selectionDisplayText.value = value;
		triggerRef(this._selectionDisplayText);
	}

	/**
	 * Generiert den Anzeigetext von selektierten Elementen. Dieser kann eine Standardanzeige sein ("kuerzel" | "text" | "kuerzelText") oder eine selbst
	 * definierte Anzeige.
	 *
	 * @param option   die seletierte Option, deren Text berechnet werden soll.
	 * @returns den Anzeigetext der Selektion
	 */
	public getSelectionText(option: T | null): string {
		if (option === null)
			return "";
		if (typeof this.selectionDisplayText === "function")
			return this.selectionDisplayText(option);
		if (this.selectionDisplayText === "kuerzel")
			return option.kuerzel;
		if (this.selectionDisplayText === "text")
			return option.text;
		return `${option.kuerzel} - ${option.text}`;
	}

	public get optionDisplayText(): "kuerzel" | "text" | "kuerzelText" | (((option: T) => string)) {
		return this._optionDisplayText.value;
	}

	public set optionDisplayText(value: "kuerzel" | "text" | "kuerzelText" | (((option: T) => string))) {
		this._optionDisplayText.value = value;
		triggerRef(this._optionDisplayText);
	}

	/**
	 * Generiert den Anzeigetext von Optionen. Dieser kann eine Standardanzeige sein ("kuerzel" | "text" | "kuerzelText") oder eine selbst definierte Anzeige.
	 *
	 * @param option   die Option, deren Text berechnet werden soll.
	 * @returns den Anzeigetext der Option
	 */
	public getOptionText(option: T | null): string {
		if (option === null)
			return "";
		if (typeof this.optionDisplayText === "function")
			return this.optionDisplayText(option);
		if (this.optionDisplayText === "kuerzel")
			return option.kuerzel;
		if (this.optionDisplayText === "text")
			return option.text;
		return `${option.kuerzel} - ${option.text}`;
	}

	/**
	 * Getter für den Manager, der die Daten der CoreTypes zur Verfügung stellt.
	 *
	 * @return der Manager
	 */
	private get manager(): CoreTypeDataManager<T, U> | null {
		return this._manager;
	}

	/**
	 * Setter für den Manager, der die Daten der CoreTypes zur Verfügung stellt.
	 *
	 * @param value   der neue Manager
	 */
	private set manager(value: CoreTypeDataManager<T, U> | null){
		this._manager = value;
		this.updateOptions();
	}

	/**
	 * Getter für die Coretype-Klasse des Selects.
	 *
	 * @return die Klasse
	 */
	public get clazz(): Class<U> | null {
		return this._clazz.value;
	}

	/**
	 * Setter für die Coretype-Klasse des Selects. Updated auch automatisch den Manager und die Optionen
	 *
	 * @param value   die neue Klasse
	 */
	public set clazz(value: Class<U> | null | undefined){
		if (value === this.clazz)
			return;
		this._clazz.value = toValue(value ?? null);
		triggerRef(this._clazz);
		this.manager = ((value === undefined) || (value === null)) ? null : CoreTypeDataManager.getManager(value);
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
		if (value === this.schuljahr)
			return;
		this._schuljahr.value = value;
		triggerRef(this._schuljahr);
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
		if (value === this.schulformen)
			return;
		this._schulformen.value = value;
		triggerRef(this._schulformen);
		this.updateOptions();
	}

	/**
	 * Aktualisiert die (ungefiltereten und gefilterten) Optionen basierend auf dem Manager, dem Schuljahr und den Schulformen.
	 * Ist kein Manager oder Schuljahr gegeben, werden die Optionen auf eine leere Liste gesetzt.
	 */
	private updateOptions() {
		if ((this.manager === null) || (this.schuljahr === null))
			return new ArrayList<T>();

		let werte: List<U>;
		if (this.schulformen === null) {
			werte = this.manager.getWerteBySchuljahr(this.schuljahr);
		} else if (this.schulformen instanceof Schulform) {
			werte = this.manager.getListBySchuljahrAndSchulform(this.schuljahr, this.schulformen);
		} else {
			const result = new ArrayList<U>();
			for (const schulform of this.schulformen) {
				const list = this.manager.getListBySchuljahrAndSchulform(this.schuljahr, schulform);
				for (const item of list)
					if (!result.contains(item))
						result.add(item);
			}
			werte = result;
		}

		const eintraege = new ArrayList<T>();
		for (const coreType of werte)
			eintraege.add(this.manager.getEintragBySchuljahrUndWert(this.schuljahr, coreType));

		this.unfilteredOptions = eintraege;
	}

}