import { RouteNode } from "~/router/RouteNode";
import { routeLehrer } from "../RouteLehrer";

const SLehrerUnterrichtsdaten = () => import("~/components/lehrer/unterrichtsdaten/SLehrerUnterrichtsdaten.vue");

	export class RouteLehrerUnterrichtsdaten extends RouteNode<unknown> {

	public constructor() {
		super("lehrer_unterrichtsdaten", "unterrichtsdaten", SLehrerUnterrichtsdaten);
		super.propHandler = (route) => routeLehrer.getProps(route);
		super.text = "Unterrichtsdaten";
	}

}

export const routeLehrerUnterrichtsdaten = new RouteLehrerUnterrichtsdaten();
