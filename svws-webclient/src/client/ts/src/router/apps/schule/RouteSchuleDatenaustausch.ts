import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { SchuleDatenaustauschAppProps } from "~/components/schule/datenaustausch/SSchuleDatenaustauschAppProps";
import { SchuleDatenaustauschAuswahlProps } from "~/components/schule/datenaustausch/SSchuleDatenaustauschAuswahlProps";
import { api } from "~/router/Api";
import { routeApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { routeSchule, RouteSchule } from "../RouteSchule";

const SSchuleDatenaustauschApp = () => import("~/components/schule/datenaustausch/SSchuleDatenaustauschApp.vue")
const SSchuleDatenaustauschAuswahl = () => import("~/components/schule/datenaustausch/SSchuleDatenaustauschAuswahl.vue")

class RouteDataSchuleDatenaustausch {

	setGostLupoImportMDBFuerJahrgang = async (formData: FormData) => {
		try {
			const res = await api.server.setGostLupoImportMDBFuerJahrgang(formData, api.schema);
			return res.success;
		} catch(e) {
			return false;
		}
	}

	setGostKurs42ImportZip = async (formData: FormData) => {
		try {
			const res = await api.server.importKurs42Blockung(formData, api.schema);
			return res.success;
		} catch(e) {
			return false;
		}
	}

}

export class RouteSchuleDatenaustausch extends RouteNode<RouteDataSchuleDatenaustausch, RouteSchule> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.datenaustausch", "datenaustausch", SSchuleDatenaustauschApp, new RouteDataSchuleDatenaustausch());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Datenaustausch";
		super.setView("liste", SSchuleDatenaustauschAuswahl, (route) => this.getAuswahlProps(route));
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchuleDatenaustauschAuswahlProps {
		return {
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			returnToSchule: routeSchule.returnToSchule,
		};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschAppProps {
		return {
			setGostLupoImportMDBFuerJahrgang: this.data.setGostLupoImportMDBFuerJahrgang,
			setGostKurs42ImportZip: this.data.setGostKurs42ImportZip,
		};
	}

}

export const routeSchuleDatenaustausch = new RouteSchuleDatenaustausch();

