import type { ApiFile } from "@core/api/BaseApi";
import { Sprachbelegung } from "@core/asd/data/schueler/Sprachbelegung";
import { Sprachpruefung } from "@core/asd/data/schueler/Sprachpruefung";
import { SchuleStammdaten } from "@core/asd/data/schule/SchuleStammdaten";
import { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import { GostBelegpruefungErgebnis } from "@core/core/abschluss/gost/GostBelegpruefungErgebnis";
import { GostBelegpruefungsArt } from "@core/core/abschluss/gost/GostBelegpruefungsArt";
import { Abiturdaten } from "@core/core/data/gost/Abiturdaten";
import { AbiturFachbelegung } from "@core/core/data/gost/AbiturFachbelegung";
import { AbiturFachbelegungHalbjahr } from "@core/core/data/gost/AbiturFachbelegungHalbjahr";
import type { GostBeratungslehrer } from "@core/core/data/gost/GostBeratungslehrer";
import { GostJahrgang } from "@core/core/data/gost/GostJahrgang";
import { GostJahrgangsdaten } from "@core/core/data/gost/GostJahrgangsdaten";
import { GostLaufbahnplanungBeratungsdaten } from "@core/core/data/gost/GostLaufbahnplanungBeratungsdaten";
import type { GostSchuelerFachwahl } from "@core/core/data/gost/GostSchuelerFachwahl";
import { GostLaufbahnplanungExportV1 } from "@core/core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1";
import { GostLaufbahnplanungExportV1Fachbelegung } from "@core/core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1Fachbelegung";
import { GostLaufbahnplanungExportV1Schueler } from "@core/core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1Schueler";
import { GostLaufbahnplanungExportV2 } from "@core/core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2";
import { GostLaufbahnplanungExportV2Fach } from "@core/core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2Fach";
import { GostLaufbahnplanungExportV2Schueler } from "@core/core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2Schueler";
import { GostLaufbahnplanungExportV2SchuelerFachbelegung } from "@core/core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2SchuelerFachbelegung";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { UserNotificationException } from "@core/core/exceptions/UserNotificationException";
import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import { GostKursart } from "@core/core/types/gost/GostKursart";
import { GostFaecherManager } from "@core/core/utils/gost/GostFaecherManager";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { GostBelegpruefungsModus, GostKlausurvorgabeEintrag, GostLaufbahnplanungState } from "@ui/states/GostLaufbahnplanungState";
import { StateManager } from "@ui/ui/StateManager";
import { Config, ConfigElement } from "@ui/utils/Config";
import { RouteManager } from "@lupo/router/RouteManager";
import { HashMap2D } from "@core/core/adt/map/HashMap2D";
import { GostSchuelerGKLWahl } from "@core/core/data/gost/GostSchuelerGKLWahl";
import { HashMap } from "@core/java/util/HashMap";
import type { JavaMap } from "@core/java/util/JavaMap";
import { GostFach } from "@core/core/data/gost/GostFach";
import { GostLaufbahnplanungExportV2GKL } from "@core/core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2GKL";
import { GostKlausurvorgabe } from "@core/core/data/gost/klausuren/GostKlausurvorgabe";
import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";


interface GostLaufbahnplanungReactiveState {
	schuleStammdaten: SchuleStammdaten;
	schuelerIDEncrypted: string;
	dirty: boolean;
	beratungslehrer: List<GostBeratungslehrer>;
	config: Config;
	auswahl: SchuelerListeEintrag | undefined;
	abiturdaten: Abiturdaten | undefined;
	abiturdatenManager: AbiturdatenManager | undefined;
	faecherManager: GostFaecherManager | undefined;
	gostBelegpruefungsArt: GostBelegpruefungsModus;
	gostBelegpruefungErgebnis: GostBelegpruefungErgebnis;
	mapKlausurvorgaben: JavaMap<number, GostKlausurvorgabeEintrag>;
	gklMoeglich: HashMap2D<number, GostHalbjahr, List<GostKlausurvorgabeEintrag>>,
	gklWahlen: GostSchuelerGKLWahl,
	gostJahrgang: GostJahrgang;
	gostJahrgangsdaten: GostJahrgangsdaten;
	zwischenspeicher: Abiturdaten | undefined;
};


/**
 * Der Zustand der Laufbahnplanung der Gymnasialen Oberstufe
 */
export class GostLaufbahnplanungStateImpl extends StateManager<GostLaufbahnplanungReactiveState> implements GostLaufbahnplanungState {

	public constructor() {
		super({
			schuleStammdaten: new SchuleStammdaten(),
			schuelerIDEncrypted: '',
			dirty: false,
			beratungslehrer: new ArrayList(),
			config: new Config(async (key, value) => { }, async (key, value) => { }),
			auswahl: undefined,
			abiturdaten: undefined,
			abiturdatenManager: undefined,
			faecherManager: undefined,
			gostBelegpruefungsArt: 'gesamt',
			gostBelegpruefungErgebnis: new GostBelegpruefungErgebnis(),
			mapKlausurvorgaben: new HashMap<number, GostKlausurvorgabeEintrag>(),
			gklMoeglich: new HashMap2D<number, GostHalbjahr, List<GostKlausurvorgabeEintrag>>(),
			gklWahlen: new GostSchuelerGKLWahl(),
			gostJahrgang: new GostJahrgang(),
			gostJahrgangsdaten: new GostJahrgangsdaten(),
			zwischenspeicher: undefined,
		});
		this._state.value.config.addElements([new ConfigElement("app.schueler.laufbahnplanung.modus", "user", "normal")]);
		this._state.value.config.addElements([new ConfigElement("app.schueler.laufbahnplanung.faecher.anzeigen", "user", "alle")]);
	}

	get valid(): boolean {
		return (this._state.value.auswahl !== undefined) && (this._state.value.abiturdatenManager !== undefined);
	}

	public async clear() {
		this.setPatchedDefaultState({});
	}

	get modified(): boolean {
		return this._state.value.dirty;
	}

	get schueler(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined) {
			throw new Error("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zur Laufbahnplanung abgerufen oder eingegeben werden.");
		}
		return this._state.value.auswahl;
	}

	get schuelerOrNull(): SchuelerListeEintrag | null {
		return this._state.value.auswahl ?? null;
	}

	get gostJahrgangsdaten(): GostJahrgangsdaten {
		return this._state.value.gostJahrgangsdaten;
	}

	get beratungslehrer(): List<GostBeratungslehrer> {
		return new ArrayList(this._state.value.gostJahrgangsdaten.beratungslehrer);
	}

	get gostBelegpruefungErgebnis(): GostBelegpruefungErgebnis {
		return this._state.value.gostBelegpruefungErgebnis;
	}

	get gostLaufbahnBeratungsdaten(): GostLaufbahnplanungBeratungsdaten {
		return new GostLaufbahnplanungBeratungsdaten();
	}

	get listeLehrer(): ArrayList<LehrerListeEintrag> {
		return new ArrayList();
	}

	get mapLehrer(): Map<number, LehrerListeEintrag> {
		return new Map();
	}

	get faechermanager(): GostFaecherManager {
		if (this._state.value.faecherManager === undefined) {
			throw new Error("Unerwarteter Fehler: Fächer-Manager nicht initialisiert");
		}
		return this._state.value.faecherManager;
	}

	set faecherManager(faecherManager: GostFaecherManager | undefined) {
		this.setPatchedState({ faecherManager });
	}

	get abiturdatenManager(): AbiturdatenManager {
		if (this._state.value.abiturdatenManager === undefined) {
			throw new Error("Unerwarteter Fehler: Abiturdaten-Manager nicht initialisiert");
		}
		return this._state.value.abiturdatenManager;
	}

	get id(): number | undefined {
		return undefined;
	}

	get zwischenspeicher(): Abiturdaten | undefined {
		return this._state.value.zwischenspeicher;
	}

	get hatZwischenspeicher(): boolean {
		return (this.zwischenspeicher !== undefined);
	}

	protected createAbiturdatenmanager(faecherManager?: GostFaecherManager, daten?: Abiturdaten): AbiturdatenManager | undefined {
		const abiturdaten = daten ?? this._state.value.abiturdaten;
		const fachManager = faecherManager ?? this._state.value.faecherManager;
		if ((abiturdaten === undefined) || (fachManager === undefined)) {
			return undefined;
		}
		const jahrgangsdaten = this._state.value.gostJahrgangsdaten;
		const art = this.gostBelegpruefungsArt;
		if (art === 'ef1') {
			return new AbiturdatenManager(abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.EF1);
		}
		if (art === 'gesamt') {
			return new AbiturdatenManager(abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.GESAMT);
		}
		const abiturdatenManager = new AbiturdatenManager(abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.GESAMT);
		if (abiturdatenManager.pruefeBelegungExistiert(abiturdatenManager.getFachbelegungen(), GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)) {
			return abiturdatenManager;
		}
		return new AbiturdatenManager(abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.EF1);
	}

	setGostBelegpruefungErgebnis = async () => {
		const abiturdatenManager = this.createAbiturdatenmanager();
		if (abiturdatenManager === undefined) {
			return;
		}
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({ abiturdatenManager, gostBelegpruefungErgebnis });
	};

	setWahl = async (fachID: number, wahl: GostSchuelerFachwahl) => {
		const abidaten = this._state.value.abiturdaten;
		if (abidaten === undefined) {
			throw new DeveloperNotificationException("Die Laufbahnplanungsdaten stehen unerwartet nicht zur Verfügung.");
		}
		const leereWahl = (wahl.halbjahre[0] === null) && (wahl.halbjahre[1] === null) && (wahl.halbjahre[2] === null) &&
			(wahl.halbjahre[3] === null) && (wahl.halbjahre[4] === null) && (wahl.halbjahre[5] === null);
		const belegung = this.abiturdatenManager.getFachbelegungByID(fachID);
		if (belegung === null) {
			this.fachbelegungErstellen(fachID, wahl);
		} else if (leereWahl) {
			this.fachbelegungEntfernen(fachID, wahl);
		} else {
			this.fachbelegungEntfernen(fachID, wahl);
			this.fachbelegungErstellen(fachID, wahl);
		}
		await this.setGostBelegpruefungErgebnis();
		this.setPatchedState({ dirty: true });
	};


	public getKlausurvorgabe(id: number | null): GostKlausurvorgabeEintrag | null {
		if (id === null) {
			return null;
		}
		return this._state.value.mapKlausurvorgaben.get(id);
	}


	public istGKLMoeglich(idFach: number, halbjahr: GostHalbjahr): List<GostKlausurvorgabeEintrag> {
		return this._state.value.gklMoeglich.getOrException(idFach, halbjahr);
	}


	private pruefeGKLWahl(idVorgabe: number | null, idFach: number, halbjahr: GostHalbjahr): boolean {
		if (idVorgabe === null) {
			return false;
		}
		const vorgabe = this.getKlausurvorgabe(idVorgabe);
		if ((vorgabe !== null) && (vorgabe.fach.id === idFach) && (vorgabe.halbjahr === halbjahr)) {
			return true;
		}
		return false;
	}


	public istGKLGewaehlt(idFach: number, halbjahr: GostHalbjahr): boolean {
		const result = ((halbjahr.istEinfuehrungsphase()
				&& (this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeEF_Sprachen, idFach, halbjahr)
					|| this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeEF_GW, idFach, halbjahr)
					|| this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeEF_NW, idFach, halbjahr)))
			|| (halbjahr.istQualifikationsphase()
				&& (this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeQ_Sprachen, idFach, halbjahr)
					|| this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeQ_GW, idFach, halbjahr)
					|| this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeQ_NW, idFach, halbjahr))));
		return result;
	}


	public get gklWahlen(): GostSchuelerGKLWahl {
		return this._state.value.gklWahlen;
	}


	public async patchGKLWahlen(patch: Partial<GostSchuelerGKLWahl>) {
		const neu = Object.assign(new GostSchuelerGKLWahl(), this._state.value.gklWahlen, patch);
		this.setPatchedState({ gklWahlen: neu });
	}


	exportLaufbahnplanung = async (): Promise<ApiFile> => {
		const json = GostLaufbahnplanungExportV2.transpilerToJSON(await this.schreibeV2Daten());
		const rawData = new Response(json).body;
		if (rawData === null) {
			throw new UserNotificationException("Unerwarteter Fehler beim Erstellen der Export-Daten aufgetreten.");
		}
		const compressedStream = rawData.pipeThrough(new CompressionStream('gzip'));
		const data = await new Response(compressedStream).blob();
		const name = `Laufbahnplanung_${this.gostJahrgangsdaten.abiturjahr}_${this.gostJahrgangsdaten.jahrgang}_${this.schueler.nachname}_${this.schueler.vorname}_${this.schueler.id}.lp`;
		this.setPatchedState({ dirty: false });
		return { data, name };
	};

	importLaufbahnplanung = async (formData: FormData): Promise<void> => {
		const gzData = formData.get("data");
		if (!(gzData instanceof File)) {
			throw new UserNotificationException("Es wurde keine gültige Datei angegeben");
		}
		const ds = new DecompressionStream("gzip");
		const rawData = await (new Response(gzData.stream().pipeThrough(ds))).text();
		const revision: number = JSON.parse(rawData).lpRevision;
		switch (revision) {
			case 1: {
				const laufbahnplanungsdaten = GostLaufbahnplanungExportV1.transpilerFromJSON(rawData);
				await this.ladeV1Daten(laufbahnplanungsdaten);
				break;
			}
			case 2: {
				const laufbahnplanungsdaten = GostLaufbahnplanungExportV2.transpilerFromJSON(rawData);
				await this.ladeV2Daten(laufbahnplanungsdaten);
				break;
			}
			default: {
				throw new UserNotificationException(`Die Revision der Laufbahnplanungsdatei (${revision}) entspricht nicht den unterstützen Revisionen 1 und 2`);
			}
		}
		await RouteManager.doRoute("laufbahnplanung");
	};

	patchBeratungsdaten = async (data: Partial<GostLaufbahnplanungBeratungsdaten>) => {
		throw new DeveloperNotificationException("Informationen zu Beratungsdaten sind in der Web-Laufbahnplanung nicht änderbar.");
	};

	saveLaufbahnplanung = async (): Promise<void> => {
		if (this._state.value.abiturdaten === undefined) {
			return;
		}
		const zwischenspeicher = Abiturdaten.transpilerFromJSON(Abiturdaten.transpilerToJSON(this._state.value.abiturdaten));
		this.setPatchedState({ zwischenspeicher });
	};

	restoreLaufbahnplanung = async (): Promise<void> => {
		if (this._state.value.zwischenspeicher === undefined) {
			return;
		}
		const abiturdaten = this._state.value.zwischenspeicher;
		const abiturdatenManager = this.createAbiturdatenmanager(this._state.value.faecherManager, abiturdaten);
		if (abiturdatenManager === undefined) {
			return;
		}
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({ zwischenspeicher: undefined, abiturdaten, abiturdatenManager, gostBelegpruefungErgebnis, dirty: true });
	};

	get gostBelegpruefungsArt(): GostBelegpruefungsModus {
		return this._state.value.gostBelegpruefungsArt;
	}

	setGostBelegpruefungsArt = async (gostBelegpruefungsArt: GostBelegpruefungsModus) => {
		this.setPatchedState({ gostBelegpruefungsArt });
		await this.setGostBelegpruefungErgebnis();
	};

	private ladeV2Sprachdaten(planungsdaten: GostLaufbahnplanungExportV2Schueler, abiturdaten: Abiturdaten): void {
		for (const bel of planungsdaten.sprachbelegungen) {
			const mappedBel = new Sprachbelegung();
			mappedBel.sprache = bel.sprache;
			mappedBel.istNachweis = bel.istNachweis;
			mappedBel.reihenfolge = bel.reihenfolge;
			mappedBel.belegungVonJahrgang = bel.belegungVonJahrgang;
			mappedBel.belegungVonAbschnitt = bel.belegungVonAbschnitt;
			mappedBel.belegungBisJahrgang = bel.belegungBisJahrgang;
			mappedBel.belegungBisAbschnitt = bel.belegungBisAbschnitt;
			mappedBel.referenzniveau = bel.referenzniveau;
			mappedBel.hatKleinesLatinum = bel.hatKleinesLatinum;
			mappedBel.hatLatinum = bel.hatLatinum;
			mappedBel.hatGraecum = bel.hatGraecum;
			mappedBel.hatHebraicum = bel.hatHebraicum;
			abiturdaten.sprachendaten.belegungen.add(mappedBel);
		}
		for (const pruef of planungsdaten.sprachpruefungen) {
			const mappedPruef = new Sprachpruefung();
			mappedPruef.sprache = pruef.sprache;
			mappedPruef.jahrgang = pruef.jahrgang;
			mappedPruef.anspruchsniveauId = pruef.anspruchsniveauId;
			mappedPruef.pruefungsdatum = pruef.pruefungsdatum;
			mappedPruef.ersetzteSprache = pruef.ersetzteSprache;
			mappedPruef.istHSUPruefung = pruef.istHSUPruefung;
			mappedPruef.istFeststellungspruefung = pruef.istFeststellungspruefung;
			mappedPruef.kannErstePflichtfremdspracheErsetzen = pruef.kannErstePflichtfremdspracheErsetzen;
			mappedPruef.kannZweitePflichtfremdspracheErsetzen = pruef.kannZweitePflichtfremdspracheErsetzen;
			mappedPruef.kannWahlpflichtfremdspracheErsetzen = pruef.kannWahlpflichtfremdspracheErsetzen;
			mappedPruef.kannBelegungAlsFortgefuehrteSpracheErlauben = pruef.kannBelegungAlsFortgefuehrteSpracheErlauben;
			mappedPruef.referenzniveau = pruef.referenzniveau;
			mappedPruef.note = pruef.note;
			mappedPruef.zeugnisbezeichnung = pruef.zeugnisbezeichnung;
			abiturdaten.sprachendaten.pruefungen.add(mappedPruef);
		}
		abiturdaten.bilingualeSprache = planungsdaten.bilingualeSprache;
	}

	private ladeV2Belegungen(planungsdaten: GostLaufbahnplanungExportV2Schueler, abiturdaten: Abiturdaten, faecherManager: GostFaecherManager): void {
		for (const hj of GostHalbjahr.values()) {
			abiturdaten.bewertetesHalbjahr[hj.id] = planungsdaten.bewertetesHalbjahr[hj.id];
		}
		for (let i = 0; i < planungsdaten.fachbelegungen.size() ; i++) {
			const belegung = new AbiturFachbelegung();
			const fb = planungsdaten.fachbelegungen.get(i);
			const fach = faecherManager.get(fb.fachID);
			if (fach === null) {
				continue;
			}
			belegung.fachID = fb.fachID;
			belegung.abiturFach = fb.abiturFach;
			belegung.idReferenzfach = fb.idReferenzfach;
			belegung.istFSNeu = fach.istFremdSpracheNeuEinsetzend;
			for (const hj of GostHalbjahr.values()) {
				const kursart = fb.kursart[hj.id];
				if (kursart === null) {
					continue;
				}
				const hjBelegung = new AbiturFachbelegungHalbjahr();
				hjBelegung.halbjahrKuerzel = hj.kuerzel;
				hjBelegung.kursartKuerzel = kursart;
				hjBelegung.schriftlich = fb.schriftlich[hj.id];
				hjBelegung.biliSprache = fach.biliSprache;
				if (fach.kuerzel === "PX") {
					hjBelegung.wochenstunden = fach.wochenstundenQualifikationsphase;
				}
				if (kursart === "AT") {
					hjBelegung.notenkuerzel = "AT";
				}
				belegung.belegungen[hj.id] = hjBelegung;
				belegung.letzteKursart = kursart;
			}
			abiturdaten.fachbelegungen.add(belegung);
		}
	}

	private ladeV2Faecher(faecher: List<GostLaufbahnplanungExportV2Fach>): List<GostFach> {
		const result = new ArrayList<GostFach>();
		for (const fach of faecher) {
			const f = new GostFach();
			f.id = fach.id;
			f.kuerzel = fach.kuerzel;
			f.kuerzelAnzeige = fach.kuerzelAnzeige;
			f.bezeichnung = fach.bezeichnung;
			f.sortierung = fach.sortierung;
			f.istPruefungsordnungsRelevant = fach.istPruefungsordnungsRelevant;
			f.istFremdsprache = fach.istFremdsprache;
			f.istFremdSpracheNeuEinsetzend = fach.istFremdSpracheNeuEinsetzend;
			f.biliSprache = fach.biliSprache;
			f.istMoeglichAbiLK = fach.istMoeglichAbiLK;
			f.istMoeglichAbiGK = fach.istMoeglichAbiGK;
			f.istMoeglichEF1 = fach.istMoeglich[0];
			f.istMoeglichEF2 = fach.istMoeglich[1];
			f.istMoeglichQ11 = fach.istMoeglich[2];
			f.istMoeglichQ12 = fach.istMoeglich[3];
			f.istMoeglichQ21 = fach.istMoeglich[4];
			f.istMoeglichQ22 = fach.istMoeglich[5];
			f.wochenstundenQualifikationsphase = fach.wochenstundenQualifikationsphase;
			f.projektKursLeitfach1ID = fach.referenzfach1ID;
			f.projektKursLeitfach2ID = fach.referenzfach2ID;
			result.add(f);
		}
		return result;
	}

	private ladeV2GKL(planungsdaten: GostLaufbahnplanungExportV2Schueler, listMoeglich: List<GostLaufbahnplanungExportV2GKL>, faecherManager: GostFaecherManager) {
		const gklMoeglich = new HashMap2D<number, GostHalbjahr, List<GostKlausurvorgabeEintrag>>();
		for (const fach of faecherManager.faecher()) {
			for (const halbjahr of GostHalbjahr.values()) {
				gklMoeglich.put(fach.id, halbjahr, new ArrayList<GostKlausurvorgabeEintrag>());
			}
		}

		const mapKlausurvorgaben = new HashMap<number, GostKlausurvorgabeEintrag>();
		for (const moeglich of listMoeglich) {
			const vorgabe = new GostKlausurvorgabe();
			vorgabe.id = moeglich.id;
			vorgabe.idFach = moeglich.idFach;
			vorgabe.halbjahr = moeglich.idHalbjahr;
			vorgabe.quartal = moeglich.quartal;
			const halbjahr = GostHalbjahr.fromIDorException(vorgabe.halbjahr);
			const fach = faecherManager.get(vorgabe.idFach);
			if (fach === null) {
				continue;
			}
			const eintrag: GostKlausurvorgabeEintrag = { fach, halbjahr, vorgabe };
			mapKlausurvorgaben.put(vorgabe.id, eintrag);

			gklMoeglich.getOrException(vorgabe.idFach, halbjahr).add(eintrag);
		}

		const gklWahlen = new GostSchuelerGKLWahl();
		gklWahlen.idKlausurvorgabeEF_Sprachen = planungsdaten.gkl[0];
		gklWahlen.idKlausurvorgabeEF_GW = planungsdaten.gkl[1];
		gklWahlen.idKlausurvorgabeEF_NW = planungsdaten.gkl[2];
		gklWahlen.idKlausurvorgabeQ_Sprachen = planungsdaten.gkl[3];
		gklWahlen.idKlausurvorgabeQ_GW = planungsdaten.gkl[4];
		gklWahlen.idKlausurvorgabeQ_NW = planungsdaten.gkl[5];

		return { mapKlausurvorgaben, gklMoeglich, gklWahlen };
	}


	public async ladeV2Daten(daten: GostLaufbahnplanungExportV2) {
		// Lade die Informationen zur Schule
		const schuleStammdaten = new SchuleStammdaten();
		schuleStammdaten.schulNr = daten.schulNr;
		schuleStammdaten.bezeichnung1 = daten.schulBezeichnung1;
		schuleStammdaten.bezeichnung2 = daten.schulBezeichnung2;
		schuleStammdaten.bezeichnung3 = daten.schulBezeichnung3;
		// Lade die Jahrgangsinformationen
		const gostJahrgang = new GostJahrgang();
		gostJahrgang.abiturjahr = daten.abiturjahr;
		gostJahrgang.jahrgang = daten.jahrgang;
		gostJahrgang.bezeichnung = "Abiturjahr " + daten.abiturjahr;
		gostJahrgang.istAbgeschlossen = false;
		const gostJahrgangsdaten = new GostJahrgangsdaten();
		gostJahrgangsdaten.abiturjahr = gostJahrgang.abiturjahr;
		gostJahrgangsdaten.jahrgang = gostJahrgang.jahrgang;
		gostJahrgangsdaten.bezeichnung = gostJahrgang.bezeichnung;
		gostJahrgangsdaten.istAbgeschlossen = gostJahrgang.istAbgeschlossen;
		gostJahrgangsdaten.hatZusatzkursGE = daten.hatZusatzkursGE;
		gostJahrgangsdaten.beginnZusatzkursGE = daten.beginnZusatzkursGE;
		gostJahrgangsdaten.hatZusatzkursSW = daten.hatZusatzkursSW;
		gostJahrgangsdaten.beginnZusatzkursSW = daten.beginnZusatzkursSW;
		gostJahrgangsdaten.textBeratungsbogen = daten.textBeratungsbogen;
		gostJahrgangsdaten.textMailversand = null;
		// Initialisiere den Fächer-Manager mit den Fächerdaten
		const faecher = this.ladeV2Faecher(daten.faecher);
		const faecherManager = new GostFaecherManager(daten.abiturjahr - 1, faecher);
		faecherManager.addFachkombinationenAll(daten.fachkombinationen);
		// Bestimme die importierten Laufbahnplanungsdaten für den Schüler
		const planungsdaten = daten.schueler.get(0);
		// Erstelle das Schüler-Objekt für die Anzeige
		const schueler = new SchuelerListeEintrag();
		schueler.id = planungsdaten.id;
		schueler.vorname = planungsdaten.vorname;
		schueler.nachname = planungsdaten.nachname;
		schueler.geschlecht = planungsdaten.geschlecht;
		schueler.abiturjahrgang = gostJahrgang.abiturjahr;

		schueler.idSchulgliederung = planungsdaten.istG8
			? Schulgliederung.GY8.historie().getLast().id
			: Schulgliederung.GY9.historie().getLast().id;

		// Erstelle das Abiturdaten-Objekt und lade die Sprachinformationen und die Fachbelegungen
		const abiturdaten = new Abiturdaten();
		abiturdaten.abiturjahr = daten.abiturjahr;
		this.ladeV2Sprachdaten(planungsdaten, abiturdaten);
		this.ladeV2Belegungen(planungsdaten, abiturdaten, faecherManager);

		const { mapKlausurvorgaben, gklMoeglich, gklWahlen } = this.ladeV2GKL(planungsdaten, daten.gkl, faecherManager);

		// Erstelle den Abiturdaten-Manager
		const abiturdatenManager = this.createAbiturdatenmanager(faecherManager, abiturdaten);
		if (abiturdatenManager === undefined) {
			throw new UserNotificationException("Belegprüfungsergebnis konnte nicht berechnet werden.");
		}
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		gostJahrgangsdaten.istBlockungFestgelegt = abiturdaten.bewertetesHalbjahr;
		this.setPatchedDefaultState({
			schuleStammdaten,
			auswahl: schueler,
			schuelerIDEncrypted: planungsdaten.idEnc,
			beratungslehrer: daten.beratungslehrer,
			gostJahrgang,
			gostJahrgangsdaten,
			faecherManager,
			abiturdaten,
			abiturdatenManager: abiturdatenManager,
			gostBelegpruefungErgebnis,
			mapKlausurvorgaben,
			gklMoeglich,
			gklWahlen,
		});
	}


	private ladeV1Sprachdaten(planungsdaten: GostLaufbahnplanungExportV1Schueler, abiturdaten: Abiturdaten): void {
		for (const bel of planungsdaten.sprachendaten.belegungen) {
			const mappedBel = new Sprachbelegung();
			mappedBel.sprache = bel.sprache;
			mappedBel.istNachweis = bel.istNachweis ?? false;
			mappedBel.reihenfolge = bel.reihenfolge;
			mappedBel.belegungVonJahrgang = bel.belegungVonJahrgang;
			mappedBel.belegungVonAbschnitt = bel.belegungVonAbschnitt;
			mappedBel.belegungBisJahrgang = bel.belegungBisJahrgang;
			mappedBel.belegungBisAbschnitt = bel.belegungBisAbschnitt;
			mappedBel.referenzniveau = bel.referenzniveau;
			mappedBel.hatKleinesLatinum = bel.hatKleinesLatinum;
			mappedBel.hatLatinum = bel.hatLatinum;
			mappedBel.hatGraecum = bel.hatGraecum;
			mappedBel.hatHebraicum = bel.hatHebraicum;
			abiturdaten.sprachendaten.belegungen.add(mappedBel);
		}
		for (const pruef of planungsdaten.sprachendaten.pruefungen) {
			const mappedPruef = new Sprachpruefung();
			mappedPruef.sprache = pruef.sprache;
			mappedPruef.jahrgang = pruef.jahrgang;
			mappedPruef.anspruchsniveauId = pruef.anspruchsniveauId;
			mappedPruef.pruefungsdatum = pruef.pruefungsdatum;
			mappedPruef.ersetzteSprache = pruef.ersetzteSprache;
			mappedPruef.istHSUPruefung = pruef.istHSUPruefung;
			mappedPruef.istFeststellungspruefung = pruef.istFeststellungspruefung;
			mappedPruef.kannErstePflichtfremdspracheErsetzen = pruef.kannErstePflichtfremdspracheErsetzen;
			mappedPruef.kannZweitePflichtfremdspracheErsetzen = pruef.kannZweitePflichtfremdspracheErsetzen;
			mappedPruef.kannWahlpflichtfremdspracheErsetzen = pruef.kannWahlpflichtfremdspracheErsetzen;
			mappedPruef.kannBelegungAlsFortgefuehrteSpracheErlauben = pruef.kannBelegungAlsFortgefuehrteSpracheErlauben;
			mappedPruef.referenzniveau = pruef.referenzniveau;
			mappedPruef.note = pruef.note;
			mappedPruef.zeugnisbezeichnung = pruef.zeugnisbezeichnung;
			abiturdaten.sprachendaten.pruefungen.add(mappedPruef);
		}
		abiturdaten.bilingualeSprache = planungsdaten.bilingualeSprache;
	}

	private ladeV1Belegungen(planungsdaten: GostLaufbahnplanungExportV1Schueler, abiturdaten: Abiturdaten, faecherManager: GostFaecherManager): void {
		for (const hj of GostHalbjahr.values()) {
			abiturdaten.bewertetesHalbjahr[hj.id] = planungsdaten.bewertetesHalbjahr[hj.id];
		}
		for (let i = 0; i < planungsdaten.fachbelegungen.size() ; i++) {
			const belegung = new AbiturFachbelegung();
			const fb = planungsdaten.fachbelegungen.get(i);
			const fach = faecherManager.get(fb.fachID);
			if (fach === null) {
				continue;
			}
			belegung.fachID = fb.fachID;
			belegung.abiturFach = fb.abiturFach;
			belegung.istFSNeu = fach.istFremdSpracheNeuEinsetzend;
			for (const hj of GostHalbjahr.values()) {
				const kursart = fb.kursart[hj.id];
				if (kursart === null) {
					continue;
				}
				const hjBelegung = new AbiturFachbelegungHalbjahr();
				hjBelegung.halbjahrKuerzel = hj.kuerzel;
				hjBelegung.kursartKuerzel = kursart;
				hjBelegung.schriftlich = fb.schriftlich[hj.id];
				hjBelegung.biliSprache = fach.biliSprache;
				if (fach.kuerzel === "PX") {
					hjBelegung.wochenstunden = fach.wochenstundenQualifikationsphase;
				}
				if (kursart === "AT") {
					hjBelegung.notenkuerzel = "AT";
				}
				belegung.belegungen[hj.id] = hjBelegung;
				belegung.letzteKursart = kursart;
			}
			abiturdaten.fachbelegungen.add(belegung);
		}
	}

	public async ladeV1Daten(daten: GostLaufbahnplanungExportV1) {
		// Lade die Informationen zur Schule
		const schuleStammdaten = new SchuleStammdaten();
		schuleStammdaten.schulNr = daten.schulNr;
		schuleStammdaten.bezeichnung1 = daten.schulBezeichnung1;
		schuleStammdaten.bezeichnung2 = daten.schulBezeichnung2;
		schuleStammdaten.bezeichnung3 = daten.schulBezeichnung3;
		// Lade die Jahrgangsinformationen
		const gostJahrgang = new GostJahrgang();
		gostJahrgang.abiturjahr = daten.abiturjahr;
		gostJahrgang.jahrgang = daten.jahrgang;
		gostJahrgang.bezeichnung = "Abiturjahr " + daten.abiturjahr;
		gostJahrgang.istAbgeschlossen = false;
		const gostJahrgangsdaten = new GostJahrgangsdaten();
		gostJahrgangsdaten.abiturjahr = gostJahrgang.abiturjahr;
		gostJahrgangsdaten.jahrgang = gostJahrgang.jahrgang;
		gostJahrgangsdaten.bezeichnung = gostJahrgang.bezeichnung;
		gostJahrgangsdaten.istAbgeschlossen = gostJahrgang.istAbgeschlossen;
		gostJahrgangsdaten.hatZusatzkursGE = daten.hatZusatzkursGE;
		gostJahrgangsdaten.beginnZusatzkursGE = daten.beginnZusatzkursGE;
		gostJahrgangsdaten.hatZusatzkursSW = daten.hatZusatzkursSW;
		gostJahrgangsdaten.beginnZusatzkursSW = daten.beginnZusatzkursSW;
		gostJahrgangsdaten.textBeratungsbogen = daten.textBeratungsbogen;
		gostJahrgangsdaten.textMailversand = null;
		// Initialisiere den Fächer-Manager mit den Fächerdaten
		const faecherManager = new GostFaecherManager(daten.abiturjahr - 1, daten.faecher);
		faecherManager.addFachkombinationenAll(daten.fachkombinationen);
		// Bestimme die importierten Laufbahnplanungsdaten für den Schüler
		const planungsdaten = daten.schueler.get(0);
		// Erstelle das Schüler-Objekt für die Anzeige
		const schueler = new SchuelerListeEintrag();
		schueler.id = planungsdaten.id;
		schueler.vorname = planungsdaten.vorname;
		schueler.nachname = planungsdaten.nachname;
		schueler.geschlecht = planungsdaten.geschlecht;
		schueler.abiturjahrgang = gostJahrgang.abiturjahr;

		// Erstelle das Abiturdaten-Objekt und lade die Sprachinformationen und die Fachbelegungen
		const abiturdaten = new Abiturdaten();
		abiturdaten.abiturjahr = daten.abiturjahr;
		this.ladeV1Sprachdaten(planungsdaten, abiturdaten);
		this.ladeV1Belegungen(planungsdaten, abiturdaten, faecherManager);

		// Erstelle den Abiturdaten-Manager
		const abiturdatenManager = this.createAbiturdatenmanager(faecherManager, abiturdaten);
		if (abiturdatenManager === undefined) {
			throw new UserNotificationException("Belegprüfungsergebnis konnte nicht berechnet werden.");
		}
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		gostJahrgangsdaten.istBlockungFestgelegt = abiturdaten.bewertetesHalbjahr;
		this.setPatchedDefaultState({
			schuleStammdaten,
			auswahl: schueler,
			schuelerIDEncrypted: planungsdaten.idEnc,
			beratungslehrer: daten.beratungslehrer,
			gostJahrgang,
			gostJahrgangsdaten,
			faecherManager,
			abiturdaten,
			abiturdatenManager: abiturdatenManager,
			gostBelegpruefungErgebnis,
		});
	}


	private schreibeV2Faecher(from: List<GostFach>, to: List<GostLaufbahnplanungExportV2Fach>): void {
		for (const fach of from) {
			const f = new GostLaufbahnplanungExportV2Fach();
			f.id = fach.id;
			f.kuerzel = fach.kuerzel;
			f.kuerzelAnzeige = fach.kuerzelAnzeige;
			f.bezeichnung = fach.bezeichnung;
			f.sortierung = fach.sortierung;
			f.istPruefungsordnungsRelevant = fach.istPruefungsordnungsRelevant;
			f.istFremdsprache = fach.istFremdsprache;
			f.istFremdSpracheNeuEinsetzend = fach.istFremdSpracheNeuEinsetzend;
			f.biliSprache = fach.biliSprache;
			f.istMoeglichAbiLK = fach.istMoeglichAbiLK;
			f.istMoeglichAbiGK = fach.istMoeglichAbiGK;
			f.istMoeglich[0] = fach.istMoeglichEF1;
			f.istMoeglich[1] = fach.istMoeglichEF2;
			f.istMoeglich[2] = fach.istMoeglichQ11;
			f.istMoeglich[3] = fach.istMoeglichQ12;
			f.istMoeglich[4] = fach.istMoeglichQ21;
			f.istMoeglich[5] = fach.istMoeglichQ22;
			f.wochenstundenQualifikationsphase = fach.wochenstundenQualifikationsphase;
			f.referenzfach1ID = fach.projektKursLeitfach1ID;
			f.referenzfach2ID = fach.projektKursLeitfach2ID;
			to.add(f);
		}
	}


	private schreibeV2GKL(daten: GostLaufbahnplanungExportV2, schueler: GostLaufbahnplanungExportV2Schueler): void {
		for (const eintrag of this._state.value.mapKlausurvorgaben.values()) {
			const vorgabe = eintrag.vorgabe;
			const gkl = new GostLaufbahnplanungExportV2GKL();
			gkl.id = vorgabe.id;
			gkl.idFach = vorgabe.idFach;
			gkl.idHalbjahr = vorgabe.halbjahr;
			gkl.quartal = vorgabe.halbjahr;
			daten.gkl.add(gkl);
		}
		const wahlen = this._state.value.gklWahlen;
		schueler.gkl[0] = wahlen.idKlausurvorgabeEF_Sprachen;
		schueler.gkl[1] = wahlen.idKlausurvorgabeEF_GW;
		schueler.gkl[2] = wahlen.idKlausurvorgabeEF_NW;
		schueler.gkl[3] = wahlen.idKlausurvorgabeQ_Sprachen;
		schueler.gkl[4] = wahlen.idKlausurvorgabeQ_GW;
		schueler.gkl[5] = wahlen.idKlausurvorgabeQ_NW;
	}


	public async schreibeV2Daten(): Promise<GostLaufbahnplanungExportV2> {
		const abiturdaten = this._state.value.abiturdaten;
		const faecherManager = this._state.value.faecherManager;
		const auswahl = this._state.value.auswahl;
		if ((faecherManager === undefined) || (abiturdaten === undefined) || (auswahl === undefined)) {
			throw new UserNotificationException("Es müssen Abiturdaten geladen sein.");
		}

		const daten = new GostLaufbahnplanungExportV2();
		daten.schulNr = this._state.value.schuleStammdaten.schulNr;
		daten.schulBezeichnung1 = this._state.value.schuleStammdaten.bezeichnung1;
		daten.schulBezeichnung2 = this._state.value.schuleStammdaten.bezeichnung2 ?? "";
		daten.schulBezeichnung3 = this._state.value.schuleStammdaten.bezeichnung3 ?? "";
		daten.anmerkungen = "Letzte Änderung am " + (new Date()).toLocaleDateString("de-DE", { dateStyle: "short" });
		daten.abiturjahr = abiturdaten.abiturjahr;
		daten.jahrgang = this._state.value.gostJahrgang.jahrgang;
		daten.hatZusatzkursGE = this._state.value.gostJahrgangsdaten.hatZusatzkursGE;
		daten.beginnZusatzkursGE = this._state.value.gostJahrgangsdaten.beginnZusatzkursGE;
		daten.hatZusatzkursSW = this._state.value.gostJahrgangsdaten.hatZusatzkursSW;
		daten.beginnZusatzkursSW = this._state.value.gostJahrgangsdaten.beginnZusatzkursSW;
		daten.beratungslehrer.addAll(this._state.value.beratungslehrer);
		daten.textBeratungsbogen = this._state.value.gostJahrgangsdaten.textBeratungsbogen;
		for (const fk of faecherManager.getFachkombinationen()) {
			daten.fachkombinationen.add(fk);
		}
		this.schreibeV2Faecher(faecherManager.faecher(), daten.faecher);
		const s = new GostLaufbahnplanungExportV2Schueler();
		s.id = auswahl.id;
		s.idEnc = this._state.value.schuelerIDEncrypted;
		s.vorname = auswahl.vorname;
		s.nachname = auswahl.nachname;
		s.geschlecht = auswahl.geschlecht;
		s.bilingualeSprache = abiturdaten.bilingualeSprache;
		s.sprachbelegungen = abiturdaten.sprachendaten.belegungen;
		s.sprachpruefungen = abiturdaten.sprachendaten.pruefungen;
		for (const hj of GostHalbjahr.values()) {
			s.bewertetesHalbjahr[hj.id] = abiturdaten.bewertetesHalbjahr[hj.id];
		}
		s.istG8 = (Schulgliederung.data().getWertByIDOrNull(auswahl.idSchulgliederung) === Schulgliederung.GY8);
		for (let i = 0; i < abiturdaten.fachbelegungen.size() ; i++) {
			const belegung = abiturdaten.fachbelegungen.get(i);
			const fb = new GostLaufbahnplanungExportV2SchuelerFachbelegung();
			fb.fachID = belegung.fachID;
			fb.abiturFach = belegung.abiturFach;
			fb.idReferenzfach = belegung.idReferenzfach;
			for (const hj of GostHalbjahr.values()) {
				const hjBelegung = belegung.belegungen[hj.id];
				if ((hjBelegung === null) || (hjBelegung.kursartKuerzel === "")) {
					continue;
				}
				fb.kursart[hj.id] = hjBelegung.kursartKuerzel;
				fb.schriftlich[hj.id] = hjBelegung.schriftlich;
			}
			s.fachbelegungen.add(fb);
		}
		daten.schueler.add(s);
		this.schreibeV2GKL(daten, s);
		return daten;
	}


	public async schreibeV1Daten(): Promise<GostLaufbahnplanungExportV1> {
		if ((this._state.value.faecherManager === undefined) || (this._state.value.abiturdaten === undefined) || (this._state.value.auswahl === undefined)) {
			throw new UserNotificationException("Es müssen Abiturdaten geladen sein.");
		}
		const daten = new GostLaufbahnplanungExportV1();
		daten.schulNr = this._state.value.schuleStammdaten.schulNr;
		daten.schulBezeichnung1 = this._state.value.schuleStammdaten.bezeichnung1;
		daten.schulBezeichnung2 = this._state.value.schuleStammdaten.bezeichnung2 ?? "";
		daten.schulBezeichnung3 = this._state.value.schuleStammdaten.bezeichnung3 ?? "";
		daten.anmerkungen = "Letzte Änderung am " + (new Date()).toLocaleDateString("de-DE", { dateStyle: "short" });
		daten.abiturjahr = this._state.value.abiturdaten.abiturjahr;
		daten.jahrgang = this._state.value.gostJahrgang.jahrgang;
		daten.hatZusatzkursGE = this._state.value.gostJahrgangsdaten.hatZusatzkursGE;
		daten.beginnZusatzkursGE = this._state.value.gostJahrgangsdaten.beginnZusatzkursGE;
		daten.hatZusatzkursSW = this._state.value.gostJahrgangsdaten.hatZusatzkursSW;
		daten.beginnZusatzkursSW = this._state.value.gostJahrgangsdaten.beginnZusatzkursSW;
		daten.beratungslehrer.addAll(this._state.value.beratungslehrer);
		daten.textBeratungsbogen = this._state.value.gostJahrgangsdaten.textBeratungsbogen;
		for (const fk of this._state.value.faecherManager.getFachkombinationen()) {
			daten.fachkombinationen.add(fk);
		}
		daten.faecher.addAll(this._state.value.faecherManager.faecher());
		const s = new GostLaufbahnplanungExportV1Schueler();
		s.id = this._state.value.auswahl.id;
		s.idEnc = this._state.value.schuelerIDEncrypted;
		s.vorname = this._state.value.auswahl.vorname;
		s.nachname = this._state.value.auswahl.nachname;
		s.geschlecht = this._state.value.auswahl.geschlecht;
		s.bilingualeSprache = this._state.value.abiturdaten.bilingualeSprache;
		s.sprachendaten = this._state.value.abiturdaten.sprachendaten;
		for (const hj of GostHalbjahr.values()) {
			s.bewertetesHalbjahr[hj.id] = this._state.value.abiturdaten.bewertetesHalbjahr[hj.id];
		}
		for (let i = 0; i < this._state.value.abiturdaten.fachbelegungen.size() ; i++) {
			const belegung = this._state.value.abiturdaten.fachbelegungen.get(i);
			const fb = new GostLaufbahnplanungExportV1Fachbelegung();
			fb.fachID = belegung.fachID;
			fb.abiturFach = belegung.abiturFach;
			for (const hj of GostHalbjahr.values()) {
				const hjBelegung = belegung.belegungen[hj.id];
				if (hjBelegung === null) {
					continue;
				}
				fb.kursart[hj.id] = hjBelegung.kursartKuerzel;
				fb.schriftlich[hj.id] = hjBelegung.schriftlich;
			}
			s.fachbelegungen.add(fb);
		}
		daten.schueler.add(s);
		return daten;
	}

	resetFachwahlen = async (forceDelete: boolean) => {
		const abiturdaten = this._state.value.abiturdaten;
		if (abiturdaten === undefined) {
			throw new DeveloperNotificationException("Die Laufbahnplanungsdaten stehen unerwartet nicht zur Verfügung.");
		}
		for (const fachbelegung of abiturdaten.fachbelegungen) {
			fachbelegung.abiturFach = null;
			for (let i = 0; i < this.gostJahrgangsdaten.istBlockungFestgelegt.length; i++) {
				if (this.gostJahrgangsdaten.istBlockungFestgelegt[i] === true) {
					continue;
				} else {
					fachbelegung.belegungen[i] = null;
				}
			}
		}
		const temp = Abiturdaten.transpilerFromJSON(Abiturdaten.transpilerToJSON(abiturdaten));
		const abiturdatenManager = this.createAbiturdatenmanager(this._state.value.faecherManager, temp);
		if (abiturdatenManager === undefined) {
			return;
		}
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({ abiturdaten, abiturdatenManager, gostBelegpruefungErgebnis });
	};

	gotoKursplanung = async (halbjahr: GostHalbjahr): Promise<void> => {
		throw new DeveloperNotificationException("Die Kursplanung steht in der Web-Laufbahnplanung nicht zur Verfügung.");
	};

	public async addBeratungslehrer(id: number) {
		throw new DeveloperNotificationException("Beratungslehrer können in der Web-Laufbahnplanung nicht hinzugefügt werden.");
	}

	public async removeBeratungslehrer(eintraege: GostBeratungslehrer[]) {
		throw new DeveloperNotificationException("Beratungslehrer können in der Web-Laufbahnplanung nicht entfernt werden.");
	}

	get hatAuswahl(): boolean {
		return (this._state.value.auswahl !== undefined);
	}

	protected fachbelegungErstellen(fachID: number, wahl: GostSchuelerFachwahl): void {
		const faecherManager = this.abiturdatenManager.faecher();
		const abidaten = this._state.value.abiturdaten;
		if (abidaten === undefined) {
			throw new DeveloperNotificationException("Die Laufbahnplanungsdaten stehen unerwartet nicht zur Verfügung.");
		}
		const belegung = new AbiturFachbelegung();
		const fach = faecherManager.get(fachID);
		if (fach === null) {
			throw new DeveloperNotificationException("Das Fach mit der ID " + fachID + " steht unerwartet nicht zur Verfügung.");
		}
		belegung.fachID = fachID;
		belegung.abiturFach = wahl.abiturFach;
		belegung.idReferenzfach = wahl.idReferenzfach;
		belegung.istFSNeu = fach.istFremdSpracheNeuEinsetzend;
		for (const hj of GostHalbjahr.values()) {
			const w = wahl.halbjahre[hj.id];
			if (w === null) {
				continue;
			}
			const hjBelegung = new AbiturFachbelegungHalbjahr();
			hjBelegung.halbjahrKuerzel = hj.kuerzel;
			if (w === "M") {
				if (fach.kuerzel === "PX") {
					hjBelegung.kursartKuerzel = GostKursart.PJK.kuerzel;
					hjBelegung.wochenstunden = fach.wochenstundenQualifikationsphase;
				} else if (fach.kuerzel === "VX") {
					hjBelegung.kursartKuerzel = GostKursart.VTF.kuerzel;
				} else {
					hjBelegung.kursartKuerzel = GostKursart.GK.kuerzel;
				}
				hjBelegung.schriftlich = false;
			} else if (w === "ZK") {
				hjBelegung.kursartKuerzel = GostKursart.ZK.kuerzel;
				hjBelegung.schriftlich = false;
			} else if (w === "S") {
				hjBelegung.schriftlich = true;
				if (fach.kuerzel === "PX") {
					hjBelegung.kursartKuerzel = GostKursart.PJK.kuerzel;
					hjBelegung.wochenstunden = fach.wochenstundenQualifikationsphase;
				} else {
					hjBelegung.kursartKuerzel = GostKursart.GK.kuerzel;
				}
			} else if (w === "LK") {
				hjBelegung.kursartKuerzel = GostKursart.LK.kuerzel;
				hjBelegung.schriftlich = true;
			} else if (w === "AT") {
				hjBelegung.kursartKuerzel = w;
				hjBelegung.schriftlich = false;
				hjBelegung.notenkuerzel = w;
			}
			hjBelegung.biliSprache = fach.biliSprache;
			belegung.belegungen[hj.id] = hjBelegung;
			belegung.letzteKursart = hjBelegung.kursartKuerzel;
		}
		abidaten.fachbelegungen.add(belegung);
	}

	protected fachbelegungEntfernen(fachID: number, wahl: GostSchuelerFachwahl): void {
		const abidaten = this._state.value.abiturdaten;
		if (abidaten === undefined) {
			throw new DeveloperNotificationException("Die Laufbahnplanungsdaten stehen unerwartet nicht zur Verfügung.");
		}
		for (let i = 0; i < abidaten.fachbelegungen.size(); i++) {
			if (abidaten.fachbelegungen.get(i).fachID === fachID) {
				abidaten.fachbelegungen.removeElementAt(i);
			}
		}
	}

	public get config(): Config {
		return this._state.value.config;
	}

}


export const gostLaufbahnplanungStateImpl = new GostLaufbahnplanungStateImpl();
