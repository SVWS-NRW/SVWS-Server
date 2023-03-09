import { BenutzerKompetenz, JahrgangsListeEintrag, KlassenDaten, KlassenListeEintrag, List, Schulform, Vector } from "@svws-nrw/svws-core";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { KlassenDatenProps } from "~/components/klassen/daten/SKlassenDatenProps";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { RouteKlassen, routeKlassen } from "../RouteKlassen";

export class RouteDataKlassenDaten {
	item: KlassenListeEintrag | undefined = undefined;
	mapJahrgaenge: Map<Number, JahrgangsListeEintrag> = new Map();
	private _daten: KlassenDaten | undefined = undefined;

	public async ladeListeJahrgaenge() {
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const mapKurse = new Map<number, JahrgangsListeEintrag>();
		for (const l of listJahrgaenge)
			mapKurse.set(l.id, l);
		this.mapJahrgaenge = mapKurse;
	}

	get daten(): KlassenDaten {
		if (this._daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._daten;
	}

	set daten(value: KlassenDaten | undefined) {
		this._daten = value;
	}

	patch = async (data : Partial<KlassenDaten>) => {
		if (this.item === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patchKlassenDaten", data);
		//await api.server.patchKursDaten(data, api.schema, this.item.id);
	}
}

const SKlassenDaten = () => import("~/components/klassen/daten/SKlassenDaten.vue");

export class RouteKlassenDaten extends RouteNode<RouteDataKlassenDaten, RouteKlassen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "klassen_daten", "daten", SKlassenDaten, new RouteDataKlassenDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.data.ladeListeJahrgaenge();
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.parent === undefined)
			throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id);
			await this.onSelect(this.parent.data.mapKlassen.get(id));
		}
	}

	protected async onSelect(item?: KlassenListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.daten = undefined;
		} else {
			this.data.item = item;
			this.data.daten = await api.server.getKlasse(api.schema, item.id)
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): KlassenDatenProps {
		return {
			patch: this.data.patch,
			data: this.data.daten,
			mapLehrer: routeKlassen.data.mapLehrer,
			mapJahrgaenge: this.data.mapJahrgaenge
		};
	}

}

export const routeKlassenDaten = new RouteKlassenDaten();

