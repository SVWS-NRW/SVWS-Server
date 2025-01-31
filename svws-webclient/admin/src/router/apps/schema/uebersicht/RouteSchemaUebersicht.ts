import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeSchema, type RouteSchema } from "~/router/apps/schema/RouteSchema";
import type { SchemaUebersichtProps } from "~/components/schema/uebersicht/SSchemaUebersichtProps";
import { api } from "~/router/Api";
import { ServerMode } from "@core/core/types/ServerMode";

const SSchemaUebersicht = () => import("~/components/schema/uebersicht/SSchemaUebersicht.vue");


export class RouteSchemaUebersicht extends RouteNode<unknown, RouteSchema> {

	public constructor() {
		super("schema.uebersicht", "uebersicht", SSchemaUebersicht);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Übersicht";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): SchemaUebersichtProps {
		return {
			data: () => routeSchema.data.auswahl,
			admins: () => routeSchema.data.admins,
			backupSchema: routeSchema.data.backupSchema,
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
