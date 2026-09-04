import type { RouteComponent, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import type { RouteDataAuswahl, RouteStateAuswahlInterface } from "./RouteDataAuswahl";
import { RouteNode } from "./RouteNode";
import type { RouteTabProps } from "./RouteTabNode";
import { RouteTabNode } from "./RouteTabNode";
import { routeError } from "./error/RouteError";
import type { PendingStateManagerRegistry } from "~/router/PendingStateManagerRegistry";
import { configStateImpl } from "~/states/ConfigStateImpl";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { AuswahlManager } from "@ui/ui/manager/AuswahlManager";
import { ViewType } from "@ui/ui/nav/ViewType";
import { ConfigElement } from "@ui/utils/Config";

/**
 * Das Interface für die Properties in Bezug auf das Handling des Listenbereichs, welcher der Komponente,
 * die dieser Route zugeordnet ist, übergeben werden.
 */
export interface RouteAuswahlListProps<TAuswahlManager extends AuswahlManager<any, any, any>> {
	manager: () => TAuswahlManager;
	setFilter: () => Promise<void>;
	activeViewType: ViewType;
	gotoDefaultView: (id?: number | null) => Promise<void>;
	gotoHinzufuegenView: (navigate: boolean) => Promise<void>;
	gotoSchnelleingabeView: (navigate: boolean, id?: number | null) => Promise<void>;
	gotoGruppenprozessView: (navigate: boolean) => Promise<void>;
	pendingStateManagerRegistry: () => PendingStateManagerRegistry;
}


/**
 * Das Interface für die Properties in Bezug auf das Handling des Applikationsbereichs der Tabs, welche der Komponente,
 * die dieser Route zugeordnet sind, übergeben werden.
 */
export interface RouteAuswahlProps<TAuswahlManager extends AuswahlManager<number, TAuswahl, TDaten>, TAuswahl = any, TDaten = any> extends RouteTabProps {
	manager: () => TAuswahlManager;
	patch: (data: Partial<TDaten>) => Promise<boolean>;
	pendingStateManagerRegistry: () => PendingStateManagerRegistry;
}

/**
 * Diese abstrakte Klasse ist die Basisklasse aller Knoten für das Routing innerhalb des
 * SVWS-Clients, welche eine Navigation über einen Tab-Manager bieten und mithilfe eines Auswahllisten-Managers
 * zur Verfügung stellen.
 *
 * Abstrakte Basisklasse für Routing-Knoten mit kombinierter Auswahllisten- und Tab-Navigation.
 * Sie erweitert {@link RouteTabNode} um die Verwaltung eines {@link AuswahlManager}-basierten
 * Listenbereichs (linke Spalte),der mit dem Tab-gesteuerten Applikationsbereich (rechte Spalte) zusammenarbeitet.
 *
 * @abstract
 * @typeParam TAuswahlManager - konkreter {@link AuswahlManager}-Typ für die Auswahlliste
 * @typeParam TRouteData      - konkreter {@link RouteDataAuswahl}-Typ mit dem zugehörigen State
 * @typeParam TRouteParent    - Typ des übergeordneten Routing-Knotens
 * @typeParam TAuswahl        - Typ eines einzelnen Listeneintrags
 * @typeParam TDaten          - Typ der Detaildaten des gewählten Eintrags
 *
  */
export abstract class RouteAuswahlNode<TAuswahlManager extends AuswahlManager<number, TAuswahl, TDaten>, TRouteData extends RouteDataAuswahl<TAuswahlManager, RouteStateAuswahlInterface<TAuswahlManager>>, TRouteParent extends RouteNode<any, any>, TAuswahl = any, TDaten = any>
	extends RouteTabNode<TRouteData, TRouteParent> {

	/** Der Routing-Parameter für die ID */
	private _idParam = "id";

	/** Diese Methode kann ersetzt werden und ergänzt ggf. weitere Routing-Parameter für die Auswahlliste */
	private _getAuswahlListProps: (props: RouteAuswahlListProps<TAuswahlManager>) => Record<string, any> = (props) => props;

	/** Diese Methode kann ersetzt werden und ergänzt ggf. weitere Routing-Parameter für den Applikationsbereich */
	private _getAuswahlProps: (props: RouteAuswahlProps<TAuswahlManager>) => RouteTabProps = (props) => props;

	/** Diese Methode kann gesetzt werden, um die Update-Methode mit speziellen Aufrufen abzuschließen, wenn diese das Ziel ist */
	private _updateIfTarget: ((to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams,
		isEntering: boolean, redirected: RouteNode<any, any> | undefined) => Promise<void | Error | RouteLocationRaw>) | undefined = undefined;


	/**
	 * Erstellt einen neuen Knoten für das Routing, welcher die Navigation mit einem Tab-Manager unterstützt.
	 *
	 * @param schulformen   die Schulformen, welche für welche die Route erlaubt ist.
	 * @param kompetenzen   die Kompetenzen, die ein Benutzer für den Zugriff auf die Route benötigt
	 * @param name          der Name des Routing-Knotens (siehe RouteRecordRaw)
	 * @param path          der Pfad der Route (siehe RouteRecordRaw)
	 * @param component     die vue-Komponente für die Darstellung der Informationen der gewählten Route
	 * @param componentList die vue-Komponente für die Darstellung der Auswahlliste der gewählten Route
	 * @param data          die dem Knoten zugeordneten Daten
	 * @param idParam       der Routingparameter für die ID
	 */
	public constructor(
		schulformen: Iterable<Schulform>,
		kompetenzen: Iterable<BenutzerKompetenz>,
		name: string,
		path: string,
		component: RouteComponent,
		componentList: RouteComponent,
		data: TRouteData,
		idParam: string = "id") {

		super(schulformen, kompetenzen, name, path, component, data);
		this._idParam = idParam;
		super.setView("liste", componentList, (_route) => this._getAuswahlListProps({
			manager: () => this.data.manager,
			setFilter: this.data.setFilter,
			activeViewType: this.data.activeViewType,
			gotoDefaultView: this.data.gotoDefaultView,
			gotoHinzufuegenView: this.data.gotoHinzufuegenView,
			gotoSchnelleingabeView: this.data.gotoSchnelleingabeView,
			gotoGruppenprozessView: this.data.gotoGruppenprozessView,
			pendingStateManagerRegistry: () => this.data.pendingStateManagerRegistry,
		}));
		configStateImpl.nonPersistentConfig.addElements([
			new ConfigElement(`${this.name}.auswahl.id`, "user", ""),
		]);
	}

	public set updateIfTarget(value: (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams,
		isEntering: boolean, redirected: RouteNode<any, any> | undefined) => Promise<void | Error | RouteLocationRaw>) {
		this._updateIfTarget = value;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams,
		isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id: paramId } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", this._idParam]);
			if (idSchuljahresabschnitt === undefined) {
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
			}
			let id = paramId;
			if ((paramId === undefined) && isEntering) {
				const lastId = Number.parseInt(configStateImpl.nonPersistentConfig.getValue(`${this.name}.auswahl.id`));
				if (!Number.isNaN(lastId)) {
					id = lastId;
				}
			}
			if (isEntering && to.hasOneOfTypes([ViewType.GRUPPENPROZESSE, ViewType.HINZUFUEGEN, ViewType.NEU])) {
				return this.getRouteView(this.data.view, { id: id ?? '' });
			}
			// Daten zum ausgewählten Schuljahresabschnitt und Schüler laden
			const idNeu = await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt, isEntering);
			if ((idNeu !== null) && (idNeu !== id)) {
				return this.data.defaultView.getRoute({ id: idNeu });
			}

			// Wenn einer der folgenden Routen Types aufgerufen wird, wird hier ein Redirect initiiert, sobald eine ID in der URL enthalten ist.
			if (to.hasOneOfTypes([ViewType.GRUPPENPROZESSE, ViewType.HINZUFUEGEN]) && (id !== undefined)) {
				return this.getRouteView(to, { id: '' });
			}

			if (to.hasType(ViewType.GRUPPENPROZESSE)) {
				await this.data.gotoGruppenprozessView(false);
			} else if (to.hasType(ViewType.HINZUFUEGEN)) {
				await this.data.gotoHinzufuegenView(false);
			} else if (to.hasType(ViewType.NEU)) {
				await this.data.gotoSchnelleingabeView(false);
			} else {
				await this.data.gotoDefaultView(id);
			}

			if (to.name === this.name) {
				if (this._updateIfTarget !== undefined) {
					return await this._updateIfTarget(to, to_params, from, from_params, isEntering, redirected);
				}
				if (this.data.manager.hasDaten()) {
					return this.getRouteSelectedChild();
				}
				return;
			}
			if (!to.name.startsWith(this.data.view.name)) {
				for (const child of this.children) {
					if (to.name.startsWith(child.name)) {
						this.data.setView(child, this.children);
					}
				}
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		// Wenn eine Route mit ViewType != Default verlassen wird, soll bei der Rückkehr zu dieser Route kein Child Node mehr selektiert sein.
		// Es soll dann die Default View angezeigt werden.
		if (this.data.activeViewType !== ViewType.DEFAULT) {
			this._selectedChild.value = undefined;
		}

		this.data.reset();
		const { id } = RouteNode.getStringParams(from_params, [this._idParam]);
		await configStateImpl.nonPersistentConfig.setValue(`${this.name}.auswahl.id`, id ?? "");
	}

	/**
	 * Fügt die ID zu der Route hinzu. Diese Methode kann überschrieben werden, wenn neben
	 * der ID noch weitere Parameter benötigt werden.
	 *
	 * @returns die Routing-Parameter mit der ID.
	 */
	public addRouteParamsFromState(): RouteParamsRawGeneric {
		const params = {};
		if (!this.data.hasManager) {
			return params;
		}
		const id = this.data.manager.auswahlID();
		if (id !== null) {
			this.data.addID(params, id);
		}
		return params;
	}

	/**
	 * Mithilfe dieses Setters kann die Methode ersetzt werden, um weitere Properties für die Auswahllisten-Komponente
	 * zu ergänzen, welche dieser Route zugeordnet ist.
	 *
	 * @returns die zusätzlichen Properties für die Komponente
	 */
	public set getAuswahlListProps(value: (props: RouteAuswahlListProps<TAuswahlManager>) => Record<string, any>) {
		this._getAuswahlListProps = value;
	}

	/**
	 * Mithilfe dieses Setters kann die Methode ersetzt werden, um weitere Properties für die Komponente zu ergänzen,
	 * welche dieser Route zugeordnet ist.
	 *
	 * @returns die zusätzlichen Properties für die Komponente
	 */
	public set getAuswahlProps(value: (props: RouteAuswahlProps<TAuswahlManager>) => RouteTabProps) {
		this._getAuswahlProps = value;
	}

	/**
	 * Überschreibt die getProps-Methode für die TabNode
	 *
	 * @param props   die Properties der TabNode
	 *
	 * @returns die Properties ergänzt um zusätzliche Properties dieser Auswahllisten-Node
	 */
	public getProps(props: RouteTabProps): RouteTabProps {
		return this._getAuswahlProps({
			...props,
			manager: () => this.data.manager,
			patch: this.data.patch,
			pendingStateManagerRegistry: () => this.data.pendingStateManagerRegistry,
		});
	}

}
