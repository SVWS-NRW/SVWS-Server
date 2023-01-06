import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";

const SGostFaecher = () => import("~/components/gost/faecher/SGostFaecher.vue");

export class RouteGostFaecher extends RouteNode<unknown> {

	public constructor() {
		super("gost_faecher", "faecher", SGostFaecher);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Fächer";
	}

}

export const routeGostFaecher = new RouteGostFaecher();
