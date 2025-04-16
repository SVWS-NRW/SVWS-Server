import type { Class, Schulform } from "../../../../../../core/src";
import { ArrayList, CoreTypeDataManager, DeveloperNotificationException, JsonCoreTypeReader } from "../../../../../../core/src";
import type { CoreTypeData } from "../../../../../../core/src/asd/data/CoreTypeData";
import type { CoreType } from "../../../../../../core/src/asd/types/CoreType";
import type { List } from "../../../../../../core/src/java/util/List";
import { BaseSelectManager } from "./BaseSelectManager";
import { ref, toRaw } from "vue";

/**
 * Spezialisierte Manager-Klasse für die Select-Komponente (`UiSelect.vue`), die einfache CoreTypes als Optionstypen unterstützt.
 */
export class CoreTypeSelectManager<T extends CoreTypeData, U extends CoreType<T, U>> extends BaseSelectManager<U> {

	// Der CoreTypeDataManager für den Zugriff auf die Daten der CoreTypes
	protected _manager: CoreTypeDataManager<CoreTypeData, U>;

	// Das Schuljahr, auf deren Basis der Eintrag ermittelt wird
	protected _schuljahr = ref<number>();

	// Die Schulform, auf dessen Basis der Eintrag ermittelt wird
	protected _schulform = ref<Schulform>();

	// Definiert, wie die aktuelle Selektion im Inputfeld dargestellt wird. Als default stehen die Werte "text" (nur Text-Attribut),
	// "kuerzel" (nur Kürzel-Attribut) und "kuerzelText" (Kürzel-Attribut - Text-Attribut) zur Verfügung. Eigene Darstellungen können über Funktionen
	// definiert werden.
	protected _selectionDisplayText: "kuerzel" | "text" | "kuerzelText" | ((option: T) => string) = "kuerzelText";

	// Definiert, wie die Optionen im Dropdown dargestellt werden. Als default stehen die Werte "text" (nur Text-Attribut),
	// "kuerzel" (nur Kürzel-Attribut) und "kuerzelText" (Kürzel-Attribut - Text-Attribut) zur Verfügung. Eigene Darstellungen können über Funktionen
	// definiert werden.
	protected _optionDisplayText: "kuerzel" | "text" | "kuerzelText" | ((option: T) => string) = "kuerzelText";

	/**
	 * Konstrutor des Managers
	 *
	 * @param multi                  definiert, ob Multi-Selektionen erlaubt sind.
	 * @param clazz                  die CoreType Klasse der Optionen.
	 * @param schuljahr 			 das Schuljahr, auf dessen Basis der Eintrag ermittelt wird
	 * @param schulform 			 die Schulform, auf deren Basis der Eintrag ermittelt wird
	 * @param selectionDisplayText   definiert, wie die aktuelle Selektion im Inputfeld dargestellt wird.
	 * @param optionDisplayText      definiert, wie die Optionen im Dropdown dargestellt werden.
	 * @param selected               optional. Die Liste der aktuell selektierten Optionen. Bei einer Singe-Select-Komponente darf maximal ein Objekt in dieser
	 * 							     Liste sein.
	 */
	public constructor(multi: boolean, clazz : Class<U>, schuljahr: number, schulform: Schulform,
		selectionDisplayText: "kuerzel" | "text" | "kuerzelText" | ((option: T) => string) = "kuerzelText",
		optionDisplayText: "kuerzel" | "text" | "kuerzelText" | ((option: T) => string) = "kuerzelText", selected?: any) {

		const manager = CoreTypeDataManager.getManager(clazz);
		const data = manager.getListBySchuljahrAndSchulform(schuljahr, schulform);
		const list = data.isEmpty() ? new ArrayList<U>() : data;

		super(multi, list, selected);
		this._manager = manager;
		this.schuljahr = schuljahr;
		this.schulform = schulform;
		this.optionDisplayText = optionDisplayText;
		this.seletcionDisplayText = selectionDisplayText;
	}

	/**
	 * Getter für den Manager, der die Daten der CoreTypes zur Verfügung stellt.
	 *
	 * @return der Manager
	 */
	public get manager(): CoreTypeDataManager<CoreTypeData, U> {
		return this._manager;
	}

	/**
	 * Setter für den Manager, der die Daten der CoreTypes zur Verfügung stellt.
	 *
	 * @param der neue Manager
	 */
	public set manager(value: CoreTypeDataManager<CoreTypeData, U>){
		this._manager = value;
	}

	/**
	 * Getter für das Schuljahr, auf dessen Basis der Eintrag ermittelt wird
	 *
	 * @return das Schuljahr
	 *
	 * @throws DeveloperNotificationException, wenn das Schuljahr nicht definiert ist, obwohl dies im Konstruktor bereits überprüft wurde.
	 */
	public get schuljahr(): number {
		if (this._schuljahr.value === undefined)
			throw new DeveloperNotificationException("Das Schuljahr ist nicht definiert");
		return this._schuljahr.value;
	}

	/**
	 * Setter für das Schuljahr, auf dessen Basis der Eintrag ermittelt wird
	 *
	 * @param value   das neue Schuljahr
	 */
	public set schuljahr(value: number) {
		this._schuljahr.value = value;
	}

	/**
	 * Getter für die Schulform, auf deren Basis der Eintrag ermittelt wird
	 *
	 * @return das Schulform
	 *
	 * @throws DeveloperNotificationException, wenn die Schulform nicht definiert ist, obwohl dies im Konstruktor bereits überprüft wurde.
	 */
	public get schulform(): Schulform {
		if (this._schulform.value === undefined)
			throw new DeveloperNotificationException("Die Schulform ist nicht definiert");
		return this._schulform.value;
	}

	/**
	 * Setter für die Schulform, auf deren Basis der Eintrag ermittelt wird
	 *
	 * @param value   die neue Schulform
	 */
	public set schulform(value: Schulform) {
		this._schulform.value = value;
	}

	/**
	 * Getter für den Text eines selektierten Objektes.
	 *
	 * @param option   Option, dessen Text abgefragt wird
	 *
	 * @returns den Text der Option.
	 */
	public getSelectionText(option: U): string {
		const eintrag = this._manager.getEintragBySchuljahrUndWert(this.schuljahr, toRaw(option));
		if (eintrag === null)
			throw new DeveloperNotificationException("Der Eintrag konnte nicht gefunden werden, obwohl die Existenz im Konstruktor bestimmt wurde.");
		if (typeof this._selectionDisplayText === "function")
			return this._selectionDisplayText(eintrag as T);
		if (this._selectionDisplayText === "kuerzel")
			return eintrag.kuerzel;
		if (this._selectionDisplayText === "text")
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
	public getOptionText(option: U): string {
		const eintrag = this._manager.getEintragBySchuljahrUndWert(this.schuljahr, option);
		if (eintrag === null)
			throw new DeveloperNotificationException("Der Eintrag konnte nicht gefunden werden, obwohl die Existenz im Konstruktor bestimmt wurde.");
		if (typeof this._optionDisplayText === "function")
			return this._optionDisplayText(eintrag as T);
		if (this._optionDisplayText === "kuerzel")
			return eintrag.kuerzel;
		if (this._optionDisplayText === "text")
			return eintrag.text;
		else
			return `${eintrag.kuerzel} - ${eintrag.text}`;
	}

}