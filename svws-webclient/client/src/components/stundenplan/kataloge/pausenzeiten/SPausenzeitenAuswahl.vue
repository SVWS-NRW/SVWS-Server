<template>
	<div class="flex flex-col gap-4">
		<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="stundenplanManager().pausenzeitGetMengeAsList()" :columns selectable v-model="selected" class="max-w-128 min-w-96">
			<template #cell(wochentag)="{ value }">
				{{ Wochentag.fromIDorException(value).beschreibung }}
			</template>
			<template #cell(beginn)="{ value }">
				{{ DateUtils.getStringOfUhrzeitFromMinuten(value) }}
			</template>
			<template #cell(ende)="{ value }">
				{{ DateUtils.getStringOfUhrzeitFromMinuten(value) }}
			</template>
			<template #actions>
				<svws-ui-button @click="doDeleteEintraege" type="trash" :disabled="selected.length === 0" />
				<svws-ui-button type="transparent" title="Pausenzeiten exportieren" @click="export_pausenzeiten" :disabled="selected.length === 0"><span class="icon-sm i-ri-upload-2-line" /></svws-ui-button>
				<s-pausenzeit-import-modal v-slot="{ openModal }" :set-katalog-pausenzeiten-import-j-s-o-n>
					<svws-ui-button type="icon" @click="openModal">
						<span class="icon-sm i-ri-download-2-line" />
					</svws-ui-button>
				</s-pausenzeit-import-modal>
				<s-pausenzeit-neu-modal v-slot="{ openModal }" :add-pausenzeiten :stundenplan-manager>
					<svws-ui-button type="icon" @click="openModal">
						<span class="icon i-ri-add-line" />
					</svws-ui-button>
				</s-pausenzeit-neu-modal>
			</template>
		</svws-ui-table>
		<svws-ui-action-button title="Alle Pausenzeiten erstellen" :is-active="actionPausenzeiten" @click="()=>actionPausenzeiten = !actionPausenzeiten" icon="i-ri-add-line">
			<svws-ui-input-wrapper>
				Voreinstellungen:
				<svws-ui-text-input placeholder="Unterrichtsbeginn" :model-value="DateUtils.getStringOfUhrzeitFromMinuten(stundenplanManager().stundenplanGetDefaultUnterrichtsbeginn())" @change="start => (start !== null) && stundenplanManager().stundenplanSetDefaultUnterrichtsbeginn(DateUtils.gibMinutenOfZeitAsString(start))" />
				<svws-ui-input-number placeholder="Stundendauer" :model-value="stundenplanManager().stundenplanGetDefaultStundendauer()" @change="dauer => (dauer !== null) && stundenplanManager().stundenplanSetDefaultStundendauer(dauer)" :min="5" :max="1440" />
				<svws-ui-input-number placeholder="Pausenzeit Für Raumwechsel" :model-value="stundenplanManager().stundenplanGetDefaultPausenzeitFuerRaumwechsel()" @change="dauer => (dauer !== null) && stundenplanManager().stundenplanSetDefaultPausenzeitFuerRaumwechsel(dauer)" :min="0" :max="1440" />
				<svws-ui-input-number placeholder="1. Pause nach Stunde" :model-value="stundenplanManager().stundenplanGetDefaultVormittagspause1Nach()" @change="nach => (nach !== null) && stundenplanManager().stundenplanSetDefaultVormittagspause1Nach(nach)" :min="0" :max="99" />
				<svws-ui-input-number placeholder="1. Pause Dauer" :model-value="stundenplanManager().stundenplanGetDefaultVormittagspause1Dauer()" @change="dauer => (dauer !== null) && stundenplanManager().stundenplanSetDefaultVormittagspause1Dauer(dauer)" :min="0" :max="99" />
				<svws-ui-input-number placeholder="2. Pause nach Stunde" :model-value="stundenplanManager().stundenplanGetDefaultVormittagspause2Nach()" @change="nach => (nach !== null) && stundenplanManager().stundenplanSetDefaultVormittagspause2Nach(nach)" :min="0" :max="99" />
				<svws-ui-input-number placeholder="2. Pause Dauer" :model-value="stundenplanManager().stundenplanGetDefaultVormittagspause2Dauer()" @change="dauer => (dauer !== null) && stundenplanManager().stundenplanSetDefaultVormittagspause2Dauer(dauer)" :min="0" :max="99" />
				<svws-ui-input-number placeholder="Mittagsause nach Stunde" :model-value="stundenplanManager().stundenplanGetDefaultMittagspauseNach()" @change="nach => (nach !== null) && stundenplanManager().stundenplanSetDefaultMittagspauseNach(nach)" :min="0" :max="99" />
				<svws-ui-input-number placeholder="Mittagsause Dauer" :model-value="stundenplanManager().stundenplanGetDefaultMittagspauseDauer()" @change="dauer => (dauer !== null) && stundenplanManager().stundenplanSetDefaultMittagspauseDauer(dauer)" :min="0" :max="99" />
				<svws-ui-button type="secondary" @click="addBlock" title="Alle Pausenzeiten erstellen">
					<span class="icon i-ri-calendar-event-line" />
					<span class="icon i-ri-add-line -ml-1" />Alle Pausenzeiten erstellen
				</svws-ui-button>
			</svws-ui-input-wrapper>
		</svws-ui-action-button>
	</div>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { PausenzeitenAuswahlProps } from "./SPausenzeitenAuswahlProps";
	import { StundenplanPausenzeit } from "@core";
	import { Wochentag , DateUtils } from "@core";

	const props = defineProps<PausenzeitenAuswahlProps>();
	const selected = ref<StundenplanPausenzeit[]>([]);
	const actionPausenzeiten = ref<boolean>(false);

	const columns = [
		{key: 'wochentag', label: 'Wochentag', span: 2},
		{key: 'beginn', label: 'Beginn', span: 1},
		{key: 'ende', label: 'Ende', span: 1}
	]

	async function doDeleteEintraege() {
		await props.deleteEintraege(selected.value);
		selected.value = [];
	}

	function export_pausenzeiten() {
		const arr = selected.value.map(r => StundenplanPausenzeit.transpilerToJSON(r));
		const blob = new Blob(['['+arr.toString()+']'], { type: "application/json", });
		const link = document.createElement("a");
		link.href = URL.createObjectURL(blob);
		link.download = "ExportPausenzeiten.json";
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	async function addBlock() {
		const list = props.stundenplanManager().pausenzeitGetDummyListe(props.stundenplanManager().zeitrasterGetWochentagMin(), props.stundenplanManager().zeitrasterGetWochentagMax());
		await props.addPausenzeiten(list);
	}

</script>
