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
})
