<template>
	<Story title="Farben" id="farben" group="top" icon="ri:palette-line" :responsive-disabled="true" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Info zu den Farben" source=" " id="info">
			<div class="htw-prose dark:htw-prose-invert">
				<p>
					Für den Client und in den Components ist eine allgemeine Farbpalette definiert, mit der alle
					möglichen Einsatzfälle abgedeckt werden können.
				</p>
			</div>
		</Variant>
		<Variant :title="color.name" source=" " :id="color.name" v-for="(color, index) in colors" :key="index">
			<div class="flex gap-2 text-base items-start">
				<div class="svws-docs-color-shade" :style="{backgroundColor: color.color}" :class="{'border border-black/25': color.name.toLowerCase() === 'white'}" />
				<div class="flex flex-wrap items-start gap-1 pt-1">
					<div class="w-full text-sm font-bold">{{ color.useFor }}</div>
					<div class="flex gap-2">
						<span class="svws-docs-color-shade-badge" style="user-select: all">var(--color-{{ color.name.toLowerCase() }})</span>
						<span style="user-select: all" class="svws-docs-color-shade-badge" :style="{color: color.text || '#2c2c2c', backgroundColor: color.color}">bg-{{ color.name.toLowerCase() }}</span>
						<span v-if="color.allowText" style="user-select: all" class="svws-docs-color-shade-badge" :style="{color: color.color, backgroundColor: color.background || '#ffffff'}">text-{{ color.name.toLowerCase() }}</span>
					</div>
				</div>
			</div>
		</Variant>
		<Variant title="Weitere Farben" source=" " id="weitere-farben">
			<div class="htw-prose dark:htw-prose-invert">
				<p>
					Zusätzlich gibt es noch eine größere Auswahl von Farben, die <strong>nur in besonderen Fällen</strong> verwendet
					werden dürfen. Zum Beispiel als Hintergrundfarben für verschiedene Fächer in der Kursübersicht. Dafür können auch die weiteren Farben von Tailwind verwendet werden.
				</p>
			</div>
		</Variant>
	</Story>
</template>
<script setup lang="ts">
	const colors = [
		{
			'name': 'Light',
			'useFor': 'App Hintergrund, Abhebung von Bereichen auf weißen Hintergründen (z.B. Subnavigation)',
			'color': '#f5f4f2',
		},
		{
			'name': 'SVWS',
			'additional': 'primary',
			'useFor': 'Primärer Button, aktiver Status, klickbare Elemente',
			'color': '#2285d5',
			'allowText': true,
			'text': '#ffffff',
		},
		{
			'name': 'Statistik',
			'useFor': 'Primäre Farbe für UI Elemente im Statistik-Bereich, Border und Label von statistikrelevanten Elementen',
			'color': '#8b5cf6',
			'tailwind': 'violet-500',
			'allowText': true,
			'text': '#ffffff',
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
			'useFor': 'Fehlermeldungen, Warnungen, "Löschen"-Icon',
			'color': '#e11d48',
			'tailwind': 'rose-600',
			'allowText': true,
			'text': '#ffffff',
		},
		{
			'name': 'Success',
			'useFor': 'Erfolgsmeldungen, "Check"-Icon',
			'color': '#84cc16',
			'tailwind': 'lime-500',
			'allowText': true,
			'text': '#ffffff',
		}
	]
</script>

<style lang="postcss" scoped>
.svws-docs-color-shade {
	@apply h-16 w-16 rounded-lg shrink-0;
}

.svws-docs-color-shade-badge {
	@apply bg-white rounded px-1 first:pl-0;
}
</style>
