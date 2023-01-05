import { JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteParams, RouteRecordRaw, useRouter } from "vue-router";
import { mainApp } from "~/apps/Main";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeKatalogJahrgaengeDaten } from "~/router/apps/jahrgaenge/RouteKatalogJahrgaengeDaten";
import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";
import { RouteNode } from "~/router/RouteNode";

export class RouteDataKatalogJahrgaenge {
	item: JahrgangsListeEintrag | undefined = undefined;
}

const SJahrgaengeAuswahl = () => import("~/components/jahrgaenge/SJahrgaengeAuswahl.vue")
const SJahrgaengeApp = () => import("~/components/jahrgaenge/SJahrgaengeApp.vue")

export class RouteKatalogJahrgaenge extends RouteNodeListView<JahrgangsListeEintrag, RouteDataKatalogJahrgaenge> {

	protected defaultChildNode = routeKatalogJahrgaengeDaten;

	public constructor() {
		super("jahrgaenge", "/kataloge/jahrgaenge/:id(\\d+)?", SJahrgaengeAuswahl, SJahrgaengeApp, new RouteDataKatalogJahrgaenge());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgänge";
        super.setView("liste", SJahrgaengeAuswahl, (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.jahrgaenge.auswahl));
		super.children = [
			routeKatalogJahrgaengeDaten
		];
	}

    public async beforeEach(to: RouteNode<unknown>, to_params: RouteParams, from: RouteNode<unknown> | undefined, from_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChildNode.name : this.selectedChild.name;
			return { name: redirect_name, params: { id: mainApp.apps.jahrgaenge.auswahl.liste.at(0)?.id }};
		}
        return true;
    }

	protected onSelect(item?: JahrgangsListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
		} else {
			this.data.item = item;
		}
	}

    protected getAuswahlComputedProperty(): WritableComputedRef<JahrgangsListeEintrag | undefined> {
		return this.getSelectorByID<JahrgangsListeEintrag, ListJahrgaenge>(mainApp.apps.jahrgaenge.auswahl);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop = RouteNodeListView.getPropsByAuswahlID(to, mainApp.apps.jahrgaenge.auswahl);
		this.onSelect(prop.item as JahrgangsListeEintrag | undefined);
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

export const routeKatalogJahrgaenge = new RouteKatalogJahrgaenge();
