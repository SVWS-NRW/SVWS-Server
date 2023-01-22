import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams } from "vue-router";
import { routeSchuleBenutzerDaten } from "~/router/apps/benutzer/RouteSchuleBenutzerDaten";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { ListBenutzer } from "~/apps/schule/benutzerverwaltung/ListBenutzer";
import { WritableComputedRef } from "vue";
import { mainApp } from "~/apps/Main"
import { RouteNode } from "~/router/RouteNode";
import { RouteApp } from "~/router/RouteApp";


const SBenutzerAuswahl = () => import("~/components/schule/benutzer/SBenutzerAuswahl.vue")
const SBenutzerApp = () => import("~/components/schule/benutzer/SBenutzerApp.vue")


export class RouteSchuleBenutzer extends RouteNodeListView<ListBenutzer, BenutzerListeEintrag, unknown, RouteApp> {

	public constructor() {
		super("benutzer", "/schule/benutzer/:id(\\d+)?", SBenutzerAuswahl, SBenutzerApp, new ListBenutzer(), 'id');
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzer";
		super.setView("liste", SBenutzerAuswahl, (route) => this.getProps(route));
		super.children = [
			routeSchuleBenutzerDaten
		];
		super.defaultChild = routeSchuleBenutzerDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChild!.name : this.selectedChild.name;
			await this.liste.update_list();
			return { name: redirect_name, params: { id: this.liste.liste.at(0)?.id }};
		}
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.liste.update_list();  // Die Auswahlliste wird als letztes geladen
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.liste.liste.find(f => f.id === id));
		}
	}

	protected async onSelect(item?: BenutzerListeEintrag) {
		if (item === this.item)
			return;
		if (item === undefined) {
			this.item = undefined;
		} else {
			this.item = item;
		}
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<BenutzerListeEintrag | undefined> {
		return this.getSelector();
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...super.getProps(to)
		};
	}

}

export const routeSchuleBenutzer = new RouteSchuleBenutzer();
