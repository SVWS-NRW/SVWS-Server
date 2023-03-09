# SVWS-NRW CoreTS-Bibliothek

Die CoreTS-Bibliothek ist eine aus Java transpilierte Bibliothek, die alle für den SVWS-Server notwendigen Typen zur Verfügung stellt.
Ebenso wird ein API-Client mitgeliefert, der mit einem SVWS-Server kommunizieren kann.

Alle API-Anfragen bzw- Antworten sind typsicher mit dieser Bibliothek.

Beispiel zur Nutzung der CoreTS-Bibliothek:

```ts
import { ApiServer, List, DBSchemaListeEintrag, Vector, SchuelerListeEintrag } from "@svws-nrw/svws-core"

const url = process.env.SVWS_URL;
const user = process.env.SVWS_USER;
const password = process.env.PASSWORD;
const server = new ApiServer(url, "", "");
let schemata: Ref<List<DBSchemaListeEintrag>> = new Vector();
let api: ApiServer | undefined;
let schueler: List<SchuelerListeEintrag> = new Vector();
const schemata = await server.getConfigDBSchemata();
try {
	const schema = schemata.value.get(0).name;
	api = new ApiServer(url, user, password);
	schueler = await api.getSchuelerAktuell(schema);
	console.log(schueler)
} catch(error) {
	console.log("Es trat ein Verbindungsfehler auf: ", error)
}
```