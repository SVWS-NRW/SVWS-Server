<template>
	<svws-ui-button v-if="hatKompetenzAendern" type="trash" :title="`${art} löschen`" :disabled="disabled || loading || items.length === 0" @click="open" />
	<svws-ui-modal v-model:show="show" size="small" :auto-close="!loading" :close-in-title="!loading">
		<template #modalTitle>{{ art }} löschen</template>
		<template #modalContent>
			<p>{{ auswahl.length }} ausgewählte Einträge aus dem Planungsabschnitt „{{ state.planungsabschnitt?.beschreibung }}“ löschen?</p>
			<ul class="mt-3 list-disc pl-5"><li v-for="item in auswahl" :key="item.id">{{ bezeichnung(item) }}</li></ul>
			<svws-ui-notification v-if="error" type="error" class="mt-4">{{ error }}</svws-ui-notification>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" :disabled="loading" @click="show = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="danger" :disabled="loading || disabled || !hatKompetenzAendern" :is-loading="loading" @click="remove">Löschen</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts" generic="T extends { id: number }">
	import { computed, ref, shallowRef, watch } from 'vue';

	import { useUvState } from '@ui/states/UvState';

	const props = defineProps<{
		items: T[];
		art: string;
		bezeichnung: (item: T) => string;
		loeschen: (items: T[]) => Promise<void>;
		disabled?: boolean;
	}>();
	const emit = defineEmits<{ deleted: [items: T[]] }>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const show = ref(false);
	const loading = ref(false);
	const error = ref('');
	const auswahl = shallowRef<T[]>([]);
	watch(() => state.planungsabschnitt?.id, () => {
		show.value = false;
		error.value = '';
	});

	function open() {
		if (!hatKompetenzAendern.value || props.disabled || loading.value || props.items.length === 0) {
			return;
		}
		auswahl.value = [...props.items];
		error.value = '';
		show.value = true;
	}

	async function remove() {
		if (!show.value || !hatKompetenzAendern.value || props.disabled || loading.value || auswahl.value.length === 0) {
			return;
		}
		const id = state.planungsabschnitt?.id;
		const items = [...auswahl.value];
		loading.value = true;
		error.value = '';
		try {
			await props.loeschen([...items]);
			if (state.planungsabschnitt?.id === id) {
				show.value = false;
				emit('deleted', items);
			}
		} catch {
			if (state.planungsabschnitt?.id === id) {
				error.value = `${props.art} konnten nicht gelöscht werden. Bitte erneut versuchen.`;
			}
		} finally {
			loading.value = false;
		}
	}
</script>
