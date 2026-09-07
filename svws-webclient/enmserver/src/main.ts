import SWrapper from "@wenom/components/SWrapper.vue";
import { createApp } from "vue";

import { AppContext } from "@ui/AppContext";

import { RouteManager } from "./router/RouteManager";
import { auskunftStateImpl } from "./states/AuskunftStateImpl";
import { registerStates } from "./states/registerStates";

import "@ui/assets/styles/index.css";
import "./main.css";

await auskunftStateImpl.init();

const context = AppContext.init(createApp(SWrapper));
RouteManager.create(AppContext.instance.router);

registerStates();

await context.mount();
