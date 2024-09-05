import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeStundenplan, type RouteStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";

import type { StundenplanDatenProps } from "~/components/stundenplan/daten/SStundenplanDatenProps";
import { routeApp } from "../RouteApp";
import { api } from "~/router/Api";

const SStundenplanDaten = () => import("~/components/stundenplan/daten/SStundenplanDaten.vue");

export class RouteStundenplanDaten extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
		], "stundenplan.daten", "daten", SStundenplanDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Grunddaten";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): StundenplanDatenProps {
		return {
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			stundenplanManager: () => routeStundenplan.data.stundenplanManager,
			patch: routeStundenplan.data.patch,
			patchRaum: routeStundenplan.data.patchRaum,
			addRaum: routeStundenplan.data.addRaum,
			removeRaeume: routeStundenplan.data.removeRaeume,
			importRaeume: routeStundenplan.data.importRaeume,
			listRaeume: () => routeStundenplan.data.listRaeume,
			listJahrgaenge: routeStundenplan.data.listJahrgaenge,
			addJahrgang: routeStundenplan.data.addJahrgang,
			removeJahrgang: routeStundenplan.data.removeJahrgang,
			patchPausenzeit: routeStundenplan.data.patchPausenzeit,
			addPausenzeiten: routeStundenplan.data.addPausenzeiten,
			removePausenzeiten: routeStundenplan.data.removePausenzeiten,
			importPausenzeiten: routeStundenplan.data.importPausenzeiten,
			listPausenzeiten: () => routeStundenplan.data.listPausenzeiten,
			patchAufsichtsbereich: routeStundenplan.data.patchAufsichtsbereich,
			addAufsichtsbereich: routeStundenplan.data.addAufsichtsbereich,
			removeAufsichtsbereiche: routeStundenplan.data.removeAufsichtsbereiche,
			importAufsichtsbereiche: routeStundenplan.data.importAufsichtsbereiche,
			listAufsichtsbereiche: () => routeStundenplan.data.listAufsichtsbereiche,
			gotoKatalog: routeStundenplan.data.gotoKatalog,
		};
	}

}

export const routeStundenplanDaten = new RouteStundenplanDaten();

