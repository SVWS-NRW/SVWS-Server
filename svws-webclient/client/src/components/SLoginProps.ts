import type { DBSchemaListeEintrag, List } from "@core";

export interface LoginProps {
	setSchema: (schema: DBSchemaListeEintrag) => Promise<void>;
	login: (schema: string, username: string, password: string) => Promise<void>;
	connectTo: () => Promise<List<DBSchemaListeEintrag>>;
	schemaPrevious: string | null;
}
