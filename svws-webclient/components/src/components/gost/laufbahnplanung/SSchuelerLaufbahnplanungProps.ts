import type { GostSchuelerFachwahl, SchuelerListeEintrag, GostJahrgangsdaten, GostBelegpruefungErgebnis, AbiturdatenManager,
	GostLaufbahnplanungBeratungsdaten, LehrerListeEintrag, GostLaufbahnplanungDaten, ApiFile, SimpleOperationResponse, GostHalbjahr } from "@core";

export interface SchuelerLaufbahnplanungProps {
	setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
	setGostBelegpruefungsArt: (value: 'ef1'|'gesamt'|'auto') => Promise<void>;
	getPdfWahlbogen: (title: string) => Promise<ApiFile>;
	exportLaufbahnplanung: () => Promise<ApiFile>;
	importLaufbahnplanung: (data: FormData) => Promise<boolean|SimpleOperationResponse>;
	schueler: SchuelerListeEintrag,
	gostJahrgangsdaten: GostJahrgangsdaten;
	gostLaufbahnBeratungsdaten: () => GostLaufbahnplanungBeratungsdaten;
	patchBeratungsdaten: (data : Partial<GostLaufbahnplanungBeratungsdaten>) => Promise<void>;
	gostBelegpruefungsArt: () => 'ef1'|'gesamt'|'auto';
	gostBelegpruefungErgebnis: () => GostBelegpruefungErgebnis;
	abiturdatenManager: () => AbiturdatenManager;
	mapLehrer: Map<number, LehrerListeEintrag>;
	id?: number;
	zwischenspeicher?: GostLaufbahnplanungDaten;
	saveLaufbahnplanung: () => Promise<void>;
	restoreLaufbahnplanung: () => Promise<void>;
	resetFachwahlen: () => Promise<void>;
	modus: 'manuell'|'normal'|'hochschreiben';
	setModus: (modus: 'manuell'|'normal'|'hochschreiben') => Promise<void>;
	faecherNichtWaehlbarAusblenden: boolean;
	setFaecherNichtWaehlbarAusblenden: (value: boolean) => Promise<void>;
	gotoKursblockung: (halbjahr: GostHalbjahr) => Promise<void>
}