import { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteRecordRaw, useRouter } from "vue-router";
import { ListFaecher } from "~/apps/faecher/ListFaecher";
import { mainApp } from "~/apps/Main";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeFaecherDaten } from "~/router/apps/faecher/RouteFaecherDaten";

const ROUTE_NAME: string = "faecher";

export class RouteDataKatalogFaecher {
	item: FaecherListeEintrag | undefined = undefined;
}

const SFaecherAuswahl = () => import("~/components/faecher/SFaecherAuswahl.vue")
const SFaecherApp = () => import("~/components/faecher/SFaecherApp.vue")

export class RouteKatalogFaecher extends RouteNodeListView<FaecherListeEintrag, RouteDataKatalogFaecher> {

	protected defaultChildNode = routeFaecherDaten;

	public constructor() {
		super("faecher", "/kataloge/faecher/:id(\\d+)?", SFaecherAuswahl, SFaecherApp, new RouteDataKatalogFaecher());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fächer";
        super.setView("liste", SFaecherAuswahl, (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.faecher.auswahl));
		super.children = [
			routeFaecherDaten
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
			return { name: redirect_name, params: { id: mainApp.apps.faecher.auswahl.liste.at(0)?.id }};
		}
        return true;
    }

	protected onSelect(item?: FaecherListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
		} else {
			this.data.item = item;
		}
	}

    protected getAuswahlComputedProperty(): WritableComputedRef<FaecherListeEintrag | undefined> {
		return this.getSelectorByID<FaecherListeEintrag, ListFaecher>(mainApp.apps.faecher.auswahl);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop = RouteNodeListView.getPropsByAuswahlID(to, mainApp.apps.faecher.auswahl);
		this.onSelect(prop.item as FaecherListeEintrag | undefined);
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

export const routeKatalogFaecher = new RouteKatalogFaecher();
