import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams, RouteRecordRaw, useRouter } from "vue-router";
import { routeSchuleBenutzerDaten } from "~/router/apps/benutzer/RouteSchuleBenutzerDaten";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { ListBenutzer } from "~/apps/schule/benutzerverwaltung/ListBenutzer";
import { computed, WritableComputedRef } from "vue";
import { mainApp } from "~/apps/Main"
import { RouteNode } from "~/router/RouteNode";
import { RouteApp } from "~/router/RouteApp";


export class RouteDataSchuleBenutzer {
	item: BenutzerListeEintrag | undefined = undefined;
}


const SBenutzerAuswahl = () => import("~/components/schule/benutzerverwaltung/SBenutzerAuswahl.vue")
const SBenutzerApp = () => import("~/components/schule/benutzerverwaltung/SBenutzerApp.vue")


export class RouteSchuleBenutzer extends RouteNodeListView<ListBenutzer, BenutzerListeEintrag, RouteDataSchuleBenutzer, RouteApp> {

	public constructor() {
		super("benutzer", "/schule/benutzer/:id(\\d+)?", SBenutzerAuswahl, SBenutzerApp, new ListBenutzer(), 'id', new RouteDataSchuleBenutzer());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzer";
        super.setView("liste", SBenutzerAuswahl, (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.benutzer.auswahl));
		super.children = [
			routeSchuleBenutzerDaten
		];
		super.defaultChild = routeSchuleBenutzerDaten;
	}

    public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChild!.name : this.selectedChild.name;
			return { name: redirect_name, params: { id: mainApp.apps.benutzer.auswahl.liste.at(0)?.id }};
		}
        return true;
    }

	protected onSelect(item?: BenutzerListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
		} else {
			this.data.item = item;
		}
	}

    protected getAuswahlComputedProperty(): WritableComputedRef<BenutzerListeEintrag | undefined> {
		return this.getSelectorByID<BenutzerListeEintrag, ListBenutzer>(mainApp.apps.benutzer.auswahl);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop = RouteNodeListView.getPropsByAuswahlID(to, mainApp.apps.benutzer.auswahl);
		this.onSelect(prop.item as BenutzerListeEintrag | undefined);
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

export const routeSchuleBenutzer = new RouteSchuleBenutzer();
