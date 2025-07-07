import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import type { StundenplanAufsichtsbereich , DeveloperNotificationException} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";

import type { AufsichtsbereicheProps } from "~/components/stundenplan/kataloge/aufsichtsbereiche/SAufsichtsbereicheProps";
import type { AufsichtsbereicheAuswahlProps } from "~/components/stundenplan/kataloge/aufsichtsbereiche/SAufsichtsbereicheAuswahlProps";
import { RouteDataKatalogAufsichtsbereiche } from "./RouteDataKatalogAufsichtsbereiche";
import { routeError } from "~/router/error/RouteError";
import { RouteStundenplan } from "../RouteStundenplan";


const SAufsichtsbereicheAuswahl = () => import("~/components/stundenplan/kataloge/aufsichtsbereiche/SAufsichtsbereicheAuswahl.vue");
const SAufsichtsbereiche = () => import("~/components/stundenplan/kataloge/aufsichtsbereiche/SAufsichtsbereiche.vue");

export class RouteKatalogAufsichtsbereiche extends RouteNode<RouteDataKatalogAufsichtsbereiche, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.kataloge.aufsichtsbereiche", "aufsichtsbereiche/:idAufsichtsbereich(\\d+)?", SAufsichtsbereiche, new RouteDataKatalogAufsichtsbereiche());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Aufsichtsbereiche";
		this.isHidden = (params?: RouteParams) => RouteStundenplan.katalogeCheckHidden(true, this, params);
		super.setView("eintraege", SAufsichtsbereicheAuswahl, (route) => this.getAuswahlProps(route));
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idAufsichtsbereich } = RouteNode.getIntParams(to_params, ["idAufsichtsbereich"]);
			if (isEntering)
				await this.data.ladeListe();
			if (this.data.stundenplanManager.aufsichtsbereichGetMengeAsList().isEmpty())
				return;
			let eintrag: StundenplanAufsichtsbereich | undefined;
			if ((idAufsichtsbereich === undefined) && this.data.auswahl)
				return this.getRoute();
			if (idAufsichtsbereich === undefined) {
				eintrag = this.data.stundenplanManager.aufsichtsbereichGetMengeAsList().get(0);
				return this.getRoute({ id: eintrag.id });
			} else
				eintrag = this.data.stundenplanManager.aufsichtsbereichGetByIdOrException(idAufsichtsbereich);
			await this.data.setEintrag(eintrag);
		} catch (error) {
			return await routeError.getErrorRoute(error as DeveloperNotificationException);
		}
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { idAufsichtsbereich : this.data.auswahl?.id ?? undefined };
	}

	public getAuswahlProps(to: RouteLocationNormalized): AufsichtsbereicheAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			gotoEintrag: this.data.gotoEintrag,
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			stundenplanManager: () => this.data.stundenplanManager,
		};
	}

	public getProps(to: RouteLocationNormalized): AufsichtsbereicheProps {
		return {
			auswahl: this.data.auswahl,
			patch: this.data.patch,
		};
	}

}

export const routeKatalogAufsichtsbereiche = new RouteKatalogAufsichtsbereiche();
