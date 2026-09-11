import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import SUvSchienen from "~/components/unterrichtsverteilung/schienen/SUvSchienen.vue";
import SUvSchienenAuswahl from "~/components/unterrichtsverteilung/schienen/SUvSchienenAuswahl.vue";
import { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteNode } from "~/router/RouteNode";
import { uvStateImpl } from '~/states/UvStateImpl';

import { RouteDataUvSchienen } from "./RouteDataUvSchienen";

export class RouteUvSchienen extends RouteNode<RouteDataUvSchienen, RouteUv> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "uv.schienen", String.raw`schienen/:idSchiene(\d+)?`, SUvSchienen, new RouteDataUvSchienen());
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Schienen";
		this.isHidden = (params?: RouteParams) => RouteUv.tabsCheckHidden(false, true, params);
		super.setView("auswahl", SUvSchienenAuswahl, () => this.getAuswahlProps());
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined,
		from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		const plan = uvStateImpl.planungsabschnitt;
		const items = plan === null || plan.id === -1 ? [] : uvStateImpl.uvManager.schieneGetMengeByPlanungsabschnitt(plan);
		this.data.setAuswahlFromRoute(RouteUv.resolveAuswahl(to_params.idSchiene, items, item => item.id));
		return super.update(to, to_params, from, from_params, isEntering, redirected);
	}

	public getAuswahlProps() {
		return {
			auswahl: this.data.auswahl,
			gotoSchiene: this.data.gotoSchiene,
		};
	}

	public getProps() {
		return {
			schiene: this.data.auswahl,
			gotoLerngruppe: this.data.gotoLerngruppe,
		};
	}

}

export const routeUvSchienen = new RouteUvSchienen();
