<template>
	<svws-ui-modal :show @update:show="emit('closeModal')">
		<template #modalTitle>{{ modalTitle }}</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2" class="text-left">
				<ui-select label="Merkmal"
					v-model="data.proxy.adrMerkmal"
					:manager="adrMerkmaleSelectManager"
					:validation="() => data.getFehler('adrMerkmal')"
					:readonly="readonly || isPatch"
					:removable="false"
					required />
				<svws-ui-text-input placeholder="Straße"
					v-model="data.proxy.strassenname"
					:validation="() => data.getFehler('strassenname')"
					:max-len="55" :readonly />
				<svws-ui-text-input placeholder="Hausnummer"
					v-model="data.proxy.hausNr"
					:validation="() => data.getFehler('hausNr')"
					:max-len="10" :readonly />
				<svws-ui-text-input placeholder="Zusatz"
					v-model="data.proxy.hausNrZusatz"
					:validation="() => data.getFehler('hausNrZusatz')"
					:max-len="30" :readonly />
				<svws-ui-text-input placeholder="PLZ"
					v-model="data.proxy.plz"
					:validation="() => data.getFehler('plz')"
					:max-len="10" :readonly />
				<svws-ui-text-input placeholder="Ort"
					v-model="data.proxy.ort"
					:validation="() => data.getFehler('ort')"
					:max-len="50" :readonly />
				<svws-ui-text-input placeholder="Kürzel"
					v-model="data.proxy.kuerzel"
					:validation="() => data.getFehler('kuerzel')"
					:max-len="30" :readonly />
				<svws-ui-text-input placeholder="Bemerkung" span="full"
					v-model="data.proxy.bemerkung"
					:validation="() => data.getFehler('bemerkung')"
					:max-len="50" :readonly />
				<svws-ui-spacing />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="emit('closeModal')">Abbrechen</svws-ui-button>
			<svws-ui-button @click="emit('save')" :disabled="saveDisabled">Speichern</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import type { Teilstandort } from "@core/core/data/schule/Teilstandort";
	import type { List } from "@core/java/util/List";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	import type { TeilstandortModelProxy } from "~/components/schule/stammdaten/adressdaten/modelproxy/TeilstandortModelProxy";

	/** Die Adressmerkmale, die für einen Teilstandort zur Auswahl stehen */
	const ADR_MERKMALE = ["A", "B", "C", "D", "E", "F", "G", "H"];

	const props = defineProps<{
		data: TeilstandortModelProxy;
		readonly: boolean;
		show: boolean;
		isPatch: boolean;
		getListTeilstandorte: () => List<Teilstandort>;
	}>();

	const emit = defineEmits<{
		'closeModal': [];
		'save': [];
	}>();

	const modalTitle = computed<string>(() => props.isPatch ? "Teilstandort bearbeiten" : "Teilstandort hinzufügen");

	const saveDisabled = computed<boolean>(() => props.data.hatBlockierendeFehler());

	// Bereits vergebene Merkmale stehen nicht zur Auswahl, außer dem Merkmal des Teilstandorts, der gerade bearbeitet wird
	const availableAdrMerkmale = computed<string[]>(() => {
		const usedAdrMerkmale = new Set<string | null>();
		for (const teilstandort of props.getListTeilstandorte()) {
			usedAdrMerkmale.add(teilstandort.adrMerkmal);
		}
		return ADR_MERKMALE.filter(merkmal => !usedAdrMerkmale.has(merkmal) || (merkmal === props.data.proxy.adrMerkmal));
	});

	const adrMerkmaleSelectManager = new SelectManager({
		options: availableAdrMerkmale,
		optionDisplayText: adrMerkmal => adrMerkmal,
		selectionDisplayText: adrMerkmal => adrMerkmal,
	});

</script>
