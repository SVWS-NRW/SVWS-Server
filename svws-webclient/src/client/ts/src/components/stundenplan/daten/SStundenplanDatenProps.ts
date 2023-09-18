import type { JahrgangsListeEintrag, List, Raum, Stundenplan, StundenplanAufsichtsbereich, StundenplanManager, StundenplanPausenzeit, StundenplanRaum } from "@core";

export interface StundenplanDatenProps {
	stundenplanManager: () => StundenplanManager;
	patch: (daten: Partial<Stundenplan>) => Promise<void>;
	patchRaum: (daten: Partial<StundenplanRaum>, id: number) => Promise<void>;
	addRaum: (raum: StundenplanRaum) => Promise<void>;
	removeRaeume: (raeume: StundenplanRaum[]) => Promise<void>;
	listRaeume: List<Raum>;
	importRaeume: (raeume: StundenplanRaum[]) => Promise<void>;
	patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
	addPausenzeit: (pausenzeit: StundenplanPausenzeit) => Promise<void>;
	removePausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
	importPausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
	listPausenzeiten: List<StundenplanPausenzeit>;
	patchAufsichtsbereich: (daten: Partial<StundenplanAufsichtsbereich>, id: number) => Promise<void>;
	addAufsichtsbereich: (aufsichtsbereich: StundenplanAufsichtsbereich) => Promise<void>;
	removeAufsichtsbereiche: (aufsichtsbereiche: StundenplanAufsichtsbereich[]) => Promise<void>;
	importAufsichtsbereiche: (aufsichtsbereiche: StundenplanAufsichtsbereich[]) => Promise<void>;
	listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
	listJahrgaenge: List<JahrgangsListeEintrag>;
	addJahrgang: (id: number) => Promise<void>;
	removeJahrgang: (id: number) => Promise<void>;
}