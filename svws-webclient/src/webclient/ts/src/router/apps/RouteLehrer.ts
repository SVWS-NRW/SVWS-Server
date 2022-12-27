import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";
import { routeLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";
import { routeLehrerUnterrichtsdaten } from "~/router/apps/lehrer/RouteLehrerUnterrichtsdaten";
import { DataLehrerStammdaten } from "~/apps/lehrer/DataLehrerStammdaten";
import { RouteNodeListView } from "../RouteNodeListView";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { WritableComputedRef } from "vue";
import { mainApp } from "~/apps/Main"


export class RouteDataLehrer {
	item: LehrerListeEintrag | undefined = undefined;
	stammdaten: DataLehrerStammdaten = new DataLehrerStammdaten();
}


const SLehrerAuswahl = () => import("~/components/lehrer/SLehrerAuswahl.vue")
const SLehrerApp = () => import("~/components/lehrer/SLehrerApp.vue")


export class RouteLehrer extends RouteNodeListView<ListLehrer, LehrerListeEintrag, RouteDataLehrer> {

	public constructor() {
		super("lehrer", "/lehrkraefte/:id(\\d+)?", SLehrerAuswahl, SLehrerApp, new RouteDataLehrer());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Personaldaten";
        super.setView("liste", SLehrerAuswahl, (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.lehrer.auswahl));
		super.children = [
			routeLehrerIndividualdaten,
			routeLehrerPersonaldaten,
			routeLehrerUnterrichtsdaten
		];
		this.autoInit = true;
	}

    /**
     * TODO see RouterManager - global hook
     * 
     * @param to    die Ziel-Route
     * @param from   die Quell-Route
     */
    public beforeEach(to: RouteLocationNormalized, from: RouteLocationNormalized): any {
		if ((to.name?.toString() === this.name) && (to.params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? routeLehrerIndividualdaten.name : this.selectedChild.name;
			return { name: redirect_name, params: mainApp.apps.lehrer.auswahl.liste.at(0) };
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
		return this.getSelectorByID<LehrerListeEintrag, ListLehrer>(injectMainApp().apps.lehrer.auswahl);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop = RouteNodeListView.getPropsByAuswahlID(to, injectMainApp().apps.lehrer.auswahl);
		this.onSelect(prop.item as LehrerListeEintrag | undefined);
		return prop;
	}

}

export const routeLehrer = new RouteLehrer();

/*
export const RouteLehrer : RouteRecordRaw = {
	meta: <RouteAppMeta<LehrerListeEintrag | undefined, RouteDataLehrer>> {
		redirect: ref(routeLehrerIndividualdaten.record),
	},
	redirect: to => {
		return to.path + "/" + routeAppMeta(RouteLehrer)?.redirect.value.path;
	},
	children: RouteLehrerChildren
};


export function routeLehrerSetRedirect(route : RouteLocationNormalizedLoaded) {
	const meta = RouteLehrer.meta as RouteAppMeta<LehrerListeEintrag | undefined, RouteDataLehrer>;
	if ((RouteLehrer.children === undefined) || (meta === undefined))
		return;
	for (var child of RouteLehrer.children) {
		if (route.name === child.name) {
			meta.redirect.value = child;
			return;
		}
	}
	meta.redirect.value = routeLehrerIndividualdaten.record;
}
*/
