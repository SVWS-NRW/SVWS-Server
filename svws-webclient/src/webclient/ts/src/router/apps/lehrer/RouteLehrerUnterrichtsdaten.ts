import { defineAsyncComponent } from "vue";
import { injectMainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";

const SLehrerUnterrichtsdaten = () => import("~/components/lehrer/unterrichtsdaten/SLehrerUnterrichtsdaten.vue");

	export class RouteLehrerUnterrichtsdaten extends RouteNode<unknown> {

	public constructor() {
		super("lehrer_unterrichtsdaten", "unterrichtsdaten", SLehrerUnterrichtsdaten);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, injectMainApp().apps.lehrer.auswahl);
		super.text = "Unterrichtsdaten";
	}

}

export const routeLehrerUnterrichtsdaten = new RouteLehrerUnterrichtsdaten();
