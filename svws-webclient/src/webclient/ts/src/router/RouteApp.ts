import { RouteRecordRaw } from "vue-router";

import { routeSchule } from "~/router/apps/RouteSchule";
import { RouteSchuleBenutzerverwaltung } from "~/router/apps/RouteSchuleBenutzerverwaltung";
import { routeKataloge } from "~/router/apps/RouteKataloge";
import { routeKatalogFaecher } from "~/router/apps/RouteKatalogFaecher";
import { routeKatalogReligion } from "~/router/apps/RouteKatalogReligion";
import { routeKatalogJahrgaenge } from "~/router/apps/RouteKatalogJahrgaenge";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { routeLehrer } from "~/router/apps/RouteLehrer";
import { routeKlassen } from "~/router/apps/RouteKlassen";
import { routeKurse } from "~/router/apps/RouteKurse";
import { routeGost } from "~/router/apps/RouteGost";
import { routeStatistik } from "~/router/apps/RouteStatistik";

import SApp from "~/components/SApp.vue";


export const RouteApp : RouteRecordRaw = {
    path: "/",
    component: SApp,
    name: "app",
    children: [
        routeSchule.record,
        RouteSchuleBenutzerverwaltung,
        routeKataloge.record,
        routeKatalogFaecher.record,
        routeKatalogReligion.record,
        routeKatalogJahrgaenge.record,
        routeSchueler.record,
        routeLehrer.record,
        routeKlassen.record,
        routeKurse.record,
        routeGost.record,
        routeStatistik.record
    ],
    meta: {
        auth_required: false,   // gibt an, ob eine Authentifizierung für diese Seite benötigt wird
        schulformen: undefined  // undefined bedeutet, dass diese Seite bei allen Schulformen angezeigt wird
    }
}
