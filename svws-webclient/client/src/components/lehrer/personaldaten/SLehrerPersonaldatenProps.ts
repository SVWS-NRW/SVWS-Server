import type { LehrerFachrichtungAnerkennung, LehrerFachrichtungEintrag, LehrerLehramtAnerkennung, LehrerLehramtEintrag,
	LehrerLehrbefaehigungAnerkennung, LehrerLehrbefaehigungEintrag, LehrerListeManager, LehrerPersonalabschnittsdaten,
	LehrerPersonaldaten, List, Schuljahresabschnitt } from "@core";

export interface LehrerPersonaldatenProps {
	lehrerListeManager: () => LehrerListeManager;
	aktAbschnitt: Schuljahresabschnitt;
	patch: (data : Partial<LehrerPersonaldaten>) => Promise<void>;
	patchAbschnittsdaten: (data : Partial<LehrerPersonalabschnittsdaten>, id : number) => Promise<void>;
	patchLehramtAnerkennung: (eintrag: LehrerLehramtEintrag, anerkennung : LehrerLehramtAnerkennung | null) => Promise<void>;
	addLehramt: (eintrag: LehrerLehramtEintrag) => Promise<void>;
	removeLehraemter: (eintraege: List<LehrerLehramtEintrag>) => Promise<void>;
	patchLehrbefaehigungAnerkennung: (eintrag: LehrerLehrbefaehigungEintrag, anerkennung : LehrerLehrbefaehigungAnerkennung | null) => Promise<void>;
	addLehrbefaehigung: (eintrag: LehrerLehrbefaehigungEintrag) => Promise<void>;
	removeLehrbefaehigungen: (eintraege: List<LehrerLehrbefaehigungEintrag>) => Promise<void>;
	patchFachrichtungAnerkennung: (eintrag: LehrerFachrichtungEintrag, anerkennung : LehrerFachrichtungAnerkennung | null) => Promise<void>;
	addFachrichtung: (eintrag: LehrerFachrichtungEintrag) => Promise<void>;
	removeFachrichtungen: (eintraege: List<LehrerFachrichtungEintrag>) => Promise<void>;
}