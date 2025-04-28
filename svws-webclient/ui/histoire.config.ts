import { defineConfig } from "histoire"

export default defineConfig({
	vite: {
		plugins: [
			{
				name: 'layer-histoire-styles',
				transform: (src, id) => {
					if (id.endsWith('app/dist/style.css')) {
						return {
							code: `@layer base {${src}}`,
							map: null,
						}
					}
				},
			},
		],
	},
	backgroundPresets:[
		{label:'Transparent',color:'transparent',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui',color:'var(--background-color-ui)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-0',color:'var(--background-color-ui-0)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-10',color:'var(--background-color-ui-10)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-25',color:'var(--background-color-ui-25)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-50',color:'var(--background-color-ui-50)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-75',color:'var(--background-color-ui-75)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-100',color:'var(--background-color-ui-100)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-hover',color:'var(--background-color-ui-hover)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-brand',color:'var(--background-color-ui-brand)',contrastColor:'var(--text-color-ui-onbrand)'},
		{label:'bg-ui-brand-hover',color:'var(--background-color-ui-brand-hover)',contrastColor:'var(--text-color-ui-onbrand)'},
		{label:'bg-ui-brand-secondary',color:'var(--background-color-ui-brand-secondary)',contrastColor:'var(--text-color-ui-onbrand)'},
		{label:'bg-ui-statistic',color:'var(--background-color-ui-statistic)',contrastColor:'var(--text-color-ui-onstatistic)'},
		{label:'bg-ui-statistic-hover',color:'var(--background-color-ui-statistic-hover)',contrastColor:'var(--text-color-ui-onstatistic)'},
		{label:'bg-ui-statistic-secondary',color:'var(--background-color-ui-statistic-secondary)',contrastColor:'var(--text-color-ui-onstatistic)'},
		{label:'bg-ui-selected',color:'var(--background-color-ui-selected)',contrastColor:'var(--text-color-ui-onselected)'},
		{label:'bg-ui-selected-hover',color:'var(--background-color-ui-selected-hover)',contrastColor:'var(--text-color-ui-onselected)'},
		{label:'bg-ui-danger',color:'var(--background-color-ui-danger)',contrastColor:'var(--text-color-ui-ondanger)'},
		{label:'bg-ui-danger-hover',color:'var(--background-color-ui-danger-hover)',contrastColor:'var(--text-color-ui-ondanger)'},
		{label:'bg-ui-danger-secondary',color:'var(--background-color-ui-danger-secondary)',contrastColor:'var(--text-color-ui-ondanger)'},
		{label:'bg-ui-success',color:'var(--background-color-ui-success)',contrastColor:'var(--text-color-ui-onsuccess)'},
		{label:'bg-ui-success-hover',color:'var(--background-color-ui-success-hover)',contrastColor:'var(--text-color-ui-onsuccess)'},
		{label:'bg-ui-success-secondary',color:'var(--background-color-ui-success-secondary)',contrastColor:'var(--text-color-ui-onsuccess)'},
		{label:'bg-ui-warning',color:'var(--background-color-ui-warning)',contrastColor:'var(--text-color-ui-onwarning)'},
		{label:'bg-ui-warning-hover',color:'var(--background-color-ui-warning-hover)',contrastColor:'var(--text-color-ui-onwarning)'},
		{label:'bg-ui-warning-secondary',color:'var(--background-color-ui-warning-secondary)',contrastColor:'var(--text-color-ui-onwarning)'},
		{label:'bg-ui-caution',color:'var(--background-color-ui-caution)',contrastColor:'var(--text-color-ui-oncaution)'},
		{label:'bg-ui-caution-hover',color:'var(--background-color-ui-caution-hover)',contrastColor:'var(--text-color-ui-oncaution)'},
		{label:'bg-ui-caution-secondary',color:'var(--background-color-ui-caution-secondary)',contrastColor:'var(--text-color-ui-oncaution)'},
		{label:'bg-ui-neutral',color:'var(--background-color-ui-neutral)',contrastColor:'var(--text-color-ui-onneutral)'},
		{label:'bg-ui-neutral-hover',color:'var(--background-color-ui-neutral-hover)',contrastColor:'var(--text-color-ui-onneutral)'},
		{label:'bg-ui-neutral-secondary',color:'var(--background-color-ui-neutral-secondary)',contrastColor:'var(--text-color-ui-onneutral)'},
		{label:'bg-ui-disabled',color:'var(--background-color-ui-disabled)',contrastColor:'var(--text-color-ui-ondisabled)'},
		{label:'bg-uistatic',color:'var(--background-color-uistatic)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-uistatic-0',color:'var(--background-color-uistatic-0)',contrastColor:'var(--text-color-ui-0)'},
		{label:'bg-uistatic-10',color:'var(--background-color-uistatic-10)',contrastColor:'var(--text-color-ui-10)'},
		{label:'bg-uistatic-25',color:'var(--background-color-uistatic-25)',contrastColor:'var(--text-color-ui-25)'},
		{label:'bg-uistatic-50',color:'var(--background-color-uistatic-50)',contrastColor:'var(--text-color-ui-50)'},
		{label:'bg-uistatic-75',color:'var(--background-color-uistatic-75)',contrastColor:'var(--text-color-ui-75)'},
		{label:'bg-uistatic-100',color:'var(--background-color-uistatic-100)',contrastColor:'var(--text-color-ui-100)'},
	  ],	  
	  theme: {
		colors: {
		  gray: {
			700: '#262627', // Diese Farbe wird für den Hintergrund im Darkmode verwendet. Sie wird hier auf die Farbe von bg-ui im Darkmode gesetzt
		  },
		},
	  }
})
