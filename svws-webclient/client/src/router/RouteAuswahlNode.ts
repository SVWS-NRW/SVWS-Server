import { type BenutzerKompetenz, DeveloperNotificationException, type Schulform, type ServerMode } from "@core";
import type { RouteDataAuswahl, RouteStateAuswahlInterface } from "./RouteDataAuswahl";
import { RouteNode } from "./RouteNode";
import type { RouteTabProps } from "./RouteTabNode";
import { RouteTabNode } from "./RouteTabNode";
import type { RouteComponent, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import type { AbschnittAuswahlDaten, AuswahlManager } from "@ui";
import { ViewType } from "@ui";
import { api } from "./Api";
import { routeApp } from "./apps/RouteApp";
import { routeError } from "./error/RouteError";
import { ConfigElement } from "../../../ui/src/utils/Config";
import type { PendingStateManagerRegistry } from "~/router/PendingStateManagerRegistry";

/**
 * Das Interface für die Properties in Bezug auf das Handling des Listenbereichs, welcher der Komponente,
 * die dieser Route zugeordnet ist, übergeben werden.
 */
export interface RouteAuswahlListProps<TAuswahlManager extends AuswahlManager<any, any, any>> {
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	manager: () => TAuswahlManager;
	setFilter: () => Promise<void>;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
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
	patch: (data: Partial<TDaten>) => Promise<void>;
	pendingStateManagerRegistry: () => PendingStateManagerRegistry;
}


/**
 * Diese abstrakte Klasse ist die Basisklasse aller Knoten für das Routing innerhalb des
 * SVWS-Clients, welche eine Navigation über einen Tab-Manager bieten und mithilfe eines Auswahllisten-Managers
 * zur Verfügung stellen.
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
	public constructor(schulformen: Iterable<Schulform>, kompetenzen: Iterable<BenutzerKompetenz>, name: string, path: string, component: RouteComponent,
		componentList: RouteComponent, data: TRouteData, idParam: string = "id") {
		super(schulformen, kompetenzen, name, path, component, data);
		this._idParam = idParam;
		super.setView("liste", componentList, (_route) => this._getAuswahlListProps({
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(true),
			manager: () => this.data.manager,
			setFilter: this.data.setFilter,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			activeViewType: this.data.activeViewType,
			gotoDefaultView: this.data.gotoDefaultView,
			gotoHinzufuegenView: this.data.gotoHinzufuegenView,
			gotoSchnelleingabeView: this.data.gotoSchnelleingabeView,
			gotoGruppenprozessView: this.data.gotoGruppenprozessView,
			pendingStateManagerRegistry: () => this.data.pendingStateManagerRegistry,
		}));
		api.nonPersistentConfig.addElements([
			new ConfigElement(`${this.name}.auswahl.id`, "user", ""),
		]);
	}

	public set updateIfTarget(value: (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams,
		isEntering: boolean, redirected: RouteNode<any, any> | undefined) => Promise<void | Error | RouteLocationRaw>) {
		this._updateIfTarget = value;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams,
		isEntering: boolean, redirected: RouteNode<any, any> | undefined) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id: paramId } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", this._idParam]);
			if (idSchuljahresabschnitt === undefined)
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
			let id = paramId;
			if ((paramId === undefined) && isEntering) {
				const lastId = parseInt(api.nonPersistentConfig.getValue(`${this.name}.auswahl.id`));
				if (!isNaN(lastId))
					id = lastId;
			}
			if (isEntering && to.hasOneOfTypes([ViewType.GRUPPENPROZESSE, ViewType.HINZUFUEGEN, ViewType.NEU]))
				return this.getRouteView(this.data.view, { id: id ?? '' });
			// Daten zum ausgewählten Schuljahresabschnitt und Schüler laden
			const idNeu = await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt, isEntering);
			if ((idNeu !== null) && (idNeu !== id))
				return this.data.defaultView.getRoute({ id: idNeu });

			// Wenn einer der folgenden Routen Types aufgerufen wird, wird hier ein Redirect initiiert, sobald eine ID in der URL enthalten ist.
			if (to.hasOneOfTypes([ViewType.GRUPPENPROZESSE, ViewType.HINZUFUEGEN]) && (id !== undefined))
				return this.getRouteView(to, { id: '' })

			if (to.hasType(ViewType.GRUPPENPROZESSE))
				await this.data.gotoGruppenprozessView(false);
			else if (to.hasType(ViewType.HINZUFUEGEN))
				await this.data.gotoHinzufuegenView(false);
			else if (to.hasType(ViewType.NEU))
				await this.data.gotoSchnelleingabeView(false);
			else
				await this.data.gotoDefaultView(id);

			if (to.name === this.name) {
				if (this._updateIfTarget !== undefined)
					return await this._updateIfTarget(to, to_params, from, from_params, isEntering, redirected);
				if (this.data.manager.hasDaten())
					return this.getRouteSelectedChild();
				return;
			}
			if (!to.name.startsWith(this.data.view.name))
				for (const child of this.children)
					if (to.name.startsWith(child.name))
						this.data.setView(child, this.children);
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams) : Promise<void> {
		// Wenn eine Route mit ViewType != Default verlassen wird, soll bei der Rückkehr zu dieser Route kein Child Node mehr selektiert sein.
		// Es soll dann die Default View angezeigt werden.
		if (this.data.activeViewType !== ViewType.DEFAULT)
			this._selectedChild.value = undefined;

		this.data.reset();
		const { id } = RouteNode.getStringParams(from_params, [ this._idParam ]);
		await api.nonPersistentConfig.setValue(`${this.name}.auswahl.id`, id ?? "");
	}

	/**
	 * Fügt die ID zu der Route hinzu. Diese Methode kann überschrieben werden, wenn neben
	 * der ID noch weitere Parameter benötigt werden.
	 *
	 * @returns die Routing-Parameter mit der ID.
	 */
	public addRouteParamsFromState(): RouteParamsRawGeneric {
		const params = {};
		if (!this.data.hasManager)
			return params;
		const id = this.data.manager.auswahlID();
		if (id !== null)
			this.data.addID(params, id);
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
