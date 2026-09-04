<template>
	<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="stundenplanManager().aufsichtsbereichGetMengeAsList()" :columns :selectable="!readonly" v-model="selected" class="max-w-128 min-w-96" scroll>
		<template v-if="!readonly" #actions>
			<svws-ui-button @click="doDeleteEintraege()" type="trash" :disabled="selected.length === 0" />
			<svws-ui-button type="transparent" title="Pausenzeiten exportieren" @click="exportAufsichtsbereiche" :disabled="selected.length === 0"><span class="icon-sm i-ri-upload-2-line" /></svws-ui-button>
			<s-aufsichtsbereich-import-modal v-slot="{ openModal }" :set-katalog-aufsichtsbereiche-import-j-s-o-n>
				<svws-ui-button type="icon" @click="openModal">
					<span class="icon-sm i-ri-download-2-line" />
				</svws-ui-button>
			</s-aufsichtsbereich-import-modal>
			<s-aufsichtsbereich-neu-modal v-slot="{ openModal }" :add-aufsichtsbereich="addEintrag">
				<svws-ui-button type="icon" @click="openModal()">
					<span class="icon i-ri-add-line" />
				</svws-ui-button>
			</s-aufsichtsbereich-neu-modal>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { AufsichtsbereicheAuswahlProps } from "./SAufsichtsbereicheAuswahlProps";
	import type { Aufsichtsbereich } from "@core/core/data/schule/Aufsichtsbereich";
	import { StundenplanAufsichtsbereich } from "@core/core/data/stundenplan/StundenplanAufsichtsbereich";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const props = defineProps<AufsichtsbereicheAuswahlProps>();
	const benutzerState = useBenutzerState();

	const readonly = computed<boolean>(() => !(benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN)));
	const selected = ref<Aufsichtsbereich[]>([]);

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc', span: 0.5 },
		{ key: "beschreibung", label: "Beschreibung", sortable: true },
	];

	async function doDeleteEintraege() {
		await props.deleteEintraege(selected.value);
		selected.value = [];
	}

	function exportAufsichtsbereiche() {
		const arr = selected.value.map(r => StundenplanAufsichtsbereich.transpilerToJSON(r));
		const blob = new Blob(['[' + arr.toString() + ']'], { type: "application/json" });
		const link = document.createElement("a");
		link.href = URL.createObjectURL(blob);
		link.download = "ExportAufsichtsbereiche.json";
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

</script>
