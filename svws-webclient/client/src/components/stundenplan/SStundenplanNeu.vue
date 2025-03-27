<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper>
					<svws-ui-text-input class="contentFocusField" :disabled="!hatUpdateKompetenz" placeholder="Bezeichnung" :required="true" :max-len="150" :valid="validateBezeichnung" v-model="data.bezeichnungStundenplan" type="text" />
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-select title="Wochentypmodell" :items="[0,2,3,4,5]" :item-text="i=> wochenTypModell[i] || ''" v-model="data.wochenTypModell" ref="select" />
						<svws-ui-input-number v-if="data.wochenTypModell! > 4" placeholder="Wochentypmodell" :model-value="data.wochenTypModell! < 5 ? 5 : data.wochenTypModell!" @change="modell => { if (modell !== null) data.wochenTypModell = modell }" :min="5" :max="100" />
					</svws-ui-input-wrapper>
					<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Gültig ab" :valid="validateGueltigAb" v-model="data.gueltigAb" type="date" />
					<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Gültig bis" :valid="validateGueltigBis" v-model="data.gueltigBis" type="date" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>

			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel" :disabled="isLoading">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addStundenplan" :disabled="!isValid || isLoading">
					Speichern <svws-ui-spinner :spinning="isLoading" />
				</svws-ui-button>
			</div>
		</div>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { StundenplanListeManager, DateUtils, type Stundenplan, DeveloperNotificationException } from "@core";
	import { ref, onMounted, watch } from "vue";
	import type { StundenplanNeuProps } from "~/components/stundenplan/SStundenplanNeuProps";

	const props = defineProps<StundenplanNeuProps>();

	const isLoading = ref<boolean>(false);
	const isValid = ref<boolean>(false);
	const data = ref<Partial<Stundenplan>>({});

	const hatUpdateKompetenz = true;

	const wochenTypModell = ['keins', null, 'AB-Wochen', 'ABC-Wochen', 'ABCD-Wochen', 'weitere'];

	onMounted(() => {
		const lastValidStundenplan = props.manager().getLastValidStundenplan();
		const abschnitt = props.manager().getSchuljahresabschnittAuswahl();

		if (abschnitt === null)
			throw new DeveloperNotificationException("SchuljahresabschnittAuswahl ist null");

		data.value = {
			gueltigAb: lastValidStundenplan === null ? ((abschnitt.abschnitt === 1) ? `${abschnitt.schuljahr}-08-01` : `${abschnitt.schuljahr + 1}-02-01`) : DateUtils.gibDatumFolgetag(lastValidStundenplan.gueltigBis),
			gueltigBis: (abschnitt.abschnitt === 1) ? `${abschnitt.schuljahr + 1}-01-31` : `${abschnitt.schuljahr + 1}-07-31`,
			bezeichnungStundenplan: "Neuer Stundenplan",
			wochenTypModell: lastValidStundenplan === null ? 0 : lastValidStundenplan.wochenTypModell,
		}

		watch(() => data.value, async () => {
			if (isLoading.value)
				return;

			props.checkpoint.active = true;
			validateAll();
		}, { immediate: false, deep: true });
	})

	const validateBezeichnung = (bezeichnung: string | null ): boolean => StundenplanListeManager.validateBezeichnung(bezeichnung);
	const validateGueltigAb = (gueltigAb: string | null ): boolean => props.manager().validateGueltigAb(gueltigAb, data.value.gueltigBis ?? null);
	const validateGueltigBis = (gueltigBis: string | null ): boolean => props.manager().validateGueltigBis(data.value.gueltigAb ?? null, gueltigBis);

	const validateAll = () => isValid.value = (data.value.gueltigAb !== undefined) && validateGueltigAb(data.value.gueltigAb) && (data.value.gueltigBis !== undefined) && validateGueltigBis(data.value.gueltigBis) && (data.value.bezeichnungStundenplan !== undefined) && validateBezeichnung(data.value.bezeichnungStundenplan);

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	async function addStundenplan() {
		if (isLoading.value === true)
			return;

		isLoading.value = true;
		props.checkpoint.active = false;
		await props.add(data.value);
		isLoading.value = false;
	}

</script>
