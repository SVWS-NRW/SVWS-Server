<template>
	<div v-if="visible">
		<svws-ui-table :v-model="selected_blockungauswahl" :columns="[{ key: 'name', label: 'Blockung' }]" :data="rows_blockungswahl" class="mt-10">
			<template #body>
				<template v-for="row in rows_blockungswahl" :key="row.hashCode()">
					<tr :class="{'vt-clicked': row === selected_blockungauswahl}" @click="select_blockungauswahl(row)">
						<td v-if=" row === selected_blockungauswahl ">
							<div class="flex">
								<span v-if="(!edit_blockungsname && row === selected_blockungauswahl)" class="text-input--inline" @click.stop="edit_blockungsname = true">
									{{ row.name }}
								</span>
								<svws-ui-text-input v-else :model-value="row.name.toString()" style="width: 10rem" headless focus
									@keyup.enter="edit_blockungsname=false" @keyup.escape="edit_blockungsname=false" @update:model-value="patch_blockung" />
							</div>
							<svws-ui-icon v-if="row.istAktiv"> <i-ri-pushpin-fill /> </svws-ui-icon>
							<div v-if="allow_add_blockung(props.halbjahr.value)" class="flex gap-1">
								<svws-ui-button size="small" type="secondary" @click.stop="create_blockungsergebnisse" title="Ergebnisse berechnen" :disabled="pending">Berechnen</svws-ui-button>
								<svws-ui-button size="small" type="error" @click.stop="toggle_remove_blockung_modal" title="Blockung löschen" :disabled="pending">
									<svws-ui-icon><i-ri-delete-bin-2-line /></svws-ui-icon>
								</svws-ui-button>
							</div>
						</td>
						<td v-else>
							<div class="flex justify-between w-full">
								<span>{{ row.name }}</span>
								<svws-ui-icon v-if="row.istAktiv"> <i-ri-pushpin-fill /> </svws-ui-icon>
							</div>
						</td>
					</tr>
					<auswahl-blockung-api-status :blockung="row" />
				</template>
			</template>
		</svws-ui-table>
		<router-view name="gost_kursplanung_ergebnis_auswahl" />
		<svws-ui-modal ref="modal_remove_blockung" size="small">
			<template #modalTitle>Blockung löschen</template>
			<template #modalDescription>
				<div class="flex gap-1 mb-2">
					Soll die Blockung mit allen Ergebnissen gelöscht werden?
				</div>
				<div class="flex gap-1">
					<svws-ui-button @click="toggle_remove_blockung_modal()">Abbrechen</svws-ui-button>
					<svws-ui-button @click="remove_blockung">Ja</svws-ui-button>
				</div>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import { GostBlockungListeneintrag, GostHalbjahr, GostJahrgang, List } from '@svws-nrw/svws-core-ts';
	import { computed, ComputedRef, ref, Ref, ShallowRef, WritableComputedRef } from 'vue';
	import { DataGostJahrgang } from '~/apps/gost/DataGostJahrgang';
	import { GOST_CREATE_BLOCKUNG_SYMBOL } from "~/apps/core/LoadingSymbols";
	import { injectMainApp, Main } from '~/apps/Main';
	import { DataSchuleStammdaten } from '~/apps/schule/DataSchuleStammdaten';
	import { DataGostFaecher } from '~/apps/gost/DataGostFaecher';
	import { App } from '~/apps/BaseApp';
	import { DataGostKursblockung } from '~/apps/gost/DataGostKursblockung';
	import { ListKursblockungen } from '~/apps/gost/ListKursblockungen';
	import { routeGostKursplanungHalbjahr } from '~/router/apps/gost/kursplanung/RouteGostKursplanungHalbjahr';
	import { useRouter } from 'vue-router';
	import { routeGostKursplanung } from '~/router/apps/gost/RouteGostKursplanung';

	const props = defineProps<{
		item: ShallowRef<GostJahrgang | undefined>;
		schule: DataSchuleStammdaten;
		jahrgangsdaten: DataGostJahrgang;
		dataFaecher: DataGostFaecher;
		halbjahr: ShallowRef<GostHalbjahr>;
		listBlockungen: ListKursblockungen;
		blockung: DataGostKursblockung;
	}>();

	const router = useRouter();

	const main: Main = injectMainApp();

	const edit_blockungsname: Ref<boolean> = ref(false);

	const rows_blockungswahl: ComputedRef<GostBlockungListeneintrag[]> = computed(() => props.listBlockungen.liste);

	const selected_blockungauswahl: WritableComputedRef<GostBlockungListeneintrag | undefined> = routeGostKursplanungHalbjahr.getSelector(
		(value) => edit_blockungsname.value = false
	);

	const pending: ComputedRef<boolean> = computed(()=> props.blockung.pending);

	const allow_add_blockung = (row: GostHalbjahr): boolean => {
		const curr_hj = (row.id === props.halbjahr.value.id);
		if (!curr_hj || props.jahrgangsdaten.daten === undefined)
			return false;
		return props.jahrgangsdaten.daten.istBlockungFestgelegt[row.id] ? false : true
	}

	function select_blockungauswahl(blockung: GostBlockungListeneintrag) {
		if (!pending.value)
			selected_blockungauswahl.value = blockung;
	}

	const create_blockungsergebnisse = () => {
		const halbjahresHashCode: number = props.listBlockungen.ausgewaehlt?.hashCode() ? props.listBlockungen.ausgewaehlt.hashCode() : -1;
		const id = props.listBlockungen.ausgewaehlt?.id;
		if (!id)
			return;
		const apiCall = do_create_blockungsergebnisse(id, halbjahresHashCode);
		main.config.apiLoadingStatus.addStatusByPromise(apiCall, {message: 'Blockung wird berechnet...', caller: 'Kursplanung (Gost)', categories: [GOST_CREATE_BLOCKUNG_SYMBOL]});
	};

	async function do_create_blockungsergebnisse(id: number, hjId: number): Promise<List<Number> | void> {
		props.listBlockungen.addIdToApiStatus(hjId);
		props.listBlockungen.setApiStatusIdle(hjId);
		try {
			const res = await App.api.rechneGostBlockung(App.schema, id, 5000)
			props.listBlockungen.removeApiStatusId(hjId)
			return res
		} catch (e) {
			props.listBlockungen.setApiStatusError(hjId);
		}
	}

	function patch_blockung(value: string | number) {
		props.blockung.patch({name: value.toString()})
			.then(res => {
				if (res && selected_blockungauswahl.value)
					selected_blockungauswahl.value.name = value.toString();
			})
			.catch(error => { throw error });
	}

	async function remove_blockung() {
		modal_remove_blockung.value.closeModal()
		if (!selected_blockungauswahl.value)
			return;
		await App.api.deleteGostBlockung(App.schema, selected_blockungauswahl.value?.id);
		const abiturjahr = props.jahrgangsdaten.daten?.abiturjahr?.valueOf();
		if (!abiturjahr)
			return;
		await router.push({ name: routeGostKursplanung.name, params: { abiturjahr: abiturjahr, halbjahr: props.halbjahr.value?.id }});
	}

	const modal_remove_blockung: Ref<any> = ref(null);
	function toggle_remove_blockung_modal() {
		modal_remove_blockung.value.isOpen ? modal_remove_blockung.value.closeModal() : modal_remove_blockung.value.openModal();
	}

	const visible: ComputedRef<boolean> = computed(() => {
		return rows_blockungswahl.value.length > 0;
	});

