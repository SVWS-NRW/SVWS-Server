const config = require("../../../tailwind.config-base.cjs");

// config.content.push("../../ui-components/ts/src/components/**/*.{vue,js,ts,jsx,tsx}")
config.content = [
	"./src/components/**/*.{vue,js,ts,jsx,tsx}"
]
module.exports = config;