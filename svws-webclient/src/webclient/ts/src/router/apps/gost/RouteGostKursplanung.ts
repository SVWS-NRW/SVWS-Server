import { RouteNode } from "~/router/RouteNode";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { GostJahrgang, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams } from "vue-router";
import { mainApp } from "~/apps/Main";

export class RouteDataGostKursplanung  {
	listLehrer: ListLehrer = new ListLehrer();
	mapLehrer: Map<Number, LehrerListeEintrag> = new Map();
}

const SGostKursplanung = () => import("~/components/gost/kursplanung/SGostKursplanung.vue");

export class RouteGostKursplanung extends RouteNode<RouteDataGostKursplanung, RouteGost> {

	public constructor() {
		super("gost_kursplanung", "kursplanung", SGostKursplanung, new RouteDataGostKursplanung());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kursplanung";
		this.isHidden = () => {
			return this.checkHidden(routeGost.data.item.value);
		}
	}

	public checkHidden(jahrgang: GostJahrgang | undefined) {
		return (jahrgang === undefined) || (jahrgang.abiturjahr === -1);;
	}

    public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if (to.name === this.name) {
			if (to_params.abiturjahr === undefined)
				return false;
			const jahrgang = mainApp.apps.gost.auswahl.liste.find(elem => elem.abiturjahr.toString() === to_params.abiturjahr);
			if (this.checkHidden(jahrgang))
				return { name: this.parent!.defaultChild!.name, params: { abiturjahr: mainApp.apps.gost.auswahl.liste.at(0)?.abiturjahr }};
		}
        return true;
    }

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
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
