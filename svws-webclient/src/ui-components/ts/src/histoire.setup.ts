import { defineSetupVue3 } from '@histoire/plugin-vue'
import SvwsUiIcon from './components/Layout/SvwsUiIcon.vue';
import SmartTable from "vuejs-smart-table";

import "./assets/styles/index.css";
import "./assets/styles/_histoire.css";

export const setupVue3 = defineSetupVue3(({ app }) => {
	app.component('SvwsUiIcon', SvwsUiIcon);
	app.use(SmartTable);
})