<template>
	<Story title="Farben" id="farben" group="top" icon="ri:palette-line" :responsive-disabled="true" :layout="{type: 'single', iframe: false}">
		<Variant title="Farben" source=" " id="farbpalette">
			<div class="svws-docs-color">
				<div class="htw-prose dark:htw-prose-invert">
					<h1>Farben</h1>
					<p>
						Für den Client und in den Components ist eine allgemeine Farbpalette definiert, mit der alle
						möglichen Einsatzfälle abgedeckt werden können:
					</p>
				</div>
				<div
					class="svws-docs-color-shades">
					<div class="flex flex-col gap-2 text-base" v-for="(color, index) in colors" :key="index">
						<div class="svws-docs-color-shade rounded-lg w-full" :style="{backgroundColor: color.color}" :class="{'border border-black/25': color.name.toLowerCase() === 'white'}">
							<span style="user-select: all" class="svws-docs-color-shade-badge" :style="{color: color.text || '#2c2c2c', backgroundColor: color.color}">bg-{{ color.name.toLowerCase() }}</span>
							<span v-if="color.allowText" style="user-select: all" class="svws-docs-color-shade-badge" :style="{color: color.color, backgroundColor: color.background || '#ffffff'}">text-{{ color.name.toLowerCase() }}</span>
						</div>
						<div class="flex flex-col gap-y-1">
							<span class="text-sm font-bold">{{ color.name }} <span style="user-select: all">var(--color-{{ color.name.toLowerCase() }})</span></span>
							<span>{{ color.useFor }}</span>
						</div>
					</div>
				</div>
				<div class="htw-prose dark:htw-prose-invert">
					<p>
						Zusätzlich gibt es noch eine größere Auswahl von Farben, die <strong>nur in besonderen Fällen</strong> verwendet
						werden dürfen. Zum Beispiel als Hintergrundfarben für verschiedene Fächer in der Kursübersicht. Dafür können auch die weiteren Farben von Tailwind verwendet werden.
					</p>
				</div>
			</div>
		</Variant>
	</Story>
</template>
<script setup lang="ts">
	const colors = [
		{
			'name': 'SVWS',
			'additional': 'primary',
			'useFor': 'Primärer Button, Aktiver Status, klickbare Elemente',
			'color': '#2285d5',
			'allowText': true,
			'text': '#ffffff',
		},
		{
			'name': 'Statistik',
			'useFor': 'Primäre Farbe für UI Elemente im Statistik-Bereich, Border und Label von statik-relevanten Elementen',
			'color': '#8b5cf6',
			'tailwind': 'violet-500',
			'allowText': true,
			'text': '#ffffff',
		},
		{
			'name': 'Light',
			'useFor': 'App Hintergrund, Abhebung von Bereichen auf weißen Hintergründen (Subnavigation)',
			'color': '#f5f4f2',
		},
		{
			'name': 'Black',
			'useFor': 'Text, Icons',
			'color': '#2C2C2C',
			'allowText': true,
			'text': '#ffffff',
		},
		{
			'name': 'White',
			'useFor': 'Hintergrund für Sidebar, Inhalt',
			'color': '#ffffff',
			'allowText': true,
			'background': '#2C2C2C'
		},
		{
			'name': 'Error',
			'useFor': 'Fehlermeldungen, Warnungen, "Löschen" Icon',
			'color': '#e11d48',
			'tailwind': 'rose-600',
			'allowText': true,
			'text': '#ffffff',
		},
		{
			'name': 'Success',
			'useFor': 'Erfolgsmeldungen, "Check" Icon',
			'color': '#84cc16',
			'tailwind': 'lime-500',
			'allowText': true,
			'text': '#ffffff',
		}
	]
</script>

<style lang="postcss" scoped>
.svws-docs-color {
@apply md:p-6;
}

.svws-docs-color-shades {
	@apply mb-24 mt-6 gap-y-10 gap-x-3 grid grid-cols-[repeat(auto-fit,minmax(400px,1fr))];
}

.svws-docs-color-shade {
	@apply h-32 flex items-center justify-center gap-1;
}

.svws-docs-color-shade-badge {
	@apply bg-white rounded px-1;
}
</style>
