import type { AbiturdatenManager, BenutzerDaten, BenutzerKompetenz, GostBelegpruefungErgebnis, GostBeratungslehrer, GostHalbjahr, GostJahrgangsdaten, GostSchuelerFachwahl, LehrerListeEintrag, List, Schulform, ServerMode } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { Config } from "@ui";

export interface GostBeratungProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	benutzerdaten: BenutzerDaten;
	config: () => Config;
	patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
	jahrgangsdaten: () => GostJahrgangsdaten;
	setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
	setGostBelegpruefungsArt: (value: 'ef1'|'gesamt'|'auto') => Promise<void>;
	gostBelegpruefungsArt: () => 'ef1'|'gesamt'|'auto';
	gostBelegpruefungErgebnis: () => GostBelegpruefungErgebnis;
	abiturdatenManager: () => AbiturdatenManager;
	mapLehrer: Map<number, LehrerListeEintrag>;
	resetFachwahlen: (forceDelete: boolean) => Promise<void>;
	beratungslehrer: () => List<GostBeratungslehrer>;
	addBeratungslehrer: (id: number) => Promise<void>;
	removeBeratungslehrer: (lehrer: GostBeratungslehrer[]) => Promise<void>;
	gotoKursblockung: (halbjahr: GostHalbjahr) => Promise<RoutingStatus>;
}