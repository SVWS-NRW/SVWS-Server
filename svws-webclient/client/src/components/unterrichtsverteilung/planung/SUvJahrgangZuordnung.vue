<template>
	<div class="flex flex-col gap-2">
		<div class="flex items-center justify-between gap-2">
			<h4 class="text-sm font-semibold">{{ titel }}</h4>
			<svws-ui-button v-if="!readonly && bearbeitung === null" type="secondary" @click="props.bearbeiten(art, idZiel)">Bearbeiten</svws-ui-button>
		</div>
		<div class="flex flex-wrap gap-2">
			<span v-for="jahrgang in jahrgaenge" :key="jahrgang.id" class="inline-flex items-center gap-1 rounded border border-ui-25 bg-ui px-2 py-1 text-sm">
				{{ jahrgang.kuerzel ?? jahrgang.id }}
				<svws-ui-button v-if="bearbeitung?.art === art && bearbeitung.idZiel === idZiel && !readonly"
					type="icon"
					:disabled="loading"
					:title="`${jahrgang.kuerzel ?? jahrgang.id} entfernen`"
					@click="props.entfernen(jahrgang.id)"><span class="icon i-ri-close-line" /></svws-ui-button>
			</span>
			<span v-if="jahrgaenge.length === 0" class="text-sm text-ui-secondary">Keine Jahrgänge zugeordnet</span>
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { JahrgangsDaten } from '@core/core/data/jahrgang/JahrgangsDaten';

	import type { UvZuordnungArt, UvZuordnungBearbeitung } from './UvPlanung';

	const props = defineProps<{
		titel: string;
		jahrgaenge: JahrgangsDaten[];
		art: UvZuordnungArt;
		idZiel: number;
		bearbeitung: UvZuordnungBearbeitung | null;
		readonly: boolean;
		loading: boolean;
		bearbeiten: (art: UvZuordnungArt, idZiel: number) => void;
		entfernen: (id: number) => void;
	}>();

</script>
