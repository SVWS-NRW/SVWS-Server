import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";

import type { SchuleAppProps } from "~/components/schule/schulbezogen/stammdaten/SSchuleAppProps";

import type { RouteSchule} from "../../RouteSchule";
import { routeSchule } from "../../RouteSchule";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";

const SSchuleStammdaten = () => import("~/components/schule/schulbezogen/stammdaten/SSchuleStammdaten.vue")

export class RouteSchuleStammdaten extends RouteNode<any, RouteSchule> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.stammdaten", "stammdaten", SSchuleStammdaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stammdaten der Schule";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await routeSchule.data.ladeDaten();
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await routeSchule.data.entferneDaten();
	}

	public getProps(to: RouteLocationNormalized): SchuleAppProps {
		return {
			schule: () => api.schuleStammdaten,
			patch: routeSchule.data.patch,
			smptServerKonfiguration: () => routeSchule.data.smtpServerKonfiguration,
			patchSMTPServerKonfiguration: routeSchule.data.patchSMTServerKonfiguration,
			benutzerIstAdmin: api.benutzerIstAdmin,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeSchuleStammdaten = new RouteSchuleStammdaten();
