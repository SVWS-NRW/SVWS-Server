<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-4">
			<ui-card icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Konfigurationen werden gelöscht."
				:is-open="currentAction === 'delete'" @update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="preConditionCheck[0]">Alle ausgewählten Konfigurationen sind bereit zum Löschen.</span>
					<template v-else v-for="message in preConditionCheck[1]" :key="message">
						<span class="text-ui-danger"> {{ message }} <br> </span>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button class="mt-4" title="Löschen" @click="entferneKonfiguration" :is-loading="loading"
						:disabled="loading">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Löschen
					</svws-ui-button>
				</template>
			</ui-card>
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
	import type { List } from "@core";
	import type { NotenmodulKonfigurationGruppenprozesseProps } from "./NotenmodulKonfigurationGruppenprozesseProps";

	const props = defineProps<NotenmodulKonfigurationGruppenprozesseProps>();

	const currentAction = ref<string>('');
	const oldAction = ref<{ name: string | undefined; open: boolean }>({
		name: undefined,
		open: false,
	});
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const preConditionCheck = computed(() => {
		return [true, []];
	});

	function setCurrentAction(newAction: string, open: boolean) {
		if (newAction === oldAction.value.name && !open)
			return;
		oldAction.value.name = currentAction.value;
		oldAction.value.open = (currentAction.value !== "");
		if (open === true)
			currentAction.value = newAction;
		else
			currentAction.value = "";
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function entferneKonfiguration() {
		loading.value = true;

		const [delStatus, logMessages] = await props.deleteKonfiguration();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';

		loading.value = false;
	}

</script>
