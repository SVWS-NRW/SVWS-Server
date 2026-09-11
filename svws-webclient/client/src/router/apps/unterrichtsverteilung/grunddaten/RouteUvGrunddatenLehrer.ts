import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import SUvGrunddatenLehrer from "~/components/unterrichtsverteilung/grunddaten/lehrer/SUvGrunddatenLehrer.vue";
import SUvGrunddatenLehrerAuswahl from "~/components/unterrichtsverteilung/grunddaten/lehrer/SUvGrunddatenLehrerAuswahl.vue";
import { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteNode } from "~/router/RouteNode";
import { uvStateImpl } from '~/states/UvStateImpl';

import { RouteDataUvGrunddatenLehrer } from "./RouteDataUvGrunddatenLehrer";

export class RouteUvGrunddatenLehrer extends RouteNode<RouteDataUvGrunddatenLehrer, RouteUv> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "uv.grunddaten.lehrer", String.raw`lehrer/:idLehrer(\d+)?`, SUvGrunddatenLehrer, new RouteDataUvGrunddatenLehrer());
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Lehrer";
		this.isHidden = (params?: RouteParams) => RouteUv.tabsCheckHidden(true, true, params);
		super.setView("auswahl", SUvGrunddatenLehrerAuswahl, () => this.getAuswahlProps());
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams,
		isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		const plan = uvStateImpl.planungsabschnitt;
		const items = plan !== null && plan.id !== -1 ? uvStateImpl.uvManager.lehrerGetMengeByPlanungsabschnitt(plan) : uvStateImpl.uvManager.lehrerGetMengeAsList();
		this.data.setAuswahlFromRoute(RouteUv.resolveAuswahl(to_params.idLehrer, items, item => item.id));
		return super.update(to, to_params, from, from_params, isEntering, redirected);
	}

	public getAuswahlProps() {
		return {
			auswahl: this.data.auswahl,
			gotoUvLehrer: this.data.gotoUvLehrer,
		};
	}

	public getProps() {
		return {
			lehrer: this.data.auswahl,
			gotoLehrer: this.data.gotoLehrer,
		};
	}

}

export const routeUvGrunddatenLehrer = new RouteUvGrunddatenLehrer();
