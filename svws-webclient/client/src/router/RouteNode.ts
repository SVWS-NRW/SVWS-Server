import type { ComputedRef, Ref } from "vue";
import { computed, defineComponent, ref } from "vue";
import type { RouteComponent, RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric, RouteRecordName, RouteRecordRaw } from "vue-router";

import type { Schulform} from "@core";
import { ServerMode, BenutzerKompetenz, DeveloperNotificationException } from "@core";

import type { TabData } from "@ui";
import { TabManager, Checkpoint, ViewType } from "@ui";

import { api } from "~/router/Api";
import { routerManager } from "./RouteManager";
import type { RouteData } from "./RouteData";

/**
 * Diese abstrakte Klasse ist die Basisklasse aller Knoten für
 * das Routing innerhalb des SVWS-Clients.
 */
export abstract class RouteNode<TRouteData extends RouteData<any>, TRouteParent extends RouteNode<any, any>> {

	/** Eine Map mit allen Knoten */
	protected static mapNodesByName: Map<string, RouteNode<any, any>> = new Map();

	/** Das vue-Router-Objekt (siehe RouteRecordRaw) */
	protected _record: RouteRecordRaw;

	/** Ein Set mit den Schulformen, für welche eine Route erlaubt ist oder nicht */
	protected _schulformenErlaubt: Set<Schulform> = new Set();

	/** Ein Set mit den Kompetenzen die ein angemeldeter Benutzer für die Route benötigt */
	protected _kompetenzenBenoetigt: Set<BenutzerKompetenz> = new Set();

	/**
	 * Eine Funktion zum Prüfen, ob der Knoten, d.h. die Route, versteckt sein soll oder nicht
	 *
	 * Wird eine Route als hidden erkannt, wird eine Umleitungsroute als Return-Statement zurückgegeben.
	 * Auf diese Route wird umgeleitet, wenn diese Route vorher aktiv war, ggf. mit anderen Daten.
	 * Diese Methode wird auch zur Erstellung der hiddenChildren verwendet, das dann tatsächlich ein Array
	 * mit Boolean-Werten zurückgibt.
	 * */
	protected isHidden: ((params?: RouteParams) => RouteLocationRaw | false) | undefined = undefined;

	/** Der Elter-Knoten, sofern es sich um einen Kind-Knoten handelt. */
	protected _parent?: TRouteParent;

	/** Die Kind-Knoten zu dieser Route */
	protected _children: RouteNode<any, any>[];

	/** Die Knoten, welche in einem Menu zu dieser Komponente zur Verfügung gestellt werden */
	protected _menu: RouteNode<any, any>[];

	/** Gibt an, ob dem Knoten Daten zugeordnet sind (siehe auch _data) */
	protected _hasData: boolean;

	/** Die Daten, die dem Knoten zugeordnet sind */
	protected _data: TRouteData;

	/** Der ausgewählte Kind-Knoten, zu welchem geroutet werden soll (z.B. bei Tabs nach einer Auswahl) */
	protected _selectedChild: Ref<RouteNode<any, any> | undefined> = ref(undefined);

	/** Der Kind-Knoten, welcher als Default ausgewählt werden soll */
	protected _defaultChild: RouteNode<any, any> | undefined = undefined;

	/** Gibt den bzw. die Typen der Route an */
	protected _types: Set<ViewType>;

	/** Der Modus, in welchem die Route zulässig ist oder nicht. */
	private _mode: ServerMode = ServerMode.DEV;

	/** Checkpoint der beim Verlassen dieser Route, für eine Checkpoint Aktion genutzt werden kann. */
	private _checkpoint: Checkpoint | undefined = undefined;

	/**
	 * Erstellt einen neuen Knoten für das Routing mithilfe einer
	 * einfachen Default-Router-View.
	 * Es können mithilfe der Methode "setView" noch weitere Router-Views
	 * ergänzt werden
	 *
	 * @param schulformen   die Schulformen, welche für welche die Route erlaubt ist.
	 * @param kompetenzen   die Kompetenzen, die ein Benutzer für den Zugriff auf die Route benötigt
	 * @param name          der Name des Routing-Knotens (siehe RouteRecordRaw)
	 * @param path          der Pfad der Route (siehe RouteRecordRaw)
	 * @param component     die vue-Komponente für die Darstellung der Informationen der gewählten Route
	 * @param data          die dem Knoten zugeordneten Daten
	 */
	public constructor(schulformen: Iterable<Schulform>, kompetenzen: Iterable<BenutzerKompetenz>, name: string, path: string, component?: RouteComponent, data?: TRouteData) {
		RouteNode.mapNodesByName.set(name, this);
		this._record = {
			name: name,
			path: path,
			components: { default: component === undefined ? defineComponent({}) : component },
			props: { default: (to) => this.getNoProps(to) },
			children: undefined,
			meta: {
				text: name, // Ein Text, welcher zur Darstellung in der GUI genutzt wird (z.B. der Text auf Tabs)
				menugroup: "", // Die Menu-Gruppe, falls die Route einem Menüeintrag zugeordnet ist und dort einer Gruppe
				icon: "", // Der Bezeichner eines Icons, falls die Route einem Menüeintrag zugeordnet ist
			},
		};
		this._children = [];
		this._menu = [];
		this._hasData = (data !== undefined);
		this._data = (data !== undefined) ? data : {} as TRouteData;
		this._types = new Set([ ViewType.DEFAULT ]);
		// Setze die erlaubten Schulformen
		for (const sf of schulformen)
			this._schulformenErlaubt.add(sf);
		for (const k of kompetenzen)
			this._kompetenzenBenoetigt.add(k);
	}


