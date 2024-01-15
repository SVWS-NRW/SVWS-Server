import type { GostKlausurraum, GostKlausurtermin, GostKursklausur, StundenplanZeitraster, GostSchuelerklausurTermin } from "@core";

export type GostKlausurplanungDragData = GostKursklausur | GostKlausurtermin | GostSchuelerklausurTermin | undefined;
export type GostKlausurplanungDropZone = GostKlausurtermin | GostKlausurraum | StundenplanZeitraster | undefined;