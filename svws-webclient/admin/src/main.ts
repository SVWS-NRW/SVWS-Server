import SWrapper from "@admin/components/SWrapper.vue";
import { createApp } from "vue";

import { AppContext } from "@ui/AppContext";

import { RouteManager } from "./router/RouteManager";
import { registerStates } from "./states/registerStates";

import "@ui/assets/styles/index.css";

const context = AppContext.init(createApp(SWrapper));
RouteManager.create(AppContext.instance.router);

registerStates();

await context.mount();
