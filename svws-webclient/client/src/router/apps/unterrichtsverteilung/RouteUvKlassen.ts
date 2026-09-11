import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import SUvKlassen from "~/components/unterrichtsverteilung/klassen/SUvKlassen.vue";
import SUvKlassenAuswahl from "~/components/unterrichtsverteilung/klassen/SUvKlassenAuswahl.vue";
import { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteNode } from "~/router/RouteNode";
import { schuleStateImpl as schuleState } from "~/states/SchuleStateImpl";
import { uvStateImpl } from '~/states/UvStateImpl';

import { RouteDataUvKlassen } from "./RouteDataUvKlassen";

export class RouteUvKlassen extends RouteNode<RouteDataUvKlassen, RouteUv> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "uv.klassen", String.raw`klassen/:idKlasse(\d+)?`, SUvKlassen, new RouteDataUvKlassen());
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Klassen";
		this.isHidden = (params?: RouteParams) => RouteUv.tabsCheckHidden(false, true, params);
		super.setView("auswahl", SUvKlassenAuswahl, () => this.getAuswahlProps());
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined,
		from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		const plan = uvStateImpl.planungsabschnitt;
		const items = plan === null || plan.id === -1 ? [] : uvStateImpl.uvManager.klasseGetMengeByPlanungsabschnitt(plan);
		this.data.setAuswahlFromRoute(RouteUv.resolveAuswahl(to_params.idKlasse, items, item => item.id));
		return super.update(to, to_params, from, from_params, isEntering, redirected);
	}

	public getAuswahlProps() {
		return {
			auswahl: this.data.auswahl,
			gotoKlasse: this.data.gotoKlasse,
		};
	}

	public getProps() {
		return {
			klasse: this.data.auswahl,
			schuljahr: schuleState.abschnitt.schuljahr,
			gotoSchuelergruppe: this.data.gotoSchuelergruppe,
			gotoSchueler: this.data.gotoSchueler,
			gotoKlasse: this.data.gotoKlasse,
			gotoLehrer: this.data.gotoLehrer,
			gotoStundentafel: this.data.gotoStundentafel,
		};
	}

}

export const routeUvKlassen = new RouteUvKlassen();
