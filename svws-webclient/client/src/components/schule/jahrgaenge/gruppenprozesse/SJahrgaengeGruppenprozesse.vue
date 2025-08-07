<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-16" v-if="ServerMode.DEV.checkServerMode(serverMode)">
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Jahrgänge werden gelöscht."
				:is-open="currentAction === 'delete'" @update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="preConditionCheck[0]">Alle ausgewählten Jahrgänge sind bereit zum Löschen.</span>
					<template v-else v-for="message in preConditionCheck[1]" :key="message">
						<span class="text-ui-danger"> {{ message }} <br> </span>
					</template>
					<span v-if="deletableEntriesExist">Einige Jahrgänge sind noch an anderer Stelle referenziert, die Übrigen können gelöscht werden.</span>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button class="mt-4" title="Löschen" @click="deleteJahrgaenge" :is-loading="loading"
						:disabled="(manager().getIdsReferencedJahrgaenge().size() === manager().liste.auswahlSize()) || loading || !hatKompetenzLoeschen">
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
		<div v-else>
			<svws-ui-todo title="Jahrgänge - Gruppenprozesse">
				Dieser Bereich ist noch in Entwicklung. Hier werden später Gruppenprozesse zu den Jahrgängen vorhanden sein.
			</svws-ui-todo>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { SchuleJahrgangGruppenprozesseProps } from "./SJahrgaengeGruppenprozesseProps";
	import type { List } from "@core";
	import { BenutzerKompetenz } from "@core";
	import { ServerMode } from "@core";

	const props = defineProps<SchuleJahrgangGruppenprozesseProps>();

	const currentAction = ref<string>('');
	const oldAction = ref<{ name: string | undefined; open: boolean }>({
		name: undefined,
		open: false,
	});
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();
	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));

	const allEntriesDeletable = computed(() => (currentAction.value === 'delete') && props.manager().getIdsReferencedJahrgaenge().isEmpty());
	const deletableEntriesExist = computed(() =>
		!allEntriesDeletable.value && (props.manager().getIdsReferencedJahrgaenge().size() !== props.manager().liste.auswahlSize()));

	const preConditionCheck = computed(() => {
		if (currentAction.value === 'delete')
			return props.deleteCheck();
		return [true, []];
	})

	function setCurrentAction(newAction: string, open: boolean) {
		if (newAction === oldAction.value.name && !open)
			return;
		oldAction.value.name = currentAction.value;
		oldAction.value.open = (currentAction.value === "");
		if (open)
			currentAction.value= newAction;
		else
			currentAction.value = "";
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
