import type { JahrgangsListeEintrag, List, Raum, Stundenplan, StundenplanManager, StundenplanRaum } from "@core";

export interface StundenplanDatenProps {
	stundenplanManager: () => StundenplanManager;
	patch: (daten: Partial<Stundenplan>) => Promise<void>;
	patchRaum: (daten: Partial<StundenplanRaum>, id: number) => Promise<void>;
	addRaum: (raum: StundenplanRaum) => Promise<void>;
	removeRaeume: (raeume: StundenplanRaum[]) => Promise<void>;
	listRaeume: () => List<Raum>;
	importRaeume: (raeume: StundenplanRaum[]) => Promise<void>;
	listJahrgaenge: List<JahrgangsListeEintrag>;
	addJahrgang: (id: number) => Promise<void>;
	removeJahrgang: (id: number) => Promise<void>;
	gotoKatalog: (katalog: 'raeume'|'aufsichtsbereiche'|'pausenzeiten') => Promise<void>;
}