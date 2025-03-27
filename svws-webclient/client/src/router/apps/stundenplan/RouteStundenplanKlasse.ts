import type { RouteLocationNormalized, RouteParams } from "vue-router";
import type { StundenplanKlasseProps } from "~/components/stundenplan/klasse/SStundenplanKlasseProps";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { RouteStundenplan, routeStundenplan} from "~/router/apps/stundenplan/RouteStundenplan";
import { api } from "~/router/Api";

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
	}

	public getProps(to: RouteLocationNormalized): StundenplanKlasseProps {
		return {
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			stundenplanManager: () => routeStundenplan.data.manager.daten(),
			patchUnterrichte: routeStundenplan.data.patchUnterrichte,
			addUnterrichte: routeStundenplan.data.addUnterrichte,
			removeUnterrichte: routeStundenplan.data.removeUnterrichte,
			mergeUnterrichte: routeStundenplan.data.mergeUnterrichte,
		};
	}

}

export const routeStundenplanKlasse = new RouteStundenplanKlasse();

