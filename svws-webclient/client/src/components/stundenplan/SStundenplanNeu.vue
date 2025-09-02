<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper>
					<div class="flex gap-1"><svws-ui-checkbox type="toggle" v-model="data.aktiv" :disabled="(data.aktiv === false && !manager().istKonfliktfreiZuAktivenStundenplaenen(data.gueltigAb, data.gueltigBis))" />Stundenplan aktiv</div>
					<svws-ui-text-input class="contentFocusField" :disabled="!hatUpdateKompetenz" placeholder="Bezeichnung" :required="true" :max-len="150" :valid="StundenplanListeManager.validateBezeichnung" v-model="data.bezeichnungStundenplan" type="text" />
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-select title="Wochentypmodell" :items="[0,2,3,4,5]" :item-text="i=> wochenTypModell[i] || ''" v-model="data.wochenTypModell" ref="select" />
						<svws-ui-input-number v-if="data.wochenTypModell! > 4" placeholder="Wochentypmodell" :model-value="data.wochenTypModell! < 5 ? 5 : data.wochenTypModell!" @change="modell => { if (modell !== null) data.wochenTypModell = modell }" :min="5" :max="100" />
					</svws-ui-input-wrapper>
					<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Gültig ab" :valid="value => manager().validateGueltigAb(value, data.gueltigBis, data.aktiv, manager().validateGueltigBis(data.gueltigAb, value, data.aktiv))" v-model="data.gueltigAb" type="date" :fehlerart="data.aktiv ? ValidatorFehlerart.MUSS : ValidatorFehlerart.HINWEIS" />
					<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Gültig bis" :valid="value => manager().validateGueltigBis(data.gueltigAb, value, data.aktiv)" v-model="data.gueltigBis" type="date" :fehlerart="data.aktiv ? ValidatorFehlerart.MUSS : ValidatorFehlerart.HINWEIS" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>

			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel" :disabled="isLoading">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addStundenplan" :disabled="!validateAll || isLoading">
					Speichern <svws-ui-spinner :spinning="isLoading" />
				</svws-ui-button>
			</div>
		</div>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { DateUtils, type Stundenplan, DeveloperNotificationException, ValidatorFehlerart } from "@core";
	import { StundenplanListeManager } from "@ui";

	import { ref, onMounted, watch, computed } from "vue";
	import type { StundenplanNeuProps } from "~/components/stundenplan/SStundenplanNeuProps";

	const props = defineProps<StundenplanNeuProps>();

	const isLoading = ref<boolean>(false);

	type PartialExcept<T, K extends keyof T> = Partial<T> & Required<Pick<T, K>>;

	const data = ref<PartialExcept<Stundenplan, 'gueltigAb' | 'gueltigBis' | 'wochenTypModell' | 'aktiv' | 'bezeichnungStundenplan'>>({ gueltigAb: "",	gueltigBis: "", bezeichnungStundenplan: "Neuer Stundenplan", wochenTypModell: 0, aktiv: false });

	const hatUpdateKompetenz = true;

	const wochenTypModell = ['keins', null, 'AB-Wochen', 'ABC-Wochen', 'ABCD-Wochen', 'weitere'];

	onMounted(() => {
		const lastValidStundenplan = props.manager().getLastAktivStundenplan();
		const abschnitt = props.manager().getSchuljahresabschnittAuswahl();

		if (abschnitt === null)
			throw new DeveloperNotificationException("SchuljahresabschnittAuswahl ist null");

		watch(() => data.value, async () => {
			if (isLoading.value)
				return;

			props.checkpoint.active = true;
		}, { immediate: false, deep: true });

		data.value = {
			gueltigAb: lastValidStundenplan === null ? ((abschnitt.abschnitt === 1) ? `${abschnitt.schuljahr}-08-01` : `${abschnitt.schuljahr + 1}-02-01`) : DateUtils.gibDatumFolgetag(lastValidStundenplan.gueltigBis),
			gueltigBis: (abschnitt.abschnitt === 1) ? `${abschnitt.schuljahr + 1}-01-31` : `${abschnitt.schuljahr + 1}-07-31`,
			wochenTypModell: lastValidStundenplan === null ? 0 : lastValidStundenplan.wochenTypModell,
			aktiv: false,
			bezeichnungStundenplan: "Neuer Stundenplan",
		}

	})

	const validateAll = computed(() =>
		StundenplanListeManager.validateBezeichnung(data.value.bezeichnungStundenplan)
		&& (DateUtils.isValidDate(data.value.gueltigAb)
			&& DateUtils.isValidDate(data.value.gueltigBis)
			&& props.manager().validateGueltigAb(data.value.gueltigAb, data.value.gueltigBis, data.value.aktiv,props. manager().validateGueltigBis(data.value.gueltigAb, data.value.gueltigAb, data.value.aktiv))
			&& props.manager().validateGueltigBis(data.value.gueltigAb, data.value.gueltigBis, data.value.aktiv)
			&& (data.value.aktiv === false || props.manager().istKonfliktfreiZuAktivenStundenplaenen(data.value.gueltigAb, data.value.gueltigBis))));

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
