import { BenutzergruppeListeEintrag, BenutzergruppeDaten, List, BenutzerListeEintrag, BenutzergruppenManager, BenutzerKompetenz, BenutzerKompetenzGruppe } from "@svws-nrw/svws-core";

export interface BenutzergruppeProps{
    auswahl: () => BenutzergruppeListeEintrag | undefined;
    listBenutzerAlle: () => List<BenutzerListeEintrag>;
    listBenutzergruppenBenutzer: () => List<BenutzerListeEintrag> ;
    aktualisiereListeBenutzerGruppenBenutzer : (benutzer: BenutzerListeEintrag) => Promise<void>;
    getBenutzergruppenManager: () => BenutzergruppenManager;
    setBezeichnung : (anzeigename: string) => Promise<void>;
    setIstAdmin : (istAdmin: boolean) => Promise<void>;
    addBenutzerToBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
    removeBenutzerFromBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
    addKompetenz : (kompetenz: BenutzerKompetenz) => Promise<boolean>;
    removeKompetenz : (kompetenz: BenutzerKompetenz) => Promise<boolean>;
    create: ( bezeichnung : string, istAdmin : boolean) => Promise<void>;
    addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
    removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>
    goToBenutzer: (b_id: number) => Promise<void>;
}