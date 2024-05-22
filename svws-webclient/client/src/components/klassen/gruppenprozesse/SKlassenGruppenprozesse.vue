<template>
	<div class="page--content">
		<div class="flex flex-col gap-y-16 lg:gap-y-16">
			<svws-ui-action-button title="Löschen"
								   description="Ausgewählte Klassen werden gelöscht."
								   icon="i-ri-delete-bin-line"
								   :action-function="deleteKlassen"
								   action-label="Löschen"
								   :is-loading="loading"
								   :is-active="currentAction === 'delete'"
								   @click="toggleDeleteKlassen" />

			<log-box :logs :status>
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen </svws-ui-button>
				</template>
			</log-box>
		</div>
	</div>
</template>

<script setup lang="ts">

	import {computed, ref} from "vue";
	import type { KlassenGruppenprozesseProps } from "./SKlassenGruppenprozesseProps";
	import {List} from "@core";

	const props = defineProps<KlassenGruppenprozesseProps>();

	const currentAction = ref<string>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string> | undefined>();
	const status = ref<boolean | undefined>();

	function toggleDeleteKlassen() {
		currentAction.value = currentAction.value === 'delete' ? '' : 'delete';
		// clearLog();
	}

	const clearLog = () => {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	const deleteKlassen = async () => {
		loading.value = true;

		await props.klassenListeManager().deleteKlassen()

		loading.value = false;
	}

</script>
