import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
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

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const tmp = parseInt(to_params.id as string);
			await this.onSelect(this.parent!.liste.liste.find(s => s.id === tmp));
		}
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

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			personaldaten: this.data.personaldaten
		};
	}

}

export const routeLehrerPersonaldaten = new RouteLehrerPersonaldaten();
