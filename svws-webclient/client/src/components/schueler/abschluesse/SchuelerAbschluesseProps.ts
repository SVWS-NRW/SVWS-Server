import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
import type { List } from "@core/java/util/List";

export interface SchuelerAbschluesseProps {
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
}
