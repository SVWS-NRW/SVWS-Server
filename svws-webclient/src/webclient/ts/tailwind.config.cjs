const config = require("../../../tailwind.config-base.cjs");

config.content = [
	"./src/components/**/*.{vue,js,ts,jsx,tsx}",
	"./index.html",
	"../../ui-components/ts/src/**/*.{vue,js,ts,jsx,tsx}"
]
module.exports = config;