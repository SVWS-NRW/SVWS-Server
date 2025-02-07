<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-16">
			<svws-ui-action-button title="Löschen" description="Ausgewählte Abiturjahrgäge werden gelöscht." icon="i-ri-delete-bin-line"
				:action-function="entferneAbiturjahrgaenge" action-label="Löschen" :is-loading="loading" :is-active="currentAction === 'delete'"
				:action-disabled="!checkIsEnabled[0]" @click="toggleDelete">
				<span v-if="checkIsEnabled[0] === true">Alle ausgewählten Abiturjahrgänge sind bereit zum Löschen.</span>
				<template v-else v-for="message in checkIsEnabled[1]" :key="message">
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

	import { computed, ref } from "vue";
	import type { GostGruppenprozesseProps } from "./SGostGruppenprozesseProps";
	import type { List } from "@core";

	const props = defineProps<GostGruppenprozesseProps>();

	const currentAction = ref<string>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const checkIsEnabled = computed(() => {
		if (currentAction.value === 'delete')
			return props.removeAbiturjahrgaengeCheck();
		return [false, []];
	})

	function toggleDelete() {
		currentAction.value = currentAction.value === 'delete' ? '' : 'delete';
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function entferneAbiturjahrgaenge() {
		loading.value = true;
		const [delStatus, logMessages] = await props.removeAbiturjahrgaenge();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';
		loading.value = false;
	}

</script>
