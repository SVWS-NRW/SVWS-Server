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
								   :actionDisabled="!preConditionCheck[0]"
								   @click="toggleDeleteKlassen" >
				<span style="color: red" v-if="!preConditionCheck[0]" v-for="message in preConditionCheck[1]">{{message}}<br></span>
				<span v-else>Alle ausgewählten Klassen sind bereit zum Löschen.</span>
			</svws-ui-action-button>
			<log-box :logs :status>
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen </svws-ui-button>
				</template>
			</log-box>
		</div>
	</div>
</template>

<script setup lang="ts">

	import {ref, computed} from "vue";
	import type { KlassenGruppenprozesseProps } from "./SKlassenGruppenprozesseProps";
	import {List} from "@core";

	const props = defineProps<KlassenGruppenprozesseProps>();

	const currentAction = ref<string>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const preConditionCheck = computed(() => {
		if (currentAction.value === 'delete')
			return props.deleteKlassenCheck();

		return [false, []];
	})

	function toggleDeleteKlassen() {
		currentAction.value = currentAction.value === 'delete' ? '' : 'delete';
	}

	const clearLog = () => {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	const deleteKlassen = async () => {
		loading.value = true;

		const [delStatus, logMessages] = await props.deleteKlassen();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';

		loading.value = false;
	}

</script>
