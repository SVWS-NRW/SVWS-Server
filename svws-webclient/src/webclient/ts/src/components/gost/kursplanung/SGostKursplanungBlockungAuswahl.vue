<template>
	<div v-if="visible">
		<svws-ui-table :model-value="auswahlBlockung" @update:model-value="setAuswahlBlockung" :columns="[{ key: 'name', label: 'Blockung' }]" :data="rows" class="mt-10">
			<template #body>
				<template v-for="row in mapBlockungen.values()" :key="row.hashCode()">
					<tr :class="{'vt-clicked': row === auswahlBlockung}" @click="select_blockungauswahl(row)">
						<td v-if=" row === auswahlBlockung ">
							<div class="flex">
								<span v-if="(!edit_blockungsname && row === auswahlBlockung)" class="text-input--inline" @click.stop="edit_blockungsname = true">
									{{ row.name }}
								</span>
								<svws-ui-text-input v-else :model-value="row.name" style="width: 10rem" headless focus
									@keyup.enter="edit_blockungsname=false" @keyup.escape="edit_blockungsname=false" @update:model-value="(value) => patch_blockung(String(value), row.id)" />
							</div>
							<svws-ui-icon v-if="row.istAktiv"> <i-ri-pushpin-fill /> </svws-ui-icon>
							<div v-if="allow_add_blockung(props.halbjahr)" class="flex gap-1">
								<svws-ui-button size="small" type="secondary" @click.stop="create_blockungsergebnisse" title="Ergebnisse berechnen" :disabled="apiStatus.pending">Berechnen</svws-ui-button>
								<svws-ui-button type="trash" class="cursor-pointer" @click.stop="toggle_remove_blockung_modal" title="Blockung löschen" :disabled="apiStatus.pending" />
							</div>
						</td>
						<td v-else>
							<div class="flex justify-between w-full">
								<span>{{ row.name }}</span>
								<svws-ui-icon v-if="row.istAktiv"> <i-ri-pushpin-fill /> </svws-ui-icon>
							</div>
						</td>
					</tr>
					<auswahl-blockung-api-status :blockung="row" :api-status="apiStatus" />
				</template>
			</template>
		</svws-ui-table>
		<s-gost-kursplanung-ergebnis-auswahl v-if="hatBlockung" :jahrgangsdaten="jahrgangsdaten" :halbjahr="halbjahr" :api-status="apiStatus"
			:get-datenmanager="getDatenmanager" :remove-ergebnis="removeErgebnis" :remove-ergebnisse="removeErgebnisse" :ergebnis-zu-neue-blockung="ergebnisZuNeueBlockung"
			:set-auswahl-ergebnis="setAuswahlErgebnis" :auswahl-ergebnis="auswahlErgebnis" />
		<svws-ui-modal ref="modal_remove_blockung" size="small">
			<template #modalTitle>Blockung löschen</template>
			<template #modalDescription>
				Soll die Blockung mit allen Ergebnissen gelöscht werden?
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="toggle_remove_blockung_modal()">Abbrechen</svws-ui-button>
				<svws-ui-button @click="remove_blockung">Ja</svws-ui-button>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import { GostBlockungListeneintrag, GostBlockungsdaten, GostBlockungsdatenManager, GostBlockungsergebnisListeneintrag, GostHalbjahr, GostJahrgangsdaten, List } from '@svws-nrw/svws-core-ts';
	import { computed, ComputedRef, ref, Ref } from 'vue';
	import { GOST_CREATE_BLOCKUNG_SYMBOL } from "~/apps/core/LoadingSymbols";
	import { routeLogin } from "~/router/RouteLogin";
	import { routeApp } from '~/router/RouteApp';
	import { ApiStatus } from '~/utils/ApiStatus';

	const props = defineProps<{
		patchBlockung: (data: Partial<GostBlockungsdaten>, idBlockung: number) => Promise<boolean>;
		removeBlockung: () => Promise<void>;
		setAuswahlBlockung: (auswahl: GostBlockungListeneintrag | undefined) => Promise<void>;
		auswahlBlockung: GostBlockungListeneintrag | undefined;
		mapBlockungen: Map<number, GostBlockungListeneintrag>;
		jahrgangsdaten: GostJahrgangsdaten | undefined;
		halbjahr: GostHalbjahr;
		apiStatus: ApiStatus;
		// ... zusätzlich für die Ergebnisauswahl
		getDatenmanager: () => GostBlockungsdatenManager;
		removeErgebnis: (idErgebnis: number) => Promise<void>;
		removeErgebnisse: (ergebnisse: GostBlockungsergebnisListeneintrag[]) => Promise<void>;
		ergebnisZuNeueBlockung: (idErgebnis: number) => Promise<void>;
		setAuswahlErgebnis: (value: GostBlockungsergebnisListeneintrag | undefined) => Promise<void>;
		hatBlockung: boolean;
		auswahlErgebnis: GostBlockungsergebnisListeneintrag | undefined;
	}>();

	const rows: ComputedRef<GostBlockungListeneintrag[]> = computed(() => {
		const result: GostBlockungListeneintrag[] = [];
		for (const bl of props.mapBlockungen.values())
			result.push(bl);
		return result;
	});

	const edit_blockungsname: Ref<boolean> = ref(false);

	const allow_add_blockung = (row: GostHalbjahr): boolean => {
		const curr_hj = (row.id === props.halbjahr.id);
		if (!curr_hj || props.jahrgangsdaten === undefined)
			return false;
		return props.jahrgangsdaten.istBlockungFestgelegt[row.id] ? false : true
	}

	async function select_blockungauswahl(blockung: GostBlockungListeneintrag) {
		if (!props.apiStatus.pending)
			await props.setAuswahlBlockung(blockung);
	}

	const create_blockungsergebnisse = () => {
		const halbjahresHashCode: number = props.auswahlBlockung?.hashCode() ? props.auswahlBlockung.hashCode() : -1;
		const id = props.auswahlBlockung?.id;
		if (!id)
			return;
		const apiCall = do_create_blockungsergebnisse(id, halbjahresHashCode);
		routeApp.data.apiLoadingStatus.addStatusByPromise(apiCall, {message: 'Blockung wird berechnet...', caller: 'Kursplanung (Gost)', categories: [GOST_CREATE_BLOCKUNG_SYMBOL]});
	};

	async function do_create_blockungsergebnisse(id: number, hjId: number): Promise<List<Number> | void> {
		props.apiStatus.start();
		try {
			const res = await routeLogin.data.api.rechneGostBlockung(routeLogin.data.schema, id, 5000)
			props.apiStatus.stop();
			return res;
		} catch (e) {
			props.apiStatus.stop(e instanceof Error ? e : undefined);
		}
	}

	async function patch_blockung(value: string, idBlockung : number) {
		const result = await props.patchBlockung({ name: value.toString() }, idBlockung);
		if (result && props.auswahlBlockung)
			props.auswahlBlockung.name = value.toString();
	}

	async function remove_blockung() {
		modal_remove_blockung.value.closeModal()
		await props.removeBlockung();
	}

	const modal_remove_blockung: Ref<any> = ref(null);
	function toggle_remove_blockung_modal() {
		modal_remove_blockung.value.isOpen ? modal_remove_blockung.value.closeModal() : modal_remove_blockung.value.openModal();
	}

	const visible: ComputedRef<boolean> = computed(() => {
		return props.mapBlockungen.size > 0;
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
