import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { routeError } from "~/router/error/RouteError";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeSchemaUebersicht } from "~/router/apps/schema/uebersicht/RouteSchemaUebersicht";

import { RouteDataSchema } from "~/router/apps/schema/RouteDataSchema";

import type { SchemaAppProps } from "~/components/schema/SSchemaAppProps";
import type { SchemaAuswahlProps } from "~/components/schema/SSchemaAuswahlProps";
import { api } from "~/router/Api";
import { ServerMode } from "@core/core/types/ServerMode";
import type { TabData } from "@ui/ui/nav/TabData";
import { TabManager } from "@ui/ui/nav/TabManager";

const SSchemaAuswahl = () => import("~/components/schema/SSchemaAuswahl.vue")
const SSchemaApp = () => import("~/components/schema/SSchemaApp.vue")


export class RouteSchema extends RouteNode<RouteDataSchema, RouteApp> {

	public constructor() {
		super("schema", "/schema/:schema?", SSchemaApp, new RouteDataSchema());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schema";
		super.setView("liste", SSchemaAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeSchemaUebersicht,
		];
		super.defaultChild = routeSchemaUebersicht;
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.schema instanceof Array)
			return routeError.getRoute(new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		// Prüfe, ob bereits ein Schema ausgewählt wurde. Wenn nicht, dann lade die Liste vom Server und wähle ein Default-Schema aus
		const auswahl = this.data.auswahl;
		if (auswahl === undefined) {
			await this.data.init(to_params.schema, !isEntering);
			// Prüfe, ob ein Schema in der neu geladenen Liste vorhanden ist
			if (this.data.auswahl !== undefined)
				return this.getChildRoute(this.data.auswahl.name);
			return;
		}
		const schemaNeu = (to_params.schema === undefined) ? undefined : this.data.mapSchema.get(to_params.schema.toLocaleLowerCase());
		await this.data.setSchema(schemaNeu);
		if (to.name === this.name)
			return this.getChildRoute(to_params.schema);
		if (!to.name.startsWith(this.data.view.name))
			for (const child of this.children)
				if (to.name.startsWith(child.name))
					await this.data.setView(child);
	}

	protected async leaveBefore(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		// Aufräumen der Quelldaten für Migrationen aus RouteData, damit diese beim Abmelden nicht erhalten bleiben!
		this.data.resetMigrationQuellinformationen();
	}

	public getRoute(schema?: string) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { schema } };
	}

	public getChildRoute(schema: string) : RouteLocationRaw {
		const redirect_name: string = (routeSchema.selectedChild === undefined) ? routeSchemaUebersicht.name : routeSchema.selectedChild.name;
		return { name: redirect_name, params: { schema } };
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchemaAuswahlProps {
		return {
			hasRootPrivileges: api.hasRootPrivileges,
			auswahl: this.data.auswahl,
			auswahlGruppe: this.data.auswahlGruppe,
			mapSchema: () => this.data.mapSchema,
			gotoSchema: this.data.gotoSchema,
			gotoSchemaNeu: this.data.gotoSchemaNeu,
			setAuswahlGruppe: this.data.setAuswahlGruppe,
			addSchema: this.data.addSchema,
			importSchema: this.data.importSchema,
			migrateSchema: this.data.migrateSchema,
			duplicateSchema: this.data.duplicateSchema,
			refresh: this.data.refresh,
			migrationQuellinformationen: () => this.data.migrationQuellinformationen.value,
			apiStatus: api.status,
			revision: this.data.revision,
		};
	}

	public getProps(to: RouteLocationNormalized): SchemaAppProps {
		return {
			auswahl: this.data.auswahl,
			schuleInfo: () => this.data.schuleInfo,
			tabManager: () => new TabManager(this.getTabs(), this.getTab(), this.setTab),
		};
	}

	private getTab(): TabData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getTabs(): TabData[] {
		const result: TabData[] = [];
		for (const { name, text } of this.children)
			result.push({ name, text });
		return result;
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { schema: this.data.auswahl?.name } });
		await this.data.setView(node);
	}

}

export const routeSchema = new RouteSchema();
