import { KursListeEintrag } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteRecordRaw, useRouter } from "vue-router";
import { ListKurse } from "~/apps/kurse/ListKurse";
import { mainApp } from "~/apps/Main";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeKurseDaten } from "~/router/apps/kurse/RouteKurseDaten";

export class RouteDataKurse {
	item: KursListeEintrag | undefined = undefined;
}

const SKurseAuswahl = () => import("~/components/kurse/SKurseAuswahl.vue")
const SKurseApp = () => import("~/components/kurse/SKurseApp.vue")

export class RouteKurse extends RouteNodeListView<KursListeEintrag, RouteDataKurse> {

	protected defaultChildNode = routeKurseDaten;

	public constructor() {
		super("kurse", "/kurse/:id(\\d+)?", SKurseAuswahl, SKurseApp, new RouteDataKurse());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kurse";
        super.setView("liste", SKurseAuswahl, (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.kurse.auswahl));
		super.children = [
			routeKurseDaten
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
			return { name: redirect_name, params: { id: mainApp.apps.kurse.auswahl.liste.at(0)?.id }};
		}
        return true;
    }

	protected onSelect(item?: KursListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
		} else {
			this.data.item = item;
		}
	}

    protected getAuswahlComputedProperty(): WritableComputedRef<KursListeEintrag | undefined> {
		return this.getSelectorByID<KursListeEintrag, ListKurse>(mainApp.apps.kurse.auswahl);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop = RouteNodeListView.getPropsByAuswahlID(to, mainApp.apps.kurse.auswahl);
		this.onSelect(prop.item as KursListeEintrag | undefined);
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

export const routeKurse = new RouteKurse();
