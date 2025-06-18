import { createApp } from 'vue'
import router from './router'
import App from './stories/App.vue'
import { JsonCoreTypeReaderStatic } from "./../../core/src/asd/utils/JsonCoreTypeReaderStatic"

import "./assets/styles/index.css";
import "./main.css"

const reader = new JsonCoreTypeReaderStatic();
reader.readAll();

const app = createApp(App);
app.use(router);

await router.isReady();
if (router.currentRoute.value.path === '/')
	await router.push("/README");

app.mount('#app');