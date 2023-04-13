import type { BenutzerListeEintrag } from "@svws-nrw/svws-core";
export interface BenutzerAppProps {
    auswahl: () => BenutzerListeEintrag | undefined;
}