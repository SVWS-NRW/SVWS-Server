import { type RouteApp, routeApp } from "@wenom/router/apps/RouteApp";
import { RouteNode } from "@wenom/router/RouteNode";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import type { EnmKlassenleitungAuswahlProps } from "@ui/components/enm/EnmKlassenleitungAuswahlProps";

const EnmKonferenzuebersicht = () => import("@ui/components/enm/EnmKonferenzuebersicht.vue");
const EnmSchuelerFaecherAuswahl = () => import("@ui/components/enm/EnmSchuelerFaecherAuswahl.vue");

export class RouteKonferenzuebersicht extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), "konferenzuebersicht", "konferenzuebersicht", EnmKonferenzuebersicht);
		super.mode = ServerMode.STABLE;
		super.propHandler = () => this.getProps();
		super.text = "Konferenzübersicht";
		super.setView("liste", EnmSchuelerFaecherAuswahl, () => this.getAuswahlProps());
	}

	public getProps() {
		return {
			ladeManagerNotenuebersicht: () => routeApp.data.ladeManagerNotenuebersicht(),
			auswahlAnsicht: () => routeApp.data.auswahlAnsicht,
			auswahlEinzel: () => routeApp.data.auswahlKlasse,
			auswahlMehrfach: () => routeApp.data.auswahlKlassenNurMehrfachauswahl,
		};
	}

	public getAuswahlProps(): EnmKlassenleitungAuswahlProps {
		return {
			enmManager: () => routeApp.data.manager,
			setAuswahlEinzel: routeApp.data.setAuswahlKlasse,
			auswahlEinzel: () => routeApp.data.auswahlKlasse,
			setAuswahlMehrfach: routeApp.data.setAuswahlKlassen,
			auswahlMehrfach: () => routeApp.data.auswahlKlassenNurMehrfachauswahl,
		};
	}

}

export const routeKonferenzuebersicht = new RouteKonferenzuebersicht();
