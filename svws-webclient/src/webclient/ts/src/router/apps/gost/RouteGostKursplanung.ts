import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { routeGost, RouteGost } from "~/router/apps/RouteGost";

const SGostKursplanung = () => import("~/components/gost/kursplanung/SGostKursplanung.vue");

export class RouteGostKursplanung extends RouteNode<unknown> {

	public constructor() {
		super("gost_kursplanung", "kursplanung", SGostKursplanung);
		super.propHandler = (route) => RouteGost.getPropsByAuswahlAbiturjahr(route, mainApp.apps.gost.auswahl);
		super.text = "Kursplanung";
		this.isHidden = () => {
			return (routeGost.data.item === undefined) || (routeGost.data.item.abiturjahr === -1);
		}
	}

}

export const routeGostKursplanung = new RouteGostKursplanung();
