<template>
	<div class="page--content">
		<div class="flex flex-col gap-y-16 lg:gap-y-16" v-if="ServerMode.DEV.checkServerMode(serverMode)">
			<svws-ui-action-button title="Löschen" description="Ausgewählte Schulen werden gelöscht." icon="i-ri-delete-bin-line"
				:action-function="deleteSchulen" action-label="Löschen" :is-loading="loading" :is-active="currentAction === 'delete'"
				:action-disabled="true" @click="toggleDeleteSchulen">
				<!-- TODO: Vollständige Vorbedingungsprüfung für das Löschen einbauen -->
				<span v-if="false">Alle ausgewählten Schulen sind bereit zum Löschen.</span>
				<template v-else v-for="message in []" :key="message">
					<span class="text-ui-danger"> {{ message }} <br> </span>
				</template>
			</svws-ui-action-button>
			<log-box :logs :status>
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen</svws-ui-button>
				</template>
			</log-box>
		</div>
		<div v-else>
			<svws-ui-todo title="Schulen - Gruppenprozesse">
				Dieser Bereich ist noch in Entwicklung. Hier werden später Gruppenprozesse zu den Schulen vorhanden sein.
			</svws-ui-todo>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { KatalogSchuleGruppenprozesseProps } from "./SKatalogSchuleGruppenprozesseProps";
	import type { List } from "@core";
	import { ServerMode } from "@core";

	const props = defineProps<KatalogSchuleGruppenprozesseProps>();

	const currentAction = ref<string>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	function toggleDeleteSchulen() {
		currentAction.value = currentAction.value === 'delete' ? '' : 'delete';
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function deleteSchulen() {
		loading.value = true;

		const [delStatus, logMessages] = await props.delete();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';

		loading.value = false;
	}

</script>
