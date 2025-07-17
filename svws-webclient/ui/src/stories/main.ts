import { createApp } from 'vue'
import router from './router'
import Stories from './Stories.vue'
import { JsonCoreTypeReaderStatic } from "../../../core/src/asd/utils/JsonCoreTypeReaderStatic"

import "../assets/styles/index.css";
import "./main.css"

const reader = new JsonCoreTypeReaderStatic();
reader.readAll();

const app = createApp(Stories);
app.use(router);
app.mount('#app');
