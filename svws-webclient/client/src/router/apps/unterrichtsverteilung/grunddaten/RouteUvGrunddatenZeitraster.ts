import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import SUvGrunddatenZeitraster from "~/components/unterrichtsverteilung/grunddaten/zeitraster/SUvGrunddatenZeitraster.vue";
import SUvGrunddatenZeitrasterAuswahl from "~/components/unterrichtsverteilung/grunddaten/zeitraster/SUvGrunddatenZeitrasterAuswahl.vue";
import { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteNode } from "~/router/RouteNode";
import { schuleStateImpl as schuleState } from "~/states/SchuleStateImpl";
import { uvStateImpl } from '~/states/UvStateImpl';

import { RouteDataUvGrunddatenZeitraster } from "./RouteDataUvGrunddatenZeitraster";

export class RouteUvGrunddatenZeitraster extends RouteNode<RouteDataUvGrunddatenZeitraster, RouteUv> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "uv.grunddaten.zeitraster", String.raw`zeitraster/:idZeitraster(\d+)?`, SUvGrunddatenZeitraster, new RouteDataUvGrunddatenZeitraster());
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Zeitraster";
		this.isHidden = (params?: RouteParams) => RouteUv.tabsCheckHidden(true, false, params);
		super.setView("auswahl", SUvGrunddatenZeitrasterAuswahl, () => this.getAuswahlProps());
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams,
		isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		const items = uvStateImpl.uvManager.zeitrasterGetMengeAsList();
		this.data.setAuswahlFromRoute(RouteUv.resolveAuswahl(to_params.idZeitraster, items, item => item.id));
		if (isEntering) {
			await this.data.ladeZeitraster();
		}
		return super.update(to, to_params, from, from_params, isEntering, redirected);
	}

	public getAuswahlProps() {
		return {
			auswahl: this.data.auswahl,
			gotoZeitraster: this.data.gotoZeitraster,
		};
	}

	public getProps() {
		return {
			schulform: schuleState.schulform,
			zeitraster: this.data.auswahl,
			selected: this.data.selected,
			setSelection: this.data.setSelection,
		};
	}

}

export const routeUvGrunddatenZeitraster = new RouteUvGrunddatenZeitraster();
