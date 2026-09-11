import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import SUvGrunddatenRaeume from "~/components/unterrichtsverteilung/grunddaten/raeume/SUvGrunddatenRaeume.vue";
import SUvGrunddatenRaeumeAuswahl from "~/components/unterrichtsverteilung/grunddaten/raeume/SUvGrunddatenRaeumeAuswahl.vue";
import type { RouteUvGrunddatenRaeumeGruppen } from "~/router/apps/unterrichtsverteilung/grunddaten/RouteUvGrunddatenRaeumeGruppen";
import { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteNode } from "~/router/RouteNode";
import { uvStateImpl } from '~/states/UvStateImpl';

import { RouteDataUvGrunddatenRaeume } from "./RouteDataUvGrunddatenRaeume";

export class RouteUvGrunddatenRaeume extends RouteNode<RouteDataUvGrunddatenRaeume, RouteUvGrunddatenRaeumeGruppen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "uv.grunddaten.raeume", String.raw`raeume/:idRaum(\d+)?`, SUvGrunddatenRaeume, new RouteDataUvGrunddatenRaeume());
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Räume";
		super.setView("auswahl", SUvGrunddatenRaeumeAuswahl, () => this.getAuswahlProps());
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams,
		isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		const items = uvStateImpl.uvManager.raumGetMengeAsList();
		this.data.setAuswahlFromRoute(RouteUv.resolveAuswahl(to_params.idRaum, items, item => item.id));
		return super.update(to, to_params, from, from_params, isEntering, redirected);
	}

	public getAuswahlProps() {
		return {
			auswahl: this.data.auswahl,
			gotoRaum: this.data.gotoRaum,
		};
	}

	public getProps() {
		return {
			raum: this.data.auswahl,
			gotoRaumgruppe: this.data.gotoRaumgruppe,
		};
	}

}

export const routeUvGrunddatenRaeume = new RouteUvGrunddatenRaeume();
