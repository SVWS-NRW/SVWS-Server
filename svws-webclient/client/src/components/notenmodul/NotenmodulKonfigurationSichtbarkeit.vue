<template>
	<div class="w-full h-fit">
		<div class="text-headline-md mb-4">Voreinstellung für die Anzeige der Spalten bei der Noteneingabe</div>
		<div class="pl-4 max-w-4xl input-wrapper input-wrapper--4">
			<div class="w-full">
				<div class="text-headline-sm">Allgemein</div>
				<div v-for="col of manager().spaltenAllgemein" :key="col">
					<svws-ui-checkbox :title="col" :model-value="manager().istSichtbar(col)" @update:model-value="manager().toggle(col)" />
					{{ col }}
				</div>
			</div>
			<div class="w-full">
				<div class="text-headline-sm">Leistungsdaten</div>
				<div v-for="col of manager().spaltenLeistungsdaten" :key="col">
					<svws-ui-checkbox :title="col" :model-value="manager().istSichtbar(col)" @update:model-value="manager().toggle(col)" />
					{{ col }}
				</div>
			</div>
			<div class="w-full">
				<div class="text-headline-sm">Bemerkungen</div>
				<div v-for="col of manager().spaltenBemerkungen" :key="col">
					<svws-ui-checkbox :title="col" :model-value="manager().istSichtbar(col)" @update:model-value="manager().toggle(col)" />
					{{ col }}
				</div>
			</div>
			<div v-if="!manager().spaltenTeilleistungen.isEmpty()" class="w-full">
				<div class="text-headline-sm">Teilleistungen</div>
				<div>
					<svws-ui-checkbox :title="manager().spalteTeilleistungen" :model-value="manager().istSichtbar(manager().spalteTeilleistungen)"
						:indeterminate="manager().hatNurMancheTeilleistungen()"
						@update:model-value="manager().toggleAlleTeilleistungen()" />
					Alle
				</div>
				<div v-for="col of manager().spaltenTeilleistungen" :key="col" class="pl-6">
					<svws-ui-checkbox :title="col" :model-value="manager().istSichtbar(col)" @update:model-value="manager().toggleTeilleistung(col)" />
					{{ col }}
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { NotenmodulConfigManagerSichtbareSpalten } from '~/router/apps/notenmodul/NotenmodulConfigManagerSichtbareSpalten';


	const props = defineProps<{
		manager: () => NotenmodulConfigManagerSichtbareSpalten;
	}>();



</script>
