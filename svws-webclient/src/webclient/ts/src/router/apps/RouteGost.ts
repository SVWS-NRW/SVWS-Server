import { GostJahrgang } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteParams, RouteRecordRaw, useRoute, useRouter } from "vue-router";
import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
import { ListGost } from "~/apps/gost/ListGost";
import { mainApp } from "~/apps/Main";
import { RouteNode } from "../RouteNode";
import { RouteNodeListView } from "../RouteNodeListView";
import { routeGostFachwahlen } from "./gost/RouteGostFachwahlen";
import { routeGostFaecher } from "./gost/RouteGostFaecher";
import { routeGostJahrgangsdaten } from "./gost/RouteGostJahrgangsdaten";
import { routeGostKlausurplanung } from "./gost/RouteGostKlausurplanung";
import { routeGostKursplanung } from "./gost/RouteGostKursplanung";

export class RouteDataGost {
	item: GostJahrgang | undefined = undefined;
	jahrgangsdaten: DataGostJahrgang = new DataGostJahrgang();
}

const SGostAuswahl = () => import("~/components/gost/SGostAuswahl.vue")
const SGostApp = () => import("~/components/gost/SGostApp.vue")


export class RouteGost extends RouteNodeListView<GostJahrgang, RouteDataGost> {

	protected defaultChildNode = routeGostJahrgangsdaten;

	public constructor() {
		super("gost", "/gost/:abiturjahr(-?\\d+)?", SGostAuswahl, SGostApp, new RouteDataGost());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Oberstufe";
        super.setView("liste", SGostAuswahl, (route) => RouteGost.getPropsByAuswahlAbiturjahr(route, mainApp.apps.gost.auswahl));
		super.children = [
			routeGostJahrgangsdaten,
			routeGostFaecher,
			routeGostFachwahlen,
			routeGostKursplanung,
			routeGostKlausurplanung
		];
	}

    public async beforeEach(to: RouteNode<unknown>, to_params: RouteParams, from: RouteNode<unknown> | undefined, from_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.abiturjahr === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChildNode.name : this.selectedChild.name;
			return { name: redirect_name, params: { abiturjahr: mainApp.apps.gost.auswahl.liste.at(0)?.abiturjahr }};
		}
        return true;
    }

	protected onSelect(item?: GostJahrgang) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.jahrgangsdaten.unselect();
		} else {
			this.data.item = item;
			this.data.jahrgangsdaten.select(this.data.item);
		}
	}

    protected getAuswahlComputedProperty(): WritableComputedRef<GostJahrgang | undefined> {
		return this.getSelectorByAbiturjahr(mainApp.apps.gost.auswahl);
	}

    /**
     * Eine Hilfs-Methode zum Erzeugen der Properties "id" und "item" zu einer Route bei
     * der Auswahl-Liste des Abiturjahrgangs.
     * 
     * @param route     die aktuelle Route, für die die Properties erzeugt werden sollen
     * @param auswahl   die Liste der Auswahl
     * 
     * @returns das Objekt mit den Werten für die Properties
     */
    public static getPropsByAuswahlAbiturjahr(route: RouteLocationNormalized, auswahl: ListGost) {
        if ((auswahl === undefined) || (route.params.abiturjahr === undefined))
            return { id: undefined, item: undefined, routename: route.name?.toString() };
        const abiturjahr = parseInt(route.params.abiturjahr as string);
        const item = auswahl.liste.find(s => s.abiturjahr === abiturjahr);
        return { id: abiturjahr, item: item, routename: route.name?.toString() };
    }

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop = RouteGost.getPropsByAuswahlAbiturjahr(to, mainApp.apps.gost.auswahl);
		this.onSelect(prop.item as GostJahrgang | undefined);
		return prop;
	}

	
    /**
     * TODO
     * 
     * @returns 
     */
    public getChildRouteSelector() {
        const router = useRouter();
        const self = this;
        const selectedRoute: WritableComputedRef<RouteRecordRaw> = computed({
            get(): RouteRecordRaw {
                return self.selectedChildRecord || self.defaultChildNode.record;
            },
            set(value: RouteRecordRaw) {
                self.selectedChildRecord = value;
				const abiturjahr = (self.data.item === undefined) ? undefined : "" + self.data.item.abiturjahr;
                router.push({ name: value.name, params: { abiturjahr: abiturjahr } });
            }
        });
        return selectedRoute;
    }

    /**
     * Eine Hilfs-Methode zum Erzeugen der beschreibaren Computed-Property für die Auswahl einer 
     * Route eines Routen-Eintrags in der zugehörigen vue-Komponente.
     * 
     * @param auswahl   die Liste der Auswahl
     * 
     * @returns die Computed-Property
     */
    public getSelectorByAbiturjahr(auswahl: ListGost) : WritableComputedRef<GostJahrgang | undefined> {
        const router = useRouter();
        const route = useRoute();
        const name: string = this.name;
        const redirect_name: string = (this.selectedChild === undefined) ? name : this.selectedChild.name;

        const selected = computed({
            get(): GostJahrgang | undefined {
                if (route.params.abiturjahr === undefined)
                    return undefined;
                let tmp = auswahl.ausgewaehlt;
                if ((tmp === undefined) || (tmp.abiturjahr.toString() !== route.params.abiturjahr))
                    tmp = auswahl.liste.find(s => s.abiturjahr.toString() === route.params.abiturjahr);
                return tmp;
            },
            set(value: GostJahrgang | undefined) {
                auswahl.ausgewaehlt = value;
                const from_name = route.name?.toString() || "";
                if ((from_name !== name) && from_name?.startsWith(name)) {  // TODO Ergänze Methode bei RouteNode isNested und nutze diese 
                    const params = {...route.params};
                    params.abiturjahr = "" + value?.abiturjahr;
                    router.push({ name: from_name, params: params });
                } else {
                    router.push({ name: redirect_name, params: { abiturjahr: value?.abiturjahr } });
                }
            }
        });
        return selected;
    }

}

export const routeGost = new RouteGost();
