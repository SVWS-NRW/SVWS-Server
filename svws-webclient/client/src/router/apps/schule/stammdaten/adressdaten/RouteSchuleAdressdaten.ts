import { RouteNode } from "~/router/RouteNode";
import type { RouteSchuleStammdaten } from "~/router/apps/schule/stammdaten/RouteSchuleStammdaten";
import { RouteDataSchuleAdressdaten } from "~/router/apps/schule/stammdaten/adressdaten/RouteDataSchuleAdressdaten";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { SchuleAdressdatenProps } from "~/components/schule/stammdaten/adressdaten/SchuleAdressdatenProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SchuleAdressdaten = () => import("~/components/schule/stammdaten/adressdaten/SchuleAdressdaten.vue");

export class RouteSchuleAdressdaten extends RouteNode<any, RouteSchuleStammdaten> {

	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KEINE],
			"schule.stammdaten.adressdaten",
			"adressdaten",
			SchuleAdressdaten,
			new RouteDataSchuleAdressdaten()
		);
		super.mode = ServerMode.STABLE;
		super.text = "Adressdaten";
		super.propHandler = (route) => this.getProps(route);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await this.data.ladeDaten();
		}
	}

	public getProps(to: RouteLocationNormalized): SchuleAdressdatenProps {
		return {
			patch: routeSchuleAdressdaten.data.patch,
			getListTeilstandorte: () => routeSchuleAdressdaten.data.getListTeilstandorte,
			addTeilstandorteintrag: routeSchuleAdressdaten.data.addTeilstandorteintrag,
			patchTeilstandorteintrag: routeSchuleAdressdaten.data.patchTeilstandorteintrag,
			deleteTeilstandorteintraege: routeSchuleAdressdaten.data.deleteTeilstandorteintraege,
		};
	}

}

export const routeSchuleAdressdaten = new RouteSchuleAdressdaten();
