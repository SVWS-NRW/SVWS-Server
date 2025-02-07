<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-16">
			<svws-ui-action-button v-if="hatKompetenzLoeschen" title="Löschen" description="Bei den ausgewählten Schülern wird ein Löschvermerk gesetzt." icon="i-ri-delete-bin-line"
				:action-function="entferneSchueler" action-label="Löschen" :is-loading="loading" :is-active="currentAction === 'delete'"
				:action-disabled="!preConditionCheck[0]" @click="toggleDeleteSchueler">
				<span v-if="preConditionCheck[0]">Alle ausgewählten Schüler sind bereit zum Löschen.</span>
				<template v-else v-for="message in preConditionCheck[1]" :key="message">
					<span class="text-ui-danger"> {{ message }} <br> </span>
				</template>
			</svws-ui-action-button>
			<log-box :logs :status>
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen</svws-ui-button>
				</template>
			</log-box>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { SchuelerGruppenprozesseProps } from "./SSchuelerGruppenprozesseProps";
	import { ArrayList, BenutzerKompetenz, type List } from "@core";

	const props = defineProps<SchuelerGruppenprozesseProps>();

	const currentAction = ref<string>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LOESCHEN));

	const preConditionCheck = computed(() => {
		if (currentAction.value === 'delete')
			return props.deleteSchuelerCheck();
		return [true, new ArrayList<string>()];
	})

	function toggleDeleteSchueler() {
		currentAction.value = currentAction.value === 'delete' ? '' : 'delete';
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function entferneSchueler() {
		loading.value = true;

		[status.value, logs.value] = await props.deleteSchueler();
		currentAction.value = '';

		loading.value = false;
	}

</script>
