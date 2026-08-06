import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import type { RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import type { SchuelerSchnelleingabeProps } from "~/components/schueler/neuanlage/SchuelerSchnelleingabeProps";
import { ViewType } from "@ui";
import { RouteDataSchuelerSchnelleingabe } from "~/router/apps/schueler/neu/RouteDataSchuelerSchnelleingabe";

const SchuelerSchnelleingabe = () => import("~/components/schueler/neuanlage/SchuelerSchnelleingabe.vue");

export class RouteSchuelerSchnelleingabe extends RouteNode<RouteDataSchuelerSchnelleingabe, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN], "schueler.schnelleingabe", "schnelleingabe", SchuelerSchnelleingabe, new RouteDataSchuelerSchnelleingabe());
		super.types = new Set([ViewType.NEU]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schnelleingabe";
		this.setCheckpoint = true;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeDaten();
	}

	public getProps(to: RouteLocationNormalized): SchuelerSchnelleingabeProps {
		return {
			manager: () => this.data.manager,
			gotoDefaultView: routeSchueler.data.gotoDefaultView,
			gotoSchuelerNeuView: routeSchueler.data.gotoHinzufuegenView,
			patchSchueler: routeSchuelerSchnelleingabe.data.patchSchueler,
			getErzieher: () => routeSchuelerSchnelleingabe.data.getErzieher,
			addErzieher: routeSchuelerSchnelleingabe.data.addErzieher,
			patchErzieher: routeSchuelerSchnelleingabe.data.patchErzieher,
			patchErzieherAnPosition: routeSchuelerSchnelleingabe.data.patchErzieherAnPosition,
			deleteErzieher: routeSchuelerSchnelleingabe.data.deleteErzieher,
			getTelefone: () => routeSchuelerSchnelleingabe.data.getTelefone,
			addTelefon: routeSchuelerSchnelleingabe.data.addTelefon,
			patchTelefon: routeSchuelerSchnelleingabe.data.patchTelefon,
			deleteTelefone: routeSchuelerSchnelleingabe.data.deleteTelefone,
			getVermerke: () => routeSchuelerSchnelleingabe.data.getVermerke,
			addVermerk: routeSchuelerSchnelleingabe.data.addVermerk,
			patchVermerk: routeSchuelerSchnelleingabe.data.patchVermerk,
			deleteVermerke: routeSchuelerSchnelleingabe.data.deleteVermerke,
			patchSchulbesuchsdaten: routeSchuelerSchnelleingabe.data.patchSchulbesuchsdaten,
			patchLernabschnittsdaten: routeSchuelerSchnelleingabe.data.patchLernabschnittsdaten,
		};
	}

}

export const routeSchuelerSchnelleingabe = new RouteSchuelerSchnelleingabe();
