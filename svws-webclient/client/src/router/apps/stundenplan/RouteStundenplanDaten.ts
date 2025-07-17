import type { RouteLocationNormalized, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { RouteStundenplan, routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";

import type { StundenplanDatenProps } from "~/components/stundenplan/daten/SStundenplanDatenProps";
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
		this.isHidden = (params?: RouteParams) => RouteStundenplan.katalogeCheckHidden(false, this, params);
	}

	public getProps(to: RouteLocationNormalized): StundenplanDatenProps {
		return {
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeStundenplan.data.manager,
			patch: routeStundenplan.data.patch,
			patchRaum: routeStundenplan.data.patchRaum,
			addRaum: routeStundenplan.data.addRaum,
			removeRaeume: routeStundenplan.data.removeRaeume,
			raeumeSyncToStundenplan: routeStundenplan.data.raeumeSyncToStundenplan,
			raeumeSyncToVorlage: routeStundenplan.data.raeumeSyncToVorlage,
			listRaeume: () => routeStundenplan.data.listRaeume,
			addJahrgang: routeStundenplan.data.addJahrgang,
			removeJahrgang: routeStundenplan.data.removeJahrgang,
			listJahrgaenge: routeStundenplan.data.listJahrgaenge,
			patchPausenzeit: routeStundenplan.data.patchPausenzeit,
			addPausenzeiten: routeStundenplan.data.addPausenzeiten,
			removePausenzeiten: routeStundenplan.data.removePausenzeiten,
			pausenzeitenSyncToStundenplan: routeStundenplan.data.pausenzeitenSyncToStundenplan,
			pausenzeitenSyncToVorlage: routeStundenplan.data.pausenzeitenSyncToVorlage,
			listPausenzeiten: () => routeStundenplan.data.listPausenzeiten,
			patchAufsichtsbereich: routeStundenplan.data.patchAufsichtsbereich,
			addAufsichtsbereich: routeStundenplan.data.addAufsichtsbereich,
			removeAufsichtsbereiche: routeStundenplan.data.removeAufsichtsbereiche,
			aufsichtsbereicheSyncToVorlage: routeStundenplan.data.aufsichtsbereicheSyncToVorlage,
			aufsichtsbereicheSyncToStundenplan: routeStundenplan.data.aufsichtsbereicheSyncToStundenplan,
			listAufsichtsbereiche: () => routeStundenplan.data.listAufsichtsbereiche,
			gotoKatalog: routeStundenplan.data.gotoKatalog,
			setSettingsDefaults: routeStundenplan.data.setSettingsDefaults,
		};
	}

}

export const routeStundenplanDaten = new RouteStundenplanDaten();

