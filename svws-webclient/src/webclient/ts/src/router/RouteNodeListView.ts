import { computed, shallowRef, ShallowRef, WritableComputedRef } from "vue";
import { RouteComponent, RouteLocationNormalized, RouteRecordRaw, useRoute, useRouter } from "vue-router";
import { BaseList } from "~/apps/BaseList";
import { RouteNode } from "~/router/RouteNode";

/**
 * Diese abstrakte Klasse ist die Basisklasse aller Knoten mit zwei
 * Router-Views für das Routing innerhalb des SVWS-Clients basierend
 * auf einer Auswahlliste.
 * Die eine View hat die Bezeichnung "liste" und dient der Darstellung
 * einer Auswahlliste und die "default"-Router-View dient der Darstellung
 * von Detail-Informationen zu dem ausgewählten Listen-Eintrag.
 */
export abstract class RouteNodeListView<TAuswahl extends BaseList<TItemAuswahl, unknown>, TItemAuswahl, TRouteData, TRouteParent extends RouteNode<unknown, any>> extends RouteNode<TRouteData, TRouteParent> {

	/** Die Auswahlliste */
	private _liste: TAuswahl | undefined;

	/** Die Referenz auf das aktuell ausgewählte Item */
	public _item: ShallowRef<TItemAuswahl | undefined> = shallowRef(undefined);

	/** Der Name des identifizierenden Attributs eines Auswahl-Elements */
	private _itemKey: (keyof TItemAuswahl) | undefined;


	/**
     * Erstellt einen Knoten für das Routing mithilfe einer Auswahl-Liste.
     *
     * TODO
     * @param name   der Name des Routing-Knotens (siehe RouteRecordRaw)
     * @param path   der Pfad der Route (siehe RouteRecordRaw)
     * @param componentListe   die vue-Komponente für die Listen-Auswahl
     * @param componentDefault   die vue-Komponente für die Darstellung der Detail-Informationen
     * @param data   die dem Knoten zugeordneten Daten
     */
	public constructor(name: string, path: string, componentListe: RouteComponent, componentDefault: RouteComponent, liste?: TAuswahl, itemKey?: keyof TItemAuswahl, data?: TRouteData) {
		super(name, path, componentDefault, data);
		super.setView("liste", componentListe, (to) => this.getNoProps(to));
		this._liste = liste;
		this._itemKey = itemKey;
	}

	public get liste(): TAuswahl {
		if (this._liste === undefined)
			throw new Error("Fehler in der Programmierung: Keine Auswahl-Liste in der Route " + this.name + " definiert.");
		return this._liste;
	}

	public get itemKey(): keyof TItemAuswahl {
		if (this._itemKey === undefined)
			throw new Error("Programmierfehler: Es ist kein Schlüsselattribut für diese Auswahl definiert.");
		return this._itemKey;
	}

	public get item(): TItemAuswahl | undefined {
		return this._item.value;
	}

	public set item(value : TItemAuswahl | undefined) {
		this._item.value = value;
	}

	public getProps(route: RouteLocationNormalized): Record<string, any> {
		return { item: this._item };
	}

	public get childRouteSelector() : WritableComputedRef<RouteRecordRaw> {
		const router = useRouter();
		return computed({
			get: () => this.selectedChildRecord || this.defaultChild!.record,
			set: (value) => {
				this.selectedChildRecord = value;
				let id_value: string | undefined = undefined;
				if ((this.item !== undefined) && (this.item !== null))
					id_value = "" + this.item[this.itemKey];
				const params = { [this.itemKey]: id_value };
				void router.push({ name: value.name, params: params });
			}
		});
	}

	/**
     * Gibt eine Computed-Property zurück, die für die Auswahl eines Elements aus einer Liste von Elementen verantwortlich ist.
     */
	public get auswahl() : WritableComputedRef<TItemAuswahl | undefined> {
		return this.getAuswahlComputedProperty();
	}

    /**
     * TODO
     *
     * Diese Methode muss für die Auswahl überschrieben werden.
     *
     * @returns TODO
     */
    protected abstract getAuswahlComputedProperty(): WritableComputedRef<TItemAuswahl | undefined>;

