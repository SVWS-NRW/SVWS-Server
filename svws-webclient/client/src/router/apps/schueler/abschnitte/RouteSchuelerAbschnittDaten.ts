import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import type { RouteSchuelerAbschnitt } from "~/router/apps/schueler/abschnitte/RouteSchuelerAbschnitt";
import { routeSchuelerAbschnitt } from "~/router/apps/schueler/abschnitte/RouteSchuelerAbschnitt";
import { RouteDataSchuelerAbschnittDaten } from "~/router/apps/schueler/abschnitte/RouteDataSchuelerAbschnittDaten";

import type { SchuelerAbschnittAuswahlProps } from "~/components/schueler/abschnitt/SSchuelerAbschnittAuswahlProps";
import type { SchuelerAbschnittDatenProps } from "~/components/schueler/abschnitt/SSchuelerAbschnittDatenProps";

const SSchuelerAbschnittDaten = () => import("~/components/schueler/abschnitt/SSchuelerAbschnittDaten.vue");
const SSchuelerAbschnittAuswahl = () => import("~/components/schueler/abschnitt/SSchuelerAbschnittAuswahl.vue")

export class RouteSchuelerAbschnittDaten extends RouteNode<RouteDataSchuelerAbschnittDaten, RouteSchuelerAbschnitt> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.abschnitt.daten", ":abschnitt(\\d+)?/:wechselNr(\\d+)?", SSchuelerAbschnittDaten, new RouteDataSchuelerAbschnittDaten());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Abschnittsdaten";
		super.setView("lernabschnittauswahl", SSchuelerAbschnittAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
		];
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		return (to_params.id !== undefined);
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array || to_params.abschnitt instanceof Array || to_params.wechselNr instanceof Array)
			throw routeError.getRoute(new Error("Die Parameter der Route dürfen keine Arrays sein"));
		if (to_params.id === undefined)
			return routeError.getRoute(new Error("Keine Schüler-ID in der URL angegeben."));
		const id = parseInt(to_params.id);
		if (to_params.abschnitt === undefined) {
			return routeSchuelerAbschnitt.getRoute(id);
		} else {
			const abschnitt = parseInt(to_params.abschnitt);
			const wechselNr = (to_params.wechselNr === undefined) || (to_params.wechselNr === "") ? 0 : parseInt(to_params.wechselNr);
			const eintrag = routeSchuelerAbschnitt.data.getEntry(abschnitt, wechselNr);
			if (eintrag === undefined)
				return routeError.getRoute(new Error("Der Schüler mit der ID `id` hat keinen Lernabschnitt für den Schuljahresabschnitt mit der ID `abschnitt` vorhanden"), 404);
			await this.data.setEintrag(eintrag);
		}
	}

	public getRoute(id: number, abschnitt: number | undefined, wechselNr: number | undefined) : RouteLocationRaw {
		return { name: this.name, params: { id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchuelerAbschnittAuswahlProps {
		return {
			lernabschnitt: this.data.auswahl,
			lernabschnitte: routeSchuelerAbschnitt.data.listAbschnitte,
			gotoLernabschnitt: this.data.gotoLernabschnitt
		};
	}

	public getProps(to: RouteLocationNormalized): SchuelerAbschnittDatenProps {
		return {
			schule: api.schuleStammdaten,
			data: this.data.daten,
			mapLehrer: this.data.mapLehrer,
			mapJahrgaenge: this.data.mapJahrgaenge,
			mapKlassen: this.data.mapKlassen,
			mapFoerderschwerpunkte: this.data.mapFoerderschwerpunkte,
			patch: this.data.patch,
			patchBemerkungen: this.data.patchBemerkungen
		};
	}

}

export const routeSchuelerAbschnittDaten = new RouteSchuelerAbschnittDaten();

