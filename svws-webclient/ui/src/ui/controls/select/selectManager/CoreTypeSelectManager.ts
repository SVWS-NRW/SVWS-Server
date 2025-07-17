import type { Class } from "../../../../../../core/src/java/lang/Class";
import { Schulform } from "../../../../../../core/src/asd/types/schule/Schulform";
import type { CoreTypeData } from "../../../../../../core/src/asd/data/CoreTypeData";
import type { CoreType } from "../../../../../../core/src/asd/types/CoreType";
import { ref, toRaw } from "vue";
import { CoreTypeDataManager } from "../../../../../../core/src/asd/utils/CoreTypeDataManager";
import { BaseSelectManager, type BaseSelectManagerConfig } from "./BaseSelectManager";
import { ArrayList } from "../../../../../../core/src/java/util/ArrayList";
import type { List } from "../../../../../../core/src/java/util/List";

import { DeveloperNotificationException } from "../../../../../../core/src/core/exceptions/DeveloperNotificationException";

export interface CoreTypeSelectManagerConfig<U extends CoreTypeData, T extends CoreType<U, T>> extends Omit<BaseSelectManagerConfig<T>, 'options'> {
	clazz?: Class<T>;
	schuljahr?: number | null;
	schulformen?: Schulform | Iterable<Schulform> | null;
	selectionDisplayText?: "kuerzel" | "text" | "kuerzelText" | ((option: U) => string);
	optionDisplayText?: "kuerzel" | "text" | "kuerzelText" | ((option: U) => string);
}

/**
 * Spezialisierte Manager-Klasse für die Select-Komponente (`UiSelect.vue`), die einfache CoreTypes als Optionstypen unterstützt.
 */
export class CoreTypeSelectManager<U extends CoreTypeData, T extends CoreType<U, T>> extends BaseSelectManager<T> {

	// Der CoreTypeDataManager für den Zugriff auf die Daten der CoreTypes
	protected _manager : CoreTypeDataManager<CoreTypeData, T> | null = null;

	// Das Schuljahr, auf deren Basis der Eintrag ermittelt wird
	protected _schuljahr = ref<number | null>(null);

	// Die Schulform, auf dessen Basis der Eintrag ermittelt wird
	protected _schulformen = ref<Schulform | Iterable<Schulform> | null>(null);

	/**
	 * Konstrutor des Managers
	 *
	 * @param config      die Konfiguration des Selects. Wenn nicht angegeben, dann wird das Select ohne Optionen generiert.
	 */
	public constructor(config?: CoreTypeSelectManagerConfig<U, T>) {
		if (config === undefined)
			return;
		let data: List<T> = new ArrayList<T>();
		let manager = null;
		if (config.clazz !== undefined) {
			manager = CoreTypeDataManager.getManager(config.clazz);
			if ((config.schuljahr !== null) && (config.schuljahr !== undefined)) {
				if (config.schulformen === null || config.schulformen === undefined)
					data = manager.getWerteBySchuljahr(config.schuljahr);
				else if (config.schulformen instanceof Schulform)
					data = manager.getListBySchuljahrAndSchulform(config.schuljahr, config.schulformen);
				else {
					const result: List<T> = new ArrayList<T>();

					for (const schulform of config.schulformen) {
						const list = manager.getListBySchuljahrAndSchulform(config.schuljahr, schulform);
						for (const item of list)
							if (!result.contains(item))
								result.add(item);
					}
					data = result;
				}
			}
		}

		const baseConfig: BaseSelectManagerConfig<T> = {
			options: data,
			multi: config.multi,
			selected: config.selected,
			removable: config.removable,
			sort: config.sort,
			filters: config.filters,
			selectionDisplayText: config.selectionDisplayText ?? "kuerzelText",
			optionDisplayText: config.optionDisplayText ?? "kuerzelText",
			deepSearchAttributes: config.deepSearchAttributes,
		};
		super(baseConfig);
		this._manager = manager;
		if (config.schuljahr !== undefined)
			this._schuljahr.value = config.schuljahr;
		if (config.schulformen !== undefined)
			this._schulformen.value = config.schulformen;
		this.updateOptions();
	}

	/**
		 * Setzt alle beliebigen Konfigurationsmerkmale und aktualisiert den Manager intern.
		 *
		 * @param config   Die Config für den SelectManager
		 */
	public setConfig(config: CoreTypeSelectManagerConfig<U, T>) {
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
	private get manager(): CoreTypeDataManager<CoreTypeData, T> | null {
		return this._manager;
	}

	/**
	 * Setter für den Manager, der die Daten der CoreTypes zur Verfügung stellt.
	 *
	 * @param der neue Manager
	 */
	private set manager(value: CoreTypeDataManager<CoreTypeData, T> | null){
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
		this.updateOptions( );
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
	 * Aktualisiert die Optionen basierend auf dem aktuellen Schuljahr und den Schulformen.
	 */
	private updateOptions () {
		if ( (this.manager === null) || (this.schuljahr === null ))
			this.options = new ArrayList<T>();
		else if ( this.schulformen === null )
			this.options = this.manager.getWerteBySchuljahr( this.schuljahr );
		else if ( this.schulformen instanceof Schulform )
			this.options = this.manager.getListBySchuljahrAndSchulform( this.schuljahr, toRaw(this.schulformen) );
		else {
			const result: List<T> = new ArrayList<T>();

			for ( const schulform of this.schulformen ) {
				const list = this.manager.getListBySchuljahrAndSchulform( this.schuljahr, schulform );
				for ( const item of list )
					if ( !result.contains( item ) )
						result.add( item );
			}
			this.options = result;
		}
	}

	/**
	 * Getter für den Text eines selektierten Objektes.
	 *
	 * @param option   Option, dessen Text abgefragt wird
	 *
	 * @returns den Text der Option.
	 */
	public getSelectionText ( option: T ): string {
		if ( (this.manager === null) || (this.schuljahr === null ))
			return ""
		const eintrag = this.manager.getEintragBySchuljahrUndWert(this.schuljahr, toRaw(option));
		if (eintrag === null)
			throw new DeveloperNotificationException("Der Eintrag konnte nicht gefunden werden, obwohl die Existenz im Konstruktor bestimmt wurde.");
		if (typeof this.selectionDisplayText === "function")
			return this.selectionDisplayText(eintrag as U);
		if (this.selectionDisplayText === "kuerzel")
			return eintrag.kuerzel;
		if (this.selectionDisplayText === "text")
			return eintrag.text;
		else
			return `${eintrag.kuerzel} - ${eintrag.text}`;
	}

	/**
	 * Getter für den Text der übergebenen Option.
	 *
	 * @param option   Option, dessen Text abgefragt wird
	 *
	 * @returns den Text der Option.
	 */
	public getOptionText ( option: T ): string {
		if ( (this.manager === null) || (this.schuljahr === null ))
			return ""
		const eintrag = this.manager.getEintragBySchuljahrUndWert(this.schuljahr, option);
		if (eintrag === null)
			throw new DeveloperNotificationException("Der Eintrag konnte nicht gefunden werden, obwohl die Existenz im Konstruktor bestimmt wurde.");
		if (typeof this.optionDisplayText === "function")
			return this.optionDisplayText(eintrag as U);
		if (this.optionDisplayText === "kuerzel")
			return eintrag.kuerzel;
		if (this.optionDisplayText === "text")
			return eintrag.text;
		else
			return `${eintrag.kuerzel} - ${eintrag.text}`;
	}

}
