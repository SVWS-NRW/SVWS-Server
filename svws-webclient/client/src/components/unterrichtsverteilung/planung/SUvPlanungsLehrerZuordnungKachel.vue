<template>
	<s-uv-planungs-lehrer-kachel modus="zuordnung-detail" :kuerzel="lehrer.kuerzel" :name="`${lehrer.nachname}, ${lehrer.vorname}`" :faecher
		:auswahl-gesperrt="editing || loading" :auswaehlen="props.oeffnen">
		<template #rechts><span class="text-xs text-ui-secondary">{{ reihenfolge }}. Lehrkraft</span></template>
		<template #details>
			<div class="mt-1 flex min-w-0 items-end gap-1 border-t border-ui-10 pt-1">
				<s-uv-lehrer-fixierung v-if="idLerngruppe !== undefined || idKlasse !== undefined"
					:id-lerngruppe
					:id-klasse
					:reihenfolge="gespeicherteReihenfolge ?? reihenfolge"
					:id-lehrer="lehrer.id"
					:disabled="loading || (idLerngruppe !== undefined && editing)"
					@busy="emit('fixierung-busy', $event)" />
				<div class="grid min-w-0 flex-1 gap-1" :class="wochenstunden === undefined ? 'grid-cols-1' : 'grid-cols-2'">
					<div class="min-w-0 text-xs text-ui-secondary">
						Reihenfolge
						<svws-ui-input-number headless
							:model-value="reihenfolge"
							:readonly="readonly || fixiert"
							:disabled="loading"
							placeholder="Reihenfolge"
							aria-label="Reihenfolge"
							:min="1"
							@change="value => props.reihenfolgeAendern(value)" />
					</div>
					<div v-if="wochenstunden !== undefined" class="min-w-0 text-xs text-ui-secondary">
						Wochenstunden
						<svws-ui-input-number headless
							:model-value="wochenstunden"
							:readonly
							:disabled="loading"
							placeholder="Wochenstunden"
							aria-label="Wochenstunden"
							:min="0"
							:max="maxStunden"
							:steps="0.5"
							:decimal-places="2"
							@change="value => props.stundenAendern?.(value)" />
					</div>
				</div>
			</div>
		</template>
		<template #aktionen>
			<svws-ui-button v-if="editing && !readonly"
				type="danger"
				class="mt-2"
				:disabled="loading || fixiert"
				:title="fixiert ? 'Zuerst die Fixierung lösen' : undefined"
				@click="props.entfernen()">
				Entfernen
			</svws-ui-button>
		</template>
	</s-uv-planungs-lehrer-kachel>
</template>

<script setup lang="ts">
	import { computed } from 'vue';

	import type { UvLehrer } from '@core/core/data/uv/UvLehrer';
	import { useUvState } from '@ui/states/UvState';

	import { createUvPresenter } from '../UvPresenter';

	import SUvLehrerFixierung from './SUvLehrerFixierung.vue';
	import SUvPlanungsLehrerKachel from './SUvPlanungsLehrerKachel.vue';
	import { useUvPlanungsManager } from './UvPlanungsKontext';

	const props = defineProps<{
		lehrer: UvLehrer;
		idLerngruppe?: number;
		idKlasse?: number;
		reihenfolge: number;
		gespeicherteReihenfolge?: number;
		wochenstunden?: number;
		maxStunden?: number;
		readonly: boolean;
		loading: boolean;
		editing: boolean;
		oeffnen: () => void;
		reihenfolgeAendern: (value: number | null) => void;
		stundenAendern?: (value: number | null) => void;
		entfernen: () => void;
	}>();

	const emit = defineEmits<{ 'fixierung-busy': [aktiv: boolean] }>();

	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));
	const faecher = computed(() => presenter.value.lehrerFaecher(props.lehrer.id));
	const planungsManager = useUvPlanungsManager();
	const fixiert = computed(() => props.idKlasse !== undefined
		? (planungsManager.value?.klassenlehrerIstFixiert(props.idKlasse, props.lehrer.id) ?? false)
		: props.idLerngruppe !== undefined && (planungsManager.value?.lehrerIstFixiert(props.idLerngruppe, props.lehrer.id) ?? false));
</script>
