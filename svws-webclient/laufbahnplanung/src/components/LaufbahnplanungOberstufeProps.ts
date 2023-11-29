import type { GostSchuelerFachwahl, SchuelerListeEintrag, GostJahrgangsdaten, GostBelegpruefungErgebnis, AbiturdatenManager,
	GostLaufbahnplanungBeratungsdaten, LehrerListeEintrag, GostLaufbahnplanungDaten, ApiFile } from "@core";

export interface LaufbahnplanungOberstufeProps {
	setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
	setGostBelegpruefungsArt: (value: 'ef1'|'gesamt'|'auto') => Promise<void>;
	exportLaufbahnplanung: () => Promise<ApiFile>;
	importLaufbahnplanung: (data: FormData) => Promise<boolean>;
	schueler: SchuelerListeEintrag,
	gostJahrgangsdaten: GostJahrgangsdaten;
	gostBelegpruefungsArt: () => 'ef1'|'gesamt'|'auto';
	gostBelegpruefungErgebnis: () => GostBelegpruefungErgebnis;
	abiturdatenManager: () => AbiturdatenManager;
	id?: number;
	zwischenspeicher?: GostLaufbahnplanungDaten;
	saveLaufbahnplanung: () => Promise<void>;
	restoreLaufbahnplanung: () => Promise<void>;
	resetFachwahlen: () => Promise<void>;
	modus: 'manuell'|'normal'|'hochschreiben';
	setModus: (modus: 'manuell'|'normal'|'hochschreiben') => Promise<void>;
}
