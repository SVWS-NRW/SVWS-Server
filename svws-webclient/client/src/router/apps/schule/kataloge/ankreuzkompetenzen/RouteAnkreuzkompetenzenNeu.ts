import { RouteNode } from "~/router/RouteNode";
import type { RouteLocationNormalized } from "vue-router";
import { RouteManager } from "~/router/RouteManager";
import type { RouteAnkreuzkompetenzen } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzen";
import { routeAnkreuzkompetenzen } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzen";
import type { AnkreuzkompetenzenNeuProps } from "~/components/schule/kataloge/ankreuzkompetenzen/AnkreuzkompetenzenNeuProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";


const AnkreuzkompetenzenNeu = () => import("~/components/schule/kataloge/ankreuzkompetenzen/AnkreuzkompetenzenNeu.vue");

export class RouteAnkreuzkompetenzenNeu extends RouteNode<any, RouteAnkreuzkompetenzen> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.ankreuzkompetenzen.neu", "neu", AnkreuzkompetenzenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Ankreuzkompetenzen Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): AnkreuzkompetenzenNeuProps {
		return {
			manager: () => routeAnkreuzkompetenzen.data.manager,
			addAnkreuzkompetenz: routeAnkreuzkompetenzen.data.addAnkreuzkompetenz,
			addJahrgaengezuordnungen: routeAnkreuzkompetenzen.data.addJahrgaengezuordnungen,
			gotoDefaultView: routeAnkreuzkompetenzen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}
export const routeAnkreuzkompetenzenNeu = new RouteAnkreuzkompetenzenNeu();