    /**
     * Eine Hilfs-Methode zum Erzeugen der beschreibaren Computed-Property für die Auswahl einer
     * Route eines Routen-Eintrags in der zugehörigen vue-Komponente.
     *
     * @returns die Computed-Property
     */
    public getSelector() : WritableComputedRef<TItemAuswahl | undefined> {
    	const router = useRouter();
    	const route = useRoute();
    	const name: string = this.name;
    	const redirect_name: string = (this.selectedChild === undefined) ? name : this.selectedChild.name;
    	if (this._itemKey === undefined)
    		throw new Error("Programmierfehler: getSelector() kann nicht ohne die Definition eines Schlüsselattributes für die Elemente der Auswahlliste verwendet werden.");

    	return computed({
    		get: () => {
    			if (route.params[this.itemKey.toString()] === undefined)
    				return undefined;
    			const tmp = this.liste.ausgewaehlt;
    			if ((tmp !== undefined) && (tmp !== null)) {
    				const id = tmp[this.itemKey];
    				if ((id !== undefined) && (id !== null) && (id.toString() === route.params[this.itemKey.toString()]))
    					return tmp;
    			}
    			return this.liste.liste.find(s => "" + s[this.itemKey] === route.params[this.itemKey.toString()]);
    		},
    		set: (value) => {
    			this.liste.ausgewaehlt = value;
    			const from_name = route.name?.toString() || "";
    			const id_value = "" + (((value === undefined) || (value === null)) ? undefined : value[this.itemKey]);
    			if ((from_name !== name) && from_name?.startsWith(name)) {  // TODO Ergänze Methode bei RouteNode isNested und nutze diese
    				const params = {...route.params};
    				params[this.itemKey.toString()] = id_value;
    				void router.push({ name: from_name, params: params });
    			} else {
    				const params = { [this.itemKey]: id_value };
    				void router.push({ name: redirect_name, params: params });
    			}
    		}
    	});
    }


    /**
     * Eine Hilfs-Methode zum Erzeugen der beschreibaren Computed-Property für die Auswahl einer
     * Route eines Routen-Eintrags in der zugehörigen vue-Komponente.
     *
     * @param auswahl   die Liste der Auswahl (siehe auch BaseList)
     *
     * @returns die Computed-Property
     */
    public getSelectorByID<TItem extends { id: number }, TAuswahl extends BaseList<TItem, unknown>>(auswahl: TAuswahl) : WritableComputedRef<TItem | undefined> {
    	const router = useRouter();
    	const route = useRoute();
    	const name: string = this.name;
    	const redirect_name: string = (this.selectedChild === undefined) ? name : this.selectedChild.name;

    	const selected = computed({
    		get(): TItem | undefined {
    			if (route.params.id === undefined)
    				return undefined;
    			let tmp = auswahl.ausgewaehlt;
    			if ((tmp === undefined) || (tmp.id === undefined) || (tmp.id.toString() !== route.params.id))
    				tmp = auswahl.liste.find(s => s.id.toString() === route.params.id);
    			return tmp;
    		},
    		set(value: TItem | undefined) {
    			auswahl.ausgewaehlt = value;
    			const from_name = route.name?.toString() || "";
    			if ((from_name !== name) && from_name?.startsWith(name)) {  // TODO Ergänze Methode bei RouteNode isNested und nutze diese
    				const params = {...route.params};
    				params.id = "" + value?.id;
    				void router.push({ name: from_name, params: params });
    			} else {
    				void router.push({ name: redirect_name, params: { id: value?.id } });
    			}
    		}
    	});
    	return selected;
    }


    /**
     * Eine Hilfs-Methode zum Erzeugen der Properties "id" und "item" zu einer Route bei "normalen"
     * Auswahl-Listen, welche ein numerisches ID-Attribut haben.
     *
     * Dies ist eine Hilfsmethode für die Nutzung beim Definieren von Routen-Einträgen.
     *
     * @param route     die aktuelle Route, für die die Properties erzeugt werden sollen
     * @param name      der Name des Routen-Eintrages, für welche die Properties erzeugt werden sollen.
     *                  Dieser wird genutzt, um zu prüfen, ob die übergebene Route zu dem Routen-Eintrag passt
     * @param auswahl   die Liste der Auswahl (siehe auch BaseList)
     *
     * @returns das Objekt mit den Werten für die Properties
     */
    public static getPropsByAuswahlID<TAuswahl extends BaseList<{ id: number }, unknown>>(route: RouteLocationNormalized, auswahl: TAuswahl) {
    	if ((auswahl === undefined) || (route.params.id === undefined))
    		return { id: undefined, item: undefined, routename: route.name?.toString() };
    	const id = parseInt(route.params.id as string);
    	const item = auswahl.liste.find(s => s.id === id);
    	return { id: id, item: item, routename: route.name?.toString() };
    }

}
