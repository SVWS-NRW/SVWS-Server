<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Ortsteil"
						v-model="model.proxy.ortsteil"
						:validation="() => model.getFehler('ortsteil')"
						@change="model.patch"
						:max-len="30" required :readonly />
					<ui-select label="Ort"
						v-model="model.ort.value"
						:validation="() => model.getFehler('idOrt')"
						:manager="ortSelectManager"
						required :removable="false" :readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@change="model.patch"
						:min="0" :max="32000"
						:readonly
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :readonly>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import type { OrtsteileDatenProps } from "~/components/schule/kataloge/ortsteile/daten/OrtsteileDatenProps";
	import { computed } from "vue";
	import { OrtsteilModelProxy } from "~/components/schule/kataloge/ortsteile/modelproxy/OrtsteilModelProxy";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	const props = defineProps<OrtsteileDatenProps>();
	const benutzerState = useBenutzerState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const model = new OrtsteilModelProxy(() => props.manager().auswahl(), props.manager, props.patch);

	const ortSelectManager = new SelectManager({
		options: model.filteredOrte,
		optionDisplayText: v => v.plz + ' ' + v.ortsname,
		selectionDisplayText: v => v.plz + ' ' + v.ortsname,
	});

</script>
