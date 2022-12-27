import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteGost } from "~/router/apps/RouteGost";

const SGostFaecher = () => import("~/components/gost/faecher/SGostFaecher.vue");

export class RouteGostFaecher extends RouteNode<unknown> {

	public constructor() {
		super("gost_faecher", "faecher", SGostFaecher);
		super.propHandler = (route) => RouteGost.getPropsByAuswahlAbiturjahr(route, mainApp.apps.gost.auswahl);
		super.text = "Fächer";
	}

}

export const routeGostFaecher = new RouteGostFaecher();
