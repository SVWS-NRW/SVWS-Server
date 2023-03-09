import { GostSchuelerFachwahl, GostBelegpruefungsArt, SchuelerListeEintrag, GostJahrgangsdaten, GostBelegpruefungErgebnis, AbiturdatenManager, GostFaecherManager, GostJahrgangFachkombination } from "@svws-nrw/svws-core";

export interface SchuelerLaufbahnplanungProps {
	setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
	setGostBelegpruefungsArt: (value: GostBelegpruefungsArt) => Promise<void>;
	getPdfWahlbogen: () => Promise<Blob>;
	getLaufbahnplanung: () => Promise<Blob>;
	schueler: SchuelerListeEintrag | undefined,
	gostJahrgangsdaten: GostJahrgangsdaten;
	gostBelegpruefungsArt: GostBelegpruefungsArt;
	gostBelegpruefungErgebnis: GostBelegpruefungErgebnis;
	abiturdatenManager: AbiturdatenManager;
	faechermanager: GostFaecherManager;
	mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
}