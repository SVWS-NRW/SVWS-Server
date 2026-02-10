import type { BenutzerKompetenz, LehrerFachrichtungEintrag, LehrerLehramtEintrag, LehrerLehrbefaehigungEintrag, LehrerPersonalabschnittsdaten,
	LehrerPersonalabschnittsdatenAnrechnungsstunden, LehrerPersonaldaten, List, SchulEintrag, Schulform, Schuljahresabschnitt, ServerMode,
	ValidatorKontext } from "@core";
import type { LehrerListeManager } from "@ui";

export interface LehrerPersonaldatenProps {
	validatorKontext: () => ValidatorKontext;
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	lehrerListeManager: () => LehrerListeManager;
	mapSchulen: () => Map<string, SchulEintrag>;
	aktAbschnitt: Schuljahresabschnitt;
	patch: (data: Partial<LehrerPersonaldaten>) => Promise<void>;
	patchAbschnittsdaten: (data: Partial<LehrerPersonalabschnittsdaten>, id: number) => Promise<void>;
	patchLehramt: (eintrag: LehrerLehramtEintrag, patch: Partial<LehrerLehramtEintrag>) => Promise<void>;
	addLehramt: (eintrag: Partial<LehrerLehramtEintrag>) => Promise<void>;
	removeLehraemter: (eintraege: List<LehrerLehramtEintrag>) => Promise<void>;
	patchLehrbefaehigung: (eintrag: LehrerLehrbefaehigungEintrag, patch: Partial<LehrerLehrbefaehigungEintrag>) => Promise<void>;
	addLehrbefaehigung: (eintrag: Partial<LehrerLehrbefaehigungEintrag>) => Promise<void>;
	removeLehrbefaehigungen: (eintraege: List<LehrerLehrbefaehigungEintrag>) => Promise<void>;
	patchFachrichtung: (eintrag: LehrerFachrichtungEintrag, patch: Partial<LehrerFachrichtungEintrag>) => Promise<void>;
	addFachrichtung: (eintrag: Partial<LehrerFachrichtungEintrag>) => Promise<void>;
	removeFachrichtungen: (eintraege: List<LehrerFachrichtungEintrag>) => Promise<void>;
	addMehrleistung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => Promise<void>;
	patchMehrleistung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number) => Promise<void>;
	removeMehrleistung: (data: LehrerPersonalabschnittsdatenAnrechnungsstunden) => Promise<void>;
	addMinderleistung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => Promise<void>;
	patchMinderleistung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number) => Promise<void>;
	removeMinderleistung: (data: LehrerPersonalabschnittsdatenAnrechnungsstunden) => Promise<void>;
	addAnrechnung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => Promise<void>;
	patchAnrechnung: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number) => Promise<void>;
	removeAnrechnung: (data: LehrerPersonalabschnittsdatenAnrechnungsstunden) => Promise<void>;
}
