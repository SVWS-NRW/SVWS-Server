import { RouteNode } from "~/router/RouteNode";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";

const SGostFaecher = () => import("~/components/gost/faecher/SGostFaecher.vue");

export class RouteGostFaecher extends RouteNode<unknown, RouteGost> {

	public constructor() {
		super("gost_faecher", "faecher", SGostFaecher);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Fächer";
	}

}

export const routeGostFaecher = new RouteGostFaecher();
