import type { EinschulungsartKatalogEintrag } from "@core/asd/data/schueler/EinschulungsartKatalogEintrag";
import type { Erzieherart } from "@core/core/data/erzieher/Erzieherart";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { KatalogEntlassgrund } from "@core/core/data/kataloge/KatalogEntlassgrund";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { Abteilung } from "@core/core/data/schule/Abteilung";
import type { Beschaeftigungsart } from "@core/core/data/schule/Beschaeftigungsart";
import type { Betrieb } from "@core/core/data/schule/Betrieb";
import type { Betriebsart } from "@core/core/data/schule/Betriebsart";
import type { Einwilligungsart } from "@core/core/data/schule/Einwilligungsart";
import type { Fahrschuelerart } from "@core/core/data/schule/Fahrschuelerart";
import type { Floskel } from "@core/core/data/schule/Floskel";
import type { Floskelgruppe } from "@core/core/data/schule/Floskelgruppe";
import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import type { Kindergarten } from "@core/core/data/schule/Kindergarten";
import type { Leitungsfunktion } from "@core/core/data/schule/Leitungsfunktion";
import type { Lernplattform } from "@core/core/data/schule/Lernplattform";
import type { Merkmal } from "@core/core/data/schule/Merkmal";
import type { ReligionEintrag } from "@core/core/data/schule/ReligionEintrag";
import type { Telefonart } from "@core/core/data/schule/Telefonart";
import type { VermerkartEintrag } from "@core/core/data/schule/VermerkartEintrag";
import type { List } from "@core/java/util/List";
import { Katalog } from "~/cache/Katalog";
import { api } from "~/router/Api";
import { schuleStateImpl } from "~/states/SchuleStateImpl";


export class KatalogCache {

	/**
	 * Mappt jeden {@link Katalog} auf eine asynchrone Funktion,
	 * die die zugehörigen Daten lädt und als `Partial<KatalogCache>` zurückgibt.
	 * Wird zur Cache-Aktualisierung verwendet.
	 */
	private _katalogCacheUpdater = new Map<Katalog, () => Promise<Partial<KatalogCache>>>();
	private _abteilungenById: Map<number, Abteilung> = new Map();
	private _beschaeftigungsartenById: Map<number, Beschaeftigungsart> = new Map();
	private _betriebeById: Map<number, Betrieb> = new Map();
	private _betriebsartenById: Map<number, Betriebsart> = new Map();
	private _einschulungsartenById: Map<number, EinschulungsartKatalogEintrag> = new Map();
	private _einwilligungsartenById: Map<number, Einwilligungsart> = new Map();
	private _entlassgruendeById: Map<number, KatalogEntlassgrund> = new Map();
	private _erzieherartenById: Map<number, Erzieherart> = new Map();
	private _fahrschuelerartenById: Map<number, Fahrschuelerart> = new Map();
	private _floskelgruppenById: Map<number, Floskelgruppe> = new Map();
	private _floskelnById: Map<number, Floskel> = new Map();
	private _foerderschwerpunkteById: Map<number, FoerderschwerpunktEintrag> = new Map();
	private _faecherById: Map<number, FachDaten> = new Map();
	private _haltestellenById: Map<number, Haltestelle> = new Map();
	private _jahrgaengeById: Map<number, JahrgangsDaten> = new Map();
	private _kindergaertenById: Map<number, Kindergarten> = new Map();
	private _lernplattformenById: Map<number, Lernplattform> = new Map();
	private _merkmaleById: Map<number, Merkmal> = new Map();
	private _religionenById: Map<number, ReligionEintrag> = new Map();
	private _schulenById: Map<number, SchulEintrag> = new Map();
	private _telefonartenById: Map<number, Telefonart> = new Map();
	private _vermerkartenById: Map<number, VermerkartEintrag> = new Map();
	private _leitungsfunktionenById: Map<number, Leitungsfunktion> = new Map();

	public constructor() {
		this.initializeCacheUpdater();
	}

