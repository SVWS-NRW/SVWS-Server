import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteRecordRaw, useRouter } from "vue-router";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeKatalogFoerderschwerpunkteDaten } from "~/router/apps/foerderschwerpunkte/RouteKatalogFoerderschwerpunkteDaten";
import { ListFoerderschwerpunkte } from "~/apps/kataloge/foerderschwerpunkt/ListFoerderschwerpunkte";


export class RouteDataKatalogFoerderschwerpunkte {
	item: FoerderschwerpunktEintrag | undefined = undefined;
	auswahl: ListFoerderschwerpunkte = new ListFoerderschwerpunkte();
}

const SFoerderschwerpunkteAuswahl = () => import("~/components/kataloge/foerderschwerpunkte/SFoerderschwerpunkteAuswahl.vue")
const SFoerderschwerpunkteApp = () => import("~/components/kataloge/foerderschwerpunkte/SFoerderschwerpunkteApp.vue")

export class RouteKatalogFoerderschwerpunkte extends RouteNodeListView<FoerderschwerpunktEintrag, RouteDataKatalogFoerderschwerpunkte> {

	protected defaultChildNode = routeKatalogFoerderschwerpunkteDaten;

	public constructor() {
		super("foerderschwerpunkte", "/kataloge/foerderschwerpunkte/:id(\\d+)?", SFoerderschwerpunkteAuswahl, SFoerderschwerpunkteApp, new RouteDataKatalogFoerderschwerpunkte());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderschwerpunkte";
        super.setView("liste", SFoerderschwerpunkteAuswahl, (route) => RouteNodeListView.getPropsByAuswahlID(route, this.data.auswahl));
		super.children = [
			routeKatalogFoerderschwerpunkteDaten
		];
	}

    /**
     * TODO see RouterManager - global hook
     * 
     * @param to    die Ziel-Route
     * @param from   die Quell-Route
     */
    public async beforeEach(to: RouteLocationNormalized, from: RouteLocationNormalized): Promise<any> {
		if (this.name !== from.name?.toString())
			await this.data.auswahl.update_list();
		if ((to.name?.toString() === this.name) && (to.params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChildNode.name : this.selectedChild.name;
			return { name: redirect_name, params: { id: this.data.auswahl.liste.at(0)?.id }};
		}
        return true;
    }

	protected onSelect(item?: FoerderschwerpunktEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
		} else {
			this.data.item = item;
		}
	}

    protected getAuswahlComputedProperty(): WritableComputedRef<FoerderschwerpunktEintrag | undefined> {
		return this.getSelectorByID<FoerderschwerpunktEintrag, ListFoerderschwerpunkte>(this.data.auswahl);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop = RouteNodeListView.getPropsByAuswahlID(to, this.data.auswahl);
		this.onSelect(prop.item as FoerderschwerpunktEintrag | undefined);
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

export const routeKatalogFoerderschwerpunkte = new RouteKatalogFoerderschwerpunkte();
