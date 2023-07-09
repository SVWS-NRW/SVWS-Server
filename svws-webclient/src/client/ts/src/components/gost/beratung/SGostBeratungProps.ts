import type { AbiturdatenManager, GostBelegpruefungErgebnis, GostJahrgangsdaten, GostSchuelerFachwahl, LehrerListeEintrag, Sprachbelegung } from "@core";

export interface GostBeratungProps {
	patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
	jahrgangsdaten: () => GostJahrgangsdaten;
	setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
	setSprachbelegung: (sprache: string, belegung: Partial<Sprachbelegung>) => Promise<void>;
	deleteSprachbelegung: (sprache: string) => Promise<void>;
	setGostBelegpruefungsArt: (value: 'ef1'|'gesamt'|'auto') => Promise<void>;
	gostBelegpruefungsArt: () => 'ef1'|'gesamt'|'auto';
	gostBelegpruefungErgebnis: () => GostBelegpruefungErgebnis;
	abiturdatenManager: () => AbiturdatenManager;
	mapLehrer: Map<number, LehrerListeEintrag>;
	id?: number;
	resetFachwahlen: () => Promise<void>;
}