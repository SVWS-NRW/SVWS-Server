<template>
	<div class="inline-flex items-center gap-1">
		<svws-ui-button v-if="state.hatKompetenzAendern"
			type="icon"
			size="small"
			class="m-0!"
			:disabled="loading || disabled"
			:is-loading="loading"
			:aria-pressed="fixiert"
			:title="titel"
			@click="umschalten">
			<span class="icon" :class="fixiert ? 'i-ri-pushpin-fill text-ui-brand' : 'i-ri-pushpin-line text-ui-secondary'" />
		</svws-ui-button>
		<span v-else-if="fixiert" class="icon i-ri-pushpin-fill text-ui-brand" :title="titel" />
		<svws-ui-notification v-if="fehler" type="error">{{ fehler }}</svws-ui-notification>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref } from 'vue';

	import { useUvState } from '@ui/states/UvState';

	import { useUvPlanungsManager } from './UvPlanungsKontext';

	const props = defineProps<{
		idLerngruppe?: number;
		idKlasse?: number;
		reihenfolge?: number;
		idLehrer: number;
		disabled?: boolean;
	}>();
	const emit = defineEmits<{ busy: [value: boolean] }>();
	const state = useUvState();
	const loading = ref(false);
	const fehler = ref('');
	const planungsManager = useUvPlanungsManager();
	const fixiert = computed(() => {
		const manager = planungsManager.value;
		if (manager === null) {
			return false;
		}
		return props.idKlasse !== undefined ? manager.klassenlehrerIstFixiert(props.idKlasse, props.idLehrer)
			: props.idLerngruppe !== undefined && manager.lehrerIstFixiert(props.idLerngruppe, props.idLehrer);
	});
	const titel = computed(() => {
		const klassenrolle = props.reihenfolge === 1 ? 'Klassenleitung' : 'Stellvertretende Klassenleitung';
		const rolle = props.idKlasse === undefined ? 'Lehrkraft für diese Lerngruppe' : klassenrolle;
		return fixiert.value ? `Fixierung der ${rolle} lösen` : `${rolle} fixieren`;
	});

	async function umschalten(): Promise<void> {
		const manager = planungsManager.value;
		const idZiel = props.idKlasse ?? props.idLerngruppe;
		if (loading.value || props.disabled === true || !state.hatKompetenzAendern || manager === null
			|| idZiel === undefined) {
			return;
		}
		loading.value = true;
		emit('busy', true);
		fehler.value = '';
		try {
			await state.regeln.lehrerFixierungUmschalten(props.idKlasse !== undefined ? 'klasse' : 'lerngruppe', idZiel, props.idLehrer, props.reihenfolge ?? 0);
		} catch (error) {
			fehler.value = error instanceof Error ? error.message : 'Die Fixierung konnte nicht geändert werden.';
		} finally {
			loading.value = false;
			emit('busy', false);
		}
	}
</script>
