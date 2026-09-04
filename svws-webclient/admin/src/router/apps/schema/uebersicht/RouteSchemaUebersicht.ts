import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { ServerMode } from "@core/core/types/ServerMode";
import type { SchemaUebersichtProps } from "@admin/components/schema/uebersicht/SSchemaUebersichtProps";
import { api } from "@admin/router/Api";
import { RouteNode } from "@admin/router/RouteNode";
import type { RouteSchema } from "../RouteSchema";
import { routeSchema } from "../RouteSchema";

const SSchemaUebersicht = () => import("@admin/components/schema/uebersicht/SSchemaUebersicht.vue");


export class RouteSchemaUebersicht extends RouteNode<unknown, RouteSchema> {

	public constructor() {
		super("schema.uebersicht", "uebersicht", SSchemaUebersicht);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Übersicht";
	}

	public getRoute(id: number): RouteLocationRaw {
		return { name: this.name, params: { id } };
	}

	public getProps(to: RouteLocationNormalized): SchemaUebersichtProps {
		return {
			data: () => routeSchema.data.auswahl,
			admins: () => routeSchema.data.admins,
			backupSchema: routeSchema.data.backupSchema,
			backupSchemaZip: routeSchema.data.backupSchemaZip,
			restoreSchema: routeSchema.data.restoreSchema,
			migrateSchema: routeSchema.data.migrateSchema,
			upgradeSchema: routeSchema.data.upgradeSchema,
			initSchema: routeSchema.data.initSchema,
			createEmptySchema: routeSchema.data.createEmptySchema,
			addExistingSchemaToConfig: routeSchema.data.addExistingSchemaToConfig,
			schuleInfo: () => routeSchema.data.schuleInfo,
			schulen: () => routeSchema.data.schulen,
			revision: routeSchema.data.revision,
			migrationQuellinformationen: () => routeSchema.data.migrationQuellinformationen.value,
			apiStatus: api.status,
		};
	}

}

export const routeSchemaUebersicht = new RouteSchemaUebersicht();
