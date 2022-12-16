import { RouteRecordRaw } from "vue-router";

import Login from "~/components/SLogin.vue";


export const RouteLogin : RouteRecordRaw = {
    path: "/login",
    name: "login",
    component: Login,
    meta: {
        auth_required: false,   // gibt an, ob eine Authentifizierung für diese Seite benötigt wird
        schulformen: undefined  // undefined bedeutet, dass diese Seite bei allen Schulformen angezeigt wird
    }
}
