import type { BenutzerKompetenz, JahrgangsDaten, List, Raum, Schulform, ServerMode, Stundenplan, StundenplanAufsichtsbereich, StundenplanKonfiguration, StundenplanPausenzeit, StundenplanRaum } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { StundenplanListeManager } from "@ui";

export interface StundenplanDatenProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	manager: () => StundenplanListeManager;
	patch: (daten: Partial<Stundenplan>) => Promise<void>;
	patchRaum: (daten: Partial<StundenplanRaum>, id: number) => Promise<void>;
	addRaum: (raum: StundenplanRaum) => Promise<void>;
	removeRaeume: (raeume: StundenplanRaum[]) => Promise<void>;
	listRaeume: () => List<Raum>;
	raeumeSyncToVorlage: (raeume: Raum[]) => Promise<void>;
	raeumeSyncToStundenplan: (raeume: Raum[]) => Promise<void>;
	listJahrgaenge: List<JahrgangsDaten>;
	addJahrgang: (id: number) => Promise<void>;
	removeJahrgang: (id: number) => Promise<void>;
	patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
	addPausenzeiten: (pausenzeiten: Iterable<Partial<StundenplanPausenzeit>>) => Promise<void>;
	removePausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
	pausenzeitenSyncToVorlage: (raeume: StundenplanPausenzeit[]) => Promise<void>;
	pausenzeitenSyncToStundenplan: (raeume: StundenplanPausenzeit[]) => Promise<void>;
	listPausenzeiten: () => List<StundenplanPausenzeit>;
	patchAufsichtsbereich: (daten: Partial<StundenplanAufsichtsbereich>, id: number) => Promise<void>;
	addAufsichtsbereich: (aufsichtsbereich: StundenplanAufsichtsbereich) => Promise<void>;
	removeAufsichtsbereiche: (aufsichtsbereiche: StundenplanAufsichtsbereich[]) => Promise<void>;
	aufsichtsbereicheSyncToVorlage: (aufsichtsbereiche: StundenplanAufsichtsbereich[]) => Promise<void>;
	aufsichtsbereicheSyncToStundenplan: (aufsichtsbereiche: StundenplanAufsichtsbereich[]) => Promise<void>;
	listAufsichtsbereiche: () => List<StundenplanAufsichtsbereich>;
	gotoKatalog: (katalog: 'raeume'|'aufsichtsbereiche'|'pausenzeiten') => Promise<RoutingStatus>;
	setSettingsDefaults: (value: StundenplanKonfiguration) => Promise<void>;
}
