<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Bezeichnung"
					v-model="model.proxy.bezeichnung"
					:validation="() => model.getFehler('bezeichnung')"
					skip-default-validation
					:max-len="255" required />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addLernplatfform" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import { Lernplattform } from "@core/core/data/schule/Lernplattform";
	import { computed, ref, watch } from "vue";
	import type { LernplattformenNeuProps } from "~/components/schule/kataloge/lernplattformen/LernplattformenNeuProps";
	import { LernplattformenModelProxy } from "~/components/schule/kataloge/lernplattformen/modelproxy/LernplattformenModelProxy";

	const props = defineProps<LernplattformenNeuProps>();
	const model = new LernplattformenModelProxy(() => new Lernplattform(), props.manager);
	const isLoading = ref<boolean>(false);
	const formIsValid = computed(() => model.getAlleFehler().isEmpty());

	async function addLernplatfform() {
		if (isLoading.value === true) {
			return;
		}

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, referenziertInAnderenTabellen, ...partialData } = model.proxy;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	watch(() => model.proxy, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
