import { BenutzergruppeListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams, RouteRecordRaw, useRouter } from "vue-router";
import { routeSchuleBenutzergruppeDaten } from "~/router/apps/benutzergruppe/RouteSchuleBenutzergruppeDaten";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { ListBenutzergruppe } from "~/apps/schule/benutzerverwaltung/ListBenutzergruppe";
import { computed, WritableComputedRef } from "vue";
import { mainApp } from "~/apps/Main"
import { RouteNode } from "~/router/RouteNode";


export class RouteDataSchuleBenutzergruppe {
	item: BenutzergruppeListeEintrag | undefined = undefined;
}


const SBenutzergruppeAuswahl = () => import("~/components/schule/benutzerverwaltung/SBenutzergruppeAuswahl.vue")
const SBenutzergruppeApp = () => import("~/components/schule/benutzerverwaltung/SBenutzergruppeApp.vue")


export class RouteSchuleBenutzergruppe extends RouteNodeListView<BenutzergruppeListeEintrag, RouteDataSchuleBenutzergruppe> {

	protected defaultChildNode = routeSchuleBenutzergruppeDaten;

	public constructor() {
		super("benutzergruppen", "/schule/benutzergruppe/:id(\\d+)?", SBenutzergruppeAuswahl, SBenutzergruppeApp, new RouteDataSchuleBenutzergruppe());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzergruppen";
        super.setView("liste", SBenutzergruppeAuswahl, (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.benutzergruppe.auswahl));
		super.children = [
			routeSchuleBenutzergruppeDaten
		];
	}

    public async beforeEach(to: RouteNode<unknown>, to_params: RouteParams, from: RouteNode<unknown> | undefined, from_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChildNode.name : this.selectedChild.name;
			return { name: redirect_name, params: { id: mainApp.apps.benutzergruppe.auswahl.liste.at(0)?.id }};
		}
        return true;
    }

	protected onSelect(item?: BenutzergruppeListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
		} else {
			this.data.item = item;
		}
	}

    protected getAuswahlComputedProperty(): WritableComputedRef<BenutzergruppeListeEintrag | undefined> {
		return this.getSelectorByID<BenutzergruppeListeEintrag, ListBenutzergruppe>(mainApp.apps.benutzergruppe.auswahl);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop = RouteNodeListView.getPropsByAuswahlID(to, mainApp.apps.benutzergruppe.auswahl);
		this.onSelect(prop.item as BenutzergruppeListeEintrag | undefined);
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

export const routeSchuleBenutzergruppe = new RouteSchuleBenutzergruppe();
