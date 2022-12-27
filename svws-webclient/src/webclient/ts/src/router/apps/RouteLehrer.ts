import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteRecordRaw, useRouter } from "vue-router";
import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";
import { routeLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";
import { routeLehrerUnterrichtsdaten } from "~/router/apps/lehrer/RouteLehrerUnterrichtsdaten";
import { DataLehrerStammdaten } from "~/apps/lehrer/DataLehrerStammdaten";
import { RouteNodeListView } from "../RouteNodeListView";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { computed, WritableComputedRef } from "vue";
import { mainApp } from "~/apps/Main"


export class RouteDataLehrer {
	item: LehrerListeEintrag | undefined = undefined;
	stammdaten: DataLehrerStammdaten = new DataLehrerStammdaten();
}


const SLehrerAuswahl = () => import("~/components/lehrer/SLehrerAuswahl.vue")
const SLehrerApp = () => import("~/components/lehrer/SLehrerApp.vue")


export class RouteLehrer extends RouteNodeListView<LehrerListeEintrag, RouteDataLehrer> {

	protected defaultChildNode = routeLehrerIndividualdaten;

	public constructor() {
		super("lehrer", "/lehrkraefte/:id(\\d+)?", SLehrerAuswahl, SLehrerApp, new RouteDataLehrer());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lehrkräfte";
        super.setView("liste", SLehrerAuswahl, (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.lehrer.auswahl));
		super.children = [
			routeLehrerIndividualdaten,
			routeLehrerPersonaldaten,
			routeLehrerUnterrichtsdaten
		];
	}

    /**
     * TODO see RouterManager - global hook
     * 
     * @param to    die Ziel-Route
     * @param from   die Quell-Route
     */
    public beforeEach(to: RouteLocationNormalized, from: RouteLocationNormalized): any {
		if ((to.name?.toString() === this.name) && (to.params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChildNode.name : this.selectedChild.name;
			return { name: redirect_name, params: { id: mainApp.apps.lehrer.auswahl.liste.at(0)?.id }};
		}
        return true;
    }

	protected onSelect(item?: LehrerListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.stammdaten.unselect();
		} else {
			this.data.item = item;
			this.data.stammdaten.select(this.data.item);
		}
	}

    protected getAuswahlComputedProperty(): WritableComputedRef<LehrerListeEintrag | undefined> {
		return this.getSelectorByID<LehrerListeEintrag, ListLehrer>(mainApp.apps.lehrer.auswahl);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop = RouteNodeListView.getPropsByAuswahlID(to, mainApp.apps.lehrer.auswahl);
		this.onSelect(prop.item as LehrerListeEintrag | undefined);
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
				const id = (self.data.item === undefined) ? undefined : "" + self.data.item.id;
                router.push({ name: value.name, params: { id: id } });
            }
        });
        return selectedRoute;
    }

}

export const routeLehrer = new RouteLehrer();