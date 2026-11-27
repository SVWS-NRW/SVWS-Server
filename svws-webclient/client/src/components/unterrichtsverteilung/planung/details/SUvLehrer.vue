<template>
	<div v-if="lehrer !== null" class="flex flex-col gap-3 p-3">
		<svws-ui-content-card title="Lehrkraft">
			<p class="font-semibold">{{ lehrer.kuerzel }} · {{ lehrer.nachname }}, {{ lehrer.vorname }}</p>
			<svws-ui-button type="secondary" @click="props.grunddatenOeffnen(lehrer.id)">Grunddaten bearbeiten</svws-ui-button>
		</svws-ui-content-card>

		<svws-ui-content-card title="Lerngruppen der Lehrkraft">
			<div class="flex flex-col gap-2">
				<s-uv-planungs-lerngruppe-zeile v-for="gruppe in lerngruppen" :key="gruppe.id" :lerngruppe="gruppe"
					:planungs-manager :auswaehlen="() => props.lerngruppeAuswaehlen(gruppe.id)" />
				<p v-if="lerngruppen.length === 0" class="text-sm text-ui-secondary">Keine Lerngruppen zugeordnet.</p>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { computed } from 'vue';

	import type { UvLehrer } from '@core/core/data/uv/UvLehrer';

	import SUvPlanungsLerngruppeZeile from '../SUvPlanungsLerngruppeZeile.vue';
	import { useUvPlanungsManager } from '../UvPlanungsKontext';

	const props = defineProps<{
		lehrer: UvLehrer | null;
		grunddatenOeffnen: (id: number) => Promise<void>;
		lerngruppeAuswaehlen: (id: number) => void;
	}>();

	const planungsManager = useUvPlanungsManager();
	const lerngruppen = computed(() => props.lehrer === null ? [] : planungsManager.value?.lerngruppenByLehrer(props.lehrer.id) ?? []);
</script>
