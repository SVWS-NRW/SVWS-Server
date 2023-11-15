import type { SchemaListeEintrag } from "@core";

export interface SchemaUebersichtProps {
	data: () => SchemaListeEintrag | undefined;
}
