<template>
	<slot :open-modal />
	<svws-ui-modal :close-in-title="!loading" v-model:show="show" class="hidden" size="medium" :auto-close="false">
		<template #modalTitle>Lehrer importieren</template>
		<template #modalContent>
			<svws-ui-notification v-if="actionError" type="error" class="mb-3">{{ actionError }}</svws-ui-notification>
			<svws-ui-table :items="filteredLehrer" :columns selectable scroll v-model="selectedLehrer" count :no-data="!props.listLehrer.length" no-data-text="Es wurden keine noch nicht importierten Lehrkräfte gefunden.">
				<template #search>
					<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" v-model="search" type="search" placeholder="Suchen" removable />
				</template>
				<template #cell(kuerzel)="{ value }">
					{{ value }}
				</template>
				<template #cell(nachname)="{ value }">
					{{ value }}
				</template>
				<template #cell(vorname)="{ value }">
					{{ value }}
				</template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button :disabled="loading || !hatKompetenzAendern" type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="importieren()" :disabled="loading || !hatKompetenzAendern || !selectedLehrer.length"> Importieren </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import { useUvState } from "@ui/states/UvState";

	const props = defineProps<{
		listLehrer: LehrerListeEintrag[];
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const actionError = ref('');
	watch(() => state.planungsabschnitt?.id, () => {
		show.value = false;
		actionError.value = '';
	});

	const show = ref<boolean>(false);
	const search = ref<string>("");
	const selectedLehrer = ref<LehrerListeEintrag[]>([]);

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc', span: 0.3 },
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Vorname", sortable: true },
	];

	const filteredLehrer = computed(() => {
		if (search.value.trim().length === 0) {
			return props.listLehrer;
		}
		const s = search.value.toLocaleLowerCase();
		return props.listLehrer.filter(l =>
			l.kuerzel.toLocaleLowerCase().includes(s)
			|| l.nachname.toLocaleLowerCase().includes(s)
			|| l.vorname.toLocaleLowerCase().includes(s)
		);
	});

	const openModal = () => {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		actionError.value = '';
		selectedLehrer.value = [];
		search.value = "";
		show.value = true;
	};

	async function importieren() {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		actionError.value = '';
		try {
			await state.importLehrerFromSchule(selectedLehrer.value);
			selectedLehrer.value = [];
			show.value = false;
		} catch {
			actionError.value = 'Die Aktion konnte nicht abgeschlossen werden. Die Eingaben bleiben erhalten. Bitte den aktuellen Bestand prüfen und erneut versuchen.';
		} finally {
			loading.value = false;
		}
	}

</script>
