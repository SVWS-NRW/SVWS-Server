<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a title="Kataloge" @click="router.push({name: 'kataloge' })">Kataloge</a>
				<span title="Förderschwerpunkte">Förderschwerpunkte</span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-data-table :clicked="auswahl" clickable @update:clicked="setFoerderschwerpunkt" :items="listFoerderschwerpunkte" :columns="cols" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@svws-nrw/svws-ui";
	import { router } from "~/router/RouteManager";
	import { FoerderschwerpunkteAuswahlProps } from "./SFoerderschwerpunkteAuswahlProps";

	const props = defineProps<FoerderschwerpunkteAuswahlProps>();

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "text", label: "Bezeichnung", sortable: true }
	];
</script>
