<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="1">
					<svws-ui-text-input placeholder="Bezeichnung"
						class="contentFocusField"
						span="2"
						v-model="modelProxy.proxy.bezeichnung"
						:validation="() => modelProxy.getFehler('bezeichnung')"
						:readonly
						required :max-len="50"
						@change="modelProxy.patch" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="modelProxy.proxy.sortierung"
						:validation="() => modelProxy.getFehler('sortierung')"
						@change="modelProxy.patch"
						:min="0" :max="32000"
						:readonly
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="modelProxy.proxy.istSichtbar" :readonly>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { computed } from "vue";
	import { TeilleistungsartenModelProxy } from "~/components/schule/kataloge/teilleistungsarten/modelproxy/TeilleistungsartenModelProxy";
	import type { TeilleistungsartenDatenProps } from './TeilleistungsartenDatenProps';
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const props = defineProps<TeilleistungsartenDatenProps>();
	const benutzerState = useBenutzerState();

	const modelProxy = new TeilleistungsartenModelProxy(() => props.manager().daten(), () => props.manager().liste.list(), props.patch);

	const hatKompetenzUpdate = computed<boolean>(() => {
		return benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	});
	const readonly = computed(() => !hatKompetenzUpdate.value);
</script>
