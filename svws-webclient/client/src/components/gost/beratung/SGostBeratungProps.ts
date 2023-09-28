import type { AbiturdatenManager, GostBelegpruefungErgebnis, GostBeratungslehrer, GostJahrgangsdaten, GostSchuelerFachwahl, LehrerListeEintrag, List } from "@core";

export interface GostBeratungProps {
	patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
	jahrgangsdaten: () => GostJahrgangsdaten;
	setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
	setGostBelegpruefungsArt: (value: 'ef1'|'gesamt'|'auto') => Promise<void>;
	gostBelegpruefungsArt: () => 'ef1'|'gesamt'|'auto';
	gostBelegpruefungErgebnis: () => GostBelegpruefungErgebnis;
	abiturdatenManager: () => AbiturdatenManager;
	mapLehrer: Map<number, LehrerListeEintrag>;
	id?: number;
	resetFachwahlen: () => Promise<void>;
	beratungslehrer: () => List<GostBeratungslehrer>;
	addBeratungslehrer: (id: number) => Promise<void>;
	removeBeratungslehrer: (lehrer: GostBeratungslehrer[]) => Promise<void>;
}