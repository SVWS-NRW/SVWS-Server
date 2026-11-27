<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-notification v-if="fehler" type="error">{{ fehler }}</svws-ui-notification>
			<svws-ui-content-card title="Allgemein" :inert="isLoading">
				<svws-ui-input-wrapper>
					<div class="flex gap-1"><svws-ui-checkbox type="toggle" v-model="data.aktiv" :disabled="(data.aktiv === false && !manager().istKonfliktfreiZuAktivenPlanungsabschnitten(data.gueltigVon, data.gueltigBis))" />Planungsabschnitt aktiv <span v-if="manager().validateGueltigVon(data.gueltigVon, data.gueltigBis, data.aktiv, true, true) && manager().validateGueltigBis(data.gueltigVon, data.gueltigBis, data.aktiv, true, true) && !manager().istKonfliktfreiZuAktivenPlanungsabschnitten(data.gueltigVon, data.gueltigBis, false)" class="text-ui-caution"><span class="icon icon-ui-caution i-ri-alert-line" /> Konflikt mit anderem Planungsabschnitt</span></div>
					<svws-ui-text-input class="contentFocusField"
						:disabled="!hatUpdateKompetenz"
						placeholder="Bezeichnung"
						:required="true"
						:max-len="150"
						:valid="UvPlanungsabschnitteListeManager.validateBezeichnung"
						v-model="data.beschreibung"
						type="text" />
					<svws-ui-select title="Datenübernahme aus Planungsabschnitt"
						:items="allePlanungsabschnitte"
						:item-text="planungsabschnittText"
						v-model="copyOf"
						ref="select" />
					<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
						<svws-ui-text-input :disabled="!hatUpdateKompetenz"
							placeholder="Gültig ab"
							:valid="value => manager().validateGueltigVon(value, data.gueltigBis, data.aktiv, true, true)"
							v-model="data.gueltigVon"
							type="date"
							:fehlerart="data.aktiv ? ValidatorFehlerart.MUSS : ValidatorFehlerart.HINWEIS" />
						<svws-ui-text-input :disabled="!hatUpdateKompetenz"
							placeholder="Gültig bis"
							:valid="value => manager().validateGueltigBis(data.gueltigVon, value, data.aktiv, true, true)"
							v-model="data.gueltigBis"
							type="date"
							:fehlerart="data.aktiv ? ValidatorFehlerart.MUSS : ValidatorFehlerart.HINWEIS" />
					</div>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>

			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel" :disabled="isLoading">Abbrechen</svws-ui-button>
				<svws-ui-button @click="add" :disabled="!validateAll || isLoading">
					Speichern <svws-ui-spinner :spinning="isLoading" />
				</svws-ui-button>
			</div>
		</div>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, onMounted, ref, watch } from "vue";

	import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
	import type { UvPlanungsabschnitt } from "@core/core/data/uv/UvPlanungsabschnitt";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { UvPlanungsabschnitteListeManager, type UvPlanungsabschnittEntwurf } from "@ui/ui/manager/unterrichtsverteilung/UvPlanungsabschnitteListeManager";
	import type { Checkpoint } from "@ui/ui/modal/Checkpoint";

	import type { RoutingStatus } from "~/router/RoutingStatus";


	const props = defineProps<{
		gotoDefaultView: (eintragId?: number | null) => Promise<void>;
		manager: () => UvPlanungsabschnitteListeManager;
		addAsCopy: (partial: Partial<UvPlanungsabschnitt>, idFromUvPlanungsabschnitt?: number) => Promise<UvPlanungsabschnitt>;
		checkpoint: Checkpoint;
		continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	}>();
	const manager = () => props.manager();

	const isLoading = ref<boolean>(false);
	const fehler = ref('');

	const copyOf = ref<Partial<UvPlanungsabschnitt>>({ id: -1 });
	const allePlanungsabschnitte = ref<Partial<UvPlanungsabschnitt>[]>([]);

	const data = ref<UvPlanungsabschnittEntwurf>({ gueltigVon: "", gueltigBis: "", beschreibung: "Neuer Planungsabschnitt", aktiv: false });

	const hatUpdateKompetenz = true;

	onMounted(async () => {
		watch(() => data.value, async () => {
			if (isLoading.value) {
				return;
			}

			props.checkpoint.active = true;
		}, { immediate: false, deep: true });

		data.value = manager().neuerPlanungsabschnittEntwurf();
		allePlanungsabschnitte.value = planungsabschnittVorlagen();
	});

	const validateAll = computed(() => manager().planungsabschnittEntwurfGueltig(data.value));

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	async function add(): Promise<void> {
		if (isLoading.value || !validateAll.value) {
			return;
		}
		isLoading.value = true;
		fehler.value = '';
		try {
			const neu = await props.addAsCopy(data.value, copyOf.value.id === -1 ? undefined : copyOf.value.id);
			props.checkpoint.active = false;
			await props.gotoDefaultView(neu.id);
		} catch {
			props.checkpoint.active = true;
			fehler.value = 'Der Vorgang konnte nicht abgeschlossen werden. Bitte den Bestand der Planungsabschnitte prüfen.';
		} finally {
			isLoading.value = false;
		}
	}

	function planungsabschnittVorlagen(): Partial<UvPlanungsabschnitt>[] {
		return [
			{ id: -1, beschreibung: 'Keine Datenübernahme', gueltigVon: '', gueltigBis: '', aktiv: false },
			...manager().alle(),
		].sort((a, b) => {
			if (a.id === -1) {
				return -1;
			}
			if (b.id === -1) {
				return 1;
			}
			if (a.gueltigVon !== b.gueltigVon) {
				return a.gueltigVon > b.gueltigVon ? -1 : 1;
			}
			if (a.aktiv !== b.aktiv) {
				return a.aktiv === true ? -1 : 1;
			}
			return 0;
		});
	}

	function planungsabschnittText(planungsabschnitt: Partial<UvPlanungsabschnitt>): string {
		if (planungsabschnitt.id === -1) {
			return 'Keine Datenübernahme';
		}
		if (planungsabschnitt.gueltigVon === undefined) {
			return planungsabschnitt.beschreibung ?? 'Unbenannter Planungsabschnitt';
		}
		const status = planungsabschnitt.aktiv === true ? 'aktiv, ' : '';
		const gueltigBis = planungsabschnitt.gueltigBis === undefined || planungsabschnitt.gueltigBis === null
			? ' ... '
			: DateUtils.gibDatumGermanFormat(planungsabschnitt.gueltigBis);
		return `${planungsabschnitt.beschreibung} (${status}${DateUtils.gibDatumGermanFormat(planungsabschnitt.gueltigVon)}–${gueltigBis})`;
	}

</script>
