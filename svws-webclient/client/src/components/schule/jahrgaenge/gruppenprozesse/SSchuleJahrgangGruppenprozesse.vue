<template>
	<div class="page--content">
		<div class="flex flex-col gap-y-16 lg:gap-y-16" v-if="ServerMode.DEV.checkServerMode(serverMode)">
			<svws-ui-action-button title="Löschen" description="Ausgewählte Jahrgänge werden gelöscht." icon="i-ri-delete-bin-line"
				:action-function="deleteJahrgaenge" action-label="Löschen" :is-loading="loading" :is-active="currentAction === 'delete'"
				:action-disabled="true" @click="toggleDeleteJahrgaenge">
				<!-- TODO: Vollständige Vorbedingungsprüfung für das Löschen einbauen -->
				<span v-if="false">Alle ausgewählten Jahrgänge sind bereit zum Löschen.</span>
				<template v-else v-for="message in []" :key="message">
					<span class="text-error"> {{ message }} <br> </span>
				</template>
			</svws-ui-action-button>
			<log-box :logs :status>
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen</svws-ui-button>
				</template>
			</log-box>
		</div>
		<div v-else>
			<svws-ui-todo title="Jahrgänge - Gruppenprozesse">
				Dieser Bereich ist noch in Entwicklung. Hier werden später Gruppenprozesse zu den Jahrgängen vorhanden sein.
			</svws-ui-todo>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { SchuleJahrgangGruppenprozesseProps } from "./SSchuleJahrgangGruppenprozesseProps";
	import type { List } from "@core";
	import { ServerMode } from "@core";

	const props = defineProps<SchuleJahrgangGruppenprozesseProps>();

	const currentAction = ref<string>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	function toggleDeleteJahrgaenge() {
		currentAction.value = currentAction.value === 'delete' ? '' : 'delete';
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function deleteJahrgaenge() {
		loading.value = true;

		const [delStatus, logMessages] = await props.delete();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';

		loading.value = false;
	}

</script>
