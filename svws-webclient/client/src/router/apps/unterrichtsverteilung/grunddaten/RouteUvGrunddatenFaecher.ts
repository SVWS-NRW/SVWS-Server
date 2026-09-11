import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import SUvGrunddatenFaecher from "~/components/unterrichtsverteilung/grunddaten/faecher/SUvGrunddatenFaecher.vue";
import SUvGrunddatenFaecherAuswahl from "~/components/unterrichtsverteilung/grunddaten/faecher/SUvGrunddatenFaecherAuswahl.vue";
import { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteNode } from "~/router/RouteNode";
import { uvStateImpl } from '~/states/UvStateImpl';

import { RouteDataUvGrunddatenFaecher } from "./RouteDataUvGrunddatenFaecher";

export class RouteUvGrunddatenFaecher extends RouteNode<RouteDataUvGrunddatenFaecher, RouteUv> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "uv.grunddaten.faecher", String.raw`faecher/:idFach(\d+)?`, SUvGrunddatenFaecher, new RouteDataUvGrunddatenFaecher());
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Fächer";
		this.isHidden = (params?: RouteParams) => RouteUv.tabsCheckHidden(true, false, params);
		super.setView("auswahl", SUvGrunddatenFaecherAuswahl, () => this.getAuswahlProps());
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams,
		isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		const items = uvStateImpl.uvManager.fachGetMengeAsList();
		this.data.setAuswahlFromRoute(RouteUv.resolveAuswahl(to_params.idFach, items, item => item.id));
		return super.update(to, to_params, from, from_params, isEntering, redirected);
	}

	public getAuswahlProps() {
		return {
			auswahl: this.data.auswahl,
			konfliktMitFach: this.data.konfliktMitFachRef,
			gotoFach: this.data.gotoFach,
		};
	}

	public getProps() {
		return {
			fach: this.data.auswahl,
			konfliktMitFach: this.data.konfliktMitFachRef,
			gotoLehrer: this.data.gotoLehrer,
		};
	}

}

export const routeUvGrunddatenFaecher = new RouteUvGrunddatenFaecher();
