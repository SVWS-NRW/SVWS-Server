<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper>
					<div class="flex gap-1"><svws-ui-checkbox type="toggle" v-model="data.aktiv" :disabled="(data.aktiv === false && !manager().istKonfliktfreiZuAktivenPlanungsabschnitten(data.gueltigVon, data.gueltigBis))" />Planungsabschnitt aktiv <span v-if="manager().validateGueltigVon(data.gueltigVon, data.gueltigBis, data.aktiv, true, true) && manager().validateGueltigBis(data.gueltigVon, data.gueltigBis, data.aktiv, true, true) && !manager().istKonfliktfreiZuAktivenPlanungsabschnitten(data.gueltigVon, data.gueltigBis, false)" class="text-ui-caution"><span class="icon icon-ui-caution i-ri-alert-line" /> Konflikt mit anderem Planungsabschnitt</span></div>
					<svws-ui-text-input class="contentFocusField" :disabled="!hatUpdateKompetenz" placeholder="Bezeichnung" :required="true" :max-len="150" :valid="UvPlanungsabschnitteListeManager.validateBezeichnung" v-model="data.beschreibung" type="text" />
					<svws-ui-select title="Datenübernahme aus Planungsabschnitt"
						:items="allePlanungsabschnitte"
						:item-text="planungsabschnittText"
						v-model="copyOf"
						ref="select" />
					<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
						<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Gültig ab" :valid="value => manager().validateGueltigVon(value, data.gueltigBis, data.aktiv, true, true)" v-model="data.gueltigVon" type="date" :fehlerart="data.aktiv ? ValidatorFehlerart.MUSS : ValidatorFehlerart.HINWEIS" />
						<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Gültig bis" :valid="value => manager().validateGueltigBis(data.gueltigVon, value, data.aktiv, true, true)" v-model="data.gueltigBis" type="date" :fehlerart="data.aktiv ? ValidatorFehlerart.MUSS : ValidatorFehlerart.HINWEIS" />
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
	import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { UvPlanungsabschnitteListeManager } from "@ui/ui/manager/unterrichtsverteilung/UvPlanungsabschnitteListeManager";
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

	type PartialExcept<T, K extends keyof T> = Partial<T> & Required<Pick<T, K>>;

	const copyOf = ref<Partial<UvPlanungsabschnitt>>({ id: -1 });
	const allePlanungsabschnitte = ref<Partial<UvPlanungsabschnitt>[]>([]);

	const data = ref<PartialExcept<UvPlanungsabschnitt, 'gueltigVon' | 'gueltigBis' | 'aktiv' | 'beschreibung'>>({ gueltigVon: "",	gueltigBis: "", beschreibung: "Neuer Planungsabschnitt", aktiv: false });

	const hatUpdateKompetenz = true;

	const wochenTypModell = ['keins', null, 'AB-Wochen', 'ABC-Wochen', 'ABCD-Wochen', 'weitere'];

	function planungsabschnittText(planungsabschnitt: Partial<UvPlanungsabschnitt>): string {
		if (planungsabschnitt.id === -1) {
			return "Keine Datenübernahme";
		}
		if (planungsabschnitt.gueltigVon === undefined) {
			return planungsabschnitt.beschreibung ?? 'Unbenannter Planungsabschnitt';
		}
		const status = (planungsabschnitt.aktiv === true) ? "aktiv, " : "";
		const gueltigBis = (planungsabschnitt.gueltigBis === undefined || planungsabschnitt.gueltigBis === null)
			? " ... "
			: DateUtils.gibDatumGermanFormat(planungsabschnitt.gueltigBis);
		return `${planungsabschnitt.beschreibung} (${status}${DateUtils.gibDatumGermanFormat(planungsabschnitt.gueltigVon)}–${gueltigBis})`;
	}

	onMounted(async () => {
		const lastAktiv = manager().getLastAktiv();
		const abschnitt = manager().getSchuljahresabschnittAuswahl();

		if (abschnitt === null) {
			throw new DeveloperNotificationException("SchuljahresabschnittAuswahl ist null");
		}

		watch(() => data.value, async () => {
			if (isLoading.value) {
				return;
			}

			props.checkpoint.active = true;
		}, { immediate: false, deep: true });

		let gueltigVon: string;
		if ((lastAktiv === null) || (lastAktiv.gueltigBis === null)) {
			gueltigVon = (abschnitt.abschnitt === 1) ? `${abschnitt.schuljahr}-08-01` : `${abschnitt.schuljahr + 1}-02-01`;
		} else {
			gueltigVon = DateUtils.gibDatumFolgetag(lastAktiv.gueltigBis);
		}

		data.value = {
			gueltigVon,
			gueltigBis: (abschnitt.abschnitt === 1) ? `${abschnitt.schuljahr + 1}-01-31` : `${abschnitt.schuljahr + 1}-07-31`,
			aktiv: false,
			beschreibung: "Neuer Planungsabschnitt",
		};

		allePlanungsabschnitte.value = [
			{ id: -1, beschreibung: "Keine Datenübernahme", gueltigVon: "", gueltigBis: "", aktiv: false },
			...Array.from(manager().alle()),
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
		const vBezeichnung = UvPlanungsabschnitteListeManager.validateBezeichnung(data.value.beschreibung);
		const vDateGueltigVon = DateUtils.isValidDate(data.value.gueltigVon);
		const vDateGueltigBis = DateUtils.isValidDate(data.value.gueltigBis);
		const vGueltigVon = manager().validateGueltigVon(data.value.gueltigVon, data.value.gueltigBis, data.value.aktiv, false, true);
		const vGueltigBis = manager().validateGueltigBis(data.value.gueltigVon, data.value.gueltigBis, data.value.aktiv, false, true);
		const vKonfliktfrei = manager().istKonfliktfreiZuAktivenPlanungsabschnitten(data.value.gueltigVon, data.value.gueltigBis, true);
		return vBezeichnung
			&& (vDateGueltigVon
				&& vDateGueltigBis
				&& vGueltigVon
				&& vGueltigBis
				&& (data.value.aktiv === false || vKonfliktfrei));
	});

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	async function add() {
		if (isLoading.value === true) {
			return;
		}

		isLoading.value = true;
		props.checkpoint.active = false;
		const neu = await props.addAsCopy(data.value, copyOf.value.id === -1 ? undefined : copyOf.value.id);
		await props.gotoDefaultView(neu.id);
		isLoading.value = false;
	}

</script>
