import { KlassenListeEintrag } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteRecordRaw, useRouter } from "vue-router";
import { mainApp } from "~/apps/Main";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeKlassenDaten } from "~/router/apps/klassen/RouteKlassenDaten";
import { ListKlassen } from "~/apps/klassen/ListKlassen";

export class RouteDataKlassen {
	item: KlassenListeEintrag | undefined = undefined;
}

const SKlassenAuswahl = () => import("~/components/klassen/SKlassenAuswahl.vue")
const SKlassenApp = () => import("~/components/klassen/SKlassenApp.vue")

export class RouteKlassen extends RouteNodeListView<KlassenListeEintrag, RouteDataKlassen> {

	protected defaultChildNode = routeKlassenDaten;

	public constructor() {
		super("klassen", "/klassen/:id(\\d+)?", SKlassenAuswahl, SKlassenApp, new RouteDataKlassen());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klassen";
        super.setView("liste", SKlassenAuswahl, (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.klassen.auswahl));
		super.children = [
			routeKlassenDaten
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
			return { name: redirect_name, params: { id: mainApp.apps.klassen.auswahl.liste.at(0)?.id }};
		}
        return true;
    }

	protected onSelect(item?: KlassenListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
		} else {
			this.data.item = item;
		}
	}

    protected getAuswahlComputedProperty(): WritableComputedRef<KlassenListeEintrag | undefined> {
		return this.getSelectorByID<KlassenListeEintrag, ListKlassen>(mainApp.apps.klassen.auswahl);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop = RouteNodeListView.getPropsByAuswahlID(to, mainApp.apps.klassen.auswahl);
		this.onSelect(prop.item as KlassenListeEintrag | undefined);
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

export const routeKlassen = new RouteKlassen();
