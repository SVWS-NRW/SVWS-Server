import { ref, Ref } from "vue";
import { RouteComponent, RouteLocationNormalized, RouteRecordRaw } from "vue-router";

/**
 * Diese abstrakte Klasse ist die Basisklasse aller Knoten für
 * das Routing innerhalb des SVWS-Clients.
 */
export abstract class RouteNode<TRouteData> {

    /** Eine Map mit allen Knoten */
    protected static mapNodesByName: Map<string, RouteNode<unknown>> = new Map();


    /** Das vue-Router-Objekt (siehe RouteRecordRaw) */
    protected _record: RouteRecordRaw;

    // Eine Funktion zum Prüfen, ob der Knoten, d.h. die Route, versteckt sein soll oder nicht
    protected isHidden: (() => boolean) | undefined = undefined;

    /** Die Kind-Knoten zu dieser Route */
    protected _children: RouteNode<unknown>[];

    /** Die Knoten, welche in einem Menu zu dieser Komponente zur Verfügung gestellt werden */
    protected _menu: RouteNode<unknown>[];

	/** Die Daten, die dem Knoten zugeordnet sind */
	protected _data: TRouteData;          

	/** Der ausgewählte Kind-Knoten, zu welchem geroutet werden soll (z.B. bei Tabs nach einer Auswahl) */
	protected _selectedChild: Ref<RouteNode<unknown> | undefined> = ref(undefined);


    /**
     * Erstellt einen neuen Knoten für das Routing mithilfe einer
     * einfachen Default-Router-View.
     * Es können mithilfe der Methode "setView" noch weitere Router-Views 
     * ergänzt werden
     * 
     * @param name   der Name des Routing-Knotens (siehe RouteRecordRaw)
     * @param path   der Pfad der Route (siehe RouteRecordRaw)
     * @param component   die vue-Komponente für die Darstellung der Informationen der gewählten Route
     * @param data   die dem Knoten zugeordneten Daten
     */
    public constructor(name: string, path: string, component: RouteComponent, data?: TRouteData) {
        RouteNode.mapNodesByName.set(name, this);
        this._record = {
            name: name,
            path: path,
            components: { default: component },
            props: { default: (to) => this.getNoProps(to) },
            children: undefined,
            meta: {
                text: name 	// Ein Text, welcher zur Darstellung in der GUI genutzt wird (z.B. der Text auf Tabs)
            }
        };
        this._children = [];
        this._menu = [];
        this._data = (data !== undefined) ? data : {} as TRouteData;
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
     * Setzt die Kind-Knoten für diesen Knoten in Bezug auf das 
     * "Nested"-Routing.
     * 
     * @param nodes   Ein Array mit den Kindern für das Routing
     */
    public set children(nodes: RouteNode<unknown>[]) {
        this._children = nodes;
        this._record.children = nodes.length === 0 ? undefined : nodes.map(n => n.record);
    }

    /**
     * Gibt die Kind-Knoten für diesen Knoten in Bezug auf das 
     * "Nested"-Routing zurück.
     * 
     * @returns ein Array mit den Kind-Knoten
     */
    public get children() : RouteNode<unknown>[] {
        return this._children;
    }

    /**
     * Setzt die Knoten, welche bei der Auswahl dieser Route für ein 
     * Menu zur Verfügung stehen.
     * 
     * @param nodes   Ein Array mit den Knoten
     */
    public set menu(nodes: RouteNode<unknown>[]) {
        this._menu = nodes;
    }

    /**
     * Gibt die Knoten zurück, welche bei der Auswahl dieser Route für ein 
     * Menu zur Verfügung stehen.
     * 
     * @returns ein Array mit den Knoten
     */
    public get menu() : RouteNode<unknown>[] {
        return this._menu;
    }

    /**
     * Gibt die vue-route-Record-Objekte der Kind-Knoten für diesen Knoten in 
     * Bezug auf das  "Nested"-Routing zurück.
     *  
     * @returns ein Array mit den vue-route-Record-Objekten
     */
    public get children_records() : RouteRecordRaw[] {
        return this._record.children || [];
    }

    /**
     * Gibt ein passend zu dem getter children ein Array zurück,
     * welches angibt, ob die einzelnen Kind-Knoten versteckt sind oder
     * nicht.
     * 
     * @returns ein Array mit der 
     */
    public get children_hidden() : boolean[] {
        return this._children.map(c => c.hidden);
    }

    /**
     * TODO
     */
    public get selectedChild() : RouteNode<unknown> | undefined {
        return this._selectedChild.value;
    }

    /**
     * TODO
     */
    public set selectedChildRecord(record : RouteRecordRaw | undefined) {
        this._selectedChild.value = (record === undefined) || (record.name === undefined) ? undefined : RouteNode.mapNodesByName.get(record.name?.toString());
    }

    /**
     * TODO
     */
    public get selectedChildRecord() : RouteRecordRaw | undefined {
        return this._selectedChild.value?.record;
    }

    /**
     * Setzt der Property-Handler für die Default-View
     */
    public set propHandler(handler: (to: RouteLocationNormalized) => Record<string, any>) {
        (this._record.props as { [key: string] : (to: RouteLocationNormalized) => Record<string, any> })["default"] = handler;
    }

	/** 
     * Gibt zurück, ob der Knoten für das Routing versteckt ist oder nicht.
     * 
     * @returns true, falls der Knoten versteckt werden soll und für das Routing nicht zur Verfügung steht.
     */
	public get hidden(): boolean {
        // TODO prüfen, ob die Komponente dargestellt werden darf oder nicht
        return (this.isHidden === undefined) ? false : this.isHidden();
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
            throw new Error("Unerwarteter Fehler in der Methode RouteNode::addView. components oder props ist undefined.");
        (this._record.components as { [key: string] : RouteComponent })[name] = component;
        (this._record.props as { [key: string] : (to: RouteLocationNormalized) => Record<string, any> })[name] = prop_handler;
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
     * TODO see RouterManager - global hook
     * 
     * @param to    die Ziel-Route
     * @param from   die Quell-Route
     */
    public async beforeEach(to: RouteLocationNormalized, from: RouteLocationNormalized) {
        return true;
    }


    /**
     * Gibt den Knoten für den übergebenen Namen zurück.
     * 
     * @param name   der Name des Knotens bzw. der Route
     * 
     * @returns der Knoten oder undefined
     */
    public static getNodeByName(name : string | undefined | null) : RouteNode<unknown> | undefined {
        if ((name === undefined) || (name === null))
            return undefined;
        return RouteNode.mapNodesByName.get(name);
    }

}
