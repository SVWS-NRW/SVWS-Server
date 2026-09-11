import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import SUvSchueler from "~/components/unterrichtsverteilung/schueler/SUvSchueler.vue";
import SUvSchuelerAuswahl from "~/components/unterrichtsverteilung/schueler/SUvSchuelerAuswahl.vue";
import { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteNode } from "~/router/RouteNode";
import { uvStateImpl } from '~/states/UvStateImpl';

import { RouteDataUvSchueler } from "./RouteDataUvSchueler";

export class RouteUvSchueler extends RouteNode<RouteDataUvSchueler, RouteUv> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "uv.schueler", String.raw`schueler/:idSchueler(\d+)?`, SUvSchueler, new RouteDataUvSchueler());
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Schüler";
		this.isHidden = (params?: RouteParams) => RouteUv.tabsCheckHidden(false, true, params);
		super.setView("auswahl", SUvSchuelerAuswahl, () => this.getAuswahlProps());
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined,
		from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		const plan = uvStateImpl.planungsabschnitt;
		const items = plan === null || plan.id === -1 ? [] : uvStateImpl.uvManager.planungsabschnittSchuelerGetMengeByPlanungsabschnitt(plan);
		this.data.setAuswahlFromRoute(RouteUv.resolveAuswahl(to_params.idSchueler, items, item => item.idSchueler));
		return super.update(to, to_params, from, from_params, isEntering, redirected);
	}

	public getAuswahlProps() {
		return {
			auswahl: this.data.auswahl,
			gotoUvSchueler: this.data.gotoUvSchueler,
		};
	}

	public getProps() {
		return {
			schueler: this.data.auswahl,
			gotoSchueler: this.data.gotoSchueler,
			gotoKlasse: this.data.gotoKlasse,
		};
	}

}

export const routeUvSchueler = new RouteUvSchueler();
