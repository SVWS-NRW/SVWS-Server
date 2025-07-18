import type { BenutzerKompetenz, LehrerFachrichtungEintrag, LehrerLehramtEintrag, LehrerLehrbefaehigungEintrag, LehrerPersonalabschnittsdaten, LehrerPersonaldaten, List, Schulform, Schuljahresabschnitt, ServerMode, ValidatorKontext} from "@core";
import type { LehrerListeManager } from "@ui";

export interface LehrerPersonaldatenProps {
	validatorKontext: () => ValidatorKontext;
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	lehrerListeManager: () => LehrerListeManager;
	aktAbschnitt: Schuljahresabschnitt;
	patch: (data : Partial<LehrerPersonaldaten>) => Promise<void>;
	patchAbschnittsdaten: (data : Partial<LehrerPersonalabschnittsdaten>, id : number) => Promise<void>;
	patchLehramt: (eintrag: LehrerLehramtEintrag, patch: Partial<LehrerLehramtEintrag>) => Promise<void>;
	addLehramt: (eintrag: Partial<LehrerLehramtEintrag>) => Promise<void>;
	removeLehraemter: (eintraege: List<LehrerLehramtEintrag>) => Promise<void>;
	patchLehrbefaehigung: (eintrag: LehrerLehrbefaehigungEintrag, patch: Partial<LehrerLehrbefaehigungEintrag>) => Promise<void>;
	addLehrbefaehigung: (eintrag: Partial<LehrerLehrbefaehigungEintrag>) => Promise<void>;
	removeLehrbefaehigungen: (eintraege: List<LehrerLehrbefaehigungEintrag>) => Promise<void>;
	patchFachrichtung: (eintrag: LehrerFachrichtungEintrag, patch : Partial<LehrerFachrichtungEintrag>) => Promise<void>;
	addFachrichtung: (eintrag: Partial<LehrerFachrichtungEintrag>) => Promise<void>;
	removeFachrichtungen: (eintraege: List<LehrerFachrichtungEintrag>) => Promise<void>;
}
