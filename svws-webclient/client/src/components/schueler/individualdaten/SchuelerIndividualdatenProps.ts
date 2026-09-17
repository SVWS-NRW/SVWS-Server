import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { Fahrschuelerart } from "@core/core/data/schule/Fahrschuelerart";
import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import type { ReligionEintrag } from "@core/core/data/schule/ReligionEintrag";
import type { Telefonart } from "@core/core/data/schule/Telefonart";

export interface SchuelerIndividualdatenProps {
	mapSchulen: Map<string, SchulEintrag>;
	fahrschuelerartenById: Map<number, Fahrschuelerart>;
	foerderschwerpunkteById: Map<number, FoerderschwerpunktEintrag>;
	haltestellenById: Map<number, Haltestelle>
	religionenById: Map<number, ReligionEintrag>;
	mapTelefonArten: Map<number, Telefonart>
	zeigeAlles: boolean;
}
