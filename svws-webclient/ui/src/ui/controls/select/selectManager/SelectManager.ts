import { shallowRef, toValue, triggerRef, watch, type MaybeRef } from "vue";
import { BaseSelectManager, type BaseSelectManagerConfig } from "./BaseSelectManager";

/**
 * Config des Managers, die die BaseSelectManagerConfig<T> erweitert.
 * Alle Werte können als Ref übergeben werden, wenn der Manager reaktiv auf Änderungen reagieren soll.
 */
export interface SelectManagerConfig<T> extends BaseSelectManagerConfig<T> {
	selectionDisplayText?: MaybeRef<((option: T) => string)>;
	optionDisplayText?: MaybeRef<((option: T) => string)>;
}

/**
 * Einfache Manager Klasse zur Verwendung in einer Select-Komponente (UiSelect.vue oder UiSelectMulti.vue). SelectManager übernehmen die Logik der
 * Select-Komponente. T bezeichnet dabei den Datentyp der auswählbaren Optionen.
 *
 * Diese Komponente kann für alle Datentypen verwendet werden. Zum Beispiel Strings, Numbers, Objects etc.
 */
export class SelectManager<T> extends BaseSelectManager<T> {

	protected _selectionDisplayText = shallowRef<((option: T) => string)>((option) => option?.toString() ?? "");

	protected _optionDisplayText = shallowRef<((option: T) => string)>((option) => option?.toString() ?? "");

	public constructor (config?: SelectManagerConfig<T>) {
		super(config);
		this.initSelectManager(config);
	}

	public setConfig(config?: SelectManagerConfig<T>, setDefaults?: boolean) {
		super.setConfig(config, setDefaults);
		this.initSelectManager(config, setDefaults);
	}

	/**
	 * Initialisierung bzw. Neukonfiguration des Selects.
	 *
	 * @param config   die (neue) Konfiguration des Selects
	 * @param setDefaults   wenn true werden die Defaultwerte für alle Konfigurationen gesetzt, die nicht übergeben wurden. Andernfalls werden die alten
	 * 					    beibehalten. Default ist false
	 */
	protected initSelectManager(config: SelectManagerConfig<T> | undefined, setDefaults: boolean = false) {
		const newSelectionDisplayText = config?.selectionDisplayText ?? (setDefaults ? ((option) => option?.toString() ?? "") : this.selectionDisplayText);
		this._selectionDisplayText = this.initShallowRef(newSelectionDisplayText, v => v);
		const newOptionDisplayText = config?.optionDisplayText ?? (setDefaults ? ((option) => option?.toString() ?? "") : this.optionDisplayText);
		this._optionDisplayText = this.initShallowRef(newOptionDisplayText, v => v);

		/*
		 * Die Watcher beobachten Änderungen der reaktiven Werte der Konfiguration, um diese intern weiterzuleiten.
		 */

		this._watcher.push(
			watch(() => config?.optionDisplayText, newDisplay => {
				this.optionDisplayText = toValue(newDisplay ?? ((option) => option?.toString() ?? ""));
			}, { deep: true }
			)
		);

		this._watcher.push(
			watch(() => config?.selectionDisplayText, newDisplay => {
				this.selectionDisplayText = toValue(newDisplay ?? ((option) => option?.toString() ?? ""));
			}, { deep: true }
			)
		);
	}

	public get selectionDisplayText(): (((option: T) => string)) {
		return this._selectionDisplayText.value;
	}

	public set selectionDisplayText(value: (((option: T) => string))) {
		this._selectionDisplayText.value = value;
		triggerRef(this._selectionDisplayText);
	}

	public get optionDisplayText(): (((option: T) => string)) {
		return this._optionDisplayText.value;
	}

	public set optionDisplayText(value: (((option: T) => string))) {
		this._optionDisplayText.value = value;
		triggerRef(this._optionDisplayText);
	}

}
