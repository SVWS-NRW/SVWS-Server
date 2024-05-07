import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";

import type { SchuelerLernabschnittKonferenzProps } from "~/components/schueler/lernabschnitte/konferenz/SSchuelerLernabschnittKonferenzProps";
import { api } from "~/router/Api";
import { routeApp } from "../../RouteApp";

const SSchuelerLernabschnittAllgmein = () => import("~/components/schueler/lernabschnitte/konferenz/SSchuelerLernabschnittKonferenz.vue");

export class RouteSchuelerLernabschnittKonferenz extends RouteNode<any, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.lernabschnitt.konferenz", "konferenz", SSchuelerLernabschnittAllgmein);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Konferenz";
		super.children = [
		];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number, abschnitt: number, wechselNr: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittKonferenzProps {
		return {
			schule: api.schuleStammdaten,
			manager: () => routeSchuelerLernabschnitte.data.manager,
			patch: routeSchuelerLernabschnitte.data.patchLernabschnitt,
			patchBemerkungen: routeSchuelerLernabschnitte.data.patchBemerkungen
		};
	}

}

export const routeSchuelerLernabschnittKonferenz = new RouteSchuelerLernabschnittKonferenz();