	/**
	 * Gibt das vue-router-Object (siehe RouteRecordRaw)
	 * für diesen Knoten zurück.
	 *
	 * @returns das Objekt
	 */
	public get record() : RouteRecordRaw {
		return this._record;
	}

	/**
	 * Gibt den Namen der Route zurück.
	 */
	public get name() : string {
		if (this._record.name === undefined)
			throw Error("Die Route hat keinen gültigen Namen");
		return this._record.name.toString();
	}

	/**
	 * Ergänzt beim Bestimmen der Route-Parameter die Route-Parameter für diesen Knoten, sofern
	 * welche gesetzt werden sollen. Für diesen Fall muss diese Methode übeschrieben werden.
	 *
	 * @returns die zu ergänzenden Routen-Parameter
	 */
	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return {};
	}

	/**
	 * Bestimmt die Routen-Parameter für diese Route und kombiniert diese mit den Routen-Parametern
	 * der Parent-Nodes.
	 *
	 * @param params Routen-Parameter, die zum Abschluss gesetzt werden können, um die aus dem State
	 *               gewonnenen Parameter ersetzen zu können.
	 *
	 * @returns die Routen-Parameter für diese Route
	 */
	public getRouteParams(params? : RouteParamsRawGeneric) : RouteParamsRawGeneric {
		let result = {};
		let cur : RouteNode<any, any> | undefined = this;
		while (cur !== undefined) {
			result = Object.assign(result, cur.addRouteParamsFromState());
			cur = cur.parent;
		}
		if (params !== undefined)
			result = Object.assign(result, params);
		return result;
	}

	/**
	 * Gibt die Route für diesen Knoten zurück. Dabei werden die Routen-Parameter
	 * mithilfe der Methode getRouteParams von diesem Knoten gesetzt.
	 *
	 * @param params  Routen-Parameter, die ggf. ermittelte Routen-Parameter ersetzen können.
	 *
	 * @returns die Route
	 */
	public getRoute(params? : RouteParamsRawGeneric): RouteLocationRaw {
		return { name: this.name, params: this.getRouteParams(params) };
	}

	/**
	 * Gibt die Route für den Einstieg in die View zurück. Dabei werden die Routen-Parameter
	 * mithilfe der Methode getRouteParams von diesem Knoten gesetzt.
	 *
	 * @param view     die View für die die Route bestimmt wird
	 * @param params   Routen-Parameter, die ggf. ermittelte Routen-Parameter ersetzen können.
	 *
	 * @returns die Route
	 */
	public getRouteView(view: RouteNode<any, any>, params? : RouteParamsRawGeneric): RouteLocationRaw {
		return { name: view.name, params: this.getRouteParams(params) };
	}

	/**
	 * Gibt die Route für den Einstieg in die Selected-Child-Route zurück. Dabei werden die Routen-Parameter
	 * mithilfe der Methode getRouteParams von diesem Knoten gesetzt. Liegt keine Auswahl vor, so wird die
	 * Default-Child-Route gewählt.
	 *
	 * @param params  Routen-Parameter, die ggf. ermittelte Routen-Parameter ersetzen können.
	 *
	 * @returns die Route
	 */
	public getRouteSelectedChild(params? : RouteParamsRawGeneric): RouteLocationRaw {
		const name: string = (this.selectedChild === undefined) ? this.defaultChild!.name : this.selectedChild.name;
		return { name: name, params: this.getRouteParams(params) };
	}

	/**
	 * Gibt die Route für den Einstieg in die Default-Child-Route zurück. Dabei werden die Routen-Parameter
	 * mithilfe der Methode getRouteParams von diesem Knoten gesetzt.
	 *
	 * @param params  Routen-Parameter, die ggf. ermittelte Routen-Parameter ersetzen können.
	 *
	 * @returns die Route
	 */
	public getRouteDefaultChild(params? : RouteParamsRawGeneric): RouteLocationRaw {
		return { name: this.defaultChild!.name, params: this.getRouteParams(params) };
	}

	/**
	 * Gibt den Text der Route zurück, welcher für die Visualisierung genutzt wird (z.B. bei Tabs).
	 */
	public get text() : string {
		return (this._record.meta as { text: string }).text;
	}

	/**
	 * Setzt den Text der Route, welcher für die Visualisierung genutzt wird (z.B. bei Tabs).
	 */
	public set text(text : string) {
		(this._record.meta as { text: string }).text = text;
	}

	/**
	 * Gibt die Menu-Gruppe der Route zurück, welche für die Gruppierung bei Link-Listen verwendet wird
	 */
	public get menugroup() : string {
		return (this._record.meta as { menugroup: string }).menugroup;
	}

	/**
	 * Setzt die Menu-Gruppe der Route, welche für die Gruppierung bei Link-Listen verwendet wird
	 */
	public set menugroup(menugroup : string) {
		(this._record.meta as { menugroup: string }).menugroup = menugroup;
	}

	/**
	 * Gibt die Menu-Gruppe der Route zurück, welche für die Gruppierung bei Link-Listen verwendet wird
	 */
	public get icon() : string {
		return (this._record.meta as { icon: string }).icon;
	}

	/**
	 * Setzt die Menu-Gruppe der Route, welche für die Gruppierung bei Link-Listen verwendet wird
	 */
	public set icon(icon : string) {
		(this._record.meta as { icon: string }).icon = icon;
	}

	/**
	 * Gibt die Daten zurück, die diesem Knoten zugeordnet sind.
	 */
	public get data() : TRouteData {
		return this._data;
	}

	/**
	 * Setzt die Daten, die diesem Knoten zugeordnet sind
	 */
	public set data(data : TRouteData ) {
		this._data = data;
	}

	/**
	 * Gibt den Server-Modus zurück, bei welchem diese Route dargestellt wird.
	 */
	public get mode() : ServerMode {
		return this._mode;
	}

	/**
	 * Setzt der Server-Modus, bei welchem diese Route angezeigt wird.
	 */
	protected set mode(mode : ServerMode) {
		this._mode = mode;
	}

	/**
	 * Gibt an, ob ein Checkpoint hinterlegt ist und dieser gerade aktiv ist.
	 */
	public isCheckpointActive() : boolean {
		return (this._checkpoint !== undefined) && this._checkpoint.active;
	}

	/**
	 * Gibt den hinterlegten Checkpoint zurück.
	 */
	public get checkpoint() : Checkpoint {
		if (this._checkpoint === undefined)
			throw new DeveloperNotificationException("Es wurde kein Checkpoint gesetzt.");
		return this._checkpoint;
	}

	/**
	 * Wenn <code>true</code> übergeben wird, wird ein Checkpoint für diese RouteNode erzeugt, ansonsten wird der Checkpoint entfernt.
	 */
	public set setCheckpoint(checkpoint: boolean) {
		this._checkpoint = checkpoint ? new Checkpoint() : undefined;
	}

	/**
	 * Gibt die Routen-Typen zurück, welche der Route zugeordnet sind.
	 */
	public get types() : Set<ViewType> {
		return this._types;
	}

	/**
	 * Setzt die Routen-Typen, welche der Route zugeordnet sind.
	 */
	protected set types(types : Set<ViewType>) {
		this._types = types;
	}

	/**
	 * Die Methode prüft ob diese RouteNode mindestens eine der übergebenen ViewTypes als Typ hinterlegt hat.
	 *
	 * @param viewTypes Array der zu prüfenden ViewTypes
	 *
	 * @return <code>true</code> wenn mindestens ein übergebener ViewType als Typ für diese RouteNode hinterlegt ist, ansonsten <code>false</code>.
	 */
	public hasOneOfTypes(viewTypes: ViewType[]): boolean {
		return !this._types.isDisjointFrom(new Set(viewTypes));
	}

	/**
	 * Die Methode prüft ob diese RouteNode den übergebenen ViewType als Typ hinterlegt hat.
	 *
	 * @param viewType der zu prüfende ViewType
	 *
	 * @return <code>true</code> wenn der übergebene ViewType als Typ für diese RouteNode hinterlegt ist, ansonsten <code>false</code>.
	 */
	public hasType(viewType: ViewType): boolean {
		return this._types.has(viewType);
	}

	/**
	 * Setzt die Kind-Knoten für diesen Knoten in Bezug auf das
	 * "Nested"-Routing.
	 *
	 * @param nodes   Ein Array mit den Kindern für das Routing
	 */
	public set children(nodes: RouteNode<any, any>[]) {
		this._children.forEach(c => c.parent = undefined);
		this._children = nodes;
		this._children.forEach(c => c.parent = this);
		this._record.children = nodes.length === 0 ? undefined : nodes.map(n => n.record);
	}

	/**
	 * Gibt die Kind-Knoten für diesen Knoten in Bezug auf das
	 * "Nested"-Routing zurück.
	 *
	 * @returns ein Array mit den Kind-Knoten
	 */
	public get children() : RouteNode<any, any>[] {
		const result : RouteNode<any, any>[] = [];
		for (const node of this._children) {
			if (api.authenticated && (!node.mode.checkServerMode(api.mode) || !node.hatSchulform() || !node.hatEineKompetenz()))
				continue;
			result.push(node);
		}
		return result;
	}

	/**
	 * Setzt die Knoten, welche bei der Auswahl dieser Route für ein
	 * Menu zur Verfügung stehen.
	 *
	 * @param nodes   Ein Array mit den Knoten
	 */
	public set menu(nodes: RouteNode<any, any>[]) {
		this._menu = nodes;
	}

	/**
	 * Gibt die Knoten zurück, welche bei der Auswahl dieser Route für ein
	 * Menu zur Verfügung stehen.
	 *
	 * @returns ein Array mit den Knoten
	 */
	public get menu() : RouteNode<any, any>[] {
		const result: RouteNode<any, any>[] = [];
		for (const node of this._menu) {
			if (api.authenticated && (!node.mode.checkServerMode(api.mode) || !node.hatSchulform() || !node.hatEineKompetenz()))
				continue;
			result.push(node);
		}
		return result;
	}

	/**
	 * Gibt den Elter-Knoten zurück, sofern einer gesetzt wurde
	 *
	 * @return der Elter-Knoten oder undefined
	 */
	public get parent(): TRouteParent | undefined {
		return this._parent;
	}

	/**
	 * Setzt oder entfernt den Elter-Knoten
	 *
	 * @param value der Elter-Knoten oder undefined
	 */
	protected set parent(value: TRouteParent | undefined) {
		this._parent = value;
	}

	/**
	 * Gibt die vue-route-Record-Objekte der Kind-Knoten für diesen Knoten in
	 * Bezug auf das  "Nested"-Routing zurück.
	 *
	 * @returns ein Array mit den vue-route-Record-Objekten
	 */
	public get children_records() : RouteRecordRaw[] {
		const result : RouteRecordRaw[] = [];
		for (const node of this.children)
			result.push(node.record);
		return result;
	}

	/**
	 * Gibt ein passend zu dem getter children ein Array zurück,
	 * welches angibt, ob die einzelnen Kind-Knoten versteckt sind oder
	 * nicht.
	 *
	 * @returns ein Array mit der
	 */
	public children_hidden() : ComputedRef<boolean[]> {
		return computed(() => this.children.map(c => c.hidden(routerManager.getRouteParams()) !== false));
	}

	/**
	 * TODO
	 */
	public get selectedChild() : RouteNode<any, any> | undefined {
		return this._selectedChild.value;
	}

	/**
	 * TODO
	 */
	public set selectedChildRecord(record : RouteRecordRaw | undefined) {
		this._selectedChild.value = ((record === undefined) || (record.name === undefined))
			? undefined : this._selectedChild.value = RouteNode.mapNodesByName.get(record.name.toString());
	}

	/**
	 * TODO
	 */
	public get selectedChildRecord() : RouteRecordRaw | undefined {
		return this._selectedChild.value?.record;
	}

	/**
	 * TODO
	 */
	public set defaultChild(node : RouteNode<any, any> | undefined) {
		this._defaultChild = node;
	}

	/**
	 * TODO
	 */
	public get defaultChild() : RouteNode<any, any> | undefined {
		return this._defaultChild;
	}

	/**
	 * Gibt ein passend zu dem getter menu ein Array zurück,
	 * welches angibt, ob die einzelnen Kind-Knoten versteckt sind oder
	 * nicht.
	 *
	 * @returns ein Array mit der
	 */
	public menu_hidden() : ComputedRef<boolean[]> {
		return computed(() => this.menu.map(c => c.hidden(routerManager.getRouteParams()) !== false));
	}

	/**
	 * Erstellt anhand der Children einen neuen Tab-Manager mit dem angegebenen Tab
	 * vorausgewählte und der angegebenen Callback-Methode bei einer Tab-Auswahl
	 *
	 * @param nodes         die Kind- oder Menu-Knoten
	 * @param nodesHidden   die Information zu den Knoten, ob diese versteckt sind oder nicht
	 * @param tabname       der Name des ausgewählten Tabs
	 * @param setTab        die Callback-Methode
	 */
	public createTabManager(nodes: RouteNode<any, any>[], nodesHidden: boolean[], tabname : string, setTab: (value: TabData) => Promise<void>, type : ViewType = ViewType.DEFAULT) {
		const tabs: TabData[] = [];
		let tab = null;
		for (const node of nodes) {
			if (!node.types.has(type))
				continue;
			if (!(node.hatEineKompetenz() && node.hatSchulform()))
				continue;
			const newTab = <TabData>{ name: node.name, text: node.text };
			if (!this.checkTabVisibility(newTab))
				continue;
			newTab.tabgroup = node.menugroup;
			newTab.image = node.icon;
			tabs.push(newTab);
			if (node.name === tabname)
				tab = newTab;
		}
		if (tab === null)
			tab = tabs[0];
		return new TabManager(tabs, tab, setTab, nodesHidden);
	}

	/**
	 * Erstellt anhand der Children einen neuen Tab-Manager mit dem angegebenen Tab
	 * vorausgewählte und der angegebenen Callback-Methode bei einer Tab-Auswahl
	 *
	 * @param tabname   der Name des ausgewählten Tabs
	 * @param setTab    die Callback-Methode
	 */
	public createTabManagerByChildren(tabname : string, setTab: (value: TabData) => Promise<void>, type : ViewType = ViewType.DEFAULT) {
		return this.createTabManager(this.children, this.children_hidden().value, tabname, setTab, type);
	}

	/**
	 * Erstellt anhand der Menu-Einträge einen neuen Tab-Manager mit dem angegebenen Tab
	 * vorausgewählte und der angegebenen Callback-Methode bei einer Tab-Auswahl
	 *
	 * @param tabname   der Name des ausgewählten Tabs
	 * @param setTab    die Callback-Methode
	 */
	public createTabManagerByMenu(tabname : string, setTab: (value: TabData) => Promise<void>, type : ViewType = ViewType.DEFAULT) {
		return this.createTabManager(this.menu, this.menu_hidden().value, tabname, setTab, type);
	}

	/**
	 * Prüft, ob das übergeben Tab sichtbar ist oder nicht. Diese Methode kann von Route-Nodes
	 * überschrieben werden, um bedingte Sichtbarkeit der Child-Routes zu steuern.
	 *
	 * @param tab   der zu prüfende Tab
	 *
	 * @returns true, falls der Tab sichtbar ist, und ansonsten false
	 */
	protected checkTabVisibility(tab: TabData) {
		return true;
	}

	/**
	 * Prüft, ob die Schulform des angemeldeten Benutzers für die Route
	 * erlaubt ist oder nicht.
	 *
	 * @param schulform   die zu prüfende Schulform
	 *
	 * @returns true, falls die Schulform erlaubt ist und ansonsten false
	 */
	public hatSchulform(): boolean {
		return api.authenticated && this._schulformenErlaubt.has(api.schulform);
	}

	/**
	 * Prüft, ob ein angemeldeter Benutzer eine der Kompetenz benötigten Komptenzen hat,
	 * die den Zugriff auf diese Route erlauben.
	 *
	 * @returns true, falls der Benutzer eine benötigte Kompetenz hat und ansonsten false
	 */
	public hatEineKompetenz(): boolean {
		if (!api.authenticated)
			return false;
		if (this._kompetenzenBenoetigt.has(BenutzerKompetenz.KEINE))
			return true;
		if (api.benutzerIstAdmin)
			return true;
		return api.benutzerHatEineKompetenz(this._kompetenzenBenoetigt);
	}

	/**
	 * Setzt der Property-Handler für die Default-View
	 */
	public set propHandler(handler: (to: RouteLocationNormalized) => Record<string, any>) {
		(this._record.props as Record<string, (to: RouteLocationNormalized) => Record<string, any>>).default = handler;
	}

	/**
	 * Gibt zurück, ob der Knoten für das Routing versteckt ist oder nicht.
	 *
	 * @returns {boolean} true, falls der Knoten versteckt werden soll und für das Routing nicht zur Verfügung steht.
	 */
	public hidden(params?: RouteParams): RouteLocationRaw | false {
		// Prüfen, ob die aktuelle Schulform und die Kompetenzen des angemdelteten Benutzers die Route erlaubt oder nicht
		if (api.authenticated && (this.name !== "init")) {
			if ((!this.hatSchulform()) || (!this.hatEineKompetenz()))
				return this.parent?.getRoute() ?? false;
		}
		// Prüfen, ob die Komponente dargestellt werden darf oder nicht
		return (this.isHidden === undefined) ? false : this.isHidden(params);
	}

	/**
	 * Prüft, ob die Route aktuell ausgewählt ist oder Parent einer anderen Route
	 *
	 * @returns {boolean} true, wenn die Route den vorgegebenen Namen hat.
	 */
	public isSelected(name: RouteRecordName | null | undefined): boolean {
		const node = RouteNode.getNodeByName(this.name);
		if ((node === undefined) || (name === null) || (name === ""))
			return false;
		if (node.name === name)
			return true;
		else if (this.parent !== undefined)
			return this.parent.isSelected(name);
		else
			return false;
	}

	/**
	 * Setzt die Informationen zu der (weiteren?) Router-View mit dem Namen "name". Ist eine
	 * View mit dem Namen bereits definiert, so wird diese ersetzt.
	 *
	 * @param name   der name der zweiten Router-View
	 * @param component   die vue-Komponente für die Darstellung
	 * @param prop_handler   der Property-Handler zum Erstellen der Properties für die Komponente
	 */
	protected setView(name: string, component: RouteComponent, prop_handler: (to: RouteLocationNormalized) => Record<string, any>) {
		if ((this._record.components === undefined) || (this._record.props === undefined))
			throw new DeveloperNotificationException("Unerwarteter Fehler in der Methode RouteNode::addView. components oder props ist undefined.");
		(this._record.components as Record<string, RouteComponent>)[name] = component;
		(this._record.props as Record<string, (to: RouteLocationNormalized) => Record<string, any>>)[name] = prop_handler;
	}


	/**
	 * Prüft, ob eine Router-View mit dem Namen "name" vorhanden ist.
	 *
	 * @param name   der Name der zu prüfenden View
	 *
	 * @returns true, wenn die View vorhanden ist, und ansonsten false
	 */
	public hasView(name : string): boolean {
		const comp : Record<string, RouteComponent | undefined> | null | undefined = this._record.components;
		if ((comp === undefined) || (comp === null))
			return false;
		return (comp[name] !== undefined);
	}


	/**
	 * Ein Default-Handler zur Bestimmung der Properties einer Route.
	 * Liefert ein leeres Objekt zurück.
	 *
	 * @param to   die Informationen zur Route
	 *
	 * @returns das Properties-Objekt
	 */
	protected getNoProps(to: RouteLocationNormalized): Record<string, any> {
		return {};
	}

	/**
	 * Hook die ausgeführt wird, nach dem ein aktiver Checkpoint betreten wurde.
	 *
	 * @param destination Ziel Route zu der ursprünglich navigiert werden sollte.
	 */
	public async doCheckpoint(destination: RouteLocationRaw): Promise<void> {
		return this._checkpoint?.doCheckpoint(destination);
	}

	/**
	 * TODO see RouterManager - global hook
	 *
	 * @param to    die Ziel-Route
	 * @param to_params die Parameter der Ziel-Route
	 * @param from   die Quell-Route
	 * @param from_params die Parameter der Quell-Route
	 */
	protected async beforeEach(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		return true;
	}

	/**
	 * TODO see RouterManager - global hook
	 *
	 * @param to    die Ziel-Route
	 * @param to_params die Parameter der Ziel-Route
	 * @param from   die Quell-Route
	 * @param from_params die Parameter der Quell-Route
	 */
	public async doBeforeEach(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		try {
			return await this.beforeEach(to, to_params, from, from_params);
		} catch (e) {
			routerManager.errorcode = undefined;
			routerManager.error = e instanceof Error ? e : new DeveloperNotificationException("Fehler beim Routing in doBeforeEach(" + to.name + ", " + from?.name + ")");
			return { name: "error" };
		}
	}

	/**
	 * Prüft, ob dieser Knoten ein Nachfolger-Knoten des Kontens mit dem angegebenen
	 * Knoten ist.
	 *
	 * @param other   der andere Knoten (bzw. der Name), der evtl. ein Vorgänger dieses Knotens ist
	 *
	 * @returns die Folge der Knoten von dem anderen Knoten bis zu diesem Knoten (einschließlich),
	 *   falls dieser Knoten ein Nachfolger-Knoten ist und ansonsten false
	 */
	public checkSuccessorOf(other: string | RouteNode<any, any>): RouteNode<any, any>[] | false {
		const other_node = typeof other === "string" ? RouteNode.getNodeByName(other) : other;
		if ((other_node === undefined) || (this.parent === undefined))
			return false;
		const result: RouteNode<any, any>[] = [ this ];
		let current: RouteNode<any, any> | undefined = this.parent;
		do {
			result.push(current);
			if (current.name === other_node.name)
				return result.reverse();
			current = current.parent;
		} while (current !== undefined);
		return false;
	}

	/**
	 * Prüft, ob dieser Knoten ein Vorgänger-Knoten des Kontens mit dem angegebenen
	 * Knoten ist.
	 *
	 * @param other   der andere Knoten (bzw. der Name), der evtl. ein Nachfolger dieses Knotens ist
	 *
	 * @returns die Folge der Knoten von diesem Knoten bis zu dem anderen Knoten (einschließlich),
	 *   falls dieser Knoten ein Vorgänger-Knoten ist und ansonsten false
	 */
	public checkPredecessorOf(other: string | RouteNode<any, any>): RouteNode<any, any>[] | false {
		const other_node = typeof other === "string" ? RouteNode.getNodeByName(other) : other;
		if ((other_node === undefined) || (other_node.parent === undefined))
			return false;
		const result: RouteNode<any, any>[] = [ this ];
		let current: RouteNode<any, any> | undefined = other_node.parent;
		while (current !== undefined) {
			result.push(current);
			if (current.name === this.name)
				return result.reverse();
			current = current.parent;
		}
		return false;
	}

	/**
	 * Bestimmt die Vorgänger dieses Knotens und gibt diese als Array
	 * zurück. Dabei ist das Array so sortiert, dass das letzte Element im
	 * Array der direkte Vorgänger dieses Knotens ist. Existiert
	 * kein Vorgänger, so ist das Array leer.
	 *
	 * @returns die Vorgänger dieses Knotens als Array
	 */
	public getPredecessors(): RouteNode<any, any>[] {
		const result: RouteNode<any, any>[] = [ ];
		let current: RouteNode<any, any> | undefined = this.parent;
		while (current !== undefined) {
			result.push(current);
			current = current.parent;
		}
		return result.reverse();
	}

	/**
	 * Ein Ereignis, welches im globalen beforeEach-Guard aufgerufen wird,
	 * wenn die Informationen einer Route aktualisiert werden sollen.
	 * Dieses Ereignis wird unabhängig davon aufgerufen, ob die Route das erste
	 * mal betreten wird oder einfach nur angepasst wird
	 *
	 * @param to            die neue Route
	 * @param to_params     die Routen-Parameter
	 * @param from          die alte Route
	 * @param from_params   die Routen-Parameter der alten Route
	 * @param isEntering    gibt an, ob die Route das erste mal betreten wird (true) oder aufgrund von Parameter-Änderungen nur aktualisiert wird (false)
	 * @param redirected    gibt den Knoten an, von dem umgeleitet wurde, falls im Routing-Prozess bereits ein redirect stattgefunden hat
	 *
	 * @returns ggf. die Route für ein redirect oder ein Fehler
	 */
	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined) : Promise<void | Error | RouteLocationRaw> {
	}

	/**
	 * Prüft, ob die Route wirklich neu betreten wurde oder vor einem redirect schon betreten wurde.
	 *
	 * @param isEntering   die Information vom Route-Manager, ob die Route neu betreten wurde
	 * @param redirected   ggf. die Information zu einem redirect, welches ausgeführt wurde
	 *
	 * @returns true, falls die Route wirklich neu betreten wurde und dies nicht zuvor vor einem redirect erfolgt ist.
	 */
	private isReallyEntering(isEntering : boolean, redirected : RouteNode<any, any> | undefined) : boolean {
		return isEntering && ((redirected === undefined) || (!redirected.name.startsWith(this.name)));
	}

	/**
	 * Ein Ereignis, welches im globalen beforeEach-Guard aufgerufen wird,
	 * wenn die Informationen einer Route aktualisiert werden sollen.
	 * Dieses Ereignis wird unabhängig davon aufgerufen, ob die Route das erste
	 * mal betreten wird oder einfach nur angepasst wird
	 *
	 * @param to            die neue Route
	 * @param to_params     die Routen-Parameter
	 * @param from          die alte Route
	 * @param from_params   die Routen-Parameter der alten Route
	 * @param isEntering    gibt an, ob die Route das erste mal betreten wird (true) oder aufgrund von Parameter-Änderungen nur aktualisiert wird (false)
	 * @param redirected    gibt den Knoten an, von dem umgeleitet wurde, falls im Routing-Prozess bereits ein redirect stattgefunden hat
	 */
	public async doUpdate(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined) : Promise<void | Error | RouteLocationRaw> {
		try {
			// Prüfe mithilfe der hidden-Methode, ob die Route sichtbar ist
			const tmpHidden = this.hidden(to_params);
			if (tmpHidden !== false)
				return this.parent === undefined ? this.getRoute() : tmpHidden;
			if (this._parent !== undefined)
				this._parent._selectedChild.value = this;
			return await this.update(to, to_params, from, from_params, this.isReallyEntering(isEntering, redirected), redirected);
		} catch (e) {
			routerManager.errorcode = undefined;
			routerManager.error = e instanceof Error ? e : new DeveloperNotificationException("Fehler beim Routing in doUpdate(" + to.name + ")");
			return { name: "error" };
		}
	}

	/**
	 * Ein Ereignis, welches im globalen beforeEach-Guard aufgerufen wird,
	 * bevor eine Route verlassen wird.
	 *
	 * @param from          die Route, die verlassen wird
	 * @param from_params   die Routen-Parameter dazu
	 * @param to            die Route, wohin verlassen wird
	 * @param to_params     die Routen-Parameter dazu
	 */
	protected async leaveBefore(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	/**
	 * Ein Ereignis, welches im globalen beforeEach-Guard aufgerufen wird,
	 * bevor eine Route verlassen wird.
	 *
	 * @param from          die Route, die verlassen wird
	 * @param from_params   die Routen-Parameter dazu
	 * @param to            die Route, wohin verlassen wird
	 * @param to_params     die Routen-Parameter dazu
	 */
	public async doLeaveBefore(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			return await this.leaveBefore(from, from_params, to, to_params);
		} catch (e) {
			routerManager.errorcode = undefined;
			routerManager.error = e instanceof Error ? e : new DeveloperNotificationException("Fehler beim Routing in doLeaveBefore(" + from.name + ")");
			return { name: "error" };
		}
	}


	/**
	 * Ein Ereignis, welches im globalen afterEach des Routings aufgerufen wird,
	 * zu dem Zeitpunkt nachdem die Route verlassen wurde.
	 *
	 * @param from          die Route, die verlassen wird
	 * @param from_params   die Routen-Parameter dazu
	 * @param to            die Route, wohin verlassen wird
	 * @param to_params     die Routen-Parameter dazu
	 */
	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams) : Promise<void> {
	}

	/**
	 * Gibt den Knoten für den übergebenen Namen zurück.
	 *
	 * @param name   der Name des Knotens bzw. der Route
	 *
	 * @returns der Knoten oder undefined
	 */
	public static getNodeByName(name : string | undefined | null) : RouteNode<any, any> | undefined {
		if ((name === undefined) || (name === null))
			return undefined;
		return RouteNode.mapNodesByName.get(name);
	}

	/**
	 * Führt einen Reset der Daten bei diesem Knoten aus.
	 */
	public resetData() : void {
		if (this._hasData)
			this._data.reset();
	}

	/**
	 * Führt einen Reset der Daten bei diesem Knoten und allen Folgenknoten im Baum aus.
	 * Dabei wird der Reset zuerst bei den Blättern ausgeführt.
	 */
	public resetDataRecursive() : void {
		for (const child of this._children)
			child.resetDataRecursive();
		this.resetData();
	}

	/**
	 * Versucht bei den angegebenen Parametern den Parameter mit dem
	 * angegebenen Namen zu bestimmen und als Integer-Wert zu interpretieren.
	 *
	 * @param params   die Parameter der Route
	 * @param names[]  der Namen der Parameter
	 *
	 * @returns der Ein Objekt mit den Integer-Werten oder undefined mit den Parametern als Key
	 */
	protected static getIntParams<const K extends ReadonlyArray<string>>(params: RouteParams, names: K): Record<K[number], number | undefined> {
		const res: Record<string, number | undefined> = {};
		for (const name of names) {
			const value = params[name] as string | string [] | undefined;
			if (value instanceof Array)
				throw new DeveloperNotificationException(`Fehler: Der Parameter ${name} der Route darf kein Array sein`);
			else if ((value === undefined) || (value.toString().trim().length === 0))
				res[name] = undefined;
			else res[name] = parseInt(value);
		}
		return res;
	}

	/**
	 * Versucht bei den angegebenen Parametern den Parameter mit dem
	 * angegebenen Namen zu bestimmen und als String-Wert zu interpretieren.
	 *
	 * @param params   die Parameter der Route
	 * @param names[]  der Namen der Parameter
	 *
	 * @returns der Ein Objekt mit den Integer-Werten oder undefined mit den Parametern als Key
	 */
	protected static getStringParams<const K extends ReadonlyArray<string>>(params: RouteParams, names: K): Record<K[number], string | undefined> {
		const res: Record<string, string | undefined> = {};
		for (const name of names) {
			const value = params[name] as string | string [] | undefined;
			if (value instanceof Array)
				throw new DeveloperNotificationException(`Fehler: Der Parameter ${name} der Route darf kein Array sein`);
			else if ((value === undefined) || (value.toString().trim().length === 0))
				res[name] = undefined;
			else res[name] = value.toString();
		}
		return res;
	}

}