	private initializeCacheUpdater() {
		this._katalogCacheUpdater.set(Katalog.ABTEILUNGEN, async () => {
			const result = await api.server.getAbteilungenByIdJahresAbschnitt(api.schema, schuleStateImpl.abschnitt.id);
			return { abteilungenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.BESCHAEFTIGUNGSARTEN, async () => {
			const result = await api.server.getBeschaeftigungsarten(api.schema);
			return { beschaeftigungsartenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.BETRIEBE, async () => {
			const result = await api.server.getBetriebe(api.schema);
			return { betriebeById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.BETRIEBSARTEN, async () => {
			const result = await api.server.getBetriebsarten(api.schema);
			return { betriebsartenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.EINSCHULUNGSARTEN, async () => {
			const result = await api.server.getEinschulungsarten(api.schema);
			return { einschulungsartenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.EINWILLIGUNGSARTEN, async () => {
			const result = await api.server.getEinwilligungsarten(api.schema);
			return { einwilligungsartenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.ENTLASSGRUENDE, async () => {
			const result = await api.server.getEntlassgruende(api.schema);
			return { entlassgruendeById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.ERZIEHERARTEN, async () => {
			const result = await api.server.getErzieherArten(api.schema);
			return { erzieherartenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.FAECHER, async () => {
			const result = await api.server.getFaecher(api.schema);
			return { faecherById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.FAHRSCHUELERARTEN, async () => {
			const result = await api.server.getFahrschuelerarten(api.schema);
			return { fahrschuelerartenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.FLOSKELGRUPPEN, async () => {
			const result = await api.server.getFloskelgruppen(api.schema);
			return { floskelgruppenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.FLOSKELN, async () => {
			const result = await api.server.getFloskeln(api.schema);
			return { floskelnById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.FOERDERSCHWERPUNKTE, async () => {
			const result = await api.server.getKatalogFoerderschwerpunkte(api.schema);
			return { foerderschwerpunkteById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.HALTESTELLEN, async () => {
			const result = await api.server.getHaltestellen(api.schema);
			return { haltestellenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.JAHRGAENGE, async () => {
			const result = await api.server.getJahrgaenge(api.schema);
			return { jahrgaengeById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.KINDERGAERTEN, async () => {
			const result = await api.server.getKindergaerten(api.schema);
			return { kindergaertenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.LERNPLATTFORMEN, async () => {
			const result = await api.server.getLernplattformen(api.schema);
			return { lernplattformenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.RELIGIONEN, async () => {
			const result = await api.server.getReligionen(api.schema);
			return { religionenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.SCHULEN, async () => {
			const result = await api.server.getSchulen(api.schema);
			return { schulenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.TELEFONARTEN, async () => {
			const result = await api.server.getTelefonarten(api.schema);
			return { telefonartenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.VERMERKARTEN, async () => {
			const result = await api.server.getVermerkarten(api.schema);
			return { vermerkartenById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.MERKMALE, async () => {
			const result = await api.server.getMerkmale(api.schema);
			return { merkmaleById: this.convertToMap(result) };
		});

		this._katalogCacheUpdater.set(Katalog.LEITUNGSFUNKTIONEN, async () => {
			const result = await api.server.getLeitungsfunktionen(api.schema);
			return { leitungsfunktionenById: this.convertToMap(result) };
		});
	}

	private convertToMap<T extends { id: number }>(list: List<T>): Map<number, T> {
		const map = new Map<number, T>();
		for (const item of list) {
			map.set(item.id, item);
		}
		return map;
	}


	get katalogCacheUpdater(): Map<Katalog, () => Promise<Partial<KatalogCache>>> {
		return this._katalogCacheUpdater;
	}

	set katalogCacheUpdater(value: Map<Katalog, () => Promise<Partial<KatalogCache>>>) {
		this._katalogCacheUpdater = value;
	}

	get abteilungenById(): Map<number, Abteilung> {
		return this._abteilungenById;
	}

	set abteilungenById(value: Map<number, Abteilung>) {
		this._abteilungenById = value;
	}

	get beschaeftigungsartenById(): Map<number, Beschaeftigungsart> {
		return this._beschaeftigungsartenById;
	}

	set beschaeftigungsartenById(value: Map<number, Beschaeftigungsart>) {
		this._beschaeftigungsartenById = value;
	}

	get betriebeById(): Map<number, Betrieb> {
		return this._betriebeById;
	}

	set betriebeById(value: Map<number, Betrieb>) {
		this._betriebeById = value;
	}

	get betriebsartenById(): Map<number, Betriebsart> {
		return this._betriebsartenById;
	}

	set betriebsartenById(value: Map<number, Betriebsart>) {
		this._betriebsartenById = value;
	}

	get einschulungsartenById(): Map<number, EinschulungsartKatalogEintrag> {
		return this._einschulungsartenById;
	}

	set einschulungsartenById(value: Map<number, EinschulungsartKatalogEintrag>) {
		this._einschulungsartenById = value;
	}

	get einwilligungsartenById(): Map<number, Einwilligungsart> {
		return this._einwilligungsartenById;
	}

	set einwilligungsartenById(value: Map<number, Einwilligungsart>) {
		this._einwilligungsartenById = value;
	}

	get entlassgruendeById(): Map<number, KatalogEntlassgrund> {
		return this._entlassgruendeById;
	}

	set entlassgruendeById(value: Map<number, KatalogEntlassgrund>) {
		this._entlassgruendeById = value;
	}

	get erzieherartenById(): Map<number, Erzieherart> {
		return this._erzieherartenById;
	}

	set erzieherartenById(value: Map<number, Erzieherart>) {
		this._erzieherartenById = value;
	}

	get fahrschuelerartenById(): Map<number, Fahrschuelerart> {
		return this._fahrschuelerartenById;
	}

	set fahrschuelerartenById(value: Map<number, Fahrschuelerart>) {
		this._fahrschuelerartenById = value;
	}

	get floskelgruppenById(): Map<number, Floskelgruppe> {
		return this._floskelgruppenById;
	}

	set floskelgruppenById(value: Map<number, Floskelgruppe>) {
		this._floskelgruppenById = value;
	}

	get floskelnById(): Map<number, Floskel> {
		return this._floskelnById;
	}

	set floskelnById(value: Map<number, Floskel>) {
		this._floskelnById = value;
	}

	get foerderschwerpunkteById(): Map<number, FoerderschwerpunktEintrag> {
		return this._foerderschwerpunkteById;
	}

	set foerderschwerpunkteById(value: Map<number, FoerderschwerpunktEintrag>) {
		this._foerderschwerpunkteById = value;
	}

	get faecherById(): Map<number, FachDaten> {
		return this._faecherById;
	}

	set faecherById(value: Map<number, FachDaten>) {
		this._faecherById = value;
	}

	get haltestellenById(): Map<number, Haltestelle> {
		return this._haltestellenById;
	}

	set haltestellenById(value: Map<number, Haltestelle>) {
		this._haltestellenById = value;
	}

	get jahrgaengeById(): Map<number, JahrgangsDaten> {
		return this._jahrgaengeById;
	}

	set jahrgaengeById(value: Map<number, JahrgangsDaten>) {
		this._jahrgaengeById = value;
	}

	get kindergaertenById(): Map<number, Kindergarten> {
		return this._kindergaertenById;
	}

	set kindergaertenById(value: Map<number, Kindergarten>) {
		this._kindergaertenById = value;
	}

	get lernplattformenById(): Map<number, Lernplattform> {
		return this._lernplattformenById;
	}

	set lernplattformenById(value: Map<number, Lernplattform>) {
		this._lernplattformenById = value;
	}

	get religionenById(): Map<number, ReligionEintrag> {
		return this._religionenById;
	}

	set religionenById(value: Map<number, ReligionEintrag>) {
		this._religionenById = value;
	}

	get schulenById(): Map<number, SchulEintrag> {
		return this._schulenById;
	}

	set schulenById(value: Map<number, SchulEintrag>) {
		this._schulenById = value;
	}

	get telefonartenById(): Map<number, Telefonart> {
		return this._telefonartenById;
	}

	set telefonartenById(value: Map<number, Telefonart>) {
		this._telefonartenById = value;
	}

	get vermerkartenById(): Map<number, VermerkartEintrag> {
		return this._vermerkartenById;
	}

	set vermerkartenById(value: Map<number, VermerkartEintrag>) {
		this._vermerkartenById = value;
	}

	get merkmaleById(): Map<number, Merkmal> {
		return this._merkmaleById;
	}

	set merkmaleById(value: Map<number, Merkmal>) {
		this._merkmaleById = value;
	}

	get leitungsfunktionenById(): Map<number, Leitungsfunktion> {
		return this._leitungsfunktionenById;
	}

	set leitungsfunktionenById(value: Map<number, Leitungsfunktion>) {
		this._leitungsfunktionenById = value;
	}
}
