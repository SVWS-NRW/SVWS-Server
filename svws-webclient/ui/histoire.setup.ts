import "./src/assets/styles/_histoire.css";
import { JsonCoreTypeReaderStatic } from "./../core/src/asd/utils/JsonCoreTypeReaderStatic"

const root = document.getElementsByTagName('html')[0];
root.setAttribute('class', 'light');

const reader = new JsonCoreTypeReaderStatic();
reader.readAll();


