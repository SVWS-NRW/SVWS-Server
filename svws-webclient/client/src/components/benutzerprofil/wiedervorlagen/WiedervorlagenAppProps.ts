import type { BenutzerDaten, WiedervorlageEintrag } from "@core";

export interface WiedervorlagenAppProps {
	benutzer: () => BenutzerDaten;
	goToPerson: (eintrag: WiedervorlageEintrag) => Promise<void>;
}
