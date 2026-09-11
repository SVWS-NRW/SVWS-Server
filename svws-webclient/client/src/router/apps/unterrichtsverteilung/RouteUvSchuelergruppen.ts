import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import SUvSchuelergruppen from "~/components/unterrichtsverteilung/schuelergruppen/SUvSchuelergruppen.vue";
import SUvSchuelergruppenAuswahl from "~/components/unterrichtsverteilung/schuelergruppen/SUvSchuelergruppenAuswahl.vue";
import { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteNode } from "~/router/RouteNode";
import { uvStateImpl } from '~/states/UvStateImpl';

import { RouteDataUvSchuelergruppen } from "./RouteDataUvSchuelergruppen";

export class RouteUvSchuelergruppen extends RouteNode<RouteDataUvSchuelergruppen, RouteUv> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "uv.schuelergruppen", String.raw`schuelergruppen/:idSchuelergruppe(\d+)?`, SUvSchuelergruppen, new RouteDataUvSchuelergruppen());
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Schülergruppen";
		this.isHidden = (params?: RouteParams) => RouteUv.tabsCheckHidden(false, true, params);
		super.setView("auswahl", SUvSchuelergruppenAuswahl, () => this.getAuswahlProps());
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined,
		from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		const plan = uvStateImpl.planungsabschnitt;
		const items = plan === null || plan.id === -1 ? [] : uvStateImpl.uvManager.schuelergruppeGetMengeByPlanungsabschnitt(plan);
		this.data.setAuswahlFromRoute(RouteUv.resolveAuswahl(to_params.idSchuelergruppe, items, item => item.id));
		return super.update(to, to_params, from, from_params, isEntering, redirected);
	}

	public getAuswahlProps() {
		return {
			auswahl: this.data.auswahl,
			gotoSchuelergruppe: this.data.gotoSchuelergruppe,
		};
	}

	public getProps() {
		return {
			schuelergruppe: this.data.auswahl,
			gotoSchueler: this.data.gotoSchueler,
			gotoKlasse: this.data.gotoKlasse,
			gotoLerngruppe: this.data.gotoLerngruppe,
		};
	}

}

export const routeUvSchuelergruppen = new RouteUvSchuelergruppen();
