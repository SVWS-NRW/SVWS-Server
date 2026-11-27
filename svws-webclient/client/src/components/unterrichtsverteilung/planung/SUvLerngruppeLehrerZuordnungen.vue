<template>
	<div class="mt-2 border-t border-ui-10 pt-2 text-xs">
		<div class="mb-1 font-semibold">Zugeordnete Lehrkräfte · {{ zuordnungen.length }}</div>
		<p v-if="zuordnungen.length === 0" class="text-ui-secondary">Noch keine Lehrkraft zugeordnet.</p>
		<div v-for="zuordnung in zuordnungen" :key="zuordnung.id" class="flex items-center gap-2 py-1">
			<span class="min-w-0 flex-1 truncate">{{ lehrerText(zuordnung.idLehrer) }}</span>
			<span class="shrink-0">{{ stunden(zuordnung.wochenstunden) }} h</span>
			<s-uv-lehrer-fixierung :id-lerngruppe :id-lehrer="zuordnung.idLehrer" />
			<svws-ui-button v-if="state.hatKompetenzAendern" type="icon" :title="`Zuordnung von ${lehrerText(zuordnung.idLehrer)} bearbeiten`"
				@click="props.bearbeiten(zuordnung.id)">
				<span class="icon i-ri-edit-2-line" />
			</svws-ui-button>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed } from 'vue';

	import { useUvState } from '@ui/states/UvState';

	import { formatUvStunden as stunden } from '../UvPresenter';

	import SUvLehrerFixierung from './SUvLehrerFixierung.vue';
	import { useUvPlanungsManager } from './UvPlanungsKontext';

	const props = defineProps<{
		idLerngruppe: number;
		bearbeiten: (id: number) => void;
	}>();

	const state = useUvState();
	const planungsManager = useUvPlanungsManager();
	const zuordnungen = computed(() => planungsManager.value?.lerngruppeLehrerZuordnungen(props.idLerngruppe) ?? []);

	function lehrerText(id: number): string {
		return planungsManager.value?.lehrerAnzeigeText(id) ?? `Lehrkraft ID ${id}`;
	}

</script>