</script>

<style>
	.loading-disclaimer {
		background-color: rgba(var(--svws-ui-color-dark-20), var(--tw-border-opacity));
		--tw-bg-opacity: 1;
		--tw-border-opacity: 1;
		border-width: 1px;
		padding: .25rem .75rem;
		line-height: 1.125;
	}

	.loading-spinner-dimensions {
		height: 1rem;
		width: 1rem;
	}

	.loading-display {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
	}

	.loading-rotation {
		display: block;
		position: relative;
		-webkit-animation: spin 6s steps(11, end) infinite;
		-moz-animation: spin 6s steps(11, end) infinite;
		animation: spin 6s steps(11, end) infinite;
	}

	.api-error-text {
		color: rgb(var(--svws-ui-color-error));
		font-weight: 700;
	}

	@-webkit-keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}

		100% {
			-webkit-transform: rotate(360deg);
		}
	}

	@-moz-keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}

		100% {
			-webkit-transform: rotate(360deg);
		}
	}

	@keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}

		100% {
			-webkit-transform: rotate(360deg);
		}
	}
</style>

<style lang="postcss" scoped>
	.cell--bewertung span {
		@apply inline-block text-center text-black rounded font-normal;
		min-width: 5ex;
		padding: 0.05em 0.2em;
	}

	.vt-clicked .cell--bewertung span {
		filter: brightness(0.8) saturate(200%);
	}
</style>
