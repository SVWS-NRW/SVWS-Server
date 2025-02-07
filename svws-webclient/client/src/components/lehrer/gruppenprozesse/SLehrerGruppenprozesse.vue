<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-16" v-if="ServerMode.DEV.checkServerMode(serverMode)">
			<svws-ui-action-button v-if="hatKompetenzLoeschen" title="Löschen" description="Ausgewählte Lehrer werden gelöscht." icon="i-ri-delete-bin-line"
				:action-function="entferneLehrer" action-label="Löschen" :is-loading="loading" :is-active="currentAction === 'delete'"
				:action-disabled="lehrerListeManager().getIdsReferenzierterLehrer().size() === lehrerListeManager().liste.auswahlSize()" @click="toggleDeleteLehrer">
				<span v-if="preConditionCheck[0]">Alle ausgewählten Lehrer sind bereit zum Löschen.</span>
				<template v-else v-for="message in preConditionCheck[1]" :key="message">
					<span class="text-ui-danger"> {{ message }} <br> </span>
				</template>
				<span v-if="loeschbareLehrerVorhanden">Einige Lehrer sind noch an anderer Stelle referenziert, die Übrigen können gelöscht werden.</span>
			</svws-ui-action-button>
			<log-box :logs :status>
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen</svws-ui-button>
				</template>
			</log-box>
		</div>
		<div v-else>
			<svws-ui-todo title="Lehrer - Gruppenprozesse">
				Dieser Bereich ist noch in Entwicklung. Hier werden später Gruppenprozesse zu den Lehrern vorhanden sein.
			</svws-ui-todo>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import { BenutzerKompetenz, List } from "@core";
	import { ServerMode } from "@core";
	import type { LehrerGruppenprozesseProps } from "~/components/lehrer/gruppenprozesse/SLehrerGruppenprozesseProps";

	const props = defineProps<LehrerGruppenprozesseProps>();

	const currentAction = ref<string>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LOESCHEN));

	const alleLehrerLoeschbar = computed(() => (currentAction.value === 'delete') && props.lehrerListeManager().getIdsReferenzierterLehrer().isEmpty());
	const loeschbareLehrerVorhanden = computed(() =>
		!alleLehrerLoeschbar.value && (props.lehrerListeManager().getIdsReferenzierterLehrer().size() !== props.lehrerListeManager().liste.auswahlSize()));

	const preConditionCheck = computed(() => {
		if (currentAction.value === 'delete')
			return props.deleteLehrerCheck();
		return [true, []];
	})

	function toggleDeleteLehrer() {
		currentAction.value = currentAction.value === 'delete' ? '' : 'delete';
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function entferneLehrer() {
		loading.value = true;

		const [delStatus, logMessages] = await props.deleteLehrer();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';

		loading.value = false;
	}

</script>
