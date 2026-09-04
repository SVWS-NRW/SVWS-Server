import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ConfigElement } from "@ui/utils/Config";
import type { RouteLocationNormalized, RouteParams } from "vue-router";
import type { StundenplanKlasseProps } from "~/components/stundenplan/klasse/SStundenplanKlasseProps";
import { RouteNode } from "~/router/RouteNode";
import { RouteStundenplan, routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import { configStateImpl } from "~/states/ConfigStateImpl";

const SStundenplanKlasse = () => import("~/components/stundenplan/klasse/SStundenplanKlasse.vue");

export class RouteStundenplanKlasse extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
		], "stundenplan.klasse", "klasse", SStundenplanKlasse);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klassen";
		this.isHidden = (params?: RouteParams) => RouteStundenplan.katalogeCheckHidden(false, this, params);
		configStateImpl.config.addElements([
			new ConfigElement("stundenplan.klassen.doppelstundenmodus", "user", 'false'),
		]);
	}

	public getProps(to: RouteLocationNormalized): StundenplanKlasseProps {
		return {
			stundenplanManager: () => routeStundenplan.data.manager.daten(),
			patchUnterrichte: routeStundenplan.data.patchUnterrichte,
			addUnterrichte: routeStundenplan.data.addUnterrichte,
			removeUnterrichte: routeStundenplan.data.removeUnterrichte,
			mergeUnterrichte: routeStundenplan.data.mergeUnterrichte,
			// Config
			doppelstundenmodus: () => routeStundenplan.data.doppelstundenmodus,
			setDoppelstundenmodus: routeStundenplan.data.setDoppelstundenmodus,
		};
	}

}

export const routeStundenplanKlasse = new RouteStundenplanKlasse();

