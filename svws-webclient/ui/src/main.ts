import { createApp } from 'vue'
import router from './router'
import App from './stories/App.vue'
import { JsonCoreTypeReaderStatic } from "./../../core/src/asd/utils/JsonCoreTypeReaderStatic"

import "./assets/styles/index.css";

const reader = new JsonCoreTypeReaderStatic();
reader.readAll();

createApp(App)
	.use(router)
	.mount('#app');