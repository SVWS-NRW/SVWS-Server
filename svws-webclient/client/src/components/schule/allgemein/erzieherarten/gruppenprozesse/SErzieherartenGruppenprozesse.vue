<template>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper>
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Erzieherarten werden gelöscht.">
				<div>
					<span v-if="nonSelected">Keine Erzieherarten ausgewählt</span>
					<span v-else-if="allDeletable">Alle ausgewählten Erzieherarten sind bereit zum Löschen.</span>
					<span v-else-if="someDeletable">Einige Erzieherarten sind noch Erziehungsberechtigten zugeordnet. Die Übrigen können gelöscht werden.</span>
					<div v-if="nonDeletableLogs.size() !== 0">
						<div v-for="log in nonDeletableLogs" :key="log" class="text-ui-danger"> {{ log }} </div>
					</div>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button title="Löschen" :disabled="(!allDeletable && !someDeletable) || props.manager().liste.auswahlSize() === 0" @click="entferneErzieherarten">
						Löschen
					</svws-ui-button>
				</template>
			</ui-card>
			<log-box :logs :status>
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen</svws-ui-button>
				</template>
			</log-box>
		</svws-ui-input-wrapper>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import { ArrayList, BenutzerKompetenz, type List } from "@core";
	import type { SErzieherartenGruppenprozesseProps } from "~/components/schule/allgemein/erzieherarten/gruppenprozesse/SErzieherartenGruppenprozesseProps";

	const props = defineProps<SErzieherartenGruppenprozesseProps>();
	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();
	const nonSelected = computed(() => props.manager().liste.auswahlSize() === 0);
	const allDeletable = computed(() => props.manager().getIdsVerwendeteErzieherarten().isEmpty());
	const someDeletable = computed(() =>
		(!allDeletable.value) && (props.manager().getIdsVerwendeteErzieherarten().size() !== props.manager().liste.auswahlSize()));

	const nonDeletableLogs = computed(() => {
		const logs = new ArrayList<string>();
		if (allDeletable.value)
			return logs;
		for (const idErzieherart of props.manager().getIdsVerwendeteErzieherarten())
			logs.add(`Die Erzieherart "${props.manager().liste.get(idErzieherart)?.bezeichnung ?? '???'}" kann nicht gelöscht werden, da sie noch Erziehungsberechtigten zugeordnet ist.`);
		return logs;
	})

	function clearLog() {
		logs.value = undefined;
		status.value = undefined;
	}

	async function entferneErzieherarten() {
		const [delStatus, logMessages] = await props.delete();
		logs.value = logMessages;
		status.value = delStatus;
	}

</script>
