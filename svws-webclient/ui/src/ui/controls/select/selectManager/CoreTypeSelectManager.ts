import type { Class } from "../../../../../../core/src/java/lang/Class";
import { Schulform } from "../../../../../../core/src/asd/types/schule/Schulform";
import { ArrayList } from "../../../../../../core/src/java/util/ArrayList";
import { CoreTypeDataManager } from "../../../../../../core/src/asd/utils/CoreTypeDataManager";
import { DeveloperNotificationException } from "../../../../../../core/src/core/exceptions/DeveloperNotificationException";
import type { CoreTypeData } from "../../../../../../core/src/asd/data/CoreTypeData";
import type { CoreType } from "../../../../../../core/src/asd/types/CoreType";
import { BaseSelectManager } from "./BaseSelectManager";
import { ref, toRaw } from "vue";
import type { List } from "../../../../../../core/src/java/util/List";

/**
 * Spezialisierte Manager-Klasse für die Select-Komponente (`UiSelect.vue`), die einfache CoreTypes als Optionstypen unterstützt.
 */
export class CoreTypeSelectManager<T extends CoreTypeData, U extends CoreType<T, U>> extends BaseSelectManager<U> {

	// Der CoreTypeDataManager für den Zugriff auf die Daten der CoreTypes
	protected _manager: CoreTypeDataManager<CoreTypeData, U>;

	// Das Schuljahr, auf deren Basis der Eintrag ermittelt wird
	protected _schuljahr = ref<number | null>(null);

	// Die Schulform, auf dessen Basis der Eintrag ermittelt wird
	protected _schulformen = ref<Schulform | Iterable<Schulform> | null>(null);

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
	 * @param schuljahr 			 	     das Schuljahr, auf dessen Basis der Eintrag ermittelt wird
	 * @param schulformen 			 		 die Schulform(en), auf deren Basis der Eintrag ermittelt wird
	 * @param selectionDisplayText   definiert, wie die aktuelle Selektion im Inputfeld dargestellt wird.
	 * @param optionDisplayText      definiert, wie die Optionen im Dropdown dargestellt werden.
	 * @param selected               optional. Die Liste der aktuell selektierten Optionen. Bei einer Singe-Select-Komponente darf maximal ein Objekt in dieser
	 * 							                 Liste sein.
	 */
	public constructor ( multi: boolean, clazz: Class<U>, schuljahr: number | null, schulformen: Schulform | Iterable<Schulform> | null,
		selectionDisplayText: "kuerzel" | "text" | "kuerzelText" | ( ( option: T ) => string ) = "kuerzelText",
		optionDisplayText: "kuerzel" | "text" | "kuerzelText" | ( ( option: T ) => string ) = "kuerzelText", selected?: any ) {

		let data: List<U> = new ArrayList<U>();
		const manager = CoreTypeDataManager.getManager( clazz );
		if ( schuljahr !== null ) {
			if ( schulformen === null )
				data = manager.getWerteBySchuljahr( schuljahr );
			else if ( schulformen instanceof Schulform )
				data = manager.getListBySchuljahrAndSchulform( schuljahr, schulformen );
			else {
				const result: List<U> = new ArrayList<U>();

				for ( const schulform of schulformen ) {
					const list = manager.getListBySchuljahrAndSchulform( schuljahr, schulform );
					for ( const item of list )
						if ( !result.contains( item ) )
							result.add( item );
				}
				data = result;
			}
		}

		super( multi, data, selected );
		this._manager = manager;
		this.schuljahr = schuljahr;
		this.schulformen = schulformen;
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
		if ( this.schuljahr === null )
			this.allOptions = new ArrayList<U>();
		else if ( this.schulformen === null )
			this.allOptions = this.manager.getWerteBySchuljahr( this.schuljahr );
		else if ( this.schulformen instanceof Schulform )
			this.allOptions = this.manager.getListBySchuljahrAndSchulform( this.schuljahr, toRaw(this.schulformen) );
		else {
			const result: List<U> = new ArrayList<U>();

			for ( const schulform of this.schulformen ) {
				const list = this.manager.getListBySchuljahrAndSchulform( this.schuljahr, schulform );
				for ( const item of list )
					if ( !result.contains( item ) )
						result.add( item );
			}
			this.allOptions = result;
		}
	}

	/**
	 * Getter für den Text eines selektierten Objektes.
	 *
	 * @param option   Option, dessen Text abgefragt wird
	 *
	 * @returns den Text der Option.
	 */
	public getSelectionText ( option: U ): string {
		if ( this.schuljahr === null )
			return ""
		const eintrag = this.manager.getEintragBySchuljahrUndWert(this.schuljahr, toRaw(option));
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
	public getOptionText ( option: U ): string {
		if ( this.schuljahr === null )
			return ""
		const eintrag = this.manager.getEintragBySchuljahrUndWert(this.schuljahr, option);
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
