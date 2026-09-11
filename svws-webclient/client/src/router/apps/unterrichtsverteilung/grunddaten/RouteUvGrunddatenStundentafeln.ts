import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import SUvGrunddatenStundentafeln from "~/components/unterrichtsverteilung/grunddaten/stundentafeln/SUvGrunddatenStundentafeln.vue";
import SUvGrunddatenStundentafelnAuswahl from "~/components/unterrichtsverteilung/grunddaten/stundentafeln/SUvGrunddatenStundentafelnAuswahl.vue";
import { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteNode } from "~/router/RouteNode";
import { uvStateImpl } from '~/states/UvStateImpl';

import { RouteDataUvGrunddatenStundentafeln } from "./RouteDataUvGrunddatenStundentafeln";

export class RouteUvGrunddatenStundentafeln extends RouteNode<RouteDataUvGrunddatenStundentafeln, RouteUv> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "uv.grunddaten.stundentafeln", String.raw`stundentafeln/:idStundentafel(\d+)?`, SUvGrunddatenStundentafeln, new RouteDataUvGrunddatenStundentafeln());
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Stundentafeln";
		this.isHidden = (params?: RouteParams) => RouteUv.tabsCheckHidden(true, false, params);
		super.setView("auswahl", SUvGrunddatenStundentafelnAuswahl, () => this.getAuswahlProps());
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams,
		isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		const items = uvStateImpl.uvManager.stundentafelGetMengeAsList();
		this.data.setAuswahlFromRoute(RouteUv.resolveAuswahl(to_params.idStundentafel, items, item => item.id));
		return super.update(to, to_params, from, from_params, isEntering, redirected);
	}

	public getAuswahlProps() {
		return {
			auswahl: this.data.auswahl,
			gotoStundentafel: this.data.gotoStundentafel,
		};
	}

	public getProps() {
		return {
			stundentafel: this.data.auswahl,
			gotoFach: this.data.gotoFach,
		};
	}

}

export const routeUvGrunddatenStundentafeln = new RouteUvGrunddatenStundentafeln();
