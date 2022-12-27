import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { routeGost, RouteGost } from "~/router/apps/RouteGost";

const SGostFachwahlen = () => import("~/components/gost/fachwahlen/SGostFachwahlen.vue");

export class RouteGostFachwahlen extends RouteNode<unknown> {

	public constructor() {
		super("gost_fachwahlen", "fachwahlen", SGostFachwahlen);
		super.propHandler = (route) => RouteGost.getPropsByAuswahlAbiturjahr(route, mainApp.apps.gost.auswahl);
		super.text = "Fachwahlen";
		this.isHidden = () => {
			return (routeGost.data.item === undefined) || (routeGost.data.item.abiturjahr === -1);
		}
	}

}

export const routeGostFachwahlen = new RouteGostFachwahlen();
