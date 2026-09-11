import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import SUvUnterrichte from "~/components/unterrichtsverteilung/unterrichte/SUvUnterrichte.vue";
import SUvUnterrichteAuswahl from "~/components/unterrichtsverteilung/unterrichte/SUvUnterrichteAuswahl.vue";
import { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteNode } from "~/router/RouteNode";
import { uvStateImpl } from '~/states/UvStateImpl';

import { RouteDataUvUnterrichte } from "./RouteDataUvUnterrichte";

export class RouteUvUnterrichte extends RouteNode<RouteDataUvUnterrichte, RouteUv> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "uv.unterrichte", String.raw`unterrichte/:idUnterricht(\d+)?`, SUvUnterrichte, new RouteDataUvUnterrichte());
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Unterrichte";
		this.isHidden = (params?: RouteParams) => RouteUv.tabsCheckHidden(false, true, params);
		super.setView("auswahl", SUvUnterrichteAuswahl, () => this.getAuswahlProps());
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined,
		from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		const plan = uvStateImpl.planungsabschnitt;
		const items = plan === null || plan.id === -1 ? [] : uvStateImpl.uvManager.unterrichtGetMengeByPlanungsabschnitt(plan);
		this.data.setAuswahlFromRoute(RouteUv.resolveAuswahl(to_params.idUnterricht, items, item => item.id));
		return super.update(to, to_params, from, from_params, isEntering, redirected);
	}

	public getAuswahlProps() {
		return {
			auswahl: this.data.auswahl,
			gotoUnterricht: this.data.gotoUnterricht,
		};
	}

	public getProps() {
		return {
			unterricht: this.data.auswahl,
			gotoLerngruppe: this.data.gotoLerngruppe,
			gotoLehrer: this.data.gotoLehrer,
			gotoRaum: this.data.gotoRaum,
		};
	}

}

export const routeUvUnterrichte = new RouteUvUnterrichte();
