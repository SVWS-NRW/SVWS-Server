import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteParams } from "vue-router";
import { routeAnkreuzkompetenzenDaten } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzenDaten";
import { routeAnkreuzkompetenzenNeu } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzenNeu";
import { routeAnkreuzkompetenzenGruppenprozesse } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzenGruppenprozesse";
import { RouteDataAnkreuzkompetenzen } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteDataAnkreuzkompetenzen";
import AnkreuzkompetenzenApp from "~/components/schule/kataloge/ankreuzkompetenzen/AnkreuzkompetenzenApp.vue";
import AnkreuzkompetenzenAuswahl from "~/components/schule/kataloge/ankreuzkompetenzen/AnkreuzkompetenzenAuswahl.vue";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { AnkreuzkompetenzenListeManager } from "@ui/ui/manager/kataloge/AnkreuzkompetenzenListeManager";

export class RouteAnkreuzkompetenzen extends RouteAuswahlNode<AnkreuzkompetenzenListeManager, RouteDataAnkreuzkompetenzen, RouteApp> {
	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.ankreuzkompetenzen",
			"schule/ankreuzkompetenzen/:id(\\d+)?", AnkreuzkompetenzenApp, AnkreuzkompetenzenAuswahl, new RouteDataAnkreuzkompetenzen());
		super.mode = ServerMode.STABLE;
		super.text = "Ankreuzkompetenzen";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeAnkreuzkompetenzenDaten,
			routeAnkreuzkompetenzenNeu,
			routeAnkreuzkompetenzenGruppenprozesse,
		];
		super.defaultChild = routeAnkreuzkompetenzenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeAnkreuzkompetenzen = new RouteAnkreuzkompetenzen();
