import type { BenutzergruppeListeEintrag, BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerManager, List } from "@core";

export interface BenutzerProps{
		listBenutzergruppen: List<BenutzergruppeListeEintrag>;
		getBenutzerManager: () => BenutzerManager;
		setAnzeigename : (anzeigename : string) => Promise<void>;
		setAnmeldename : (anzeigename : string) => Promise<void>;
		setPassword : (passwort : string) => Promise<boolean|undefined>;
		addBenutzerToBenutzergruppe : (bg_id : number) => Promise<void>;
		removeBenutzerFromBenutzergruppe : (bg_id : number) => Promise<void>;
		setIstAdmin : (istAdmin: boolean) => Promise<void>;
		addKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		gotoBenutzergruppe: (b_id: number) => Promise<void>;
		benutzerKompetenzen:(kompetenzgruppe : BenutzerKompetenzGruppe) => List<BenutzerKompetenz>;
}

