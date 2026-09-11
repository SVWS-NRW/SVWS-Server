import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import SUvGrunddatenRaumgruppen from "~/components/unterrichtsverteilung/grunddaten/raeume/SUvGrunddatenRaumgruppen.vue";
import SUvGrunddatenRaumgruppenAuswahl from "~/components/unterrichtsverteilung/grunddaten/raeume/SUvGrunddatenRaumgruppenAuswahl.vue";
import type { RouteUvGrunddatenRaeumeGruppen } from "~/router/apps/unterrichtsverteilung/grunddaten/RouteUvGrunddatenRaeumeGruppen";
import { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteNode } from "~/router/RouteNode";
import { uvStateImpl } from '~/states/UvStateImpl';

import { RouteDataUvGrunddatenRaumgruppen } from "./RouteDataUvGrunddatenRaumgruppen";

export class RouteUvGrunddatenRaumgruppen extends RouteNode<RouteDataUvGrunddatenRaumgruppen, RouteUvGrunddatenRaeumeGruppen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "uv.grunddaten.raumgruppen", String.raw`raumgruppen/:idRaumgruppe(\d+)?`, SUvGrunddatenRaumgruppen, new RouteDataUvGrunddatenRaumgruppen());
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Raumgruppen";
		super.setView("auswahl", SUvGrunddatenRaumgruppenAuswahl, () => this.getAuswahlProps());
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams,
		isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		const items = uvStateImpl.uvManager.raumgruppeGetMengeAsList();
		this.data.setAuswahlFromRoute(RouteUv.resolveAuswahl(to_params.idRaumgruppe, items, item => item.id));
		return super.update(to, to_params, from, from_params, isEntering, redirected);
	}

	public getAuswahlProps() {
		return {
			auswahl: this.data.auswahl,
			gotoRaumgruppe: this.data.gotoRaumgruppe,
		};
	}

	public getProps() {
		return {
			raumgruppe: this.data.auswahl,
		};
	}

}

export const routeUvGrunddatenRaumgruppen = new RouteUvGrunddatenRaumgruppen();
