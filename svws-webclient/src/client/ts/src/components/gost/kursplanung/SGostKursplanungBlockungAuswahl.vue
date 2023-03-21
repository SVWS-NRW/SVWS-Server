<template>
	<template v-if="visible">
		<svws-ui-data-table clickable :clicked="auswahlBlockung" @update:clicked="select_blockungauswahl" :columns="[{ key: 'name', label: 'Blockung' }]" :items="rows" class="mt-10">
			<template #cell(name)="{ rowData: row }">
				<div v-if="row === auswahlBlockung" class="flex justify-between w-full">
					<div class="flex">
						<span v-if="(!edit_blockungsname)" class="text-input--inline" @click.stop="edit_blockungsname = true">
							{{ row.name }}
						</span>
						<svws-ui-text-input v-else :model-value="row.name" style="width: 10rem" headless focus
							@keyup.enter="edit_blockungsname=false" @keyup.escape="edit_blockungsname=false" @update:model-value="(value) => patch_blockung(String(value), (row as unknown as GostBlockungListeneintrag).id)" />
					</div>
					<svws-ui-icon v-if="row.istAktiv"> <i-ri-pushpin-fill /> </svws-ui-icon>
					<div v-if="allow_add_blockung(props.halbjahr)" class="flex gap-1">
						<svws-ui-button size="small" type="secondary" @click.stop="do_create_blockungsergebnisse" title="Ergebnisse berechnen" :disabled="apiStatus.pending">Berechnen</svws-ui-button>
						<s-gost-kursplanung-remove-blockung-modal :remove-blockung="removeBlockung" v-slot="{ openModal }">
							<svws-ui-button type="trash" class="cursor-pointer" @click.stop="openModal()" title="Blockung löschen" :disabled="apiStatus.pending" />
						</s-gost-kursplanung-remove-blockung-modal>
					</div>
				</div>
				<div v-else>
					<div class="flex justify-between w-full">
						<span>{{ row.name }}</span>
						<svws-ui-icon v-if="row.istAktiv"> <i-ri-pushpin-fill /> </svws-ui-icon>
					</div>
				</div>
			</template>
			<template #footer>
				<auswahl-blockung-api-status v-if="auswahlBlockung !== undefined && isPending(auswahlBlockung.id)" :blockung="auswahlBlockung" :api-status="apiStatus" />
			</template>
		</svws-ui-data-table>
		<s-gost-kursplanung-ergebnis-auswahl v-if="hatBlockung" :jahrgangsdaten="jahrgangsdaten" :halbjahr="halbjahr" :api-status="apiStatus"
			:get-datenmanager="getDatenmanager" :remove-ergebnisse="removeErgebnisse" :ergebnis-zu-neue-blockung="ergebnisZuNeueBlockung"
			:set-auswahl-ergebnis="setAuswahlErgebnis" :auswahl-ergebnis="auswahlErgebnis" />
	</template>
</template>

<script setup lang="ts">

	import { GostBlockungListeneintrag, GostBlockungsdaten, GostBlockungsdatenManager, GostBlockungsergebnisListeneintrag, GostHalbjahr, GostJahrgangsdaten, List } from "@svws-nrw/svws-core";
	import { DataTableItem } from "@ui";
	import { computed, ComputedRef, ref, Ref } from 'vue';
	import { ApiStatus } from '~/components/ApiStatus';

	const props = defineProps<{
		patchBlockung: (data: Partial<GostBlockungsdaten>, idBlockung: number) => Promise<boolean>;
		removeBlockung: () => Promise<void>;
		setAuswahlBlockung: (auswahl: GostBlockungListeneintrag | undefined) => Promise<void>;
		auswahlBlockung: GostBlockungListeneintrag | undefined;
		mapBlockungen: () => Map<number, GostBlockungListeneintrag>;
		jahrgangsdaten: GostJahrgangsdaten | undefined;
		halbjahr: GostHalbjahr;
		apiStatus: ApiStatus;
		// ... zusätzlich für die Ergebnisauswahl
		getDatenmanager: () => GostBlockungsdatenManager;
		rechneGostBlockung: () => Promise<List<number>>;
		removeErgebnisse: (ergebnisse: GostBlockungsergebnisListeneintrag[]) => Promise<void>;
		ergebnisZuNeueBlockung: (idErgebnis: number) => Promise<void>;
		setAuswahlErgebnis: (value: GostBlockungsergebnisListeneintrag | undefined) => Promise<void>;
		hatBlockung: boolean;
		auswahlErgebnis: GostBlockungsergebnisListeneintrag | undefined;
	}>();

	const rows: ComputedRef<GostBlockungListeneintrag[]> = computed(() => {
		const result: GostBlockungListeneintrag[] = [];
		for (const bl of props.mapBlockungen().values())
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

	async function select_blockungauswahl(blockung: DataTableItem | null) {
		if ((blockung === null) || props.apiStatus.pending)
			return;
		await props.setAuswahlBlockung(blockung as unknown as GostBlockungListeneintrag);
	}

	const isPending = (id: number) : boolean => ((props.apiStatus.data !== undefined) && (props.apiStatus.data.name === "gost.kursblockung.berechnen") && (props.apiStatus.data.id === id));

	async function do_create_blockungsergebnisse() {
		const id = props.auswahlBlockung?.id;
		if (id === undefined)
			return;
		await props.rechneGostBlockung();
	}

	async function patch_blockung(value: string, idBlockung : number) {
		const result = await props.patchBlockung({ name: value.toString() }, idBlockung);
		if (result && props.auswahlBlockung)
			props.auswahlBlockung.name = value.toString();
	}

	const visible: ComputedRef<boolean> = computed(() => {
		return props.mapBlockungen().size > 0;
	});
</script>

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
