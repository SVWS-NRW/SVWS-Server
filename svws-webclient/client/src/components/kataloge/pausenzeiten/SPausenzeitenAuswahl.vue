<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToKataloge">Kataloge</a>
				<span title="Pausenzeiten">Pausenzeiten</span>
			</nav>
		</template>
		<template #abschnitt>
			<span class="text-base font-bold opacity-50 select-none">{{ aktAbschnitt.schuljahr + "." + aktAbschnitt.abschnitt }}</span>
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items :columns selectable v-model="selected">
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
						<svws-ui-button @click="doDeleteEintraege()" type="trash" :disabled="selected.length === 0" />
						<svws-ui-button type="transparent" title="Pausenzeiten exportieren" @click="export_pausenzeiten" :disabled="selected.length === 0"><span class="icon-sm i-ri-upload-2-line" /></svws-ui-button>
						<s-pausenzeit-import-modal v-slot="{ openModal }" :set-katalog-pausenzeiten-import-j-s-o-n>
							<svws-ui-button type="icon" @click="openModal()">
								<span class="icon-sm i-ri-download-2-line" />
							</svws-ui-button>
						</s-pausenzeit-import-modal>
						<s-pausenzeit-neu-modal v-slot="{ openModal }" :add-pausenzeit="addEintrag">
							<svws-ui-button type="icon" @click="openModal()">
								<span class="icon i-ri-add-line" />
							</svws-ui-button>
						</s-pausenzeit-neu-modal>
					</template>
				</svws-ui-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { PausenzeitenAuswahlProps } from "./SPausenzeitenAuswahlProps";
	import { StundenplanPausenzeit } from "@core";
	import { Wochentag , DateUtils } from "@core";
	import { computed, ref } from "vue";

	const props = defineProps<PausenzeitenAuswahlProps>();
	const selected = ref<StundenplanPausenzeit[]>([]);

	const columns = [
		{key: 'wochentag', label: 'Wochentag', span: 2},
		{key: 'beginn', label: 'Beginn', span: 1},
		{key: 'ende', label: 'Ende', span: 1}
	]

	const items = computed(() => <StundenplanPausenzeit[]>props.stundenplanManager().pausenzeitGetMengeAsList().toArray());

	async function doDeleteEintraege() {
		await props.deleteEintraege(selected.value);
		selected.value = [];
	}

	function export_pausenzeiten() {
		const arr = selected.value.map(r => StundenplanPausenzeit.transpilerToJSON(r));
		const blob = new Blob(['['+arr.toString()+']'], {
			type: "application/json",
		});
		const link = document.createElement("a");
		link.href = URL.createObjectURL(blob);
		link.download = "ExportPausenzeiten.json";
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

</script>
