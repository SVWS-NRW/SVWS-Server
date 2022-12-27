import { ReligionEintrag } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteRecordRaw, useRouter } from "vue-router";
import { mainApp } from "~/apps/Main";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeKatalogReligionDaten } from "~/router/apps/religion/RouteKatalogReligionDaten";
import { ListReligionen } from "~/apps/kataloge/religionen/ListReligionen";


export class RouteDataKatalogReligion {
	item: ReligionEintrag | undefined = undefined;
}

const SReligionenAuswahl = () => import("~/components/kataloge/religionen/SReligionenAuswahl.vue")
const SReligionenApp = () => import("~/components/kataloge/religionen/SReligionenApp.vue")

export class RouteKatalogReligion extends RouteNodeListView<ReligionEintrag, RouteDataKatalogReligion> {

	protected defaultChildNode = routeKatalogReligionDaten;

	public constructor() {
		super("religionen", "/kataloge/religion/:id(\\d+)?", SReligionenAuswahl, SReligionenApp, new RouteDataKatalogReligion());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Religion";
        super.setView("liste", SReligionenAuswahl, (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.religionen.auswahl));
		super.children = [
			routeKatalogReligionDaten
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
			return { name: redirect_name, params: { id: mainApp.apps.religionen.auswahl.liste.at(0)?.id }};
		}
        return true;
    }

	protected onSelect(item?: ReligionEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
		} else {
			this.data.item = item;
		}
	}

    protected getAuswahlComputedProperty(): WritableComputedRef<ReligionEintrag | undefined> {
		return this.getSelectorByID<ReligionEintrag, ListReligionen>(mainApp.apps.religionen.auswahl);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop = RouteNodeListView.getPropsByAuswahlID(to, mainApp.apps.religionen.auswahl);
		this.onSelect(prop.item as ReligionEintrag | undefined);
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

export const routeKatalogReligion = new RouteKatalogReligion();
