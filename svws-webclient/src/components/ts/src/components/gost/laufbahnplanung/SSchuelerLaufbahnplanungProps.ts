import type { GostSchuelerFachwahl, SchuelerListeEintrag, GostJahrgangsdaten, GostBelegpruefungErgebnis, AbiturdatenManager, GostFaecherManager, GostJahrgangFachkombination, GostLaufbahnplanungBeratungsdaten, LehrerListeEintrag, GostLaufbahnplanungDaten } from "@svws-nrw/svws-core";

export interface SchuelerLaufbahnplanungProps {
	setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
	setGostBelegpruefungsArt: (value: 'ef1'|'gesamt'|'auto') => Promise<void>;
	getPdfWahlbogen: () => Promise<Blob>;
	exportLaufbahnplanung: () => Promise<Blob>;
	importLaufbahnplanung: (data: FormData) => Promise<boolean>;
	schueler: SchuelerListeEintrag,
	gostJahrgangsdaten: GostJahrgangsdaten;
	gostLaufbahnBeratungsdaten: () => GostLaufbahnplanungBeratungsdaten;
	patchBeratungsdaten: (data : Partial<GostLaufbahnplanungBeratungsdaten>) => Promise<void>;
	gostBelegpruefungsArt: 'ef1'|'gesamt'|'auto';
	gostBelegpruefungErgebnis: GostBelegpruefungErgebnis;
	abiturdatenManager: AbiturdatenManager;
	faechermanager: GostFaecherManager;
	mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	id?: number;
	zwischenspeicher?: GostLaufbahnplanungDaten;
	saveLaufbahnplanung: () => Promise<void>;
	restoreLaufbahnplanung: () => Promise<void>;
}