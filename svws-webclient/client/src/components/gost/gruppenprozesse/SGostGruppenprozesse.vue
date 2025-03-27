<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-16">
			<ui-card icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Abiturjahrgäge werden gelöscht." :is-open="currentAction === 'delete'" @update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="checkIsEnabled[0] === true">Alle ausgewählten Abiturjahrgänge sind bereit zum Löschen.</span>
					<template v-else v-for="message in checkIsEnabled[1]" :key="message">
						<span class="text-ui-danger"> {{ message }} <br> </span>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="loading" title="Löschen" @click="entferneAbiturjahrgaenge" :is-loading="loading">
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
	import type { GostGruppenprozesseProps } from "./SGostGruppenprozesseProps";
	import type { List } from "@core";

	const props = defineProps<GostGruppenprozesseProps>();

	const currentAction = ref<string>('');
	const oldAction = ref({
		name: "",
		open: false,
	});

	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const checkIsEnabled = computed(() => {
		if (currentAction.value === 'delete')
			return props.removeAbiturjahrgaengeCheck();
		return [false, []];
	})

	function setCurrentAction(newAction: "delete", open: boolean) {
		if(newAction === oldAction.value.name && !open)
			return;
		oldAction.value.name = currentAction.value;
		oldAction.value.open = (currentAction.value === "") ? false : true;
		if(open === true)
			currentAction.value= newAction;
		else
			currentAction.value = "";
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
