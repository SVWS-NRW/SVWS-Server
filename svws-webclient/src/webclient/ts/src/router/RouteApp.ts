import { RouteRecordRaw } from "vue-router";

import { RouteSchule } from "~/router/apps/RouteSchule";
import { RouteSchuleBenutzerverwaltung } from "~/router/apps/RouteSchuleBenutzerverwaltung";
import { RouteKataloge } from "~/router/apps/RouteKataloge";
import { RouteKatalogFaecher } from "~/router/apps/RouteKatalogFaecher";
import { RouteKatalogReligion } from "~/router/apps/RouteKatalogReligion";
import { RouteKatalogJahrgaenge } from "~/router/apps/RouteKatalogJahrgaenge";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { routeLehrer } from "~/router/apps/RouteLehrer";
import { RouteKlassen } from "~/router/apps/RouteKlassen";
import { RouteKurse } from "~/router/apps/RouteKurse";
import { routeGost } from "~/router/apps/RouteGost";
import { RouteStatistik } from "~/router/apps/RouteStatistik";

import SApp from "~/components/SApp.vue";


export const RouteApp : RouteRecordRaw = {
    path: "/",
    component: SApp,
    name: "app",
    children: [
        RouteSchule,
        RouteSchuleBenutzerverwaltung,
        RouteKataloge,
        RouteKatalogFaecher,
        RouteKatalogReligion,
        RouteKatalogJahrgaenge,
        routeSchueler.record,
        routeLehrer.record,
        RouteKlassen,
        RouteKurse,
        routeGost.record,
        RouteStatistik
    ],
    meta: {
        auth_required: false,   // gibt an, ob eine Authentifizierung für diese Seite benötigt wird
        schulformen: undefined  // undefined bedeutet, dass diese Seite bei allen Schulformen angezeigt wird
    }
}
