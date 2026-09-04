<template>
	<div class="flex gap-4 pt-8 px-6 lg:px-9 3xl:px-12 4xl:px-20">
		<SvwsUiTooltip color="light" :show-arrow="false">
			<template #content>
				Änderungen übernehmen
			</template>
			<svws-ui-button :disabled="!props.pendingStateManager().pendingStateExists()" @click="patchPendingStates" type="primary" size="big">
				Speichern
				<svws-ui-spinner :spinning="loading" />
			</svws-ui-button>
		</SvwsUiTooltip>
		<SvwsUiTooltip color="light" :show-arrow="false">
			<template #content>
				Änderungen zurücksetzen
			</template>
			<svws-ui-button :disabled="!props.pendingStateManager().pendingStateExists()" @click="pendingStateManager().resetPendingState()" type="danger"
				size="big">
				Zurücksetzen
			</svws-ui-button>
		</SvwsUiTooltip>
	</div>
	<div class="page page-grid-cards" />

	<svws-ui-checkpoint-modal :checkpoint :continue-routing="continueRoutingAfterCheckpointCustom" />
</template>
<script setup lang="ts">

	import type { LehrerIndividualdatenGruppenprozesseProps } from "~/components/lehrer/individualdaten/LehrerIndividualdatenGruppenprozesseProps";
	import { computed, ref } from "vue";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const props = defineProps<LehrerIndividualdatenGruppenprozesseProps>();
	const benutzerState = useBenutzerState();

	const loading = ref<boolean>(false);

	const hatKompetenzAnsehen = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.LEHRERDATEN_ANSEHEN));
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.LEHRERDATEN_AENDERN));

	async function patchPendingStates() {
		loading.value = true;
		await props.patchMultiple();
		loading.value = false;
	}

	async function continueRoutingAfterCheckpointCustom() {
		props.pendingStateManager().resetPendingState();
		await props.continueRoutingAfterCheckpoint();
	}


</script>
<style scoped>

</style>
