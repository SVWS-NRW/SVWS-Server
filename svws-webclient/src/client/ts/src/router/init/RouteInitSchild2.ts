import type { InitSchild2Props } from "~/components/init/SInitSchild2Props";
import type { RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { api } from "../Api";
import { routeApp } from "../RouteApp";
import type { RouteInit } from "../RouteInit";

const SInitSchild2 = () => import("~/components/init/SInitSchild2.vue")

export class RouteInitSchild2 extends RouteNode<unknown, RouteInit> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "init.schild2", "/init/schild2", SInitSchild2);
		super.propHandler = (route) => this.getProps();
		super.text = "Initialisierung mit Schild 2-Datenabank";
	}

	migrateMDB = async (data: FormData): Promise<boolean> => {
		try {
			await api.server.migrateFromMDB(data, api.schema);
			await RouteManager.doRoute(routeApp.getRoute());
			return true;
		} catch(error) {
			console.warn(`Das Initialiseren des Schemas mit der Schild 2-Datenbank ist fehlgeschlagen.`);
			return false;
		}
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name };
	}

	public getProps(): InitSchild2Props {
		return {
			migrateMDB: this.migrateMDB,
		}
	}

}

export const routeInitSchild2 = new RouteInitSchild2();
