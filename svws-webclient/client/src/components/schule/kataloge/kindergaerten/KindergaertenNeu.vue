<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<!-- Allgemein -->
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="full"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						:max-len="100" required :disabled />
					<svws-ui-text-input placeholder="Bemerkung" span="full"
						v-model="model.proxy.bemerkung"
						:validation="() => model.getFehler('bemerkung')"
						:max-len="50" :disabled />
					<svws-ui-text-input placeholder="Telefon" type="tel"
						v-model="model.proxy.tel"
						:validation="() => model.getFehler('tel')"
						:max-len="20" :disabled />
					<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
						v-model="model.proxy.email"
						:validation="() => model.getFehler('email')"
						:max-len="40" :disabled />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Adresse -->
			<svws-ui-content-card title="Adresse">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Straße" span="full"
						v-model="model.adresse.value"
						:validation="() => model.getFehler('strassenname')"
						:max-len="55" :disabled />
					<svws-ui-text-input placeholder="PLZ"
						v-model="model.proxy.plz"
						:validation="() => model.getFehler('plz')"
						:max-len="10" :disabled />
					<svws-ui-text-input placeholder="Wohnort"
						v-model="model.proxy.ort"
						:validation="() => model.getFehler('ort')"
						:max-len="30" :disabled />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Sonstige -->
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number placeholder="Sortierung"
					v-model="model.proxy.sortierung"
					:validation="() => model.getFehler('sortierung')"
					:min="0" :max="32000" :removable="false" required :disabled />
				<svws-ui-spacing />
				<svws-ui-checkbox v-model="model.proxy.istSichtbar" :disabled>
					Sichtbar
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addKindergarten" :disabled="!hatKompetenzAdd || !formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import type { KindergaertenNeuProps } from "~/components/schule/kataloge/kindergaerten/KindergaertenNeuProps";
	import { KindergaertenModelProxy } from "./modelproxy/KindergaertenModelProxy";
	import { Kindergarten } from "@core/core/data/schule/Kindergarten";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const props = defineProps<KindergaertenNeuProps>();
	const benutzerState = useBenutzerState();

	const data = ref<Kindergarten>(Object.assign(new Kindergarten(), { istSichtbar: true, sortierung: 32000 }));
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed<boolean>(() => !hatKompetenzAdd.value);
	const isLoading = ref<boolean>(false);
	const model = new KindergaertenModelProxy(() => data.value, () => props.manager().liste.list());
	const formIsValid = computed(() => model.getAlleFehler().isEmpty());

	async function addKindergarten() {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, referenziertInAnderenTabellen, ...partialData } = model.proxy;
		await props.add(partialData);
		isLoading.value = false;
	}

	function cancel() {
		props.checkpoint.active = false;
		void props.goToDefaultView(null);
	}

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
