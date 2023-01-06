import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams } from "vue-router";

export class RouteDataGostKursplanung  {
	listLehrer: ListLehrer = new ListLehrer();
	mapLehrer: Map<Number, LehrerListeEintrag> = new Map();
}

const SGostKursplanung = () => import("~/components/gost/kursplanung/SGostKursplanung.vue");

export class RouteGostKursplanung extends RouteNode<RouteDataGostKursplanung> {

	public constructor() {
		super("gost_kursplanung", "kursplanung", SGostKursplanung, new RouteDataGostKursplanung());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kursplanung";
		this.isHidden = () => {
			return (routeGost.data.item.value === undefined) || (routeGost.data.item.value.abiturjahr === -1);
		}
	}

	public async enter(to: RouteNode<unknown>, to_params: RouteParams) {
		await this.data.listLehrer.update_list();
		this.data.mapLehrer.clear();
		this.data.listLehrer.liste.forEach(k => this.data.mapLehrer.set(k.id, k));
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop: Record<string, any> = routeGost.getProps(to);
		prop.listLehrer = this.data.listLehrer;
		prop.mapLehrer = this.data.mapLehrer;
		return prop;
	}

}

export const routeGostKursplanung = new RouteGostKursplanung();
