<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-16" v-if="ServerMode.DEV.checkServerMode(serverMode)">
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Fächer werden gelöscht."
				:is-open="currentAction === 'delete'" @update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="preConditionCheck[0]">Alle ausgewählten Fächer sind bereit zum Löschen.</span>
					<template v-else v-for="message in preConditionCheck[1]" :key="message">
						<span class="text-ui-danger"> {{ message }} <br> </span>
					</template>
					<span v-if="loeschbareFaecherVorhanden">Einige Fächer sind noch an anderer Stelle referenziert, die Übrigen können gelöscht werden.</span>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="manager().getIdsReferenzierterFaecher().size() === manager().liste.auswahlSize() || loading"
						title="Löschen" @click="entferneFaecher" :is-loading="loading" class="mt-4">
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
			<svws-ui-todo title="Fächer - Gruppenprozesse">
				Dieser Bereich ist noch in Entwicklung. Hier werden später Gruppenprozesse zu den Fächern vorhanden sein.
			</svws-ui-todo>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { SchuleFachGruppenprozesseProps } from "./SSchuleFachGruppenprozesseProps";
	import type { List } from "@core";
	import { ServerMode, BenutzerKompetenz } from "@core";

	const props = defineProps<SchuleFachGruppenprozesseProps>();

	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));

	const currentAction = ref<string>('');
	const oldAction = ref<{ name: string | undefined; open: boolean }>({
		name: undefined,
		open: false,
	});
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const alleFaecherLoeschbar = computed(() => (currentAction.value === 'delete') && props.manager().getIdsReferenzierterFaecher().isEmpty());
	const loeschbareFaecherVorhanden = computed(() =>
		!alleFaecherLoeschbar.value && (props.manager().getIdsReferenzierterFaecher().size() !== props.manager().liste.auswahlSize()));

	const preConditionCheck = computed(() => {
		if (currentAction.value === 'delete')
			return props.deleteFaecherCheck();
		return [true, []];
	})

	function setCurrentAction(newAction: string, open: boolean) {
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

	async function entferneFaecher() {
		loading.value = true;

		const [delStatus, logMessages] = await props.deleteFaecher();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';

		loading.value = false;
	}

</script>
