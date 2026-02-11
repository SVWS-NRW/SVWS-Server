<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper>
					<div class="flex gap-1"><svws-ui-checkbox type="toggle" v-model="data.aktiv" :disabled="(data.aktiv === false && !manager().istKonfliktfreiZuAktivenStundenplaenen(data.gueltigAb, data.gueltigBis))" />Stundenplan aktiv <span v-if="manager().validateGueltigAb(data.gueltigAb, data.gueltigBis, data.aktiv, true, true) && manager().validateGueltigBis(data.gueltigAb, data.gueltigBis, data.aktiv, true, true) && !manager().istKonfliktfreiZuAktivenStundenplaenen(data.gueltigAb, data.gueltigBis, false)" class="text-ui-caution"><span class="icon icon-ui-caution i-ri-alert-line" /> Konflikt mit anderem Stundenplan</span></div>
					<svws-ui-text-input class="contentFocusField" :disabled="!hatUpdateKompetenz" placeholder="Bezeichnung" :required="true" :max-len="150" :valid="StundenplanListeManager.validateBezeichnung" v-model="data.bezeichnungStundenplan" type="text" />
					<svws-ui-select title="Datenübernahme aus Stundenplan"
						:items="alleStundenplaene"
						:item-text="i => i.id === manager().getStundenplanVorlage().id ? 'Keine Datenübernahme' : (i.bezeichnung + ' (' + (i.aktiv ? 'aktiv, ' : '') + DateUtils.gibDatumGermanFormat(i.gueltigAb) + '–' + DateUtils.gibDatumGermanFormat(i.gueltigBis) + ')')"
						v-model="copyOf"
						ref="select" />
					<div v-if="copyOf.id !== manager().getStundenplanVorlage().id" class="ml-2 mb-1 flex gap-1"><span class="icon i-ri-information-line" /> Das Wochentypmodell wird aus dem gewählten Stundenplan übernommen, kann aber nachträglich geändert werden.</div>
					<svws-ui-input-wrapper :grid="2" v-else>
						<svws-ui-select title="Wochentypmodell" :items="[0,2,3,4,5]" :item-text="i=> wochenTypModell[i] || ''" v-model="data.wochenTypModell" ref="select" :disabled="copyOf.id !== manager().getStundenplanVorlage().id" />
						<svws-ui-input-number v-if="data.wochenTypModell! > 4" placeholder="Wochentypmodell" :model-value="data.wochenTypModell! < 5 ? 5 : data.wochenTypModell!" @change="modell => { if (modell !== null) data.wochenTypModell = modell }" :min="5" :max="100" :disabled="copyOf.id !== manager().getStundenplanVorlage().id" />
					</svws-ui-input-wrapper>
					<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Gültig ab" :valid="value => manager().validateGueltigAb(value, data.gueltigBis, data.aktiv, true, true)" v-model="data.gueltigAb" type="date" :fehlerart="data.aktiv ? ValidatorFehlerart.MUSS : ValidatorFehlerart.HINWEIS" />
					<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Gültig bis" :valid="value => manager().validateGueltigBis(data.gueltigAb, value, data.aktiv, true, true)" v-model="data.gueltigBis" type="date" :fehlerart="data.aktiv ? ValidatorFehlerart.MUSS : ValidatorFehlerart.HINWEIS" />
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

	import type { StundenplanListeEintrag } from "@core";
	import { DateUtils, type Stundenplan, DeveloperNotificationException, ValidatorFehlerart } from "@core";
	import { StundenplanListeManager } from "@ui";

	import { ref, onMounted, watch, computed } from "vue";
	import type { StundenplanNeuProps } from "~/components/stundenplan/SStundenplanNeuProps";

	const props = defineProps<StundenplanNeuProps>();

	const isLoading = ref<boolean>(false);

	type PartialExcept<T, K extends keyof T> = Partial<T> & Required<Pick<T, K>>;

	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const copyOf = ref<StundenplanListeEintrag>(props.manager().getStundenplanVorlage());
	const alleStundenplaene = ref<StundenplanListeEintrag[]>([]);

	const data = ref<PartialExcept<Stundenplan, 'gueltigAb' | 'gueltigBis' | 'wochenTypModell' | 'aktiv' | 'bezeichnungStundenplan'>>({ gueltigAb: "",	gueltigBis: "", bezeichnungStundenplan: "Neuer Stundenplan", wochenTypModell: 0, aktiv: false });

	const hatUpdateKompetenz = true;

	const wochenTypModell = ['keins', null, 'AB-Wochen', 'ABC-Wochen', 'ABCD-Wochen', 'weitere'];

	onMounted(async () => {
		const lastValidStundenplan = props.manager().getLastAktivStundenplan();
		const abschnitt = props.manager().getSchuljahresabschnittAuswahl();

		if (abschnitt === null) {
			throw new DeveloperNotificationException("SchuljahresabschnittAuswahl ist null");
		}

		watch(() => data.value, async () => {
			if (isLoading.value) {
				return;
			}

			props.checkpoint.active = true;
		}, { immediate: false, deep: true });

		data.value = {
			gueltigAb: lastValidStundenplan === null ? ((abschnitt.abschnitt === 1) ? `${abschnitt.schuljahr}-08-01` : `${abschnitt.schuljahr + 1}-02-01`) : DateUtils.gibDatumFolgetag(lastValidStundenplan.gueltigBis),
			gueltigBis: (abschnitt.abschnitt === 1) ? `${abschnitt.schuljahr + 1}-01-31` : `${abschnitt.schuljahr + 1}-07-31`,
			wochenTypModell: lastValidStundenplan === null ? 0 : lastValidStundenplan.wochenTypModell,
			aktiv: false,
			bezeichnungStundenplan: "Neuer Stundenplan",
		};

		alleStundenplaene.value = [
			props.manager().getStundenplanVorlage(),
			...Array.from(await props.getStundenplanListeEintragVorgaengerabschnitt()),
			...Array.from(props.manager().alle()),
		].sort((a, b) => {
			if (a.id === -1) {
				return -1;
			}
			if (b.id === -1) {
				return 1;
			}
			if (a.gueltigAb !== b.gueltigAb) {
				return a.gueltigAb > b.gueltigAb ? -1 : 1;
			}
			if (a.aktiv && !b.aktiv) {
				return -1;
			}
			if (!a.aktiv && b.aktiv) {
				return 1;
			}
			return 0;
		});
	});

	const validateAll = computed(() => {
		const vBezeichnung = StundenplanListeManager.validateBezeichnung(data.value.bezeichnungStundenplan);
		const vDateGueltigAb = DateUtils.isValidDate(data.value.gueltigAb);
		const vDateGueltigBis = DateUtils.isValidDate(data.value.gueltigBis);
		const vGueltigAb = props.manager().validateGueltigAb(data.value.gueltigAb, data.value.gueltigBis, data.value.aktiv, false, true);
		const vGueltigBis = props.manager().validateGueltigBis(data.value.gueltigAb, data.value.gueltigBis, data.value.aktiv, false, true);
		const vKonfliktfrei = props.manager().istKonfliktfreiZuAktivenStundenplaenen(data.value.gueltigAb, data.value.gueltigBis, true);
		return vBezeichnung
			&& (vDateGueltigAb
				&& vDateGueltigBis
				&& vGueltigAb
				&& vGueltigBis
				&& (data.value.aktiv === false || vKonfliktfrei));
	});

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	async function addStundenplan() {
		if (isLoading.value === true) {
			return;
		}

		isLoading.value = true;
		props.checkpoint.active = false;
		await props.addAsCopy(data.value, copyOf.value.id === props.manager().getStundenplanVorlage().id ? undefined : copyOf.value.id);
		isLoading.value = false;
	}

</script>
