const config = require("../../../tailwind.config-base");

config.content.push("../../ui-components/ts/src/**/components/**/*.{vue,js,ts,jsx,tsx}")
config.content.push("../../webclient/ts/src/**/components/**/*.{vue,js,ts,jsx,tsx}")

module.exports = config;