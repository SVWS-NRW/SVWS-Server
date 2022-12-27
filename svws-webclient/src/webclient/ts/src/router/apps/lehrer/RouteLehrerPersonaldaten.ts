import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized } from "vue-router";
import { DataLehrerPersonaldaten } from "~/apps/lehrer/DataLehrerPersonaldaten";
import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";



export class RouteDataLehrerPersonaldaten {
	item: LehrerListeEintrag | undefined = undefined;
	personaldaten: DataLehrerPersonaldaten = new DataLehrerPersonaldaten();
}

const SLehrerPersonaldaten = () => import("~/components/lehrer/personaldaten/SLehrerPersonaldaten.vue");

export class RouteLehrerPersonaldaten extends RouteNode<RouteDataLehrerPersonaldaten> {

	public constructor() {
		super("lehrer_personaldaten", "personaldaten", SLehrerPersonaldaten, new RouteDataLehrerPersonaldaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Personaldaten";
	}

	protected onSelect(item?: LehrerListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.personaldaten.unselect();
		} else {
			this.data.item = item;
			this.data.personaldaten.select(this.data.item);
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop = RouteNodeListView.getPropsByAuswahlID(to, mainApp.apps.lehrer.auswahl);
		this.onSelect(prop.item as LehrerListeEintrag | undefined);
		return prop;
	}

}

export const routeLehrerPersonaldaten = new RouteLehrerPersonaldaten();
