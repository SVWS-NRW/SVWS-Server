import type { DBSchemaListeEintrag } from "@core/core/data/db/DBSchemaListeEintrag";
import type { List } from "@core/java/util/List";

export interface LoginProps {
	setSchema: (schema: DBSchemaListeEintrag) => Promise<void>;
	login: (schema: string, username: string, password: string) => Promise<void>;
	connectTo: () => Promise<List<DBSchemaListeEintrag>>;
	schemaPrevious: string | null;
}
