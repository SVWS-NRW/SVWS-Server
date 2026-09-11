import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import SUvKurse from "~/components/unterrichtsverteilung/kurse/SUvKurse.vue";
import SUvKurseAuswahl from "~/components/unterrichtsverteilung/kurse/SUvKurseAuswahl.vue";
import { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteNode } from "~/router/RouteNode";
import { schuleStateImpl as schuleState } from "~/states/SchuleStateImpl";
import { uvStateImpl } from '~/states/UvStateImpl';

import { RouteDataUvKurse } from "./RouteDataUvKurse";

export class RouteUvKurse extends RouteNode<RouteDataUvKurse, RouteUv> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "uv.kurse", String.raw`kurse/:idKurs(\d+)?`, SUvKurse, new RouteDataUvKurse());
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Kurse";
		this.isHidden = (params?: RouteParams) => RouteUv.tabsCheckHidden(false, true, params);
		super.setView("auswahl", SUvKurseAuswahl, () => this.getAuswahlProps());
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined,
		from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		const plan = uvStateImpl.planungsabschnitt;
		const items = plan === null || plan.id === -1 ? [] : uvStateImpl.uvManager.kursGetMengeByPlanungsabschnitt(plan);
		this.data.setAuswahlFromRoute(RouteUv.resolveAuswahl(to_params.idKurs, items, item => item.id));
		return super.update(to, to_params, from, from_params, isEntering, redirected);
	}

	public getAuswahlProps() {
		return {
			auswahl: this.data.auswahl,
			gotoKurs: this.data.gotoKurs,
		};
	}

	public getProps() {
		return {
			kurs: this.data.auswahl,
			schuljahr: schuleState.abschnitt.schuljahr,
			gotoSchuelergruppe: this.data.gotoSchuelergruppe,
			gotoSchueler: this.data.gotoSchueler,
			gotoKlasse: this.data.gotoKlasse,
		};
	}

}

export const routeUvKurse = new RouteUvKurse();
