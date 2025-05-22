import type { Abteilung, AbteilungenListeManager, BenutzerKompetenz, List } from "@core";

export interface AbteilungenDatenProps {
	goToLehrer: (idAbteilungsleiter : number) => Promise<void>;
	manager: () => AbteilungenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data : Partial<Abteilung>) => Promise<void>;
	deleteKlassenzuordnungen: (ids: List<number>) => Promise<void>;
}
