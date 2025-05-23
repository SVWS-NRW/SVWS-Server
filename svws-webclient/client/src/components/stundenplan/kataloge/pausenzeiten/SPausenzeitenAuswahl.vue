<template>
	<div class="flex flex-col gap-4">
		<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="stundenplanManager().pausenzeitGetMengeAsList()" :columns selectable v-model="selected" class="max-w-128 min-w-96" scroll>
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
		<ui-card icon="i-ri-add-line" title="Alle Pausenzeiten erstellen" :is-open="actionPausenzeiten" @update:is-open="(isOpen) => actionPausenzeiten = isOpen">
			<stundenplan-zeitraster-einstellungen :manager="stundenplanManager" :set-settings-defaults>
				<svws-ui-button type="secondary" @click="addBlock" title="Alle Pausenzeiten erstellen">
					<span class="icon i-ri-calendar-event-line" />
					<span class="icon i-ri-add-line -ml-1" />Alle Pausenzeiten erstellen
				</svws-ui-button>
			</stundenplan-zeitraster-einstellungen>
		</ui-card>
	</div>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { PausenzeitenAuswahlProps } from "./SPausenzeitenAuswahlProps";
	import { StundenplanPausenzeit } from "@core";
	import { Wochentag, DateUtils } from "@core";

	const props = defineProps<PausenzeitenAuswahlProps>();
	const selected = ref<StundenplanPausenzeit[]>([]);
	const actionPausenzeiten = ref<boolean>(false);

	const columns = [
		{key: 'wochentag', label: 'Wochentag', span: 2},
		{key: 'beginn', label: 'Beginn', span: 1},
		{key: 'ende', label: 'Ende', span: 1},
	]

	async function doDeleteEintraege() {
		await props.deleteEintraege(selected.value);
		selected.value = [];
	}

	function export_pausenzeiten() {
		const arr = selected.value.map(r => StundenplanPausenzeit.transpilerToJSON(r));
		const blob = new Blob(['['+arr.toString()+']'], { type: "application/json" });
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
