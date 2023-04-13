import type { BenutzergruppeListeEintrag} from "@svws-nrw/svws-core";
export interface BenutzergruppeAppProps {
    auswahl:() =>  BenutzergruppeListeEintrag | undefined;
}