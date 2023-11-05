import type { LehrerListeManager, LehrerPersonalabschnittsdaten, LehrerPersonaldaten, Schuljahresabschnitt } from "@core";

export interface LehrerPersonaldatenProps {
	lehrerListeManager: () => LehrerListeManager;
	aktAbschnitt: Schuljahresabschnitt;
	patch: (data : Partial<LehrerPersonaldaten>) => Promise<void>;
	patchAbschnittsdaten: (data : Partial<LehrerPersonalabschnittsdaten>, id : number) => Promise<void>;
}