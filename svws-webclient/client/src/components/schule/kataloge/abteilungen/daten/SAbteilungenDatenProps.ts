import type { Abteilung, AbteilungenListeManager } from "@core";

export interface AbteilungenDatenProps {
	goToLehrer: (idAbteilungsleiter : number) => Promise<void>;
	manager: () => AbteilungenListeManager;
	patch: (data : Partial<Abteilung>) => Promise<void>;
}
