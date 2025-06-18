import { Schulform } from "@core";
import type { FoerderschwerpunktEintrag, KatalogEintrag, ReligionEintrag, SchulEintrag, SchulformKatalogEintrag, TelefonArt } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { PendingStateManagerSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/PendingStateManagerSchuelerIndividualdaten";


interface RouteStateDataSchuelerIndividualdaten extends RouteStateInterface {
	mapFahrschuelerarten: Map<number, KatalogEintrag>;
	mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag>;
	mapHaltestellen: Map<number, KatalogEintrag>;
	mapReligionen: Map<number, ReligionEintrag>;
	mapSchulen: Map<string, SchulEintrag>;
	mapTelefonArten: Map<number, TelefonArt>;
	pendingStateManager: PendingStateManagerSchuelerIndividualdaten | undefined;
}

export class RouteDataSchuelerIndividualdaten extends RouteData<RouteStateDataSchuelerIndividualdaten> {

	public constructor() {
		super({
			mapFahrschuelerarten: new Map(),
			mapFoerderschwerpunkte: new Map(),
			mapHaltestellen: new Map(),
			mapReligionen: new Map(),
			mapSchulen: new Map<string, SchulEintrag>(),
			mapTelefonArten: new Map(),
			pendingStateManager: undefined,
		});
	}

	get pendingStateManager(): PendingStateManagerSchuelerIndividualdaten {
		if (this._state.value.pendingStateManager === undefined) {
			this._state.value.pendingStateManager = new PendingStateManagerSchuelerIndividualdaten('id',
				() => routeSchueler.data.manager, this._state.value.mapReligionen,
				this._state.value.mapSchulen, this._state.value.mapHaltestellen, this._state.value.mapFahrschuelerarten);
			routeSchueler.data.pendingStateManagerRegistry.addPendingStateManager(this._state.value.pendingStateManager);
		}
		return this._state.value.pendingStateManager;
	}

	get mapFahrschuelerarten(): Map<number, KatalogEintrag> {
		return this._state.value.mapFahrschuelerarten;
	}

	get mapFoerderschwerpunkte(): Map<number, FoerderschwerpunktEintrag> {
		return this._state.value.mapFoerderschwerpunkte;
	}

	get mapHaltestellen(): Map<number, KatalogEintrag> {
		return this._state.value.mapHaltestellen;
	}

	get mapReligionen(): Map<number, ReligionEintrag> {
		return this._state.value.mapReligionen;
	}

	get mapSchulen(): Map<string, SchulEintrag> {
		return this._state.value.mapSchulen;
	}

	get mapTelefonArten(): Map<number, TelefonArt> {
		return this._state.value.mapTelefonArten;
	}

	public async ladeListe() {
		// Lade den Katalog der Fahrschülerarten
		const fahrschuelerarten = await api.server.getSchuelerFahrschuelerarten(api.schema)
		const mapFahrschuelerarten = new Map();
		for (const fa of fahrschuelerarten)
			mapFahrschuelerarten.set(fa.id, fa);
		// Lade den Katalog der Förderschwerpunkte
		const foerderschwerpunkte = await api.server.getSchuelerFoerderschwerpunkte(api.schema);
		const mapFoerderschwerpunkte = new Map();
		for (const fs of foerderschwerpunkte)
			mapFoerderschwerpunkte.set(fs.id, fs);
		// Lade den Katalog der Haltestellen
		const haltestellen = await api.server.getHaltestellen(api.schema);
		const mapHaltestellen = new Map();
		for (const h of haltestellen)
			mapHaltestellen.set(h.id, h);
		// Lade den Katalog der Religionen
		const religionen = await api.server.getReligionen(api.schema)
		const mapReligionen = new Map();
		for (const r of religionen)
			mapReligionen.set(r.id, r);
		// Lade den Katalog der TelefonArten
		const telefonArten = await api.server.getTelefonarten(api.schema);
		const mapTelefonArten = new Map();
		for (const ta of telefonArten)
			mapTelefonArten.set(ta.id, ta);
		// Ermittle den Katalog der Schulen, welche ein Kürzel haben und als Stammschulen für Schüler in Frage kommen
		const schulen = await api.server.getSchulen(api.schema);
		const mapSchulen = new Map<string, SchulEintrag>();
		for (const schule of schulen) {
			if (schule.schulnummerStatistik === null)
				continue;
			const sfEintrag: SchulformKatalogEintrag | null = schule.idSchulform === null ? null : Schulform.data().getEintragByID(schule.idSchulform);
			const sf: Schulform | null = sfEintrag === null ? null : Schulform.data().getWertBySchluessel(sfEintrag.schluessel);
			if (sf === api.schulform)
				mapSchulen.set(schule.schulnummerStatistik, schule);
		}
		this.setPatchedDefaultState({ mapFahrschuelerarten, mapFoerderschwerpunkte, mapHaltestellen, mapReligionen, mapSchulen, mapTelefonArten })
	}

}
