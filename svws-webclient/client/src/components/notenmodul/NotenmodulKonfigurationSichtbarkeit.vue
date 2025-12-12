<template>
	<div class="w-full h-fit">
		<div class="text-headline-md mb-4">Voreinstellung für die Anzeige der Spalten bei den Notenübersichten</div>
		<div class="max-w-4xl input-wrapper input-wrapper--4">
			<div class="w-full">
				<div class="text-headline-sm">Allgemein</div>
				<div v-for="col of spaltenAllgemein" :key="col">
					<svws-ui-checkbox :title="col" :model-value="manager().istSichtbar(col)" @update:model-value="manager().toggle(col)" />
					{{ col }}
				</div>
			</div>
			<div class="w-full">
				<div class="text-headline-sm">Leistungsdaten</div>
				<div v-for="col of spaltenLeistungsdaten" :key="col">
					<svws-ui-checkbox :title="col" :model-value="manager().istSichtbar(col)" @update:model-value="manager().toggle(col)" />
					{{ col }}
				</div>
			</div>
			<div class="w-full">
				<div class="text-headline-sm">Bemerkungen</div>
				<div v-for="col of spaltenBemerkungen" :key="col">
					<svws-ui-checkbox :title="col" :model-value="manager().istSichtbar(col)" @update:model-value="manager().toggle(col)" />
					{{ col }}
				</div>
			</div>
			<div v-if="!manager().spaltenTeilleistungen.isEmpty()" class="w-full">
				<div class="text-headline-sm">Teilleistungen</div>
				<div>
					<svws-ui-checkbox :title="spalteTeilleistungen" :model-value="manager().istSichtbar(spalteTeilleistungen)"
						:indeterminate="hatNurMancheTeilleistungen()"
						@update:model-value="manager().toggleAlleTeilleistungen()" />
					Alle
				</div>
				<div v-for="col of manager().spaltenTeilleistungen" :key="col" class="pl-6">
					<svws-ui-checkbox :title="col" :model-value="manager().istSichtbar(col)" @update:model-value="toggleTeilleistung(col)" />
					{{ col }}
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { NotenmodulConfigManagerSichtbareSpalten } from '~/router/apps/notenmodul/NotenmodulConfigManagerSichtbareSpalten';

	const spaltenAllgemein = ["Kurs", "Kursart", "Lehrer"];
	const spaltenBemerkungen = ["FB", "ASV", "AUE", "ZB"];
	const spaltenLeistungsdaten = ["Quartalsnoten", "Note", "Mahnung", "Fehlstunden"];
	const spalteTeilleistungen = "Teilnoten";

	const props = defineProps<{
		manager: () => NotenmodulConfigManagerSichtbareSpalten;
	}>();

	async function toggleTeilleistung(colname: string) {
		const istSichtbar = props.manager().istSichtbar(spalteTeilleistungen);
		await props.manager().toggle(colname);
		const teilleistungIstSichtbar = props.manager().istSichtbar(colname);
		if (!istSichtbar && teilleistungIstSichtbar) {
			await props.manager().toggle(spalteTeilleistungen);
		}
	}

	function hatNurMancheTeilleistungen(): boolean {
		const istSichtbar = props.manager().istSichtbar(spalteTeilleistungen);
		for (const col of props.manager().spaltenTeilleistungen) {
			if (props.manager().istSichtbar(col) !== istSichtbar) {
				return true;
			}
		}
		return false;
	}

</script>
