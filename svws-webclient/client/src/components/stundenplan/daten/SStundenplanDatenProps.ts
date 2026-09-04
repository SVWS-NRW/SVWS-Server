import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { Raum } from "@core/core/data/schule/Raum";
import type { Stundenplan } from "@core/core/data/stundenplan/Stundenplan";
import type { StundenplanAufsichtsbereich } from "@core/core/data/stundenplan/StundenplanAufsichtsbereich";
import type { StundenplanKonfiguration } from "@core/core/data/stundenplan/StundenplanKonfiguration";
import type { StundenplanPausenzeit } from "@core/core/data/stundenplan/StundenplanPausenzeit";
import type { StundenplanRaum } from "@core/core/data/stundenplan/StundenplanRaum";
import type { List } from "@core/java/util/List";
import type { StundenplanListeManager } from "@ui/ui/manager/stundenplan/StundenplanListeManager";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface StundenplanDatenProps {
	manager: () => StundenplanListeManager;
	patch: (daten: Partial<Stundenplan>) => Promise<boolean>;
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
	gotoKatalog: (katalog: 'raeume' | 'aufsichtsbereiche' | 'pausenzeiten') => Promise<RoutingStatus>;
	setSettingsDefaults: (value: StundenplanKonfiguration) => Promise<void>;
}
