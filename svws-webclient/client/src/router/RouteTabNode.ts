import { DeveloperNotificationException, type BenutzerKompetenz, type Schulform } from "@core";
import type { RouteData } from "./RouteData";
import { RouteNode } from "./RouteNode";
import type { RouteComponent } from "vue-router";
import type { TabData, TabManager, ViewType } from "@ui";
import { RouteManager } from "./RouteManager";


/**
 * Das Interface für die Properties in Bezug auf das Handling der Tabs, welche der Komponente,
 * die dieser Route zugeordnet ist, übergeben werden.
 */
export interface RouteTabProps {
	tabManager: () => TabManager;
	activeViewType: ViewType;
}


/**
 * Diese abstrakte Klasse ist die Basisklasse aller Knoten für das Routing innerhalb des
 * SVWS-Clients, welche eine Navigation über einen Tab-Manager bieten.
 */
export abstract class RouteTabNode<TRouteData extends RouteData<any>, TRouteParent extends RouteNode<any, any>> extends RouteNode<TRouteData, TRouteParent> {

	/**
	 * Erstellt einen neuen Knoten für das Routing, welcher die Navigation mit einem Tab-Manager unterstützt.
	 *
	 * @param schulformen   die Schulformen, welche für welche die Route erlaubt ist.
	 * @param kompetenzen   die Kompetenzen, die ein Benutzer für den Zugriff auf die Route benötigt
	 * @param name          der Name des Routing-Knotens (siehe RouteRecordRaw)
	 * @param path          der Pfad der Route (siehe RouteRecordRaw)
	 * @param component     die vue-Komponente für die Darstellung der Informationen der gewählten Route
	 * @param data          die dem Knoten zugeordneten Daten
	 */
	public constructor(schulformen: Iterable<Schulform>, kompetenzen: Iterable<BenutzerKompetenz>, name: string, path: string, component?: RouteComponent, data?: TRouteData) {
		super(schulformen, kompetenzen, name, path, component, data);
		super.propHandler = (route) => this.getProps({
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab, this.data.activeViewType),
			activeViewType: this.data.activeViewType,
		});
	}

	/**
	 * Diese Methode muss überschrieben werden, um die Properties für die Komponente zu bestimmen, welche
	 * dieser Route zugeordnet ist.
	 *
	 * @returns die Properties für die Komponente
	 */
	public abstract getProps(props: RouteTabProps): Record<string, any>;

	/**
	 * Callback-Methode, welche vom Tab-Manager aufgerufen wird, um das Routing zu dem ausgewählten Tab-
	 * bzw. Menu-Eintrag auszuführen.
	 */
	private setTab = async (value: TabData) => {
		this.data.autofocus = true;
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute(this.getRouteView(node));
		this.data.setView(node, this.children);
		this.data.autofocus = false;
	}

}