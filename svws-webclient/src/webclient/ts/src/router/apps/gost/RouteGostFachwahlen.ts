import { RouteNode } from "~/router/RouteNode";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";
import { RouteParams } from "vue-router";
import { GostJahrgang } from "@svws-nrw/svws-core-ts";
import { mainApp } from "~/apps/Main";

const SGostFachwahlen = () => import("~/components/gost/fachwahlen/SGostFachwahlen.vue");

export class RouteGostFachwahlen extends RouteNode<unknown, RouteGost> {

	public constructor() {
		super("gost_fachwahlen", "fachwahlen", SGostFachwahlen);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Fachwahlen";
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

}

export const routeGostFachwahlen = new RouteGostFachwahlen();
