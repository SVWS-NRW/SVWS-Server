import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { RouteSchulwechsel } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchulwechsel";
import { RouteNode } from "~/router/RouteNode";
import { schulwechselErfolgreicheBewerbungenStateImpl } from "~/states/schulwechsel/SchulwechselErfolgreicheBewerbungenStateImpl";

const schulwechselErfolgreicheBewerbungen = () => import("~/components/schule/datenaustausch/schulwechsel/SchulwechselErfolgreicheBewerbungen.vue");

export class RouteSchulwechselErfolgreicheBewerbungen extends RouteNode<any, RouteSchulwechsel> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE,
		], "schule.datenaustausch.schulwechsel.erfolgreichebewerbungen", "erfolgreichebewerbungen", schulwechselErfolgreicheBewerbungen);
		super.mode = ServerMode.DEV;
		super.text = "Erfolgreiche Bewerbungen";
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (!isEntering) {
			return;
		}
		await schulwechselErfolgreicheBewerbungenStateImpl.updateErfolgreicheBewerbungen();
		// Die Vorschau fragt den Remote-Server ab. Sie wird nicht abgewartet, damit eine langsame oder
		// nicht erreichbare Gegenstelle das Öffnen der Ansicht nicht blockiert. Ein Fehler wird vom State als Notification gemeldet.
		void schulwechselErfolgreicheBewerbungenStateImpl.updateNewBewerbungenCount();
	}

}

export const routeSchulwechselErfolgreicheBewerbungen = new RouteSchulwechselErfolgreicheBewerbungen();
