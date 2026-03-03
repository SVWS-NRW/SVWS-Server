import { RouteNode } from "~/router/RouteNode";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { ViewType } from "@ui";
import type { RouteLocationNormalized } from "vue-router";
import { RouteManager } from "~/router/RouteManager";
import { api } from "~/router/Api";
import type { RouteAnkreuzkompetenzen } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzen";
import { routeAnkreuzkompetenzen } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzen";
import type { AnkreuzkompetenzenNeuProps } from "~/components/schule/kataloge/ankreuzkompetenzen/AnkreuzkompetenzenNeuProps";


const AnkreuzkompetenzenNeu = () => import("~/components/schule/kataloge/ankreuzkompetenzen/AnkreuzkompetenzenNeu.vue");

export class RouteAnkreuzkompetenzenNeu extends RouteNode<any, RouteAnkreuzkompetenzen> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.ankreuzkompetenzen.neu", "neu", AnkreuzkompetenzenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Ankreuzkompetenzen Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): AnkreuzkompetenzenNeuProps {
		return {
			manager: () => routeAnkreuzkompetenzen.data.manager,
			addAnkreuzkompetenz: routeAnkreuzkompetenzen.data.addAnkreuzkompetenz,
			addJahrgaengezuordnungen: routeAnkreuzkompetenzen.data.addJahrgaengezuordnungen,
			schuljahr: api.abschnitt.schuljahr,
			schulform: api.schulform,
			gotoDefaultView: routeAnkreuzkompetenzen.data.gotoDefaultView,
			benutzerKompetenzen: api.benutzerKompetenzen,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}
export const routeAnkreuzkompetenzenNeu = new RouteAnkreuzkompetenzenNeu();
