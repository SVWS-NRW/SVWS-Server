import type { GostSchuelerFachwahl, GostBelegpruefungsArt, SchuelerListeEintrag, GostJahrgangsdaten, GostBelegpruefungErgebnis, AbiturdatenManager, GostFaecherManager, GostJahrgangFachkombination, GostLaufbahnplanungBeratungsdaten } from "@svws-nrw/svws-core";

export interface SchuelerLaufbahnplanungProps {
	setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
	setGostBelegpruefungsArt: (value: GostBelegpruefungsArt) => Promise<void>;
	getPdfWahlbogen: () => Promise<Blob>;
	exportLaufbahnplanung: () => Promise<Blob>;
	importLaufbahnplanung: (data: FormData) => Promise<boolean>;
	schueler: SchuelerListeEintrag | undefined,
	gostJahrgangsdaten: GostJahrgangsdaten;
	gostLaufbahnBeratungsdaten: () => GostLaufbahnplanungBeratungsdaten;
	patchBeratungsdaten: (data : Partial<GostLaufbahnplanungBeratungsdaten>) => Promise<void>;
	gostBelegpruefungsArt: GostBelegpruefungsArt;
	gostBelegpruefungErgebnis: GostBelegpruefungErgebnis;
	abiturdatenManager: AbiturdatenManager;
	faechermanager: GostFaecherManager;
	mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
}