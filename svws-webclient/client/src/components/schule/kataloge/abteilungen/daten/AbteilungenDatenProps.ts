import type { Abteilung } from "@core/core/data/schule/Abteilung";
import type { AbteilungKlassenzuordnung } from "@core/core/data/schule/AbteilungKlassenzuordnung";
import type { List } from "@core/java/util/List";
import type { AbteilungenListeManager } from "@ui/ui/manager/kataloge/AbteilungenListeManager";

export interface AbteilungenDatenProps {
	goToLehrer: (idAbteilungsleiter: number) => Promise<void>;
	manager: () => AbteilungenListeManager;
	isReadonly: boolean;
	isAbteilungImZukuenftigenAbschnitt: boolean;
	patch: (data: Partial<Abteilung>) => Promise<boolean>;
	deleteKlassenzuordnungen: (klassenzuordnungen: List<AbteilungKlassenzuordnung>) => Promise<void>;
	addKlassenzuordnungen: (idAbteilung: number, idsKlassen: List<number>) => Promise<void>;
}
