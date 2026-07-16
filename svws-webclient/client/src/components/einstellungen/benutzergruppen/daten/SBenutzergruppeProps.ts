import type { BenutzergruppeListeEintrag, List, BenutzerListeEintrag, BenutzergruppenManager, BenutzerKompetenz, BenutzerKompetenzGruppe } from "@core";

export interface BenutzergruppeProps {
	auswahl: () => BenutzergruppeListeEintrag | undefined;
	alleBenutzer: () => List<BenutzerListeEintrag>;
	benutzerInBenutzergruppe: () => List<BenutzerListeEintrag>;
	manager: () => BenutzergruppenManager;
	setBezeichnung: (anzeigename: string | null) => Promise<void>;
	setIstAdmin: (istAdmin: boolean) => Promise<void>;
	addBenutzerToBenutzergruppe: (benutzer: BenutzerListeEintrag) => Promise<void>;
	removeBenutzerFromBenutzergruppe: (benutzer: BenutzerListeEintrag) => Promise<void>;
	addKompetenz: (kompetenz: BenutzerKompetenz) => Promise<boolean>;
	removeKompetenz: (kompetenz: BenutzerKompetenz) => Promise<boolean>;
	create: (bezeichnung: string, istAdmin: boolean) => Promise<void>;
	addBenutzerKompetenzGruppe: (kompetenzgruppe: BenutzerKompetenzGruppe) => Promise<boolean>;
	removeBenutzerKompetenzGruppe: (kompetenzgruppe: BenutzerKompetenzGruppe) => Promise<boolean>
	gotoBenutzer: (idBenutzer: number) => Promise<void>;
	benutzerKompetenzen: (kompetenzgruppe: BenutzerKompetenzGruppe) => List<BenutzerKompetenz>;
	mapBenutzergruppen: Map<number, BenutzergruppeListeEintrag>
}
