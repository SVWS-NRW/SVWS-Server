import { computed, WritableComputedRef } from "vue";
import { RouteComponent, RouteLocationNormalized, useRoute, useRouter } from "vue-router";
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
export abstract class RouteNodeListView<TItemAuswahl, TRouteData, TRouteParent extends RouteNode<unknown, any>> extends RouteNode<TRouteData, TRouteParent> {

    /**
     * Erstellt einen Knoten für das Routing mithilfe einer Auswahl-Liste.
     * 
     * @param name   der Name des Routing-Knotens (siehe RouteRecordRaw)
     * @param path   der Pfad der Route (siehe RouteRecordRaw)
     * @param componentListe   die vue-Komponente für die Listen-Auswahl
     * @param componentDefault   die vue-Komponente für die Darstellung der Detail-Informationen
     * @param data   die dem Knoten zugeordneten Daten
     */
    public constructor(name: string, path: string, componentListe: RouteComponent, componentDefault: RouteComponent, data?: TRouteData) {
        super(name, path, componentDefault, data);
        super.setView("liste", componentListe, (to) => this.getNoProps(to));
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
                    router.push({ name: from_name, params: params });
                } else {
                    router.push({ name: redirect_name, params: { id: value?.id } });
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
