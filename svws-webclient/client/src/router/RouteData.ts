import { ref } from "vue";
import { DeveloperNotificationException } from "@core";
import { StateManager, ViewType } from "@ui";
import { type RouteNode } from "~/router/RouteNode";

/**
 * Definiert die gemeinsamen State-Attribute, die jede Route mindestens bereitstellen kann.
 *
 * Die Felder sind optional, da nicht jede Route "view" und "gruppenprozesse" Ansichten hat.
 * Konkrete `RouteData*`-Implementierungen erweitern dieses Interface.
 */
export interface RouteStateInterface {
	/** Die aktuell aktive Ansicht (Child Route) */
	view?: RouteNode<any, any>;
	/** Die aktuell aktive Gruppenprozess-Ansicht (Child Route) */
	gruppenprozesseView?: RouteNode<any, any>,
	/** Die Art der aktuell aktiven Ansicht (z.B. Default, Hinzufügen, Gruppenprozess) */
	activeViewType?: ViewType;
}

/**
 * Abstrakte Basisklasse für den Datenzugriff einer Route.
 *
 * Erweitert den {@link StateManager} um route-spezifische Hilfsmethoden für die Verwaltung von
 * Ansichten (`view`), Gruppenprozess-Ansichten und dem aktiven View-Typ ({@link ViewType}).
 * Konkrete Subklassen (`RouteData*`) erben diese Klasse und befüllen den State mit fachlichen Daten.
 *
 * @abstract
 * @typeParam RouteState - Der konkrete State-Typ der Route; muss {@link RouteStateInterface} erfüllen.
 * @example
 * ```ts
 * interface SchuelerState extends RouteStateInterface {
 *   schuelerListe: SchuelerListeEintrag[];
 *   ausgewaehlterSchueler: SchuelerListeEintrag | null;
 * }
 *
 * export class RouteDataSchueler extends RouteData<SchuelerState> {
 *   public constructor() {
 *     super({
 *       schuelerListe: [],
 *       ausgewaehlterSchueler: null,
 *     });
 *   }
 * }
 * ```
 */
export abstract class RouteData<RouteState extends RouteStateInterface> extends StateManager<RouteState> {

	/** Parameter zum automatischen Setzen des Fokus nach der Reiterauswahl */
	protected _autofocus = ref<boolean>(false);

	/**
	 * Erzeugt ein neues `RouteData`-Objekt mit dem übergebenen Default-State.
	 * Delegiert die Initialisierung an {@link StateManager}.
	 *
	 * @param defaultState - Der initiale Default-State der Route.
	 */
	protected constructor(defaultState: RouteState) {
		super(defaultState);
	}

	/**
	 * Aktualisiert den aktuellen State reaktiv mit dem angegebenen Patch, aber erhält die
	 * gewählte Ansicht/Child Route - selbst wenn der Patch eine alternative Route angibt.
	 *
	 * @param patch   der Patch, welcher auf den Default-State angewendet wird.
	 */
	protected setPatchedStateKeepView(patch: Partial<RouteState>) {
		this.setPatchedState({ ...patch, view: this._state.value.view });
	}

	/**
	 * Setter für die aktuelle Ansicht/Child Route. Das Setzen der Ansicht triggert die Reaktivität
	 * des States.
	 *
	 * @param value        die zu setzende Ansicht
	 * @param validViews   die Menge der gültigen Ansichten
	 */
	public setView(value: RouteNode<any, any>, validViews: RouteNode<any, any>[]) {
		if (validViews.includes(value)) {
			this.setPatchedState(<RouteState>{ view: value });
		} else {
			throw new DeveloperNotificationException("Die gewählte Ansicht wird nicht unterstützt.");
		}
	}

	/**
	 * Getter für die aktuelle Ansicht/Child Route.
	 */
	public get view(): RouteNode<any, any> {
		if (this._state.value.view === undefined) {
			throw new DeveloperNotificationException("Bei dieser Route wurde keine Ansicht im Default-State definiert.");
		}
		return this._state.value.view;
	}

	/**
	 * Getter für die Default-View
	 */
	public get defaultView(): RouteNode<any, any> {
		if (this._defaultState.view === undefined) {
			throw new DeveloperNotificationException("Bei dieser Route wurde keine Ansicht im Default-State definiert.");
		}
		return this._defaultState.view;
	}

	public get defaultGruppenprozesseView(): RouteNode<any, any> {
		if (this._defaultState.gruppenprozesseView === undefined) {
			throw new DeveloperNotificationException("Bei dieser Route wurde keine Gruppenprozess Ansicht im Default-State definiert.");
		}
		return this._defaultState.gruppenprozesseView;
	}

	/**
	 * Gibt die aktuelle Art der View zurück (Default, Hinzufügen oder Gruppenprozess).
	 * Ist keiner spezifiziert, so wird DEFAULT zurückgegeben.
	 */
	get activeViewType(): ViewType {
		if (this._state.value.activeViewType === undefined) {
			return ViewType.DEFAULT;
		}
		return this._state.value.activeViewType;
	}

	/**
	 * Setzt die aktuelle Art der View zurück (Default, Hinzufügen oder Gruppenprozess).
	 *
	 * @param value   die Art der View
	 */
	set activeViewType(value: ViewType) {
		this._state.value.activeViewType = value;
	}

	/**
	 * Getter für die Information, ob die aktuelle Ansicht einen Autofokus unterstützen soll.
	 */
	public get autofocus(): boolean {
		return this._autofocus.value;
	}

	/**
	 * Setter für die Information, ob die aktuelle Ansicht einen Autofokus unterstützen soll.
	 */
	public set autofocus(value: boolean) {
		this._autofocus.value = value;
	}

}
