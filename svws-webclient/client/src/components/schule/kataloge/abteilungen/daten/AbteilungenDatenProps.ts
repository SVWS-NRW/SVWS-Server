import type { Abteilung, AbteilungKlassenzuordnung, BenutzerKompetenz, List } from "@core";
import type { AbteilungenListeManager } from "@ui";

export interface AbteilungenDatenProps {
	goToLehrer: (idAbteilungsleiter: number) => Promise<void>;
	manager: () => AbteilungenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	isReadonly: boolean;
	isAbteilungImZukuenftigenAbschnitt: boolean;
	patch: (data: Partial<Abteilung>) => Promise<boolean>;
	deleteKlassenzuordnungen: (klassenzuordnungen: List<AbteilungKlassenzuordnung>) => Promise<void>;
	addKlassenzuordnungen: (idAbteilung: number, idsKlassen: List<number>) => Promise<void>;
}
