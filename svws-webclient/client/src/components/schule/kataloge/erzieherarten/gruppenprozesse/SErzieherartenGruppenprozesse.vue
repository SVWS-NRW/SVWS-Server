<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-16">
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Erzieherarten werden gelöscht."
				:is-open="currentAction === 'delete'" @update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="alleErziehungsberechtigteLeer">Alle ausgewählten Erzieherarten sind bereit zum Löschen.</span>
					<span v-if="leereErziehungsberechtigteVorhanden">Einige Erzieherarten haben noch Erziehungsberechtigte, leere Erzieherarten können gelöscht werden.</span>
					<div v-if="!alleErziehungsberechtigteLeer">
						<span v-for="message in nichtAlleErziehungsberechtigteLeer" :key="message" class="text-ui-danger"> {{ message }} <br> </span>
					</div>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="(manager().getErzieherartIDsMitPersonen().size() === manager().liste.auswahlSize()) || (loading)"
						title="Löschen" @click="entferneErzieherarten" :is-loading="loading" class="mt-4">
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

	import { ref, computed } from "vue";
	import { ArrayList, BenutzerKompetenz, type List } from "@core";
	import type { SErzieherartenGruppenprozesseProps } from "~/components/schule/kataloge/erzieherarten/gruppenprozesse/SErzieherartenGruppenprozesseProps";

	const props = defineProps<SErzieherartenGruppenprozesseProps>();

	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));

	const currentAction = ref<string>('');
	const oldAction = ref<{ name: string | undefined; open: boolean }>({
		name: undefined,
		open: false,
	});
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const alleErziehungsberechtigteLeer = computed(() => (currentAction.value === 'delete') && (props.manager().getErzieherartIDsMitPersonen().isEmpty()));

	const nichtAlleErziehungsberechtigteLeer = computed(() => {
		const errorLog: List<string> = new ArrayList<string>();
		if (!alleErziehungsberechtigteLeer.value)
			for (const erzieherart of props.manager().getErzieherartIDsMitPersonen())
				errorLog.add(`Erzieherart ${props.manager().liste.get(erzieherart)?.bezeichnung ?? '???'} (ID: ${erzieherart}) kann nicht gelöscht werden, da ihr noch Erziehungsberechtigten zugeordnet sind.`);
		return errorLog;
	})

	const leereErziehungsberechtigteVorhanden = computed(() =>
		(!alleErziehungsberechtigteLeer.value) && (props.manager().getErzieherartIDsMitPersonen().size() !== props.manager().liste.auswahlSize()));

	function setCurrentAction(newAction: string, open: boolean) {
		if(newAction === oldAction.value.name && !open)
			return;
		oldAction.value.name = currentAction.value;
		oldAction.value.open = (currentAction.value !== "");
		if(open)
			currentAction.value= newAction;
		else
			currentAction.value = "";
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function entferneErzieherarten() {
		loading.value = true;
		const [delStatus, logMessages] = await props.delete();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';
		loading.value = false;
	}

</script>
