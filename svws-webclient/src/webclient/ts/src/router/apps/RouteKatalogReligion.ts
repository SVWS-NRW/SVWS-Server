import { ReligionEintrag } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteParams, RouteRecordRaw, useRouter } from "vue-router";
import { mainApp } from "~/apps/Main";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeKatalogReligionDaten } from "~/router/apps/religion/RouteKatalogReligionDaten";
import { ListReligionen } from "~/apps/kataloge/religionen/ListReligionen";
import { RouteNode } from "../RouteNode";


export class RouteDataKatalogReligion {
	item: ReligionEintrag | undefined = undefined;
}

const SReligionenAuswahl = () => import("~/components/kataloge/religionen/SReligionenAuswahl.vue")
const SReligionenApp = () => import("~/components/kataloge/religionen/SReligionenApp.vue")

export class RouteKatalogReligion extends RouteNodeListView<ReligionEintrag, RouteDataKatalogReligion> {

	public constructor() {
		super("religionen", "/kataloge/religion/:id(\\d+)?", SReligionenAuswahl, SReligionenApp, new RouteDataKatalogReligion());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Religion";
        super.setView("liste", SReligionenAuswahl, (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.religionen.auswahl));
		super.children = [
			routeKatalogReligionDaten
		];
		super.defaultChild = routeKatalogReligionDaten;
	}

    public async beforeEach(to: RouteNode<unknown>, to_params: RouteParams, from: RouteNode<unknown> | undefined, from_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChild!.name : this.selectedChild.name;
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
        const selectedRoute: WritableComputedRef<RouteRecordRaw> = computed({
            get: () => this.selectedChildRecord || this.defaultChild!.record,
            set: (value) => {
                this.selectedChildRecord = value;
				const id = (this.data.item === undefined) ? undefined : "" + this.data.item.id;
                router.push({ name: value.name, params: { id: id } });
            }
        });
        return selectedRoute;
    }

}

export const routeKatalogReligion = new RouteKatalogReligion();
