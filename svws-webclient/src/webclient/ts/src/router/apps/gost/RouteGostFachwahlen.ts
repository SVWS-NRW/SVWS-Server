import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";

const SGostFachwahlen = () => import("~/components/gost/fachwahlen/SGostFachwahlen.vue");

export class RouteGostFachwahlen extends RouteNode<unknown> {

	public constructor() {
		super("gost_fachwahlen", "fachwahlen", SGostFachwahlen);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Fachwahlen";
		this.isHidden = () => {
			return (routeGost.data.item.value === undefined) || (routeGost.data.item.value.abiturjahr === -1);
		}
	}

}

export const routeGostFachwahlen = new RouteGostFachwahlen();
