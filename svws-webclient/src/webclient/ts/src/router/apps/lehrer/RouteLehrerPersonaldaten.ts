import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized } from "vue-router";
import { DataLehrerPersonaldaten } from "~/apps/lehrer/DataLehrerPersonaldaten";
import { RouteNode } from "~/router/RouteNode";
import { RouteLehrer, routeLehrer } from "~/router/apps/RouteLehrer";



export class RouteDataLehrerPersonaldaten {
	item: LehrerListeEintrag | undefined = undefined;
	personaldaten: DataLehrerPersonaldaten = new DataLehrerPersonaldaten();
}

const SLehrerPersonaldaten = () => import("~/components/lehrer/personaldaten/SLehrerPersonaldaten.vue");

export class RouteLehrerPersonaldaten extends RouteNode<RouteDataLehrerPersonaldaten, RouteLehrer> {

	public constructor() {
		super("lehrer_personaldaten", "personaldaten", SLehrerPersonaldaten, new RouteDataLehrerPersonaldaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Personaldaten";
	}

	protected async onSelect(item?: LehrerListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			await this.data.personaldaten.unselect();
		} else {
			this.data.item = item;
			await this.data.personaldaten.select(this.data.item);
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop = routeLehrer.getProps(to);
		this.onSelect(prop.item.value);
		prop.personaldaten = this.data.personaldaten;
		return prop;
	}

}

export const routeLehrerPersonaldaten = new RouteLehrerPersonaldaten();
