import type { BenutzergruppeListeEintrag } from "@core/core/data/benutzer/BenutzergruppeListeEintrag";
import type { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { BenutzerKompetenzGruppe } from "@core/core/types/benutzer/BenutzerKompetenzGruppe";
import type { BenutzerManager } from "@core/core/utils/benutzer/BenutzerManager";
import type { List } from "@core/java/util/List";

export interface BenutzerProps {
	listBenutzergruppen: List<BenutzergruppeListeEintrag>;
	getBenutzerManager: () => BenutzerManager;
	setAnzeigename: (anzeigename: string | null) => Promise<void>;
	setAnmeldename: (anzeigename: string | null) => Promise<void>;
	setPassword: (passwort: string) => Promise<void>;
	addBenutzerToBenutzergruppe: (bg_id: number) => Promise<void>;
	removeBenutzerFromBenutzergruppe: (bg_id: number) => Promise<void>;
	setIstAdmin: (istAdmin: boolean) => Promise<void>;
	addKompetenz: (kompetenz: BenutzerKompetenz) => Promise<boolean>;
	removeKompetenz: (kompetenz: BenutzerKompetenz) => Promise<boolean>;
	addBenutzerKompetenzGruppe: (kompetenzgruppe: BenutzerKompetenzGruppe) => Promise<boolean>;
	removeBenutzerKompetenzGruppe: (kompetenzgruppe: BenutzerKompetenzGruppe) => Promise<boolean>;
	gotoBenutzergruppe: (b_id: number) => Promise<void>;
	benutzerKompetenzen: (kompetenzgruppe: BenutzerKompetenzGruppe) => List<BenutzerKompetenz>;
}

