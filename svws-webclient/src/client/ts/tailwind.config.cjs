const config = require("../../../tailwind.config-base.cjs");

config.content = [
	"../../ui/ts/src/**/*.{vue,js,ts,jsx,tsx}",
	"./index.html",
	"./src/components/**/*.{vue,js,ts,jsx,tsx}",
]
module.exports = config;
