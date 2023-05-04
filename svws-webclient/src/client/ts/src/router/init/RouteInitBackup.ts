import type { RouteLocationRaw } from "vue-router";
import type { InitBackupProps } from "~/components/init/SInitBackupProps";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { api } from "../Api";
import { routeApp } from "../RouteApp";
import type { RouteInit } from "../RouteInit";

const SInitBackup = () => import("~/components/init/SInitBackup.vue")

export class RouteInitBackup extends RouteNode<unknown, RouteInit> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "init.backup", "/init/backup", SInitBackup);
		super.propHandler = (route) => this.getProps();
		super.text = "Initialisierung mit SVWS-Backup";
	}

	migrateBackup = async (data: FormData) => {
		try {
			await api.server.migrateFromMDB(data, api.schema);
			await RouteManager.doRoute(routeApp.getRoute());
			return true;
		} catch(error) {
			console.warn(`Das Initialiseren des Schemas mit der SVWS Backup-Datenbank ist fehlgeschlagen.`);
			return false;
		}
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name };
	}

	public getProps(): InitBackupProps {
		return {
			migrateBackup: this.migrateBackup,
		}
	}

}

export const routeInitBackup = new RouteInitBackup();
